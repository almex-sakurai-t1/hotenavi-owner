package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelCity_M2;

/**
 * 
 * This class will delegate required method for fetching City information
 * 
 * @author HCL Technologies Ltd
 * @version 2.0 2008/08/19
 */
public class SearchArea_M2 implements Serializable
{
    private static final long serialVersionUID  = 2104164772288242328L;
    private int               HappieMemberHotel = 3;
    private int               m_hotelAllCount;
    private int               m_pref_id;
    private int               m_cityCount;
    private String            m_pref_name;
    private String            m_city_name;

    /**
     * データを初期化します。
     */
    public SearchArea_M2()
    {
        m_hotelAllCount = 0;
        m_cityCount = 0;
        m_city_name = "";
        m_pref_name = "";
        m_pref_id = 0;
    }

    /** Fetch City Information * */
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int getCityCount()
    {
        return(m_cityCount);
    }

    public String getCityName()
    {
        return(m_city_name);
    }

    public int getPrefId()
    {
        return(m_pref_id);
    }

    public String getPrefName()
    {
        return(m_pref_name);
    }

    /**
     * ホテルIDリスト取得（市区町村コード）
     * 
     * @param int jisCode
     * @return int[] hotelId List
     * @throws Exception
     */

    public int[] getSearchIdList(int jisCode) throws Exception
    {

        int[] arrHotelIdList = null;
        int count = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic ,hh_hotel_sort WHERE jis_code = ?"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                + " AND hh_hotel_sort.collect_date = 0"
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, jisCode );
            result = prestate.executeQuery();

