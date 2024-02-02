/*
 * @(#)SearchHotelMap.java 1.00 2009/07/10 Copyright (C) ALMEX Inc. 2009 �z�e�����ӂ̉w�AIC����
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapPoint;

/**
 * �z�e�����ӂ̉w�AIC����
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/10
 */
public class SearchHotelMapPoint implements Serializable
{

    private static final long serialVersionUID = -6939301017250195719L;

    private int               m_stationCount;
    private int               m_icCount;
    private String[]          m_stationDistance;
    private String[]          m_icDistance;
    private DataMapPoint[]    m_dmPointStation;
    private DataMapPoint[]    m_dmPointIC;

    /**
     * �f�[�^�����������܂��B
     */
    public SearchHotelMapPoint()
    {
        m_stationCount = 0;
        m_icCount = 0;
    }

    /**
     * @return �߂���IC�̃}�b�v�|�C���g�f�[�^
     */
    public DataMapPoint[] getIc()
    {
        return m_dmPointIC;
    }

    /**
     * @return �߂���IC����
     */
    public int getIcCount()
    {
        return m_icCount;
    }

    /**
     * @return m_icDistance
     */
    public String[] getIcDistance()
    {
        return m_icDistance;
    }

    /**
     * @return �߂��̉w�̃}�b�v�|�C���g�f�[�^
     */
    public DataMapPoint[] getStation()
    {
        return m_dmPointStation;
    }

    /**
     * @return �߂��̉w����
     */
    public int getStationCount()
    {
        return m_stationCount;
    }

    /**
     * @return m_stationDistance
     */
    public String[] getStationDistance()
    {
        return m_stationDistance;
    }

    /**
     * @param �߂���IC�̃}�b�v�|�C���g�f�[�^
     */
    public void setIc(DataMapPoint[] ic)
    {
        m_dmPointIC = ic;
    }

    /**
     * @param �߂���IC����
     */
    public void setIcCount(int icCount)
    {
        m_icCount = icCount;
    }

    /**
     * @param mIcDistance �Z�b�g���� m_icDistance
     */
    public void setIcDistance(String[] icDistance)
    {
        m_icDistance = icDistance;
    }

    /**
     * @param �߂��̉w�̃}�b�v�|�C���g�f�[�^
     */
    public void setStation(DataMapPoint[] station)
    {
        m_dmPointStation = station;
    }

    /**
     * @param �߂��̉w����
     */
    public void setStationCount(int stationCount)
    {
        m_stationCount = stationCount;
    }

    /**
     * @param mStationDistance �Z�b�g���� m_stationDistance
     */
    public void setStationDistance(String[] stationDistance)
    {
        m_stationDistance = stationDistance;
    }

    /**
     * �z�e�����ӂ̉w�AIC����
     * 
     * @param hotelId �z�e��ID
     * @return �������ʁitrue:����, false:�ُ�j
     */
    public boolean getSearchHotelNear(String hotelId)
    {
        int i;
        int countSt;
        int countIc;
        int countAll;
        String[] distance;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataMapPoint[] dmPoint;

        countSt = 0;
        countIc = 0;
        countAll = 0;
        query = "SELECT hh_hotel_map.distance, hh_map_point.*" +
                " FROM hh_hotel_map, hh_map_point" +
                " WHERE hh_hotel_map.id = ?" +
                        // " AND hh_hotel_map.distance <= 5000" +
                " AND hh_hotel_map.disp_flag = 1" +
                " AND hh_hotel_map.map_id = hh_map_point.option_4" +
                " GROUP BY hh_map_point.option_4" +
                " ORDER BY hh_hotel_map.distance";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    // �������̎擾
                    countAll = result.getRow();
                }

                dmPoint = new DataMapPoint[countAll];
                distance = new String[countAll];

                result.beforeFirst();
                while( result.next() != false )
                {
                    // class_code�̓���4��������IC�J�E���g�𑝂₵�A5��������w�̃J�E���g�𑝂₷
                    if ( result.getString( "class_code" ).startsWith( "4" ) )
                    {
                        m_icCount++;
                    }
                    else if ( result.getString( "class_code" ).startsWith( "5" ) )
                    {
                        m_stationCount++;
                    }
                    dmPoint[countSt] = new DataMapPoint();
                    dmPoint[countSt].setData( result );
                    distance[countSt] = Integer.toString( result.getInt( "distance" ) );
                    countSt++;
                }

