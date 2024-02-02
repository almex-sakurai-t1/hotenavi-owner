/*
 * @(#)DataHotelMessage.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテル最新情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * ホテル最新情報データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/16
 * @version 1.2 2010/03/12
 */
public class DataHotelMessage implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 879270131702904019L;

    private int               id;
    private int               seq;
    private String            dispMessage;
    private int               startDate;
    private int               endDate;
    private int               mailSend;
    private int               delFlag;
    private int               addDate;
    private int               addTime;
    private int               lastUpDate;
    private int               lastUpTime;
    private int               startTime;
    private int               endTime;

    /**
     * データを初期化します。
     */
    public DataHotelMessage()
    {
        id = 0;
        seq = 0;
        dispMessage = "";
        startDate = 0;
        endDate = 0;
        mailSend = 0;
        delFlag = 0;
        addDate = 0;
        addTime = 0;
        lastUpDate = 0;
        lastUpTime = 0;
        startTime = 0;
        endTime = 0;
    }

    /* getter */
    public int getAddDate()
    {
        return addDate;
    }

    public int getAddTime()
    {
        return addTime;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public String getDispMessage()
    {
        return dispMessage;
    }

    public int getEndDate()
    {
        return endDate;
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

    public int getMailSend()
    {
        return mailSend;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getStartTime()
    {
        return startTime;
    }

    public int getEndTime()
    {
        return endTime;
    }

    /* setter */
    public void setAddDate(int addDate)
    {
        this.addDate = addDate;
    }

    public void setAddTime(int addTime)
    {
        this.addTime = addTime;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setDispMessage(String dispMessage)
    {
        this.dispMessage = dispMessage;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
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

    public void setMailSend(int mailSend)
    {
        this.mailSend = mailSend;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setStartTime(int startTime)
    {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime)
    {
        this.endTime = endTime;
    }

    /**
     * ホテル最新情報データ取得
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(-1:失敗)
     */
    public int getMaxSeq(int hotelId)
    {
        int maxSeq;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        maxSeq = 0;
        query = "SELECT MAX(seq) FROM hh_hotel_message WHERE id = ?";
        query = query + " AND ( hh_hotel_message.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_message.end_date >= " + DateEdit.getDate( 2 ) + ")";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    maxSeq = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMessage.getMaxSeq] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(maxSeq);
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

        query = "SELECT * FROM hh_hotel_message WHERE id = ? AND seq = ?";

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
                    this.dispMessage = result.getString( "disp_message" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.mailSend = result.getInt( "mail_send" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.addDate = result.getInt( "add_date" );
                    this.addTime = result.getInt( "add_time" );
                    this.lastUpDate = result.getInt( "last_update" );
                    this.lastUpTime = result.getInt( "last_uptime" );
                    this.startTime = result.getInt( "start_time" );
                    this.endTime = result.getInt( "end_time" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMessage.getData] Exception=" + e.toString() );
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
                this.dispMessage = result.getString( "disp_message" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.mailSend = result.getInt( "mail_send" );
                this.delFlag = result.getInt( "del_flag" );
                this.addDate = result.getInt( "add_date" );
                this.addTime = result.getInt( "add_time" );
                this.lastUpDate = result.getInt( "last_update" );
                this.lastUpTime = result.getInt( "last_uptime" );
                this.startTime = result.getInt( "start_time" );
                this.endTime = result.getInt( "end_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMessage.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_hotel_message SET ";
        query = query + " id = ?,";
        query = query + " seq = 0,";
        query = query + " disp_message = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " mail_send = ?,";
        query = query + " del_flag = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " start_time = ?";
        query = query + " end_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.dispMessage );
            prestate.setInt( 3, this.startDate );
            prestate.setInt( 4, this.endDate );
            prestate.setInt( 5, this.mailSend );
            prestate.setInt( 6, this.delFlag );
            prestate.setInt( 7, this.addDate );
            prestate.setInt( 8, this.addTime );
            prestate.setInt( 9, this.lastUpDate );
            prestate.setInt( 10, this.lastUpTime );
            prestate.setInt( 11, this.startTime );
            prestate.setInt( 12, this.endTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMessage.insertData] Exception=" + e.toString() );
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

        query = "UPDATE hh_hotel_message SET ";
        query = query + " disp_message = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " mail_send = ?,";
        query = query + " del_flag = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " start_time = ?";
        query = query + " end_time = ?";
        query = query + " WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.dispMessage );
            prestate.setInt( 2, this.startDate );
            prestate.setInt( 3, this.endDate );
            prestate.setInt( 4, this.mailSend );
            prestate.setInt( 5, this.delFlag );
            prestate.setInt( 6, this.addDate );
            prestate.setInt( 7, this.addTime );
            prestate.setInt( 8, this.lastUpDate );
            prestate.setInt( 9, this.lastUpTime );
            prestate.setInt( 10, this.startTime );
            prestate.setInt( 11, this.endTime );
            prestate.setInt( 12, id );
            prestate.setInt( 13, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMessage.updateData] Exception=" + e.toString() );
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
