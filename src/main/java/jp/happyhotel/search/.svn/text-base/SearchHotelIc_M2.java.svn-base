package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapPoint_M2;

/**
 * 
 * This class handles the core InterChange search logic and the data fetching from database.
 * 
 * @author HCL Technologies Ltd.
 * 
 */

public class SearchHotelIc_M2 implements Serializable
{
    private static final long    serialVersionUID     = 2841015074152561820L;

    private int                  m_hotelAllCount;                            // Total number of hotels found
    private int                  m_mapPointCount;
    private int[]                m_hotelIdList        = null;                // List of hotel id's searched
    private int[]                m_mapPointHotelCount = null;
    private String               errorMessage         = "";
    private DataMapPoint_M2      m_icInfo             = null;
    private DataMapPoint_M2[]    m_mapPoint           = null;
    private SearchEngineBasic_M2 searchEngineBasic    = null;                // Basic Details regarding Hotels

    public SearchHotelIc_M2()
    {
        m_hotelAllCount = 0;
        m_mapPointCount = 0;
    }

    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int[] getHotelIdList()
    {
        return m_hotelIdList;
    }

    /** IC情報取得 **/
    public DataMapPoint_M2 getIcInfo()
    {
        return(m_icInfo);
    }

    public int getMapPointCount()
    {
        return m_mapPointCount;
    }

    public int[] getMapPointHotelCount()
    {
        return(m_mapPointHotelCount);
    }

    public DataMapPoint_M2[] getMapPoint()
    {
        return(m_mapPoint);
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * Gets IC Information
     * 
     * @param routeId 駅ID
     * @throws Exception
     */
    public void getMapPointInfo(String routeId) throws Exception
    {
        // IC情報の取得
        searchEngineBasic = new SearchEngineBasic_M2();
        // Sets Ic Information
        m_icInfo = searchEngineBasic.getDataMapPoint( routeId );

        searchEngineBasic = null;

    }

    /**
     * Acquires Hotel Ids for a given IcId or routeId
     * 
     * @param paramId ( icId or routeId )
     * 
     * @return hotelIdList
     * @throws Exception
     */
    public int[] getSearchIdList(String paramId) throws Exception
    {
        int count;
        String query;
        int[] hotelIdList = null;
        int totalHotel = 0;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_map,hh_hotel_sort"
                + " WHERE hh_hotel_map.map_id = ?"
                + " AND hh_hotel_basic.id = hh_hotel_map.id"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.pref_id <> 0"
                + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                + " AND hh_hotel_sort.collect_date = 0"
                + " AND hh_hotel_map.disp_flag = 1"
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        count = 0;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, paramId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    totalHotel = result.getRow();
                }

                hotelIdList = new int[totalHotel];

                result.beforeFirst();
                while( result.next() != false )
                {
                    hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
            return(hotelIdList);
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelIc_M2.getSearchIdList(String paramId : " + paramId + ")] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            hotelIdList = null;
        }
    }

    /**
     * Gets hotel data relating to ICName
     * 
     * @param paramIcName
     * @throws Exception
     */
    public void getHotelResultList(String paramIcName) throws Exception
    {
        boolean ret = false;
        searchEngineBasic = new SearchEngineBasic_M2();
        // Gets IC List and their data
        ret = searchEngineBasic.getIcListByName( paramIcName );

        if ( ret != false )
        {
            m_mapPointCount = searchEngineBasic.getMapPointCount();
            m_mapPointHotelCount = searchEngineBasic.getMapPointHotelCount();
            m_mapPoint = searchEngineBasic.getMapPoint();

        }
        else
        {
            errorMessage = "<li>見つかりませんでした。</li>";
        }
        searchEngineBasic = null;

    }

}
