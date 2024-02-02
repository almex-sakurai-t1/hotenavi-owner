package jp.happyhotel.reserve;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.GMORsvPayment;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;

/**
 * 予約クレジット分実売上実行判断クラス
 * 
 * @author S.Tashiro
 * 
 */

public class RsvCreditAlterTran
{
    static Connection        connection     = null;
    static PreparedStatement prestate       = null;
    static ResultSet         result         = null;

    static String            DB_URL;               // URL for database server
    static String            user;                 // DB user
    static String            password;             // DB password
    static String            driver;               // DB driver
    static String            jdbcds;               // DB jdbcds
    private static final int PLAN_TYPE_KEEP = 5;
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
            System.out.println( "RsvCreditAlterTran Static Block Error=" + e.toString() );
        }
    }

    /**
     * メイン
     */

    public static void main(String[] args)
    {
        String query = "";
        int param_id = 0;
        String reserve_no = "";
        int collectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int collectTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        GMORsvPayment payment = new GMORsvPayment();
        System.out.println( "[RsvCreditAlterTran] Start" );

        query = "SELECT";
        query += " hrr.id";
        query += ",hrr.reserve_no";
        query += " FROM newRsvDB.hh_rsv_reserve hrr";
        query += " INNER JOIN hh_rsv_reserve_basic hrrb ON hrr.id = hrrb.id";
        query += " INNER JOIN newRsvDB.hh_rsv_credit hrc ON hrr.reserve_no = hrc.reserve_no AND hrc.del_flag = 0 AND hrc.sales_flag = 0";
        query += " WHERE hrr.payment = " + ReserveCommon.PAYMENT_CREDIT; /* ReserveCommon.PAYMENT_KIND_CREDIT:1 */
        query += " AND hrr.payment_status = " + ReserveCommon.PAYMENT_STATUS_UNSETTLED; /* ReserveCommon.PAYMENT_STATUS_UNSETTLED:2 */
        query += " AND ((hrr.status =" + ReserveCommon.RSV_STATUS_ZUMI + ") OR (hrr.status = " + ReserveCommon.RSV_STATUS_CANCEL + " AND hrr.noshow_flag = " + ReserveCommon.NOSHOW_TRUE + ") OR ( plan.plan_type=" + PLAN_TYPE_KEEP + " AND hrr.status ="
                + ReserveCommon.RSV_STATUS_CANCEL + "))";
        query += " AND hrr.accept_date * 1000000 + hrr.accept_time < ? * 1000000 + hrrb.deadline_time ";
        query += " AND hrrb.deadline_time < ? ";

        try
        {
            connection = makeConnection();
            System.out.println( "RsvCreditAlterTran connection" );
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, collectDate );
            prestate.setInt( 2, collectTime );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    param_id = result.getInt( "id" );
                    reserve_no = result.getString( "reserve_no" );
                    System.out.println( "[RsvCreditAlterTran] param_id" + param_id );
                    System.out.println( "[RsvCreditAlterTran] reserve_no" + reserve_no );

                    // 実売上
                    payment.AlterTran( param_id, reserve_no, connection );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[RsvCreditAlterTran] Exception =" + e.toString(), "RsvCreditAlterTran" );
            e.printStackTrace();
        }
        finally
        {
            releaseResources( result, prestate, connection );
        }
        System.out.println( "[RsvCreditAlterTran] End" );
    }

    /**
     * DBコネクション作成クラス
     * 
     * @return
     */
    private static Connection makeConnection()
    {
        Connection conn = null;
        try
        {
            Class.forName( driver );
            conn = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * リソースを閉じる
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
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the connection resources" + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the connection resources" + ex.toString() );
        }
    }

    /**
     * コネクションを閉じる
     * 
     * @param connection
     */
    public static void releaseResources(Connection connection)
    {
        try
        {
            if ( connection != null )
            {
                connection.close();
                connection = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the connection resources" + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the connection resources" + ex.toString() );
        }
    }

    /**
     * ResultSetオブジェクトを閉じる
     * 
     * @param resultset
     */
    public static void releaseResources(ResultSet resultset)
    {
        try
        {
            if ( resultset != null )
            {
                resultset.close();
                resultset = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the resultset " + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the resultset " + ex.toString() );
        }
    }

    /**
     * statementオブジェクトを閉じる
     * 
     * @param statement
     */
    public static void releaseResources(Statement statement)
    {
        try
        {
            if ( statement != null )
            {
                statement.close();
                statement = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the statement " + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the statement " + ex.toString() );
        }
    }

}
