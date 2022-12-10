package users;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Consumer implements CSProcess {

    private final List<One2OneChannelInt> inputChannels;
    private final List<One2OneChannelInt> requestChannels;
    private final Random generator = new Random(1000);

    public Consumer(List<One2OneChannelInt> inputChannels, List<One2OneChannelInt> requestChannels) {
        this.inputChannels = inputChannels;
        this.requestChannels = requestChannels;
    }

    public Consumer() {
        this.inputChannels = new ArrayList<>();
        this.requestChannels = new ArrayList<>();
    }

    public void addInputChannel(One2OneChannelInt channelInt) {
        inputChannels.add(channelInt);
    }

    public void addRequestChannel(One2OneChannelInt channelInt) {
        requestChannels.add(channelInt);
    }

    @Override
    public void run() {
        while (true) {
            try {
                int index = generator.nextInt(requestChannels.size());
                requestChannels.get(index).out().write(1);
                inputChannels.get(index).in().read();
//                System.out.format("[%s] Consumed\n", Thread.currentThread().getName());
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }
}
