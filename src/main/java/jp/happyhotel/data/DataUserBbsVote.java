/*
 * @(#)DataUserBbsVote.java 1.00 2008/04/23 Copyright (C) ALMEX Inc. 2008 ユーザクチコミ投票履歴取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ユーザクチコミ投票履歴取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2008/04/23
 */
public class DataUserBbsVote implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -1020983392407628101L;

    private String            userId;
    private int               hotelId;
    private int               seq;
    private int               voteDate;
    private int               voteTime;
    private int               voteStatus;

    /**
     * データを初期化します。
     */
    public DataUserBbsVote()
    {
        userId = "";
        hotelId = 0;
        seq = 0;
        voteDate = 0;
        voteTime = 0;
        voteStatus = 0;
    }

    public int getHotelId()
    {
        return hotelId;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getVoteDate()
    {
        return voteDate;
    }

    public int getVoteStatus()
    {
        return voteStatus;
    }

    public int getVoteTime()
    {
        return voteTime;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setVoteDate(int voteDate)
    {
        this.voteDate = voteDate;
    }

    public void setVoteStatus(int voteStatus)
    {
        this.voteStatus = voteStatus;
    }

    public void setVoteTime(int voteTime)
    {
        this.voteTime = voteTime;
    }

    /**
     * ユーザクチコミ投票履歴データ取得
     * 
     * @param userId ユーザ基本データ
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int hotelId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_bbsvote WHERE user_id = ? AND id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, hotelId );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.hotelId = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.voteDate = result.getInt( "vote_date" );
                    this.voteTime = result.getInt( "vote_time" );
                    this.voteStatus = result.getInt( "vote_status" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBbsVote.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザクチコミ投票履歴データ設定
     * 
     * @param result ユーザクチコミ投票履歴データレコード
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
                this.seq = result.getInt( "seq" );
                this.voteDate = result.getInt( "vote_date" );
                this.voteTime = result.getInt( "vote_time" );
                this.voteStatus = result.getInt( "vote_status" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBbsVote.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * クチコミ投票履歴データ追加
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

        query = "INSERT hh_user_bbsvote SET ";
        query = query + " user_id = ?,";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " vote_date = ?,";
        query = query + " vote_time = ?,";
        query = query + " vote_status = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.hotelId );
            prestate.setInt( 3, this.seq );
            prestate.setInt( 4, this.voteDate );
            prestate.setInt( 5, this.voteTime );
            prestate.setInt( 6, this.voteStatus );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBbsVote.insertData] Exception=" + e.toString() );
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
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int id, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_bbsvote SET ";
        query = query + " vote_date = ?,";
        query = query + " vote_time = ?,";
        query = query + " vote_status = ?";
        query = query + " WHERE user_id = ? AND id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.voteDate );
            prestate.setInt( 2, this.voteTime );
            prestate.setInt( 3, this.voteStatus );
            prestate.setString( 4, userId );
            prestate.setInt( 5, id );
            prestate.setInt( 6, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBbsVote.updateData] Exception=" + e.toString() );
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
