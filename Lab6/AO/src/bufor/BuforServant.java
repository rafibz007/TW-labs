package bufor;

public class BuforServant {

    private final int maxAmount;
    private int currentAmount;

    public BuforServant(int maxAmount) {
        this.maxAmount = maxAmount;
        this.currentAmount = 0;
    }

    public int produce(int amount) {
        if (currentAmount + amount > maxAmount)
            throw new IllegalArgumentException(String.format("Producing value %d will exceed limit\n", amount));
        currentAmount += amount;
        System.out.format("Produced %d (current: %d/%d)\n", amount, currentAmount, maxAmount);
        return currentAmount;
    }

    public int consume(int amount) {
        if (currentAmount < amount)
            throw new IllegalArgumentException(String.format("Not enough resources to consume value %d", amount));
        currentAmount -= amount;
        System.out.format("Consumed %d (current: %d/%d)\n", amount, currentAmount, maxAmount);
        return currentAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }
}
