package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.ConvertGeodesic;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSearchHotel_M2;

/**
 * GPS�����z�e���擾�N���X
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/10/06
 */
public class SearchHotelGps_M2 implements Serializable
{
    /**
     *
     */
    private static final long    serialVersionUID = 5224942568576351030L;

    private static final int     LATLON_RANGE     = 100000;

    private int                  m_hotelAllCount;
    private int[]                m_hotelIdList;
    private int[]                m_distance;
    private double               m_latW;
    private double               m_lonW;
    private double               m_latT;
    private double               m_lonT;
    private int                  m_latWNum;
    private int                  m_lonWNum;
    private DataSearchHotel_M2[] m_hotelInfo;
    /**
     * �ܓx�P�O�i�\�L�i���{���n�n�j
     */
    private int                  m_latTNum;
    /**
     * �o�x�P�O�i�\�L�i���{���n�n�j
     */
    private int                  m_lonTNum;

    /**
     * �f�[�^�����������܂��B
     */
    public SearchHotelGps_M2()
    {
        m_hotelAllCount = 0;
        m_latW = 0.0;
        m_lonW = 0.0;
        m_latT = 0.0;
        m_lonT = 0.0;
        m_latWNum = 0;
        m_lonWNum = 0;
        m_latTNum = 0;
        m_lonTNum = 0;
    }

    /** Fetch Hotel All Count **/
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    /** Fetch Hotel Id List **/
    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    /** Fetch hotel Details **/
    public DataSearchHotel_M2[] getHotelInfo()
    {
        return(m_hotelInfo);
    }

    public int[] getHotelDistance()
    {
        return(m_distance);
    }

    /**
     * @return m_latT
     */
    public double getLatT()
    {
        return m_latT;
    }

    /**
     * @return m_latTNum
     */
    public int getLatTNum()
    {
        return m_latTNum;
    }

    /**
     * @return m_latW
     */
    public double getLatW()
    {
        return m_latW;
    }

    /**
     * @return m_latWNum
     */
    public int getLatWNum()
    {
        return m_latWNum;
    }

    /**
     * @return m_lonT
     */
    public double getLonT()
    {
        return m_lonT;
    }

    /**
     * @return m_lonTNum
     */
    public int getLonTNum()
    {
        return m_lonTNum;
    }

    /**
     * @return m_lonW
     */
    public double getLonW()
    {
        return m_lonW;
    }

    /**
     * @return m_lonWNum
     */
    public int getLonWNum()
    {
        return m_lonWNum;
    }

