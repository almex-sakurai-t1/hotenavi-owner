/*
 * @(#)HotelBasicInfo.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテル情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelEquip;
import jp.happyhotel.data.DataHotelMap;
import jp.happyhotel.data.DataHotelMaster;
import jp.happyhotel.data.DataHotelMessage;
import jp.happyhotel.data.DataHotelPrice;
import jp.happyhotel.data.DataHotelRemarks;
import jp.happyhotel.data.DataHotelStatus;
import jp.happyhotel.data.DataMasterEquip;
import jp.happyhotel.search.SearchEngineBasic;

/**
 * ホテル情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/15
 */
public class HotelBasicInfo implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -4801063650044967621L;

    private int                m_hotelCount;
    private DataHotelMaster    m_hotelMaster;
    private DataHotelBasic     m_hotelInfo;
    private DataHotelMessage   m_hotelMessage;
    private int                m_hotelRemarksCount;
    private DataHotelRemarks[] m_hotelRemarks;
    private int                m_hotelPriceCount;
    private DataHotelPrice[]   m_hotelPrice;
    private int                m_hotelEquipCount;
    private DataHotelEquip[]   m_hotelEquip;
    private DataMasterEquip[]  m_equipName;
    private int                m_hotelMapCount;
    private DataHotelMap[]     m_hotelMap;
    private int                m_hotelStatusCount;
    private DataHotelStatus    m_hotelStatus;
    private int                m_hotelGroupCount;
    private int[]              m_hotelChainGroupid;
    private int[]              m_hotelChainDispflag;
    private String[]           m_hotelChainName;
    private int[]              m_hotelIdList;
    private boolean            m_isHotenavi;

    /**
     * データを初期化します。
     */
    public HotelBasicInfo()
    {
        m_hotelCount = 0;
        m_hotelPriceCount = 0;
        m_hotelEquipCount = 0;
        m_hotelStatusCount = 0;
        m_isHotenavi = false;
    }

    /** ホテル基本情報件数取得 **/
    public int getCount()
    {
        return(m_hotelCount);
    }

    /** ホテル基本情報取得 **/
    public DataHotelBasic getHotelInfo()
    {
        return(m_hotelInfo);
    }

    /** ホテル最新情報取得 **/
    public DataHotelMessage getHotelMessage()
    {
        return(m_hotelMessage);
    }

    /** ホテル料金情報件数取得 **/
    public int getPriceCount()
    {
        return(m_hotelPriceCount);
    }

    /** ホテル料金情報取得 **/
    public DataHotelPrice[] getHotelPrice()
    {
        return(m_hotelPrice);
    }

    /** ホテル備考情報件数取得 **/
    public int getRemarksCount()
    {
        return(m_hotelRemarksCount);
    }

    /** ホテル料金情報取得 **/
    public DataHotelRemarks[] getHotelRemarks()
    {
        return(m_hotelRemarks);
    }

    /** ホテル設備情報件数取得 **/
    public int getEquipCount()
    {
        return(m_hotelEquipCount);
    }

    /** ホテル設備情報取得 **/
    public DataHotelEquip[] getHotelEquip()
    {
        return(m_hotelEquip);
    }

    /** マスタ設備情報取得 **/
    public DataMasterEquip[] getEquipName()
    {
        return(m_equipName);
    }

    /** ホテル地図関連情報件数取得 **/
    public int getMapCount()
    {
        return(m_hotelMapCount);
    }

    /** ホテル地図関連情報取得 **/
    public DataHotelMap[] getHotelMap()
    {
        return(m_hotelMap);
    }

    /** ホテルステータス関連情報取得 **/
    public int getStatusCount()
    {
        return(m_hotelStatusCount);
    }

    /** ホテルステータス関連情報取得 **/
    public DataHotelStatus getHotelStatus()
    {
        return(m_hotelStatus);
    }

    /** ホテル設定情報取得取得 **/
    public DataHotelMaster getHotelMaster()
    {
        return(m_hotelMaster);
    }

    public int getGroupCount()
    {
        return m_hotelGroupCount;
    }

    /** ホテルチェーングループID取得 **/
    public int[] getHotelChainGroupid()
    {
        return(m_hotelChainGroupid);
    }

    /** ホテルチェーン表示フラグ取得 **/
    public int[] getHotelChainDispflag()
    {
        return(m_hotelChainDispflag);
    }

    /** ホテルチェーン名取得 **/
    public String[] getHotelChainName()
    {
        return(m_hotelChainName);
    }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    /** ホテナビ有無 **/
    public boolean getIsHotenavi()
    {
        return(m_isHotenavi);
    }

    /**
     * ホテル情報取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotel(int hotelId)
    {
        boolean retR = true;

        // ホテル設定情報の取得
        getHotelMaster( hotelId );

        // ホテル基本情報の取得
        getHotelBasicInfo( hotelId );

        // ホテルステータス情報取得
        getHotelStatus( hotelId );

        // ホテナビ有無
        retR = getHotenavi( hotelId );

        // ホテル最新情報の取得
        retR = getHotelMessage( hotelId );

        if ( retR )
        {
            // ホテル料金情報の取得
            retR = getHotelPrice( hotelId );
        }
        if ( retR )
        {
            // ホテル設備情報の取得
            retR = getHotelEquipment( hotelId );
        }
        if ( retR )
        {
            // ホテル地図関連情報の取得
            retR = getHotelMap( hotelId );
        }
        if ( retR )
        {
            // グループID、表示フラグ、グループ名の取得
            retR = getHotelChain( hotelId );
        }
        return(retR);
    }

    /**
     * ホテル設定情報の取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelMaster(int hotelId)
    {
        boolean ret = true;

        // ホテル設定情報の取得
        this.m_hotelMaster = new DataHotelMaster();
        ret = this.m_hotelMaster.getData( hotelId );

        return ret;
    }

    /**
     * ホテル基本情報の取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelBasicInfo(int hotelId)
    {
        boolean ret = true;

        // ホテル基本情報の取得
        this.m_hotelInfo = new DataHotelBasic();
        ret = this.m_hotelInfo.getData( hotelId );
        if ( ret )
        {
            m_hotelCount = 1;
        }
        return ret;
    }

    /**
     * ホテルステータス情報の取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelStatus(int hotelId)
    {
        boolean ret = true;

        // ホテルステータス情報の取得
        this.m_hotelStatus = new DataHotelStatus();
        ret = this.m_hotelStatus.getData( hotelId );
        if ( ret )
        {
            m_hotelStatusCount = 1;
        }
        return ret;
    }

    /**
     * ホテル最新情報の取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelMessage(int hotelId)
    {
        boolean ret = true;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        int today_date = Integer.parseInt( DateEdit.getDate( 2 ) );
        int today_time = Integer.parseInt( DateEdit.getTime( 1 ) );

        // ホテル最新情報の取得
        query = "SELECT * FROM hh_hotel_message WHERE id = ?";
        query = query + " AND del_flag = 0";
        query = query + " AND ( start_date < ? OR (start_date = ? AND start_time <= ? ))";
        query = query + " AND ( end_date > ? OR ( end_date = ? AND end_time >= ? ))";
        query = query + " AND disp_message <> ''";
        query = query + " ORDER BY start_date DESC, start_time DESC, last_update DESC, last_uptime DESC,seq DESC";
        query = query + " LIMIT 1";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, today_date );
            prestate.setInt( 3, today_date );
            prestate.setInt( 4, today_time );
            prestate.setInt( 5, today_date );
            prestate.setInt( 6, today_date );
            prestate.setInt( 7, today_time );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // クラスを初期化する。
                m_hotelMessage = new DataHotelMessage();
                if ( result.next() != false )
                {
                    // ホテル最新情報の取得
                    this.m_hotelMessage.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelMessage] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * ホテルグループID、表示フラグ、グループ名の取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelChain(int hotelId)
    {
        boolean ret = true;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        // グループID、表示フラグ、グループ名の取得
        query = "SELECT hh_hotel_chain.group_id,hh_master_chain.disp_flag,hh_master_chain.name FROM hh_master_chain,hh_hotel_chain WHERE hh_hotel_chain.id = ?";
        query = query + " AND (hh_hotel_chain.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_chain.end_date >= " + DateEdit.getDate( 2 ) + ")";
        query = query + " AND hh_hotel_chain.group_id = hh_master_chain.group_id";
        query = query + " AND hh_master_chain.disp_flag <= 1";
        query = query + " ORDER BY hh_hotel_chain.disp_index";

        int count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() )
                {
                    m_hotelGroupCount = result.getRow();
                }
                // 配列の初期化
                this.m_hotelChainGroupid = new int[this.m_hotelGroupCount];
                this.m_hotelChainDispflag = new int[this.m_hotelGroupCount];
                this.m_hotelChainName = new String[this.m_hotelGroupCount];
                for( int i = 0 ; i < m_hotelGroupCount ; i++ )
                {
                    m_hotelChainGroupid[i] = -1;
                    m_hotelChainDispflag[i] = -1;
                    m_hotelChainName[i] = null;
                }

                result.beforeFirst();
                while( result.next() )
                {
                    // グループIDの取得
                    this.m_hotelChainGroupid[count] = result.getInt( "hh_hotel_chain.group_id" );
                    // 表示フラグの取得
                    this.m_hotelChainDispflag[count] = result.getInt( "hh_master_chain.disp_flag" );
                    // グループ名の取得
                    this.m_hotelChainName[count] = result.getString( "hh_master_chain.name" );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelChain] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * ホテル料金情報の取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelPrice(int hotelId)
    {
        boolean ret = true;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        // ホテル料金情報の取得
        query = "SELECT * FROM hh_hotel_price WHERE id = ?";
        query = query + " AND data_flag = 1";
        query = query + " AND (start_date < " + DateEdit.getDate( 2 ) +
                " or (start_date = " + DateEdit.getDate( 2 ) + " AND start_time <= " + DateEdit.getTime( 1 ) + "))";
        query = query + " AND (end_date > " + DateEdit.getDate( 2 ) +
                " or (end_date = " + DateEdit.getDate( 2 ) + " ANd end_time >= " + DateEdit.getTime( 1 ) + "))";
        query = query + " ORDER BY disp_index";

        int count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() )
                {
                    m_hotelPriceCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelPrice = new DataHotelPrice[this.m_hotelPriceCount];
                for( int i = 0 ; i < m_hotelPriceCount ; i++ )
                {
                    m_hotelPrice[i] = new DataHotelPrice();
                }

                result.beforeFirst();
                while( result.next() )
                {
                    // ホテル料金情報の取得
                    this.m_hotelPrice[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelPrice] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * ホテル地図関連情報の取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelMap(int hotelId)
    {
        boolean ret = true;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        // ホテル地図関連情報の取得
        query = "SELECT * FROM hh_hotel_map WHERE id = ?";

        int count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() )
                {
                    m_hotelMapCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelMap = new DataHotelMap[this.m_hotelMapCount];
                for( int i = 0 ; i < m_hotelMapCount ; i++ )
                {
                    m_hotelMap[i] = new DataHotelMap();
                }

                result.beforeFirst();
                while( result.next() )
                {
                    // ホテル地図関連情報の取得
                    this.m_hotelMap[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelMap] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * ホテル設備情報の取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelEquipment(int hotelId)
    {
        boolean ret = true;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        // ホテル設備情報の取得
        query = "SELECT hh_hotel_equip.id, hh_hotel_equip.equip_type, hh_hotel_equip.equip_rental, hh_hotel_equip.memo, hh_master_equip.* ";
        query = query + " FROM hh_hotel_equip";
        query = query + " INNER JOIN hh_master_equip ON hh_master_equip.equip_id = hh_hotel_equip.equip_id";
        query = query + " WHERE hh_hotel_equip.id  = ?";
        query = query + " AND hh_master_equip.input_flag5 <> 4";
        query = query + " AND hh_master_equip.input_flag6 > 0";
        query = query + " ORDER BY hh_master_equip.input_flag6, hh_master_equip.sort_display";

        int count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() )
                {
                    m_hotelEquipCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelEquip = new DataHotelEquip[this.m_hotelEquipCount];
                this.m_equipName = new DataMasterEquip[this.m_hotelEquipCount];
                for( int i = 0 ; i < m_hotelEquipCount ; i++ )
                {
                    m_hotelEquip[i] = new DataHotelEquip();
                    m_equipName[i] = new DataMasterEquip();
                }

                result.beforeFirst();
                while( result.next() )
                {
                    // ホテル設備情報の取得
                    this.m_hotelEquip[count].setData( result );
                    // 設備マスタ情報の取得
                    this.m_equipName[count].setData( result );

                    count++;
                }

                // 設備情報表示操作
                int addId = 0;
                int equipType = 0;
                for( int i = 0 ; i < m_hotelEquipCount ; i++ )
                {
                    // 設備情報マスタ.追加検索対象設備ID(hh_master_equip.add_id) が0以外で、かつホテル設備.設備タイプ(hh_hotel_equip.euqip_type)=1(全室)の場合
                    // hh_master_equip.add_idに該当するhh_hotel_equip.euqip_typeに9(なし)をセットする
                    addId = this.m_equipName[i].getAddId();
                    equipType = this.m_hotelEquip[i].getEquipType();
                    if ( addId != 0 && equipType == 1 )
                    {
                        for( int n = 0 ; n < m_hotelEquipCount ; n++ )
                        {
                            if ( this.m_hotelEquip[n].getEquipId() == addId )
                            {
                                this.m_hotelEquip[n].setEquipType( 9 );
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelEquipment] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * ホテナビ物件判断
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotenavi(int hotelId)
    {
        boolean ret = true;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        // ホテナビ有無
        query = "SELECT count(hotel.hotel_id) FROM hotel INNER JOIN hh_hotel_basic ON hotel.hotel_id = hh_hotel_basic.hotenavi_id AND hh_hotel_basic.id = ? WHERE hotel.plan IN (1,3,4)";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() )
                {
                    if ( result.getInt( 1 ) != 0 )
                    {
                        m_isHotenavi = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getIsHotenavi] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテナビ物件判断
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean getIsHotenavi(int hotelId)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        // ホテナビ有無
        query = "SELECT count(hotel.hotel_id) FROM hotel INNER JOIN hh_hotel_basic ON hotel.hotel_id = hh_hotel_basic.hotenavi_id AND hh_hotel_basic.id = ? WHERE hotel.plan IN (1,3,4)";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() )
                {
                    if ( result.getInt( 1 ) != 0 )
                        ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getIsHotenavi] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテル件数取得（地方IDから）
     * 
     * @param localId 地方ID
     * @return ホテル件数
     */
    public int getHotelCountByLocal(int localId)
    {
        int i;
        int count;
        int hotelCount;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        SearchEngineBasic seb;

        count = 0;
        hotelCount = 0;

        // 地方IDから都道府県一覧を取得
        seb = new SearchEngineBasic();
        seb.getPrefListByLocal( localId, 0 );

        query = "SELECT id FROM hh_hotel_basic WHERE id <> 0";

        query = query + " AND ";
        query = query + " ( ";
        for( i = 0 ; i < seb.getMasterPrefCount() ; i++ )
        {
            query = query + " pref_id = " + seb.getMasterPref()[i].getPrefId();

            if ( i < seb.getMasterPrefCount() - 1 )
            {
                query = query + " OR ";
            }
        }
        query = query + " ) ";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

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
                    hotelCount = result.getRow();
                }

                m_hotelIdList = new int[hotelCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelCountByLocal] Exception=" + e.toString() );
        }
        finally
        {
            seb = null;
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(hotelCount);
    }

    /**
     * ホテル件数取得（都道府県IDから）
     * 
     * @param prefId 都道府県ID
     * @return ホテル件数
     */
    public int getHotelCountByPref(int prefId)
    {
        int count;
        int hotelCount;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        count = 0;
        hotelCount = 0;

        query = "SELECT id FROM hh_hotel_basic WHERE id <> 0";
        query = query + " AND pref_id = ?";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    hotelCount = result.getRow();
                }

                m_hotelIdList = new int[hotelCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelCountByPref] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(hotelCount);
    }

    /**
     * ホテル件数取得（市区町村IDから）
     * 
     * @param jisCode 市区町村ID
     * @return ホテル件数
     */
    public int getHotelCountByCity(int jisCode)
    {
        int count;
        int hotelCount;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        count = 0;
        hotelCount = 0;

        query = "SELECT id FROM hh_hotel_basic WHERE id <> 0";
        query = query + " AND jis_code = ?";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, jisCode );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    hotelCount = result.getRow();
                }

                m_hotelIdList = new int[hotelCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelCountByCity] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(hotelCount);
    }

    /**
     * ホテル件数取得（エリアIDから）
     * 
     * @param areaId エリアID
     * @return ホテル件数
     */
    public int getHotelCountByArea(int areaId)
    {
        int count;
        int hotelCount;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        count = 0;
        hotelCount = 0;

        if ( areaId == 0 )
        {
            return(0);
        }

        query = "SELECT hh_hotel_area.id FROM hh_hotel_area,hh_hotel_basic WHERE hh_hotel_area.id <> 0";
        query = query + " AND hh_hotel_area.area_id = ?";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_area.id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, areaId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    hotelCount = result.getRow();
                }

                m_hotelIdList = new int[hotelCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "hh_hotel_area.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelCountByArea] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(hotelCount);
    }

    /**
     * ホテル件数取得（経路IDから）
     * 
     * @param routeId 経路ID
     * @return ホテル件数
     */
    public int getHotelCountByMapRoute(String routeId)
    {
        int i;
        int count;
        int hotelCount;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        SearchEngineBasic seb;

        count = 0;
        hotelCount = 0;

        // 経路からその駅・IC一覧を取得
        seb = new SearchEngineBasic();
        seb.getRailwayStationList( routeId );

        query = "SELECT hh_hotel_basic.id FROM hh_hotel_basic,hh_hotel_map WHERE hh_hotel_basic.id <> 0";
        query = query + " AND ";
        query = query + " ( ";
        for( i = 0 ; i < seb.getMapPointCount() ; i++ )
        {
            query = query + " hh_hotel_map.map_id = '" + seb.getMapPoint()[i].getOption4() + "'";

            if ( i < seb.getMapPointCount() - 1 )
            {
                query = query + " OR ";
            }
        }
        query = query + " ) ";
        query = query + " AND hh_hotel_basic.id = hh_hotel_map.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_map.disp_flag = 1";
        query = query + " GROUP BY hh_hotel_basic.id";
        query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

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
                    hotelCount = result.getRow();
                }

                m_hotelIdList = new int[hotelCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelCountByMapRoute] Exception=" + e.toString() );
        }
        finally
        {
            seb = null;
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(hotelCount);
    }

    /**
     * ホテル件数取得（地図IDから）
     * 
     * @param mapId 地図ID
     * @return ホテル件数
     */
    public int getHotelCountByMap(String mapId)
    {
        int count;
        int hotelCount;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        count = 0;
        hotelCount = 0;

        query = "SELECT hh_hotel_basic.id FROM hh_hotel_basic,hh_hotel_map WHERE hh_hotel_basic.id <> 0";
        query = query + " AND hh_hotel_map.map_id = ?";
        query = query + " AND hh_hotel_basic.id = hh_hotel_map.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_map.disp_flag = 1";
        query = query + " GROUP BY hh_hotel_basic.id";
        query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, mapId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    hotelCount = result.getRow();
                }

                m_hotelIdList = new int[hotelCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelCountByMap] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(hotelCount);
    }
}
