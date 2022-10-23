public class Producer implements Runnable{

    Bufor bufor;

    public Producer(Bufor bufor) {
        this.bufor = bufor;
    }

    private void produce() throws InterruptedException {
        bufor.produce();
    }

    @Override
    public void run() {
        while (true) {
            try {
                produce();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
