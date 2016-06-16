package ayudin.diplom.test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by AYUdin on 16.06.2016.
 */
public class Utils {

    static class Timer {
        private long startTime;

        public void startCount(){
            startTime = System.currentTimeMillis();
        }

        public long getPassedTime(){
            return System.currentTimeMillis() - startTime;
        }
    }

    private static Map<String, Timer> timers = new HashMap<String, Timer>();

    public static void setTimer(String timerName){
        if (timers.containsKey(timerName)){
            throw new RuntimeException("Name already in use");
        }
        Timer newTimer = new Timer();
        newTimer.startCount();
        timers.put(timerName, newTimer);
        System.out.println("Timer set: "+ timerName);
    }

    public static void printTime(String timerName){
        long time = timers.get(timerName).getPassedTime();
        System.out.println(timerName + ": time passed: " + time + " ms");
        timers.remove(timerName);
    }

    public static void generateLog(long linesCount, File realLogFile){



        Random random = new Random();
        BufferedReader reader;
        ArrayList<String> logLines = new ArrayList<String>();
        if (!realLogFile.exists()) throw new RuntimeException("No real log file!");
        try {
            reader = new BufferedReader(new FileReader(realLogFile));
            String line;
            while ((line = reader.readLine()) != null) {
                logLines.add(line);
            }
            reader.close();
        } catch (IOException e){
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter("GeneratedLog_" + linesCount + "_lines.log" );
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

        for (long c = 0; c < linesCount; c++){
            if (c % 1000 == 0) System.out.println( c * 100 / linesCount + "%");
            out.println(logLines.get(random.nextInt(logLines.size())));
        }
        out.close();
    }
}
