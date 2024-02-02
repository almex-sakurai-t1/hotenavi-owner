package jp.happyhotel.sponsor;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.SendMail;

/**
 * ���[�e�[�V�����o�i�[�f�[�^�쐬�N���X
 * 
 * @author S.Tashiro
 * 
 */

public class InsertRandomSponsor
{
    static Connection     con  = null; // Database connection
    static Statement      stmt = null;
    static ResultSet      rs   = null;
    static String         DB_URL;      // URL for database server
    static String         user;        // DB user
    static String         password;    // DB password

    private static int[]  sponsor;
    private static int    sponsorCount;
    private static String mailTo;
    private static String mailFrom;
    private static String mailSubject;

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/random_sponsor.conf" );
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
            Logging.error( "InsertRandomSponsor Static Block Error=" + e.toString() );
        }
    }

    /**
     * ���C��
     * 
     * @param args
     */

    public static void main(String[] args)
    {
        int nCount;
        nCount = 0;

        Logging.info( "[InsertRandomSponsor.insertRandomSponsor( )] Start" );
        try
        {
            nCount = InsertRandomSponsor.insertData();
            if ( nCount > 0 )
            {
                Logging.info( "[InsertRandomSponsor.insertRandomSponsor( )] insert:" + nCount );
            }
            else
            {
                Logging.info( "[InsertRandomSponsor.insertRandomSponsor( )] insert no_record" );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[InsertRandomSponsor.insertRandomSponsor( )]Exception:" + e.toString() );
            e.printStackTrace();
        }
        finally
        {
            InsertRandomSponsor.closeConnection();
        }
        Logging.info( "[InsertRandomSponsor.insertRandomSponsor( )] End" );
    }

    /**
     * ���[�e�[�V�����o�i�[�f�[�^�쐬
     * 
     * @see ������hh_sponsor_data���쐬
     * @return ��������
     * @throws SQLException
     */

    public static int insertData() throws SQLException
    {
        String query;
        String body;
        int nextDay;
        int i;
        int count;
        int result;
        boolean ret;

        ret = false;
        count = 0;
        nextDay = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), 1 );
        body = "";
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
                + " AND ( hh_master_sponsor.start_date <= " + nextDay
                + " AND hh_master_sponsor.end_date >= " + nextDay + " )"
                + " AND hh_master_sponsor.random_disp_flag = 2";

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
                    sponsorCount = rs.getRow();
                }
                // �N���X�̔z���p�ӂ��A����������B
                sponsor = new int[sponsorCount];
                rs.beforeFirst();
                while( rs.next() != false )
                {
                    sponsor[count] = rs.getInt( "sponsor_code" );
                    count++;
                }
                ret = true;
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            ret = false;
            throw e;
        }

        count = 0;
        try
        {
            if ( ret != false )
            {
                // �擾�����f�[�^���C���T�[�g����
                if ( sponsorCount > 0 )
                {
                    // �擾�����f�[�^�������J��Ԃ�
                    for( i = 0 ; i < sponsorCount ; i++ )
                    {
                        result = 0;
                        // �C���T�[�g����N�G���[�̍쐬
                        query = "INSERT hh_sponsor_data SET sponsor_code=" + sponsor[i]
                                + ", addup_date=" + nextDay;

                        // �o�^�Ɏ��s���Ă����̃��R�[�h��o�^���邽��try catch���g��
                        try
                        {
                            result = stmt.executeUpdate( query );
                        }
                        catch ( Exception e )
                        {
                            result = -1;
                            body += "[InsertRandomSponsor.insertData( )] Exception=" + e.toString() + " \r\n\r\n";
                        }
                        // ����������J�E���g�𑝂₷
                        if ( result >= 0 )
                        {
                            count++;
                        }
                    }
                }
                body += "[InsertRandomSponsor.insertData( )] insert:" + count;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[InsertRandomSponsor.insertData( )] Exception=" + e.toString() );
            body += "[InsertRandomSponsor.insertData( )] Exception=" + e.toString() + "\r\n";
        }
        finally
        {
            if ( mailFrom.compareTo( "" ) != 0 && mailTo.compareTo( "" ) != 0 && mailSubject.compareTo( "" ) != 0 )
            {
                try
                {
                    body = new String( body.getBytes( "Shift_JIS" ) );
                    mailSubject += "(" + DateEdit.getDate( 1 ) + ")";
                    SendMail.send( mailFrom, mailTo, mailSubject, body );
                }
                catch ( Exception e )
                {
                    Logging.error( "[InsertRandomSponsor.insertData( )] Exception=" + e.toString() );
                }
            }
        }
        return(count);
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
