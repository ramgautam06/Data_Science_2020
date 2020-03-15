import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GraphNodeEdge {

    //setting up local lookup function for the intersection of the set
    private static Set<String> lookUpSet(Set<String> setofCharacter)
    {
        Set<String> checkset = new HashSet<String>();

        for(String s1: setofCharacter)
        {
            String check = "";
            String [] tokens = s1.split(" ");

            check = tokens[0].toLowerCase();

            if(Utilities.STOPWORDS.contains(check))
            {
                if (s1.length()>1)
                {
                    check = tokens[1].toLowerCase();
                    checkset.add(check);
                }
            }
            else
            {
                checkset.add(check);
            }
        }

        return checkset;
    }

    //main class starts from here
    public static void graphNodeEdge(Hashtable<String, List<String>> allDocsDict, List<List<String>> tokensList)
    {
        System.out.println("Starting to create Node Edge");
        //getting all the sentences list here
        List<String> sentenceList = new ArrayList<String>();

        // getting keySet() into Set
        Set<String> setofCharacter = allDocsDict.keySet();

        //List<String>edgeList = new ArrayList<>();

        Set<String> edgeSet = new HashSet<String>();

        String line_out = "";
        String name = "";
        String node = "";

        int count_key = 0;
        int count_list = 0;

        // create FileWriter object with file as parameter
        try {

            FileWriter outputfile = new FileWriter(Utilities.featuresFileNodeEdge);
            CSVWriter writer = new CSVWriter(outputfile);

            String[] header = {"Name", "Edge List"};
            writer.writeNext(header);

            // for-each loop
            for (String key : setofCharacter) {

                count_key = count_key + 1;
                //System.out.println(count_key + " charcater name: "  + key);

                sentenceList = allDocsDict.get(key);
                for (String sentence : sentenceList) {

                    //setting up Check Set for each sentence
                    Set<String> CheckSet = new HashSet<String>();
                    CheckSet = lookUpSet(setofCharacter);

                    //check if this sentence contains the our search key - example John
                    //System.out.println(count_list +  " "+ key + " " + sentence);

                    List<String> docList = new ArrayList<>();
                    String del = " ";
                    String[] docWords = sentence.split(del);

                    Set<String> hash_Set = new HashSet<String>();

                    for (String term : docWords) {
                        //if it is present in stopwords - exclude it from docList
                        if (Utilities.STOPWORDS.contains(term)) {
                        } else {
                            //System.out.println(term.toLowerCase());
                            docList.add(term.toLowerCase());
                            hash_Set.add(term.toLowerCase());
                        }
                        //docList.add(term.toLowerCase());
                        //System.out.println("term added: " + term);
                    }

                    CheckSet.retainAll(hash_Set);

                    //if check set contains Character then only add to the edgeSet
                    //this means if the sentence we are looking into mentions character name
                    List<String> KeyTokens = EntityCreation.TokenizeKey(key);
                    for(String keytoken:KeyTokens)
                    {
                        if(docList.contains(keytoken.toLowerCase()))
                        {
                            //adding this to the edge list
                            for (String c: CheckSet)
                            {
                                edgeSet.add(c);
                            }
                        }
                    }

                    count_list++;

                } //finishing all the sections of the pages

                //setting up line_out

                Iterator<String> itr = edgeSet.iterator();
                int count = 0;
                while (itr.hasNext())
                {
                    line_out = line_out + "(" + key + "-" + itr.next() + ")";
                    count++;
                }

                //System.out.println(count_key + "total edges " + count);

                //need to flush edge set here word

                /*
                if(count_key == 1)
                {
                    break;
                }
                */
                name = key;
                header[0] = name;
                header[1] = line_out;
                writer.writeNext(header);

                line_out = "";
                edgeSet.clear();
            }
            writer.close();

        }
        catch (IOException exe) {
            exe.printStackTrace();
        }
    }
}
