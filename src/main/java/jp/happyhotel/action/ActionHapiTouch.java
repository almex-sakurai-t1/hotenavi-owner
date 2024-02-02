/*
 * @(#)HotelRoomPicture.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * ハピタッチ制御クラス
 */
package jp.happyhotel.action;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DecodeData;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelAuth;
import jp.happyhotel.others.CiError;
import jp.happyhotel.others.HapiTouch;
import jp.happyhotel.others.HapiTouchRsv;
import jp.happyhotel.others.Member;
import jp.happyhotel.others.RsvPlanData;
import jp.happyhotel.others.RsvPlanStart;
import jp.happyhotel.others.TouchHistory;

/**
 * レジャーホテルデモサーブレット<br>
 * 
 * @author S.Tashiro
 * @version 1.00 2010/11/17
 */
public class ActionHapiTouch extends BaseAction
{
    private static final String     METHOD_REGIST                  = "Regist";
    private static final String     METHOD_VISIT                   = "Visit";
    private static final String     METHOD_FIND                    = "Find";
    private static final String     METHOD_HOTEL_INFO              = "HotelInfo";
    private static final String     METHOD_MODIFY_CI               = "ModifyCi";
    private static final String     METHOD_CORRECT_CI              = "CorrectCi";
    private static final String     METHOD_STATUS_CI               = "StatusCi";
    private static final String     METHOD_GET_CONFIG              = "GetConfig";
    private static final String     METHOD_SYNC_CI                 = "SyncCi";
    private static final String     METHOD_SYNC_CI_BySeq           = "SyncCiBySeq";
    private static final String     METHOD_VISIT_CANCEL            = "VisitCancel";
    private static final String     METHOD_USE_ALL_POINTS          = "UseAllPoints";
    private static final String     METHOD_POINT_CANCEL            = "PointCancel";
    private static final String     METHOD_HT_FRONT_TOUCH          = "HtFrontTouch";        // タッチ端末フロントタッチ受付

    private static final String     METHOD_RSV_DATA                = "RsvData";
    private static final String     METHOD_RSV_DATA_DETAIL         = "RsvDataDetail";
    private static final String     METHOD_GET_RSV_ROOM            = "GetRsvRoom";
    private static final String     METHOD_RSV_FIX                 = "RsvFix";
    private static final String     METHOD_RSV_CANCEL              = "RsvCancel";
    private static final String     METHOD_GET_AVAILABLE_ROOMS     = "GetAvailableRooms";
    private static final String     METHOD_RSV_MODIFY_ARRIVAL_TIME = "RsvModifyArrivalTime";
    private static final String     METHOD_RSV_UNDO_CANCEL         = "RsvUndoCancel";
    private static final String     METHOD_RSV_ROOM_CHANGE         = "RsvRoomChange";
    private static final String     METHOD_RSV_UNDO_FIX            = "RsvUndoFix";
    private static final String     METHOD_RSV_DAILY_COUNT         = "RsvDailyCount";
    private static final String     METHOD_RSV_LIST                = "RsvList";
    private static final String     METHOD_RSV_PLAN_DATA           = "RsvPlanData";
    private static final String     METHOD_RSV_PLAN_START          = "RsvPlanStart";
    private static final String     METHOD_GET_RSV_CONFIG          = "GetRsvConfig";
    private static final String     METHOD_GET_MEMBER              = "GetMember";
    private static final String     METHOD_GET_MEMBER_LIST         = "GetMemberList";
    private static final String     METHOD_MEMBER_CANCEL           = "MemberCancel";

    // ハピホテアプリからのタッチ
    private static final String     METHOD_HT_CHECK_IN             = "HtCheckIn";           // タッチ端末チェックイン
    private static final String     METHOD_HT_CHECK_IN_TOUCH       = "HtCheckInTouch";      // タッチ端末チェックイン（タッチ後）
    private static final String     METHOD_HT_HOME                 = "HtHome";              // タッチ後ホーム画面
    private static final String     METHOD_HT_USE_MILE             = "HtUseMile";           // タッチ端末マイル使用
    private static final String     METHOD_HT_RSV_FIX              = "HtRsvFix";            // タッチ端末予約確定
    private static final String     METHOD_HT_PRINT_COUPON         = "HtPrintCoupon";       // タッチ端末クーポン印字
    private static final String     METHOD_HT_DISCOUNT_COUPON      = "HtDiscountCoupon";    // タッチ端末割引クーポン
    private static final String     METHOD_HT_USE_COUPON           = "HtUseCoupon";         // タッチPCクーポン使用
    private static final String     METHOD_HT_CANCEL_COUPON        = "HtCancelCoupon";      // タッチPCクーポンキャンセル
    private static final String     METHOD_HT_MEMBER_REUSE         = "HtMemberReuse";       // タッチ端末メンバー再利用
    private static final String     METHOD_HT_MEMBER_CHANGE        = "HtMemberChange";      // タッチ端末メンバー変更
    private static final String     METHOD_HT_INQUIRY_FORM         = "HtInquiryForm";       // タッチ端末問合せフォーム
    private static final String     METHOD_HT_COUPON               = "HtCoupon";            // タッチ端末クーポン取得
    private static final String     METHOD_HT_REGIST_CUSTOM        = "HtRegistCustom";      // タッチ端末新規メンバー登録
    private static final String     METHOD_HT_ACCEPT_INFO          = "HtAcceptInfo";        // タッチ端末メンバーカード受付状況
    private static final String     METHOD_HT_OVERWRITE_CUSTOM     = "HtOverwriteCustom";   // タッチ端末メンバー情報登録
    private static final String     METHOD_HT_DISP_COUPON          = "HtDispCoupon";        // タッチPCクーポン表示
    private static final String     METHOD_HT_NONAUTO              = "HtNonAuto";           // タッチ後部屋番号選択待ち

