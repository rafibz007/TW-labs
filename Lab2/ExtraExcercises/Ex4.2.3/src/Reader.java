public class Reader implements Runnable{

    Library library;

    public Reader(Library library) {
        this.library = library;
    }

    @Override
    public void run() {
        System.out.format("[%s] READER Starting...\n", Thread.currentThread());
        while (true) {
            try {
                library.startReading();
                Thread.sleep(3000);
                library.stopReading();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
