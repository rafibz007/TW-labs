import java.util.ArrayList;
import java.util.List;

public class Monitor {

    int maxPortionsAmount;
    List<Element> elementsList = new ArrayList<>();

    public Monitor(int maxPortionsAmount) {
        this.maxPortionsAmount = maxPortionsAmount;
    }

    public synchronized void produce(Element element) throws InterruptedException {
        System.out.println("Producing... (current amount = " + elementsList.size() + ")");
        while (elementsList.size() == maxPortionsAmount)
            wait();

        elementsList.add(element);
        System.out.println("Produced. New value = " + element.value);

        notify();
    }

    public synchronized Element consume() throws InterruptedException {
        System.out.println("Consuming... (current amount = " + elementsList.size() + ")");
        while (elementsList.size() == 0)
            wait();

        Element element = elementsList.remove(0);
        System.out.println("Consumed. New value = " + element.value);

        notify();

        return element;
    }
}