    // チェックイン未処理データ対応
    private static final String     METHOD_GET_CI_ERROR            = "GetCiError";
    private static final String     METHOD_REFLECT_CI              = "ReflectCi";

    private static final String     MESSAGE_DENY                   = "認証に失敗しました。\n";
    private static final String     MESSAGE_NO_METHOD              = "メソッドが正しくありません。\n";

    private int                     hotelId                        = 0;
    private HapiTouch               hapiTouch                      = null;
    private HapiTouchRsv            hapiTouchRsv                   = null;
    private ActionHtCheckIn         htCheckIn                      = null;                  // ハピホテタッチ
    private ActionHtCheckInTouch    htCheckInTouch                 = null;                  // ハピホテタッチ（タッチ後）
    private ActionHtUseMile         htUseMile                      = null;                  // マイル使用
    private ActionHtRsvFix          htRsvFix                       = null;                  // 予約来店
    private ActionHtPrintCoupon     htPrintCoupon                  = null;                  // 印字クーポン
    private ActionHtDiscountCoupon  htDiscountCoupon               = null;                  // 割引クーポン
    private ActionHtUseCoupon       htUseCoupon                    = null;                  // 申告制クーポン使用
    private ActionHtCancelCoupon    htCancelCoupon                 = null;                  // 申告制クーポン取消
    private ActionHtMemberReuse     htMemberReuse                  = null;                  // 顧客再利用
    private ActionHtMemberChange    htMemberChange                 = null;                  // 顧客変更
    private ActionHtInquiryForm     htInquiryForm                  = null;                  // 問合せフォーム
    private ActionHtCoupon          htCoupon                       = null;                  // クーポン
    private ActionHtRegistCustom    htRegistCustom                 = null;                  // 顧客新規登録
    private ActionHtAcceptInfo      htAcceptInfo                   = null;                  // メンバーカード受付状況
    private ActionHtOverwriteCustom htOverwriteCustom              = null;                  // 顧客登録
    private ActionHtHome            htHome                         = null;                  // タッチ後ホーム画面
    private ActionHtDispCoupon      htDisp                         = null;                  // タッチPC　クーポン表示
    private ActionHtNonAuto         htNonAuto                      = null;                  // タッチ後部屋番号選択待ち
    private int                     errorCode                      = 0;
    private TouchHistory            touchHistory                   = null;
    private RsvPlanData             rsvPlanData                    = null;
    private RsvPlanStart            rsvPlanStart                   = null;
    private Member                  member                         = null;
    private CiError                 ciError                        = null;

    private RequestDispatcher       requestDispatcher              = null;
    final String                    userAgent                      = "HappyHotel";

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int kind = 0;
        // 定義
        boolean retAuth;
        boolean vpnFlag = false; // VPN接続フラグ
        boolean roomDispFlag = true; // 部屋情報取得フラグ

        String paramIdm;
        String paramMethod;
        String paramLicenceKey;
        String paramMacAddr;
        String paramCode;
        String paramPrice;
        String paramPoint;
        String root;
        String paramSlipNo;
        String paramRoomNo;
        String paramEmployeeCode;
        String paramSeq;
        String paramRegistType;
        String configFilePath = "";
        String paramError;
        int frontTouchLimit = 10 * 60 * 1000;

        FileInputStream propfile = null;
        Properties config;

