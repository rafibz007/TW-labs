public class Consumer implements Runnable{

    Monitor monitor;

    public Consumer(Monitor monitor) {
        this.monitor = monitor;
    }

    private void consume() throws InterruptedException {
        monitor.consume();
    }

    @Override
    public void run() {
        while (true) {
            try {
                consume();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
