package com.hotenavi2.common;

import java.io.Serializable;

/**
 * �ėp�̃��O�o�̓��C�u�����N���X�B
 * Log4J-1.2.8.jar,common-logging.jar,common-logging-api.jar���C�u�������K�v�ł��B
 * �ݒ�t�@�C����/etc/hotenavi/hotenavi_log4j.xml�ɋL�ڂ��邱��
 * �o�̓��x���� DEBUG < INFO < WARN < ERROR < FATAL
 */
public class Logging implements Serializable
{

    private static final long serialVersionUID = 1231420285527351344L;

    // static Logger logger = Logger.getLogger( LogLib.class.getName() );

    /**
     * LogTest �����������܂��B
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
     * DEBUG���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void debug(String log)
    {
        // logger.debug( log );
    }

    /**
     * DEBUG���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     * @param t �G���[�I�u�W�F�N�g
     */
    public static void debug(String log, Throwable t)

    {
        // logger.debug( log, t );
    }

    /**
     * INFO���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void info(String log)
    {
        // logger.info( log );
    }

    /**
     * INFO���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     * @param t �G���[�I�u�W�F�N�g
     */
    public static void info(String log, Throwable t)
    {
        // logger.info( log, t );
    }

    /**
     * WARNING���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void warn(String log)
    {
        // logger.warn( log );
    }

    /**
     * WARNING���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     * @param t �G���[�I�u�W�F�N�g
     */
    public static void warn(String log, Throwable t)
    {
        // logger.warn( log, t );
    }

    /**
     * ERROR���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void error(String log)
    {
        System.out.println( log );
    }

    /**
     * ERROR���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     * @param t �G���[�I�u�W�F�N�g
     */
    public static void error(String log, Throwable t)
    {
        // logger.error( log, t );
    }

    /**
     * FATAL���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void fatal(String log)
    {
        // logger.error( log );
    }

    /**
     * FATAL���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void fatal(String log, Throwable t)
    {
        // logger.error( log, t );
    }
}
