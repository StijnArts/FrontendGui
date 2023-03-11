package stijn.dev.threads;

import java.util.*;

public class ThreadManager {
    private static HashMap<Thread,Boolean> parsingThreads= new HashMap<>();
    public static void waitForParsingThreads(){
        double startTime = System.currentTimeMillis();
        boolean anyThreadsAlive = parsingThreads.containsValue(true);
        while(anyThreadsAlive) {
            for (Thread thread : parsingThreads.keySet()) {
                parsingThreads.put(thread,thread.isAlive());
                anyThreadsAlive = parsingThreads.containsValue(true);
            }
            double timePassed = System.currentTimeMillis();
            if(timePassed-startTime>1000){
                startTime = System.currentTimeMillis();
                System.out.println("Still Parsing...");
            }
        }
    }

    public static void  addParsingThread(Thread thread){
        parsingThreads.put(thread,true);
    }

    public static HashMap<Thread,Boolean> getParsingThreads() {
        return parsingThreads;
    }
}
