/*
 * @(#)AccessLog.java 2.00 2007/12/03 Copyright (C) ALMEX Inc. 2007 アクセスログ集計クラス
 */
package jp.happyhotel.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * アクセスログ集計クラス。
 * 
 * @author S.Shiiya
 * @version 2.00 2004/12/02
 */
public class AccessLog
{
    private static final int         PAGENAME_MAX            = 9999;

    private static int               collectDate             = 0;
    private static int               totalPv                 = 0;
    private static int               pcPv                    = 0;
    private static int               docomoPv                = 0;
    private static int               auPv                    = 0;
    private static int               softbankPv              = 0;
    private static int               iphonewebPV             = 0;
    private static int               androidwebPV            = 0;
    private static int               iphoneappliPV           = 0;
    private static int               androidappliPV          = 0;
    private static int               iphoneapplirsvPV        = 0;
    private static int               androidapplirsvPV       = 0;

    private static int[]             timePv                  = new int[24];

    private static int[]             collectDateDetail       = new int[PAGENAME_MAX];
    private static String[]          targetPageDetail        = new String[PAGENAME_MAX];
    private static int[]             totalPvDetail           = new int[PAGENAME_MAX];
    private static int[]             pcPvDetail              = new int[PAGENAME_MAX];
    private static int[]             docomoPvDetail          = new int[PAGENAME_MAX];
    private static int[]             auPvDetail              = new int[PAGENAME_MAX];
    private static int[]             softbankPvDetail        = new int[PAGENAME_MAX];
    private static int[]             iphonewebPVDetail       = new int[PAGENAME_MAX];
    private static int[]             androidwebPVDetail      = new int[PAGENAME_MAX];
    private static int[]             iphoneappliPVDetail     = new int[PAGENAME_MAX];
    private static int[]             androidappliPVDetail    = new int[PAGENAME_MAX];
    private static int[]             iphoneapplirsvPVDetail  = new int[PAGENAME_MAX];
    private static int[]             androidapplirsvPVDetail = new int[PAGENAME_MAX];
    private static int[][]           timePvDetail            = new int[PAGENAME_MAX][24];

    private static int[]             collectDateExt          = new int[PAGENAME_MAX];
    private static String[]          targetPageExt           = new String[PAGENAME_MAX];
    private static String[]          targetQueryExt          = new String[PAGENAME_MAX];
    private static int[]             totalPvExt              = new int[PAGENAME_MAX];
    private static int[]             pcPvExt                 = new int[PAGENAME_MAX];
    private static int[]             docomoPvExt             = new int[PAGENAME_MAX];
    private static int[]             auPvExt                 = new int[PAGENAME_MAX];
    private static int[]             softbankPvExt           = new int[PAGENAME_MAX];
    private static int[]             iphonewebPVExt          = new int[PAGENAME_MAX];
    private static int[]             androidwebPVExt         = new int[PAGENAME_MAX];
    private static int[]             iphoneappliPVExt        = new int[PAGENAME_MAX];
    private static int[]             androidappliPVExt       = new int[PAGENAME_MAX];
    private static int[]             iphoneapplirsvPVExt     = new int[PAGENAME_MAX];
    private static int[]             androidapplirsvPVExt    = new int[PAGENAME_MAX];
    private static int[][]           timePvExt               = new int[PAGENAME_MAX][24];

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

        // 処理結果
        Debug( "total_pv=" + totalPv );
        Debug( "pc_pv=" + pcPv );
        Debug( "docomo_pv=" + docomoPv );
        Debug( "au_pv=" + auPv );
        Debug( "softbank_pv=" + softbankPv );
        Debug( "iphone_web_pv=" + iphonewebPV );
        Debug( "android_web_pv=" + androidwebPV );
        Debug( "iphone_appli_pv=" + iphoneappliPV );
        Debug( "android_appli_pv=" + androidappliPV );

        for( i = 0 ; i < timePv.length ; i++ )
        {
            Debug( "time_pv[" + i + "]=" + timePv[i] );
        }

        // リファラーメール送信
        RefererMail( convdate );

