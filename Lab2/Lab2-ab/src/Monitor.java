public class Monitor {

    int maxPortionsAmount;
    int value = 0;

    public Monitor(int maxPortionsAmount) {
        this.maxPortionsAmount = maxPortionsAmount;
    }

    public synchronized void produce() throws InterruptedException {
        System.out.println("Producing... (current value = " + value + ")");
        while (value == maxPortionsAmount)
            wait();

        value++;
        System.out.println("Produced. New value = " + value);

        notify(); // notifyAll() fixes deadlock
    }

    public synchronized void consume() throws InterruptedException {
        System.out.println("Consuming... (current value = " + value + ")");
        while (value == 0)
            wait();

        value--;
        System.out.println("Consumed. New value = " + value);

        notify(); // notifyAll() fixes deadlock
    }
}
