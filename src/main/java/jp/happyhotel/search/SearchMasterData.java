package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMapRoute;
import jp.happyhotel.data.DataMasterArea;
import jp.happyhotel.data.DataMasterCity;
import jp.happyhotel.data.DataMasterLocal;
import jp.happyhotel.data.DataMasterPref;

/**
 * マスターデータ取得クラス
 * 
 * @author S.Tashiro
 */
public class SearchMasterData implements Serializable
{
    /**
     *
     */
    private static final long                    serialVersionUID = 7569189936050499391L;
    private final boolean                        DISP_PC          = false;
    private final int                            DISP_COUNT       = 2;

    private ArrayList<DataMasterLocal>           masterLocal;
    private int                                  masterLocalCount;
    private ArrayList<DataMasterPref>            masterPref;
    private int                                  masterPrefCount;
    private ArrayList<DataMasterCity>            masterCity;
    private int                                  masterCityCount;
    private ArrayList<ArrayList<DataMasterArea>> masterArea;
    private int                                  masterAreaCount;

    private ArrayList<Integer>                   stPref;
    private ArrayList<ArrayList<DataMapRoute>>   stRoute;
    private int                                  stRouteCount;
    private ArrayList<DataMapPoint>              st;
    private int                                  stCount;

    private ArrayList<Integer>                   icPref;
    private ArrayList<ArrayList<DataMapRoute>>   icRoute;
    private int                                  icRouteCount;
    private ArrayList<DataMapPoint>              ic;
    private int                                  icCount;

    private ArrayList<Integer>                   ic2Local;
    private ArrayList<ArrayList<DataMapRoute>>   ic2Route;
    private int                                  ic2RouteCount;
    private ArrayList<DataMapPoint>              ic2;
    private int                                  ic2Count;
    private ArrayList<Integer>                   ic2HotelCount;

    /** 都道府県ID **/
    private int                                  prefId;
    private String                               name;
    private int                                  dataMasterPrefId;
    private String                               dataMasterPrefName;
    private int                                  dataMasterAreaprefId;
    private String                               dataMasterAreaName;

    /* ▼IC検索仕様変更用▼ */
    public ArrayList<Integer> getIc2Local()
    {
        return ic2Local;
    }

    public void setIc2Local(ArrayList<Integer> ic2Local)
    {
        this.ic2Local = ic2Local;
    }

    public ArrayList<ArrayList<DataMapRoute>> getIc2Route()
    {
        return ic2Route;
    }

    public void setIc2Route(ArrayList<ArrayList<DataMapRoute>> ic2Route)
    {
        this.ic2Route = ic2Route;
    }

    public int getIc2RouteCount()
    {
        return ic2RouteCount;
    }

    public void setIc2RouteCount(int ic2RouteCount)
    {
        this.ic2RouteCount = ic2RouteCount;
    }

    public ArrayList<DataMapPoint> getIc2()
    {
        return ic2;
    }

    public void setIc2(ArrayList<DataMapPoint> ic2)
    {
        this.ic2 = ic2;
    }

    public int getIc2Count()
    {
        return ic2Count;
    }

    public void setIc2Count(int ic2Count)
    {
        this.ic2Count = ic2Count;
    }

    public ArrayList<Integer> getIc2HotelCount()
    {
        return this.ic2HotelCount;
    }

    public void setIc2HotelCount(ArrayList<Integer> ic2HotelCount)
    {
        this.ic2HotelCount = ic2HotelCount;
    }

    /* ▲IC検索仕様変更用▲ */

    public ArrayList<DataMasterLocal> getMasterLocal()
    {
        return masterLocal;
    }

    public int getMasterLocalCount()
    {
        return masterLocalCount;
    }

    public ArrayList<DataMasterPref> getMasterPref()
    {
        return masterPref;
    }

    public int getMasterPrefCount()
    {
        return masterPrefCount;
    }

    public ArrayList<DataMasterCity> getMasterCity()
    {
        return masterCity;
    }

    public int getMasterCityCount()
    {
        return masterCityCount;
    }

    public ArrayList<ArrayList<DataMasterArea>> getMasterArea()
    {
        return masterArea;
    }

    public int getMasterAreaCount()
    {
        return masterAreaCount;
    }

