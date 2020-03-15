import java.util.Arrays;
import java.util.List;

public class TFIDF_Calculator {

    /**
     * @param doc  list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public static double tf(List<String> doc, String term) {
        double result = 0.0;
        for (String word : doc) {

            if(term.toLowerCase().equals(word.toLowerCase()))
            {
                result++;
            }
        }
        return result / (double) doc.size();
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */

    public static double idf(List<List<String>> docs, String term) {
        double n = 0.0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if(term.toLowerCase().equals(word.toLowerCase()))
                {
                    n++;
                }
            }
        }

        return Math.log10(n/(double)docs.size());
    }

    /**
     * @param doc  a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public static double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        //System.out.println("tf score: " + tf(doc, term));
        //System.out.println("idf score: " + idf(docs, term));
        return tf(doc, term) * idf(docs, term);
    }

}
