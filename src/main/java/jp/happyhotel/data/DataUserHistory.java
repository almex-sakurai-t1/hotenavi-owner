/*
 * @(#)DataUserHistory.java 1.00 2007/09/05
 * Copyright (C) ALMEX Inc. 2007-2011
 * ホテル基本情報クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテルの基本情報の取得
 * 
 * @author S.Tashiro
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/28
 */
public class DataUserHistory implements Serializable
{

    /**
	 *
	 */
    private static final long serialVersionUID = -1227746929306854217L;

    private String            userId;
    private int               seq;
    private int               id;
    private int               dispDate;
    private int               dispTime;
    private String            dispIp;
    private String            dispUserAgent;
    private String            referer;

    public DataUserHistory()
    {
        userId = "";
        seq = 0;
        id = 0;
        dispDate = 0;
        dispTime = 0;
        dispIp = "";
        dispUserAgent = "";
        referer = "";
    }

    public int getDispDate()
    {
        return dispDate;
    }

    public String getDispIp()
    {
        return dispIp;
    }

    public int getDispTime()
    {
        return dispTime;
    }

    public String getDispUserAgent()
    {
        return dispUserAgent;
    }

    public int getId()
    {
        return id;
    }

    public String getReferer()
    {
        return referer;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setDispDate(int dispDate)
    {
        this.dispDate = dispDate;
    }

    public void setDispIp(String dispIp)
    {
        String ipAddr;

        // IPアドレスのチェックを行う
        // アクセスログ不正対策
        // 20110620:E-Mobile対策
        ipAddr = checkSourceIp( dispIp );

        this.dispIp = ipAddr;
    }

    public void setDispTime(int dispTime)
    {
        this.dispTime = dispTime;
    }

    public void setDispUserAgent(String dispUserAgent)
    {
        this.dispUserAgent = dispUserAgent;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setReferer(String referer)
    {
        this.referer = referer;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * 履歴データ取得
     * 
     * @param userId ユーザ基本データ
     * @param id ホテルデータ
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int id, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_history WHERE user_id = ? AND id = ? AND seq= ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, id );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.id = result.getInt( "id" );
                    this.dispDate = result.getInt( "disp_date" );
                    this.dispTime = result.getInt( "disp_time" );
                    this.dispIp = result.getString( "disp_ip" );
                    this.dispUserAgent = result.getString( "disp_useragent" );
                    this.referer = result.getString( "referer" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserHistory.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ユーザ履歴データ設定
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
                this.seq = result.getInt( "seq" );
                this.id = result.getInt( "id" );
                this.dispDate = result.getInt( "disp_date" );
                this.dispTime = result.getInt( "disp_time" );
                this.dispIp = result.getString( "disp_ip" );
                this.dispUserAgent = result.getString( "disp_useragent" );
                this.referer = result.getString( "referer" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBasic.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザ履歴情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        boolean retUser = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataUserViewHotel duvh;
        final int START_IDX = 0;
        final int END_IDX = 255;

        ret = false;

        query = "INSERT hh_user_history SET ";
        query = query + " user_id = ?,";
        query = query + " seq = ?,";
        query = query + " id = ?,";
        query = query + " disp_date = ?,";
        query = query + " disp_time = ?,";
        query = query + " disp_ip = ?,";
        query = query + " disp_useragent = ?,";
        query = query + " referer = ?";

        if ( this.dispUserAgent != null )
        {
            if ( this.dispUserAgent.length() > END_IDX )
            {
                this.dispUserAgent = this.dispUserAgent.substring( START_IDX, END_IDX );
            }
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, 0 );
            prestate.setInt( 3, this.id );
            prestate.setInt( 4, this.dispDate );
            prestate.setInt( 5, this.dispTime );
            prestate.setString( 6, this.dispIp );
            prestate.setString( 7, this.dispUserAgent );
            prestate.setString( 8, this.referer );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
            DBConnection.releaseResources( prestate );

            if ( this.dispDate >= 20151218 )
            {
                /* クローンデータ */
                query = "INSERT hh_user_history_clone SET ";
                query = query + " user_id = ?,";
                query = query + " seq = ?,";
                query = query + " id = ?,";
                query = query + " disp_date = ?,";
                query = query + " disp_time = ?,";
                query = query + " disp_ip = ?,";
                query = query + " disp_useragent = ?,";
                query = query + " referer = ?";
                prestate = connection.prepareStatement( query );

                // 更新対象の値をセットする
                prestate.setString( 1, this.userId );
                prestate.setInt( 2, 0 );
                prestate.setInt( 3, this.id );
                prestate.setInt( 4, this.dispDate );
                prestate.setInt( 5, this.dispTime );
                prestate.setString( 6, this.dispIp );
                prestate.setString( 7, this.dispUserAgent );
                prestate.setString( 8, this.referer );
                prestate.executeUpdate();
                DBConnection.releaseResources( prestate );
            }
            ResultSet res = null;
            try
            {
                // ユーザチェック後必要に応じて確認
                query = "SELECT user_id FROM hh_user_basic ";
                query += " WHERE user_id = ? AND regist_status > 0 AND del_flag = 0";

                prestate = connection.prepareStatement( query );
                prestate.setString( 1, this.userId );
                res = prestate.executeQuery();
                if ( res != null )
                {
                    if ( res.next() != false )
                    {
                        retUser = true;
                    }
                }
                if ( retUser != false )
                {
                    duvh = new DataUserViewHotel();
                    if ( duvh.getData( this.dispDate, this.id, this.userId, connection ) == false )
                    {
                        duvh.setRegistDate( this.dispDate );
                        duvh.setRegistTime( this.dispTime );
                        duvh.setUserId( this.userId );
                        duvh.setId( this.id );
                        duvh.insertData( connection );
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[DataUserHistory.insertData2] Exception=" + e.toString() );
                DBConnection.releaseResources( res );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserHistory.insertData] Exception=" + e.toString() );
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
     * ユーザ履歴情報データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_history SET ";
        query = query + " id = ?,";
        query = query + " disp_date = ?,";
        query = query + " disp_time = ?,";
        query = query + " disp_ip = ?,";
        query = query + " disp_useragent = ?,";
        query = query + " referer = ?";
        query = query + " WHERE user_id = ?";
        query = query + " AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.dispDate );
            prestate.setInt( 3, this.dispTime );
            prestate.setString( 4, this.dispIp );
            prestate.setString( 5, this.dispUserAgent );
            prestate.setString( 6, this.referer );
            prestate.setString( 7, userId );
            prestate.setInt( 8, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserHistory.updateData] Exception=" + e.toString() );
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
     * ソースIPアドレスチェック処理
     * 
     * @param sourceIpAddr ソースIPアドレス
     * @return チェック後IPアドレス
     */
    private String checkSourceIp(String sourceIpAddr)
    {
        String ipAddr;

        ipAddr = sourceIpAddr;

        while( true )
        {
            // E-Mobile対策
            // 1.112.xxx.xxx⇒1.112.0.0
            // 1.113.xxx.xxx⇒1.113.0.0
            // 114.48.xxx.xxx⇒114.48.0.0
            // 111.188.xxx.xxx⇒111.188.0.0
            if ( sourceIpAddr.indexOf( "1.112." ) == 0 )
            {
                ipAddr = "1.112.0.0";
                break;
            }
            if ( sourceIpAddr.indexOf( "1.113." ) == 0 )
            {
                ipAddr = "1.113.0.0";
                break;
            }
            if ( sourceIpAddr.indexOf( "114.48." ) == 0 )
            {
                ipAddr = "114.48.0.0";
                break;
            }
            if ( sourceIpAddr.indexOf( "111.188." ) == 0 )
            {
                ipAddr = "111.188.0.0";
                break;
            }

            break;
        }
        return(ipAddr);
    }
}
