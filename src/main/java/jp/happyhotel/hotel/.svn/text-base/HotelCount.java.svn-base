/*
 * @(#)HotelCount.java 1.00 2008/01/24 Copyright (C) ALMEX Inc. 2018 ホテル件数取得用ファイルクラス
 */
package jp.happyhotel.hotel;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.Url;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelCount;
import jp.happyhotel.data.DataHotelSort;

/**
 * 検索対象ホテル抽出用クラス
 * 
 * @author T.Sakurai
 * @version 1.00 2018/01/25
 */
public class HotelCount implements Serializable
{

    /**
     * 
     */
    private static final long  serialVersionUID  = -1516194319968532100L;
    public final static String IMAGE_PATH        = "/common/images/HB/";
    static Connection          con               = null;                 // Database connection
    static PreparedStatement   stmt              = null;
    static ResultSet           rs                = null;

    static String              DB_URL;                                   // URL for database server
    static String              user;                                     // DB user
    static String              password;                                 // DB password
    static String              driver;                                   // DB driver
    static String              jdbcds;                                   // DB jdbcds

    private final static int   HappieMemberHotel = 3;

    static
    {
        try
        {
            Properties prop = new Properties();
            // Linux環境
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
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
            System.out.println( "HotelCount Static Block Error=" + e.toString() );
        }
    }

    /**
     * メイン
     * 
     */

    public static void main(String[] args)
    {
        try
        {
            System.out.println( "[HotelCount] Start" );
            setData();
        }
        catch ( Exception e )
        {
            Logging.info( e.toString() );
            e.printStackTrace();
        }
        finally
        {
        }
        System.out.println( "[HotelCount] End" );
    }

    /**
     * ホテルカウント情報一括更新
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean setData()
    {
        String query = "";
        query += " SELECT id FROM  hh_hotel_count";
        boolean ret = false;
        // コネクションを作る
        if ( con == null )
        {
            con = makeConnection();
        }
        try
        {
            stmt = con.prepareStatement( query );
            rs = stmt.executeQuery();
            if ( rs != null )
            {
                while( rs.next() != false )
                {
                    ret = setData( con, rs.getInt( "id" ) );
                    if ( ret == false )
                    {
                        break;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCount.setData()] Exception:" + e.toString() );
        }
        finally
        {
            releaseResources( rs, stmt, con );
        }
        return(ret);
    }

    /**
     * ホテルカウント情報個別作成
     * 
     * @param id ホテルID
     * @param updateFlag ホテル情報更新フラグ true:更新する
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean setData(int id)
    {
        boolean ret = false;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            ret = setData( connection, id );
        }
        catch ( Exception e )
        {
            Logging.info( e.toString() );
            e.printStackTrace();
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return ret;
    }

    /**
     * ホテルカウント情報個別作成
     * 
     * @param id ホテルID
     * @param updateFlag ホテル情報更新フラグ true:更新する
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean setData(Connection connection, int id)
    {
        boolean isHotel = false;
        boolean ret = false;
        /* hh_hotel_countの有無調査 */
        DataHotelCount dhc = new DataHotelCount();
        /* hh_hotel_basicの有無調査 */
        DataHotelBasic dhb = new DataHotelBasic();

        if ( dhb.getData( connection, id ) )
        {
            isHotel = true;
            if ( id >= 100000000 )
            {
                isHotel = false;
            }
            else if ( dhb.getPrefId() == 0 )
            {
                isHotel = false;
            }
            else if ( dhb.getKind() > 7 )
            {
                isHotel = false;
            }
        }
        else
        {
            isHotel = false;
        }

