package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * �ėp�̃��O�o�̓��C�u�����N���X
 * 
 * @see "Log4J-1.2.8.jar,common-logging.jar,common-logging-api.jar���C�u�������K�v�ł��B<br>
 *      �ݒ�t�@�C����/etc/happyhotel/happyhotel_log4j.xml�ɋL�ڂ��邱��<br>
 *      �o�̓��x���� DEBUG < INFO < WARN < ERROR < FATAL"
 */
public class Logging implements Serializable
{

    private static final long serialVersionUID = 1231420285527351344L;

    static Logger             logger           = Logger.getLogger( LogLib.class.getName() );

    /**
     * LogTest �����������܂��B
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
     * DEBUG���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void debug(String log)
    {
        logger.debug( log );
    }

    /**
     * DEBUG���O�o�̓��\�b�h
     * 
     * @param message �o�̓��O���e
     * @param t �G���[
     */
    public static void debug(Object message, Throwable t)
    {
        logger.debug( message, t );
    }

    /**
     * INFO���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void info(String log)
    {
        logger.info( log );
    }

    /**
     * INFO���O�o�̓��\�b�h
     * 
     * @param message �o�̓��O���e
     * @param t �G���[
     */
    public static void info(Object message, Throwable t)
    {
        logger.info( message, t );
    }

    /**
     * INFO���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
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

            // �X�V�Ώۂ̒l���Z�b�g����
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
     * WARNING���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
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
     * ERROR���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void error(String log)
    {

        logger.error( log );
    }

    /**
     * ERROR���O�o�̓��\�b�h
     * 
     * @param message �o�̓��O���e
     * @param t �G���[
     */
    public static void error(Object message, Throwable t)
    {
        logger.info( message, t );
    }

    /**
     * ERROR���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
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

            // �X�V�Ώۂ̒l���Z�b�g����
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
     * ERROR���O�o�̓��\�b�h
     * 
     * @param Message �o�̓��O���e
     * @param exception �G���[
     */
    public static void error(String Message, Exception exception)
    {

        logger.error( Message, exception );
    }

    /**
     * FATAL���O�o�̓��\�b�h
     * 
     * @param log �o�̓��O���e
     */
    public static void fatal(String log)
    {
        logger.error( log );
    }

}
