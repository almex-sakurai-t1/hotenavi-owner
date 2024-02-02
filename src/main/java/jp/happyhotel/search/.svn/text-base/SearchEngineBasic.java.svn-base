/*
 * @(#)SearchEngineBasic.java 1.00 2007/07/13 Copyright (C) ALMEX Inc. 2007 ホテル検索エンジン基本クラス
 */

package jp.happyhotel.search;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;
import jp.happyhotel.hotel.*;

/**
 * ホテル検索エンジン基本クラス。 ホテル検索以外の検索機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/15
 */
public class SearchEngineBasic implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 7569189936050499391L;

    private DataMasterLocal[] m_masterLocal;
    private int               m_masterLocalCount;
    private int[]             m_masterLocalHotelCount;
    private DataMasterPref[]  m_masterPref;
    private int               m_masterPrefCount;
    private int[]             m_masterPrefHotelCount;
    private DataMasterCity[]  m_masterCity;
    private int               m_masterCityCount;
    private int[]             m_masterCityHotelCount;
    private DataMasterArea[]  m_masterArea;
    private int               m_masterAreaCount;
    private int[]             m_masterAreaHotelCount;
    private DataMasterEquip[] m_masterEquip;
    private DataMasterName[]  m_masterEquipClassName;
    private int               m_masterEquipCount;

    private DataMapRoute[]    m_mapRoute;
    private int               m_mapRouteCount;
    private int[]             m_mapRouteHotelCount;
    private DataMapPoint[]    m_mapPoint;
    private int               m_mapPointCount;
    private int[]             m_mapPointHotelCount;

    private int[][]           m_hotelIdList;

    private boolean           hotelCountFlag;

    /**
     * データを初期化します。
     */
    public SearchEngineBasic()
    {
        m_masterLocalCount = 0;
        m_masterPrefCount = 0;
        m_masterCityCount = 0;
        m_masterAreaCount = 0;

        m_mapRouteCount = 0;
        m_mapPointCount = 0;

        m_hotelIdList = new int[0][0];
        hotelCountFlag = true;
    }

    public DataMasterLocal[] getMasterLocal()
    {
        return(m_masterLocal);
    }

    public int getMasterLocalCount()
    {
        return(m_masterLocalCount);
    }

    public int[] getMasterLocalHotelCount()
    {
        return(m_masterLocalHotelCount);
    }

    public DataMasterPref[] getMasterPref()
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

    public DataMasterCity[] getMasterCity()
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

    public DataMasterArea[] getMasterArea()
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

    public DataMasterEquip[] getMasterEquip()
    {
        return(m_masterEquip);
    }

    public int getMasterEquipCount()
    {
        return(m_masterEquipCount);
    }

    public DataMasterName[] getMasterEquipClassName()
    {
        return(m_masterEquipClassName);
    }

    public DataMapRoute[] getMapRoute()
    {
        return(m_mapRoute);
    }

    public int getMapRouteCount()
    {
        return(m_mapRouteCount);
    }

    public int[] getMapRouteHotelCount()
    {
        return(m_mapRouteHotelCount);
    }

    public DataMapPoint[] getMapPoint()
    {
        return(m_mapPoint);
    }

    public int getMapPointCount()
    {
        return(m_mapPointCount);
    }

    public int[] getMapPointHotelCount()
    {
        return(m_mapPointHotelCount);
    }

    public boolean isHotelCountFlag()
    {
        return hotelCountFlag;
    }

    public void setHotelCountFlag(boolean hotelCountFlag)
    {
        this.hotelCountFlag = hotelCountFlag;
    }

    public int[][] getHotelIdList()
    {
        return m_hotelIdList;
    }

    /**
     * 地方の一覧を取得する（IDから）
     * 
     * @param localId 地方ID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:地方ID昇順,1:表示順昇順,2:表示順降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getLocalList(int localId, int sortOrder)
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }

            ret = getLocalListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getLocalList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 地方の一覧を取得する（名称から）
     * 
     * @param localName 検索名称
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getLocalListByName(String localName, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_local";

        if ( localName.compareTo( "" ) != 0 )
        {
            query = query + " WHERE (name LIKE ? OR name_kana LIKE ?)";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( localName.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, "%" + localName + "%" );
                prestate.setString( 2, "%" + localName + "%" );
            }

            ret = getLocalListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getLocalListByName] Exception=" + e.toString() );
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
    private boolean getLocalListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hbi;

        count = 0;
        hbi = new HotelBasicInfo();
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
                        this.m_masterLocalHotelCount[count] = hbi.getHotelCountByLocal( result.getInt( "local_id" ) );
                        this.m_hotelIdList[count] = hbi.getHotelIdList();
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
            Logging.info( "[getLocalListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * 都道府県を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPrefList(int prefId, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_pref";

        if ( prefId > 0 )
        {
            query = query + " WHERE pref_id = ?";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY pref_id ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            ret = getPrefListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrefList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 都道府県を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @param sortQuery ソートクエリー(ORDER BY ??)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPrefList(int prefId, String sortQuery)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_pref";

        if ( prefId > 0 )
        {
            query = query + " WHERE pref_id = ?";
        }

        if ( sortQuery.compareTo( "" ) != 0 )
        {
            query = query + sortQuery;
        }
        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }

            ret = getPrefListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrefList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 都道府県の一覧を取得する（地方IDから）
     * 
     * @param localId 地方ID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPrefListByLocal(int localId, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_pref";

        if ( localId > 0 )
        {
            query = query + " WHERE local_id = ?";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY pref_id ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }
            ret = getPrefListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrefListByLocal] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 都道府県の一覧を取得する（名称から）
     * 
     * @param prefName 検索名称
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPrefListByName(String prefName, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_pref";

        if ( prefName.compareTo( "" ) != 0 )
        {
            query = query + " WHERE (name LIKE ? OR name_kana LIKE ?)";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY pref_id ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefName.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, "%" + prefName + "%" );
                prestate.setString( 2, "%" + prefName + "%" );
            }
            ret = getPrefListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrefListByName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 都道府県の一覧データをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getPrefListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hbi;
        count = 0;
        hbi = new HotelBasicInfo();
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_masterPrefCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_masterPrefHotelCount = new int[this.m_masterPrefCount];
                this.m_hotelIdList = new int[this.m_masterPrefCount][];
                this.m_masterPref = new DataMasterPref[this.m_masterPrefCount];
                for( i = 0 ; i < m_masterPrefCount ; i++ )
                {
                    m_masterPref[i] = new DataMasterPref();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 都道府県データ情報の設定
                    this.m_masterPref[count].setData( result );
                    if ( this.hotelCountFlag != false )
                    {
                        this.m_masterPrefHotelCount[count] = hbi.getHotelCountByPref( result.getInt( "pref_id" ) );
                        this.m_hotelIdList[count] = hbi.getHotelIdList();
                    }
                    else
                    {
                        this.m_masterPrefHotelCount[count] = 0;
                    }

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrefListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * 市区町村を取得する（市区町村IDから）
     * 
     * @param jisCode 市区町村ID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getCityList(int jisCode, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_city";

        if ( jisCode > 0 )
        {
            query = query + " WHERE jis_code = ?";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY jis_code ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( jisCode > 0 )
            {
                prestate.setInt( 1, jisCode );
            }
            ret = getCityListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getCityList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 市区町村の一覧を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getCityListByPref(int prefId, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_city";

        if ( prefId > 0 )
        {
            query = query + " WHERE pref_id = ?";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY jis_code ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            ret = getCityListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getCityListByPref] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 市区町村の一覧を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @param sortQuery ソートクエリー(ORDER BY ???)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getCityListByPref(int prefId, String sortQuery)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_city";

        if ( prefId > 0 )
        {
            query = query + " WHERE pref_id = ?";
        }

        if ( sortQuery.compareTo( "" ) != 0 )
        {
            query = query + " " + sortQuery;
        }
        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }

            ret = getCityListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getCityListByPref] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 市区町村の一覧を取得する（名称から）
     * 
     * @param cityName 検索名称
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getCityListByName(String cityName, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_city";

        if ( cityName.compareTo( "" ) != 0 )
        {
            query = query + " WHERE (name LIKE ? OR name_kana LIKE ?)";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY pref_id ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( cityName.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, "%" + cityName + "%" );
                prestate.setString( 2, "%" + cityName + "%" );
            }
            ret = getCityListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getCityListByName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 市区町村の一覧データをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getCityListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hbi;

        count = 0;
        hbi = new HotelBasicInfo();
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_masterCityCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_masterCityHotelCount = new int[this.m_masterCityCount];
                this.m_hotelIdList = new int[this.m_masterCityCount][];
                this.m_masterCity = new DataMasterCity[this.m_masterCityCount];
                for( i = 0 ; i < m_masterCityCount ; i++ )
                {
                    m_masterCity[i] = new DataMasterCity();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 市区町村データ情報の設定
                    this.m_masterCity[count].setData( result );
                    if ( this.hotelCountFlag != false )
                    {
                        this.m_masterCityHotelCount[count] = hbi.getHotelCountByCity( result.getInt( "jis_code" ) );
                        this.m_hotelIdList[count] = hbi.getHotelIdList();
                    }
                    else
                    {
                        this.m_masterCityHotelCount[count] = 0;
                    }

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getCityListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * エリアを取得する（エリアIDから）
     * 
     * @param areaId エリアID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAreaList(int areaId, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_area";

        if ( areaId > 0 )
        {
            query = query + " WHERE area_id = ?";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY area_id ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( areaId > 0 )
            {
                prestate.setInt( 1, areaId );
            }

            ret = getAreaListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getAreaList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * エリアを取得する（エリアIDから）
     * 
     * @param areaId エリアID(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAreaListByLocal(int areaId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_master_area.* FROM hh_master_area,hh_master_pref WHERE ";

        if ( areaId > 0 )
        {
            query = query + " area_id = ?";
        }
        else
        {
            query = query + " area_id <> ?";
        }

        query = query + " AND hh_master_pref.pref_id=hh_master_area.pref_id";
        query = query + " ORDER BY hh_master_pref.local_id,hh_master_pref.pref_id,hh_master_area.disp_index";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, areaId );
            ret = getAreaListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getAreaList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * エリアの一覧を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAreaListByPref(int prefId, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_area";

        if ( prefId > 0 )
        {
            query = query + " WHERE pref_id = ?";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY area_id ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }

            ret = getAreaListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getAreaListByPref] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * エリアの一覧を取得する（市区町村IDから）
     * 
     * @param jisCode 市区町村ID(0:全件)
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAreaListByCity(int jisCode, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_area";

        if ( jisCode > 0 )
        {
            query = query + " WHERE jis_code = ?";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY area_id ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( jisCode > 0 )
            {
                prestate.setInt( 1, jisCode );
            }

            ret = getAreaListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getAreaListByCity] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * エリアの一覧を取得する（名称から）
     * 
     * @param areaName 検索名称
     * @param sortOrder disp_index ソートオーダー(0:昇順,1:降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAreaListByName(String areaName, int sortOrder)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_area";

        if ( areaName.compareTo( "" ) != 0 )
        {
            query = query + " WHERE (name LIKE ? OR name_kana LIKE ?)";
        }

        switch( sortOrder )
        {
            case 0:
                query = query + " ORDER BY area_id ASC";
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( areaName.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, "%" + areaName + "%" );
                prestate.setString( 2, "%" + areaName + "%" );
            }

            ret = getAreaListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getAreaListByName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * エリアの一覧データをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getAreaListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hbi;

        count = 0;
        hbi = new HotelBasicInfo();

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_masterAreaCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_masterAreaHotelCount = new int[this.m_masterAreaCount];
                this.m_hotelIdList = new int[this.m_masterAreaCount][];
                this.m_masterArea = new DataMasterArea[this.m_masterAreaCount];
                for( i = 0 ; i < m_masterAreaCount ; i++ )
                {
                    m_masterArea[i] = new DataMasterArea();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 市区町村データ情報の設定
                    this.m_masterArea[count].setData( result );
                    if ( this.hotelCountFlag != false )
                    {
                        this.m_masterAreaHotelCount[count] = hbi.getHotelCountByArea( result.getInt( "area_id" ) );
                        this.m_hotelIdList[count] = hbi.getHotelIdList();
                    }
                    else
                    {
                        this.m_masterAreaHotelCount[count] = 0;
                    }

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getAreaListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(true);
    }

    /**
     * 路線の一覧を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRailwayRouteList(int prefId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_map_route_pref,hh_map_route";
        query = query + " WHERE hh_map_route_pref.pref_id = ?";
        query = query + " AND hh_map_route_pref.route_id=hh_map_route.route_id";
        query = query + " AND (";
        query = query + " hh_map_route.class_code='511@'";
        query = query + " OR hh_map_route.class_code='512@'";
        query = query + " OR hh_map_route.class_code='513@'";
        query = query + " OR hh_map_route.class_code='514@'";
        query = query + " OR hh_map_route.class_code='516@'";
        query = query + " OR hh_map_route.class_code='517@'";
        query = query + " )";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            ret = getRailwayRouteListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRailwayRouteList] Exception=" + e.toString() );
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
     */
    private boolean getRailwayRouteListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hbi;

        count = 0;
        hbi = new HotelBasicInfo();
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
                this.m_mapRoute = new DataMapRoute[this.m_mapRouteCount];
                for( i = 0 ; i < m_mapRouteCount ; i++ )
                {
                    m_mapRoute[i] = new DataMapRoute();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 路線データ情報の設定
                    this.m_mapRoute[count].setData( result );
                    if ( this.hotelCountFlag != false )
                    {
                        this.m_mapRouteHotelCount[count] = hbi.getHotelCountByMapRoute( result.getString( "route_id" ) );
                        this.m_hotelIdList[count] = hbi.getHotelIdList();
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
            Logging.info( "[getRailwayRouteListSub] Exception=" + e.toString() );
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
     */
    public boolean getRailwayStationList(String routeId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_map_point";
        query = query + " WHERE option_6 = ?";
        query = query + " ORDER BY option_5";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( routeId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, routeId );
            }

            ret = getRailwayStationListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRailwayStationList] Exception=" + e.toString() );
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
     */
    public boolean getRailwayStationList(String routeId, int prefId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_point.* FROM hh_map_point, hh_map_route_pref";
        query = query + " WHERE hh_map_point.option_6 = ?";
        query = query + " AND hh_map_route_pref.pref_id = ?";
        query = query + " AND hh_map_point.option_1=hh_map_route_pref.pref_name";
        query = query + " GROUP BY hh_map_point.id";
        query = query + " ORDER BY hh_map_point.option_5";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( routeId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, routeId );
                prestate.setInt( 2, prefId );
            }

            ret = getRailwayStationListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRailwayStationList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 駅の一覧を取得する（駅名から）
     * 
     * @param stationName 駅名
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRailwayStationListByName(String stationName)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        // 入力値を変換する

        query = "SELECT * FROM hh_map_point";
        query = query + " WHERE (";
        query = query + " class_code='521@'";
        query = query + " OR class_code='522@'";
        query = query + " OR class_code='523@'";
        query = query + " )";
        query = query + " AND (";
        query = query + " name LIKE ? OR name_kana LIKE ?";
        query = query + " )";
        query = query + " GROUP BY name";
        query = query + " ORDER BY id";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( stationName.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, "%" + stationName + "%" );
                prestate.setString( 2, "%" + stationName + "%" );
            }

            ret = getRailwayStationListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRailwayStationListByName] Exception=" + e.toString() );
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
     */
    private boolean getRailwayStationListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hbi;

        count = 0;
        hbi = new HotelBasicInfo();

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

                // クラスの配列を用意し、初期化する。
                this.m_mapPointHotelCount = new int[this.m_mapPointCount];
                this.m_hotelIdList = new int[this.m_mapPointCount][];
                this.m_mapPoint = new DataMapPoint[this.m_mapPointCount];
                for( i = 0 ; i < m_mapPointCount ; i++ )
                {
                    m_mapPoint[i] = new DataMapPoint();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 駅データ情報の設定
                    this.m_mapPoint[count].setData( result );
                    if ( this.hotelCountFlag != false )
                    {
                        this.m_mapPointHotelCount[count] = hbi.getHotelCountByMap( result.getString( "option_4" ) );
                        this.m_hotelIdList[count] = hbi.getHotelIdList();
                    }
                    else
                    {
                        this.m_mapPointHotelCount[count] = 0;
                    }

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getRailwayStationListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * 高速道路の一覧を取得する（都道府県IDから）
     * 
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHighwayList(int prefId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_route.* FROM hh_map_route_pref,hh_map_route";
        query = query + " WHERE hh_map_route_pref.pref_id = ?";
        query = query + " AND hh_map_route_pref.route_id=hh_map_route.route_id";
        query = query + " AND (";
        query = query + " hh_map_route.class_code='411@'";
        query = query + " OR hh_map_route.class_code='421@'";
        query = query + " OR hh_map_route.class_code='434@'";
        query = query + " )";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }

            ret = getHighwayListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getHighwayList] Exception=" + e.toString() );
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
    public boolean getHighwayListByLocal(int localId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_route.* FROM hh_map_route_pref,hh_map_route,hh_master_pref";
        query = query + " WHERE hh_master_pref.local_id = ?";
        query = query + " AND hh_map_route_pref.pref_id  = hh_master_pref.pref_id";
        query = query + " AND hh_map_route_pref.route_id=hh_map_route.route_id";
        query = query + " AND (";
        query = query + " hh_map_route.class_code='411@'";
        query = query + " OR hh_map_route.class_code='421@'";
        query = query + " OR hh_map_route.class_code='434@'";
        query = query + " )";
        query = query + " GROUP BY hh_map_route.route_id";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }

            ret = getHighwayListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getHighwayList] Exception=" + e.toString() );
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
    private boolean getHighwayListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hbi;

        count = 0;
        hbi = new HotelBasicInfo();

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
                this.m_mapRoute = new DataMapRoute[this.m_mapRouteCount];
                for( i = 0 ; i < m_mapRouteCount ; i++ )
                {
                    m_mapRoute[i] = new DataMapRoute();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ICデータ情報の設定
                    this.m_mapRoute[count].setData( result );
                    if ( this.hotelCountFlag != false )
                    {
                        this.m_mapRouteHotelCount[count] = hbi.getHotelCountByMapRoute( result.getString( "route_id" ) );
                        this.m_hotelIdList[count] = hbi.getHotelIdList();
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
            Logging.info( "[getHighwayListSub] Exception=" + e.toString() );
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
    public boolean getIcList(String routeId)
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
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( routeId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, routeId );
            }

            ret = getIcListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getIcList] Exception=" + e.toString() );
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
    public boolean getIcList(String routeId, int prefId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_point.* FROM hh_map_point, hh_map_route_pref, hh_map_sortdata";
        query = query + " WHERE hh_map_point.option_6 = ?";
        query = query + " AND hh_map_point.option_5 <= 3";
        query = query + " AND hh_map_point.class_code ='416@'";
        query = query + " AND hh_map_route_pref.pref_id = ?";
        query = query + " AND hh_map_point.option_1=hh_map_route_pref.pref_name";
        query = query + " AND hh_map_point.id = hh_map_sortdata.id";
        query = query + " GROUP BY hh_map_point.option_4";
        query = query + " ORDER BY hh_map_sortdata.sort_key";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( routeId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, routeId );
                prestate.setInt( 2, prefId );
            }

            ret = getIcListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getIcList] Exception=" + e.toString() );
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
    public boolean getIcListByName(String icName)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_map_point.* FROM hh_map_point, hh_map_sortdata";
        query = query + " WHERE (";
        query = query + " hh_map_point.class_code='416@'";
        query = query + " )";
        query = query + " AND (";
        query = query + " hh_map_point.name LIKE ? OR hh_map_point.name_kana LIKE ?";
        query = query + " )";
        query = query + " AND hh_map_point.id = hh_map_sortdata.id";
        query = query + " GROUP BY hh_map_point.option_4";
        query = query + " ORDER BY hh_map_sortdata.sort_key";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( icName.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, "%" + icName + "%" );
                prestate.setString( 2, "%" + icName + "%" );
            }

            ret = getIcListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getIcListByName] Exception=" + e.toString() );
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
    private boolean getIcListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;
        HotelBasicInfo hbi;

        count = 0;
        hbi = new HotelBasicInfo();

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

                // クラスの配列を用意し、初期化する。
                this.m_mapPointHotelCount = new int[this.m_mapPointCount];
                this.m_hotelIdList = new int[this.m_mapPointCount][];
                this.m_mapPoint = new DataMapPoint[this.m_mapPointCount];
                for( i = 0 ; i < m_mapPointCount ; i++ )
                {
                    m_mapPoint[i] = new DataMapPoint();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ICデータ情報の設定
                    this.m_mapPoint[count].setData( result );
                    if ( this.hotelCountFlag != false )
                    {
                        this.m_mapPointHotelCount[count] = hbi.getHotelCountByMap( result.getString( "option_4" ) );
                        this.m_hotelIdList[count] = hbi.getHotelIdList();
                    }
                    else
                    {
                        this.m_mapPointHotelCount[count] = 0;
                    }

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getIcListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(true);
    }

    /**
     * 設備の一覧を取得する
     * 
     * @param mobileFlag 携帯フラグ(true:携帯)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getEquipList(boolean mobileFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_equip";
        query = query + " WHERE equip_id > 0";
        if ( mobileFlag != false )
        {
            query = query + " AND (input_flag4 = 1 OR input_flag4 = 3)";
        }
        query = query + " AND input_flag4 <> 4";
        query = query + " ORDER BY sort_display";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            ret = getEquipListSub( prestate );
            ret = getEquipClassNameListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getEquipList] Exception=" + e.toString() );
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
    public boolean getEquipClassGroup(boolean mobileFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_equip";
        query = query + " WHERE input_flag6 > 0";
        if ( mobileFlag != false )
        {
            query = query + " AND (input_flag4 = 1 OR input_flag4 = 3)";
        }
        query = query + " AND input_flag4 <> 4";
        query = query + " ORDER BY input_flag6";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            ret = getEquipListSub( prestate );
            ret = getEquipClassNameListSub( prestate );

            prestate.close();
        }
        catch ( Exception e )
        {
            Logging.info( "[getEquipClassGroup] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 設備の一覧データをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getEquipListSub(PreparedStatement prestate)
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
                this.m_masterEquip = new DataMasterEquip[this.m_masterEquipCount];
                for( i = 0 ; i < m_masterEquipCount ; i++ )
                {
                    m_masterEquip[i] = new DataMasterEquip();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 設備データ情報の設定
                    this.m_masterEquip[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getEquipListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(true);
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
}
