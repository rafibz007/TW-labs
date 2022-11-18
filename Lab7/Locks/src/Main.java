import config.Config;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<Integer> mainTaskTimes = List.of(
                0,
                500,
                1000,
                1500,
                2000
        );
        List<Integer> additionalTaskTimes = List.of(
                0,
                500,
                1000,
                1500,
                2000
        );
        List<Integer> pkAmounts = List.of(
                1,
                3,
                5
        );
        int repetitionsPefConfig = 3;
        for (Integer pkAmount: pkAmounts) {
            for (Integer mainTaskTime: mainTaskTimes) {
                Config.mainTaskTime = mainTaskTime;
                for (Integer additionalTaskTime: additionalTaskTimes) {
                    Config.additionalTaskTime = additionalTaskTime;

                    for (int i=0; i<repetitionsPefConfig; i++){
                        Config.amountOfAccesses.clear();
                        Config.accessTime.clear();
                        measure(pkAmount);
                    }
                }
            }
        }
    }
    public static void measure(int pkAmount) throws InterruptedException, IOException {
        int m = 500;
        Bufor bufor = new Bufor(m); // minimum maxAmount is 2*(max produce/consume amount) to prevent blocking

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

        Thread.sleep(20*1000);

        threads.forEach(Thread::stop);
        long end = System.currentTimeMillis();

        Map<String, Double> meanAccessTime = new HashMap<>(Map.copyOf(Config.accessTime));
        meanAccessTime.replaceAll((k, v) -> Config.accessTime.get(k) / Double.valueOf(Config.amountOfAccesses.get(k)));

        System.out.println(Config.accessTime);
        System.out.println(meanAccessTime);
        System.out.println(Config.amountOfAccesses);

        var accessTimeMean = meanAccessTime.values().stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        var accessesAmountMean = Config.amountOfAccesses.values().stream().mapToInt(Integer::intValue).average().orElse(Double.NaN);

        System.out.format("Total time: %d ms\n", end - start);
        System.out.format("Mean tasks time: %f ms\n", accessTimeMean);
        System.out.format("Mean amount of accesses: %f\n", accessesAmountMean);

//        save output to file
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(
                new String[] {
                        "LC",
                        String.valueOf(pkAmount),
                        String.valueOf(seed),
                        String.valueOf(end-start),
                        String.valueOf(Config.mainTaskTime),
                        String.valueOf(Config.additionalTaskTime),
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