        if ( modeReferer == false )
        {
            // 合計PV更新
            UpdatePvData( convdate );

            // 詳細PV更新
            for( i = 0 ; i < PAGENAME_MAX ; i++ )
            {
                if ( targetPageDetail[i] == null )
                {
                    break;
                }
                Debug( "[detail]target_page=" + targetPageDetail[i] );
                Debug( "[detail]total_pv=" + totalPvDetail[i] );
                Debug( "[detail]pc_pv=" + pcPvDetail[i] );
                Debug( "[detail]docomo_pv=" + docomoPvDetail[i] );
                Debug( "[detail]au_pv=" + auPvDetail[i] );
                Debug( "[detail]softbank_pv=" + softbankPvDetail[i] );
                Debug( "[detail]iphone_web_pv=" + iphonewebPVDetail[i] );
                Debug( "[detail]android_web_pv=" + androidwebPVDetail[i] );
                Debug( "[detail]iphone_appli_pv=" + iphoneappliPVDetail[i] );
                Debug( "[detail]android_appli_pv=" + androidappliPVDetail[i] );

                for( j = 0 ; j < timePv.length ; j++ )
                {
                    Debug( "[detail]time_pv[" + j + "]=" + timePvDetail[i][j] );
                }

                UpdatePvDetailData( convdate, i );
            }

            // 拡張PV更新
            for( i = 0 ; i < PAGENAME_MAX ; i++ )
            {
                if ( targetPageExt[i] == null )
                {
                    break;
                }
                Debug( "[ext]target_page=" + targetPageExt[i] );
                Debug( "[ext]total_pv=" + totalPvExt[i] );
                Debug( "[ext]pc_pv=" + pcPvExt[i] );
                Debug( "[ext]docomo_pv=" + docomoPvExt[i] );
                Debug( "[ext]au_pv=" + auPvExt[i] );
                Debug( "[ext]softbank_pv=" + softbankPvExt[i] );
                Debug( "[ext]iphone_web_pv=" + iphonewebPVExt[i] );
                Debug( "[ext]android_web_pv=" + androidwebPVExt[i] );
                Debug( "[ext]iphone_appli_pv=" + iphoneappliPVExt[i] );
                Debug( "[ext]android_appli_pv=" + androidappliPVExt[i] );

                for( j = 0 ; j < timePv.length ; j++ )
                {
                    Debug( "[ext]time_pv[" + j + "]=" + timePvExt[i][j] );
                }

                UpdatePvExtData( convdate, i );
            }
        }
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

            // 集計対象ページ
            pagename = new String[PAGENAME_MAX];
            for( i = 0 ; i < PAGENAME_MAX ; i++ )
            {
                pagename[i] = prop.getProperty( "ext_page" + (i + 1), "" );
            }

