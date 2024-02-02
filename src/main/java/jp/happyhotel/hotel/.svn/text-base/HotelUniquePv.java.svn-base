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
import jp.happyhotel.data.DataHotelUniquePv;

/**
 * ホテルユニークページビュー情報クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2008/01/21
 */
public class HotelUniquePv implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID = -7432002705172538009L;

    private int                 hotelPvCount;
    private int                 lastUpdate;
    private DataHotelUniquePv[] hotelUniquePv;
    private DataHotelPv[]       hotelPv;

    /**
     * データを初期化します。
     */
    public HotelUniquePv()
    {
        hotelPvCount = 0;
        hotelUniquePv = null;
    }

    public DataHotelUniquePv[] getHotelUniquePv()
    {
        return hotelUniquePv;
    }

    public int getHotelPvCount()
    {
        return hotelPvCount;
    }

    public void setHotelUniquePv(DataHotelUniquePv[] hotelPv)
    {
        this.hotelUniquePv = hotelPv;
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

    /** 集計したデータをHotelPVに入れるためのデータ **/
    public DataHotelPv[] getHotelPv()
    {
        return this.hotelPv;
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

        query = "SELECT UPV.* FROM hh_hotel_unique_pv UPV ";
        query += " INNER JOIN hh_hotel_basic HB ON HB.id = UPV.id AND HB.rank >=1 AND HB.kind <= 7";

        query += " WHERE UPV.collect_date = ?";
        query += " ORDER BY UPV.total_uu_pv DESC, HB.rank DESC";

        if ( getCount == 0 )
        {
            getCount = 1000;
        }

        query = query + " LIMIT " + getCount;

        count = 0;

        try
        {
            Logging.info( query );
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, collectDate );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelPvCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelUniquePv = new DataHotelUniquePv[this.hotelPvCount];
                this.hotelPv = new DataHotelPv[this.hotelPvCount];
                for( i = 0 ; i < hotelPvCount ; i++ )
                {
                    hotelUniquePv[i] = new DataHotelUniquePv();
                    hotelPv[i] = new DataHotelPv();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.hotelUniquePv[count].setData( result );

                    // ホテルPV情報の取得
                    this.hotelPv[count].setCollectDate( result.getInt( "collect_date" ) );
                    this.hotelPv[count].setId( result.getInt( "id" ) );
                    this.hotelPv[count].setTotalUuPv( result.getInt( "total_uu_pv" ) );
                    this.hotelPv[count].setPrevDayRatio( result.getInt( "prev_day_ratio" ) );
                    count++;

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

        query = "SELECT PV.* FROM hh_hotel_unique_pv PV,hh_hotel_basic BASIC WHERE PV.collect_date = ?";
        query = query + " AND PV.id = BASIC.id";
        query = query + " GROUP BY PV.id";
        query = query + " ORDER BY PV.total_uu_pv DESC, PV.prev_day_ratio DESC, BASIC.name_kana";

        query = "SELECT UPV.* FROM hh_hotel_unique_pv UPV ";
        query += " INNER JOIN hh_hotel_basic HB ON HB.id = UPV.id AND HB.rank >=1 AND HB.kind <= 7";
        query += " LEFT JOIN hh_hotel_pv PV ON PV.id = UPV.id";

        // マンスリーのPV集計の場合はPV.collect_date = 0で結合
        if ( collectDate > 0 && collectDate % 100 == 0 )
        {
            query += "  AND PV.collect_date = " + (collectDate + 1);
        }
        // デイリーのPV集計の場合は同日データで結合
        else
        {
            query += "  AND PV.collect_date = UPV.collect_date";
        }

        query += " WHERE UPV.collect_date = ?";
        query += " ORDER BY UPV.total_uu_pv DESC, PV.total_uu_pv DESC, HB.rank DESC";

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
                this.hotelUniquePv = new DataHotelUniquePv[this.hotelPvCount];
                this.hotelPv = new DataHotelPv[this.hotelPvCount];

                for( i = 0 ; i < hotelPvCount ; i++ )
                {
                    hotelUniquePv[i] = new DataHotelUniquePv();
                    hotelPv[i] = new DataHotelPv();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.hotelUniquePv[count].setData( result );

                    // ホテルPV情報の取得
                    this.hotelPv[count].setCollectDate( result.getInt( "collect_date" ) );
                    this.hotelPv[count].setId( result.getInt( "id" ) );
                    this.hotelPv[count].setTotalUuPv( result.getInt( "total_uu_pv" ) );
                    this.hotelPv[count].setPrevDayRatio( result.getInt( "prev_day_ratio" ) );
                    count++;
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

        query = "SELECT PV.* FROM hh_hotel_unique_pv PV,hh_hotel_basic BASIC WHERE PV.collect_date = ?";
        query = query + " AND BASIC.pref_id = ?";
        query = query + " AND PV.id = BASIC.id";
        query = query + " GROUP BY PV.id";
        query = query + " ORDER BY PV.total_uu_pv DESC, PV.prev_day_ratio DESC, BASIC.name_kana";

        query = "SELECT UPV.* FROM hh_hotel_unique_pv UPV ";
        query += " INNER JOIN hh_hotel_basic HB ON HB.id = UPV.id AND HB.rank >=1 AND HB.kind <= 7 ";
        query += " LEFT JOIN hh_hotel_pv PV ON PV.id = UPV.id";

        // マンスリーのPV集計の場合はPV.collect_date = 0で結合
        if ( collectDate > 0 && collectDate % 100 == 0 )
        {
            query += "  AND PV.collect_date = " + (collectDate + 1);
        }
        // デイリーのPV集計の場合は同日データで結合
        else
        {
            query += "  AND PV.collect_date = UPV.collect_date";
        }

        query += " WHERE UPV.collect_date = ?";
        query += " AND HB.pref_id = ?";
        query += " ORDER BY UPV.total_uu_pv DESC, PV.total_uu_pv DESC, HB.rank DESC";

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
                this.hotelUniquePv = new DataHotelUniquePv[this.hotelPvCount];
                this.hotelPv = new DataHotelPv[this.hotelPvCount];
                for( i = 0 ; i < hotelPvCount ; i++ )
                {
                    hotelUniquePv[i] = new DataHotelUniquePv();
                    hotelPv[i] = new DataHotelPv();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.hotelUniquePv[count].setData( result );

                    // ホテルPV情報の取得
                    this.hotelPv[count].setCollectDate( result.getInt( "collect_date" ) );
                    this.hotelPv[count].setId( result.getInt( "id" ) );
                    this.hotelPv[count].setTotalUuPv( result.getInt( "total_uu_pv" ) );
                    this.hotelPv[count].setPrevDayRatio( result.getInt( "prev_day_ratio" ) );
                    count++;
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

        query = "SELECT hh_hotel_unique_pv.collect_date FROM hh_hotel_unique_pv ORDER BY collect_date DESC LIMIT 1";

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
