/*
 * @(#)CheckString.java 1.00 2021/09/22 Copyright (C) ALMEX Inc. 2021 ホテルメールマガジンチェック汎用クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * ホテルメールマガジン有無取得
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
     * ホテルIPアドレス取得（顧客接続用　NEO+ルームサーバの場合はこちらを参照する）
     * 
     * @param orgData チェック対象文字列
     * @return 処理結果(true:異常,false:正常)
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
     * ホテルIPアドレス取得（顧客接続用　NEO+ルームサーバの場合はこちらを参照する）
     * 
     * @param orgData チェック対象文字列
     * @return 処理結果(true:異常,false:正常)
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
     * ホテルIPアドレス取得（マイル使用の場合はこちらを参照する）
     * 
     * @param orgData チェック対象文字列
     * @return 処理結果(true:異常,false:正常)
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
     * ホテルIPアドレス取得
     * 
     * @param orgData チェック対象文字列
     * @return 処理結果(true:異常,false:正常)
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
     * ホテルIPアドレス取得
     * 
     * @param orgData チェック対象文字列
     * @return 処理結果(true:異常,false:正常)
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
     * ホテルIPアドレス変換
     * 
     * @param targetIP 元のIPアドレス
     * @return 処理結果(変換後のIPアドレス)
     */
    public static String convertIP(String targetIP)
    {
        String unusableIP = "172.16.48";
        String convertIP = "172.25.188";
        // IPアドレスを変換（取得されるIPアドレスが172.16.48.XXXののとき、172.25.188.XXXに変換する。#34317）
        // GCPのクラウドSQLの帯域が　172.16.48.0/24 となっており重なってしまったホテルは通信できないのでDB上のIPアドレスを172.25.188.XXXに変更したため
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
