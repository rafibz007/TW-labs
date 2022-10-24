import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bufor {

    int maxPortionsAmount;
    int elements = 0;

    final Lock lock = new ReentrantLock();
    final Condition consumerCondition = lock.newCondition();
    final Condition producerCondition = lock.newCondition();
    final Map<String, Integer> amountOfAccesses;

    public Bufor(int maxPortionsAmount, Map<String, Integer> amountOfAccesses) {
        this.maxPortionsAmount = maxPortionsAmount;
        this.amountOfAccesses = amountOfAccesses;
    }

    public void produce(int amount) throws InterruptedException {
        lock.lock();
        try {
            while (maxPortionsAmount - elements < amount) // will lock if all producers wants to produce more than available space (and consume cond)
            {
                producerCondition.await();
            }


            elements += amount;
            amountOfAccesses.put(Thread.currentThread().getName(), amountOfAccesses.getOrDefault(Thread.currentThread().getName(), 0)+1);
            System.out.println(amountOfAccesses);

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
                consumerCondition.await();
            }

            elements -= amount;

            producerCondition.signal();
        } finally {
            lock.unlock();
        }
    }
}
