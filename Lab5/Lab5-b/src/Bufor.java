import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bufor {


    public Map<String, Long> accessTime = new HashMap<>();
    public Map<String, Integer> amountOfAccesses = new HashMap<>();

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
        long start = System.currentTimeMillis();
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

            anotherProducers.signal();
            firstConsumer.signal();

            long end = System.currentTimeMillis();
            accessTime.put(consumerThreadName(), accessTime.getOrDefault(consumerThreadName(), 0L)+(end-start));
            amountOfAccesses.put(consumerThreadName(), amountOfAccesses.getOrDefault(consumerThreadName(), 0)+1);
        } finally {
            lock.unlock();
        }
    }

    public void consume(int amount) throws InterruptedException {
        long start = System.currentTimeMillis();
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

            long end = System.currentTimeMillis();
            accessTime.put(consumerThreadName(), accessTime.getOrDefault(consumerThreadName(), 0L)+(end-start));
            amountOfAccesses.put(consumerThreadName(), amountOfAccesses.getOrDefault(consumerThreadName(), 0)+1);
        } finally {
            lock.unlock();
        }
    }


    private String consumerThreadName() {
        return Thread.currentThread().getName();
//        return "c" + Thread.currentThread().getName().split("-")[1];
    }

    private String producerThreadName() {
        return Thread.currentThread().getName();
//        return "p" + Thread.currentThread().getName().split("-")[1];
    }

}
