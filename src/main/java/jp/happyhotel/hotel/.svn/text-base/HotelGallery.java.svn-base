/*
 * @(#)HotelRoom.java 1.00 2009/08/18
 * Copyright (C) ALMEX Inc. 2009
 * ホテル部屋情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelGallery;
import jp.happyhotel.data.DataHotelRoomMore;

/**
 * ホテルギャラリー取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2010/04/07
 */
public class HotelGallery implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID = 6804431645880452624L;

    private int                 categoryCount;
    private int[]               categoryCountSub;
    private int                 galleryCount;
    private DataHotelGallery[]  hotelCategory;
    private DataHotelRoomMore[] hotelGallery;

    /**
     * データを初期化します。
     */
    public HotelGallery()
    {
        categoryCount = 0;
        galleryCount = 0;
    }

    /** ホテルカテゴリ情報件数取得 **/
    public int getHotelCategoryCount()
    {
        return(categoryCount);
    }

    /** ホテルカテゴリ情報件数取得 **/
    public int[] getHotelCategoryCountSub()
    {
        return(categoryCountSub);
    }

    /** ホテルギャラリー情報件数取得 **/
    public int getHotelGalleryCount()
    {
        return(galleryCount);
    }

    /** ホテルギャラリーデータ情報取得 **/
    public DataHotelRoomMore[] getHotelGallery()
    {
        return(hotelGallery);
    }

    /** ホテルカテゴリ情報取得 **/
    public DataHotelGallery[] getHotelCategory()
    {
        return(hotelCategory);
    }

    /**
     * ホテルカテゴリ一覧情報取得
     * 
     * @param hotelId ホテルID
     * @param category ギャラリーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getCategoryData(int hotelId, int category)
    {
        int i;
        int dataCount;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_gallery.* FROM hh_hotel_gallery, hh_hotel_room_more"
                + " WHERE hh_hotel_room_more.id = hh_hotel_gallery.id"
                + " AND hh_hotel_room_more.room_rank = hh_hotel_gallery.category"
                + " AND hh_hotel_room_more.disp_flag=2"
                + " AND hh_hotel_gallery.disp_index <> 0"
                + " AND hh_hotel_gallery.id = ?";
        if ( category > 0 )
        {
            query += " AND hh_hotel_gallery.category = ?";

        }
        query += " GROUP BY hh_hotel_gallery.category"
                + " ORDER BY hh_hotel_gallery.disp_index, hh_hotel_gallery.category";

        count = 0;
        this.categoryCount = 0;
        dataCount = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            if ( category > 0 )
            {
                prestate.setInt( 2, category );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.categoryCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelCategory = new DataHotelGallery[this.categoryCount];
                this.categoryCountSub = new int[this.categoryCount];

                for( i = 0 ; i < this.categoryCount ; i++ )
                {
                    hotelCategory[i] = new DataHotelGallery();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.hotelCategory[count].setData( result );
                    this.categoryCountSub[count] = getCountByCategory( hotelId, result.getInt( "category" ) );
                    dataCount += this.categoryCountSub[count];
                    count++;
                }
                this.galleryCount = dataCount;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelGallery.getCategoryData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( this.galleryCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ホテルギャラリー一覧情報取得
     * 
     * @param hotelId ホテルID
     * @param category ギャラリーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getGalleryData(int hotelId, int category)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_room_more.*, hh_hotel_gallery.* FROM hh_hotel_room_more, hh_hotel_gallery"
                + " WHERE hh_hotel_room_more.id = ?"
                + " AND hh_hotel_room_more.id = hh_hotel_gallery.id"
                + " AND hh_hotel_room_more.room_rank = hh_hotel_gallery.category"
                + " AND hh_hotel_room_more.disp_flag = 2"
                + " AND hh_hotel_gallery.disp_index <> 0";
        if ( category > 0 )
        {
            query += " AND hh_hotel_room_more.room_rank = ?";
        }
        query += " ORDER BY hh_hotel_gallery.disp_index, hh_hotel_gallery.category, hh_hotel_room_more.seq";

        count = 0;
        this.galleryCount = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            if ( category > 0 )
            {
                prestate.setInt( 2, category );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.galleryCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelGallery = new DataHotelRoomMore[this.galleryCount];
                this.hotelCategory = new DataHotelGallery[this.galleryCount];

                for( i = 0 ; i < this.galleryCount ; i++ )
                {
                    this.hotelGallery[i] = new DataHotelRoomMore();
                    this.hotelCategory[i] = new DataHotelGallery();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
                    this.hotelGallery[count].setData( result );
                    this.hotelCategory[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelGallery.getGalleryData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * カテゴリのデータ件数取得
     * 
     * @param hotelId ホテルID
     * @param category ギャラリーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getCountByCategory(int hotelId, int category)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT COUNT(*) FROM hh_hotel_room_more"
                + " WHERE id = ?"
                + " AND disp_flag = 2";
        if ( category > 0 )
        {
            query += " AND room_rank = ?";
        }

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            if ( category > 0 )
            {
                prestate.setInt( 2, category );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelGallery.getCategoryData] Exception=" + e.toString() );
            count = -1;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * 部屋データの順番を取得
     * 
     * @param hotelId ホテルID
     * @param category 管理番号
     * @return 処理結果()
     */
    public int getGalleryNumber(int hotelId, int category)
    {
        int i;
        int count;
        count = 0;

        if ( hotelId <= 0 && category <= 0 )
        {
            return(-1);
        }
        for( i = 1 ; i < category ; i++ )
        {
            count += this.getCountByCategory( hotelId, i );
        }

        return(count);
    }
}
