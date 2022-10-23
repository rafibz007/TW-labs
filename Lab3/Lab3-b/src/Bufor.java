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

    public void produce(int amount) throws InterruptedException {
        lock.lock();
        try {
            while (maxPortionsAmount - elements < amount) // will lock if all producers wants to produce more than available space (and consume cond)
            {
                System.out.format("[%s] Wanted to produce: %d, but failed\n", Thread.currentThread().getName(), amount);
                producerCondition.await();
            }


            elements += amount;
            System.out.format("[%s] Produced. New value = " + elements + "\n", Thread.currentThread().getName());

            consumerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume(int amount) throws InterruptedException {
        lock.lock();
        try {
            while (elements < amount) // will lock if all consumers wants to consume more than available space (and produce condition)
            {
                System.out.format("[%s] Wanted to consume: %d, but failed\n", Thread.currentThread().getName(), amount);
                consumerCondition.await();
            }

            elements -= amount;
            System.out.format("[%s] Consumed. New value = " + elements + "\n", Thread.currentThread().getName());

            producerCondition.signal();
        } finally {
            lock.unlock();
        }
    }
}
