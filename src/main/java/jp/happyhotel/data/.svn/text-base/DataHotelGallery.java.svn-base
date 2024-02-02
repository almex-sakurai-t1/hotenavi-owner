/*
 * @(#)DataMasterGallery.java 1.00 2010/04/06 Copyright (C) ALMEX Inc. 2010 ギャラリーマスタ
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテルギャラリー取得クラス
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
     * データを初期化します。
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
     * ホテルギャラリー取得
     * 
     * @param id ホテルID
     * @param category ギャラリーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ホテルギャラリー設定
     * 
     * @param result 地方データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ホテルギャラリー設定
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

            // 更新対象の値をセットする
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
     * ホテルギャラリー更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param category ギャラリーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
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

            // 更新対象の値をセットする
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
