/*
 * @(#)SearchHotelGps.java 1.00 2007/08/06 Copyright (C) ALMEX Inc. 2007 GPS検索ホテル取得クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.ConvertGeodesic;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;

/**
 * GPS検索ホテル取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/06
 * @version 1.1 2007/11/20
 */
public class SearchHotelGps implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 5224942568576351030L;

    private static final int  LATLON_RANGE     = 100000;

    private int               m_hotelCount;
    private int               m_hotelAllCount;
    private DataHotelBasic[]  m_hotelInfo;
    private int[]             m_hotelIdList;

    private double            m_latW;
    private double            m_lonW;
    private double            m_latT;
    private double            m_lonT;
    private int               m_latWNum;
    private int               m_lonWNum;
    /**
     * 緯度１０進表記（日本測地系）
     */
    private int               m_latTNum;
    /**
     * 経度１０進表記（日本測地系）
     */
    private int               m_lonTNum;

    /**
     * データを初期化します。
     */
    public SearchHotelGps()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;

        m_latW = 0.0;
        m_lonW = 0.0;
        m_latT = 0.0;
        m_lonT = 0.0;
        m_latWNum = 0;
        m_lonWNum = 0;
        m_latTNum = 0;
        m_lonTNum = 0;
    }

    /** ホテル基本情報件数取得 **/
    public int getCount()
    {
        return(m_hotelCount);
    }

    /** ホテル基本情報件数取得 **/
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    /** ホテル基本情報取得 **/
    public DataHotelBasic[] getHotelInfo()
    {
        return(m_hotelInfo);
    }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    /**
     * @return m_latT
     */
    public double getLatT()
    {
        return m_latT;
    }

    /**
     * @return m_latTNum
     */
    public int getLatTNum()
    {
        return m_latTNum;
    }

    /**
     * @return m_latW
     */
    public double getLatW()
    {
        return m_latW;
    }

    /**
     * @return m_latWNum
     */
    public int getLatWNum()
    {
        return m_latWNum;
    }

    /**
     * @return m_lonT
     */
    public double getLonT()
    {
        return m_lonT;
    }

    /**
     * @return m_lonTNum
     */
    public int getLonTNum()
    {
        return m_lonTNum;
    }

    /**
     * @return m_lonW
     */
    public double getLonW()
    {
        return m_lonW;
    }

    /**
     * @return m_lonWNum
     */
    public int getLonWNum()
    {
        return m_lonWNum;
    }

    /**
     * ホテル一覧情報取得(ホテルランク順)
     * 
     * @param lat 緯度(世界測地系 999.99.99.99)
     * @param lon 経度(世界測地系 99.99.99.99)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelList(String lat, String lon, int countNum, int pageNum)
    {
        int i;
        int count;
        int latLeft;
        int lonLeft;
        int latRight;
        int lonRight;
        String query;
        String subQuery;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 世界測地系から日本測地系へ変換
        convertDms2Degree( lat, lon );

        if ( m_latTNum <= 0 || m_lonTNum <= 0 )
        {
            return(false);
        }

        // 左端(lat-10000,lon-10000)
        latLeft = m_latTNum - LATLON_RANGE;
        lonLeft = m_lonTNum - LATLON_RANGE;
        latRight = m_latTNum + LATLON_RANGE;
        lonRight = m_lonTNum + LATLON_RANGE;

        subQuery = " hh_hotel_basic.id, hh_hotel_basic.rank, hh_hotel_basic.kind," +
                " hh_hotel_basic.hotenavi_id, hh_hotel_basic.name, hh_hotel_basic.name_kana," +
                " hh_hotel_basic.name_mobile, hh_hotel_basic.zip_code, hh_hotel_basic.jis_code," +
                " hh_hotel_basic.pref_id, hh_hotel_basic.pref_name, hh_hotel_basic.pref_kana," +
                " hh_hotel_basic.address1, hh_hotel_basic.address2, hh_hotel_basic.address3, hh_hotel_basic.address_all," +
                " hh_hotel_basic.tel1, hh_hotel_basic.tel2, hh_hotel_basic.fax, hh_hotel_basic.charge_last," +
                " hh_hotel_basic.charge_first, hh_hotel_basic.charge_kana_last, hh_hotel_basic.charge_kana_first," +
                " hh_hotel_basic.charge_tel, hh_hotel_basic.charge_mail, hh_hotel_basic.open_date, hh_hotel_basic.renewal_date," +
                " hh_hotel_basic.url, hh_hotel_basic.url_official1, hh_hotel_basic.url_official2, hh_hotel_basic.url_official_mobile," +
                " hh_hotel_basic.pr, hh_hotel_basic.pr_detail, hh_hotel_basic.pr_event, hh_hotel_basic.pr_member," +
                " hh_hotel_basic.access, hh_hotel_basic.access_station, hh_hotel_basic.access_ic, hh_hotel_basic.room_count," +
                " hh_hotel_basic.parking, hh_hotel_basic.parking_count, hh_hotel_basic.type_building, hh_hotel_basic.type_kodate," +
                " hh_hotel_basic.type_rentou, hh_hotel_basic.type_etc, hh_hotel_basic.location_station, hh_hotel_basic.location_ic," +
                " hh_hotel_basic.location_kougai, hh_hotel_basic.benefit, hh_hotel_basic.benefit, hh_hotel_basic.roomservice," +
                " hh_hotel_basic.pay_front, hh_hotel_basic.pay_auto, hh_hotel_basic.credit, hh_hotel_basic.credit_visa," +
                " hh_hotel_basic.credit_master, hh_hotel_basic.credit_jcb, hh_hotel_basic.credit_dc, hh_hotel_basic.credit_nicos," +
                " hh_hotel_basic.credit_amex, credit_etc, hh_hotel_basic.halfway, hh_hotel_basic.coupon," +
                " hh_hotel_basic.possible_one, hh_hotel_basic.possible_three, hh_hotel_basic.reserve, hh_hotel_basic.reserve_tel," +
                " hh_hotel_basic.reserve_mail, hh_hotel_basic.reserve_web, hh_hotel_basic.empty_method, hh_hotel_basic.hotel_lat," +
                " hh_hotel_basic.hotel_lon, hh_hotel_basic.hotel_lat_num, hh_hotel_basic.hotel_lon_num, hh_hotel_basic.disp_lat," +
                " hh_hotel_basic.disp_lon, hh_hotel_basic.zoom, hh_hotel_basic.over18_flag, hh_hotel_basic.company_type," +
                " IFNULL(hh_hotel_status.`mode`, 0) empty_kind, IFNULL(hh_hotel_status.empty_status, 0) empty_status, hh_hotel_basic.group_code, hh_hotel_basic.last_update," +
                " hh_hotel_basic.last_uptime, hh_hotel_basic.pr_room, hh_hotel_basic.pr_etc, hh_hotel_basic.renewal_flag," +
                " hh_hotel_basic.url_special, hh_hotel_basic.hotel_lat_jp, hh_hotel_basic.hotel_lon_jp, hh_hotel_basic.map_code," +
                " hh_hotel_basic.high_roof, hh_hotel_basic.high_roof_count, hh_hotel_basic.empty_hotenavi_id, attention_flag," +
                " hh_hotel_basic.ad_pref_id, hh_hotel_basic.renewal_date_text, hh_hotel_basic.new_open_search_flag";

        query = "SELECT " + subQuery + " FROM hh_hotel_basic";
        query = query + " LEFT JOIN hh_hotel_status";
        query = query + " ON hh_hotel_basic.id = hh_hotel_status.id";
        query = query + " ,hh_hotel_pv WHERE";
        query = query + " (hotel_lat_num > ? AND hotel_lat_num < ?)";
        query = query + " AND ";
        query = query + " (hotel_lon_num > ? AND hotel_lon_num < ?)";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
        query = query + " AND hh_hotel_pv.collect_date = 0";
        query = query + " GROUP BY hh_hotel_basic.id";
        query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_pv.total_uu_pv DESC, hh_hotel_basic.name_kana";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, latLeft );
            prestate.setInt( 2, latRight );
            prestate.setInt( 3, lonLeft );
            prestate.setInt( 4, lonRight );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_hotelCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelInfo = new DataHotelBasic[this.m_hotelCount];
                for( i = 0 ; i < m_hotelCount ; i++ )
                {
                    m_hotelInfo[i] = new DataHotelBasic();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelInfo[count].setId( result.getInt( "id" ) );
                    this.m_hotelInfo[count].setRank( result.getInt( "rank" ) );
                    this.m_hotelInfo[count].setKind( result.getInt( "kind" ) );
                    this.m_hotelInfo[count].setHotenaviId( result.getString( "hotenavi_id" ) );
                    this.m_hotelInfo[count].setName( result.getString( "name" ) );
                    this.m_hotelInfo[count].setNameKana( result.getString( "name_kana" ) );
                    this.m_hotelInfo[count].setNameMobile( result.getString( "name_mobile" ) );
                    this.m_hotelInfo[count].setZipCode( result.getString( "zip_code" ) );
                    this.m_hotelInfo[count].setJisCode( result.getInt( "jis_code" ) );
                    this.m_hotelInfo[count].setPrefId( result.getInt( "pref_id" ) );
                    this.m_hotelInfo[count].setPrefName( result.getString( "pref_name" ) );
                    this.m_hotelInfo[count].setPrefKana( result.getString( "pref_kana" ) );
                    this.m_hotelInfo[count].setAddress1( result.getString( "address1" ) );
                    this.m_hotelInfo[count].setAddress2( result.getString( "address2" ) );
                    this.m_hotelInfo[count].setAddress3( result.getString( "address3" ) );
                    this.m_hotelInfo[count].setAddressAll( result.getString( "address_all" ) );
                    this.m_hotelInfo[count].setTel1( result.getString( "tel1" ) );
                    this.m_hotelInfo[count].setTel2( result.getString( "tel2" ) );
                    this.m_hotelInfo[count].setFax( result.getString( "fax" ) );
                    this.m_hotelInfo[count].setChargeLast( result.getString( "charge_last" ) );
                    this.m_hotelInfo[count].setChargeFirst( result.getString( "charge_first" ) );
                    this.m_hotelInfo[count].setChargeKanaLast( result.getString( "charge_kana_last" ) );
                    this.m_hotelInfo[count].setChargeKanaFirst( result.getString( "charge_kana_first" ) );
                    this.m_hotelInfo[count].setChargeTel( result.getString( "charge_tel" ) );
                    this.m_hotelInfo[count].setChargeMail( result.getString( "charge_mail" ) );
                    this.m_hotelInfo[count].setOpenDate( result.getInt( "open_date" ) );
                    this.m_hotelInfo[count].setRenewalDate( result.getInt( "renewal_date" ) );
                    this.m_hotelInfo[count].setUrl( result.getString( "url" ) );
                    this.m_hotelInfo[count].setUrlOfficial1( result.getString( "url_official1" ) );
                    this.m_hotelInfo[count].setUrlOfficial2( result.getString( "url_official2" ) );
                    this.m_hotelInfo[count].setUrlOfficialMobile( result.getString( "url_official_mobile" ) );
                    this.m_hotelInfo[count].setPr( result.getString( "pr" ) );
                    this.m_hotelInfo[count].setPrDetail( result.getString( "pr_detail" ) );
                    this.m_hotelInfo[count].setPrEvent( result.getString( "pr_event" ) );
                    this.m_hotelInfo[count].setPrMember( result.getString( "pr_member" ) );
                    this.m_hotelInfo[count].setAccess( result.getString( "access" ) );
                    this.m_hotelInfo[count].setAccessStation( result.getString( "access_station" ) );
                    this.m_hotelInfo[count].setAccessIc( result.getString( "access_ic" ) );
                    this.m_hotelInfo[count].setRoomCount( result.getInt( "room_count" ) );
                    this.m_hotelInfo[count].setParking( result.getInt( "parking" ) );
                    this.m_hotelInfo[count].setParkingCount( result.getInt( "parking_count" ) );
                    this.m_hotelInfo[count].setTypeBuilding( result.getInt( "type_building" ) );
                    this.m_hotelInfo[count].setTypeKodate( result.getInt( "type_kodate" ) );
                    this.m_hotelInfo[count].setTypeRentou( result.getInt( "type_rentou" ) );
                    this.m_hotelInfo[count].setTypeEtc( result.getString( "type_etc" ) );
                    this.m_hotelInfo[count].setLocationStation( result.getInt( "location_station" ) );
                    this.m_hotelInfo[count].setLocationIc( result.getInt( "location_ic" ) );
                    this.m_hotelInfo[count].setLocationKougai( result.getInt( "location_kougai" ) );
                    this.m_hotelInfo[count].setBenefit( result.getInt( "benefit" ) );
                    this.m_hotelInfo[count].setRoomService( result.getInt( "roomservice" ) );
                    this.m_hotelInfo[count].setPayFront( result.getInt( "pay_front" ) );
                    this.m_hotelInfo[count].setPayAuto( result.getInt( "pay_auto" ) );
                    this.m_hotelInfo[count].setCredit( result.getInt( "credit" ) );
                    this.m_hotelInfo[count].setCreditVisa( result.getInt( "credit_visa" ) );
                    this.m_hotelInfo[count].setCreditMaster( result.getInt( "credit_master" ) );
                    this.m_hotelInfo[count].setCreditJcb( result.getInt( "credit_jcb" ) );
                    this.m_hotelInfo[count].setCreditDc( result.getInt( "credit_dc" ) );
                    this.m_hotelInfo[count].setCreditNicos( result.getInt( "credit_nicos" ) );
                    this.m_hotelInfo[count].setCreditAmex( result.getInt( "credit_amex" ) );
                    this.m_hotelInfo[count].setCreditEtc( result.getString( "credit_etc" ) );
                    this.m_hotelInfo[count].setHalfway( result.getInt( "halfway" ) );
                    this.m_hotelInfo[count].setCoupon( result.getInt( "coupon" ) );
                    this.m_hotelInfo[count].setPossibleOne( result.getInt( "possible_one" ) );
                    this.m_hotelInfo[count].setPossibleThree( result.getInt( "possible_three" ) );
                    this.m_hotelInfo[count].setReserve( result.getInt( "reserve" ) );
                    this.m_hotelInfo[count].setReserveTel( result.getInt( "reserve_tel" ) );
                    this.m_hotelInfo[count].setReserveMail( result.getInt( "reserve_mail" ) );
                    this.m_hotelInfo[count].setReserveWeb( result.getInt( "reserve_web" ) );
                    this.m_hotelInfo[count].setEmptyMethod( result.getInt( "empty_method" ) );
                    this.m_hotelInfo[count].setHotelLat( result.getString( "hotel_lat" ) );
                    this.m_hotelInfo[count].setHotelLon( result.getString( "hotel_lon" ) );
                    this.m_hotelInfo[count].setHotelLatNum( result.getInt( "hotel_lat_num" ) );
                    this.m_hotelInfo[count].setHotelLonNum( result.getInt( "hotel_lon_num" ) );
                    this.m_hotelInfo[count].setDispLat( result.getString( "disp_lat" ) );
                    this.m_hotelInfo[count].setDispLon( result.getString( "disp_lon" ) );
                    this.m_hotelInfo[count].setZoom( result.getInt( "zoom" ) );
                    this.m_hotelInfo[count].setOver18Flag( result.getInt( "over18_flag" ) );
                    this.m_hotelInfo[count].setCompanyType( result.getInt( "company_type" ) );
                    this.m_hotelInfo[count].setEmptyKind( result.getInt( "empty_kind" ) );
                    this.m_hotelInfo[count].setEmptyStatus( result.getInt( "empty_status" ) );
                    this.m_hotelInfo[count].setGroupCode( result.getInt( "group_code" ) );
                    this.m_hotelInfo[count].setLastUpdate( result.getInt( "last_update" ) );
                    this.m_hotelInfo[count].setLastUptime( result.getInt( "last_uptime" ) );
                    this.m_hotelInfo[count].setPrRoom( result.getString( "pr_room" ) );
                    this.m_hotelInfo[count].setPrEtc( result.getString( "pr_etc" ) );
                    this.m_hotelInfo[count].setRenewalFlag( result.getInt( "renewal_flag" ) );
                    this.m_hotelInfo[count].setUrlSpecial( result.getString( "url_special" ) );
                    this.m_hotelInfo[count].setHotelLatJp( result.getString( "hotel_lat_jp" ) );
                    this.m_hotelInfo[count].setHotelLonJp( result.getString( "hotel_lon_jp" ) );
                    this.m_hotelInfo[count].setMapCode( result.getString( "map_code" ) );
                    this.m_hotelInfo[count].setHighRoof( result.getInt( "high_roof" ) );
                    this.m_hotelInfo[count].setHighRoofCount( result.getInt( "high_roof_count" ) );
                    this.m_hotelInfo[count].setEmptyHotenaviId( result.getString( "empty_hotenavi_id" ) );
                    this.m_hotelInfo[count].setAttentionFlag( result.getInt( "attention_flag" ) );
                    this.m_hotelInfo[count].setAdPrefId( result.getInt( "ad_pref_id" ) );
                    this.m_hotelInfo[count].setRenewalDateText( result.getString( "renewal_date_text" ) );
                    this.m_hotelInfo[count].setNewOpenSearchFlag( result.getInt( "new_open_search_flag" ) );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList1] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT id FROM hh_hotel_basic WHERE";
        query = query + " (hotel_lat_num > ? AND hotel_lat_num < ?)";
        query = query + " AND ";
        query = query + " (hotel_lon_num > ? AND hotel_lon_num < ?)";

        count = 0;

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, latLeft );
            prestate.setInt( 2, latRight );
            prestate.setInt( 3, lonLeft );
            prestate.setInt( 4, lonRight );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.m_hotelAllCount = result.getRow();
                }

                m_hotelIdList = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * Dms⇒degreeコンバート
     * 
     * @param lat 緯度(999.99.99.99)
     * @param lon 経度(99.99.99.99)
     */
    public void convertDms2Degree(String lat, String lon)
    {
        int i;
        double[] cutLat;
        double[] cutLon;
        Double cutLatD = 0.0;
        Double cutLonD = 0.0;
        String[] cutData;
        ConvertGeodesic cg;

        cg = new ConvertGeodesic();

        // dms→degree変換
        cutData = lat.split( "\\.", 3 );
        cutLat = new double[cutData.length];
        for( i = 0 ; i < cutData.length ; i++ )
        {
            cutLat[i] = Double.valueOf( cutData[i] );
        }
        if ( cutData.length > 2 )
        {
            cutLatD = (double)(cutLat[0]) + (double)(cutLat[1] / 60) + (double)(cutLat[2] / 3600);
        }

        cutData = lon.split( "\\.", 3 );
        cutLon = new double[cutData.length];
        for( i = 0 ; i < cutData.length ; i++ )
        {
            cutLon[i] = Double.valueOf( cutData[i] );
        }
        if ( cutData.length > 2 )
        {
            cutLonD = (double)(cutLon[0]) + (double)(cutLon[1] / 60) + (double)(cutLon[2] / 3600);
        }

        // 世界測地系dgree
        m_latW = cutLatD;
        m_lonW = cutLonD;
        // 世界測地系num変換
        cg.Wgs2WgsNum( m_latW, m_lonW );
        m_latWNum = cg.getLatWGSNum();
        m_lonWNum = cg.getLonWGSNum();

        // WGS->Tokyo
        cg.Wgs2Tokyo( cutLatD, cutLonD );

        // 日本測地系dgree
        m_latT = cg.getLatTOKYO();
        m_lonT = cg.getLonTOKYO();
        // 日本測地系num変換
        cg.Tokyo2TokyoNum( m_latT, m_lonT );
        m_latTNum = cg.getLatTOKYONum();
        m_lonTNum = cg.getLonTOKYONum();
    }

    /**
     * ホテル一覧情報取得(ホテルランク順)
     * 
     * @param lat 緯度(世界測地系 999.99.99.99)
     * @param lon 経度(世界測地系 99.99.99.99)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelList(String lat, String lon, int countNum, int pageNum, int kind)
    {
        int i;
        int count;
        int latLeft;
        int lonLeft;
        int latRight;
        int lonRight;
        String query;
        String subQuery;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 世界測地系から日本測地系へ変換
        convertDms2Degree( lat, lon );
        Logging.error( "[getHotelList] lat=" + m_latT + ",lon=" + m_lonT );

        if ( m_latTNum <= 0 || m_lonTNum <= 0 )
        {
            return(false);
        }

        if ( kind == 0 )
        {
            // 左端(lat-10000,lon-10000)
            latLeft = m_latTNum - LATLON_RANGE;
            lonLeft = m_lonTNum - LATLON_RANGE;
            latRight = m_latTNum + LATLON_RANGE;
            lonRight = m_lonTNum + LATLON_RANGE;
        }
        else if ( kind == 1 )
        {
            latLeft = m_latTNum - (LATLON_RANGE * 2);
            lonLeft = m_lonTNum - (LATLON_RANGE * 2);
            latRight = m_latTNum + (LATLON_RANGE * 2);
            lonRight = m_lonTNum + (LATLON_RANGE * 2);
        }
        else
        {
            latLeft = m_latTNum - (LATLON_RANGE / 2);
            lonLeft = m_lonTNum - (LATLON_RANGE / 2);
            latRight = m_latTNum + (LATLON_RANGE / 2);
            lonRight = m_lonTNum + (LATLON_RANGE / 2);
        }

        ConvertGeodesic cg;
        cg = new ConvertGeodesic();
        cg.TokyoNum2Tokyo( latRight, lonRight );
        Logging.error( "[getHotelList] lat2=" + cg.getLatTOKYO() + ",lon2=" + cg.getLonTOKYO() );
        cg.Tokyo2Wgs( cg.getLatTOKYO(), cg.getLonTOKYO() );
        Logging.error( "[getHotelList] lat3=" + cg.getLatWGS() + ",lon3=" + cg.getLonWGS() );

        subQuery = " hh_hotel_basic.id, hh_hotel_basic.rank, hh_hotel_basic.kind," +
                " hh_hotel_basic.hotenavi_id, hh_hotel_basic.name, hh_hotel_basic.name_kana," +
                " hh_hotel_basic.name_mobile, hh_hotel_basic.zip_code, hh_hotel_basic.jis_code," +
                " hh_hotel_basic.pref_id, hh_hotel_basic.pref_name, hh_hotel_basic.pref_kana," +
                " hh_hotel_basic.address1, hh_hotel_basic.address2, hh_hotel_basic.address3, hh_hotel_basic.address_all," +
                " hh_hotel_basic.tel1, hh_hotel_basic.tel2, hh_hotel_basic.fax, hh_hotel_basic.charge_last," +
                " hh_hotel_basic.charge_first, hh_hotel_basic.charge_kana_last, hh_hotel_basic.charge_kana_first," +
                " hh_hotel_basic.charge_tel, hh_hotel_basic.charge_mail, hh_hotel_basic.open_date, hh_hotel_basic.renewal_date," +
                " hh_hotel_basic.url, hh_hotel_basic.url_official1, hh_hotel_basic.url_official2, hh_hotel_basic.url_official_mobile," +
                " hh_hotel_basic.pr, hh_hotel_basic.pr_detail, hh_hotel_basic.pr_event, hh_hotel_basic.pr_member," +
                " hh_hotel_basic.access, hh_hotel_basic.access_station, hh_hotel_basic.access_ic, hh_hotel_basic.room_count," +
                " hh_hotel_basic.parking, hh_hotel_basic.parking_count, hh_hotel_basic.type_building, hh_hotel_basic.type_kodate," +
                " hh_hotel_basic.type_rentou, hh_hotel_basic.type_etc, hh_hotel_basic.location_station, hh_hotel_basic.location_ic," +
                " hh_hotel_basic.location_kougai, hh_hotel_basic.benefit, hh_hotel_basic.benefit, hh_hotel_basic.roomservice," +
                " hh_hotel_basic.pay_front, hh_hotel_basic.pay_auto, hh_hotel_basic.credit, hh_hotel_basic.credit_visa," +
                " hh_hotel_basic.credit_master, hh_hotel_basic.credit_jcb, hh_hotel_basic.credit_dc, hh_hotel_basic.credit_nicos," +
                " hh_hotel_basic.credit_amex, credit_etc, hh_hotel_basic.halfway, hh_hotel_basic.coupon," +
                " hh_hotel_basic.possible_one, hh_hotel_basic.possible_three, hh_hotel_basic.reserve, hh_hotel_basic.reserve_tel," +
                " hh_hotel_basic.reserve_mail, hh_hotel_basic.reserve_web, hh_hotel_basic.empty_method, hh_hotel_basic.hotel_lat," +
                " hh_hotel_basic.hotel_lon, hh_hotel_basic.hotel_lat_num, hh_hotel_basic.hotel_lon_num, hh_hotel_basic.disp_lat," +
                " hh_hotel_basic.disp_lon, hh_hotel_basic.zoom, hh_hotel_basic.over18_flag, hh_hotel_basic.company_type," +
                " IFNULL(hh_hotel_status.`mode`, 0) empty_kind, IFNULL(hh_hotel_status.empty_status, 0) empty_status, hh_hotel_basic.group_code, hh_hotel_basic.last_update," +
                " hh_hotel_basic.last_uptime, hh_hotel_basic.pr_room, hh_hotel_basic.pr_etc, hh_hotel_basic.renewal_flag," +
                " hh_hotel_basic.url_special, hh_hotel_basic.hotel_lat_jp, hh_hotel_basic.hotel_lon_jp, hh_hotel_basic.map_code," +
                " hh_hotel_basic.high_roof, hh_hotel_basic.high_roof_count, hh_hotel_basic.empty_hotenavi_id, attention_flag," +
                " hh_hotel_basic.ad_pref_id, hh_hotel_basic.renewal_date_text, hh_hotel_basic.new_open_search_flag";

        query = "SELECT " + subQuery + " FROM hh_hotel_basic";
        query = query + " LEFT JOIN hh_hotel_status";
        query = query + " ON hh_hotel_basic.id = hh_hotel_status.id";
        query = query + " ,hh_hotel_pv WHERE";
        query = query + " (hotel_lat_num > ? AND hotel_lat_num < ?)";
        query = query + " AND ";
        query = query + " (hotel_lon_num > ? AND hotel_lon_num < ?)";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
        query = query + " AND hh_hotel_pv.collect_date = 0";
        query = query + " GROUP BY hh_hotel_basic.id";
        query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_pv.total_uu_pv DESC, hh_hotel_basic.name_kana";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, latLeft );
            prestate.setInt( 2, latRight );
            prestate.setInt( 3, lonLeft );
            prestate.setInt( 4, lonRight );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_hotelCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelInfo = new DataHotelBasic[this.m_hotelCount];
                for( i = 0 ; i < m_hotelCount ; i++ )
                {
                    m_hotelInfo[i] = new DataHotelBasic();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelInfo[count].setId( result.getInt( "id" ) );
                    this.m_hotelInfo[count].setRank( result.getInt( "rank" ) );
                    this.m_hotelInfo[count].setKind( result.getInt( "kind" ) );
                    this.m_hotelInfo[count].setHotenaviId( result.getString( "hotenavi_id" ) );
                    this.m_hotelInfo[count].setName( result.getString( "name" ) );
                    this.m_hotelInfo[count].setNameKana( result.getString( "name_kana" ) );
                    this.m_hotelInfo[count].setNameMobile( result.getString( "name_mobile" ) );
                    this.m_hotelInfo[count].setZipCode( result.getString( "zip_code" ) );
                    this.m_hotelInfo[count].setJisCode( result.getInt( "jis_code" ) );
                    this.m_hotelInfo[count].setPrefId( result.getInt( "pref_id" ) );
                    this.m_hotelInfo[count].setPrefName( result.getString( "pref_name" ) );
                    this.m_hotelInfo[count].setPrefKana( result.getString( "pref_kana" ) );
                    this.m_hotelInfo[count].setAddress1( result.getString( "address1" ) );
                    this.m_hotelInfo[count].setAddress2( result.getString( "address2" ) );
                    this.m_hotelInfo[count].setAddress3( result.getString( "address3" ) );
                    this.m_hotelInfo[count].setAddressAll( result.getString( "address_all" ) );
                    this.m_hotelInfo[count].setTel1( result.getString( "tel1" ) );
                    this.m_hotelInfo[count].setTel2( result.getString( "tel2" ) );
                    this.m_hotelInfo[count].setFax( result.getString( "fax" ) );
                    this.m_hotelInfo[count].setChargeLast( result.getString( "charge_last" ) );
                    this.m_hotelInfo[count].setChargeFirst( result.getString( "charge_first" ) );
                    this.m_hotelInfo[count].setChargeKanaLast( result.getString( "charge_kana_last" ) );
                    this.m_hotelInfo[count].setChargeKanaFirst( result.getString( "charge_kana_first" ) );
                    this.m_hotelInfo[count].setChargeTel( result.getString( "charge_tel" ) );
                    this.m_hotelInfo[count].setChargeMail( result.getString( "charge_mail" ) );
                    this.m_hotelInfo[count].setOpenDate( result.getInt( "open_date" ) );
                    this.m_hotelInfo[count].setRenewalDate( result.getInt( "renewal_date" ) );
                    this.m_hotelInfo[count].setUrl( result.getString( "url" ) );
                    this.m_hotelInfo[count].setUrlOfficial1( result.getString( "url_official1" ) );
                    this.m_hotelInfo[count].setUrlOfficial2( result.getString( "url_official2" ) );
                    this.m_hotelInfo[count].setUrlOfficialMobile( result.getString( "url_official_mobile" ) );
                    this.m_hotelInfo[count].setPr( result.getString( "pr" ) );
                    this.m_hotelInfo[count].setPrDetail( result.getString( "pr_detail" ) );
                    this.m_hotelInfo[count].setPrEvent( result.getString( "pr_event" ) );
                    this.m_hotelInfo[count].setPrMember( result.getString( "pr_member" ) );
                    this.m_hotelInfo[count].setAccess( result.getString( "access" ) );
                    this.m_hotelInfo[count].setAccessStation( result.getString( "access_station" ) );
                    this.m_hotelInfo[count].setAccessIc( result.getString( "access_ic" ) );
                    this.m_hotelInfo[count].setRoomCount( result.getInt( "room_count" ) );
                    this.m_hotelInfo[count].setParking( result.getInt( "parking" ) );
                    this.m_hotelInfo[count].setParkingCount( result.getInt( "parking_count" ) );
                    this.m_hotelInfo[count].setTypeBuilding( result.getInt( "type_building" ) );
                    this.m_hotelInfo[count].setTypeKodate( result.getInt( "type_kodate" ) );
                    this.m_hotelInfo[count].setTypeRentou( result.getInt( "type_rentou" ) );
                    this.m_hotelInfo[count].setTypeEtc( result.getString( "type_etc" ) );
                    this.m_hotelInfo[count].setLocationStation( result.getInt( "location_station" ) );
                    this.m_hotelInfo[count].setLocationIc( result.getInt( "location_ic" ) );
                    this.m_hotelInfo[count].setLocationKougai( result.getInt( "location_kougai" ) );
                    this.m_hotelInfo[count].setBenefit( result.getInt( "benefit" ) );
                    this.m_hotelInfo[count].setRoomService( result.getInt( "roomservice" ) );
                    this.m_hotelInfo[count].setPayFront( result.getInt( "pay_front" ) );
                    this.m_hotelInfo[count].setPayAuto( result.getInt( "pay_auto" ) );
                    this.m_hotelInfo[count].setCredit( result.getInt( "credit" ) );
                    this.m_hotelInfo[count].setCreditVisa( result.getInt( "credit_visa" ) );
                    this.m_hotelInfo[count].setCreditMaster( result.getInt( "credit_master" ) );
                    this.m_hotelInfo[count].setCreditJcb( result.getInt( "credit_jcb" ) );
                    this.m_hotelInfo[count].setCreditDc( result.getInt( "credit_dc" ) );
                    this.m_hotelInfo[count].setCreditNicos( result.getInt( "credit_nicos" ) );
                    this.m_hotelInfo[count].setCreditAmex( result.getInt( "credit_amex" ) );
                    this.m_hotelInfo[count].setCreditEtc( result.getString( "credit_etc" ) );
                    this.m_hotelInfo[count].setHalfway( result.getInt( "halfway" ) );
                    this.m_hotelInfo[count].setCoupon( result.getInt( "coupon" ) );
                    this.m_hotelInfo[count].setPossibleOne( result.getInt( "possible_one" ) );
                    this.m_hotelInfo[count].setPossibleThree( result.getInt( "possible_three" ) );
                    this.m_hotelInfo[count].setReserve( result.getInt( "reserve" ) );
                    this.m_hotelInfo[count].setReserveTel( result.getInt( "reserve_tel" ) );
                    this.m_hotelInfo[count].setReserveMail( result.getInt( "reserve_mail" ) );
                    this.m_hotelInfo[count].setReserveWeb( result.getInt( "reserve_web" ) );
                    this.m_hotelInfo[count].setEmptyMethod( result.getInt( "empty_method" ) );
                    this.m_hotelInfo[count].setHotelLat( result.getString( "hotel_lat" ) );
                    this.m_hotelInfo[count].setHotelLon( result.getString( "hotel_lon" ) );
                    this.m_hotelInfo[count].setHotelLatNum( result.getInt( "hotel_lat_num" ) );
                    this.m_hotelInfo[count].setHotelLonNum( result.getInt( "hotel_lon_num" ) );
                    this.m_hotelInfo[count].setDispLat( result.getString( "disp_lat" ) );
                    this.m_hotelInfo[count].setDispLon( result.getString( "disp_lon" ) );
                    this.m_hotelInfo[count].setZoom( result.getInt( "zoom" ) );
                    this.m_hotelInfo[count].setOver18Flag( result.getInt( "over18_flag" ) );
                    this.m_hotelInfo[count].setCompanyType( result.getInt( "company_type" ) );
                    this.m_hotelInfo[count].setEmptyKind( result.getInt( "empty_kind" ) );
                    this.m_hotelInfo[count].setEmptyStatus( result.getInt( "empty_status" ) );
                    this.m_hotelInfo[count].setGroupCode( result.getInt( "group_code" ) );
                    this.m_hotelInfo[count].setLastUpdate( result.getInt( "last_update" ) );
                    this.m_hotelInfo[count].setLastUptime( result.getInt( "last_uptime" ) );
                    this.m_hotelInfo[count].setPrRoom( result.getString( "pr_room" ) );
                    this.m_hotelInfo[count].setPrEtc( result.getString( "pr_etc" ) );
                    this.m_hotelInfo[count].setRenewalFlag( result.getInt( "renewal_flag" ) );
                    this.m_hotelInfo[count].setUrlSpecial( result.getString( "url_special" ) );
                    this.m_hotelInfo[count].setHotelLatJp( result.getString( "hotel_lat_jp" ) );
                    this.m_hotelInfo[count].setHotelLonJp( result.getString( "hotel_lon_jp" ) );
                    this.m_hotelInfo[count].setMapCode( result.getString( "map_code" ) );
                    this.m_hotelInfo[count].setHighRoof( result.getInt( "high_roof" ) );
                    this.m_hotelInfo[count].setHighRoofCount( result.getInt( "high_roof_count" ) );
                    this.m_hotelInfo[count].setEmptyHotenaviId( result.getString( "empty_hotenavi_id" ) );
                    this.m_hotelInfo[count].setAttentionFlag( result.getInt( "attention_flag" ) );
                    this.m_hotelInfo[count].setAdPrefId( result.getInt( "ad_pref_id" ) );
                    this.m_hotelInfo[count].setRenewalDateText( result.getString( "renewal_date_text" ) );
                    this.m_hotelInfo[count].setNewOpenSearchFlag( result.getInt( "new_open_search_flag" ) );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList2] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT id FROM hh_hotel_basic WHERE";
        query = query + " (hotel_lat_num > ? AND hotel_lat_num < ?)";
        query = query + " AND ";
        query = query + " (hotel_lon_num > ? AND hotel_lon_num < ?)";

        count = 0;

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, latLeft );
            prestate.setInt( 2, latRight );
            prestate.setInt( 3, lonLeft );
            prestate.setInt( 4, lonRight );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.m_hotelAllCount = result.getRow();
                }

                m_hotelIdList = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList2] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

}
