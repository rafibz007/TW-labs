public class DecrementingThread implements Runnable{
    private int numberOfIterations;
    private SimpleNumber simpleNumber;

    public DecrementingThread(
            int numberOfIterations,
            SimpleNumber simpleNumber
    ) {
        this.numberOfIterations = numberOfIterations;
        this.simpleNumber = simpleNumber;
    }

    @Override
    public void run() {
        for (int i=0; i<numberOfIterations; i++) {
            simpleNumber.decrement();
        }
    }
}
