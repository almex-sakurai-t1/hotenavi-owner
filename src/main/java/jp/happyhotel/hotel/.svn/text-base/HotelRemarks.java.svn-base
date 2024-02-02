/*
 * @(#)HotelRemarks.java 1.00 2007/09/26 Copyright (C) ALMEX Inc. 2007 ホテル情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * ホテル情報取得クラス
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
     * データを初期化します。
     */
    public HotelRemarks()
    {
        m_hotelRemarksCount = 0;
    }

    /** ホテル備考情報件数取得 **/
    public int getRemarksCount()
    {
        return(m_hotelRemarksCount);
    }

    /** ホテル料金情報取得 **/
    public DataHotelRemarks[] getHotelRemarks()
    {
        return(m_hotelRemarks);
    }

    /**
     * ホテル備考情報取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRemarksData(int hotelId)
    {
        int i;
        int count;
        String query;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // ホテル備考情報の取得
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
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_hotelRemarksCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelRemarks = new DataHotelRemarks[this.m_hotelRemarksCount];
                for( i = 0 ; i < m_hotelRemarksCount ; i++ )
                {
                    m_hotelRemarks[i] = new DataHotelRemarks();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル備考情報の取得
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
     * ホテル備考情報取得
     * 
     * @param hotelId ホテルID
     * @param dispKind 表示箇所種類
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRemarksDataByDispKind(int hotelId, int dispKind)
    {
        int i;
        int count;
        String query;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // ホテル備考情報の取得
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
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_hotelRemarksCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelRemarks = new DataHotelRemarks[this.m_hotelRemarksCount];
                for( i = 0 ; i < m_hotelRemarksCount ; i++ )
                {
                    m_hotelRemarks[i] = new DataHotelRemarks();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル備考情報の取得
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
