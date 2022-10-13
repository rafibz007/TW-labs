public class SimpleNumber {
    private int number;

    public SimpleNumber(int number) {
        this.number = number;
    }

    public void increment() {
        number = number + 1;
    }

    public void decrement() {
        number = number - 1;
    }

    public int getValue() {
        return number;
    }
}
