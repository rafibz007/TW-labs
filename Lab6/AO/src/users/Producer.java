package users;

import bufor.Bufor;
import future.BuforFuture;

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
        BuforFuture<Integer> future = bufor.produce(randInt(min, max));
        while (!future.isDone()) {

            for (int i=0; i<300; i++)
                Math.sin(i);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int randInt(int Min, int Max) {
        return generator.nextInt(Max) + Min;
    }
}
