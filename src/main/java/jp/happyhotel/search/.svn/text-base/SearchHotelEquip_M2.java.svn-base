package jp.happyhotel.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class SearchHotelEquip_M2
{

    private int   m_hotelAllCount;
    private int[] hotelIdList;

    public SearchHotelEquip_M2()
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
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelIdList(int[] equiplist, int countNum, int pageNum) throws Exception
    {
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( equiplist == null || equiplist.length <= 0 )
        {
            return(false);
        }
        int equipCount = equiplist.length;
        // ホテル総件数の取得
        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_equip,hh_hotel_sort WHERE"
                + " hh_hotel_equip.equip_id in ( ";

        for( int i = 0 ; i < equipCount ; i++ )
        {
            if ( i == 0 )
                query = query + "?";
            else
                query = query + ", " + "?";
        }

        query = query + " ) AND (hh_hotel_equip.equip_type = 1 OR hh_hotel_equip.equip_type = 2 OR hh_hotel_equip.equip_type = 3)"
                + " AND hh_hotel_basic.id = hh_hotel_equip.id"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                + " AND hh_hotel_sort.collect_date = 0"
                + " GROUP BY hh_hotel_basic.id"
                + " having count(hh_hotel_basic.id) >= " + equipCount
                + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        count = 0;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );

            for( int i = 0 ; i < equipCount ; i++ )
            {
                prestate.setInt( (i + 1), equiplist[i] );
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
            Logging.error( "[SearchHotelEquip_M2.getHotelList(" + equiplist + "," + countNum + "," + pageNum + ")] Exception=" + e.toString() );
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
