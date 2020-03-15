import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.*;
import java.util.*;

public class DeathClassifier {

    public static void deathclassifer(Hashtable<String, List<String>> allDocsDict, List<List<String>> tokensList)
    {
        //assuming all the genders are female
        int dead = 0;

        //System.out.println("genderClassifer Called");
        TFIDF_Calculator calculator = new TFIDF_Calculator();

        //death identifier
        //List<String> deathList = Arrays.asList("die");
        List<String> deathList = Arrays.asList("die", "kill", "died", "death", "demise", "killed");
        double deathScore = 0.0;

        //alive identifier
        //List<String> lifeList = Arrays.asList("alive");
        List<String> lifeList = Arrays.asList("alive", "life", "survive", "survived", "live");
        double aliveScore = 0.0;

        //getting all the sentences list here
        List<String> sentenceList = new ArrayList<String>();

        // getting keySet() into Set
        Set<String> setofCharacter = allDocsDict.keySet();

        int count_key = 0;
        int count_list = 0;

        int count_dead = 0;
        int count_alive = 0;
        // create FileWriter object with file as parameter
        try {

            FileWriter outputfile = new FileWriter(Utilities.featuresFileDeath);
            CSVWriter writer = new CSVWriter(outputfile);

            String[] header = {"Name", "dead_alive"};
            writer.writeNext(header);

            // for-each loop
            for (String key : setofCharacter) {

                count_key = count_key + 1;
                //System.out.println(count_key + " charcater name: "  + key);

                //splitting the key into tokens
                List<String> KeyTokens = EntityCreation.TokenizeKey(key);

                sentenceList = allDocsDict.get(key);
                for (String sentence : sentenceList) {
                    //check if this sentence contains the our search key - example John

                    //System.out.println(count_list +  " "+ key + " " + sentence);
                    List<String> docList = new ArrayList<>();
                    String del = " ";
                    String[] docWords = sentence.split(del);

                    for (String term : docWords) {

                        //if it is present in stopwords - exclude it from docList
                        if (Utilities.STOPWORDS.contains(term)) {
                        } else {
                            docList.add(term.toLowerCase());
                        }
                        //docList.add(term.toLowerCase());
                        //System.out.println("term added: " + term);
                    }

                    //------
                    for (String keytoken : KeyTokens) {
                        //computing the tfidf score only if Character is mentioned in sentence
                        if (docList.contains(keytoken.toLowerCase())) {
                            //computing socre for all the males
                            for (String die : deathList) {
                                double dscore = TFIDF_Calculator.tfIdf(docList, tokensList, die);
                                if (dscore > 0) {
                                    //System.out.println("tfidf score " + mscore);
                                    deathScore = deathScore + dscore;
                                }
                            }

                            //computign score for all the female
                            for (String alive : lifeList) {
                                double ascore = TFIDF_Calculator.tfIdf(docList, tokensList, alive);
                                if (ascore > 0) {
                                    //System.out.println("tfidf score " + fscore);
                                    aliveScore = aliveScore + ascore;
                                }
                            }
                        }
                    }
                    count_list++;
                }

                //computing gender based on TF-IDF Score
                if (deathScore >= aliveScore) {
                    //System.out.println(key + ": female - " + femaleScore);
                    dead = 0;
                    count_dead = count_dead + 1;

                    //if the 100 characters are found dead, force one to live
                    if ((count_dead%100) == 0)
                    {
                        dead = 1;
                        //System.out.println("I got in ");
                    }

                    //if all the characters are found dead, force one to live
                    if ((setofCharacter.size()-2) == count_dead)
                    {
                        dead = 1;
                        //System.out.println("I got in ");
                    }


                } else {
                    //System.out.println(key + " : male - " + maleScore);
                    dead = 1;
                    count_alive = count_alive + 1;
                }


                //writing into csv file after computing gender based on tf-idf score
                String[] record = {key, Integer.toString(dead)};

                writer.writeNext(record);

                //initialize score here
                deathScore = 0.0;
                aliveScore = 0.0;
                //System.out.println("charcater : "  + key + "\n");
            }
            writer.close();

            System.out.println("total dead count " + count_dead);
        }
        catch (IOException exe) {
            exe.printStackTrace();
        }
    }
}
