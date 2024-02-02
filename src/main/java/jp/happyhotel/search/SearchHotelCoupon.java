/*
 * @(#)SearchHotelCoupon.java 1.00 2007/07/18
 * @(#)SearchHotelCoupon.java 1.01 2008/02/27 Copyright (C) ALMEX Inc. 2007 こだわり検索ホテル取得クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;

/**
 * クーポン検索ホテル取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/19
 */
public class SearchHotelCoupon implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 5377516176123625349L;

    private int               m_hotelCount;
    private int               m_hotelAllCount;
    private int[]             m_hotelIdList;
    private DataHotelBasic[]  m_hotelInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelCoupon()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;
    }

    public int getCount()
    {
        return(m_hotelCount);
    }

    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    public DataHotelBasic[] getHotelInfo()
    {
        return(m_hotelInfo);
    }

    /**
     * クーポン検索結果取得(ホテルランク順)
     * 
     * @param hotelIdList ホテルIDリスト(null:全件検索)
     * @param mobileFlag 携帯フラグ(true:携帯)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelList(int[] hotelIdList, boolean mobileFlag, int countNum, int pageNum)
    {
        int i;
        int count;
        boolean ret;
        String query;
        String subQuery;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

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

        query = "SELECT " + subQuery + " FROM hh_hotel_coupon,hh_hotel_basic";
        query = query + " LEFT JOIN hh_hotel_status";
        query = query + " ON hh_hotel_basic.id = hh_hotel_status.id";
        query = query + " ,hh_hotel_pv, hh_master_coupon WHERE";
        query = query + " hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.del_flag <> 1";

        if ( mobileFlag != false )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }
        if ( hotelIdList != null )
        {
            if ( hotelIdList.length > 0 )
            {
                query = query + " AND hh_hotel_basic.id IN(";
                for( i = 0 ; i < hotelIdList.length ; i++ )
                {
                    query = query + hotelIdList[i];
                    if ( i < hotelIdList.length - 1 )
                    {
                        query = query + ",";
                    }
                }
                query = query + ")";
            }
        }

        query = query + " AND hh_hotel_basic.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
        query = query + " AND hh_hotel_pv.collect_date = 0";

        // ホテナビのクーポンは取得しない
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id";
        query = query + " AND hh_master_coupon.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR hh_master_coupon.service_flag = 1 )";

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
                    this.m_hotelInfo[count++].setData( result );
                }
            }

            ret = true;
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchHotelCoupon.getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id FROM hh_hotel_coupon,hh_hotel_basic, hh_master_coupon WHERE";
        query = query + " hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.del_flag <> 1";
        if ( mobileFlag != false )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }
        if ( hotelIdList != null )
        {
            if ( hotelIdList.length > 0 )
            {
                query = query + " AND hh_hotel_basic.id IN(";
                for( i = 0 ; i < hotelIdList.length ; i++ )
                {
                    query = query + hotelIdList[i];
                    if ( i < hotelIdList.length - 1 )
                    {
                        query = query + ",";
                    }
                }
                query = query + ")";
            }
        }

        // ホテナビのクーポンは取得しない
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id";
        query = query + " AND hh_master_coupon.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR hh_master_coupon.service_flag = 1 )";

        query = query + " GROUP BY hh_hotel_basic.id";

        count = 0;

        try
        {
            prestate = connection.prepareStatement( query );
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
                    m_hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelCoupon.getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * クーポン検索結果取得(ホテルランク順)
     * 
     * @param prefId 都道府県ID(0:全件検索)
     * @param available メンバーフラグ（0:制限なし、1:メンバーのみ, 2:ビジターのみ）
     * @param mobileFlag 携帯フラグ(true:携帯)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelListByPref(int prefId, boolean mobileFlag, int countNum, int pageNum)
    {
        int i;
        int count;
        boolean ret;
        String query;
        String subQuery;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

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

        query = "SELECT " + subQuery + " FROM hh_hotel_coupon,hh_hotel_basic";
        query = query + " LEFT JOIN hh_hotel_status";
        query = query + " ON hh_hotel_basic.id = hh_hotel_status.id";
        query = query + " ,hh_hotel_pv, hh_master_coupon WHERE";
        query = query + " hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.del_flag <> 1";

        if ( mobileFlag != false )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }
        if ( prefId > 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = ?";
        }

        query = query + " AND hh_hotel_basic.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
        query = query + " AND hh_hotel_pv.collect_date = 0";

        // ホテナビのクーポンは取得しない
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id";
        query = query + " AND hh_master_coupon.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR hh_master_coupon.service_flag = 1 )";

        query = query + " GROUP BY hh_hotel_basic.id";
        query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_pv.total_uu_pv DESC, hh_hotel_basic.name_kana";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }
        Logging.info( "[SearchHotelCoupon.getHotelList] query=" + query );
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
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

            ret = true;
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchHotelCoupon.getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id FROM hh_hotel_coupon,hh_hotel_basic, hh_master_coupon WHERE";
        query = query + " hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.del_flag <> 1";
        if ( mobileFlag != false )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }
        if ( prefId > 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = ?";
        }

        // ホテナビのクーポンは取得しない
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id";
        query = query + " AND hh_master_coupon.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR hh_master_coupon.service_flag = 1 )";

        query = query + " GROUP BY hh_hotel_basic.id";

        count = 0;

        try
        {
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
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
                    m_hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelCoupon.getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * クーポン検索結果取得(会員判別)
     * 
     * @param hotelIdList ホテルIDリスト(null:全件検索)
     * @param available メンバーフラグ（0:制限なし、1:メンバーのみ, 2:ビジターのみ）
     * @param mobileFlag 携帯フラグ(true:携帯)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelList(int[] hotelIdList, int available, boolean mobileFlag, int countNum, int pageNum)
    {
        int i;
        int count;
        boolean ret;
        String query;
        String subQuery;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

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

        query = "SELECT " + subQuery + " FROM hh_hotel_coupon,hh_hotel_basic";
        query = query + " LEFT JOIN hh_hotel_status";
        query = query + " ON hh_hotel_basic.id = hh_hotel_status.id";
        query = query + ",hh_hotel_pv,hh_master_coupon WHERE";
        query = query + " hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.del_flag <> 1";

        if ( mobileFlag != false )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }

        if ( hotelIdList != null )
        {
            if ( hotelIdList.length > 0 )
            {
                query = query + " AND hh_hotel_basic.id IN(";
                for( i = 0 ; i < hotelIdList.length ; i++ )
                {
                    query = query + hotelIdList[i];
                    if ( i < hotelIdList.length - 1 )
                    {
                        query = query + ",";
                    }
                }
                query = query + ")";
            }
        }

        query = query + " AND ( hh_master_coupon.available = 0";
        if ( available > 0 )
        {
            query = query + " OR hh_master_coupon.available = ?";
        }
        query = query + " )";
        query = query + " AND hh_hotel_basic.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
        query = query + " AND hh_hotel_pv.collect_date = 0";
        // ホテナビのクーポンは取得しない
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id ";
        query = query + " AND hh_hotel_coupon.id = hh_master_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR hh_master_coupon.service_flag = 1 )";

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
            if ( available > 0 )
            {
                prestate.setInt( 1, available );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_hotelCount = result.getRow();
                    Logging.info( "[SearchHotelCoupon.getHotelList] Count=" + m_hotelCount );
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

            ret = true;
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchHotelCoupon.getHotelList] exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id FROM hh_hotel_coupon,hh_hotel_basic, hh_master_coupon,hh_hotel_pv WHERE";
        query = query + " hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.del_flag <> 1";
        if ( mobileFlag != false )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }
        if ( hotelIdList != null )
        {
            if ( hotelIdList.length > 0 )
            {
                query = query + " AND hh_hotel_basic.id IN(";
                for( i = 0 ; i < hotelIdList.length ; i++ )
                {
                    query = query + hotelIdList[i];
                    if ( i < hotelIdList.length - 1 )
                    {
                        query = query + ",";
                    }
                }
                query = query + ")";
            }
        }

        query = query + " AND ( hh_master_coupon.available = 0";
        if ( available > 0 )
        {
            query = query + " OR hh_master_coupon.available = ?";
        }
        query = query + " )";
        query = query + " AND hh_hotel_basic.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
        query = query + " AND hh_hotel_pv.collect_date = 0";

        // ホテナビのクーポンは取得しない
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id";
        query = query + " AND hh_master_coupon.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR hh_master_coupon.service_flag = 1 )";

        query = query + " GROUP BY hh_hotel_basic.id";

        count = 0;

        try
        {
            prestate = connection.prepareStatement( query );
            if ( available > 0 )
            {
                prestate.setInt( 1, available );
            }
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
                    m_hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelCoupon.getHotelList] exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * クーポン検索結果取得(会員判別)
     * 
     * @param prefId 都道府県ID(0:全件検索)
     * @param mobileFlag 携帯フラグ(true:携帯)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelListByPref(int prefId, int available, boolean mobileFlag, int countNum, int pageNum)
    {
        int i;
        int count;
        boolean ret;
        String query;
        String subQuery;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

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

        query = "SELECT " + subQuery + " FROM hh_hotel_coupon,hh_hotel_basic";
        query = query + " LEFT JOIN hh_hotel_status";
        query = query + " ON hh_hotel_basic.id = hh_hotel_status.id";
        query = query + " ,hh_hotel_pv,hh_master_coupon WHERE";
        query = query + " hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.del_flag <> 1";

        if ( mobileFlag != false )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }
        if ( prefId > 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = ?";
        }

        query = query + " AND ( hh_master_coupon.available = 0";
        if ( available > 0 )
        {
            query = query + " OR hh_master_coupon.available = ?";
        }
        query = query + " )";
        query = query + " AND hh_hotel_basic.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
        query = query + " AND hh_hotel_pv.collect_date = 0";
        // ホテナビクーポンは取得しない
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id ";
        query = query + " AND hh_hotel_coupon.id = hh_master_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR hh_master_coupon.service_flag = 1 )";

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
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
                if ( available > 0 )
                {
                    prestate.setInt( 2, available );
                }
            }
            else
            {
                if ( available > 0 )
                {
                    prestate.setInt( 1, available );
                }

            }
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

            ret = true;
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchHotelCoupon.getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id FROM hh_hotel_coupon,hh_hotel_basic,hh_hotel_pv,hh_master_coupon WHERE";
        query = query + " hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.del_flag <> 1";
        if ( mobileFlag != false )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }
        if ( prefId > 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = ?";
        }

        query = query + " AND ( hh_master_coupon.available = 0";
        if ( available > 0 )
        {
            query = query + " OR hh_master_coupon.available = ?";
        }
        query = query + " )";
        query = query + " AND hh_hotel_basic.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
        query = query + " AND hh_hotel_pv.collect_date = 0";

        // ホテナビのクーポンは取得しない
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id";
        query = query + " AND hh_master_coupon.id = hh_hotel_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR hh_master_coupon.service_flag = 1 )";

        query = query + " GROUP BY hh_hotel_basic.id";

        count = 0;

        try
        {
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
                if ( available > 0 )
                {
                    prestate.setInt( 2, available );
                }
            }
            else
            {
                if ( available > 0 )
                {
                    prestate.setInt( 1, available );
                }

            }
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
                    m_hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelCoupon.getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
