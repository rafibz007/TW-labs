import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bufor {

    int halfOfMaxAmount;
    int currentAmount;
    ReentrantLock lock = new ReentrantLock();
    Condition firstProducer = lock.newCondition();
    Condition anotherProducers = lock.newCondition();
    Condition firstConsumer = lock.newCondition();
    Condition anotherConsumers = lock.newCondition();
    final Map<String, Integer> amountOfAccesses;

    public Bufor(int halfOfMaxAmount, Map<String, Integer> amountOfAccesses) {
        this.halfOfMaxAmount = halfOfMaxAmount;
        this.amountOfAccesses = amountOfAccesses;
    }

    public void produce(int amount) throws InterruptedException {
        lock.lock();
        try {
            while (lock.hasWaiters(firstProducer))
                anotherProducers.await();

            while (2*halfOfMaxAmount - currentAmount < amount)
                firstProducer.await();

            currentAmount += amount;
            amountOfAccesses.put(Thread.currentThread().getName(), amountOfAccesses.getOrDefault(Thread.currentThread().getName(), 0)+1);
            System.out.println(amountOfAccesses);

            anotherProducers.signal();
            firstConsumer.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume(int amount) throws InterruptedException {
        lock.lock();
        try {
            while (lock.hasWaiters(firstConsumer))
                anotherConsumers.await();

            while (currentAmount < amount)
                firstConsumer.await();

            currentAmount -= amount;

            anotherConsumers.signal();
            firstProducer.signal();
        } finally {
            lock.unlock();
        }
    }
}
