package buffer;

import org.jcsp.lang.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerNode implements CSProcess {

    private final List<One2OneChannelInt> inputChannels;
    private final List<One2OneChannelInt> outputChannels;
    private final Random generator = new Random(1000);
    private final int maxAmount;
    private int amount = 0;

    public ProducerNode(List<One2OneChannelInt> inputChannels, List<One2OneChannelInt> outputChannels, int maxAmount) {
        this.inputChannels = inputChannels;
        this.outputChannels = outputChannels;
        this.maxAmount = maxAmount;
    }

    public ProducerNode(int maxAmount){
        this.inputChannels = new ArrayList<>();
        this.outputChannels = new ArrayList<>();
        this.maxAmount = maxAmount;
    }

    public void addInputChannel(One2OneChannelInt channelInt) {
        inputChannels.add(channelInt);
    }

    public void addOutputChannel(One2OneChannelInt channelInt) {
        outputChannels.add(channelInt);
    }

    @Override
    public void run() {
        final Guard[] guards = inputChannels
                .stream()
                .map(One2OneChannelInt::in)
                .toList()
                .toArray(new Guard[0]);
        final Alternative alternative = new Alternative(guards);

        while (true) {

            int index = alternative.select();
            if (amount < maxAmount) {
                int value = inputChannels.get(index).in().read();
                amount += value;
            }

            while (amount > 0) {
                outputChannels.get(
                    generator.nextInt(outputChannels.size())
                ).out()
                .write(1);
                amount--;
            }

        }
    }

    @Override
    public String toString() {
        return "P{" + amount + "}";
    }
}
