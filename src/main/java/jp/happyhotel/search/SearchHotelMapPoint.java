/*
 * @(#)SearchHotelMap.java 1.00 2009/07/10 Copyright (C) ALMEX Inc. 2009 ホテル周辺の駅、IC検索
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapPoint;

/**
 * ホテル周辺の駅、IC検索
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/10
 */
public class SearchHotelMapPoint implements Serializable
{

    private static final long serialVersionUID = -6939301017250195719L;

    private int               m_stationCount;
    private int               m_icCount;
    private String[]          m_stationDistance;
    private String[]          m_icDistance;
    private DataMapPoint[]    m_dmPointStation;
    private DataMapPoint[]    m_dmPointIC;

    /**
     * データを初期化します。
     */
    public SearchHotelMapPoint()
    {
        m_stationCount = 0;
        m_icCount = 0;
    }

    /**
     * @return 近くのICのマップポイントデータ
     */
    public DataMapPoint[] getIc()
    {
        return m_dmPointIC;
    }

    /**
     * @return 近くのIC件数
     */
    public int getIcCount()
    {
        return m_icCount;
    }

    /**
     * @return m_icDistance
     */
    public String[] getIcDistance()
    {
        return m_icDistance;
    }

    /**
     * @return 近くの駅のマップポイントデータ
     */
    public DataMapPoint[] getStation()
    {
        return m_dmPointStation;
    }

    /**
     * @return 近くの駅件数
     */
    public int getStationCount()
    {
        return m_stationCount;
    }

    /**
     * @return m_stationDistance
     */
    public String[] getStationDistance()
    {
        return m_stationDistance;
    }

    /**
     * @param 近くのICのマップポイントデータ
     */
    public void setIc(DataMapPoint[] ic)
    {
        m_dmPointIC = ic;
    }

    /**
     * @param 近くのIC件数
     */
    public void setIcCount(int icCount)
    {
        m_icCount = icCount;
    }

    /**
     * @param mIcDistance セットする m_icDistance
     */
    public void setIcDistance(String[] icDistance)
    {
        m_icDistance = icDistance;
    }

    /**
     * @param 近くの駅のマップポイントデータ
     */
    public void setStation(DataMapPoint[] station)
    {
        m_dmPointStation = station;
    }

    /**
     * @param 近くの駅件数
     */
    public void setStationCount(int stationCount)
    {
        m_stationCount = stationCount;
    }

    /**
     * @param mStationDistance セットする m_stationDistance
     */
    public void setStationDistance(String[] stationDistance)
    {
        m_stationDistance = stationDistance;
    }

    /**
     * ホテル周辺の駅、IC検索
     * 
     * @param hotelId ホテルID
     * @return 処理結果（true:正常, false:異常）
     */
    public boolean getSearchHotelNear(String hotelId)
    {
        int i;
        int countSt;
        int countIc;
        int countAll;
        String[] distance;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataMapPoint[] dmPoint;

        countSt = 0;
        countIc = 0;
        countAll = 0;
        query = "SELECT hh_hotel_map.distance, hh_map_point.*" +
                " FROM hh_hotel_map, hh_map_point" +
                " WHERE hh_hotel_map.id = ?" +
                        // " AND hh_hotel_map.distance <= 5000" +
                " AND hh_hotel_map.disp_flag = 1" +
                " AND hh_hotel_map.map_id = hh_map_point.option_4" +
                " GROUP BY hh_map_point.option_4" +
                " ORDER BY hh_hotel_map.distance";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    countAll = result.getRow();
                }

                dmPoint = new DataMapPoint[countAll];
                distance = new String[countAll];

                result.beforeFirst();
                while( result.next() != false )
                {
                    // class_codeの頭が4だったらICカウントを増やし、5だったら駅のカウントを増やす
                    if ( result.getString( "class_code" ).startsWith( "4" ) )
                    {
                        m_icCount++;
                    }
                    else if ( result.getString( "class_code" ).startsWith( "5" ) )
                    {
                        m_stationCount++;
                    }
                    dmPoint[countSt] = new DataMapPoint();
                    dmPoint[countSt].setData( result );
                    distance[countSt] = Integer.toString( result.getInt( "distance" ) );
                    countSt++;
                }

