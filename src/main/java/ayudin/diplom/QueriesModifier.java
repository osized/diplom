package ayudin.diplom;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AYUdin on 5/13/2016.
 */
public class QueriesModifier {

    public List<String> getModifiedQueries(Map<String, List<List<String>>> queryToParameters){
        List<String> results = new ArrayList<String>();
        Map<String, List<List<String>>> queryToGroupedParams = new HashMap<String, List<List<String>>>();
        for (Map.Entry<String, List<List<String>>> entry: queryToParameters.entrySet()){
            queryToGroupedParams.put(entry.getKey(),
                    groupParams(entry.getValue()));
        }

        for (String query: queryToGroupedParams.keySet()){
            results.add(addParams(query, queryToGroupedParams.get(query)));
        }
        return results;
    }

    private List<List<String>> groupParams(List<List<String>> paramsLists){
        List<List<String>> results = new ArrayList<List<String>>();
        for (List<String> paramList: paramsLists){
            for (int i = 0; i<paramList.size(); i++){
                if (results.size()==i) results.add(i, new ArrayList<String>());
                results.get(i).add(paramList.get(i));
            }
        }
        return results;
    }

    private String addParams(String query, List<List<String>> groupedParams){
        int paramCount = query.length() - query.replace("?", "").length();
        if (groupedParams.size() != paramCount) throw new RuntimeException("Wrong param count!");
        String result = query;
        for (List<String> paramsGroup: groupedParams){
            //todo: тут сделать алгоритм подстановки параметров. Пока подставляется первый параметр из группы
            result.replaceFirst("\\?", paramsGroup.get(0));
        }
        return result;
    }
}