        retAuth = false;
        paramIdm = request.getParameter( "idm" );
        paramMethod = request.getParameter( "method" );
        paramLicenceKey = request.getParameter( "key" );
        paramMacAddr = request.getParameter( "uuid" );
        paramCode = request.getParameter( "code" );
        paramPrice = request.getParameter( "price" );
        paramPoint = request.getParameter( "point" );
        paramSlipNo = request.getParameter( "slipNo" );
        paramRoomNo = request.getParameter( "roomNo" );
        paramEmployeeCode = request.getParameter( "employeeCode" );
        paramSeq = request.getParameter( "seq" );
        paramRegistType = request.getParameter( "registType" );
        paramError = request.getParameter( "error" );

        // ファイル取得して、設定情報を取得
        try
        {
            // OSの種類でコンフィグファイルのパスを変える
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                configFilePath = "C:\\ALMEX\\WWW\\WEB-INF\\hapitouch.conf";
            }
            else
            {
                configFilePath = "//etc//happyhotel//hapitouch.conf";
            }

            propfile = new FileInputStream( configFilePath );
            config = new Properties();
            config.load( propfile );

            // VPN接続フラグを取得
            vpnFlag = Boolean.valueOf( config.getProperty( "vpn" ) );
            // 部屋表示フラグを取得
            roomDispFlag = Boolean.valueOf( config.getProperty( "dispRoomFlag" ) );

            // フロントタッチ端末有効の制限時間
            frontTouchLimit = Integer.valueOf( config.getProperty( "frontTouchLimit" ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch.init()]Exception:" + e.toString() );
            vpnFlag = false;
            roomDispFlag = true;
        }

        // Logging.info( "[ActionHapiTouch.execute()]:接続元ホスト" + request.getRemoteHost() );
        // Logging.info( "[ActionHapiTouch.execute()]:接続元IPアドレス" + request.getRemoteAddr() );

        hapiTouch = new HapiTouch();
        hapiTouchRsv = new HapiTouchRsv();
        touchHistory = new TouchHistory();

        root = "";
        // パラメータチェック
        if ( (paramIdm == null) || (paramIdm.compareTo( "" ) == 0) )
        {
            paramIdm = "";
        }
        if ( paramMethod == null )
        {
            paramMethod = "";
        }
        if ( (paramLicenceKey == null) || (paramLicenceKey.compareTo( "" ) == 0) )
        {
            paramLicenceKey = "";
        }
        if ( (paramMacAddr == null) || (paramMacAddr.compareTo( "" ) == 0) )
        {
            paramMacAddr = "";
        }
        if ( (paramPrice == null) || (paramPrice.compareTo( "" ) == 0) || CheckString.numCheck( paramPrice ) == false )
        {
            paramPrice = "0";
        }
        if ( (paramPoint == null) || (paramPoint.compareTo( "" ) == 0) || CheckString.numCheck( paramPoint ) == false )
        {
            paramPoint = "0";
        }
        if ( (paramCode == null) || (paramCode.compareTo( "" ) == 0) )
        {
            paramCode = "";
        }
        if ( (paramSlipNo == null) || (paramSlipNo.compareTo( "" ) == 0) || CheckString.numCheck( paramSlipNo ) == false )
        {
            paramSlipNo = "0";
        }
        if ( paramRoomNo == null )
        {
            paramRoomNo = "";
        }
        if ( (paramEmployeeCode == null) || (paramEmployeeCode.compareTo( "" ) == 0) || CheckString.numCheck( paramEmployeeCode ) == false )
        {
            paramEmployeeCode = "0";
        }
        if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
        {
            paramSeq = "0";
        }
        if ( (paramRegistType == null) || (paramRegistType.compareTo( "" ) == 0) || CheckString.numCheck( paramRegistType ) == false )
        {
            paramRegistType = "0";
        }
        if ( (paramError == null) || (paramError.compareTo( "" ) == 0) || CheckString.numCheck( paramError ) == false )
        {
            paramError = "0";
        }

