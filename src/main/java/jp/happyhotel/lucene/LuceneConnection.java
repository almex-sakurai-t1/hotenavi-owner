package jp.happyhotel.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import jp.happyhotel.common.Constants;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 * This class provides the functionality to connect to lucene indexes ans search for specific words
 * 
 * @author HCL Technologies Ltd.
 * 
 */

public class LuceneConnection extends IndexSearcher
{

    private static String indexDir = ""; // lucene indexes root location

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

    /**
     * Constructor
     * 
     * @param fsDir
     * @throws IOException
     */

    public LuceneConnection(Directory fsDir) throws IOException
    {
        super( fsDir );
    }

    public static LuceneConnection getConnection(String indexSubDir) throws IOException
    {
        LuceneConnection con = null;
        try
        {
            Directory fsDir = FSDirectory.getDirectory( new File( indexDir + "//" + indexSubDir ), false );
            con = new LuceneConnection( fsDir );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return con;
    }

    public static Hits executeQuery(LuceneConnection connection, String primaryKey)
            throws Exception
    {
        BooleanQuery.setMaxClauseCount( Integer.MAX_VALUE );
        Query query = new WildcardQuery( new Term( "primary", "*" + primaryKey + "*" ) );

        Hits hits = connection.search( query );

        return hits;
    }

    public static Hits executeTermQuery(LuceneConnection connection, String primaryKey)
            throws Exception
    {

        Query query = new TermQuery( new Term( "primary", primaryKey ) );

        Hits hits = connection.search( query );

        return hits;
    }

    public static Hits executeParsedQuery(LuceneConnection connection, String primaryKey)
            throws Exception
    {

        QueryParser QueryParser = new QueryParser( "primary", new StandardAnalyzer() );
        Query query = QueryParser.parse( primaryKey );
        Hits hits = connection.search( query );

        return hits;
    }

    public static void closeConnection(LuceneConnection connection)
    {
        try
        {
            if ( connection != null )
            {
                connection.close();
                connection = null;
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        try
        {
            LuceneConnection con = LuceneConnection.getConnection( LuceneDBConstants.HOTEL_BASIC_INDEX_DIR );
            Hits hits = null;
            long start = new Date().getTime();

            hits = LuceneConnection.executeQuery( con, "5400167" );

            long end = new Date().getTime();

            System.out.println( hits.length() + " records in " + (end - start) + " ms" );
            for( int j = 0 ; j < hits.length() ; j++ )
            {
                // ƒzƒeƒ‹î•ñ‚ÌŽæ“¾
                Document doc = hits.doc( j );
                System.out.println( "--->" + doc.get( "contents" ) );
            }
            LuceneConnection.closeConnection( con );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static String[] GetRecordWorked(String str)
    {
        StringTokenizer stringTokenizer = new StringTokenizer( str, "|" );
        int count = stringTokenizer.countTokens();
        String[] fields = new String[count];

        for( int i = 0 ; i < count ; i++ )
        {
            fields[i] = stringTokenizer.nextToken();
            if ( fields[i].equals( "null" ) )
                fields[i] = "";
            int pos = fields[i].indexOf( "##~#~##" );
            if ( pos != -1 )
            {
                if ( pos == 0 )
                    fields[i] = fields[i].substring( 8, fields[i].length() );
                else if ( pos == (fields[i].length() - 7) )
                    fields[i] = fields[i].substring( 0, (fields[i].length() - 8) );
                else
                    fields[i] = fields[i].substring( 0, (pos - 1) ) + "|" + fields[i].substring( pos + 7 );
            }
        }

        return fields;
    }

    public static String[] GetRecord(String str)
    {
        StringTokenizer stringTokenizer = new StringTokenizer( str, "|" );
        int count = stringTokenizer.countTokens();
        String[] fields = new String[count];

        for( int i = 0 ; i < count ; i++ )
        {
            fields[i] = stringTokenizer.nextToken();
        }

        return fields;
    }

}
