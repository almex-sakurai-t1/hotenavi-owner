/*
 * @(#)HotelRoom.java 1.00 2009/08/18
 * Copyright (C) ALMEX Inc. 2009
 * ホテル部屋ランク情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelRoomrank;

/**
 * ホテル部屋情報取得クラス
 * 
 * @author N.ide
 * @version 1.00 2009/08/18
 */
public class HotelRoomrank implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID = 4230522511481834929L;

    private int                 m_rankCount;
    private DataHotelRoomrank[] m_hotelRoomrank;

    /** ホテル部屋ランク情報件数取得 **/
    public int getHotelRankCount()
    {
        return(m_rankCount);
    }

    /** ホテル部屋ランク情報取得 **/
    public DataHotelRoomrank[] getHotelRoomrank()
    {
        return(m_hotelRoomrank);
    }

    /**
     * ホテル部屋一覧情報取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRoomrankData(int hotelId)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_roomrank WHERE id = ? ";
        query = query + " AND room_rank != 0";
        query = query + " AND disp_index != 0";
        query = query + " ORDER BY disp_index";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_rankCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelRoomrank = new DataHotelRoomrank[this.m_rankCount];
                for( i = 0 ; i < m_rankCount ; i++ )
                {
                    m_hotelRoomrank[i] = new DataHotelRoomrank();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoomrank[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelRoomrank.getRoomrankData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * ホテル部屋一覧情報取得
     * 
     * @param hotelId ホテルID
     * @param roomRank 部屋ランク
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRoomrankDataByRank(int hotelId, int roomRank)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_roomrank WHERE id = ? ";
        query = query + " AND room_rank = ?";
        query = query + " AND room_rank != 0";
        query = query + " AND disp_index != 0";
        query = query + " ORDER BY disp_index";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, roomRank );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_rankCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelRoomrank = new DataHotelRoomrank[this.m_rankCount];
                for( i = 0 ; i < m_rankCount ; i++ )
                {
                    m_hotelRoomrank[i] = new DataHotelRoomrank();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoomrank[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelRoomrank.getRoomrankDataByRank] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }
}
