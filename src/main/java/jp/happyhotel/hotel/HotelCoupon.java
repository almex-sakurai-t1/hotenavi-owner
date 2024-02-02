/*
 * @(#)HotelCoupon.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 �z�e���N�[�|�����擾�N���X
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelCoupon;

/**
 * �z�e���N�[�|�����擾�N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class HotelCoupon implements Serializable
{
    private static final long serialVersionUID = 1947404061823922467L;

    private int               m_hotelCouponCount;
    private DataHotelCoupon   m_hotelCoupon;

    /**
     * �f�[�^�����������܂��B
     */
    public HotelCoupon()
    {
        m_hotelCouponCount = 0;
    }

    /** �z�e���N�[�|�����擾 **/
    public int getHotelCouponCount()
    {
        return(m_hotelCouponCount);
    }

    public DataHotelCoupon getHotelCoupon()
    {
        return(m_hotelCoupon);
    }

    /**
     * �z�e���N�[�|���ꗗ���擾
     * 
     * @param hotelId �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getCouponData(Connection connection, int hotelId)
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_coupon.* FROM hh_hotel_coupon, hh_master_coupon WHERE hh_hotel_coupon.id = ? ";
        query = query + " AND hh_hotel_coupon.del_flag <> 1";
        query = query + " AND ( hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 ) + ")";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR";
        query = query + " hh_master_coupon.service_flag = 1 )";
        query = query + " AND hh_hotel_coupon.id = hh_master_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " ORDER BY hh_hotel_coupon.seq DESC, hh_master_coupon.seq DESC";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    m_hotelCouponCount = result.getRow();
                }
                m_hotelCoupon = new DataHotelCoupon();

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e���N�[�|�����̎擾
                    this.m_hotelCoupon.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getCouponData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(true);
    }

    /**
     * �z�e���N�[�|���ꗗ���擾
     * 
     * @param hotelId �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getCouponData(int hotelId)
    {
        Connection connection = null;
        boolean ret = false;

        try
        {
            connection = DBConnection.getConnection();
            ret = getCouponData( connection, hotelId );
        }
        catch ( Exception e )
        {
            Logging.error( "[getCouponData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �z�e���N�[�|���ꗗ���擾
     * 
     * @param hotelId �z�e��ID
     * @param available ���p�t���O(0:����Ȃ�,1:���ް,2:�޼���)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getCouponData(int hotelId, int available)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_coupon.* FROM hh_hotel_coupon, hh_master_coupon WHERE hh_hotel_coupon.id = ? ";
        query = query + " AND hh_hotel_coupon.del_flag <> 1";
        query = query + " AND (hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 ) + ")";
        query = query + " AND ( hh_master_coupon.available = 0";
        if ( available > 0 )
        {
            query = query + " OR hh_master_coupon.available = ?";
        }
        query = query + " )";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR";
        query = query + " hh_master_coupon.service_flag = 1 )";
        query = query + " AND hh_hotel_coupon.id = hh_master_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " ORDER BY hh_hotel_coupon.seq DESC, hh_master_coupon.seq DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );

            if ( available > 0 )
            {
                prestate.setInt( 2, available );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    m_hotelCouponCount = result.getRow();
                }
                m_hotelCoupon = new DataHotelCoupon();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // �z�e���N�[�|�����̎擾
                    this.m_hotelCoupon.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getCouponData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * �z�e���N�[�|���ꗗ���擾
     * 
     * @param hotelId �z�e��ID
     * @param available ���p�t���O(0:����Ȃ�,1:���ް,2:�޼���)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMobileCouponData(int hotelId, int available)
    {
        Connection connection = null;
        boolean ret = false;

        try
        {
            connection = DBConnection.getConnection();
            ret = getMobileCouponData( connection, hotelId, available );
        }
        catch ( Exception e )
        {
            Logging.error( "[getMobileCouponData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �z�e���N�[�|���ꗗ���擾
     * 
     * @param hotelId �z�e��ID
     * @param available ���p�t���O(0:����Ȃ�,1:���ް,2:�޼���)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMobileCouponData(Connection connection, int hotelId)
    {
        boolean ret = false;
        ret = getMobileCouponData( connection, hotelId, 1 );
        if ( ret )
        {
            if ( m_hotelCouponCount == 0 )
            {
                ret = getMobileCouponData( connection, hotelId, 2 );
            }
        }
        return(ret);
    }

    /**
     * �z�e���N�[�|���ꗗ���擾
     * 
     * @param hotelId �z�e��ID
     * @param available ���p�t���O(0:����Ȃ�,1:���ް,2:�޼���)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMobileCouponData(Connection connection, int hotelId, int available)
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_coupon.* FROM hh_hotel_coupon, hh_master_coupon WHERE hh_hotel_coupon.id = ? ";
        query = query + " AND hh_hotel_coupon.del_flag <> 1";
        query = query + " AND (hh_hotel_coupon.start_date <= " + DateEdit.getDate( 2 );
        query = query + " AND hh_hotel_coupon.end_date >= " + DateEdit.getDate( 2 ) + ")";
        query = query + " AND ( hh_master_coupon.available = 0";
        if ( available > 0 )
        {
            query = query + " OR hh_master_coupon.available = ?";
        }
        query = query + " )";
        query = query + " AND ( hh_master_coupon.service_flag = 0 OR";
        query = query + " hh_master_coupon.service_flag = 1 )";
        query = query + " AND hh_hotel_coupon.disp_mobile = 1";
        query = query + " AND hh_hotel_coupon.id = hh_master_coupon.id";
        query = query + " AND hh_hotel_coupon.seq = hh_master_coupon.coupon_no";
        query = query + " ORDER BY hh_hotel_coupon.seq DESC, hh_master_coupon.seq DESC";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );

            if ( available > 0 )
            {
                prestate.setInt( 2, available );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    m_hotelCouponCount = result.getRow();
                }
                m_hotelCoupon = new DataHotelCoupon();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // �z�e���N�[�|�����̎擾
                    this.m_hotelCoupon.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getMobileCouponData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(true);
    }
}
