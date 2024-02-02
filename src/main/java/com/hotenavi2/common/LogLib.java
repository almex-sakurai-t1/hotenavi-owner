/*
 * @(#)Loglib.java 2.01 2004/03/17
 * Copyright (C) ALMEX Inc. 2004
 * ログ出力汎用クラス
 */

package com.hotenavi2.common;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 汎用のログ出力ライブラリクラス。
 * Log4J-1.2.8.jar,common-logging.jar,common-logging-api.jarライブラリが必要です。
 * 設定ファイルは/etc/hotenavi/hotenavi_log4j.xmlに記載すること
 * 出力レベルは DEBUG < INFO < WARN < ERROR < FATAL
 * 
 * @autho S.Shiiya
 * @version 2.01 2004/03/19
 */
public class LogLib implements Serializable
{
    private static final Logger logger = LoggerFactory.getLogger( Logging.class );

    /**
     * Loglibを初期化します。
     */
    public LogLib()
    {
        try
        {
            // System.out.println( "Logging Static Block Called" );
        }
        catch ( Exception e )
        {
        }
    }

    /**
     * DEBUGログ出力メソッド
     * 
     * @param 出力ログ内容
     */
    public void debug(String log)
    {
        logger.debug( log );
    }

    /**
     * INFOログ出力メソッド
     * 
     * @param 出力ログ内容
     */
    public void info(String log)
    {
        logger.info( log );
    }

    /**
     * WARNINGログ出力メソッド
     * 
     * @param 出力ログ内容
     */
    public void warn(String log)
    {
        logger.warn( log );
    }

    /**
     * ERRORログ出力メソッド
     * 
     * @param 出力ログ内容
     */
    public void error(String log)
    {
        logger.error( log );
        System.out.println( log );
    }

    /**
     * FATALログ出力メソッド
     * 
     * @param 出力ログ内容
     */
    public void fatal(String log)
    {
        logger.error( log );
    }

}
