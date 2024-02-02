package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * 汎用のログ出力ライブラリクラス
 * 
 * @see "Log4J-1.2.8.jar,common-logging.jar,common-logging-api.jarライブラリが必要です。<br>
 *      設定ファイルは/etc/happyhotel/happyhotel_log4j.xmlに記載すること<br>
 *      出力レベルは DEBUG < INFO < WARN < ERROR < FATAL"
 */
public class Logging implements Serializable
{

    private static final long serialVersionUID = 1231420285527351344L;

    static Logger             logger           = Logger.getLogger( LogLib.class.getName() );

    /**
     * LogTest を初期化します。
     */
    static
    {
        System.out.println( "Logging Static Block Called" );
        try
        {
            DOMConfigurator.configure( Constants.configFilesPath + "happyhotel_log4j.xml" );
        }
        catch ( Exception e )
        {
            System.out.println( "Exception in static block of Logging " + e.toString() );
        }
    }

    /**
     * DEBUGログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void debug(String log)
    {
        logger.debug( log );
    }

    /**
     * DEBUGログ出力メソッド
     * 
     * @param message 出力ログ内容
     * @param t エラー
     */
    public static void debug(Object message, Throwable t)
    {
        logger.debug( message, t );
    }

    /**
     * INFOログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void info(String log)
    {
        logger.info( log );
    }

    /**
     * INFOログ出力メソッド
     * 
     * @param message 出力ログ内容
     * @param t エラー
     */
    public static void info(Object message, Throwable t)
    {
        logger.info( message, t );
    }

    /**
     * INFOログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void info(String log, String className)
    {
        logger.info( log );

        int i = 1;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        query = "INSERT error_history SET ";
        query += "class=?";
        query += ", detail=?";
        query += ", error_date=?";
        query += ", error_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, className );
            prestate.setString( i++, log );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * WARNINGログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void warn(String log)
    {
        logger.warn( log );
    }

    public static void warm(Object message, Throwable t)
    {
        logger.info( message, t );
    }

    /**
     * ERRORログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void error(String log)
    {

        logger.error( log );
    }

    /**
     * ERRORログ出力メソッド
     * 
     * @param message 出力ログ内容
     * @param t エラー
     */
    public static void error(Object message, Throwable t)
    {
        logger.info( message, t );
    }

    /**
     * ERRORログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void error(String log, String className)
    {
        logger.info( log );

        int i = 1;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        query = "INSERT error_history SET ";
        query += "class=?";
        query += ", detail=?";
        query += ", error_date=?";
        query += ", error_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, className );
            prestate.setString( i++, log );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * ERRORログ出力メソッド
     * 
     * @param Message 出力ログ内容
     * @param exception エラー
     */
    public static void error(String Message, Exception exception)
    {

        logger.error( Message, exception );
    }

    /**
     * FATALログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void fatal(String log)
    {
        logger.error( log );
    }

}
