package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.hotel.HotelCountGCP;

/**
 * �z�e���f�[�^�W�v�����o�b�`
 * 
 * @author paku-k1
 */
public class HotelDataProcess
{
    static String DB_URL;  // URL for database server
    static String user;    // DB user
    static String password; // DB password
    static String driver;  // DB driver
    static String jdbcds;  // DB jdbcds

    static
    {
        try
        {
            Properties prop = new Properties();
            // Linux��
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
            // windows��
            // FileInputStream propfile = new FileInputStream( "C:\\ALMEX\\WWW\\WEB-INF\\dbconnect.conf" );
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
            System.out.println( "CollectHotelSort Static Block Error=" + e.toString() );
        }
    }

    private static Connection makeConnection()
    {
        Connection conn = null;
        Properties props = new Properties();
        props.setProperty( "user", user );
        props.setProperty( "password", password );
        props.setProperty( "rewriteBatchedStatements", "true" );
        try
        {
            Class.forName( driver );
            conn = DriverManager.getConnection( DB_URL, props );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args)
    {
        Connection connection = makeConnection();
        boolean countSuccess = false;
        boolean pvSuccess = false;
        boolean sortSuccess = false;
        int yesterday = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );
        String strDate;
        try
        {
            if ( args != null && args.length > 0 )
            {
                strDate = args[0];
                yesterday = Integer.parseInt( strDate );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDataProcess] �R�}���h���C�������̏������s:" + e.toString() );
        }

        try
        {
            Logging.info( "START CollectPvData" );
            // 1.hotel_pv�W�v����
            pvSuccess = CollectHotelPv.updateHotelPv( connection, yesterday );
            if ( pvSuccess )
            {
                // 2.hotel_sort�W�v����
                sortSuccess = CollectHotelSortGCP.updateHotelSort( connection, yesterday );
            }
            if ( sortSuccess )
            {
                // 3.hh_hotel_count�W�v����
                countSuccess = HotelCountGCP.setData( connection );
            }
            if ( countSuccess )
            {
                Logging.info( "CollectPvData Success!" );
            }
            else
            {
                Logging.info( "CollectPvData failed!" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CollectPvData] Exception=" + e.toString() );
            e.printStackTrace();
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        Logging.info( "END CollectPvData" );
    }
}