    public ArrayList<Integer> getStPrefList()
    {
        return stPref;
    }

    public ArrayList<ArrayList<DataMapRoute>> getStRoute()
    {
        return stRoute;
    }

    public int getStRouteCount()
    {
        return stRouteCount;
    }

    public ArrayList<DataMapPoint> getSt()
    {
        return st;
    }

    public int getStCount()
    {
        return stCount;
    }

    public ArrayList<Integer> getIcPrefList()
    {
        return icPref;
    }

    public ArrayList<ArrayList<DataMapRoute>> getIcRoute()
    {
        return icRoute;
    }

    public int getIcRouteCount()
    {
        return icRouteCount;
    }

    public ArrayList<DataMapPoint> getIc()
    {
        return ic;
    }

    public int getIcCount()
    {
        return icCount;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public String getName()
    {
        return name;
    }

    public int getDataMasterPrefId()
    {
        return dataMasterPrefId;
    }

    public String getDataMasterPrefName()
    {
        return dataMasterPrefName;
    }

    public int getDataMasterAreaprefId()
    {
        return dataMasterAreaprefId;
    }

    public String getDataMasterAreaName()
    {
        return dataMasterAreaName;
    }

    public void setMasterLocal(ArrayList<DataMasterLocal> masterLocal)
    {
        this.masterLocal = masterLocal;
    }

    public void setMasterLocalCount(int masterLocalCount)
    {
        this.masterLocalCount = masterLocalCount;
    }

    public void setMasterPref(ArrayList<DataMasterPref> masterPref)
    {
        this.masterPref = masterPref;
    }

    public void setMasterPrefCount(int masterPrefCount)
    {
        this.masterPrefCount = masterPrefCount;
    }

    public void setMasterCity(ArrayList<DataMasterCity> masterCity)
    {
        this.masterCity = masterCity;
    }

    public void setMasterCityCount(int masterCityCount)
    {
        this.masterCityCount = masterCityCount;
    }

    public void setMasterArea(ArrayList<ArrayList<DataMasterArea>> masterArea)
    {
        this.masterArea = masterArea;
    }

    public void setMasterAreaCount(int masterAreaCount)
    {
        this.masterAreaCount = masterAreaCount;
    }

    public void setStRoute(ArrayList<ArrayList<DataMapRoute>> stRoute)
    {
        this.stRoute = stRoute;
    }

    public void setStRouteCount(int stRouteCount)
    {
        this.stRouteCount = stRouteCount;
    }

    public void setSt(ArrayList<DataMapPoint> st)
    {
        this.st = st;
    }

    public void setStCount(int stCount)
    {
        this.stCount = stCount;
    }

    public void setIcRoute(ArrayList<ArrayList<DataMapRoute>> icRoute)
    {
        this.icRoute = icRoute;
    }

    public void setIcRouteCount(int icRouteCount)
    {
        this.icRouteCount = icRouteCount;
    }

    public void setIc(ArrayList<DataMapPoint> ic)
    {
        this.ic = ic;
    }

    public void setIcCount(int icCount)
    {
        this.icCount = icCount;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDataMasterPrefId(int dataMasterPrefId)
    {
        this.dataMasterPrefId = dataMasterPrefId;
    }

    public void setDataMasterPrefName(String dataMasterPrefName)
    {
        this.dataMasterPrefName = dataMasterPrefName;
    }

    public void setDataMasterAreaprefId(int dataMasterAreaprefId)
    {
        this.dataMasterAreaprefId = dataMasterAreaprefId;
    }

    public void setDataMasterAreaName(String dataMasterAreaName)
    {
        this.dataMasterAreaName = dataMasterAreaName;
    }

    /**
     * 地方マスタの取得
     * 
     * @param localId 地方ID(0：全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterLocal(int localId)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;
        DataMasterLocal dml;
        ArrayList<DataMasterLocal> localList;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_local";
        if ( localId > 0 )
        {
            query += " WHERE local_id = ?";
        }
        query += " ORDER BY local_id";
        try
        {
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterLocalCount = result.getRow();
                }

                dml = new DataMasterLocal();
                localList = new ArrayList<DataMasterLocal>();

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( dml != null )
                    {
                        dml = null;
                        dml = new DataMasterLocal();
                    }

                    dml.setData( result );
                    localList.add( dml );
                }
                masterLocal = localList;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterLocal()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( masterLocalCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * 都道府県マスタの取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterPref(int prefId)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;
        DataMasterPref dmp;
        ArrayList<DataMasterPref> prefList;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_pref";
        if ( prefId > 0 )
        {
            query += " WHERE pref_id = ?";
        }
        query += " ORDER BY pref_id";
        try
        {
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterPrefCount = result.getRow();
                }

                dmp = new DataMasterPref();
                prefList = new ArrayList<DataMasterPref>();

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( dmp != null )
                    {
                        dmp = null;
                        dmp = new DataMasterPref();
                    }

                    dmp.setData( result );
                    prefList.add( dmp );
                }
                masterPref = prefList;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterPref()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( masterPrefCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * 市区町村マスタの取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterCity(int prefId)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;
        DataMasterCity dmc;
        ArrayList<DataMasterCity> cityList;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_city";
        if ( prefId > 0 )
        {
            query += " WHERE pref_id = ?";
        }
        query += " ORDER BY pref_id, jis_code";
        try
        {
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterCityCount = result.getRow();
                }

                dmc = new DataMasterCity();
                cityList = new ArrayList<DataMasterCity>();

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( dmc != null )
                    {
                        dmc = null;
                        dmc = new DataMasterCity();
                    }

                    dmc.setData( result );
                    cityList.add( dmc );
                }
                masterCity = cityList;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterCity()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( masterCityCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * ホテルエリアマスタの取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterHotelArea(int prefId)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;
        DataMasterArea dma;
        ArrayList<DataMasterArea> hotelArea = null;
        ArrayList<ArrayList<DataMasterArea>> hotelAreaList = null;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT HMA.*, count( HB.id ) AS hotel_count";
        query += " FROM hh_master_area HMA, hh_hotel_area HHA, hh_hotel_basic HB";
        query += " WHERE HMA.area_id = HHA.area_id";
        query += " AND HHA.id = HB.id";
        if ( prefId > 0 )
        {
            query += " AND HMA.pref_id =?";
        }
        query += " AND HB.kind <=7";
        query += " GROUP BY HMA.area_id";
        query += " order by HMA.disp_index";

        try
        {
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterAreaCount = result.getRow();
                }

                dma = new DataMasterArea();
                hotelArea = new ArrayList<DataMasterArea>();
                hotelAreaList = new ArrayList<ArrayList<DataMasterArea>>();

                count = 0;
                prefId = 1;
                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( dma != null )
                    {
                        dma = null;
                        dma = new DataMasterArea();
                    }

                    // 都道府県が異なったらセット
                    if ( prefId != result.getInt( "pref_id" ) )
                    {
                        prefId = result.getInt( "pref_id" );
                        hotelAreaList.add( hotelArea );
                        hotelArea = null;
                        hotelArea = new ArrayList<DataMasterArea>();
                    }
                    dma.setData( result );
                    hotelArea.add( dma );
                }
                // 最後に追加されていないArrayListを追加
                if ( hotelArea.size() > 0 )
                {
                    hotelAreaList.add( hotelArea );
                }
                masterArea = hotelAreaList;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterArea()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( masterAreaCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * 路線マスタの取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterStationRoute(int prefId)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;
        DataMapRoute dmrSt;
        ArrayList<DataMapRoute> routeSt;
        ArrayList<ArrayList<DataMapRoute>> routeStList;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_route_pref.pref_id, hh_map_route.* FROM hh_map_route_pref, hh_map_route, hh_master_pref "
                + "WHERE hh_map_route_pref.pref_id = hh_master_pref.pref_id "
                + "AND hh_map_route_pref.route_id=hh_map_route.route_id ";

        if ( prefId > 0 )
        {
            query += "AND hh_map_route_pref.pref_id = ? ";
        }
        query += " AND hh_map_route.class_code IN( '511@', '512@', '513@', '514@', '516@', '517@' )"
                + " ORDER BY hh_map_route_pref.pref_id, hh_map_route.route_id";

        try
        {
            stPref = new ArrayList<Integer>();
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            else
            {
                prefId = 1;
            }
            stPref.add( prefId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    stRouteCount = result.getRow();
                }
                // データの初期化
                dmrSt = new DataMapRoute();
                routeSt = new ArrayList<DataMapRoute>();
                routeStList = new ArrayList<ArrayList<DataMapRoute>>();

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( dmrSt != null )
                    {
                        dmrSt = null;
                        dmrSt = new DataMapRoute();
                    }
                    if ( prefId != result.getInt( "pref_id" ) )
                    {
                        prefId = result.getInt( "pref_id" );
                        stPref.add( prefId );
                        routeStList.add( routeSt );
                        routeSt = null;
                        routeSt = new ArrayList<DataMapRoute>();
                    }
                    dmrSt.setData( result );
                    routeSt.add( dmrSt );
                }
                // 駅ルートのArrayListが残っていたら追加する
                if ( routeSt.size() > 0 )
                {
                    routeStList.add( routeSt );
                }
                stRoute = routeStList;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterStationRoute()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( stRouteCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * 駅マスタの取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterStation(String option6, int prefId)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;
        ArrayList<DataMapPoint> dmpSt = null;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( option6 == null || option6.equals( "" ) != false )
        {
            return(false);
        }

        query = "SELECT * FROM hh_map_point WHERE class_code IN( '521@', '522@', '523@' )";
        if ( option6 != null && option6.equals( "" ) == false )
        {
            query += "AND option_6 = ? ";
        }
        if ( prefId > 0 )
        {
            query += " AND jis_code > ? AND jis_code < ?";
        }
        query += " ORDER BY option_6, option_5";

        try
        {
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            count = 1;
            if ( option6 != null && option6.equals( "" ) == false )
            {
                prestate.setString( count++, option6 );
            }
            if ( prefId > 0 )
            {
                prestate.setInt( count++, prefId * 1000 );
                prestate.setInt( count++, (prefId + 1) * 1000 );
            }

            dmpSt = this.getMasterStationSub( prestate );

            st = dmpSt;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterStation()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( stCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /***
     * 駅情報を取得する
     * 
     * @param prestate ステートメント
     * @return
     * @throws Exception
     */
    public ArrayList<DataMapPoint> getMasterStationSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count = 0;
        boolean ret = false;
        ResultSet result = null;
        DataMapPoint dmpSt = null;
        ArrayList<DataMapPoint> dmpStList = null;

        this.stCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.stCount = result.getRow();
                }

                dmpSt = new DataMapPoint();
                dmpStList = new ArrayList<DataMapPoint>();

                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( dmpSt != null )
                    {
                        dmpSt = null;
                        dmpSt = new DataMapPoint();
                    }
                    dmpSt.setData( result );
                    dmpStList.add( dmpSt );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchMasterData.getMasterStationSub(prestate)] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(dmpStList);
    }

    /**
     * 高速道路マスタの取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterIcRoute(int prefId)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;

        DataMapRoute dmrIc;
        ArrayList<DataMapRoute> routeIc;
        ArrayList<ArrayList<DataMapRoute>> routeIcList;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_route_pref.pref_id, hh_map_route.* FROM hh_map_route_pref,hh_map_route"
                + " WHERE hh_map_route.class_code IN ( '411@', '421@', '434@' ) "
                + " AND hh_map_route_pref.route_id=hh_map_route.route_id";
        if ( prefId > 0 )
        {
            query += " AND hh_map_route_pref.pref_id = ?";
        }
        query += " ORDER BY hh_map_route_pref.pref_id, hh_map_route.route_id";

        try
        {
            icPref = new ArrayList<Integer>();
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            else
            {
                prefId = 1;
            }
            icPref.add( prefId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    icRouteCount = result.getRow();
                }

                dmrIc = new DataMapRoute();
                routeIc = new ArrayList<DataMapRoute>();
                routeIcList = new ArrayList<ArrayList<DataMapRoute>>();

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( dmrIc != null )
                    {
                        dmrIc = null;
                        dmrIc = new DataMapRoute();
                    }
                    if ( prefId != result.getInt( "pref_id" ) )
                    {
                        prefId = result.getInt( "pref_id" );
                        icPref.add( prefId );
                        routeIcList.add( routeIc );
                        routeIc = null;
                        routeIc = new ArrayList<DataMapRoute>();
                    }
                    dmrIc.setData( result );
                    routeIc.add( dmrIc );
                }
                // ICルートのArrayListが残っていたら追加する
                if ( routeIc.size() > 0 )
                {
                    routeIcList.add( routeIc );
                }
                icRoute = routeIcList;

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterIcRoute()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( icRouteCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * ICマスタの取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterIc(String option6, int prefId)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;
        DataMapPoint dmp;
        ArrayList<DataMapPoint> dmpIc = null;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_point.* FROM hh_map_point, hh_map_route_pref, hh_map_sortdata";
        query += " WHERE hh_map_point.option_6 = ?";
        query += " AND hh_map_point.option_5 <= 3";
        query += " AND hh_map_point.class_code ='416@'";
        if ( prefId > 0 )
        {
            query += " AND hh_map_route_pref.pref_id = ?";
        }
        query += " AND hh_map_point.option_1=hh_map_route_pref.pref_name";
        query += " AND hh_map_point.id = hh_map_sortdata.id";
        query += " GROUP BY hh_map_point.option_4";
        query += " ORDER BY hh_map_sortdata.sort_key";

        try
        {
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            count = 1;
            // それぞれデータをセットする際にcountを増やす
            if ( option6 != null && option6.equals( "" ) == false )
            {
                prestate.setString( count++, option6 );
            }
            if ( prefId > 0 )
            {
                prestate.setInt( count++, prefId );
            }
            dmpIc = this.getMasterIcSub( prestate );
            ic = dmpIc;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterIc()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( icCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /***
     * IC情報を取得する
     * 
     * @param prestate ステートメント
     * @return
     * @throws Exception
     */
    public ArrayList<DataMapPoint> getMasterIcSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count = 0;
        boolean ret = false;
        ResultSet result = null;
        DataMapPoint dmpIc = null;
        ArrayList<DataMapPoint> dmpIcList = null;

        this.icCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.icCount = result.getRow();
                }

                dmpIc = new DataMapPoint();
                dmpIcList = new ArrayList<DataMapPoint>();

                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( dmpIc != null )
                    {
                        dmpIc = null;
                        dmpIc = new DataMapPoint();
                    }

                    dmpIc.setData( result );
                    dmpIcList.add( dmpIc );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchMasterData.getMasterIcSub(prestate)] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(dmpIcList);
    }

    /**
     * 高速道路マスタの取得
     * 
     * @param localId 地方ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterIc2Route(int localId)
    {
        int i;
        boolean ret = false;
        SearchEngineBasic_M2 seb;

        DataMapRoute dmrIc;
        ArrayList<DataMapRoute> routeIc;
        ArrayList<ArrayList<DataMapRoute>> routeIcList;

        seb = new SearchEngineBasic_M2();

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            ic2Local = new ArrayList<Integer>();
            Logging.info( "localId=" + localId );

            // 地方IDを取得
            ret = seb.getLocalList( localId, 0 );
            if ( ret != false )
            {
                for( i = 0 ; i < seb.getMasterLocalCount() ; i++ )
                {
                    ic2Local.add( seb.getMasterLocal()[i].getLocalId() );
                }
            }

            dmrIc = new DataMapRoute();
            routeIc = new ArrayList<DataMapRoute>();
            routeIcList = new ArrayList<ArrayList<DataMapRoute>>();

            if ( ic2Local != null )
            {
                // 地方IDに紐付く自動車道を取得していく
                for( i = 0 ; i < ic2Local.size() ; i++ )
                {
                    // 地方IDに紐付く自動車道を取得する
                    if ( seb.getHighwayListByLocal( ic2Local.get( i ) ) != false )
                    {
                        for( int j = 0 ; j < seb.getMapRouteCount() ; j++ )
                        {
                            if ( dmrIc != null )
                            {
                                dmrIc = null;
                                dmrIc = new DataMapRoute();
                            }

                            dmrIc.getData( seb.getMapRoute()[j].getRouteId() );
                            routeIc.add( dmrIc );
                        }
                        routeIcList.add( routeIc );
                        routeIc = null;
                        routeIc = new ArrayList<DataMapRoute>();
                    }
                    ic2Route = routeIcList;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterIc2Route()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( icRouteCount > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * ICマスタの取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public boolean getMasterIc2(String routeId)
    {
        int i;
        int count = 0;
        boolean ret = false;
        SearchEngineBasic_M2 seb;
        DataMapPoint dmpIc = null;
        ArrayList<DataMapPoint> dmpIcList = null;
        ArrayList<Integer> hotelCount = null;

        try
        {
            this.ic2Count = 0;

            seb = new SearchEngineBasic_M2();
            Logging.info( routeId );
            ret = seb.getIcList( routeId );
            if ( ret != false )
            {

                this.ic2Count = seb.getMapPointCount();

                dmpIc = new DataMapPoint();
                dmpIcList = new ArrayList<DataMapPoint>();
                hotelCount = new ArrayList<Integer>();
                for( i = 0 ; i < seb.getMapPointCount() ; i++ )
                {
                    if ( seb.getMapPointHotelCount()[i] != 0 )
                    {
                        if ( dmpIc != null )
                        {
                            dmpIc = null;
                            dmpIc = new DataMapPoint();
                        }
                        dmpIc.setId( seb.getMapPoint()[i].getId() );
                        dmpIc.setClassCode( seb.getMapPoint()[i].getClassCode() );
                        dmpIc.setJisCode( seb.getMapPoint()[i].getJisCode() );
                        dmpIc.setLat( seb.getMapPoint()[i].getLat() );
                        dmpIc.setLon( seb.getMapPoint()[i].getLon() );
                        dmpIc.setMapPrecision( seb.getMapPoint()[i].getMapPrecision() );
                        dmpIc.setName( seb.getMapPoint()[i].getName() );
                        dmpIc.setNameKana( seb.getMapPoint()[i].getNameKana() );
                        dmpIc.setOption1( seb.getMapPoint()[i].getOption1() );
                        dmpIc.setOption2( seb.getMapPoint()[i].getOption2() );
                        dmpIc.setOption3( seb.getMapPoint()[i].getOption3() );
                        dmpIc.setOption4( seb.getMapPoint()[i].getOption4() );
                        dmpIc.setOption5( seb.getMapPoint()[i].getOption5() );
                        dmpIc.setOption6( seb.getMapPoint()[i].getOption6() );
                        dmpIc.setOption7( seb.getMapPoint()[i].getOption7() );
                        dmpIc.setOption8( seb.getMapPoint()[i].getOption8() );
                        dmpIc.setOption9( seb.getMapPoint()[i].getOption9() );
                        hotelCount.add( seb.getMapPoint()[i].getMapPointHotelCount() );
                        dmpIcList.add( dmpIc );
                        count++;
                    }
                }
                this.ic2 = dmpIcList;
                this.ic2HotelCount = hotelCount;
                this.ic2Count = count;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterIc2()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
        }
        return(ret);
    }

    /**
     * 駅マスタ件数の取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public int getMasterStationCount(int prefId)
    {
        int count = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT COUNT(*) FROM hh_map_point WHERE class_code IN( '521@', '522@', '523@' )";
        if ( prefId > 0 )
        {
            query += "AND jis_code >= ?  AND jis_code < ?  ";
        }
        query += "ORDER BY option_6, option_5";

        try
        {
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId * 1000 );
                prestate.setInt( 2, (prefId + 1) * 1000 );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterStationCount()] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * ICマスタ件数の取得
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:成功、FALSE:失敗)
     **/
    public int getMasterIcCount(int prefId)
    {
        int count = 0;
        String query;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT COUNT(*) FROM hh_map_point "
                + " WHERE class_code IN( '416@' )";

        if ( prefId > 0 )
        {
            query += "AND jis_code >= ? "
                    + " AND jis_code < ? ";
        }
        query += " ORDER BY option_6";

        try
        {
            // TODO プライマリDBを見るように変更
            // connection = DBConnection.getReadOnlyConnection();
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                // jis_code / 1000 がpref_idのため、範囲内であれば同じ県とする
                prestate.setInt( 1, prefId * 1000 );
                prestate.setInt( 2, (prefId + 1) * 1000 );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchMasterData.getMasterIcCount()] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

}
