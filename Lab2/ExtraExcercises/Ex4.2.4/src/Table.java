import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Table {

    Lock lock = new ReentrantLock();
    Condition lackeyCondition = lock.newCondition();
    Condition[] forkConditions = {
            lock.newCondition(),
            lock.newCondition(),
            lock.newCondition(),
            lock.newCondition(),
            lock.newCondition()
    };
    boolean[] forkTaken = {false, false, false, false, false};
    int current = 0;

    public void takeForks(int i) throws InterruptedException {
        lock.lock();
        try {
            while (current == 4)
                lackeyCondition.await();

            current++;

            while (forkTaken[i])
                forkConditions[i].await();
            forkTaken[i] = true;

            while (forkTaken[(i+1) % 5])
                forkConditions[(i+1) % 5].await();
            forkTaken[(i+1) % 5] = true;
        } finally {
            lock.unlock();
        }
    }

    public void returnForks(int i) {
        lock.lock();
        try {
            forkTaken[i] = false;
            forkConditions[i].signal();

            forkTaken[(i+1) % 5] = false;
            forkConditions[(i+1) % 5].signal();

            current--;
            lackeyCondition.signal();
        } finally {
            lock.unlock();
        }
    }

//    public void takeForks(int i) throws InterruptedException {
//        lock.lock();
//        try {
//            while (freeForks[i] < 2)
//                philosophers[i].await();
//
//            freeForks[i+1 % 5]--;
//            freeForks[i+4 % 5]--;
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void returnForks(int i) {
//        lock.lock();
//        try {
//            freeForks[i+1 % 5]++;
//            freeForks[i+4 % 5]++;
//
//            if (freeForks[i+1 % 5] == 2)
//                philosophers[i+1 % 5].signal();
//            if (freeForks[i+4 % 5] == 2)
//                philosophers[i+4 % 5].signal();
//
//        } finally {
//            lock.unlock();
//        }
//    }
}
