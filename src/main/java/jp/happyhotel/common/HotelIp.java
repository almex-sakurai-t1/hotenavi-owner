/*
 * @(#)CheckString.java 1.00 2021/09/22 Copyright (C) ALMEX Inc. 2021 �z�e�����[���}�K�W���`�F�b�N�ėp�N���X
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * �z�e�����[���}�K�W���L���擾
 * 
 * @author T.Sakurai
 * @version 1.00 21/09/22
 */
public class HotelIp implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * �z�e��IP�A�h���X�擾�i�ڋq�ڑ��p�@NEO+���[���T�[�o�̏ꍇ�͂�������Q�Ƃ���j
     * 
     * @param orgData �`�F�b�N�Ώە�����
     * @return ��������(true:�ُ�,false:����)
     */
    public static String getFrontIp(int id)
    {
        String query;
        String frontIp = "";

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // query = "SELECT hh_hotel_basic.id,hotel.front_ip FROM hh_hotel_basic,hotel WHERE hh_hotel_basic.id = ? AND hh_hotel_basic.hotenavi_id = hotel.hotel_id";
        try
        {
            query = "SELECT hh_hotel_basic.id, hh_hotel_master.front_ip FROM hh_hotel_basic";
            query += " INNER JOIN hh_hotel_master ON hh_hotel_basic.id = hh_hotel_master.id ";
            query += " WHERE hh_hotel_basic.id = ? ";
            query += " AND hh_hotel_master.touch_sync_flag = 1 ";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    frontIp = result.getString( "front_ip" );
                    System.out.println( "frontIp:" + frontIp );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CheckHotelFrontIp.checkFrontIp] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frontIp);
    }

    /**
     * �z�e��IP�A�h���X�擾�i�ڋq�ڑ��p�@NEO+���[���T�[�o�̏ꍇ�͂�������Q�Ƃ���j
     * 
     * @param orgData �`�F�b�N�Ώە�����
     * @return ��������(true:�ُ�,false:����)
     */
    public static String getFrontIp(int id, int pmsFlag)
    {
        String query;
        String frontIp = "";

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // query = "SELECT hh_hotel_basic.id,hotel.front_ip FROM hh_hotel_basic,hotel WHERE hh_hotel_basic.id = ? AND hh_hotel_basic.hotenavi_id = hotel.hotel_id";
        try
        {
            query = "SELECT hh_hotel_basic.id, hh_hotel_master.front_ip FROM hh_hotel_basic";
            query += " INNER JOIN hh_hotel_master ON hh_hotel_basic.id = hh_hotel_master.id ";
            query += " WHERE hh_hotel_basic.id = ? ";
            query += " AND hh_hotel_master.touch_sync_flag = 1 AND hh_hotel_master.pms_flag = ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, pmsFlag );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    frontIp = result.getString( "front_ip" );
                    System.out.println( "frontIp:" + frontIp );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CheckHotelFrontIp.checkFrontIp] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frontIp);
    }

    /**
     * �z�e��IP�A�h���X�擾�i�}�C���g�p�̏ꍇ�͂�������Q�Ƃ���j
     * 
     * @param orgData �`�F�b�N�Ώە�����
     * @return ��������(true:�ُ�,false:����)
     */
    public static String getFrontIpForUseMile(int id)
    {
        String query;
        String frontIp = "";

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "SELECT hh_hotel_basic.id, hh_hotel_master.front_ip,hh_hotel_master.use_front_disp_flag FROM hh_hotel_basic";
            query += " INNER JOIN hh_hotel_master ON hh_hotel_basic.id = hh_hotel_master.id ";
            query += " WHERE hh_hotel_basic.id = ? ";
            query += "   AND hh_hotel_master.use_front_disp_flag = 0 ";
            query += "   AND hh_hotel_master.touch_sync_flag = 1 ";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    frontIp = result.getString( "front_ip" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CheckHotelFrontIp.checkFrontIp] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frontIp);
    }

    /**
     * �z�e��IP�A�h���X�擾
     * 
     * @param orgData �`�F�b�N�Ώە�����
     * @return ��������(true:�ُ�,false:����)
     */
    public static String getHotenaviIp(int id)
    {
        String query;
        String frontIp = "";

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_basic.id,hotel.front_ip FROM hh_hotel_basic";
        query += " INNER JOIN hotel ON hh_hotel_basic.hotenavi_id = hotel.hotel_id ";
        query += " WHERE hh_hotel_basic.id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    frontIp = result.getString( "front_ip" );
                    System.out.println( "frontIp:" + frontIp );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CheckHotelFrontIp.checkFrontIp] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frontIp);
    }

    /**
     * �z�e��IP�A�h���X�擾
     * 
     * @param orgData �`�F�b�N�Ώە�����
     * @return ��������(true:�ُ�,false:����)
     */
    public static String getHotenaviIp(String hotenaviId)
    {
        String query;
        String frontIp = "";

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hotel.front_ip FROM hotel";
        query += " WHERE hotel_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotenaviId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    frontIp = result.getString( "front_ip" );
                    System.out.println( "frontIp:" + frontIp );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CheckHotelFrontIp.checkFrontIp] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frontIp);
    }

    /**
     * �z�e��IP�A�h���X�ϊ�
     * 
     * @param targetIP ����IP�A�h���X
     * @return ��������(�ϊ����IP�A�h���X)
     */
    public static String convertIP(String targetIP)
    {
        String unusableIP = "172.16.48";
        String convertIP = "172.25.188";
        // IP�A�h���X��ϊ��i�擾�����IP�A�h���X��172.16.48.XXX�̂̂Ƃ��A172.25.188.XXX�ɕϊ�����B#34317�j
        // GCP�̃N���E�hSQL�̑ш悪�@172.16.48.0/24 �ƂȂ��Ă���d�Ȃ��Ă��܂����z�e���͒ʐM�ł��Ȃ��̂�DB���IP�A�h���X��172.25.188.XXX�ɕύX��������
        try
        {
            int index = targetIP.lastIndexOf( "." );
            if ( index > 0 )
            {
                if ( unusableIP.equals( targetIP.substring( 0, index ) ) )
                {
                    targetIP = convertIP + targetIP.substring( index );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelIp.convertIP] Exception=" + e.toString(), e );
        }
        finally
        {
        }
        return(targetIP);
    }

}