        if ( dhc.getData( connection, id ) )
        {
            if ( isHotel )
            {
                // dhc.setSortKey( getSortKey( connection, id ) );
                dhc.setPrefId( dhb.getPrefId() );
                dhc.setMileFlag( isMile( connection, id ) ? 1 : 0 );
                // dhc.setCouponFlag( isCoupon( connection, id ) ? 1 : 0 );
                dhc.setReserveFlag( isReserve( connection, id ) ? 1 : 0 );
                // dhc.setStationFlag( isStation( connection, id ) ? 1 : 0 );
                // dhc.setIcFlag( isIc( connection, id ) ? 1 : 0 );
                // dhc.setAreaFlag( isArea( connection, id ) ? 1 : 0 );
                // if ( dhc.getHotelImg().equals( "" ) )
                // {
                // dhc.setHotelImg( getHotelImg( id, dhb.getRank() ) );
                // }
                dhc.setHotenaviFlag( !dhb.getUrl().equals( "" ) ? 1 : 0 );
                dhc.setCardlessFlag( isCardless( connection, id ) ? 1 : 0 );
                ret = dhc.updateData( connection, id );
            }
            else
            {
                // 掲載対象ではないので削除する
                ret = dhc.deleteData( connection, id );
            }
        }
        else
        {
            if ( isHotel )
            {
                // 新規に書き込む
                dhc.setId( id );
                // dhc.setSortKey( getSortKey( connection, id ) );
                dhc.setPrefId( dhb.getPrefId() );
                dhc.setMileFlag( isMile( connection, id ) ? 1 : 0 );
                // dhc.setCouponFlag( isCoupon( connection, id ) ? 1 : 0 );
                dhc.setReserveFlag( isReserve( connection, id ) ? 1 : 0 );
                // dhc.setStationFlag( isStation( connection, id ) ? 1 : 0 );
                // dhc.setIcFlag( isIc( connection, id ) ? 1 : 0 );
                // dhc.setAreaFlag( isArea( connection, id ) ? 1 : 0 );
                // if ( dhc.getHotelImg().equals( "" ) )
                // {
                // dhc.setHotelImg( getHotelImg( id, dhb.getRank() ) );
                // }
                dhc.setHotenaviFlag( !dhb.getUrl().equals( "" ) ? 1 : 0 );
                dhc.setCardlessFlag( isCardless( connection, id ) ? 1 : 0 );
                ret = dhc.insertData( connection );
            }
            else
            {
                ret = true; // hh_hotel_basic も hh_hotel_count もないので正常
            }
        }
        return ret;
    }

    /**
     * 表示順の取得
     * 
     * @param id ホテルコード
     * @return 処理結果(int sortKey 表示順)
     */
    public static int getSortKey(Connection connection, int id)
    {
        int sortKey = 0;
        DataHotelSort dhs = new DataHotelSort();
        if ( dhs.getData( connection, id, 0 ) )
        {
            sortKey = dhs.getAllPoint();
        }
        return sortKey;
    }

    /**
     * マイル加盟店有無の取得
     * 
     * @param id ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean isMile(Connection connection, int id)
    {
        boolean ret = false;
        DataHotelBasic dhb = new DataHotelBasic();
        if ( dhb.getData( connection, id ) )
        {
            if ( dhb.getRank() >= HappieMemberHotel && dhb.getKind() <= 7 )
            {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * クーポン有無の取得
     * 
     * @param id ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean isCoupon(Connection connection, int id)
    {
        boolean ret = false;
        HotelCoupon hc = new HotelCoupon();
        if ( hc.getMobileCouponData( connection, id ) )
        {
            if ( hc.getHotelCouponCount() >= 1 )
            {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * 予約有無の取得
     * 
     * @param id ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean isReserve(Connection connection, int id)
    {
        boolean ret = false;
        ret = ReserveCommon.isNewReserve( connection, id );
        return ret;
    }

    /**
     * 駅検索対象有無の取得
     * 
     * @param id ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean isStation(Connection connection, int id)
    {
        boolean ret = false;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";
        query += " SELECT count(DISTINCT map.option_4) FROM  hh_hotel_map hotel";
        query += " INNER JOIN hh_map_point map ON hotel.map_id = map.option_4";
        query += " AND map.class_code IN ('521@','522@','523@')";
        query += " WHERE hotel.id = ?";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null && result.next() != false )
            {
                if ( result.getInt( 1 ) != 0 )
                    ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCount.isStation()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * IC検索対象有無の取得
     * 
     * @param id ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean isIc(Connection connection, int id)
    {
        boolean ret = false;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";
        query += " SELECT count(DISTINCT map.option_4) FROM  hh_hotel_map hotel";
        query += " INNER JOIN hh_map_point map ON hotel.map_id = map.option_4";
        query += " AND map.class_code ='416@'";
        query += " WHERE hotel.id = ?";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null && result.next() != false )
            {
                if ( result.getInt( 1 ) != 0 )
                    ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCount.isIc()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * ホテルエリア検索対象有無の取得
     * 
     * @param id ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean isArea(Connection connection, int id)
    {
        boolean ret = false;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";
        query += "SELECT count(*) FROM  hh_hotel_area area WHERE area.id = ?";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null && result.next() != false )
            {
                if ( result.getInt( 1 ) != 0 )
                    ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCount.isArea()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * ホテル外観イメージURLの取得
     * 
     * @param id ホテルコード
     * @return 処理結果(イメージURL)
     */
    public static String getHotelImg(int id, int rank)
    {
        String hotelImg = "";
        if ( rank >= 2 )
        {
            File fileImg = new File( "/happyhotel" + IMAGE_PATH + id + "n.jpg" );

            // リニューアル用の～n.jpgがあるかどうか
            if ( fileImg.exists() == false )
            {
                // 通常の外観画像があるかどうか
                fileImg = new File( "/happyhotel" + IMAGE_PATH + id + "jpg.jpg" );
                if ( fileImg.exists() )
                {
                    hotelImg = IMAGE_PATH + id + "jpg.jpg";
                }
            }
            else
            {
                hotelImg = IMAGE_PATH + id + "n.jpg";
            }
        }
        if ( !hotelImg.equals( "" ) )
        {
            hotelImg = Url.getUrl() + hotelImg;
        }
        return(hotelImg);
    }

    /**
     * カードレス顧客有無の取得
     * 
     * @param id ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean isCardless(Connection connection, int id)
    {
        DataApHotelSetting dahs = new DataApHotelSetting();
        return dahs.getIsCardless( connection, id );
    }

    /**
     * DBコネクション作成クラス
     * 
     * @return
     */
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

    /**
     * リソースを閉じる
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
     * コネクションを閉じる
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
     * ResultSetオブジェクトを閉じる
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
     * statementオブジェクトを閉じる
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
}
