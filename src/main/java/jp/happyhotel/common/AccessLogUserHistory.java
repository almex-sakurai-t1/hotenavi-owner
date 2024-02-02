/*
 * @(#)AccessLog.java 2.00 2007/12/03 Copyright (C) ALMEX Inc. 2007 アクセスログ集計クラス
 */
package jp.happyhotel.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import jp.happyhotel.data.DataUserHistory;

/**
 * アクセスログ集計クラス。
 * 
 * @author S.Shiiya
 * @version 2.00 2004/12/02
 */
public class AccessLogUserHistory
{
    private static final int         PAGENAME_MAX      = 9999;

    private static int               collectDate       = 0;
    private static int               totalPv           = 0;
    private static int               pcPv              = 0;
    private static int               docomoPv          = 0;
    private static int               auPv              = 0;
    private static int               softbankPv        = 0;
    private static int[]             timePv            = new int[24];

    private static int[]             collectDateDetail = new int[PAGENAME_MAX];
    private static String[]          targetPageDetail  = new String[PAGENAME_MAX];
    private static int[]             totalPvDetail     = new int[PAGENAME_MAX];
    private static int[]             pcPvDetail        = new int[PAGENAME_MAX];
    private static int[]             docomoPvDetail    = new int[PAGENAME_MAX];
    private static int[]             auPvDetail        = new int[PAGENAME_MAX];
    private static int[]             softbankPvDetail  = new int[PAGENAME_MAX];
    private static int[][]           timePvDetail      = new int[PAGENAME_MAX][24];

    private static int[]             collectDateExt    = new int[PAGENAME_MAX];
    private static String[]          targetPageExt     = new String[PAGENAME_MAX];
    private static String[]          targetQueryExt    = new String[PAGENAME_MAX];
    private static int[]             totalPvExt        = new int[PAGENAME_MAX];
    private static int[]             pcPvExt           = new int[PAGENAME_MAX];
    private static int[]             docomoPvExt       = new int[PAGENAME_MAX];
    private static int[]             auPvExt           = new int[PAGENAME_MAX];
    private static int[]             softbankPvExt     = new int[PAGENAME_MAX];
    private static int[][]           timePvExt         = new int[PAGENAME_MAX][24];

    private static boolean           modeReferer;
    private static boolean           modeDebug;
    private static String            toAddress;
    private static String            refererFile;
    private static ArrayList<String> refererList;

    /**
     * メイン
     * 
     * @param argv
     */
    public static void main(String argv[])
    {
        int i;
        int j;
        String convdate = "";

        modeReferer = false;
        modeDebug = false;
        refererList = new ArrayList<String>();

        if ( argv.length == 1 )
        {
            convdate = argv[0];
        }
        else if ( argv.length == 2 )
        {
            convdate = argv[0];
            modeReferer = true;
        }
        else if ( argv.length == 3 )
        {
            convdate = argv[0];
            modeReferer = true;
            modeDebug = true;
        }
        else
        {
            // 日付指定がない場合は前日
            Calendar cal = Calendar.getInstance();
            cal.add( Calendar.DATE, -1 );
            convdate = Integer.toString( (cal.get( Calendar.YEAR ) * 10000 + ((cal.get( Calendar.MONTH ) + 1) * 100) + cal.get( Calendar.DATE )) );
        }

        Debug( "Starting ConvertAccessLog(" + DateEdit.getDate( 0 ) + " " + DateEdit.getTime( 0 ) + ")" );

        // アクセスログの解析開始
        ConvertAccessLog( convdate );

        Debug( "End ConvertAccessLog(" + DateEdit.getDate( 0 ) + " " + DateEdit.getTime( 0 ) + ")" );
    }

