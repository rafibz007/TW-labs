public class Consumer implements Runnable{

    Bufor bufor;
    int max;

    public Consumer(Bufor bufor, int max) {
        this.bufor = bufor;
        this.max = max;
    }

    private void consume() throws InterruptedException {
        bufor.consume(randInt(1,max));
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

    private int randInt(int Min, int Max) {
        return Min + (int)(Math.random() * ((Max - Min) + 1));
    }
}
