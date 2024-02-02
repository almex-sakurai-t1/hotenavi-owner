/*
 * @(#)DataHotelBbsMaillist.java 1.00 2008/01/10 Copyright (C) ALMEX Inc. 2007 クチコミメールデータ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * クチコミメールデータ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/01/10
 * 
 */
public class DataHotelBbsMaillist implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 1696171727178998938L;

    private int               id;
    private int               seq;
    private int               bbsSeq;
    private String            toAddr;
    private String            fromAddr;
    private String            title;
    private String            body;
    private int               sendStatus;
    private int               registDate;
    private int               registTime;
    private int               sendMailStartTime;
    private int               sendMailEndTime;
    private int               sendFlag;
    private int               sendDate;
    private int               sendTime;
    private String            memo;

    /**
     * データを初期化します。
     */
    public DataHotelBbsMaillist()
    {
        id = 0;
        seq = 0;
        bbsSeq = 0;
        toAddr = "";
        fromAddr = "";
        title = "";
        body = "";
        sendStatus = 0;
        registDate = 0;
        registTime = 0;
        sendMailStartTime = 0;
        sendMailEndTime = 0;
        sendFlag = 0;
        sendDate = 0;
        sendTime = 0;
        memo = "";
    }

    public int getBbsSeq()
    {
        return bbsSeq;
    }

    public String getBody()
    {
        return body;
    }

    public String getFromAddr()
    {
        return fromAddr;
    }

    public int getId()
    {
        return id;
    }

    public String getMemo()
    {
        return memo;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getSendDate()
    {
        return sendDate;
    }

    public int getSendFlag()
    {
        return sendFlag;
    }

    public int getSendMailEndTime()
    {
        return sendMailEndTime;
    }

    public int getSendMailStartTime()
    {
        return sendMailStartTime;
    }

    public int getSendTime()
    {
        return sendTime;
    }

    public int getSendStatus()
    {
        return sendStatus;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getTitle()
    {
        return title;
    }

    public String getToAddr()
    {
        return toAddr;
    }

    public void setBbsSeq(int bbsSeq)
    {
        this.bbsSeq = bbsSeq;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public void setFromAddr(String fromAddr)
    {
        this.fromAddr = fromAddr;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setSendDate(int sendDate)
    {
        this.sendDate = sendDate;
    }

    public void setSendFlag(int sendFlag)
    {
        this.sendFlag = sendFlag;
    }

    public void setSendMailEndTime(int sendMailEndTime)
    {
        this.sendMailEndTime = sendMailEndTime;
    }

    public void setSendMailStartTime(int sendMailStartTime)
    {
        this.sendMailStartTime = sendMailStartTime;
    }

    public void setSendTime(int sendTime)
    {
        this.sendTime = sendTime;
    }

    public void setSendStatus(int sendStatus)
    {
        this.sendStatus = sendStatus;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setToAddr(String toAddr)
    {
        this.toAddr = toAddr;
    }

    /**
     * クチコミメールリストデータ取得
     * 
     * @param Id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_bbs_maillist WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.bbsSeq = result.getInt( "bbs_seq" );
                    this.toAddr = result.getString( "to_address" );
                    this.fromAddr = result.getString( "from_address" );
                    this.title = result.getString( "title" );
                    this.body = result.getString( "body" );
                    this.sendStatus = result.getInt( "send_status" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.sendMailStartTime = result.getInt( "sendmail_starttime" );
                    this.sendMailEndTime = result.getInt( "sendmail_endtime" );
                    this.sendFlag = result.getInt( "send_flag" );
                    this.sendDate = result.getInt( "send_date" );
                    this.sendTime = result.getInt( "send_time" );
                    this.memo = result.getString( "memo" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBbsMaillist.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * クチコミメールリストデータ設定
     * 
     * @param Id ホテルID
     * @param seq 管理番号
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
                this.bbsSeq = result.getInt( "bbs_seq" );
                this.toAddr = result.getString( "to_address" );
                this.fromAddr = result.getString( "from_address" );
                this.title = result.getString( "title" );
                this.body = result.getString( "body" );
                this.sendStatus = result.getInt( "send_status" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.sendMailStartTime = result.getInt( "sendmail_starttime" );
                this.sendMailEndTime = result.getInt( "sendmail_endtime" );
                this.sendFlag = result.getInt( "send_flag" );
                this.sendDate = result.getInt( "send_date" );
                this.sendTime = result.getInt( "send_time" );
                this.memo = result.getString( "memo" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBbsMaillist.setData] Exception=" + e.toString() );
        }
        return(ret);
    }

    /**
     * ユーザ基本履歴情報データ追加
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

        query = "INSERT hh_hotel_bbs_maillist SET ";
        query = query + " id = ?,";
        query = query + " seq = 0,";
        query = query + " bbs_seq = ?,";
        query = query + " to_address = ?,";
        query = query + " from_address = ?,";
        query = query + " title = ?,";
        query = query + " body = ?,";
        query = query + " send_status = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " sendmail_starttime = ?,";
        query = query + " sendmail_endtime = ?,";
        query = query + " send_flag = ?,";
        query = query + " send_date = ?,";
        query = query + " send_time = ?,";
        query = query + " memo = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.bbsSeq );
            prestate.setString( 3, this.toAddr );
            prestate.setString( 4, this.fromAddr );
            prestate.setString( 5, this.title );
            prestate.setString( 6, this.body );
            prestate.setInt( 7, this.sendStatus );
            prestate.setInt( 8, this.registDate );
            prestate.setInt( 9, this.registTime );
            prestate.setInt( 10, this.sendMailStartTime );
            prestate.setInt( 11, this.sendMailEndTime );
            prestate.setInt( 12, this.sendFlag );
            prestate.setInt( 13, this.sendDate );
            prestate.setInt( 14, this.sendTime );
            prestate.setString( 15, this.memo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBbsMaillist.insertData] Exception=" + e.toString() );
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
     * ユーザ基本履歴情報データ追加
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

        query = "update hh_hotel_bbs_maillist SET ";
        query = query + " bbs_seq = ?,";
        query = query + " to_address = ?,";
        query = query + " from_address = ?,";
        query = query + " title = ?,";
        query = query + " body = ?,";
        query = query + " send_status = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " sendmail_starttime = ?,";
        query = query + " sendmail_endtime = ?,";
        query = query + " send_flag = ?,";
        query = query + " send_date = ?,";
        query = query + " send_time = ?,";
        query = query + " memo = ?";
        query = query + " WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.bbsSeq );
            prestate.setString( 2, this.toAddr );
            prestate.setString( 3, this.fromAddr );
            prestate.setString( 4, this.title );
            prestate.setString( 5, this.body );
            prestate.setInt( 6, this.sendStatus );
            prestate.setInt( 7, this.registDate );
            prestate.setInt( 8, this.registTime );
            prestate.setInt( 9, this.sendMailStartTime );
            prestate.setInt( 10, this.sendMailEndTime );
            prestate.setInt( 11, this.sendFlag );
            prestate.setInt( 12, this.sendDate );
            prestate.setInt( 13, this.sendTime );
            prestate.setString( 14, this.memo );
            prestate.setInt( 15, this.id );
            prestate.setInt( 16, this.seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBbsMaillist.updateData] Exception=" + e.toString() );
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
