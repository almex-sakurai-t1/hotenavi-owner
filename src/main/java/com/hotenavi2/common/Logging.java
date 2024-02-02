package com.hotenavi2.common;

import java.io.Serializable;

/**
 * 汎用のログ出力ライブラリクラス。
 * Log4J-1.2.8.jar,common-logging.jar,common-logging-api.jarライブラリが必要です。
 * 設定ファイルは/etc/hotenavi/hotenavi_log4j.xmlに記載すること
 * 出力レベルは DEBUG < INFO < WARN < ERROR < FATAL
 */
public class Logging implements Serializable
{

    private static final long serialVersionUID = 1231420285527351344L;

    // static Logger logger = Logger.getLogger( LogLib.class.getName() );

    /**
     * LogTest を初期化します。
     */
    static
    {
        System.out.println( "Logging Static Block Called" );
        try
        {
            // DOMConfigurator.configure( "/etc/hotenavi/hotenavi_log4j.xml" );
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
        // logger.debug( log );
    }

    /**
     * DEBUGログ出力メソッド
     * 
     * @param log 出力ログ内容
     * @param t エラーオブジェクト
     */
    public static void debug(String log, Throwable t)

    {
        // logger.debug( log, t );
    }

    /**
     * INFOログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void info(String log)
    {
        // logger.info( log );
    }

    /**
     * INFOログ出力メソッド
     * 
     * @param log 出力ログ内容
     * @param t エラーオブジェクト
     */
    public static void info(String log, Throwable t)
    {
        // logger.info( log, t );
    }

    /**
     * WARNINGログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void warn(String log)
    {
        // logger.warn( log );
    }

    /**
     * WARNINGログ出力メソッド
     * 
     * @param log 出力ログ内容
     * @param t エラーオブジェクト
     */
    public static void warn(String log, Throwable t)
    {
        // logger.warn( log, t );
    }

    /**
     * ERRORログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void error(String log)
    {
        System.out.println( log );
    }

    /**
     * ERRORログ出力メソッド
     * 
     * @param log 出力ログ内容
     * @param t エラーオブジェクト
     */
    public static void error(String log, Throwable t)
    {
        // logger.error( log, t );
    }

    /**
     * FATALログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void fatal(String log)
    {
        // logger.error( log );
    }

    /**
     * FATALログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public static void fatal(String log, Throwable t)
    {
        // logger.error( log, t );
    }
}
