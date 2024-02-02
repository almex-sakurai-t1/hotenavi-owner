package jp.happyhotel.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import jp.happyhotel.common.Constants;

import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

public class Indexer
{

    private static String      indexDir = "";

    private static IndexWriter writer2  = null;

    static
    {
        try
        {
            Properties prop;
            FileInputStream propfile = new FileInputStream( Constants.configFilesPath + "lucene.conf" );
            prop = new Properties();
            prop.load( propfile );
            indexDir = prop.getProperty( "index.root.windows" );
            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            System.out.println( "Lucene Indexer Static Block Error=" + e.toString() );
        }
    }

    public static void indexRecordAnalyzed(String pk, String data) throws IOException
    {
        Document doc = new Document();
        Field f1 = new Field( "contents", data, Field.Store.YES, Field.Index.NO );
        Field f2 = new Field( "primary", pk, Field.Store.YES, Field.Index.TOKENIZED );
        doc.add( f1 );
        doc.add( f2 );
        writer2.addDocument( doc );

    }

    public static void indexRecord(String pk, String data) throws IOException
    {
        Document doc = new Document();
        Field f1 = new Field( "contents", data, Field.Store.YES, Field.Index.NO );
        Field f2 = new Field( "primary", pk, Field.Store.NO, Field.Index.UN_TOKENIZED );
        doc.add( f1 );
        doc.add( f2 );
        writer2.addDocument( doc );

    }

    public static void initializeIndexer(String indexSubPAth)
    {
        try
        {
            System.out.println( "IDXROOT - " + indexDir + "//" + indexSubPAth );
            writer2 = new IndexWriter( new File( indexDir + "/" + indexSubPAth ), new CJKAnalyzer(), true );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static void CloseIndexer()
    {
        try
        {
            writer2.optimize();
            writer2.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception
    {

        String data = "CHINKU";
        initializeIndexer( "TESTCJK" );
        for( int i = 1 ; i <= 100 ; i++ )
        {

            data = "PK" + i + "   DK" + i + "   オスス" + i + "   MK" + i + " \n";
            if ( (i % 2) == 0 )
                indexRecord( "オスス" + i, data );
            else
                indexRecord( "PK" + i, data );

            System.out.println( i + " Records Indexed" );
        }
        CloseIndexer();
    }

}
