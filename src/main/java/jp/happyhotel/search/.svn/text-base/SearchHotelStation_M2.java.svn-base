package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.ConvertGeodesic;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapPoint_M2;
import jp.happyhotel.data.DataMapRoute_M2;

/**
 * 駅別ホテル取得クラス
 * 
 * @author HCL
 */
public class SearchHotelStation_M2 implements Serializable
{
    private static final long serialVersionUID = -546461189304906761L;

    // private int m_hotelCount;

    private int               m_hotelAllCount;

    /** 都道府県ID * */
    private int               prefId;

    private int               m_mapRouteCount;

    private int               m_mapPointCount;

    private String            prefName;

    private int[]             m_hotelIdList;

    private int[]             m_mapPointHotelCount;

    // private DataSearchHotel_M2[] m_hotelInfo;

    private DataMapRoute_M2[] m_mapRoute;

    private DataMapPoint_M2[] m_mapPoint;

    private DataMapPoint_M2   m_stationInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelStation_M2()
    {
        // m_hotelCount = 0;
        m_hotelAllCount = 0;
        prefId = 0;
        m_mapRouteCount = 0;
        prefName = "";
    }

    public String getPrefName()
    {
        return prefName;
    }

    public int getMapRouteCount()
    {
        return(m_mapRouteCount);
    }

    public DataMapRoute_M2[] getMapRoute()
    {
        return(m_mapRoute);
    }

    public int getMapPointCount()
    {
        return(m_mapPointCount);
    }

    public int[] getMapPointHotelCount()
    {
        return(m_mapPointHotelCount);
    }

    public DataMapPoint_M2[] getMapPoint()
    {
        return(m_mapPoint);
    }

    // /** ホテル基本情報件数取得 * */
    // public int getCount() {
    // return (m_hotelCount);
    // }

    /** ホテル基本情報件数取得 * */
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    // /** ホテル基本情報取得 * */
    // public DataSearchHotel_M2[] getHotelInfo() {
    // return (m_hotelInfo);
    // }

    /** 駅情報取得 * */
    public DataMapPoint_M2 getStationInfo()
    {
        return(m_stationInfo);
    }

    public int[] getHotelIdList()
    {
        return m_hotelIdList;
    }

    public int getDataMasterCityPrefId()
    {
        return prefId;
    }