            // メール送信宛先
            toAddress = prop.getProperty( "mailto" );
            // メール送信宛先
            refererFile = prop.getProperty( "referer_file" );

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
                        AccessAnalyze( convdate, acccal, url, status, referer, agent );
                    }
                }

                // アクセス解析&DB書き込み（拡張）
                AccessAnalyzeExt( convdate, acccal, url, status, referer, agent, pagename );
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
     */
    private static void AccessAnalyze(String convdate, Calendar acccal, String url, int status, String referer, String agent)
    {
        int i;
        int end;
        String cutpage;
        String refDecode = "";

        // 画像ファイルはカウントしない
        if ( url.indexOf( "/image/" ) >= 0 ||
                url.indexOf( "/images/" ) >= 0 ||
                url.indexOf( "/_debug_/" ) >= 0 ||
                url.indexOf( "/servlet/" ) >= 0 ||
                url.indexOf( "/js/" ) >= 0 ||
                url.indexOf( ".gif" ) >= 0 ||
                url.indexOf( ".png" ) >= 0 ||
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

        // 全PV取得
        // ユーザエージェント取得
        if ( agent.indexOf( "DoCoMo" ) >= 0 )
        {
            docomoPv++;
        }
        else if ( agent.indexOf( "J-PHONE" ) >= 0 || agent.indexOf( "Vodafone" ) >= 0 || agent.indexOf( "SoftBank" ) >= 0 )
        {
            softbankPv++;
        }
        else if ( agent.indexOf( "UP.Browser" ) >= 0 )
        {
            auPv++;
        }
        else if ( agent.indexOf( "iPhone" ) >= 0 && agent.indexOf( "HappyHotelRsv" ) >= 0 )
        {
            iphoneapplirsvPV++;
        }
        else if ( agent.indexOf( "Android" ) >= 0 && agent.indexOf( "HappyHotelRsv" ) >= 0 )
        {
            androidapplirsvPV++;
        }
        else if ( agent.indexOf( "iPhone" ) >= 0 && agent.indexOf( "HappyHotel" ) >= 0 )
        {
            iphoneappliPV++;
        }
        else if ( agent.indexOf( "Android" ) >= 0 && agent.indexOf( "HappyHotel" ) >= 0 )
        {
            androidappliPV++;
        }
        else if ( agent.indexOf( "Apache-HttpClient/UNAVAILABLE" ) >= 0 )
        {
            androidappliPV++;
        }
        else if ( agent.indexOf( "HappyHotel" ) >= 0 ) // 旧iPhoneアプリのAPIは、HappyHotel/ から始まる
        {
            iphoneappliPV++;
        }
        else if ( agent.indexOf( "iPhone" ) >= 0 )
        {
            iphonewebPV++;
        }
        else if ( agent.indexOf( "Android" ) >= 0 )
        {
            androidwebPV++;
        }
        else
        {
            pcPv++;
        }

        totalPv++;
        timePv[acccal.get( Calendar.HOUR_OF_DAY )]++;

        if ( (totalPv % 10000) == 0 )
        {
            Debug( "total_pv=" + totalPv );
        }

        // ページ別PV取得
        end = url.indexOf( ";" );
        if ( end >= 0 )
        {
            cutpage = url.substring( 0, end );
        }
        else
        {
            // ページ解析
            end = url.indexOf( "?" );
            if ( end >= 0 )
            {
                cutpage = url.substring( 0, end );
            }
            else
            {
                cutpage = url;
            }
        }

        // モバイルページの共通化
        cutpage = cutpage.replaceAll( "/i/", "/mobile/" );
        cutpage = cutpage.replaceAll( "/y/", "/mobile/" );
        cutpage = cutpage.replaceAll( "/au/", "/mobile/" );
        cutpage = cutpage.replaceAll( "/j/", "/mobile/" );
        cutpage = cutpage.replaceAll( "/ez/", "/mobile/" );

        for( i = 0 ; i < PAGENAME_MAX ; i++ )
        {
            if ( targetPageDetail[i] == null )
            {
                targetPageDetail[i] = cutpage;
            }
            if ( targetPageDetail[i].compareTo( cutpage ) == 0 )
            {
                targetPageDetail[i] = cutpage;

                // ユーザエージェント取得
                if ( agent.indexOf( "DoCoMo" ) >= 0 )
                {
                    docomoPvDetail[i]++;
                }
                else if ( agent.indexOf( "J-PHONE" ) >= 0 || agent.indexOf( "Vodafone" ) >= 0 || agent.indexOf( "SoftBank" ) >= 0 )
                {
                    softbankPvDetail[i]++;
                }
                else if ( agent.indexOf( "UP.Browser" ) >= 0 )
                {
                    auPvDetail[i]++;
                }
                else if ( agent.indexOf( "iPhone" ) >= 0 && agent.indexOf( "HappyHotelRsv" ) >= 0 )
                {
                    iphoneapplirsvPVDetail[i]++;
                }
                else if ( agent.indexOf( "Android" ) >= 0 && agent.indexOf( "HappyHotelRsv" ) >= 0 )
                {
                    androidapplirsvPVDetail[i]++;
                }
                else if ( agent.indexOf( "iPhone" ) >= 0 && agent.indexOf( "HappyHotel" ) >= 0 )
                {
                    iphoneappliPVDetail[i]++;
                }
                else if ( agent.indexOf( "Android" ) >= 0 && agent.indexOf( "HappyHotel" ) >= 0 )
                {
                    androidappliPVDetail[i]++;
                }
                else if ( agent.indexOf( "Apache-HttpClient/UNAVAILABLE" ) >= 0 )
                {
                    androidappliPVDetail[i]++;
                }
                else if ( agent.indexOf( "HappyHotel" ) >= 0 )
                {
                    iphoneappliPVDetail[i]++;
                }
                else if ( agent.indexOf( "iPhone" ) >= 0 )
                {
                    iphonewebPVDetail[i]++;
                }
                else if ( agent.indexOf( "Android" ) >= 0 )
                {
                    androidwebPVDetail[i]++;
                }
                else
                {
                    pcPvDetail[i]++;
                }

                totalPvDetail[i]++;
                timePvDetail[i][acccal.get( Calendar.HOUR_OF_DAY )]++;

                break;
            }
        }

        // リファラー格納
        if ( referer.indexOf( Url.getUrl() ) == -1 &&
                referer.indexOf( Url.getSslUrl() ) == -1 &&
                referer.indexOf( "-" ) == -1 )
        {
            try
            {
                if ( referer.indexOf( "google" ) != -1 )
                {
                    Debug( "url=" + url + " ref=" + URLDecoder.decode( referer, "UTF-8" ) + " ua=" + agent );
                    refDecode = URLDecoder.decode( referer, "UTF-8" );
                }
                else if ( referer.indexOf( "msn" ) != -1 || referer.indexOf( "live.com" ) != -1 )
                {
                    Debug( "url=" + url + " ref=" + new String( URLDecoder.decode( referer, "ISO-8859-1" ).getBytes( "ISO-8859-1" ), "UTF-8" ) + " ua=" + agent );
                    refDecode = new String( URLDecoder.decode( referer, "ISO-8859-1" ).getBytes( "ISO-8859-1" ), "UTF-8" );
                }
                else
                {
                    Debug( "url=" + url + " ref=" + new String( URLDecoder.decode( referer, "ISO-8859-1" ).getBytes( "ISO-8859-1" ), "Windows-31J" ) + " ua=" + agent );
                    refDecode = new String( URLDecoder.decode( referer, "ISO-8859-1" ).getBytes( "ISO-8859-1" ), "Windows-31J" );
                }
                refererList.add( url + "," + agent + "," + refDecode + "," + referer );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
    }

    /**
     * アクセス解析処理（拡張）
     * 
     * @param convdate 対象日付
     * @param acccal アクセス日時
     * @param url URL
     * @param status HTTPステータス
     * @param refrere リファラー
     * @param agent ユーザエージェント
     */
    private static void AccessAnalyzeExt(String convdate, Calendar acccal, String url, int status, String referer, String agent, String[] pagename)
    {
        int i;
        int end;
        boolean findpage = false;
        String cutpage;
        String cutquery;
        StringTokenizer stoken;

        // URL分析
        for( i = 0 ; i < pagename.length ; i++ )
        {
            if ( pagename[i].compareTo( "" ) == 0 )
            {
                break;
            }

            stoken = new StringTokenizer( pagename[i], "," );
            while( stoken.hasMoreTokens() != false )
            {
                cutpage = stoken.nextToken().trim();
                end = url.indexOf( cutpage );
                if ( end == 0 )
                {
                    findpage = true;
                    break;
                }
                /**
                 * findpage = url.matches(cutpage); if( findpage != false ) { break; }
                 **/
            }

            if ( findpage != false )
            {
                break;
            }
        }

        if ( findpage == false )
        {
            return;
        }

        end = url.indexOf( ";" );
        if ( end >= 0 )
        {
            cutpage = url.substring( 0, end );

            // ページ解析
            end = url.indexOf( "?" );
            if ( end >= 0 )
            {
                cutquery = url.substring( end + 1 );
            }
            else
            {
                cutquery = "";
            }
        }
        else
        {
            // ページ解析
            end = url.indexOf( "?" );
            if ( end >= 0 )
            {
                cutpage = url.substring( 0, end );
                cutquery = url.substring( end + 1 );
            }
            else
            {
                cutpage = url;
                cutquery = "";
            }
        }

        // モバイルページの共通化
        cutpage = cutpage.replaceAll( "/i/", "/mobile/" );
        cutpage = cutpage.replaceAll( "/y/", "/mobile/" );
        cutpage = cutpage.replaceAll( "/au/", "/mobile/" );
        cutpage = cutpage.replaceAll( "/j/", "/mobile/" );
        cutpage = cutpage.replaceAll( "/ez/", "/mobile/" );

        for( i = 0 ; i < PAGENAME_MAX ; i++ )
        {
            if ( targetPageExt[i] == null )
            {
                targetPageExt[i] = cutpage;
                targetQueryExt[i] = cutquery;
            }
            if ( targetPageExt[i].compareTo( cutpage ) == 0 && targetQueryExt[i].compareTo( cutquery ) == 0 )
            {
                targetPageExt[i] = cutpage;
                targetQueryExt[i] = cutquery;

                // ユーザエージェント取得
                if ( agent.indexOf( "DoCoMo" ) >= 0 )
                {
                    docomoPvExt[i]++;
                }
                else if ( agent.indexOf( "J-PHONE" ) >= 0 || agent.indexOf( "Vodafone" ) >= 0 || agent.indexOf( "SoftBank" ) >= 0 )
                {
                    softbankPvExt[i]++;
                }
                else if ( agent.indexOf( "UP.Browser" ) >= 0 )
                {
                    auPvExt[i]++;
                }
                else if ( agent.indexOf( "iPhone" ) >= 0 && agent.indexOf( "HappyHotelRsv" ) >= 0 )
                {
                    iphoneapplirsvPVExt[i]++;
                }
                else if ( agent.indexOf( "Android" ) >= 0 && agent.indexOf( "HappyHotelRsv" ) >= 0 )
                {
                    androidapplirsvPVExt[i]++;
                }
                else if ( agent.indexOf( "iPhone" ) >= 0 && agent.indexOf( "HappyHotel" ) >= 0 )
                {
                    iphoneappliPVExt[i]++;
                }
                else if ( agent.indexOf( "Android" ) >= 0 && agent.indexOf( "HappyHotel" ) >= 0 )
                {
                    androidappliPVExt[i]++;
                }
                else if ( agent.indexOf( "Apache-HttpClient/UNAVAILABLE" ) >= 0 )
                {
                    androidappliPVExt[i]++;
                }
                else if ( agent.indexOf( "HappyHotel" ) >= 0 )
                {
                    iphoneappliPVExt[i]++;
                }
                else if ( agent.indexOf( "iPhone" ) >= 0 )
                {
                    iphonewebPVExt[i]++;
                }
                else if ( agent.indexOf( "Android" ) >= 0 )
                {
                    androidwebPVExt[i]++;
                }
                else
                {
                    pcPvExt[i]++;
                }
                totalPvExt[i]++;
                timePvExt[i][acccal.get( Calendar.HOUR_OF_DAY )]++;

                break;
            }
        }
    }

    /**
     * ページビューデータ更新処理
     * 
     * @param convdate 対象日付
     */
    private static void UpdatePvData(String convdate)
    {
        String query = "";
        String driver;
        String connurl;
        String user;
        String password;
        Properties prop;
        Connection connect = null;
        Statement stateacclog;
        Statement stateupdate;
        ResultSet retacclog;

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
            // アクセスログ取得
            query = "SELECT * FROM hh_system_pv WHERE collect_date=" + convdate;
            stateacclog = connect.createStatement();
            retacclog = stateacclog.executeQuery( query );
            if ( retacclog != null )
            {
                if ( retacclog.next() != false )
                {
                    query = "UPDATE hh_system_pv SET ";
                    query = query + "total_pv=" + totalPv + ",";
                    query = query + "pc_pv=" + pcPv + ",";
                    query = query + "docomo_pv=" + docomoPv + ",";
                    query = query + "au_pv=" + auPv + ",";
                    query = query + "softbank_pv=" + softbankPv + ",";
                    query = query + "iphone_web_pv=" + iphonewebPV + ",";
                    query = query + "android_web_pv=" + androidwebPV + ",";
                    query = query + "iphone_appli_pv=" + iphoneappliPV + ",";
                    query = query + "android_appli_pv=" + androidappliPV + ",";
                    query = query + "iphone_appli_reserve_pv=" + iphoneapplirsvPV + ",";
                    query = query + "android_appli_reserve_pv=" + androidapplirsvPV + ",";
                    query = query + "time01=" + timePv[0] + ",";
                    query = query + "time02=" + timePv[1] + ",";
                    query = query + "time03=" + timePv[2] + ",";
                    query = query + "time04=" + timePv[3] + ",";
                    query = query + "time05=" + timePv[4] + ",";
                    query = query + "time06=" + timePv[5] + ",";
                    query = query + "time07=" + timePv[6] + ",";
                    query = query + "time08=" + timePv[7] + ",";
                    query = query + "time09=" + timePv[8] + ",";
                    query = query + "time10=" + timePv[9] + ",";
                    query = query + "time11=" + timePv[10] + ",";
                    query = query + "time12=" + timePv[11] + ",";
                    query = query + "time13=" + timePv[12] + ",";
                    query = query + "time14=" + timePv[13] + ",";
                    query = query + "time15=" + timePv[14] + ",";
                    query = query + "time16=" + timePv[15] + ",";
                    query = query + "time17=" + timePv[16] + ",";
                    query = query + "time18=" + timePv[17] + ",";
                    query = query + "time19=" + timePv[18] + ",";
                    query = query + "time20=" + timePv[19] + ",";
                    query = query + "time21=" + timePv[20] + ",";
                    query = query + "time22=" + timePv[21] + ",";
                    query = query + "time23=" + timePv[22] + ",";
                    query = query + "time24=" + timePv[23];
                    query = query + " WHERE collect_date=" + convdate;
                }
                else
                {
                    query = "INSERT INTO  hh_system_pv SET ";
                    query = query + "collect_date=" + convdate + ",";
                    query = query + "total_pv=" + totalPv + ",";
                    query = query + "pc_pv=" + pcPv + ",";
                    query = query + "docomo_pv=" + docomoPv + ",";
                    query = query + "au_pv=" + auPv + ",";
                    query = query + "softbank_pv=" + softbankPv + ",";
                    query = query + "iphone_web_pv=" + iphonewebPV + ",";
                    query = query + "android_web_pv=" + androidwebPV + ",";
                    query = query + "iphone_appli_pv=" + iphoneappliPV + ",";
                    query = query + "android_appli_pv=" + androidappliPV + ",";
                    query = query + "iphone_appli_reserve_pv=" + iphoneapplirsvPV + ",";
                    query = query + "android_appli_reserve_pv=" + androidapplirsvPV + ",";
                    query = query + "time01=" + timePv[0] + ",";
                    query = query + "time02=" + timePv[1] + ",";
                    query = query + "time03=" + timePv[2] + ",";
                    query = query + "time04=" + timePv[3] + ",";
                    query = query + "time05=" + timePv[4] + ",";
                    query = query + "time06=" + timePv[5] + ",";
                    query = query + "time07=" + timePv[6] + ",";
                    query = query + "time08=" + timePv[7] + ",";
                    query = query + "time09=" + timePv[8] + ",";
                    query = query + "time10=" + timePv[9] + ",";
                    query = query + "time11=" + timePv[10] + ",";
                    query = query + "time12=" + timePv[11] + ",";
                    query = query + "time13=" + timePv[12] + ",";
                    query = query + "time14=" + timePv[13] + ",";
                    query = query + "time15=" + timePv[14] + ",";
                    query = query + "time16=" + timePv[15] + ",";
                    query = query + "time17=" + timePv[16] + ",";
                    query = query + "time18=" + timePv[17] + ",";
                    query = query + "time19=" + timePv[18] + ",";
                    query = query + "time20=" + timePv[19] + ",";
                    query = query + "time21=" + timePv[20] + ",";
                    query = query + "time22=" + timePv[21] + ",";
                    query = query + "time23=" + timePv[22] + ",";
                    query = query + "time24=" + timePv[23];
                }

                stateupdate = connect.createStatement();
                stateupdate.executeUpdate( query );
                stateupdate.close();
            }
            stateacclog.close();
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
     * 詳細ページビューデータ更新処理
     * 
     * @param convdate 対象日付
     */
    private static void UpdatePvDetailData(String convdate, int arrayIdx)
    {
        String query = "";
        String driver;
        String connurl;
        String user;
        String password;
        Properties prop;
        Connection connect = null;
        Statement stateacclog;
        Statement stateupdate;
        ResultSet retacclog;

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
            // アクセスログ取得
            query = "SELECT * FROM hh_system_pv_detail WHERE collect_date=" + convdate;
            query = query + " AND target_page='" + targetPageDetail[arrayIdx] + "'";
            stateacclog = connect.createStatement();
            retacclog = stateacclog.executeQuery( query );
            if ( retacclog != null )
            {
                if ( retacclog.next() != false )
                {
                    query = "UPDATE hh_system_pv_detail SET ";
                    query = query + "total_pv=" + totalPvDetail[arrayIdx] + ",";
                    query = query + "pc_pv=" + pcPvDetail[arrayIdx] + ",";
                    query = query + "docomo_pv=" + docomoPvDetail[arrayIdx] + ",";
                    query = query + "au_pv=" + auPvDetail[arrayIdx] + ",";
                    query = query + "softbank_pv=" + softbankPvDetail[arrayIdx] + ",";
                    query = query + "iphone_web_pv=" + iphonewebPVDetail[arrayIdx] + ",";
                    query = query + "android_web_pv=" + androidwebPVDetail[arrayIdx] + ",";
                    query = query + "iphone_appli_pv=" + iphoneappliPVDetail[arrayIdx] + ",";
                    query = query + "android_appli_pv=" + androidappliPVDetail[arrayIdx] + ",";
                    query = query + "iphone_appli_reserve_pv=" + iphoneapplirsvPVDetail[arrayIdx] + ",";
                    query = query + "android_appli_reserve_pv=" + androidapplirsvPVDetail[arrayIdx] + ",";
                    query = query + "time01=" + timePvDetail[arrayIdx][0] + ",";
                    query = query + "time02=" + timePvDetail[arrayIdx][1] + ",";
                    query = query + "time03=" + timePvDetail[arrayIdx][2] + ",";
                    query = query + "time04=" + timePvDetail[arrayIdx][3] + ",";
                    query = query + "time05=" + timePvDetail[arrayIdx][4] + ",";
                    query = query + "time06=" + timePvDetail[arrayIdx][5] + ",";
                    query = query + "time07=" + timePvDetail[arrayIdx][6] + ",";
                    query = query + "time08=" + timePvDetail[arrayIdx][7] + ",";
                    query = query + "time09=" + timePvDetail[arrayIdx][8] + ",";
                    query = query + "time10=" + timePvDetail[arrayIdx][9] + ",";
                    query = query + "time11=" + timePvDetail[arrayIdx][10] + ",";
                    query = query + "time12=" + timePvDetail[arrayIdx][11] + ",";
                    query = query + "time13=" + timePvDetail[arrayIdx][12] + ",";
                    query = query + "time14=" + timePvDetail[arrayIdx][13] + ",";
                    query = query + "time15=" + timePvDetail[arrayIdx][14] + ",";
                    query = query + "time16=" + timePvDetail[arrayIdx][15] + ",";
                    query = query + "time17=" + timePvDetail[arrayIdx][16] + ",";
                    query = query + "time18=" + timePvDetail[arrayIdx][17] + ",";
                    query = query + "time19=" + timePvDetail[arrayIdx][18] + ",";
                    query = query + "time20=" + timePvDetail[arrayIdx][19] + ",";
                    query = query + "time21=" + timePvDetail[arrayIdx][20] + ",";
                    query = query + "time22=" + timePvDetail[arrayIdx][21] + ",";
                    query = query + "time23=" + timePvDetail[arrayIdx][22] + ",";
                    query = query + "time24=" + timePvDetail[arrayIdx][23];
                    query = query + " WHERE collect_date=" + convdate;
                    query = query + " AND target_page='" + targetPageDetail[arrayIdx] + "'";
                }
                else
                {
                    query = "INSERT INTO  hh_system_pv_detail SET ";
                    query = query + "collect_date=" + convdate + ",";
                    query = query + "target_page='" + targetPageDetail[arrayIdx] + "',";
                    query = query + "total_pv=" + totalPvDetail[arrayIdx] + ",";
                    query = query + "pc_pv=" + pcPvDetail[arrayIdx] + ",";
                    query = query + "docomo_pv=" + docomoPvDetail[arrayIdx] + ",";
                    query = query + "au_pv=" + auPvDetail[arrayIdx] + ",";
                    query = query + "softbank_pv=" + softbankPvDetail[arrayIdx] + ",";
                    query = query + "iphone_web_pv=" + iphonewebPVDetail[arrayIdx] + ",";
                    query = query + "android_web_pv=" + androidwebPVDetail[arrayIdx] + ",";
                    query = query + "iphone_appli_pv=" + iphoneappliPVDetail[arrayIdx] + ",";
                    query = query + "android_appli_pv=" + androidappliPVDetail[arrayIdx] + ",";
                    query = query + "iphone_appli_reserve_pv=" + iphoneapplirsvPVDetail[arrayIdx] + ",";
                    query = query + "android_appli_reserve_pv=" + androidapplirsvPVDetail[arrayIdx] + ",";
                    query = query + "time01=" + timePvDetail[arrayIdx][0] + ",";
                    query = query + "time02=" + timePvDetail[arrayIdx][1] + ",";
                    query = query + "time03=" + timePvDetail[arrayIdx][2] + ",";
                    query = query + "time04=" + timePvDetail[arrayIdx][3] + ",";
                    query = query + "time05=" + timePvDetail[arrayIdx][4] + ",";
                    query = query + "time06=" + timePvDetail[arrayIdx][5] + ",";
                    query = query + "time07=" + timePvDetail[arrayIdx][6] + ",";
                    query = query + "time08=" + timePvDetail[arrayIdx][7] + ",";
                    query = query + "time09=" + timePvDetail[arrayIdx][8] + ",";
                    query = query + "time10=" + timePvDetail[arrayIdx][9] + ",";
                    query = query + "time11=" + timePvDetail[arrayIdx][10] + ",";
                    query = query + "time12=" + timePvDetail[arrayIdx][11] + ",";
                    query = query + "time13=" + timePvDetail[arrayIdx][12] + ",";
                    query = query + "time14=" + timePvDetail[arrayIdx][13] + ",";
                    query = query + "time15=" + timePvDetail[arrayIdx][14] + ",";
                    query = query + "time16=" + timePvDetail[arrayIdx][15] + ",";
                    query = query + "time17=" + timePvDetail[arrayIdx][16] + ",";
                    query = query + "time18=" + timePvDetail[arrayIdx][17] + ",";
                    query = query + "time19=" + timePvDetail[arrayIdx][18] + ",";
                    query = query + "time20=" + timePvDetail[arrayIdx][19] + ",";
                    query = query + "time21=" + timePvDetail[arrayIdx][20] + ",";
                    query = query + "time22=" + timePvDetail[arrayIdx][21] + ",";
                    query = query + "time23=" + timePvDetail[arrayIdx][22] + ",";
                    query = query + "time24=" + timePvDetail[arrayIdx][23];
                }

                stateupdate = connect.createStatement();
                stateupdate.executeUpdate( query );
                stateupdate.close();
            }
            stateacclog.close();
        }
        catch ( Exception e )
        {
            Debug( "query_detail=" + query );
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
     * 拡張ページビューデータ更新処理
     * 
     * @param convdate 対象日付
     */
    private static void UpdatePvExtData(String convdate, int arrayIdx)
    {
        String query = "";
        String driver;
        String connurl;
        String user;
        String password;
        Properties prop;
        Connection connect = null;
        Statement stateacclog;
        Statement stateupdate;
        ResultSet retacclog;

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
            // アクセスログ取得
            query = "SELECT * FROM hh_system_pv_ext WHERE collect_date=" + convdate;
            query = query + " AND ext_page='" + targetPageExt[arrayIdx] + "'";
            query = query + " AND query='" + targetQueryExt[arrayIdx] + "'";
            stateacclog = connect.createStatement();
            retacclog = stateacclog.executeQuery( query );
            if ( retacclog != null )
            {
                if ( retacclog.next() != false )
                {
                    query = "UPDATE hh_system_pv_ext SET ";
                    query = query + "total_pv=" + totalPvExt[arrayIdx] + ",";
                    query = query + "pc_pv=" + pcPvExt[arrayIdx] + ",";
                    query = query + "docomo_pv=" + docomoPvExt[arrayIdx] + ",";
                    query = query + "au_pv=" + auPvExt[arrayIdx] + ",";
                    query = query + "softbank_pv=" + softbankPvExt[arrayIdx] + ",";
                    query = query + "iphone_web_pv=" + iphonewebPVExt[arrayIdx] + ",";
                    query = query + "android_web_pv=" + androidwebPVExt[arrayIdx] + ",";
                    query = query + "iphone_appli_pv=" + iphoneappliPVExt[arrayIdx] + ",";
                    query = query + "android_appli_pv=" + androidappliPVExt[arrayIdx] + ",";
                    query = query + "iphone_appli_reserve_pv=" + iphoneapplirsvPVExt[arrayIdx] + ",";
                    query = query + "android_appli_reserve_pv=" + androidapplirsvPVExt[arrayIdx] + ",";
                    query = query + "time01=" + timePvExt[arrayIdx][0] + ",";
                    query = query + "time02=" + timePvExt[arrayIdx][1] + ",";
                    query = query + "time03=" + timePvExt[arrayIdx][2] + ",";
                    query = query + "time04=" + timePvExt[arrayIdx][3] + ",";
                    query = query + "time05=" + timePvExt[arrayIdx][4] + ",";
                    query = query + "time06=" + timePvExt[arrayIdx][5] + ",";
                    query = query + "time07=" + timePvExt[arrayIdx][6] + ",";
                    query = query + "time08=" + timePvExt[arrayIdx][7] + ",";
                    query = query + "time09=" + timePvExt[arrayIdx][8] + ",";
                    query = query + "time10=" + timePvExt[arrayIdx][9] + ",";
                    query = query + "time11=" + timePvExt[arrayIdx][10] + ",";
                    query = query + "time12=" + timePvExt[arrayIdx][11] + ",";
                    query = query + "time13=" + timePvExt[arrayIdx][12] + ",";
                    query = query + "time14=" + timePvExt[arrayIdx][13] + ",";
                    query = query + "time15=" + timePvExt[arrayIdx][14] + ",";
                    query = query + "time16=" + timePvExt[arrayIdx][15] + ",";
                    query = query + "time17=" + timePvExt[arrayIdx][16] + ",";
                    query = query + "time18=" + timePvExt[arrayIdx][17] + ",";
                    query = query + "time19=" + timePvExt[arrayIdx][18] + ",";
                    query = query + "time20=" + timePvExt[arrayIdx][19] + ",";
                    query = query + "time21=" + timePvExt[arrayIdx][20] + ",";
                    query = query + "time22=" + timePvExt[arrayIdx][21] + ",";
                    query = query + "time23=" + timePvExt[arrayIdx][22] + ",";
                    query = query + "time24=" + timePvExt[arrayIdx][23];
                    query = query + " WHERE collect_date=" + convdate;
                    query = query + " AND ext_page='" + targetPageExt[arrayIdx] + "'";
                    query = query + " AND query='" + targetQueryExt[arrayIdx] + "'";
                }
                else
                {
                    query = "INSERT INTO  hh_system_pv_ext SET ";
                    query = query + "collect_date=" + convdate + ",";
                    query = query + "ext_page='" + targetPageExt[arrayIdx] + "',";
                    query = query + "query='" + targetQueryExt[arrayIdx] + "',";
                    query = query + "total_pv=" + totalPvExt[arrayIdx] + ",";
                    query = query + "pc_pv=" + pcPvExt[arrayIdx] + ",";
                    query = query + "docomo_pv=" + docomoPvExt[arrayIdx] + ",";
                    query = query + "au_pv=" + auPvExt[arrayIdx] + ",";
                    query = query + "softbank_pv=" + softbankPvExt[arrayIdx] + ",";
                    query = query + "iphone_web_pv=" + iphonewebPVExt[arrayIdx] + ",";
                    query = query + "android_web_pv=" + androidwebPVExt[arrayIdx] + ",";
                    query = query + "iphone_appli_pv=" + iphoneappliPVExt[arrayIdx] + ",";
                    query = query + "android_appli_pv=" + androidappliPVExt[arrayIdx] + ",";
                    query = query + "iphone_appli_reserve_pv=" + iphoneapplirsvPVExt[arrayIdx] + ",";
                    query = query + "android_appli_reserve_pv=" + androidapplirsvPVExt[arrayIdx] + ",";
                    query = query + "time01=" + timePvExt[arrayIdx][0] + ",";
                    query = query + "time02=" + timePvExt[arrayIdx][1] + ",";
                    query = query + "time03=" + timePvExt[arrayIdx][2] + ",";
                    query = query + "time04=" + timePvExt[arrayIdx][3] + ",";
                    query = query + "time05=" + timePvExt[arrayIdx][4] + ",";
                    query = query + "time06=" + timePvExt[arrayIdx][5] + ",";
                    query = query + "time07=" + timePvExt[arrayIdx][6] + ",";
                    query = query + "time08=" + timePvExt[arrayIdx][7] + ",";
                    query = query + "time09=" + timePvExt[arrayIdx][8] + ",";
                    query = query + "time10=" + timePvExt[arrayIdx][9] + ",";
                    query = query + "time11=" + timePvExt[arrayIdx][10] + ",";
                    query = query + "time12=" + timePvExt[arrayIdx][11] + ",";
                    query = query + "time13=" + timePvExt[arrayIdx][12] + ",";
                    query = query + "time14=" + timePvExt[arrayIdx][13] + ",";
                    query = query + "time15=" + timePvExt[arrayIdx][14] + ",";
                    query = query + "time16=" + timePvExt[arrayIdx][15] + ",";
                    query = query + "time17=" + timePvExt[arrayIdx][16] + ",";
                    query = query + "time18=" + timePvExt[arrayIdx][17] + ",";
                    query = query + "time19=" + timePvExt[arrayIdx][18] + ",";
                    query = query + "time20=" + timePvExt[arrayIdx][19] + ",";
                    query = query + "time21=" + timePvExt[arrayIdx][20] + ",";
                    query = query + "time22=" + timePvExt[arrayIdx][21] + ",";
                    query = query + "time23=" + timePvExt[arrayIdx][22] + ",";
                    query = query + "time24=" + timePvExt[arrayIdx][23];
                }

                stateupdate = connect.createStatement();
                stateupdate.executeUpdate( query );
                stateupdate.close();
            }
            stateacclog.close();
        }
        catch ( Exception e )
        {
            Debug( "query_ext=" + query );
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
    private static void RefererMail(String convdate)
    {
        // ファイルに保存
        try
        {
            refererFile = refererFile.replaceAll( "ymd", convdate );
            FileOutputStream fos = new FileOutputStream( refererFile );
            OutputStreamWriter osw = new OutputStreamWriter( fos, "Shift_JIS" );

            for( int i = 0 ; i < refererList.size() ; i++ )
            {
                osw.write( refererList.get( i ) + "," );
                osw.write( "\r\n" );
            }

            osw.close();
            fos.close();

            // ファイルを圧縮する
            CompressFile( refererFile, refererFile + ".zip" );

            // SendMail.sendMailAttach( "report@happyhotel.jp", toAddress, "ハピホテリファラー - " + convdate, "", refererFile + ".zip" );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * ファイル圧縮処理
     * 
     * @param orgfile 解凍前ファイル名
     * @param zipfile zipファイル名
     */
    private static void CompressFile(String orgfile, String zipfile)
    {

        int inData = 0;
        String entryName = null;
        BufferedInputStream zipin = null;
        ZipOutputStream zipout = null;

        try
        {
            zipout = new ZipOutputStream( new BufferedOutputStream( new FileOutputStream( zipfile ) ) );
            zipin = new BufferedInputStream( new FileInputStream( orgfile ) );

            entryName = new File( orgfile ).getName();
            zipout.putNextEntry( new ZipEntry( entryName ) );

            while( (inData = zipin.read()) != -1 )
            {
                zipout.write( inData );
            }

            zipin.close();
            zipout.closeEntry();
            zipout.close();

            zipin = null;
            zipout = null;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
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
