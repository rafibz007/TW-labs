package bufor.request;

import bufor.BuforServant;
import future.ReadWriteBuforFuture;

public class ConsumeRequest extends MethodRequest {

    public ConsumeRequest(BuforServant buforServant, ReadWriteBuforFuture<Integer> future, int amount) {
        super(buforServant, future, amount);
    }

    @Override
    public boolean guard() {
        return buforServant.getCurrentAmount() >= amount;
    }

    @Override
    public void call() {
        Integer result = buforServant.consume(amount);
        future.set(result);
    }

    @Override
    public String toString() {
        return String.format("C(%d)", amount);
    }
}
