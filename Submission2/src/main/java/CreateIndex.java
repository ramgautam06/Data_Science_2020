import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Path;

public class CreateIndex {

    public static int createIndex() throws IOException {

        IndexWriter writer = null;
        Path indexLocation = Utilities.INDEX_DIRECTORY_PATH;

        try {

            FSDirectory dir = FSDirectory.open(indexLocation);
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            writer = new IndexWriter(dir, config);
        }
        catch (IOException e)
        {
            System.out.println("Index Writer Creation Failed "+e.getMessage());
            System.exit(10);
        }

        File fileStream = new File(Utilities.COBOR_FILE);
        InputStream inputStream = null;

        try
        {
            inputStream = new BufferedInputStream(new FileInputStream(fileStream));
        }
        catch (FileNotFoundException e) {
            System.out.println("Cannot open  Test Data Directory "+ e.getMessage());
            System.exit(1);
        }

        Iterable<Data.Page> iterableAnnotations = DeserializeData.iterableAnnotations(inputStream);

        int docCount = 0;
        int tot_pages = 0;

        for (Data.Page p : iterableAnnotations)
        {
            for (Data.Page.SectionPathParagraphs flatSectionPathsParagraphs : p.flatSectionPathsParagraphs()) {
                Document doc = new Document();
                doc.add(new StringField(Utilities.PageID, flatSectionPathsParagraphs.getParagraph().getParaId(), Field.Store.YES));
                doc.add(new TextField(Utilities.PageContents, flatSectionPathsParagraphs.getParagraph().getTextOnly(), Field.Store.YES));
                writer.addDocument(doc);
                docCount++;
            }
            if (docCount >= 1000) {
                writer.commit();
                //System.out.println("Committed " + docCount + " docs");
                docCount = 0;
            }

            tot_pages = tot_pages + 1;
        }
        writer.close();

        //System.out.println("Indexing Complete");
        //System.out.println("lucene index stored in: " + writer .getDirectory());
        //System.out.println("lucene index number of Docs: " + tot_pages);
        //System.out.println("lucene index field names: " + writer.getFieldNames());

        return tot_pages;
    }
}
