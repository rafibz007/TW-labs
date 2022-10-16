import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    int maxPortionsAmount;
    List<Element> elementsList = new ArrayList<>();

    final Lock lock = new ReentrantLock();
    final Condition consumerCondition = lock.newCondition();
    final Condition producerCondition = lock.newCondition();

    public Monitor(int maxPortionsAmount) {
        this.maxPortionsAmount = maxPortionsAmount;
    }

    public void produce(Element element) throws InterruptedException {
        lock.lock();
        try {
            System.out.println("Producing... (current amount = " + elementsList.size() + ")");
            while (elementsList.size() == maxPortionsAmount)
                producerCondition.await();

            elementsList.add(element);
            System.out.println("Produced. New value = " + element.value);

            consumerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public Element consume() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("Consuming... (current amount = " + elementsList.size() + ")");
            while (elementsList.size() == 0)
                consumerCondition.await();

            Element element = elementsList.remove(0);
            System.out.println("Consumed. New value = " + element.value);

            producerCondition.signal();

            return element;
        } finally {
            lock.unlock();
        }
    }
}
