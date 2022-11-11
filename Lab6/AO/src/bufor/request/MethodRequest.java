package bufor.request;

import bufor.BuforServant;
import future.ReadWriteBuforFuture;

public abstract class MethodRequest {

    protected final int amount;
    protected final BuforServant buforServant;
    protected final ReadWriteBuforFuture<Integer> future;

    public MethodRequest(BuforServant buforServant, ReadWriteBuforFuture<Integer> future, int amount) {
        this.buforServant = buforServant;
        this.amount = amount;
        this.future = future;
    }

    public abstract boolean guard();
    public abstract void call();
}
