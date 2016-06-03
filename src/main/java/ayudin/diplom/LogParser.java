package ayudin.diplom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AYUdin on 5/13/2016.
 */
public class LogParser {

    public Map<String, List<List<String>>> getSqlQueries(File logfile){

        BufferedReader reader;
        ArrayList<String> logLines = new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader(logfile));
            String line;
            while ((line = reader.readLine()) != null) {
                logLines.add(line);
            }
            reader.close();
        } catch (IOException e){
            System.out.println("Невозможно прочитать файл логов!");
            System.exit(0);
        }

        Map<String, List<List<String>>> results = new HashMap();
        String queryRegexp = "(select|update|insert|delete) (\\w+\\.\\w+ \\*\\/) (.*)";
        String paramsRegexp = "binding parameter \\[\\d+\\] as \\[\\S+\\] - \\[(.+)\\]";
        Pattern queryPattern = Pattern.compile(queryRegexp);
        Pattern paramsPattern = Pattern.compile(paramsRegexp);

        Iterator<String> logLinesIterator = logLines.iterator();
        if (!logLinesIterator.hasNext()) throw new RuntimeException("Пустой файл логов!");
        String next = logLinesIterator.next();
        while (logLinesIterator.hasNext()) {
            Matcher queryMatcher = queryPattern.matcher(next);
            if (!queryMatcher.find()){
                next = logLinesIterator.next();
                continue;
            }
            ArrayList<String> params = new ArrayList<String>();
            String extractedQuery = queryMatcher.group(3);

            if (logLinesIterator.hasNext()) next = logLinesIterator.next();
            Matcher paramsMatcher = paramsPattern.matcher(next);
            while (paramsMatcher.find()){
                params.add(paramsMatcher.group(1));
                if (logLinesIterator.hasNext()){
                    next = logLinesIterator.next();
                    paramsMatcher = paramsPattern.matcher(next);
                } else{
                    break;
                }

            }
            if (results.containsKey(extractedQuery)){
                results.get(extractedQuery).add(params);
            } else{
                List<List<String>> paramsList = new ArrayList();
                paramsList.add(params);
                results.put(extractedQuery, paramsList);
            }

        }

        return results;
    }

}
