import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.*;
import java.util.*;

public class NobleClassifier {

    public static void nobleclassifer(Hashtable<String, List<String>> allDocsDict, List<List<String>> tokensList)
    {
        //assuming all the characters were noble
        int noble = 1;

        TFIDF_Calculator calculator = new TFIDF_Calculator();

        //List of Noble House
        //List<String> houseList = Arrays.asList("Stark", "Targaryen", "Lannister", "Baratheon",
                //"Tyrell", "Martell", "Tully");

        List<String> houseList = Arrays.asList ("stark", "arryn", "baratheon", "tully", "greyjoy",
            "lannister", "tyrell", "martell", "targaryen");

        double tfidfScore = 0.0;

        //getting all the sentences list here
        List<String> sentenceList = new ArrayList<String>();

        // getting keySet() into Set
        Set<String> setofCharacter = allDocsDict.keySet();

        int count_key = 0;
        int count_list = 0;

        // create FileWriter object with file as parameter
        try {

            FileWriter outputfile = new FileWriter(Utilities.featuresFileNobility);
            CSVWriter writer = new CSVWriter(outputfile);

            String[] header = { "Name", "Nobility"};
            writer.writeNext(header);

            // for-each loop
            for(String key : setofCharacter) {

                count_key = count_key + 1;
                //System.out.println(count_key + " charcater name: "  + key);

                //splitting the key into tokens
                List<String> KeyTokens = EntityCreation.TokenizeKey(key);

                sentenceList = allDocsDict.get(key);
                for (String sentence : sentenceList)
                {
                    //check if this sentence contains the our search key - example John

                    //System.out.println(count_list +  " "+ key + " " + sentence);
                    List<String> docList = new ArrayList<>();
                    String del = " ";
                    String [] docWords = sentence.split(del);

                    for (String term : docWords) {

                        //if it is present in stopwords - exclude it from docList
                        if (Utilities.STOPWORDS.contains(term)) {
                            // @ not including terms if not needed
                        } else {
                            docList.add(term.toLowerCase());
                        }

                        //docList.add(term.toLowerCase());
                        //System.out.println("term added: " + term);
                    }

                    //------
                    for(String keytoken:KeyTokens)
                    {
                        //computing the tfidf score only if Character is mentioned in sentence
                        if (docList.contains(keytoken.toLowerCase()))
                        {
                            //computing socre for all the males
                            for(String house : houseList)
                            {
                                tfidfScore = TFIDF_Calculator.tfIdf(docList, tokensList, house);
                                //System.out.println("tfidf score " + tfidfScore);

                                if(tfidfScore > 0)
                                {
                                    break;
                                }
                            }
                        }

                        if(tfidfScore > 0)
                        {
                            break;
                        }
                    }

                    //coming out loop as soon as tf-idf score is positive
                    if(tfidfScore > 0)
                    {
                        break;
                    }

                    count_list++;
                }

                //computing gender based on TF-IDF Score
                if (tfidfScore > 0)
                {
                    //System.out.println(key + ": female - " + femaleScore);
                    noble = 1;
                }
                else
                {
                    //System.out.println(key + " : male - " + maleScore);
                    noble = 0;
                }

                //writing into csv file after computing gender based on tf-idf score
                String[] record = {key , Integer.toString(noble)};
                writer.writeNext(record);

                //initialize score here
                tfidfScore = 0.0;

                //System.out.println("charcater : "  + key + "\n");
            }

            writer.close();
        }
        catch (IOException exe) {
            exe.printStackTrace();
        }
    }
}
