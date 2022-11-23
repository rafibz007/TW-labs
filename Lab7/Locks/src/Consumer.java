import config.Config;

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
        bufor.consume(randInt(min,max));
        for (int i=0; i<Config.additionalTaskTime; i++)
            Math.sin(i);
    }

    @Override
    public void run() {
        while (true) {
            try {
                double start = System.currentTimeMillis();
                consume();
                double end = System.currentTimeMillis();
                Config.amountOfAccesses.put(Thread.currentThread().getName(), Config.amountOfAccesses.getOrDefault(Thread.currentThread().getName(), 0)+1);
                Config.accessTime.put(Thread.currentThread().getName(), Config.accessTime.getOrDefault(Thread.currentThread().getName(), 0D)+(end-start));
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }

    private int randInt(int Min, int Max) {
        return generator.nextInt(Max) + Min;
    }
}
