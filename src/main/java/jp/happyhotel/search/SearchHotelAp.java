package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelSetting;

/**
 * �V�K�I�[�v���z�e�������N���X
 * 
 * @author N.Ide
 * @version 1.00 2011/05/26
 */
public class SearchHotelAp implements Serializable
{
    private static final long    serialVersionUID = -5437231184752741778L;

    private int                  m_hotelCount;
    private int                  m_hotelAllCount;
    private int[]                m_hotelIdList;
    private DataApHotelSetting[] m_hotelAp;

    /**
     * �f�[�^�����������܂��B
     */
    public SearchHotelAp()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;
        m_hotelIdList = new int[0];
        m_hotelAp = new DataApHotelSetting[0];
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

    public DataApHotelSetting[] getHotelAp()
    {
        return(m_hotelAp);
    }

    /**
     * �z�e�����擾
     * 
     * @param prefId �s���{��ID�i0:�S���j
     * @param flag �擾�敪�i0:��ʂȂ�,1:�ғ��\��̂�,2:�ғ��ς̂݁j
     * @param countNum �z�e������
     * @param pageNum �y�[�W��
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getHotelList(int prefId, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT HS.*" +
                " FROM ap_hotel_setting HS" +
                " INNER JOIN hh_hotel_basic HB ON ( HS.id = HB.id ) AND (HB.id < 100000000)" +
                " WHERE  current_date >= HS.start_date " +
                " AND current_date <= HS.end_date ";
        if ( prefId > 0 )
        {
            query += " AND HB.pref_id = ?";
        }
        query += " GROUP BY ( HS.id )";
        query += " ORDER BY start_date DESC, RAND()";
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
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    m_hotelCount = result.getRow();
                }
                // �N���X�̔z���p�ӂ��A����������B
                this.m_hotelAp = new DataApHotelSetting[this.m_hotelCount];
                for( i = 0 ; i < m_hotelCount ; i++ )
                {
                    m_hotelAp[i] = new DataApHotelSetting();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����̎擾
                    this.m_hotelAp[count++].setData( result );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelNewOpen.getRsvHotelListByPref] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // �z�e���������̎擾
        query = "SELECT *" +
                " FROM ap_hotel_setting HS" +
                " INNER JOIN hh_hotel_basic HB ON ( HS.id = HB.id ) AND (HB.id < 100000000)" +
                " WHERE current_date >= HS.start_date " +
                " AND current_date <= HS.end_date ";
        if ( prefId > 0 )
        {
            query += " AND HB.pref_id = ?";
        }
        query += " GROUP BY ( HS.id )";
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
                    m_hotelIdList[count++] = result.getInt( "HS.id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelNewOpen.getRsvHotelListByPref] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }
}
