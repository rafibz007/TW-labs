import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bufor {

    int maxPortionsAmount;
    int elements = 0;

    final Lock lock = new ReentrantLock();
    final Condition consumerCondition = lock.newCondition();
    final Condition producerCondition = lock.newCondition();

    public Bufor(int maxPortionsAmount) {
        this.maxPortionsAmount = maxPortionsAmount;
    }

    public void produce() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("Producing... (current amount = " + elements + ")");
            while (elements == maxPortionsAmount)
                producerCondition.await();

            elements++;
            System.out.println("Produced. New value = " + elements);

            consumerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("Consuming... (current amount = " + elements + ")");
            while (elements == 0)
                consumerCondition.await();

            elements--;
            System.out.println("Consumed. New value = " + elements);

            producerCondition.signal();
        } finally {
            lock.unlock();
        }
    }
}
