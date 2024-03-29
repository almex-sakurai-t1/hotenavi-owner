/*
 * @(#)DateEdit.java 2.00 2004/03/18
 * Copyright (C) ALMEX Inc. 2004
 * 日付･時間取得クラス
 */

package com.hotenavi2.common;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * 現在日付・時刻の取得、編集を行います。
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/18
 */
public class DateEdit implements Serializable
{
    /**
     * 現在日付取得
     * 
     * @param type 取得タイプ(0:YYYY-MM-DD, 1:YYYY/MM/DD, 2:YYYYMMDD)
     * @return 日付
     */
    public String getDate(int type)
    {
        String buf;

        Calendar cal = Calendar.getInstance();

        int year = cal.get( Calendar.YEAR );
        int month = cal.get( Calendar.MONTH ) + 1;
        int day = cal.get( Calendar.DATE );

        NumberFormat nf = new DecimalFormat( "00" );

        switch( type )
        {
            case 0:
                buf = year + "-" + nf.format( month ) + "-" + nf.format( day );
                return(buf);

            case 1:
                buf = year + "/" + nf.format( month ) + "/" + nf.format( day );
                return(buf);

            case 2:
            default:
                buf = year + nf.format( month ) + nf.format( day );
                return(buf);
        }
    }

    /**
     * 現在時刻取得
     * 
     * @param type 取得タイプ(0:HH:MM:SS, 1:HHMMSS, 2:HH:MM, 3:HHMM)
     * @return 時刻
     */
    public String getTime(int type)
    {
        String buf;

        Calendar cal = Calendar.getInstance();

        int hour = cal.get( Calendar.HOUR_OF_DAY );
        int minute = cal.get( Calendar.MINUTE );
        int second = cal.get( Calendar.SECOND );

        NumberFormat nf = new DecimalFormat( "00" );
        switch( type )
        {
            case 0:
                buf = hour + ":" + nf.format( minute ) + ":" + nf.format( second );
                return(buf);

            case 1:
                buf = hour + nf.format( minute ) + nf.format( second );
                return(buf);

            case 2:
                buf = hour + ":" + nf.format( minute );
                return(buf);

            case 3:
            default:
                buf = hour + nf.format( minute );
                return(buf);
        }
    }

    /**
     * 日付年数加算取得
     * 
     * @param ymd 基準日付(YYYYMMDD)
     *            @ @param add 加算年(±)
     * @return 加算後日付(YYYYMMDD)
     */
    public int addYear(int ymd, int add)
    {
        int retymd;
        Calendar cal = Calendar.getInstance();

        cal.set( Calendar.YEAR, (ymd / 10000) );
        cal.set( Calendar.MONTH, (ymd / 100 % 100) - 1 );
        cal.set( Calendar.DATE, (ymd % 100) );

        cal.add( Calendar.YEAR, add );

        retymd = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

        return(retymd);
    }

    /**
     * 日付月数加算取得
     * 
     * @param ymd 基準日付(YYYYMMDD)
     *            @ @param add 加算月(±)
     * @return 加算後日付(YYYYMMDD)
     */
    public int addMonth(int ymd, int add)
    {
        int retymd;
        Calendar cal = Calendar.getInstance();

        cal.set( Calendar.YEAR, (ymd / 10000) );
        cal.set( Calendar.MONTH, (ymd / 100 % 100) - 1 );
        cal.set( Calendar.DATE, (ymd % 100) );

        cal.add( Calendar.MONTH, add );

        retymd = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

        return(retymd);
    }

    /**
     * 日付日数加算取得
     * 
     * @param ymd 基準日付(YYYYMMDD)
     *            @ @param add 加算月(±)
     * @return 加算後日付(YYYYMMDD)
     */
    public int addDay(int ymd, int add)
    {
        int retymd;
        Calendar cal = Calendar.getInstance();

        cal.set( Calendar.YEAR, (ymd / 10000) );
        cal.set( Calendar.MONTH, (ymd / 100 % 100) - 1 );
        cal.set( Calendar.DATE, (ymd % 100) );

        cal.add( Calendar.DATE, add );

        retymd = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

        return(retymd);
    }

    /**
     * 曜日名称取得
     * 
     * @param ymd 取得日付(YYYYMMDD)
     * @return 曜日名
     */
    public String getWeekName(int ymd)
    {
        Calendar cal = Calendar.getInstance();

        cal.set( Calendar.YEAR, (ymd / 10000) );
        cal.set( Calendar.MONTH, (ymd / 100 % 100) - 1 );
        cal.set( Calendar.DATE, (ymd % 100) );

        switch( cal.get( Calendar.DAY_OF_WEEK ) )
        {
            case Calendar.SUNDAY:
                return("日");

            case Calendar.MONDAY:
                return("月");

            case Calendar.TUESDAY:
                return("火");

            case Calendar.WEDNESDAY:
                return("水");

            case Calendar.THURSDAY:
                return("木");

            case Calendar.FRIDAY:
                return("金");

            case Calendar.SATURDAY:
                return("土");

            default:
                return("");
        }
    }

    /**
     * 秒数加算取得
     * 
     * @param ymd 基準日付(YYYYMMDD)
     * @param hms 基準時刻(HHMMSS)
     * @param add 加算秒(±)
     * @return ret[0]加算後日付(YYYYMMDD)
     * @return ret[1]加算後時刻(HHMMSS)
     */
    public static int[] addSec(int ymd, int hsm, int add)
    {
        int ret[] = new int[2];
        NumberFormat nf = new DecimalFormat( "00" );
        Calendar cal = Calendar.getInstance();

        cal.set( Calendar.YEAR, (ymd / 10000) );
        cal.set( Calendar.MONTH, (ymd / 100 % 100) - 1 );
        cal.set( Calendar.DATE, (ymd % 100) );
        cal.set( Calendar.HOUR_OF_DAY, (hsm / 10000) );
        cal.set( Calendar.MINUTE, (hsm / 100 % 100) );
        cal.set( Calendar.SECOND, (hsm % 100) );

        cal.add( Calendar.SECOND, add );

        ret[0] = Integer.parseInt( (cal.get( Calendar.YEAR )) + nf.format( (cal.get( Calendar.MONTH ) + 1) ) + nf.format( cal.get( Calendar.DATE ) ) );
        ret[1] = Integer.parseInt( (cal.get( Calendar.HOUR_OF_DAY )) + nf.format( (cal.get( Calendar.MINUTE )) ) + nf.format( cal.get( Calendar.SECOND ) ) );
        return(ret);
    }

}
