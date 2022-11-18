import config.Config;

import java.util.Random;

public class Producer implements Runnable{

    Bufor bufor;
    Random generator;
    int max;
    int min;

    public Producer(Bufor bufor, Random generator, int min, int max) {
        this.bufor = bufor;
        this.generator = generator;
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
                double start = System.currentTimeMillis();
                produce();
                Thread.sleep(Config.additionalTaskTime);
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
