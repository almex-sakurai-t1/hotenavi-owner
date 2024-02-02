/*
 * @(#)UserMap.java 1.00
 * 2007/07/31 Copyright (C) ALMEX Inc. 2007
 * ユーザーマップ取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 被害にあったユーザのポイント残数の算出
 * 
 * 
 * @author Mitsuhasi
 */
public class UserGetNowPoint2 implements Serializable
{
    static Connection         connection = null; // Database connection
    static String             DB_URL;            // URL for database server
    static String             user;              // DB user
    static String             password;          // DB password
    static int                before2Date;       // 2日前日付
    static String             errMsg     = "";

    static ArrayList<String>  userIdList;
    static ArrayList<String>  mailAddrList;
    static ArrayList<String>  mailAddrMobileList;
    static ArrayList<Integer> pointList;

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
            Logging.error( "UserDelTemporaryUser Static Block Error=" + e.toString() );
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

        System.out.println( "[UserGetNowPoint.main( )] Start" );

        userIdList = new ArrayList<String>();
        mailAddrList = new ArrayList<String>();
        mailAddrMobileList = new ArrayList<String>();
        pointList = new ArrayList<Integer>();

        try
        {
            System.out.println( "[UserGetNowPoint.getNowUserPoint()] Start" );

            // ユーザポイント取得処理
            getNowUserPoint( connection );

            // CSV出力
            // System.out.println( "[UserGetNowPoint.DeleteUser()] CSV出力 " + userIdList.size() + "件" );
            // exportCsv();

        }
        catch ( Exception e )
        {

            System.out.println( "[UserGetNowPoint.main( )] Exception:" + e.toString() );

            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            UserGetNowPoint2.closeConnection();
        }

        if ( errMsg.equals( "" ) )
        {
            errMsg = "ユーザポイント取得処理が終了しました";
        }

        System.out.println( "[UserGetNowPoint.main( )] End\r\n" + errMsg );
    }

    /*
     * ユーザーポイント取得
     */
    public static void getNowUserPoint(Connection connection)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int count = 0;

        query = "SELECT basic.user_id";
        query += " FROM hh_user_basic basic";
        query += " INNER JOIN ap_uuid_user uuid ON uuid.user_id = basic.user_id";
        query += " WHERE (((basic.mail_addr <> '' OR basic.mail_addr_mobile <> '' ) and length(basic.passwd)<=5) OR  (basic.mail_addr = '' AND basic.mail_addr_mobile= '' ))";
        query += " AND basic.del_flag = 0 AND basic.passwd <> ''";
        query += " GROUP BY uuid.user_id";

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    count++;
                    insertDB( connection, result.getString( "user_id" ), getNowPoint( connection, result.getString( "user_id" ) ) );

                    if ( count % 10000 == 0 )
                        System.out.println( "[UserNowUserPoint.main( )] count:" + count );
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
                // Logging.error( "[UserDelTemporaryUser.DeletedUser()] Exception:" + e.toString() );
            }
        }
    }

    public static int getNowPoint(Connection connection, String userId)
    {
        int point = 0;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        // マイル表示日の確認を行う。

        // 最終集計日以降のもののみ取得する
        query = "SELECT SUM( hh_user_point_pay.point ) FROM hh_user_point_pay, hh_user_basic";
        query += " WHERE hh_user_point_pay.user_id = ?";
        query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id ";
        query += " AND hh_user_basic.regist_status = 9";
        query += " AND hh_user_basic.del_flag = 0";
        // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
        query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";
        try
        {
            prestate = connection.prepareStatement( query );

            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    point = result.getInt( 1 );
                }
            }

        }
        catch ( Exception e )
        {
            System.out.println( "[UserPointPay.getNowPointPayMember] e.toString()=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(point);
    }

    private static boolean insertDB(Connection connection, String userId, int point)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT user_now_point ";
        query += " SET user_id = '" + userId + "'";
        query += " ,point = " + point;

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
            System.out.println( "[UserGetNowPoint.insertDB()] e.toString()=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);

    }

    public static void exportCsv()
    {

        try
        {

            // 出力ファイルの作成
            FileWriter f = new FileWriter( "/etc/userGetNowPoint.csv", false );
            PrintWriter p = new PrintWriter( new BufferedWriter( f ) );

            // ヘッダーを指定する
            p.print( "user_id" );
            p.print( "," );
            p.print( "mail_addr" );
            p.print( "," );
            p.print( "mail_addr_mobile" );
            p.print( "," );
            p.print( "point" );
            p.println();

            // 内容をセットする
            for( int i = 0 ; i < userIdList.size() ; i++ )
            {
                p.print( userIdList.get( i ) );
                p.print( "," );
                p.print( mailAddrList.get( i ) );
                p.print( "," );
                p.print( mailAddrMobileList.get( i ) );
                p.print( "," );
                p.print( pointList.get( i ) );
                p.println(); // 改行
            }

            // ファイルに書き出し閉じる
            p.close();

            System.out.println( "ファイル出力完了！" );

        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }

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
