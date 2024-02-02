package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.hotel.HotelCountGCP;

/**
 * ホテルデータ集計処理バッチ
 * 
 * @author paku-k1
 */
public class HotelDataProcess
{
    static String DB_URL;  // URL for database server
    static String user;    // DB user
    static String password; // DB password
    static String driver;  // DB driver
    static String jdbcds;  // DB jdbcds

    static
    {
        try
        {
            Properties prop = new Properties();
            // Linux環境
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
            // windows環境
            // FileInputStream propfile = new FileInputStream( "C:\\ALMEX\\WWW\\WEB-INF\\dbconnect.conf" );
            prop = new Properties();
            // プロパティファイルを読み込む
            prop.load( propfile );

            // "jdbc.driver"に設定されている値を取得します
            driver = prop.getProperty( "jdbc.driver" );

            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"に設定されている値を取得します
            jdbcds = prop.getProperty( "jdbc.datasource" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            System.out.println( "CollectHotelSort Static Block Error=" + e.toString() );
        }
    }

    private static Connection makeConnection()
    {
        Connection conn = null;
        Properties props = new Properties();
        props.setProperty( "user", user );
        props.setProperty( "password", password );
        props.setProperty( "rewriteBatchedStatements", "true" );
        try
        {
            Class.forName( driver );
            conn = DriverManager.getConnection( DB_URL, props );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args)
    {
        Connection connection = makeConnection();
        boolean countSuccess = false;
        boolean pvSuccess = false;
        boolean sortSuccess = false;
        int yesterday = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );
        String strDate;
        try
        {
            if ( args != null && args.length > 0 )
            {
                strDate = args[0];
                yesterday = Integer.parseInt( strDate );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDataProcess] コマンドライン引数の処理失敗:" + e.toString() );
        }

        try
        {
            Logging.info( "START CollectPvData" );
            // 1.hotel_pv集計処理
            pvSuccess = CollectHotelPv.updateHotelPv( connection, yesterday );
            if ( pvSuccess )
            {
                // 2.hotel_sort集計処理
                sortSuccess = CollectHotelSortGCP.updateHotelSort( connection, yesterday );
            }
            if ( sortSuccess )
            {
                // 3.hh_hotel_count集計処理
                countSuccess = HotelCountGCP.setData( connection );
            }
            if ( countSuccess )
            {
                Logging.info( "CollectPvData Success!" );
            }
            else
            {
                Logging.info( "CollectPvData failed!" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CollectPvData] Exception=" + e.toString() );
            e.printStackTrace();
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        Logging.info( "END CollectPvData" );
    }
}
