/*
 * @(#)DataUserMessage.java 1.00 2008/08/14 Copyright (C) ALMEX Inc. 2008 ユーザ個別メッセージ情報取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ユーザ基本情報取得クラス
 * 
 * @author N.Ide
 * @version 1.00 2008/08/14
 */
public class DataUserMessage implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 8907305886759909744L;

    private int               id;
    private String            userId;
    private int               alertDispFlag;
    private int               msgDispFlag;
    private int               startDate;
    private int               endDate;
    private String            title;
    private String            msg;
    private int               lastUpdate;
    private int               lastUptime;
    private int               alertCheckDate;
    private int               alertCheckTime;
    private int               msgCheckDate;
    private int               msgCheckTime;

    /**
     * データを初期化します。
     */
    public DataUserMessage()
    {
        id = 0;
        userId = "";
        alertDispFlag = 0;
        msgDispFlag = 0;
        startDate = 0;
        endDate = 0;
        title = "";
        msg = "";
        lastUpdate = 0;
        lastUptime = 0;
        alertCheckDate = 0;
        alertCheckTime = 0;
        msgCheckDate = 0;
        msgCheckTime = 0;

    }

    public int getId()
    {
        return id;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getAlertDispFlag()
    {
        return alertDispFlag;
    }

    public int getMsgDispFlag()
    {
        return msgDispFlag;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public String getTitle()
    {
        return title;
    }

    public String getMsg()
    {
        return msg;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getAlertCheckDate()
    {
        return alertCheckDate;
    }

    public int getAlertCheckTime()
    {
        return alertCheckTime;
    }

    public int getMsgCheckDate()
    {
        return msgCheckDate;
    }

    public int getMsgCheckTime()
    {
        return msgCheckTime;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setAlertDispFlag(int alertDispFlag)
    {
        this.alertDispFlag = alertDispFlag;
    }

    public void setMsgDispFlag(int msgDispFlag)
    {
        this.msgDispFlag = msgDispFlag;
    }

    public void setStartdate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setAlertCheckDate(int alertCheckDate)
    {
        this.alertCheckDate = alertCheckDate;
    }

    public void setAlertCheckTime(int alertCheckTime)
    {
        this.alertCheckTime = alertCheckTime;
    }

    public void setMsgCheckDate(int msgCheckDate)
    {
        this.msgCheckDate = msgCheckDate;
    }

    public void setMsgCheckTime(int msgCheckTime)
    {
        this.msgCheckTime = msgCheckTime;
    }

    /**
     * ユーザ個別メッセージデータ取得
     * 
     * @param id 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_message WHERE id = ?";

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
                    this.userId = result.getString( "user_id" );
                    this.alertDispFlag = result.getInt( "alert_disp_flag" );
                    this.msgDispFlag = result.getInt( "msg_disp_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.title = result.getString( "title" );
                    this.msg = result.getString( "msg" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.alertCheckDate = result.getInt( "alert_check_date" );
                    this.alertCheckTime = result.getInt( "alert_check_time" );
                    this.msgCheckDate = result.getInt( "msg_check_date" );
                    this.msgCheckTime = result.getInt( "msg_check_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMessage.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザ個別メッセージデータ設定
     * 
     * @param result ユーザ個別メッセージデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.userId = result.getString( "user_id" );
                this.alertDispFlag = result.getInt( "alert_disp_flag" );
                this.msgDispFlag = result.getInt( "msg_disp_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.title = result.getString( "title" );
                this.msg = result.getString( "msg" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.alertCheckDate = result.getInt( "alert_check_date" );
                this.alertCheckTime = result.getInt( "alert_check_time" );
                this.msgCheckDate = result.getInt( "msg_check_date" );
                this.msgCheckTime = result.getInt( "msg_check_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserM.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザマイエリア情報データ追加
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

        query = "INSERT hh_user_message SET ";
        query = query + " id = 0,";
        query = query + " userId = ?,";
        query = query + " alertDispFlag = ?,";
        query = query + " msgDispFlag = ?,";
        query = query + " startDate = ?,";
        query = query + " endDate = ?,";
        query = query + " title = ?,";
        query = query + " msg = ?,";
        query = query + " lastUpdate = ?,";
        query = query + " lastUptime = ?,";
        query = query + " alertCheckDate = ?,";
        query = query + " alertCheckTime = ?,";
        query = query + " msgCheckDate = ?,";
        query = query + " msgCheckTime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.alertDispFlag );
            prestate.setInt( 3, this.msgDispFlag );
            prestate.setInt( 4, this.startDate );
            prestate.setInt( 5, this.endDate );
            prestate.setString( 6, this.title );
            prestate.setString( 7, this.msg );
            prestate.setInt( 8, this.lastUpdate );
            prestate.setInt( 9, this.lastUptime );
            prestate.setInt( 10, this.alertCheckDate );
            prestate.setInt( 11, this.alertCheckTime );
            prestate.setInt( 12, this.msgCheckDate );
            prestate.setInt( 13, this.msgCheckTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMessage.insertData] Exception=" + e.toString() );
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
     * ユーザマイエリア情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id 管理番号
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

        query = "UPDATE hh_user_message SET ";
        query = query + " userId = ?,";
        query = query + " alertDispFlag = ?,";
        query = query + " msgDispFlag = ?,";
        query = query + " startDate = ?,";
        query = query + " endDate = ?,";
        query = query + " title = ?,";
        query = query + " msg = ?,";
        query = query + " lastUpdate = ?,";
        query = query + " lastUptime = ?,";
        query = query + " alertCheckDate = ?,";
        query = query + " alertCheckTime = ?,";
        query = query + " msgCheckDate = ?,";
        query = query + " msgCheckTime = ?";
        query = query + " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.alertDispFlag );
            prestate.setInt( 3, this.msgDispFlag );
            prestate.setInt( 4, this.startDate );
            prestate.setInt( 5, this.endDate );
            prestate.setString( 6, this.title );
            prestate.setString( 7, this.msg );
            prestate.setInt( 8, this.lastUpdate );
            prestate.setInt( 9, this.lastUptime );
            prestate.setInt( 10, this.alertCheckDate );
            prestate.setInt( 11, this.alertCheckTime );
            prestate.setInt( 12, this.msgCheckDate );
            prestate.setInt( 13, this.msgCheckTime );
            prestate.setInt( 14, id );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMessage.updateData] Exception=" + e.toString() );
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
