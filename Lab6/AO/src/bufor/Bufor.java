package bufor;

import bufor.request.ConsumeRequest;
import bufor.request.ProduceRequest;
import future.BuforFuture;
import future.ReadWriteBuforFuture;

public class Bufor {
    private final BuforScheduler buforScheduler;
    private final BuforServant buforServant;
    private final Thread buferSchedulerThread;

    public Bufor(int maxAmount) {
        this.buforScheduler = new BuforScheduler();
        this.buforServant = new BuforServant(maxAmount);
        this.buferSchedulerThread = new Thread(buforScheduler);
        buferSchedulerThread.start();
    }

    public BuforFuture<Integer> produce(int amount) {
        ReadWriteBuforFuture<Integer> future = new ReadWriteBuforFuture<>();
        buforScheduler.enqueue(
                new ProduceRequest(buforServant, future, amount)
        );
        return future;
    }

    public BuforFuture<Integer> consume(int amount) {
        ReadWriteBuforFuture<Integer> future = new ReadWriteBuforFuture<>();
        buforScheduler.enqueue(
                new ConsumeRequest(buforServant, future, amount)
        );
        return future;
    }
}
