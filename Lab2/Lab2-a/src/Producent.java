public class Producent implements Runnable{

    Bufor bufor;
    int val = 0;

    public Producent(Bufor bufor) {
        this.bufor = bufor;
    }

    private void produce() throws InterruptedException {
        System.out.println("PRODUCING");
        bufor.put(new Portion(val));
        System.out.println("Produced new Portion with value = " + val);
        val++;

        Thread.sleep(600);
    }

    @Override
    public void run() {
        while (true) {
            try {
                produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
