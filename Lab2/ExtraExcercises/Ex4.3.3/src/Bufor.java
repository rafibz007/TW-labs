import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bufor {

    int halfOfMaxAmount;
    int currentAmount;
    Lock lock = new ReentrantLock();
    Condition firstProducer = lock.newCondition();
    Condition anotherProducers = lock.newCondition();
    Condition firstConsumer = lock.newCondition();
    Condition anotherConsumers = lock.newCondition();

    boolean firstProducerWaiting = false;
    boolean firstConsumerWaiting = false;

    public Bufor(int halfOfMaxAmount) {
        this.halfOfMaxAmount = halfOfMaxAmount;
    }

    public void produce(int amount) throws InterruptedException {
        lock.lock();
        try {
            while (firstProducerWaiting)
                anotherProducers.await();

            while (2*halfOfMaxAmount - currentAmount < amount) {
                firstProducerWaiting = true;
                firstProducer.await();
                firstProducerWaiting = false;
            }

            System.out.format("Producing: %d (current: %d/%d)\n", amount, currentAmount+amount, 2*halfOfMaxAmount);
            currentAmount += amount;

            anotherProducers.signal();
            firstConsumer.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume(int amount) throws InterruptedException {
        lock.lock();
        try {
            while (firstConsumerWaiting)
                anotherConsumers.await();

            while (currentAmount < amount){
                firstConsumerWaiting = true;
                firstConsumer.await();
                firstConsumerWaiting = false;
            }

            System.out.format("Consuming: %d (current: %d/%d)\n", amount, currentAmount-amount, 2*halfOfMaxAmount);
            currentAmount -= amount;

            anotherConsumers.signal();
            firstProducer.signal();
        } finally {
            lock.unlock();
        }
    }
}
