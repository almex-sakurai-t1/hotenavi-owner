/*
 * @(#)HotelStatus.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテル最新情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelMessage;

/**
 * ホテル最新情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/26
 */
public class HotelMessage implements Serializable
{
    private static final long serialVersionUID = 6930710001058181862L;
    private DataHotelMessage  hotelMessage;

    /**
     * データを初期化します。
     */
    public HotelMessage()
    {

    }

    /** ホテル最新情報取得 **/
    public DataHotelMessage getHotelMessage()
    {
        return(hotelMessage);
    }

    /**
     * ホテル最新情報取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int today_date = Integer.parseInt( DateEdit.getDate( 2 ) );
        int today_time = Integer.parseInt( DateEdit.getTime( 1 ) );

        // ホテル最新情報の取得
        query = "SELECT * FROM hh_hotel_message WHERE id = ?";
        query = query + " AND del_flag = 0";
        query = query + " AND ( start_date < ? OR ( start_date = ? AND start_time <= ? ))";
        query = query + " AND ( end_date > ? OR ( end_date = ? AND end_time >= ?))";
        query = query + " AND disp_message <> ''";
        query = query + " ORDER BY start_date DESC, start_time DESC, last_update DESC, last_uptime DESC,seq DESC";
        query = query + " LIMIT 1";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, today_date );
            prestate.setInt( 3, today_date );
            prestate.setInt( 4, today_time );
            prestate.setInt( 5, today_date );
            prestate.setInt( 6, today_date );
            prestate.setInt( 7, today_time );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // クラスを初期化する。
                hotelMessage = new DataHotelMessage();
                if ( result.next() != false )
                {
                    // ホテル最新情報の取得
                    this.hotelMessage.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelMessage.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }
}
