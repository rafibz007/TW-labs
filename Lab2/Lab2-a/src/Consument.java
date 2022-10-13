public class Consument implements Runnable{

    Bufor bufor;

    public Consument(Bufor bufor) {
        this.bufor = bufor;
    }

    private void consume() throws InterruptedException {
        System.out.println("CONSUMING");
        Portion portion = bufor.pop();
        System.out.println("Consumed Portion with value = " + portion.value);

        Thread.sleep(1000);
    }

    @Override
    public void run() {
        while (true) {
            try {
                consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
