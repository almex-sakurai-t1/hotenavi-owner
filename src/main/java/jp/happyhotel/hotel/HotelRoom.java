/*
 * @(#)HotelRoom.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテル部屋情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.*;
import java.sql.*;
import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * ホテル部屋情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/26
 */
public class HotelRoom implements Serializable
{
    private static final long serialVersionUID = -2660121573353454312L;

    private int               m_roomCount;
    private DataHotelRoom[]   m_hotelRoom;

    /**
     * データを初期化します。
     */
    public HotelRoom()
    {
        m_roomCount = 0;
    }

    /** ホテル部屋情報件数取得 **/
    public int getHotelRoomCount()
    {
        return(m_roomCount);
    }

    /** ホテル部屋情報取得 **/
    public DataHotelRoom[] getHotelRoom()
    {
        return(m_hotelRoom);
    }

    /**
     * ホテル部屋一覧情報取得
     * 
     * @param hotelId ホテルID
     * @param roomName 部屋名称("":全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRoomData(int hotelId, String roomName)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room WHERE id = ? ";

        if ( roomName.compareTo( "" ) != 0 )
        {
            query = query + " AND room_name = ?";
        }
        query = query + " AND disp_flag = 1";
        if ( roomName.compareTo( "" ) == 0 )
        {
            query = query + " ORDER BY seq";
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            if ( roomName.compareTo( "" ) != 0 )
            {
                prestate.setString( 2, roomName );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_roomCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelRoom = new DataHotelRoom[this.m_roomCount];
                for( i = 0 ; i < m_roomCount ; i++ )
                {
                    m_hotelRoom[i] = new DataHotelRoom();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoom[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getRoomData] Exception=" + e.toString() );
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
     * @param seq 管理番号(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRoomData(int hotelId, int seq)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room WHERE id = ? ";

        if ( seq != 0 )
        {
            query = query + " AND seq = ?";
        }
        query = query + " AND disp_flag = 1";
        if ( seq == 0 )
        {
            query = query + " ORDER BY seq";
        }

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            if ( seq != 0 )
            {
                prestate.setInt( 2, seq );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_roomCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelRoom = new DataHotelRoom[this.m_roomCount];
                for( i = 0 ; i < m_roomCount ; i++ )
                {
                    m_hotelRoom[i] = new DataHotelRoom();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoom[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getRoomData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }
}
