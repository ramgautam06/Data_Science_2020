import org.apache.lucene.analysis.Analyzer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Utilities {

    public static final String PageID = "PageId";
    public static final String PageContents = "text";

    public static Analyzer ANALYZER;

    public static Path INDEX_DIRECTORY_PATH;
    public static String COBOR_FILE;
    public static String DEATH_FILE;
    public static String FEATURES_FILE = "";

    public static int totalPages = 0;

    public static List<String> STOPWORDS  = new ArrayList<String>();

    //setting up files based on features
    public static String featuresFileGender;
    public static String featuresFileDeath;
    public static String featuresFileNobility;
    public static String featuresFileNodeEdge;

    public static void setPaths(String cborfile, String deathfile, String indexDirectoryPath, String features_file)
    {
        COBOR_FILE = cborfile;
        DEATH_FILE = deathfile;
        INDEX_DIRECTORY_PATH = FileSystems.getDefault().getPath(indexDirectoryPath, "Pages.lucene");
        FEATURES_FILE = features_file;
    }

    //setting up the analyzer
    public static void setAnalyzer(Analyzer analyzer){
        ANALYZER = analyzer;
    }

    //setting up total pages
    public static void setTotal_pages(int pages)
    {
        totalPages = pages;
    }

    //setting up stopwords
    public static void setSTOPWORDS(List<String> stopwords )
    {
        STOPWORDS = stopwords;
    }

    public static void setFeaturesFilePath (String genderFile, String deathFile, String nobilityFile, String nodeedgeFile)
    {

        featuresFileGender = genderFile;

        featuresFileDeath  = deathFile;

        featuresFileNobility = nobilityFile;

        featuresFileNodeEdge = nodeedgeFile;
    }
}
