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

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserBasic;

/**
 * ���o�^����2���o�߂������o�^��ԃ��[�U���폜����
 * �폜����f�[�^�̓��[�U�[�����e�[�u���ɏ�����
 * 
 * @author T.Sakurai
 */
public class UserDelTemporaryUser implements Serializable
{
    static Connection connection = null; // Database connection
    static String     DB_URL;           // URL for database server
    static String     user;             // DB user
    static String     password;         // DB password
    static int        before2Date;      // 2���O���t
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
            Logging.error( "UserDelTemporaryUser Static Block Error=" + e.toString() );
        }
    }

    /**
     * ���C��
     * 
     * @param args
     */

    public static void main(String[] args)
    {

        String strDate = "";
        int nowDate = 0;
        try
        {
            if ( args != null && args.length > 0 )
            {
                strDate = args[0];
                nowDate = Integer.parseInt( strDate );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserDelTemporaryUser] �R�}���h���C�������̏������s:" + e.toString() );
        }

        if ( nowDate == 0 )
        {
            nowDate = (Integer.parseInt( DateEdit.getDate( 2 ) ));
        }
        System.out.println( "nDate:" + nowDate );

        /*
         * 2���O�̓��t�̎擾
         */
        before2Date = DateEdit.addDay( nowDate, -2 );
        System.out.println( "before2Date :" + before2Date );

        connection = makeConnection();

        System.out.println( "[UserDelTemporaryUser.main( )] Start" );

        try
        {
            // ���o�^����2���o�߂������o�^��ԃ��[�U���폜����
            System.out.println( "[UserDelTemporaryUser.DeleteUser()] Start" );
            UserDelTemporaryUser.DeleteUser();

        }
        catch ( Exception e )
        {
            Logging.error( "[UserDelTemporaryUser.main( )]Exception:" + e.toString() );
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            UserDelTemporaryUser.closeConnection();
        }

        if ( errMsg.equals( "" ) )
        {
            errMsg = "���o�^����2���o�߂������o�^��ԃ��[�U�폜�������I�����܂���";
        }

        System.out.println( "[UserDelTemporaryUser.main( )] End\r\n" + errMsg );
    }

    /*
     * ���o�^����2���o�߂������o�^��ԃ��[�U�폜
     */
    public static void DeleteUser()
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        DataUserBasic dub;

        query = "SELECT hh_user_basic.user_id";
        query += " FROM hh_user_basic ";
        query += " WHERE hh_user_basic.regist_status = 0 ";
        query += "   AND (hh_user_basic.temp_date_pc <= ? ";
        query += "   AND hh_user_basic.temp_date_mobile <= ?)";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, before2Date );
            prestate.setInt( 2, before2Date );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    System.out.println( "[UserDelTemporaryUser.DeleteUser( )] user_id=" + result.getString( "user_id" ) );

                    // DataUserBasic��������
                    dub = new DataUserBasic();
                    dub.deleteData( connection, result.getString( "user_id" ) );
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
                Logging.error( "[UserDelTemporaryUser.DeletedUser()] Exception:" + e.toString() );
            }
        }
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
