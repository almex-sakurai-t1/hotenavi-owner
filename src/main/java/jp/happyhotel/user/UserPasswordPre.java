/*
 * @(#)UserMap.java 1.00
 * 2007/07/31 Copyright (C) ALMEX Inc. 2007
 * ユーザーマップ取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import jp.happyhotel.common.ConvertString;
import jp.happyhotel.common.Logging;

/**
 * 被害にあったユーザのパスワードをリセット
 * 
 * 
 * @author Mitsuhasi
 */
public class UserPasswordPre implements Serializable
{
    static Connection connection = null; // Database connection
    static String     DB_URL;           // URL for database server
    static String     user;             // DB user
    static String     password;         // DB password
    static String     errMsg     = "";

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
            prop = new Properties();
            // プロパティファイルを読み込む
            prop.load( propfile );

            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "UserPasswordConvert Static Block Error=" + e.toString() );
        }
    }

    /**
     * メイン
     * 
     * @param args
     */

    public static void main(String[] args)
    {

        connection = makeConnection();

        System.out.println( "[UserPasswordConvert.main( )] Start" );

        try
        {

            // ユーザパスワードリセット処理
            passwordConvert( connection );

        }
        catch ( Exception e )
        {
            // Logging.error( "[UserPasswordConvert.main( )]Exception:" + e.toString() );
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            UserPasswordPre.closeConnection();
        }

        if ( errMsg.equals( "" ) )
        {
            errMsg = "ユーザパスワード変換処理が終了しました";
        }

        System.out.println( "[UserPasswordConvert.main( )] End\r\n" + errMsg );
    }

    /*
     * ユーザパスワードリセット
     */
    public static void passwordConvert(Connection connection)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;

        int count = 0;
        query = "SELECT user_id,passwd FROM hh_user_basic WHERE (mail_addr <> '' OR mail_addr_mobile <> '' ) and length(passwd)<=15 AND length(passwd)>=6";

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    count++;
                    updateDB( connection, result.getString( "user_id" ), UserLogin.encrypt( ConvertString.convert2md5( result.getString( "passwd" ) ) ) );

                    if ( count % 10000 == 0 )
                        System.out.println( "[UserPasswordConvert.main( )] count:" + count );

                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            try
            {
                // それぞれを閉じる
                if ( result != null )
                    result.close();
                if ( prestate != null )
                    prestate.close();
            }
            catch ( Exception e )
            {
                // Logging.error( "[UserPasswordConvert.passwordReset()] Exception:" + e.toString() );
            }
        }
    }

    private static boolean updateDB(Connection connection, String userId, String pass)
    {
        // System.out.println( "[UserPasswordConvert.updateDB( )] userId=" + userId );

        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "UPDATE user_now_point ";
        query += " SET passwd = '" + pass + "'";
        query += " WHERE user_id ='" + userId + "'";

        try
        {
            prestate = connection.prepareStatement( query );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            ret = false;
        }
        try
        {
            if ( prestate != null )
                prestate.close();
        }
        catch ( Exception e )
        {
        }

        return(ret);

    }

    /**
     * DBコネクション作成クラス
     * 
     * @return
     */
    private static Connection makeConnection()
    {
        try
        {
            Class.forName( "com.mysql.jdbc.Driver" );
            connection = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * DBコネクション開放クラス
     * 
     * @return
     */
    private static void closeConnection()
    {
        try
        {
            if ( connection != null )
                connection.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

}
