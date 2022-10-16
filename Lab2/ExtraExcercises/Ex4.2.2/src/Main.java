public class Main {
    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor(1);
        Thread producerThread = new Thread(new Producer(monitor));
        Thread producerThread2 = new Thread(new Producer(monitor));
        Thread consumerThread = new Thread(new Consumer(monitor));

        consumerThread.start();
        producerThread.start();
        producerThread2.start(); // second producer makes deadlock
    }
}