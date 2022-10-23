public class Consumer implements Runnable{

    Bufor bufor;

    public Consumer(Bufor bufor) {
        this.bufor = bufor;
    }

    private void consume() throws InterruptedException {
        bufor.consume();
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