                countSt = 0;
                if ( countAll > 0 )
                {
                    if ( m_icCount > 0 || m_stationCount > 0 )
                    {
                        // ICの要素数があれば新しく作る
                        if ( m_icCount > 0 )
                        {
                            m_icDistance = new String[m_icCount];
                            m_dmPointIC = new DataMapPoint[m_icCount];
                        }
                        // 駅の要素数があれば新しく作る
                        if ( m_stationCount > 0 )
                        {
                            m_stationDistance = new String[m_stationCount];
                            m_dmPointStation = new DataMapPoint[m_stationCount];
                        }
                        // DBから取得した分だけ繰り返し、それぞれの配列に分ける
                        for( i = 0 ; i < countAll ; i++ )
                        {
                            if ( dmPoint[i].getClassCode().startsWith( "4" ) != false )
                            {
                                m_icDistance[countIc] = distance[i];
                                m_dmPointIC[countIc] = new DataMapPoint();
                                m_dmPointIC[countIc] = dmPoint[i];
                                countIc++;
                            }
                            if ( dmPoint[i].getClassCode().startsWith( "5" ) != false )
                            {
                                m_stationDistance[countSt] = distance[i];
                                m_dmPointStation[countSt] = new DataMapPoint();
                                m_dmPointStation[countSt] = dmPoint[i];
                                countSt++;
                            }
                        }
                    }
                }
            }
            return(true);
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelMapPoint] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            dmPoint = null;
        }

        return(false);
    }

    /**
     * ホテル周辺の駅検索
     * 
     * @param hotelId ホテルID
     * @return 処理結果（true:正常, false:異常）
     */
    public boolean getSearchHotelNearStation(String hotelId)
    {
        int i;
        int count;
        String[] distance;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        count = 0;
        query = "SELECT hh_hotel_map.distance, hh_map_point.*" +
                " FROM hh_hotel_map, hh_map_point" +
                " WHERE hh_hotel_map.id = ?" +
                        // " AND hh_hotel_map.distance <= 5000" +
                " AND hh_hotel_map.disp_flag = 1" +
                " AND hh_map_point.class_code like '5%'" +
                " AND hh_hotel_map.map_id = hh_map_point.option_4" +
                " GROUP BY hh_map_point.option_4" +
                " ORDER BY hh_hotel_map.distance";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    m_stationCount = result.getRow();
                }

                m_dmPointStation = new DataMapPoint[m_stationCount];
                m_stationDistance = new String[m_stationCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_dmPointStation[count] = new DataMapPoint();
                    m_dmPointStation[count].setData( result );
                    m_stationDistance[count] = Integer.toString( result.getInt( "distance" ) );
                    count++;
                }
            }
            return(true);
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelMapPoint.getSearchHotelNearStation] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(false);
    }

    /**
     * ホテル周辺のIC検索
     * 
     * @param hotelId ホテルID
     * @return 処理結果（true:正常, false:異常）
     */
    public boolean getSearchHotelNearIc(String hotelId)
    {
        int i;
        int count;
        String[] distance;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        count = 0;
        query = "SELECT hh_hotel_map.distance, hh_map_point.*" +
                " FROM hh_hotel_map, hh_map_point" +
                " WHERE hh_hotel_map.id = ?" +
                        // " AND hh_hotel_map.distance <= 5000" +
                " AND hh_hotel_map.disp_flag = 1" +
                " AND hh_map_point.class_code like '4%'" +
                " AND ( hh_map_point.name like '%出口%'" +
                " OR hh_map_point.name like '%出入口%'" +
                " OR hh_map_point.name like '%インターチェンジ%' )" +
                " AND hh_hotel_map.map_id = hh_map_point.option_4" +
                " ORDER BY hh_hotel_map.distance";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.m_icCount = result.getRow();
                }

                m_dmPointIC = new DataMapPoint[m_icCount];
                m_icDistance = new String[m_icCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_dmPointIC[count] = new DataMapPoint();
                    m_dmPointIC[count].setData( result );
                    m_icDistance[count] = Integer.toString( result.getInt( "distance" ) );
                    count++;
                }
            }
            return(true);
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelMapPoint.getSearchHotelNearIc] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(false);
    }

}
