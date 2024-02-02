package jp.happyhotel.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hotenavi2.common.Logging;

public class DBConnection implements Serializable
{
    private static final long serialVersionUID = 239312761897519395L;
    private static String     jdbcds           = null;
    private static String     jdbcdsRO         = null;
    /**
     * DataSource Object
     */
    private static DataSource objDS            = null;
    private static Properties prop;
    private static String     driver;
    private static String     url;
    private static String     user;
    private static String     password;

    /**
     * JNDI root context
     */
    private static Context    initCtx          = null;

    static
    {
        try
        {
            File file = new File( "/etc/happyhotel/dbconnect.conf" );
            if (!file.isFile()) {
                file = new File( "c:\\ALMEX\\etc\\hotenavi\\dbconnect.conf" );
            }
            FileInputStream propfile = new FileInputStream( file );
            prop = new Properties();
            // �����ȩ̀�ق��緰�ƒl��ؽĂ�ǂݍ��݂܂�
            prop.load( propfile );
            // "jdbc.driver"�ɐݒ肳��Ă���l���擾���܂�
            driver = prop.getProperty( "jdbc.driver" );
            // "jdbc.url"�ɐݒ肳��Ă���l���擾���܂�
            url = prop.getProperty( "jdbc.url" );
            // "jdbc.user"�ɐݒ肳��Ă���l���擾���܂�
            user = prop.getProperty( "jdbc.user" );
            // "jdbc.url"�ɐݒ肳��Ă���l���擾���܂�
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"�ɐݒ肳��Ă���l���擾���܂�
            jdbcds = prop.getProperty( "jdbc.datasource" );
            propfile.close();
        }
        catch ( Exception e )
        {
            System.out.println( e.toString() );
        }
    }


    /**
     * DB�R�l�N�V������Ԃ�
     * 
     * @return Connection
     * @throws Exception
     */
    public static Connection getConnection() throws Exception
    {
     	Logging.debug( "getConnection called" );
        Connection conn = null;
        try
        {
            Class.forName( driver );
            conn = DriverManager.getConnection( url, user, password );
            if ( conn == null )
            {
                Logging.info( " DBConnection - getConnection() ---> Connection:null" );
            }
            else
            {
                conn.setReadOnly( false );
                conn.setAutoCommit( true );
            }
        }
        catch ( Exception ex )
        {
            Logging.error( " DBConnection - getConnection() ---> connection failed : " + ex.toString() );
            throw ex;
        }
        return conn;

    }

