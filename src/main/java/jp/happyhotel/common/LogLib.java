/*
 * @(#)Loglib.java 1.00 2007/07/13 Copyright (C) ALMEX Inc. 2007 ログ出力汎用クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * 汎用のログ出力ライブラリクラス。 Log4J-1.2.8.jar,common-logging.jar,common-logging-api.jarライブラリが必要です。 設定ファイルは/etc/happyhotel/happyhotel_log4j.xmlに記載すること 出力レベルは DEBUG < INFO < WARN < ERROR < FATAL
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/13
 */
public class LogLib implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1231420285527351344L;

    // static Logger logger = Logger.getLogger(LogLib.class.getName());

    /**
     * Loglibを初期化します。
     */
    public LogLib()
    {
        try
        {
            DOMConfigurator.configure( Constants.configFilesPath + "happyhotel_log4j.xml" );
        }
        catch ( Exception e )
        {
        }
    }

    /**
     * DEBUGログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public void debug(String log)
    {
        // logger.debug(log);
    }

    /**
     * INFOログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public void info(String log)
    {
        // logger.info(log);
    }

    /**
     * WARNINGログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public void warn(String log)
    {
        // logger.warn(log);
    }

    /**
     * ERRORログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public void error(String log)
    {
        // logger.error(log);
    }

    /**
     * FATALログ出力メソッド
     * 
     * @param log 出力ログ内容
     */
    public void fatal(String log)
    {
        // logger.error(log);
    }

}
