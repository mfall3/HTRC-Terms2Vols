import org.apache.commons.io.FileUtils;

import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

public class Main {

    /**
     * Reads keyword terms from given list in file,
     * gets the ids for volumes containing those terms,
     * and writes them to a file.
     *
     *
     * Usage is: java -jar HTRC-Terms2Vols.jar [endpoint] [outputFile] [keywordFile]
     *
     * @param args
     *   arg[0] -> endpoint - HTRC Solr Proxy API endpoint, default: http://chinkapin.pti.indiana.edu:9994/solr/ocr/select/
     *   arg[1] -> outputFile - filename for output list of volume id, default: output.txt
     *   arg[2] -> keywordFile - filename for input list of keyword terms, default: keywords.txt
     */

    public static void main(String[] args) {

        String endpoint = "http://chinkapin.pti.indiana.edu:9994/solr/ocr/select/";
        String outputFile = "output.txt";
        String keywordFile = "keywords.txt";

        if (args.length > 0) endpoint = args[0];
        if (args.length > 1) outputFile = args[1];
        if (args.length > 2) keywordFile = args[2];

        SolrApiClient solrApiClient = new SolrApiClient(endpoint);
        File file = new File(outputFile);
        FileWriter fw = null;

        // if file does not exist,then create it
        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String rowsString;
        int batchNum = 0;
        int batchSize = 1000;
        int totalBatches = 0;

        HashSet<String> volSet = new HashSet<String>();
        List<String> batchVolList = new ArrayList<String>();
        String listString = "";
        List<String> keywords;
        List<String> queries = new ArrayList<String>();

        try {

            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            keywords = FileUtils.readLines(new File(keywordFile), "UTF-8");
            for (String keyword : keywords){
                queries.add("ocr:" + URLEncoder.encode("\"" + keyword + "\"", "UTF-8"));
            }

            for (String query : queries) {

                System.out.println("query url: " + endpoint + "?q=" + query + "&fl=id");
                int maxRows = solrApiClient.getCount(query);
                System.out.println("Total ids found: " + maxRows);


                totalBatches = 1 + maxRows / batchSize;

                for (int i = 0; i <= (maxRows + 1); i += batchSize) {

                    if (i == 0) {
                        rowsString = "&rows=" + (batchSize);
                        System.out.println("fetching up to the first " + batchSize + " ids ...");

                    } else  {
                        batchNum = i / batchSize;
                        rowsString = "&start=" + (batchSize * batchNum) + "&rows=" + (batchSize);
                        if ((batchNum % 10) == 0) {
                            System.out.println("fetching id " + (batchSize * batchNum) + " and up to the next " + batchSize + " ids ...");
                        }
                    }
                    //batchVolList.clear();
                    if ((batchNum % 10) == 0) {

                        listString = solrApiClient.getCommaSeparatedValueList((query + rowsString));
                        batchVolList = Arrays.asList(listString.split("\\s*,\\s*"));
                        for (String id : batchVolList) {
                            volSet.add(id);
                        }
                    }

                    System.out.println("completed batch " + (batchNum + 1) + " of " + totalBatches );
                }

                for (String volId : volSet) {
                    bw.write(volId);
                }
            }

            bw.close();
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            System.out.println("xpath trouble\n");
            e.printStackTrace();
        }

    }
}
