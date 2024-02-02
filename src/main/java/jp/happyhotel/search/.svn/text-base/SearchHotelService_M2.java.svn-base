package jp.happyhotel.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

// import jp.happyhotel.data.DataHotelBasic;

public class SearchHotelService_M2
{

    private int   m_hotelAllCount;
    private int[] hotelIdList;

    public SearchHotelService_M2()
    {
        // m_hotelCount = 0;
        m_hotelAllCount = 0;
        hotelIdList = new int[0];
    }

    /** ホテル基本情報件数取得 **/
    // public int getCount( ) { return( m_hotelCount ); }
    /** ホテル基本情報件数取得 **/
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    /** ホテル基本情報取得 **/
    // public DataHotelBasic[] getHotelInfo( ) { return( m_hotelInfo ); }

    public int[] getHotelIdList()
    {
        return hotelIdList;
    }

    /**
     * ホテル一覧情報取得(ホテルランク順)
     * 
     * @param service サービス区分(1=検索対象) （[0]:クレジット,[1]:駐車場,[2]:クーポン,[3]:外出,[4]:予約,[5]:１人,[6]:３人,[7]:ルームサービス,[8]:精算機,[9]:空室）
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param mobileFlag 携帯フラグ(0:PC、1:携帯)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelIdList(int[] service, int countNum, int pageNum, int mobileFlag) throws Exception
    {
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( service.length < 9 )
        {
            return(false);
        }
        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id FROM hh_hotel_basic"
                + " LEFT JOIN hh_hotel_status"
                + " ON hh_hotel_basic.id = hh_hotel_status.id"
                + " , hh_hotel_master WHERE hh_hotel_basic.id <> 0"
                + " AND hh_hotel_basic.id = hh_hotel_master.id";

        if ( service[0] == 1 )
        {
            query = query + " AND ("
                    + " hh_hotel_basic.credit = 1"
                    + " OR hh_hotel_basic.credit_visa = 1"
                    + " OR hh_hotel_basic.credit_master = 1"
                    + " OR hh_hotel_basic.credit_jcb = 1"
                    + " OR hh_hotel_basic.credit_dc = 1"
                    + " OR hh_hotel_basic.credit_nicos = 1"
                    + " OR hh_hotel_basic.credit_amex = 1"
                    + " OR hh_hotel_basic.credit_etc <> ''"
                    + " )";
        }
        if ( service[1] == 1 )
        {
            query = query + " AND hh_hotel_basic.parking = 1";
        }
        if ( service[3] == 1 )
        {
            query = query + " AND hh_hotel_basic.halfway = 1";
        }
        if ( service[4] == 1 )
        {
            query = query + " AND ("
                    + " hh_hotel_basic.reserve = 1"
                    + " OR hh_hotel_basic.reserve_tel = 1"
                    + " OR hh_hotel_basic.reserve_mail = 1"
                    + " OR hh_hotel_basic.reserve_web = 1"
                    + " )";
        }
        if ( service[5] == 1 )
        {
            query = query + " AND hh_hotel_basic.possible_one = 1";
        }
        if ( service[6] == 1 )
        {
            query = query + " AND hh_hotel_basic.possible_three = 1";
        }
        if ( service[7] == 1 )
        {
            query = query + " AND hh_hotel_basic.roomservice = 1";
        }
        if ( service[8] == 1 )
        {
            query = query + " AND hh_hotel_basic.pay_auto = 1";
        }
        if ( service[9] == 1 )
        {
            query = query + " AND hh_hotel_status.empty_status = 1"
                    + " AND hh_hotel_master.empty_disp_kind = 1";
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

                hotelIdList = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    hotelIdList[count++] = result.getInt( "id" );
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelService_M2.getHotelList(" + service + "," + countNum + "," + pageNum + ")] Exception= " + e.toString() );
            ret = false;
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( ret != false )
        {
            if ( service[2] == 1 )
            {
                ret = getHotelIdListByCoupon( hotelIdList, 0, 0, mobileFlag );
            }
        }
        return(ret);
    }

    /**
     * ホテル一覧情報取得(ホテルランク順)
     * 
     * @param service サービス区分(1=検索対象) （[0]:クレジット,[1]:駐車場,[2]:クーポン,[3]:外出,[4]:予約,[5]:１人,[6]:３人,[7]:ルームサービス,[8]:精算機,[9]:空室）
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param mobileFlag(0:PC、1:携帯)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelIdListByCoupon(int[] hotelIdList, int countNum, int pageNum, int mobileFlag) throws Exception
    {
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_coupon,hh_hotel_basic, hh_master_coupon,hh_hotel_sort, hh_master_pref" +
                " WHERE hh_hotel_coupon.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) ) +
                " AND hh_hotel_coupon.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) ) +
                " AND hh_hotel_coupon.del_flag <> 1";
        if ( mobileFlag == 1 )
        {
            query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        }
        query = query + " AND ( hh_master_coupon.available = 0 OR hh_master_coupon.available = 1 ) " +
                " AND hh_hotel_basic.id = hh_hotel_coupon.id" +
                " AND hh_hotel_basic.kind <= 7" +
                " AND hh_hotel_basic.id = hh_hotel_sort.id" +
                " AND hh_hotel_sort.collect_date = 0" +
                " AND hh_hotel_basic.pref_id = hh_master_pref.pref_id " +
                " AND hh_master_pref.local_id <> 0" +
                " AND hh_hotel_basic.id = hh_master_coupon.id " +
                " AND hh_hotel_coupon.id = hh_master_coupon.id" +
                " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no" +
                " AND ( hh_master_coupon.service_flag = 0 OR hh_master_coupon.service_flag = 1 )";
        if ( hotelIdList != null )
        {
            query = query + " AND hh_hotel_basic.id IN(";
            for( int i = 0 ; i < hotelIdList.length ; i++ )
            {
                if ( hotelIdList[i] == 0 )
                {
                    break;
                }
                query = query + hotelIdList[i];

                if ( i + 1 < hotelIdList.length )
                {
                    if ( hotelIdList[i + 1] != 0 )
                    {
                        query = query + ",";
                    }
                }
            }
            query = query + ")";

        }

        query = query + " GROUP BY hh_hotel_basic.id" +
                " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

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
                    Logging.info( "[SearchHotelService_M2.getHotelList] m_hotelAllCount= " + m_hotelAllCount );
                }

                this.hotelIdList = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.hotelIdList[count++] = result.getInt( "id" );
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelService_M2.getHotelIdListByCoupon(" + hotelIdList + "," + countNum + "," + pageNum + ")] Exception= " + e.toString() );
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
