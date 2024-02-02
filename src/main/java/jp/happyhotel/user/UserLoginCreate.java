package jp.happyhotel.user;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.RandomString;

/**
 * ユーザーデータ作成
 */
public class UserLoginCreate
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
            System.out.println( "UserLoginCreate Static Block Error=" + e.toString() );
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
        try
        {
            userDataCreate( makeConnection() );
        }
        catch ( Exception e )
        {
            System.out.println( "[UserLoginCreate] コマンドライン引数の処理失敗:" + e.toString() );
        }
    }

    public static boolean userDataCreate(Connection connection) throws Exception
    {
        boolean ret = false;
        String query = "";
        String loginSql = "";
        ResultSet result = null;
        PreparedStatement prestate = null;

        System.out.println( "[UserLoginCreate start]" );

        query = "SELECT * FROM hh_user_basic";
        query += " WHERE del_flag=0 AND regist_status = 9";
        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            while( result.next() != false )
            {
                System.out.println( "[UserLoginCreate] " + result.getString( "user_id" ) );

                loginSql = "INSERT INTO hh_user_login SET";
                loginSql += " login_id = ? ";
                loginSql += ",user_id = ? ";
                loginSql += ",security_key = ? ";
                if ( isUserLogin( connection, result.getString( "user_id" ) ) )
                {
                    System.out.println( "user_id " + result.getString( "user_id" ) );
                    prestate = connection.prepareStatement( loginSql );
                    prestate.setString( 1, result.getString( "user_id" ) );
                    prestate.setString( 2, result.getString( "user_id" ) );
                    prestate.setString( 3, getSecurityKey( connection ) );
                    prestate.executeUpdate();
                }
                if ( !result.getString( "mail_addr_md5" ).equals( "" ) )
                {
                    System.out.println( "mail_addr" + result.getString( "mail_addr" ) );

                    if ( isUserLogin( connection, result.getString( "mail_addr" ) ) )
                    {
                        prestate = connection.prepareStatement( loginSql );
                        prestate.setString( 1, result.getString( "mail_addr" ) );
                        prestate.setString( 2, result.getString( "user_id" ) );
                        prestate.setString( 3, getSecurityKey( connection ) );
                        prestate.executeUpdate();
                    }
                }
                if ( !result.getString( "mail_addr_mobile_md5" ).equals( "" ) )
                {
                    if ( isUserLogin( connection, result.getString( "mail_addr_mobile" ) ) )
                    {
                        prestate = connection.prepareStatement( loginSql );
                        prestate.setString( 1, result.getString( "mail_addr_mobile" ) );
                        prestate.setString( 2, result.getString( "user_id" ) );
                        prestate.setString( 3, getSecurityKey( connection ) );
                        prestate.executeUpdate();
                    }
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            System.out.println( "[UserLoginCreate Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    private static boolean isUserLogin(Connection connection, String loginId) throws Exception
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = true;
        if ( !loginId.equals( "" ) )
        {
            String query = "SELECT 1 FROM hh_user_login WHERE login_id= ? ";
            try
            {
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, loginId );
                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    ret = false;
                }
            }
            catch ( Exception e )
            {
                System.out.println( "[isUserLogin Exception=" + e.toString() );
                ret = false;
            }
            finally
            {
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
            }
        }
        else
        {
            ret = false;
        }
        return ret;
    }

    private static String getSecurityKey(Connection connection)
    {
        final int KEY_LENGTH = 32;
        String securityKey;
        // 問題のない会員IDが生成されたらbreak
        while( true )
        {
            securityKey = DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) + RandomString.getRandomString( KEY_LENGTH - 14 );
            // 同じIDが登録されていなければOK
            if ( isSecurityKey( connection, securityKey ) == true )
            {
                break;
            }
        }
        return(securityKey);
    }

    /**
     * ユーザセキュリティキーを検査する
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:レコードがないのでOK,FALSE:レコード登録済)
     */
    private static boolean isSecurityKey(Connection connection, String securityKey)
    {
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( securityKey == null )
        {
            return(false);
        }
        query = "SELECT 1 FROM hh_user_login";

        if ( securityKey.compareTo( "" ) != 0 )
        {
            query = query + " WHERE security_key = ?";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            prestate = connection.prepareStatement( query );
            if ( securityKey.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, securityKey );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = false;
                }
                else
                {
                    ret = true;
                }
            }

        }
        catch ( Exception e )
        {
            System.out.println( "[isSecurityKey Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return ret;
    }

}
