/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * ハピタッチ制御クラス
 */
package jp.happyhotel.others;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.FileUrl;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataApHotelTerminal;
import jp.happyhotel.data.DataApTouchCi;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataHotelAuth;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.data.DataHotelFelicaNg;
import jp.happyhotel.data.DataHotelHapiTouchConfig;
import jp.happyhotel.data.DataHotelMaster;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataSystemFelicaMatching;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.hotel.HotelAuth;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.hotel.HotelCoupon;
import jp.happyhotel.hotel.HotelHappie;
import jp.happyhotel.hotel.HotelRoom;
import jp.happyhotel.hotel.HotelRoomMore;
import jp.happyhotel.owner.FormOwnerRsvList;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.owner.LogicOwnerRsvList;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.touch.FrontTouchAcceptCheck;
import jp.happyhotel.touch.RsvFix;
import jp.happyhotel.touch.RsvList;
import jp.happyhotel.touch.TokenUser;
import jp.happyhotel.touch.TouchErrorHistory;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserLoginInfo;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserPointPayTemp;

/**
 * ハピタッチ
 * 
 * @author S.Tashiro
 * @version 1.00 2010/11/17
 */
public class HapiTouch
{
    // ポイント区分
    private static final int    POINT_KIND_RAITEN   = 21;                                                    // 来店
    private static final int    POINT_KIND_RIYOU    = 22;                                                    // 利用
    private static final int    POINT_KIND_WARIBIKI = 23;                                                    // 割引
    private static final int    POINT_KIND_YOYAKU   = 24;                                                    // 予約
    private static final int    COME_POINT          = 1000004;
    private static final int    AMOUNT_POINT        = 1000005;
    private static final int    USE_POINT           = 1000006;
    private static final int    RSV_POINT           = 1000007;
    private static final int    CI_STATUS_DEL       = 3;
    private static final int    SYNC_CI_DEL_FLAG    = 1;                                                     // SyncCiで削除対象のフラグ
    private static final int    SYNC_CI_ADD_FLAG    = 0;                                                     // SyncCiで追加対象のフラグ
    private static final String METHOD_REGIST       = "Regist";
    private static final String RESULT_OK           = "OK";
    private static final String RESULT_NG           = "NG";
    private static final String RESULT_NO           = "NO";
    private static final String RESULT_DENY         = "DENY";
    private static final String CONTENT_TYPE        = "text/xml; charset=UTF-8";
    private static final String ENCODE              = "UTF-8";
    private DataLoginInfo_M2    dataLoginInfo_M2    = null;
    /* 社内環境 */
    // private static String BASE_URL = "http://121.101.88.177/";
    // private static String INSTALL_URL32 = "http://121.101.88.177/install/"; // インストール時のURL
    // private static String INSTALL_URL64 = "http://121.101.88.177/install/"; // インストール時のURL
    // private static String FILE_URL32 = "/happyhotel/install/"; // ローカルでファイル検索を行う場合のURL
    // private static String FILE_URL64 = "/happyhotel/install/"; // ローカルでファイル検索を行う場合のURL
    // private static String FILE_URL32WIN = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ローカルでファイル検索を行う場合のURL(WindowsがOSの場合)
    // private static String FILE_URL64WIN = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ローカルでファイル検索を行う場合のURL(WindowsがOSの場合)
    /* 本番環境 */
    private static String       BASE_URL            = Url.getUrl() + "/";
    private static String       INSTALL_URL32       = Url.getSslUrl() + "/owner/install/";                   // インストール時のURL
    private static String       INSTALL_URL64       = Url.getSslUrl() + "/owner/install/";                   // インストール時のURL
    private static String       FILE_URL32          = "/happyhotel/secure/owner/install/";                   // ローカルでファイル検索を行う場合のURL
    private static String       FILE_URL64          = "/happyhotel/secure/owner/install/";                   // ローカルでファイル検索を行う場合のURL
    private static String       FILE_URL32WIN       = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ローカルでファイル検索を行う場合のURL(WindowsがOSの場合)
    private static String       FILE_URL64WIN       = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ローカルでファイル検索を行う場合のURL(WindowsがOSの場合)
    /* デバッグ */
    // private static String BASE_URL = "http://debug.happyhotel.jp/";
    // private static String INSTALL_URL32 = "https://debugssl.happyhotel.jp/owner/install/"; // インストール時のURL
    // private static String INSTALL_URL64 = "https://debugssl.happyhotel.jp/owner/install/"; // インストール時のURL
    // private static String FILE_URL32 = "/happyhotel/secure/owner/install/"; // ローカルでファイル検索を行う場合のURL
    // private static String FILE_URL64 = "/happyhotel/secure/owner/install/"; // ローカルでファイル検索を行う場合のURL
    // private static String FILE_URL32WIN = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ローカルでファイル検索を行う場合のURL(WindowsがOSの場合)
    // private static String FILE_URL64WIN = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ローカルでファイル検索を行う場合のURL(WindowsがOSの場合)
    private static String       FILE_NAME           = "HapiTouchInstall";                                    // マッチングを行う場合に必要なファイルのURL
    private static String       PAGE_URL            = "regist_idm.jsp";
    private static String       PAGE_URL_TEX1500    = "ht/";
    private int                 errorCode           = 0;

