package jp.happyhotel.imageupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import jp.happyhotel.common.Logging;

/**
 * This class fetch data from database
 * 
 * @author HCL Technologies Ltd.
 * 
 */

public class DataUpload_M2
{

    // ConfigurationPath for WINDOW
    public static final String CONFIGURATION_WINDOW_PATH = "c:/etc/happyhotel/imageupload.conf";
    // ConfigurationPath for LINUX
    // TODO:
    public static final String CONFIGURATION_LINUX_PATH  = "/etc/happyhotel/imageupload.conf";

    public static final String OS_NAME_WINDOWS           = "window";
    public static final String OS_NAME_LINUX             = "linux";
    public static final String UNKNOWN_OS                = "unknown";

    public static final String HOTEL_BASIC_INDEX_PRIMARY = "id";

    public static final String QUERY_HOTEL_BASIC         = " SELECT HB.id, HB.rank, HB.name, HB.pref_name, "
                                                                 + " HB.address1, HB.address_all, HB.tel1, HB.url, HB.url_official1, "
                                                                 + " HB.url_official2, HB.pr, HB.reserve, HB.reserve_tel, HB.reserve_mail, "
                                                                 + " HB.reserve_web, IFNULL(HS.empty_status, 0) empty_status,HB.hotel_picture_pc, HC.bbs_config FROM hh_hotel_pv HP,"
                                                                 + " hh_hotel_basic HB LEFT JOIN hh_hotel_master HC ON (HB.id = HC.id)"
                                                                 + " LEFT JOIN hh_hotel_status HS ON (HB.id = HS.id)"
                                                                 + " WHERE HB.kind <= 7"
                                                                 + " AND HB.id = HP.id"
                                                                 + " AND HP.collect_date = 0";

    static String              DB_Driver                 = null;
    static String              DB_ServerIP               = null;
    static String              DB_Name                   = null;
    static String              DB_URL                    = null;                                // URL for database server
    static String              user                      = null;                                // DB user
    static String              password                  = null;                                // DB password
    static String              imagePath                 = null;                                // Images root path

    // Finds from configuration files, for database connectivity URL, user and
    // password.
    // it also sets the images root

    static
    {
        FileInputStream propfile = null;
        Properties prop = null;
        try
        {
            String configurationPath = null;

            // Check For OS Type
            if ( getOsType() == 1 )
            {
                configurationPath = CONFIGURATION_WINDOW_PATH;
                Logging.info( "OS NAME : " + OS_NAME_WINDOWS );
            }
            else if ( getOsType() == 2 )
            {
                configurationPath = CONFIGURATION_LINUX_PATH;
                Logging.info( "OS NAME : " + OS_NAME_LINUX );
            }
            else
            {
                Logging.info( UNKNOWN_OS );
            }

            // Check For File Exist OR Not
            if ( isFileExist( configurationPath ) )
            {
                Logging.info( "Configuration File Found :" + configurationPath );
            }
            else
            {
                Logging.info( "Configuration File Not Found" );
                System.exit( 1 );
            }

            propfile = new FileInputStream( configurationPath );
            prop = new Properties();

            // Load Properties from property File
            prop.load( propfile );

            // Check for valid property
            if ( !checkProperty( prop ) )
            {
                System.exit( 1 );
            }

            // Make DataBase URL
            DB_URL = "jdbc:mysql://" + DB_ServerIP + ":3306/" + DB_Name
                    + "?useUnicode=yes&characterEncoding=sjis";

            // Close Property File
            propfile.close();
        }
        catch ( Exception exception )
        {
            Logging.info( "DATALOAD Static Block Error=" + exception.toString() );
        }
        finally
        {
            prop = null;
        }
    }

    /**
     * Check OS type : Window or Linux
     * 
     * @return int type
     */
    public static int getOsType()
    {
        String strOSName = System.getProperty( "os.name" );
        if ( strOSName.toLowerCase().contains( OS_NAME_WINDOWS ) )
        {
            return 1;
        }
        else if ( strOSName.toLowerCase().contains( OS_NAME_LINUX ) )
        {
            return 2;
        }
        else
        {
            return 3;
        }
    }

    /**
     * This function check given file name exist or not
     * 
     * @param fileName
     * @return TRUE or FALSE
     */
    public static boolean isFileExist(String fileName)
    {
        File file = new File( fileName );
        return file.exists();
    }

    /**
     * This function check All Property Value is valid or not
     * 
     * @param prop
     * @return TRUE or FALSE
     */
    public static boolean checkProperty(Properties prop)
    {
        DB_Driver = prop.getProperty( "database.driver" );
        DB_ServerIP = prop.getProperty( "databaseserver.ip" );
        DB_Name = prop.getProperty( "database.name" );
        user = prop.getProperty( "database.user" );
        password = prop.getProperty( "user.password" );
        imagePath = prop.getProperty( "images.root" );

        // Check For Valid DB_Driver Property
        if ( !isValidProperty( DB_Driver ) )
        {
            Logging.info( "DBDriver Property is not valid or null" );
            return false;
        }

        // Check For Valid DB_ServerIP Property
        if ( !isValidProperty( DB_ServerIP ) )
        {
            Logging.info( "DBServer Property is not valid or null" );
            return false;
        }

        // Check For Valid DB_Name Property
        if ( !isValidProperty( DB_Name ) )
        {
            Logging.info( "DBName Property is not valid or null" );
            return false;
        }

        // Check For Valid User Property
        if ( !isValidProperty( user ) )
        {
            Logging.info( "User Property is not valid or null" );
            return false;
        }

        // Check For Valid Password Property
        // Return TRUE: If password is Blank
        if ( password == null )
        {
            Logging.info( "Password Property is not valid or null" );
            return false;
        }

        // Check For Valid ImagePath Property
        if ( !isValidProperty( imagePath ) )
        {
            Logging.info( "ImagePath Property is not valid or null" );
            return false;
        }

        return true;

    }

