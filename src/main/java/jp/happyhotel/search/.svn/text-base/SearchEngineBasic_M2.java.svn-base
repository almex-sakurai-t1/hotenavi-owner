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
import jp.happyhotel.data.DataMasterCity_M2;
import jp.happyhotel.data.DataMasterHotelArea_M2;
import jp.happyhotel.data.DataMasterLocal;
import jp.happyhotel.data.DataMasterName;
import jp.happyhotel.data.DataMasterPref_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.hotel.HotelBasicInfo;
import jp.happyhotel.hotel.HotelBasicInfo_M2;
import jp.happyhotel.sponsor.SponsorData_M2;

/**
 * 
 * @author HCL Technologies LTD
 * 
 */
public class SearchEngineBasic_M2 implements Serializable
{
    /**
     *
     */
    private static final long          serialVersionUID = 7569189936050499391L;
    private final boolean              DISP_PC          = false;
    private final int                  DISP_COUNT       = 2;

    private DataMasterLocal[]          m_masterLocal;
    private DataSearchMasterEquip_M2[] m_masterEquip;
    private int                        m_masterEquipCount;
    private int[]                      m_masterLocalHotelCount;
    private int                        m_masterLocalCount;
    private int                        m_mapRouteCount;
    private DataMapRoute_M2[]          m_mapRoute;
    private int                        m_mapPointCount;
    private int[]                      m_mapPointHotelCount;
    private int[]                      m_mapRouteHotelCount;
    private DataMapPoint_M2[]          m_mapPoint;
    private boolean                    hotelCountFlag;

    private int[][]                    m_hotelIdList;
    private DataMasterPref_M2          m_masterPref;
    private int                        m_masterPrefCount;
    private int[]                      m_masterPrefHotelCount;
    private DataMasterName[]           m_masterEquipClassName;
    private DataMasterCity_M2[]        m_masterCity;
    private int                        m_masterCityCount;
    private int[]                      m_masterCityHotelCount;
    private DataMasterHotelArea_M2[]   m_masterArea;
    private int                        m_masterAreaCount;
    private int[]                      m_masterAreaHotelCount;
    /** 都道府県ID **/
    private int                        prefId;
    private String                     name;
    private int                        dataMasterprefId;
    private String                     dataMasterPrefName;
    private int                        dataMasterAreaprefId;
    private String                     dataMasterAreaName;

    public int getDataMasterCityPrefId()
    {
        return prefId;
    }

    public String getDataMasterCityName()
    {
        return name;
    }

    public int getDataMasterPrefPrefId()
    {
        return dataMasterprefId;
    }

    public String getDataMasterAreaName()
    {
        return dataMasterAreaName;
    }

    public int getDataMasterAreaPrefId()
    {
        return dataMasterAreaprefId;
    }

    public String getDataMasterPrefName()
    {
        return dataMasterPrefName;
    }

    public DataMasterLocal[] getMasterLocal()
    {
        return(m_masterLocal);
    }

    public DataMasterCity_M2[] getMasterCity()
    {
        return(m_masterCity);
    }

    public int getMasterCityCount()
    {
        return(m_masterCityCount);
    }

    public int[] getMasterCityHotelCount()
    {
        return(m_masterCityHotelCount);
    }

    public DataMasterHotelArea_M2[] getMasterArea()
    {
        return(m_masterArea);
    }

    public int getMasterAreaCount()
    {
        return(m_masterAreaCount);
    }

    public int[] getMasterAreaHotelCount()
    {
        return(m_masterAreaHotelCount);
    }

    public int getMasterLocalCount()
    {
        return(m_masterLocalCount);
    }

    public int[] getMasterLocalHotelCount()
    {
        return(m_masterLocalHotelCount);
    }

    public DataSearchMasterEquip_M2[] getMasterEquip()
    {
        return(m_masterEquip);
    }

    public DataMasterName[] getMasterEquipClassName()
    {
        return(m_masterEquipClassName);
    }

