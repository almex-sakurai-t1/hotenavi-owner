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
 * URL�擾�N���X
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
     * �n�b�s�[�z�e��Url�̎擾
     * 
     * @return �v���p�e�B�t�@�C���ɐݒ肳�ꂽ�T�[�o�[��
     */
    public static String getUrl()
    {
        String URL = null;
        URL = getUrl( true );
        return URL;
    }

    /**
     * �n�b�s�[�z�e��Url�̎擾
     * 
     * @param httpsFlag
     *            httpsFlag=true�̂Ƃ�:�v���p�e�B�t�@�C���ɐݒ肳�ꂽ�T�[�o�[�����擾����
     *            httpsFlag=false�̂Ƃ�:�v���p�e�B�t�@�C���ɐݒ肳�ꂽ�T�[�o�[�����擾���Ahttps:// ��http:// �ɕϊ�����
     * @return �v���p�e�B�t�@�C���ɐݒ肳�ꂽ�T�[�o�[��
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
     * SslUrl�̎擾
     * 
     * @return �v���p�e�B�t�@�C���ɐݒ肳�ꂽSsl�T�[�o�[��
     */
    public static String getSslUrl()
    {
        String URL = null;
        URL = getUrl( SERVER_SSL_NAME );
        return URL;
    }

    /**
     * �\��Url�̎擾
     * 
     * @return �v���p�e�B�t�@�C���ɐݒ肳�ꂽ�\��T�[�o�[��
     */
    public static String getRsvUrl()
    {
        String URL = null;
        URL = getUrl( SERVER_RSV_NAME );
        return URL;
    }

    /**
     * OTA�A�g�pAPIUrl�̎擾
     * 
     * @return �v���p�e�B�t�@�C���ɐݒ肳�ꂽOTA�A�g�pAPIURL
     */
    public static String getSiteconApilUrl()
    {
        String URL = null;
        URL = getUrl( SITECON_API_URL );
        return URL;
    }

    /**
     * �\�[�V�������O�C����GCP��Url�̎擾
     * 
     * @return �v���p�e�B�t�@�C���ɐݒ肳�ꂽ�\�[�V�������O�C����GCP��URL
     */
    public static String getAslUrl()
    {
        String URL = null;
        URL = getUrl( ASL_URL );
        return URL;
    }

    /**
     * URL�擾
     * 
     * @param target �^�[�Q�b�g�ƂȂ�URL
     * @return �v���p�e�B�t�@�C������擾����URL
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
            // Properties�I�u�W�F�N�g�𐶐�
            Properties props = new Properties();

            // �t�@�C����ǂݍ���
            props.load( new FileInputStream( INPUT_FILE ) );

            // �v���p�e�B�t�@�C������擾
            URL = props.getProperty( targetUrl );

        }
        catch ( Exception e )
        {
            Logging.error( "[Url.getUrl()] Exception=" + e.toString() + ", targetUrl:" + targetUrl );
        }
        return URL;
    }

    /**
     * URL�擾�N���X
     * 
     * @param request
     * @return �擾����URL
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
            // URL��Ԃ�
        }
        catch ( Exception e )
        {
            Logging.info( "[Url.getUrl(request)]Exception:" + e.toString() );
            return(null);
        }
        return(URL);
    }

    /****
     * URL�ϊ��N���X<br>
     * <br>
     * ������URL�̃T�[�o�[���� �v���p�e�B�t�@�C������擾�����T�[�o�[���ɕϊ����܂�<br>
     * http://happyhotel.jp �� https://staging.happyhotel.jp<br>
     * https://happyhotel.jp �� https://staging.happyhotel.jp<br>
     * 
     * @param targetUrl �ϊ���URL
     * @return �ϊ�����URL
     */
    public static String convertUrl(String targetUrl)
    {
        String URL = targetUrl;
        boolean isImage = false;
        boolean isFeaturePhone = false;

        if ( URL.indexOf( ".png" ) != -1 || URL.indexOf( ".gif" ) != -1 || URL.indexOf( ".jpg" ) != -1 || URL.indexOf( ".jpeg" ) != -1 )
        {
            isImage = true; // �摜�t�@�C��
        }
        if ( URL.indexOf( ".jp/i/" ) != -1 || URL.indexOf( ".jp/au/" ) != -1 || URL.indexOf( ".jp/y/" ) != -1 )
        {
            isFeaturePhone = true; // �t���[�`���[�t�H��
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
     * URL�ϊ��N���X<br>
     * PC����X�}�z��ʂɃA�N�Z�X->PC��ʂɂ���
     * 
     * @param HttpServletRequest request
     * @return �ϊ�����URL
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
        // �z�e���ڍ�
        if ( Url.indexOf( "/phone/search/hotel_details.jsp" ) != -1 )
        {
            Url = "/detail/detail_top.jsp?id=" + request.getParameter( "hotel_id" );
        }
        // �Z������
        else if ( Url.indexOf( "/searchAreaMobile.act" ) != -1 )
        {
            Url = "/searchArea.act" + param;
        }
        // �z�e���G���A����
        else if ( Url.indexOf( "/searchHotelAreaMobile.act" ) != -1 || Url.indexOf( "/phone/search/hotelarea/index_M2.jsp" ) != -1 )
        {
            Url = "/searchHotelArea.act" + param;
        }
        // IC����
        else if ( Url.indexOf( "/searchInterChangeMobile.act" ) != -1 )
        {
            Url = "/searchInterChange.act";
            // �s���{������
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
        // �w����
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
        // �\�񌟍�
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
        // �}�C�������X����
        else if ( Url.indexOf( "/searchHappieMobile.act" ) != -1 )
        {
            Url = "/searchHappie.act" + param;
        }
        // �\��ł���z�e���ڍ׉��
        else if ( Url.indexOf( "/phone/search/hotel_rsv_plan.jsp" ) != -1 )
        {
            Url = getRsvUrl() + "/others/reservePlanDetail.act";
            if ( hotelId != null )
            {
                Url += "?id=" + hotelId;
            }
        }
        // ������茟��
        else if ( Url.indexOf( "/searchKodawariMobile.act" ) != -1 )
        {
            Url = "/searchKodawari.act" + param;
        }
        // �z�e�i�r�����X����
        else if ( Url.indexOf( "/searchHotenaviMobile.act" ) != -1 )
        {
            Url = "/searchHotenavi.act" + param;
        }
        // �n�s�z�e�N�[�|���̂���z�e��������
        else if ( Url.indexOf( "/searchCouponMobile.act" ) != -1 || Url.indexOf( "/phone/search/coupon/index_M2.jsp" ) != -1 )
        {
            if ( !param.equals( "" ) )
            {
                Url = "/searchKodawari.act" + param; // �p�����[�^�����鎞��PC�̂�����茟����ʂɍs��
                Url += "&search=true&rest_from=0&rest_to=999999&stay_from=0&stay_to=999999&coupon=1&kuchikomi=0&point=0&cleanness=0&width=0&service=0&equip=0&cost=0";
            }
            else
            {
                Url = "/search/search_coupon_01.jsp"; // �p�����[�^�Ȃ��ꍇ�APC�N�[�|��������ʂɍs��
            }
        }
        // �n�s�z�e�`�F�[���X���猟��
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
     * URL�ϊ��N���X<br>
     * �X�}�z��ʂ���PC��ʂɃA�N�Z�X->�X�}�z��ʂɃ��_�C���N�g
     * 
     * @param HttpServletRequest request
     * @return �ϊ�����URL
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
            // �Z������
            if ( Url.indexOf( "/searchArea.act" ) != -1 )
            {
                Url = "/phone/search/area/searchAreaMobile.act" + param;
                if ( CheckString.numCheck( paramPrefId ) )
                {
                    Url += "&local_id=" + getLocalId( paramPrefId );
                }
            }
            // �z�e���G���A����
            else if ( Url.indexOf( "/searchHotelArea.act" ) != -1 )
            {
                Url = "/phone/search/hotelarea/searchHotelAreaMobile.act" + param;
            }
            // IC����
            else if ( Url.indexOf( "/searchInterChange.act" ) != -1 )
            {
                Url = "/phone/search/ic/searchInterChangeMobile.act";
                if ( paramIcName == null )
                {
                    paramIcName = "";
                }
                if ( "GET".equals( request.getMethod() ) ) // GET�ŃA�N�Z�X���ꂽ�ꍇ�̓p�����[�^���f�R�[�h����K�v����
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
                // �C���^�[�`�F���W�����猟��
                else if ( !paramIcName.equals( "" ) )
                {
                    URLCodec codec = new URLCodec( "Shift-JIS" );
                    Url += "?name=" + codec.encode( paramIcName );
                }
            }
            // �w����
            else if ( Url.indexOf( "/searchStation.act" ) != -1 )
            {
                Url = "/phone/search/st/searchStationMobile.act";
                String paramStation = request.getParameter( "station_id" );
                String paramJisCode = request.getParameter( "jis_code" );
                // �wID�`�F�b�N
                if ( CheckString.isvalidString( paramStation ) )
                {
                    Url += "?route_id=" + paramStation;
                    // �s�撬���R�[�h������Ύs�撬���R�[�h��n��
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
            // �\�񌟍�
            else if ( Url.indexOf( "/reserveUserTop.act" ) != -1 )
            {
                Url = getUrl( true ) + "/phone/search/rsv_happie/searchRsvHappieMobile.act" + param;
                if ( CheckString.numCheck( paramPrefId ) )
                {
                    Url += "&local_id=" + getLocalId( paramPrefId );
                }
            }
            // �}�C�������X����
            else if ( Url.indexOf( "/searchHappie.act" ) != -1 )
            {
                Url = "/phone/search/happie/searchHappieMobile.act" + param;
                if ( CheckString.numCheck( paramPrefId ) )
                {
                    Url += "&local_id=" + getLocalId( paramPrefId );
                }
            }
            // ������茟��
            else if ( Url.indexOf( "/searchKodawari.act" ) != -1 )
            {
                Url = "/phone/search/detail/searchKodawariMobile.act" + param;
            }
            // �z�e�i�r�����X����
            else if ( Url.indexOf( "/searchHotenavi.act" ) != -1 )
            {
                Url = "/phone/search/hotenavisp/searchHotenaviMobile.act" + param;
            }
            // �n�s�z�e�N�[�|���̂���z�e��������
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
     * LocalId�擾�N���X<br>
     * 
     * @param String prefId �s���{��ID
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
