package ayudin.diplom;




import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;


import java.util.List;

/**
 * Created by AYUdin on 5/13/2016.
 */
public class XmlGenerator {

    //Уточнить
    private String queryName = "Query from Hibernate log";
    private String queryTypeConn = "1";
    //todo: homepath
    private String queryNameConn = "C:\\Users\\Oleg\\connection.ini";


    public String getXml(List<String> sqlStatements){
        Element querylist = new Element("querylist");

        for (String sqlStatement: sqlStatements){
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
}
