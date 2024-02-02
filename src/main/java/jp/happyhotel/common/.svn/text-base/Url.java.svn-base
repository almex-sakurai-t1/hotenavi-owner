/*
 * @(#)Url.java 1.00 2018/02/18 Copyright (C) ALMEX Inc.
 */

package jp.happyhotel.common;

import java.io.FileInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import jp.happyhotel.data.DataMasterPref;

/**
 * URL取得クラス
 * 
 * @author T.Sakurai
 * @version 1.00 2018/02/16
 */
public class Url implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID = -2237569853797206634L;
    private static final String SERVER_NAME      = "serverName";
    private static final String SERVER_SSL_NAME  = "serverSslName";
    private static final String SERVER_RSV_NAME  = "serverRsvName";
    private static final String SITECON_API_URL  = "siteconApiUrl";
    private static final String INPUT_FILE       = "/etc/happyhotel/application.properties";
    private static final String HTTP_STR         = "http://happyhotel.jp";
    private static final String HTTPS_STR        = "https://happyhotel.jp";
    private static final String HTTPS_RSV_STR    = "https://reserve.happyhotel.jp";
    private static final String ASL_URL          = "aslUrl";

    /**
     * ハッピーホテルUrlの取得
     * 
     * @return プロパティファイルに設定されたサーバー名
     */
    public static String getUrl()
    {
        String URL = null;
        URL = getUrl( true );
        return URL;
    }

    /**
     * ハッピーホテルUrlの取得
     * 
     * @param httpsFlag
     *            httpsFlag=trueのとき:プロパティファイルに設定されたサーバー名を取得する
     *            httpsFlag=falseのとき:プロパティファイルに設定されたサーバー名を取得し、https:// をhttp:// に変換する
     * @return プロパティファイルに設定されたサーバー名
     */
    public static String getUrl(boolean httpsFlag)
    {
        String URL = null;
        URL = getUrl( SERVER_NAME );

        if ( !httpsFlag )
        {
            URL = URL.replace( "https://", "http://" );
        }
        return URL;
    }

    /**
     * SslUrlの取得
     * 
     * @return プロパティファイルに設定されたSslサーバー名
     */
    public static String getSslUrl()
    {
        String URL = null;
        URL = getUrl( SERVER_SSL_NAME );
        return URL;
    }

    /**
     * 予約Urlの取得
     * 
     * @return プロパティファイルに設定された予約サーバー名
     */
    public static String getRsvUrl()
    {
        String URL = null;
        URL = getUrl( SERVER_RSV_NAME );
        return URL;
    }

    /**
     * OTA連携用APIUrlの取得
     * 
     * @return プロパティファイルに設定されたOTA連携用APIURL
     */
    public static String getSiteconApilUrl()
    {
        String URL = null;
        URL = getUrl( SITECON_API_URL );
        return URL;
    }

    /**
     * ソーシャルログインのGCPのUrlの取得
     * 
     * @return プロパティファイルに設定されたソーシャルログインのGCPのURL
     */
    public static String getAslUrl()
    {
        String URL = null;
        URL = getUrl( ASL_URL );
        return URL;
    }

    /**
     * URL取得
     * 
     * @param target ターゲットとなるURL
     * @return プロパティファイルから取得したURL
     * 
     *         premiumInfoUrlAndroid=https://dev.happyhotel.jp/phone/others/info_premium_app.jsp
     *         mileInfoUrl=https://dev.happyhotel.jp/phone/mile/sp_new_mile.jsp
     *         reserveInfoUrl=https://dev.happyhotel.jp/phone/mile/sp_new_reserve.jsp
     *         nocardInfoUrl=https://dev.happyhotel.jp/phone/mile/sp_new_cardless.jsp
     *         couponUrl=https://dev.happyhotel.jp/phone/search/coupon_distinction.jsp
     *         memberPageUrl=https://dev.happyhotel.jp/phone/app/hotenaviLink.jsp
     *         touchStateUrl=https://dev.happyhotel.jp
     *         newsUrl=http://dev.happyhotel.jp/phone/htap/PushInfoDetail.jsp
     *         happieMemberImg=https://dev.happyhotel.jp/common/image/touch_detail_bnr01.gif
     *         happieMemberUrl=https://dev.happyhotel.jp/phone/others/touch_index.jsp
     *         rsvHappieMemberImg=https://dev.happyhotel.jp/common/image/yoyaku_detail_bnr01.gif
     *         rsvHappieMemberUrl=https://dev.happyhotel.jp/phone/bn_newreserve.jsp
     *         nomemberUrl=https://dev.happyhotel.jp/phone/others/info_premium_app.jsp
     *         happyhotelUrl=https://dev.happyhotel.jp/phone/search/
     *         hotelSearchUrl=https://dev.happyhotel.jp/searchFreeword.act
     *         serverName=https://dev.happyhotel.jp
     *         advertisementUrl=https://dev.hapyhotel.jp/phone/send_sponsor.jsp?sponsor_code=
     *         adImageUrl=https://dev.happyhotel.jp/servlet/SponsorPicture?sponsor_code=
     * 
     */

    public static String getUrl(String targetUrl)
    {
        String URL = null;
        try
        {
            // Propertiesオブジェクトを生成
            Properties props = new Properties();

            // ファイルを読み込む
            props.load( new FileInputStream( INPUT_FILE ) );

            // プロパティファイルから取得
            URL = props.getProperty( targetUrl );

        }
        catch ( Exception e )
        {
            Logging.error( "[Url.getUrl()] Exception=" + e.toString() + ", targetUrl:" + targetUrl );
        }
        return URL;
    }

    /**
     * URL取得クラス
     * 
     * @param request
     * @return 取得したURL
     */
    public static String getUrl(HttpServletRequest request)
    {
        String URL = null;
        String requestUrl = ""; // URL exp. https://owner.hotenavi.com/happyhotel/tool/request.jsp
        String requestUri = ""; // URI exp. /happyhotel/tool/request.jsp

        try
        {
            requestUrl = request.getRequestURL().toString();
            requestUri = request.getRequestURI();
            URL = requestUrl.replace( requestUri, "" );
            // URLを返す
        }
        catch ( Exception e )
        {
            Logging.info( "[Url.getUrl(request)]Exception:" + e.toString() );
            return(null);
        }
        return(URL);
    }

    /****
     * URL変換クラス<br>
     * <br>
     * 受取ったURLのサーバー名を プロパティファイルから取得したサーバー名に変換します<br>
     * http://happyhotel.jp → https://staging.happyhotel.jp<br>
     * https://happyhotel.jp → https://staging.happyhotel.jp<br>
     * 
     * @param targetUrl 変換元URL
     * @return 変換したURL
     */
    public static String convertUrl(String targetUrl)
    {
        String URL = targetUrl;
        boolean isImage = false;
        boolean isFeaturePhone = false;

        if ( URL.indexOf( ".png" ) != -1 || URL.indexOf( ".gif" ) != -1 || URL.indexOf( ".jpg" ) != -1 || URL.indexOf( ".jpeg" ) != -1 )
        {
            isImage = true; // 画像ファイル
        }
        if ( URL.indexOf( ".jp/i/" ) != -1 || URL.indexOf( ".jp/au/" ) != -1 || URL.indexOf( ".jp/y/" ) != -1 )
        {
            isFeaturePhone = true; // フューチャーフォン
        }
        try
        {
            String serverName = getUrl();
            if ( isImage || isFeaturePhone )
            {
                URL = URL.replace( HTTP_STR, "" );
                URL = URL.replace( HTTPS_STR, "" );
            }
            else
            {
                URL = URL.replace( HTTP_STR, serverName );
                URL = URL.replace( HTTPS_STR, serverName );
            }
            serverName = getRsvUrl();
            URL = URL.replace( HTTPS_RSV_STR, serverName );
        }
        catch ( Exception e )
        {
            Logging.error( "[Url.convertUrl()] Exception=" + e.toString() + ", targetUrl:" + targetUrl );
        }
        return URL;
    }

    /****
     * URL変換クラス<br>
     * PCからスマホ画面にアクセス->PC画面にする
     * 
     * @param HttpServletRequest request
     * @return 変換したURL
     * @throws EncoderException
     */
    public static String getPCUrl(HttpServletRequest request)
    {
        String hotelId = request.getParameter( "hotel_id" );
        String paramIcId = request.getParameter( "ic_id" );
        String paramLocalId = request.getParameter( "local_id" );
        String paramSearch = request.getParameter( "search" );
        String paramRouteId = request.getParameter( "route_id" );
        String paramPrefId = request.getParameter( "pref_id" );
        String Url = request.getRequestURI();
        String param = request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" );
        // ホテル詳細
        if ( Url.indexOf( "/phone/search/hotel_details.jsp" ) != -1 )
        {
            Url = "/detail/detail_top.jsp?id=" + request.getParameter( "hotel_id" );
        }
        // 住所検索
        else if ( Url.indexOf( "/searchAreaMobile.act" ) != -1 )
        {
            Url = "/searchArea.act" + param;
        }
        // ホテルエリア検索
        else if ( Url.indexOf( "/searchHotelAreaMobile.act" ) != -1 || Url.indexOf( "/phone/search/hotelarea/index_M2.jsp" ) != -1 )
        {
            Url = "/searchHotelArea.act" + param;
        }
        // IC検索
        else if ( Url.indexOf( "/searchInterChangeMobile.act" ) != -1 )
        {
            Url = "/searchInterChange.act";
            // 都道府県検索
            if ( paramIcId != null && paramIcId.equals( "true" ) )
            {
                Url += "?isSubmit=true";
                if ( paramLocalId != null )
                {
                    Url += "&local_id=" + paramLocalId;
                }
                if ( paramRouteId != null )
                {
                    Url += "&ic_id=" + paramRouteId;
                }
            }
        }
        // 駅検索
        else if ( Url.indexOf( "/searchStationMobile.act" ) != -1 )
        {
            Url = "/searchStation.act";
            if ( paramSearch != null )
            {
                Url += "?station_id=" + paramRouteId;
                if ( paramPrefId != null )
                {
                    int jisCode = Integer.parseInt( paramPrefId ) * 1000;
                    Url += "&jis_code=" + String.valueOf( jisCode );
                }
            }
            else
            {
                Url += param;
            }
        }
        // 予約検索
        else if ( Url.indexOf( "/searchRsvHappieMobile.act" ) != -1 )
        {
            if ( param.equals( "" ) )
            {
                Url = getRsvUrl() + "/others/reserve_index_pickup.jsp";
            }
            else
            {
                Url = getRsvUrl() + "/others/reserveUserTop.act" + param;
            }
        }
        // マイル加盟店検索
        else if ( Url.indexOf( "/searchHappieMobile.act" ) != -1 )
        {
            Url = "/searchHappie.act" + param;
        }
        // 予約できるホテル詳細画面
        else if ( Url.indexOf( "/phone/search/hotel_rsv_plan.jsp" ) != -1 )
        {
            Url = getRsvUrl() + "/others/reservePlanDetail.act";
            if ( hotelId != null )
            {
                Url += "?id=" + hotelId;
            }
        }
        // こだわり検索
        else if ( Url.indexOf( "/searchKodawariMobile.act" ) != -1 )
        {
            Url = "/searchKodawari.act" + param;
        }
        // ホテナビ加盟店検索
        else if ( Url.indexOf( "/searchHotenaviMobile.act" ) != -1 )
        {
            Url = "/searchHotenavi.act" + param;
        }
        // ハピホテクーポンのあるホテルを検索
        else if ( Url.indexOf( "/searchCouponMobile.act" ) != -1 || Url.indexOf( "/phone/search/coupon/index_M2.jsp" ) != -1 )
        {
            if ( !param.equals( "" ) )
            {
                Url = "/searchKodawari.act" + param; // パラメータがある時はPCのこだわり検索画面に行く
                Url += "&search=true&rest_from=0&rest_to=999999&stay_from=0&stay_to=999999&coupon=1&kuchikomi=0&point=0&cleanness=0&width=0&service=0&equip=0&cost=0";
            }
            else
            {
                Url = "/search/search_coupon_01.jsp"; // パラメータない場合、PCクーポン検索画面に行く
            }
        }
        // ハピホテチェーン店から検索
        else if ( Url.indexOf( "/searchChainMobile.act" ) != -1 )
        {
            Url = "/searchChain.act" + param;
        }
        else
        {
            Url = "/";
        }
        return Url;
    }

    /****
     * URL変換クラス<br>
     * スマホ画面からPC画面にアクセス->スマホ画面にリダイレクト
     * 
     * @param HttpServletRequest request
     * @return 変換したURL
     * @throws EncoderException
     * @throws UnsupportedEncodingException
     */
    public static String getSmartUrl(HttpServletRequest request)
    {
        String isSubmitValue = request.getParameter( "isSubmit" );
        String paramIcId = request.getParameter( "ic_id" );
        String isViaIcName = request.getParameter( "viaIcName" );
        String paramIcName = request.getParameter( "ic_name" );
        String paramLocalId = request.getParameter( "local_id" );
        String paramPrefId = request.getParameter( "pref_id" );
        String Url = request.getRequestURI();
        String param = request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" );
        try
        {
            // 住所検索
            if ( Url.indexOf( "/searchArea.act" ) != -1 )
            {
                Url = "/phone/search/area/searchAreaMobile.act" + param;
                if ( CheckString.numCheck( paramPrefId ) )
                {
                    Url += "&local_id=" + getLocalId( paramPrefId );
                }
            }
            // ホテルエリア検索
            else if ( Url.indexOf( "/searchHotelArea.act" ) != -1 )
            {
                Url = "/phone/search/hotelarea/searchHotelAreaMobile.act" + param;
            }
            // IC検索
            else if ( Url.indexOf( "/searchInterChange.act" ) != -1 )
            {
                Url = "/phone/search/ic/searchInterChangeMobile.act";
                if ( paramIcName == null )
                {
                    paramIcName = "";
                }
                if ( "GET".equals( request.getMethod() ) ) // GETでアクセスされた場合はパラメータをデコードする必要あり
                    paramIcName = new String( paramIcName.getBytes( "8859_1" ), "Windows-31J" );

                if ( isSubmitValue != null && isSubmitValue.equals( "true" ) )
                {
                    Url += "?ic_id=true";
                    if ( paramLocalId != null )
                    {
                        Url += "&local_id=" + paramLocalId;
                    }
                    if ( paramIcId != null )
                    {
                        Url += "&route_id=" + paramIcId;
                    }
                }
                else if ( isViaIcName != null && isViaIcName.equals( "true" ) )
                {
                    Url += "?ic_id=true";
                    if ( paramIcId != null )
                    {
                        Url += "&route_id=" + paramIcId;
                    }
                }
                // インターチェンジ名から検索
                else if ( !paramIcName.equals( "" ) )
                {
                    URLCodec codec = new URLCodec( "Shift-JIS" );
                    Url += "?name=" + codec.encode( paramIcName );
                }
            }
            // 駅検索
            else if ( Url.indexOf( "/searchStation.act" ) != -1 )
            {
                Url = "/phone/search/st/searchStationMobile.act";
                String paramStation = request.getParameter( "station_id" );
                String paramJisCode = request.getParameter( "jis_code" );
                // 駅IDチェック
                if ( CheckString.isvalidString( paramStation ) )
                {
                    Url += "?route_id=" + paramStation;
                    // 市区町村コードがあれば市区町村コードを渡す
                    if ( CheckString.numCheck( paramJisCode ) )
                    {
                        String getPref = String.valueOf( Integer.parseInt( paramJisCode ) / 1000 );
                        Url += "&search=2&pref_id=" + getPref;
                        Url += "&local_id=" + getLocalId( getPref );
                    }
                }
                else if ( CheckString.numCheck( paramPrefId ) )
                {
                    Url += param;
                    Url += "&local_id=" + getLocalId( paramPrefId );
                }
            }
            // 予約検索
            else if ( Url.indexOf( "/reserveUserTop.act" ) != -1 )
            {
                Url = getUrl( true ) + "/phone/search/rsv_happie/searchRsvHappieMobile.act" + param;
                if ( CheckString.numCheck( paramPrefId ) )
                {
                    Url += "&local_id=" + getLocalId( paramPrefId );
                }
            }
            // マイル加盟店検索
            else if ( Url.indexOf( "/searchHappie.act" ) != -1 )
            {
                Url = "/phone/search/happie/searchHappieMobile.act" + param;
                if ( CheckString.numCheck( paramPrefId ) )
                {
                    Url += "&local_id=" + getLocalId( paramPrefId );
                }
            }
            // こだわり検索
            else if ( Url.indexOf( "/searchKodawari.act" ) != -1 )
            {
                Url = "/phone/search/detail/searchKodawariMobile.act" + param;
            }
            // ホテナビ加盟店検索
            else if ( Url.indexOf( "/searchHotenavi.act" ) != -1 )
            {
                Url = "/phone/search/hotenavisp/searchHotenaviMobile.act" + param;
            }
            // ハピホテクーポンのあるホテルを検索
            else if ( Url.indexOf( "/search/search_coupon_01.jsp" ) != -1 )
            {
                Url = "/phone/search/coupon/searchCouponMobile.act" + param;
            }
            else
            {
                Url = "/";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[Url.getSmartUrl() ] Exception=" + e.toString() );
        }
        return Url;
    }

    /****
     * LocalId取得クラス<br>
     * 
     * @param String prefId 都道府県ID
     */
    public static String getLocalId(String prefId)
    {
        boolean ret = false;
        DataMasterPref pref = new DataMasterPref();
        String localId = "";
        ret = pref.getData( Integer.parseInt( prefId ) );
        if ( ret )
        {
            localId = String.valueOf( pref.getLocalId() );
        }
        return localId;
    }
}
