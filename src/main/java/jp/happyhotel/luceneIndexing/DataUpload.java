package jp.happyhotel.luceneIndexing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

/**
 * This class fetch data from database and loads data to lucene indexes
 * 
 * @author HCL Technologies Ltd.
 * 
 */

public class DataUpload
{
    static Connection          con              = null; // Database connection
    static Statement           stmt             = null;
    static ResultSet           rs               = null;
    static Connection          innerCon         = null; // Inner Database connection for fetching data under enclosing live connection
    static Statement           innerStmt        = null;
    static ResultSet           innerRs          = null;
    static String              DB_URL;                 // URL for database server
    static String              user;                   // DB user
    static String              password;               // DB password
    static String              indexSubPath;           // Index subpath for specific query

    static String              data             = null;
    static FileOutputStream    fileOutputStream = null;
    static ResultSetMetaData   rsmd             = null;

    static byte[]              image            = null;
    static String              imagePath        = null; // Images root path
    static String              noimagePath      = null;
    static String              pKey             = null; // Primary Key (keyword) for indexes
    static String              pKey2            = null;
    private static String      indexDirectory   = "";  // lucene indexes root directory
    private static IndexWriter writer2          = null; // index writer object

    // Finds from configuration files, for database connectivity URL, user and password.
    // it also sets the images root and lucene index root

    static
    {
        try
        {
            String configurationPath = "/etc/happyhotel/lucene.conf";

            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
                configurationPath = "c:/etc/happyhotel/lucene.conf";

            FileInputStream propfile = new FileInputStream( configurationPath );
            Properties prop = new Properties();
            prop.load( propfile );
            DB_URL = "jdbc:mysql://" + prop.getProperty( "databaseserver.ip" ) + ":3306/" + prop.getProperty( "database.name" );
            user = prop.getProperty( "database.user" );
            password = prop.getProperty( "user.password" );
            imagePath = prop.getProperty( "images.root.windows" );
            noimagePath = prop.getProperty( "noimages.root.windows" );
            indexDirectory = prop.getProperty( "index.root.windows" );
            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            System.out.println( "Lucene DATALOAD Static Block Error=" + e.toString() );
        }
    }

    /**
     * This function executes the query and pass the ResultSet for Indexing
     * 
     * @param query
     * @param idxSubPath
     * @param primary
     * @return
     * @throws SQLException
     */

    public static ResultSet uploadData(String query, String idxSubPath, String primary, String primary2) throws SQLException
    {
        try
        {
            con = makeConnection();
            DataUpload.indexSubPath = idxSubPath;
            DataUpload.pKey = primary;
            DataUpload.pKey2 = primary2;
            stmt = con.createStatement();
            System.out.println( "INITIATING : QUERYING DATABASE FOR LOADING " );
            Long start = new Date().getTime();
            rs = stmt.executeQuery( query );
            Long end = new Date().getTime();
            System.out.println( "DONE       : QUERY COMPLETE IN TIME " + (end - start) + " milliseconds" );
            if ( DataUpload.indexSubPath.equalsIgnoreCase( LuceneDBConstants.HOTEL_BASIC_INDEX_DIR ) )
                UploadeHotelDetails( rs );
            else if ( DataUpload.indexSubPath.equalsIgnoreCase( LuceneDBConstants.HOTEL_ROOM_INDEX_DIR ) )
                UploadeHotelRoomPicture( rs );
            else if ( DataUpload.indexSubPath.equalsIgnoreCase( LuceneDBConstants.HOTEL_ROOM_MORE_INDEX_DIR ) )
                UploadeHotelRoomPicture( rs );
            else
                IndexResultSet( rs );
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            throw e;
        }
        return rs;
    }

