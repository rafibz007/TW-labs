import java.util.Random;

public class Consumer implements Runnable{

    Bufor bufor;
    Random generator;
    int max;
    int min;

    public Consumer(Bufor bufor, Random generator, int min, int max) {
        this.bufor = bufor;
        this.generator = generator;
        this.max = max;
        this.min = min;
    }

    private void consume() throws InterruptedException {
        bufor.consume(randInt(1,max));
    }

    @Override
    public void run() {
        while (true) {
            try {
                consume();
//                Thread.sleep(1000);
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
            }
        }
    }

    private int randInt(int Min, int Max) {
        return generator.nextInt(Max) + Min;
    }
}
