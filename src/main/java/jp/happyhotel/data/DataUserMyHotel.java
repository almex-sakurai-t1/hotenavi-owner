/*
 * @(#)DataUserMyHotel.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザマイホテル情報取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザマイホテル情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/27
 */
public class DataUserMyHotel implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 1858755464141698808L;

    private String            userId;
    private int               hotelId;
    private int               appendDate;
    private int               appendTime;
    private int               dispDate;
    private int               dispTime;
    private int               delFlag;
    private int               pushFlag;

    /**
     * データを初期化します。
     */
    public DataUserMyHotel()
    {
        userId = "";
        hotelId = 0;
        appendDate = 0;
        appendTime = 0;
        dispDate = 0;
        dispTime = 0;
        delFlag = 0;
        pushFlag = 1;
    }

    public int getAppendDate()
    {
        return appendDate;
    }

    public int getAppendTime()
    {
        return appendTime;
    }

    public int getDispDate()
    {
        return dispDate;
    }

    public int getDispTime()
    {
        return dispTime;
    }

    public int getHotelId()
    {
        return hotelId;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getPushFlag()
    {
        return pushFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setAppendDate(int appendDate)
    {
        this.appendDate = appendDate;
    }

    public void setAppendTime(int appendTime)
    {
        this.appendTime = appendTime;
    }

    public void setDispDate(int dispDate)
    {
        this.dispDate = dispDate;
    }

    public void setDispTime(int dispTime)
    {
        this.dispTime = dispTime;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setPushFlag(int pushFlag)
    {
        this.pushFlag = pushFlag;
    }

    /**
     * ユーザマイホテルデータ取得
     * 
     * @param userId ユーザ基本データ
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int hotelId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_myhotel WHERE user_id = ? AND id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.hotelId = result.getInt( "id" );
                    this.appendDate = result.getInt( "append_date" );
                    this.appendTime = result.getInt( "append_time" );
                    this.dispDate = result.getInt( "disp_date" );
                    this.dispTime = result.getInt( "disp_time" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.pushFlag = result.getInt( "push_flag" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMyHotel.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );

        }
        return(ret);
    }

    /**
     * ユーザマイホテルデータ設定
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.hotelId = result.getInt( "id" );
                this.appendDate = result.getInt( "append_date" );
                this.appendTime = result.getInt( "append_time" );
                this.dispDate = result.getInt( "disp_date" );
                this.dispTime = result.getInt( "disp_time" );
                this.delFlag = result.getInt( "del_flag" );
                this.pushFlag = result.getInt( "push_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMyHotel.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * マイホテル情報データ追加
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

        query = "INSERT hh_user_myhotel SET ";
        query = query + " user_id = ?,";
        query = query + " id = ?,";
        query = query + " append_date = ?,";
        query = query + " append_time = ?,";
        query = query + " disp_date = ?,";
        query = query + " disp_time = ?,";
        query = query + " del_flag = ?,";
        query = query + " push_flag = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.hotelId );
            prestate.setInt( 3, this.appendDate );
            prestate.setInt( 4, this.appendTime );
            prestate.setInt( 5, this.dispDate );
            prestate.setInt( 6, this.dispTime );
            prestate.setInt( 7, this.delFlag );
            prestate.setInt( 8, this.pushFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMyHotel.insertData] Exception=" + e.toString() );
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
     * マイホテル情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int id)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_myhotel SET ";
        query = query + " append_date = ?,";
        query = query + " append_time = ?,";
        query = query + " disp_date = ?,";
        query = query + " disp_time = ?,";
        query = query + " del_flag = ?,";
        query = query + " push_flag = ? ";
        query = query + " WHERE user_id = ? AND id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.appendDate );
            prestate.setInt( 2, this.appendTime );
            prestate.setInt( 3, this.dispDate );
            prestate.setInt( 4, this.dispTime );
            prestate.setInt( 5, this.delFlag );
            prestate.setInt( 6, this.pushFlag );
            prestate.setString( 7, userId );
            prestate.setInt( 8, id );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMyHotel.updateData] Exception=" + e.toString() );
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
