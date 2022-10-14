public class Producer implements Runnable{

    Monitor monitor;
    int val = 0;

    public Producer(Monitor monitor) {
        this.monitor = monitor;
    }

    private void produce() throws InterruptedException {
        monitor.produce(new Element(val));
        val++;
    }

    @Override
    public void run() {
        while (true) {
            try {
                produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
