/*
 * @(#)HotelRoom.java 1.00 2009/08/18
 * Copyright (C) ALMEX Inc. 2009
 * ホテル部屋情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelRoomMore;

/**
 * ホテル部屋情報取得クラス
 * 
 * @author N.ide
 * @version 1.00 2009/08/18
 */
public class HotelRoomMore implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID = 6804431645880452624L;

    private int                 m_roomCount;
    private DataHotelRoomMore[] m_hotelRoomMore;

    /**
     * データを初期化します。
     */
    public HotelRoomMore()
    {
        m_roomCount = 0;
    }

    /** ホテル部屋情報件数取得 **/
    public int getHotelRoomCount()
    {
        return(m_roomCount);
    }

    /** ホテル部屋情報取得 **/
    public DataHotelRoomMore[] getHotelRoom()
    {
        return(m_hotelRoomMore);
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

        query = "SELECT * FROM hh_hotel_room_more, hh_hotel_roomrank WHERE hh_hotel_room_more.id = ? ";
        query = query + " AND hh_hotel_room_more.id = hh_hotel_roomrank.id";
        query = query + " AND hh_hotel_room_more.room_rank = hh_hotel_roomrank.room_rank";
        query = query + " AND hh_hotel_roomrank.disp_index != 0";
        if ( roomName.compareTo( "" ) != 0 )
        {
            query = query + " AND hh_hotel_room_more.room_name = ?";
        }
        query = query + " AND hh_hotel_room_more.disp_flag = 1";
        if ( roomName.compareTo( "" ) == 0 )
        {
            query = query + " ORDER BY hh_hotel_room_more.seq";
        }

        count = 0;
        this.m_roomCount = 0;
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
                this.m_hotelRoomMore = new DataHotelRoomMore[this.m_roomCount];
                for( i = 0 ; i < m_roomCount ; i++ )
                {
                    m_hotelRoomMore[i] = new DataHotelRoomMore();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoomMore[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomData()] Exception=" + e.toString() );
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

        query = "SELECT * FROM hh_hotel_room_more,hh_hotel_roomrank WHERE hh_hotel_room_more.id = ? ";
        query = query + " AND hh_hotel_room_more.id = hh_hotel_roomrank.id";
        query = query + " AND hh_hotel_room_more.room_rank = hh_hotel_roomrank.room_rank";
        query = query + " AND hh_hotel_roomrank.disp_index != 0";
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
        this.m_roomCount = 0;
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
                this.m_hotelRoomMore = new DataHotelRoomMore[this.m_roomCount];
                for( i = 0 ; i < m_roomCount ; i++ )
                {
                    m_hotelRoomMore[i] = new DataHotelRoomMore();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoomMore[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomData()] Exception=" + e.toString() );
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
    public boolean getRoomDataByRoomrank(int hotelId, int roomRank)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room_more WHERE id = ? ";
        query = query + " AND room_rank = ?";
        query = query + " AND disp_flag = 1";
        query = query + " ORDER BY seq";

        count = 0;
        this.m_roomCount = 0;
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
                    m_roomCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelRoomMore = new DataHotelRoomMore[this.m_roomCount];
                for( i = 0 ; i < m_roomCount ; i++ )
                {
                    m_hotelRoomMore[i] = new DataHotelRoomMore();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoomMore[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomDataByRoomrank()] Exception=" + e.toString() );
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
    public boolean getRoomImageDataByRoomrank(int hotelId, int roomRank)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room_more WHERE id = ? ";
        query = query + " AND room_rank = ?";
        query = query + " AND disp_flag = 1";
        query = query + " AND refer_name != ''";
        query = query + " ORDER BY seq";

        count = 0;
        this.m_roomCount = 0;
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
                    m_roomCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelRoomMore = new DataHotelRoomMore[this.m_roomCount];
                for( i = 0 ; i < m_roomCount ; i++ )
                {
                    m_hotelRoomMore[i] = new DataHotelRoomMore();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoomMore[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomDataByRoomrank()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ギャラリ情報取得
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getGalleryData(int hotelId, int seq)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room_more,hh_hotel_gallery WHERE hh_hotel_room_more.id = ? ";
        query = query + " AND hh_hotel_room_more.id = hh_hotel_gallery.id";
        query = query + " AND hh_hotel_room_more.room_rank = hh_hotel_gallery.category";
        query = query + " AND hh_hotel_gallery.disp_index != 0";
        if ( seq != 0 )
        {
            query = query + " AND hh_hotel_room_more.seq = ?";
        }
        query = query + " AND disp_flag = 2";
        if ( seq == 0 )
        {
            query = query + " ORDER BY hh_hotel_room_more.seq";
        }

        count = 0;
        this.m_roomCount = 0;
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
                this.m_hotelRoomMore = new DataHotelRoomMore[this.m_roomCount];
                for( i = 0 ; i < m_roomCount ; i++ )
                {
                    m_hotelRoomMore[i] = new DataHotelRoomMore();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoomMore[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 部屋データの順番を取得
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @return 処理結果(0:件数無し、1以上:データあり)
     */
    public int getGalleryNumber(int hotelId, int seq)
    {
        int number = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_room_more.*, hh_hotel_gallery.* FROM hh_hotel_room_more, hh_hotel_gallery"
                + " WHERE hh_hotel_room_more.id = ?"
                + " AND hh_hotel_room_more.id = hh_hotel_gallery.id"
                + " AND hh_hotel_room_more.room_rank = hh_hotel_gallery.category"
                + " AND hh_hotel_room_more.disp_flag = 2"
                + " AND hh_hotel_gallery.disp_index <> 0";
        query += " ORDER BY hh_hotel_gallery.disp_index, hh_hotel_gallery.category, hh_hotel_room_more.seq";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );

            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    // 一致したら行数を取得
                    if ( seq == result.getInt( "seq" ) )
                    {
                        number = result.getRow();
                        break;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getGalleryNumber()] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(number);
    }

    /**
     * ルームギャラリーデータ件数取得
     * 
     * @param hotelId ホテルID
     * @param roomNo 部屋番号
     * @param kind 0:部屋データを含む、1:部屋データを含まない
     * @return 処理結果(0:件数無し、1以上:データあり)
     */
    public int getRoomGalleryCount(int hotelId, int roomNo, int kind)
    {
        int number = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( hotelId <= 0 || roomNo < 0 )
        {
            return(0);
        }

        query = "SELECT COUNT(*) FROM hh_hotel_room_more WHERE id = ?";
        if ( roomNo > 0 )
        {
            query += " AND room_no = ?";
        }
        else
        {
            query += " AND room_no > 0";
        }
        if ( kind == 0 )
        {
            query += " AND disp_flag BETWEEN 1 AND 3";
        }
        else
        {
            query += " AND disp_flag BETWEEN 2 AND 3";
        }
        query += " ORDER BY room_no, seq";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            if ( roomNo > 0 )
            {
                prestate.setInt( 2, roomNo );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    number = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomGalleryCount()] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(number);
    }

    /**
     * ルームギャラリーデータ取得（部屋データは除く）
     * 
     * @param hotelId ホテルID
     * @param roomNo 部屋番号
     * @param kind 0:部屋データを含む、1:部屋データを含まない
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRoomGallery(int hotelId, int roomNo, int kind)
    {
        int i;
        int count;
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( hotelId <= 0 )
        {
            return(false);
        }

        count = 0;
        query = "SELECT * FROM hh_hotel_room_more WHERE id = ?";
        if ( roomNo > 0 )
        {
            query += " AND room_no = ?";
        }
        else if ( roomNo == 0 )
        {
            query += " AND room_no > 0";
        }
        if ( kind == 0 )
        {
            query += " AND disp_flag BETWEEN 1 AND 3";
        }
        else
        {
            query += " AND disp_flag BETWEEN 2 AND 3";
        }
        query += " ORDER BY room_no, seq";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            if ( roomNo > 0 )
            {
                prestate.setInt( 2, roomNo );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        m_roomCount = result.getRow();
                    }

                    // クラスの配列を用意し、初期化する。
                    this.m_hotelRoomMore = new DataHotelRoomMore[this.m_roomCount];
                    for( i = 0 ; i < m_roomCount ; i++ )
                    {
                        m_hotelRoomMore[i] = new DataHotelRoomMore();
                    }

                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ホテル部屋情報の取得
                        this.m_hotelRoomMore[count++].setData( result );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomGallery()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( m_roomCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * ルームギャラリーデータ取得（部屋データは除く）
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @param kind 0:部屋データを含む、1:部屋データを含まない
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRoomGalleryBySeq(int hotelId, int seq, int kind)
    {
        int i;
        int count;
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( hotelId <= 0 || seq <= 0 )
        {
            return(false);
        }

        count = 0;
        query = "SELECT * FROM hh_hotel_room_more WHERE id = ?"
                + " AND seq = ?"
                + " AND room_no > 0";
        if ( kind == 0 )
        {
            query += " AND disp_flag BETWEEN 1 AND 3";
        }
        else
        {
            query += " AND disp_flag BETWEEN 2 AND 3";
        }
        query += " ORDER BY room_no, seq";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        m_roomCount = result.getRow();
                        Logging.info( "[HoteRoomMore.getRoomGalleryBySeq()] Count=" + m_roomCount );
                    }

                    // クラスの配列を用意し、初期化する。
                    this.m_hotelRoomMore = new DataHotelRoomMore[this.m_roomCount];
                    for( i = 0 ; i < m_roomCount ; i++ )
                    {
                        m_hotelRoomMore[i] = new DataHotelRoomMore();
                    }

                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ホテル部屋情報の取得
                        this.m_hotelRoomMore[count++].setData( result );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomGalleryBySeq()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( m_roomCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * ルームギャラリーデータの順番を取得
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @return 処理結果(0:件数無し、1以上:データあり)
     */
    public int getRoomGalleryNumber(int hotelId, int seq)
    {
        int number = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room_more"
                + " WHERE id = ?"
                + " AND room_no > 0"
                + " AND disp_flag BETWEEN 1 AND 3"
                + " ORDER BY room_no, seq";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );

            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    // 一致したら行数を取得
                    if ( seq == result.getInt( "seq" ) )
                    {
                        number = result.getRow();
                        break;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getGalleryNumber()] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(number);
    }

    /**
     * ホテル部屋一覧情報取得（スマフォアプリ用画像があるところだけ）
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRoomImageData(int hotelId, int seq)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room_more,hh_hotel_roomrank WHERE hh_hotel_room_more.id = ? ";
        query = query + " AND hh_hotel_room_more.id = hh_hotel_roomrank.id";
        query = query + " AND hh_hotel_room_more.room_rank = hh_hotel_roomrank.room_rank";
        query = query + " AND hh_hotel_roomrank.disp_index != 0";
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
        this.m_roomCount = 0;
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
                this.m_hotelRoomMore = new DataHotelRoomMore[this.m_roomCount];
                for( i = 0 ; i < m_roomCount ; i++ )
                {
                    m_hotelRoomMore[i] = new DataHotelRoomMore();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoomMore[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomData()] Exception=" + e.toString() );
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
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRoomImageData(int hotelId)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room_more, hh_hotel_roomrank WHERE hh_hotel_room_more.id = ? ";
        query = query + " AND hh_hotel_room_more.id = hh_hotel_roomrank.id";
        query = query + " AND hh_hotel_room_more.room_rank = hh_hotel_roomrank.room_rank";
        query = query + " AND hh_hotel_roomrank.disp_index != 0";
        query = query + " AND hh_hotel_room_more.disp_flag = 1";
        query = query + " AND hh_hotel_room_more.refer_name != ''";
        query = query + " ORDER BY hh_hotel_room_more.seq";

        count = 0;
        this.m_roomCount = 0;
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
                    m_roomCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelRoomMore = new DataHotelRoomMore[this.m_roomCount];
                for( i = 0 ; i < m_roomCount ; i++ )
                {
                    m_hotelRoomMore[i] = new DataHotelRoomMore();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.m_hotelRoomMore[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HoteRoomMore.getRoomImageData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

}
