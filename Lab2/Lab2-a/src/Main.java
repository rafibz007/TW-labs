public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bufor bufor = new Bufor(1);
        Thread producerThread = new Thread(new Producent(bufor));
        Thread consumerThread = new Thread(new Consument(bufor));

        consumerThread.start();
        producerThread.start();

        producerThread.join();
        consumerThread.join();
    }
}