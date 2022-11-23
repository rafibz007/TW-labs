package future;

public class ReadWriteBuforFuture<T> extends BuforFuture<T>{

    public synchronized void set(T value) {
        this.value = value;
        this.done = true;
        notifyAll();
    }
}
