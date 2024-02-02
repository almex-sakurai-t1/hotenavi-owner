/*
 * @(#)UserMap.java 1.00
 * 2007/07/31 Copyright (C) ALMEX Inc. 2007
 * ���[�U�[�}�b�v�擾�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import jp.happyhotel.common.ConvertString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.RandomString;

/**
 * ��Q�ɂ��������[�U�̃p�X���[�h�����Z�b�g
 * 
 * 
 * @author Mitsuhasi
 */
public class UserPasswordReset implements Serializable
{
    static Connection connection = null; // Database connection
    static String     DB_URL;           // URL for database server
    static String     user;             // DB user
    static String     password;         // DB password
    static String     errMsg     = "";

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
            prop = new Properties();
            // �v���p�e�B�t�@�C����ǂݍ���
            prop.load( propfile );

            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "UserPasswordReset Static Block Error=" + e.toString() );
        }
    }

    /**
     * ���C��
     * 
     * @param args
     */

    public static void main(String[] args)
    {

        connection = makeConnection();

        System.out.println( "[UserPasswordReset.main( )] Start" );

        try
        {

            // ���[�U�p�X���[�h���Z�b�g����
            UserPasswordReset.passwordReset( connection );

        }
        catch ( Exception e )
        {
            Logging.error( "[UserPasswordReset.main( )]Exception:" + e.toString() );
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            UserPasswordReset.closeConnection();
        }

        if ( errMsg.equals( "" ) )
        {
            errMsg = "���[�U�p�X���[�h���Z�b�g�������I�����܂���";
        }

        System.out.println( "[UserPasswordReset.main( )] End\r\n" + errMsg );
    }

    /*
     * ���[�U�p�X���[�h���Z�b�g
     */
    public static void passwordReset(Connection connection)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;

        query = "SELECT user_id,mail_addr, mail_addr_mobile ";
        query += " FROM hh_user_basic";
        query += " WHERE (mail_addr <> '' OR mail_addr_mobile <> '' ) and length(passwd)<=15 AND length(passwd)>=6";

        try
        {

            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    updateDB( connection, result.getString( "user_id" ) );
                }

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            try
            {
                // ���ꂼ������
                if ( result != null )
                    result.close();
                if ( prestate != null )
                    prestate.close();
            }
            catch ( Exception e )
            {
                // Logging.error( "[UserPasswordReset.passwordReset()] Exception:" + e.toString() );
            }
        }
    }

    private static boolean updateDB(Connection connection, String userId)
    {
        // System.out.println( "[UserPasswordReset.updateDB( )] userId=" + userId );

        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        String pass = RandomString.getRandomString( 16 );
        pass = ConvertString.convert2md5( pass );
        pass = UserLogin.encrypt( pass );
        query = "UPDATE hh_user_basic ";
        query += " SET passwd = '" + pass + "'";
        query += " WHERE user_id ='" + userId + "'";

        try
        {
            prestate = connection.prepareStatement( query );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            // Logging.error( "[DataOtaRsvErrorInfo.updateDB] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            try
            {
                if ( prestate != null )
                    prestate.close();
            }
            catch ( Exception e )
            {
                // Logging.error( "[UserPasswordReset.passwordReset()] Exception:" + e.toString() );
            }
        }

        return(ret);

    }

    /**
     * DB�R�l�N�V�����쐬�N���X
     * 
     * @return
     */
    private static Connection makeConnection()
    {
        try
        {
            Class.forName( "com.mysql.jdbc.Driver" );
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
    private static void closeConnection()
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
