import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.apache.lucene.search.TopDocs;

import java.util.List;
import java.util.*;

public class SearchIndex {
    public static void IndexSearcher() throws IOException, ParseException {

        //set up index searcher
        Directory indexDir = FSDirectory.open(Utilities.INDEX_DIRECTORY_PATH);
        IndexReader reader = DirectoryReader.open(indexDir);
        IndexSearcher searcher = new IndexSearcher(reader);

        String csvFile = Utilities.DEATH_FILE;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        //List<String> allDocContents = new ArrayList<String>();
        // creating a My HashTable Dictionary
        Hashtable<String, List<String>> allDocsDict = new Hashtable<String, List<String>>();
        List<String>listcontents= new ArrayList<String>();

        //more list define to pass in classifers list
        List<List<String>> tokensList = new ArrayList<List<String>>();
        List<String> wList = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            int lines_count = 0;
            while ((line = br.readLine()) != null) {

                //if (lines_count > 0)
                //{
                    // use comma as separator
                    String[] deaths_file = line.split(cvsSplitBy);
                    String searchString = deaths_file[0];

                    //System.out.println(lines_count + " searchString " + searchString);

                    //for  each loop starting here --
                    List<String> nameList = EntityCreation.EntityList(searchString);

                    for (String name: nameList) {
                        //System.out.println("Search String: "  + searchString);

                        QueryParser queryParser = new QueryParser(Utilities.PageContents, Utilities.ANALYZER);
                        //Query query = queryParser.parse(searchString);

                        Query query = queryParser.parse(name);
                        //TopDocs[] topDocs = searcher.search(query, totalHits);

                        //TopDocs topDocs = searcher.search(query, Utilities.totalPages);
                        TopDocs topDocs = searcher.search(query, 10);

                        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

                        //looking one document at a time ---
                        for (int i = 0; i < scoreDocs.length; i++) {
                            int docID = scoreDocs[i].doc;
                            Document doc = searcher.doc(docID);

                            //System.out.print((i+1) + ": " + "Score: " + scoreDocs[i].score + " PageID: " + doc.getField(Utilities.PageID).stringValue() + "PageContents: " + doc.getField(Utilities.PageContents).stringValue() + "\n");
                            //System.out.print((i+1) + " : " + "name: " + searchString + "  " + doc.getField(Utilities.PageID).stringValue() + " PageContents: " + doc.getField(Utilities.PageContents).stringValue() + "\n");

                            //allDocsDict.put(searchString, doc.getField(Utilities.PageContents).stringValue());
                            listcontents.add(doc.getField(Utilities.PageContents).stringValue());
                            //GenderClassifier.classifer(searchString, doc.getField(Utilities.PageContents).stringValue());

                            //get all the possible tokens here as List<List<String>>;
                            String toTokenize = doc.getField(Utilities.PageContents).stringValue();
                            String delimitter = " ";
                            String[] words = toTokenize.split(delimitter);

                            for (String t : words) {

                                // Add each element into the list - remove stop words RAM
                                if(Utilities.STOPWORDS.contains(t))
                                {}
                                else {
                                    wList.add(t);
                                }

                                //wList.add(t);
                            }
                            tokensList = Arrays.asList(wList);
                        }

                        //get the tokenizer
                        //class the classes with
                        allDocsDict.put(searchString, listcontents);
                    }
                    //for each search list ending here
                //}
                lines_count = lines_count + 1;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        //calling the classifier functions from here
        GenderClassifier.genderclassifer(allDocsDict, tokensList);

        //calling for death classifier function from here
        DeathClassifier.deathclassifer(allDocsDict, tokensList);

        //calling for nobility classifier
        NobleClassifier.nobleclassifer(allDocsDict, tokensList);

        //creating Node and Edge
        GraphNodeEdge.graphNodeEdge(allDocsDict, tokensList);
    }
}
