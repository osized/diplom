package ayudin.diplom;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AYUdin on 5/13/2016.
 */
public class QueriesModifier {

    public List<String> getModifiedQueries(Map<String, List<List<String>>> queryToParameters){
        List<String> results = new ArrayList<String>();
        for (Map.Entry<String, List<List<String>>> entry: queryToParameters.entrySet()){
            String modifiedQuery = addParameters(entry.getKey(), entry.getValue());
            results.add(modifiedQuery);
        }
        return results;
    }

    private String addParameters(String query, List<List<String>> parameters){
        Map<Integer, List<String>> paramsAtPos;
        int paramIndex = 0;
        for (List<String> paramsGroup: parameters){
            for (String param: paramsGroup){
                //groupedParams.add(paramIndex, param);
            }
        }

        int paramCount = query.length() - query.replace("?", "").length();

        if (parameters.size() != paramCount) throw new RuntimeException("Wrong param count!");
        for (List<String> parameterGroup: parameters) {
            //query = query.replaceFirst("\\?", parameter);
        }
        return "";
    }
}
