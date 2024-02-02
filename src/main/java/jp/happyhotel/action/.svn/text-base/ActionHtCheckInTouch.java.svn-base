package jp.happyhotel.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;
import jp.happyhotel.others.HapiTouchRsvSub;
import jp.happyhotel.others.TouchHistory;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.touch.FrontTouchAcceptCheck;
import jp.happyhotel.touch.HotelTerminal;
import jp.happyhotel.touch.TerminalTouch;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.user.UserLogin;
import jp.happyhotel.user.UserLoginInfo;
import jp.happyhotel.util.AccessToken;

import org.apache.commons.lang.StringUtils;

// import jp.happyhotel.common.SSTouchDecoder;

/**
 * ハピホテアプリチェックインクラス（非会員タッチ後ログイン）
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtCheckInTouch extends BaseAction
{
    final String          URL_MATCHING          = "happyhotel.jp";
    final String          HAPITOUCH             = "hapiTouch.act?method=";
    final String          METHOD_HT_CHECKIN     = "HtCheckIn";
    final String          METHOD_HT_HOME        = "HtHome";
    final String          METHOD_HT_RSV_FIX     = "HtRsvFix";
    final String          SP_URL                = "/phone/htap/";
    final String          FILE_URL              = "common.jsp";
    final int             CI_STATUS_NOT_DISPLAY = 3;                      // タッチPCのタッチ履歴に残さない
    private UserLoginInfo uli;

    // private SSTouchDecoder sstDecoder;

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

        String serialNo;
        String paramUserId;
        String paramPassword;
        String auto;

        paramUserId = request.getParameter( "user_id" );
        paramPassword = request.getParameter( "password" );
        serialNo = request.getParameter( "serialNo" );
        auto = request.getParameter( "auto" );

        try
        {
            String accessToken = StringUtils.defaultIfEmpty( request.getParameter( "t" ), "" );

            // トークンを受け取った場合、トークンに設定されたユーザIDとそのユーザのパスワードを変数に設定
            if ( accessToken.equals( "" ) == false )
            {
                // トークンの検証に失敗
                if ( AccessToken.verify( accessToken ) == false )
                {
                    Logging.info( "method=HtCheckInTouch, access_token=" + accessToken );
                    Logging.warn( "token verification failed. (token: " + accessToken + ")" );
                    // エラー処理Httpステータスを401にする。
                    response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "認証エラー" );
                    return;
                }

                // トークンの有効期限切れ
                if ( AccessToken.isWithinExpirationTime( accessToken ) == false )
                {
                    Logging.info( "method=HtCheckInTouch, access_token=" + accessToken );
                    Logging.warn( "token has expired. (token: " + accessToken + ")" );
                    // エラー処理Httpステータスを401にする。
                    response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "認証エラー" );
                    return;
                }

                paramUserId = AccessToken.getUserId( accessToken );
                DataUserBasic dataUserBasic = new DataUserBasic();
                dataUserBasic.getData( paramUserId );
                paramPassword = UserLogin.decrypt( dataUserBasic.getPasswd() );
            }
            // 未ログインユーザの場合、未ログインユーザを示すユーザIDとパスワードを設定
            // else if ( paramUserId.equals( "" ) && paramPassword.equals( "" ) && accessToken.equals( "" ) )
            // {
            // paramUserId = UserLoginInfo.NON_MEMBER_USER_ID;
            // paramPassword = UserLoginInfo.NON_MEMBER_PASSWD;
            // }

            uli = new UserLoginInfo();
            uli.getUserLoginInfo( paramUserId, paramPassword );

            paramUserId = uli.getUserInfo().getUserId(); // メールアドレスの場合があるので、ユーザーIDに変換
            registCookie( uli.getUserInfo(), request, response );

            if ( serialNo.equals( "" ) == false && paramUserId.equals( "" ) == false )
            {
                checkIn( request, response, serialNo, paramUserId, paramPassword, auto );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHtCheckInTouch.decodeParam()] Exception:" + e.toString() );
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
    public void checkIn(HttpServletRequest request, HttpServletResponse response, String serialNo, String userId, String passWord, String auto)
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

        try
        {
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

                        // フロントのIPアドレスを取得
                        hotelIp = HotelIp.getFrontIp( hotelId );

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
                        else if ( dhrm.getData( hotelId, frm.getSeq() ) != false )// 部屋名称を取得
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
                                // フロントタッチの場合は、チェックインデータの部屋名称にする。
                                if ( roomNo == 0 )
                                {
                                    roomName = hc.getHotelCi().getRoomNo();
                                }
                                if ( hotelIp.equals( "" ) == false )
                                {
                                    // 2015.03.20 部屋番号が違っていたらエラー tashiro
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
                    // 取得したタッチ端末履歴データから、その部屋のタッチ履歴を呼び出す(24時間経過していたらデータは取得しない。)
                    // 2015.03.21 フロントでタッチした場合は下記のロジックを通さない。 tashiro
                    if ( roomNo > 0 )
                    {
                        if ( hc.getDataFromRoom( hotelId, roomName ) != false ) // 部屋に以前のチェックインデータがある
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
                        }
                        // タッチしたユーザーでのチェックインデータがないが、他のユーザーでのチェックインデータはないだろうか？
                        else
                        {
                            boolRegTc = true;
                            // 2015.06.11 ホスト連動物件で、ユーザの以前のチェックインデータがある
                            if ( hotelIp.equals( "" ) == false )
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
                            }
                            // 対象データがなかったのだたら予約Noはクリアする
                            hc.getHotelCi().setRsvNo( "" );
                        }
                    }
                    if ( ret != false )
                    {
                        // 2015.03.22 予約がない場合でタッチされた部屋がフロントだった場合 tashiro
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
                                if ( !hc.getHotelCi().getRsvNo().equals( "" ) )
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
            }

            // ホテルタッチ履歴書込み
            TouchHistory touchHistory = null;
            touchHistory = new TouchHistory();
            touchHistory.touchHistory( hotelId, "HtCheckInTouch", String.valueOf( ciSeq ), userId, request );

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
                    response.sendRedirect( forwardUrl + HAPITOUCH + METHOD_HT_HOME + "&id=" + hotelId + "&seq=" + ciSeq + "&user_id=" + userId + "&password=" + passWord + "&reTouch=" + reTouch + "&auto=" + auto + uidLink );
                    return;
                }
            }
            else
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

                // requestDispatcher = request.getRequestDispatcher( forwardUrl + FILE_URL );
                // requestDispatcher.forward( request, response );

                response.sendRedirect( forwardUrl );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHtCheckInTouch.checkIn()]Exception:" + exception, "ActionHtCheckInTouch.checkIn()" );
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
        String requestURL = new String( request.getRequestURL() );
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

        // ハピホテ環境であれば
        if ( requestURL.indexOf( URL_MATCHING ) != -1 )
        {
            hhcookie.setDomain( ".happyhotel.jp" );
        }
        else
        {
            hhcookie.setDomain( "121.101.88.177" );
        }
        hhcookie.setMaxAge( Integer.MAX_VALUE );

        // ログイン成功
        response.addCookie( hhcookie );

        // クッキー("u"=)作成
        UserLogin login = new UserLogin();
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
        // ログイン成功
        response.addCookie( hhcookie );
    }

}
