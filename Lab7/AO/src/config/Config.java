package config;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final Map<String, String> env = System.getenv();
    private static final int additionalTaskTimeDefault = 500;
    private static final int mainTaskTimeDefault = 500;


    public static int additionalTaskTime = Integer.parseInt(env.getOrDefault("ADDITIONAL_TASK_TIME", String.valueOf(additionalTaskTimeDefault)));
    public static int mainTaskTime = Integer.parseInt(env.getOrDefault("MAIN_TASK_TIME", String.valueOf(mainTaskTimeDefault)));;

    public static final Map<String, Integer> amountOfAccesses = new HashMap<>();
    public static final Map<String, Double> accessTime = new HashMap<>();
}
