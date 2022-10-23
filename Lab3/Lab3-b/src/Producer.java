public class Producer implements Runnable{

    Bufor bufor;
    int max;

    public Producer(Bufor bufor, int max) {
        this.bufor = bufor;
        this.max = max;
    }

    private void produce() throws InterruptedException {
        bufor.produce(randInt(1,max));
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

    private int randInt(int Min, int Max) {
        return Min + (int)(Math.random() * ((Max - Min) + 1));
    }
}
