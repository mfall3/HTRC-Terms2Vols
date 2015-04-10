HTRC-Terms2Vols
==================

Reads keyword terms from a given list in a file, gets the ids for volumes containing those terms from HTRC's Solr Proxy AP, then writes the set of volume ids to a file.

Usage: java -jar HTRC-Terms2Vols.jar [endpoint] [outputFile] [keywordFile]

## arguments:

+ *endpoint* HTRC Solr Proxy API endpoint, default: http://chinkapin.pti.indiana.edu:9994/solr/ocr/select/

+ *outputFile* filename for output list of volume id, default: output.txt

+ *keywordFile* filename for input list of keyword terms, default: keywords.txt