    public int getMasterEquipCount()
    {
        return(m_masterEquipCount);
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

    public int[] getMapRouteHotelCount()
    {
        return(m_mapRouteHotelCount);
    }

    public DataMapPoint_M2[] getMapPoint()
    {
        return(m_mapPoint);
    }

    public DataMasterPref_M2 getMasterPref()
    {
        return(m_masterPref);
    }

    public int getMasterPrefCount()
    {
        return(m_masterPrefCount);
    }

    public int[] getMasterPrefHotelCount()
    {
        return(m_masterPrefHotelCount);
    }

    public int[][] getHotelIdList()
    {
        return m_hotelIdList;
    }

    public SearchEngineBasic_M2()
    {
        m_mapRouteCount = 0;
        m_mapPointCount = 0;
        hotelCountFlag = true;
        m_masterPrefCount = 0;
        m_masterLocalCount = 0;
        m_hotelIdList = new int[0][0];
    }

    /**
     * 設備の一覧を取得する
     * 
     * @param mobileFlag 携帯フラグ(true:携帯)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getEquipList(boolean mobileFlag) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_equip"
                + " WHERE equip_id > 0";
        if ( mobileFlag != false )
        {
            query = query + " AND (input_flag4 = 1 OR input_flag4 = 3)";
        }
        query = query + " AND input_flag4 <> 4"
                + " ORDER BY input_flag6, sort_display";

        ret = false;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            ret = getEquipListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getEquipList( boolean mobileFlag = " + mobileFlag + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 分類別設備の一覧を取得する
     * 
     * @param mobileFlag 携帯フラグ(true:携帯)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getEquipClassGroup(boolean mobileFlag) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_equip"
                + " WHERE input_flag6 > 0";
        if ( mobileFlag != false )
        {
            query = query + " AND (input_flag4 = 1 OR input_flag4 = 3)";
        }
        query = query + " AND input_flag4 <> 4"
                + " ORDER BY input_flag6, sort_display ";

        ret = false;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            ret = getEquipListSub( prestate );
            ret = getEquipClassNameListSub( prestate );

            prestate.close();
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getEquipClassGroup(" + mobileFlag + ")] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    private boolean getEquipListSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count;
        ResultSet result = null;
        boolean ret = false;

        count = 0;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_masterEquipCount = result.getRow();
                }

                if ( m_masterEquipCount < 200 )
                {

                    this.m_masterEquip = new DataSearchMasterEquip_M2[this.m_masterEquipCount];
                    for( i = 0 ; i < m_masterEquipCount ; i++ )
                    {
                        m_masterEquip[i] = new DataSearchMasterEquip_M2();
                    }

                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // 設備データ情報の設定
                        this.m_masterEquip[count++].setData( result );
                    }
                    ret = true;

                }
                else

                    ret = false;

            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getEquipListSub (prestate)] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(ret);

    }

    /**
     * 設備区分名称の一覧データをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getEquipClassNameListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;

        count = 0;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_masterEquipCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_masterEquipClassName = new DataMasterName[this.m_masterEquipCount];
                for( i = 0 ; i < m_masterEquipCount ; i++ )
                {
                    m_masterEquipClassName[i] = new DataMasterName();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // こだわり設備名称データ情報の設定
                    this.m_masterEquipClassName[count++].getData( 1, result.getInt( "input_flag6" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getEquipClassNameListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(true);
    }

    /**
     * This function gets the Precture Information
     * 
     * @param prefId
     * @return boolean
     */

    public boolean getPrefInfo(int prefId) throws Exception
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        if ( prefId > 0 )
        {
            query = "SELECT * FROM hh_master_pref"
                    + " WHERE pref_id = ?";

            try
            {
                connection = DBConnection.getReadOnlyConnection();
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, prefId );
                result = prestate.executeQuery();

                if ( result != null )
                {
                    while( result.next() != false )
                    {

                        this.m_masterPref = new DataMasterPref_M2();
                        this.m_masterPref.setName( result.getString( "name" ) );
                        this.m_masterPref.setPrefId( result.getInt( "pref_id" ) );
                        ret = true;
                    }
                }
            }
            catch ( Exception e )
            {
                ret = false;
                Logging.info( "[SearchEngineBasic_M2.getPrefInfo( int prefId = " + prefId + ")] Exception=" + e.toString() );
                throw e;
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }
        return ret;
    }

