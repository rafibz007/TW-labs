import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        List<Writer> writers = List.of(
              new Writer(library),
              new Writer(library),
              new Writer(library)
        );

        List<Reader> readers = List.of(
              new Reader(library),
              new Reader(library),
              new Reader(library),
              new Reader(library),
              new Reader(library),
              new Reader(library),
              new Reader(library)
        );

        List<Thread> threads = Stream.of(
                        readers,
                        writers
                )
                .flatMap(Collection::stream)
                .map(Thread::new)
                .toList();

        threads.forEach(Thread::start);
    }
}