package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelArea_M2;
import jp.happyhotel.data.DataMasterHotelArea_M2;

/**
 * Fetch Hotel Area Information
 * 
 * @author HCL Technologies Ltd
 * @version 2.0 2008/09/23
 */
public class SearchHotelArea_M2 implements Serializable
{
    private static final long serialVersionUID = 2104164772288242328L;

    private int               m_hotelAllCount;
    private int               m_pref_id;
    private int               m_areaCount;
    private String            m_area_name;

    /**
     * データを初期化します。
     */
    public SearchHotelArea_M2()
    {
        // m_hotelCount = 0;
        m_hotelAllCount = 0;
        m_areaCount = 0;
        m_area_name = "";
        m_pref_id = 0;
    }

    /** Fetch Area Information **/
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int getAreaCount()
    {
        return(m_areaCount);
    }

    public String getAreaName()
    {
        return(m_area_name);
    }

    public int getPrefId()
    {
        return(m_pref_id);
    }

    /**
     * It will fetch Hotel Id List corresponding to areaId
     * 
     * @param areaId
     * @return int[] hotelIdList
     */
    public int[] getSearchIdList(int areaId) throws Exception
    {

        int[] arrHotelIdList = null;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_area,hh_hotel_sort WHERE hh_hotel_area.area_id = ?"
                + " AND hh_hotel_basic.id = hh_hotel_area.id"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.pref_id <> 0"
                + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                + " AND hh_hotel_sort.collect_date = 0"
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        count = 0;

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, areaId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.m_hotelAllCount = result.getRow();

                }

                arrHotelIdList = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    arrHotelIdList[count++] = result.getInt( "id" );
                }
            }
            return arrHotelIdList;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelArea_M2.getSearchIdList : AreaId = " + areaId + "] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrHotelIdList = null;
        }
    }

    /**
     * It will fetch Hotel Area Details corresponding to areaId
     * 
     * @param areaId
     */
    public void getHotelAreaDetail(int areaId) throws Exception
    {

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT HMA.name as area_name,HMA.pref_id"
                + " FROM hh_master_area HMA where HMA.area_id=? ";

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, areaId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    m_area_name = result.getString( "area_name" );
                    m_pref_id = result.getInt( "pref_id" );

                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelArea_M2.getHotelAreaDetail : AreaId = " + areaId + "] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

    }

    /**
     * Fetch Data for Hotel Area By Local Id
     * 
     * @param localId
     * @return
     * @throws Exception
     */
    public DataMasterHotelArea_M2[] getHotelAreaListByLocal(int localId) throws Exception
    {
        int hotelcount = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        DataMasterHotelArea_M2[] arrDataMasterArea = null;
        query = " select HMA.area_id,HMP.pref_id,HML.local_id,HMA.name,HMP.name,HML.name, count(HA.id) as hotel_count"
                + " from hh_master_area HMA, hh_hotel_area HA, hh_master_pref HMP, hh_master_local HML, hh_hotel_basic HB "
                + " where HMA.pref_id = HMP.pref_id"
                + " and HMP.local_id = HML.local_id"
                + " and HA.id = HB.id"
                + " and HB.kind <= 7"
                + " and HB.pref_id <> 0"
                + " and HMA.area_id = HA.area_id ";
        if ( localId != 0 )
        {
            query = query + " and HML.local_id = " + localId;
        }
        query = query + " group by HMA.area_id"
                + " order by HMP.local_id, HMP.pref_id, HMA.disp_index";

        try
        {
            connection = DBConnection.getConnectionRO();

            prestate = connection.prepareStatement( query );

            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    hotelcount = result.getRow();
                }
                arrDataMasterArea = new DataMasterHotelArea_M2[hotelcount];
                result.beforeFirst();
                for( int i = 0 ; result.next() ; i++ )
                {
                    arrDataMasterArea[i] = new DataMasterHotelArea_M2();
                    arrDataMasterArea[i].setAreaId( result.getInt( "HMA.area_id" ) );
                    arrDataMasterArea[i].setAreaPrefId( result.getInt( "HMP.pref_id" ) );
                    arrDataMasterArea[i].setAreaLocalId( result.getInt( "HML.local_id" ) );
                    arrDataMasterArea[i].setName( CheckString.checkStringForNull( result.getString( "HMA.name" ) ) );
                    arrDataMasterArea[i].setAreaPrefName( CheckString.checkStringForNull( result.getString( "HMP.name" ) ) );
                    arrDataMasterArea[i].setAreaLocalName( CheckString.checkStringForNull( result.getString( "HML.name" ) ) );
                    arrDataMasterArea[i].setAreaHotelCount( result.getInt( "hotel_count" ) );

                }
            }
            return arrDataMasterArea;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelArea_M2.getHotelAreaListByLocal() : LocalId =" + localId + " ] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrDataMasterArea = null;
        }

    }

    /**
     * This function gets the Precture Name
     * 
     * @param prefId
     * @return boolean
     */

    public String getPrefInfo(int prefId) throws Exception
    {

        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String prefName = "";

        if ( prefId > 0 && prefId < 48 )
        {
            query = "SELECT name FROM hh_master_pref"
                    + " WHERE pref_id = ?";

            try
            {
                connection = DBConnection.getConnectionRO();
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, prefId );
                result = prestate.executeQuery();

                if ( result != null )
                {
                    while( result.next() != false )
                    {
                        prefName = result.getString( "name" );

                    }
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[SearchArea_M2.getPrefInfo( int prefId = " + prefId + ")] Exception=" + e.toString() );
                throw e;
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }
        return prefName;
    }

    /**
     * It will fetch Hotel Area Details By PrefId
     * 
     * @param prefId
     * @return
     */
    public DataHotelArea_M2[] getAreaListByPref(int prefId) throws Exception
    {

        int count;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        DataHotelArea_M2 DataHotelArea[] = null;

        query = "SELECT HMA.area_id, HMA.name, count( HB.id ) AS hotel_count"
                + " FROM hh_master_area HMA, hh_hotel_area HHA, hh_hotel_basic HB"
                + " WHERE HMA.area_id = HHA.area_id"
                + " AND HHA.id = HB.id"
                + " AND HMA.pref_id =?"
                + " AND HB.kind <=7"
                + " AND HB.pref_id <> 0"
                + " GROUP BY HMA.area_id"
                + " order by HMA.disp_index";

        try
        {
            connection = DBConnection.getConnectionRO();

            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );

            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    m_areaCount = result.getRow();
                }

                result.beforeFirst();
                DataHotelArea = new DataHotelArea_M2[m_areaCount];
                for( count = 0 ; result.next() ; count++ )
                {
                    DataHotelArea[count] = new DataHotelArea_M2();
                    DataHotelArea[count].setAreaId( result.getInt( "area_id" ) );
                    DataHotelArea[count].setAreaName( result.getString( "name" ) );
                    DataHotelArea[count].setHotelCount( result.getInt( "hotel_count" ) );
                }
            }
            return DataHotelArea;
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchHotelArea_M2.getAreaListByPref() : PrefId = " + prefId + "] Exception=" + e.toString() );
            throw e;

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            DataHotelArea = null;
        }
    }
}
