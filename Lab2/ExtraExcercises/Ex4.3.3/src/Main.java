import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        int m = 10;
        Bufor bufor = new Bufor(m);

        List<Producer> producers = List.of(
                new Producer(bufor, m),
                new Producer(bufor, m),
                new Producer(bufor, m),
                new Producer(bufor, m)
        );

        List<Consumer> consumers = List.of(
                new Consumer(bufor, m),
                new Consumer(bufor, m),
                new Consumer(bufor, m),
                new Consumer(bufor, m),
                new Consumer(bufor, m)
        );

        List<Thread> threads = Stream.of(
                        producers,
                        consumers
                )
                .flatMap(Collection::stream)
                .map(Thread::new)
                .toList();

        threads.forEach(Thread::start);
    }
}