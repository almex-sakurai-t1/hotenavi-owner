package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;

/**
 * タッチエラープッシュ実行クラス
 * 
 * @author Keion.Park
 * 
 */
public class ChangePayMemberStatus
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
            System.out.println( "CollectHotelUniquePv Static Block Error=" + e.toString() );
        }
    }

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

    public static void main(String[] args) throws Exception
    {

        updatePayMemberStatus( makeConnection() );
    }

    public static void updatePayMemberStatus(Connection connection) throws Exception
    {
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> userIds = new ArrayList<String>();
        try
        {
            query = "SELECT b.user_id FROM hh_user_basic b";
            query += " INNER JOIN ap_uuid_user uu ON uu.user_id = b.user_id";
            query += " INNER JOIN ap_uuid ON uu.uuid = ap_uuid.uuid  ";
            query += " WHERE ap_uuid.regist_status_pay = 2";
            query += " AND (b.regist_status_pay <> 9 OR b.del_flag = 1 OR b.regist_status <> 9)";

            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            while( result.next() )
            {
                userIds.add( result.getString( "user_id" ) );
            }

            if ( userIds.size() > 0 )
            {
                query = "UPDATE hh_user_basic SET del_flag=0,regist_status_pay=9 ";
                query += " WHERE user_id IN(";
                for( int i = 0 ; i < userIds.size() ; i++ )
                {
                    query += "'" + userIds.get( i ) + "'";
                    if ( i < userIds.size() - 1 )
                    {
                        query += ",";
                    }
                }
                query += ")";
                System.out.println( query );
                prestate = connection.prepareStatement( query.toString() );
                int count = prestate.executeUpdate();
                System.out.println( "有料会員更新数:" + count );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[ChangePayMemberStatus.updatePayMemberStatus] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }
}
