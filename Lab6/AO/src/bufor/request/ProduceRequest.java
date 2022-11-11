package bufor.request;

import bufor.BuforServant;
import future.ReadWriteBuforFuture;

public class ProduceRequest extends MethodRequest {

    public ProduceRequest(BuforServant buforServant, ReadWriteBuforFuture<Integer> future, int amount) {
        super(buforServant, future, amount);
    }

    @Override
    public boolean guard() {
        return buforServant.getMaxAmount() - buforServant.getCurrentAmount() >= amount;
    }

    @Override
    public void call() {
        Integer result = buforServant.produce(amount);
        future.set(result);
    }

    @Override
    public String toString() {
        return String.format("P(%d)", amount);
    }
}
