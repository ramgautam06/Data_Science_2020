import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.*;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GenderClassifier {

    public static void genderclassifer(Hashtable<String, List<String>> allDocsDict, List<List<String>> tokensList)
    {
        //assuming all the genders are female
        int gender = 0;

        //System.out.println("genderClassifer Called");
        TFIDF_Calculator calculator = new TFIDF_Calculator();

        //male identifier
        //List<String> maleList = Arrays.asList("he", "him", "man");
        List<String> maleList = Arrays.asList("he");
        double maleScore = 0.0;

        //female identifier
        //List<String> femaleList = Arrays.asList("she", "her", "woman");
        List<String> femaleList = Arrays.asList("she");
        double femaleScore = 0.0;

        //getting all the sentences list here
        List<String> sentenceList = new ArrayList<String>();

        // getting keySet() into Set
        Set<String> setofCharacter = allDocsDict.keySet();

        int count_key = 0;
        int count_list = 0;

        // create FileWriter object with file as parameter
        try {

            //FileWriter outputfile = new FileWriter(Utilities.FEATURES_FILE);
            FileWriter outputfile = new FileWriter(Utilities.featuresFileGender);

            CSVWriter writer = new CSVWriter(outputfile);

            String[] header = { "Name", "Gender"};
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
                        if(Utilities.STOPWORDS.contains(term))
                        {
                        }
                        else
                        {
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
                            for(String male : maleList)
                            {
                                double mscore = TFIDF_Calculator.tfIdf(docList, tokensList, male);
                                if (mscore > 0)
                                {
                                    //System.out.println("tfidf score " + mscore);
                                    maleScore = maleScore + mscore;
                                }
                            }

                            //computign score for all the female
                            for(String female : femaleList)
                            {
                                double fscore = TFIDF_Calculator.tfIdf(docList, tokensList, female);
                                if (fscore > 0)
                                {
                                    //System.out.println("tfidf score " + fscore);
                                    femaleScore = femaleScore + fscore;
                                }
                            }
                        }
                    }

                    count_list++;
                }

                //computing gender based on TF-IDF Score
                if (femaleScore >= maleScore)
                {
                    //System.out.println(key + ": female - " + femaleScore);
                    gender = 0;
                }
                else
                {
                    //System.out.println(key + " : male - " + maleScore);
                    gender = 1;
                }

                //writing into csv file after computing gender based on tf-idf score
                String[] record = {key , Integer.toString(gender)};
                writer.writeNext(record);

                //initialize score here
                maleScore = 0.0;
                femaleScore = 0.0;
                //System.out.println("charcater : "  + key + "\n");
            }

            writer.close();
        }
        catch (IOException exe) {
            exe.printStackTrace();
        }
    }
}
