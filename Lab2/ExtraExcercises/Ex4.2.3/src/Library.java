import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Library {

    int reading = 0;
    int writing = 0;

    int readersWaiting = 0;
    int writersWaiting = 0;

    final Lock lock = new ReentrantLock();
    final Condition readerCondition = lock.newCondition();
    final Condition writerCondition = lock.newCondition();

    public void startReading() throws InterruptedException {
        lock.lock();
        try {
            while (writersWaiting > 0 || writing > 0){
                readersWaiting++;
                readerCondition.await();
                readersWaiting--;
            }
            reading++;
            System.out.format("[%s] Readers in Library: %d\n", Thread.currentThread(), reading);
        } finally {
            lock.unlock();
        }
    }

    public void stopReading() {
        lock.lock();
        try {
            reading--;
            System.out.format("[%s] Readers in Library: %d\n", Thread.currentThread(), reading);
            if (reading == 0)
                writerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void startWriting() throws InterruptedException {
        lock.lock();
        try {
            while (reading + writing > 0){
                writersWaiting++;
                writerCondition.await();
                writersWaiting--;
            }
            writing = 1;
            System.out.format("[%s] Writers in Library: %d\n", Thread.currentThread(), writing);
        } finally {
            lock.unlock();
        }
    }

    public void stopWriting() {
        lock.lock();
        try {
            writing = 0;
            System.out.format("[%s] Writers in Library: %d\n", Thread.currentThread(), writing);
            if (readersWaiting == 0)
                writerCondition.signal();
            else
                readerCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
