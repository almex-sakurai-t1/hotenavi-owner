/*
 * @(#)DateEdit.java 2.00 2004/03/18
 * Copyright (C) ALMEX Inc. 2004
 * “ú•t¥ŠÔæ“¾ƒNƒ‰ƒX
 */

package com.hotenavi2.common;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * Œ»İ“ú•tE‚Ìæ“¾A•ÒW‚ğs‚¢‚Ü‚·B
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/18
 */
public class DateEdit implements Serializable
{
    /**
     * Œ»İ“ú•tæ“¾
     * 
     * @param type æ“¾ƒ^ƒCƒv(0:YYYY-MM-DD, 1:YYYY/MM/DD, 2:YYYYMMDD)
     * @return “ú•t
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
     * Œ»İæ“¾
     * 
     * @param type æ“¾ƒ^ƒCƒv(0:HH:MM:SS, 1:HHMMSS, 2:HH:MM, 3:HHMM)
     * @return 
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
     * “ú•t”N”‰ÁZæ“¾
     * 
     * @param ymd Šî€“ú•t(YYYYMMDD)
     *            @ @param add ‰ÁZ”N(})
     * @return ‰ÁZŒã“ú•t(YYYYMMDD)
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
     * “ú•tŒ”‰ÁZæ“¾
     * 
     * @param ymd Šî€“ú•t(YYYYMMDD)
     *            @ @param add ‰ÁZŒ(})
     * @return ‰ÁZŒã“ú•t(YYYYMMDD)
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
     * “ú•t“ú”‰ÁZæ“¾
     * 
     * @param ymd Šî€“ú•t(YYYYMMDD)
     *            @ @param add ‰ÁZŒ(})
     * @return ‰ÁZŒã“ú•t(YYYYMMDD)
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
     * —j“ú–¼Ìæ“¾
     * 
     * @param ymd æ“¾“ú•t(YYYYMMDD)
     * @return —j“ú–¼
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
                return("“ú");

            case Calendar.MONDAY:
                return("Œ");

            case Calendar.TUESDAY:
                return("‰Î");

            case Calendar.WEDNESDAY:
                return("…");

            case Calendar.THURSDAY:
                return("–Ø");

            case Calendar.FRIDAY:
                return("‹à");

            case Calendar.SATURDAY:
                return("“y");

            default:
                return("");
        }
    }

    /**
     * •b”‰ÁZæ“¾
     * 
     * @param ymd Šî€“ú•t(YYYYMMDD)
     * @param hms Šî€(HHMMSS)
     * @param add ‰ÁZ•b(})
     * @return ret[0]‰ÁZŒã“ú•t(YYYYMMDD)
     * @return ret[1]‰ÁZŒã(HHMMSS)
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
