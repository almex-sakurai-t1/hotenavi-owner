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
public class OwnerUserPasswordConvert implements Serializable
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
            Logging.error( "OwnerUserPasswordConvert Static Block Error=" + e.toString() );
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

        System.out.println( "[OwnerUserPasswordConvert.main( )] Start" );

        try
        {

            // ユーザパスワードリセット処理
            passwordConvert( connection );

        }
        catch ( Exception e )
        {
            // Logging.error( "[OwnerUserPasswordConvert.main( )]Exception:" + e.toString() );
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            OwnerUserPasswordConvert.closeConnection();
        }

        if ( errMsg.equals( "" ) )
        {
            errMsg = "ユーザパスワード変換処理が終了しました";
        }

        System.out.println( "[OwnerUserPasswordConvert.main( )] End\r\n" + errMsg );
    }

    /*
     * ユーザパスワードリセット
     */
    public static void passwordConvert(Connection connection)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;

        query = "SELECT hotel.hotel_id,hotel.ftp_passwd,owner_user.userid,owner_user.passwd_pc,owner_user.passwd_mobile";
        query += " FROM owner_user";
        query += " INNER JOIN hotel ON owner_user.hotelid = hotel.hotel_id";
        query += " WHERE owner_user.passwd_pc<>'' OR owner_user.passwd_mobile<>''";
        try
        {

            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    String passwd_pc_hashed = "";
                    String passwd_mobile_hashed = "";
                    int loop = result.getInt( "owner_user.userid" ) >= 512 ? 1 : 512 - result.getInt( "owner_user.userid" );

                    if ( !result.getString( "passwd_pc" ).equals( "" ) )
                    {
                        passwd_pc_hashed = result.getString( "passwd_pc" );
                        for( int i = 0 ; i < loop ; i++ )
                        {
                            passwd_pc_hashed = ConvertString.convert2md5( passwd_pc_hashed );
                        }
                        passwd_pc_hashed = UserLogin.encrypt( passwd_pc_hashed, result.getString( "ftp_passwd" ) );

                    }

                    if ( !result.getString( "passwd_mobile" ).equals( "" ) )
                    {
                        passwd_mobile_hashed = result.getString( "passwd_mobile" );
                        for( int i = 0 ; i < loop ; i++ )
                        {
                            passwd_mobile_hashed = ConvertString.convert2md5( passwd_mobile_hashed );
                        }
                        passwd_mobile_hashed = UserLogin.encrypt( passwd_mobile_hashed, result.getString( "ftp_passwd" ) );
                    }

                    updateDB( connection, result.getString( "hotel_id" ), result.getInt( "userid" ), passwd_pc_hashed, passwd_mobile_hashed );

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
                // Logging.error( "[OwnerUserPasswordConvert.passwordReset()] Exception:" + e.toString() );
            }
        }
    }

    private static boolean updateDB(Connection connection, String hotelId, int userId, String passwdPcHasded, String passwdMobileHashed)
    {

        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "UPDATE owner_user SET";
        query += " passwd_pc_hashed = ?";
        query += ",passwd_mobile_hashed = ?";
        query += " WHERE hotelid = ? AND userid = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, passwdPcHasded );
            prestate.setString( 2, passwdMobileHashed );
            prestate.setString( 3, hotelId );
            prestate.setInt( 4, userId );

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
