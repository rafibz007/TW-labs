import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int pkAmount = 10;
        int m = 500;
        Bufor bufor = new Bufor(m); // minimum maxAmount is 2*(max produce/consume amount) to prevent blocking
        Map<String, Long> cpuTime = new HashMap<>();

        int seed = 1000;
        Random generator = new Random(seed);

        List<Producer> producers = new ArrayList<>();
        for (int i=0; i<pkAmount; i++)
            producers.add(new Producer(bufor, generator, 1, m));

        List<Consumer> consumers = new ArrayList<>();
        for (int i=0; i<pkAmount; i++)
            consumers.add(new Consumer(bufor, generator, 1, m));


        List<Thread> producerThreads = producers
                .stream()
                .map(Thread::new)
                .toList();
        for (int i=0; i<producerThreads.size(); i++)
            producerThreads.get(i).setName("Producer-" + i);


        List<Thread> consumerThreads = consumers
                .stream()
                .map(Thread::new)
                .toList();
        for (int i=0; i<producerThreads.size(); i++)
            consumerThreads.get(i).setName("Consumer-" + i);


        List<Thread> threads = Stream.of(
                        consumerThreads,
                        producerThreads
                )
                .flatMap(Collection::stream)
                .toList();


        long start = System.currentTimeMillis();
        threads.forEach(Thread::start);

        Thread.sleep(10*1000);

        threads.forEach(thread -> {
            cpuTime.put(thread.getName(), cpuTime(thread)/1000000);
            thread.interrupt();
        });

        long end = System.currentTimeMillis();

        System.out.println(cpuTime);
        System.out.println(bufor.accessTime);
        System.out.println(bufor.amountOfAccesses);

        var cpuMean = cpuTime.values().stream().mapToLong(Long::longValue).average().orElse(Double.NaN);
        var accessTimeMean = bufor.accessTime.values().stream().mapToLong(Long::longValue).average().orElse(Double.NaN);
        var accessesAmountMean = bufor.amountOfAccesses.values().stream().mapToInt(Integer::intValue).average().orElse(Double.NaN);

        System.out.format("Total time: %d ms\n", end - start);
        System.out.format("Mean CPU time: %f ms\n", cpuMean);
        System.out.format("Mean access time: %f ms\n", accessTimeMean);
        System.out.format("Mean amount of accesses: %f\n", accessesAmountMean);

//        save output to file
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(
                new String[] {
                        "C",
                        String.valueOf(pkAmount),
                        String.valueOf(seed),
                        String.valueOf(end-start),
                        String.valueOf(cpuMean),
                        String.valueOf(accessTimeMean),
                        String.valueOf(accessesAmountMean)
                }
        );

        File csvOutputFile = new File("output.csv");
        try (
                FileWriter fr = new FileWriter(csvOutputFile, true);
                BufferedWriter br = new BufferedWriter(fr);
                PrintWriter pr = new PrintWriter(br);
                ) {
            dataLines.stream()
                    .map(Main::convertToCSV)
                    .forEach(pr::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static long cpuTime(Thread thr) {
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        if (mxBean.isThreadCpuTimeSupported()) {
            try {
                return mxBean.getThreadCpuTime(thr.getId());
            } catch (UnsupportedOperationException e) {
                System.out.println(e.toString());
            }
        } else {
            System.out.println("Not supported");
        }
        return 0;
    }

    public static String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(Main::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}