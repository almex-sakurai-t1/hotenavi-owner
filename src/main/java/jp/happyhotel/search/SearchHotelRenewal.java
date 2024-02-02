/*
 * @(#)SearchHotelNewOpen.java 1.00 2007/09/21 Copyright (C) ALMEX Inc. 2007 リニューアルホテル検索クラス
 */

package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DbAccess;
import jp.happyhotel.common.LogLib;
import jp.happyhotel.data.DataHotelBasic;

/**
 * リニューアルホテル検索クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2007/09/21
 */
public class SearchHotelRenewal implements Serializable
{

    /**
	 *
	 */
    private static final long serialVersionUID = 8942267209385412830L;

    private int               m_hotelCount;
    private int               m_hotelAllCount;
    private int[]             m_hotelIdList;
    private DataHotelBasic[]  m_hotelInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelRenewal()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;
        m_hotelIdList = new int[0];
        m_hotelInfo = new DataHotelBasic[0];
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

    public boolean getHotelList(int renewalDate, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        String subQuery;
        LogLib log = new LogLib();
        DbAccess db;
        ResultSet result;
        PreparedStatement prestate;

        subQuery = " id, rank, kind, hotenavi_id, name, name_kana, name_mobile, zip_code, jis_code, pref_id," +
                " pref_name, pref_kana, address1, address2, address3, address_all, tel1, tel2, fax," +
                " charge_last, charge_first, charge_kana_last, charge_kana_first, charge_tel, charge_mail," +
                " open_date, renewal_date, url, url_official1, url_official2, url_official_mobile, pr," +
                " pr_detail, pr_event, pr_member, access, access_station, access_ic, room_count, parking," +
                " parking_count, type_building, type_kodate, type_rentou, type_etc, location_station, " +
                " location_ic, location_kougai, benefit, roomservice, pay_front, pay_auto, credit, " +
                " credit_visa, credit_master, credit_jcb, credit_dc, credit_nicos, credit_amex, credit_etc," +
                " halfway, coupon, possible_one, possible_three, reserve, reserve_tel, reserve_mail," +
                " reserve_web, empty_method, hotel_lat, hotel_lon, hotel_lat_num, hotel_lon_num, disp_lat," +
                " disp_lon, zoom, over18_flag, company_type, empty_kind, empty_status, group_code," +
                " last_update, last_uptime, pr_room, pr_etc, renewal_flag, url_special, hotel_lat_jp," +
                " hotel_lon_jp, map_code, high_roof, high_roof_count, empty_hotenavi_id, attention_flag," +
                " ad_pref_id, renewal_date_text, new_open_search_flag";

        query = "SELECT " + getNewColumnString(subQuery) + " FROM hh_hotel_basic";
        query = query + " LEFT JOIN hh_hotel_status";
        query = query + " ON hh_hotel_basic.id = hh_hotel_status.id";
        query = query + " WHERE open_date != renewal_date ";
        query = query + " AND renewal_date >= ?";
        query = query + " AND renewal_date <= ?";
        query = query + " ORDER BY renewal_date DESC, hh_hotel_basic.name_kana";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        m_hotelCount = 0;
        m_hotelAllCount = 0;

        db = new DbAccess();
        prestate = db.createPrepared( query );

        try
        {
            prestate.setInt( 1, renewalDate - 10000 );
            prestate.setInt( 2, renewalDate );
            result = db.execQuery( prestate );
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
            result.close();
            prestate.close();
        }
        catch ( Exception e )
        {
            log.error( "[SearchHotelNewOpen.getHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            db.close();
        }

        // ホテル総件数の取得
        query = "SELECT id FROM hh_hotel_basic WHERE open_date != renewal_date ";
        query = query + " AND renewal_date >= ?";
        query = query + " AND renewal_date <= ?";
        query = query + " ORDER BY renewal_date DESC, hh_hotel_basic.name_kana";

        count = 0;
        db = new DbAccess();
        prestate = db.createPrepared( query );

        try
        {
            prestate.setInt( 1, renewalDate - 10000 );
            prestate.setInt( 2, renewalDate );
            result = db.execQuery( prestate );
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
            result.close();
            prestate.close();
        }
        catch ( Exception e )
        {
            log.error( "[SearchHotelNewOpen.getHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            db.close();
        }

        return(true);
    }
    
    private String getNewColumnString(String colString) {
        StringBuilder newColumnSb = new StringBuilder();
        String[] colArr = colString.split(",");
        String col;
        for (int i = 0; i < colArr.length; i++) {
            newColumnSb.append(" ");
            if (i > 0) {
                newColumnSb.append(", ");
            }
            col = colArr[i].trim().toLowerCase();
            if ("empty_kind".equals(col)) {
                newColumnSb.append("IFNULL(hh_hotel_status.`mode`, 0)");
            } else if ("empty_status".equals(col)) {
                newColumnSb.append("IFNULL(hh_hotel_status.").append(col).append(", 0)");
            } else {
                newColumnSb.append("hh_hotel_basic.").append(col);
            }
            newColumnSb.append(" ").append(col);
        }
        return newColumnSb.toString();
    }

}
