import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.util.*;

public class Main {
    public static void main(String args[]) throws IOException,ParseException
    {
        //The paths are assumed to be always the same
        String cobrFile = "common-group-data/got.cbor";

        //String deathFile = "common-group-data/character-deaths.csv";
        //String deathFile = "common-group-data/test_few_char.csv";

        //need to initialize this
        //String deathFile = "common-group-data/subset0.csv";

        String IndexDir = "OUTPUT";
        int total_pages = 0;
        String featuresFile = "common-group-data/characters_features.csv";

        //creating features file
        File file = new File(featuresFile);

        if (file.createNewFile()) {

            System.out.println("Features File Path " + featuresFile);
        } else {

            System.out.println("Features File exists in: " + featuresFile);
        }

        //setting up the analyzer
        Utilities.setAnalyzer(new StandardAnalyzer());

        //setting the file paths
        //Utilities.setPaths(cobrFile, deathFile, IndexDir, featuresFile);

        //adding stop words
        List<String> stopwords = Arrays.asList("0o", "0s", "3a", "3b", "3d", "6b", "6o", "a", "A", "a1", "a2",
            "a3", "a4", "ab", "able", "about", "above", "abst", "ac", "accordance",
            "according", "accordingly", "across", "act", "actually", "ad", "added", "adj",
            "ae", "af", "affected", "affecting", "after", "afterwards", "ag", "again", "against",
            "ah", "ain", "aj", "al", "all", "allow", "allows", "almost", "alone", "along",
            "already", "also", "although", "always", "am", "among", "amongst",
            "amoungst", "amount", "an", "and", "announce", "another", "any", "anybody", "anyhow", "anymore",
            "anyone", "anyway", "anyways", "anywhere", "ao", "ap", "apart", "apparently",
            "appreciate", "approximately", "ar", "are", "aren", "arent", "arise",
            "around", "as", "aside", "ask", "asking", "at", "au", "auth", "av", "available",
            "aw", "away", "awfully", "ax", "ay", "az", "b", "B", "b1", "b2", "b3", "ba", "back",
            "bc", "bd", "be", "became", "been", "before", "beforehand", "beginnings", "behind",
            "below", "beside", "besides", "best", "between", "beyond", "bi", "bill", "biol", "bj",
            "bk", "bl", "bn", "both", "bottom", "bp", "br", "brief", "briefly", "bs", "bt", "bu",
            "but", "bx", "by", "c", "C", "c1", "c2", "c3", "ca", "call", "came", "can", "cannot",
            "cant", "cc", "cd", "ce", "certain", "certainly", "cf", "cg", "ch", "ci", "cit", "cj",
            "cl", "clearly", "cm", "cn", "co", "com", "come", "comes", "con", "concerning",
            "consequently", "consider", "considering", "could", "couldn", "couldnt",
            "course", "cp", "cq", "cr", "cry", "cs", "ct", "cu", "cv", "cx", "cy", "cz", "d",
            "D", "d2", "da", "date", "dc", "dd", "de", "definitely", "describe", "described",
            "despite", "detail", "df", "di", "did", "didn", "dj", "dk", "dl", "do", "does",
            "doesn", "doing", "don", "done", "down", "downwards", "dp", "dr", "ds", "dt", "du",
            "due", "during", "dx", "dy", "e", "E", "e2", "e3", "ea", "each", "ec", "ed",
            "edu", "ee", "ef", "eg", "ei", "eight", "eighty", "either", "ej", "el", "eleven",
            "else", "elsewhere", "em", "en", "end", "ending", "enough", "entirely", "eo",
            "ep", "eq", "er", "es", "especially", "est", "et", "et-al", "etc", "eu", "ev",
            "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex",
            "exactly", "example", "except", "ey", "f", "F", "f2", "fa", "far", "fc", "few",
            "ff", "fi", "fifteen", "fifth", "fify", "fill", "find", "fire", "five", "fix",
            "fj", "fl", "fn", "fo", "followed", "following", "follows", "for", "former",
            "formerly", "forth", "forty", "found", "four", "fr", "from", "front", "fs", "ft",
            "fu", "full", "further", "furthermore", "fy", "g", "G", "ga", "gave", "ge", "get",
            "gets", "getting", "gi", "give", "given", "gives", "giving", "gj", "gl", "go",
            "goes", "going", "gone", "got", "gotten", "gr", "greetings", "gs", "gy", "h", "H",
            "h2", "h3", "had", "hadn", "happens", "hardly", "has", "hasn", "hasnt", "have",
            "haven", "having", "hed", "hello", "help", "hence", "here", "hereafter",
            "hereby", "herein", "heres", "hereupon", "hes", "hh", "hi", "hid", "hither",
            "hj", "ho", "hopefully", "how", "howbeit", "however", "hr", "hs", "http", "hu",
            "hundred", "hy", "i2", "i3", "i4", "i6", "i7", "i8", "ia", "ib", "ibid", "ic", "id", "ie", "if",
            "ig", "ignored", "ih", "ii", "ij", "il", "im", "immediately", "in", "inasmuch", "inc",
            "indeed", "index", "indicate", "indicated", "indicates", "information", "inner",
            "insofar", "instead", "interest", "into", "inward", "io", "ip", "iq", "ir", "is", "isn", "it",
            "itd", "its", "iv", "ix", "iy", "iz", "j", "J", "jj", "jr", "js", "jt", "ju", "just",
            "k", "K", "ke", "keep", "keeps", "kept", "kg", "kj", "km", "ko", "l", "L", "l2",
            "la", "largely", "last", "lately", "later", "latter", "latterly", "lb", "lc", "le",
            "least", "les", "less", "lest", "let", "lets", "lf", "like", "liked", "likely",
            "line", "little", "lj", "ll", "ln", "lo", "look", "looking", "looks", "los", "lr",
            "ls", "lt", "ltd", "m", "M", "m2", "ma", "made", "mainly", "make", "makes", "many",
            "may", "maybe", "me", "meantime", "meanwhile", "merely", "mg", "might",
            "mightn", "mill", "million", "mine", "miss", "ml", "mn", "mo", "more", "moreover",
            "most", "mostly", "move", "mr", "mrs", "ms", "mt", "mu", "much", "mug",
            "must", "mustn", "my", "n", "N", "n2", "na", "name", "namely", "nay", "nc",
            "nd", "ne", "near", "nearly", "necessarily", "neither", "nevertheless", "new",
            "next", "ng", "ni", "nine", "ninety", "nj", "nl", "nn", "no", "nobody",
            "non", "none", "nonetheless", "noone", "nor", "normally", "nos", "not",
            "noted", "novel", "now", "nowhere", "nr", "ns", "nt", "ny", "o", "O", "oa",
            "ob", "obtain", "obtained", "obviously", "oc", "od", "of", "off", "often",
            "og", "oh", "oi", "oj", "ok", "okay", "ol", "old", "om", "omitted", "on", "once",
            "one", "ones", "only", "onto", "oo", "op", "oq", "or", "ord", "os", "ot",
            "otherwise", "ou", "ought", "our", "out", "outside", "over", "overall", "ow",
            "owing", "own", "ox", "oz", "p", "P", "p1", "p2", "p3", "page", "pagecount",
            "pages", "par", "part", "particular", "particularly", "pas", "past", "pc", "pd",
            "pe", "per", "perhaps", "pf", "ph", "pi", "pj", "pk", "pl", "placed", "please",
            "plus", "pm", "pn", "po", "poorly", "pp", "pq", "pr",
            "predominantly", "presumably", "previously", "primarily", "probably",
            "promptly", "proud", "provides", "ps", "pt", "pu", "put", "py",
            "q", "Q", "qj", "qu", "que", "quickly", "quite", "qv", "r", "R", "r2", "ra",
            "ran", "rather", "rc", "rd", "re", "readily", "really", "reasonably",
            "recent", "recently", "ref", "refs", "regarding", "regardless", "regards", "related",
            "relatively", "research-articl", "respectively", "resulted", "resulting",
            "results", "rf", "rh", "ri", "right", "rj", "rl", "rm", "rn", "ro", "rq",
            "rr", "rs", "rt", "ru", "run", "rv", "ry", "s", "S", "s2", "sa", "said", "saw",
            "say", "saying", "says", "sc", "sd", "se", "sec", "second", "secondly",
            "section", "seem", "seemed", "seeming", "seems", "seen", "sent", "seven",
            "several", "sf", "shall", "shan", "shed", "show", "showed",
            "shown", "showns", "shows", "si", "side", "since", "sincere", "six", "sixty",
            "sj", "sl", "slightly", "sm", "sn", "so", "some", "somehow", "somethan",
            "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "sp",
            "specifically", "specified", "specify", "specifying", "sq", "sr", "ss",
            "st", "still", "stop", "strongly", "sub", "substantially", "successfully", "such",
            "sufficiently", "suggest", "sup", "sure", "sy", "sz", "t", "T", "t1", "t2",
            "t3", "take", "taken", "taking", "tb", "tc", "td", "te",
            "tell", "ten", "tends", "tf", "th", "than", "thank", "thanks", "thanx", "that",
            "thats", "the", "their", "theirs", "them",
            "themselves", "then", "thence", "there", "thereafter", "thereby", "thered",
            "therefore", "therein", "thereof", "therere",
            "theres", "thereto", "thereupon", "these", "they", "theyd", "theyre", "thickv",
            "thin", "think", "third", "this", "thorough",
            "thoroughly", "those", "thou", "though", "thoughh", "thousand", "three",
            "throug", "through", "throughout", "thru", "thus", "ti",
            "til", "tip", "tj", "tl", "tm", "tn", "to", "together", "too", "took", "top",
            "toward", "towards", "tp", "tq", "tr", "tried", "tries", "truly", "try",
            "trying", "ts", "tt", "tv", "twelve", "twenty", "twice", "two", "tx", "u", "U",
            "u201d", "ue", "ui", "uj", "uk", "um", "un", "under", "unfortunately", "unless",
            "unlike", "unlikely", "until", "unto", "uo", "up", "upon", "ups",
            "ur", "us", "used", "useful", "usefully", "usefulness", "using", "usually",
            "ut", "v", "V", "va", "various", "vd", "ve", "very", "via", "viz", "vj", "vo",
            "vol", "vols", "volumtype", "vq", "vs", "vt", "vu", "w", "W", "wa", "was", "wasn",
            "wasnt", "way", "we", "wed", "welcome", "well", "well-b", "went", "were", "weren",
            "werent", "what", "whatever", "whats", "when", "whence", "whenever", "where",
            "whereafter", "whereas", "whereby", "wherein", "wheres", "whereupon", "wherever",
            "whether", "which", "while", "whim", "whither", "who", "whod", "whoever", "whole",
            "whom", "whomever", "whos", "whose", "why", "wi", "widely", "with", "within",
            "without", "wo", "won", "wonder", "wont", "would", "wouldn", "wouldnt", "www",
            "x", "X", "x1", "x2", "x3", "xf", "xi", "xj", "xk", "xl", "xn", "xo", "xs", "xt",
            "xv", "xx", "y", "Y", "y2", "yes", "yet", "yj", "yl", "you", "youd",
            "your", "youre", "yours", "yr", "ys", "yt", "z", "Z", "zero", "zi", "zz");


        //setting up stopwords
        Utilities.setSTOPWORDS(stopwords);


        //checking to see if the paragraph indexes are created
        /*
        if(!Files.exists(Utilities.INDEX_DIRECTORY_PATH))
        {
            total_pages = CreateIndex.createIndex();
            Utilities.setTotal_pages(total_pages);
            System.out.println("total pages from main " + Utilities.totalPages);
            System.out.println("Page Indexes needs to be created ");
        }
        else
        {
            System.out.println("Index Already Created");
        }
        */
        //these needs to be called 10 times

        //setting up more features file
        /*
        String featuresFileGender = "common-group-data/characters_features_Gender.csv";
        String featuresFileDeath = "common-group-data/characters_features_Death.csv";
        String featuresFileNobility = "common-group-data/characters_features_Nobility.csv";
        String featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge.csv";

        //creating features file gender
        File file1 = new File(featuresFileGender);
        File file2 = new File(featuresFileDeath);
        File file3 = new File(featuresFileNobility);
        File file4 = new File(featuresFileNodeEdge);
        if (file1.createNewFile() && file2.createNewFile() && file3.createNewFile() && file4.createNewFile()) {

            System.out.println("All Features File Path " + featuresFileDeath);
        } else {

            System.out.println("All Features File exists in: " + featuresFileGender);
        }

        //initializing features file
        Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);
        */

        //calling
        /*
        try
        {
            SearchIndex.IndexSearcher();
        }
        catch(IOException ioe)
        {
            ioe.fillInStackTrace();
            System.out.println(ioe);
        }
         */

        for(int i = 0; i <10; i++) {

            int subset = i;
            String deathFile = "common-group-data/" + "subset" + Integer.toString(subset) + ".csv";

            //setting the file path
            Utilities.setPaths(cobrFile, deathFile, IndexDir, featuresFile);

            //checking to see if the paragraph indexes are created
            if(!Files.exists(Utilities.INDEX_DIRECTORY_PATH))
            {
                total_pages = CreateIndex.createIndex();
                Utilities.setTotal_pages(total_pages);
                System.out.println("total pages from main " + Utilities.totalPages);
                System.out.println("Page Indexes needs to be created ");
            }
            else
            {
                System.out.println("Index Already Created");
            }

            //flushing every single time
            String featuresFileGender = "";
            String featuresFileDeath = "";
            String featuresFileNobility = "";
            String featuresFileNodeEdge = "";

            //selecting the subset files from here
            switch (subset) {
                case 0:

                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file1 = new File(featuresFileGender);
                    File file2 = new File(featuresFileDeath);
                    File file3 = new File(featuresFileNobility);
                    File file4 = new File(featuresFileNodeEdge);
                    if (file1.createNewFile() && file2.createNewFile() && file3.createNewFile() && file4.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }

                    break;

                case 1:

                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file5 = new File(featuresFileGender);
                    File file6 = new File(featuresFileDeath);
                    File file7 = new File(featuresFileNobility);
                    File file8 = new File(featuresFileNodeEdge);
                    if (file5.createNewFile() && file6.createNewFile() && file7.createNewFile() && file8.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }

                    break;

                case 2:

                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file9 = new File(featuresFileGender);
                    File file10 = new File(featuresFileDeath);
                    File file11 = new File(featuresFileNobility);
                    File file12 = new File(featuresFileNodeEdge);
                    if (file9.createNewFile() && file10.createNewFile() && file11.createNewFile() && file12.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }

                    break;

                case 3:

                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file13 = new File(featuresFileGender);
                    File file14 = new File(featuresFileDeath);
                    File file15 = new File(featuresFileNobility);
                    File file16 = new File(featuresFileNodeEdge);
                    if (file13.createNewFile() && file14.createNewFile() && file15.createNewFile() && file16.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }

                    break;

                case 4:
                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file17 = new File(featuresFileGender);
                    File file18 = new File(featuresFileDeath);
                    File file19 = new File(featuresFileNobility);
                    File file20 = new File(featuresFileNodeEdge);
                    if (file17.createNewFile() && file18.createNewFile() && file19.createNewFile() && file20.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }
                    break;

                case 5:

                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file21 = new File(featuresFileGender);
                    File file22 = new File(featuresFileDeath);
                    File file23 = new File(featuresFileNobility);
                    File file24 = new File(featuresFileNodeEdge);
                    if (file21.createNewFile() && file22.createNewFile() && file23.createNewFile() && file24.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }

                    break;

                case 6:
                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file25 = new File(featuresFileGender);
                    File file26 = new File(featuresFileDeath);
                    File file27 = new File(featuresFileNobility);
                    File file28 = new File(featuresFileNodeEdge);
                    if (file25.createNewFile() && file26.createNewFile() && file27.createNewFile() && file28.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }
                    break;

                case 7:
                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file29 = new File(featuresFileGender);
                    File file30 = new File(featuresFileDeath);
                    File file31 = new File(featuresFileNobility);
                    File file32 = new File(featuresFileNodeEdge);
                    if (file29.createNewFile() && file30.createNewFile() && file31.createNewFile() && file32.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }
                    break;

                case 8:

                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file33 = new File(featuresFileGender);
                    File file34 = new File(featuresFileDeath);
                    File file35 = new File(featuresFileNobility);
                    File file36 = new File(featuresFileNodeEdge);
                    if (file33.createNewFile() && file34.createNewFile() && file35.createNewFile() && file36.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }

                    break;

                case 9:

                    //setting up more features file
                    featuresFileGender = "common-group-data/characters_features_Gender" + Integer.toString(subset) + ".csv";
                    featuresFileDeath = "common-group-data/characters_features_Death" + Integer.toString(subset) + ".csv" ;
                    featuresFileNobility = "common-group-data/characters_features_Nobility"+ Integer.toString(subset) + ".csv";
                    featuresFileNodeEdge = "common-group-data/characters_features_NodeEdge" + Integer.toString(subset)+ ".csv";

                    //creating features file gender
                    File file37 = new File(featuresFileGender);
                    File file38 = new File(featuresFileDeath);
                    File file39 = new File(featuresFileNobility);
                    File file40 = new File(featuresFileNodeEdge);
                    if (file37.createNewFile() && file38.createNewFile() && file39.createNewFile() && file40.createNewFile()) {

                        System.out.println("All Features File Path " + featuresFileDeath);
                    } else {

                        System.out.println("All Features File exists in: " + featuresFileGender);
                    }

                    //initializing features file
                    Utilities.setFeaturesFilePath(featuresFileGender, featuresFileDeath, featuresFileNobility , featuresFileNodeEdge);

                    //calling searcher
                    try
                    {
                        SearchIndex.IndexSearcher();
                    }
                    catch(IOException ioe)
                    {
                        ioe.fillInStackTrace();
                        System.out.println(ioe);
                    }

                    break;

                default:
                    break;
            }
        }
        System.out.println("--- DONE JAVA CALL --");
    }
}
