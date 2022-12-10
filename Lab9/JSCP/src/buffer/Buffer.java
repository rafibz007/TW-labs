package buffer;

import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import users.Consumer;
import users.Producer;

import java.util.ArrayList;
import java.util.List;

public class Buffer {
    private final List<ProducerNode> producerNodes = new ArrayList<>();
    private final List<ConsumerNode> consumerNodes = new ArrayList<>();

    public Buffer(
            int producerNodesAmount,
            int consumerNodesAmount,
            int nodeMaxAmount
    ) {
        for (int i=0; i<producerNodesAmount; i++) {
            ProducerNode producerNode = new ProducerNode(nodeMaxAmount);
            producerNodes.add(producerNode);
        }

        for (int i=0; i<consumerNodesAmount; i++) {
            ConsumerNode consumerNode = new ConsumerNode(nodeMaxAmount);
            consumerNodes.add(consumerNode);
            for (ProducerNode producerNode : producerNodes) {
                One2OneChannelInt channel = Channel.one2oneInt();
                producerNode.addOutputChannel(channel);
                consumerNode.addInputChannel(channel);
            }
        }
    }

    public void registerConsumer(Consumer consumer) {
        for (ConsumerNode consumerNode : consumerNodes) {
            One2OneChannelInt channel = Channel.one2oneInt();
            One2OneChannelInt requestChannel = Channel.one2oneInt();

            consumer.addInputChannel(channel);
            consumer.addRequestChannel(requestChannel);

            consumerNode.addOutputChannel(channel);
            consumerNode.addRequestChannel(requestChannel);
        }
    }

    public void registerProducer(Producer producer) {
        for (ProducerNode producerNode : producerNodes) {
            One2OneChannelInt channel = Channel.one2oneInt();

            producer.addOutputChannel(channel);

            producerNode.addInputChannel(channel);
        }
    }

    public List<ConsumerNode> getConsumerNodes() {
        return consumerNodes;
    }

    public List<ProducerNode> getProducerNodes() {
        return producerNodes;
    }
}
