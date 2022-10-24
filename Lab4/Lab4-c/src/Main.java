import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int m = 100;
        Map<String, Integer> amountOfAccesses = new HashMap<>();
        Bufor bufor = new Bufor(m, amountOfAccesses); // minimum maxAmount is 2*(max produce/consume amount) to prevent blocking

        List<Producer> producers = List.of(
                new Producer(bufor, m-1, m),
                new Producer(bufor, 1, 2),
                new Producer(bufor, 1, 2),
                new Producer(bufor, 1, 2),
                new Producer(bufor, 1, 2),
                new Producer(bufor, 1, 2)
        );

        List<Consumer> consumers = List.of(
                new Consumer(bufor, 1, 2),
                new Consumer(bufor, 1, 2),
                new Consumer(bufor, 1, 2),
                new Consumer(bufor, 1, 2)
        );

        List<Thread> threads = Stream.of(
                        producers,
                        consumers
                )
                .flatMap(Collection::stream)
                .map(Thread::new)
                .toList();

        threads.forEach(Thread::start);
        for (Thread thread : threads)
            thread.join();


    }
}