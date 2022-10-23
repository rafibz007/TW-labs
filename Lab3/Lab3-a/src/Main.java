public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bufor bufor = new Bufor(3);
        Thread producerThread = new Thread(new Producer(bufor));
        Thread producerThread2 = new Thread(new Producer(bufor));
        Thread consumerThread = new Thread(new Consumer(bufor));

        consumerThread.start();
        producerThread.start();
        producerThread2.start();
    }
}