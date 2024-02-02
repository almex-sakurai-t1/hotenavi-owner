/*
 * @(#)DateEdit.java 2.00 2004/03/18
 * Copyright (C) ALMEX Inc. 2004
 * ���t����Ԏ擾�N���X
 */

package com.hotenavi2.common;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * ���ݓ��t�E�����̎擾�A�ҏW���s���܂��B
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/18
 */
public class DateEdit implements Serializable
{
    /**
     * ���ݓ��t�擾
     * 
     * @param type �擾�^�C�v(0:YYYY-MM-DD, 1:YYYY/MM/DD, 2:YYYYMMDD)
     * @return ���t
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
     * ���ݎ����擾
     * 
     * @param type �擾�^�C�v(0:HH:MM:SS, 1:HHMMSS, 2:HH:MM, 3:HHMM)
     * @return ����
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
     * ���t�N�����Z�擾
     * 
     * @param ymd ����t(YYYYMMDD)
     *            @ @param add ���Z�N(�})
     * @return ���Z����t(YYYYMMDD)
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
     * ���t�������Z�擾
     * 
     * @param ymd ����t(YYYYMMDD)
     *            @ @param add ���Z��(�})
     * @return ���Z����t(YYYYMMDD)
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
     * ���t�������Z�擾
     * 
     * @param ymd ����t(YYYYMMDD)
     *            @ @param add ���Z��(�})
     * @return ���Z����t(YYYYMMDD)
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
     * �j�����̎擾
     * 
     * @param ymd �擾���t(YYYYMMDD)
     * @return �j����
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
                return("��");

            case Calendar.MONDAY:
                return("��");

            case Calendar.TUESDAY:
                return("��");

            case Calendar.WEDNESDAY:
                return("��");

            case Calendar.THURSDAY:
                return("��");

            case Calendar.FRIDAY:
                return("��");

            case Calendar.SATURDAY:
                return("�y");

            default:
                return("");
        }
    }

    /**
     * �b�����Z�擾
     * 
     * @param ymd ����t(YYYYMMDD)
     * @param hms �����(HHMMSS)
     * @param add ���Z�b(�})
     * @return ret[0]���Z����t(YYYYMMDD)
     * @return ret[1]���Z�㎞��(HHMMSS)
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
