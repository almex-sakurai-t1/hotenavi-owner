/*
 * @(#)DataHotelRoomMore.java 1.00 2009/08/18
 * Copyright (C) ALMEX Inc. 2009
 * ホテル部屋情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテル部屋情報データ取得クラス
 * 
 * @author N.Ide
 * @version 1.00 2009/08/18
 */

public class DataHotelRoomMore implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 7902840787374140120L;

    private int               id;
    private int               seq;
    private String            roomName;
    private String            roomText;
    private byte[]            roomPicturePc;
    private byte[]            roomPictureGif;
    private byte[]            roomPicturePng;
    private int               dispFlag;
    private String            referName;
    private int               roomNo;
    private int               roomRank;
    private int               prevSeq;
    private String            prevText;
    private int               nextSeq;
    private String            nextText;
    private String            roomNameHost;
    private float             area;
    private String            bed;

    /**
     * データを初期化します。
     */
    public DataHotelRoomMore()
    {
        id = 0;
        seq = 0;
        roomName = "";
        roomText = "";
        roomPicturePc = new byte[0];
        roomPictureGif = new byte[0];
        roomPicturePng = new byte[0];
        dispFlag = 0;
        referName = "";
        roomNo = 0;
        roomRank = 0;
        prevSeq = 0;
        prevText = "";
        nextSeq = 0;
        nextText = "";
        roomNameHost = "";
        area = 0;
        bed = "";
    }

    /** getter **/
    public int getDispFlag()
    {
        return dispFlag;
    }

    public int getId()
    {
        return id;
    }

    public int getNextSeq()
    {
        return nextSeq;
    }

    public String getNextText()
    {
        return nextText;
    }

    public int getPrevSeq()
    {
        return prevSeq;
    }

    public String getPrevText()
    {
        return prevText;
    }

    public String getReferName()
    {
        return referName;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public int getRoomNo()
    {
        return roomNo;
    }

    public byte[] getRoomPictureGif()
    {
        return roomPictureGif;
    }

    public byte[] getRoomPicturePc()
    {
        return roomPicturePc;
    }

    public byte[] getRoomPicturePng()
    {
        return roomPicturePng;
    }

    public int getRoomRank()
    {
        return roomRank;
    }

    public String getRoomText()
    {
        return roomText;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getRoomNameHost()
    {
        return roomNameHost;
    }

    public Float getArea()
    {
        return area;
    }

    public String getBed()
    {
        return bed;
    }

    /** setter **/

    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setNextSeq(int nextSeq)
    {
        this.nextSeq = nextSeq;
    }

    public void setNextText(String nextText)
    {
        this.nextText = nextText;
    }

    public void setPrevSeq(int prevSeq)
    {
        this.prevSeq = prevSeq;
    }

    public void setPrevText(String prevText)
    {
        this.prevText = prevText;
    }

    public void setReferName(String referName)
    {
        this.referName = referName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setRoomNo(int roomNo)
    {
        this.roomNo = roomNo;
    }

    public void setRoomPictureGif(byte[] roomPictureGif)
    {
        this.roomPictureGif = roomPictureGif;
    }

    public void setRoomPicturePc(byte[] roomPicturePc)
    {
        this.roomPicturePc = roomPicturePc;
    }

    public void setRoomPicturePng(byte[] roomPicturePng)
    {
        this.roomPicturePng = roomPicturePng;
    }

    public void setRoomRank(int roomRank)
    {
        this.roomRank = roomRank;
    }

    public void setRoomText(String roomText)
    {
        this.roomText = roomText;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setRoomNameHost(String roomNameHost)
    {
        this.roomNameHost = roomNameHost;
    }

    public void setArea(Float area)
    {
        this.area = area;
    }

    public void setBed(String bed)
    {
        this.bed = bed;
    }

    /**
     * ホテル部屋情報データ取得(seqから)
     * 
     * @param hotelId ホテルコード
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room_more WHERE id = ?";
        query = query + " AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.roomName = result.getString( "room_name" );
                    this.roomText = result.getString( "room_text" );
                    this.roomPicturePc = result.getBytes( "room_picture_pc" );
                    this.roomPictureGif = result.getBytes( "room_picture_gif" );
                    this.roomPicturePng = result.getBytes( "room_picture_png" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.referName = result.getString( "refer_name" );
                    this.roomNo = result.getInt( "room_no" );
                    this.roomRank = result.getInt( "room_rank" );
                    this.prevSeq = result.getInt( "prev_seq" );
                    this.prevText = result.getString( "prev_text" );
                    this.nextSeq = result.getInt( "next_seq" );
                    this.nextText = result.getString( "next_text" );
                    this.roomNameHost = result.getString( "room_name_host" );
                    this.area = result.getFloat( "area" );
                    this.bed = result.getString( "bed" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRoomMore.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル部屋情報データ取得(roomNameから)
     * 
     * @param hotelId ホテルコード
     * @param roomName 部屋番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, String roomName)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_room_more WHERE id = ?";
        query = query + " AND room_name_host = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, roomName );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.roomName = result.getString( "room_name" );
                    this.roomText = result.getString( "room_text" );
                    this.roomPicturePc = result.getBytes( "room_picture_pc" );
                    this.roomPictureGif = result.getBytes( "room_picture_gif" );
                    this.roomPicturePng = result.getBytes( "room_picture_png" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.referName = result.getString( "refer_name" );
                    this.roomNo = result.getInt( "room_no" );
                    this.roomRank = result.getInt( "room_rank" );
                    this.prevSeq = result.getInt( "prev_seq" );
                    this.prevText = result.getString( "prev_text" );
                    this.nextSeq = result.getInt( "next_seq" );
                    this.nextText = result.getString( "next_text" );
                    this.roomNameHost = result.getString( "room_name_host" );
                    this.area = result.getFloat( "area" );
                    this.bed = result.getString( "bed" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRoomMore.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル部屋情報データ設定
     * 
     * @param result ホテル部屋情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.roomName = result.getString( "room_name" );
                this.roomText = result.getString( "room_text" );
                this.roomPicturePc = result.getBytes( "room_picture_pc" );
                this.roomPictureGif = result.getBytes( "room_picture_gif" );
                this.roomPicturePng = result.getBytes( "room_picture_png" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.referName = result.getString( "refer_name" );
                this.roomNo = result.getInt( "room_no" );
                this.roomRank = result.getInt( "room_rank" );
                this.prevSeq = result.getInt( "prev_seq" );
                this.prevText = result.getString( "prev_text" );
                this.nextSeq = result.getInt( "next_seq" );
                this.nextText = result.getString( "next_text" );
                this.roomNameHost = result.getString( "room_name_host" );
                this.area = result.getFloat( "area" );
                this.bed = result.getString( "bed" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRoomMore.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ホテル部屋情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_hotel_room_more SET ";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " room_name = ?,";
        query = query + " room_text = ?,";
        query = query + " room_picture_pc = ?,";
        query = query + " room_picture_gif = ?,";
        query = query + " room_picture_png = ?,";
        query = query + " disp_flag = ?,";
        query = query + " refer_name = ?,";
        query = query + " room_no = ?,";
        query = query + " room_rank = ?,";
        query = query + " prev_seq = ?,";
        query = query + " prev_text = ?,";
        query = query + " next_seq = ?,";
        query = query + " next_text = ?,";
        query = query + " room_name_host = ?";
        query = query + " area = ?";
        query = query + " bed = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.seq );
            prestate.setString( 3, this.roomName );
            prestate.setString( 4, this.roomText );
            prestate.setBytes( 5, this.roomPicturePc );
            prestate.setBytes( 6, this.roomPictureGif );
            prestate.setBytes( 7, this.roomPicturePng );
            prestate.setInt( 8, this.dispFlag );
            prestate.setString( 9, this.referName );
            prestate.setInt( 10, this.roomNo );
            prestate.setInt( 11, this.roomRank );
            prestate.setInt( 12, this.prevSeq );
            prestate.setString( 13, this.prevText );
            prestate.setInt( 14, this.nextSeq );
            prestate.setString( 15, this.nextText );
            prestate.setString( 16, this.roomNameHost );
            prestate.setFloat( 17, this.area );
            prestate.setString( 18, this.bed );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRoomMore.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ホテル部屋情報データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_room_more SET ";
        query = query + " room_name = ?,";
        query = query + " room_text = ?,";
        query = query + " room_picture_pc = ?,";
        query = query + " room_picture_gif = ?,";
        query = query + " room_picture_png = ?,";
        query = query + " disp_flag = ?,";
        query = query + " refer_name = ?,";
        query = query + " room_no = ?,";
        query = query + " room_rank = ?,";
        query = query + " prev_seq = ?,";
        query = query + " prev_text = ?,";
        query = query + " next_seq = ?,";
        query = query + " next_text = ?,";
        query = query + " room_name_host = ?";
        query = query + " area = ?";
        query = query + " bed = ?";
        query = query + " WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.roomName );
            prestate.setString( 2, this.roomText );
            prestate.setBytes( 3, this.roomPicturePc );
            prestate.setBytes( 4, this.roomPictureGif );
            prestate.setBytes( 5, this.roomPicturePng );
            prestate.setInt( 6, this.dispFlag );
            prestate.setString( 7, this.referName );
            prestate.setInt( 8, this.roomNo );
            prestate.setInt( 9, this.roomRank );
            prestate.setInt( 10, this.prevSeq );
            prestate.setString( 11, this.prevText );
            prestate.setInt( 12, this.nextSeq );
            prestate.setString( 13, this.nextText );
            prestate.setString( 14, this.roomNameHost );
            prestate.setFloat( 15, this.area );
            prestate.setString( 16, this.bed );
            prestate.setInt( 17, id );
            prestate.setInt( 18, seq );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRoomMore.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /****
     * 部屋名称取得
     * 
     * @param id
     * @param seq
     * @return 部屋名称（部屋番号だったら○○号室）
     */
    public String getRoomName(int id, int seq)
    {
        boolean ret = false;
        String roomName = "";
        ret = this.getData( id, seq );
        if ( ret != false )
        {
            if ( this.getRoomName().equals( Integer.toString( seq ) ) != false )
            {
                roomName = this.getRoomName() + "号室";
            }
            else
            {
                roomName = this.getRoomName();
            }
        }
        return roomName;
    }

    /**
     * 部屋ランク一覧
     * 
     * @param id
     * @param seq
     * @return
     */
    public List<Integer> getRoomRanks(int id)
    {
        String query = "SELECT room_rank FROM hh_hotel_room_more WHERE id = ? GROUP BY room_rank";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            List<Integer> roomRanks = new ArrayList<Integer>();
            while( result.next() )
            {
                roomRanks.add( result.getInt( "room_rank" ) );
            }
            return roomRanks;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhHotelRoomMore.getRoomRanks] Exception=" + e.toString() );
            return null;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

}
