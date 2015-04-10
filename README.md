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