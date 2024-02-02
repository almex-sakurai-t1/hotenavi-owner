package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelNewhappie;

/**
 * 新規オープンホテル検索クラス
 * 
 * @author N.Ide
 * @version 1.00 2011/05/26
 */
public class SearchHotelNewHappie implements Serializable
{
    private static final long    serialVersionUID = -5437231184752741778L;

    private int                  m_hotelCount;
    private int                  m_hotelAllCount;
    private int[]                m_hotelIdList;
    private DataHotelNewhappie[] m_hotelNewhappie;

    /**
     * データを初期化します。
     */
    public SearchHotelNewHappie()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;
        m_hotelIdList = new int[0];
        m_hotelNewhappie = new DataHotelNewhappie[0];
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

    public DataHotelNewhappie[] getHotelnewhappie()
    {
        return(m_hotelNewhappie);
    }

    /**
     * ホテル情報取得
     * 
     * @param flag 取得区分（0:区別なし,1:稼働予定のみ,2:稼働済のみ）
     * @param countNum ホテル件数
     * @param pageNum ページ数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelList(int flag, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_newhappie";
        if ( flag == 0 )
        {
            query = query + " WHERE date_due > 0 OR date_start > 0";
        }
        else if ( flag == 1 )
        {
            query = query + " WHERE date_due > 0 AND date_start = 0";
        }
        else if ( flag == 2 )
        {
            query = query + " WHERE date_due = 0 AND date_start > 0";
        }
        query = query + " ORDER BY date_due, date_start DESC, RAND()";
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
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_hotelCount = result.getRow();
                }
                // クラスの配列を用意し、初期化する。
                this.m_hotelNewhappie = new DataHotelNewhappie[this.m_hotelCount];
                for( i = 0 ; i < m_hotelCount ; i++ )
                {
                    m_hotelNewhappie[i] = new DataHotelNewhappie();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelNewhappie[count++].setData( result );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelNewHappie.getHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT id FROM hh_hotel_newhappie";
        if ( flag == 0 )
        {
            query = query + " WHERE date_due > 0 OR date_start > 0";
        }
        else if ( flag == 1 )
        {
            query = query + " WHERE date_due > 0 AND date_start = 0";
        }
        else if ( flag == 2 )
        {
            query = query + " WHERE date_due = 0 AND date_start > 0";
        }
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
                    m_hotelIdList[count++] = result.getInt( "hh_hotel_newhappie.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelNewHappie.getHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * ホテル情報取得
     * 
     * @param prefId 都道府県ID（0:全国）
     * @param flag 取得区分（0:区別なし,1:稼働予定のみ,2:稼働済のみ）
     * @param countNum ホテル件数
     * @param pageNum ページ数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelListByPref(int prefId, int flag, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_newhappie.* ";
        query = query + " FROM hh_hotel_newhappie, hh_hotel_basic";
        query = query + " WHERE hh_hotel_newhappie.id = hh_hotel_basic.id";
        if ( flag == 0 )
        {
            query = query + " AND (date_due > 0 OR date_start > 0)";
        }
        else if ( flag == 1 )
        {
            query = query + " AND date_due > 0 AND date_start = 0";
        }
        else if ( flag == 2 )
        {
            query = query + " AND date_due = 0 AND date_start > 0";
        }
        if ( prefId > 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = ?";
        }
        query = query + " GROUP BY ( hh_hotel_newhappie.id )";
        query = query + " ORDER BY date_due, date_start DESC, RAND()";
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
                this.m_hotelNewhappie = new DataHotelNewhappie[this.m_hotelCount];
                for( i = 0 ; i < m_hotelCount ; i++ )
                {
                    m_hotelNewhappie[i] = new DataHotelNewhappie();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelNewhappie[count++].setData( result );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelNewHappie.getHotelListByPref] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT hh_hotel_newhappie.id FROM hh_hotel_newhappie, hh_hotel_basic";
        query = query + " WHERE hh_hotel_newhappie.id = hh_hotel_basic.id";
        if ( flag == 0 )
        {
            query = query + " AND (date_due > 0 OR date_start > 0)";
        }
        else if ( flag == 1 )
        {
            query = query + " AND date_due > 0 AND date_start = 0";
        }
        else if ( flag == 2 )
        {
            query = query + " AND date_due = 0 AND date_start > 0";
        }
        if ( prefId > 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = ?";
        }
        query = query + " GROUP BY ( hh_hotel_newhappie.id )";
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
                    m_hotelIdList[count++] = result.getInt( "hh_hotel_newhappie.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelNewHappie.getHotelListByPref] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * ホテル情報取得
     * 
     * @param prefId 都道府県ID（0:全国）
     * @param flag 取得区分（0:区別なし,1:稼働予定のみ,2:稼働済のみ）
     * @param countNum ホテル件数
     * @param pageNum ページ数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRsvHotelListByPref(int prefId, int flag, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = " SELECT HN.id,HN.date_due,HN.date_due_text,HN.date_start,RB.rsv_date_due,RB.rsv_date_due_text,RB.rsv_date_start,HN.bko_date_start" +
                " , (CASE WHEN HB.rank=3 THEN 2 ELSE HB.rank END) AS Ranking " +
                " FROM hh_hotel_newhappie HN" +
                " INNER JOIN hh_hotel_basic HB ON ( HN.id = HB.id ) " +
                " INNER JOIN newRsvDB.hh_rsv_reserve_basic RB ON ( HN.id = RB.id )" +
                " INNER JOIN hh_hotel_sort HS ON ( HN.id = HS.id )" +
                " WHERE RB.sales_flag = 1" +
                "   AND HS.collect_date = 0";
        if ( flag == 0 )
        {
            query += " AND (RB.rsv_date_due > 0 OR RB.rsv_date_start > 0)";
        }
        else if ( flag == 1 )
        {
            query += " AND RB.rsv_date_due > 0 AND RB.rsv_date_start = 0";
        }
        else if ( flag == 2 )
        {
            query += " AND RB.rsv_date_due = 0 AND RB.rsv_date_start > 0";
        }
        if ( prefId > 0 )
        {
            query += " AND HB.pref_id = ?";
        }
        query += " GROUP BY ( HN.id )";
        query += " ORDER BY Ranking DESC, HS.all_point DESC, HB.name_kana,HB.id";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }
        Logging.info( "[SearchHotelNewHappie.getRsvHotelListByPref]" + query );

        count = 0;
        m_hotelCount = 0;
        m_hotelAllCount = 0;
        try
        {
            connection = DBConnection.getConnectionRO();
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
                this.m_hotelNewhappie = new DataHotelNewhappie[this.m_hotelCount];
                for( i = 0 ; i < m_hotelCount ; i++ )
                {
                    m_hotelNewhappie[i] = new DataHotelNewhappie();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelNewhappie[count++].setData( result );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelNewHappie.getRsvHotelListByPref] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT HN.*" +
                " FROM hh_hotel_newhappie HN" +
                " INNER JOIN hh_hotel_basic HB ON ( HN.id = HB.id ) " +
                " INNER JOIN newRsvDB.hh_rsv_reserve_basic RB ON ( HN.id = RB.id )" +
                " WHERE RB.sales_flag = 1";
        if ( flag == 0 )
        {
            query += " AND (RB.rsv_date_due > 0 OR RB.rsv_date_start > 0)";
        }
        else if ( flag == 1 )
        {
            query += " AND RB.rsv_date_due > 0 AND RB.rsv_date_start = 0";
        }
        else if ( flag == 2 )
        {
            query += " AND RB.rsv_date_due = 0 AND RB.rsv_date_start > 0";
        }
        if ( prefId > 0 )
        {
            query += " AND HB.pref_id = ?";
        }
        query += " GROUP BY ( HN.id )";
        count = 0;
        Logging.info( "[SearchHotelNewHappie.getRsvHotelListByPref]" + query );

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
                    m_hotelIdList[count++] = result.getInt( "HN.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelNewHappie.getRsvHotelListByPref] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }
}
