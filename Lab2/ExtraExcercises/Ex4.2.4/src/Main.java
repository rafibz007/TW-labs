import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Table table = new Table();
        List<Philosopher> philosophers = new ArrayList<>();
        for (int i=0; i<5; i++)
            philosophers.add(
                    new Philosopher(table, i)
            );

        List<Thread> threads = philosophers.stream().map(Thread::new).toList();
        threads.forEach(Thread::start);
    }
}