    /**
     * Check Property Value is valid or not
     * 
     * @param propertyName
     * @return TRUE or FALSE
     */
    public static boolean isValidProperty(String propertyName)
    {

        if ( propertyName != null && propertyName.trim().length() > 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This method closes database resources and returns the connection object back to the pooler
     * 
     * @param resultset
     * @param statement
     * @param connection
     */
    public static void releaseResources(ResultSet resultset,
            Statement statement, Connection connection)
    {
        try
        {
            if ( resultset != null )
            {
                resultset.close();
                resultset = null;
            }
            if ( statement != null )
            {
                statement.close();
                statement = null;
            }
            if ( connection != null )
            {
                connection.close();
                connection = null;
            }
        }
        catch ( SQLException sqlException )
        {
            Logging.error( "Error while closing the connection resources" + sqlException.toString() );
        }
        catch ( Exception exception )
        {
            Logging.error( "Error while closing the connection resources" + exception.toString() );
        }
    }

    /**
     * This function executes the query and pass the ResultSet for Indexing
     * 
     * @param query
     * @throws Exception
     */

    public static void uploadData(String query) throws Exception
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            // Make Connection
            Class.forName( DB_Driver );
            connection = DriverManager.getConnection( DB_URL, user, password );
            statement = connection.createStatement();
            Logging.info( "INITIATING : QUERYING DATABASE FOR LOADING " );
            Long start = new Date().getTime();
            resultSet = statement.executeQuery( query );
            // Fetch data from database
            IndexResultSetHotelDetails( resultSet );
            // Calculate query Time
            Long end = new Date().getTime();
            Logging.info( "DONE       : QUERY COMPLETE IN TIME "
                    + (end - start) + " milliseconds" );

        }
        catch ( SQLException sqlException )
        {
            Logging.info( "DataUpload.uploadData()" + sqlException.toString() );
            throw sqlException;
        }
        catch ( Exception exception )
        {
            Logging.info( "DataUpload.uploadData()" + exception.toString() );
            throw exception;
        }
        finally
        {
            releaseResources( resultSet, statement, connection );
        }
    }

    /**
     * This method iterates through the resultset and loads the data
     * 
     * @param resultSet
     * @throws Exception
     */
    public static void IndexResultSetHotelDetails(ResultSet resultSet) throws Exception
    {
        int count = 0;
        String primary = null;
        String imageType = null;
        String tableName = null;
        ResultSetMetaData rsmd = null;

        try
        {
            if ( resultSet.next() )
            {
                rsmd = resultSet.getMetaData();
                count = rsmd.getColumnCount();

                // Check for path separator end of the file path
                int length = imagePath.length();
                int index = imagePath.substring( length - 1, length ).lastIndexOf( File.separator );

                if ( index != -1 )
                {
                    imagePath = imagePath.substring( 0, length - 1 );
                }

                File ImagesRootDir = new File( imagePath );
                if ( !ImagesRootDir.exists() )
                {
                    ImagesRootDir.mkdir();
                }

                resultSet.beforeFirst();
                while( resultSet.next() )
                {
                    for( int i = 1 ; i <= count ; i++ )
                    {

                        if ( rsmd.getColumnName( i ).equalsIgnoreCase(
                                HOTEL_BASIC_INDEX_PRIMARY ) )
                        {
                            primary = resultSet.getString( i );
                        }

                        if ( rsmd.getColumnTypeName( i ).toLowerCase().endsWith(
                                "blob" )
                                && ((rsmd.getColumnName( i ).toLowerCase())
                                .indexOf( "picture" ) != -1) )
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

                            if ( !imagePath.toUpperCase().contains( tableName ) )
                            {
                                imagePath = imagePath + File.separator + tableName;
                            }

                            File innerFile = new File( imagePath );
                            if ( !innerFile.exists() )
                            {
                                innerFile.mkdir();
                            }

                            InputStream is = resultSet.getBinaryStream( i );
                            if ( is != null )
                            {
                                FileOutputStream fos = new FileOutputStream(
                                        imagePath + File.separator
                                        + primary + imageType + "."
                                        + imageType );
                                byte[] buf = new byte[3000];
                                int read = 0;
                                while( (read = is.read( buf )) > 0 )
                                {
                                    fos.write( buf, 0, read );
                                }
                                fos.close();
                                is.close();
                            }
                        }
                    }

                }
            }
        }
        catch ( Exception exception )
        {
            Logging.info( "DataUpload.IndexResultSetHotelDetails()" + exception.toString() );
            throw exception;
        }
    }

    /**
     * Call this method for database Indexing
     * 
     * @param args
     */

    public static void main(String[] args)
    {
        try
        {
            Logging.info( "START UPLOADING PROCESS" );
            DataUpload_M2.uploadData( QUERY_HOTEL_BASIC );
        }
        catch ( Exception exception )
        {
            Logging.error( "DataUpload.main() Exception", exception );
        }
    }

}
