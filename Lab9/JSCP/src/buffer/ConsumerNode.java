package buffer;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConsumerNode implements CSProcess {
    private final List<One2OneChannelInt> inputChannels;
    private final List<One2OneChannelInt> outputChannels;
    private final List<One2OneChannelInt> requestChannels;
    private final int maxAmount;
    private int amount = 0;

    public ConsumerNode(
            List<One2OneChannelInt> inputChannels,
            List<One2OneChannelInt> outputChannels,
            List<One2OneChannelInt> requestChannels,
            int maxAmount) {
        this.inputChannels = inputChannels;
        this.outputChannels = outputChannels;
        this.requestChannels = requestChannels;
        this.maxAmount = maxAmount;
    }

    public ConsumerNode(int maxAmount){
        this.inputChannels = new ArrayList<>();
        this.outputChannels = new ArrayList<>();
        this.requestChannels = new ArrayList<>();
        this.maxAmount = maxAmount;
    }

    public void addInputChannel(One2OneChannelInt channelInt) {
        inputChannels.add(channelInt);
    }

    public void addOutputChannel(One2OneChannelInt channelInt) {
        outputChannels.add(channelInt);
    }

    public void addRequestChannel(One2OneChannelInt channelInt) {
        requestChannels.add(channelInt);
    }


    @Override
    public void run() {
        final Guard[] guards = Stream
                .concat(
                    inputChannels.stream(),
                    requestChannels.stream()
                ).map(One2OneChannelInt::in)
                .toList()
                .toArray(new Guard[0]);
        final Alternative alternative = new Alternative(guards);

        while (true) {

            int index = alternative.select();
            if (index < inputChannels.size()){
//                INPUT
                if (amount < maxAmount) {
                    int value = inputChannels.get(index).in().read();
                    amount += value;
                }

            } else {
//                READ REQUEST
                index -= inputChannels.size();
                if (amount > 0) {
                    requestChannels.get(index).in().read();
                    outputChannels.get(index).out().write(1);
                    amount--;
                }
            }

        }
    }

    @Override
    public String toString() {
        return "C{" + amount + "}";
    }
}