    /**
     * 路線の一覧を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getRailwayRouteList(int prefId) throws Exception
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_route.route_id, hh_map_route.name, hh_master_pref.name as prefname FROM "
                + "hh_map_route_pref,hh_map_route,hh_master_pref "
                + "WHERE hh_map_route_pref.pref_id = ? "
                + "AND hh_map_route_pref.pref_id = hh_master_pref.pref_id "
                + "AND hh_map_route_pref.route_id=hh_map_route.route_id "
                + "AND ( "
                + "hh_map_route.class_code='511@' "
                + "OR hh_map_route.class_code='512@' "
                + "OR hh_map_route.class_code='513@' "
                + "OR hh_map_route.class_code='514@' "
                + "OR hh_map_route.class_code='516@' "
                + "OR hh_map_route.class_code='517@' " + ")";
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
                ret = getRailwayRouteListSub( prestate );
            }
        }
        catch ( Exception exception )
        {
            Logging
                    .info( "[SearchEngineBasic_M2.getRailwayRouteList()] Exception for prefId ("
                            + prefId + ") - " + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 路線の一覧データをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    private boolean getRailwayRouteListSub(PreparedStatement prestate)
            throws Exception
    {
        int i = 0;
        int count = 0;
        ResultSet result = null;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() )
                {
                    prefName = result.getString( "prefname" );
                    m_mapRouteCount = result.getRow();
                }
                else
                {
                    return false;
                }

                // クラスの配列を用意し、初期化する。
                this.m_mapRoute = new DataMapRoute_M2[this.m_mapRouteCount];
                for( i = 0 ; i < m_mapRouteCount ; i++ )
                {
                    m_mapRoute[i] = new DataMapRoute_M2();
                }

                result.beforeFirst();
                while( result.next() )
                {
                    // 路線データ情報の設定
                    this.m_mapRoute[count].setRouteId( result
                            .getString( "route_id" ) );
                    this.m_mapRoute[count].setName( result.getString( "name" ) );
                    count++;
                }
            }
            else
            {
                return false;
            }
        }
        catch ( Exception exception )
        {
            Logging.info( "[SearchHotelStation_M2.getRailwayRouteListSub()] Exception=" + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(true);
    }

    /**
     * 駅の一覧を取得する（路線IDから）
     * 
     * @param routeId 路線ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getRailwayStationList(String routeId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_point.option_4, hh_map_point.name, hh_map_point.jis_code FROM hh_map_point "
                + "WHERE option_6 = ? " + "ORDER BY option_5";

        ret = false;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );

            if ( routeId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, routeId );
                ret = getRailwayStationListSub( prestate, false );
            }

        }
        catch ( Exception exception )
        {
            Logging.info( "[SearchHotelStation_M2.getRailwayStationList()] Exception for RouteId("
                    + routeId + ") - " + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 駅の一覧を取得する（路線ID,都道府県IDから）
     * 
     * @param routeId 路線ID
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getRailwayStationList(String routeId, int prefId)
            throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_point.* FROM hh_map_point, hh_map_route_pref "
                + "WHERE hh_map_point.option_6 = ? "
                + "AND hh_map_route_pref.pref_id = ? "
                + "AND hh_map_point.option_1=hh_map_route_pref.pref_name "
                + "GROUP BY hh_map_point.id "
                + "ORDER BY hh_map_point.option_5";

        ret = false;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );

            if ( routeId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, routeId );
                prestate.setInt( 2, prefId );
                ret = getRailwayStationListSub( prestate, true );
            }
        }
        catch ( Exception exception )
        {
            Logging.info( "[SearchHotelStation_M2.getRailwayStationList()] Exception for RouteId("
                    + routeId + ") prefId(" + routeId + ") - " + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 駅の一覧を取得する（GPS位置情報から）
     * 
     * @param routeId 路線ID
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getRailwayStationListByGps(String lat, String lon, int kind) throws Exception
    {
        final int LATLON_RANGE = 100000;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ConvertGeodesic cg;
        int latLeft;
        int lonLeft;
        int latRight;
        int lonRight;

        cg = new ConvertGeodesic();
        // 世界測地系から日本測地系へ変換
        cg.convertDms2Degree( lat, lon );

        if ( kind == 0 )
        {
            // 左端(lat-10000,lon-10000)
            latLeft = cg.getLatTOKYONum() - LATLON_RANGE;
            lonLeft = cg.getLonTOKYONum() - LATLON_RANGE;
            latRight = cg.getLatTOKYONum() + LATLON_RANGE;
            lonRight = cg.getLonTOKYONum() + LATLON_RANGE;
        }
        else if ( kind == 1 )
        {
            latLeft = cg.getLatTOKYONum() - (LATLON_RANGE * 2);
            lonLeft = cg.getLonTOKYONum() - (LATLON_RANGE * 2);
            latRight = cg.getLatTOKYONum() + (LATLON_RANGE * 2);
            lonRight = cg.getLonTOKYONum() + (LATLON_RANGE * 2);
        }
        else
        {
            latLeft = cg.getLatTOKYONum() - (LATLON_RANGE / 2);
            lonLeft = cg.getLonTOKYONum() - (LATLON_RANGE / 2);
            latRight = cg.getLatTOKYONum() + (LATLON_RANGE / 2);
            lonRight = cg.getLonTOKYONum() + (LATLON_RANGE / 2);
        }

        query = "SELECT hh_map_point.* FROM hh_map_point"
                + " WHERE class_code like  '5__@' "
                + " AND (lat > ? AND lat < ?)"
                + " AND (lon > ? AND lon < ?)"
                + " AND option_6 <> ''"
                // + " GROUP BY option_4 " // 駅でグループ化
                + " ORDER BY option_6, id";

        ret = false;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, latLeft );
            prestate.setInt( 2, latRight );
            prestate.setInt( 3, lonLeft );
            prestate.setInt( 4, lonRight );
            ret = getRailwayStationListSub( prestate, true );

        }
        catch ( Exception exception )
        {
            Logging.info( "[SearchHotelStation_M2.getRailwayStationListByGps()] Exception " + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 駅の一覧データをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    private boolean getRailwayStationListSub(PreparedStatement prestate,
            boolean mobileValue) throws Exception
    {
        int recordCount;
        ResultSet result = null;
        String subQuery;
        Connection subConnection = null;
        PreparedStatement subPrestate = null;
        String[] hotelIdList = null;
        int[] countList = null;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {

                // レコード件数取得
                if ( result.last() )
                {
                    m_mapPointCount = result.getRow();
                }
                else
                {
                    return false;
                }

                // クラスの配列を用意し、初期化する。
                this.m_mapPointHotelCount = new int[this.m_mapPointCount];
                this.m_mapPoint = new DataMapPoint_M2[this.m_mapPointCount];

                for( int i = 0 ; i < m_mapPointCount ; i++ )
                {
                    m_mapPoint[i] = new DataMapPoint_M2();
                }

                result.beforeFirst();
                recordCount = 0;
                while( result.next() )
                {
                    // 駅データ情報の設定
                    if ( mobileValue )
                    {
                        this.m_mapPoint[recordCount].setOption3( result
                                .getString( "option_3" ) );
                    }

                    this.m_mapPoint[recordCount].setOption4( result
                            .getString( "option_4" ) );
                    this.m_mapPoint[recordCount].setName( result
                            .getString( "name" ) );
                    this.m_mapPoint[recordCount].setJisCode( result
                            .getInt( "jis_code" ) );
                    recordCount++;
                }

                if ( m_mapPointCount > 0 )
                {
                    subQuery = "SELECT hh_hotel_map.map_id,count(hh_hotel_basic.id)as count_id FROM hh_hotel_basic,hh_hotel_map "
                            + "WHERE hh_hotel_basic.id <> 0 "
                            + "AND hh_hotel_map.map_id in (";
                    for( int len = 0 ; len < m_mapPointCount ; len++ )
                    {
                        if ( len == 0 )
                            subQuery = subQuery + "?";
                        else
                            subQuery = subQuery + ", " + "?";
                    }
                    subQuery = subQuery
                            + " ) AND hh_hotel_basic.id = hh_hotel_map.id "
                            + "AND hh_hotel_basic.kind <= 7 "
                            + "AND hh_hotel_basic.pref_id <> 0 "
                            + "AND hh_hotel_map.disp_flag = 1 "
                            + " group by hh_hotel_map.map_id ";

                    subConnection = DBConnection.getConnectionRO();
                    subPrestate = subConnection.prepareStatement( subQuery );
                    for( int len = 0 ; len < m_mapPointCount ; len++ )
                    {
                        subPrestate.setString( (len + 1), m_mapPoint[len]
                                .getOption4() );
                    }

                    result = subPrestate.executeQuery();
                    if ( result != null )
                    {

                        if ( result.last() != false )
                        {
                            hotelIdList = new String[result.getRow()];
                            countList = new int[result.getRow()];
                        }

                        result.beforeFirst();
                        recordCount = 0;
                        while( result.next() != false )
                        {
                            hotelIdList[recordCount] = result.getString( "map_id" );
                            countList[recordCount] = Integer.parseInt( result
                                    .getString( "count_id" ) );
                            recordCount++;
                        }

                        if ( (hotelIdList != null) && (hotelIdList.length != 0) )
                        {
                            for( int i = 0 ; i < m_mapPointCount ; i++ )
                            {
                                for( int j = 0 ; j < hotelIdList.length ; j++ )
                                {
                                    if ( m_mapPoint[i].getOption4().equals(
                                            hotelIdList[j] ) )
                                    {
                                        m_mapPointHotelCount[i] = countList[j];
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                return false;
            }
        }
        catch ( Exception exception )
        {
            Logging
                    .info( "[SearchHotelStation_M2.getRailwayStationListSub()] Exception="
                            + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, subPrestate, subConnection );
        }
        return(true);
    }

    /**
     * 駅の一覧を取得する（駅名から）
     * 
     * @param stationName 駅名
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getRailwayStationListByName(String stationName) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        // 入力値を変換する

        query = "SELECT * FROM hh_map_point"
                + " WHERE ("
                + " class_code='521@'"
                + " OR class_code='522@'"
                + " OR class_code='523@'"
                + " )"
                + " AND ("
                + " name LIKE ? OR name_kana LIKE ?"
                + " )"
                + " GROUP BY name"
                + " ORDER BY id";
        ret = false;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            if ( stationName.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, "%" + stationName + "%" );
                prestate.setString( 2, "%" + stationName + "%" );
                ret = getRailwayStationListSub( prestate, false );
            }

        }
        catch ( Exception exception )
        {
            Logging.info( "[SearchHotelStation_M2.getRailwayStationListByName()] Exception for Station Name["
                    + stationName + "] - " + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 地図路線マスタデータ取得
     * 
     * @param routeId 路線ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public String getDataMapRouteName(String routeId) throws Exception
    {
        String name = "";
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT name FROM hh_map_route WHERE route_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, routeId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    name = result.getString( "name" );
                }
            }
        }
        catch ( Exception exception )
        {
            Logging
                    .error( "[SearchHotelStation_M2.getDataMapRouteName()] Exception for RouteId["
                            + routeId + "] - " + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return name;
    }

    /**
     * 地図ポイントマスタデータ取得
     * 
     * @param id id(option_4から取得)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getDataMapPoint(String id) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataMapPoint_M2 dataMapPointDTO = null;
        query = "SELECT * FROM hh_map_point WHERE option_4 = ?";

        try
        {
            dataMapPointDTO = new DataMapPoint_M2();
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    dataMapPointDTO.setJisCode( result.getInt( "jis_code" ) );
                    dataMapPointDTO.setName( result.getString( "name" ) );
                    dataMapPointDTO.setOption6( result.getString( "option_6" ) );
                }
                m_stationInfo = dataMapPointDTO;
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[SearchHotelStation_M2.getDataMapPoint] Exception for ID["
                    + id + "]" + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 市区町村データ取得
     * 
     * @param jisCode 市区町村コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getMasterCityData(int jisCode) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT pref_id FROM hh_master_city WHERE jis_code = ?";

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, jisCode );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.prefId = result.getInt( "pref_id" );
                }
            }
        }
        catch ( Exception exception )
        {
            Logging
                    .error( "[SearchHotelStation_M2.getMasterCityData] Exception for jisCode["
                            + jisCode + "] - " + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * すべてのhotelIDリストを取得します
     * 
     * @param routeId
     * @return
     * @throws Exception
     */
    public int[] getHotelIdList(String routeId) throws Exception
    {
        int i = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int hotelIds[] = null;

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

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, routeId );
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
                }
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[SearchHotelStation_M2.getHotelIdList] Exception for RouteID["
                    + "] - " + exception.toString() );
            throw exception;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return hotelIds;
    }

}
