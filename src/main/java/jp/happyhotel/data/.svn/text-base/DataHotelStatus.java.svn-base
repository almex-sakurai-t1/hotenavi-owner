/*
 * @(#)DataHotelStatus.java 1.00 2007/09/06 Copyright (C) ALMEX Inc. 2007 ホテルステータスデータ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DBSync;
import jp.happyhotel.common.Logging;

/**
 * ホテルステータス情報データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/16
 * @version 1.2 2009/12/28
 */
public class DataHotelStatus implements Serializable
{
    private static final long serialVersionUID = -3728207082582675813L;

    private int               id;
    private String            hotenaviId;
    private int               lastUpDate;
    private int               lastUpTime;
    private int               retryCount;
    private int               empty;
    private int               clean;
    private int               mode;
    private int               emptyStatus;
    private int               waiting;
    private int               serviceUpDate;
    private int               serviceUpTime;

    /**
     * データを初期化します。
     */
    public DataHotelStatus()
    {
        id = 0;
        hotenaviId = "";
        lastUpDate = 0;
        lastUpTime = 0;
        retryCount = 0;
        empty = 0;
        clean = 0;
        mode = 0;
        emptyStatus = 0;
        waiting = 0;
    }

    /* ▼getter▼ */
    public int getClean()
    {
        return clean;
    }

    public int getEmpty()
    {
        return empty;
    }

    public int getEmptyStatus()
    {
        return emptyStatus;
    }

    public String getHotenaviId()
    {
        return hotenaviId;
    }

    public int getId()
    {
        return id;
    }

    public int getLastUpDate()
    {
        return lastUpDate;
    }

    public int getLastUpTime()
    {
        return lastUpTime;
    }

    public int getMode()
    {
        return mode;
    }

    public int getRetryCount()
    {
        return retryCount;
    }

    public int getServiceUpdate()
    {
        return serviceUpDate;
    }

    public int getServiceUptime()
    {
        return serviceUpTime;
    }

    public int getWaiting()
    {
        return waiting;
    }

    /* ▲getter▲ */

    /* ▼setter▼ */
    public void setClean(int clean)
    {
        this.clean = clean;
    }

    public void setEmpty(int empty)
    {
        this.empty = empty;
    }

    public void setEmptyStatus(int emptyStatus)
    {
        this.emptyStatus = emptyStatus;
    }

    public void setHotenaviId(String hotenaviId)
    {
        this.hotenaviId = hotenaviId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setLastUpDate(int lastUpDate)
    {
        this.lastUpDate = lastUpDate;
    }

    public void setLastUpTime(int lastUpTime)
    {
        this.lastUpTime = lastUpTime;
    }

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public void setRetryCount(int retryCount)
    {
        this.retryCount = retryCount;
    }

    public void setServiceUpDate(int serviceUpdate)
    {
        this.serviceUpDate = serviceUpdate;
    }

    public void setServiceUpTime(int serviceUptime)
    {
        this.serviceUpTime = serviceUptime;
    }

    public void setWaiting(int waiting)
    {
        this.waiting = waiting;
    }

    /* ▲setter▲ */

    /**
     * ホテルステータスデータ取得
     * 
     * @param id ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_status WHERE id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.hotenaviId = result.getString( "hotenavi_id" );
                    this.lastUpDate = result.getInt( "last_update" );
                    this.lastUpTime = result.getInt( "last_uptime" );
                    this.retryCount = result.getInt( "retry_count" );
                    this.empty = result.getInt( "empty" );
                    this.clean = result.getInt( "clean" );
                    this.mode = result.getInt( "mode" );
                    this.emptyStatus = result.getInt( "empty_status" );
                    this.waiting = result.getInt( "waiting" );
                    this.serviceUpDate = result.getInt( "service_update" );
                    this.serviceUpTime = result.getInt( "service_uptime" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelStatus.getData] Exception=" + e.toString() );
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
                this.hotenaviId = result.getString( "hotenavi_id" );
                this.lastUpDate = result.getInt( "last_update" );
                this.lastUpTime = result.getInt( "last_uptime" );
                this.retryCount = result.getInt( "retry_count" );
                this.empty = result.getInt( "empty" );
                this.clean = result.getInt( "clean" );
                this.mode = result.getInt( "mode" );
                this.emptyStatus = result.getInt( "empty_status" );
                this.waiting = result.getInt( "waiting" );
                this.serviceUpDate = result.getInt( "service_update" );
                this.serviceUpTime = result.getInt( "service_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelStatus.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_hotel_status SET ";
        query = query + " id = ?,";
        query = query + " hotenavi_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " retry_count = ?,";
        query = query + " empty = ?,";
        query = query + " clean = ?,";
        query = query + " mode = ?,";
        query = query + " empty_status = ?,";
        query = query + " waiting = ?,";
        query = query + " service_update = ?,";
        query = query + " service_uptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.hotenaviId );
            prestate.setInt( 3, this.lastUpDate );
            prestate.setInt( 4, this.lastUpTime );
            prestate.setInt( 5, this.retryCount );
            prestate.setInt( 6, this.empty );
            prestate.setInt( 7, this.clean );
            prestate.setInt( 8, this.mode );
            prestate.setInt( 9, this.emptyStatus );
            prestate.setInt( 10, this.waiting );
            prestate.setInt( 11, this.serviceUpDate );
            prestate.setInt( 12, this.serviceUpTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                Logging.info( "publish!" );
                // GCPデータ連携
                DBSync.publish( prestate, true );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelStatus.insertData] Exception=" + e.toString() );
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
     * ホテル部屋情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_status SET ";
        query = query + " hotenavi_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " retry_count = ?,";
        query = query + " empty = ?,";
        query = query + " clean = ?,";
        query = query + " mode = ?,";
        query = query + " empty_status = ?,";
        query = query + " waiting = ?,";
        query = query + " service_update = ?,";
        query = query + " service_uptime = ?";
        query = query + " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.hotenaviId );
            prestate.setInt( 2, this.lastUpDate );
            prestate.setInt( 3, this.lastUpTime );
            prestate.setInt( 4, this.retryCount );
            prestate.setInt( 5, this.empty );
            prestate.setInt( 6, this.clean );
            prestate.setInt( 7, this.mode );
            prestate.setInt( 8, this.emptyStatus );
            prestate.setInt( 9, this.waiting );
            prestate.setInt( 10, this.serviceUpDate );
            prestate.setInt( 11, this.serviceUpTime );
            prestate.setInt( 12, id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                Logging.info( "publish!" );
                // GCPデータ連携
                DBSync.publish( prestate, true );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelStatus.updateData] Exception=" + e.toString() );
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
