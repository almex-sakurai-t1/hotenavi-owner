/*
 * @(#)UserMap.java 1.00
 * 2007/07/31 Copyright (C) ALMEX Inc. 2007
 * ���[�U�[�}�b�v�擾�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ��Q�ɂ��������[�U�̃|�C���g�c���̎Z�o
 * 
 * 
 * @author Mitsuhasi
 */
public class UserGetNowPoint2 implements Serializable
{
    static Connection         connection = null; // Database connection
    static String             DB_URL;            // URL for database server
    static String             user;              // DB user
    static String             password;          // DB password
    static int                before2Date;       // 2���O���t
    static String             errMsg     = "";

    static ArrayList<String>  userIdList;
    static ArrayList<String>  mailAddrList;
    static ArrayList<String>  mailAddrMobileList;
    static ArrayList<Integer> pointList;

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

        connection = makeConnection();

        System.out.println( "[UserGetNowPoint.main( )] Start" );

        userIdList = new ArrayList<String>();
        mailAddrList = new ArrayList<String>();
        mailAddrMobileList = new ArrayList<String>();
        pointList = new ArrayList<Integer>();

        try
        {
            System.out.println( "[UserGetNowPoint.getNowUserPoint()] Start" );

            // ���[�U�|�C���g�擾����
            getNowUserPoint( connection );

            // CSV�o��
            // System.out.println( "[UserGetNowPoint.DeleteUser()] CSV�o�� " + userIdList.size() + "��" );
            // exportCsv();

        }
        catch ( Exception e )
        {

            System.out.println( "[UserGetNowPoint.main( )] Exception:" + e.toString() );

            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            UserGetNowPoint2.closeConnection();
        }

        if ( errMsg.equals( "" ) )
        {
            errMsg = "���[�U�|�C���g�擾�������I�����܂���";
        }

        System.out.println( "[UserGetNowPoint.main( )] End\r\n" + errMsg );
    }

    /*
     * ���[�U�[�|�C���g�擾
     */
    public static void getNowUserPoint(Connection connection)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int count = 0;

        query = "SELECT basic.user_id";
        query += " FROM hh_user_basic basic";
        query += " INNER JOIN ap_uuid_user uuid ON uuid.user_id = basic.user_id";
        query += " WHERE (((basic.mail_addr <> '' OR basic.mail_addr_mobile <> '' ) and length(basic.passwd)<=5) OR  (basic.mail_addr = '' AND basic.mail_addr_mobile= '' ))";
        query += " AND basic.del_flag = 0 AND basic.passwd <> ''";
        query += " GROUP BY uuid.user_id";

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    count++;
                    insertDB( connection, result.getString( "user_id" ), getNowPoint( connection, result.getString( "user_id" ) ) );

                    if ( count % 10000 == 0 )
                        System.out.println( "[UserNowUserPoint.main( )] count:" + count );
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
                // Logging.error( "[UserDelTemporaryUser.DeletedUser()] Exception:" + e.toString() );
            }
        }
    }

    public static int getNowPoint(Connection connection, String userId)
    {
        int point = 0;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        // �}�C���\�����̊m�F���s���B

        // �ŏI�W�v���ȍ~�̂��̂̂ݎ擾����
        query = "SELECT SUM( hh_user_point_pay.point ) FROM hh_user_point_pay, hh_user_basic";
        query += " WHERE hh_user_point_pay.user_id = ?";
        query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id ";
        query += " AND hh_user_basic.regist_status = 9";
        query += " AND hh_user_basic.del_flag = 0";
        // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
        query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";
        try
        {
            prestate = connection.prepareStatement( query );

            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    point = result.getInt( 1 );
                }
            }

        }
        catch ( Exception e )
        {
            System.out.println( "[UserPointPay.getNowPointPayMember] e.toString()=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(point);
    }

    private static boolean insertDB(Connection connection, String userId, int point)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT user_now_point ";
        query += " SET user_id = '" + userId + "'";
        query += " ,point = " + point;

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
            System.out.println( "[UserGetNowPoint.insertDB()] e.toString()=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);

    }

    public static void exportCsv()
    {

        try
        {

            // �o�̓t�@�C���̍쐬
            FileWriter f = new FileWriter( "/etc/userGetNowPoint.csv", false );
            PrintWriter p = new PrintWriter( new BufferedWriter( f ) );

            // �w�b�_�[���w�肷��
            p.print( "user_id" );
            p.print( "," );
            p.print( "mail_addr" );
            p.print( "," );
            p.print( "mail_addr_mobile" );
            p.print( "," );
            p.print( "point" );
            p.println();

            // ���e���Z�b�g����
            for( int i = 0 ; i < userIdList.size() ; i++ )
            {
                p.print( userIdList.get( i ) );
                p.print( "," );
                p.print( mailAddrList.get( i ) );
                p.print( "," );
                p.print( mailAddrMobileList.get( i ) );
                p.print( "," );
                p.print( pointList.get( i ) );
                p.println(); // ���s
            }

            // �t�@�C���ɏ����o������
            p.close();

            System.out.println( "�t�@�C���o�͊����I" );

        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
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
