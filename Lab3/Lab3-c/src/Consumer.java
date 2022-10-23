import java.util.Random;

public class Consumer implements Runnable{

    final int minVal = 0;
    final int maxVal;

    final Bufor bufor;

    public Consumer(Bufor bufor, int maxVal) {
        this.maxVal = maxVal;
        this.bufor = bufor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                bufor.consume(randInt(minVal, maxVal));
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int randInt(int Min, int Max) {
        return Min + (int)(Math.random() * ((Max - Min) + 1));
    }
}
