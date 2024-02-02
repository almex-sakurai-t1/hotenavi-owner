/*
 * @(#)UserAgent.java 2.01 2004/04/02 Copyright (C) ALMEX Inc. 2004 HTTP-USER-AGENT関連クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * USER-AGENTの取得、携帯製造番号等の取得を行うクラス。
 * 
 * @author S.Shiiya
 * @version 2.01 2004/04/02
 */
public class UserAgent implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID       = 2865991214985888771L;

    /** 端末種別：DoCoMo **/
    public static final int   USERAGENT_DOCOMO       = 1;
    /** 端末種別：au **/
    public static final int   USERAGENT_AU           = 2;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int   USERAGENT_JPHONE       = 3;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int   USERAGENT_VODAFONE     = 3;
    /** 端末種別：J-PHONE,Vodafone,SoftBank **/
    public static final int   USERAGENT_SOFTBANK     = 3;
    /** 端末種別：pc **/
    public static final int   USERAGENT_PC           = 4;
    /** 端末種別：SmartPhone **/
    public static final int   USERAGENT_SMARTPHONE   = 5;

    /** デバイス種別：PC **/
    public static final int   DEVICE_PC              = 1;
    /** デバイス種別：スマホWEB **/
    public static final int   DEVICE_SMARTPHONE_WEB  = 2;
    /** デバイス種別：予約アプリ **/
    public static final int   DEVICE_RSV_APP         = 3;
    /** デバイス種別：統合アプリ **/
    public static final int   DEVICE_APP_NEW         = 4;
    /** デバイス種別：予約アプリ(iOS) **/
    public static final int   DEVICE_RSV_APP_IOS     = 5;
    /** デバイス種別：予約アプリ(Android) **/
    public static final int   DEVICE_RSV_APP_ANDROID = 6;
    /** デバイス種別：統合アプリ(iOS) **/
    public static final int   DEVICE_APP_NEW_IOS     = 7;
    /** デバイス種別：統合アプリ(Android) **/
    public static final int   DEVICE_APP_NEW_ANDROID = 8;
    /** デバイス種別：StayConciergeアプリ **/
    public static final int   DEVICE_SC              = 9;
    /** デバイス種別：StayConcierge(IOS) **/
    public static final int   DEVICE_SC_IOS          = 10;
    /** デバイス種別：StayConcierge(ANDROID) **/
    public static final int   DEVICE_SC_ANDROID      = 11;

    /**
     * ユーザエージェント取得処理
     * 
     * @param request Httpリクエスト
     * @return ユーザエージェント
     */
    public static String getUserAgent(HttpServletRequest request)
    {
        Logging.debug( "[getUserAgent] request.getHeader( user-agent )=" + request.getHeader( "user-agent" ) );

        String agent = request.getHeader( "user-agent" ).length() > 255 ? request.getHeader( "user-agent" ).substring( 0, 255 ) : request.getHeader( "user-agent" );
        return(agent);
    }

    /**
     * ユーザエージェントタイプ取得処理
     * 
     * @param request Httpリクエスト
     * @return ユーザエージェントタイプ
     */
    public static int getUserAgentType(HttpServletRequest request)
    {
        String agent;

        agent = request.getHeader( "User-Agent" );
        if ( agent != null )
        {
            if ( agent.startsWith( "DoCoMo" ) != false )
            {
                return(USERAGENT_DOCOMO);
            }
            if ( agent.startsWith( "J-PHONE" ) != false ||
                    agent.startsWith( "Vodafone" ) != false ||
                    agent.startsWith( "SoftBank" ) != false ||
                    agent.startsWith( "Semulator" ) != false )
            {
                return(USERAGENT_JPHONE);
            }
            if ( agent.startsWith( "KDDI-" ) != false || agent.startsWith( "UP.Browser" ) != false )
            {
                return(USERAGENT_AU);
            }
            if ( agent.indexOf( "iPhone" ) != -1 )
            {
                return(USERAGENT_SMARTPHONE);
            }
            // Androidの文字列が含まれる
            if ( agent.indexOf( "Android" ) != -1 )
            {
                // Mobileの文字列が含まれるｓ
                if ( agent.indexOf( "Mobile" ) != -1 )
                {
                    return(USERAGENT_SMARTPHONE);
                }
                else
                {
                    return(USERAGENT_PC);
                }
            }
        }

        return(USERAGENT_PC);
    }

    /**
     * ユーザエージェントタイプ取得処理（タッチ後）
     * 
     * @param request Httpリクエスト
     * @return ユーザエージェントタイプ
     */
    public static int getUserAgentTypeFromTouch(HttpServletRequest request)
    {
        String agent;

        agent = request.getHeader( "User-Agent" );
        if ( agent != null )
        {
            if ( agent.startsWith( "DoCoMo" ) != false )
            {
                return(USERAGENT_DOCOMO);
            }
            if ( agent.startsWith( "J-PHONE" ) != false ||
                    agent.startsWith( "Vodafone" ) != false ||
                    agent.startsWith( "SoftBank" ) != false ||
                    agent.startsWith( "Semulator" ) != false )
            {
                return(USERAGENT_JPHONE);
            }
            if ( agent.startsWith( "KDDI-" ) != false || agent.startsWith( "UP.Browser" ) != false )
            {
                return(USERAGENT_AU);
            }
            if ( agent.indexOf( "iPhone" ) != -1 )
            {
                return(USERAGENT_SMARTPHONE);
            }
            // 「iPad」「iPod」が含まれていればスマホ
            if ( agent.indexOf( "iPad" ) != -1 || agent.indexOf( "iPod" ) != -1 )
            {
                return(USERAGENT_SMARTPHONE);
            }
            // 「Android」が含まれていれば「Mobile」が含まれているいかんに関わらずスマホ
            if ( agent.indexOf( "Android" ) != -1 )
            {
                return(USERAGENT_SMARTPHONE);
            }
        }

        return(USERAGENT_PC);
    }

    /**
     * ユーザエージェントタイプ取得処理（ディレクトリ）
     * 
     * @param request Httpリクエスト
     * @return ユーザエージェントタイプ ("i"：DoCoMo) ("j"：J-PHONE) ("ez"：EzWeb) ("sp"：スマートフォン) ("pc"：PCその他)
     */
    public static String getUserAgentTypeString(HttpServletRequest request)
    {
        String agent;

        agent = request.getHeader( "User-Agent" );
        if ( agent != null )
        {
            // スマホアプリ
            if ( agent.indexOf( "HappyHotel" ) != -1 || agent.startsWith( "Apache-HttpClient/UNAVAILABLE" ) != false )
            {
                // iOS
                if ( agent.startsWith( "HappyHotel" ) != false )
                {
                    return("ipa");
                }
                // Android
                else
                {
                    return("ada");
                }
            }
            else
            {
                if ( agent.startsWith( "DoCoMo" ) != false )
                {
                    return("i");
                }
                if ( agent.startsWith( "J-PHONE" ) != false ||
                        agent.startsWith( "Vodafone" ) != false ||
                        agent.startsWith( "SoftBank" ) != false ||
                        agent.startsWith( "Semulator" ) != false )
                {
                    return("y");
                }
                if ( agent.startsWith( "KDDI-" ) != false || agent.startsWith( "UP.Browser" ) != false )
                {
                    return("au");
                }
                if ( agent.indexOf( "iPhone" ) != -1 || agent.indexOf( "Android" ) != -1 )
                {
                    return("sp");
                }
            }

        }

        return("pc");
    }

    /**
     * ユーザエージェントタイプ取得処理（予約システム用）
     * 
     * @param request Httpリクエスト
     * @return ユーザエージェントタイプ ("ara"：Android) ("ira"：iOS) (""：その他)
     */
    public static String getUserAgentTypeStringForRsv(HttpServletRequest request)
    {
        String agent;

        agent = request.getHeader( "User-Agent" );
        Logging.info( "agent=" + agent );
        if ( agent != null )
        {
            // スマホアプリ
            if ( agent.indexOf( "HappyHotelRsv" ) != -1 )
            {

                // Android
                if ( agent.indexOf( "Android" ) != -1 )
                {

                    return("ara");
                }
                // iOS
                else
                {
                    return("ira");
                }
            }
        }

        return("");
    }

    /**
     * 製造番号取得処理
     * 
     * @param request Httpリクエスト
     * @return 製造番号
     */
    public String getSerialNo(HttpServletRequest request)
    {
        int i;
        int adrs;
        String agent;
        String productno;
        StringBuffer before;
        StringBuffer after;

        productno = "";
        after = new StringBuffer();

        agent = request.getHeader( "User-Agent" );
        if ( agent != null )
        {
            if ( agent.startsWith( "DoCoMo" ) != false )
            {
                // serの文字を見つける
                adrs = agent.indexOf( "ser" );
                if ( adrs != 0 )
                {
                    before = new StringBuffer( agent );

                    // 文字の最後または、セミコロンまたはスラッシュが出るまでコピーする
                    for( i = adrs + 3 ; i < agent.length() ; i++ )
                    {
                        if ( before.charAt( i ) == ';' || before.charAt( i ) == '/' )
                        {
                            break;
                        }
                        after.append( before.charAt( i ) );
                    }
                }

                productno = after.toString();
            }
            if ( agent.startsWith( "J-PHONE" ) != false || agent.startsWith( "Vodafone" ) != false || agent.startsWith( "SoftBank" ) != false )
            {
                // /SNの次から次のスペースまで
                adrs = agent.indexOf( "/SN" );
                if ( adrs != 0 )
                {
                    before = new StringBuffer( agent );

                    // 文字の最後または、セミコロンが出るまでコピーする
                    for( i = adrs + 3 ; i < agent.length() ; i++ )
                    {
                        if ( before.charAt( i ) == ' ' )
                        {
                            break;
                        }
                        after.append( before.charAt( i ) );
                    }
                }

                productno = after.toString();
            }
            if ( agent.startsWith( "KDDI-" ) != false || agent.startsWith( "UP.Browser" ) != false )
            {
                productno = request.getHeader( "x-up-subno" );
            }
        }

        return(productno);
    }

    /**
     * アプリバージョン取得処理
     * 
     * @param request Httpリクエスト
     * @return アプリバージョン (バージョン不明の場合は空文字列)
     */
    public static String getAppVersion(HttpServletRequest request)
    {
        String agent;
        String version = "";

        agent = getUserAgent( request );

        if ( agent != null )
        {

            if ( agent.indexOf( "HappyHotel" ) != -1 )
            {
                String[] elements = agent.split( " " );

                for( String element : elements )
                {
                    String[] parts = element.split( "/" );

                    if ( parts.length >= 2 )
                    {
                        if ( parts[0].equals( "HappyHotel" ) )
                        {
                            version = parts[1].replaceAll( "Android", "" );
                            break;
                        }
                    }
                }
            }
        }

        return version;
    }

    /**
     * 新予約アプリバージョン取得処理
     * 
     * @param request Httpリクエスト
     * @return アプリバージョン (バージョン不明の場合は空文字列)
     */
    public static String getAppVersionForRsv(HttpServletRequest request)
    {
        String agent;
        String version = "";

        agent = getUserAgent( request );

        if ( agent != null )
        {

            if ( agent.indexOf( "Mozilla" ) != -1 )
            {
                String[] elements = agent.split( "/" );

                for( int i = 0 ; i < elements.length ; i++ )
                {
                    if ( elements[i].equals( "HappyHotelRsv" ) )
                    {
                        version = elements[i + 1];
                    }
                }
            }
        }

        return version;
    }

    /**
     * アプリバージョン比較
     * アプリのバージョンが、指定したバージョン以上かどうかを比較
     * 
     * @param request Httpリクエスト
     * @param major メジャーバージョン
     * @param minor マイナーバージョン
     * @param build ビルド番号
     * @return 処理結果(true:指定バージョン以上, false:指定バージョン未満)
     */
    public static boolean IsLargerAppVersion(HttpServletRequest request, int[] targetVersion)
    {
        String appVersion = getAppVersion( request );

        try
        {
            // バージョンあり
            if ( !appVersion.equals( "" ) )
            {
                String[] versionunit = appVersion.split( "\\." );

                for( int i = 0 ; i < targetVersion.length ; i++ )
                {
                    if ( versionunit.length < i + 1 )
                    {
                        return false;
                    }
                    if ( (Integer.parseInt( versionunit[i] ) > targetVersion[i]) )
                    {
                        return true;
                    }
                    else if ( (Integer.parseInt( versionunit[i] ) < targetVersion[i]) )
                    {
                        return false;
                    }
                }

                return true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserAgent.IsLargerAppVersion]Exception:" + e.toString() );
        }

        return false;
    }

    /**
     * 予約アプリバージョン比較
     * アプリのバージョンが、指定したバージョン以上かどうかを比較
     * 
     * @param request Httpリクエスト
     * @param major メジャーバージョン
     * @param minor マイナーバージョン
     * @param build ビルド番号
     * @return 処理結果(true:指定バージョン以上, false:指定バージョン未満)
     */
    public static boolean IsLargerAppVersionForRsv(HttpServletRequest request, String target)
    {
        try
        {
            // 指定バージョン
            int targetVersion = Integer.parseInt( target.replaceAll( "\\.", "" ) );

            // ユーザーのアプリバージョン
            String appVer = getAppVersionForRsv( request );
            int appVersion = 0;

            if ( !appVer.equals( "" ) )
            {
                appVer = appVer.replaceAll( "\\.", "" );
                appVersion = Integer.parseInt( appVer );
            }

            // ユーザーのアプリバージョンが指定バージョンより以上の場合、
            if ( appVersion >= targetVersion )
            {
                return true;
            }
            else
            {
                return false;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[UserAgent.IsLargerAppVersionForRsv]Exception:" + e.toString() );
        }

        return false;
    }

    /**
     * Bot判断得処理
     * 
     * @param request Httpリクエスト
     * @return Botの場合はtrueを返す
     */
    public static boolean isBot(HttpServletRequest request)
    {
        String agent;
        boolean ret = false;
        agent = getUserAgent( request );

        if ( agent != null )
        {
            if ( agent.indexOf( "Googlebot" ) != -1 )
            {
                ret = true;
            }
            else if ( agent.indexOf( "AdsBot" ) != -1 )
            {
                ret = true;
            }
            else if ( agent.indexOf( "bingbot" ) != -1 )
            {
                ret = true;
            }
            else if ( agent.indexOf( "msnbot" ) != -1 )
            {
                ret = true;
            }
            else if ( agent.indexOf( "Y!J-SRD" ) != -1 )
            {
                ret = true;
            }
            else if ( agent.indexOf( "Yahoo! Slurp" ) != -1 )
            {
                ret = true;
            }
            else if ( agent.indexOf( "Yahoo! DE Slurp" ) != -1 )
            {
                ret = true;
            }
            Logging.info( "[UserAgent.isBot]agent:" + agent, "UserAgent.isBot" );
        }
        return ret;
    }

    /**
     * アプリ内ログイン可能取得処理
     * 
     * @param request Httpリクエスト
     * @return true:アプリ内ログイン可能 false:アプリ内ログイン不可
     */
    public static boolean getIsAppLogin(HttpServletRequest request)
    {
        boolean ret = false;
        String agent;
        String version = "";

        agent = getUserAgent( request );

        if ( agent != null )
        {
            if ( agent.indexOf( "HappyHotel" ) != -1 )
            {
                if ( agent.indexOf( "HappyHotel_SC" ) != -1 )
                {
                    ret = true;
                }
                else
                {
                    String[] elements = agent.split( " " );

                    for( String element : elements )
                    {
                        String[] parts = element.split( "/" );

                        if ( parts.length >= 2 )
                        {
                            if ( parts[0].equals( "HappyHotel" ) )
                            {
                                if ( parts[1].indexOf( "Android" ) != -1 )
                                {
                                    version = parts[1].replaceAll( "Android", "" );
                                    if ( version.compareTo( "3.1.6" ) >= 0 )
                                    {
                                        ret = true;
                                    }
                                }
                                else if ( parts[1].indexOf( "iOS" ) != -1 )
                                {
                                    version = parts[1].replaceAll( "iOS", "" );
                                    if ( version.compareTo( "3.1.3" ) >= 0 )
                                    {
                                        ret = true;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    /**
     * デバイス取得処理
     * 
     * @param request Httpリクエスト
     * @return デバイスタイプ (PC:1、スマホWEB:2、予約アプリ:3、統合アプリ:4、その他:0)
     */
    public static int getDeviceFromUserAgent(String agent)
    {
        int agentCd = getDeviceFromUserAgentDetail( agent );
        if ( agentCd == DEVICE_RSV_APP_IOS || agentCd == DEVICE_RSV_APP_ANDROID )
        {
            agentCd = DEVICE_RSV_APP;
        }
        if ( agentCd == DEVICE_APP_NEW_IOS || agentCd == DEVICE_APP_NEW_ANDROID )
        {
            agentCd = DEVICE_APP_NEW;
        }
        if ( agentCd == DEVICE_SC_IOS || agentCd == DEVICE_SC_ANDROID )
        {
            agentCd = DEVICE_SC;
        }
        return agentCd;

    }

    /**
     * デバイス取得処理
     * 
     * @param request Httpリクエスト
     * @return デバイスタイプ (PC:1、スマホWEB:2、予約アプリ(iOS):5、予約アプリ(android):6、統合アプリ(iOS):7、統合アプリ(android):8、その他:0 9、StayConciergeアプリ 10、StayConcierge(IOS) 11、StayConcierge(ANDROID))
     */
    public static int getDeviceFromUserAgentDetail(String agent)
    {
        // Logging.info( "agent=" + agent );
        if ( agent != null )
        {
            if ( agent.indexOf( "HappyHotelRsv" ) != -1 )
            {
                // 予約アプリ
                if ( agent.indexOf( "iPhone" ) != -1 )
                {
                    return DEVICE_RSV_APP_IOS;
                }
                else
                {
                    return DEVICE_RSV_APP_ANDROID;
                }
            }
            // StayConcierge
            else if ( agent.indexOf( "HappyHotel_SC" ) != -1 )
            {
                // IOS
                if ( agent.indexOf( "iPhone" ) != -1 )
                {
                    return DEVICE_SC_IOS;
                }
                // ANDROID
                else
                {
                    return DEVICE_SC_ANDROID;
                }
            }
            else if ( agent.indexOf( "HappyHotel" ) != -1 )
            {
                if ( (agent.indexOf( "iPhone" ) != -1 || agent.indexOf( "Android" ) != -1) )
                {
                    // スマホアプリ
                    String regexiPhone = "HappyHotel/iOS[0-9]";
                    String regexAndroid = "HappyHotel/Android[0-9]";
                    String editUserAgent = "";
                    int versionNo = 0;

                    Pattern p1 = Pattern.compile( regexiPhone );
                    Pattern p2 = Pattern.compile( regexAndroid );

                    Matcher m1 = p1.matcher( agent );
                    Matcher m2 = p2.matcher( agent );
                    // アプリユーザーか判断
                    if ( m1.find() )
                    {
                        editUserAgent = m1.group();
                    }
                    if ( m2.find() )
                    {
                        editUserAgent = m2.group();
                    }
                    if ( !editUserAgent.equals( "" ) )
                    {
                        versionNo = Integer.parseInt( editUserAgent.substring( editUserAgent.length() - 1 ) );
                    }

                    if ( versionNo >= 5 )
                    {
                        // リニューアルアプリ（統合アプリ）
                        if ( agent.indexOf( "iPhone" ) != -1 )
                        {
                            return DEVICE_APP_NEW_IOS;
                        }
                        else
                        {
                            return DEVICE_APP_NEW_ANDROID;
                        }
                    }
                    else
                    {
                        // スマホWEB（ラブインジャパンの時・ハピホテアプリからWEB　VIEWに遷移して予約した時）
                        return DEVICE_SMARTPHONE_WEB;
                    }
                }
                else
                {
                    // PC(ラブインジャパン）
                    return DEVICE_PC;
                }
            }
            else
            {
                if ( (agent.indexOf( "iPhone" ) != -1 || agent.indexOf( "Android" ) != -1) )
                {
                    // スマホWEB
                    return DEVICE_SMARTPHONE_WEB;
                }
                else
                {
                    // PC
                    return DEVICE_PC;
                }
            }
        }
        return 0;
    }

    /**
     * ステイコンシェルジュ用アカウントID取得
     * 
     * @param request Httpリクエスト
     * @return アカウントID : 0以外ならStayConcierge
     */
    public static int getAccountId(HttpServletRequest request)
    {
        int accountId = 0;
        String userAgent = request.getHeader( "User-Agent" );
        String paramAccountId = "";
        if ( userAgent.indexOf( "HappyHotel_SC" ) != -1 )
        {
            paramAccountId = userAgent.split( "HappyHotel_SC" )[1];
            if ( paramAccountId.indexOf( "/" ) != -1 )
            {
                paramAccountId = paramAccountId.split( "/" )[0];
            }
            else
            {
                paramAccountId = "";
            }
        }
        if ( CheckString.numCheck( paramAccountId ) )
        {
            accountId = Integer.parseInt( paramAccountId );
        }
        return accountId;
    }

}
