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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * ���[�U�[�̕\��
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/30
 */
public class UserPointPayCheckAllUse implements Serializable
{
    private static final long serialVersionUID = -9077082078242062976L;
    static Connection         con              = null;                 // Database connection
    static Statement          stmt             = null;
    static ResultSet          rs               = null;
    static String             DB_URL;                                  // URL for database server
    static String             user;                                    // DB user
    static String             password;                                // DB password
    private static int        userCount;
    private static String     mailTo;
    private static String     mailFrom;
    private static String     mailSubject;
    private String            errMsg           = "";

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/user_point_alluse.conf" );
            prop = new Properties();
            // �v���p�e�B�t�@�C����ǂݍ���
            prop.load( propfile );

            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );
            mailTo = prop.getProperty( "mail.to" );
            mailFrom = prop.getProperty( "mail.from" );
            mailSubject = prop.getProperty( "mail.subject" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "UserPointPayReflect Static Block Error=" + e.toString() );
        }
    }

    /**
     * ���C��
     * 
     * @param args
     */

    public static void main(String[] args)
    {
        boolean ret;
        ret = false;

        Logging.info( "[UserPointPayCheckAllUse.main( )] Start" );
        try
        {
            ret = UserPointPayCheckAllUse.findData( Integer.parseInt( DateEdit.getDate( 2 ) ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointPayCheckAllUse.main( )]Exception:" + e.toString() );
            e.printStackTrace();
        }
        finally
        {
            UserPointPayCheckAllUse.closeConnection();
        }
        Logging.info( "[UserPointPayCheckAllUse.main( )] End" );
    }

    /***
     * ���t��������؂�̃f�[�^��T��
     * 
     * @param date ���t(YYYYMMDD)
     * @return
     */
    public static boolean findData(int date)
    {
        int i;
        int count;
        int errCount;
        int result;
        boolean ret;
        String query;
        String query2;
        String query3;
        String query4;
        String query5;
        String errMsg = "";

        ret = false;
        count = 0;
        errCount = 0;

        query = " SELECT id, seq, MAX(sub_seq) AS subSeq FROM hh_hotel_ci"
                + " WHERE ci_date <= " + date
                + " AND alluse_flag = 1"
                + " AND fix_flag = 0"
                + " GROUP BY id, seq";

        // query = "SELECT * FROM hh_hotel_ci"
        // + " WHERE ci_date <= " + date
        // + " AND alluse_flag = 1"
        // + " AND fix_flag = 0"
        // + " ORDER BY id, seq, sub_seq";

        query2 = "SELECT * FROM hh_hotel_ci WHERE id = ";
        query3 = " AND seq = ";
        query4 = " AND sub_seq = ";
        query5 = " UNION ";

        try
        {
            // �R�l�N�V���������
            con = makeConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery( query );
            if ( rs != null )
            {
                // ���R�[�h�����擾
                if ( rs.last() != false )
                {
                    userCount = rs.getRow();
                }
                rs.beforeFirst();

                if ( userCount > 0 )
                {

                    i = 0;
                    query = "";
                    rs.beforeFirst();
                    while( rs.next() != false )
                    {
                        if ( i > 0 )
                        {
                            query += query5;
                        }
                        query += query2 + rs.getInt( "id" );
                        query += query3 + rs.getInt( "seq" );
                        query += query4 + rs.getInt( "subSeq" );
                        i++;
                    }
                    // TODO
                    Logging.info( query );

                    // TODO
                    Logging.info( "" + userCount );

                    rs = null;
                    rs = stmt.executeQuery( query );

                    if ( rs != null )
                    {
                        // ���R�[�h�����擾
                        if ( rs.last() != false )
                        {
                            userCount = rs.getRow();
                        }

                        if ( userCount > 0 )
                        {
                            // TODO
                            Logging.info( "bb" );

                            i = 0;
                            rs.beforeFirst();
                            while( rs.next() != false )
                            {

                                // TODO
                                Logging.info( "count" + count );
                                errMsg += "id:" + rs.getInt( "id" )
                                        + ", seq=" + rs.getInt( "seq" )
                                        + ", subSeq:" + rs.getInt( "sub_seq" )
                                        + ", userId:" + rs.getString( "user_id" ) + "\r\n";
                                count++;
                            }
                        }
                    }
                }

                if ( errMsg.compareTo( "" ) != 0 )
                {
                    errMsg += "�ȏ�̍s���C���Ώۃf�[�^�ł��B\r\n";
                }

                Logging.info( errMsg );

                if ( errMsg.compareTo( "" ) != 0 )
                {
                    // SendMail.send( mailFrom, mailTo, mailSubject, errMsg );
                }

                ret = true;
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            ret = false;
        }
        return ret;
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
            con = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return con;
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
            // ���ꂼ������
            if ( rs != null )
                rs.close();
            if ( stmt != null )
                stmt.close();
            if ( con != null )
                con.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

}
