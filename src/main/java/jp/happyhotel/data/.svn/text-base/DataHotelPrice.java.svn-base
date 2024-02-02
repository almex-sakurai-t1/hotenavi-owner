/*
 * @(#)DataHotelPrice.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテル料金情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテル料金情報データ取得クラス
 *
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/16
 */
public class DataHotelPrice implements Serializable
{
    private static final long serialVersionUID = 1736622921468996878L;
    private int               id;
    private int               dataType;
    private int               seq;
    private int               dispIndex;
    private int               dataFlag;
    private int               startDate;
    private int               endDate;
    private String            title;
    private String            week;
    private int               timeFrom;
    private int               timeTo;
    private int               timeSpan;
    private int               priceFrom;
    private int               priceTo;
    private int               timeFlag;
    private int               maxPriceDisp;

    /**
     * データを初期化します。
     */
    public DataHotelPrice()
    {
        id = 0;
        dataType = 0;
        seq = 0;
        dispIndex = 0;
        dataFlag = 0;
        startDate = 0;
        endDate = 0;
        title = "";
        week = "";
        timeFrom = 0;
        timeTo = 0;
        timeSpan = 0;
        priceFrom = 0;
        priceTo = 0;
        timeFlag = 0;
        maxPriceDisp = 0;
    }

    public int getDataFlag()
    {
        return dataFlag;
    }

    public int getDataType()
    {
        return dataType;
    }

    public int getDispIndex()
    {
        return dispIndex;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getId()
    {
        return id;
    }

    public int getMaxPriceDisp()
    {
        return maxPriceDisp;
    }

    public int getPriceFrom()
    {
        return priceFrom;
    }

    public int getPriceTo()
    {
        return priceTo;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getTimeFlag()
    {
        return timeFlag;
    }

    public int getTimeFrom()
    {
        return timeFrom;
    }

    public int getTimeSpan()
    {
        return timeSpan;
    }

    public int getTimeTo()
    {
        return timeTo;
    }

    public String getTitle()
    {
        return title;
    }

    public String getWeek()
    {
        return week;
    }

    public void setDataFlag(int dataFlag)
    {
        this.dataFlag = dataFlag;
    }

    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMaxPriceDisp(int maxPriceDisp)
    {
        this.maxPriceDisp = maxPriceDisp;
    }

    public void setPriceFrom(int priceFrom)
    {
        this.priceFrom = priceFrom;
    }

    public void setPriceTo(int priceTo)
    {
        this.priceTo = priceTo;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setTimeFlag(int timeFlag)
    {
        this.timeFlag = timeFlag;
    }

    public void setTimeFrom(int timeFrom)
    {
        this.timeFrom = timeFrom;
    }

    public void setTimeSpan(int timeSpan)
    {
        this.timeSpan = timeSpan;
    }

    public void setTimeTo(int timeTo)
    {
        this.timeTo = timeTo;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setWeek(String week)
    {
        this.week = week;
    }

    /**
     * ホテル料金情報データ取得
     *
     * @param hotelId ホテルコード
     * @param dataType 表示タイプ
     * @param seq  管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, int dataType, int seq )
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_price WHERE id = ? AND data_type = ? AND seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, dataType );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.dataType = result.getInt( "data_type" );
                    this.seq = result.getInt( "seq" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.dataFlag = result.getInt( "data_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.title = result.getString( "title" );
                    this.week = result.getString( "week" );
                    this.timeFrom = result.getInt( "time_from" );
                    this.timeTo = result.getInt( "time_to" );
                    this.timeSpan = result.getInt( "time_span" );
                    this.priceFrom = result.getInt( "price_from" );
                    this.priceTo = result.getInt( "price_to" );
                    this.timeFlag = result.getInt( "time_flag" );
                    this.maxPriceDisp = result.getInt( "max_price_disp" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPrice.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル料金情報データ設定
     *
     * @param result ホテル最新情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.dataType = result.getInt( "data_type" );
                this.seq = result.getInt( "seq" );
                this.dispIndex = result.getInt( "disp_index" );
                this.dataFlag = result.getInt( "data_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.title = result.getString( "title" );
                this.week = result.getString( "week" );
                this.timeFrom = result.getInt( "time_from" );
                this.timeTo = result.getInt( "time_to" );
                this.timeSpan = result.getInt( "time_span" );
                this.priceFrom = result.getInt( "price_from" );
                this.priceTo = result.getInt( "price_to" );
                this.timeFlag = result.getInt( "time_flag" );
                this.maxPriceDisp = result.getInt( "max_price_disp" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPrice.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ホテル料金情報データ追加
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

        query = "INSERT hh_hotel_price SET ";
        query = query + " id = ?,";
        query = query + " data_type = ?,";
        query = query + " seq = 0,";
        query = query + " disp_index = ?,";
        query = query + " data_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " title = ?,";
        query = query + " week = ?,";
        query = query + " time_from = ?,";
        query = query + " time_to = ?,";
        query = query + " time_span = ?,";
        query = query + " price_from = ?,";
        query = query + " price_to = ?,";
        query = query + " time_flag = ?,";
        query = query + " max_price_disp = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.dataType );
            prestate.setInt( 3, this.dispIndex );
            prestate.setInt( 4, this.dataFlag );
            prestate.setInt( 5, this.startDate );
            prestate.setInt( 6, this.endDate );
            prestate.setString( 7, this.title );
            prestate.setString( 8, this.week );
            prestate.setInt( 9, this.timeFrom );
            prestate.setInt( 10, this.timeTo );
            prestate.setInt( 11, this.timeSpan );
            prestate.setInt( 12, this.priceFrom );
            prestate.setInt( 13, this.priceTo );
            prestate.setInt( 14, this.timeFlag );
            prestate.setInt( 15, this.maxPriceDisp );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPrice.insertData] Exception=" + e.toString() );
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
     * ホテル料金情報データ更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param dataType 表示タイプ
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int dataType, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_price SET ";
        query = query + " disp_index = ?,";
        query = query + " data_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " title = ?,";
        query = query + " week = ?,";
        query = query + " time_from = ?,";
        query = query + " time_to = ?,";
        query = query + " time_span = ?,";
        query = query + " price_from = ?,";
        query = query + " price_to = ?,";
        query = query + " time_flag = ?,";
        query = query + " max_price_disp = ?";
        query = query + " WHERE id = ? AND data_type = ? AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.dispIndex );
            prestate.setInt( 2, this.dataFlag );
            prestate.setInt( 3, this.startDate );
            prestate.setInt( 4, this.endDate );
            prestate.setString( 5, this.title );
            prestate.setString( 6, this.week );
            prestate.setInt( 7, this.timeFrom );
            prestate.setInt( 8, this.timeTo );
            prestate.setInt( 9, this.timeSpan );
            prestate.setInt( 10, this.priceFrom );
            prestate.setInt( 11, this.priceTo );
            prestate.setInt( 12, this.timeFlag );
            prestate.setInt( 13, this.maxPriceDisp );
            prestate.setInt( 14, id );
            prestate.setInt( 15, dataType );
            prestate.setInt( 16, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPrice.insertData] Exception=" + e.toString() );
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
