package jp.happyhotel.user;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletOutputStream;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.data.DataUserSp;
import jp.happyhotel.data.DataUserSpHistory;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * 差分チェック情報取得
 * *
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class UserYWalletDiffCheck
{
    static Connection        connection   = null;                   // Database connection
    static PreparedStatement prestate     = null;
    static ResultSet         result       = null;
    static String            DB_URL;                                // URL for database server
    static String            user;                                  // DB user
    static String            password;                              // DB password
    static String            driver;                                // DB driver
    static String            jdbcds;                                // DB jdbcds
    static String            fileDir;                               // File Dir
    private static int       userCount;
    private static String    mailTo;
    private static String    mailFrom;
    private static String    mailSubject;
    private String           errMsg       = "";
    final static String      FTP_HOST     = "ftp.castle.yahoofs.jp";
    final static String      FTP_PATH     = "/check";
    final static String      FTP_USER     = "yoswgw-wg1236";
    final static String      FTP_PASSWORD = "HoKoG5LG";

    static
    {
        try
        {
            DataUserSp[] duSp = null;

            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
            // プロパティファイルを読み込む
            prop.load( propfile );

            // "jdbc.driver"に設定されている値を取得します
            driver = prop.getProperty( "jdbc.driver" );
            // "jdbc.url"に設定されている値を取得します
            DB_URL = prop.getProperty( "jdbc.url" );
            // "jdbc.user"に設定されている値を取得します
            user = prop.getProperty( "jdbc.user" );
            // "jdbc.password"に設定されている値を取得します
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"に設定されている値を取得します
            jdbcds = prop.getProperty( "jdbc.datasource" );

            prop = null;
            propfile.close();

            // フォルダの配置場所の設定情報を取得
            prop = new Properties();
            propfile = new FileInputStream( "/etc/happyhotel/userYWalletDiff.conf" );
            // プロパティファイルを読み込む
            prop.load( propfile );

            // "fileDir"に設定されている値を取得します
            fileDir = prop.getProperty( "fileDir" );

            prop = null;
            propfile.close();

        }
        catch ( Exception e )
        {
            System.out.println( "UserPointPayReflect Static Block Error=" + e.toString() );
        }
    }

    /**
     * メイン
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        boolean ret;
        ret = false;
        String date = "";
        System.out.println( "[UserYWalletDiffCheck.main( )] Start" );
        try
        {
            if ( args.length > 0 )
            {
                date = args[0];
            }
            else
            {
                date = DateEdit.getDate( 2 );
            }
            UserYWalletDiffCheck.execute( date );
        }
        catch ( Exception e )
        {
            System.out.println( "[UserYWalletDiffCheck.main( )]Exception:" + e.toString() );
            e.printStackTrace();
        }
        finally
        {
            UserYWalletDiffCheck.closeConnection();
        }
        System.out.println( "[UserYWalletDiffCheck.main( )] End" );
    }

    /**
     * 差分チェック実行
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public static void execute(String date)
    {

        // final String PID = "123600005";// テスト
        final String PID = "123600006";// 本番
        final String PROP_ID = "1236";
        final String PRODUCT_TYPE = "MS";
        boolean ret = false;
        String strMd5 = "";
        String fileDate;
        String fileTime;
        String fileName = "";
        String filePath = "";
        int i = 0;
        int nContentLength = 0;
        int result = 99;
        int thisMonth = 0;
        int lastMonth = 0;
        DataUserSpHistory[] duSp = null;

        ServletOutputStream stream;

        if ( (date == null) || (date.equals( "" ) != false) || (CheckString.numCheck( date ) == false) )
        {
            return;
        }

        try
        {
            filePath = fileDir;
            // ファイル名生成
            fileName = PROP_ID + "-" + DateEdit.getDate( 2 ) + String.format( "%06d", Integer.parseInt( DateEdit.getTime( 1 ) ) ) + "-" + PRODUCT_TYPE;

            System.out.println( "filename:" + fileName );
            File tsv = new File( filePath + fileName ); // TSVデータファイル

            duSp = UserYWalletDiffCheck.getUserData( Integer.parseInt( date ) );
            if ( duSp != null && duSp.length > 0 )
            {

                thisMonth = DateEdit.addDay( Integer.parseInt( date ), -1 );
                lastMonth = thisMonth / 100 * 100 + 1;

                // 追記モード
                BufferedWriter bw = new BufferedWriter( new FileWriter( tsv, true ) );

                // 新たなデータ行の追加
                for( i = 0 ; i < duSp.length ; i++ )
                {
                    // 課金ユーザか、先月退会したユーザ
                    if ( duSp[i].getChargeFlag() == 1 || (duSp[i].getChargeFlag() == 0 &&
                            duSp[i].getDelDatePay() >= lastMonth && duSp[i].getDelDatePay() <= thisMonth) )
                    {
                        bw.write( duSp[i].getOrderNo() + "\t" + duSp[i].getOpenId() + "\t" +
                                UserYWalletDiffCheck.formatDate( duSp[i].getRegistDatePay(), duSp[i].getRegistTimePay() ) + "\t" + PID );
                        bw.newLine();
                    }
                }
                bw.close();

                // ファイルが出力されたかを確認する
                if ( tsv.exists() != false )
                {
                    strMd5 = UserYWalletDiffCheck.getMd5( filePath + fileName );
                    File newTsv = new File( filePath + fileName + "." + strMd5 );
                    ret = tsv.renameTo( newTsv );
                    fileName += "." + strMd5;
                }
                if ( ret != false )
                {
                    result = UserYWalletDiffCheck.ftp( FTP_HOST, FTP_USER, FTP_PASSWORD, filePath, fileName );
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[ActionYWalletDiffUser.execute()] Exception:" + e.toString() );
        }
        finally
        {
        }
    }

    /**
     * 有効ユーザSPデータ取得（Yahooウォレット）
     * 
     * @param yyyymmdd
     * @return
     */
    private static DataUserSpHistory[] getUserData(int yyyymmdd)
    {
        int i = 0;
        int count = 0;
        int thisMonth = 0;
        int lastMonth = 0;
        DataUserSpHistory[] dusp = null;
        boolean ret = false;
        String[] openId = null;
        int[] seq = null;

        String query;

        try
        {
            System.out.println( "yyyymmdd:" + yyyymmdd );
            thisMonth = DateEdit.addDay( yyyymmdd, -1 );
            lastMonth = thisMonth / 100 * 100 + 1;
            System.out.println( "thisMonth" + thisMonth );
            System.out.println( "lastMonth" + lastMonth );
            if ( lastMonth > 0 && thisMonth > 0 )
            {
                query = "SELECT open_id, MAX(regist_date_pay) as maxDay FROM hh_user_sp_history";
                query += " WHERE carrier_kind=4";
                query += " AND regist_date_pay <= ?";
                query += " AND CHAR_LENGTH( order_no ) = 12";
                query += " AND token IN( 'ST1', 'RM1', 'RM2' )";
                query += " GROUP BY open_id";
                query += " HAVING maxDay <= ?";
                query += " ORDER BY maxDay DESC";

                connection = UserYWalletDiffCheck.makeConnection();
                prestate = connection.prepareStatement( query );

                prestate.setInt( 1, thisMonth );
                prestate.setInt( 2, thisMonth );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        count = result.getRow();
                    }

                    // クラスの配列を用意し、初期化する。
                    dusp = new DataUserSpHistory[count];
                    openId = new String[count];
                    seq = new int[count];
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        openId[i] = new String();
                        dusp[i] = new DataUserSpHistory();
                        openId[i] = result.getString( "open_id" );
                        System.out.println( "openId:" + openId[i] + "seq:" + seq[i] );
                        i++;
                    }
                }

                for( i = 0 ; i < count ; i++ )
                {
                    query = "SELECT * FROM hh_user_sp_history";
                    query += " WHERE carrier_kind=4";
                    query += " AND regist_date_pay <= ?";
                    query += " AND CHAR_LENGTH( order_no ) = 12";
                    query += " AND token IN( 'ST1', 'RM1', 'RM2' )";
                    query += " AND open_id = ?";
                    query += " ORDER BY regist_date_pay DESC, regist_time_pay DESC, del_date_pay DESC, del_time_pay DESC";
                    query += " LIMIT 1";
                    prestate = connection.prepareStatement( query );

                    prestate.setInt( 1, thisMonth );
                    prestate.setString( 2, openId[i] );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        if ( result.next() != false )
                        {
                            dusp[i].setData( result );
                        }
                    }
                }
            }
            System.out.println( "count:" + count );
        }
        catch ( Exception e )
        {
            System.out.println( "[ActionYWalletDiffUser.getUserData()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return dusp;
    }

    /**
     * MD5変換
     * 
     * @param passwd パスワード
     * @return 処理結果(null：MD5変換失敗)
     **/
    private static String getMd5(String filename)
    {

        String strMd5 = "";
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance( "MD5" );
            DigestInputStream inStream = new DigestInputStream(
                    new BufferedInputStream( new FileInputStream( filename ) ), md );

            while( inStream.read() != -1 )
            {
            }
            byte[] digest = md.digest();
            inStream.close();

            for( int i = 0 ; i < digest.length ; i++ )
            {
                strMd5 += String.format( "%02x", digest[i] );
            }

        }
        catch ( Exception e )
        {
            System.out.println( "[ActionYWalletDiffUser.getMD5()] Exception:" + e.toString() );
        }

        return strMd5;
    }

    /**
     * 日付時刻のフォーマット
     * 
     * @param date
     * @param time
     * @return
     */
    private static String formatDate(int date, int time)
    {
        String returnDateTime = "";

        returnDateTime = DateEdit.getDate( 3, date ) + " " + DateEdit.getTime( 0, time );

        return returnDateTime;
    }

    /***
     * 
     * @param host
     * @param user
     * @param password
     * @param path
     * @param filename
     * @return
     */
    private static int ftp(String host, String user, String password, String path, String filename)
    {

        int returnInt = 99;
        FTPClient fp = new FTPClient();
        FileInputStream is = null;
        try
        {

            System.out.println( "FTP LOADED" );

            fp.connect( host );
            if ( !FTPReply.isPositiveCompletion( fp.getReplyCode() ) )
            { // コネクトできたか？
                returnInt = 1;
            }
            if ( fp.login( user, password ) == false )
            { // ログインできたか？
                returnInt = 2;
            }
            fp.enterLocalPassiveMode();

            // ファイル送信
            is = new FileInputStream( path + filename );// クライアント側
            System.out.println( path + filename );

            if ( fp.storeFile( FTP_PATH + "/" + filename, is ) )// サーバー側
            {
                System.out.println( "FTP UPLOAD:" + FTP_PATH + "/" + filename );
                returnInt = 0;
            }
            else
            {
                returnInt = 3;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[ActionYWalletDiffUser.ftp()] Exception:" + e.toString() );
        }
        finally
        {
            try
            {
                fp.disconnect();
            }
            catch ( IOException e )
            {
                // TODO 自動生成された catch ブロック
                System.out.println( "[ActionYWalletDiffUser.ftp()] disconnect IOException:" + e.toString() );
            }
        }
        return returnInt;
    }

    /**
     * DBコネクション作成
     * 
     * @return
     */
    private static Connection makeConnection()
    {
        try
        {
            Class.forName( driver );
            connection = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * DBコネクションリリース
     * 
     * @return
     */
    private static void closeConnection()
    {
        try
        {
            // それぞれを閉じる
            if ( result != null )
            {
                result.close();
            }
            if ( prestate != null )
            {
                prestate.close();
            }
            if ( connection != null )
            {
                connection.close();
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }
}