    /**
     * This method iterates through the resultset and loads the data to the lucene indexes if any image found in database,
     * it upload it to directory structure only image paths are kept into the lucene indexes
     * 
     * @param rs
     */
    public static void IndexResultSet(ResultSet rs)
    {
        try
        {
            if ( rs != null )
            {
                String primary = "";
                String imageType = "";
                String tableName = "";
                rsmd = rs.getMetaData();
                int count = rsmd.getColumnCount();
                int recCount = 0;
                System.out.println( "INITIATING : WRITING DATA TO INDEX " );
                DataUpload.initializeIndexer( indexSubPath );
                Long start = new Date().getTime();

                File ImagesRootDir = new File( imagePath );
                if ( !ImagesRootDir.exists() )
                {
                    ImagesRootDir.mkdir();
                }
                while( rs.next() )
                {
                    data = null;
                    for( int i = 1 ; i <= count ; i++ )
                    {
                        try
                        {
                            if ( rsmd.getColumnName( i ).equalsIgnoreCase( pKey ) )
                            {
                                primary = rs.getString( i );
                            }
                            if ( rsmd.getColumnTypeName( i ).toLowerCase().endsWith( "blob" ) && ((rsmd.getColumnName( i ).toLowerCase()).indexOf( "picture" ) != -1) )
                            {
                                imageType = rsmd.getColumnName( i );
                                tableName = rsmd.getTableName( i );

                                int a = imageType.lastIndexOf( "_" ) + 1;
                                int b = imageType.length();

                                imageType = imageType.substring( a, b );
                                if ( imageType.equalsIgnoreCase( "pc" ) )
                                {
                                    imageType = "jpg";
                                }
                                if ( imageType.equalsIgnoreCase( "gif" ) )
                                {
                                    imageType = "gif";
                                }
                                if ( imageType.equalsIgnoreCase( "png" ) )
                                {
                                    imageType = "png";
                                }

                                // If the file doesn't exist, create it now.
                                File innerFile = new File( imagePath + "/" + tableName );
                                if ( !innerFile.exists() )
                                {
                                    innerFile.mkdir();
                                }

                                InputStream is = rs.getBinaryStream( i );
                                if ( is != null )
                                {
                                    FileOutputStream fos = new FileOutputStream( imagePath + "/" + tableName + "/" + primary + imageType + "." + imageType );
                                    byte[] buf = new byte[3000];
                                    int read = 0;
                                    while( (read = is.read( buf )) > 0 )
                                    {
                                        fos.write( buf, 0, read );
                                    }
                                    fos.close();
                                    is.close();
                                    recCount++;
                                }
                            }
                        }
                        catch ( IOException e )
                        {
                            System.out.println( "exception" + e );
                        }
                    }
                    recCount++;
                }
                Long end = new Date().getTime();
                System.out.println( "DONE       : " + recCount + " FILES UPLOAD IN TIME " + (end - start) + " milliseconds\n" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        finally
        {
            DataUpload.CloseIndexer();
            closeConnection();
        }
    }

    /**
     * DBに保存されている外観画像をuploadする
     * （Indexing処理は行わない）
     * 
     * @param rs
     */
    public static void UploadeHotelDetails(ResultSet rs)
    {
        String primary = "";
        String imageType = "";
        String tableName = "";

        try
        {
            innerCon = makeInnerConnection();
            if ( rs != null )
            {

                rsmd = rs.getMetaData();
                int count = rsmd.getColumnCount();
                int recCount = 0;
                System.out.println( "INITIATING : WRITING DATA TO INDEX " );
                DataUpload.initializeIndexer( indexSubPath );
                Long start = new Date().getTime();

                File ImagesRootDir = new File( imagePath );
                if ( !ImagesRootDir.exists() )
                {
                    ImagesRootDir.mkdir();
                }

                while( rs.next() )
                {
                    data = null;
                    for( int i = 1 ; i <= count ; i++ )
                    {
                        try
                        {

                            if ( rsmd.getColumnName( i ).equalsIgnoreCase( pKey ) )
                            {
                                primary = rs.getString( i );
                            }

                            if ( rsmd.getColumnTypeName( i ).toLowerCase().endsWith( "blob" ) && ((rsmd.getColumnName( i ).toLowerCase()).indexOf( "picture" ) != -1) )
                            {
                                imageType = rsmd.getColumnName( i );
                                tableName = rsmd.getTableName( i );

                                int a = imageType.lastIndexOf( "_" ) + 1;
                                int b = imageType.length();

                                imageType = imageType.substring( a, b );
                                if ( imageType.equalsIgnoreCase( "pc" ) )
                                {
                                    imageType = "jpg";
                                }
                                if ( imageType.equalsIgnoreCase( "gif" ) )
                                {
                                    imageType = "gif";
                                }
                                if ( imageType.equalsIgnoreCase( "png" ) )
                                {
                                    imageType = "png";
                                }

                                // If the file doesn't exist, create it now.
                                File innerFile = new File( imagePath + "/" + tableName );
                                if ( !innerFile.exists() )
                                {
                                    innerFile.mkdir();
                                }

                                InputStream is = rs.getBinaryStream( i );
                                if ( is != null )
                                {
                                    FileOutputStream fos = new FileOutputStream( imagePath + "/" + tableName + "/" + primary + imageType + "." + imageType );
                                    byte[] buf = new byte[3000];
                                    int read = 0;
                                    while( (read = is.read( buf )) > 0 )
                                    {
                                        fos.write( buf, 0, read );
                                    }
                                    fos.close();
                                    is.close();
                                    recCount++;
                                }
                            }
                        }
                        catch ( IOException e )
                        {
                            System.out.println( "exception" + e );
                        }
                    }
                }

                Long end = new Date().getTime();
                System.out.println( "DONE       : " + recCount + " FILES UPLOAD IN TIME " + (end - start) + " milliseconds\n" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        finally
        {
            closeInnerConnection();
            closeConnection();
        }
    }

    /**
     * DBに保存されている部屋画像をuploadする
     * （Indexing処理は行わない）
     * 
     * @param rs
     */
    public static void UploadeHotelRoomPicture(ResultSet rs)
    {
        String primary = "";
        String primary2 = "";
        String imageType = "";
        String tableName = "";
        NumberFormat nf;

        nf = new DecimalFormat( "0000" );
        try
        {
            innerCon = makeInnerConnection();
            if ( rs != null )
            {

                rsmd = rs.getMetaData();
                int count = rsmd.getColumnCount();
                int recCount = 0;
                Long start = new Date().getTime();

                File ImagesRootDir = new File( imagePath );
                if ( !ImagesRootDir.exists() )
                {
                    ImagesRootDir.mkdir();
                }

                while( rs.next() )
                {
                    data = null;
                    for( int i = 1 ; i <= count ; i++ )
                    {
                        try
                        {

                            if ( rsmd.getColumnName( i ).equalsIgnoreCase( pKey ) )
                            {
                                primary = rs.getString( i );
                            }
                            if ( rsmd.getColumnName( i ).equalsIgnoreCase( LuceneDBConstants.HOTEL_ROOM_INDEX_PRIMARY2 ) )
                            {
                                primary2 = rs.getString( i );
                                primary2 = "_" + nf.format( Integer.parseInt( primary2 ) );
                            }

                            if ( rsmd.getColumnTypeName( i ).toLowerCase().endsWith( "blob" ) && ((rsmd.getColumnName( i ).toLowerCase()).indexOf( "picture" ) != -1) )
                            {
                                imageType = rsmd.getColumnName( i );
                                tableName = rsmd.getTableName( i );

                                int a = imageType.lastIndexOf( "_" ) + 1;
                                int b = imageType.length();

                                imageType = imageType.substring( a, b );
                                if ( imageType.equalsIgnoreCase( "pc" ) )
                                {
                                    imageType = "jpg";
                                }
                                if ( imageType.equalsIgnoreCase( "gif" ) )
                                {
                                    imageType = "gif";
                                }
                                if ( imageType.equalsIgnoreCase( "png" ) )
                                {
                                    imageType = "png";
                                }

                                // If the file doesn't exist, create it now.
                                File innerFile = new File( imagePath + "/" + tableName );
                                if ( !innerFile.exists() )
                                {
                                    innerFile.mkdir();
                                }

                                InputStream is = rs.getBinaryStream( i );
                                if ( is != null )
                                {
                                    FileOutputStream fos = new FileOutputStream( imagePath + "/" + tableName + "/" + primary + primary2 + imageType + "." + imageType );
                                    byte[] buf = new byte[3000];
                                    int read = 0;
                                    while( (read = is.read( buf )) > 0 )
                                    {
                                        fos.write( buf, 0, read );
                                    }
                                    fos.close();
                                    is.close();
                                    recCount++;
                                }
                            }
                        }
                        catch ( IOException e )
                        {
                            System.out.println( "exception" + e );
                        }
                    }
                }
                Long end = new Date().getTime();
                System.out.println( "DONE       : " + recCount + " FILES UPLOAD IN TIME " + (end - start) + " milliseconds\n" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        finally
        {
            closeInnerConnection();
            closeConnection();
        }
    }

    /**
     * Call this method for database Indexing
     * 
     * @param args
     */

    public static void main(final String[] args)
    {
        try
        {
            System.out.println( "START UPLOADING PROCESS" );
            if ( args.length == 0 )
            {
                DataUpload.uploadData( LuceneDBConstants.QUERY_HOTEL_BASIC, LuceneDBConstants.HOTEL_BASIC_INDEX_DIR, LuceneDBConstants.HOTEL_BASIC_INDEX_PRIMARY, "" );
                DataUpload.uploadData( LuceneDBConstants.QUERY_HOTEL_ROOM, LuceneDBConstants.HOTEL_ROOM_INDEX_DIR, LuceneDBConstants.HOTEL_ROOM_INDEX_PRIMARY, LuceneDBConstants.HOTEL_ROOM_INDEX_PRIMARY2 );
                DataUpload.uploadData( LuceneDBConstants.QUERY_HOTEL_ROOM_MORE, LuceneDBConstants.HOTEL_ROOM_MORE_INDEX_DIR, LuceneDBConstants.HOTEL_ROOM_INDEX_PRIMARY, LuceneDBConstants.HOTEL_ROOM_INDEX_PRIMARY2 );
            }
            else
            {
                for( int i = 0 ; i < args.length ; i++ )
                {
                    if ( "-h".equals( args[i] ) )
                    {
                        System.out.println( " -g : 外観画像" );
                        System.out.println( " -r : 部屋画像(標準)" );
                        System.out.println( " -m : 部屋画像(増量分)" );
                        System.out.println( " 引数なし : 上記すべて" );
                    }
                    // 外観画像
                    else if ( "-g".equals( args[i] ) )
                    {
                        DataUpload.uploadData( LuceneDBConstants.QUERY_HOTEL_BASIC, LuceneDBConstants.HOTEL_BASIC_INDEX_DIR, LuceneDBConstants.HOTEL_BASIC_INDEX_PRIMARY, "" );
                    }
                    // 部屋画像(標準)
                    else if ( "-r".equals( args[i] ) )
                    {
                        DataUpload.uploadData( LuceneDBConstants.QUERY_HOTEL_ROOM, LuceneDBConstants.HOTEL_ROOM_INDEX_DIR, LuceneDBConstants.HOTEL_ROOM_INDEX_PRIMARY, LuceneDBConstants.HOTEL_ROOM_INDEX_PRIMARY2 );
                    }
                    // 部屋画像(増量分)
                    else if ( "-m".equals( args[i] ) )
                    {
                        DataUpload.uploadData( LuceneDBConstants.QUERY_HOTEL_ROOM_MORE, LuceneDBConstants.HOTEL_ROOM_MORE_INDEX_DIR, LuceneDBConstants.HOTEL_ROOM_INDEX_PRIMARY, LuceneDBConstants.HOTEL_ROOM_INDEX_PRIMARY2 );
                    }
                    else
                    {
                        System.out.println( " -g : 外観画像" );
                        System.out.println( " -r : 部屋画像(標準)" );
                        System.out.println( " -m : 部屋画像(増量分)" );
                        System.out.println( " 引数なし : 上記すべて" );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }

    /**
     * It indexes the primary data and store other data unindexed
     * 
     * @param pkWord
     * @param data
     * @throws IOException
     */

    public static void indexRecord(String pkWord, String data) throws IOException
    {
        Document doc = new Document();
        Field f1 = new Field( "contents", data, Field.Store.YES, Field.Index.NO );// IDs with pipe seprated
        Field f2 = new Field( "primary", pkWord, Field.Store.NO, Field.Index.UN_TOKENIZED );// Words
        doc.add( f1 );
        doc.add( f2 );
        writer2.addDocument( doc );
    }

    /**
     * Initializes the index writer object
     * 
     * @param indexSubPAth
     */

    public static void initializeIndexer(String indexSubPAth)
    {
        try
        {
            System.out.println( "IDXROOT    : " + indexDirectory + "\\" + indexSubPAth );
            writer2 = new IndexWriter( new File( indexDirectory + "\\" + indexSubPAth ), new CJKAnalyzer(), true );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    /**
     * creates database connection
     * 
     * @return
     */
    private static Connection makeConnection()
    {
        try
        {
            Class.forName( "com.mysql.jdbc.Driver" );
            con = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * Closes database connection
     */
    private static void closeConnection()
    {
        try
        {
            if ( rs != null )
                rs.close();
            if ( stmt != null )
                stmt.close();
            if ( con != null )
                con.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * creates database connection
     * 
     * @return
     */
    private static Connection makeInnerConnection()
    {
        try
        {
            Class.forName( "com.mysql.jdbc.Driver" );
            innerCon = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return innerCon;
    }

    /**
     * Closes database connection
     */
    private static void closeInnerConnection()
    {
        try
        {
            if ( innerRs != null )
                innerRs.close();
            if ( innerStmt != null )
                innerStmt.close();
            if ( innerCon != null )
                innerCon.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Closes Lucene Indexer
     */
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

    /**
     * Return date in required format
     * 
     * @return string
     */
    public static String getDate()
    {
        String buf;

        Calendar cal = Calendar.getInstance();
        int year = cal.get( Calendar.YEAR );
        int month = cal.get( Calendar.MONTH ) + 1;
        int day = cal.get( Calendar.DATE );

        NumberFormat nf = new DecimalFormat( "00" );
        buf = year + nf.format( month ) + nf.format( day );

        return buf;

    }

}

class LuceneDBConstants
{
    public static String       LUCENE_DB_ROOT_DIR        = "/lucene/LUCENE_INDEXES";
    public static final String HOTEL_BASIC_INDEX_PRIMARY = "id";
    public static final String HOTEL_BASIC_INDEX_DIR     = "HOTEL_BASIC_INFO";
    public static final String HOTEL_ROOM_INDEX_PRIMARY  = "id";
    public static final String HOTEL_ROOM_INDEX_PRIMARY2 = "seq";
    public static final String HOTEL_ROOM_INDEX_DIR      = "HOTEL_ROOM";
    public static final String HOTEL_ROOM_MORE_INDEX_DIR = "HOTEL_ROOM_MORE";
    public static final int    InnerConnectionLoadFactor = 50;

    // TODO 要修正
    public static final String QUERY_HOTEL_BASIC         = " SELECT HB.id, HB.hotel_picture_pc, HB.hotel_picture_gif, HB.hotel_picture_png" +
                                                                 " FROM hh_hotel_pv HP, hh_hotel_basic HB LEFT JOIN hh_hotel_master HC ON (HB.id = HC.id)" +
                                                                 " WHERE (HB.kind <= 7 OR HB.kind=10)" +
                                                                 " AND HB.id = HP.id" +
                                                                 " AND HP.collect_date = 0" +
                                                                 " AND HB.rank >= 2";

    public static final String QUERY_HOTEL_ROOM_MORE     = "SELECT HRM.id, HRM.seq, HRM.room_picture_pc, HRM.room_picture_gif, HRM.room_picture_png" +
                                                                 " FROM hh_hotel_room_more HRM, hh_hotel_basic HB" +
                                                                 " WHERE HB.id = HRM.id" +
                                                                 " AND HB.rank >= 2" +
                                                                 " AND HRM.disp_flag >= 1" +
                                                                 " AND HRM.refer_name = ''";
    // TODO 要修正
    public static final String QUERY_HOTEL_ROOM          = "SELECT HR.id, HR.seq, HR.room_picture_pc, HR.room_picture_gif, HR.room_picture_png" +
                                                                 " FROM hh_hotel_room HR, hh_hotel_basic HB" +
                                                                 " WHERE HB.id = HR.id" +
                                                                 " AND HB.rank >= 2" +
                                                                 " AND HR.disp_flag >= 1" +
                                                                 " AND HR.refer_name = ''" +
                                                                 " ORDER BY HR.room_picture_pc DESC";

    static
    {
        if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
        {
            LUCENE_DB_ROOT_DIR = "C:/lucene/LUCENE_INDEXES";
        }
        else
        {
            LUCENE_DB_ROOT_DIR = "/lucene/LUCENE_INDEXES";
        }
    }

}
