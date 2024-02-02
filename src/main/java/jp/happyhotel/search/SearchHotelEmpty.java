/*
 * @(#)SearchHotelEmpty.java
 * 1.00 2009/07/10 Copyright (C) ALMEX Inc. 2009
 * ホテル周辺の駅、IC検索
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DistanceDetermination;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelDistance;

/**
 * 空満検索クラス（緯度経度から範囲内のホテルを探す）
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/22
 */
public class SearchHotelEmpty implements Serializable
{
    private static final long   serialVersionUID = 7240757951682101195L;

    private int                 hotelAllCount;
    private DataHotelDistance[] dhd;
    private int[]               hotelId;
    private int[]               distance;

    /**
     * データを初期化します。
     */
    public SearchHotelEmpty()
    {
        hotelAllCount = 0;
    }

    /**
     * @return 距離
     */
    public int[] getDistance()
    {
        return distance;
    }

    /**
     * @return ホテル数
     */
    public int getHotelAllCount()
    {
        return hotelAllCount;
    }

    /**
     * @return ホテルID,ホテルの距離
     */
    public DataHotelDistance[] getHotelDistance()
    {
        return dhd;
    }

    /**
     * @return ホテルID
     */
    public int[] getHotelId()
    {
        return hotelId;
    }

    /**
     * @param 距離
     */
    public void setDistance(int[] distance)
    {
        this.distance = distance;
    }

    /**
     * @param ホテル数
     */
    public void setHotelAllCount(int hotelAllCount)
    {
        this.hotelAllCount = hotelAllCount;
    }

    /**
     * @param ホテルID
     */
    public void setHotelId(int[] hotelId)
    {
        this.hotelId = hotelId;
    }

    /**
     * 範囲内のホテルを検索する
     * 
     * @param dlLat 左下の緯度
     * @param dlLon 左下の経度
     * @param urLat 右上の緯度
     * @param urLon 右上の経度
     * @param cLat 中心の緯度
     * @param cLon 中心の経度
     * @param kind 検索の種類
     * @return 処理結果（true:正常, false:異常）
     */
    public boolean getSearchHotel(int dlLat, int dlLon, int urLat, int urLon, int cLat, int cLon, int kind)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DistanceDetermination dd;

