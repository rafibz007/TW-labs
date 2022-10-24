import java.util.Map;
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
    final Map<String, Integer> amountOfAccesses;

    public Bufor(int halfOfMaxAmount, Map<String, Integer> amountOfAccesses) {
        this.halfOfMaxAmount = halfOfMaxAmount;
        this.amountOfAccesses = amountOfAccesses;
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
            while (firstConsumerWaiting)
                anotherConsumers.await();

            while (currentAmount < amount){
                firstConsumerWaiting = true;
                firstConsumer.await();
                firstConsumerWaiting = false;
            }

            currentAmount -= amount;

            anotherConsumers.signal();
            firstProducer.signal();
        } finally {
            lock.unlock();
        }
    }
}
