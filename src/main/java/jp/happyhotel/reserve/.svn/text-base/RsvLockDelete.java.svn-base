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
import jp.happyhotel.common.Logging;

/**
 * 予約ロックデータ削除クラス
 * 
 * @author S.Tashiro
 * 
 */

public class RsvLockDelete
{

    static Connection        con  = null; // Database connection
    static PreparedStatement stmt = null;
    static ResultSet         rs   = null;

    static String            DB_URL;     // URL for database server
    static String            user;       // DB user
    static String            password;   // DB password
    static String            driver;     // DB driver
    static String            jdbcds;     // DB jdbcds

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
            System.out.println( "RsvLockDelete Static Block Error=" + e.toString() );
        }
    }

    /**
     * メイン
     * 
     * @param args 任意の日付(YYYYMMまたはYYYYMM00)
     */

    public static void main(String[] args)
    {
        int nCount = 0;
        int nDelCount = 0;
        String strDate = "";
        int nDate = 0;
        int nTime = 0;
        try
        {
            if ( args != null && args.length > 0 )
            {
                strDate = args[0];
                nDate = Integer.parseInt( strDate );
            }
            if ( nDate == 0 )
            {
                // 10秒前の時刻を取得
                int ret[] = DateEdit.addSec( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), -10 );
                nDate = ret[0];
                nTime = ret[1];
            }

            System.out.println( "[RsvLockDelete] Start" );
            System.out.println( "nDate:" + nDate + " nTime:" + nTime );
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvLockDelete] コマンドライン引数の処理失敗:" + e.toString() );
        }

        try
        {
            if ( nDate > 0 )
            {
                nCount = getLockCount( nDate, nTime );
                if ( nCount > 0 )
                {
                    nDelCount = deleteRockData( nDate, nTime );
                }
            }
            System.out.println( "[RsvLockDelete] nDate=" + nDate + ",nTime=" + nTime + ", count=" + nCount + ", delCount=" + nDelCount );
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvLockDelete.main] Exception=" + e.toString() );
            e.printStackTrace();
        }
        finally
        {
            releaseResources( rs, stmt, con );
        }
        System.out.println( "[RsvLockDelete] End" );
    }

    /**
     * 予約ロックデータ件数取得
     * 
     * @param targetDate 削除対象最終更新日
     * @param targetTime 削除対象最終更新時刻
     * @return
     * @throws SQLException
     */
    public static int getLockCount(int targetDate, int targetTime) throws SQLException
    {
        String query;
        int count = 0;
        int i;

        query = "SELECT COUNT(id) FROM hh_rsv_reserve_lock";
        query += " WHERE last_update <= ? ";
        query += "   AND last_uptime <= ? ";

        try
        {

            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, targetDate );
            stmt.setInt( 2, targetTime );
            rs = stmt.executeQuery();
            if ( rs != null )
            {
                if ( rs.next() != false )
                {
                    count = rs.getInt( 1 );
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[RsvLockDelete.getLockCount] Exception=" + e.toString() );
            e.printStackTrace();
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }

        return(count);
    }

    /**
     * ホテルID取得
     * 
     * @param targetDate 削除対象最終更新日
     * @param targetTime 削除対象最終更新時刻
     * @return
     * @throws SQLException
     */
    public static int deleteRockData(int targetDate, int targetTime) throws SQLException
    {
        String query;
        int count = 0;

        query = "DELETE FROM hh_rsv_reserve_lock";
        query += " WHERE last_update <= ? ";
        query += "   AND last_uptime <= ? ";

        try
        {

            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, targetDate );
            stmt.setInt( 2, targetTime );
            count = stmt.executeUpdate();
        }
        catch ( SQLException e )
        {
            Logging.error( "[RsvLockDelete.deleteRockData] Exception=" + e.toString() );
            e.printStackTrace();
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( stmt );
        }

        return(count);
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
            Logging.error( "[RsvLockDelete] Error while closing the connection resources" + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "[RsvLockDelete] Error while closing the connection resources" + ex.toString() );
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
            Logging.error( "[RsvLockDelete] Error while closing the connection resources" + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "[RsvLockDelete] Error while closing the connection resources" + ex.toString() );
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
            Logging.error( "[RsvLockDelete] Error while closing the resultset " + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "[RsvLockDelete] Error while closing the resultset " + ex.toString() );
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
            Logging.error( "[RsvLockDelete] Error while closing the statement " + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "[RsvLockDelete] Error while closing the statement " + ex.toString() );
        }
    }

}
