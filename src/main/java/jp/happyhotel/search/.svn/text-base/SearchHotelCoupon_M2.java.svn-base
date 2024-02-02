package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterCoupon_M2;

/**
 * クーポン検索ホテル取得クラス
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/11
 */
public class SearchHotelCoupon_M2 implements Serializable
{
    private static final long serialVersionUID = 5377516176123625349L;

    private int               m_hotelAllCount;
    private int               m_prefCount;
    private int[]             m_hotelIdList;

    /**
     * データを初期化します。
     */
    public SearchHotelCoupon_M2()
    {
        m_hotelAllCount = 0;
        m_prefCount = 0;
        m_hotelIdList = null;
    }

    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int getPrefCount()
    {
        return(m_prefCount);
    }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
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
    public int[] getHotelIdList(String local_id, int available, boolean mobileFlag) throws Exception
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_coupon,hh_hotel_basic, hh_master_coupon,hh_hotel_sort, hh_master_pref WHERE"
                + " hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 )
                + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 )
                + " AND hh_hotel_coupon.del_flag <> 1";
        if ( mobileFlag != false )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }

        query = query + " AND ( hh_master_coupon.available = 0";
        if ( available > 0 )
        {
            query = query + " OR hh_master_coupon.available = ?";
        }
        query = query + " )"
                + " AND hh_hotel_basic.id = hh_hotel_coupon.id"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                + " AND hh_hotel_sort.collect_date = 0"
                + " AND hh_hotel_basic.pref_id = hh_master_pref.pref_id"
                + " AND hh_master_pref.local_id = ?"
                + " AND hh_hotel_basic.id = hh_master_coupon.id "
                + " AND hh_hotel_coupon.id = hh_master_coupon.id"
                + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no"
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        count = 0;

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            if ( available > 0 )
            {
                prestate.setInt( 1, available );
                prestate.setInt( 2, Integer.parseInt( local_id ) );
            }
            else
            {
                prestate.setInt( 1, Integer.parseInt( local_id ) );
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
            Logging.error( "[SearchHotelCoupon_M2.getHotelList(" + local_id + "," + available + "," + mobileFlag + ")] exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(m_hotelIdList);
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
    public int[] getHotelIdListByPref(int prefId, int available, boolean mobileFlag)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_coupon,hh_hotel_basic,hh_hotel_sort,hh_master_coupon WHERE";
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
        query = query + " AND hh_hotel_basic.id = hh_hotel_sort.id";
        query = query + " AND hh_hotel_sort.collect_date = 0";
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id ";
        query = query + " AND hh_hotel_coupon.id = hh_master_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " GROUP BY hh_hotel_basic.id";
        query = query + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        count = 0;

        try
        {
            connection = DBConnection.getConnectionRO();
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
            Logging.error( "[SearchHotelCoupon.getHotelList] exception=" + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(m_hotelIdList);
    }

    /**
     * This function will set Hotel Ids by prefectures
     * 
     * @param hotelIds
     * @param localId
     * @return Coupon Information by prefecture
     * @throws Exception
     */

    public DataMasterCoupon_M2[] getHotelListByPref(int[] hotelIds, int localId) throws Exception
    {

        int i;
        String query = null;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        DataMasterCoupon_M2 dataMasterCoupon[] = new DataMasterCoupon_M2[8];
        query = "SELECT hh_master_pref.pref_id,hh_master_pref.name,count(hh_hotel_basic.id) as count,hh_master_local.name as local_name FROM hh_master_pref";
        query = query + " LEFT JOIN hh_master_local ON (hh_master_pref.local_id = hh_master_local.local_id )";
        query = query + " LEFT JOIN hh_hotel_basic ON (hh_hotel_basic.pref_id = hh_master_pref.pref_id ";
        query = query + " AND hh_hotel_basic.id IN(";

        if ( hotelIds.length != 0 )
        {
            for( i = 0 ; i < hotelIds.length ; i++ )
            {
                query = query + hotelIds[i];
                if ( i < hotelIds.length - 1 )
                {
                    query = query + ",";
                }
            }
        }
        else
        {
            query = query + 0;
        }
        query = query + "))";
        query = query + " WHERE hh_master_local.local_id = ? ";
        query = query + " GROUP BY hh_master_pref.pref_id";

        try
        {
            connection = DBConnection.getConnection();

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, localId );
            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.last() != false )
                {
                    m_prefCount = result.getRow();
                }

                result.beforeFirst();
                for( int count = 0 ; result.next() ; count++ )
                {
                    dataMasterCoupon[count] = new DataMasterCoupon_M2();
                    dataMasterCoupon[count].setPrefId( result.getInt( "pref_id" ) );
                    dataMasterCoupon[count].setPrefName( result.getString( "name" ) );
                    dataMasterCoupon[count].setHotelCount( result.getInt( "count" ) );
                    dataMasterCoupon[count].setLocalName( result.getString( "local_name" ) );
                }
            }
            return dataMasterCoupon;
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelBasicCountByLocal] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            dataMasterCoupon = null;
        }
    }

    /**
     * This function fetch Id list corresponds to GPS search
     * 
     * @param hotelIdList
     * @param available
     * @param mobileFlag
     * @return Integer Array
     */
    public int[] getHotelIdListGps(int[] hotelIdList, int available, boolean mobileFlag)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_coupon,hh_hotel_basic,hh_hotel_sort,hh_master_coupon WHERE";
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
        query = query + " AND hh_hotel_basic.id = hh_hotel_sort.id";
        query = query + " AND hh_hotel_sort.collect_date = 0";
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id ";
        query = query + " AND hh_hotel_coupon.id = hh_master_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " GROUP BY hh_hotel_basic.id";
        query = query + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        count = 0;

        try
        {
            connection = DBConnection.getConnectionRO();
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

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(m_hotelIdList);
    }

}
