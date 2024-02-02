package jp.happyhotel.others;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.servlet.ServletOutputStream;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.data.DataUserSp;
import jp.happyhotel.data.DataYahooVacancy;

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

public class YahooVacancy
{
    static Connection        connection   = null; // Database connection
    static PreparedStatement prestate     = null;
    static ResultSet         result       = null;
    static String            DB_URL;             // URL for database server
    static String            user;               // DB user
    static String            password;           // DB password
    static String            driver;             // DB driver
    static String            jdbcds;             // DB jdbcds
    private static int       userCount;
    private static int       RESULT_UP_NG = 3;
    private static String    mailTo;
    private static String    mailFrom;
    private static String    mailSubject;
    private String           errMsg       = "";
    static String            ftpHost;
    static String            ftpUser;
    static String            ftpPassword;

    static String            ftpPath;
    static String            ftpFilename;

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
            propfile = new FileInputStream( "/etc/happyhotel/yahooVacancy.conf" );
            // プロパティファイルを読み込む
            prop.load( propfile );

            // "ftp_host"に設定されている値を取得します
            ftpHost = prop.getProperty( "ftp_host" );
            // "ftp_user"に設定されている値を取得します
            ftpUser = prop.getProperty( "ftp_user" );
            // "ftp_password"に設定されている値を取得します
            ftpPassword = prop.getProperty( "ftp_password" );
            // "ftp_path"に設定されている値を取得します
            ftpPath = prop.getProperty( "ftp_path" );
            // "ftp_filename"に設定されている値を取得します
            ftpFilename = prop.getProperty( "ftp_filename" );
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
        System.out.println( DateEdit.getDate( 2 ) + "_" + DateEdit.getTime( 1 ) + ": [YahooVacancy.main( )] Start" );
        try
        {
            YahooVacancy.execute( date );
        }
        catch ( Exception e )
        {
            System.out.println( DateEdit.getDate( 2 ) + "_" + DateEdit.getTime( 1 ) + ":[YahooVacancy.main( )]Exception:" + e.toString() );
            e.printStackTrace();
        }
        finally
        {
            YahooVacancy.closeConnection();
        }
        System.out.println( DateEdit.getDate( 2 ) + "_" + DateEdit.getTime( 1 ) + ":[YahooVacancy.main( )] End" );
    }

    /**
     * 差分チェック実行
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public static void execute(String date)
    {

        boolean ret = false;
        String strMd5 = "";
        String fileDate;
        String fileTime;
        int i = 0;
        int nContentLength = 0;
        int result = 99;
        int thisMonth = 0;
        int lastMonth = 0;
        DataYahooVacancy[] dyv = null;

        ServletOutputStream stream;

        NumberFormat nf6 = new DecimalFormat( "000000" );
        Calendar cal;
        cal = Calendar.getInstance();
        int now_year = cal.get( cal.YEAR );
        int now_month = cal.get( cal.MONTH ) + 1;
        int now_day = cal.get( cal.DATE );
        int now_date = now_year * 10000 + now_month * 100 + now_day;

        // 2日前　※これを過ぎたものは不明扱いにする。
        cal.set( now_year, now_month - 1, now_day );
        cal.add( cal.DATE, -2 );
        int before_date = cal.get( cal.YEAR ) * 10000 + (cal.get( cal.MONTH ) + 1) * 100 + cal.get( cal.DATE );
        // 30日前　※これを過ぎたものは書き込まない。
        cal.set( now_year, now_month - 1, now_day );
        cal.add( cal.DATE, -30 );
        int ng_date = cal.get( cal.YEAR ) * 10000 + (cal.get( cal.MONTH ) + 1) * 100 + cal.get( cal.DATE );

        try
        {
            System.out.println( "" + ftpPath );
            dyv = YahooVacancy.getVacancyData( before_date, ng_date );
            if ( dyv != null && dyv.length > 0 )
            {
                FileWriter fw = new FileWriter( ftpPath + ftpFilename, false );
                PrintWriter pw = new PrintWriter( new BufferedWriter( fw ) );

                pw.print( "id," );
                pw.print( "empty," );
                pw.print( "clean," );
                pw.print( "empty_status," );
                pw.print( "last_update," );
                pw.print( "last_uptime," );
                pw.print( "empty_disp_type," );
                // 最終更新日がbefore_date以下ならば
                pw.print( "mainte_start_date," );
                pw.print( "mainte_start_time," );
                pw.print( "mainte_end_date," );
                pw.print( "mainte_end_time" );
                pw.println();
                for( i = 0 ; i < dyv.length ; i++ )
                {
                    pw.print( dyv[i].getId() + "," );
                    pw.print( dyv[i].getEmpty() + "," );
                    pw.print( dyv[i].getClean() + "," );
                    pw.print( dyv[i].getEmptyStatus() + "," );
                    pw.print( dyv[i].getLastUpdate() + "," );
                    pw.print( nf6.format( dyv[i].getLastUptime() ) + "," );
                    pw.print( dyv[i].getEmptyDispType() + "," );
                    if ( dyv[i].getLastUpdate() <= before_date )
                    {
                        pw.print( dyv[i].getLastUpdate() + "," );
                        pw.print( nf6.format( dyv[i].getLastUptime() ) + "," );
                        pw.print( "99999999," );
                        pw.print( "999999" );
                    }
                    else
                    {
                        pw.print( "," );
                        pw.print( "," );
                        pw.print( "," );
                    }
                    pw.println();
                }
                pw.close();

                result = sendTrans( ftpHost, ftpUser, ftpPassword, ftpPath, ftpFilename );
                System.out.println( DateEdit.getDate( 2 ) + "_" + DateEdit.getTime( 1 ) + ":result:" + result );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[YahooVacancy.execute()] Exception:" + e.toString() );
        }
        finally
        {
        }
    }

    /**
     * 有効な空満データの取得
     * 
     * @param beforeDate 2日前のデータ
     * @param ngDate NG日付
     * @return
     */
    private static DataYahooVacancy[] getVacancyData(int beforeDate, int ngDate)
    {
        int i = 0;
        int count = 0;
        int thisMonth = 0;
        int lastMonth = 0;
        DataYahooVacancy[] dyv = null;
        boolean ret = false;

        String query;

        try
        {
            query = "SELECT HS.id,";
            query += " HS.empty,";
            query += " HS.clean,";
            query += " HS.empty_status,";
            query += " HS.last_update,";
            query += " HS.last_uptime,";
            query += " HM.empty_disp_type";
            query += " FROM ( hh_hotel_master HM INNER JOIN hh_hotel_status HS ON HM.id = HS.id )";
            query += " INNER JOIN hh_hotel_basic HB ON HS.id = HB.id";
            query += " WHERE ( ( ( HS.last_update ) <> 0 )";
            query += " AND ( ( HS.last_update ) > ?)";
            query += " AND ( ( HM.empty_disp_kind ) = 1 )";
            query += " AND ( ( HB.kind ) <= 7 )";
            query += " AND ( ( HB.rank ) >=1 ) )";

            connection = YahooVacancy.makeConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, ngDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                dyv = new DataYahooVacancy[count];
                result.beforeFirst();
                while( result.next() != false )
                {
                    dyv[i] = new DataYahooVacancy();
                    dyv[i].setId( result.getInt( "HS.id" ) );
                    dyv[i].setEmpty( result.getInt( "HS.empty" ) );
                    dyv[i].setClean( result.getInt( "HS.clean" ) );
                    if ( result.getInt( "HS.last_update" ) <= beforeDate )
                    {
                        dyv[i].setEmptyStatus( 0 );
                    }
                    else
                    {
                        dyv[i].setEmptyStatus( result.getInt( "HS.empty_status" ) );
                    }
                    dyv[i].setLastUpdate( result.getInt( "HS.last_update" ) );
                    dyv[i].setLastUptime( result.getInt( "HS.last_uptime" ) );
                    dyv[i].setEmptyDispType( result.getInt( "HM.empty_disp_type" ) );
                    i++;

                }
            }

            System.out.println( "count:" + count );
        }
        catch ( Exception e )
        {
            System.out.println( "[YahooVacancy.getVacancyData()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return dyv;
    }

    public static int sendTrans(String host, String user, String password, String path, String filename)
    {
        FTPClient fp = new FTPClient();
        FileInputStream is = null;
        int result = -1;
        try
        {
            fp.connect( host );
            if ( !FTPReply.isPositiveCompletion( fp.getReplyCode() ) )
            { // コネクトできたか？
                result = 1;
            }
            if ( fp.login( user, password ) == false )
            { // ログインできたか？
                result = 2;
            }
            fp.enterLocalPassiveMode();
            // ファイル送信
            is = new FileInputStream( path + filename );// クライアント側
            if ( fp.storeFile( filename, is ) )// サーバー側
            {
                result = 0;
            }
            else
            {
                result = 3;

                // 一時ファイルを削除する
                fp.deleteFile( ".in.happyhotel_empty.csv." );
                // ファイル再送信
                is = new FileInputStream( path + filename );// クライアント側
                if ( fp.storeFile( filename, is ) )// サーバー側
                {
                    result = 0;
                }
                else
                {
                    result = 3;
                }
            }
        }
        catch ( Exception e )
        {
            // TODO 自動生成された catch ブロック
            System.out.println( "[YahooVacancy.sendTrans()] Exception:" + e.toString() );
            result = -1;
        }
        finally
        {
            try
            {
                fp.disconnect();
            }
            catch ( Exception e )
            {
                // TODO 自動生成された catch ブロック
                System.out.println( "[YahooVacancy.sendTrans()] disconnect Exception:" + e.toString() );
            }
        }
        return(result);
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
            System.out.println( "[YahooVacancy.makeConnection()] Exception:" + e.toString() );
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
            System.out.println( "[YahooVacancy.closeConnection()] Exception:" + e.toString() );
            e.printStackTrace();
        }
    }
}
