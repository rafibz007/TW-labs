import bufor.Bufor;
import users.Consumer;
import users.Producer;

import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int pkAmount = 5;
        int m = 500;
        Bufor bufor = new Bufor(2*m); // minimum maxAmount is 2*(max produce/consume amount) to prevent blocking

        int seed = 1000;
        Random generator = new Random(seed);

        List<Producer> producers = new ArrayList<>();
        for (int i=0; i<pkAmount; i++)
            producers.add(new Producer(bufor, generator, 1, m));

        List<Consumer> consumers = new ArrayList<>();
        for (int i=0; i<pkAmount; i++)
            consumers.add(new Consumer(bufor, generator, 1, m));


        List<Thread> producerThreads = producers
                .stream()
                .map(Thread::new)
                .toList();
        for (int i=0; i<producerThreads.size(); i++)
            producerThreads.get(i).setName("Producer-" + i);


        List<Thread> consumerThreads = consumers
                .stream()
                .map(Thread::new)
                .toList();
        for (int i=0; i<producerThreads.size(); i++)
            consumerThreads.get(i).setName("Consumer-" + i);


        List<Thread> threads = Stream.of(
                        consumerThreads,
                        producerThreads
                )
                .flatMap(Collection::stream)
                .toList();

        threads.forEach(Thread::start);

        for (Thread thread: threads)
            thread.join();
    }
}