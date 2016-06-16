package ayudin.diplom.test;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by AYUdin on 16.06.2016.
 */
public class Utils {
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
            out = new PrintWriter("Generated.log");
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
