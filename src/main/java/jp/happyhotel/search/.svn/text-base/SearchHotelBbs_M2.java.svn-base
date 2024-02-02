/*
 * @(#)SearchHotelBbs.java 1.00 2007/09/21 Copyright (C) ALMEX Inc. 2007 クチコミ検索クラス
 */

package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;

/**
 * クチコミ検索クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/04/15
 */
public class SearchHotelBbs_M2 implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 8942167209335412830L;

    private int               m_hotelCount;
    private int               m_hotelAllCount;
    private int[]             m_hotelIdList;
    private DataHotelBasic[]  m_hotelInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelBbs_M2()
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

    /**
     * ホテル情報取得（掲載ＯＫのクチコミのみ）
     * 
     * @param hotelIdList ホテルIDリスト(null:全件検索)
     * @param bbsCount クチコミ件数
     * @param point クチコミ評価(0: こだわらない)
     * @param cleannessPt きれいさ(0: こだわらない)
     * @param widthPt 広さ(0: こだわらない)
     * @param servicePt サービス(0: こだわらない)
     * @param equipPt 設備(0: こだわらない)
     * @param cost 価格満足度(0:こだわらない)
     * @param countNum ホテル件数
     * @param pageNum ページ数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @see 取得順は、クチコミの評価順となる。※ランク･PV順に並び替える場合は、SearchHotelCommonでマージをする
     */
    public boolean getHotelList(int[] hotelIdList, int bbsCount, int point, int cleannessPt, int widthPt,
            int servicePt, int equipPt, int costPt, int countNum, int pageNum)
    {
        boolean ret;
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;
        if ( hotelIdList != null )
        {
            if ( hotelIdList.length == 0 )
            {
                return(true);
            }
        }

        query = "SELECT AVG(hh_hotel_bbs.point) as average, hh_hotel_basic.id";
        query = query + " FROM hh_hotel_bbs, hh_hotel_basic";
        query = query + " WHERE hh_hotel_bbs.id = hh_hotel_basic.id";
        query = query + " AND hh_hotel_basic.rank > 1";

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

        query = query + " AND  hh_hotel_bbs.kind_flag= 0";
        query = query + " AND  hh_hotel_bbs.thread_status between 1 AND 2";
        query = query + " GROUP BY ( hh_hotel_bbs.id ) HAVING COUNT(hh_hotel_bbs.id) >= " + bbsCount;
        if ( point > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.point) >= " + point;
        }
        if ( cleannessPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.cleanness_point) >= " + cleannessPt;
        }
        if ( widthPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.width_point) >= " + widthPt;
        }
        if ( servicePt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.service_point) >= " + servicePt;
        }
        if ( equipPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.equip_point) >= " + equipPt;
        }
        if ( costPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.cost_point) >= " + costPt;
        }
        query = query + " ORDER BY average DESC, hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }
        count = 0;
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

                    count++;
                }
            }

            ret = true;
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT AVG(hh_hotel_bbs.point) as average, hh_hotel_basic.id";
        query = query + " FROM hh_hotel_bbs, hh_hotel_basic";
        query = query + " WHERE hh_hotel_bbs.id = hh_hotel_basic.id";
        query = query + " AND hh_hotel_basic.rank > 1";

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

        query = query + " AND  hh_hotel_bbs.kind_flag= 0";
        query = query + " AND  hh_hotel_bbs.thread_status between 1 and 2";
        query = query + " GROUP BY ( hh_hotel_bbs.id ) HAVING COUNT(hh_hotel_bbs.id) >= " + bbsCount;
        if ( point > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.point) >=" + point;
        }
        if ( cleannessPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.cleanness_point) >=" + cleannessPt;
        }
        if ( widthPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.width_point) >=" + widthPt;
        }
        if ( servicePt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.service_point) >=" + servicePt;
        }
        if ( equipPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.equip_point) >=" + equipPt;
        }
        if ( costPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.cost_point) >=" + costPt;
        }
        query = query + " ORDER BY average DESC, hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

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
                    m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルID取得（掲載ＯＫのクチコミのみ）
     * 
     * @param hotelIdList ホテルIDリスト(null:全件検索)
     * @param bbsCount クチコミ件数
     * @param point クチコミ評価(0: こだわらない)
     * @param cleannessPt きれいさ(0: こだわらない)
     * @param widthPt 広さ(0: こだわらない)
     * @param servicePt サービス(0: こだわらない)
     * @param equipPt 設備(0: こだわらない)
     * @param cost 価格満足度(0:こだわらない)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @see 取得順は、クチコミの評価順となる。※ランク･PV順に並び替える場合は、SearchHotelCommonでマージをする<br>
     *      ホテルIDリストのみを取得しhh_hotel_basicのデータは取得しない
     */
    public boolean getHotelIdListOnly(int[] hotelIdList, int bbsCount, int point, int cleannessPt, int widthPt,
            int servicePt, int equipPt, int costPt, int prefId)
    {
        boolean ret;
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;
        if ( hotelIdList != null )
        {
            if ( hotelIdList.length == 0 )
            {
                return(true);
            }
        }

        count = 0;

        // ホテル総件数の取得
        query = "SELECT AVG(hh_hotel_bbs.point) as average, hh_hotel_basic.id";
        query = query + " FROM hh_hotel_bbs, hh_hotel_basic";
        query = query + " WHERE hh_hotel_bbs.id = hh_hotel_basic.id";
        query = query + " AND hh_hotel_basic.pref_id = " + prefId;
        query = query + " AND hh_hotel_basic.rank > 1";

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
                        if ( hotelIdList[i + 1] == 0 )
                        {
                            break;
                        }
                        else
                        {
                            query = query + ",";
                        }
                    }
                }
                query = query + ")";
            }
        }

        query = query + " AND  hh_hotel_bbs.kind_flag= 0";
        query = query + " AND  hh_hotel_bbs.thread_status between 1 and 2";
        query = query + " GROUP BY ( hh_hotel_bbs.id ) HAVING COUNT(hh_hotel_bbs.id) >= " + bbsCount;
        if ( point > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.point) >=" + point;
        }
        if ( cleannessPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.cleanness_point) >=" + cleannessPt;
        }
        if ( widthPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.width_point) >=" + widthPt;
        }
        if ( servicePt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.service_point) >=" + servicePt;
        }
        if ( equipPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.equip_point) >=" + equipPt;
        }
        if ( costPt > 0 )
        {
            query = query + " AND AVG(hh_hotel_bbs.cost_point) >=" + costPt;
        }

        count = 0;

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
                    // 総件数の取得
                    this.m_hotelAllCount = result.getRow();
                }

                this.m_hotelIdList = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( this.m_hotelAllCount > 0 )
        {
            ret = true;
        }

        return(ret);
    }
}
