package debug;

import buffer.Buffer;
import buffer.ProducerNode;
import buffer.ConsumerNode;
import org.jcsp.lang.CSProcess;

import java.util.List;

public class Logger implements CSProcess {

    private final Buffer buffer;

    public Logger(Buffer buffer) {
        this.buffer = buffer;
    }


    @Override
    public void run() {
        while (true) {
            try {
                List<ProducerNode> producerNodes = buffer.getProducerNodes();
                List<ConsumerNode> consumerNodes = buffer.getConsumerNodes();
                System.out.format("===============\n");
                for (int i=0; i<Math.max(consumerNodes.size(), producerNodes.size()); i++) {
                    String producer = "";
                    try {
                        producer = producerNodes.get(i).toString();
                    } catch (IndexOutOfBoundsException ignore) {}

                    String consumer = "";
                    try {
                        consumer = consumerNodes.get(i).toString();
                    } catch (IndexOutOfBoundsException ignore) {}

                    System.out.format("%7s %7s\n", producer, consumer);
                }
                System.out.format("===============\n");
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }
}