        // データの初期化
        this.hotelAllCount = 0;
        count = 0;
        dd = new DistanceDetermination();
        query = "SELECT hh_hotel_basic.id, hh_hotel_basic.hotel_lat_num, hh_hotel_basic.hotel_lon_num, hh_hotel_basic.hotel_lat, hh_hotel_basic.hotel_lon, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking" +
                " FROM hh_hotel_basic" +
                " LEFT JOIN hh_hotel_status" +
                " ON hh_hotel_basic.id = hh_hotel_status.id" +
                " , hh_hotel_master WHERE" +
                " hh_hotel_basic.id = hh_hotel_master.id" +
                " AND ( hh_hotel_basic.hotel_lat_num > ? AND hh_hotel_basic.hotel_lat_num < ? )" +
                " AND ( hh_hotel_basic.hotel_lon_num > ? AND hh_hotel_basic.hotel_lon_num < ? )" +
                " AND hh_hotel_basic.kind <= 7";
        if ( kind == 1 )
        {
            query = query + " AND hh_hotel_status.empty_status = 1";
            query = query + " AND hh_hotel_master.empty_disp_kind = 1";

        }
        else if ( kind == 2 )
        {
            query = query + " AND hh_hotel_basic.rank > 0";
        }
        query = query + " GROUP BY hh_hotel_basic.id" +
                " ORDER BY Ranking DESC, hh_hotel_basic.name_kana";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, dlLat );
            prestate.setInt( 2, urLat );
            prestate.setInt( 3, dlLon );
            prestate.setInt( 4, urLon );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }

                this.dhd = new DataHotelDistance[hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.dhd[count] = new DataHotelDistance();
                    this.dhd[count].setId( result.getInt( "id" ) );
                    this.dhd[count].setHotelLat( result.getString( "hotel_lat" ) );
                    this.dhd[count].setHotelLon( result.getString( "hotel_lon" ) );
                    this.dhd[count].setDistance( dd.getDistance( cLat, cLon, result.getInt( "hotel_lat_num" ), result.getInt( "hotel_lon_num" ) ) );
                    count++;
                }
                Arrays.sort( this.dhd );
                hotelId = new int[this.hotelAllCount];
                distance = new int[this.hotelAllCount];
                for( i = 0 ; i < this.hotelAllCount ; i++ )
                {
                    hotelId[i] = this.dhd[i].getId();
                    distance[i] = this.dhd[i].getDistance();
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelEmpty] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( hotelAllCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ホテルIDリストと中心地からそれぞれのホテルまでの距離を求める
     * 
     * @param cLat 中心の緯度
     * @param cLon 中心の経度
     * @param kind 検索の種類
     * @return 処理結果（true:正常, false:異常）
     */
    public boolean getSearchHotel(int[] hotelIdList, int cLat, int cLon, int kind)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DistanceDetermination dd;

        // データの初期化
        this.hotelAllCount = 0;
        count = 0;
        dd = new DistanceDetermination();

        if ( hotelIdList.length <= 0 )
        {
            return(false);
        }

        query = "SELECT hh_hotel_basic.id, hotel_lat_num, hotel_lon_num, hotel_lat, hotel_lon, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking " +
                " FROM hh_hotel_basic" +
                " LEFT JOIN hh_hotel_status" +
                " ON hh_hotel_basic.id = hh_hotel_status.id" +
                " WHERE" +
                " hh_hotel_basic.id IN (";

        for( i = 0 ; i < hotelIdList.length ; i++ )
        {
            if ( i == 0 )
            {
                query += hotelIdList[i];
            }
            else
            {
                query += ", " + hotelIdList[i];
            }
        }
        query += ") AND kind <= 7";
        if ( kind == 1 )
        {
            query = query + " AND hh_hotel_status.empty_status = 1";
        }
        else if ( kind == 2 )
        {
            query = query + " AND rank > 0";
        }
        query = query + " GROUP BY hh_hotel_basic.id" +
                " ORDER BY Ranking DESC, name_kana";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }

                this.dhd = new DataHotelDistance[hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.dhd[count] = new DataHotelDistance();
                    this.dhd[count].setId( result.getInt( "id" ) );
                    this.dhd[count].setHotelLat( result.getString( "hotel_lat" ) );
                    this.dhd[count].setHotelLon( result.getString( "hotel_lon" ) );
                    this.dhd[count].setDistance( dd.getDistance( cLat, cLon, result.getInt( "hotel_lat_num" ), result.getInt( "hotel_lon_num" ) ) );
                    count++;
                }
                Arrays.sort( this.dhd );
                hotelId = new int[this.hotelAllCount];
                distance = new int[this.hotelAllCount];
                for( i = 0 ; i < this.hotelAllCount ; i++ )
                {
                    hotelId[i] = this.dhd[i].getId();
                    distance[i] = this.dhd[i].getDistance();
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelEmpty] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( hotelAllCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 範囲内のホテルを検索する
     * 
     * @param dlLatNum 左下の緯度(int)
     * @param dlLonNum 左下の経度(int)
     * @param urLatNum 右上の緯度(int)
     * @param urLonNum 右上の経度(int)
     * @param cLat 中心の緯度(float)
     * @param cLon 中心の経度(float)
     * @param kind 検索の種類
     * @param scale 縮尺
     * @return 処理結果（true:正常, false:異常）
     */
    public boolean getSearchHotel(int dlLatNum, int dlLonNum, int urLatNum, int urLonNum, double cLat, double cLon, int kind, int scale)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // データの初期化
        this.hotelAllCount = 0;
        count = 0;
        query = "SELECT ( 6378140  *  acos( cos( radians(" + cLat + ") ) *  cos( radians( hotel_lat ) ) * cos( radians( hotel_lon ) " +
                " - radians(" + cLon + ") ) + sin( radians(" + cLat + ") ) * sin( radians( hotel_lat ) ) ) )  AS distance, " +
                "hh_hotel_basic.id, hh_hotel_basic.hotel_lat_num, hh_hotel_basic.hotel_lon_num, hh_hotel_basic.hotel_lat, hh_hotel_basic.hotel_lon" +
                " FROM hh_hotel_basic" +
                " LEFT JOIN hh_hotel_status" +
                " ON hh_hotel_basic.id = hh_hotel_status.id" +
                " , hh_hotel_master WHERE" +
                " hh_hotel_basic.id = hh_hotel_master.id" +
                " AND ( hh_hotel_basic.hotel_lat_num > ? AND hh_hotel_basic.hotel_lat_num < ? )" +
                " AND ( hh_hotel_basic.hotel_lon_num > ? AND hh_hotel_basic.hotel_lon_num < ? )" +
                " AND hh_hotel_basic.kind <= 7";
        if ( kind == 1 )
        {
            query = query + " AND hh_hotel_status.empty_status = 1";
            query = query + " AND hh_hotel_master.empty_disp_kind = 1";

        }
        else if ( kind == 2 )
        {
            query = query + " AND hh_hotel_basic.rank > 0";
        }
        query += " GROUP BY hh_hotel_basic.id HAVING distance <= ( ? * 120 ) / 10000 ORDER BY distance";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, dlLatNum );
            prestate.setInt( 2, urLatNum );
            prestate.setInt( 3, dlLonNum );
            prestate.setInt( 4, urLonNum );
            prestate.setInt( 5, scale );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }

                this.dhd = new DataHotelDistance[hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.dhd[count] = new DataHotelDistance();
                    this.dhd[count].setId( result.getInt( "id" ) );
                    this.dhd[count].setHotelLat( result.getString( "hotel_lat" ) );
                    this.dhd[count].setHotelLon( result.getString( "hotel_lon" ) );
                    this.dhd[count].setDistance( result.getInt( "distance" ) );
                    count++;
                }
                // Arrays.sort( this.dhd );
                hotelId = new int[this.hotelAllCount];
                distance = new int[this.hotelAllCount];
                for( i = 0 ; i < this.hotelAllCount ; i++ )
                {
                    hotelId[i] = this.dhd[i].getId();
                    distance[i] = this.dhd[i].getDistance();
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelEmpty] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( hotelAllCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ホテルIDリストと中心地からそれぞれのホテルまでの距離を求める
     * 
     * @param cLat 中心の緯度
     * @param cLon 中心の経度
     * @param kind 検索の種類
     * @return 処理結果（true:正常, false:異常）
     */
    public boolean getSearchHotel(int[] hotelIdList, String cLat, String cLon, int kind, int scale)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // データの初期化
        this.hotelAllCount = 0;
        count = 0;

        if ( hotelIdList.length <= 0 )
        {
            return(false);
        }

        query = "SELECT hh_hotel_basic.id, ( 6378140  *  acos( cos( radians(" + cLat + ") ) *  cos( radians( hotel_lat ) ) * cos( radians( hotel_lon ) " +
                " - radians(" + cLon + ") ) + sin( radians(" + cLat + ") ) * sin( radians( hotel_lat ) ) ) )  AS distance, " +
                " hotel_lat_num, hotel_lon_num, hotel_lat, hotel_lon " +
                " FROM hh_hotel_basic" +
                " LEFT JOIN hh_hotel_status" +
                " ON hh_hotel_basic.id = hh_hotel_status.id" +
                " WHERE" +
                " hh_hotel_basic.id IN (";

        for( i = 0 ; i < hotelIdList.length ; i++ )
        {
            if ( i == 0 )
            {
                query += hotelIdList[i];
            }
            else
            {
                query += ", " + hotelIdList[i];
            }
        }
        query += ") AND kind <= 7";
        if ( kind == 1 )
        {
            query = query + " AND hh_hotel_status.empty_status = 1";
        }
        else if ( kind == 2 )
        {
            query = query + " AND rank > 0";
        }
        query += " GROUP BY hh_hotel_basic.id HAVING distance <= ( ? * 120 ) / 10000 ORDER BY distance";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, scale );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }

                this.dhd = new DataHotelDistance[hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.dhd[count] = new DataHotelDistance();
                    this.dhd[count].setId( result.getInt( "id" ) );
                    this.dhd[count].setHotelLat( result.getString( "hotel_lat" ) );
                    this.dhd[count].setHotelLon( result.getString( "hotel_lon" ) );
                    this.dhd[count].setDistance( result.getInt( "distance" ) );
                    count++;
                }
                hotelId = new int[this.hotelAllCount];
                distance = new int[this.hotelAllCount];
                for( i = 0 ; i < this.hotelAllCount ; i++ )
                {
                    hotelId[i] = this.dhd[i].getId();
                    distance[i] = this.dhd[i].getDistance();
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelEmpty] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( hotelAllCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }
}
