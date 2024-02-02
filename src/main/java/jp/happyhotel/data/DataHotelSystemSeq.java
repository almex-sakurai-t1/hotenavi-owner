/*
 * @(#)DataHotelCoupon.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテルクーポン情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;
import jp.happyhotel.common.*;

/**
 * ホテル連番データ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/03/25
 */
public class DataHotelSystemSeq implements Serializable
{
    private static final long serialVersionUID = -1409131595782804959L;

    private int               kind;
    private int               seq;
    private int               id;
    private int               registDate;
    private int               registTime;

    /**
     * データを初期化します。
     */
    public DataHotelSystemSeq()
    {
        kind = 0;
        seq = 0;
        id = 0;
        registDate = 0;
        registTime = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getKind()
    {
        return kind;
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

    public void setId(int id)
    {
        this.id = id;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
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

    /**
     * ホテル連番データ取得
     * 
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int kind, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_system_seq WHERE kind = ? AND seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.seq = result.getInt( "seq" );
                    this.id = result.getInt( "id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSystemSeq.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル連番最新データ取得
     * 
     * @param kind 区分（1：クーポン）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getLatestData(int kind)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_system_seq ";
        query = query + " WHERE kind = ?";
        query = query + " ORDER BY regist_date DESC, regist_time DESC";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.kind );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.seq = result.getInt( "seq" );
                    this.id = result.getInt( "id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSystemSeq.getLatestData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル連番データ設定
     * 
     * @param result ホテルクーポン情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.kind = result.getInt( "kind" );
                this.seq = result.getInt( "seq" );
                this.id = result.getInt( "id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSystemSeq.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ホテル連番データ追加
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

        query = "INSERT hh_hotel_system_seq SET ";
        query = query + " kind = ?,";
        query = query + " seq = 0,";
        query = query + " id = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.kind );
            prestate.setInt( 2, this.id );
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
            Logging.error( "[DataHotelSystemSeq.insertData] Exception=" + e.toString() );
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
     * ホテルクーポン情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int kind, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_system_seq SET ";
        query = " id = ?,";
        query = " regist_date = ?";
        query = " regist_time = ?";
        query = query + " WHERE kind = ?";
        query = query + " AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate.setInt( 1, seq );
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.registDate );
            prestate.setInt( 3, this.registTime );
            prestate.setInt( 4, kind );
            prestate.setInt( 5, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSystemSeq.updateData] Exception=" + e.toString() );
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
