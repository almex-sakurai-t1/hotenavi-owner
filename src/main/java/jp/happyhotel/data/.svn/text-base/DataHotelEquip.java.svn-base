/*
 * @(#)DataHotelEquip.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテル設備情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテル設備情報データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/16
 */
public class DataHotelEquip implements Serializable
{
    /**
    *
    */
    private static final long serialVersionUID = -624324030367675873L;

    private int               id;
    private int               equipId;
    private int               equipType;
    private int               equipRental;
    private String            memo;

    /**
     * データを初期化します。
     */
    public DataHotelEquip()
    {
        id = 0;
        equipId = 0;
        equipType = 0;
        equipRental = 0;
        memo = "";
    }

    public int getEquipId()
    {
        return equipId;
    }

    public int getEquipRental()
    {
        return equipRental;
    }

    public int getEquipType()
    {
        return equipType;
    }

    public int getId()
    {
        return id;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setEquipId(int equipId)
    {
        this.equipId = equipId;
    }

    public void setEquipRental(int equipRental)
    {
        this.equipRental = equipRental;
    }

    public void setEquipType(int equipType)
    {
        this.equipType = equipType;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    /**
     * ホテル設備情報データ取得
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_master WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.equipId = result.getInt( "equip_id" );
                    this.equipType = result.getInt( "equip_type" );
                    this.equipRental = result.getInt( "equip_rental" );
                    this.memo = result.getString( "memo" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelEquip.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル設備情報データ取得
     * 
     * @param hotelId ホテルコード
     * @param equipId 機器コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, int equipId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = "select * from hh_hotel_equip where id = ? and equip_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, equipId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.equipId = result.getInt( "equip_id" );
                    this.equipType = result.getInt( "equip_type" );
                    this.equipRental = result.getInt( "equip_rental" );
                    this.memo = result.getString( "memo" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelEquip.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * ホテル設備情報データ設定
     * 
     * @param result ホテル設備情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.equipId = result.getInt( "equip_id" );
                this.equipType = result.getInt( "equip_type" );
                this.equipRental = result.getInt( "equip_rental" );
                this.memo = result.getString( "memo" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelEquip.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ホテル設備情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_hotel_equip SET ";
        query = query + " id = ?,";
        query = query + " equip_id = ?,";
        query = query + " equip_type = ?,";
        query = query + " equip_rental = ?,";
        query = query + " memo = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.equipId );
            prestate.setInt( 3, this.equipType );
            prestate.setInt( 4, this.equipRental );
            prestate.setString( 5, this.memo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelEquip.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ホテル設備情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param equipId 設備ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int equipId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE research_hotel_equip SET ";
        query = query + " equip_type = ?,";
        query = query + " equip_rental = ?,";
        query = query + " memo = ?";
        query = query + " WHERE id = ? AND equip_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.equipType );
            prestate.setInt( 2, this.equipRental );
            prestate.setString( 3, this.memo );
            prestate.setInt( 4, this.id );
            prestate.setInt( 5, this.equipId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                query = "UPDATE hh_hotel_equip SET ";
                query = query + " equip_type = ?,";
                query = query + " equip_rental = ?,";
                query = query + " memo = ?";
                query = query + " WHERE id = ? AND equip_id = ?";

                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, this.equipType );
                prestate.setInt( 2, this.equipRental );
                prestate.setString( 3, this.memo );
                prestate.setInt( 4, this.id );
                prestate.setInt( 5, this.equipId );

                if ( prestate.executeUpdate() > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelEquip.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