                countSt = 0;
                if ( countAll > 0 )
                {
                    if ( m_icCount > 0 || m_stationCount > 0 )
                    {
                        // IC�̗v�f��������ΐV�������
                        if ( m_icCount > 0 )
                        {
                            m_icDistance = new String[m_icCount];
                            m_dmPointIC = new DataMapPoint[m_icCount];
                        }
                        // �w�̗v�f��������ΐV�������
                        if ( m_stationCount > 0 )
                        {
                            m_stationDistance = new String[m_stationCount];
                            m_dmPointStation = new DataMapPoint[m_stationCount];
                        }
                        // DB����擾�����������J��Ԃ��A���ꂼ��̔z��ɕ�����
                        for( i = 0 ; i < countAll ; i++ )
                        {
                            if ( dmPoint[i].getClassCode().startsWith( "4" ) != false )
                            {
                                m_icDistance[countIc] = distance[i];
                                m_dmPointIC[countIc] = new DataMapPoint();
                                m_dmPointIC[countIc] = dmPoint[i];
                                countIc++;
                            }
                            if ( dmPoint[i].getClassCode().startsWith( "5" ) != false )
                            {
                                m_stationDistance[countSt] = distance[i];
                                m_dmPointStation[countSt] = new DataMapPoint();
                                m_dmPointStation[countSt] = dmPoint[i];
                                countSt++;
                            }
                        }
                    }
                }
            }
            return(true);
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelMapPoint] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            dmPoint = null;
        }

        return(false);
    }

    /**
     * �z�e�����ӂ̉w����
     * 
     * @param hotelId �z�e��ID
     * @return �������ʁitrue:����, false:�ُ�j
     */
    public boolean getSearchHotelNearStation(String hotelId)
    {
        int i;
        int count;
        String[] distance;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        count = 0;
        query = "SELECT hh_hotel_map.distance, hh_map_point.*" +
                " FROM hh_hotel_map, hh_map_point" +
                " WHERE hh_hotel_map.id = ?" +
                        // " AND hh_hotel_map.distance <= 5000" +
                " AND hh_hotel_map.disp_flag = 1" +
                " AND hh_map_point.class_code like '5%'" +
                " AND hh_hotel_map.map_id = hh_map_point.option_4" +
                " GROUP BY hh_map_point.option_4" +
                " ORDER BY hh_hotel_map.distance";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    // �������̎擾
                    m_stationCount = result.getRow();
                }

                m_dmPointStation = new DataMapPoint[m_stationCount];
                m_stationDistance = new String[m_stationCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_dmPointStation[count] = new DataMapPoint();
                    m_dmPointStation[count].setData( result );
                    m_stationDistance[count] = Integer.toString( result.getInt( "distance" ) );
                    count++;
                }
            }
            return(true);
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelMapPoint.getSearchHotelNearStation] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(false);
    }

    /**
     * �z�e�����ӂ�IC����
     * 
     * @param hotelId �z�e��ID
     * @return �������ʁitrue:����, false:�ُ�j
     */
    public boolean getSearchHotelNearIc(String hotelId)
    {
        int i;
        int count;
        String[] distance;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        count = 0;
        query = "SELECT hh_hotel_map.distance, hh_map_point.*" +
                " FROM hh_hotel_map, hh_map_point" +
                " WHERE hh_hotel_map.id = ?" +
                        // " AND hh_hotel_map.distance <= 5000" +
                " AND hh_hotel_map.disp_flag = 1" +
                " AND hh_map_point.class_code like '4%'" +
                " AND ( hh_map_point.name like '%�o��%'" +
                " OR hh_map_point.name like '%�o����%'" +
                " OR hh_map_point.name like '%�C���^�[�`�F���W%' )" +
                " AND hh_hotel_map.map_id = hh_map_point.option_4" +
                " ORDER BY hh_hotel_map.distance";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    // �������̎擾
                    this.m_icCount = result.getRow();
                }

                m_dmPointIC = new DataMapPoint[m_icCount];
                m_icDistance = new String[m_icCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_dmPointIC[count] = new DataMapPoint();
                    m_dmPointIC[count].setData( result );
                    m_icDistance[count] = Integer.toString( result.getInt( "distance" ) );
                    count++;
                }
            }
            return(true);
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelMapPoint.getSearchHotelNearIc] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(false);
    }

}
