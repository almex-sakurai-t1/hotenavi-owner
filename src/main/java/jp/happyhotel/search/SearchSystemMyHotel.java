/*
 * @(#)SearchSystemMyHotel.java 1.00 2010/01/25
 * Copyright (C) ALMEX Inc. 2010
 * システムマイホテル情報取得クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSystemMyHotel;

/**
 * システムマイホテル情報クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2010/01/25
 */
public class SearchSystemMyHotel implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID = -7432002705172538009L;

    private int                 myhotelCount;
    private DataSystemMyHotel[] myhotel;

    /**
     * データを初期化します。
     */
    public SearchSystemMyHotel()
    {
        myhotelCount = 0;
        myhotel = null;
    }

    public DataSystemMyHotel[] getMyHotel()
    {
        return myhotel;
    }

    public int getMyHotelCount()
    {
        return myhotelCount;
    }

    public void setMyHotel(DataSystemMyHotel[] myhotel)
    {
        this.myhotel = myhotel;
    }

    public void setMyHotelCount(int myhotelCount)
    {
        this.myhotelCount = myhotelCount;
    }

    /**
     * 全件マイホテル取得
     * 
     * @param collectDate 対象日付(0:最新PV)
     * @param getCount 取得件数(0:1000件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyHotelAll(int collectDate, int getCount)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_system_myhotel.* FROM hh_system_myhotel, hh_hotel_basic";
        query = query + " WHERE hh_system_myhotel.collect_date = ?";
        query = query + " AND hh_system_myhotel.id = hh_hotel_basic.id";
        query = query + " AND hh_system_myhotel.rank <= ?";
        query = query + " ORDER BY hh_system_myhotel.count DESC, hh_system_myhotel.total_count, hh_hotel_basic.id";

        if ( getCount == 0 )
        {
            getCount = 30;
        }

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, collectDate );
            prestate.setInt( 2, getCount );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.myhotelCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.myhotel = new DataSystemMyHotel[this.myhotelCount];
                for( i = 0 ; i < myhotelCount ; i++ )
                {
                    this.myhotel[i] = new DataSystemMyHotel();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.myhotel[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemMyHotel.getMyHotelAll] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * 都道府県別マイホテル取得
     * 
     * @param prefId 都道府県ID
     * @param collectDate 対象日付(0:最新PV)
     * @param getCount 取得件数(0:1000件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyHotelPref(int prefId, int collectDate, int getCount)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_system_myhotel.* from hh_system_myhotel, hh_hotel_basic";
        query = query + " WHERE hh_system_myhotel.id = hh_hotel_basic.id";
        query = query + " AND hh_system_myhotel.collect_date = ?";
        query = query + " AND hh_system_myhotel.pref_rank <= ?";
        if ( prefId > 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = ?";
        }
        query = query + " ORDER BY hh_system_myhotel.count DESC, hh_system_myhotel.total_count, hh_hotel_basic.id";

        if ( getCount == 0 )
        {
            getCount = 10;
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, collectDate );
            prestate.setInt( 2, getCount );
            prestate.setInt( 3, prefId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.myhotelCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.myhotel = new DataSystemMyHotel[this.myhotelCount];
                for( i = 0 ; i < this.myhotelCount ; i++ )
                {
                    this.myhotel[i] = new DataSystemMyHotel();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.myhotel[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemMyHotel.getMyHotelPref] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }
}
