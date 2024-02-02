/*
 * @(#)AccessLog.java 2.00 2005/01/17
 * Copyright (C) ALMEX Inc. 2005
 * アクセスログ集計クラス
 */
package com.hotenavi2.tool.AccessLog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * アクセスログ集計クラス。
 * 
 * @author S.Shiiya
 * @version 2.00 2004/12/02
 */
public class AccessLogTemp
{
    private static final int PAGENAME_MAX = 99;
    static int               counts       = 0;

    public static void main(String argv[])
    {
        String convdate = "";

        if ( argv.length == 1 )
        {
            convdate = argv[0];
        }

        Debug( "Starting ConvertAccessLog" );

        ConvertAccessLog( convdate );
    }

    private static void ConvertAccessLog(String convdate)
    {
        int i;
        int count;
        int status = 0;
        String pagework;
        String pathname;
        String headname;
        String filename;
        String pagename[];
        String mempagename[];
        String line;
        String data;
        String url = "";
        String referer = "";
        String agent = "";
        String ipaddr = "";
        Calendar acccal = Calendar.getInstance();
        Properties prop;
        FileReader fr;
        BufferedReader br;
        StringTokenizer stoken;
        try
        {
            // 設定ファイルの取得
            prop = new Properties();
            prop.load( new FileInputStream( "/etc/hotenavi/accesslog.conf.pc" ) );

            pathname = prop.getProperty( "pathname" );
            headname = prop.getProperty( "headname", "hotenavi_log" );

            pagename = new String[PAGENAME_MAX];
            for( i = 0 ; i < PAGENAME_MAX ; i++ )
            {
                pagename[i] = prop.getProperty( "page" + (i + 1), "" );
            }

            mempagename = new String[PAGENAME_MAX];
            for( i = 0 ; i < PAGENAME_MAX ; i++ )
            {
                mempagename[i] = prop.getProperty( "mem_page" + (i + 1), "" );
            }

            // アクセスログファイル名
            if ( convdate.compareTo( "" ) != 0 )
            {
                filename = pathname + headname + "." + convdate;
            }
            else
            {
                Calendar cal = Calendar.getInstance();
                cal.add( cal.DATE, -1 );
                convdate = Integer.toString( (cal.get( cal.YEAR ) * 10000 + ((cal.get( cal.MONTH ) + 1) * 100) + cal.get( cal.DATE )) );
                filename = pathname + headname + "." + convdate;
            }

            // アクセスログの取得
            fr = new FileReader( filename );
            br = new BufferedReader( fr );
            while( true )
            {
                line = br.readLine();
                if ( line == null )
                {
                    break;
                }

                count = 0;

                stoken = new StringTokenizer( line, "\"" );
                while( stoken.hasMoreTokens() != false )
                {
                    data = stoken.nextToken().trim();
                    switch( count )
                    {
                        case 0:
                            // IPアドレス・日付
                            ipaddr = getIpAddress( data );
                            acccal = getAccessDate( data );
                            break;

                        case 1:
                            // URL
                            url = getURL( data );
                            break;

                        case 2:
                            // HTTPステータス
                            status = getStatus( data );
                            break;

                        case 3:
                            // Referer
                            referer = getReferer( data );
                            break;

                        case 4:
                            //
                            break;

                        case 5:
                            // USERAGENT
                            agent = getAgent( data );
                            break;
                    }
                    count++;
                }

                if ( status == 200 )
                {
                    if ( ipaddr.compareTo( "122.208.42.42 " ) != 0 )
                    {
                        if ( ipaddr.compareTo( "192.168.210" ) != 0 )
                        {
                            Debug( "url=" + url );
                            // アクセス解析&DB書き込み
                            AccessAnalyze( convdate, acccal, url, status, referer, agent, pagename, mempagename );
                            /**
                             * Debug("accdate=" + (acccal.get(acccal.YEAR) * 10000 + (acccal.get(acccal.MONTH)+1) * 100 + acccal.get(acccal.DATE)) );
                             * Debug("acctime=" + (acccal.get(acccal.HOUR) * 100 + acccal.get(acccal.MINUTE)));
                             * Debug("accweek=" + acccal.get(acccal.WEEK_OF_MONTH));
                             * Debug("url=" + url);
                             * Debug("status=" + status);
                             * Debug("referer=" + referer);
                             * Debug("agent=" + agent);
                             **/
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Debug( e.toString() );
        }
    }

    private static String getIpAddress(String data)
    {
        int start;
        int end;
        String cutdata = "";

        // 開始 から スペース までの間を取得
        end = data.indexOf( " " );
        if ( end > 0 )
        {
            cutdata = data.substring( 0, end );
        }

        return(cutdata);
    }

    private static Calendar getAccessDate(String data)
    {
        int start;
        int end;
        String cutdata = "";
        String ymd = "";
        java.util.Date datetime;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MMM/yyyy:HH:mm:ss Z", Locale.US );

        // [ から ] までの間を取得
        start = data.indexOf( "[" );
        if ( start >= 0 )
        {
            end = data.indexOf( "]", start + 1 );

            cutdata = data.substring( start + 1, end );

            try
            {
                datetime = sdf.parse( cutdata );
                cal.setTime( datetime );

                // 時間を-0900する
                cal.add( cal.HOUR, -9 );
            }
            catch ( Exception e )
            {
                Debug( e.toString() );
            }
        }

        return(cal);
    }

    private static String getURL(String data)
    {
        int count = 0;
        String url = "";
        String cutdata = "";
        StringTokenizer stoken;

        stoken = new StringTokenizer( data, " " );
        while( stoken.hasMoreTokens() != false )
        {
            cutdata = stoken.nextToken().trim();

            if ( count == 1 )
            {
                url = cutdata;
            }

            count++;
        }

        return(url);
    }

    private static int getStatus(String data)
    {
        int status = 0;
        int count = 0;
        String cutdata = "";
        StringTokenizer stoken;

        stoken = new StringTokenizer( data, " " );
        while( stoken.hasMoreTokens() != false )
        {
            cutdata = stoken.nextToken().trim();

            if ( count == 0 )
            {
                try
                {
                    status = Integer.parseInt( cutdata );
                }
                catch ( NumberFormatException ne )
                {
                    Debug( "getStatus Error=" + ne.toString() );
                    status = 500;
                }
            }

            count++;
        }

        return(status);
    }

    private static String getReferer(String data)
    {
        return(data);
    }

    private static String getAgent(String data)
    {
        return(data);
    }

    private static void AccessAnalyze(String convdate, Calendar acccal, String url, int status, String refrere, String agent, String[] pagename, String[] mempagename)
    {
        int i;
        int start;
        int end;
        int retupdate;
        boolean okhotel = false;
        boolean findpage = false;
        boolean image = false;
        boolean style = false;
        String query;
        String cutpage;
        String hotelid = "";
        String pagefield = "";
        String timefield = "";
        String weekfield = "";
        String agentfield = "";
        ResultSet rethotel;
        ResultSet retacclog;
        NumberFormat nf = new DecimalFormat( "00" );
        StringTokenizer stoken;

        String driver;
        String connurl;
        String user;
        String password;
        Properties prop;
        Connection connect = null;
        Statement statehotel;
        Statement stateacclog;
        Statement stateupdate;
        int count = 0;

        // 画像ファイルはカウントしない
        if ( url.indexOf( "/image/" ) >= 0 ||
                url.indexOf( ".gif" ) >= 0 ||
                url.indexOf( ".jpeg" ) >= 0 ||
                url.indexOf( ".jpg" ) >= 0 ||
                url.indexOf( ".swf" ) >= 0 ||
                url.indexOf( ".asf" ) >= 0 )
        {
            return;
        }

        // スタイルシートはカウントしない
        if ( url.indexOf( ".css" ) >= 0 )
        {
            return;
        }

        // JSPファイルのみをカウントする
        // if ( url.indexOf( ".jsp" ) == -1 )
        // {
        // return;
        // }

        try
        {
            // データベースの初期化
            prop = new Properties();
            // ﾌﾟﾛﾊﾟﾃｨﾌｧｲﾙからｷｰと値のﾘｽﾄを読み込みます
            prop.load( new FileInputStream( "/etc/hotenavi/dbconnect.conf" ) );
            // "jdbc.driver"に設定されている値を取得します
            driver = prop.getProperty( "jdbc.driver" );
            // "jdbc.url"に設定されている値を取得します
            connurl = prop.getProperty( "jdbc.url" );
            // "jdbc.user"に設定されている値を取得します
            user = prop.getProperty( "jdbc.user" );
            // "jdbc.url"に設定されている値を取得します
            password = prop.getProperty( "jdbc.password" );

            // MySQLへの接続を確立する
            Class.forName( driver );
            // MySQLへ接続する
            connect = DriverManager.getConnection( connurl, user, password );
        }
        catch ( Exception e )
        {
            Debug( "dbiniterr " + e.toString() );
        }

        // ホテルID解析
        url = url.replace( "http://www.hotenavi.com", "" );
        start = url.indexOf( "/" );
        if ( start >= 0 )
        {
            end = url.indexOf( "/", start + 1 );
            if ( end >= 0 )
            {
                hotelid = url.substring( start + 1, end );
            }
            else
            {
                hotelid = url.substring( start + 1 );
            }
        }

        if ( hotelid.compareTo( "" ) != 0 && image == false && style == false )
        {
            try
            {
                // ホテルID有無
                statehotel = connect.createStatement();
                query = "SELECT * FROM hotel WHERE hotel_id='" + hotelid + "'";
                rethotel = statehotel.executeQuery( query );
                if ( rethotel != null )
                {
                    if ( rethotel.next() != false )
                    {
                        okhotel = true;
                    }
                }
                statehotel.close();
            }
            catch ( Exception e )
            {
                okhotel = false;
            }
        }

        // ホテルあり
        if ( okhotel != false && image == false && style == false )
        {
            // URL分析(メンバーページ)
            for( i = 0 ; i < mempagename.length ; i++ )
            {
                stoken = new StringTokenizer( mempagename[i], "," );
                while( stoken.hasMoreTokens() != false )
                {
                    cutpage = stoken.nextToken().trim();

                    findpage = url.matches( cutpage );
                    if ( findpage != false )
                    {
                        pagefield = "mem_page" + nf.format( i + 1 );
                        break;
                    }
                }

                if ( findpage != false )
                {
                    break;
                }
            }

            if ( findpage == false )
            {
                // URL分析
                for( i = 0 ; i < pagename.length ; i++ )
                {
                    stoken = new StringTokenizer( pagename[i], "," );
                    while( stoken.hasMoreTokens() != false )
                    {
                        cutpage = stoken.nextToken().trim();
                        findpage = url.matches( cutpage );
                        if ( findpage != false )
                        {
                            pagefield = "page" + nf.format( i + 1 );
                            break;
                        }
                    }

                    if ( findpage != false )
                    {
                        break;
                    }
                }
            }

            // ページが見つからなかったまたはホテルIDがhotenaviの場合はその他ページに追加。
            if ( pagefield.compareTo( "" ) == 0 || hotelid.compareTo( "hotenavi" ) == 0 )
            {
                pagefield = "page99";
            }

            // 時間取得
            timefield = "time" + nf.format( acccal.get( acccal.HOUR_OF_DAY ) + 1 );
            // 週番号取得
            weekfield = "week" + nf.format( acccal.get( acccal.DAY_OF_WEEK ) );

            // ユーザエージェント取得
            if ( url.indexOf( "/i/" ) >= 0 )
            {
                agentfield = "docomo";
            }
            else if ( url.indexOf( "/j/" ) >= 0 )
            {
                agentfield = "jphone";
            }
            else if ( url.indexOf( "/ez/" ) >= 0 )
            {
                agentfield = "au";
            }
            else if ( url.indexOf( "/smart/" ) >= 0 )
            {
                agentfield = "smart";
            }
            else
            {
                agentfield = "etc";
            }

            try
            {
                // アクセスログ取得
                query = "SELECT * FROM access_mobile_detail WHERE hotel_id='" + hotelid + "'";
                query = query + " AND acc_date=" + convdate;
                stateacclog = connect.createStatement();
                retacclog = stateacclog.executeQuery( query );
                if ( retacclog != null )
                {
                    counts++;
                    if ( counts > 308584 )
                    {
                        if ( retacclog.next() != false )
                        {
                            query = "UPDATE access_mobile_detail SET ";
                            query = query + "total=total+1" + ",";
                            query = query + agentfield + "=" + agentfield + "+1,";
                            query = query + weekfield + "=" + weekfield + "+1,";
                            query = query + timefield + "=" + timefield + "+1,";
                            query = query + pagefield + "=" + pagefield + "+1";
                            query = query + " WHERE hotel_id='" + hotelid + "'";
                            query = query + " AND acc_date=" + convdate;
                        }
                        else
                        {
                            query = "INSERT INTO access_mobile_detail ";
                            query = query + "(hotel_id, acc_date, total,";
                            query = query + agentfield + ",";
                            query = query + weekfield + ",";
                            query = query + timefield + ",";
                            query = query + pagefield;
                            query = query + " ) VALUES ( ";
                            query = query + "'" + hotelid + "',";
                            query = query + convdate + ",";
                            query = query + 1 + ",";
                            query = query + 1 + ",";
                            query = query + 1 + ",";
                            query = query + 1 + ",";
                            query = query + 1 + ")";
                        }

                        stateupdate = connect.createStatement();
                        retupdate = stateupdate.executeUpdate( query );
                        stateupdate.close();
                    }
                }
                stateacclog.close();
            }
            catch ( Exception e )
            {
                Debug( "logupdate(" + e.toString() + ")" );
            }
        }

        try
        {
            connect.close();
        }
        catch ( Exception e )
        {
        }
    }

    private static void Debug(String output)
    {
        System.out.println( output );
    }
}
