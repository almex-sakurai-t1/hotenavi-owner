/*
 * @(#)HotelCoupon.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテルクーポン情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelHappie;

/**
 * ホテルハピー情報取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2011/04/28
 */
public class HotelHappie implements Serializable
{
    private static final long serialVersionUID = 854731495746514358L;
    private int               m_hotelHappieCount;
    private DataHotelHappie   m_hotelHappie;
    private DataHotelHappie[] m_hotelHappieMulti;

    /**
     * データを初期化します。
     */
    public HotelHappie()
    {
        m_hotelHappieCount = 0;
    }

    /** ホテルクーポン情報取得 **/
    public int getHotelCouponCount()
    {
        return(m_hotelHappieCount);
    }

    public DataHotelHappie getHotelHappie()
    {
        return(m_hotelHappie);
    }

    public DataHotelHappie[] getHotelHappieMulti()
    {
        return(m_hotelHappieMulti);
    }

    /**
     * ハピー情報取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM  hh_hotel_happie WHERE id = ?";
        query += " AND start_date <=" + DateEdit.getDate( 2 );
        query += " AND end_date >=" + DateEdit.getDate( 2 );
        query += " ORDER BY seq DESC LIMIT 0,1";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.m_hotelHappieCount = result.getRow();
                }
                this.m_hotelHappie = new DataHotelHappie();

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテルクーポン情報の取得
                    this.m_hotelHappie.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelHappie.getData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ハピー情報取得
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataMulti(int hotelId, int seq)
    {
        int count = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM  hh_hotel_happie WHERE id = ?";
        query += " AND end_date >=" + DateEdit.getDate( 2 );
        query += " AND seq > ?";
        query += " ORDER BY start_date, seq DESC  LIMIT 0,2";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( result.last() != false )
                    {
                        this.m_hotelHappieCount = result.getRow();
                    }
                    this.m_hotelHappieMulti = new DataHotelHappie[this.m_hotelHappieCount];

                    result.beforeFirst();
                    count = 0;
                    while( result.next() != false )
                    {
                        this.m_hotelHappieMulti[count] = new DataHotelHappie();
                        // ホテルクーポン情報の取得
                        this.m_hotelHappieMulti[count].setData( result );
                        count++;
                    }
                }
                else
                {
                    this.m_hotelHappieCount = 0;
                }
            }
            else
            {
                this.m_hotelHappieCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelHappie.getData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( this.m_hotelHappieCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }
}