    /**
     * DB�R�l�N�V������Ԃ�
     * 
     * @return Connection
     * @throws Exception
     */
    public static Connection getConnectionRO() throws Exception
    {
        System.out.println( "getConnectionRO called");
        Logging.debug( "getConnectionRO called" );
        Connection conn = null;
        String DB_URL = null; // URL for database server
        String user = null; // DB user
        String password = null; // DB password
        String driver = null; // DB driver

        try
        {
            Properties prop;
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnectRO.conf" );
            prop = new Properties();
            // �����ȩ̀�ق��緰�ƒl��ؽĂ�ǂݍ��݂܂�
            prop.load( propfile );
            // "jdbc.datasource"�ɐݒ肳��Ă���l���擾���܂�
            driver = prop.getProperty( "jdbc.driver" );
            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );
            jdbcdsRO = prop.getProperty( "jdbc.datasource" );
            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "DBConnection dbconnectRO.conf Static Block Error=" + e.toString() );
        }

        try
        {
            // if ( initCtx == null )
            // {
            // initCtx = new InitialContext();
            // }
            // if ( objDS == null )
            // {
            // objDS = (DataSource)initCtx.lookup( jdbcdsRO );
            // }
            Class.forName( driver );
            conn = DriverManager.getConnection( DB_URL, user, password );

            // conn = objDS.getConnection();
            if ( conn == null )
            {
                Logging.info( " DBConnection - getConnectionRO() ---> Connection:null" );
            }
            else
            {
                conn.setReadOnly( true );
                // conn.setAutoCommit( true );
            }
        }
        catch ( Exception ex )
        {
            Logging.error( " DBConnection - getConnectionRO() ---> connection failed : " + ex.toString() );
            throw ex;
        }
        return conn;

    }

    /**
     * DB�R�l�N�V������Ԃ�(�O����Ή�)
     * 
     * @return Connection
     * @throws Exception
     */
    public static Connection getConnectionLVJ() throws Exception
    {
        Logging.debug( "getConnectionLVJ called" );
        Connection conn = null;
        String DB_URL = null; // URL for database server
        String user = null; // DB user
        String password = null; // DB password
        String driver = null; // DB driver

        try
        {
            Properties prop;
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnectLVJ.conf" );
            prop = new Properties();
            // �����ȩ̀�ق��緰�ƒl��ؽĂ�ǂݍ��݂܂�
            prop.load( propfile );
            // "jdbc.datasource"�ɐݒ肳��Ă���l���擾���܂�
            driver = prop.getProperty( "jdbc.driver" );
            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );
            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "DBConnection dbconnectLVJ.conf Static Block Error=" + e.toString() );
        }

        try
        {
            Class.forName( driver );
            conn = DriverManager.getConnection( DB_URL, user, password );

            // conn = objDS.getConnection();
            if ( conn == null )
            {
                Logging.info( " DBConnection - getConnectionLVJ() ---> Connection:null" );
            }
            else
            {
                conn.setReadOnly( false );
                conn.setAutoCommit( true );
            }
        }
        catch ( Exception ex )
        {
            Logging.error( " DBConnection - getConnectionLVJ() ---> connection failed : " + ex.toString() );
            throw ex;
        }
        return conn;

    }

    /**
     * DB�R�l�N�V������Ԃ��iautoCommit��true�Afalse���w��j
     * 
     * @param autoCommit �I�[�g�R�~�b�g�t���O�itrue:�I�[�g�R�~�b�g�Afalse:�I�[�g�R�~�b�g�I�t�j
     * @return Connection
     * @throws Exception
     */
    public static Connection getConnection(boolean autoCommit) throws Exception
    {
        Logging.debug( "getConnection called" );
        Connection conn = null;
        try
        {
            if ( initCtx == null )
            {
                initCtx = new InitialContext();
            }
            if ( objDS == null )
            {
                objDS = (DataSource)initCtx.lookup( jdbcds );
            }
            conn = objDS.getConnection();
            if ( conn == null )
            {
                Logging.info( " DBConnection - getConnection() ---> Connection:null" );
            }
            else
            {
                conn.setReadOnly( false );
                conn.setAutoCommit( autoCommit );
            }
        }
        catch ( Exception ex )
        {
            Logging.error( " DBConnection - getConnection() ---> connection failed : " + ex.toString() );
            throw ex;
        }
        return conn;
    }

    /**
     * DB�R�l�N�V������Ԃ��i�ڑ���̓X���C�u��DB�j
     * 
     * @return Connection
     * @throws Exception
     */
    public static Connection getReadOnlyConnection() throws Exception
    {
        Logging.debug( "getReadOnlyConnection called" );
        Connection conn = null;
        try
        {
            if ( initCtx == null )
            {
                initCtx = new InitialContext();
            }
            if ( objDS == null )
            {
                objDS = (DataSource)initCtx.lookup( jdbcds );
            }
            conn = objDS.getConnection();
            if ( conn == null )
            {
                Logging.info( " DBConnection - getReadOnlyConnection() ---> Connection:null" );
            }
            else
            {
                conn.setReadOnly( true );
            }
        }
        catch ( Exception ex )
        {
            Logging.error( " DBConnection - getReadOnlyConnection() ---> connection failed : " + ex.toString() );
            throw ex;
        }
        return conn;

    }

    /**
     * ���\�[�X�����
     * 
     * @param resultset
     * @param statement
     * @param connection
     */
    public static void releaseResources(ResultSet resultset,
            Statement statement, Connection connection)
    {
        try
        {
            if ( resultset != null )
            {
                resultset.close();
                resultset = null;
            }
            if ( statement != null )
            {
                statement.close();
                statement = null;
            }
            if ( connection != null )
            {
                connection.close();
                connection = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the connection resources" + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the connection resources" + ex.toString() );
        }
    }

    /**
     * �R�l�N�V���������
     * 
     * @param connection
     */
    public static void releaseResources(Connection connection)
    {
        try
        {
            if ( connection != null )
            {
                connection.close();
                connection = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the connection resources" + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the connection resources" + ex.toString() );
        }
    }

    /**
     * ResultSet�I�u�W�F�N�g�����
     * 
     * @param resultset
     */
    public static void releaseResources(ResultSet resultset)
    {
        try
        {
            if ( resultset != null )
            {
                resultset.close();
                resultset = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the resultset " + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the resultset " + ex.toString() );
        }
    }

    /**
     * statement�I�u�W�F�N�g�����
     * 
     * @param statement
     */
    public static void releaseResources(Statement statement)
    {
        try
        {
            if ( statement != null )
            {
                statement.close();
                statement = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the statement " + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the statement " + ex.toString() );
        }
    }

} // end class DBConnection

