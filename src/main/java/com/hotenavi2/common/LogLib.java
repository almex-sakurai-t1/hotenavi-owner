/*
 * @(#)Loglib.java 2.01 2004/03/17
 * Copyright (C) ALMEX Inc. 2004
 * ���O�o�͔ėp�N���X
 */

package com.hotenavi2.common;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * �ėp�̃��O�o�̓��C�u�����N���X�B
 * Log4J-1.2.8.jar,common-logging.jar,common-logging-api.jar���C�u�������K�v�ł��B
 * �ݒ�t�@�C����/etc/hotenavi/hotenavi_log4j.xml�ɋL�ڂ��邱��
 * �o�̓��x���� DEBUG < INFO < WARN < ERROR < FATAL
 * 
 * @autho S.Shiiya
 * @version 2.01 2004/03/19
 */
public class LogLib implements Serializable
{
    private static final Logger logger = LoggerFactory.getLogger( Logging.class );

    /**
     * Loglib�����������܂��B
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
     * DEBUG���O�o�̓��\�b�h
     * 
     * @param �o�̓��O���e
     */
    public void debug(String log)
    {
        logger.debug( log );
    }

    /**
     * INFO���O�o�̓��\�b�h
     * 
     * @param �o�̓��O���e
     */
    public void info(String log)
    {
        logger.info( log );
    }

    /**
     * WARNING���O�o�̓��\�b�h
     * 
     * @param �o�̓��O���e
     */
    public void warn(String log)
    {
        logger.warn( log );
    }

    /**
     * ERROR���O�o�̓��\�b�h
     * 
     * @param �o�̓��O���e
     */
    public void error(String log)
    {
        logger.error( log );
        System.out.println( log );
    }

    /**
     * FATAL���O�o�̓��\�b�h
     * 
     * @param �o�̓��O���e
     */
    public void fatal(String log)
    {
        logger.error( log );
    }

}
