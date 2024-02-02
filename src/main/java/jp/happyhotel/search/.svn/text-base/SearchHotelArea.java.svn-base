/*
 * @(#)SearchHotelArea.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテル街別ホテル取得クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataMasterArea;

/**
 * ホテル街別ホテル取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class SearchHotelArea implements Serializable
{
    private static final long serialVersionUID = -2730059667048308999L;

    private int               m_hotelCount;
    private int               m_hotelAllCount;
    private DataHotelBasic[]  m_hotelInfo;
    private DataMasterArea    m_areaInfo;
    private int[]             hotelIdList;

    /**
     * データを初期化します。
     */
    public SearchHotelArea()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;
        hotelIdList = new int[0];
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

    /** ホテル街情報取得 **/
    public DataMasterArea getAreaInfo()
    {
        return(m_areaInfo);
    }

    public int[] getHotelIdList()
    {
        return hotelIdList;
    }

    /**
     * ホテル一覧情報取得(ホテルランク順)
     * 
     * @param areaId ホテル街コード
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelList(int areaId, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result;
        PreparedStatement prestate;

        query = "SELECT";
        query = query + " hh_hotel_basic.id";
        query = query + ", hh_hotel_basic.rank";
        query = query + ", hh_hotel_basic.kind";
        query = query + ", hh_hotel_basic.hotenavi_id";
        query = query + ", hh_hotel_basic.name";
        query = query + ", hh_hotel_basic.name_kana";
        query = query + ", hh_hotel_basic.name_mobile";
        query = query + ", hh_hotel_basic.zip_code";
        query = query + ", hh_hotel_basic.jis_code";
        query = query + ", hh_hotel_basic.pref_id";
        query = query + ", hh_hotel_basic.pref_name";
        query = query + ", hh_hotel_basic.pref_kana";
        query = query + ", hh_hotel_basic.address1";
        query = query + ", hh_hotel_basic.address2";
        query = query + ", hh_hotel_basic.address3";
        query = query + ", hh_hotel_basic.address_all";
        query = query + ", hh_hotel_basic.tel1";
        query = query + ", hh_hotel_basic.tel2";
        query = query + ", hh_hotel_basic.fax";
        query = query + ", hh_hotel_basic.charge_last";
        query = query + ", hh_hotel_basic.charge_first";
        query = query + ", hh_hotel_basic.charge_kana_last";
        query = query + ", hh_hotel_basic.charge_kana_first";
        query = query + ", hh_hotel_basic.charge_tel";
        query = query + ", hh_hotel_basic.charge_mail";
        query = query + ", hh_hotel_basic.open_date";
        query = query + ", hh_hotel_basic.renewal_date";
        query = query + ", hh_hotel_basic.url";
        query = query + ", hh_hotel_basic.url_official1";
        query = query + ", hh_hotel_basic.url_official2";
        query = query + ", hh_hotel_basic.url_official_mobile";
        query = query + ", hh_hotel_basic.pr";
        query = query + ", hh_hotel_basic.pr_detail";
        query = query + ", hh_hotel_basic.pr_event";
        query = query + ", hh_hotel_basic.pr_member";
        query = query + ", hh_hotel_basic.access";
        query = query + ", hh_hotel_basic.access_station";
        query = query + ", hh_hotel_basic.access_ic";
        query = query + ", hh_hotel_basic.room_count";
        query = query + ", hh_hotel_basic.parking";
        query = query + ", hh_hotel_basic.parking_count";
        query = query + ", hh_hotel_basic.type_building";
        query = query + ", hh_hotel_basic.type_kodate";
        query = query + ", hh_hotel_basic.type_rentou";
        query = query + ", hh_hotel_basic.type_etc";
        query = query + ", hh_hotel_basic.location_station";
        query = query + ", hh_hotel_basic.location_ic";
        query = query + ", hh_hotel_basic.location_kougai";
        query = query + ", hh_hotel_basic.benefit";
        query = query + ", hh_hotel_basic.roomservice";
        query = query + ", hh_hotel_basic.pay_front";
        query = query + ", hh_hotel_basic.pay_auto";
        query = query + ", hh_hotel_basic.credit";
        query = query + ", hh_hotel_basic.credit_visa";
        query = query + ", hh_hotel_basic.credit_master";
        query = query + ", hh_hotel_basic.credit_jcb";
        query = query + ", hh_hotel_basic.credit_dc";
        query = query + ", hh_hotel_basic.credit_nicos";
        query = query + ", hh_hotel_basic.credit_amex";
        query = query + ", hh_hotel_basic.credit_etc";
        query = query + ", hh_hotel_basic.halfway";
        query = query + ", hh_hotel_basic.coupon";
        query = query + ", hh_hotel_basic.possible_one";
        query = query + ", hh_hotel_basic.possible_three";
        query = query + ", hh_hotel_basic.reserve";
        query = query + ", hh_hotel_basic.reserve_tel";
        query = query + ", hh_hotel_basic.reserve_mail";
        query = query + ", hh_hotel_basic.reserve_web";
        query = query + ", hh_hotel_basic.empty_method";
        query = query + ", hh_hotel_basic.hotel_lat";
        query = query + ", hh_hotel_basic.hotel_lon";
        query = query + ", hh_hotel_basic.hotel_lat_num";
        query = query + ", hh_hotel_basic.hotel_lon_num";
        query = query + ", hh_hotel_basic.disp_lat";
        query = query + ", hh_hotel_basic.disp_lon";
        query = query + ", hh_hotel_basic.zoom";
        query = query + ", hh_hotel_basic.over18_flag";
        query = query + ", hh_hotel_basic.company_type";
        query = query + ", hh_hotel_basic.last_update";
        query = query + ", hh_hotel_basic.last_uptime";
        query = query + ", IFNULL(hh_hotel_status.`mode`, 0) empty_kind";
        query = query + ", IFNULL(hh_hotel_status.empty_status, 0) empty_status";
        query = query + ", hh_hotel_basic.group_code";
        query = query + ", hh_hotel_basic.hotel_picture_pc";
        query = query + ", hh_hotel_basic.hotel_picture_gif";
        query = query + ", hh_hotel_basic.hotel_picture_png";
        query = query + ", hh_hotel_basic.pr_room";
        query = query + ", hh_hotel_basic.pr_etc";
        query = query + ", hh_hotel_basic.renewal_flag";
        query = query + ", hh_hotel_basic.url_special";
        query = query + ", hh_hotel_basic.hotel_lat_jp";
        query = query + ", hh_hotel_basic.hotel_lon_jp";
        query = query + ", hh_hotel_basic.map_code";
        query = query + ", hh_hotel_basic.high_roof";
        query = query + ", hh_hotel_basic.high_roof_count";
        query = query + ", hh_hotel_basic.empty_hotenavi_id";
        query = query + ", hh_hotel_basic.attention_flag";
        query = query + ", hh_hotel_basic.ad_pref_id";
        query = query + ", hh_hotel_basic.renewal_date_text";
        query = query + ", hh_hotel_basic.new_open_search_flag";
        query = query + ", hh_hotel_basic.url_yahoo";
        query = query + ", hh_hotel_basic.touch_equip_flag";
        query = query + ", hh_hotel_basic.noshow_hotelid";
        query = query + " FROM hh_hotel_basic";
        query = query + " LEFT JOIN hh_hotel_status";
        query = query + " ON hh_hotel_basic.id = hh_hotel_status.id";
        query = query + " ,hh_hotel_area,hh_hotel_pv";
        query = query + " WHERE hh_hotel_area.area_id = ?";
        query = query + " AND hh_hotel_basic.id = hh_hotel_area.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.pref_id <> 0";
        query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
        query = query + " AND hh_hotel_pv.collect_date = 0";
        query = query + " GROUP BY hh_hotel_basic.id";
        query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_pv.total_uu_pv DESC, hh_hotel_basic.name_kana";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }
        count = 0;
        m_hotelCount = 0;
        m_hotelAllCount = 0;
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
        }
        catch ( Exception e )
        {
            Logging.error( "Exception in getHotelList =" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }
        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id FROM hh_hotel_basic,hh_hotel_area WHERE hh_hotel_area.area_id = ?";
        query = query + " AND hh_hotel_basic.id = hh_hotel_area.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.pref_id <> 0";

        count = 0;
        try
        {
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
                hotelIdList = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "Exception in getHotelList =" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        // ホテル街の取得
        this.m_areaInfo = new DataMasterArea();
        this.m_areaInfo.getData( areaId );
        return(true);
    }

}