            if ( result != null )
            {

                // レコード件数取得
                if ( result.last() )
                {
                    // 総件数の取得
                    this.m_hotelAllCount = result.getRow();
                }

                arrHotelIdList = new int[this.m_hotelAllCount];
                result.beforeFirst();

                while( result.next() )
                {
                    arrHotelIdList[count++] = result.getInt( "id" );
                }
            }
            return arrHotelIdList;
        }
        catch ( Exception exception )
        {
            Logging.error( "[SearchArea_M2.getSearchIdList] Exception="
                    + exception.toString() );
            throw exception;

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrHotelIdList = null;
        }

    }

    /**
     * ハピー加盟店ホテルIDリスト取得（市区町村コード）
     * 
     * @param int jisCode
     * @param int reserve(0：予約ありも含む、1：予約有のみ)
     * @return int[] hotelId List
     * @throws Exception
     */

    public int[] getSearchHappieIdList(int jisCode, int reserve) throws Exception
    {

        int[] arrHotelIdList = null;
        int count = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 予約ハピー加盟店のみを検索する場合
        if ( reserve == 1 )
        {
            query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_sort, newRsvDB.hh_rsv_reserve_basic"
                    + " WHERE jis_code = ?"
                    + " AND hh_hotel_basic.rank >= " + HappieMemberHotel
                    + " AND hh_hotel_basic.kind <= 7"
                    + " AND hh_rsv_reserve_basic.sales_flag = 1"
                    + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                    + " AND hh_hotel_basic.id = hh_rsv_reserve_basic.id"
                    + " AND hh_hotel_sort.collect_date = 0"
                    + " GROUP BY hh_hotel_basic.id"
                    + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        }
        // ハピー加盟店を検索する場合
        else
        {
            query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_sort WHERE jis_code = ?"
                    + " AND hh_hotel_basic.rank >= " + HappieMemberHotel
                    + " AND hh_hotel_basic.kind <= 7"
                    + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                    + " AND hh_hotel_sort.collect_date = 0"
                    + " GROUP BY hh_hotel_basic.id"
                    + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";
        }

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, jisCode );
            result = prestate.executeQuery();

            if ( result != null )
            {

                // レコード件数取得
                if ( result.last() )
                {
                    // 総件数の取得
                    this.m_hotelAllCount = result.getRow();
                }

                arrHotelIdList = new int[this.m_hotelAllCount];
                result.beforeFirst();

                while( result.next() )
                {
                    arrHotelIdList[count++] = result.getInt( "id" );
                }
            }
            return arrHotelIdList;
        }
        catch ( Exception exception )
        {
            Logging.error( "[SearchArea_M2.getSearchIdList] Exception="
                    + exception.toString() );
            throw exception;

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrHotelIdList = null;
        }

    }

    /**
     * ハピー加盟店ホテルIDリスト取得（地域指定）
     * 
     * @param prefId
     * @param reserve(0：予約ありも含む、1：予約有のみ)
     * @return int[] hotelId List
     * @throws Exception
     * 
     */
    public int[] getSearchHappieIdListByPrefId(int prefId, int reserve) throws Exception
    {
        int[] arrHotelIdList = null;
        int count = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 予約ハピー加盟店のみを検索する場合
        if ( reserve == 1 )
        {
            query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_sort, newRsvDB.hh_rsv_reserve_basic"
                    + " WHERE pref_id = ?"
                    + " AND hh_hotel_basic.rank >= " + HappieMemberHotel
                    + " AND hh_hotel_basic.kind <= 7"
                    + " AND hh_rsv_reserve_basic.sales_flag = 1"
                    + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                    + " AND hh_hotel_basic.id = hh_rsv_reserve_basic.id"
                    + " AND hh_hotel_sort.collect_date = 0"
                    + " GROUP BY hh_hotel_basic.id"
                    + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";
        }
        // ハピー加盟店を検索する場合
        else
        {
            query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_sort WHERE pref_id = ?"
                    + " AND hh_hotel_basic.rank >= " + HappieMemberHotel
                    + " AND hh_hotel_basic.kind <= 7"
                    + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                    + " AND hh_hotel_sort.collect_date = 0"
                    + " GROUP BY hh_hotel_basic.id"
                    + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";
        }

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefId );
            result = prestate.executeQuery();

            if ( result != null )
            {

                // レコード件数取得
                if ( result.last() )
                {
                    // 総件数の取得
                    this.m_hotelAllCount = result.getRow();
                }

                arrHotelIdList = new int[this.m_hotelAllCount];
                result.beforeFirst();

                while( result.next() )
                {
                    arrHotelIdList[count++] = result.getInt( "id" );
                }
            }
            return arrHotelIdList;
        }
        catch ( Exception exception )
        {
            Logging.error( "[SearchArea_M2.getSearchHappieIdListByPrefId] Exception="
                    + exception.toString() );
            throw exception;

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrHotelIdList = null;
        }
    }

    /**
     * 市区町村詳細データ
     * 
     * @param jisCode
     */
    public void getCityDetail(int jisCode) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT HMC.name as city_name,HMP.pref_id,HMP.name  as pref_name"
                + " FROM hh_master_city HMC ,hh_master_pref HMP where HMC.jis_code=? "
                + " AND HMC.pref_id=HMP.pref_id";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, jisCode );
            result = prestate.executeQuery();

            if ( result != null )
            {

                while( result.next() )
                {
                    m_city_name = result.getString( "city_name" );
                    m_pref_id = result.getInt( "pref_id" );
                    m_pref_name = result.getString( "pref_name" );
                }
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[SearchArea_M2.getCityDetail : JisCode = " + jisCode
                    + "] Exception=" + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

    }

    /**
     * 都道府県名取得
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

        try
        {
            if ( prefId > 0 && prefId < 48 )
            {
                query = "SELECT name FROM hh_master_pref"
                        + " WHERE pref_id = ?";

                connection = DBConnection.getReadOnlyConnection();
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, prefId );
                result = prestate.executeQuery();

                if ( result != null )
                {
                    while( result.next() )
                    {
                        prefName = result.getString( "name" );
                    }
                }
            }
            return prefName;
        }
        catch ( Exception exception )
        {
            Logging.info( "[SearchArea_M2.getPrefInfo( int prefId = " + prefId
                    + ")] Exception=" + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 都道府県IDから市区町村の取得
     * 
     * @param prefId
     * @return
     * @throws Exception
     */
    public DataHotelCity_M2[] getCityListByPref(int prefId) throws Exception
    {

        int count = 0;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        DataHotelCity_M2[] arrDataHotelCity = null;

        query = "SELECT HMC.jis_code,HMC.name,HMP.name as pref_name,count(HB.id) as hotel_count "
                + " FROM hh_master_city HMC,hh_hotel_basic HB,hh_master_pref HMP "
                + " WHERE HMC.pref_id = ?"
                + " AND HB.id <>0 "
                + " AND HMP.pref_id = HMC.pref_id"
                + " AND HB.jis_code = HMC.jis_code "
                + "AND HB.kind <=7 group by HMC.jis_code";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefId );
            result = prestate.executeQuery();

            if ( result.next() )
            {
                if ( result.last() )
                {
                    m_cityCount = result.getRow();
                }

                result.beforeFirst();
                arrDataHotelCity = new DataHotelCity_M2[m_cityCount];
                for( count = 0 ; result.next() ; count++ )
                {
                    arrDataHotelCity[count] = new DataHotelCity_M2();
                    arrDataHotelCity[count].setJisCode( result
                            .getInt( "jis_code" ) );
                    arrDataHotelCity[count].setCityName( result
                            .getString( "name" ) );
                    arrDataHotelCity[count].setHotelCount( result
                            .getInt( "hotel_count" ) );
                    arrDataHotelCity[count].setPrefId( prefId );
                    arrDataHotelCity[count].setPrefName( result
                            .getString( "pref_name" ) );

                }
            }
            return arrDataHotelCity;
        }
        catch ( Exception exception )
        {
            Logging.error( "[SearchArea_M2.getCityListByPref(" + prefId
                    + ")] Exception=" + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrDataHotelCity = null;
        }
    }

    /**
     * 都道府県IDから市区町村の取得
     * 
     * @param prefId 都道府県ID
     * @return
     * @throws Exception
     */
    public DataHotelCity_M2[] getCityListHappieByPref(int prefId) throws Exception
    {

        int count = 0;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        DataHotelCity_M2[] arrDataHotelCity = null;

        query = "SELECT HMC.jis_code,HMC.name,HMP.name as pref_name,count(HB.id) as hotel_count "
                + " FROM hh_master_city HMC,hh_hotel_basic HB,hh_master_pref HMP "
                + " WHERE HMC.pref_id = ?"
                + " AND HB.id <>0 "
                + " AND HMP.pref_id = HMC.pref_id"
                + " AND HB.jis_code = HMC.jis_code"
                + " AND HB.rank >=" + HappieMemberHotel
                + " AND HB.kind <=7 group by HMC.jis_code";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefId );
            result = prestate.executeQuery();

            if ( result.next() )
            {
                if ( result.last() )
                {
                    m_cityCount = result.getRow();
                }

                result.beforeFirst();
                arrDataHotelCity = new DataHotelCity_M2[m_cityCount];
                for( count = 0 ; result.next() ; count++ )
                {
                    arrDataHotelCity[count] = new DataHotelCity_M2();
                    arrDataHotelCity[count].setJisCode( result
                            .getInt( "jis_code" ) );
                    arrDataHotelCity[count].setCityName( result
                            .getString( "name" ) );
                    arrDataHotelCity[count].setHotelCount( result
                            .getInt( "hotel_count" ) );
                    arrDataHotelCity[count].setPrefId( prefId );
                    arrDataHotelCity[count].setPrefName( result
                            .getString( "pref_name" ) );

                }
            }
            return arrDataHotelCity;
        }
        catch ( Exception exception )
        {
            Logging.error( "[SearchArea_M2.getCityListByPref(" + prefId
                    + ")] Exception=" + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrDataHotelCity = null;
        }
    }

    /**
     * 都道府県IDから市区町村の取得
     * 
     * @param prefId 都道府県ID
     * @return
     * @throws Exception
     */
    public DataHotelCity_M2[] getCityListRsvHappieByPref(int prefId) throws Exception
    {

        int count = 0;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        DataHotelCity_M2[] arrDataHotelCity = null;

        query = "SELECT HMC.jis_code,HMC.name,HMP.name as pref_name,count(HB.id) as hotel_count "
                + " FROM hh_master_city HMC,hh_hotel_basic HB,hh_master_pref HMP, newRsvDB.hh_rsv_reserve_basic HRRB "
                + " WHERE HMC.pref_id = ?"
                + " AND HB.id <>0 "
                + " AND HB.id = HRRB.id"
                + " AND HMP.pref_id = HMC.pref_id"
                + " AND HB.jis_code = HMC.jis_code"
                + " AND HB.rank >=" + HappieMemberHotel
                + " AND HB.kind <=7"
                + " AND HRRB.sales_flag = 1"
                + " GROUP BY HMC.jis_code";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefId );
            result = prestate.executeQuery();

            if ( result.next() )
            {
                if ( result.last() )
                {
                    m_cityCount = result.getRow();
                }

                result.beforeFirst();
                arrDataHotelCity = new DataHotelCity_M2[m_cityCount];
                for( count = 0 ; result.next() ; count++ )
                {
                    arrDataHotelCity[count] = new DataHotelCity_M2();
                    arrDataHotelCity[count].setJisCode( result
                            .getInt( "jis_code" ) );
                    arrDataHotelCity[count].setCityName( result
                            .getString( "name" ) );
                    arrDataHotelCity[count].setHotelCount( result
                            .getInt( "hotel_count" ) );
                    arrDataHotelCity[count].setPrefId( prefId );
                    arrDataHotelCity[count].setPrefName( result
                            .getString( "pref_name" ) );

                }
            }
            return arrDataHotelCity;
        }
        catch ( Exception exception )
        {
            Logging.error( "[SearchArea_M2.getCityListByPref(" + prefId
                    + ")] Exception=" + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrDataHotelCity = null;
        }
    }

}