    /**
     * 都道府県を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public void getPrefHotelList(int[] prefId) throws Exception
    {
        HotelBasicInfo_M2 hbi = new HotelBasicInfo_M2();
        hbi.getHotelCountByPref( prefId );
        m_hotelIdList = new int[1][];
        this.m_hotelIdList[0] = hbi.getHotelIdList();
    }

    /**
     * ICのスポンサーデータを取得する
     */
    public SponsorData_M2 getSponsorDataForIC(String paramIcId) throws Exception
    {
        boolean ret = false;
        SponsorData_M2 sponserData = null;
        DataMapPoint_M2 dataMapPoint = null;

        sponserData = new SponsorData_M2();

        if ( paramIcId != null )
        {
            // Fetch IC Data
            dataMapPoint = getDataMapPoint( paramIcId );

            // 市区町村コードは合併・編入・消滅・政令指定都市で新設になるなど変更される場合があるため1000で割り、都道府県コードをセット
            ret = sponserData.getSponsorByPref( dataMapPoint.getJisCode() / 1000 );

            dataMapPoint = null;
        }
        sponserData.setSponsorDataStatus( ret );
        return sponserData;
    }

    /**
     * ICのスポンサーデータを取得する
     */
    public SponsorData_M2 getRandomSponsorDataForIC(String paramIcId) throws Exception
    {
        boolean ret = false;
        SponsorData_M2 sponserData = null;
        DataMapPoint_M2 dataMapPoint = null;

        sponserData = new SponsorData_M2();

        if ( paramIcId != null )
        {
            // Fetch IC Data
            dataMapPoint = getDataMapPoint( paramIcId );

            // 市区町村コードは合併・編入・消滅・政令指定都市で新設になるなど変更される場合があるため1000で割り、都道府県コードをセット
            ret = sponserData.getRandomSponsorByPref( dataMapPoint.getJisCode() / 1000, DISP_COUNT, DISP_PC );

            dataMapPoint = null;
        }
        sponserData.setSponsorDataStatus( ret );
        return sponserData;
    }

    public boolean isHotelCountFlag()
    {
        return hotelCountFlag;
    }

    public void setHotelCountFlag(boolean hotelCountFlag)
    {
        this.hotelCountFlag = hotelCountFlag;
    }

    /**
     * 地図ポイントマスタデータ取得
     * 
     * @param paramId(option_4から取得)
     * @return DataMapPoint_M2 object(With Data set for particular Id)
     */
    public DataMapPoint_M2 getDataMapPoint(String paramId) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataMapPoint_M2 dataMapPoint = null;
        query = "SELECT * FROM hh_map_point WHERE option_4 = ?";

