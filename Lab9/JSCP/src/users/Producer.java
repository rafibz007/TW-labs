package users;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Producer implements CSProcess {

    private final List<One2OneChannelInt> outputChannels;
    private final Random generator = new Random();

    public Producer(List<One2OneChannelInt> outputChannels) {
        this.outputChannels = outputChannels;
    }

    public Producer() {
        this.outputChannels = new ArrayList<>();
    }

    public void addOutputChannel(One2OneChannelInt channelInt) {
        outputChannels.add(channelInt);
    }

    @Override
    public void run() {
        while (true) {
            try {
                outputChannels.get(
                                generator.nextInt(outputChannels.size())
                        )
                        .out()
                        .write(1);
//                System.out.format("[%s] Produced\n", Thread.currentThread().getName());
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }
}
