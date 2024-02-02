package jp.happyhotel.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

// import jp.happyhotel.data.DataHotelBasic;

public class SearchHotelPrice_M2
{

    private static final long serialVersionUID = 4146033710422271672L;

    // private int m_hotelCount;
    private int               m_hotelAllCount;
    private int[]             m_hotelIdList;

    // private DataHotelBasic[] m_hotelInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelPrice_M2()
    {
        // m_hotelCount = 0;
        m_hotelAllCount = 0;
    }

    // public int getCount( ) { return( m_hotelCount ); }
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    // public DataHotelBasic[] getHotelInfo( ) { return( m_hotelInfo ); }

    /**
     * 料金検索結果取得(ホテルランク順)
     * 
     * @param hotelIdList ホテルIDリスト(null:全件検索)
     * @param priceKind 料金区分
     * @param startPrice 最低料金
     * @param endPrice 最高料金
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelIdList(int[] hotelIdList, int[] priceKind, int startPrice, int endPrice, int countNum, int pageNum) throws Exception
    {
        int i;
        int count;
        boolean boolAllDay = false;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( priceKind == null )
        {
            return(false);
        }

        m_hotelAllCount = 0;

        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_price,hh_hotel_basic,hh_hotel_sort WHERE"
                + " hh_hotel_price.price_from <= " + endPrice
                + " AND hh_hotel_price.price_to >= " + startPrice
                + " AND hh_hotel_price.start_date <= " + DateEdit.getDate( 2 )
                + " AND hh_hotel_price.end_date >= " + DateEdit.getDate( 2 )
                + " AND hh_hotel_price.data_flag = 1"
                + " AND (";
        for( i = 0 ; i < priceKind.length ; i++ )
        {
            query = query + " hh_hotel_price.data_type = " + priceKind[i];
            if ( priceKind[i] == 3 || priceKind[i] == 4 )
            {
                boolAllDay = true;
            }

            if ( i < priceKind.length - 1 )
            {
                query = query + " OR ";
            }
        }
        if ( boolAllDay != false )
        {
            query = query + " OR hh_hotel_price.week LIKE '%全日%'";
        }
        query = query + " )";

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

        query = query + " AND hh_hotel_basic.id = hh_hotel_price.id"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                + " AND hh_hotel_sort.collect_date = 0"
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

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

                m_hotelIdList = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelPrice_M2.getHotelList(" + hotelIdList + "," + priceKind + "," + startPrice + "," + endPrice + "," + countNum + "," + pageNum + ")] Exception=" + e.toString() );
            ret = false;
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 料金検索結果取得(ホテルランク順)
     * 
     * @param hotelIdList ホテルIDリスト(null:全件検索)
     * @param priceKind 料金区分
     * @param startTime 適用開始時刻(HHMM)
     * @param endTime 適用終了時刻(HHMM)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelIdListByTime(int[] hotelIdList, int[] priceKind, int startTime, int endTime, int countNum, int pageNum) throws Exception
    {
        int i;
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( priceKind == null )
        {
            return(false);
        }

        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_price,hh_hotel_basic WHERE";
        if ( startTime == 0 && endTime == 2400 )
        {
            query = query + " hh_hotel_price.time_from = " + startTime
                    + " AND hh_hotel_price.time_to = " + endTime;
        }
        else
        {
            query = query + " hh_hotel_price.time_from <= " + endTime
                    + " AND hh_hotel_price.time_to >= " + startTime;
        }
        query = query + " AND hh_hotel_price.start_date <= " + DateEdit.getDate( 2 )
                + " AND hh_hotel_price.end_date >= " + DateEdit.getDate( 2 )
                + " AND hh_hotel_price.data_flag = 1"
                + " AND (";

        for( i = 0 ; i < priceKind.length ; i++ )
        {
            query = query + " hh_hotel_price.data_type = " + priceKind[i];

            if ( i < priceKind.length - 1 )
            {
                query = query + " OR ";
            }
        }

        query = query + " )";

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

        query = query + " AND hh_hotel_basic.id = hh_hotel_price.id"
                + " AND hh_hotel_basic.kind <= 7"
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY Ranking DESC, hh_hotel_basic.name_kana";

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

                m_hotelIdList = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelPrice_M2.getHotelIdListByTime(" + hotelIdList + "," + priceKind + "," + startTime + "," + endTime + "," + countNum + "," + pageNum + ")] Exception=" + e.toString() );
            ret = false;
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
