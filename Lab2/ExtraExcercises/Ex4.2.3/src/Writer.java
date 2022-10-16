public class Writer implements Runnable{

    Library library;

    public Writer(Library library) {
        this.library = library;
    }

    @Override
    public void run() {
        System.out.format("[%s] WRITER Starting...\n", Thread.currentThread());
        while (true) {
            try {
                library.startWriting();
                Thread.sleep(5000);
                library.stopWriting();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
