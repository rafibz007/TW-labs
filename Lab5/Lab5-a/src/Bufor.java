import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bufor {

    int halfOfMaxAmount;
    int currentAmount;
    Lock lock = new ReentrantLock();
    Lock anotherConsumersLock = new ReentrantLock();
    Lock anotherProducersLock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public Map<String, Long> accessTime = new HashMap<>();
    public Map<String, Integer> amountOfAccesses = new HashMap<>();

    public Bufor(int halfOfMaxAmount) {
        this.halfOfMaxAmount = halfOfMaxAmount;
    }

    public void produce(int amount) throws InterruptedException {
        long start = System.currentTimeMillis();
        anotherProducersLock.lock();
        try {

            lock.lock();
            try {

                while (2*halfOfMaxAmount - currentAmount < amount)
                    condition.await();

                currentAmount += amount;

                condition.signal();

                long end = System.currentTimeMillis();
                accessTime.put(producerThreadName(), accessTime.getOrDefault(producerThreadName(), 0L)+(end-start));
                amountOfAccesses.put(producerThreadName(), amountOfAccesses.getOrDefault(producerThreadName(), 0)+1);

            } finally {
                lock.unlock();
            }

        } finally {
            anotherProducersLock.unlock();
        }

    }

    public void consume(int amount) throws InterruptedException {
        long start = System.currentTimeMillis();
        anotherConsumersLock.lock();
        try {

            lock.lock();
            try {

                while (currentAmount < amount)
                    condition.await();

                currentAmount -= amount;

                condition.signal();

                long end = System.currentTimeMillis();
                accessTime.put(consumerThreadName(), accessTime.getOrDefault(consumerThreadName(), 0L)+(end-start));
                amountOfAccesses.put(consumerThreadName(), amountOfAccesses.getOrDefault(consumerThreadName(), 0)+1);

            } finally {
                lock.unlock();
            }

        } finally {
            anotherConsumersLock.unlock();
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
