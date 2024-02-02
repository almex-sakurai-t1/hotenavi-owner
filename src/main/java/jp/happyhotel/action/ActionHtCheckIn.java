package jp.happyhotel.action;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckMailAddr;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.RandomString;
import jp.happyhotel.common.SSTouchDecoder;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataScMAccount;
import jp.happyhotel.data.DataScMMember;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;
import jp.happyhotel.others.HapiTouchRsvSub;
import jp.happyhotel.others.TouchHistory;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.touch.FrontTouchAcceptCheck;
import jp.happyhotel.touch.GroupHotelCustom;
import jp.happyhotel.touch.HotelTerminal;
import jp.happyhotel.touch.MemberRegist;
import jp.happyhotel.touch.NonAutoCheckIn;
import jp.happyhotel.touch.TerminalTouch;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.touch.UserAuth;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserLogin;
import jp.happyhotel.user.UserLoginInfo;
import jp.happyhotel.util.AccessToken;
import jp.happyhotel.util.MD5;

import org.apache.commons.lang.StringUtils;

/**
 * ハピホテアプリチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtCheckIn extends BaseAction
{
    final String           URL_MATCHING          = "happyhotel.jp";
    final String           HAPITOUCH             = "hapiTouch.act?method=";
    final String           METHOD_HT_CHECKIN     = "HtCheckIn";
    final String           METHOD_HT_HOME        = "HtHome";
    final String           METHOD_HT_RSV_FIX     = "HtRsvFix";
    final String           METHOD_HT_NON_AUTO    = "HtNonAuto";
    final int              HAPIHOTE_PORT_NO      = 7046;
    final int              TIMEOUT               = 10000;
    final String           SP_URL                = "/phone/htap/";
    final String           FILE_URL              = "common.jsp";
    final int              CI_STATUS_NOT_DISPLAY = 3;                      // タッチPCのタッチ履歴に残さない
    private UserLoginInfo  uli;
    private SSTouchDecoder sstDecoder;

    /**
     * ハピホテタッチ
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String reqesutURL = new String( request.getRequestURL() );

        // ハピホテ環境であれば
        if ( reqesutURL.indexOf( URL_MATCHING ) != -1 )
        {
            decodeParam( request, response );
        }
        // 社内環境であれば
        else
        {
            decodeParam( request, response );
        }

    }

    /***
     * BUGのリダイレクトサーバー経由で来た場合の復号処理
     * 
     * @param request
     * @param response
     */
    public void decodeParam(HttpServletRequest request, HttpServletResponse response)
    {
        String version;
        String paramX;
        String paramY;
        String paramC;
        String serialNo;
        String forwardUrl;
        int accountID = 0;

        String URL = Url.getUrl() + "/";

        version = request.getParameter( "v" );
        paramX = request.getParameter( "x" );
        paramY = request.getParameter( "y" );
        paramC = request.getParameter( "c" );

        if ( version == null )
        {
            version = "";
        }
        if ( paramX == null )
        {
            paramX = "";
        }
        if ( paramY == null )
        {
            paramY = "";
        }
        if ( paramC == null )
        {
            paramC = "";
        }

        try
        {

            this.sstDecoder = new SSTouchDecoder();
            this.sstDecoder.decode( version, paramX, paramY );
            serialNo = sstDecoder.getTermId();

            // スマホでNFCタッチ

            if ( request.getHeader( "USER-AGENT" ).indexOf( "Android" ) != -1 && sstDecoder.getKind() == sstDecoder.KIND_NFC
                    && paramC.equals( "" ) != false )
            {
                forwardUrl = URL + HAPITOUCH + METHOD_HT_CHECKIN + "&v=" + version + "&x=" + paramX + "&y=" + paramY + "&c=1";
                // URLはUTF-8でURLエンコードを行う。
                forwardUrl = URLEncoder.encode( forwardUrl, "UTF-8" );
                forwardUrl = "intent://?url=" + forwardUrl + "#Intent;scheme=happyhotel;package=jp.happyhotel.android;end";
                response.sendRedirect( forwardUrl );
                return;
            }

            boolean hasAccountID = false;
            String apiKey = StringUtils.defaultIfEmpty( request.getHeader( "x-happyhotel-authorization" ), "" );
            if ( !"".equals( apiKey ) )
            {
                DataScMAccount scAccount = new DataScMAccount();
                hasAccountID = scAccount.getAccountID( apiKey );
                accountID = scAccount.getAccountId();
            }
            if ( accountID == 0 )
            {
                accountID = UserAgent.getAccountId( request );
                if ( accountID != 0 )
                {
                    hasAccountID = true;
                }
            }

            if ( hasAccountID )
            {
                // ステイコンシェルジュアプリからのタッチ
                scApliTouch( request, response, serialNo, accountID );
            }
            else
            {
                // 統合アプリからのタッチ
                happyhotelApliTouch( request, response, serialNo );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionCheckIn.decodeParam()] Exception:" + e.toString() );
        }
    }

    public void scApliTouch(HttpServletRequest request, HttpServletResponse response, String serialNo, int accountID)
    {
        String paramMemberNo;
        String paramUserId;
        DataUserBasic dataUserBasic = new DataUserBasic();

        try
        {
            String accessToken = StringUtils.defaultIfEmpty( request.getParameter( "t" ), "" );

            // トークンを受け取った場合、トークンに設定されたユーザIDとそのユーザのパスワードを変数に設定
            if ( accessToken.equals( "" ) == false )
            {
                // トークンの検証に失敗
                if ( AccessToken.verify( accessToken ) == false )
                {
                    Logging.debug( "method=HtCheckIn, access_token=" + accessToken );
                    Logging.warn( "token verification failed. (token: " + accessToken + ")" );
                    // エラー処理Httpステータスを401にする。
                    response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "認証エラー" );
                    return;
                }

                // トークンの有効期限切れ
                if ( AccessToken.isWithinExpirationTime( accessToken ) == false )
                {
                    Logging.debug( "method=HtCheckIn, access_token=" + accessToken );
                    Logging.warn( "token has expired. (token: " + accessToken + ")" );
                    // エラー処理Httpステータスを401にする。
                    response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "認証エラー" );
                    return;
                }

                paramMemberNo = AccessToken.getUserId( accessToken );

                DataScMMember scData = new DataScMMember();
                scData.getData( accountID, Integer.parseInt( paramMemberNo ) );

                UserBasicInfo ubi = new UserBasicInfo();
                String hhUserID = scData.getHhUserId();
                if ( hhUserID == null )
                {
                    String memberID = scData.getMemberId();

                    int nowDateNum = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTimeNum = Integer.parseInt( DateEdit.getTime( 1 ) );

                    /*
                     * memberID :メールアドレス
                     * memberID が、hh_user_basic に登録されているか否かを検査し、登録されている（ハピホテメンバー）であればそのユーザIDを使う。
                     * 登録されていなければ、sc_から始まるユーザーIDを自動発行する
                     */
                    if ( ubi.getUserBasicByMailaddr( memberID, -1 ) != false )
                    {
                        paramUserId = ubi.getUserInfo().getUserId();
                        if ( ubi.getUserInfo().getRegistStatus() == 0 || ubi.getUserInfo().getRegistStatus() == 4 ) // もし仮登録状態のメアドであれば、ハッピーホテルのユーザは本登録に変更する。
                        {
                            ubi.getUserInfo().setRegistStatus( 9 );
                            if ( CheckMailAddr.checkMailKind( memberID ) == 2 )
                            {
                                ubi.getUserInfo().setRegistDateMobile( nowDateNum );
                                ubi.getUserInfo().setRegistTimeMobile( nowTimeNum );
                            }
                            else
                            {
                                ubi.getUserInfo().setRegistDatePc( nowDateNum );
                                ubi.getUserInfo().setRegistTimePc( nowTimeNum );
                            }
                            ubi.getUserInfo().updateData( paramUserId );
                        }
                    }
                    else
                    {
                        // ユーザーIDを自動発行
                        paramUserId = RandomString.getUserId( "sc_" );
                        int passwordLength = 10;
                        dataUserBasic.setPasswd( ConvertString.convert2md5( RandomString.getRandomString( passwordLength ) ) );// ハッシュ化パスワード
                    }

                    dataUserBasic.setUserId( paramUserId );
                    dataUserBasic.setHandleName( scData.getName() );

                    // 生年月日
                    int birthday = scData.getBirthday1();
                    int birthdayYear = birthday / 10000;
                    int birthdayMonth = birthday / 100 % 100;
                    int birthdayDay = birthday % 100;
                    dataUserBasic.setBirthdayYear( birthdayYear );
                    dataUserBasic.setBirthdayMonth( birthdayMonth );
                    dataUserBasic.setBirthdayDay( birthdayDay );

                    // 性別
                    dataUserBasic.setSex( getHhSex( scData.getSex() ) );

                    String name = CheckString.checkStringForNull( scData.getName() );
                    dataUserBasic.setHandleName( name );

                    // 名前を設定
                    String[] nameArr = getNameArr( name );
                    dataUserBasic.setNameLast( nameArr[0] );
                    if ( nameArr.length == 2 )
                    {
                        dataUserBasic.setNameFirst( nameArr[1] );
                    }
                    String nameKana = CheckString.checkStringForNull( scData.getNameKana() );
                    String[] nameKanaArr = getNameArr( nameKana );
                    dataUserBasic.setNameLastKana( nameKanaArr[0] );
                    if ( nameKanaArr.length == 2 )
                    {
                        dataUserBasic.setNameFirstKana( nameKanaArr[1] );
                    }

                    dataUserBasic.setTel1( scData.getTel1() );

                    int mailType = CheckMailAddr.checkMailKind( memberID );
                    if ( mailType == 1 )
                    {
                        dataUserBasic.setMailAddr( memberID );
                        dataUserBasic.setMailAddrMd5( MD5.convert( memberID ) );
                    }
                    else
                    {
                        dataUserBasic.setMailAddrMobile( memberID );
                        dataUserBasic.setMailAddrMobileMd5( MD5.convert( memberID ) );
                    }

                    dataUserBasic.setTempDateMobile( nowDateNum );
                    dataUserBasic.setTempTimeMobile( nowTimeNum );
                    dataUserBasic.setRegistDateMobile( nowDateNum );
                    dataUserBasic.setRegistTimeMobile( nowTimeNum );
                    dataUserBasic.setRegistStatus( 9 );

                    dataUserBasic.insertData();

                    // sc.r_user_memberにデータ挿入（ハピホテユーザと連携する）
                    scData.setHhUserId( paramUserId );
                    scData.setRegistDate( nowDateNum );
                    scData.setRegistTime( nowTimeNum );
                    scData.setUpdateDate( nowDateNum );
                    scData.setUpdateTime( nowTimeNum );
                    scData.insertUserMember();

                }
                else
                {
                    paramUserId = hhUserID;
                    ubi.getUserBasic( paramUserId );
                    registCookie( uli.getUserInfo(), request, response );
                }
                registCookie( uli.getUserInfo(), request, response );
                checkIn( request, response, serialNo, paramUserId, accountID );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionCheckIn.scApliTouch()] Exception:" + e.toString() );
        }
    }

    public void happyhotelApliTouch(HttpServletRequest request, HttpServletResponse response, String serialNo)
    {
        int carrierFlag;
        String forwardUrl;
        String paramUserId;
        DataUserFelica duf;
        String URL = Url.getUrl() + "/";

        try
        {
            paramUserId = UserAuth.getUserId( request.getParameter( "t" ) );
            if ( StringUtils.isEmpty( paramUserId ) )
            {
                response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "認証エラー" );
                return;
            }

            uli = new UserLoginInfo();
            uli.getUserLoginInfo( paramUserId );

            // タッチの場合、タブレットはスマホと同じ扱いにする。
            carrierFlag = UserAgent.getUserAgentTypeFromTouch( request );

            // スマホ以外のユーザエージェントは携帯のタッチとみなして、IDMからデータを取得
            if ( carrierFlag != DataMasterUseragent.CARRIER_SMARTPHONE )
            {
                // フェリカデータから取得
                duf = new DataUserFelica();
                if ( duf.getUserData( sstDecoder.getIdm() ) != false )
                {
                    paramUserId = duf.getUserId();
                }
                else
                {
                    forwardUrl = URL + "regist_idm.jsp?method=Regist&idm=" + sstDecoder.getIdm();
                    response.sendRedirect( forwardUrl );
                    return;
                }
            }
            else
            {
                // スマホの場合はアプリ経由なので、UserLoginInfoにメンバー情報が入っている。
                // メンバー情報がない場合は非会員として扱う。
                if ( uli.isMemberFlag() == false )
                {
                    paramUserId = "";
                }
                // ログインOKの場合ログイン処理を行う。
                else
                {
                    if ( uli.isMemberFlag() != false )
                    {
                        paramUserId = uli.getUserInfo().getUserId(); // メールアドレスの場合があるので、ユーザーIDに変換
                        registCookie( uli.getUserInfo(), request, response );
                    }
                }
            }
            if ( !serialNo.equals( "" ) && !paramUserId.equals( "" ) )
            {
                checkIn( request, response, serialNo, paramUserId, 0 );
            }
            // 端末ID、ユーザIDが取得できない場合はログイン画面へ
            else if ( paramUserId.equals( "" ) != false && sstDecoder.getKind() == sstDecoder.KIND_NFC )
            {
                // ログインを促す画面を表示
                response.sendRedirect( URL + "/phone/others/info_login_app.jsp" );
            }
            else
            {
                // ログインを促す画面を表示
                response.sendRedirect( URL + "/phone/others/info_login_app.jsp" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionCheckIn.happyhotelApliTouch()] Exception:" + e.toString() );
        }
    }

    /****
     * 端末番号とユーザIDからチェックイン処理を行う。
     * 
     * @param request
     * @param response
     * @param termNo
     * @param userId
     */
    public void checkIn(HttpServletRequest request, HttpServletResponse response, String serialNo, String userId, int accountID)
    {
        int carrierFlag;
        boolean ret = false;
        boolean boolRegTc = false;// タッチデータ登録
        boolean boolExistRsv = false;
        FormReserveSheetPC frm = null;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();

        String idm = "";
        int hotelId = 0;// ホテルID
        int termId = 0;// ホテルごとの端末ID
        int termNo = 0;// ターミナルNo
        int roomNo = 0;// 部屋番号
        int ttRoomNo = 0;// 履歴から取得した部屋番号
        String roomName = "";// 部屋名称
        String ttRoomName = "";// 履歴から取得した部屋名称
        String hotelName = ""; // ホテル名
        String hotenaviId = ""; // ホテナビID
        String rsvNo = "";
        String touchIp = "";
        String touchUseragent = "";
        int errorCode = 0;
        String forwardUrl = "";
        int ciSeq = 0; // チェックインコード
        String uidLink = "";
        String paramType = "";
        String hotelIp = "";
        int terminalFlag = 0;
        boolean reTouch = false;
        int autoCheckinFlag = 1; // 1:タッチ時自動来店
        String ipAddress = ""; // タッチ端付属機器IPアドレス
        String[] anotherRoomList = null;
        int anotherRoomCount = 0;
        boolean isReplaceUserId = false;

        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        DataUserBasic dub = new DataUserBasic();
        DataApHotelSetting dahs = new DataApHotelSetting();
        DataApUuidUser dauu = new DataApUuidUser();
        HotelTerminal ht = new HotelTerminal();
        TerminalTouch tt = new TerminalTouch();
        TouchCi tc = new TouchCi();
        HotelCi hc = new HotelCi();
        NonAutoCheckIn naci = new NonAutoCheckIn();
        DataApHotelCustom dahc = new DataApHotelCustom();

        try
        {
            String accessToken = request.getParameter( "t" );
            touchIp = request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr();
            touchUseragent = request.getHeader( "User-Agent" );
            paramType = request.getParameter( "type" );

            carrierFlag = UserAgent.getUserAgentTypeFromTouch( request );
            // URLを判断
            if ( carrierFlag == DataMasterUseragent.CARRIER_SMARTPHONE )
            {
                forwardUrl = SP_URL;
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
            {
                forwardUrl = "/i/htap/";
                uidLink = "&uid=NULLGWDOCOMO";
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_AU )
            {
                forwardUrl = "/au/htap/";
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
            {
                forwardUrl = "/y/htap/";
                uidLink = "&uid=1&sid=EIJC&pid=P0P3";
            }
            else
            {
                if ( UserAgent.getUserAgentTypeString( request ).equals( "ipa" ) ||
                        UserAgent.getUserAgentTypeString( request ).equals( "ada" ) )
                {
                    forwardUrl = SP_URL;
                }
            }

            if ( paramType == null || paramType.equals( "resserve" ) == false )
            {
                paramType = "";
            }

            ret = dub.getData( userId );
            if ( ret == false )
            {
                userId = "";
            }
            if ( userId.equals( "" ) != false )
            {
                ret = false;
                // 非会員がタッチしている場合
                errorCode = HapiTouchErrorMessage.ERR_30102;
            }
            if ( ret != false )
            {

                if ( serialNo.equals( "" ) != false )
                {
                    ret = false;
                    // 端末IDが取得できない場合
                    errorCode = HapiTouchErrorMessage.ERR_30110;
                }
            }

            int appStatus = 1; // 0:未ログイン状態,1:ログイン状態
            // ap_uuid_user に登録されていないユーザーは同意済みユーザーとみなす。
            if ( dauu.getAppData( userId ) != false )
            {
                appStatus = dauu.getAppStatus();
            }

            if ( ret != false )
            {
                // 端末のシリアル番号からホテルID、部屋番号を取得
                if ( ht.getHotelTerminal( serialNo ) == false )
                {
                    errorCode = HapiTouchErrorMessage.ERR_30110;
                    ret = false;
                }
                else if ( ht.getTerminal().getStartDate() > Integer.parseInt( DateEdit.getDate( 2 ) ) || ht.getTerminal().getEndDate() < Integer.parseInt( DateEdit.getDate( 2 ) ) )
                {
                    /* 端末の日付範囲が有効でない場合 */
                    errorCode = HapiTouchErrorMessage.ERR_30122;
                    ret = false;
                }
                else
                {
                    hotelId = ht.getTerminal().getId();
                    termId = ht.getTerminal().getTerminalId();
                    termNo = ht.getTerminal().getTerminalNo();
                    roomNo = ht.getTerminal().getRoomNo();
                    autoCheckinFlag = ht.getTerminal().getAutoCheckinFlag();
                    ipAddress = ht.getTerminal().getIpAddress();

                    if ( dhb.getData( hotelId ) != false )
                    {
                        hotelName = dhb.getName();
                        hotenaviId = dhb.getHotenaviId();
                    }
                    else
                    {
                        ret = false;
                        // ホテルマスタを取得できない場合
                        errorCode = HapiTouchErrorMessage.ERR_30114;
                    }
                    if ( roomNo != 0 )// フロント以外
                    {
                        if ( dhrm.getData( hotelId, roomNo ) != false )
                        {
                            if ( dhrm.getRoomNameHost().equals( "" ) )
                            {
                                ret = false;
                                // 部屋マスタを取得できない場合(データがないとき)
                                errorCode = HapiTouchErrorMessage.ERR_30115;
                            }
                            else
                            {
                                roomName = dhrm.getRoomNameHost();
                            }
                        }
                        else
                        {
                            ret = false;
                            // 部屋マスタを取得できない場合(Exceptionのとき)
                            errorCode = HapiTouchErrorMessage.ERR_30115;
                        }
                    }
                    if ( dahs.getData( hotelId ) != false )
                    {
                        terminalFlag = dahs.getTerminalFlag(); // ターミナルフラグ：1･･･ターミナル端末あり
                        if ( terminalFlag <= 0 )
                        {
                            ret = false;
                            // 端末IDが有効ではない場合
                            errorCode = HapiTouchErrorMessage.ERR_30111;
                        }

                    }
                    else
                    {
                        ret = false;
                        // ホテル設定ファイル取得できない場合
                        errorCode = HapiTouchErrorMessage.ERR_30116;
                    }
                    if ( ret != false )
                    {
                        // SSTの不正録音対策に、RTCタイマーの比較を行う。
                        // タイマー値が小さい場合は、エラー
                        if ( ht.getTerminal().getRtcTimer() < sstDecoder.getRtcTimer() )
                        {
                            ht.getTerminal().setRtcTimer( sstDecoder.getRtcTimer() );
                            ht.getTerminal().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            ht.getTerminal().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            ht.getTerminal().updateData( hotelId, termId );
                        }
                        else
                        {
                            ret = false;
                            // RTCタイマー不正
                            errorCode = HapiTouchErrorMessage.ERR_30103;
                        }
                    }
                    if ( ret != false )
                    {
                        // フロントのIPアドレスを取得
                        hotelIp = HotelIp.getFrontIp( hotelId );
                        if ( autoCheckinFlag == 1 ) // チェックイン時にhh_hotel_ciを作成する端末の場合
                        {
                            boolean boolTerminal = false;
                            if ( roomNo == 0 ) // フロントタッチ
                            {
                                boolTerminal = tt.getTerminalTouch( hotelId, termId );
                            }
                            else
                            {
                                boolTerminal = tt.getTerminalTouchFromRoomNo( hotelId, roomNo );
                            }
                            // ホテルid、端末IDからタッチ端末履歴データを取得

                            if ( boolTerminal != false )
                            {
                                ciSeq = tt.getTerminal().getCiSeq();
                                ttRoomNo = tt.getTerminal().getRoomNo();
                                if ( dhrm.getData( hotelId, ttRoomNo ) != false )
                                {
                                    ttRoomName = dhrm.getRoomNameHost();
                                }
                                else
                                {
                                    ret = false;
                                    // 部屋マスタを取得できない場合
                                    errorCode = HapiTouchErrorMessage.ERR_30115;
                                }
                                // もし、同ユーザ・同部屋で最新チェックインコードがあるのならそれを優先する
                                if ( hc.getCheckInCode( hotelId, ciSeq, ttRoomName, userId ) )
                                {
                                    if ( hc.getHotelCi().getSeq() > ciSeq )
                                    {
                                        ciSeq = hc.getHotelCi().getSeq();
                                    }
                                    rsvNo = hc.getHotelCi().getRsvNo();
                                    if ( rsvNo.equals( "" ) == false )
                                    {
                                        boolExistRsv = true;
                                    }
                                    if ( hc.getHotelCi().getCiStatus() == 0 || (hc.getHotelCi().getCiStatus() == 3 && hc.getHotelCi().getExtUserFlag() == 1) )
                                    {
                                        reTouch = true; // 再タッチということ
                                    }
                                }
                            }
                        }
                        else
                        {
                            // メニュー盤でのタッチでは、チェックインデータをまだ作成しない。（ホストからのVisit電文により作成する）
                            ret = false;

                            //
                            frm = htRsvSub.getReserveData( userId, hotelId, ciSeq );
                            // 取得した予約データが連泊予約の途中の日付の場合、親予約データを取得し直す
                            if ( StringUtils.isNotBlank( frm.getRsvNoMain() ) && !frm.getRsvNo().substring( frm.getRsvNo().indexOf( "-" ) + 1 ).equals( frm.getRsvNoMain() ) )
                            {
                                String reserveNo = htRsvSub.getMainReserveNo( hotelId, frm.getRsvNoMain() );
                                if ( StringUtils.isNotBlank( reserveNo ) )
                                {
                                    frm = htRsvSub.getReserveData( userId, hotelId, reserveNo );
                                }
                            }
                            roomName = Integer.toString( frm.getSeq() );
                            if ( dhrm.getData( hotelId, frm.getSeq() ) != false )
                            {
                                roomName = dhrm.getRoomNameHost();
                            }

                            // 同ユーザーの同ホテルでの24時間以内のチェックインを調べる
                            if ( hc.getCheckInBeforeData( hotelId, userId ) != false )
                            {
                                if ( !frm.getRsvNo().equals( hc.getHotelCi().getRsvNo() ) )
                                {
                                    // チェックイン中の予約Noと違うのでエラーとする。
                                    ret = false;
                                    ciSeq = hc.getHotelCi().getSeq();
                                    errorCode = HapiTouchErrorMessage.ERR_30120;
                                }
                            }
                            if ( errorCode == 0 )
                            {
                                /** （1014）ホストにユーザーIDを知らせる **/

                                naci.setUserId( userId );
                                if ( dahc.getValidData( hotelId, userId ) != false )
                                {
                                    naci.setCustomId( dahc.getCustomId() );
                                    naci.setSecurityCode( dahc.getSecurityCode() );
                                }
                                else
                                {
                                    GroupHotelCustom hotelCustom = new GroupHotelCustom();
                                    if ( hotelCustom.getMutltiCustomData( hotelId, userId ) != false )
                                    {
                                        naci.setCustomId( hotelCustom.getCustom().getCustomId() );
                                        naci.setSecurityCode( hotelCustom.getCustom().getSecurityCode() );
                                    }
                                }

                                naci.setRsvNo( frm.getRsvNo() );
                                naci.setRoomName( roomName );
                                anotherRoomList = htRsvSub.getAnotherRoom( hotelId, frm.getSelPlanId(), frm.getSelPlanSubId(), frm.getRsvDate(), roomName, frm.getRsvNo() );
                                anotherRoomCount = anotherRoomList.length;

                                if ( !frm.getRsvNo().equals( "" ) )
                                {
                                    naci.setDispRsvNo( frm.getRsvNo().substring( frm.getRsvNo().length() - 6 ) );
                                }
                                naci.setRoomNameListLength( anotherRoomCount );
                                naci.setRoomNameList( anotherRoomList );

                                naci.setIpAddress( ipAddress );
                                naci.setErrorNo( errorCode );
                                naci.sendToHost( hotelIp, TIMEOUT, HAPIHOTE_PORT_NO, Integer.toString( hotelId ) );
                            }
                        }
                    }
                }

                if ( ret != false )
                {
                    // 2015.03.22 予約データを取得:フロントタッチの許可をするために取得順を変更 tashiro
                    // ユーザーの予約データの取得

                    frm = htRsvSub.getReserveData( userId, hotelId, ciSeq );
                    // 取得した予約データが連泊予約の途中の日付の場合、親予約データを取得し直す
                    if ( StringUtils.isNotBlank( frm.getRsvNoMain() ) && !frm.getRsvNo().substring( frm.getRsvNo().indexOf( "-" ) + 1 ).equals( frm.getRsvNoMain() ) )
                    {
                        String reserveNo = htRsvSub.getMainReserveNo( hotelId, frm.getRsvNoMain() );
                        if ( StringUtils.isNotBlank( reserveNo ) )
                        {
                            frm = htRsvSub.getReserveData( userId, hotelId, reserveNo );
                        }
                    }

                    if ( frm.getSeq() != 0 && roomNo != 0 ) // フロントタッチの場合は予約部屋名称を使わない
                    {
                        if ( roomNo != frm.getSeq() ) // フロントタッチではなくて予約案内部屋以外に入室した場合にはエラーにする。
                        {
                            ret = false;
                            errorCode = HapiTouchErrorMessage.ERR_30121;
                        }
                        else if ( dhrm.getData( hotelId, frm.getSeq() ) != false )// ホスト用部屋名称を取得
                        {
                            roomName = dhrm.getRoomNameHost();
                        }
                    }
                    if ( ret != false )
                    {
                        // 同ユーザーの同ホテルでの24時間以内のチェックインを調べる
                        if ( hc.getCheckInBeforeData( hotelId, userId ) != false )
                        {
                            if ( (!frm.getRsvNo().equals( "" ) || !hc.getHotelCi().getRsvNo().equals( "" )) && !frm.getRsvNo().equals( hc.getHotelCi().getRsvNo() ) )
                            {
                                // チェックイン中の予約Noと違うのでエラーとする。
                                ret = false;
                                ciSeq = hc.getHotelCi().getSeq();
                                errorCode = HapiTouchErrorMessage.ERR_30120;
                            }
                            else
                            {
                                rsvNo = frm.getRsvNo();
                                if ( rsvNo.equals( "" ) == false )
                                {
                                    boolExistRsv = true;
                                }
                            }
                            if ( ret != false )
                            {
                                if ( roomNo == 0 ) // フロントタッチの場合は、チェックインデータの部屋名称にする。
                                {
                                    roomName = hc.getHotelCi().getRoomNo();
                                }
                                if ( hotelIp.equals( "" ) == false )
                                {
                                    if ( hc.getHotelCi().getRoomNo().equals( roomName ) == false )
                                    {
                                        ret = false;
                                        ciSeq = hc.getHotelCi().getSeq();
                                        errorCode = HapiTouchErrorMessage.ERR_30106;
                                    }
                                    else
                                    {
                                        TouchCi.registTouchCi( hc.getHotelCi() );// ap_touch_ci と同期
                                    }
                                }
                            }
                        }
                        else
                        {
                            rsvNo = frm.getRsvNo();
                            if ( rsvNo.equals( "" ) == false )
                            {
                                boolExistRsv = true;
                            }
                        }
                    }
                }
                if ( ret != false )
                {
                    if ( roomNo > 0 )
                    {
                        // if ( hc.getData( hotelId, ciSeq ) != false )
                        if ( hc.getDataFromRoom( hotelId, roomName ) != false ) // 部屋に24時間以内のチェックインデータがある
                        {
                            // チェックインコードの読み込み
                            ciSeq = hc.getHotelCi().getSeq();

                            // データがあり、ciStatus != 0の場合はap_touch_ci書き込み
                            // またはIDMが違っている・マイルが使用されていない場合はap_touch_ciに書き込みを行う。
                            if ( hc.getHotelCi().getCiStatus() != 0 && hc.getHotelCi().getCiStatus() != 4 )
                            {
                                boolRegTc = true;
                            }
                            else if ( userId.equals( hc.getHotelCi().getUserId() ) == false )
                            {

                                // マイル使用済みの場合は新しいタッチを受付けない
                                if ( hc.getHotelCi().getUsePoint() > 0 )
                                {
                                    ret = false;
                                    errorCode = HapiTouchErrorMessage.ERR_30104;
                                }
                                // ホスト連動物件で、メンバーカード受付済みの場合は新しいタッチを受付けない
                                else if ( hotelIp.equals( "" ) == false && hc.getHotelCi().getCustomId().equals( "" ) == false )
                                {
                                    ret = false;
                                    errorCode = HapiTouchErrorMessage.ERR_30105;
                                }
                                // 既に割当済みのデータが非会員以外の場合、タッチした人が非会員であれば上書き不可
                                // hh_hotel_ciが非会員で上書きされるのは避けるため
                                else if ( hc.getHotelCi().getUserType() != 99 && userId.equals( "" ) != false )
                                {
                                    ret = false;
                                    errorCode = HapiTouchErrorMessage.ERR_30109;
                                }
                                // 既に割当済みのデータが予約チェックインの場合は受け付けない
                                else if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                                {
                                    ret = false;
                                    errorCode = HapiTouchErrorMessage.ERR_30120;
                                }
                                else
                                {
                                    if ( appStatus == 0 )
                                    {
                                        ret = false;
                                        errorCode = HapiTouchErrorMessage.ERR_30109;
                                    }
                                    else
                                    {
                                        boolRegTc = true;
                                        if ( tc.getData( hotelId, ciSeq ) != false )
                                        {
                                            tc.getTouchCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                            tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                            tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                            tc.getTouchCi().updateData( hotelId, ciSeq );
                                            tc.registHotelCi( tc.getTouchCi() );
                                        }
                                        else
                                        {
                                            hc.getHotelCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                            hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                            hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                            hc.getHotelCi().updateData( hotelId, ciSeq, hc.getHotelCi().getSubSeq() );
                                        }

                                        /* チェックインデータを新しく作成はするが、チェックインコードは新しくしない ユーザーIDを置き換える */
                                        isReplaceUserId = true;
                                    }
                                }
                            }
                        }
                        // タッチしたユーザーでのチェックインデータがないが、他のユーザーでのチェックインデータはないだろうか？
                        else
                        {
                            boolRegTc = true;
                            // 2015.06.11 ホスト連動物件で、ユーザの以前のチェックインデータがある
                            if ( hotelIp.equals( "" ) == false && hc.getDataFromRoom( hotelId, roomName ) != false )
                            {
                                // なんだか知らないけど、同じ部屋にチェックインデータがある。フロントでの予約チェックインなどなど
                                if ( !hc.getHotelCi().getRsvNo().equals( "" ) )
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_30120;
                                    ret = false;
                                    boolRegTc = false;
                                }
                            }
                            // 対象データがなかったのだたら予約Noはクリアする
                            hc.getHotelCi().setRsvNo( "" );
                        }
                    }
                }
                if ( ret != false )
                { // 2015.03.22 予約がない場合でタッチされた部屋がフロントだった場合 tashiro
                    if ( boolExistRsv == false && roomName.equals( "" ) != false )
                    {
                        FrontTouchAcceptCheck ftac = new FrontTouchAcceptCheck();

                        // 2015.03.22 フロントのタッチが有効かどうかを確認する tashiro
                        if ( ftac.check( ht.getTerminal() ) == false )
                        {
                            errorCode = HapiTouchErrorMessage.ERR_30107;
                            ret = false;
                            boolRegTc = false;
                        }
                        else
                        {
                            boolRegTc = true;
                        }
                    }
                    else if ( boolExistRsv )
                    {
                        if ( roomName.equals( "" ) != false ) // 予約でフロントだったら
                        {
                            boolRegTc = true;
                        }
                        else
                        {
                            if ( !hc.getHotelCi().getRsvNo().equals( "" ) ) // タッチ済みが予約だったら
                            {
                                boolRegTc = false;
                            }
                            else
                            {
                                boolRegTc = true;
                            }
                        }
                    }
                }
                // チェックインデータ作成だったらチェックインデータ作成
                if ( boolRegTc != false && ret != false )
                {
                    // ap_touch_ciデータに書き込み 同時にhh_hotel_ciにも書き込まれる
                    tc = tc.registCiData( userId, idm, hotelId, roomName, isReplaceUserId, ciSeq );

                    // 書き込み後のチェックインコードの読み込み
                    ciSeq = tc.getTouchCi().getSeq();

                    if ( roomNo == 0 ) // フロントチェックインの場合は、予約の部屋番号をセット
                    {
                        roomNo = frm.getSeq();
                    }
                    // チェックインデータ送信OKであれば、ap_terminal_touchデータに書き込み(ci_seq書き込み)
                    ret = tt.registTerminalTouch( tt.getTerminal(), hotelId, termId, userId, idm, roomNo, touchIp, touchUseragent, ciSeq );

                    // チェックインデータ作成に失敗したらエラー
                    if ( ret == false )
                    {
                        errorCode = HapiTouchErrorMessage.ERR_30101;
                    }
                }
            }

            // SCメンバーが初回来店タッチしたとき顧客登録
            if ( accountID != 0 )/* ステイコンシェルジュ */
            {
                MemberRegist mr = new MemberRegist();
                mr.makeScHotenaviMember( accountID, hotelId, userId );
            }

            // ホテルタッチ履歴書込み
            TouchHistory touchHistory = null;
            touchHistory = new TouchHistory();
            touchHistory.touchHistory( hotelId, "HtCheckIn", String.valueOf( ciSeq ), userId, request );

            Logging.info( "[ActionHtCheckIn.checkIn()]ret:" + ret + ",boolRegTc:" + boolRegTc + ",ciSeq=" + ciSeq + ",userId=" + userId + " rsvNo=" + rsvNo + ",roomName=" + roomName + ",dhrm.getRoomNameHost() =" + dhrm.getRoomNameHost() );

            // チェックインが登録できたらホーム画面へ、失敗したらエラー画面へ
            if ( ret != false )
            {

                // 予約番号が取得できたら、予約来店処理の画面へ遷移する
                if ( rsvNo.equals( "" ) == false )
                {
                    response.sendRedirect( forwardUrl + HAPITOUCH + METHOD_HT_RSV_FIX + "&id=" + hotelId + "&seq=" + ciSeq + "&rsvNo=" + rsvNo + "&reTouch=" + reTouch + uidLink );
                    return;
                }
                else
                {
                    response.sendRedirect( forwardUrl + HAPITOUCH + METHOD_HT_HOME + "&id=" + hotelId + "&seq=" + ciSeq + "&t=" + accessToken + "&serialNo=" + serialNo + "&reTouch=" + reTouch + uidLink );
                    return;
                }
            }
            else if ( errorCode != 0 )
            {
                // エラー内容を登録
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setTerminalId( termId );
                daeh.setTerminalNo( termNo );
                daeh.setRoomName( roomName );
                daeh.setRoomNo( roomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( ciSeq );
                daeh.setReserveNo( rsvNo );
                daeh.setUserId( userId );
                daeh.insertData();

                // リダイレクトURLをセット
                forwardUrl += "error.jsp?id=" + hotelId;
                forwardUrl += "&seq=" + ciSeq;
                forwardUrl += "&reserveNo=" + rsvNo;
                forwardUrl += "&roomNo=" + roomNo;
                forwardUrl += "&errorCode=" + errorCode;
                forwardUrl += "&head=1";

                response.sendRedirect( forwardUrl );
                return;
            }
            else
            // Rdesign の場合
            {
                // NonAuto.jsp にフォワードする
                response.sendRedirect( forwardUrl + HAPITOUCH + METHOD_HT_NON_AUTO + "&id=" + hotelId + "&seq=" + ciSeq + "&t=" + accessToken + "&serialNo=" + serialNo + uidLink );
                return;
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHtCheckIn.checkIn()]Exception:" + exception );
        }
        finally
        {
        }
    }

    /**
     * 
     * @param uli
     * @param request
     * @param response
     */
    public void registCookie(DataUserBasic uli, HttpServletRequest request, HttpServletResponse response)
    {
        Cookie hhcookie = null;
        String cookieValue = "";

        if ( uli.getMailAddrMd5().compareTo( "" ) != 0 )
        {
            cookieValue = uli.getMailAddrMd5();
        }
        else if ( uli.getMailAddrMobileMd5().compareTo( "" ) != 0 )
        {
            cookieValue = uli.getMailAddrMobileMd5();
        }
        else
        {
            cookieValue = uli.getUserId();
        }
        hhcookie = new Cookie( "hhuid", cookieValue );
        hhcookie.setPath( "/" );
        hhcookie.setDomain( ".happyhotel.jp" );
        hhcookie.setMaxAge( Integer.MAX_VALUE );

        // ログイン成功
        response.addCookie( hhcookie );

        // クッキー("u"=)作成
        UserLogin login = new UserLogin();

        if ( login.userLoginbyTouch( request ) != false )
        {
            if ( login.makeCookieValue( uli.getUserId() ) != false )
            {
                cookieValue = login.getCookieValue();
                hhcookie = new Cookie( "u", cookieValue );
                hhcookie.setPath( "/" );
                hhcookie.setDomain( ".happyhotel.jp" );
                hhcookie.setMaxAge( Integer.MAX_VALUE );
            }
            else
            {
                // クッキー削除
                hhcookie = new Cookie( "u", "" );
                hhcookie.setPath( "/" );
                hhcookie.setMaxAge( 0 );
                hhcookie.setDomain( ".happyhotel.jp" );
                hhcookie.setValue( "" );
            }
        }
        else
        {
            // クッキー削除
            hhcookie = new Cookie( "u", "" );
            hhcookie.setPath( "/" );
            hhcookie.setMaxAge( 0 );
            hhcookie.setDomain( ".happyhotel.jp" );
            hhcookie.setValue( "" );
        }
        // ログイン成功
        response.addCookie( hhcookie );
    }

    private String[] getNameArr(String name)
    {
        return name.replace( "　", " " ).split( " ", 2 );
    }

    /**
     * ステイコンシェルジュホテナビメンバーの性別データをハピホテ用性別データに変換
     * 
     * @param sex
     * @return
     */
    private int getHhSex(int sex)
    {
        // 男性
        if ( sex == 1 )
            return 0;
        // 女性
        else if ( sex == 2 )
            return 1;
        else
            return 2;
    }

}