        try
        {
            dataMapPoint = new DataMapPoint_M2();
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, paramId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // Sets IC Data
                if ( result.next() != false )
                {
                    dataMapPoint.setId( result.getString( "id" ) );
                    dataMapPoint.setClassCode( result.getString( "class_code" ) );
                    dataMapPoint.setJisCode( result.getInt( "jis_code" ) );
                    dataMapPoint.setLat( result.getInt( "lat" ) );
                    dataMapPoint.setLon( result.getInt( "lon" ) );
                    dataMapPoint.setMapPrecision( result.getString( "map_precision" ) );
                    dataMapPoint.setName( result.getString( "name" ) );
                    dataMapPoint.setNameKana( result.getString( "name_kana" ) );
                    dataMapPoint.setOption1( result.getString( "option_1" ) );
                    dataMapPoint.setOption2( result.getString( "option_2" ) );
                    dataMapPoint.setOption3( result.getString( "option_3" ) );
                    dataMapPoint.setOption4( result.getString( "option_4" ) );
                    dataMapPoint.setOption5( result.getString( "option_5" ) );
                    dataMapPoint.setOption6( result.getString( "option_6" ) );
                    dataMapPoint.setOption7( result.getString( "option_7" ) );
                    dataMapPoint.setOption8( result.getString( "option_8" ) );
                    dataMapPoint.setOption9( result.getString( "option_9" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchEngineBasic_M2.getDataMapPoint( String paramId = " + paramId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(dataMapPoint);
    }

    /**
     * 地方の一覧を取得する（IDから）
     * 
     * @param localId 地方ID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:地方ID昇順,1:表示順昇順,2:表示順降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getLocalList(int localId, int sortOrder) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_local";

        if ( localId > 0 )
        {
            query = query + " WHERE local_id = ?";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY local_id ASC";
                break;
            case 1:
                query = query + " ORDER BY disp_index ASC";
                break;
            case 2:
                query = query + " ORDER BY disp_index DESC";
                break;
        }

        ret = false;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }
            // Gets Local Data
            ret = getLocalListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getLocalList( int localId = " + localId + " , int sortOrder = " + sortOrder + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 地方の一覧データをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getLocalListSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hotelBasicInfo;

        count = 0;
        hotelBasicInfo = new HotelBasicInfo();
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_masterLocalCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_masterLocalHotelCount = new int[this.m_masterLocalCount];
                this.m_hotelIdList = new int[this.m_masterLocalCount][];
                this.m_masterLocal = new DataMasterLocal[this.m_masterLocalCount];
                for( i = 0 ; i < m_masterLocalCount ; i++ )
                {
                    m_masterLocal[i] = new DataMasterLocal();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 地方データ情報の設定
                    this.m_masterLocal[count].setData( result );
                    // ホテル件数の取得]
                    if ( this.hotelCountFlag != false )
                    {
                        this.m_masterLocalHotelCount[count] = hotelBasicInfo.getHotelCountByLocal( result.getInt( "local_id" ) );
                        this.m_hotelIdList[count] = hotelBasicInfo.getHotelIdList();
                    }
                    else
                    {
                        this.m_masterLocalHotelCount[count] = 0;
                    }

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getLocalListSub(prestate)] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * ICの一覧を取得する（路線IDから）
     * 
     * @param routeId 路線ID(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getIcList(String routeId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_point.* FROM hh_map_point,hh_map_sortdata";
        query = query + " WHERE hh_map_point.option_6 = ?";
        query = query + " AND hh_map_point.class_code ='416@'";
        query = query + " AND hh_map_point.option_5 <= 3";
        query = query + " AND hh_map_point.id = hh_map_sortdata.id";
        query = query + " GROUP BY hh_map_point.option_4";
        query = query + " ORDER BY hh_map_sortdata.sort_key";

        ret = false;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            if ( routeId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, routeId );
            }
            // Gets IC Data
            ret = getIcListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getIcList( String routeId = " + routeId + ")] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ICの一覧を取得する（路線ID,都道府県IDから）
     * 
     * @param routeId 路線ID(0:全件)
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getIcListByRouteAndPref(String routeId, int prefId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_point.* FROM hh_map_point, hh_map_route_pref, hh_map_sortdata"
                + " WHERE hh_map_point.option_6 = ?"
                + " AND hh_map_point.option_5 <= 3"
                + " AND hh_map_point.class_code ='416@'"
                + " AND hh_map_route_pref.pref_id = ?"
                + " AND hh_map_point.option_1=hh_map_route_pref.pref_name"
                + " AND hh_map_point.id = hh_map_sortdata.id"
                + " GROUP BY hh_map_point.option_4"
                + " ORDER BY hh_map_sortdata.sort_key";

        ret = false;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            if ( routeId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, routeId );
                prestate.setInt( 2, prefId );
            }
            // Gets IC Data
            ret = getIcListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getIcListByRouteAndPref( String routeId = " + routeId + ", int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ICの一覧を取得する（GPS）
     * 
     * @param lat 緯度
     * @param lon 経度
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getIcListByGps(String lat, String lon, int kind) throws Exception
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
                + " WHERE class_code like  '4__@' "
                + " AND hh_map_point.class_code ='416@'"
                + " AND hh_map_point.option_5 <= 3"
                + " AND (lat > ? AND lat < ?)"
                + " AND (lon > ? AND lon < ?)"
                + " AND ( hh_map_point.name like '%出口%'"
                + " OR hh_map_point.name like '%出入口%'"
                + " OR hh_map_point.name like '%インターチェンジ%' )"
                + " AND option_6 <> ''"
                + " GROUP BY option_4" // ICでグループ化
                + " ORDER BY option_6, id";

        ret = false;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, latLeft );
            prestate.setInt( 2, latRight );
            prestate.setInt( 3, lonLeft );
            prestate.setInt( 4, lonRight );
            // Gets IC Data
            ret = getIcListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getIcListByGps()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ICの一覧を取得する（IC名から）
     * 
     * @param icName IC名
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getIcListByName(String icName) throws Exception
    {

        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_point.* FROM hh_map_point, hh_map_sortdata"
                + " WHERE ("
                + " hh_map_point.class_code='416@'"
                + " )"
                + " AND ("
                + " hh_map_point.name LIKE '%" + icName + "%' OR hh_map_point.name_kana LIKE '%" + icName + "%'"
                + " )"
                + " AND hh_map_point.id = hh_map_sortdata.id"
                + " GROUP BY hh_map_point.option_4"
                + " ORDER BY hh_map_sortdata.sort_key";

        ret = false;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            // Fetching InterChange Data
            ret = getIcListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getIcListByName(icName : " + icName + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ICの一覧データをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getIcListSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count;
        String hotelIcIdList[] = null;
        ResultSet result = null;
        count = 0;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {

                // レコード件数取得
                if ( result.last() != false )
                {
                    m_mapPointCount = result.getRow();
                }

                // Array of IC IDs
                hotelIcIdList = new String[m_mapPointCount];

                // クラスの配列を用意し、初期化する。
                this.m_mapPointHotelCount = new int[this.m_mapPointCount];
                this.m_hotelIdList = new int[this.m_mapPointCount][];
                this.m_mapPoint = new DataMapPoint_M2[this.m_mapPointCount];
                for( i = 0 ; i < m_mapPointCount ; i++ )
                {
                    m_mapPoint[i] = new DataMapPoint_M2();
                }

                result.beforeFirst();

                while( result.next() != false )
                {
                    // Setting IC Data

                    this.m_mapPoint[count].setId( result.getString( "id" ) );
                    this.m_mapPoint[count].setClassCode( result.getString( "class_code" ) );
                    this.m_mapPoint[count].setJisCode( result.getInt( "jis_code" ) );
                    this.m_mapPoint[count].setLat( result.getInt( "lat" ) );
                    this.m_mapPoint[count].setLon( result.getInt( "lon" ) );
                    this.m_mapPoint[count].setMapPrecision( result.getString( "map_precision" ) );
                    this.m_mapPoint[count].setName( result.getString( "name" ) );
                    this.m_mapPoint[count].setNameKana( result.getString( "name_kana" ) );
                    this.m_mapPoint[count].setOption1( result.getString( "option_1" ) );
                    this.m_mapPoint[count].setOption2( result.getString( "option_2" ) );
                    this.m_mapPoint[count].setOption3( result.getString( "option_3" ) );
                    this.m_mapPoint[count].setOption4( result.getString( "option_4" ) );
                    this.m_mapPoint[count].setOption5( result.getString( "option_5" ) );
                    this.m_mapPoint[count].setOption6( result.getString( "option_6" ) );
                    this.m_mapPoint[count].setOption7( result.getString( "option_7" ) );
                    this.m_mapPoint[count].setOption8( result.getString( "option_8" ) );
                    this.m_mapPoint[count].setOption9( result.getString( "option_9" ) );

                    hotelIcIdList[count] = result.getString( "option_4" );

                    count++;
                }

                if ( this.hotelCountFlag != false )
                {
                    // Fetch number of hotels for each ICID
                    this.m_mapPointHotelCount = getHotelCountForIC( hotelIcIdList );
                }
                else
                {
                    this.m_mapPointHotelCount = null;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getIcListSub(prestate)] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(true);
    }

    /**
     * 
     * @param hotelIcIdList ( Array of IC Ids)
     * @return (array of count(of number of hotels for each IC Id))
     * @throws Exception
     */
    private int[] getHotelCountForIC(String hotelIcIdList[]) throws Exception
    {
        String query;
        String hotelIdList[] = null;
        int hotelIcIdListLength = 0;
        if ( hotelIcIdList != null )
        {
            hotelIcIdListLength = hotelIcIdList.length; // Total number of ICIDs
        }
        if ( hotelIcIdListLength > 0 )
        {
            int countList[] = new int[hotelIcIdListLength];
            int countListFinal[] = new int[hotelIcIdListLength]; // contains count for number of hotels in each IC
            int recordCount = 0;
            Connection connection = null;
            PreparedStatement prestate = null;
            ResultSet result = null;
            query = "SELECT hh_hotel_map.map_id,count(hh_hotel_basic.id) AS count FROM hh_hotel_basic,hh_hotel_map WHERE hh_hotel_basic.id <> 0"
                    + " AND hh_hotel_map.map_id in(";
            for( int i = 0 ; i < hotelIcIdListLength ; i++ )
            {
                if ( i == 0 )
                    query = query + "'" + hotelIcIdList[i] + "'";
                else
                    query = query + ", '" + hotelIcIdList[i] + "'";
            }
            query = query + ") AND hh_hotel_basic.id = hh_hotel_map.id"
                    + " AND hh_hotel_basic.kind <= 7"
                    + " AND hh_hotel_map.disp_flag = 1"
                    + " GROUP BY hh_hotel_map.map_id";

            try
            {
                connection = DBConnection.getReadOnlyConnection();
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                if ( result != null )
                {
                    if ( result.last() != false )
                    {
                        hotelIdList = new String[result.getRow()];
                    }

                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        hotelIdList[recordCount] = result.getString( "map_id" );
                        countList[recordCount] = Integer.parseInt( result.getString( "count" ) );
                        recordCount++;
                    }

                }
                if ( (hotelIdList != null) && (hotelIdList.length != 0) )
                {
                    for( int i = 0 ; i < hotelIcIdListLength ; i++ )
                    {
                        for( int j = 0 ; j < hotelIdList.length ; j++ )
                        {
                            if ( hotelIcIdList[i].equals( hotelIdList[j] ) )
                            {
                                countListFinal[i] = countList[j];
                            }
                        }
                    }
                }

            }
            catch ( Exception e )
            {
                Logging.info( "[SearchEngineBasic_M2.getHotelCountForIc(hotelIcIdList : " + hotelIcIdList + " )] Exception=" + e.toString() );
                throw e;
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );

            }
            return countListFinal;
        }
        else
        {
            return null;
        }
    }

    /**
     * 高速道路の一覧を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHighwayList(int prefId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_route.* FROM hh_map_route_pref,hh_map_route"
                + " WHERE hh_map_route_pref.pref_id = ?"
                + " AND hh_map_route_pref.route_id=hh_map_route.route_id"
                + " AND ("
                + " hh_map_route.class_code='411@'"
                + " OR hh_map_route.class_code='421@'"
                + " OR hh_map_route.class_code='434@'"
                + " ) ORDER BY hh_map_route.route_id";

        ret = false;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            // Gets Route Data
            ret = getHighwayListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getHighwayList( int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 高速道路の一覧を取得する（地方IDから）
     * 
     * @param localId 地方ID(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHighwayListByLocal(int localId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_route.* FROM hh_map_route_pref,hh_map_route,hh_master_pref"
                + " WHERE hh_master_pref.local_id = ?"
                + " AND hh_map_route_pref.pref_id  = hh_master_pref.pref_id"
                + " AND hh_map_route_pref.route_id=hh_map_route.route_id"
                + " AND ("
                + " hh_map_route.class_code='411@'"
                + " OR hh_map_route.class_code='421@'"
                + " OR hh_map_route.class_code='434@'"
                + " )"
                + " GROUP BY hh_map_route.route_id";

        ret = false;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }
            // Gets route Data
            ret = getHighwayListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getHighwayListByLocal( int localId = " + localId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 高速道路の一覧データをセット
     * 
     * @param db データベースアクセスクラス
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getHighwayListSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hotelBasicInfo;

        count = 0;
        hotelBasicInfo = new HotelBasicInfo();

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_mapRouteCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_mapRouteHotelCount = new int[this.m_mapRouteCount];
                this.m_hotelIdList = new int[this.m_mapRouteCount][];
                this.m_mapRoute = new DataMapRoute_M2[this.m_mapRouteCount];
                for( i = 0 ; i < m_mapRouteCount ; i++ )
                {
                    m_mapRoute[i] = new DataMapRoute_M2();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ICデータ情報の設定

                    this.m_mapRoute[count].setName( result.getString( "name" ) );
                    this.m_mapRoute[count].setRouteId( result.getString( "route_id" ) );
                    if ( this.hotelCountFlag != false )
                    {
                        this.m_mapRouteHotelCount[count] = hotelBasicInfo.getHotelCountByMapRoute( result.getString( "route_id" ) );
                        this.m_hotelIdList[count] = hotelBasicInfo.getHotelIdList();
                    }
                    else
                    {
                        this.m_mapRouteHotelCount[count] = 0;
                    }

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchEngineBasic_M2.getHighwayListSub(String prestate = )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(true);
    }

    /**
     * 市区町村データ取得
     * 
     * @param jisCode 市区町村コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataMasterCity(int jisCode)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_city WHERE jis_code = ?";
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, jisCode );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.prefId = result.getInt( "pref_id" );
                    this.name = result.getString( "name" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchEngineBasic_M2.getDataMasterCity( int jisCode = " + jisCode + " )] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

}
