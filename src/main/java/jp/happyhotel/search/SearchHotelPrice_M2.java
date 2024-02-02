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
     * �f�[�^�����������܂��B
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
     * �����������ʎ擾(�z�e�������N��)
     * 
     * @param hotelIdList �z�e��ID���X�g(null:�S������)
     * @param priceKind �����敪
     * @param startPrice �Œᗿ��
     * @param endPrice �ō�����
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     * @return ��������(TRUE:����,FALSE:�ُ�)
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

        // �z�e���������̎擾
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
            query = query + " OR hh_hotel_price.week LIKE '%�S��%'";
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
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    // �������̎擾
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
     * �����������ʎ擾(�z�e�������N��)
     * 
     * @param hotelIdList �z�e��ID���X�g(null:�S������)
     * @param priceKind �����敪
     * @param startTime �K�p�J�n����(HHMM)
     * @param endTime �K�p�I������(HHMM)
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     * @return ��������(TRUE:����,FALSE:�ُ�)
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

        // �z�e���������̎擾
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
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    // �������̎擾
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
