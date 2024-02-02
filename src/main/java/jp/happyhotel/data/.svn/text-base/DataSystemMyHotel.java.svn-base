/*
 * @(#)DataSystemMyHotel.java 1.00 2010/01/22
 * Copyright (C) ALMEX Inc. 2010
 * システムマイホテルデータ取得クラス
 */

package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * システムマイホテルデータ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2010/01/22
 */
public class DataSystemMyHotel implements Serializable
{

    private static final long serialVersionUID = -2184912173362550225L;
    private int               collectDate;
    private int               id;
    private int               rank;
    private int               prefRank;
    private int               count;
    private int               totalCount;

    /**
     * データを初期化します。
     */
    public DataSystemMyHotel()
    {
        collectDate = 0;
        id = 0;
        rank = 0;
        prefRank = 0;
        count = 0;
        totalCount = 0;
    }

    public int getCollectDate()
    {
        return collectDate;
    }

    public int getCount()
    {
        return count;
    }

    public int getId()
    {
        return id;
    }

    public int getPrefRank()
    {
        return prefRank;
    }

    public int getRank()
    {
        return rank;
    }

    public int getTotalCount()
    {
        return totalCount;
    }

    public void setCollectDate(int collectDate)
    {
        this.collectDate = collectDate;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setPrefRank(int prefRank)
    {
        this.prefRank = prefRank;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public void setTotalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }

    /**
     * システムマイホテルデータ取得
     * 
     * @param collectDate 集計日(YYYYMMDD)
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int collectDate, int hotelId)
    {
        boolean ret;
        int i;
        int date;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;
        i = 0;
        if ( collectDate == 0 )
        {
            return(false);
        }
        // 日付が8桁未満だったら
        if ( Integer.toString( collectDate ).length() < 8 )
        {
            i = Integer.toString( collectDate ).length();
            date = 1;
            while( i < 8 )
            {
                date *= 10;
                i++;
            }
            collectDate *= date;
        }
        else if ( Integer.toString( collectDate ).length() >= 8 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_system_myhotel WHERE collect_date = ? AND id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, collectDate );
            prestate.setInt( 2, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.collectDate = result.getInt( "collect_date" );
                    this.id = result.getInt( "id" );
                    this.rank = result.getInt( "rank" );
                    this.prefRank = result.getInt( "pref_rank" );
                    this.count = result.getInt( "count" );
                    this.totalCount = result.getInt( "total_count" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMyHotel.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * システムマイホテルデータ設定
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
                this.collectDate = result.getInt( "collect_date" );
                this.id = result.getInt( "id" );
                this.rank = result.getInt( "rank" );
                this.prefRank = result.getInt( "pref_rank" );
                this.count = result.getInt( "count" );
                this.totalCount = result.getInt( "total_count" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMyHotel.setData] Exception=" + e.toString() );
            return(false);
        }

        return(true);
    }

    /**
     * システムマイホテルデータ追加
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

        query = "INSERT hh_system_myhotel SET ";
        query = query + " collect_date = ?,";
        query = query + " id = ?,";
        query = query + " rank = ?,";
        query = query + " pref_rank = ?,";
        query = query + " count = ?,";
        query = query + " total_count = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.collectDate );
            prestate.setInt( 2, this.id );
            prestate.setInt( 3, this.rank );
            prestate.setInt( 4, this.prefRank );
            prestate.setInt( 5, this.count );
            prestate.setInt( 6, this.totalCount );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMyHotel.insertData] Exception=" + e.toString() );
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
     * システムマイホテルデータ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int id)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_system_myhotel SET ";
        query = query + " rank = ?,";
        query = query + " pref_rank = ?,";
        query = query + " count = ?,";
        query = query + " total_count = ?,";
        query = query + " WHERE collect_date = ?,";
        query = query + " AND id = ?,";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.rank );
            prestate.setInt( 2, this.prefRank );
            prestate.setInt( 3, this.count );
            prestate.setInt( 4, this.totalCount );
            prestate.setInt( 5, this.collectDate );
            prestate.setInt( 6, this.id );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMyHotel.updateData] Exception=" + e.toString() );
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
