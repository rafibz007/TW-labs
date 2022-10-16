public class Philosopher implements Runnable{

    Table table;
    int number;

    public Philosopher(Table table, int number) {
        this.table = table;
        this.number = number;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.format("[Philosopher %d] Trying to take forks...\n", number);
                table.takeForks(number);
                System.out.format("[Philosopher %d] Eating...\n", number);
                Thread.sleep(5000);
                System.out.format("[Philosopher %d] Returning forks...\n", number);
                table.returnForks(number);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
