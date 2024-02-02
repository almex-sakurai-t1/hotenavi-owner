/*
 * @(#)DataHotelRemarks.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテル最新情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;
import jp.happyhotel.common.*;

/**
 * ホテル最新情報データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/26
 */
public class DataHotelRemarks implements Serializable
{
    private static final long serialVersionUID = 1227831797875555772L;
    private int               id;
    private int               seq;
    private int               dispKind;
    private int               dispNo;
    private int               dispSubNo;
    private int               dispIndex;
    private int               dispFlag;
    private int               startDate;
    private int               endDate;
    private String            dispMessage;

    /**
     * データを初期化します。
     */
    public DataHotelRemarks()
    {
        id = 0;
        seq = 0;
        dispKind = 0;
        dispNo = 0;
        dispSubNo = 0;
        dispIndex = 0;
        dispFlag = 0;
        startDate = 0;
        endDate = 0;
        dispMessage = "";
    }

    public int getDispFlag()
    {
        return dispFlag;
    }

    public int getDispIndex()
    {
        return dispIndex;
    }

    public int getDispKind()
    {
        return dispKind;
    }

    public String getDispMessage()
    {
        return dispMessage;
    }

    public int getDispNo()
    {
        return dispNo;
    }

    public int getDispSubNo()
    {
        return dispSubNo;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getId()
    {
        return id;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public void setDispKind(int dispKind)
    {
        this.dispKind = dispKind;
    }

    public void setDispMessage(String dispMessage)
    {
        this.dispMessage = dispMessage;
    }

    public void setDispNo(int dispNo)
    {
        this.dispNo = dispNo;
    }

    public void setDispSubNo(int dispSubNo)
    {
        this.dispSubNo = dispSubNo;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    /**
     * ホテル最新情報データ取得
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

        query = "SELECT * FROM hh_hotel_remarks WHERE id = ? AND seq = ?";
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
                    this.dispKind = result.getInt( "disp_kind" );
                    this.dispNo = result.getInt( "disp_no" );
                    this.dispSubNo = result.getInt( "disp_sub_no" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.dispMessage = result.getString( "disp_message" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRemarks.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル最新情報データ設定
     * 
     * @param result ホテル最新情報データレコード
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
                this.dispKind = result.getInt( "disp_kind" );
                this.dispNo = result.getInt( "disp_no" );
                this.dispSubNo = result.getInt( "disp_sub_no" );
                this.dispIndex = result.getInt( "disp_index" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.dispMessage = result.getString( "disp_message" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRemarks.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ホテル最新情報データ追加
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

        query = "INSERT hh_hotel_remarks SET ";
        query = query + " id = ?,";
        query = query + " seq = 0,";
        query = query + " disp_kind = ?,";
        query = query + " disp_no = ?,";
        query = query + " disp_sub_no = ?,";
        query = query + " disp_index = ?,";
        query = query + " disp_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " disp_message = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.dispKind );
            prestate.setInt( 3, this.dispNo );
            prestate.setInt( 4, this.dispSubNo );
            prestate.setInt( 5, this.dispIndex );
            prestate.setInt( 6, this.dispFlag );
            prestate.setInt( 7, this.startDate );
            prestate.setInt( 8, this.endDate );
            prestate.setString( 9, this.dispMessage );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRemarks.insertData] Exception=" + e.toString() );
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
     * ホテル最新情報データ更新
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

        query = "UPDATE hh_hotel_remarks SET ";
        query = query + " disp_kind = ?,";
        query = query + " disp_no = ?,";
        query = query + " disp_sub_no = ?,";
        query = query + " disp_index = ?,";
        query = query + " disp_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " disp_message = ?";
        query = query + " WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.dispKind );
            prestate.setInt( 2, this.dispNo );
            prestate.setInt( 3, this.dispSubNo );
            prestate.setInt( 4, this.dispIndex );
            prestate.setInt( 5, this.dispFlag );
            prestate.setInt( 6, this.startDate );
            prestate.setInt( 7, this.endDate );
            prestate.setString( 8, this.dispMessage );
            prestate.setInt( 9, id );
            prestate.setInt( 10, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelRemarks.updateData] Exception=" + e.toString() );
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
