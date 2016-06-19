package ayudin.diplom;


import ayudin.diplom.test.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AYUdin on 5/13/2016.
 */
public class QueriesModifier {

    public List<String> getModifiedQueries(Map<String, List<List<String>>> queryToParameters){
        Utils.setTimer("getModifiedQueries");
        List<String> results = new ArrayList<String>();
        Map<String, List<List<String>>> queryToGroupedParams = new HashMap<String, List<List<String>>>();
        for (Map.Entry<String, List<List<String>>> entry: queryToParameters.entrySet()){
            queryToGroupedParams.put(entry.getKey(),
                    groupParams(entry.getValue()));
        }

        for (String query: queryToGroupedParams.keySet()){
            results.add(addParams(query, queryToGroupedParams.get(query)));
        }
        Utils.printTime("getModifiedQueries");
        return results;
    }

    private List<List<String>> groupParams(List<List<String>> paramsLists){
        Utils.setTimer("groupParams");
        List<List<String>> results = new ArrayList<List<String>>();
        for (List<String> paramList: paramsLists){
            for (int i = 0; i<paramList.size(); i++){
                if (results.size()==i) results.add(i, new ArrayList<String>());
                results.get(i).add(paramList.get(i));
            }
        }
        Utils.printTime("groupParams");
        return results;
    }

    private String addParams(String query, List<List<String>> groupedParams){
        int paramCount = query.length() - query.replace("?", "").length();

        //Если число групп не равно числу параметров - ничего не подставлять
        if (groupedParams.size() != paramCount || paramCount == 0) return query;

        String result = query;
        int paramsInFirstGroup = groupedParams.get(0).size();
        for (List<String> paramsGroup: groupedParams){
            //Если в группах разное число параметров - ничего не подставлять
            if (paramsGroup.size() != paramsInFirstGroup) return query;

            Pattern intPattern = Pattern.compile("\\A(\\d+)\\Z");
            if (intPattern.matcher(paramsGroup.get(0)).find()){
                Integer min = Integer.MAX_VALUE;
                Integer max = Integer.MIN_VALUE;
                for (String param: paramsGroup){
                    Integer intParam = 0;
                    try {
                        intParam = Integer.parseInt(param);
                    } catch (NumberFormatException e){
                        //Не все параметры в группе инты - ничего не подставляем
                        return query;
                    }
                    if (min > intParam) min = intParam;
                    if (max < intParam) max = intParam;
                }
                result = result.replaceFirst("\\?", "@int" + min + "-" + max);

            } else{
                //// TODO: 05.06.16
                result = result.replaceFirst("\\?","RandomString");
            }
        }
        return result;
    }
}
