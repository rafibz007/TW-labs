import config.Config;

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

    public Bufor(int halfOfMaxAmount) {
        this.halfOfMaxAmount = halfOfMaxAmount;
    }

    public void produce(int amount) throws InterruptedException {
        anotherProducersLock.lock();
        try {

            lock.lock();
            try {

                while (2*halfOfMaxAmount - currentAmount < amount)
                    condition.await();

                try {
                    Thread.sleep(Config.mainTaskTime);
                } catch (InterruptedException ignored) {}

                currentAmount += amount;
                condition.signal();

                System.out.format("Produced %d (current: %d/%d)\n", amount, currentAmount, 2*halfOfMaxAmount);

            } finally {
                lock.unlock();
            }

        } finally {
            anotherProducersLock.unlock();
        }

    }

    public void consume(int amount) throws InterruptedException {
        anotherConsumersLock.lock();
        try {

            lock.lock();
            try {

                while (currentAmount < amount)
                    condition.await();

                try {
                    Thread.sleep(Config.mainTaskTime);
                } catch (InterruptedException ignored) {}

                currentAmount -= amount;
                condition.signal();

                System.out.format("Consumed %d (current: %d/%d)\n", amount, currentAmount, 2*halfOfMaxAmount);

            } finally {
                lock.unlock();
            }

        } finally {
            anotherConsumersLock.unlock();
        }
    }
}
