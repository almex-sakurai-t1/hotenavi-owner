/*
 * @(#)SearchHotelHotenavi.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテナビ加盟店検索ホテル取得クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSearchHotel_M2;

/**
 * ホテナビ加盟店検索ホテル取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/09/10
 * @version 1.1 2007/11/20
 */
public class SearchHotelHotenavi_M2 implements Serializable
{
    /**
     *
     */
    private static final long    serialVersionUID = -1608867817744034648L;

    private int                  m_hotelCount;
    private int                  m_hotelAllCount;
    private int[]                m_hotelIdList;
    private DataSearchHotel_M2[] m_hotelInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelHotenavi_M2()
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

    public DataSearchHotel_M2[] getHotelInfo()
    {
        return(m_hotelInfo);
    }

    public DataSearchHotel_M2[] getHotelList() throws Exception
    {
        int i;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataSearchHotel_M2 dataSearchHotel[] = null;

        query = " SELECT hh_hotel_basic.id,hh_hotel_basic.name,hh_hotel_basic.pref_id,hh_hotel_basic.pref_name,hh_hotel_basic.url, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_sort WHERE"
                + " hh_hotel_basic.hotenavi_id <> ''"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                + " AND hh_hotel_basic.url <> ''"
                + " AND hh_hotel_sort.collect_date = 0"
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY hh_hotel_basic.pref_id, Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        try
        {
            connection = DBConnection.getConnectionRO();
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
                dataSearchHotel = new DataSearchHotel_M2[m_hotelCount];
                result.beforeFirst();
                for( i = 0 ; result.next() ; i++ )
                {
                    dataSearchHotel[i] = new DataSearchHotel_M2();
                    dataSearchHotel[i].setId( result.getInt( "id" ) );
                    dataSearchHotel[i].setName( CheckString.checkStringForNull( result.getString( "name" ) ) );
                    dataSearchHotel[i].setPrefId( "" + result.getInt( "pref_id" ) );
                    dataSearchHotel[i].setPrefName( CheckString.checkStringForNull( result.getString( "pref_name" ) ) );
                    dataSearchHotel[i].setUrl( CheckString.checkStringForNull( result.getString( "url" ) ) );

                }
            }
            return(dataSearchHotel);
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchHotelHotenavi.getHotelList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            dataSearchHotel = null;
        }
    }

    /**
     * ホテナビ加盟店検索結果取得(ホテルランク順)
     * 
     * @param prefId 都道府県ID
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int[] getHotelIdList(int prefId) throws Exception
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_sort WHERE"
                + " hh_hotel_basic.hotenavi_id <> ''"
                + " AND hh_hotel_basic.pref_id = ?"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                + " AND hh_hotel_basic.url <> ''"
                + " AND hh_hotel_sort.collect_date = 0"
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        count = 0;

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefId );
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
            Logging.error( "[SearchHotelHotenavi.getHotelList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(m_hotelIdList);
    }

    /**
     * Get Pref ID Array on the basis of localID and returns hotel count corresponding PrefID
     * 
     * @param pref_ID
     * @return hotelCount int array list
     * @throws Exception
     */
    public int[] getHotelCountMobile(int[] pref_ID) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int[] hotelCount = new int[pref_ID.length];

        // ホテル総件数の取得
        query = "select HB.pref_id, count(*) as count_id from hh_hotel_basic HB,hh_hotel_sort HP "
                + "where HB.pref_id in ( ";
        for( int i = 0 ; i < pref_ID.length ; i++ )
        {
            if ( i == 0 )
                query = query + "?";
            else
                query = query + ", " + "?";
        }
        query = query + " ) "
                + "AND HP.collect_date = 0 "
                + "AND HB.hotenavi_id <> '' "
                + "AND HB.url <> '' "
                + "AND HB.kind <= 7 "
                + "AND HB.id = HP.id "
                + "group by HB.pref_id";

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );

            for( int i = 0 ; i < pref_ID.length ; i++ )
            {
                prestate.setInt( (i + 1), pref_ID[i] );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                result.first();
                for( int count = 0 ; count < pref_ID.length ; count++ )
                {
                    // 該当するpref_idのホテル件数データがない場合は0をセット
                    if ( result.getInt( "pref_id" ) != pref_ID[count] )
                    {
                        hotelCount[count] = 0;
                    }
                    else
                    {
                        hotelCount[count] = result.getInt( "count_id" );
                        if ( result.next() == false )
                        {
                            break;
                        }
                    }
                }
            }
            return hotelCount;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelHotenavi.getHotelCountMobile] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            hotelCount = null;
        }
    }

}
