package future;

public class ReadWriteBuforFuture<T> extends BuforFuture<T>{

    public void set(T value) {
        this.value = value;
        this.done = true;
    }
}
