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

    public synchronized T get() {
        return value;
    }

    public synchronized T awaitGet() throws InterruptedException {
        if (!isDone()) {
            wait();
        }
        return value;
    }
}
