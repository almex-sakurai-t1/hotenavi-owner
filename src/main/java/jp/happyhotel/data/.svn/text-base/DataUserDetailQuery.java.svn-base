/*
 * @(#)DataUserDetailQuery.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ポイント管理マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ユーザーこだわり検索取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/02/21
 */
public class DataUserDetailQuery implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 2315573336543031033L;

    private String            userId;
    private String            queryString;
    private int               seq;
    private int               registDate;
    private int               registTime;

    /**
     * データを初期化します。
     */
    public DataUserDetailQuery()
    {
        userId = "";
        queryString = "";
        seq = 0;
        registDate = 0;
        registTime = 0;
    }

    public String getQueryString()
    {
        return queryString;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setQueryString(String queryString)
    {
        this.queryString = queryString;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
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
     * ユーザーこだわり検索データ取得
     * 
     * @param code ポイントコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_detail_query WHERE user_id= ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.queryString = result.getString( "query_string" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDetailQuery.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ユーザーこだわり検索データ設定
     * 
     * @param result デコメ管理マスタデータレコード
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
                this.queryString = result.getString( "query_string" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDetailQuery.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ユーザーこだわり検索データ設定
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

        query = "INSERT hh_user_detail_query SET";
        query = query + " user_id = ?,";
        query = query + " seq = 0,";
        query = query + " query_string = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.queryString );
            prestate.setInt( 3, this.registDate );
            prestate.setInt( 4, this.registTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDetailQuery.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ユーザーこだわり検索データ設定
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_user_detail_query SET";
        query = query + " query_string = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";
        query = query + " WHERE user_id = ?";
        query = query + " AND seq = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.queryString );
            prestate.setInt( 2, this.registDate );
            prestate.setInt( 3, this.registTime );
            prestate.setString( 4, this.userId );
            prestate.setInt( 5, seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDetailQuery.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ユーザーこだわり検索データ取得
     * 
     * @param userId ポイントコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getMaxSeq(String userId)
    {
        int seq;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        seq = 0;
        query = "SELECT MAX(seq) FROM hh_user_detail_query WHERE user_id= ?";

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
                    seq = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDetailQuery.getMaxData] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(seq);
    }

}
