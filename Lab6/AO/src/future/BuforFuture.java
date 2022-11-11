package future;

public class BuforFuture<T> {

    protected boolean done;
    protected T value;

    public BuforFuture() {
        this.done = false;
        this.value = null;
    }

    public synchronized boolean isDone() {
        return done;
    }

    public T get() {
        return value;
    }


}
