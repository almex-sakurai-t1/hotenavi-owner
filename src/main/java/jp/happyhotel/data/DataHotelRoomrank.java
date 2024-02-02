/*
 * @(#)DataHotelRoomMore.java 1.00 2009/08/18
 * Copyright (C) ALMEX Inc. 2009
 * ホテル部屋ランク情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテル部屋ランク情報データ取得クラス
 * 
 * @author N.Ide
 * @version 1.00 2009/08/18
 */

public class DataHotelRoomrank implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -734337290872415402L;

    private int               id;
    private int               roomRank;
    private String            rankName;
    private String            roomText;
    private String            memo;
    private int               dispIndex;
    private String            rankNameEn;
    private String            grade;
    private int               roomTypeCd;
    private int               maxOccupancy;
    private int               maxOccupancyAdult;

    /**
     * データを初期化します。
     */
    public DataHotelRoomrank()
    {
        id = 0;
        roomRank = 0;
        rankName = "";
        roomText = "";
        memo = "";
        dispIndex = 0;
        rankNameEn = "";
        grade = "";
        roomTypeCd = 0;
        maxOccupancy = 0;
        maxOccupancyAdult = 0;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getRoomRank()
    {
        return roomRank;
    }

    public void setRoomRank(int roomRank)
    {
        this.roomRank = roomRank;
    }

    public String getRankName()
    {
        return rankName;
    }

    public void setRankName(String rankName)
    {
        this.rankName = rankName;
    }

    public String getRoomText()
    {
        return roomText;
    }

    public void setRoomText(String roomText)
    {
        this.roomText = roomText;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public int getDispIndex()
    {
        return dispIndex;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public String getRankNameEn()
    {
        return rankNameEn;
    }

    public void setRankNameEn(String rankNameEn)
    {
        this.rankNameEn = rankNameEn;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    public int getRoomTypeCd()
    {
        return roomTypeCd;
    }

    public void setRoomTypeCd(int roomTypeCd)
    {
        this.roomTypeCd = roomTypeCd;
    }

    public int getMaxOccupancy()
    {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy)
    {
        this.maxOccupancy = maxOccupancy;
    }

    public int getMaxOccupancyAdult()
    {
        return maxOccupancyAdult;
    }

    public void setMaxOccupancyAdult(int maxOccupancyAdult)
    {
        this.maxOccupancyAdult = maxOccupancyAdult;
    }

    /**
     * ホテル部屋ランク情報データ取得
     * 
     * @param hotelId ホテルコード
     * @param dispIndex 表示順序
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, int dispIndex)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_roomrank WHERE id = ?";
        query = query + " AND disp_index = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, dispIndex );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.roomRank = result.getInt( "room_rank" );
                    this.rankName = result.getString( "rank_name" );
                    this.roomText = result.getString( "room_text" );
                    this.memo = result.getString( "memo" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.rankNameEn = result.getString( "rank_name_en" );
                    this.grade = result.getString( "grade" );
                    this.roomTypeCd = result.getInt( "room_type_cd" );
                    this.maxOccupancy = result.getInt( "max_occupancy" );
                    this.maxOccupancyAdult = result.getInt( "max_occupancy_adult" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRoomrank.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル部屋ランク情報データ設定
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
                this.roomRank = result.getInt( "room_rank" );
                this.rankName = result.getString( "rank_name" );
                this.roomText = result.getString( "room_text" );
                this.memo = result.getString( "memo" );
                this.dispIndex = result.getInt( "disp_index" );
                this.rankNameEn = result.getString( "rank_name_en" );
                this.grade = result.getString( "grade" );
                this.roomTypeCd = result.getInt( "room_type_cd" );
                this.maxOccupancy = result.getInt( "max_occupancy" );
                this.maxOccupancyAdult = result.getInt( "max_occupancy_adult" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRoomrank.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ホテル部屋ランク情報データ追加
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

        query = "INSERT hh_hotel_roomrank SET ";
        query = query + " id = ?,";
        query = query + " room_rank = ?,";
        query = query + " rank_name = ?,";
        query = query + " room_text = ?,";
        query = query + " memo = ?,";
        query = query + " disp_index = ?";
        query = query + " rank_name_en = ?,";
        query = query + " grade = ?,";
        query = query + " room_type_cd = ?,";
        query = query + " max_occupancy = ?,";
        query = query + " max_occupancy_adult = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.roomRank );
            prestate.setString( 3, this.rankName );
            prestate.setString( 4, this.roomText );
            prestate.setString( 5, this.memo );
            prestate.setInt( 6, this.dispIndex );
            prestate.setString( 7, this.rankNameEn );
            prestate.setString( 8, this.grade );
            prestate.setInt( 9, this.roomTypeCd );
            prestate.setInt( 10, this.maxOccupancy );
            prestate.setInt( 11, this.maxOccupancyAdult );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRoomrank.insertData] Exception=" + e.toString() );
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
     * ホテル部屋ランク情報データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param hotelId ホテルID
     * @param roomrank 部屋ランク
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int hotelId, int roomrank)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_roomrank SET ";
        query = query + " rank_name = ?,";
        query = query + " room_text = ?,";
        query = query + " memo = ?,";
        query = query + " disp_index = ?";
        query = query + " rank_name_en = ?,";
        query = query + " grade = ?,";
        query = query + " room_type_cd = ?,";
        query = query + " max_occupancy = ?,";
        query = query + " max_occupancy_adult = ?";

        query = query + " WHERE id = ? AND room_rank = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.rankName );
            prestate.setString( 2, this.roomText );
            prestate.setString( 3, this.memo );
            prestate.setInt( 4, this.dispIndex );
            prestate.setString( 5, this.rankNameEn );
            prestate.setString( 6, this.grade );
            prestate.setInt( 7, this.roomTypeCd );
            prestate.setInt( 8, this.maxOccupancy );
            prestate.setInt( 9, this.maxOccupancyAdult );
            prestate.setInt( 10, id );
            prestate.setInt( 11, this.roomRank );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRoomrank.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