    /**
     * アクセスログ集計処理
     * 
     * @param convdate 対象日付
     */
    private static void ConvertAccessLog(String convdate)
    {
        int i;
        int count;
        int status = 0;
        int dataCount = 0;
        String pathname;
        String headname;
        String filename;
        String pagename[];
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
            prop.load( new FileInputStream( "/etc/happyhotel/accesslog.conf" ) );

            pathname = prop.getProperty( "pathname" );
            headname = prop.getProperty( "headname", "happyhotel_log" );

            // アクセスログファイル名
            if ( convdate.compareTo( "" ) != 0 )
            {
                filename = pathname + headname + "." + convdate;
            }
            else
            {
                Calendar cal = Calendar.getInstance();
                cal.add( Calendar.DATE, -1 );
                convdate = Integer.toString( (cal.get( Calendar.YEAR ) * 10000 + ((cal.get( Calendar.MONTH ) + 1) * 100) + cal.get( Calendar.DATE )) );
                filename = pathname + headname + "." + convdate;
            }

            Debug( "LogFileName=" + filename );

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

                if ( modeDebug != false && dataCount > 10000 )
                {
                    break;
                }

                count = 0;
                dataCount++;

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
                    if ( ipaddr.compareTo( "122.208.42.42" ) != 0 )
                    {
                        // アクセス解析&DB書き込み
                        AccessAnalyze( convdate, acccal, url, status, referer, agent, ipaddr );
                    }
                }

            }
        }
        catch ( Exception e )
        {
            Debug( e.toString() );
        }
    }

    /**
     * IPアドレス取得処理
     * 
     * @param data 対象データ
     * @return IPアドレス
     */
    private static String getIpAddress(String data)
    {
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

    /**
     * アクセス日時取得処理
     * 
     * @param data 対象データ
     * @return アクセス日時
     */
    private static Calendar getAccessDate(String data)
    {
        int start;
        int end;
        String cutdata = "";
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
                cal.add( Calendar.HOUR, -9 );
            }
            catch ( Exception e )
            {
                Debug( e.toString() );
            }
        }

        return(cal);
    }

    /**
     * URL取得処理
     * 
     * @param data 対象データ
     * @return URL
     */
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

    /**
     * HTTPステータス取得処理
     * 
     * @param data 対象データ
     * @return HTTPステータス
     */
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

    /**
     * リファラー取得処理
     * 
     * @param data 対象データ
     * @return リファラー
     */
    private static String getReferer(String data)
    {
        return(data);
    }

    /**
     * ユーザエージェント取得処理
     * 
     * @param data 対象データ
     * @return ユーザエージェント
     */
    private static String getAgent(String data)
    {
        return(data);
    }

    /**
     * アクセス解析処理（通常）
     * 
     * @param convdate 対象日付
     * @param acccal アクセス日時
     * @param url URL
     * @param status HTTPステータス
     * @param refrere リファラー
     * @param agent ユーザエージェント
     * @param ipaddr IPアドレス
     */
    private static void AccessAnalyze(String convdate, Calendar acccal, String url, int status, String referer, String agent, String ipaddr)
    {
        int i;
        int start;
        int end;
        int findpos;
        int ymd;
        int hms;
        String cutpage;
        String refDecode = "";
        String id = "";
        String userid = "";
        boolean mobile = false;
        boolean pc = false;
        int ua;
        DataUserHistory duh;

        // 全PV取得
        // ユーザエージェント取得
        if ( agent.indexOf( "DoCoMo" ) >= 0 )
        {
            ua = 1;
        }
        else if ( agent.indexOf( "J-PHONE" ) >= 0 || agent.indexOf( "Vodafone" ) >= 0 || agent.indexOf( "SoftBank" ) >= 0 )
        {
            ua = 2;
        }
        else if ( agent.indexOf( "UP.Browser" ) >= 0 )
        {
            ua = 3;
        }
        else
        {
            ua = 4;
        }

        // ページの解析
        while( true )
        {
            findpos = url.indexOf( "hotel_details.jsp" );
            if ( findpos >= 0 )
            {
                mobile = true;
                break;
            }
            findpos = url.indexOf( "hotel_details01.jsp" );
            if ( findpos >= 0 )
            {
                mobile = true;
                break;
            }
            findpos = url.indexOf( "hotel_details02.jsp" );
            if ( findpos >= 0 )
            {
                mobile = true;
                break;
            }
            findpos = url.indexOf( "detail_buzz.jsp" );
            if ( findpos >= 0 )
            {
                pc = true;
                break;
            }
            findpos = url.indexOf( "detail_coupon.jsp" );
            if ( findpos >= 0 )
            {
                pc = true;
                break;
            }
            findpos = url.indexOf( "detail_map.jsp" );
            if ( findpos >= 0 )
            {
                pc = true;
                break;
            }
            findpos = url.indexOf( "detail_room.jsp" );
            if ( findpos >= 0 )
            {
                pc = true;
                break;
            }
            findpos = url.indexOf( "detail_top.jsp" );
            if ( findpos >= 0 )
            {
                pc = true;
                break;
            }
            break;
        }
        if ( mobile != false )
        {
            try
            {
                start = url.indexOf( "hotel_id=" );
                end = url.indexOf( "&", start + 9 );
                if ( end == -1 )
                {
                    id = url.substring( start + 9 );
                }
                else
                {
                    id = url.substring( start + 9, end );
                }
            }
            catch ( Exception e )
            {
                id = "";
            }
        }
        if ( pc != false )
        {
            try
            {
                start = url.indexOf( "id=" );
                end = url.indexOf( "&", start + 3 );
                if ( end == -1 )
                {
                    id = url.substring( start + 3 );
                }
                else
                {
                    id = url.substring( start + 3, end );
                }
            }
            catch ( Exception e )
            {
                id = "";
            }
        }

        if ( pc == true || mobile == true )
        {
            ymd = acccal.get( Calendar.YEAR ) * 10000 + (acccal.get( Calendar.MONTH ) + 1) * 100 + acccal.get( Calendar.DATE );
            hms = acccal.get( Calendar.HOUR_OF_DAY ) * 10000 + acccal.get( Calendar.MINUTE ) * 100 + acccal.get( Calendar.SECOND );

            if ( (ymd == 20100604 && hms >= 132600) || (ymd == 20100605 && hms <= 175700) )
            {
                Debug( "date=" + ymd + ",hms=" + hms + ",id=" + id + ",ua=" + ua + "    url=" + url );

                switch( ua )
                {
                    case 1:
                        userid = "i";
                        agent = "i";
                        break;
                    case 2:
                        userid = "y";
                        agent = "y";
                        break;
                    case 3:
                        userid = "au";
                        agent = "au";
                        break;
                    case 4:
                        userid = "";
                        break;
                }

                try
                {
                    insertData( userid, Integer.valueOf( id ), ymd, hms, ipaddr, agent );
                }
                catch ( Exception e )
                {
                    Debug( e.toString() );
                }
            }
        }
    }

    private static void insertData(String userid, int id, int ymd, int hms, String ip, String agent)
    {
        String query = "";
        String driver;
        String connurl;
        String user;
        String password;
        Properties prop;
        Connection connect = null;
        Statement stateupdate;

        try
        {
            // データベースの初期化
            prop = new Properties();
            // ﾌﾟﾛﾊﾟﾃｨﾌｧｲﾙからｷｰと値のﾘｽﾄを読み込みます
            prop.load( new FileInputStream( "/etc/happyhotel/dbconnect.conf" ) );
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
        try
        {
            query = "INSERT hh_user_history SET ";
            query = query + " user_id = '" + userid + "',";
            query = query + " seq = 0,";
            query = query + " id = " + id + ",";
            query = query + " disp_date = " + ymd + ",";
            query = query + " disp_time = " + hms + ",";
            query = query + " disp_ip = '" + ip + "',";
            query = query + " disp_useragent = '" + agent + "'";

            stateupdate = connect.createStatement();
            stateupdate.executeUpdate( query );
            stateupdate.close();
        }
        catch ( Exception e )
        {
            Debug( "query=" + query );
            Debug( "logupdate(" + e.toString() + ")" );
        }
        try
        {
            connect.close();
        }
        catch ( Exception e )
        {
        }
    }

    /**
     * ログ出力処理
     * 
     * @param output
     */
    private static void Debug(String output)
    {
        System.out.println( output );
    }
}
