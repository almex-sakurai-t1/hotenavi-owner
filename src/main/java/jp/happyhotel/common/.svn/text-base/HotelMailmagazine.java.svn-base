/*
 * @(#)HotelMailmagazine.java 1.00 2021/09/22 Copyright (C) ALMEX Inc. 2021 メールマガジン有無チェック汎用クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.data.DataHotelMailmagazine;

/**
 * ホテルメールマガジンチェック
 * 
 * @author T.Sakurai
 * @version 1.00 2021/09/22
 */
public class HotelMailmagazine implements Serializable
{
    private int hotelCount;

    /**
     * データを初期化します。
     */
    public HotelMailmagazine()
    {
        hotelCount = 0;
    }

    private DataHotelMailmagazine[] hotelMailmagazine;

    /** メールマガジン対象ホテル件数取得 **/
    public int getHotelCount()
    {
        return(hotelCount);
    }

    /** メールマガジン対象ホテル取得 **/
    public DataHotelMailmagazine[] getHotelMailmagazine()
    {
        return(hotelMailmagazine);
    }

    /**
     *
     */
    /* private static final long serialVersionUID = 2562595388140081993L; */

    /**
     * ホテルメールマガジン有無
     * 
     * @param hotelId ホテルID
     * @return 処理結果(true:メールマガジンあり,false:メールマガジンなし)
     */
    public boolean isMailMagazine(String hotelId)
    {
        String query;

        boolean ret = false;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        try
        {
            query = "SELECT";
            query += " CASE WHEN hotel_element.start_date IS NULL THEN 0 WHEN hotel_element.start_date <= ? THEN 1 ELSE 0 END isNewHotenavi";
            query += ",CASE WHEN oldhotenavi.hotelid IS NULL THEN 0 ELSE 1 END isOldMailmagazine";
            query += ",CASE WHEN newhotenavi.hotelid IS NULL THEN 0 ELSE 1 END isNewMailmagazine";
            query += " FROM hotel";
            query += " LEFT JOIN hotel_element ON hotel.hotel_id = hotel_element.hotel_id";
            query += " LEFT JOIN (SELECT hotelid FROM menu WHERE `contents` LIKE '%magazine%' AND disp_flg = 1 AND start_date <= ? AND end_date >= ? GROUP BY hotelid) oldhotenavi ON hotel.hotel_id = oldhotenavi.hotelid";
            query += " LEFT JOIN (SELECT hotelid FROM menu_config WHERE `contents` LIKE '%magazine%' AND disp_flg = 1 AND start_date <= ? AND end_date >= ? GROUP BY hotelid) newhotenavi ON  hotel.hotel_id = newhotenavi.hotelid";
            query += " WHERE hotel.plan IN (1,3,4) AND hotel.hotel_id = ?";
            query += " HAVING (isNewHotenavi = 1 AND isNewMailmagazine = 1 OR isNewHotenavi = 0 AND isOldMailmagazine = 1) ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, nowDate );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );
            prestate.setInt( 4, nowDate );
            prestate.setInt( 5, nowDate );
            prestate.setString( 6, hotelId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                ret = result.next();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelMailmagazine.isMailMagazine] hotelId=" + hotelId + ",Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 管理可能ホテルの取得
     * 
     * @param db DbAccessハンドル
     * @param hotelid ﾎﾃﾙID
     * @param userid ユーザID
     * @return 処理結果
     */
    public boolean getManageHotel(String loginHotelId, int ownerUserId)
    {
        String query;
        int count = 0;
        boolean ret = false;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        try
        {
            query = "SELECT";
            query += " hotel.hotel_id";
            query += ",hotel.name";
            query += ",CASE WHEN hotel_element.start_date IS NULL THEN 0 WHEN hotel_element.start_date <= ? THEN 1 ELSE 0 END isNewHotenavi";
            query += ",CASE WHEN oldhotenavi.hotelid IS NULL THEN 0 ELSE 1 END isOldMailmagazine";
            query += ",CASE WHEN newhotenavi.hotelid IS NULL THEN 0 ELSE 1 END isNewMailmagazine";
            query += " FROM hotel";
            query += " INNER JOIN owner_user_hotel ON owner_user_hotel.hotelid = ? AND owner_user_hotel.userid = ? AND owner_user_hotel.accept_hotelid = hotel.hotel_id";
            query += " LEFT JOIN hotel_element ON hotel.hotel_id = hotel_element.hotel_id";
            query += " LEFT JOIN (SELECT hotelid FROM menu WHERE `contents` LIKE '%magazine%' AND disp_flg = 1 AND start_date <= ? AND end_date >= ? GROUP BY hotelid) oldhotenavi ON hotel.hotel_id = oldhotenavi.hotelid";
            query += " LEFT JOIN (SELECT hotelid FROM menu_config WHERE `contents` LIKE '%magazine%' AND disp_flg = 1 AND start_date <= ? AND end_date >= ? GROUP BY hotelid) newhotenavi ON  hotel.hotel_id = newhotenavi.hotelid";
            query += " WHERE hotel.plan IN (1,3,4)";
            query += " HAVING (isNewHotenavi = 1 AND isNewMailmagazine = 1 OR isNewHotenavi = 0 AND isOldMailmagazine = 1) ";
            query += " ORDER BY hotel.sort_num,hotel.hotel_id";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, nowDate );
            prestate.setString( 2, loginHotelId );
            prestate.setInt( 3, ownerUserId );
            prestate.setInt( 4, nowDate );
            prestate.setInt( 5, nowDate );
            prestate.setInt( 6, nowDate );
            prestate.setInt( 7, nowDate );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelMailmagazine = new DataHotelMailmagazine[this.hotelCount];

                for( int i = 0 ; i < this.hotelCount ; i++ )
                {
                    hotelMailmagazine[i] = new DataHotelMailmagazine();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルメールマガジン対象ホテルの取得
                    this.hotelMailmagazine[count].setData( result );
                    count++;
                }
                ret = count == 0 ? false : true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelMailmagazine.getManageHotel] loginHotelId=" + loginHotelId + ",ownerUserId=" + ownerUserId + ",Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

}
