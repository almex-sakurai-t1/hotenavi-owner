package jp.happyhotel.common;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionBatch implements Serializable
{
    static Connection connection = null; // Database connection
    static String     DB_URL;           // URL for database server
    static String     user;             // DB user
    static String     password;         // DB password
    static int        before2Date;      // 2���O���t
    static String     errMsg     = "";
    static String     driver;           // DB driver
    static String     jdbcds;           // DB jdbcds

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
            prop = new Properties();
            // �v���p�e�B�t�@�C����ǂݍ���
            prop.load( propfile );

            // "jdbc.driver"�ɐݒ肳��Ă���l���擾���܂�
            driver = prop.getProperty( "jdbc.driver" );
            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"�ɐݒ肳��Ă���l���擾���܂�
            jdbcds = prop.getProperty( "jdbc.datasource" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "DBConnectionBatch Static Block Error=" + e.toString() );
        }
    }

    /**
     * DB�R�l�N�V�����쐬�N���X
     * 
     * @return
     */
    public static Connection makeConnection()
    {
        Connection connection = null;
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
     * DB�R�l�N�V�����J���N���X
     * 
     * @return
     */
    public static void closeConnection()
    {
        try
        {
            if ( connection != null )
                connection.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }
}
