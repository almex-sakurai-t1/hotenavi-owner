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

/**
 * ��Q�ɂ��������[�U�̃p�X���[�h�����Z�b�g
 * 
 * 
 * @author Mitsuhasi
 */
public class UserPasswordConvert implements Serializable
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
            Logging.error( "UserPasswordConvert Static Block Error=" + e.toString() );
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

        System.out.println( "[UserPasswordConvert.main( )] Start" );

        try
        {

            // ���[�U�p�X���[�h���Z�b�g����
            passwordConvert( connection );

        }
        catch ( Exception e )
        {
            // Logging.error( "[UserPasswordConvert.main( )]Exception:" + e.toString() );
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            UserPasswordConvert.closeConnection();
        }

        if ( errMsg.equals( "" ) )
        {
            errMsg = "���[�U�p�X���[�h�ϊ��������I�����܂���";
        }

        System.out.println( "[UserPasswordConvert.main( )] End\r\n" + errMsg );
    }

    /*
     * ���[�U�p�X���[�h���Z�b�g
     */
    public static void passwordConvert(Connection connection)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;

        int count = 0;
        query = "SELECT user_id,mail_addr, mail_addr_mobile,passwd ";
        query += " FROM hh_user_basic";
        query += " WHERE (((mail_addr <> '' OR mail_addr_mobile <> '' ) and length(passwd)<=5) OR  (mail_addr = '' AND mail_addr_mobile= '' ))";
        query += " AND del_flag = 0 AND passwd<>''";

        try
        {

            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    count++;
                    updateDB( connection, result.getString( "user_id" ), UserLogin.encrypt( ConvertString.convert2md5( result.getString( "passwd" ) ) ) );

                    if ( count % 10000 == 0 )
                        System.out.println( "[UserPasswordConvert.main( )] count:" + count );

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
                // Logging.error( "[UserPasswordConvert.passwordReset()] Exception:" + e.toString() );
            }
        }
    }

    private static boolean updateDB(Connection connection, String userId, String pass)
    {
        // System.out.println( "[UserPasswordConvert.updateDB( )] userId=" + userId );

        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

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
            ret = false;
        }
        try
        {
            if ( prestate != null )
                prestate.close();
        }
        catch ( Exception e )
        {
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
