package zad.a;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Consumer implements Runnable, CSProcess {
    final One2OneChannelInt channel;

    public Consumer(final One2OneChannelInt in) {
        this.channel = in;
    }

    @Override
    public void run() {
        int item = channel.in().read();
        System.out.println(item);
    }
}
