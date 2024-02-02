/*
 * @(#)HotelPv.java 1.00 2008/01/21 Copyright (C) ALMEX Inc. 2008 ホテルページビュー情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelPv;

/**
 * ホテルページビュー情報クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2008/01/21
 */
public class HotelPv implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -7432002705172538009L;

    private int               hotelPvCount;
    private int               lastUpdate;
    private DataHotelPv[]     hotelPv;

    /**
     * データを初期化します。
     */
    public HotelPv()
    {
        hotelPvCount = 0;
        hotelPv = null;
    }

    public DataHotelPv[] getHotelPv()
    {
        return hotelPv;
    }

    public int getHotelPvCount()
    {
        return hotelPvCount;
    }

    public void setHotelPv(DataHotelPv[] hotelPv)
    {
        this.hotelPv = hotelPv;
    }

    public void setHotelPvCount(int hotelPvCount)
    {
        this.hotelPvCount = hotelPvCount;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    /**
     * 全件PV取得
     * 
     * @param collectDate 対象日付(0:最新PV)
     * @param getCount 取得件数(0:1000件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPvAll(int collectDate, int getCount)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_pv.* FROM hh_hotel_pv,hh_hotel_basic WHERE hh_hotel_pv.collect_date = ?";
        query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id";
        query = query + " ORDER BY hh_hotel_pv.total_uu_pv DESC, hh_hotel_pv.prev_day_ratio DESC, hh_hotel_basic.name_kana";

        if ( getCount == 0 )
        {
            getCount = 1000;
        }

        query = query + " LIMIT " + getCount;

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, collectDate );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    hotelPvCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelPv = new DataHotelPv[this.hotelPvCount];
                for( i = 0 ; i < hotelPvCount ; i++ )
                {
                    hotelPv[i] = new DataHotelPv();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.hotelPv[count++].setData( result );
                }

                // 最終更新日の取得
                this.lastUpdate = getLastUpdate( connection );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelPv.getPvAll] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * 全件PV取得
     * 
     * @param collectDate 対象日付(0:最新PV)
     * @param countNum 取得件数(0:1000件)
     * @param pageNum ページ番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPvAllByRank(int collectDate, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_pv.* FROM hh_hotel_pv,hh_hotel_basic WHERE hh_hotel_pv.collect_date = ?";
        query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id";
        query = query + " GROUP BY hh_hotel_pv.id";
        query = query + " ORDER BY hh_hotel_pv.total_uu_pv DESC, hh_hotel_pv.prev_day_ratio DESC, hh_hotel_basic.name_kana";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }
        else if ( countNum == 0 )
        {
            countNum = 1000;
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, collectDate );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    hotelPvCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelPv = new DataHotelPv[this.hotelPvCount];
                for( i = 0 ; i < hotelPvCount ; i++ )
                {
                    hotelPv[i] = new DataHotelPv();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.hotelPv[count++].setData( result );
                }

                // 最終更新日の取得
                this.lastUpdate = getLastUpdate( connection );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelPv.getPvAll] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * 都道府県別PV取得
     * 
     * @param prefId 都道府県ID
     * @param collectDate 対象日付(0:最新PV)
     * @param getCount 取得件数(0:1000件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPvPref(int prefId, int collectDate, int getCount)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_pv.* FROM hh_hotel_pv,hh_hotel_basic WHERE hh_hotel_pv.collect_date = ?";
        query = query + " AND hh_hotel_basic.pref_id = ?";
        query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id";
        query = query + " GROUP BY hh_hotel_pv.id";
        query = query + " ORDER BY hh_hotel_pv.total_uu_pv DESC, hh_hotel_pv.prev_day_ratio DESC, hh_hotel_basic.name_kana";

        if ( getCount == 0 )
        {
            getCount = 1000;
        }

        query = query + " LIMIT " + getCount;

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, collectDate );
            prestate.setInt( 2, prefId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    hotelPvCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelPv = new DataHotelPv[this.hotelPvCount];
                for( i = 0 ; i < hotelPvCount ; i++ )
                {
                    hotelPv[i] = new DataHotelPv();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.hotelPv[count++].setData( result );
                }

                // 最終更新日の取得
                this.lastUpdate = getLastUpdate( connection );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelPv.getPvAll] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * 最終更新日取得
     * 
     * @param connection DBコネクション
     * @return 最終更新日
     */
    private int getLastUpdate(Connection connection)
    {
        int lastDate;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // デフォルトは前日日付
        lastDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );

        query = "SELECT hh_hotel_pv.collect_date FROM hh_hotel_pv ORDER BY collect_date DESC LIMIT 1";

        try
        {
            prestate = connection.prepareStatement( query );

            result = prestate.executeQuery();
            if ( result != null )
            {
                result.beforeFirst();
                while( result.next() != false )
                {
                    lastDate = result.getInt( "collect_date" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelPv.getLastUpdate] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(lastDate);
    }
}
