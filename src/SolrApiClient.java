import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SolrApiClient {

    private String endpoint;

    private DocumentBuilderFactory docFactory;

    public SolrApiClient(String endpoint) {
        this.endpoint = endpoint;
        this.docFactory = DocumentBuilderFactory.newInstance();
    }

    private Document getResponseDoc(String query) throws IOException {

        Document doc = null;

        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            String charset = "UTF-8";
            URL url = new URL(this.endpoint + query);
            URLConnection connection =url.openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();
            doc = docBuilder.parse(response);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public String getCommaSeparatedValueList(String query) throws IOException, XPathExpressionException {

        String returnString = "";
        Document doc = getResponseDoc("?q=" + query + "&fl=id");
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList)xPath.evaluate("//str[@name = 'id']", doc.getDocumentElement(), XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element e = (Element) nodes.item(i);
            returnString += e.getTextContent() + "\n";
        }

        return returnString;
    }

    public int getCount(String query) {
        String returnString = "0";

        try {
            Document doc = getResponseDoc("?q=" + query + "&fl=id");
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodes = (NodeList)xPath.evaluate("//result", doc.getDocumentElement(), XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); ++i) {
                Element e = (Element) nodes.item(i);
                returnString = e.getAttribute("numFound");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(returnString);
    }

}