        // ホテルタッチ履歴書込み
        if ( request.getQueryString() != null )
        {
            if ( request.getQueryString().indexOf( "method=HotelInfo" ) == -1 && request.getQueryString().indexOf( "method=RsvData&" ) == -1 && request.getQueryString().indexOf( "method=GetCiError" ) == -1 ) // HotelInfo 及び RsvData以外をログに落とす
            {
                // ホテルID取得
                String paramId = request.getParameter( "id" );
                int id = 0;
                if ( paramId == null || (paramId.compareTo( "" ) == 0) )
                {
                    paramId = request.getParameter( "h_id" );// HtOverwriteCustomのとき
                }
                if ( paramId == null || (paramId.compareTo( "" ) == 0) )
                {
                    // 認証
                    retAuth = this.authLicence( paramLicenceKey, paramMacAddr, paramCode, vpnFlag );
                    if ( retAuth == false )
                    {
                        hotelId = 0;
                    }
                    id = hotelId;
                }
                else
                {
                    id = Integer.parseInt( paramId );
                    // 不正アクセス防止
                    try
                    {
                        if ( request.getHeader( "user-agent" ).indexOf( userAgent ) == -1 && UserAgent.getUserAgentTypeFromTouch( request ) == UserAgent.USERAGENT_SMARTPHONE )
                        {
                            // 不正アクセスの為画面遷移
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/index.jsp" );
                            requestDispatcher.forward( request, response );
                            return;
                        }
                        else if ( request.getHeader( "user-agent" ).indexOf( userAgent ) != -1 )
                        {
                            hotelId = id;
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[ActionApiWebService]Exception:" + e.toString() );
                    }
                }

                // ホテルIDがパラメータ、またはlicenekeyなどのパラメータから取得できれば、実行
                if ( id != 0 )
                {
                    touchHistory.touchHistory( id, request );
                }
            }
        }

        // 紐付け処理
        if ( paramMethod.compareTo( METHOD_REGIST ) == 0 )
        {
            hapiTouch.registUserFelica( request, response );
            return;
        }
        // 以下ハピホテタッチ端末開発に伴う修正
        /*-----------------------------------------------*/
        // ハピホテタッチチェックイン
        else if ( paramMethod.compareTo( METHOD_HT_CHECK_IN ) == 0 )
        {
            htCheckIn = new ActionHtCheckIn();
            htCheckIn.execute( request, response );
            return;
        }
        // ハピホテタッチチェックイン（タッチ後）
        else if ( paramMethod.compareTo( METHOD_HT_CHECK_IN_TOUCH ) == 0 )
        {
            htCheckInTouch = new ActionHtCheckInTouch();
            htCheckInTouch.execute( request, response );
            return;
        }
        // ハピホテタッチマイル使用
        else if ( paramMethod.compareTo( METHOD_HT_USE_MILE ) == 0 )
        {
            htUseMile = new ActionHtUseMile();
            htUseMile.execute( request, response );
            return;
        }
        // ハピホテタッチ予約来店要求
        else if ( paramMethod.compareTo( METHOD_HT_RSV_FIX ) == 0 )
        {
            htRsvFix = new ActionHtRsvFix();
            htRsvFix.execute( request, response );
            return;
        }
        // ハピホテタッチクーポン印字要求
        else if ( paramMethod.compareTo( METHOD_HT_PRINT_COUPON ) == 0 )
        {
            htPrintCoupon = new ActionHtPrintCoupon();
            htPrintCoupon.execute( request, response );
            return;
        }
        // ハピホテタッチクーポン割引要求
        else if ( paramMethod.compareTo( METHOD_HT_DISCOUNT_COUPON ) == 0 )
        {
            htDiscountCoupon = new ActionHtDiscountCoupon();
            htDiscountCoupon.execute( request, response );
            return;
        }
        // 問合せフォーム
        else if ( paramMethod.compareTo( METHOD_HT_INQUIRY_FORM ) == 0 )
        {
            htInquiryForm = new ActionHtInquiryForm();
            htInquiryForm.execute( request, response );
            return;
        }
        // クーポンリスト
        else if ( paramMethod.compareTo( METHOD_HT_COUPON ) == 0 )
        {
            htCoupon = new ActionHtCoupon();
            htCoupon.execute( request, response );
            return;
        } // 顧客新規登録
        else if ( paramMethod.compareTo( METHOD_HT_REGIST_CUSTOM ) == 0 )
        {
            htRegistCustom = new ActionHtRegistCustom();
            htRegistCustom.execute( request, response );
            return;
        }
        // 顧客カード受付確認
        else if ( paramMethod.compareTo( METHOD_HT_OVERWRITE_CUSTOM ) == 0 )
        {
            htOverwriteCustom = new ActionHtOverwriteCustom();
            htOverwriteCustom.execute( request, response );
            return;
        }
        // 顧客情報登録
        else if ( paramMethod.compareTo( METHOD_HT_ACCEPT_INFO ) == 0 )
        {
            htAcceptInfo = new ActionHtAcceptInfo();
            htAcceptInfo.execute( request, response );
            return;
        }
        else if ( paramMethod.compareTo( METHOD_HT_HOME ) == 0 )
        {
            htHome = new ActionHtHome();
            htHome.execute( request, response );
            return;
        }
        else if ( paramMethod.compareTo( METHOD_HT_NONAUTO ) == 0 )
        {
            htNonAuto = new ActionHtNonAuto();
            htNonAuto.execute( request, response );
            return;
        }
        // 以上ハピホテタッチ端末開発に伴う修正
        /*-----------------------------------------------*/

        // 認証が必要な機能については以下から
        else
        {

            // ライセンスキーとUUIDの紐付け、及びホテル情報を取得する
            if ( paramMethod.compareTo( METHOD_HOTEL_INFO ) == 0 )
            {
                if ( vpnFlag != false )
                {
                    // 認証
                    retAuth = this.authLicence( paramLicenceKey, paramMacAddr, paramCode, vpnFlag );
                    hapiTouch.getHotelInfo( paramLicenceKey, paramMacAddr, request, response, retAuth );
                }
                else
                {
                    hapiTouch.getHotelInfo( paramLicenceKey, paramMacAddr, request, response, false );
                }
                return;
            }

            // 認証
            retAuth = this.authLicence( paramLicenceKey, paramMacAddr, paramCode, vpnFlag );

            if ( retAuth == false )
            {
                errorCode = 0;
            }
        }

        request.setAttribute( "HOTEL_ID", Integer.toString( hotelId ) );

        // IDm情報取得
        if ( paramMethod.compareTo( "" ) == 0 || paramMethod.compareTo( METHOD_FIND ) == 0 )
        {
            root = METHOD_FIND;
            if ( retAuth != false )
            {
                hapiTouch.findData( paramIdm, hotelId, request, response );
                return;
            }
        }
        // 来店時
        else if ( paramMethod.compareTo( METHOD_VISIT ) == 0 )
        {
            root = METHOD_VISIT;
            kind = this.checkTouchKind( request );
            // if ( Integer.parseInt( paramRegistType ) == 0 )
            // {
            // if ( retAuth != false )
            // {
            // hapiTouch.visitData( paramIdm, hotelId, Integer.parseInt( paramEmployeeCode ), response );
            // return;
            // }
            // }
            // else
            // {
            if ( retAuth != false )
            {
                hapiTouch.visitData( paramIdm, hotelId, Integer.parseInt( paramEmployeeCode ), Integer.parseInt( paramRegistType ), kind, request, response );
                return;
            }
            // }
        }
        // チェックインデータ変更
        else if ( paramMethod.compareTo( METHOD_MODIFY_CI ) == 0 && Integer.parseInt( paramSeq ) > 0 )
        {
            root = METHOD_MODIFY_CI;

            if ( retAuth != false )
            {
                hapiTouch.modifyCi( hotelId, request, response );
                return;
            }
        }
        // ステータス確認
        else if ( paramMethod.compareTo( METHOD_STATUS_CI ) == 0 && Integer.parseInt( paramSeq ) > 0 )
        {
            root = METHOD_STATUS_CI;
            if ( retAuth != false )
            {
                hapiTouch.statusCi( hotelId, Integer.parseInt( paramSeq ), response );
                return;
            }
        }
        // コンフィグ取得
        else if ( paramMethod.compareTo( METHOD_GET_CONFIG ) == 0 )
        {
            root = METHOD_GET_CONFIG;
            if ( retAuth != false )
            {
                hapiTouch.getConfig( hotelId, roomDispFlag, response );
                return;
            }
        }
        // チェックインデータ訂正
        else if ( paramMethod.compareTo( METHOD_CORRECT_CI ) == 0 )
        {
            root = METHOD_CORRECT_CI;
            if ( retAuth != false )
            {
                hapiTouch.correctCi( hotelId, request, response );
                return;
            }
        }
        // チェックインデータ同期
        else if ( paramMethod.compareTo( METHOD_SYNC_CI ) == 0 )
        {
            root = METHOD_SYNC_CI;
            if ( retAuth != false )
            {
                hapiTouch.syncCi( hotelId, request, response );
                return;
            }
        }
        // チェックインデータ同期(1データ)
        else if ( paramMethod.compareTo( METHOD_SYNC_CI_BySeq ) == 0 )
        {
            root = METHOD_SYNC_CI_BySeq;
            if ( retAuth != false )
            {
                hapiTouch.syncCiBySeq( hotelId, request, response );
                return;
            }
        }
        // 来店取消
        else if ( paramMethod.compareTo( METHOD_VISIT_CANCEL ) == 0 )
        {
            root = METHOD_VISIT_CANCEL;
            if ( retAuth != false )
            {
                hapiTouch.visitCancel( hotelId, request, response );
                return;
            }
        }

        // ポイント取消
        else if ( paramMethod.compareTo( METHOD_POINT_CANCEL ) == 0 )
        {
            root = METHOD_POINT_CANCEL;
            if ( retAuth != false )
            {
                hapiTouch.pointCancel( hotelId, request, response );
                return;
            }
        }

        // 全額使用
        else if ( paramMethod.compareTo( METHOD_USE_ALL_POINTS ) == 0 )
        {
            root = METHOD_USE_ALL_POINTS;
            if ( retAuth != false )
            {
                hapiTouch.visitCancel( hotelId, request, response );
                return;
            }
        }
        // 予約データ取得
        else if ( paramMethod.compareTo( METHOD_RSV_DATA ) == 0 )
        {
            root = METHOD_RSV_DATA;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvData( hotelId, request, response );
                return;
            }
        }
        // 予約詳細データ取得
        else if ( paramMethod.compareTo( METHOD_RSV_DATA_DETAIL ) == 0 )
        {
            root = METHOD_RSV_DATA_DETAIL;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvDataDetail( hotelId, request, response );
                return;
            }
        }
        // 予約リスト取得
        else if ( paramMethod.compareTo( METHOD_RSV_LIST ) == 0 )
        {
            root = METHOD_RSV_LIST;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvList( hotelId, request, response );
                return;
            }
        }
        // 予約部屋番号取得
        else if ( paramMethod.compareTo( METHOD_GET_RSV_ROOM ) == 0 )
        {
            root = METHOD_GET_RSV_ROOM;
            if ( retAuth != false )
            {
                hapiTouchRsv.getRsvRoom( hotelId, request, response );
                return;
            }
        }
        // 予約来店確定
        else if ( paramMethod.compareTo( METHOD_RSV_FIX ) == 0 )
        {
            root = METHOD_RSV_FIX;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvFix( hotelId, request, response );
                return;
            }
        }
        // 予約キャンセル
        else if ( paramMethod.compareTo( METHOD_RSV_CANCEL ) == 0 )
        {
            root = METHOD_RSV_CANCEL;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvCancel( hotelId, request, response );
                return;
            }
        }
        // 利用可能部屋
        else if ( paramMethod.compareTo( METHOD_GET_AVAILABLE_ROOMS ) == 0 )
        {
            root = METHOD_GET_AVAILABLE_ROOMS;
            if ( retAuth != false )
            {
                hapiTouch.getAvailableRooms( response );
                return;
            }
        }
        // 到着時刻変更要求
        else if ( paramMethod.compareTo( METHOD_RSV_MODIFY_ARRIVAL_TIME ) == 0 )
        {
            root = METHOD_RSV_MODIFY_ARRIVAL_TIME;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvModifyArrvialTime( hotelId, request, response );
                return;
            }
        }
        // キャンセル取り消し
        else if ( paramMethod.compareTo( METHOD_RSV_UNDO_CANCEL ) == 0 )
        {
            root = METHOD_RSV_UNDO_CANCEL;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvUndoCancel( hotelId, request, response );
                return;
            }
        }
        // 部屋変更
        else if ( paramMethod.compareTo( METHOD_RSV_ROOM_CHANGE ) == 0 )
        {
            root = METHOD_RSV_ROOM_CHANGE;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvRoomChange( hotelId, request, response );
                return;
            }
        }
        // 来店取消
        else if ( paramMethod.compareTo( METHOD_RSV_UNDO_FIX ) == 0 )
        {
            root = METHOD_RSV_UNDO_FIX;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvUndoFix( hotelId, request, response );
                return;
            }
        }
        // 日別予約件数
        else if ( paramMethod.compareTo( METHOD_RSV_DAILY_COUNT ) == 0 )
        {
            root = METHOD_RSV_DAILY_COUNT;
            if ( retAuth != false )
            {
                hapiTouchRsv.rsvDailyCount( hotelId, request, response );
                return;
            }
        }
        // 予約設定
        else if ( paramMethod.compareTo( METHOD_GET_RSV_CONFIG ) == 0 )
        {
            root = METHOD_GET_RSV_CONFIG;
            if ( retAuth != false )
            {
                hapiTouchRsv.getRsvConfig( hotelId, response );
                return;
            }
        }
        // ▽ハピホテタッチ端末開発用▽
        /*-----------------------------------------------*/
        // ハピホテタッチクーポン使用要求(ハピホテタッチ)
        else if ( paramMethod.compareTo( METHOD_HT_USE_COUPON ) == 0 )
        {
            root = METHOD_HT_USE_COUPON;
            if ( retAuth != false )
            {
                htUseCoupon = new ActionHtUseCoupon();
                htUseCoupon.execute( request, response );
            }
            return;
        }
        // ハピホテタッチクーポン取消要求
        else if ( paramMethod.compareTo( METHOD_HT_CANCEL_COUPON ) == 0 )
        {
            root = METHOD_HT_CANCEL_COUPON;
            if ( retAuth != false )
            {
                htCancelCoupon = new ActionHtCancelCoupon();
                htCancelCoupon.execute( request, response );
            }
            return;
        }
        // ハピホテタッチメンバー再利用通知
        else if ( paramMethod.compareTo( METHOD_HT_MEMBER_REUSE ) == 0 )
        {
            root = METHOD_HT_MEMBER_REUSE;
            if ( retAuth != false )
            {
                htMemberReuse = new ActionHtMemberReuse();
                htMemberReuse.execute( request, response );
                return;
            }
        }
        // ハピホテタッチメンバー変更通知
        else if ( paramMethod.compareTo( METHOD_HT_MEMBER_CHANGE ) == 0 )
        {
            root = METHOD_HT_MEMBER_CHANGE;
            if ( retAuth != false )
            {
                htMemberChange = new ActionHtMemberChange();
                htMemberChange.execute( request, response );
                return;
            }
        }

        // ハピホテタッチクーポン表示
        else if ( paramMethod.compareTo( METHOD_HT_DISP_COUPON ) == 0 )
        {
            root = METHOD_HT_DISP_COUPON;
            if ( retAuth != false )
            {
                htDisp = new ActionHtDispCoupon();
                htDisp.execute( request, response );
            }
            return;
        }

        // ハピホテタッチフロント端末許可要求
        else if ( paramMethod.compareTo( METHOD_HT_FRONT_TOUCH ) == 0 )
        {
            root = METHOD_HT_FRONT_TOUCH;
            if ( retAuth != false )
            {
                hapiTouch.htFrontTouch( hotelId, frontTouchLimit, response );

                return;
            }
        }

        // △ハピホテタッチ端末開発用△
        /*-----------------------------------------------*/
        // 予約PMS連携用
        else if ( paramMethod.compareTo( METHOD_RSV_PLAN_DATA ) == 0 )
        {
            root = METHOD_RSV_PLAN_DATA;
            if ( retAuth != false )
            {
                rsvPlanData = new RsvPlanData();
                rsvPlanData.rsvPlanData( hotelId, request, response );
                return;
            }
        }
        // 予約PMS連携用予約プランスタート
        else if ( paramMethod.compareTo( METHOD_RSV_PLAN_START ) == 0 )
        {
            root = METHOD_RSV_PLAN_START;
            if ( retAuth != false )
            {
                rsvPlanStart = new RsvPlanStart();
                rsvPlanStart.rsvPlanStart( hotelId, request, response );
                return;
            }
        }
        // カードレスメンバー情報取得
        else if ( paramMethod.compareTo( METHOD_GET_MEMBER ) == 0 )
        {
            root = METHOD_GET_MEMBER;
            if ( retAuth != false )
            {
                member = new Member();
                member.getMember( hotelId, request, response );
                return;
            }
        }
        // カードレスメンバー一覧情報取得
        else if ( paramMethod.compareTo( METHOD_GET_MEMBER_LIST ) == 0 )
        {
            root = METHOD_GET_MEMBER_LIST;
            if ( retAuth != false )
            {
                member = new Member();
                member.getMemberList( hotelId, request, response );
                return;
            }
        }
        // カードレスメンバーリンク解除
        else if ( paramMethod.compareTo( METHOD_MEMBER_CANCEL ) == 0 )
        {
            root = METHOD_MEMBER_CANCEL;
            if ( retAuth != false )
            {
                member = new Member();
                member.memberCancel( hotelId, request, response );
                return;
            }
        }
        // チェックインエラーデータ取得
        else if ( paramMethod.compareTo( METHOD_GET_CI_ERROR ) == 0 )
        {
            root = METHOD_GET_CI_ERROR;
            ciError = new CiError();
            ciError.getCiError( hotelId, response );
            return;
        }
        // チェックインエラーデータ反映
        else if ( paramMethod.compareTo( METHOD_REFLECT_CI ) == 0 )
        {
            root = METHOD_REFLECT_CI;
            ciError = new CiError();
            ciError.reflectCi( hotelId, request, response );
            return;
        }

        /*-----------------------------------------------*/
        else
        {
            hapiTouch.errorData( root, MESSAGE_NO_METHOD, response );
            return;
        }
        hapiTouch.errorData( root, MESSAGE_DENY, response );
    }

    /**
     * ライセンスキー認証
     * 
     * @param licenceKey ライセンスキー
     * @param macAddr UUID
     * @param code 暗号化コード
     * @return
     */
    private boolean authLicence(String licenceKey, String macAddr, String code, boolean vpnFlag)
    {
        boolean retTimeAuth = false;
        boolean retAuth = false;

        // VPN接続の場合、ライセンスキーが有効であれば、認証OKとみなす
        if ( vpnFlag != false )
        {
            // ライセンスキーからホテル情報を取得
            DataHotelAuth dha = new DataHotelAuth();
            dha.getData( licenceKey );
            // ホテルIDが取得でき、認証済みのステータスだったらOK
            if ( (dha.getId() > 0) && (dha.getRegistStatus() == 1) )
            {
                hotelId = dha.getId();
                retAuth = true;
            }
        }
        else
        {

            // 暗号化文字列の復号処理
            if ( code.compareTo( "" ) != 0 )
            {
                // 暗号キー
                byte[] key = "axpol ptmhyeeahl".getBytes();

                // 暗号ベクター（Initialization Vector：初期化ベクトル）
                byte[] ivBytes = "s h t t i s n h ".getBytes();

                String strDecode = DecodeData.decode( key, ivBytes, code );
                retTimeAuth = this.decodeData( strDecode );
            }

            // ライセンスキーからのホテル認証
            if ( (licenceKey.compareTo( "" ) != 0) && (macAddr.compareTo( "" ) != 0) )
            {
                // ライセンスキー、MACアドレスで認証
                hotelId = hapiTouch.authMacAddr( licenceKey, macAddr );
            }
            // 暗号化文字列の値とライセンスキーでのホテル認証がOKだったら先の処理をさせる
            if ( retTimeAuth != false && hotelId > 0 )
            {
                retAuth = true;
            }
            else
            {
                retAuth = false;
            }
        }
        return retAuth;
    }

    /**
     * 復号化処理
     * 
     * @param decodeWord 暗号化文字列
     * @return 処理結果(true,false)
     **/
    public boolean decodeData(String decodeWord)
    {
        boolean ret = false;
        String strDate = "";
        String strTime = "";

        if ( decodeWord != null )
        {
            // 抜き出した文字列がnullではなく、既定の文字数あるかどうか
            if ( decodeWord.length() == 17 )
            {
                // 日付の抜出
                strDate = decodeWord.substring( 0, 8 );
                // 時刻の抜出
                strTime = decodeWord.substring( 9, 15 );

                // 日付比較メソッドへ
                ret = this.compareDate( Integer.parseInt( strDate ), Integer.parseInt( strTime ) );
            }
            else
            {
                ret = false;
            }
        }
        else
        {
            ret = false;
        }

        return(ret);
    }

    /***
     * 日付、時刻からサーバとの時間差確認処理
     * 
     * @param strDate 日付
     * @param strTime 時刻
     * @return 処理結果（true、false）
     * @see 差が5分以内だったらtrue、それ以外ならfalse
     */
    public boolean compareDate(int strDate, int strTime)
    {
        boolean ret = false;
        boolean retBefore = false;
        boolean retAfter = false;
        int nResult = 0;
        int nYear = 0;
        int nMonth = 0;
        int nDate = 0;
        int nHour = 0;
        int nMinute = 0;
        int nSecond = 0;
        Calendar cal1 = Calendar.getInstance();// 比較時間
        Calendar cal2 = Calendar.getInstance(); // 現在の時間

        // 日付の値セット
        nYear = strDate / 10000;
        nMonth = strDate % 10000 / 100;
        nDate = strDate % 100;

        // 時刻のセット
        nHour = strTime / 10000;
        nMinute = strTime % 10000 / 100;
        nSecond = strTime % 100;

        // 日付を比較する
        cal1.clear();
        cal1.set( nYear, nMonth - 1, nDate, nHour, nMinute, nSecond );
        // 5分後のカレンダーを取得
        cal2.add( Calendar.MINUTE, 5 );

        // 比較　カレンダー2（5分後のカレンダー） > カレンダー1（リクエストされたカレンダー）
        nResult = cal2.compareTo( cal1 );

        // cal2の方が大きかったらtrue
        if ( nResult >= 0 )
        {
            retAfter = true;
        }

        // 5分前のカレンダーを取得(5分進めたため倍の数値で引く)
        cal2.add( Calendar.MINUTE, -10 );

        // 比較　カレンダー2（5分前のカレンダー） < カレンダー1（リクエストされたカレンダー）
        nResult = cal2.compareTo( cal1 );
        if ( nResult <= 0 )
        {
            retBefore = true;
        }

        // 両方とも正しければtrue
        if ( (retBefore != false) && (retAfter != false) )
        {
            ret = true;
        }

        return(ret);
    }

    /***
     * タッチ時筐体チェック
     * 
     * @param request
     * @return
     */
    public int checkTouchKind(HttpServletRequest request)
    {

        int kind = 0;
        String code = request.getParameter( "code" );
        String registType = request.getParameter( "registType" );

        // ハピホテタッチアクセス時に必ず付与されるパラメータがない
        if ( code == null )
        {
            // 精算機タッチ時に付与されるパラメータがない
            if ( registType == null )
            {
                kind = 2;
            }
            else
            {
                kind = 1;
            }
        }

        return kind;
    }
}
