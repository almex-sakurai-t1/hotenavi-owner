/*
 * @(#)DataUserMessage.java 1.00 2008/08/15 Copyright (C) ALMEX Inc. 2008 ユーザ個別メッセージ情報取得クラス
 */
package jp.happyhotel.user;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * ユーザ基本情報取得クラス
 * 
 * @author N.Ide
 * @version 1.00 2008/08/15
 */
public class UserMessage implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 4036103019391641586L;

    private int               dataCount;
    private int               allCount;
    private DataUserMessage[] userMessage;

    /**
     * データを初期化します。
     */
    public UserMessage()
    {
        dataCount = 0;
        allCount = 0;
    }

    public int getDataCount()
    {
        return dataCount;
    }

    public int getAllCount()
    {
        return allCount;
    }

    public DataUserMessage[] getUserMessage()
    {
        return userMessage;
    }

    /**
     * ユーザ別メッセージ情報データ取得(start_date,last_update,last_uptime順)
     * 
     * @param userId ユーザID
     * @param countNum 取得件数（0：全件）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean getDataList(String userId, int countNum)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_message WHERE";
        query = query + " hh_user_message.user_id = ?";
        query = query + " AND hh_user_message.msg_disp_flag = 1";
        query = query + " AND hh_user_message.start_date <= ?";
        query = query + " AND hh_user_message.end_date >= ?";
        query = query + " ORDER BY hh_user_message.start_date DESC, hh_user_message.last_update DESC, hh_user_message.last_uptime DESC";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + countNum;
        }

        count = 0;
        dataCount = 0;
        allCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userMessage = new DataUserMessage[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    userMessage[i] = new DataUserMessage();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.userMessage[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserMessage.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // メッセージ総件数を取得
        query = "SELECT * FROM hh_user_message WHERE";
        query = query + " hh_user_message.user_id = ?";
        query = query + " AND hh_user_message.start_date <= ?";
        query = query + " AND hh_user_message.end_date >= ?";
        query = query + " ORDER BY hh_user_message.start_date DESC, hh_user_message.last_update DESC, hh_user_message.last_uptime DESC";

        count = 0;

        try
        {
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.allCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserMessage.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ユーザ別アラートデータ取得
     * 
     * @param userId ユーザID
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean getAlertData(String userId)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_message WHERE";
        query = query + " hh_user_message.user_id = ?";
        query = query + " AND hh_user_message.alert_disp_flag = 1";
        query = query + " AND hh_user_message.msg_disp_flag = 1";
        query = query + " AND hh_user_message.start_date <= ?";
        query = query + " AND hh_user_message.end_date >= ?";
        query = query + " ORDER BY hh_user_message.start_date DESC, hh_user_message.last_update DESC, hh_user_message.last_uptime DESC";
        query = query + " LIMIT 1";

        count = 0;
        dataCount = 0;
        allCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userMessage = new DataUserMessage[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    userMessage[i] = new DataUserMessage();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.userMessage[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserMessage.getAlertData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * アラート表示フラグを非表示に変更
     * 
     * @param id 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateAlertDate(int id)
    {
        int nowDate;
        int nowTime;
        String query = null;
        Connection connection = null;
        PreparedStatement prestate = null;

        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        query = "UPDATE hh_user_message";
        query = query + " SET alert_check_date = ?,";
        query = query + " alert_check_time = ?,";
        query = query + " alert_disp_flag = 0";
        query = query + " WHERE id = ?";
        query = query + " AND alert_disp_flag = 1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, nowDate );
            prestate.setInt( 2, nowTime );
            prestate.setInt( 3, id );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[UserMessage.updateAlertDate] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(true);
    }

    /**
     * アラート表示フラグをすべて非表示に変更
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateAlertDateAll(String userId)
    {
        int nowDate;
        int nowTime;
        String query = null;
        Connection connection = null;
        PreparedStatement prestate = null;

        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        query = "UPDATE hh_user_message";
        query = query + " SET alert_check_date = ?,";
        query = query + " alert_check_time = ?,";
        query = query + " alert_disp_flag = 0";
        query = query + " WHERE alert_disp_flag = 1";
        query = query + " AND user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, nowDate );
            prestate.setInt( 2, nowTime );
            prestate.setString( 3, userId );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[UserMessage.updateAlertDate] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(true);
    }

    /**
     * メッセージ表示フラグを非表示に変更
     * 
     * @param userId ユーザーID
     * @param id 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateMsgDate(int id)
    {
        int nowDate;
        int nowTime;
        String query = null;
        Connection connection = null;
        PreparedStatement prestate = null;

        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        query = "UPDATE hh_user_message";
        query = query + " SET msg_check_date = ?,";
        query = query + " msg_check_time = ?,";
        query = query + " msg_disp_flag = 0";
        query = query + " WHERE id = ?";
        query = query + " AND msg_disp_flag = 1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, nowDate );
            prestate.setInt( 2, nowTime );
            prestate.setInt( 3, id );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[UserMessage.updateAlertDate] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(true);
    }
}
