/*
 * @(#)DataHotelAdjustmentHistory.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテル修正履歴データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;
import jp.happyhotel.common.*;

/**
 * ホテル修正履歴データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/16
 */
public class DataHotelAdjustmentHistory implements Serializable
{
    private static final long serialVersionUID = -8724534367321428767L;
    private int               id;
    private int               seq;
    private String            hotelId;
    private int               userId;
    private int               inputDate;
    private int               inputTime;
    private int               editId;
    private int               editSub;
    private String            memo;

    /**
     * データを初期化します。
     */
    public DataHotelAdjustmentHistory()
    {
        id = 0;
        seq = 0;
        hotelId = "";
        userId = 0;
        inputDate = 0;
        inputTime = 0;
        editId = 0;
        editSub = 0;
        memo = "";
    }

    public int getEditId()
    {
        return editId;
    }

    public int getEditSub()
    {
        return editSub;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public int getId()
    {
        return id;
    }

    public int getInputDate()
    {
        return inputDate;
    }

    public int getInputTime()
    {
        return inputTime;
    }

    public String getMemo()
    {
        return memo;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setEditId(int editId)
    {
        this.editId = editId;
    }

    public void setEditSub(int editSub)
    {
        this.editSub = editSub;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setInputDate(int inputDate)
    {
        this.inputDate = inputDate;
    }

    public void setInputTime(int inputTime)
    {
        this.inputTime = inputTime;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    /**
     * ホテル修正履歴データ取得
     * 
     * @param hotelId ホテルコード
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_adjustment_history WHERE id = ? AND seq = ?";

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
                    this.hotelId = result.getString( "hotel_id" );
                    this.userId = result.getInt( "user_id" );
                    this.inputDate = result.getInt( "input_date" );
                    this.inputTime = result.getInt( "input_time" );
                    this.editId = result.getInt( "edit_id" );
                    this.editSub = result.getInt( "edit_sub" );
                    this.memo = result.getString( "memo" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelAdjustmentHistory.getData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテル修正履歴データ設定
     * 
     * @param result ホテル設定情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.hotelId = result.getString( "hotel_id" );
                this.userId = result.getInt( "user_id" );
                this.inputDate = result.getInt( "input_date" );
                this.inputTime = result.getInt( "input_time" );
                this.editId = result.getInt( "edit_id" );
                this.editSub = result.getInt( "edit_sub" );
                this.memo = result.getString( "memo" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelAdjustmentHistory.setData] Exception=" + e.toString() );
        }
        return(ret);
    }

    /**
     * ホテル修正履歴データ追加
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
        query = "INSERT hh_hotel_adjustment_history SET ";
        query = query + " id = ?,";
        query = query + " seq = 0,";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " input_date = ?,";
        query = query + " input_time = ?,";
        query = query + " edit_id = ?,";
        query = query + " edit_sub = ?,";
        query = query + " memo = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.hotelId );
            prestate.setInt( 3, this.userId );
            prestate.setInt( 4, this.inputDate );
            prestate.setInt( 5, this.inputTime );
            prestate.setInt( 6, this.editId );
            prestate.setInt( 7, this.editSub );
            prestate.setString( 8, this.memo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelAdjustmentHistory.insertData] Exception=" + e.toString() );
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
     * ホテル修正履歴データ更新
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

        query = "UPDATE hh_hotel_adjustment_history SET ";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " input_date = ?,";
        query = query + " input_time = ?,";
        query = query + " edit_id = ?,";
        query = query + " edit_sub = ?,";
        query = query + " memo = ?";
        query = query + " WHERE id = ? AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.hotelId );
            prestate.setInt( 2, this.userId );
            prestate.setInt( 3, this.inputDate );
            prestate.setInt( 4, this.inputTime );
            prestate.setInt( 5, this.editId );
            prestate.setInt( 6, this.editSub );
            prestate.setString( 7, this.memo );
            prestate.setInt( 8, id );
            prestate.setInt( 9, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelAdjustmentHistory.updateData] Exception=" + e.toString() );
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
