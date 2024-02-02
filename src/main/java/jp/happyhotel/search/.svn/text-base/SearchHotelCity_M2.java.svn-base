/*
 * @(#)SearchHotelCity.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 市区町村別ホテル取得クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterCity_M2;

/**
 * 市区町村別ホテル取得クラス
 * 
 * @HCL TECHNOLOGIES
 * 
 */
public class SearchHotelCity_M2 implements Serializable
{
    private static final long serialVersionUID = 2104164772288242328L;

    private int               m_hotelCount;

    private int               m_hotelAllCount;

    private DataMasterCity_M2 m_cityInfo;

    private int[]             hotelIdList;

    /**
     * データを初期化します。
     */
    public SearchHotelCity_M2()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;
        // m_hotelInfo = new DataHotelBasic[0];
        // m_hotelInfo = new DataSearchHotel_M2[0];
        hotelIdList = new int[0];
    }

    /** ホテル基本情報件数取得 * */
    public int getCount()
    {
        return(m_hotelCount);
    }

    /** ホテル基本情報件数取得 * */
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    /** 市区町村情報取得 * */
    public DataMasterCity_M2 getCityInfo()
    {
        return(m_cityInfo);
    }

    public int[] getHotelIdList()
    {
        return hotelIdList;
    }

    /**
     * it searches Hotels in a given city
     * 
     * @param jisCode
     * @return
     */
    public int[] getHotelIdList(int jisCode) throws Exception
    {
        int i = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int hotelIds[] = null;

        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_sort WHERE jis_code = ?"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                + " AND hh_hotel_sort.collect_date = 0"
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        m_hotelCount = 0;
        m_hotelAllCount = 0;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, jisCode );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_hotelAllCount = result.getRow();
                }

                hotelIds = new int[m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    hotelIds[i] = Integer.parseInt( result.getString( "id" ) );
                    i++;
                    // ホテル情報の取得
                    // this.m_hotelInfo[count++].setData( result );
                }
            }
            return hotelIds;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelCity_M2.getHotelList(" + jisCode + ")] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            hotelIds = null;
        }

    }

}