    /**
     * �z�e���ꗗ���擾(�z�e�������N��)
     * 
     * @param lat �ܓx(���E���n�n 999.99.99.99)
     * @param lon �o�x(���E���n�n 99.99.99.99)
     * @param kind �͈́i0:�f�t�H���g�A1:�L����(2�{)�A����ȊO:���߂�(1/2�{�j)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public int[] getHotelIdList(String lat, String lon, int kind) throws Exception
    {
        int[] m_hotelIdList;
        int count;
        int latLeft;
        int lonLeft;
        int latRight;
        int lonRight;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        m_hotelIdList = new int[0];

        // ���E���n�n������{���n�n�֕ϊ�
        convertDms2Degree( lat, lon );

        if ( m_latTNum <= 0 || m_lonTNum <= 0 )
        {
            return m_hotelIdList;
        }
        if ( kind == 0 )
        {
            // ���[(lat-10000,lon-10000)
            latLeft = m_latTNum - LATLON_RANGE;
            lonLeft = m_lonTNum - LATLON_RANGE;
            latRight = m_latTNum + LATLON_RANGE;
            lonRight = m_lonTNum + LATLON_RANGE;
        }
        else if ( kind == 1 )
        {
            latLeft = m_latTNum - (LATLON_RANGE * 2);
            lonLeft = m_lonTNum - (LATLON_RANGE * 2);
            latRight = m_latTNum + (LATLON_RANGE * 2);
            lonRight = m_lonTNum + (LATLON_RANGE * 2);
        }
        else
        {
            latLeft = m_latTNum - (LATLON_RANGE / 2);
            lonLeft = m_lonTNum - (LATLON_RANGE / 2);
            latRight = m_latTNum + (LATLON_RANGE / 2);
            lonRight = m_lonTNum + (LATLON_RANGE / 2);
        }
        ConvertGeodesic cg;
        cg = new ConvertGeodesic();
        cg.TokyoNum2Tokyo( latRight, lonRight );
        cg.Tokyo2Wgs( cg.getLatTOKYO(), cg.getLonTOKYO() );

        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_sort WHERE";
        query = query + " (hotel_lat_num > ? AND hotel_lat_num < ?)";
        query = query + " AND ";
        query = query + " (hotel_lon_num > ? AND hotel_lon_num < ?)";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_basic.id = hh_hotel_sort.id";
        query = query + " AND hh_hotel_basic.pref_id <> 0";		//�s���{���R�[�h�����݂��Ȃ��z�e���͏��O����B(�z�e���A�����b�N�X)
        query = query + " AND hh_hotel_sort.collect_date = 0";
        query = query + " GROUP BY hh_hotel_basic.id";
        query = query + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

        count = 0;

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, latLeft );
            prestate.setInt( 2, latRight );
            prestate.setInt( 3, lonLeft );
            prestate.setInt( 4, lonRight );
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
            return m_hotelIdList;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelGps_M2.getHotelIdList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            m_hotelIdList = null;
        }
    }

    /**
     * Dms��degree�R���o�[�g
     * 
     * @param lat �ܓx(999.99.99.99)
     * @param lon �o�x(99.99.99.99)
     */
    public void convertDms2Degree(String lat, String lon)
    {
        int i;
        double[] cutLat;
        double[] cutLon;
        Double cutLatD = 0.0;
        Double cutLonD = 0.0;
        String[] cutData;
        ConvertGeodesic cg;

        cg = new ConvertGeodesic();

        // dms��degree�ϊ�
        cutData = lat.split( "\\.", 3 );
        cutLat = new double[cutData.length];
        for( i = 0 ; i < cutData.length ; i++ )
        {
            cutLat[i] = Double.valueOf( cutData[i] );
        }
        if ( cutData.length > 2 )
        {
            cutLatD = (double)(cutLat[0]) + (double)(cutLat[1] / 60) + (double)(cutLat[2] / 3600);
        }

        cutData = lon.split( "\\.", 3 );
        cutLon = new double[cutData.length];
        for( i = 0 ; i < cutData.length ; i++ )
        {
            cutLon[i] = Double.valueOf( cutData[i] );
        }
        if ( cutData.length > 2 )
        {
            cutLonD = (double)(cutLon[0]) + (double)(cutLon[1] / 60) + (double)(cutLon[2] / 3600);
        }

        // ���E���n�ndgree
        m_latW = cutLatD;
        m_lonW = cutLonD;
        // ���E���n�nnum�ϊ�
        cg.Wgs2WgsNum( m_latW, m_lonW );
        m_latWNum = cg.getLatWGSNum();
        m_lonWNum = cg.getLonWGSNum();

        // WGS->Tokyo
        cg.Wgs2Tokyo( cutLatD, cutLonD );

        // ���{���n�ndgree
        m_latT = cg.getLatTOKYO();
        m_lonT = cg.getLonTOKYO();
        // ���{���n�nnum�ϊ�
        cg.Tokyo2TokyoNum( m_latT, m_lonT );
        m_latTNum = cg.getLatTOKYONum();
        m_lonTNum = cg.getLonTOKYONum();
    }

    /****
     * �z�e���ꗗ���擾�iGooglemap�����A�������j
     * 
     * @param lat �ܓx
     * @param lon �o�x
     * @param zoom �Y�[��
     * @return
     */
    public int[] getHotelIdListForGoogleMap(String lat, String lon, int zoom) throws Exception
    {

        int[] m_hotelIdList;
        int count;
        int distance = 50;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        m_hotelIdList = null;

        int i = 0;
        int j = 0;

        j = 21 - zoom;
        if ( j > 10 )
        {
            j = 11;
        }
        for( i = 0 ; i < j ; i++ )
        {
            distance = distance * 2;
        }

        query = "SELECT id, ( 6378140 * acos( cos( radians( ? ) ) * cos( radians( hotel_lat ) ) * cos( radians( hotel_lon ) ";
        query += " - radians( ? ) ) + sin( radians( ? ) ) * sin( radians( hotel_lat ) ) ) ) AS distance ";
        query += " FROM hh_hotel_basic WHERE kind <=7 AND pref_id <> 0 HAVING distance <= ? ORDER BY distance LIMIT 0,20";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, lat );
            prestate.setString( 2, lon );
            prestate.setString( 3, lat );
            prestate.setInt( 4, distance );
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
                m_distance = new int[this.m_hotelAllCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count] = result.getInt( "id" );
                    m_distance[count] = result.getInt( "distance" );
                    count++;
                }
            }
            return m_hotelIdList;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelGps_M2.getHotelIdList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            m_hotelIdList = null;
        }
    }
}
