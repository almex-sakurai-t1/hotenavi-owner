/*
 * @(#)DataUserBasic.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 予約ユーザ基本情報取得クラス
 * 
 * @author Y.Tanabe
 * @version 1.00 2012/09/20
 * @version
 */
public class DataRsvUserBasic implements Serializable
{
    private static final long serialVersionUID = -5004946880747817567L;

    private String            userId;
    private int               reserveCount;
    private int               cancelCount;
    private int               noshowCount;
    private int               checkinCount;
    private int               lastReseveDate;
    private int               limitationFlag;
    private int               limitationStartDate;
    private int               limitationEndDate;
    private int               lastUpdate;
    private int               lastUptime;
    private int               cardSeq;
    private String            limitationReserveNo;

    /**
     * データを初期化します。
     */
    public DataRsvUserBasic()
    {
        userId = "";
        reserveCount = 0;
        cancelCount = 0;
        noshowCount = 0;
        checkinCount = 0;
        lastReseveDate = 0;
        limitationFlag = 0;
        limitationStartDate = 0;
        limitationEndDate = 0;
        lastUpdate = 0;
        cardSeq = -1;
        limitationReserveNo = "";
    }

    public String getUserId()
    {
        return userId;
    }

    public int getReserveCount()
    {
        return reserveCount;
    }

    public int getCancelCount()
    {
        return cancelCount;
    }

    public int getNoshowCount()
    {
        return noshowCount;
    }

    public int getCheckinCount()
    {
        return checkinCount;
    }

    public int getLastReseveDate()
    {
        return lastReseveDate;
    }

    public int getLimitationFlag()
    {
        return limitationFlag;
    }

    public int getLimitationStartDate()
    {
        return limitationStartDate;
    }

