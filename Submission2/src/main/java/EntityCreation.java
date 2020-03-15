import java.util.ArrayList;
import java.util.List;

public class EntityCreation {

    public static List<String> EntityList(String fullname)
    {
        List<String> nameList = new ArrayList<>();
        nameList.add(fullname); //adding full name

        String delimitter = " ";
        String [] words = fullname.split(delimitter);

        nameList.add(words[0]); //

        //for (String word : words) {

            //nameList.add(word); //
            //nameList.add(word.toLowerCase()); // adding the lower case here
            //System.out.println(word + " its lower case " + word.toLowerCase());
        //}

        //
        /*
        for(int i = 0; i < nameList.size(); i++) {
            System.out.println(nameList.get(i));
        }
        */

        return nameList;
    }

    public static List<String> TokenizeKey(String fullname)
    {
        List<String> KeyTokens = new ArrayList<>();
        String delimitter = " ";
        String [] words = fullname.split(delimitter);

        //removing stop words while computing tf-idf
        if(Utilities.STOPWORDS.contains(words[0]))
        {
            if(Utilities.STOPWORDS.contains(words[1])) {

                if(words.length > 2)
                {
                    KeyTokens.add(words[2]);
                }
                else
                {
                    KeyTokens.add(words[1]);
                }
            }
            else
            {
                if (words.length > 1)
                {
                    KeyTokens.add(words[1]);
                }
                else
                {
                    KeyTokens.add(words[0]);
                }
            }
        }
        else
        {
            KeyTokens.add(words[0]);
        }

        //KeyTokens.add(words[0]);
        //for (String word : words) {

            //KeyTokens.add(word);
        //}

        return KeyTokens;
    }
}
