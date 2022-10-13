public class Bufor {

    int putIndex = 0;
    int popIndex = 0;
    int maxPortionsAmount;
    int currentPortionsAmount = 0;
    Portion[] portionList;

    public Bufor(int maxPortionsAmount) {
        this.maxPortionsAmount = maxPortionsAmount;
        portionList = new Portion[maxPortionsAmount];
    }

    public synchronized void put(Portion portion) throws InterruptedException {
        while (currentPortionsAmount == maxPortionsAmount)
            wait();

        putIndex = putIndex+1 % maxPortionsAmount;
        portionList[putIndex] = portion;
        currentPortionsAmount += 1;

        notify();
    }

    public synchronized Portion pop() throws InterruptedException {
        while (currentPortionsAmount == 0)
            wait();

        popIndex = popIndex+1 % maxPortionsAmount;
        currentPortionsAmount -= 1;
        Portion portion = portionList[popIndex];

        notify();

        return portion;
    }
}
