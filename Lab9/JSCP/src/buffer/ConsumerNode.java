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
    private final List<One2OneChannelInt> inputRequestChannels;
    private final List<One2OneChannelInt> inputResponseChannels;
    private final List<One2OneChannelInt> outputChannels;
    private final List<One2OneChannelInt> outputRequestChannels;
    private final int maxAmount;
    private int amount = 0;

    public ConsumerNode(
            List<One2OneChannelInt> inputChannels,
            List<One2OneChannelInt> inputRequestChannels,
            List<One2OneChannelInt> inputResponseChannels,
            List<One2OneChannelInt> outputChannels,
            List<One2OneChannelInt> outputRequestChannels,
            int maxAmount
    ) {
        this.inputChannels = inputChannels;
        this.inputRequestChannels = inputRequestChannels;
        this.inputResponseChannels = inputResponseChannels;
        this.outputChannels = outputChannels;
        this.outputRequestChannels = outputRequestChannels;
        this.maxAmount = maxAmount;
    }

    public ConsumerNode(int maxAmount) {
        this.inputChannels = new ArrayList<>();
        this.inputRequestChannels = new ArrayList<>();
        this.inputResponseChannels = new ArrayList<>();
        this.outputChannels = new ArrayList<>();
        this.outputRequestChannels = new ArrayList<>();
        this.maxAmount = maxAmount;
    }

    public void addInputChannel(One2OneChannelInt channelInt) {
        inputChannels.add(channelInt);
    }
    public void addInputRequestChannel(One2OneChannelInt channelInt) {
        inputRequestChannels.add(channelInt);
    }

    public void addInputResponseChannel(One2OneChannelInt channelInt) {
        inputResponseChannels.add(channelInt);
    }

    public void addOutputChannel(One2OneChannelInt channelInt) {
        outputChannels.add(channelInt);
    }

    public void addRequestChannel(One2OneChannelInt channelInt) {
        outputRequestChannels.add(channelInt);
    }


    @Override
    public void run() {
        final Guard[] guards = Stream
                .concat(
                    inputRequestChannels.stream(),
                    outputRequestChannels.stream()
                ).map(One2OneChannelInt::in)
                .toList()
                .toArray(new Guard[0]);
        final Alternative alternative = new Alternative(guards);

        while (true) {

            int index = alternative.select();
            if (index < inputRequestChannels.size()){
//                INPUT
                inputRequestChannels.get(index).in().read();
                if (amount < maxAmount) {
                    inputResponseChannels.get(index).out().write(1);
                    int value = inputChannels.get(index).in().read();
                    amount += value;
                } else {
                    inputResponseChannels.get(index).out().write(0);
                }

            } else {
//                READ REQUEST
                index -= inputRequestChannels.size();
                if (amount > 0) {
                    outputRequestChannels.get(index).in().read();
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
