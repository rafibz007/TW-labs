package buffer;

import org.jcsp.lang.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerNode implements CSProcess {

    private final List<One2OneChannelInt> inputChannels;
    private final List<One2OneChannelInt> outputChannels;
    private final List<One2OneChannelInt> outputRequestChannels;
    private final List<One2OneChannelInt> outputResponseChannels;
    private final Random generator = new Random();
    private final int maxAmount;
    private int amount = 0;

    public ProducerNode(
            List<One2OneChannelInt> inputChannels,
            List<One2OneChannelInt> outputChannels,
            List<One2OneChannelInt> outputRequestChannels,
            List<One2OneChannelInt> outputResponseChannels,
            int maxAmount
    ) {
        this.inputChannels = inputChannels;
        this.outputChannels = outputChannels;
        this.outputRequestChannels = outputRequestChannels;
        this.outputResponseChannels = outputResponseChannels;
        this.maxAmount = maxAmount;
    }

    public ProducerNode(int maxAmount){
        this.inputChannels = new ArrayList<>();
        this.outputChannels = new ArrayList<>();
        this.outputRequestChannels = new ArrayList<>();
        this.outputResponseChannels = new ArrayList<>();
        this.maxAmount = maxAmount;
    }

    public void addInputChannel(One2OneChannelInt channelInt) {
        inputChannels.add(channelInt);
    }

    public void addOutputChannel(One2OneChannelInt channelInt) {
        outputChannels.add(channelInt);
    }
    public void addOutputRequestChannel(One2OneChannelInt channelInt) {
        outputRequestChannels.add(channelInt);
    }

    public void addOutputResponseChannel(One2OneChannelInt channelInt) {
        outputResponseChannels.add(channelInt);
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
                int outputIndex = generator.nextInt(outputRequestChannels.size());
                outputRequestChannels.get(outputIndex).out().write(1);
                if (outputResponseChannels.get(outputIndex).in().read() > 0) {
                    outputChannels.get(outputIndex).out().write(1);
                    amount--;
                } else {
                    break; // stop sending after first failure and serve producers
                }
            }

        }
    }

    @Override
    public String toString() {
        return "P{" + amount + "}";
    }
}