    /**
     * IDmデータ取得
     * 
     * @param idm フェリカID
     * @param response レスポンス
     * 
     */
    public void findData(String idm, int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        int nPremiumPoint = 0;
        boolean ret;
        boolean retCI;
        DataUserFelica duf;
        String seq = request.getParameter( "seq" );
        GenerateXmlHapiTouchFind gxTouch;
        HotelCi hc;
        ServletOutputStream stream = null;
        UserPointPay upp;
        hc = new HotelCi();
        duf = new DataUserFelica();
        gxTouch = new GenerateXmlHapiTouchFind();
        upp = new UserPointPay();
        retCI = false;
        int nUsePoint = 0;
        try
        {
            if ( idm == null )
            {
                idm = "";
            }
            if ( seq == null || seq.compareTo( "" ) == 0 || CheckString.numCheck( seq ) == false )
            {
                seq = "0";
            }
            stream = response.getOutputStream();
            if ( idm.equals( "" ) == false )
            {
                ret = duf.getUserData( idm );
                if ( ret != false )
                {
                    nPremiumPoint = upp.getNowPoint( duf.getUserId(), false );
                }
                retCI = hc.getCheckInBeforeData( hotelId, duf.getUserId() );
                if ( retCI != false )
                {
                    nUsePoint = hc.getHotelCi().getUsePoint();
                    gxTouch.setCiCode( hc.getHotelCi().getSeq() );
                    gxTouch.setIdm( hc.getHotelCi().getIdm() );
                }
                else
                {
                    gxTouch.setCiCode( 0 );
                    gxTouch.setIdm( "" );
                }
            }
            else if ( seq.equals( "" ) == false )
            {
                ret = hc.getData( hotelId, Integer.parseInt( seq ) );
                if ( ret )
                {
                    nPremiumPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                    nUsePoint = hc.getHotelCi().getUsePoint();
                    /*
                     * if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                     * {
                     * // 予約Noが入っていたら残高を0で返してマイル使用をさせない。
                     * nPremiumPoint = 0;
                     * }
                     * else
                     * {
                     * nPremiumPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                     * }
                     */
                    // CiStatusが来店取消、料金確定していたら0を返す
                    if ( hc.getHotelCi().getCiStatus() >= 0 && hc.getHotelCi().getCiStatus() < 2 &&
                            hc.getHotelCi().getFixFlag() == 0 )
                    {
                        gxTouch.setCiCode( hc.getHotelCi().getSeq() );
                        gxTouch.setIdm( hc.getHotelCi().getIdm() );
                    }
                    else
                    {
                        gxTouch.setCiCode( 0 );
                        gxTouch.setIdm( "" );
                    }
                }
            }
            else
            {
                ret = false;
                gxTouch.setCiCode( 0 );
            }
            // xml出力クラスにノードをセット
            if ( ret != false )
            {
                // xml出力クラスに値をセット
                gxTouch.setResult( RESULT_OK );
                // メッセージノードにポイントタグ、ポイントをセット
                gxTouch.setPoint( nPremiumPoint );
                gxTouch.setUsePoint( nUsePoint );
            }
            else
            {
                // xml出力クラスに値をセット
                gxTouch.setResult( RESULT_NO );
                gxTouch.setMessage( "マイルを取得できません。" );
                // マイル取得エラー
                errorCode = 0;
            }
            // XMLの出力
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            Logging.info( xmlOut );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch findData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch findData]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * マイルなし来店データ取得
     * 
     * @param idm フェリカID
     * @param hotelId
     * @param response レスポンス
     */
    public void visitData(String idm, int hotelId, int employeeCode, int registType, int kind, HttpServletRequest request, HttpServletResponse response)
    {
        int ciCode = 0;
        boolean ret = false;
        boolean retComeMile = false;
        DataUserBasic dub;
        DataUserFelica duf;
        DataMasterPoint dmp;
        GenerateXmlHapiTouchVisit gxTouch;
        ServletOutputStream stream = null;
        UserPointPay upp;
        UserPointPayTemp uppt;
        HotelCi hc;
        DataHotelFelicaNg dhfn = new DataHotelFelicaNg();
        String userId = "";
        DataHotelBasic dhb;
        String hotelName = ""; // ←hh_hotel_basic
        String hotenaviId = ""; // ←hh_hotel_basic
        String roomNo = "";
        int ciDate = 0;
        int ciTime = 0;
        duf = new DataUserFelica();
        dmp = new DataMasterPoint();
        gxTouch = new GenerateXmlHapiTouchVisit();
        upp = new UserPointPay();
        uppt = new UserPointPayTemp();
        hc = new HotelCi();
        dhb = new DataHotelBasic();
        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();
            if ( idm == null || idm.equals( "" ) )
            {
                userId = request.getParameter( "userId" );
                if ( userId != null )
                {
                    ret = true;
                }
            }
            else
            {
                ret = duf.getUserData( idm );
                if ( ret )
                {
                    userId = duf.getUserId();
                }
            }
            // xml出力クラスにノードをセット
            if ( ret != false )
            {
                dub = new DataUserBasic();
                dub.getData( userId );
                if ( hotelId == 0 )
                {
                    hotelId = duf.getId();
                }
                // 来店マイルが追加可能かどうかをチェックする（一時の方）
                retComeMile = uppt.getMasterPointExtNum( userId, POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( retComeMile != false )
                {
                    // 来店マイルが追加可能かどうかをチェックする
                    retComeMile = upp.getMasterPointExtNum( userId, POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                // 未確定のデータが24時間以内にあったら登録しない。
                ret = hc.getCheckInBeforeData( hotelId, userId );
                if ( ret == false )
                {
                    // チェックインデータ登録
                    hc = hc.registCiData( userId, hotelId );
                    ciCode = hc.getHotelCi().getSeq();
                }
                else
                {
                    ciCode = hc.getHotelCi().getSeq();
                    if ( ciCode <= 0 )
                    {
                        // チェックインデータ取得失敗
                        errorCode = 0;
                    }
                }
                /* ▼予約データセット▼ */
                // 予約番号がない場合は予約のデータを取得しに行く
                if ( hc.getHotelCi().getRsvNo().equals( "" ) != false )
                {
                    HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
                    hc = htRsvSub.getReserveData( userId, hotelId, hc );
                    gxTouch.setRsvNo( hc.getHotelCi().getRsvNo() );
                }
                else
                {
                    gxTouch.setRsvNo( hc.getHotelCi().getRsvNo() );
                }
                /* ▲予約データセット▲ */
                // 来店マイルが付与可能であればOK付与不可能であればNGを返す
                if ( retComeMile != false )
                {
                    gxTouch.setResult( RESULT_OK );
                    if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                    {
                        gxTouch.setMessage( "ご来店ありがとうございます。\n\nマイルをご使用の際はフロントに申しつけください。" );
                    }
                    else
                    {
                        gxTouch.setMessage( "タッチを受け付けました。\n\nフロントに「 部屋番号 」をご連絡ください。" );
                    }
                }
                else
                {
                    gxTouch.setResult( RESULT_NG );
                    if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                    {
                        gxTouch.setMessage( "ご来店ありがとうございます。\n\nマイルをご使用の際はフロントに申しつけください。" );
                    }
                    else
                    {
                        gxTouch.setMessage( "本日のタッチは受け付け済みです。\n\nフロントに「 部屋番号 」をご連絡ください。" );
                    }
                }
                gxTouch.setUserType( hc.getHotelCi().getUserType() );
                if ( ret == false )
                {
                    gxTouch.setCiResult( RESULT_OK );
                }
                else
                {
                    gxTouch.setCiResult( RESULT_NG );
                    if ( idm.equals( "" ) == false )
                    {
                        // NGなのでidm情報を保存しておく
                        dhfn.insertData( hotelId, kind, idm, "" );
                    }
                }
                gxTouch.setCiCode( ciCode );
                gxTouch.setUserId( hc.getHotelCi().getUserSeq() );
                gxTouch.setPoint( upp.getNowPoint( userId, false ) );
                gxTouch.setCiDate( hc.getHotelCi().getCiDate() );
                gxTouch.setCiTime( hc.getHotelCi().getCiTime() );
                gxTouch.setRoomNo( hc.getHotelCi().getRoomNo() );
                gxTouch.setAmountRate( hc.getHotelCi().getAmountRate() );
                gxTouch.setAmount( hc.getHotelCi().getAmount() );
                gxTouch.setUsePoint( hc.getHotelCi().getUsePoint() );
                gxTouch.setSlipNo( hc.getHotelCi().getSlipNo() );
                gxTouch.setUserType( hc.getHotelCi().getUserType() );
                ciDate = hc.getHotelCi().getCiDate();
                ciTime = hc.getHotelCi().getCiTime();
                roomNo = hc.getHotelCi().getRoomNo();
            }
            else
            {
                if ( idm.equals( "" ) == false )
                {
                    // NGなのでidm情報を保存しておく
                    dhfn.insertData( hotelId, kind, idm, "" );
                }
                Logging.info( "[HapiTouch.visitData]:紐付けされていません。idm:" + idm );
                if ( registType == 0 )
                {
                    gxTouch.setResult( RESULT_NO );
                    gxTouch.setMessage( "紐付されていません。" );
                    gxTouch.setRegistUrl( BASE_URL + PAGE_URL + "?method=" + METHOD_REGIST + "&idm=" + idm );
                    gxTouch.setCiResult( RESULT_NG );
                    gxTouch.setCiCode( 0 );
                }
                else if ( registType == 1 )
                {
                    FelicaMatching fm;
                    fm = new FelicaMatching();
                    ret = fm.getIdm( idm );
                    if ( ret != false )
                    {
                        gxTouch.setAccessKey1( fm.getFelicaDataInfo().getKey1() );
                        gxTouch.setAccessKey2( fm.getFelicaDataInfo().getKey2() );
                    }
                    else
                    {
                        gxTouch.setAccessKey1( "" );
                        gxTouch.setAccessKey2( "" );
                    }
                    gxTouch.setResult( RESULT_NO );
                    gxTouch.setMessage( "紐付されていません。" );
                    gxTouch.setRegistUrl( BASE_URL + PAGE_URL_TEX1500 );
                    // gxTouch.setRegistUrl( "http://happyhotel.jp/" + PAGE_URL_TEX1500 );
                    gxTouch.setCiResult( RESULT_NG );
                    gxTouch.setCiCode( 0 );
                }
            }
            StringBuffer requestUrl = request.getRequestURL();
            String requestUrlStr = requestUrl.toString();
            if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) > 0 )
            {
                errorCode = HapiTouchErrorMessage.ERR_10303;
            }
            if ( errorCode != 0 )
            // タッチPCからオフラインの電文が送られてきた場合にはエラー履歴に書き込む
            {
                // ホテナビIDを取得する
                if ( dhb.getData( hotelId ) != false )
                {
                    hotenaviId = dhb.getHotenaviId();
                    hotelName = dhb.getName();
                }
                Logging.info( "visitData Error:" + errorCode + " requestUrlStr:" + requestUrlStr, "visitData Error" );
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( ciCode );
                daeh.setUserId( userId );
                daeh.setEntryDate( ciDate );
                daeh.setEntryTime( ciTime );
                daeh.insertData();
                errorCode = 0;
            }
            // XMLの出力
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            Logging.info( xmlOut );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch visitData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch visitData]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * フェリカIDとユーザIDの紐付け処理
     * 
     * @param reques リクエスト
     * @param response レスポンス
     */
    public void registUserFelica(HttpServletRequest request, HttpServletResponse response)
    {
        // 定義
        boolean ret;
        int carrierFlag;
        String paramUidLink;
        String paramIdm;
        String sendUrl;
        String oldUserId;
        String paramChange;
        String paramKey1;
        String paramKey2;
        DataUserFelica duf;
        duf = new DataUserFelica();
        ret = false;
        sendUrl = "";
        oldUserId = "";
        paramChange = request.getParameter( "change" );
        carrierFlag = UserAgent.getUserAgentType( request );
        dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        paramIdm = request.getParameter( "idm" );
        paramKey1 = request.getParameter( "key1" );
        paramKey2 = request.getParameter( "key2" );
        if ( (paramIdm == null) || (paramIdm.compareTo( "" ) == 0) )
        {
            paramIdm = "";
        }
        if ( (paramChange == null) || (paramChange.compareTo( "" ) == 0) || CheckString.numCheck( paramChange ) == false )
        {
            paramChange = "0";
        }
        if ( (paramKey1 == null) || (paramKey1.compareTo( "" ) == 0) || CheckString.numCheck( paramKey1 ) == false )
        {
            paramKey1 = "0";
        }
        if ( (paramKey2 == null) || (paramKey2.compareTo( "" ) == 0) || CheckString.numCheck( paramKey2 ) == false )
        {
            paramKey2 = "0";
        }
        try
        {
            // ユーザー情報の取得
            if ( dataLoginInfo_M2 == null )
            {
                if ( carrierFlag == DataMasterUseragent.CARRIER_SMARTPHONE )
                {
                    response.sendRedirect( "https://ssl.happyhotel.jp/phone/mypage/mypage_login_sp.jsp?idm=" + paramIdm );
                }
                else
                {
                    if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
                    {
                        response.sendRedirect( BASE_URL + "i/free/mymenu/hapitouch_join.jsp?" + paramUidLink );
                    }
                    else if ( carrierFlag == DataMasterUseragent.CARRIER_AU )
                    {
                        response.sendRedirect( BASE_URL + "au/free/mymenu/hapitouch_join.jsp?" + paramUidLink );
                    }
                    else if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
                    {
                        response.sendRedirect( BASE_URL + "y/free/mymenu/hapitouch_join.jsp?" + paramUidLink );
                    }
                }
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch ] Exception:" + e.toString() );
        }
        if ( (paramIdm.compareTo( "" ) != 0) && (paramIdm.length() == 16) )
        {
            if ( Integer.parseInt( paramChange ) == 1 )
            {
                // IDMの一致するデータを全て削除
                duf.deleteDataByIdm( paramIdm );
                // ユーザ情報でデータを取得する
                ret = duf.getData( dataLoginInfo_M2.getUserId() );
            }
            else
            {
                // フェリカIDでデータを取得する
                ret = duf.getUserDataNoCheck( paramIdm );
            }
            if ( ret != false )
            {
                oldUserId = duf.getUserId();
            }
            duf.setUserId( dataLoginInfo_M2.getUserId() );
            duf.setIdm( paramIdm );
            duf.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            duf.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            duf.setDelFlag( 0 );
            if ( ret != false )
            {
                ret = duf.updateData( oldUserId );
                Logging.info( "[ActionHapiTouch duf.updateData]:" + ret );
            }
            else
            {
                duf.setUserId( dataLoginInfo_M2.getUserId() );
                ret = duf.insertData();
                Logging.info( "[ActionHapiTouch duf.insertData]:" + ret );
            }
        }
        // アクセスキー1、2のパラメータがセットされていたら
        else if ( paramKey1.equals( "0" ) == false && paramKey2.equals( "0" ) == false )
        {
            // フェリカ紐付けデータからIDmを取得する
            DataSystemFelicaMatching dsfm = new DataSystemFelicaMatching();
            ret = dsfm.getData( paramKey1, paramKey2 );
            if ( ret != false )
            {
                paramIdm = dsfm.getIdm();
                // フェリカIDでデータを取得する
                ret = duf.getUserDataNoCheck( paramIdm );
                if ( ret == false )
                {
                    // ユーザ情報でデータを取得する
                    ret = duf.getData( dataLoginInfo_M2.getUserId() );
                }
                if ( ret != false )
                {
                    oldUserId = duf.getUserId();
                }
                duf.setUserId( dataLoginInfo_M2.getUserId() );
                duf.setIdm( paramIdm );
                duf.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                duf.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                duf.setDelFlag( 0 );
                if ( ret != false )
                {
                    ret = duf.updateData( oldUserId );
                    Logging.info( "[ActionHapiTouch duf.updateData]:" + ret );
                }
                else
                {
                    duf.setUserId( dataLoginInfo_M2.getUserId() );
                    ret = duf.insertData();
                    Logging.info( "[ActionHapiTouch duf.insertData]:" + ret );
                }
                // 登録・更新がうまくいったら、Felicaの紐付けデータを登録済みに変更する
                if ( ret != false )
                {
                    dsfm.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dsfm.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    dsfm.setRegistStatus( 1 );
                    ret = dsfm.updateData( paramKey1, paramKey2 );
                }
            }
        }
        if ( ret != false )
        {
            sendUrl = PAGE_URL;
            // ドコモ又はソフトバンクの場合はuidLinkを追加する
            if ( (carrierFlag == DataMasterUseragent.CARRIER_DOCOMO) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
            {
                sendUrl += "?" + paramUidLink;
            }
            try
            {
                response.sendRedirect( sendUrl );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch registUserFelica] Exception:" + e.toString() );
            }
        }
        else
        {
            sendUrl = PAGE_URL;
            sendUrl += "?ret=" + ret;
            // ドコモ又はソフトバンクの場合はuidLinkを追加する
            if ( (carrierFlag == DataMasterUseragent.CARRIER_DOCOMO) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
            {
                sendUrl += "&" + paramUidLink;
            }
            try
            {
                response.sendRedirect( sendUrl );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch registUserFelica] Exception:" + e.toString() );
            }
        }
    }

    /**
     * ホテル情報取得操作
     * 
     * @param licenceKey ライセンスキー
     * @param macAddr MACアドレス
     * @param response レスポンス
     **/
    public void getHotelInfo(String licenceKey, String macAddr, HttpServletRequest request, HttpServletResponse response, boolean retAuth)
    {
        final int HOTEL_INFO_INTERVAL = 0;
        boolean ret;
        boolean terminalFlag = true;
        int newTouch = 0; // 新タッチ端末対応:1
        String paramVersion;
        String paramGetRsvCount;
        HotelAuth ha;
        ServletOutputStream stream = null;
        String fileName = "";
        DataHotelAuth dha;
        DataHotelHapiTouchConfig dhhtc;
        DataRsvReserveBasic drrb;
        DataApHotelSetting dahs;
        DataApHotelTerminal daht;
        GenerateXmlHapiTouchHotelInfo gxTouch;
        FormOwnerRsvList dsp0; // form
        LogicOwnerRsvList lgLPC; // logic
        DataHotelMaster dhm;
        ha = new HotelAuth();
        dha = new DataHotelAuth();
        gxTouch = new GenerateXmlHapiTouchHotelInfo();
        String strFileURL32 = "";
        String strFileURL64 = "";
        String globalIp; // ハピホテタッチPCのグローバルIPを取得する。
        // レスポンスをセット
        try
        {
            paramVersion = request.getParameter( "version" );
            paramGetRsvCount = request.getParameter( "getRsvCount" );
            if ( paramVersion == null )
            {
                paramVersion = "";
            }
            if ( paramGetRsvCount == null || paramGetRsvCount.equals( "" ) != false || CheckString.numCheck( paramGetRsvCount ) == false )
            {
                paramGetRsvCount = "0";
            }
            if ( retAuth != false )
            {
                ret = dha.getData( licenceKey );
                if ( ret != false )
                {
                    dha.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dha.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    if ( paramVersion.equals( "" ) == false )
                    {
                        dha.setVersion( paramVersion );
                    }
                    dha.updateData( licenceKey );
                    ret = true;
                }
                else
                {
                    errorCode = HapiTouchErrorMessage.ERR_10101;
                }
            }
            else
            {
                ret = ha.registMacAddr( licenceKey, macAddr, paramVersion );
            }
            // OSの種類でインストールファイルの設置パスを変更する
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                strFileURL32 = FILE_URL32WIN;
                strFileURL64 = FILE_URL64WIN;
            }
            else
            {
                strFileURL32 = FILE_URL32;
                strFileURL64 = FILE_URL64;
            }
            if ( ret != false )
            {
                gxTouch.setResult( RESULT_OK );
                gxTouch.setMessage( "認証が完了しました。" );
                fileName = FileUrl.getFileUrl( strFileURL32, FILE_NAME );
                if ( (fileName != null) && (fileName.compareTo( "" ) != 0) )
                {
                    if ( fileName.indexOf( fileName ) > 0 )
                    {
                        fileName = fileName.substring( fileName.indexOf( fileName ) );
                    }
                    gxTouch.setFile32( INSTALL_URL32 + fileName );
                }
                // 64ビット版
                fileName = FileUrl.getFileUrl( strFileURL64, FILE_NAME );
                if ( (fileName != null) && (fileName.compareTo( "" ) != 0) )
                {
                    if ( fileName.indexOf( fileName ) > 0 )
                    {
                        fileName = fileName.substring( fileName.indexOf( fileName ) );
                    }
                    gxTouch.setFile64( INSTALL_URL64 + fileName );
                }
                if ( dha != null )
                {
                    dha = null;
                }
                dha = new DataHotelAuth();
                dha.getData( licenceKey );
                dahs = new DataApHotelSetting();
                if ( dahs.getData( dha.getId() ) != false )
                {
                    newTouch = dahs.getTerminalFlag();
                    if ( dahs.getTerminalFlag() == 0 )
                    {
                        terminalFlag = false;
                    }
                    else if ( dahs.getStartDate() > Integer.parseInt( DateEdit.getDate( 2 ) ) )
                    {
                        terminalFlag = false;
                    }
                    else if ( dahs.getEndDate() < Integer.parseInt( DateEdit.getDate( 2 ) ) )
                    {
                        terminalFlag = false;
                    }
                    else
                    {
                        terminalFlag = true;
                    }
                }
                else
                {
                    terminalFlag = false;
                }
                globalIp = request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr();
                dhm = new DataHotelMaster();
                if ( dhm.getData( dha.getId() ) != false )
                {
                    if ( dhm.getGlobalIp().equals( "0.0.0.0" ) )
                    {
                        dhm.setGlobalIp( globalIp );
                        dhm.updateData( dha.getId() );
                    }
                }
                gxTouch.setNewTouch( newTouch ); // 新タッチ端末対応物件か否か
                daht = new DataApHotelTerminal();
                if ( daht.getFrontTerminal( dha.getId() ) != false )
                {
                    if ( daht.getFrontTouchState() == 1 )
                    {
                        if ( daht.getLimitDate() < Integer.parseInt( DateEdit.getDate( 2 ) ) )
                        {
                            terminalFlag = false;
                        }
                        else if ( daht.getLimitDate() == Integer.parseInt( DateEdit.getDate( 2 ) ) && daht.getLimitTime() < Integer.parseInt( DateEdit.getTime( 1 ) ) )
                        {
                            terminalFlag = false;
                        }
                    }
                    if ( terminalFlag == false )
                    {
                        // フロントタッチを無効にする
                        daht.setFrontTouchState( 0 );
                        daht.setLimitDate( 0 );
                        daht.setLimitTime( 0 );
                        daht.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        daht.setLimitTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        daht.updateFrontData( dha.getId() );
                        gxTouch.setFrontTouchState( 0 );
                        gxTouch.setFrontTouchLimitDate( 0 );
                        gxTouch.setFrontTouchLimitTime( 0 );
                    }
                    else
                    {
                        gxTouch.setFrontTouchState( daht.getFrontTouchState() );
                        gxTouch.setFrontTouchLimitDate( daht.getLimitDate() );
                        gxTouch.setFrontTouchLimitTime( daht.getLimitTime() );
                    }
                }
                dhhtc = new DataHotelHapiTouchConfig();
                if ( dhhtc.getData( dha.getId() ) != false )
                {
                    // YYYYMMDDHHMMSSを日付と時刻に分ける
                    gxTouch.setSyncCiUpdate( dhhtc.getCiUpdate() );
                    gxTouch.setSyncCiUptime( dhhtc.getCiUptime() );
                    gxTouch.setHotelInfoInterval( dhhtc.getHotelInfoInterval() );
                }
                else
                {
                    // YYYYMMDDHHMMSSを日付と時刻に分ける
                    gxTouch.setSyncCiUpdate( 0 );
                    gxTouch.setSyncCiUptime( 0 );
                    gxTouch.setHotelInfoInterval( HOTEL_INFO_INTERVAL );
                }
                // サーバーの日付をセット
                gxTouch.setDate( DateEdit.getDate( 2 ) );
                // サーバーの時刻をセット
                gxTouch.setTime( DateEdit.getTime( 1 ) );
                // クレジット請求フラグをセット
                drrb = new DataRsvReserveBasic();
                drrb.getData( dha.getId() );
                gxTouch.setNoShowCredit( drrb.getNoshow_credit_flag() );
                // クレジット請求開始時刻をセット
                gxTouch.setChargeStartTime( ReserveCommon.getChargeStartTime() );
                // 予約開始していれば
                if ( (drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 1) && Integer.parseInt( paramGetRsvCount ) == 1 )
                {
                    dsp0 = new FormOwnerRsvList();
                    lgLPC = new LogicOwnerRsvList();
                    dsp0.setSelHotelID( dha.getId() );
                    dsp0.setDateFrom( DateEdit.getDate( 1 ) ); // システム日付
                    dsp0.setDateTo( DateEdit.getDate( 1 ) ); // システム日付
                    lgLPC.setDateFrom( DateEdit.getDate( 1 ) );
                    lgLPC.setDateTo( DateEdit.getDate( 1 ) );
                    lgLPC.setHotelId( dha.getId() );
                    ret = lgLPC.getDailyCount( dsp0, 5 );
                    if ( ret != false )
                    {
                        gxTouch.setRsvCount( lgLPC.getIdList().size() );
                    }
                    else
                    {
                        gxTouch.setRsvCount( 0 );
                    }
                }
            }
            else
            {
                gxTouch.setResult( RESULT_NG );
                gxTouch.setErrorCode( errorCode );
                gxTouch.setMessage( "認証に失敗しました。" );
            }
            // ここで表示するGenerateXmlクラスを
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getHotelInfo]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch getHotelInfo]Exception:" + e.toString() );
                }
            }
        }
    }

    /***
     * エラーメッセージ出力処理
     * 
     * @param root ルートノードネーム
     * @param message エラーメッセージ
     * @param response レスポンス
     */
    public void getAvailableRooms(HttpServletResponse response)
    {
        GenerateXmlHapiTouchGetAvailableRooms gxTouch;
        ServletOutputStream stream = null;
        try
        {
            stream = response.getOutputStream();
            gxTouch = new GenerateXmlHapiTouchGetAvailableRooms();
            // xml出力クラスにノードをセット
            gxTouch.setResult( RESULT_OK );
            // XMLの出力
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
                }
            }
        }
    }

    /***
     * エラーメッセージ出力処理
     * 
     * @param root ルートノードネーム
     * @param message エラーメッセージ
     * @param response レスポンス
     */
    public void errorData(String root, String message, HttpServletResponse response)
    {
        GenerateXmlHapiTouchHotelInfo gxTouch;
        ServletOutputStream stream = null;
        try
        {
            stream = response.getOutputStream();
            gxTouch = new GenerateXmlHapiTouchHotelInfo();
            // xml出力クラスにノードをセット
            gxTouch.setResult( RESULT_DENY );
            gxTouch.setMessage( message );
            // XMLの出力
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * MACアドレス認証処理
     * 
     * @param licenceKey ライセンスキー
     * @param macAddr MACアドレス
     **/
    public int authMacAddr(String licenceKey, String macAddr)
    {
        int hotelId;
        boolean ret;
        DataHotelAuth dha;
        ret = false;
        hotelId = 0;
        dha = new DataHotelAuth();
        ret = dha.getValidData( licenceKey, macAddr );
        if ( ret != false )
        {
            hotelId = dha.getId();
        }
        return(hotelId);
    }

    /****
     * チェックインデータ変更
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void modifyCi(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        int nowPoint;
        int nAmountPoint;
        int nVisitPoint = 0;
        boolean ret;
        boolean retIsUppt;
        boolean retIsUsed;
        boolean retUpdUppt;
        boolean retUpdUpp;
        boolean retUpdBko;
        boolean retInsCi;
        String paramIdm;
        String paramPrice;
        String paramPoint;
        String paramSlipNo;
        String paramRoomNo;
        String paramEmployeeCode;
        String paramSeq;
        String paramFix;
        String paramAllUse;
        // DataUserFelica duf;
        DataHotelBasic dhb;
        HotelCi hc;
        DataHotelCi dhc;
        GenerateXmlHapiTouchModifyCi gxTouch;
        ServletOutputStream stream = null;
        UserPointPayTemp uppt;
        UserPointPay upp;
        DataMasterPoint dmp;
        HapiTouchBko bko = new HapiTouchBko();
        // duf = new DataUserFelica();
        gxTouch = new GenerateXmlHapiTouchModifyCi();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        dhb = new DataHotelBasic();
        hc = new HotelCi();
        dhc = new DataHotelCi();
        nowPoint = 0;
        nAmountPoint = -1;
        ret = false;
        retIsUppt = false;
        retIsUsed = false;
        retUpdUppt = false;
        retUpdUpp = false;
        retUpdBko = false;
        retInsCi = false;
        paramIdm = request.getParameter( "idm" );
        paramPrice = request.getParameter( "price" );
        if ( paramPrice == null )
        {
            paramPrice = (String)request.getAttribute( "price" );
        }
        paramPoint = request.getParameter( "point" );
        paramSlipNo = request.getParameter( "slipNo" );
        paramRoomNo = request.getParameter( "roomNo" );
        paramEmployeeCode = request.getParameter( "employeeCode" );
        paramSeq = request.getParameter( "seq" );
        if ( paramSeq == null )
        {
            paramSeq = (String)request.getAttribute( "seq" );
        }
        paramAllUse = request.getParameter( "allUse" );
        paramFix = request.getParameter( "fix" );
        if ( paramFix == null )
        {
            paramFix = (String)request.getAttribute( "fix" );
        }
        Logging.info( "idm:" + paramIdm + ",Price:" + paramPrice + ",Point:" + paramPoint + ",SlipNo:" + paramSlipNo + ",roomNo:" + paramRoomNo + ",employeeCode:" + paramEmployeeCode + ",seq:" + paramSeq + ",allUse:" + paramAllUse + ",paramFix:"
                + paramFix, "" );
        String userId = ""; // ←hh_hotel_ci
        int ciStatus = 0; // ←hh_hotel_ci
        int ciFixFlag = 0; // ←hh_hotel_ci
        int ciAllUseFlag = 0; // ←hh_hotel_ci
        String ciRoomNo = ""; // ←hh_hotel_ci
        int ciSlipNo = 0; // ←hh_hotel_ci
        int ciUserSeq = 0; // ←hh_hotel_ci
        int ciVisitSeq = 0; // ←hh_hotel_ci
        int ciUsePoint = 0; // ←hh_hotel_ci
        int ciAllUsePoint = 0; // ←hh_hotel_ci
        int ciAmount = 0; // ←hh_hotel_ci
        double ciAmountRate = 0; // ←hh_hotel_ci
        int ciSubSeq = 0; // ←hh_hotel_ci
        String ciRsvNo = "";
        String hotelName = ""; // ←hh_hotel_basic
        String hotenaviId = ""; // ←hh_hotel_basic
        // レスポンスをセット
        try
        {
            // パラメータチェック
            if ( paramIdm == null )
            {
                paramIdm = "";
            }
            if ( (paramPrice == null) || (paramPrice.compareTo( "" ) == 0) || CheckString.numCheck( paramPrice ) == false )
            {
                paramPrice = "-1";
            }
            if ( (paramPoint == null) || (paramPoint.compareTo( "" ) == 0) || CheckString.numCheck( paramPoint ) == false )
            {
                paramPoint = "0";
            }
            if ( (paramSlipNo == null) || (paramSlipNo.compareTo( "" ) == 0) || CheckString.numCheck( paramSlipNo ) == false )
            {
                paramSlipNo = "0";
            }
            if ( paramRoomNo == null )
            {
                paramRoomNo = "";
            }
            else
            {
                paramRoomNo = new String( URLDecoder.decode( paramRoomNo, "8859_1" ).getBytes( "8859_1" ), "utf-8" );
            }
            if ( (paramEmployeeCode == null) || (paramEmployeeCode.compareTo( "" ) == 0) || CheckString.numCheck( paramEmployeeCode ) == false )
            {
                paramEmployeeCode = "0";
            }
            if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( (paramFix == null) || (paramFix.compareTo( "" ) == 0) || CheckString.numCheck( paramFix ) == false )
            {
                paramFix = "0";
            }
            if ( (paramAllUse == null) || (paramAllUse.compareTo( "" ) == 0) || CheckString.numCheck( paramAllUse ) == false )
            {
                paramAllUse = "0";
            }
            if ( Integer.parseInt( paramSeq ) > 0 )
            {
                // ホテルチェックインデータを取得する
                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
                if ( ret != false )
                {
                    dhc = hc.getHotelCi();
                    userId = dhc.getUserId();
                    ciStatus = dhc.getCiStatus();
                    ciFixFlag = dhc.getFixFlag();
                    ciAllUseFlag = dhc.getAllUseFlag();
                    ciRoomNo = dhc.getRoomNo();
                    // チェックインデータが更新されていない可能性があるため、必要な情報をセットする
                    if ( paramRoomNo.equals( "" ) == false && dhc.getRoomNo().equals( "" ) != false )
                    {
                        dhc.setRoomNo( paramRoomNo );
                    }
                    if ( Integer.parseInt( paramSlipNo ) > 0 && dhc.getSlipNo() == 0 )
                    {
                        dhc.setSlipNo( Integer.parseInt( paramSlipNo ) );
                    }
                    ciSlipNo = dhc.getSlipNo();
                    ciUserSeq = dhc.getUserSeq();
                    ciVisitSeq = dhc.getVisitSeq();
                    ciUsePoint = dhc.getUsePoint();
                    ciAllUsePoint = dhc.getAllUsePoint();
                    ciAmount = dhc.getAmount();
                    ciAmountRate = dhc.getAmountRate();
                    ciSubSeq = dhc.getSubSeq();
                    ciRsvNo = dhc.getRsvNo();
                }
            }
            // ホテナビIDを取得する
            if ( dhb.getData( hotelId ) != false )
            {
                hotenaviId = dhb.getHotenaviId();
                hotelName = dhb.getName();
            }
            Logging.info( "STEP00.1", "ret:" + ret + ",status:" + ciStatus + ",userId:" + userId + ",rsvNo:" + ciRsvNo + ",ciFixFlag:" + ciFixFlag + ",paramFix:" + paramFix );
            StringBuffer requestUrl = request.getRequestURL();
            String requestUrlStr = requestUrl.toString();
            if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) > 0 )
            {
                errorCode = HapiTouchErrorMessage.ERR_10514;
            }
            if ( errorCode != 0 )
            // タッチPCからオフラインの電文が送られてきた場合にはエラー履歴に書き込む
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( paramRoomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
                errorCode = 0;
            }
            // 来店取消の場合は何も変更させない
            if ( ciStatus >= 2 )
            {
                gxTouch.setResult( RESULT_NG );
                gxTouch.setCiStatus( ciStatus );
                if ( ciStatus == 4 )
                {
                    ciStatus = 3;
                    dhc.setCiStatus( ciStatus );
                    dhc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dhc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = dhc.updateData( hotelId, Integer.parseInt( paramSeq ), ciSubSeq );
                }
            }
            else
            {

                if ( ret != false )
                {
                    if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) == -1 && !paramPoint.equals( "0" ) && request.getParameter( "code" ) != null && request.getParameter( "uuid" ) != null )
                    { // 連動物件で、タッチPCからのマイル使用は許可しない
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setUseResult( RESULT_NG );
                        errorCode = HapiTouchErrorMessage.ERR_10516;
                        gxTouch.setErrorCode( errorCode );
                        dhc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dhc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dhc.updateData( hotelId, Integer.parseInt( paramSeq ), ciSubSeq );
                    }
                    else if ( !ciRsvNo.equals( "" ) && Integer.parseInt( paramPoint ) != 0 )
                    {
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setUseResult( RESULT_NG );
                        errorCode = HapiTouchErrorMessage.ERR_10512;
                        gxTouch.setErrorCode( errorCode );
                    }
                    else
                    {
                        if ( UserLoginInfo.checkMileUser( userId ) != false )// マイル加算対象 2015.3.26sakurai追加
                        {
                            // ret = duf.getUserData( paramIdm );
                            // ↓使っていないのでコメントアウト 2015.04.17 sakurai
                            // duf.getData( dhc.getUserId() );
                            // 現在のポイントを取得
                            nowPoint = upp.getNowPoint( userId, false );
                            // 全使用フラグのリクエストで、チェックインデータ（hh_hotel_ci）の全使用フラグがたっていない場合のみ
                            if ( Integer.parseInt( paramAllUse ) == 1 && ciAllUseFlag == 0 )
                            {
                                // 全マイル使用処理
                                if ( this.allUsePoint( userId, hc, Integer.parseInt( paramEmployeeCode ), Integer.parseInt( paramPoint ) ) != false )
                                {
                                    // 全マイル使用処理がうまくいったら、更新後のチェックインデータを読み込む
                                    ciAllUseFlag = 1;
                                    if ( hc.getData( hotelId, Integer.parseInt( paramSeq ) ) != false )
                                    {
                                        dhc = hc.getHotelCi();
                                        ciAllUseFlag = dhc.getAllUseFlag();
                                        ciUsePoint = dhc.getUsePoint();
                                        ciAmount = dhc.getAmount();
                                    }
                                }
                            }
                            // 追加か更新かを判断する
                            // チェックインデータ（hh_hotel_ci）に部屋番号が登録済みで、リクエストの部屋番号が違っていたらインサート
                            if ( ciRoomNo.equals( "" ) == false && paramRoomNo.equals( "" ) == false && ciRoomNo.equals( paramRoomNo ) == false )
                            {
                                retInsCi = true;
                            }
                            // チェックインデータ（hh_hotel_ci）に伝票番号が登録済みで、リクエストの伝票番号が違っていたらインサート
                            else if ( ciSlipNo > 0 && Integer.parseInt( paramSlipNo ) > 0 && Integer.parseInt( paramSlipNo ) != ciSlipNo )
                            {
                                retInsCi = true;
                            }
                            Logging.info( "STEP01", "" + retInsCi );
                            // 全額利用をしていない場合で、
                            if ( ciAllUseFlag == 0 )
                            {
                                Logging.info( "STEP02", "ciUsePoint:" + ciUsePoint + ",paramPoint:" + paramPoint + ",paramPrice:" + paramPrice );
                                // ポイントが入っていたらマイル使用
                                if ( (Integer.parseInt( paramPoint ) > 0 && Integer.parseInt( paramPoint ) != ciUsePoint)
                                        || (ciUsePoint > 0 && Integer.parseInt( paramPrice ) >= 0/* && ciUsePoint > Integer.parseInt( paramPrice ) */) )
                                {
                                    retIsUsed = true;
                                    // 金額より使用マイルが大きい場合は、使用マイルを金額と同じにする。
                                    if ( ciUsePoint > 0 && Integer.parseInt( paramPrice ) >= 0 && ciUsePoint > Integer.parseInt( paramPrice ) )
                                    {
                                        dhc.setUsePoint( Integer.parseInt( paramPrice ) );
                                        ciUsePoint = Integer.parseInt( paramPrice );
                                        paramPoint = paramPrice;
                                    }
                                    Logging.info( "STEP03.0", "ciUsePoint:" + ciUsePoint + ",paramPoint:" + paramPoint );
                                    if ( Integer.parseInt( paramPoint ) != 0 )
                                    {
                                        if ( Integer.parseInt( paramPoint ) != ciUsePoint )
                                        {
                                            ciUsePoint = Integer.parseInt( paramPoint );
                                        }
                                    }
                                    if ( ciUsePoint != 0 )
                                    {
                                        // 既に使用マイルデータがあるかどうかを確認する
                                        retIsUppt = uppt.getUserPointHistory( userId, hotelId, POINT_KIND_WARIBIKI, ciUserSeq, ciVisitSeq );
                                        if ( retIsUppt != false )
                                        {
                                            // データがあるため、使用マイルを更新
                                            retUpdUppt = uppt.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            if ( retUpdUppt != false ) // 予約Noが入っていたら使用マイルは更新しない。
                                            {
                                                // hh_user_point_payの使用マイルの更新
                                                retUpdUpp = upp.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            }
                                        }
                                        else
                                        {
                                            // データがないため、hh_user_point_pay_tempに使用マイルを追加
                                            retUpdUppt = uppt.setUsePoint( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            if ( retUpdUppt != false )
                                            {
                                                if ( !dhc.getRsvNo().equals( "" ) )
                                                {
                                                    // 事前予約の場合は、すでに使用マイルが入っているので、user_seq とvisit_seqのみを更新する
                                                    retUpdUpp = upp.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                                }
                                                else
                                                {
                                                    // hh_user_point_payに使用マイルの追加
                                                    retUpdUpp = upp.setUsePoint( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                                }
                                            }
                                        }
                                        Logging.info( "STEP03", "retUpdUpp:" + retUpdUpp + ",usePoint:" + dhc.getUsePoint() );
                                        // 使用マイルを更新・追加できたかどうか
                                        if ( retUpdUpp != false )
                                        {
                                            // OKだったら引いた後のポイントを取得
                                            nowPoint = upp.getNowPoint( userId, false );
                                            gxTouch.setUseResult( RESULT_OK );
                                        }
                                        else
                                        {
                                            gxTouch.setUseResult( RESULT_NG );
                                            // マイル使用失敗
                                            errorCode = HapiTouchErrorMessage.ERR_10505;
                                        }
                                    }
                                }
                                // 料金が入っていたらマイル付与を行う
                                if ( Integer.parseInt( paramPrice ) >= 0 )
                                {
                                    retIsUppt = uppt.getUserPointHistory( userId, hotelId, POINT_KIND_RAITEN, ciUserSeq, ciVisitSeq );
                                    if ( retIsUppt == false )
                                    {
                                        // 来店ハピー加算(一時保存テーブルに保存する)
                                        nVisitPoint = uppt.setVisitPoint( userId, COME_POINT, nowPoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                        if ( nVisitPoint > 0 )
                                        {
                                            gxTouch.setVisitResult( RESULT_OK );
                                            dhc.setVisitPoint( nVisitPoint );
                                        }
                                        else
                                        {
                                            gxTouch.setVisitResult( RESULT_NG );
                                            // 来店マイル付与エラー
                                            if ( nVisitPoint < 0 )
                                            {
                                                errorCode = HapiTouchErrorMessage.ERR_10504;
                                            }
                                        }
                                    }
                                    Logging.info( "STEP05", "nVisitPoint:" + nVisitPoint );
                                    // チェックインデータが更新されていない可能性があるため、必要な情報をセットする
                                    if ( paramRoomNo.equals( "" ) == false )
                                    {
                                        dhc.setRoomNo( paramRoomNo );
                                    }
                                    if ( Integer.parseInt( paramSlipNo ) > 0 )
                                    {
                                        dhc.setSlipNo( Integer.parseInt( paramSlipNo ) );
                                    }
                                    // ポイント履歴の確認
                                    retIsUppt = uppt.getUserPointHistory( userId, hotelId, POINT_KIND_RIYOU, ciUserSeq, ciVisitSeq );
                                    if ( retIsUppt != false )
                                    {
                                        // 過去データがあればインサートフラグをtrueにする
                                        // retInsCi = true;
                                        // データがある場合、ポイントを更新
                                        nAmountPoint = uppt.setAmountPointUpdate( userId, AMOUNT_POINT, nowPoint, Integer.parseInt( paramPrice ), Integer.parseInt( paramEmployeeCode ), dhc );
                                    }
                                    else
                                    {
                                        // データがない場合、ポイントを追加
                                        nAmountPoint = uppt.setAmountPoint( userId, AMOUNT_POINT, nowPoint, Integer.parseInt( paramPrice ), Integer.parseInt( paramEmployeeCode ), dhc );
                                        // nAmountPoint>=0のとき
                                        // 新規に金額が入ったのでPUSHする
                                        if ( nAmountPoint >= 0 && requestUrlStr.indexOf( "happyhotel" ) == -1 && DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -2 ) < dhc.getCiDate() )
                                        {
                                            TokenUser tk = new TokenUser();
                                            tk.sendCheckOutMessage( userId, hotelId );
                                        }
                                    }
                                    Logging.info( "STEP06", "nAmountPoint:" + nAmountPoint );
                                    if ( nAmountPoint >= 0 )
                                    {
                                        gxTouch.setAmountResult( RESULT_OK );
                                        // ポイント履歴がある場合
                                        if ( retIsUppt != false && retInsCi != false )
                                        {
                                            if ( retInsCi != false )
                                            {
                                                // マイナスの履歴追加
                                                hc.setMinusHistory( dhc, Integer.parseInt( paramEmployeeCode ) );
                                                // 再度最新のデータを取得する
                                                if ( hc.getData( hotelId, Integer.parseInt( paramSeq ) ) != false )
                                                {
                                                    dhc = hc.getHotelCi();
                                                    userId = dhc.getUserId();
                                                    ciStatus = dhc.getCiStatus();
                                                    ciFixFlag = dhc.getFixFlag();
                                                    ciAllUseFlag = dhc.getAllUseFlag();
                                                    ciRoomNo = dhc.getRoomNo();
                                                    ciSlipNo = dhc.getSlipNo();
                                                    ciUserSeq = dhc.getUserSeq();
                                                    ciVisitSeq = dhc.getVisitSeq();
                                                    ciUsePoint = dhc.getUsePoint();
                                                    ciAllUsePoint = dhc.getAllUsePoint();
                                                    ciAmount = dhc.getAmount();
                                                    ciAmountRate = dhc.getAmountRate();
                                                    ciSubSeq = dhc.getSubSeq();
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        gxTouch.setAmountResult( RESULT_NG );
                                        // マイル付与エラー
                                        errorCode = HapiTouchErrorMessage.ERR_10506;
                                    }
                                    Logging.info( "STEP07", "retUpdBko:" + retUpdBko + ",retIsUppt:" + retIsUppt + ",retInsCi:" + retInsCi );
                                }
                                // 金額確定のステータスがあった場合は使用マイルを確認する
                                if ( Integer.parseInt( paramFix ) == 1 )
                                {
                                    if ( ciUsePoint > 0 && ciAmount >= 0 )
                                    {
                                        if ( ciUsePoint > (Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount) )
                                        {
                                            dhc.setUsePoint( Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount );
                                            ciUsePoint = Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount;
                                        }
                                        // 既に使用マイルデータがあるかどうかを確認する
                                        retIsUppt = uppt.getUserPointHistory( userId, hotelId, POINT_KIND_WARIBIKI, ciUserSeq, ciVisitSeq );
                                        if ( retIsUppt != false )
                                        {
                                            // 過去データがあればインサートフラグをtrueにする
                                            // = true;
                                            // データがあるため、使用マイルを更新
                                            retUpdUppt = uppt.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            if ( retUpdUppt != false )
                                            {
                                                // hh_user_point_payに使用マイルの追加
                                                retUpdUpp = upp.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            }
                                        }
                                        else
                                        {
                                            // データがないため、hh_user_point_pay_tempに使用マイルを追加
                                            retUpdUppt = uppt.setUsePoint( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            if ( retUpdUppt != false )
                                            {
                                                if ( !dhc.getRsvNo().equals( "" ) )
                                                {
                                                    // 事前予約の場合は、すでに使用マイルが入っているので、user_seq とvisit_seqのみを更新する
                                                    retUpdUpp = upp.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                                }
                                                else
                                                {
                                                    // hh_user_point_payに使用マイルの追加
                                                    retUpdUpp = upp.setUsePoint( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                                }
                                            }
                                        }
                                    }
                                    Logging.info( "STEP08", "paramPoint:" + paramPoint + ", ciUsePoint:" + ciUsePoint + ", ciAmount:" + ciAmount );
                                }
                            }
                            else
                            // 全額使用
                            {
                                if ( Integer.parseInt( paramFix ) == 0 )
                                {
                                    dmp = new DataMasterPoint();
                                    // ポイントが入っていたらマイル使用
                                    if ( Integer.parseInt( paramPoint ) > 0 && Integer.parseInt( paramPoint ) != ciUsePoint )
                                    {
                                        retIsUsed = true;
                                    }
                                    // 料金が入っていたらマイル付与を行う
                                    if ( Integer.parseInt( paramPrice ) >= 0 )
                                    {
                                        nAmountPoint = (int)(ciAmountRate * Integer.parseInt( paramPrice ) / 100);
                                        // 来店マイルが追加可能かどうかをチェックする（一時の方）
                                        if ( uppt.getMasterPointExtNum( userId, POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) ) != false )
                                        {
                                            // 来店マイルが追加可能かどうかをチェックする
                                            if ( upp.getMasterPointExtNum( userId, POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) ) != false )
                                            {
                                                nVisitPoint = 1;
                                            }
                                        }
                                    }
                                    Logging.info( "STEP09", "paramPoint:" + paramPoint + ", ciUsePoint:" + ciUsePoint );
                                }
                                // fix=1になったら
                                else if ( Integer.parseInt( paramFix ) == 1 )
                                {
                                    // マイルを使用する
                                    ret = this.returnUsePoint( userId, hc, Integer.parseInt( paramEmployeeCode ) );
                                    if ( ret != false )
                                    {
                                        if ( ciAllUsePoint >= (Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount) )
                                        {
                                            dhc.setUsePoint( (Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount) );
                                        }
                                        else
                                        {
                                            dhc.setUsePoint( ciAllUsePoint );
                                        }
                                    }
                                    // 来店マイルを付与する
                                    nVisitPoint = uppt.setVisitPoint( userId, COME_POINT, nowPoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                    if ( nVisitPoint > 0 )
                                    {
                                        gxTouch.setVisitResult( RESULT_OK );
                                        dhc.setVisitPoint( nVisitPoint );
                                    }
                                    // マイル付与の履歴を追加
                                    nAmountPoint = uppt.setAmountPoint( userId, AMOUNT_POINT, nowPoint, ciAmount, Integer.parseInt( paramEmployeeCode ), dhc );
                                    // マイルを付与する
                                    if ( nAmountPoint > 0 )
                                    {
                                        paramPoint = Integer.toString( ciAmount );
                                    }
                                    Logging.info( "STEP10", "ciAmount:" + ciAmount + ", ciAllUsePoint:" + ciAllUsePoint );
                                }
                            }
                        }
                        else
                        {
                            ret = true;
                            dhc.setCiStatus( 3 ); // 未ログイン会員の場合はなかったことにする
                            errorCode = HapiTouchErrorMessage.ERR_10513;
                        }

                        // 現在のポイントをセット nowPoint = upp.getNowPoint( userId, false );
                        gxTouch.setPoint( nowPoint );
                        if ( ret != false )
                        {
                            dhc = hc.getHotelCi();
                            // 全マイル使用の場合は、精算時に来店マイルが確定する。全マイル使用でなければ来店マイルを書き換えるが、全マイル使用の場合は、FIX時のみに書き換える
                            if ( nVisitPoint > 0 && (ciAllUseFlag == 0 || Integer.parseInt( paramFix ) == 1) )
                            {
                                dhc.setVisitPoint( nVisitPoint );
                            }
                            dhc.setUserId( userId );
                            dhc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            dhc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            if ( paramRoomNo.equals( "" ) == false )
                            {
                                dhc.setRoomNo( paramRoomNo );
                            }
                            if ( Integer.parseInt( paramSlipNo ) > 0 )
                            {
                                dhc.setSlipNo( Integer.parseInt( paramSlipNo ) );
                            }
                            if ( nAmountPoint > 0 )
                            {
                                dhc.setCiStatus( 1 );
                                if ( Integer.parseInt( paramPrice ) > 0 )
                                {
                                    dhc.setAmount( Integer.parseInt( paramPrice ) );
                                    ciAmount = Integer.parseInt( paramPrice );
                                }
                                dhc.setAddPoint( nAmountPoint );
                                dhc.setAddDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                dhc.setAddTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                dhc.setAddHotenaviId( hotenaviId );
                                dhc.setAddEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            else if ( nAmountPoint == 0 )
                            {
                                dhc.setCiStatus( 1 );
                                if ( Integer.parseInt( paramPrice ) > 0 )
                                {
                                    dhc.setAmount( Integer.parseInt( paramPrice ) );
                                    ciAmount = Integer.parseInt( paramPrice );
                                }
                                dhc.setAddHotenaviId( hotenaviId );
                                dhc.setAddEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            if ( retIsUsed != false )
                            {
                                // HotelCiを登録する
                                if ( Integer.parseInt( paramPoint ) > 0 )
                                {
                                    dhc.setUsePoint( Integer.parseInt( paramPoint ) );
                                }
                                dhc.setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                dhc.setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                dhc.setUseHotenaviId( hotenaviId );
                                dhc.setUseEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            if ( Integer.parseInt( paramFix ) == 1 )
                            {
                                dhc.setFixFlag( 1 );
                            }
                            Logging.info( "STEP11", "retIsUsed:" + retIsUsed + ",paramPoint:" + paramPoint + ",retInsCi:" + retInsCi + ",paramFix:" + paramFix );
                            // インサート対象だったら、枝番号を追加してインサート
                            if ( retInsCi != false )
                            {
                                // Logging.info( "" + dhc.getSubSeq(), "" + ciSubSeq );
                                dhc.setSubSeq( ciSubSeq + 1 );
                                ret = dhc.insertData();
                                if ( ret != false )
                                {
                                    hc.registTouchCi( dhc, retIsUsed );
                                }
                                /** ハピホテタッチ端末用に追加 **/
                                else
                                {
                                    // チェックインデータ追加エラー
                                    errorCode = HapiTouchErrorMessage.ERR_10503;
                                }
                            }
                            else
                            {
                                // HotelCiを更新する
                                Logging.info( "dhc", "slipNo:" + dhc.getSlipNo() + ",AddPoint:" + dhc.getAddPoint() + ",VisitPoint:" + dhc.getVisitPoint() );
                                ret = dhc.updateData( hotelId, Integer.parseInt( paramSeq ), ciSubSeq );
                                if ( ret != false )
                                {
                                    hc.registTouchCi( dhc, retIsUsed );
                                }
                                /** ハピホテタッチ端末用更新 **/
                                else
                                {
                                    // チェックインデータ更新エラー
                                    errorCode = HapiTouchErrorMessage.ERR_10502;
                                }
                            }
                            if ( ret != false )
                            {
                                if ( !dhc.getRsvNo().equals( "" ) )
                                {
                                    /** 予約ボーナスマイルの更新 */
                                    if ( uppt.setAddBonusMile( userId, nowPoint, Integer.parseInt( paramEmployeeCode ), dhc ) != false )
                                    {
                                    }
                                }
                            }
                            if ( ret != false )
                            {
                                Logging.info( "STEP12", "paramSlipNo:" + paramSlipNo + ", nAmountPoint:" + nAmountPoint + ", ciAmount:" + ciAmount );
                                // 料金が登録されており、金額の変更がなかったら更新を行う。
                                if ( (paramRoomNo.equals( "" ) == false || Integer.parseInt( paramSlipNo ) > 0) && (nAmountPoint == 0 && ciAmount > 0) )
                                {
                                    // ホテル利用マイル更新
                                    nAmountPoint = uppt.setAmountPointUpdate( userId, AMOUNT_POINT, nowPoint, ciAmount, Integer.parseInt( paramEmployeeCode ), dhc );
                                    if ( nAmountPoint <= 0 )
                                    {
                                        // 付与マイル更新エラー
                                        errorCode = HapiTouchErrorMessage.ERR_10506;
                                    }
                                }
                                if ( Integer.parseInt( paramFix ) == 1 )
                                {
                                    if ( !dhc.getRsvNo().equals( "" ) ) // 予約部屋在庫を開放する
                                    {
                                        LogicOwnerRsvCheckIn logic = new LogicOwnerRsvCheckIn();
                                        if ( logic.releaseRoomRemaindar( hotelId, dhc.getRoomNo(), dhc.getRsvNo(), dhc.getCiDate() ) )
                                        {
                                            if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) && requestUrlStr.indexOf( "happyhotel" ) == -1 && DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -2 ) < dhc.getCiDate() )
                                            {
                                                RsvList rl = new RsvList();
                                                // 要確認
                                                if ( rl.getData( hotelId, dhc.getCiDate(), dhc.getCiDate(), 0, dhc.getRsvNo(), 1 ) != false )
                                                {
                                                    rl.sendToHost( hotelId );
                                                }
                                            }
                                        }
                                        else
                                        {
                                            DataApErrorHistory daeh = new DataApErrorHistory();
                                            daeh.setErrorCode( HapiTouchErrorMessage.ERR_10515 );
                                            daeh.setErrorSub( 0 );
                                            daeh.setHotelName( hotelName );
                                            daeh.setHotenaviId( hotenaviId );
                                            daeh.setRoomName( paramRoomNo );
                                            daeh.setId( hotelId );
                                            daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                            daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                            daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                                            daeh.setUserId( userId );
                                            daeh.insertData();
                                        }
                                    }
                                    // TokenUser tk = new TokenUser();
                                    // tk.sendCheckOutMessage( userId, hotelId );
                                }

                                gxTouch.setResult( RESULT_OK );
                                gxTouch.setCiStatus( ciStatus );
                            }
                            else
                            {
                                gxTouch.setResult( RESULT_NG );
                                gxTouch.setCiStatus( 0 );
                            }
                        }
                        else
                        {
                            gxTouch.setResult( RESULT_NG );
                            gxTouch.setCiStatus( 0 );
                        }
                    }
                }
                else
                {
                    if ( ciFixFlag == 1 )
                    {
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setCiStatus( ciStatus );
                    }
                    else
                    {
                        gxTouch.setResult( RESULT_NO );
                        gxTouch.setErrorCode( HapiTouchErrorMessage.ERR_10501 );
                        gxTouch.setRegistUrl( BASE_URL + PAGE_URL + "?method=" + METHOD_REGIST + "&idm=" + paramIdm );
                    }
                }
            }
            if ( errorCode == 0 && ReserveCommon.isSyncReserve( hotelId ) )
            {
                if ( dhc.getRsvNo().equals( "" ) == false && !ciRoomNo.equals( paramRoomNo ) )
                {
                    // 部屋替えを行う
                    HapiTouchRsv.rsvRoomChange( hotelId, dhc.getRsvNo(), paramRoomNo );
                }
            }
            if ( errorCode == 0 )
            {
                // バックオフィスデータ更新
                retUpdBko = bko.updateBkoData( userId, hotelId, dhc );
                // バックオフィスマイル更新エラー
                if ( retUpdBko == false )
                {
                    errorCode = HapiTouchErrorMessage.ERR_10510;
                }
            }
            if ( errorCode != 0 )
            // エラーが発生していた場合にはエラー履歴に書き込む
            {
                Logging.info( "ModifyCi Error:" + errorCode, "ModifyCi Error" );
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( paramRoomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
            }
            if ( requestUrlStr.indexOf( "owner.hotenavi.com" ) == -1 )
            {
                stream = response.getOutputStream();
                // XMLの出力
                String xmlOut = gxTouch.createXml();
                Logging.info( xmlOut );
                ServletOutputStream out = null;
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch modifyCi]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch modifyCi]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * チェックインデータ変更
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void correctCi(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        int nowPoint;
        int nAmountPoint;
        int nVisitPoint = 0;
        boolean ret;
        boolean retUse;
        boolean retData;
        String hotenaviId = "";
        String hotelName = "";
        String userId = "";
        String paramIdm;
        String paramPrice;
        String paramPoint;
        String paramSlipNo;
        String paramRoomNo;
        String ciRoomNo = "";
        String paramEmployeeCode;
        String paramSeq;
        String paramFix;
        String paramAllUse;
        DataUserFelica duf;
        DataHotelBasic dhb;
        HotelCi hc;
        GenerateXmlHapiTouchModifyCi gxTouch;
        ServletOutputStream stream = null;
        UserPointPayTemp uppt;
        UserPointPay upp;
        DataMasterPoint dmp;
        HapiTouchBko bko = new HapiTouchBko();
        duf = new DataUserFelica();
        gxTouch = new GenerateXmlHapiTouchModifyCi();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        dhb = new DataHotelBasic();
        hc = new HotelCi();
        nowPoint = 0;
        nAmountPoint = -1;
        retUse = false;
        retData = false;
        int ciStatus = 0;
        paramIdm = request.getParameter( "idm" );
        paramPrice = request.getParameter( "price" );
        paramPoint = request.getParameter( "point" );
        paramSlipNo = request.getParameter( "slipNo" );
        paramRoomNo = request.getParameter( "roomNo" );
        paramEmployeeCode = request.getParameter( "employeeCode" );
        paramSeq = request.getParameter( "seq" );
        paramAllUse = request.getParameter( "allUse" );
        paramFix = request.getParameter( "fix" );
        // レスポンスをセット
        try
        {
            // パラメータチェック
            if ( paramIdm == null )
            {
                paramIdm = "";
            }
            if ( (paramPrice == null) || (paramPrice.compareTo( "" ) == 0) || CheckString.numCheck( paramPrice ) == false )
            {
                paramPrice = "-1";
            }
            if ( (paramPoint == null) || (paramPoint.compareTo( "" ) == 0) || CheckString.numCheck( paramPoint ) == false )
            {
                paramPoint = "0";
            }
            if ( (paramSlipNo == null) || (paramSlipNo.compareTo( "" ) == 0) || CheckString.numCheck( paramSlipNo ) == false )
            {
                paramSlipNo = "0";
            }
            if ( paramRoomNo == null )
            {
                paramRoomNo = "";
            }
            else
            {
                paramRoomNo = new String( URLDecoder.decode( paramRoomNo, "8859_1" ).getBytes( "8859_1" ), "utf-8" );
            }
            if ( (paramEmployeeCode == null) || (paramEmployeeCode.compareTo( "" ) == 0) || CheckString.numCheck( paramEmployeeCode ) == false )
            {
                paramEmployeeCode = "0";
            }
            if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( (paramFix == null) || (paramFix.compareTo( "" ) == 0) || CheckString.numCheck( paramFix ) == false )
            {
                paramFix = "0";
            }
            if ( (paramAllUse == null) || (paramAllUse.compareTo( "" ) == 0) || CheckString.numCheck( paramAllUse ) == false )
            {
                paramAllUse = "0";
            }
            // ホテルチェックインデータを取得する
            ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
            userId = hc.getHotelCi().getUserId();
            ciStatus = hc.getHotelCi().getCiStatus();
            ciRoomNo = hc.getHotelCi().getRoomNo();
            Logging.info( "[ActionHapiTouch correctCi]ciStatus:" + ciStatus + ",ciRoomNo:" + ciRoomNo + ",ret:" + ret );
            // 来店取消の場合,部屋替えがあったと判断 2015.3.26
            if ( ciStatus == 2 )
            {
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                hc.getHotelCi().setRoomNo( paramRoomNo );
                hc.getHotelCi().setCiStatus( 0 );
                ret = hc.getHotelCi().insertData();
                /** ハピホテタッチ端末用に追加 **/
                hc.registTouchCi( hc.getHotelCi(), false );
                /** ハピホテタッチ端末用に追加 **/
                gxTouch.setResult( RESULT_OK );
                gxTouch.setCiStatus( 0 );
                Logging.info( "[ActionHapiTouch correctCi]HotelIp.getFrontIp( hotelId, 1 ):" + HotelIp.getFrontIp( hotelId, 1 ) + ",hc.getHotelCi().getRsvNo():" + hc.getHotelCi().getRsvNo() + ",paramRoomNo" + paramRoomNo );

                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) && !hc.getHotelCi().getRsvNo().equals( "" ) && !paramRoomNo.equals( "" ) )
                {
                    RsvFix rf = new RsvFix();
                    /** （1004）ハピホテ_ハピホテタッチ予約来店要求 **/
                    rf.setSeq( hc.getHotelCi().getSeq() );
                    rf.setRsvNo( hc.getHotelCi().getRsvNo() );
                    rf.setRoomName( paramRoomNo );
                    rf.setDispRsvNo( hc.getHotelCi().getRsvNo().substring( hc.getHotelCi().getRsvNo().length() - 6 ) );
                    rf.setTouchRoomName( paramRoomNo );
                    rf.sendToHost( HotelIp.getFrontIp( hotelId, 1 ), 10000, 7046, Integer.toString( hotelId ) );

                    FormReserveSheetPC frm = new FormReserveSheetPC();
                    LogicReserveSheetPC logic = new LogicReserveSheetPC();
                    HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
                    frm.setSelHotelId( hotelId );
                    frm.setRsvNo( hc.getHotelCi().getRsvNo() );
                    // 予約データ抽出
                    logic.setFrm( frm );
                    // 実績データ取得
                    logic.getData( 2 );
                    frm.setMode( ReserveCommon.MODE_RAITEN );
                    htRsvSub.execRaiten( frm, frm.getStatus() );
                }
            }
            else if ( ciStatus == 4 )
            {
                gxTouch.setResult( RESULT_OK );
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( paramRoomNo.equals( "" ) == false && paramRoomNo.equals( hc.getHotelCi().getRoomNo() ) == false )
                {
                    hc.getHotelCi().setRoomNo( paramRoomNo );
                }
                if ( Integer.parseInt( paramSlipNo ) > 0 )
                {
                    hc.getHotelCi().setSlipNo( Integer.parseInt( paramSlipNo ) );
                }
                if ( Integer.parseInt( paramPrice ) >= 0 )
                {
                    ciStatus = 3;
                    hc.getHotelCi().setAmount( Integer.parseInt( paramPrice ) );
                }
                gxTouch.setCiStatus( ciStatus );
                hc.getHotelCi().setCiStatus( ciStatus );
                ret = hc.getHotelCi().insertData();
            }
            else
            { // xml出力クラスにノードをセット
                if ( ret != false && hc.getHotelCi().getFixFlag() == 0 )
                {
                    StringBuffer requestUrl = request.getRequestURL();
                    String requestUrlStr = requestUrl.toString();
                    if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) == -1 && !paramPoint.equals( "0" ) && request.getParameter( "code" ) != null && request.getParameter( "uuid" ) != null )
                    { // 連動物件で、タッチPCからのマイル使用は許可しない
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setUseResult( RESULT_NG );
                        errorCode = HapiTouchErrorMessage.ERR_10516;
                        hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        hc.getHotelCi().updateData( hotelId, Integer.parseInt( paramSeq ), hc.getHotelCi().getSubSeq() );
                    }
                    else if ( !hc.getHotelCi().getRsvNo().equals( "" ) && Integer.parseInt( paramPoint ) != 0 )
                    {
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setUseResult( RESULT_NG );
                        errorCode = HapiTouchErrorMessage.ERR_10512;
                    }
                    else
                    {
                        duf.getData( hc.getHotelCi().getUserId() );
                        // ホテナビIDを取得する
                        dhb.getData( hotelId );
                        if ( dhb.getId() > 0 )
                        {
                            hotenaviId = dhb.getHotenaviId();
                            hotelName = dhb.getName();
                        }
                        nowPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                        // 全使用フラグのリクエストで全使用フラグがたっていない場合のみ
                        if ( Integer.parseInt( paramAllUse ) == 1 && hc.getHotelCi().getAllUseFlag() == 0 )
                        {
                            ret = this.allUsePoint( hc.getHotelCi().getUserId(), hc, Integer.parseInt( paramEmployeeCode ), Integer.parseInt( paramPoint ) );
                            if ( ret != false )
                            {
                                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
                            }
                        }
                        if ( hc.getHotelCi().getAllUseFlag() == 0 )
                        {
                            // 料金が入っていればマイル付与の変更を行う
                            if ( Integer.parseInt( paramPrice ) >= 0 )
                            {
                                // 来店ハピー加算(一時保存テーブルに保存する)
                                nVisitPoint = uppt.setVisitPoint( hc.getHotelCi().getUserId(), COME_POINT, nowPoint, Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                                if ( nVisitPoint > 0 )
                                {
                                    gxTouch.setVisitResult( RESULT_OK );
                                    hc.getHotelCi().setVisitPoint( nVisitPoint );
                                }
                                else
                                {
                                    gxTouch.setVisitResult( RESULT_NG );
                                    // 来店マイルエラー
                                    if ( nVisitPoint < 0 )
                                    {
                                        errorCode = HapiTouchErrorMessage.ERR_10504;
                                    }
                                }
                                // ホテル利用マイル加算(一時保存テーブルに保存する)
                                nAmountPoint = uppt.setAmountPointUpdate( hc.getHotelCi().getUserId(), AMOUNT_POINT, nowPoint, Integer.parseInt( paramPrice ), Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                                if ( nAmountPoint >= 0 )
                                {
                                    gxTouch.setAmountResult( RESULT_OK );
                                    // マイナスの履歴追加
                                    hc.setMinusHistory( hc.getHotelCi(), Integer.parseInt( paramEmployeeCode ) );
                                    // 再度最新のデータを取得する
                                    ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
                                }
                                else
                                {
                                    gxTouch.setAmountResult( RESULT_NG );
                                    // 付与マイルエラー
                                    errorCode = HapiTouchErrorMessage.ERR_10511;
                                }
                            }
                        }
                        else
                        {
                            if ( Integer.parseInt( paramFix ) == 0 )
                            {
                                dmp = new DataMasterPoint();
                                // ポイントが入っていたらマイル使用
                                if ( Integer.parseInt( paramPoint ) > 0 && Integer.parseInt( paramPoint ) != hc.getHotelCi().getUsePoint() )
                                {
                                    retUse = true;
                                }
                                // 料金が入っていたらマイル付与を行う
                                if ( Integer.parseInt( paramPrice ) > 0 )
                                {
                                    nAmountPoint = (int)(hc.getHotelCi().getAmountRate() * Integer.parseInt( paramPrice ) / 100);
                                    // クラスで持っている料金とリクエストされた料金が違ったらマイナスの履歴を追加
                                    if ( nAmountPoint > 0 && nAmountPoint != hc.getHotelCi().getAmount() )
                                    {
                                        // マイナスの履歴追加
                                        hc.setMinusHistory( hc.getHotelCi(), Integer.parseInt( paramEmployeeCode ) );
                                    }
                                    // 来店マイルが追加可能かどうかをチェックする（一時の方）
                                    if ( uppt.getMasterPointExtNum( hc.getHotelCi().getUserId(), POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) ) != false )
                                    {
                                        // 来店マイルが追加可能かどうかをチェックする
                                        if ( upp.getMasterPointExtNum( hc.getHotelCi().getUserId(), POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) ) != false )
                                        {
                                            nVisitPoint = 1;
                                        }
                                    }
                                }
                            }
                        }

                        // 現在のポイントをセット
                        nowPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                        gxTouch.setPoint( nowPoint );
                        if ( ret != false )
                        {
                            hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                            hc.getHotelCi().setUserId( hc.getHotelCi().getUserId() );
                            hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            if ( paramRoomNo.equals( "" ) == false && paramRoomNo.equals( hc.getHotelCi().getRoomNo() ) == false )
                            {
                                hc.getHotelCi().setRoomNo( paramRoomNo );
                            }
                            if ( Integer.parseInt( paramSlipNo ) > 0 )
                            {
                                hc.getHotelCi().setSlipNo( Integer.parseInt( paramSlipNo ) );
                            }
                            if ( nAmountPoint >= 0 )
                            {
                                hc.getHotelCi().setCiStatus( 1 );
                                hc.getHotelCi().setAmount( Integer.parseInt( paramPrice ) );
                                hc.getHotelCi().setAddPoint( nAmountPoint );
                                hc.getHotelCi().setAddDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                hc.getHotelCi().setAddTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                hc.getHotelCi().setAddHotenaviId( hotenaviId );
                                hc.getHotelCi().setAddEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            if ( retUse != false )
                            {
                                // HotelCiを登録する
                                hc.getHotelCi().setUsePoint( Integer.parseInt( paramPoint ) );
                                hc.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                hc.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                hc.getHotelCi().setUseHotenaviId( hotenaviId );
                                hc.getHotelCi().setUseEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            if ( Integer.parseInt( paramAllUse ) > 0 )
                            {
                                hc.getHotelCi().setCiStatus( Integer.parseInt( paramAllUse ) );
                            }
                            ret = hc.getHotelCi().insertData();
                            Logging.info( "[ActionHapiTouch correctCi]ret2:" + ret );
                            if ( ret != false )
                            {
                                // 料金が登録されており、金額の変更がなかったら更新を行う。
                                if ( (paramRoomNo.equals( "" ) == false || Integer.parseInt( paramSlipNo ) > 0) && (nAmountPoint == 0 && hc.getHotelCi().getAmount() > 0) )
                                {
                                    // ホテル利用マイル更新
                                    nAmountPoint = uppt.setAmountPointUpdate( hc.getHotelCi().getUserId(), AMOUNT_POINT, nowPoint, hc.getHotelCi().getAmount(), Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                                    // ホテル利用マイル更新エラー
                                    if ( nAmountPoint < 0 )
                                    {
                                        errorCode = HapiTouchErrorMessage.ERR_10508;
                                    }
                                }
                                // 仮マイルの使用を戻すためにポイントのリクエストがあったらretUseをtrueにする
                                if ( Integer.parseInt( paramPoint ) > 0 )
                                {
                                    retUse = true;
                                }
                                /** ハピホテタッチ端末用に追加 **/
                                hc.registTouchCi( hc.getHotelCi(), retUse );
                                /** ハピホテタッチ端末用に追加 **/
                                if ( Integer.parseInt( paramFix ) == 1 )
                                {
                                    if ( !hc.getHotelCi().getRsvNo().equals( "" ) ) // 予約部屋在庫を開放する
                                    {
                                        LogicOwnerRsvCheckIn logic = new LogicOwnerRsvCheckIn();
                                        if ( logic.releaseRoomRemaindar( hotelId, paramRoomNo, hc.getHotelCi().getRsvNo(), hc.getHotelCi().getCiDate() ) )
                                        {
                                            if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                                            {
                                                RsvList rl = new RsvList();
                                                // 要確認
                                                if ( rl.getData( hotelId, hc.getHotelCi().getCiDate(), hc.getHotelCi().getCiDate(), 0, hc.getHotelCi().getRsvNo(), 1 ) != false )
                                                {
                                                    rl.sendToHost( hotelId );
                                                }
                                            }
                                        }
                                        else
                                        {
                                            DataApErrorHistory daeh = new DataApErrorHistory();
                                            daeh.setErrorCode( HapiTouchErrorMessage.ERR_10615 );
                                            daeh.setErrorSub( 0 );
                                            daeh.setHotelName( hotelName );
                                            daeh.setHotenaviId( hotenaviId );
                                            daeh.setRoomName( paramRoomNo );
                                            daeh.setId( hotelId );
                                            daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                            daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                            daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                                            daeh.setUserId( userId );
                                            daeh.insertData();
                                        }
                                    }
                                }
                                gxTouch.setResult( RESULT_OK );
                                gxTouch.setCiStatus( hc.getHotelCi().getCiStatus() );
                            }
                            else
                            {
                                gxTouch.setResult( RESULT_NG );
                                gxTouch.setCiStatus( 0 );
                                // チェックインコード追加エラー
                                errorCode = HapiTouchErrorMessage.ERR_10603;
                            }
                        }
                        else
                        {
                            gxTouch.setResult( RESULT_NG );
                            gxTouch.setCiStatus( 0 );
                        }
                    }
                }
                else
                {
                    if ( hc.getHotelCi().getFixFlag() == 1 )
                    {
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setCiStatus( hc.getHotelCi().getCiStatus() );
                    }
                    gxTouch.setResult( RESULT_NO );
                    gxTouch.setRegistUrl( BASE_URL + PAGE_URL + "?method=" + METHOD_REGIST + "&idm=" + paramIdm );
                    // チェックインデータ取得エラー
                    errorCode = HapiTouchErrorMessage.ERR_10601;
                }
            }
            if ( errorCode == 0 )
            {
                Logging.info( "STEP13", "userId:" + userId );
                // バックオフィスデータ更新
                if ( bko.updateBkoData( userId, hotelId, hc.getHotelCi() ) == false )
                {
                    // バックオフィスマイル更新エラー
                    errorCode = HapiTouchErrorMessage.ERR_10510;
                }
            }
            if ( errorCode == 0 && ReserveCommon.isSyncReserve( hotelId ) )
            {
                if ( hc.getHotelCi().getRsvNo().equals( "" ) == false && !ciRoomNo.equals( paramRoomNo ) )
                {
                    // 部屋替えを行う
                    HapiTouchRsv.rsvRoomChange( hotelId, hc.getHotelCi().getRsvNo(), paramRoomNo );
                }
            }
            if ( errorCode != 0 )
            // エラーが発生していた場合にはエラー履歴に書き込む
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 1 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( paramRoomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
            }
            // XMLの出力
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch modifyCi]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch modifyCi]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * ステータス確認
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @param response レスポンス
     **/
    public void statusCi(int hotelId, int seq, HttpServletResponse response)
    {
        int status;
        HotelCi hc;
        GenerateXmlHapiTouchModifyCi gxTouch;
        ServletOutputStream stream = null;
        gxTouch = new GenerateXmlHapiTouchModifyCi();
        hc = new HotelCi();
        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();
            status = hc.getCiStatus( hotelId, seq, 0 );
            gxTouch.setCiStatus( status );
            // XMLの出力
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch useData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch useData]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * ステータス確認
     * 
     * @param hotelId ホテルID
     * @param response レスポンス
     **/
    public void getConfig(int hotelId, boolean roomDispFlag, HttpServletResponse response)
    {
        int seq = 0;
        boolean retRoom = false;
        boolean retRoomMore = false;
        boolean retHappieNow = false;
        boolean retHappieNext = false;
        HotelHappie hh;
        HotelRoom hr;
        HotelRoomMore hrm;
        DataRsvReserveBasic drrb;
        DataHotelBasic dhb;
        DataMasterPoint dmpCome;
        DataMasterPoint dmpAmount;
        DataMasterPoint dmpRsv;
        GenerateXmlHapiTouchGetConfig gxTouch;
        GenerateXmlHapiTouchGetConfigRoom gxRoom;
        GenerateXmlHapiTouchGetConfigPoint gxPoint;
        ServletOutputStream stream = null;
        hh = new HotelHappie();
        hr = new HotelRoom();
        hrm = new HotelRoomMore();
        drrb = new DataRsvReserveBasic();
        dhb = new DataHotelBasic();
        dmpCome = new DataMasterPoint();
        dmpAmount = new DataMasterPoint();
        dmpRsv = new DataMasterPoint();
        gxTouch = new GenerateXmlHapiTouchGetConfig();
        gxRoom = new GenerateXmlHapiTouchGetConfigRoom();
        gxPoint = new GenerateXmlHapiTouchGetConfigPoint();
        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();
            if ( dhb.getData( hotelId ) != false )
            {
                gxTouch.setHotelName( dhb.getName() );
            }
            // 部屋情報を取得する場合のみ取得
            if ( roomDispFlag != false )
            {
                // 部屋情報を取得
                retRoomMore = hrm.getRoomData( hotelId, 0 );
                // 部屋情報がない場合はhh_hotel_roomから取得
                if ( retRoomMore == false && hrm.getHotelRoomCount() == 0 )
                {
                    retRoom = hr.getRoomData( hotelId, 0 );
                }
                // 部屋情報をセット
                if ( retRoomMore != false && hrm.getHotelRoomCount() > 0 )
                {
                    // ホテルの情報をセット
                    for( int i = 0 ; i < hrm.getHotelRoomCount() ; i++ )
                    {
                        GenerateXmlHapiTouchGetConfigRoomNo gxRoomNo = new GenerateXmlHapiTouchGetConfigRoomNo();
                        gxRoomNo.setRoomNo( hrm.getHotelRoom()[i].getRoomNameHost() );
                        gxRoom.setRoomNo( gxRoomNo );
                    }
                    gxTouch.setRoom( gxRoom );
                }
                else if ( retRoom != false && hr.getHotelRoomCount() > 0 )
                {
                    // ホテルの情報をセット
                    for( int i = 0 ; i < hr.getHotelRoomCount() ; i++ )
                    {
                        GenerateXmlHapiTouchGetConfigRoomNo gxRoomNo = new GenerateXmlHapiTouchGetConfigRoomNo();
                        gxRoomNo.setRoomNo( hr.getHotelRoom()[i].getRoomName() );
                        gxRoom.setRoomNo( gxRoomNo );
                    }
                    gxTouch.setRoom( gxRoom );
                }
                else
                {
                    errorCode = 0;
                }
            }
            dmpCome.getData( COME_POINT );
            dmpAmount.getData( AMOUNT_POINT );
            dmpRsv.getData( RSV_POINT );
            // デフォルトの有料会員向けポイントをセット
            gxPoint.setDefault( "" );
            gxPoint.setVisitRate( dmpCome.getAddPoint() );
            gxPoint.setAmountRate( dmpAmount.getAddPoint() );
            gxPoint.setRsvRate( dmpRsv.getAddPoint() );
            // デフォルトの無料会員向けポイントをセット
            gxPoint.setVisitRateFree( dmpCome.getAddPoint() * dmpCome.getFreeMultiple() );
            gxPoint.setAmountRateFree( dmpAmount.getAddPoint() * dmpAmount.getFreeMultiple() );
            gxPoint.setRsvRateFree( dmpRsv.getAddPoint() * dmpRsv.getFreeMultiple() );
            // マイルの情報を取得
            retHappieNow = hh.getData( hotelId );
            if ( retHappieNow != false )
            {
                if ( hh.getHotelCouponCount() > 0 )
                {
                    seq = hh.getHotelHappie().getSeq();
                    // データが取得できたらデータをセット
                    GenerateXmlHapiTouchGetConfigPointData dataList = new GenerateXmlHapiTouchGetConfigPointData();
                    dataList.setId( hh.getHotelHappie().getSeq() );
                    dataList.setStartDate( hh.getHotelHappie().getStartDate() );
                    dataList.setEndDate( hh.getHotelHappie().getEndDate() );
                    // 有料会員のレートをセット
                    dataList.setVisitRate( dmpCome.getAddPoint() * hh.getHotelHappie().getComePointMultiple() );
                    dataList.setAmountRate( dmpAmount.getAddPoint() * hh.getHotelHappie().getUsePointMultiple() );
                    dataList.setRsvRate( dmpRsv.getAddPoint() );
                    // 無料会員のレートをセット
                    dataList.setVisitRateFree( dmpCome.getAddPoint() * dmpCome.getFreeMultiple() * hh.getHotelHappie().getComePointMultiple() );
                    dataList.setAmountRateFree( dmpAmount.getAddPoint() * dmpAmount.getFreeMultiple() * hh.getHotelHappie().getUsePointMultiple() );
                    dataList.setRsvRateFree( dmpRsv.getAddPoint() * dmpRsv.getFreeMultiple() );
                    gxPoint.setData( dataList );
                    retHappieNext = hh.getDataMulti( hotelId, seq );
                    if ( retHappieNext != false )
                    {
                        for( int i = 0 ; i < hh.getHotelHappieMulti().length ; i++ )
                        {
                            GenerateXmlHapiTouchGetConfigPointData dataListNext = new GenerateXmlHapiTouchGetConfigPointData();
                            dataListNext.setId( hh.getHotelHappieMulti()[i].getSeq() );
                            dataListNext.setStartDate( hh.getHotelHappieMulti()[i].getStartDate() );
                            dataListNext.setEndDate( hh.getHotelHappieMulti()[i].getEndDate() );
                            // 有料会員のレートをセット
                            dataListNext.setVisitRate( dmpCome.getAddPoint() * hh.getHotelHappieMulti()[i].getComePointMultiple() );
                            dataListNext.setAmountRate( dmpAmount.getAddPoint() * hh.getHotelHappieMulti()[i].getUsePointMultiple() );
                            dataListNext.setRsvRate( dmpRsv.getAddPoint() );
                            // 無料会員のレートをセット
                            dataListNext.setVisitRateFree( dmpCome.getAddPoint() * dmpCome.getFreeMultiple() * hh.getHotelHappieMulti()[i].getComePointMultiple() );
                            dataListNext.setAmountRateFree( dmpAmount.getAddPoint() * dmpAmount.getFreeMultiple() * hh.getHotelHappieMulti()[i].getUsePointMultiple() );
                            dataListNext.setRsvRateFree( dmpRsv.getAddPoint() * dmpRsv.getFreeMultiple() );
                            // 近い情報をセットする
                            gxPoint.setData( dataListNext );
                        }
                    }
                }
                else
                {
                    // マイル倍率取得エラー
                    errorCode = 0;
                }
            }
            else
            {
                // マイル倍率取得エラー
                errorCode = 0;
            }
            // 予約可能かどうかを判断する
            drrb.getData( hotelId );
            if ( drrb.getID() > 0 )
            {
                if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 1 )
                {
                    gxTouch.setIsValidRsv( 1 );
                    gxTouch.setDeadlineTime( drrb.getDeadline_time() );
                }
            }
            if ( dhb.getRank() >= 2 )
            {
                // ホテルのクーポンを取得する
                HotelCoupon hc = new HotelCoupon();
                hc.getCouponData( hotelId );
                if ( hc.getHotelCouponCount() > 0 )
                {
                    // gxTouch.setIsCoupon( 1 );
                    gxTouch.setIsCoupon( 0 ); // タッチPCにクーポンの表示をしない
                }
                else
                {
                    gxTouch.setIsCoupon( 0 );
                }
            }
            else
            {
                gxTouch.setIsCoupon( 0 );
            }
            // マイルの情報をセット
            gxPoint.setNowId( seq );
            gxTouch.setPoint( gxPoint );
            // IPアドレスをセット（getConfigにセットするのはスーパーフロンティア以外）
            gxTouch.setFrontIp( HotelIp.getFrontIpForUseMile( hotelId ) );
            // XMLの出力
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            Logging.info( xmlOut );
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getConfig]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch getConfig]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * チェックインデータ同期処理
     * 
     * @param id ホテルID
     * @param request リクエスト
     * @param response レスポンス
     * @return
     */
    public void syncCi(int id, HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = false;
        int i;
        String paramStartDate;
        String paramEndDate;
        String paramStartTime;
        String paramEndTime;
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchSyncCi gxSyncCi;
        HotelCi hc;
        DataApTouchCi datc;
        DataApUuidUser dauu;
        UserPointPay upp = null;
        TouchErrorHistory teh;
        teh = new TouchErrorHistory();
        hc = new HotelCi();
        paramStartDate = request.getParameter( "startDate" );
        paramEndDate = request.getParameter( "endDate" );
        paramStartTime = request.getParameter( "startTime" );
        paramEndTime = request.getParameter( "endTime" );
        gxSyncCi = new GenerateXmlHapiTouchSyncCi();
        datc = new DataApTouchCi();
        dauu = new DataApUuidUser();
        // 開始日のチェック
        if ( (paramStartDate == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartDate ) == false) )
        {
            paramStartDate = "0";
        }
        // 終了日のチェック
        if ( (paramEndDate == null) || (paramEndDate.equals( "" ) != false) || (CheckString.numCheck( paramEndDate ) == false) )
        {
            paramEndDate = "0";
        }
        // 開始時刻のチェック
        if ( (paramStartTime == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartTime ) == false) )
        {
            paramStartTime = "0";
        }
        // 終了時刻のチェック
        if ( (paramEndTime == null) || (paramEndTime.equals( "" ) != false) || (CheckString.numCheck( paramEndTime ) == false) )
        {
            paramEndTime = "0";
        }
        try
        {
            stream = response.getOutputStream();
            // ホテルID、開始日、終了日からチェックインデータを取得
            ret = hc.getData( id, Integer.parseInt( paramStartDate ), Integer.parseInt( paramStartTime ), Integer.parseInt( paramEndDate ), Integer.parseInt( paramEndTime ) );
            if ( ret != false )
            {
                if ( hc.gethotelCiCount() > 0 )
                {
                    for( i = 0 ; i < hc.gethotelCiCount() ; i++ )
                    {
                        if ( upp != null )
                        {
                            upp = null;
                        }
                        upp = new UserPointPay();
                        GenerateXmlHapiTouchSyncCiData gxSyncCiData = new GenerateXmlHapiTouchSyncCiData();
                        // 取得したデータをxmlにセット
                        gxSyncCiData.setCiCode( hc.getHotelCiMulti()[i].getSeq() );
                        gxSyncCiData.setCiDate( hc.getHotelCiMulti()[i].getCiDate() );
                        gxSyncCiData.setCiTime( hc.getHotelCiMulti()[i].getCiTime() );
                        gxSyncCiData.setUserId( Integer.toString( hc.getHotelCiMulti()[i].getUserSeq() ) );
                        gxSyncCiData.setPoint( upp.getNowPoint( hc.getHotelCiMulti()[i] ) );
                        gxSyncCiData.setAmountRate( hc.getHotelCiMulti()[i].getAmountRate() );
                        gxSyncCiData.setRoomNo( hc.getHotelCiMulti()[i].getRoomNo() );
                        gxSyncCiData.setUsePoint( hc.getHotelCiMulti()[i].getUsePoint() );
                        // 料金が0円の場合もあるため、ステータスで確認する
                        if ( hc.getHotelCiMulti()[i].getCiStatus() > 0 )
                        {
                            gxSyncCiData.setAmount( hc.getHotelCiMulti()[i].getAmount() );
                        }
                        // 伝票番号が0より大きい場合にセット
                        if ( hc.getHotelCiMulti()[i].getSlipNo() > 0 )
                        {
                            gxSyncCiData.setSlipNo( hc.getHotelCiMulti()[i].getSlipNo() );
                        }
                        int ciStatus = hc.getHotelCiMulti()[i].getCiStatus();
                        // ハピホテユーザーでない場合でかつ連動物件の場合は無効扱い表示
                        gxSyncCiData.setCiStatus( ciStatus );
                        gxSyncCiData.setUserType( hc.getHotelCiMulti()[i].getUserType() );
                        // CiStatus = 3だったら削除対象とする
                        if ( ciStatus == CI_STATUS_DEL )
                        {
                            if ( teh.getErrorData( id, hc.getHotelCiMulti()[i].getSeq() ) )
                            {
                                gxSyncCiData.setDelFlag( SYNC_CI_ADD_FLAG );
                            }
                            else
                            {
                                gxSyncCiData.setDelFlag( SYNC_CI_DEL_FLAG );
                            }
                        }
                        else
                        {
                            gxSyncCiData.setDelFlag( SYNC_CI_ADD_FLAG );
                        }
                        gxSyncCiData.setRsvNo( hc.getHotelCiMulti()[i].getRsvNo() );
                        if ( datc.getData( id, hc.getHotelCiMulti()[i].getSeq() ) != false )
                        {
                            gxSyncCiData.setUseTempFlag( datc.getUseTempFlag() );
                        }
                        else
                        {
                            // データが取得できなかったら0をセット
                            gxSyncCiData.setUseTempFlag( 0 );
                        }
                        if ( dauu.getAppData( hc.getHotelCiMulti()[i].getUserId() ) )
                        {
                            gxSyncCiData.setAppStatus( dauu.getAppStatus() );
                        }
                        else
                        {
                            gxSyncCiData.setAppStatus( 1 );
                        }
                        // 予約番号が入っていて、外部予約でかつ外部ユーザの場合はLIJと表示する
                        if ( !hc.getHotelCiMulti()[i].getRsvNo().equals( "" ) )
                        {
                            if ( hc.getHotelCiMulti()[i].getExtUserFlag() == 1 )
                            {
                                if ( DataRsvReserve.getExtFlag( id, hc.getHotelCiMulti()[i].getRsvNo() ) == ReserveCommon.EXT_LVJ )
                                {
                                    gxSyncCiData.setUserId( "LIJ" );
                                    // gxSyncCiData.setUserType( 2 );
                                }
                            }
                        }
                        gxSyncCi.setCiData( gxSyncCiData );
                    }
                }
            }
            else
            {
                GenerateXmlHapiTouchSyncCiData gxSyncCiData = new GenerateXmlHapiTouchSyncCiData();
                gxSyncCi.setCiData( gxSyncCiData );
            }
            // XMLの出力
            String xmlOut = gxSyncCi.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            Logging.info( xmlOut );
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch syncCi]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch syncCi]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * チェックインデータ同期処理
     * 
     * @param id ホテルID
     * @param request リクエスト
     * @param response レスポンス
     * @return
     */
    public void syncCiBySeq(int id, HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = false;
        String paramSeq;
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchSyncCiBySeq gxSyncCiBySeq;
        DataHotelCi dhc;
        DataApTouchCi datc;
        UserPointPay upp = null;
        TouchErrorHistory teh;
        dhc = new DataHotelCi();
        paramSeq = request.getParameter( "seq" );
        gxSyncCiBySeq = new GenerateXmlHapiTouchSyncCiBySeq();
        datc = new DataApTouchCi();
        teh = new TouchErrorHistory();
        DataApUuidUser dauu;
        dauu = new DataApUuidUser();
        // 開始日のチェック
        if ( (paramSeq == null) || (paramSeq.equals( "" ) != false) || (CheckString.numCheck( paramSeq ) == false) )
        {
            paramSeq = "0";
        }
        try
        {
            stream = response.getOutputStream();
            // ホテルID、開始日、終了日からチェックインデータを取得
            ret = dhc.getData( id, Integer.parseInt( paramSeq ) );
            if ( ret != false )
            {
                upp = new UserPointPay();
                // 取得したデータをxmlにセット
                gxSyncCiBySeq.setCiCode( dhc.getSeq() );
                gxSyncCiBySeq.setCiDate( dhc.getCiDate() );
                gxSyncCiBySeq.setCiTime( dhc.getCiTime() );
                gxSyncCiBySeq.setUserId( Integer.toString( dhc.getUserSeq() ) );
                gxSyncCiBySeq.setAmountRate( dhc.getAmountRate() );
                gxSyncCiBySeq.setRoomNo( dhc.getRoomNo() );
                gxSyncCiBySeq.setUsePoint( dhc.getUsePoint() );
                gxSyncCiBySeq.setPoint( upp.getNowPoint( dhc ) );
                // 料金が0円の場合もあるため、ステータスで確認する
                if ( dhc.getCiStatus() > 0 )
                {
                    gxSyncCiBySeq.setAmount( dhc.getAmount() );
                }
                // 伝票番号が0より大きい場合にセット
                if ( dhc.getSlipNo() > 0 )
                {
                    gxSyncCiBySeq.setSlipNo( dhc.getSlipNo() );
                }
                // ハピホテユーザーでない場合は無効扱い表示
                int ciStatus = dhc.getCiStatus();
                gxSyncCiBySeq.setCiStatus( ciStatus );
                gxSyncCiBySeq.setUserType( dhc.getUserType() );
                // CiStatus = 3だったら削除対象とする
                if ( ciStatus == CI_STATUS_DEL )
                {
                    if ( teh.getErrorData( id, dhc.getSeq() ) )
                    {
                        gxSyncCiBySeq.setDelFlag( SYNC_CI_ADD_FLAG );
                    }
                    else
                    {
                        gxSyncCiBySeq.setDelFlag( SYNC_CI_DEL_FLAG );
                    }
                }
                else
                {
                    gxSyncCiBySeq.setDelFlag( SYNC_CI_ADD_FLAG );
                }
                gxSyncCiBySeq.setRsvNo( dhc.getRsvNo() );
                if ( datc.getData( id, dhc.getSeq() ) != false )
                {
                    gxSyncCiBySeq.setUseTempFlag( datc.getUseTempFlag() );
                }
                else
                {
                    // データが取得できなかったら0をセット
                    gxSyncCiBySeq.setUseTempFlag( 0 );
                }
                if ( dauu.getAppData( dhc.getUserId() ) )
                {
                    gxSyncCiBySeq.setAppStatus( dauu.getAppStatus() );
                }
                else
                {
                    gxSyncCiBySeq.setAppStatus( 1 );
                }
                // 予約番号が入っていて、外部予約でかつ外部ユーザの場合はLIJと表示する
                if ( !dhc.getRsvNo().equals( "" ) )
                {
                    if ( dhc.getExtUserFlag() == 1 )
                    {
                        if ( DataRsvReserve.getExtFlag( id, dhc.getRsvNo() ) == ReserveCommon.EXT_LVJ )
                        {
                            gxSyncCiBySeq.setUserId( "LIJ" );
                            // gxSyncCiBySeq.setUserType( 2 );
                        }
                    }
                }
            }
            // XMLの出力
            String xmlOut = gxSyncCiBySeq.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            Logging.info( xmlOut );
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch syncCi]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch syncCi]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * 来店キャンセル
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void visitCancel(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret;
        String paramEmployeeCode;
        String paramSeq;
        String paramFix;
        HotelCi hc;
        GenerateXmlHapiTouchVisitCancel gxTouch;
        ServletOutputStream stream = null;
        UserPointPayTemp uppt;
        UserPointPay upp;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        HapiTouchBko bko = new HapiTouchBko();
        DataHotelBasic dhb;
        gxTouch = new GenerateXmlHapiTouchVisitCancel();
        dhb = new DataHotelBasic();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        hc = new HotelCi();
        ret = false;
        paramEmployeeCode = request.getParameter( "employeeCode" );
        paramSeq = request.getParameter( "seq" );
        paramFix = request.getParameter( "fix" );
        String hotelName = "";
        String hotenaviId = "";
        String rsvNo = "";
        String roomNo = "";
        String userId = "";
        // レスポンスをセット
        try
        {
            if ( dhb.getData( hotelId ) != false )
            {
                hotelName = dhb.getName();
                hotenaviId = dhb.getHotenaviId();
            }
            // パラメータチェック
            if ( (paramEmployeeCode == null) || (paramEmployeeCode.compareTo( "" ) == 0) || CheckString.numCheck( paramEmployeeCode ) == false )
            {
                paramEmployeeCode = "0";
            }
            if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( (paramFix == null) || (paramFix.compareTo( "" ) == 0) || CheckString.numCheck( paramFix ) == false )
            {
                paramFix = "0";
            }
            stream = response.getOutputStream();
            if ( Integer.parseInt( paramSeq ) > 0 )
            {
                // ホテルチェックインデータを取得する
                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
            }
            // xml出力クラスにノードをセット
            if ( ret != false && hc.getHotelCi().getCiStatus() == 0 )
            {
                rsvNo = hc.getHotelCi().getRsvNo();
                roomNo = hc.getHotelCi().getRoomNo();
                userId = hc.getHotelCi().getUserId();
                hc = htRsvSub.visitCancel( hotelId, hc, 1 );
                if ( hc.getErrorMsg() > 0 )
                {
                    ret = false;
                }
                if ( ret != false )
                {
                    gxTouch.setResult( RESULT_OK );
                    /** ハピホテタッチ端末用に追加 **/
                    hc.registTouchCi( hc.getHotelCi(), false );
                    /** ハピホテタッチ端末用に追加 **/
                }
                else
                {
                    gxTouch.setResult( RESULT_NG );
                    // チェックインデータ追加エラー
                    errorCode = HapiTouchErrorMessage.ERR_10802;
                }
                gxTouch.setCiStatus( hc.getHotelCi().getCiStatus() );
                gxTouch.setCiCode( hc.getHotelCi().getSeq() );
                gxTouch.setCiDate( hc.getHotelCi().getCiDate() );
                gxTouch.setCiTime( hc.getHotelCi().getCiTime() );
                gxTouch.setUserId( hc.getHotelCi().getUserSeq() );
                gxTouch.setAmountRate( hc.getHotelCi().getAmountRate() );
                gxTouch.setRoomNo( hc.getHotelCi().getRoomNo() );
                gxTouch.setUsePoint( hc.getHotelCi().getUsePoint() );
                gxTouch.setAmount( hc.getHotelCi().getAmount() );
                gxTouch.setSlipNo( hc.getHotelCi().getSlipNo() );
                gxTouch.setRsvNo( hc.getHotelCi().getRsvNo() );
                gxTouch.setUserType( hc.getHotelCi().getUserType() );
                // 予約番号が入っている場合
                if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                {
                    // フォームにセット
                    frm.setSelHotelId( hotelId );
                    frm.setRsvNo( hc.getHotelCi().getRsvNo() );
                    // 予約データ抽出
                    logic.setFrm( frm );
                    // 実績データ取得
                    logic.getData( 2 );
                    frm.setMode( "" ); // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                    frm.setErrMsg( "" );
                    frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
                    // 部屋番号が登録されており、来店のステータスのデータで、チェックインデータが作成されている場合
                    if ( frm.getSeq() > 0 && frm.getStatus() == ReserveCommon.RSV_STATUS_ZUMI && ret != false )
                    {
                        // 予約登録したユーザーのメールアドレス取得
                        UserBasicInfo ubi = new UserBasicInfo();
                        ubi.getUserBasic( frm.getUserId() );
                        frm.setLoginUserId( frm.getUserId() );
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                        frm.setLoginUserTel( ubi.getUserInfo().getTel1() );
                        frm.setMail( ubi.getUserInfo().getMailAddr() );
                        DataRsvPlan dataPlan = new DataRsvPlan();
                        int offerKind;
                        // ホテルの提供区分取得
                        dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                        offerKind = dataPlan.getOfferKind();
                        frm.setOfferKind( offerKind );
                        // ▼来店のキャンセル
                        frm = htRsvSub.execUndoFix( frm, frm.getStatus() );
                        // エラーあり時
                        if ( frm.getErrMsg().trim().length() != 0 )
                        {
                            errorCode = HapiTouchErrorMessage.ERR_20904;
                        }
                        else
                        {
                            if ( hc.getHotelCi().getCiStatus() <= 2 && frm.getExtFlag() == ReserveCommon.EXT_HAPIHOTE )
                            {
                                errorCode = hc.getErrorMsg();
                                ret = bko.cancelBkoData( hc.getHotelCi().getUserId(), hotelId, POINT_KIND_YOYAKU, hc.getHotelCi() );
                                if ( ret == false )
                                {
                                    // バックオフィスデータ取消処理失敗
                                    errorCode = HapiTouchErrorMessage.ERR_20912;
                                }
                                ret = uppt.cancelRsvPoint( hc.getHotelCi(), POINT_KIND_YOYAKU, 0 );
                                if ( ret == false )
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_20913;
                                }
                                // upptのマイル取り消しがうまくいったら、uppも取り消す
                                if ( ret != false )
                                {
                                    ret = upp.cancelRsvPoint( hc.getHotelCi(), POINT_KIND_YOYAKU, 0 );
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                errorCode = HapiTouchErrorMessage.ERR_10801;
                gxTouch.setResult( RESULT_NO );
                gxTouch.setErrorCode( errorCode );
            }
            if ( errorCode == 0 )
            {
                // IPアドレスが登録されている場合は、ホスト連動物件。予約データの最新状態をホストに伝える
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) && hc.getHotelCi().getRsvNo().equals( "" ) == false )
                {
                    RsvList rl = new RsvList();
                    if ( rl.getData( hotelId, 0, 0, 0, hc.getHotelCi().getRsvNo(), 0 ) != false )
                    {
                        rl.sendToHost( hotelId );
                    }
                }
            }
            if ( errorCode != 0 )
            // エラーが発生していた場合にはエラー履歴に書き込む
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 1 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setReserveNo( rsvNo );
                daeh.setUserId( userId );
                daeh.insertData();
            }
            // XMLの出力
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch visitCancel]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch visitCancel]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * ポイントキャンセル
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void pointCancel(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        int nowPoint;
        boolean ret;
        boolean retUse;
        boolean retData;
        String hotelName = "";
        String hotenaviId = "";
        String rsvNo = "";
        String roomNo = "";
        String userId = "";
        String paramEmployeeCode;
        String paramSeq;
        DataUserFelica duf;
        DataHotelBasic dhb;
        HotelCi hc;
        GenerateXmlHapiTouchPointCancel gxTouch;
        ServletOutputStream stream = null;
        UserPointPayTemp uppt;
        UserPointPay upp;
        HapiTouchBko bko = new HapiTouchBko();
        duf = new DataUserFelica();
        gxTouch = new GenerateXmlHapiTouchPointCancel();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        dhb = new DataHotelBasic();
        hc = new HotelCi();
        nowPoint = 0;
        ret = false;
        retUse = false;
        retData = false;
        paramEmployeeCode = request.getParameter( "employeeCode" );
        paramSeq = request.getParameter( "seq" );
        // レスポンスをセット
        try
        {
            // パラメータチェック
            if ( (paramEmployeeCode == null) || (paramEmployeeCode.compareTo( "" ) == 0) || CheckString.numCheck( paramEmployeeCode ) == false )
            {
                paramEmployeeCode = "0";
            }
            if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            stream = response.getOutputStream();
            if ( Integer.parseInt( paramSeq ) > 0 )
            {
                // ホテルチェックインデータを取得する
                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
            }
            // xml出力クラスにノードをセット

            StringBuffer requestUrl = request.getRequestURL();
            String requestUrlStr = requestUrl.toString();
            if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) == -1 && request.getParameter( "code" ) != null && request.getParameter( "uuid" ) != null )
            {
                // 連動物件でタッチPCからのマイル使用キャンセルはNGとする
                gxTouch.setResult( RESULT_NG );
                errorCode = HapiTouchErrorMessage.ERR_10907;
                gxTouch.setErrorCode( HapiTouchErrorMessage.ERR_10907 );
                hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                hc.getHotelCi().updateData( hotelId, Integer.parseInt( paramSeq ), hc.getHotelCi().getSubSeq() );
            }
            else if ( ret != false )
            {
                rsvNo = hc.getHotelCi().getRsvNo();
                roomNo = hc.getHotelCi().getRoomNo();
                userId = hc.getHotelCi().getUserId();
                if ( hc.getHotelCi().getCiStatus() == 0 )
                {
                    duf.getData( hc.getHotelCi().getUserId() );
                    // ホテナビIDを取得する
                    dhb.getData( hotelId );
                    if ( dhb.getId() > 0 )
                    {
                        hotenaviId = dhb.getHotenaviId();
                        hotelName = dhb.getName();
                    }
                    nowPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                    // ポイントが入っていたらマイルを取り消す
                    if ( hc.getHotelCi().getUsePoint() > 0 )
                    {
                        // 既に使用マイルデータがあるかどうかを確認する
                        retData = uppt.getUserPointHistory( hc.getHotelCi().getUserId(), hc.getHotelCi().getId(), POINT_KIND_WARIBIKI, hc.getHotelCi().getUserSeq(), hc.getHotelCi().getVisitSeq() );
                        if ( retData != false )
                        {
                            // データがあるため、使用マイルを更新
                            retUse = uppt.setUsePointUpdate( hc.getHotelCi().getUserId(), USE_POINT, nowPoint, 0, Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                            if ( retUse != false )
                            {
                                // hh_user_point_payに使用マイルの更新
                                retUse = upp.setUsePointUpdate( hc.getHotelCi().getUserId(), USE_POINT, nowPoint, 0, Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                                if ( retUse == false )
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_10903;
                                }
                            }
                            else
                            {
                                errorCode = HapiTouchErrorMessage.ERR_10903;
                            }
                        }
                        else
                        {
                            // errorCode をセット
                            errorCode = HapiTouchErrorMessage.ERR_10904;
                        }
                        if ( errorCode > 0 )
                        {
                            retData = false;
                        }
                        else
                        {
                            retData = true;
                        }
                    }
                    else
                    {
                        retData = true;
                    }
                    if ( retData != false )
                    {
                        hc.getHotelCi().setUserId( hc.getHotelCi().getUserId() );
                        hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        if ( retUse != false )
                        {
                            // HotelCiを登録する
                            // ポイントの符号を反転させて登録
                            if ( hc.getHotelCi().getUsePoint() >= 0 )
                            {
                                hc.getHotelCi().setUsePoint( Math.abs( hc.getHotelCi().getUsePoint() ) * -1 );
                            }
                            else
                            {
                                hc.getHotelCi().setUsePoint( Math.abs( hc.getHotelCi().getUsePoint() ) );
                            }
                            hc.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            hc.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            hc.getHotelCi().setUseHotenaviId( hotenaviId );
                            hc.getHotelCi().setUseEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            if ( hc.getHotelCi().getAllUseFlag() == 1 )
                            {
                                hc.getHotelCi().setAllUseFlag( 0 );
                                hc.getHotelCi().setAllUsePoint( 0 );
                            }
                        }
                        hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                        ret = hc.getHotelCi().insertData();
                        if ( ret != false )
                        {
                            // 再度最新のデータを取得する
                            ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
                            /** ハピホテタッチ端末用に追加 **/
                            hc.registTouchCi( hc.getHotelCi(), retUse );
                            /** ハピホテタッチ端末用に追加 **/
                            gxTouch.setResult( RESULT_OK );
                            if ( retUse != false )
                            {
                                // バックオフィスのデータを追加
                                retData = bko.cancelBkoData( hc.getHotelCi().getUserId(), hotelId, POINT_KIND_WARIBIKI, hc.getHotelCi() );
                                if ( retData == false )
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_10905;
                                }
                            }
                        }
                        else
                        {
                            gxTouch.setResult( RESULT_NG );
                            // チェックインデータ追加エラー
                            errorCode = HapiTouchErrorMessage.ERR_10902;
                        }
                    }
                    else
                    {
                        gxTouch.setResult( RESULT_NG );
                    }
                    // エラーコードがセットされていればエラーコードをセットする
                    if ( errorCode > 0 )
                    {
                        gxTouch.setErrorCode( errorCode );
                    }
                }
                else
                {
                    gxTouch.setResult( RESULT_NO );
                    errorCode = HapiTouchErrorMessage.ERR_10906;
                    gxTouch.setErrorCode( HapiTouchErrorMessage.ERR_10906 );
                }
            }
            else
            {
                gxTouch.setResult( RESULT_NO );
                errorCode = HapiTouchErrorMessage.ERR_10901;
                gxTouch.setErrorCode( HapiTouchErrorMessage.ERR_10901 );
            }
            if ( errorCode != 0 )
            // エラーが発生していた場合にはエラー履歴に書き込む
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 1 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setReserveNo( rsvNo );
                daeh.setUserId( userId );
                daeh.insertData();
            }
            // XMLの出力
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch visitCancel]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch visitCancel]Exception:" + e.toString() );
                }
            }
        }
    }

    // ポイントを全部減らして、アップデートする
    /****
     * ポイント全使用処理
     * 
     * @param userId ユーザID
     * @param ci ホテルチェックインデータ
     * @param employeeCode 従業員番号
     * @return
     */
    public boolean allUsePoint(String userId, HotelCi ci, int employeeCode, int point)
    {
        int nowPoint = 0;
        boolean ret = false;
        boolean retData = false;
        boolean retUse = false;
        UserPointPay upp;
        UserPointPayTemp uppt;
        upp = new UserPointPay();
        uppt = new UserPointPayTemp();
        // 現在のポイントをセット
        nowPoint = point;
        // 既に使用マイルデータがあるかどうかを確認する
        retData = uppt.getUserPointHistory( ci.getHotelCi().getUserId(), ci.getHotelCi().getId(), POINT_KIND_WARIBIKI, ci.getHotelCi().getUserSeq(), ci.getHotelCi().getVisitSeq() );
        if ( retData != false )
        {
            // データがあるため、使用マイルを更新
            retUse = uppt.setUsePointUpdate( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, nowPoint, employeeCode, ci.getHotelCi() );
            if ( retUse != false )
            {
                // hh_user_point_payに使用マイルの追加
                retUse = upp.setUsePointUpdate( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, nowPoint, employeeCode, ci.getHotelCi() );
            }
        }
        else
        {
            // データがないため、hh_user_point_pay_tempに使用マイルを追加
            retUse = uppt.setUsePoint( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, nowPoint, employeeCode, ci.getHotelCi() );
            if ( retUse != false && ci.getHotelCi().getRsvNo().equals( "" ) ) // 予約Noが入っていたら使用マイルは更新しない。
            {
                // hh_user_point_payに使用マイルの追加
                retUse = upp.setUsePoint( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, nowPoint, employeeCode, ci.getHotelCi() );
            }
        }
        // ポイント履歴の登録がうまくいったら更新
        if ( retUse != false )
        {
            ci.getHotelCi().setAllUseFlag( 1 );
            ci.getHotelCi().setAllUsePoint( nowPoint );
            ci.getHotelCi().setUsePoint( nowPoint );
            ci.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            ci.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = ci.getHotelCi().updateData( ci.getHotelCi().getId(), ci.getHotelCi().getSeq(), ci.getHotelCi().getSubSeq() );
            /** ハピホテタッチ端末用に追加 **/
            if ( ret != false )
            {
                ci.registTouchCi( ci.getHotelCi(), false );
            }
            /** ハピホテタッチ端末用に追加 **/
        }
        return(ret);
    }

    /****
     * ポイント返却処理
     * 
     * @param userId ユーザID
     * @param ci ホテルチェックインデータ
     * @param employeeCode 従業員番号
     * @return
     */
    public boolean returnUsePoint(String userId, HotelCi ci, int employeeCode)
    {
        int nowPoint = 0;
        int price = 0;
        int minusPoint = 0;
        boolean retUse = false;
        UserPointPay upp;
        UserPointPayTemp uppt;
        upp = new UserPointPay();
        uppt = new UserPointPayTemp();
        // 既に使用マイルデータがあるかどうかを確認する
        uppt.getUserPointHistory( ci.getHotelCi().getUserId(), ci.getHotelCi().getId(), POINT_KIND_WARIBIKI, ci.getHotelCi().getUserSeq(), ci.getHotelCi().getVisitSeq() );
        nowPoint = ci.getHotelCi().getAllUsePoint();
        price = ci.getHotelCi().getAmount();
        if ( nowPoint >= price )
        {
            minusPoint = price;
        }
        else
        {
            minusPoint = nowPoint;
        }
        // データがあるため、使用マイルを更新
        retUse = uppt.setUsePointUpdate( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, minusPoint, employeeCode, ci.getHotelCi() );
        if ( retUse != false )
        {
            // hh_user_point_payに使用マイルの追加
            retUse = upp.setUsePointUpdate( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, minusPoint, employeeCode, ci.getHotelCi() );
        }
        return(retUse);
    }

    /**
     * ステータス確認
     * 
     * @param hotelId ホテルID
     * @param response レスポンス
     **/
    public void htFrontTouch(int hotelId, int frontTouchLimit, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        errorCode = 0;
        Logging.info( "hotelId:" + hotelId + ",frontTouchLimit:" + frontTouchLimit, "[ActionHapiTouch htFrontTouch]" );
        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();
            GenerateXmlHapiTouchFrontTouch gxFrontTouch = new GenerateXmlHapiTouchFrontTouch();
            DataApHotelTerminal daht = new DataApHotelTerminal();
            // フロントタッチ端末を許可状態にする
            FrontTouchAcceptCheck ftac = new FrontTouchAcceptCheck();
            if ( ftac.updateFrontTouchEnable( hotelId, frontTouchLimit ) != false )
            {
                if ( daht.getFrontTerminal( hotelId ) != false )
                {
                    gxFrontTouch.setResult( "OK" );
                    gxFrontTouch.setErrorCode( errorCode );
                    gxFrontTouch.setLimitDate( daht.getLimitDate() );
                    gxFrontTouch.setLimitTime( daht.getLimitTime() );
                }
                else
                {
                    errorCode = HapiTouchErrorMessage.ERR_30502;
                }
            }
            else
            {
                errorCode = HapiTouchErrorMessage.ERR_30501;
            }
            if ( errorCode > 0 )
            {
                gxFrontTouch.setResult( "NG" );
                gxFrontTouch.setErrorCode( errorCode );
                gxFrontTouch.setLimitDate( 0 );
                gxFrontTouch.setLimitTime( 0 );
                DataHotelBasic dhb = new DataHotelBasic();
                String hotenaviId = "";
                String hotelName = "";
                // ホテナビIDを取得する
                if ( dhb.getData( hotelId ) != false )
                    ;
                {
                    hotenaviId = dhb.getHotenaviId();
                    hotelName = dhb.getName();
                }
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 1 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.insertData();
            }
            // XMLの出力
            String xmlOut = gxFrontTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch htFrontTouch]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch htFrontTouch]Exception:" + e.toString() );
                }
            }
        }
    }
}
