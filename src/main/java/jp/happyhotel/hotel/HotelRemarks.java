/*
 * @(#)HotelRemarks.java 1.00 2007/09/26 Copyright (C) ALMEX Inc. 2007 �z�e�����擾�N���X
 */
package jp.happyhotel.hotel;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * �z�e�����擾�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2007/09/26
 * @version 1.1 2007/11/26
 */
public class HotelRemarks implements Serializable
{
    private static final long  serialVersionUID = -351738235590944249L;
    private int                m_hotelRemarksCount;
    private DataHotelRemarks[] m_hotelRemarks;

    /**
     * �f�[�^�����������܂��B
     */
    public HotelRemarks()
    {
        m_hotelRemarksCount = 0;
    }

    /** �z�e�����l��񌏐��擾 **/
    public int getRemarksCount()
    {
        return(m_hotelRemarksCount);
    }

    /** �z�e���������擾 **/
    public DataHotelRemarks[] getHotelRemarks()
    {
        return(m_hotelRemarks);
    }

    /**
     * �z�e�����l���擾
     * 
     * @param hotelId �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getRemarksData(int hotelId)
    {
        int i;
        int count;
        String query;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // �z�e�����l���̎擾
        query = "SELECT * FROM hh_hotel_remarks WHERE id = ?";
        query = query + " AND disp_flag = 1";
        query = query + " AND (start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND end_date >= " + DateEdit.getDate( 2 ) + ")";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    m_hotelRemarksCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.m_hotelRemarks = new DataHotelRemarks[this.m_hotelRemarksCount];
                for( i = 0 ; i < m_hotelRemarksCount ; i++ )
                {
                    m_hotelRemarks[i] = new DataHotelRemarks();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����l���̎擾
                    this.m_hotelRemarks[count++].setData( result );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelRemarks] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * �z�e�����l���擾
     * 
     * @param hotelId �z�e��ID
     * @param dispKind �\���ӏ����
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getRemarksDataByDispKind(int hotelId, int dispKind)
    {
        int i;
        int count;
        String query;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // �z�e�����l���̎擾
        query = "SELECT * FROM hh_hotel_remarks WHERE id = ?";
        query = query + " AND disp_kind = ?";
        query = query + " AND disp_flag = 1";
        query = query + " AND (start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND end_date >= " + DateEdit.getDate( 2 ) + ")";
        count = 0;

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, dispKind );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    m_hotelRemarksCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.m_hotelRemarks = new DataHotelRemarks[this.m_hotelRemarksCount];
                for( i = 0 ; i < m_hotelRemarksCount ; i++ )
                {
                    m_hotelRemarks[i] = new DataHotelRemarks();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����l���̎擾
                    this.m_hotelRemarks[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelRemarks] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }
}
