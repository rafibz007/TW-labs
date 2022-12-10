import buffer.Buffer;
import debug.Logger;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;
import users.Consumer;
import users.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(3, 4, 100);
        Logger logger = new Logger(buffer);

        List<Consumer> consumers = new ArrayList<>();
        for (int i=0; i<20; i++)
            consumers.add(new Consumer());

        List<Producer> producers = new ArrayList<>();
        for (int i=0; i<15; i++)
            producers.add(new Producer());

        consumers.forEach(buffer::registerConsumer);
        producers.forEach(buffer::registerProducer);

        CSProcess[] procList = Stream.concat(
                Stream.concat(
                        buffer.getConsumerNodes().stream(),
                        buffer.getProducerNodes().stream()
                ),
                Stream.concat(
                        Stream.concat(
                                producers.stream(),
                                consumers.stream()
                        ),
                        Stream.of(logger)
                )
        ).toList().toArray(new CSProcess[0]);

        Parallel par = new Parallel(procList);
        par.run();
    }
}