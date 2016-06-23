package ayudin.diplom;




import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;


import java.util.List;

/**
 * Created by AYUdin on 5/13/2016.
 */
public class XmlGenerator {

    private String queryName = "Query from Hibernate log";
    private String queryTypeConn = "1";
    private String queryNameConn = System.getProperty("user.home") + System.getProperty("file.separator") + "connection.ini";


    public String getXml(List<String> sqlStatements){
        Element querylist = new Element("querylist");

        for (String sqlStatement: sqlStatements){
            queryName = makeQueryName(sqlStatement);
            Element query = new Element("query");

            Element command = new Element("command");
            command.addContent(sqlStatement);
            query.addContent(command);

            Element queryNameElement = new Element("name");
            queryNameElement.addContent(queryName);
            query.addContent(queryNameElement);

            Element queryTypeConnElement = new Element("typeconnection");
            queryTypeConnElement.addContent(queryTypeConn);
            query.addContent(queryTypeConnElement);

            Element queryNameConnElement = new Element("nameconnection");
            queryNameConnElement.addContent(queryNameConn);
            query.addContent(queryNameConnElement);

            querylist.addContent(query);

        }


        Document doc = new Document(querylist);
        return new XMLOutputter().outputString(doc);

    }

    private String makeQueryName(String query){
        //до первого пробела после 15-го символа
        return query.substring(0, query.indexOf(" ", 15));
    }
}
