import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        SimpleNumber simpleNumber = new SimpleNumber(0);
        int amountOfIterations = 1000;
        int amountOfThreads = 30;

        List<Runnable> runnables = new ArrayList<>();
        for (int i=0; i<amountOfThreads; i++)
            runnables.add(new IncrementingThread(amountOfIterations, simpleNumber));

        for (int i=0; i<amountOfThreads; i++)
            runnables.add(new DecrementingThread(amountOfIterations, simpleNumber));

        List<Thread> threads = runnables.stream().map(Thread::new).toList();
        threads.forEach(Thread::start);
        for (Thread thread : threads ) {
            thread.join();
        }

        System.out.println(simpleNumber.getValue());
    }
}