    public int getLimitationEndDate()
    {
        return limitationEndDate;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getCardSeq()
    {
        return cardSeq;
    }

    public String getLimitationReserveNo()
    {
        return limitationReserveNo;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setReserveCount(int reserveCount)
    {
        this.reserveCount = reserveCount;
    }

    public void setCancelCount(int cancelCount)
    {
        this.cancelCount = cancelCount;
    }

    public void setNoshowCount(int noshowCount)
    {
        this.noshowCount = noshowCount;
    }

    public void setCheckinCount(int checkinCount)
    {
        this.checkinCount = checkinCount;
    }

    public void setLastReseveDate(int lastReseveDate)
    {
        this.lastReseveDate = lastReseveDate;
    }

    public void setLimitationFlag(int limitationFlag)
    {
        this.limitationFlag = limitationFlag;
    }

    public void setLimitationStartDate(int limitationStartDate)
    {
        this.limitationStartDate = limitationStartDate;
    }

    public void setLimitationEndDate(int limitationEndDate)
    {
        this.limitationEndDate = limitationEndDate;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setCardSeq(int cardSeq)
    {
        this.cardSeq = cardSeq;
    }

    public void setLimitationReserveNo(String limitationReserveNo)
    {
        this.limitationReserveNo = limitationReserveNo;
    }

    /**
     * 予約ユーザ基本データ取得
     * 
     * @param userId ユーザ基本データ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean rtn = false;

        query = "SELECT * FROM hh_rsv_user_basic WHERE user_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.reserveCount = result.getInt( "reserve_count" );
                    this.cancelCount = result.getInt( "cancel_count" );
                    this.noshowCount = result.getInt( "noshow_count" );
                    this.checkinCount = result.getInt( "checkin_count" );
                    this.lastReseveDate = result.getInt( "last_reserve_date" );
                    this.limitationFlag = result.getInt( "limitation_flag" );
                    this.limitationStartDate = result.getInt( "limitation_start_date" );
                    this.limitationEndDate = result.getInt( "limitation_end_date" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.cardSeq = result.getInt( "card_seq" );
                    this.limitationReserveNo = result.getString( "limitation_reserve_no" );
                    rtn = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvUserBasic.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(rtn);
    }

    /**
     * 予約ユーザ基本データ設定
     * 
     * @param result 予約ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.reserveCount = result.getInt( "reserve_count" );
                this.cancelCount = result.getInt( "cancel_count" );
                this.noshowCount = result.getInt( "noshow_count" );
                this.checkinCount = result.getInt( "checkin_count" );
                this.lastReseveDate = result.getInt( "last_reserve_date" );
                this.limitationFlag = result.getInt( "limitation_flag" );
                this.limitationStartDate = result.getInt( "limitation_start_date" );
                this.limitationEndDate = result.getInt( "limitation_end_date" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.cardSeq = result.getInt( "card_seq" );
                this.limitationReserveNo = result.getString( "limitation_reserve_no" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvUserBasic.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * 予約ユーザ基本情報データ追加
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

        query = "INSERT hh_rsv_user_basic SET ";
        query = query + " user_id = ?,";
        query = query + " reserve_count = ?,";
        query = query + " cancel_count = ?,";
        query = query + " noshow_count = ?,";
        query = query + " checkin_count = ?,";
        query = query + " last_reserve_date = ?,";
        query = query + " limitation_flag = ?,";
        query = query + " limitation_start_date = ?,";
        query = query + " limitation_end_date = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " card_seq = ?,";
        query = query + " limitation_reserve_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.reserveCount );
            prestate.setInt( 3, this.cancelCount );
            prestate.setInt( 4, this.noshowCount );
            prestate.setInt( 5, this.checkinCount );
            prestate.setInt( 6, this.lastReseveDate );
            prestate.setInt( 7, this.limitationFlag );
            prestate.setInt( 8, this.limitationStartDate );
            prestate.setInt( 9, this.limitationEndDate );
            prestate.setInt( 10, this.lastUpdate );
            prestate.setInt( 11, this.lastUptime );
            prestate.setInt( 12, this.cardSeq );
            prestate.setString( 13, this.limitationReserveNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvUserBasic.insertData] Exception=" + e.toString() );
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
     * 予約ユーザ基本情報データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_user_basic SET ";
        query = query + " reserve_count = ?,";
        query = query + " cancel_count = ?,";
        query = query + " noshow_count = ?,";
        query = query + " checkin_count = ?,";
        query = query + " last_reserve_date = ?,";
        query = query + " limitation_flag = ?,";
        query = query + " limitation_start_date = ?,";
        query = query + " limitation_end_date = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " card_seq = ?,";
        query = query + " limitation_reserve_no = ?";
        query = query + " WHERE user_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, reserveCount );
            prestate.setInt( 2, cancelCount );
            prestate.setInt( 3, noshowCount );
            prestate.setInt( 4, checkinCount );
            prestate.setInt( 5, lastReseveDate );
            prestate.setInt( 6, limitationFlag );
            prestate.setInt( 7, limitationStartDate );
            prestate.setInt( 8, limitationEndDate );
            prestate.setInt( 9, lastUpdate );
            prestate.setInt( 10, lastUptime );
            prestate.setInt( 11, cardSeq );
            prestate.setString( 12, limitationReserveNo );
            prestate.setString( 13, userId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvUserBasic.updateData] Exception=" + e.toString() );
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
     * 予約ユーザ基本情報データ削除
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        if ( userId != null )
        {
            if ( userId.compareTo( "" ) != 0 )
            {
                query = "DELETE FROM hh_rsv_user_basic WHERE user_id = ?";

                try
                {
                    connection = DBConnection.getConnection();
                    prestate = connection.prepareStatement( query );
                    // 更新対象の値をセットする
                    prestate.setString( 1, userId );
                    result = prestate.executeUpdate();
                    if ( result > 0 )
                    {
                        ret = true;
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[DataUserBasic.deleteData] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( prestate );
                    DBConnection.releaseResources( connection );
                }

            }
        }
        return(ret);
    }

}
