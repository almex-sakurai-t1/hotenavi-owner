/*
 * @(#)RsvCalendar.java 1.00 2011/02/21
 * Copyright (C) ALMEX Inc. 2011
 * 予約カレンダークラス
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataRsvCalendar;

/**
 * 予約カレンダー
 * 
 * @author S.Tashiro
 * @version 1.00 2008/04/22
 */
public class RsvCalendar implements Serializable
{
    private static final long             serialVersionUID = 5687860746373887759L;
    int                                   rsvCount;
    ArrayList<ArrayList<DataRsvCalendar>> monthList        = new ArrayList<ArrayList<DataRsvCalendar>>();

    public int getCount()
    {
        return this.rsvCount;
    }

    /***
     * カレンダーのArrayListを取得
     * 
     * @return ArrayList<ArrayList<DataRsvCalendar>>
     */
    public ArrayList<ArrayList<DataRsvCalendar>> getCalendarList()
    {
        return monthList;
    }

    /**
     * カレンダー取得クラス
     * 
     * @param targetYM ターゲット日付（YYYYMM）
     * @return
     */
    public boolean getCalendar(int targetYM)
    {

        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_rsv_calendar" +
                " WHERE cal_date BETWEEN ? AND ? " +
                " ORDER BY cal_date";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, Integer.toString( targetYM ) + "01" );
            prestate.setString( 2, Integer.toString( targetYM ) + "31" );
            ret = getCalendarSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[RsvCalendar.getCalendar()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 予約カレンダーのデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getCalendarSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        boolean ret;
        DataRsvCalendar dataRsvCal;
        ArrayList<DataRsvCalendar> array = null;

        dataRsvCal = new DataRsvCalendar();
        array = new ArrayList<DataRsvCalendar>();

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    rsvCount = result.getRow();
                }
                result.beforeFirst();

                while( result.next() != false )
                {
                    if ( dataRsvCal != null )
                    {
                        dataRsvCal = null;
                        dataRsvCal = new DataRsvCalendar();
                    }

                    dataRsvCal.setData( result );
                    array.add( dataRsvCal );
                    if ( result.getInt( "week" ) == 6 )
                    {
                        monthList.add( array );
                        array = null;
                        array = new ArrayList<DataRsvCalendar>();
                    }
                }
                // 最後のデータが6以外だったらデータを追加する
                if ( dataRsvCal.getWeek() != 6 )
                {
                    monthList.add( array );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getCalendarSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        if ( rsvCount > 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * カレンダー取得クラス
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public boolean getCalendar(int startDate, int endDate)
    {

        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_rsv_calendar" +
                " WHERE cal_date BETWEEN ? AND ? " +
                " ORDER BY cal_date";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, startDate );
            prestate.setInt( 2, endDate );
            ret = getCalendarDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[RsvCalendar.getCalendar(startDate, endDate)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 予約カレンダーのデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getCalendarDataSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        boolean ret;
        DataRsvCalendar dataRsvCal;
        ArrayList<DataRsvCalendar> array = null;

        dataRsvCal = new DataRsvCalendar();
        array = new ArrayList<DataRsvCalendar>();

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    rsvCount = result.getRow();
                }
                result.beforeFirst();

                while( result.next() != false )
                {
                    if ( dataRsvCal != null )
                    {
                        dataRsvCal = null;
                        dataRsvCal = new DataRsvCalendar();
                    }

                    dataRsvCal.setData( result );
                    array.add( dataRsvCal );
                }
                monthList.add( array );
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getCalendarSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        if ( rsvCount > 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

}
