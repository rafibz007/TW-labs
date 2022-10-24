public class Producer implements Runnable{

    Bufor bufor;
    int max;
    int min;

    public Producer(Bufor bufor, int min, int max) {
        this.bufor = bufor;
        this.max = max;
        this.min = min;
    }

    private void produce() throws InterruptedException {
        bufor.produce(randInt(min,max));
    }

    @Override
    public void run() {
        while (true) {
            try {
                produce();
//                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int randInt(int Min, int Max) {
        return Min + (int)(Math.random() * ((Max - Min) + 1));
    }
}
