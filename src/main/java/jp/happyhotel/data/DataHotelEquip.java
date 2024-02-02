/*
 * @(#)DataHotelEquip.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 �z�e���ݔ����f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �z�e���ݔ����f�[�^�擾�N���X
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
     * �f�[�^�����������܂��B
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
     * �z�e���ݔ����f�[�^�擾
     * 
     * @param hotelId �z�e���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �z�e���ݔ����f�[�^�擾
     * 
     * @param hotelId �z�e���R�[�h
     * @param equipId �@��R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �z�e���ݔ����f�[�^�ݒ�
     * 
     * @param result �z�e���ݔ����f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �z�e���ݔ����f�[�^�ǉ�
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
            // �X�V�Ώۂ̒l���Z�b�g����
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
     * �z�e���ݔ����f�[�^�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param id �z�e��ID
     * @param equipId �ݔ�ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
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

            // �X�V�Ώۂ̒l���Z�b�g����
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
