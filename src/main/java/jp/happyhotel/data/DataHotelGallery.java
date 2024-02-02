/*
 * @(#)DataMasterGallery.java 1.00 2010/04/06 Copyright (C) ALMEX Inc. 2010 �M�������[�}�X�^
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �z�e���M�������[�擾�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2010/04/12
 */
public class DataHotelGallery implements Serializable
{

    private static final long serialVersionUID = 6828848679025117913L;
    private int               category;
    private String            categoryName;
    private int               dispIndex;
    private int               id;
    private String            memo;

    /**
     * �f�[�^�����������܂��B
     */
    public DataHotelGallery()
    {
        category = 0;
        id = 0;
        dispIndex = 0;
    }

    public int getCategory()
    {
        return category;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public int getDispIndex()
    {
        return dispIndex;
    }

    public int getId()
    {
        return id;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setCategory(int category)
    {
        this.category = category;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
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
     * �z�e���M�������[�擾
     * 
     * @param id �z�e��ID
     * @param category �M�������[�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int id, int category)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_gallery WHERE id = ? AND category = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, id );
            prestate.setInt( 2, category );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.category = result.getInt( "category" );
                    this.categoryName = result.getString( "category_name" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.id = result.getInt( "id" );
                    this.memo = result.getString( "memo" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelGallery.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �z�e���M�������[�ݒ�
     * 
     * @param result �n���f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.category = result.getInt( "category" );
                this.categoryName = result.getString( "category_name" );
                this.dispIndex = result.getInt( "disp_index" );
                this.id = result.getInt( "id" );
                this.memo = result.getString( "memo" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelGallery.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * �z�e���M�������[�ݒ�
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

        query = "INSERT hh_hotel_gallery SET ";
        query = query + " id = ?,";
        query = query + " category = ?,";
        query = query + " category_name = ?,";
        query = query + " memo = ?,";
        query = query + " disp_index = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.category );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelGallery.insertData] Exception=" + e.toString() );
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
     * �z�e���M�������[�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param id �z�e��ID
     * @param category �M�������[�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(int id, int category)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_gallery SET ";
        query = query + " category_name = ?,";
        query = query + " memo = ?,";
        query = query + " disp_index = ?";
        query = query + " WHERE id = ? ";
        query = query + " AND category = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, this.categoryName );
            prestate.setString( 2, this.memo );
            prestate.setInt( 3, this.dispIndex );
            prestate.setInt( 4, id );
            prestate.setInt( 5, category );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelGallery.updateData] Exception=" + e.toString() );
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
