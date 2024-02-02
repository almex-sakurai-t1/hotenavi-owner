/*
 * @(#)DateEdit.java 1.00 2007/07/23 Copyright (C) ALMEX Inc. 2007 ���t����Ԏ擾�N���X
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ���ݓ��t�E�����̎擾�A�ҏW���s���܂��B
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/23
 */
public class DateEdit implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -2029503830926144208L;

    /**
     * ���ݓ��t�擾
     * 
     * @param type �擾�^�C�v(0:YYYY-MM-DD, 1:YYYY/MM/DD, 2:YYYYMMDD)
     * @return ���t
     */
    public static String getDate(int type)
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
     * �w����t�擾
     * 
     * @param type �擾�^�C�v(0:YYYY-MM-DD, 1:YYYY/MM/DD, 2:YYYYMMDD)
     * @param year �N
     * @param month ��
     * @param day ��
     * @return ���t
     */
    public static String getDate(int type, int year, int month, int day)
    {
        String buf;

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
    public static String getTime(int type)
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
     * �w����t�擾
     * 
     * @param type �擾�^�C�v(0:YYYY-MM-DD, 1:YYYY/MM/DD, 2:YYYYMMDD,4:YYYY-MM-DD)
     * @param year �N
     * @param month ��
     * @param day ��
     * @return ���t
     */
    public static String getDate(int type, int date)
    {
        String buf;

        NumberFormat nf = new DecimalFormat( "00" );
        String strDate = String.format( "%8d", date );

        int year = Integer.parseInt( strDate ) / 10000;
        int month = Integer.parseInt( strDate ) / 100 % 100;
        int day = Integer.parseInt( strDate ) % 100;

        switch( type )
        {
            case 0:
                buf = year + "-" + nf.format( month ) + "-" + nf.format( day );
                return(buf);

            case 1:
                buf = year + "/" + nf.format( month ) + "/" + nf.format( day );
                return(buf);

            case 2:
                buf = year + nf.format( month ) + nf.format( day );
                return(buf);
            case 3:
                buf = year + "-" + nf.format( month ) + "-" + nf.format( day );
                return(buf);

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
    public static String getTime(int type, int time)
    {
        String buf;

        Calendar cal = Calendar.getInstance();
        String strTime = String.format( "%06d", time );

        int hour = Integer.parseInt( strTime ) / 10000;
        int minute = Integer.parseInt( strTime ) / 100 % 100;
        int second = Integer.parseInt( strTime ) % 100;

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
     * @param add ���Z�N(�})
     * @return ���Z����t(YYYYMMDD)
     */
    public static int addYear(int ymd, int add)
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
     * @param add ���Z��(�})
     * @return ���Z����t(YYYYMMDD)
     */
    public static int addMonth(int ymd, int add)
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
     * @param add ���Z��(�})
     * @return ���Z����t(YYYYMMDD)
     */
    public static int addDay(int ymd, int add)
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

    /**
     * ����t�̌��̍ŏI���t��Ԃ�
     * 
     * @param date ����t(YYYYMMDD)
     * @return ���l(YYYYMMDD)
     */
    public static int getLastDayOfMonth(int date)
    {
        Calendar cal;
        int ret;

        cal = Calendar.getInstance();
        // ����̌��̂P�����Z�b�g
        cal.set( date / 10000, (date / 100 % 100) - 1, 1 );
        // ����
        cal.add( Calendar.MONTH, 1 );
        // �O��
        cal.add( Calendar.DATE, -1 );

        ret = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );
        return ret;
    }

    /**
     * �j�����̎擾
     * 
     * @param ymd �擾���t(YYYYMMDD)
     * @return �j����
     */
    public static String getWeekName(int ymd)
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
     * �j��Index�擾
     * 
     * @param ymd �擾���t(YYYYMMDD)
     * @return �j��Index(0:���j-6:�y�j)
     */
    public static int getWeekIndex(int ymd)
    {
        Calendar cal = Calendar.getInstance();

        cal.set( Calendar.YEAR, (ymd / 10000) );
        cal.set( Calendar.MONTH, (ymd / 100 % 100) - 1 );
        cal.set( Calendar.DATE, (ymd % 100) );

        return(cal.get( Calendar.DAY_OF_WEEK ) - 1);
    }

    /**
     * ���t�̑Ó����`�F�b�N
     * 
     * @param year �N(YYYY)
     * @param month �N(MM)
     * @param day �N(DD)
     * @return ���ʌ���(true:������,false�F�������Ȃ�)
     */
    public static boolean checkDate(int year, int month, int day)
    {
        boolean ret = false;
        String date;
        date = Integer.toString( year ) + "/" + Integer.toString( month ) + "/" + Integer.toString( day );

        DateFormat format = DateFormat.getDateInstance();
        // ���t/������͂������ɍs�����ǂ�����ݒ肷��B
        format.setLenient( false );

        try
        {
            format.parse( date );
            ret = true;
        }
        catch ( Exception e )
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * ���t�̑Ó����`�F�b�N
     * 
     * @param date ���t(YYYY/MM/DD �܂���YYYY-MM-DD)
     * @return ���ʌ���(true:������,false�F�������Ȃ�)
     */
    public static boolean checkDate(String date)
    {
        final int DATE_LENGTH = 8;
        boolean ret = false;

        // null���󔒂�������G���[
        if ( (date != null) && (date.equals( "" ) == false) )
        {
            date = date.replace( '-', '/' );
        }
        else
        {
            return false;
        }

        DateFormat format = DateFormat.getDateInstance();
        // ���t/������͂������ɍs�����ǂ�����ݒ肷��B
        format.setLenient( false );

        if ( (CheckString.numCheck( date ) != false) && (date.length() == DATE_LENGTH) )
        {
            date = date.substring( 0, 4 ) + "/" + date.substring( 4, 6 ) + "/" + date.substring( 6 );
        }

        try
        {
            format.parse( date );
            ret = true;
        }
        catch ( Exception e )
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * �����̑Ó����`�F�b�N
     * 
     * @param time ����(HHMMSS)
     * @return ���ʌ���(true:������,false�F�������Ȃ�)
     */
    public static boolean checkTime(int time)
    {
        boolean ret = true;
        String strTime = "";
        int hour;
        int minute;
        int second;

        // null���󔒂�������G���[
        if ( (time < 0) && (time > 240000) )
        {
            return false;
        }
        strTime = String.format( "%06d", time );
        try
        {
            hour = Integer.parseInt( strTime.substring( 0, 2 ) );
            minute = Integer.parseInt( strTime.substring( 2, 4 ) );
            second = Integer.parseInt( strTime.substring( 4, 6 ) );

            // 0������23���͈̔͊O���ƃG���[
            if ( hour < 0 || hour > 23 )
            {
                ret = false;
            }
            // 0������59���͈̔͊O���ƃG���[
            if ( minute < 0 || minute > 59 )
            {
                ret = false;
            }
            // 0�b����59�͈̔͊O���ƃG���[
            if ( second < 0 || second > 59 )
            {
                ret = false;
            }

        }
        catch ( Exception e )
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * �L�������`�F�b�N(���ݎ��Ԃ��L�����������ǂ������m�F)
     * 
     * @param date ����t(YYYYMMDD�Ŏw��)
     * @param time �����(HHMMSS�Ŏw��)
     * @param kind ���(0�F�N�A1�F���A2�F���A3�F���ԁA4�F���A5�F�b)
     * @param elapsedTime �o�ߎ��ԁi�P�ʂ�kind���Q�Ɓj
     * @return ���ʌ���(true:�L��������,false�F�L�������؂�)
     */
    public static boolean isValidDate(int date, int time, int kind, int elapsedTime)
    {
        int nResult;
        int nYear;
        int nMonth;
        int nDay;
        int nHour;
        int nMinute;
        int nSecond;
        NumberFormat nf;
        Calendar cal = Calendar.getInstance(); // �L�������`�F�b�N���s���J�����_�[
        Calendar nowCal = Calendar.getInstance(); // ���݂̃J�����_�[
        String strCheckTime;

        nYear = date / 10000;
        nMonth = date % 10000 / 100 - 1;
        nDay = date % 100;

        // ������6�������̏ꍇ������̂ŁA�t�H�[�}�b�g
        nf = new DecimalFormat( "000000" );
        strCheckTime = nf.format( time );
        nHour = Integer.parseInt( strCheckTime.substring( 0, 2 ) );
        nMinute = Integer.parseInt( strCheckTime.substring( 2, 4 ) );
        nSecond = Integer.parseInt( strCheckTime.substring( 4, 6 ) );

        cal.clear();
        cal.set( nYear, nMonth, nDay, nHour, nMinute, nSecond );
        // �o�ߎ��Ԃ�����΃J�����_�[�𑀍삷��
        if ( elapsedTime > 0 )
        {
            switch( kind )
            {
                case 0:
                    cal.add( Calendar.YEAR, elapsedTime );
                    break;
                case 1:
                    cal.add( Calendar.MONTH, elapsedTime );
                    break;
                case 2:
                    cal.add( Calendar.DATE, elapsedTime );
                    break;
                case 3:
                    cal.add( Calendar.HOUR_OF_DAY, elapsedTime );
                    break;
                case 4:
                    cal.add( Calendar.MINUTE, elapsedTime );
                    break;
                case 5:
                    cal.add( Calendar.SECOND, elapsedTime );
                    break;
                default:
                    cal.add( Calendar.DATE, elapsedTime );
                    break;
            }
        }

        // ���̎��Ԃ������؂ꎞ�Ԃ��i��ł�����L�������؂�
        nResult = cal.compareTo( nowCal );
        if ( nResult == -1 )
        {
            return(false);
        }
        else if ( nResult == 0 || nResult == 1 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /*****
     * �L�������`�F�b�N(���ݎ��Ԃ��L�����������ǂ������m�F)
     * 
     * @param date ����t(YYYYMMDD�Ŏw��)
     * @param time �����(HHMMSS�Ŏw��)
     * @param date2 ����t(YYYYMMDD�Ŏw��)
     * @param time2 �����(HHMMSS�Ŏw��)
     * @param kind ���(0�F�N�A1�F���A2�F���A3�F���ԁA4�F���A5�F�b)
     * @param elapsedTime �o�ߎ��ԁi�P�ʂ�kind���Q�Ɓj
     * @return
     */
    public static boolean isValidDate(int date, int time, int date2, int time2, int kind, int elapsedTime)
    {
        int nResult;
        int nYear1;
        int nYear2;
        int nMonth1;
        int nMonth2;
        int nDay1;
        int nDay2;
        int nHour1;
        int nHour2;
        int nMinute1;
        int nMinute2;
        int nSecond1;
        int nSecond2;
        NumberFormat nf;
        Calendar cal1 = Calendar.getInstance(); // �L�������`�F�b�N���s���J�����_�[
        Calendar cal2 = Calendar.getInstance(); // �`�F�b�N�C�����̎���
        String strCheckTime;

        // �J�����_�[1�̓��t�E�������Z�b�g����
        nYear1 = date / 10000;
        nMonth1 = date % 10000 / 100 - 1;
        nDay1 = date % 100;

        // ������6�������̏ꍇ������̂ŁA�t�H�[�}�b�g
        nf = new DecimalFormat( "000000" );
        strCheckTime = nf.format( time );
        nHour1 = Integer.parseInt( strCheckTime.substring( 0, 2 ) );
        nMinute1 = Integer.parseInt( strCheckTime.substring( 2, 4 ) );
        nSecond1 = Integer.parseInt( strCheckTime.substring( 4, 6 ) );

        cal1.clear();
        cal1.set( nYear1, nMonth1, nDay1, nHour1, nMinute1, nSecond1 );

        // �J�����_�[2�̓��t�E�������Z�b�g����
        nYear2 = date2 / 10000;
        nMonth2 = date2 % 10000 / 100 - 1;
        nDay2 = date2 % 100;

        nf = new DecimalFormat( "000000" );
        strCheckTime = nf.format( time2 );
        nHour2 = Integer.parseInt( strCheckTime.substring( 0, 2 ) );
        nMinute2 = Integer.parseInt( strCheckTime.substring( 2, 4 ) );
        nSecond2 = Integer.parseInt( strCheckTime.substring( 4, 6 ) );

        cal2.clear();
        cal2.set( nYear2, nMonth2, nDay2, nHour2, nMinute2, nSecond2 );

        // �o�ߎ��Ԃ�����΃J�����_�[�𑀍삷��
        if ( elapsedTime > 0 )
        {
            switch( kind )
            {
                case 0:
                    cal1.add( Calendar.YEAR, elapsedTime );
                    break;
                case 1:
                    cal1.add( Calendar.MONTH, elapsedTime );
                    break;
                case 2:
                    cal1.add( Calendar.DATE, elapsedTime );
                    break;
                case 3:
                    cal1.add( Calendar.HOUR_OF_DAY, elapsedTime );
                    break;
                case 4:
                    cal1.add( Calendar.MINUTE, elapsedTime );
                    break;
                case 5:
                    cal1.add( Calendar.SECOND, elapsedTime );
                    break;
                default:
                    cal1.add( Calendar.DATE, elapsedTime );
                    break;
            }
        }

        // ���̎��Ԃ������؂ꎞ�Ԃ��i��ł�����L�������؂�
        nResult = cal1.compareTo( cal2 );
        if ( nResult == -1 )
        {
            return(false);
        }
        else if ( nResult == 0 || nResult == 1 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �o�ߎ��Ԏ擾
     * 
     * @param date ����t(YYYYMMDD�Ŏw��)
     * @param time �����(HHMMSS�Ŏw��)
     * @param kind ���(0�F�N�A1�F���A2�F���A3�F���ԁA4�F���A5�F�b)
     * @param elapsedTime �o�ߎ��ԁi�P�ʂ�kind���Q�Ɓj
     * @return ���ʌ���(true:�L��������,false�F�L�������؂�)
     */
    public static String elapsedDate(int date, int time, int kind, int elapsedTime)
    {
        int nResult;
        int nYear;
        int nMonth;
        int nDay;
        int nHour;
        int nMinute;
        int nSecond;
        NumberFormat nf;
        Calendar cal = Calendar.getInstance(); // �L�������`�F�b�N���s���J�����_�[
        Calendar nowCal = Calendar.getInstance(); // ���݂̃J�����_�[
        String strCheckTime;

        nYear = date / 10000;
        nMonth = date % 10000 / 100 - 1;
        nDay = date % 100;

        // ������6�������̏ꍇ������̂ŁA�t�H�[�}�b�g
        nf = new DecimalFormat( "000000" );
        strCheckTime = nf.format( time );
        nHour = Integer.parseInt( strCheckTime.substring( 0, 2 ) );
        nMinute = Integer.parseInt( strCheckTime.substring( 2, 4 ) );
        nSecond = Integer.parseInt( strCheckTime.substring( 4, 6 ) );

        cal.clear();
        cal.set( nYear, nMonth, nDay, nHour, nMinute, nSecond );
        // �o�ߎ��Ԃ�����΃J�����_�[�𑀍삷��
        if ( elapsedTime != 0 )
        {
            switch( kind )
            {
                case 0:
                    cal.add( Calendar.YEAR, elapsedTime );
                    break;
                case 1:
                    cal.add( Calendar.MONTH, elapsedTime );
                    break;
                case 2:
                    cal.add( Calendar.DATE, elapsedTime );
                    break;
                case 3:
                    cal.add( Calendar.HOUR_OF_DAY, elapsedTime );
                    break;
                case 4:
                    cal.add( Calendar.MINUTE, elapsedTime );
                    break;
                case 5:
                    cal.add( Calendar.SECOND, elapsedTime );
                    break;
                default:
                    cal.add( Calendar.DATE, elapsedTime );
                    break;
            }

        }

        int year = cal.get( Calendar.YEAR );
        int month = cal.get( Calendar.MONTH ) + 1;
        int day = cal.get( Calendar.DATE );

        String strDate = Integer.toString( year ) + String.format( "%1$02d", month ) + String.format( "%1$02d", day );
        try
        {
            return(strDate);
        }
        catch ( Exception e )
        {
            Logging.error( "[DateEdit.elapsedDate()]Exception:" + e.toString() );
            return("");
        }
    }

    /**
     * �o�ߎ��Ԏ擾
     * 
     * @param date ����t(YYYYMMDD�Ŏw��)
     * @param time �����(HHMMSS�Ŏw��)
     * @param kind ���(0�F�N�A1�F���A2�F���A3�F���ԁA4�F���A5�F�b)
     * @param elapsedTime �o�ߎ��ԁi�P�ʂ�kind���Q�Ɓj
     * @return ���ʌ���(true:�L��������,false�F�L�������؂�)
     */
    public static String elapsedTime(int date, int time, int kind, int elapsedTime)
    {
        int nResult;
        int nYear;
        int nMonth;
        int nDay;
        int nHour;
        int nMinute;
        int nSecond;
        NumberFormat nf;
        Calendar cal = Calendar.getInstance(); // �L�������`�F�b�N���s���J�����_�[
        Calendar nowCal = Calendar.getInstance(); // ���݂̃J�����_�[
        String strCheckTime;

        nYear = date / 10000;
        nMonth = date % 10000 / 100 - 1;
        nDay = date % 100;

        // ������6�������̏ꍇ������̂ŁA�t�H�[�}�b�g
        nf = new DecimalFormat( "000000" );
        strCheckTime = nf.format( time );
        nHour = Integer.parseInt( strCheckTime.substring( 0, 2 ) );
        nMinute = Integer.parseInt( strCheckTime.substring( 2, 4 ) );
        nSecond = Integer.parseInt( strCheckTime.substring( 4, 6 ) );

        cal.clear();
        cal.set( nYear, nMonth, nDay, nHour, nMinute, nSecond );
        // �o�ߎ��Ԃ�����΃J�����_�[�𑀍삷��
        if ( elapsedTime != 0 )
        {
            switch( kind )
            {
                case 0:
                    cal.add( Calendar.YEAR, elapsedTime );
                    break;
                case 1:
                    cal.add( Calendar.MONTH, elapsedTime );
                    break;
                case 2:
                    cal.add( Calendar.DATE, elapsedTime );
                    break;
                case 3:
                    cal.add( Calendar.HOUR_OF_DAY, elapsedTime );
                    break;
                case 4:
                    cal.add( Calendar.MINUTE, elapsedTime );
                    break;
                case 5:
                    cal.add( Calendar.SECOND, elapsedTime );
                    break;
                default:
                    cal.add( Calendar.DATE, elapsedTime );
                    break;
            }
        }
        int hour = cal.get( Calendar.HOUR_OF_DAY );
        int minute = cal.get( Calendar.MINUTE );
        int second = cal.get( Calendar.SECOND );

        String strTime = String.format( "%1$02d", hour ) + String.format( "%1$02d", minute ) + String.format( "%1$02d", second );

        try
        {
            return(strTime);
        }
        catch ( Exception e )
        {
            Logging.error( "[DateEdit.elapsedTime()]Exception:" + e.toString() );
            return("");
        }

    }

    /**
     * ���ݓ��t�擾
     * 
     * @param type �擾�^�C�v(0:YYYY�NMM��DD��(�j��), 1:MM��DD��(�j��), 3:YYYY�NMM��DD��, 4:MM��DD��, 5:HH/MM/DD, 6:HH.MM.DD 7:YYYY/MM/DD(�j��))
     * @return ���t
     */
    public static String formatDate(int type, int date)
    {
        String buf;
        String week;

        switch( type )
        {
            case 0:
                buf = String.format( "%04d�N%02d��%02d��", date / 10000, date % 10000 / 100, date % 100 );
                week = DateEdit.getWeekName( date );
                buf += "(" + week + ")";
                return(buf);

            case 1:
                buf = String.format( "%02d��%02d��", date % 10000 / 100, date % 100 );
                week = DateEdit.getWeekName( date );
                buf += "(" + week + ")";
                return(buf);

            case 2:
                buf = String.format( "%02d��%02d��", date % 10000 / 100, date % 100 );
                week = DateEdit.getWeekName( date );
                buf += "(" + week + ")";
                return(buf);

            case 3:
                buf = String.format( "%04d�N%02d��%02d��", date / 10000, date % 10000 / 100, date % 100 );
                return(buf);

            case 4:
                buf = String.format( "%02d��%02d��", date % 10000 / 100, date % 100 );
                return(buf);

            case 5:
                buf = String.format( "%04d/%02d/%02d", date / 10000, date % 10000 / 100, date % 100 );
                return(buf);

            case 6:
                buf = String.format( "%04d.%02d.%02d", date / 10000, date % 10000 / 100, date % 100 );
                return(buf);

            case 7:
                buf = String.format( "%04d/%02d/%02d", date / 10000, date % 10000 / 100, date % 100 );
                week = DateEdit.getWeekName( date );
                buf += "(" + week + ")";
                return(buf);

            default:
                buf = String.format( "%04d.%02d.%02d", date / 10000, date % 10000 / 100, date % 100 );
                return(buf);

        }
    }

    /**
     * �w�莞���擾
     * 
     * @param type �擾�^�C�v(0:HH:MM:SS, 1:HHMMSS, 2:HH:MM, 3:HHMM, 4:HH:MM��0����, 5:24���\�LHH:MM, 6:HH����MM��)
     * @return ����
     */
    public static String formatTime(int type, int time)
    {
        String buf;

        Calendar cal = Calendar.getInstance();
        String strTime = String.format( "%06d", time );

        int hour = Integer.parseInt( strTime.substring( 0, 2 ) );
        int minute = Integer.parseInt( strTime.substring( 2, 4 ) );
        int second = Integer.parseInt( strTime.substring( 4 ) );

        NumberFormat nf = new DecimalFormat( "00" );
        switch( type )
        {
            case 0:
                buf = nf.format( hour ) + ":" + nf.format( minute ) + ":" + nf.format( second );
                return(buf);

            case 1:
                buf = nf.format( hour ) + nf.format( minute ) + nf.format( second );
                return(buf);

            case 2:
                buf = nf.format( hour ) + ":" + nf.format( minute );
                return(buf);

            case 3:
                buf = nf.format( hour ) + nf.format( minute );
                return(buf);
            case 4:
                buf = nf.format( hour ) + ":" + nf.format( minute );
                return(buf);
            case 5:
                buf = nf.format( hour + 24 ) + ":" + nf.format( minute );
                return(buf);
            case 6:
            default:
                buf = hour + "����";
                if ( minute > 0 )
                {
                    buf += nf.format( minute ) + "��";
                }
                return(buf);

        }
    }

    /**
     * �j�����̎擾
     * 
     * @param date�@���t
     * @return �j������
     */
    /* 2015.04.17 sakurai �j���@�����ɂ��A20150811���u�R�̓��v�ǉ� */
    public static String getHolidayName(int date)
    {
        String[][] nationalHolidaysA = {
                { "0101", "���U" },
                { "0102", "����" },
                { "0103", "����" },
                { "0211", "�����L�O�̓�" },
                { "0429", "���a�̓�" },
                { "0503", "���@�L�O��" },
                { "0504", "�݂ǂ�̓�" },
                { "0505", "���ǂ��̓�" },
                { "1103", "�����̓�" },
                { "1123", "�ΘJ���ӂ̓�" }
        };
        String[][] nationalHolidaysB = {
                { "20120109", "���l�̓�" },
                { "20120320", "�t���̓�" },
                { "20120430", "�U�֋x��" },
                { "20120716", "�C�̓�" },
                { "20120917", "�h�V�̓�" },
                { "20120922", "�H���̓�" },
                { "20121008", "�̈�̓�" },
                { "20121223", "�V�c�a����" },
                { "20121224", "�U�֋x��" },
                { "20130114", "���l�̓�" },
                { "20130320", "�t���̓�" },
                { "20130506", "�U�֋x��" },
                { "20130715", "�C�̓�" },
                { "20130916", "�h�V�̓�" },
                { "20130923", "�H���̓�" },
                { "20131014", "�̈�̓�" },
                { "20131104", "�U�֋x��" },
                { "20131223", "�V�c�a����" },
                { "20140113", "���l�̓�" },
                { "20140321", "�t���̓�" },
                { "20140506", "�U�֋x��" },
                { "20140721", "�C�̓�" },
                { "20140915", "�h�V�̓�" },
                { "20140923", "�H���̓�" },
                { "20141013", "�̈�̓�" },
                { "20141124", "�U�֋x��" },
                { "20141223", "�V�c�a����" },
                { "20150112", "���l�̓�" },
                { "20150321", "�t���̓�" },
                { "20150506", "�U�֋x��" },
                { "20150720", "�C�̓�" },
                { "20150921", "�h�V�̓�" },
                { "20150922", "�����̋x��" },
                { "20150923", "�H���̓�" },
                { "20151012", "�̈�̓�" },
                { "20151223", "�V�c�a����" },
                { "20160111", "���l�̓�" },
                { "20160320", "�t���̓�" },
                { "20160321", "�U�֋x��" },
                { "20160718", "�C�̓�" },
                { "20160811", "�R�̓�" },
                { "20160919", "�h�V�̓�" },
                { "20160922", "�H���̓�" },
                { "20161010", "�̈�̓�" },
                { "20161223", "�V�c�a����" },
                { "20170109", "���l�̓�" },
                { "20170320", "�t���̓�" },
                { "20170717", "�C�̓�" },
                { "20170811", "�R�̓�" },
                { "20170918", "�h�V�̓�" },
                { "20170923", "�H���̓�" },
                { "20171009", "�̈�̓�" },
                { "20171223", "�V�c�a����" },
                { "20180108", "���l�̓�" },
                { "20180212", "�U�֋x��" },
                { "20180321", "�t���̓�" },
                { "20180430", "�U�֋x��" },
                { "20180716", "�C�̓�" },
                { "20180811", "�R�̓�" },
                { "20180917", "�h�V�̓�" },
                { "20180923", "�H���̓�" },
                { "20180924", "�U�֋x��" },
                { "20181008", "�̈�̓�" },
                { "20181223", "�V�c�a����" },
                { "20181224", "�U�֋x��" },
                { "20190114", "���l�̓�" },
                { "20190321", "�t���̓�" },
                { "20190430", "�����̋x��" },
                { "20190501", "�V�c�̑��ʂ̓�" },
                { "20190502", "�����̋x��" },
                { "20190506", "�U�֋x��" },
                { "20190715", "�C�̓�" },
                { "20190811", "�R�̓�" },
                { "20190812", "�U�֋x��" },
                { "20190916", "�h�V�̓�" },
                { "20190923", "�H���̓�" },
                { "20191014", "�̈�̓�" },
                { "20191022", "���ʗ琳�a�̋V�̍s�����" },
                { "20191104", "�U�֋x��" },
                { "20200113", "���l�̓�" },
                { "20200223", "�V�c�a����" },
                { "20200224", "�U�֋x��" },
                { "20200320", "�t���̓�" },
                { "20200506", "�U�֋x��" },
                { "20200723", "�C�̓�" },
                { "20200724", "�X�|�[�c�̓�" },
                { "20200810", "�R�̓�" },
                { "20200921", "�h�V�̓�" },
                { "20200922", "�H���̓�" },
                { "20210111", "���l�̓�" },
                { "20210223", "�V�c�a����" },
                { "20210320", "�t���̓�" },
                { "20210722", "�C�̓�" },
                { "20210723", "�X�|�[�c�̓�" },
                { "20210808", "�R�̓�" },
                { "20210809", "�U�֋x��" },
                { "20210920", "�h�V�̓�" },
                { "20210923", "�H���̓�" },
                { "20220110", "���l�̓�" },
                { "20220223", "�V�c�a����" },
                { "20220321", "�t���̓�" },
                { "20220718", "�C�̓�" },
                { "20220811", "�R�̓�" },
                { "20220919", "�h�V�̓�" },
                { "20220923", "�H���̓�" },
                { "20221010", "�X�|�[�c�̓�" },
                { "20230109", "���l�̓�" },
                { "20230223", "�V�c�a����" },
                { "20230321", "�t���̓�" },
                { "20230717", "�C�̓�" },
                { "20230811", "�R�̓�" },
                { "20230918", "�h�V�̓�" },
                { "20230923", "�H���̓�" },
                { "20231009", "�X�|�[�c�̓�" }
        };
        String d = Integer.toString( date ).substring( 4, 8 );
        for( int i = 0 ; i < nationalHolidaysB.length ; i++ )
        {
            if ( nationalHolidaysB[i][0].equals( Integer.toString( date ) ) )
            {
                return nationalHolidaysB[i][1];
            }
        }
        for( int i = 0 ; i < nationalHolidaysA.length ; i++ )
        {
            if ( nationalHolidaysA[i][0].equals( d ) )
            {
                return nationalHolidaysA[i][1];
            }
        }
        return "";
    }

    /**
     * �w����͏j�����H
     * 
     * @param date ����t(YYYYMMDD�Ŏw��)
     * @return
     */
    public static boolean isHoliday(int date)
    {
        // �����񂪕Ԃ��Ă�����true
        return(0 != getHolidayName( date ).length());
    }

    /**
     * �����擾
     * 
     * @param date �J�n��(YYYYMMDD�Ŏw��)
     * @param dateTo �I����(YYYYMMDD�Ŏw��)
     * @return ����
     */
    public static int differenceDays(int dateFrom, int dateTo) throws Exception
    {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.setLenient( false );
        simpleDateFormat.applyPattern( "yyyyMMdd" );
        Date date1 = simpleDateFormat.parse( String.valueOf( dateFrom ) );
        Date date2 = simpleDateFormat.parse( String.valueOf( dateTo ) );
        long datetime1 = date1.getTime();
        long datetime2 = date2.getTime();
        long one_date_time = 1000 * 60 * 60 * 24;
        long diffDays = (datetime2 - datetime1) / one_date_time;
        return (int)diffDays;

    }

}