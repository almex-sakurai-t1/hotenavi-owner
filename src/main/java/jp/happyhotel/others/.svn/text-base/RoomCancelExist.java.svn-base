package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;

/**
 * 当日以降の予約キャンセル分が在庫割り当てを検索するクラス
 * 
 * @author Keion.Park
 * 
 */
public class RoomCancelExist
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
            // Linux環境
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
            // windows環境
            // FileInputStream propfile = new FileInputStream( "C:\\ALMEX\\WWW\\WEB-INF\\dbconnect.conf" );
            prop = new Properties();
            // プロパティファイルを読み込む
            prop.load( propfile );

            // "jdbc.driver"に設定されている値を取得します
            driver = prop.getProperty( "jdbc.driver" );

            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"に設定されている値を取得します
            jdbcds = prop.getProperty( "jdbc.datasource" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            System.out.println( "CollectHotelUniquePv Static Block Error=" + e.toString() );
        }
    }

    private static Connection makeConnection()
    {
        Connection conn = null;
        try
        {
            Class.forName( driver );
            conn = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) throws Exception
    {
        isCancelRoomExist( makeConnection() );
    }

    /**
     * 当日以降の予約キャンセル分が在庫割り当てされていないかを検索
     * 
     * @param connection
     * @throws Exception
     */
    public static void isCancelRoomExist(Connection connection) throws Exception
    {
        String query = "";
        int today = Integer.parseInt( DateEdit.getDate( 2 ) );
        query = " SELECT rsv.id,rsv.reserve_no,rsv.reserve_date ";
        query += " FROM newRsvDB.hh_rsv_reserve rsv ";
        query += " INNER JOIN newRsvDB.hh_rsv_room_remainder rr ";
        query += " ON rr.id = rsv.id ";
        query += " AND rr.cal_date = rsv.reserve_date ";
        query += " AND (rr.stay_reserve_no = rsv.reserve_no OR rr.rest_reserve_no = rsv.reserve_no) ";
        query += "  WHERE rsv.status = 3 AND rsv.reserve_date >= ?";

        int id = 0;
        int reserveDate = 0;
        String reserveNumber = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, today );
            result = prestate.executeQuery();
            while( result.next() )
            {
                id = result.getInt( "id" );
                reserveDate = result.getInt( "reserve_date" );
                reserveNumber = result.getString( "reserve_no" );
                updateRoomData( id, reserveDate, reserveNumber, connection );
            }
        }

        catch ( Exception e )
        {
            System.out.println( "[RoomCancelExist.isCancelRoomExist] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * hh_rsv_room_remainder から予約Noを消す
     * 
     * @param id ホテルID
     * @param reserveDate 予約日
     * @parem reserveNumber 予約番号
     * @parem connection
     * @throws Exception
     */
    public static boolean updateRoomData(int id, int reserveDate, String reserveNumber, Connection connection)
    {
        String query = "";
        query = " UPDATE newRsvDB.hh_rsv_room_remainder ";
        query += " SET stay_reserve_no ='' ";
        query += " WHERE id=? ";
        query += " AND cal_date=? ";
        query += " AND stay_reserve_no=? ";

        PreparedStatement prestate = null;
        int result;
        boolean ret = false;
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, reserveDate );
            prestate.setString( 3, reserveNumber );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[RoomCancelExist.updateRoomData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return ret;
    }

}
