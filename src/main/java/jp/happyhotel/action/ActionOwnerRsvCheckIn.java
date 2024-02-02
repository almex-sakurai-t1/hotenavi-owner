package jp.happyhotel.action;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.EncodeData;
import jp.happyhotel.common.GMOCcsCredit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvCredit;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataRsvRoom;
import jp.happyhotel.data.DataRsvSpid;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveInitPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserPointPay;

/**
 * 
 * 予約確定／来店確認／キャンセル画面 Action Class
 */

public class ActionOwnerRsvCheckIn extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        // ログイン者情報
        String loginTel = "";
        int paramHotelId = 0;
        String paramRsvNo = "";
        int paramRsvDate = 0;
        int paramCheckAgree = 0;
        int paramBtn = 0; // 押されたボタン
        int paramStatus = 0;
        int paramWorkId = 0;
        int paramPlanId = 0;
        int paramSeq = 0;
        int paramOrgRsvDate = 0;
        int paramNoshow = 0;
        int offerKind = 0;
        int paramCancelCheck = 0;
        int paramAdult = 0;
        int paramChild = 0;
        int paramMan = 0;
        int paramWoman = 0;
        String paramHapihoteuserId = "";
        String paramMode = "";
        String paramHapihoteId = "";
        String paramHapihoteMail = "";
        String paramHapihoteTel = "";
        String paramCardNo = "";
        String paramDispCardNo = "";
        String paramCreditUpdateFlag = "";
        int paramExpireMonth = 0;
        int paramExpireYear = 0;
        String url = "";
        String paramUserKbn = "";
        String errMsg = "";
        String[] paramHapihoteMailList = null;
        ArrayList<String> hapihoteMailList = new ArrayList<String>();
        int ownerUserId = 0;
        boolean isLoginUser = false;
        FormReserveSheetPC frmSheetPC;
        FormReservePersonalInfoPC frmInfo;
        DataRsvPlan dataPlan = new DataRsvPlan();
        DataLoginInfo_M2 dataLoginInfo_M2 = null;
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        DataRsvReserveBasic drrb = new DataRsvReserveBasic();
        DataRsvReserve dhrr = new DataRsvReserve();

        String checkHotelId = "";

        try
        {
            // お気に入りの登録されて呼び出された場合は、受け渡し項目はnullとなる
            // ホテルIDが、nullの場合はエラーページへ遷移させる
            checkHotelId = request.getParameter( "selHotelId" );

            if ( (checkHotelId == null) )
            {
                errMsg = Message.getMessage( "erro.30008" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            frmSheetPC = new FormReserveSheetPC();

            // 画面のパラメータ取得
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelId" ) );
            paramRsvNo = request.getParameter( "rsvNo" );
            paramRsvDate = Integer.parseInt( request.getParameter( "rsvDate" ) );
            paramStatus = Integer.parseInt( request.getParameter( "status" ) );
            if ( (request.getParameter( "check" ) != null) && (request.getParameter( "check" ).trim().length() != 0) )
            {
                paramCheckAgree = 1;
            }
            paramWorkId = Integer.parseInt( request.getParameter( "workId" ) );
            paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );
            paramSeq = Integer.parseInt( request.getParameter( "seq" ) );
            paramMode = request.getParameter( "mode" );
            paramOrgRsvDate = Integer.parseInt( request.getParameter( "orgRsvDate" ) );
            if ( (request.getParameter( "noShow" ) != null) && (request.getParameter( "noShow" ).trim().length() != 0) )
            {
                paramNoshow = Integer.parseInt( request.getParameter( "noShow" ) );
            }
            paramUserKbn = request.getParameter( "userKbn" );
            if ( (request.getParameter( "cancelCheck" ) != null) && (request.getParameter( "cancelCheck" ).trim().length() != 0) )
            {
                paramCancelCheck = Integer.parseInt( request.getParameter( "cancelCheck" ) );
            }
            paramHapihoteId = request.getParameter( "hapihoteUserId" );
            paramHapihoteMail = request.getParameter( "hapihoteAddr" );
            paramHapihoteMailList = request.getParameterValues( "hapihoteAddr" );
            paramHapihoteTel = request.getParameter( "hapihoteTel" );
            paramHapihoteuserId = request.getParameter( "hapihoteUserId" );
            paramAdult = Integer.parseInt( request.getParameter( "adult" ) );
            paramChild = Integer.parseInt( request.getParameter( "child" ) );
            if ( (request.getParameter( "man" ) != null) && (request.getParameter( "man" ).trim().length() != 0) )
            {
                paramMan = Integer.parseInt( request.getParameter( "man" ) );
            }
            else
            {
                paramMan = 0;
            }
            if ( (request.getParameter( "woman" ) != null) && (request.getParameter( "woman" ).trim().length() != 0) )
            {
                paramWoman = Integer.parseInt( request.getParameter( "woman" ) );
            }
            else
            {
                paramWoman = 0;
            }
            if ( request.getParameter( "cardno" ) != null )
            {
                paramCardNo = request.getParameter( "cardno" );
            }
            if ( request.getParameter( "dispcardno" ) != null )
            {
                paramDispCardNo = request.getParameter( "dispcardno" );
            }
            if ( request.getParameter( "expiremonth" ) != null )
            {
                paramExpireMonth = Integer.parseInt( request.getParameter( "expiremonth" ) );
            }
            if ( request.getParameter( "expireyear" ) != null )
            {
                paramExpireYear = Integer.parseInt( request.getParameter( "expireyear" ) );
            }
            if ( request.getParameter( "creditUpdateFlag" ) != null )
            {
                paramCreditUpdateFlag = request.getParameter( "creditUpdateFlag" );
            }

            // メールアドレスリスト
            if ( paramHapihoteMailList != null )
            {
                for( int l = 0 ; l < paramHapihoteMailList.length ; l++ )
                {
                    hapihoteMailList.add( paramHapihoteMailList[l] );
                }
            }
            paramHapihoteTel = request.getParameter( "hapihoteTel" );

            // ログインユーザー情報取得
            if ( paramUserKbn.equals( ReserveCommon.USER_KBN_USER ) )
            {
                // ユーザーの場合
                dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

                if ( dataLoginInfo_M2 == null )
                {
                    // ログアウト状態の場合はエラー
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_nomember.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }

                if ( dataLoginInfo_M2.isMemberFlag() == true )
                {
                    // 存在する
                    isLoginUser = true;
                }
            }
            else if ( paramUserKbn.equals( ReserveCommon.USER_KBN_OWNER ) )
            {
                // オーナーの場合
                ownerUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
                if ( ownerUserId != 0 )
                {
                    // 存在する
                    isLoginUser = true;
                }
                hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
                logicMenu.setFrm( new FormOwnerBkoMenu() );
                disptype = logicMenu.getUserAuth( hotenaviId, ownerUserId );
                adminflag = logicMenu.getAdminFlg( hotenaviId, ownerUserId );
                imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, ownerUserId );
            }

            if ( (isLoginUser == false) || ((!paramUserKbn.equals( ReserveCommon.USER_KBN_USER )) && (!paramUserKbn.equals( ReserveCommon.USER_KBN_OWNER ))) )
            {
                // ログインユーザーが存在しない場合はエラー
                errMsg = Message.getMessage( "erro.30004" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // 仮オープンかどうかを取得する
            drrb.getData( paramHotelId );

            frmSheetPC.setSelHotelId( paramHotelId );
            frmSheetPC.setRsvNo( paramRsvNo );
            frmSheetPC.setRsvDate( paramRsvDate );
            frmSheetPC.setAgree( paramCheckAgree );
            frmSheetPC.setUserId( paramHapihoteuserId );
            frmSheetPC.setMail( paramHapihoteMail );
            frmSheetPC.setLoginUserTel( loginTel );
            frmSheetPC.setWorkId( paramWorkId );
            frmSheetPC.setSelPlanId( paramPlanId );
            frmSheetPC.setSeq( paramSeq );
            frmSheetPC.setOrgRsvSeq( paramSeq );
            frmSheetPC.setMode( paramMode );
            frmSheetPC.setNoShow( paramNoshow );
            frmSheetPC.setUserKbn( paramUserKbn );
            frmSheetPC.setStatus( paramStatus );
            frmSheetPC.setCancelCheck( paramCancelCheck );
            frmSheetPC.setLoginUserId( paramHapihoteId );
            frmSheetPC.setLoginUserTel( paramHapihoteTel );
            frmSheetPC.setLoginUserMail( paramHapihoteMail );
            frmSheetPC.setTermKind( ReserveCommon.TERM_KIND_PC );
            frmSheetPC.setMailList( hapihoteMailList );
            frmSheetPC.setCardno( paramCardNo );
            frmSheetPC.setDispcardno( paramDispCardNo );
            frmSheetPC.setExpireMonth( paramExpireMonth );
            frmSheetPC.setExpireYear( paramExpireYear );
            if ( paramCreditUpdateFlag.equals( "true" ) )
            {
                frmSheetPC.setCreditUpdateFlag( true );
            }
            else
            {
                frmSheetPC.setCreditUpdateFlag( false );
            }

            // ユーザの場合に、パラメータとマスタチェックを実施する
            // キャンセルはパラメータチェックをしない
            if ( paramUserKbn.equals( ReserveCommon.USER_KBN_USER ) && (paramMode.equals( ReserveCommon.MODE_CANCEL ) == false) )
            {
                // パラメータの整合性チェックを実施
                ReserveCommon rsvCmm = new ReserveCommon();

                errMsg = rsvCmm.checkParam( paramHotelId, paramPlanId, paramSeq, paramRsvDate, paramUserKbn, paramMode );
                if ( errMsg.trim().length() != 0 )
                {
                    // 仮オープンだったら何もしない
                    if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                    {
                        // チェックNGの場合、エラーページへ遷移する。
                        request.setAttribute( "errMsg", errMsg );
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                        requestDispatcher.forward( request, response );
                        return;
                    }
                }

                // 予約人数の範囲チェック
                errMsg = rsvCmm.checkAdultChildNum( paramHotelId, paramPlanId, paramAdult, paramChild );
                if ( errMsg.trim().length() != 0 )
                {
                    // チェックNGの場合、エラーページへ遷移する。
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }

                // 予約確定時に、マスタチェックを実施
                if ( request.getParameter( "btnRsvFix" ) != null )
                {
                    FormReserveSheetPC frmSheetPC_Check = new FormReserveSheetPC();
                    frmSheetPC_Check = rsvCmm.chkDspMaster( frmSheetPC );
                    if ( frmSheetPC_Check.getErrMsg().trim().length() != 0 )
                    {
                        // 仮オープンじゃないとエラー
                        if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                        {
                            // チェックNGの場合、エラーページへ遷移する。
                            request.setAttribute( "errMsg", frmSheetPC_Check.getErrMsg() );
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                            requestDispatcher.forward( request, response );
                            return;
                        }
                    }
                    frmInfo = new FormReservePersonalInfoPC();
                    frmInfo.setSelHotelID( paramHotelId );
                    frmInfo.setSelPlanID( paramPlanId );
                    frmInfo.setSelSeq( paramSeq );
                    frmInfo.setSelRsvDate( paramRsvDate );
                    frmInfo.setOrgReserveDate( paramOrgRsvDate );
                    frmInfo.setLoginUserId( paramHapihoteId );
                    frmInfo.setLoginMailAddr( paramHapihoteMail );
                    frmInfo.setLoginMailAddrList( hapihoteMailList );
                    frmInfo.setLoginTel( paramHapihoteTel );
                    if ( dataLoginInfo_M2 != null )
                    {
                        frmInfo.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
                    }
                    else
                    {
                        frmInfo.setPaymemberFlg( false );
                    }
                    frmInfo = rsvCmm.getPlanData( frmInfo );

                    int curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int curTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    int checkDateST = DateEdit.addDay( curDate, (frmInfo.getRsvEndDate()) );
                    int checkDateED = DateEdit.addDay( curDate, (frmInfo.getRsvStartDate()) );
                    int checkDatePremiumED = DateEdit.addDay( curDate, (frmInfo.getRsvStartDatePremium()) );
                    int checkDatePremiumST = DateEdit.addDay( curDate, (frmInfo.getRsvEndDatePremium()) );

                    // 予約可能かチェック
                    if ( (paramRsvDate > checkDateST && paramRsvDate < checkDateED &&
                            paramRsvDate >= frmInfo.getSalesStartDay() && paramRsvDate <= frmInfo.getSalesEndDay()) ||
                            (paramRsvDate == checkDateST && curTime <= frmInfo.getRsvEndTime() &&
                                    paramRsvDate >= frmInfo.getSalesStartDay() && paramRsvDate <= frmInfo.getSalesEndDay()) ||
                            (paramRsvDate == checkDateED && curTime >= frmInfo.getRsvStartTime() &&
                                    paramRsvDate >= frmInfo.getSalesStartDay() && paramRsvDate <= frmInfo.getSalesEndDay()) )
                    {
                    }
                    else
                    {
                        if ( ((paramRsvDate > checkDatePremiumST) && (paramRsvDate < checkDatePremiumED) &&
                                (paramRsvDate >= frmInfo.getSalesStartDay()) && (paramRsvDate <= frmInfo.getSalesEndDay())) ||
                                (paramRsvDate == checkDatePremiumST && curTime <= frmInfo.getRsvEndTime()) ||
                                (paramRsvDate == checkDatePremiumED && curTime >= frmInfo.getRsvStartTime()) )
                        {
                            // プレミアム会員が有効の場合は表示を変更
                            String err1 = "本プランはハピホテプレミアム会員様のみ予約可能です。";
                            errMsg = err1;
                        }
                        else
                        {
                            // 仮オープンだったら何もしない
                            if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                            {

                                errMsg = "予約受付の期間外です。お手数ですが、もう一度最初からやり直してください。";
                            }
                        } // エラー情報が入っている場合のみ
                        if ( errMsg.equals( "" ) == false )
                        {
                            // チェックNGの場合、エラーページへ遷移する。
                            request.setAttribute( "errMsg", errMsg );
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                            requestDispatcher.forward( request, response );
                            return;
                        }
                    }

                }
            }

            // ホテルの提供区分取得
            dataPlan.getData( paramHotelId, paramPlanId );
            offerKind = dataPlan.getOfferKind();
            frmSheetPC.setOfferKind( offerKind );

            if ( paramUserKbn.equals( ReserveCommon.USER_KBN_OWNER ) == true && (request.getParameter( "btnCancel" ) != null || request.getParameter( "btnRaiten" ) != null || request.getParameter( "btnRsvFix" ) != null ||
                    request.getParameter( "btnBack" ) != null || request.getParameter( "btnUndoCancel" ) != null) &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // 押されたボタンによって処理を振り分ける。
            if ( request.getParameter( "btnCancel" ) != null )
            {
                if ( paramUserKbn.equals( ReserveCommon.USER_KBN_USER ) )
                {
                    frmSheetPC.setCancelKind( ReserveCommon.CANCEL_USER );
                }

                // ▼キャンセル
                frmSheetPC = execCancel( frmSheetPC, paramStatus );
                frmSheetPC.setMail( paramHapihoteMail );
                frmSheetPC.setMailList( hapihoteMailList );
                url = "/reserve/reserve_sheet_PC.jsp";
                request.setAttribute( "FORM_ReserveSheetPC", frmSheetPC );
            }
            else if ( request.getParameter( "btnRaiten" ) != null )
            {
                // ▼来店確認
                paramSeq = 0;
                if ( (request.getParameter( "roomNo" ) != null) && (request.getParameter( "roomNo" ).trim().length() != 0) )
                {
                    paramSeq = Integer.parseInt( request.getParameter( "roomNo" ) );
                }
                frmSheetPC.setSeq( paramSeq );
                // TODO
                frmSheetPC = execRaiten( frmSheetPC, paramStatus );
                // エラーあり時
                if ( frmSheetPC.getErrMsg().trim().length() != 0 )
                {
                    frmSheetPC.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );
                    frmSheetPC.setMode( ReserveCommon.MODE_RAITEN );
                    frmSheetPC.setMail( paramHapihoteMail );
                    frmSheetPC.setMailList( hapihoteMailList );
                }
                else
                {
                    // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                    frmSheetPC.setMode( "" );

                    // ここからハピホテタッチのデータ作成

                    // チェックインデータを作成する
                    int nowPoint = 0;
                    int ciCode = 0;
                    boolean ret = false;
                    boolean updateCi = false;
                    UserPointPay upp = new UserPointPay();
                    HotelCi hc = new HotelCi();
                    nowPoint = upp.getNowPoint( frmSheetPC.getUserId(), false );

                    // 未確定のデータが24時間以内にあったら登録しない。
                    ret = hc.getCheckInBeforeData( frmSheetPC.getSelHotelId(), frmSheetPC.getUserId() );
                    if ( ret == false )
                    {
                        // チェックインデータ登録
                        hc = hc.registCiData( frmSheetPC.getUserId(), frmSheetPC.getSelHotelId() );
                        ciCode = hc.getHotelCi().getSeq();
                        if ( ciCode > 0 )
                        {
                            ret = true;
                        }
                        else
                        {
                            ret = false;
                        }
                    }
                    else
                    {
                        ciCode = hc.getHotelCi().getSeq();
                        ret = false;
                    }

                    // 部屋名が登録済みであれば枝番追加で挿入、登録されていなければ更新
                    if ( hc.getHotelCi().getRoomNo().compareTo( "" ) != 0 )
                    {
                        updateCi = false;
                    }
                    else
                    {
                        updateCi = true;
                    }
                    hc.getHotelCi().setRoomNo( Integer.toString( paramSeq ) );
                    // 予約番号をセット
                    hc.getHotelCi().setRsvNo( paramRsvNo );

                    UserBasicInfo ubi = new UserBasicInfo();
                    int extUserFlag = 0;
                    if ( ubi.isLvjUser( frmSheetPC.getUserId() ) )
                    {
                        extUserFlag = 1;
                    }
                    hc.getHotelCi().setExtUserFlag( extUserFlag );

                    // 予約入力時使用マイルをセット
                    if ( dhrr.getData( frmSheetPC.getSelHotelId(), paramRsvNo ) )
                    {
                        hc.getHotelCi().setUsePoint( dhrr.getUsedMile() );
                        hc.getHotelCi().setUseDate( dhrr.getAcceptDate() );
                        hc.getHotelCi().setUseTime( dhrr.getAcceptTime() );
                    }

                    // 予約の利用料金をセットしておく
                    // hc.getHotelCi().setAmount( frm.getChargeTotal() + frm.getOptionChargeTotal() );

                    // チェックインデータを更新またはインサート
                    if ( updateCi != false )
                    {
                        // 更新
                        ret = hc.getHotelCi().updateData( frmSheetPC.getSelHotelId(), hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
                    }
                    else
                    {
                        // falseならば枝番号を追加する必要がある
                        if ( ret == false )
                        {
                            hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                        }
                        ret = hc.getHotelCi().insertData();
                    }

                }
                url = "/reserve/reserve_sheet_PC.jsp";
                request.setAttribute( "FORM_ReserveSheetPC", frmSheetPC );
            }
            else if ( request.getParameter( "btnRsvFix" ) != null )
            {
                // ▼予約登録・更新
                frmSheetPC.setUserAgent( request.getHeader( "user-agent" ) );
                frmSheetPC = execRsvFix( frmSheetPC );
                if ( frmSheetPC.isCreditAuthorityNgFlag() == true )
                {
                    // クレジット認証しているホテルでＮＧだった場合のみクレジット番号入力画面へ戻す
                    url = "/reserve/reserve_cancel_credit_info.jsp";
                    frmInfo = new FormReservePersonalInfoPC();
                    frmInfo.setSelHotelID( paramHotelId );
                    frmInfo.setSelSeq( paramSeq );
                    frmInfo.setSelPlanID( paramPlanId );
                    frmInfo.setSelRsvDate( paramRsvDate );
                    frmInfo.setMode( paramMode );
                    frmInfo.setWorkId( paramWorkId );
                    frmInfo.setOrgReserveDate( paramOrgRsvDate );
                    frmInfo.setSelNumAdult( paramAdult );
                    frmInfo.setSelNumChild( paramChild );
                    frmInfo.setSelNumMan( paramMan );
                    frmInfo.setSelNumWoman( paramWoman );

                    frmInfo = execBack( frmInfo );
                    frmInfo.setLoginUserId( paramHapihoteId );
                    frmInfo.setLoginTel( paramHapihoteTel );
                    frmInfo.setLoginMailAddr( paramHapihoteMail );
                    frmInfo.setLoginMailAddrList( hapihoteMailList );
                    frmInfo.setLoginUserKbn( paramUserKbn );
                    // さっきのエラーをのせる
                    frmInfo.setErrMsg( frmSheetPC.getErrMsg() );
                    request.setAttribute( "dsp", frmInfo );
                }
                else
                {
                    frmSheetPC.setOrgRsvDate( paramOrgRsvDate );
                    frmSheetPC.setMail( paramHapihoteMail );
                    frmSheetPC.setMailList( hapihoteMailList );
                    frmSheetPC.setLoginUserId( paramHapihoteId );
                    frmSheetPC.setLoginUserMail( paramHapihoteMail );
                    frmSheetPC.setLoginUserTel( paramHapihoteTel );
                    frmSheetPC.setUserKbn( paramUserKbn );
                    frmSheetPC.setSeq( paramSeq );
                    frmSheetPC.setOfferKind( offerKind );
                    url = "/reserve/reserve_jump_sheet.jsp";
                    request.setAttribute( "FORM_ReserveSheetPC", frmSheetPC );
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // ▼戻る
                frmInfo = new FormReservePersonalInfoPC();
                frmInfo.setSelHotelID( paramHotelId );
                frmInfo.setSelSeq( paramSeq );
                frmInfo.setSelPlanID( paramPlanId );
                frmInfo.setSelRsvDate( paramRsvDate );
                frmInfo.setMode( paramMode );
                frmInfo.setWorkId( paramWorkId );
                frmInfo.setOrgReserveDate( paramOrgRsvDate );

                frmInfo = execBack( frmInfo );

                // ログインユーザー(予約登録ユーザー)情報取得
                frmInfo.setLoginUserId( paramHapihoteId );
                frmInfo.setLoginTel( paramHapihoteTel );
                frmInfo.setLoginMailAddr( paramHapihoteMail );
                frmInfo.setLoginMailAddrList( hapihoteMailList );
                frmInfo.setLoginUserKbn( paramUserKbn );
                if ( ReserveCommon.checkNoShowCreditHotel( paramHotelId ) && ((paramMode.equals( ReserveCommon.MODE_INS ) || paramCreditUpdateFlag.equals( "true" ) == true)) )
                {
                    frmInfo.setCardno( paramCardNo );
                    frmInfo.setExpiremonth( paramExpireMonth );
                    frmInfo.setExpireyear( paramExpireYear );
                    url = "/reserve/reserve_cancel_credit_info.jsp";
                }
                else
                {
                    url = "/reserve/reserve_personal_info.jsp";
                }
                request.setAttribute( "dsp", frmInfo );
            }
            // キャンセルの取り消し処理の動作
            else if ( request.getParameter( "btnUndoCancel" ) != null )
            {
                frmSheetPC = execUndoCancel( frmSheetPC, frmSheetPC.getStatus() );

                url = "/reserve/reserve_sheet_PC.jsp";
                request.setAttribute( "FORM_ReserveSheetPC", frmSheetPC );
            }

            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execute() ][hotelId = "
                    + paramHotelId + ",reserveNo = " + paramRsvNo + ",paramBtn = " + paramBtn + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( IncompatibleClassChangeError eee )
            {
                Logging.info( eee.toString() );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvCheckIn.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * キャンセルボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    private FormReserveSheetPC execCancel(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            // 新規登録時以外既存データのステータスのチェック
            if ( rsvCmm.checkStatus( frm.getSelHotelId(), frm.getRsvNo(), status ) == false )
            {
                errMsg = Message.getMessage( "warn.00019" );
                frm.setErrMsg( errMsg );
                return(frm);
            }

            // 予約キャンセルチェックがされているか
            if ( frm.getCancelCheck() == 0 )
            {
                // 登録失敗
                errMsg = Message.getMessage( "warn.00032" );

                // 画面内容再取得
                // 予約データ抽出
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( frm.getMode() );

                return(frm);
            }

            // ワークテーブルに登録されている通常オプション情報取得
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // データ更新（キャンセル処理）
            logic.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // 新予約
            {
                ret = logic.execRsvCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );
            }
            else
            {
                ret = logic.execRsvCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_OLDRSV );
            }
            if ( ret == false )
            {
                // 登録失敗
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // 画面内容再取得
                // 予約データ抽出
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // 登録データの取得
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
            frm.setMode( "" );
            frm.setStatus( ReserveCommon.RSV_STATUS_CANCEL );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execCancel() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * キャンセル取消ボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    public FormReserveSheetPC execUndoCancel(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {

            // ワークテーブルに登録されている通常オプション情報取得
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // データ更新（キャンセル取り消し処理）
            logic.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // 新予約
            {
                ret = logic.execRsvUndoCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );
            }
            else
            {
                ret = logic.execRsvUndoCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_OLDRSV );
            }
            if ( ret == false )
            {
                // 登録失敗
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // 画面内容再取得
                // 予約データ抽出
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // 登録データの取得
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
            frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execCancel() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * 来店確認ボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    private FormReserveSheetPC execRaiten(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        int addPoint = 0;
        int reflectDate = 0;
        int rsvDate = 0;
        int arrivalTime = 0;
        int addBonusMile = 0;
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            // 新規登録時以外既存データのステータスのチェック
            if ( rsvCmm.checkStatus( frm.getSelHotelId(), frm.getRsvNo(), status ) == false )
            {
                errMsg = Message.getMessage( "warn.00020" );
                frm.setErrMsg( errMsg );
                frm = getRaitenRegistData( frm );
                return(frm);
            }

            // 部屋の未選択チェック
            if ( frm.getSeq() == 0 )
            {
                errMsg = Message.getMessage( "warn.00002", "チェックインする部屋番号" );
                frm.setErrMsg( errMsg );
                frm = getRaitenRegistData( frm );
                return(frm);
            }

            // ポイントデータの設定
            addPoint = logicCheckIn.getRsvPointData( frm.getRsvNo(), 1 );
            rsvDate = logicCheckIn.getRsvPointData( frm.getRsvNo(), 2 );
            arrivalTime = logicCheckIn.getRsvPointData( frm.getRsvNo(), 3 );
            addBonusMile = logicCheckIn.getRsvPointData( frm.getRsvNo(), 4 );
            frm.setAddPoint( addPoint );
            frm.setAddBonusMile( addBonusMile );

            // 反映日の設定
            reflectDate = getReflectDate( rsvDate, arrivalTime );
            frm.setReflectDate( reflectDate );

            // データ更新（来店確認）
            logicCheckIn.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // 新予約
            {
                ret = logicCheckIn.execRaiten( frm.getSelHotelId(), frm.getRsvNo(), frm.getRsvDate(), frm.getRoomNo(), ReserveCommon.SCHEMA_NEWRSV );
            }
            else
            {
                ret = logicCheckIn.execRaiten( frm.getSelHotelId(), frm.getRsvNo(), frm.getRsvDate(), frm.getRoomNo(), ReserveCommon.SCHEMA_OLDRSV );
            }

            if ( ret == false )
            {
                // 登録失敗
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // 画面内容再取得
                // 予約データ抽出
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );

                return(frm);
            }

            // 処理完了メッセージ
            switch( status )
            {
                case 1:
                    // 受付
                    frm.setProcMsg( Message.getMessage( "warn.00024" ) );
                    break;
                case 2:
                    // 利用済み
                    if ( frm.getMode().equals( ReserveCommon.MODE_RAITEN ) )
                    {
                        // 来店確認完了
                        frm.setProcMsg( Message.getMessage( "warn.00025" ) );
                    }
                    else
                    {
                        // 利用期限が過ぎている場合
                        frm.setProcMsg( Message.getMessage( "warn.00026" ) );
                    }
                    break;
            }

            // 登録データの取得
            frm = getRaitenRegistData( frm );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execRaiten() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execRaiten() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * 有料ユーザポイント一時データの反映日取得
     * 
     * @param int rsvDate 予約日
     * @param int arrivalTime 到着予定時刻
     * @return int 反映日
     * @throws Exception
     */
    private int getReflectDate(int rsvDate, int arrivalTime) throws Exception
    {
        int retDate = 0;
        int limitFlg = 0;
        int range = 0;
        String year = "";
        String month = "";
        String day = "";
        String rsvYear = "";
        String rsvMonth = "";
        String rsvDay = "";
        String rsvHour = "";
        String rsvMinutes = "";
        String rsvSecond = "";
        String arrivalTimeStr = "";
        Calendar calendar = Calendar.getInstance();

        // ポイント管理マスタからデータ取得
        limitFlg = OwnerRsvCommon.getInitHapyPoint( 3 );
        range = OwnerRsvCommon.getInitHapyPoint( 4 );

        // 日付設定
        rsvYear = Integer.toString( rsvDate ).substring( 0, 4 );
        rsvMonth = Integer.toString( rsvDate ).substring( 4, 6 );
        rsvDay = Integer.toString( rsvDate ).substring( 6, 8 );

        arrivalTimeStr = ConvertTime.convTimeStr( arrivalTime, 0 );
        rsvHour = arrivalTimeStr.substring( 0, 2 );
        rsvMinutes = arrivalTimeStr.substring( 2, 4 );
        rsvSecond = arrivalTimeStr.substring( 4 );
        calendar.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ),
                Integer.parseInt( rsvHour ), Integer.parseInt( rsvMinutes ), Integer.parseInt( rsvSecond ) );

        switch( limitFlg )
        {
            case OwnerRsvCommon.LIMIT_FLG_TIME:
                // 時間加算
                calendar.add( Calendar.HOUR, range );
                break;

            case OwnerRsvCommon.LIMIT_FLG_DAY:
                // 日付加算
                calendar.add( Calendar.DATE, range );
                break;

            case OwnerRsvCommon.LIMIT_FLG_MONTH:
                // 月加算
                calendar.add( Calendar.MONTH, range );
                break;
        }

        year = Integer.toString( calendar.get( Calendar.YEAR ) );
        month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
        day = String.format( "%1$02d", calendar.get( Calendar.DATE ) );

        retDate = Integer.parseInt( year + month + day );

        return(retDate);
    }

    /**
     * 来店確認時の登録データ取得処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws Exception
     */
    private FormReserveSheetPC getRaitenRegistData(FormReserveSheetPC frm) throws Exception
    {
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();

        // 登録データの取得
        logicSheet.setFrm( frm );
        logicSheet.getData( 2 );

        frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
        frm.setStatus( ReserveCommon.RSV_STATUS_ZUMI );
        frm.setMode( ReserveCommon.MODE_RAITEN );

        return(frm);
    }

    /**
     * 予約確定ボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    private FormReserveSheetPC execRsvFix(FormReserveSheetPC frm) throws Exception
    {
        String errMsg = "";
        String mode = "";
        String paramRsvNo = "";
        int reminderFlg = 0;
        LogicReservePersonalInfoPC logicPC = new LogicReservePersonalInfoPC();
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        FormReservePersonalInfoPC frmPC = new FormReservePersonalInfoPC();
        ReserveCommon rsvcomm = new ReserveCommon();
        boolean noshowCreditFlag = false;
        GMOCcsCredit gmoccs = null;
        DataRsvSpid dataspid = null;

        try
        {
            paramRsvNo = frm.getRsvNo();

            // ▼エラーなし時、確定処理実行
            // 同意チェックがされているか
            if ( frm.getAgree() == 0 )
            {
                errMsg = errMsg + Message.getMessage( "warn.00010" ) + "<br />";
                logicPC.setFrmSheet( frm );
                logicPC.getReserveWorkData( frm.getWorkId() );
                frm = logicPC.getFrmSheet();
                frm.setRsvNo( paramRsvNo );
                frm.setErrMsg( errMsg );

                // オプションワークテーブルからオプションデータを取得
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_IMP );
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

                return(frm);
            }

            noshowCreditFlag = rsvcomm.checkNoShowCreditHotel( frm.getSelHotelId() );

            // 処理実行
            // ノーショークレジット対象ホテルならクレジット情報チェックする
            if ( noshowCreditFlag && (frm.getMode().equals( ReserveCommon.MODE_INS ) || frm.isCreditUpdateFlag() == true) )
            {
                gmoccs = new GMOCcsCredit();
                dataspid = new DataRsvSpid();
                dataspid.getDataByHotelid( frm.getSelHotelId() );
                // カード番号
                gmoccs.setCardNo( frm.getCardno() );
                // 有効期限
                gmoccs.setCardExpire( String.valueOf( (frm.getExpireYear() * 100 + frm.getExpireMonth()) ) );
                // SPID
                gmoccs.setSpid( dataspid.getSpid() );
                // 1円セット
                gmoccs.setAmount( 1 );
                // 1円オーソリ
                if ( gmoccs.execAuthority() == false )
                {
                    errMsg = "このクレジットカードは、ご利用できません。<br />申し訳ありませんが再度入力お願いします。<br />";
                    logicPC.setFrmSheet( frm );
                    logicPC.getReserveWorkData( frm.getWorkId() );
                    frm = logicPC.getFrmSheet();
                    frm.setRsvNo( paramRsvNo );
                    frm.setErrMsg( errMsg );
                    frm.setCreditAuthorityNgFlag( true );

                    // オプションワークテーブルからオプションデータを取得
                    frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_IMP );
                    frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_USUAL );
                    return(frm);
                }
                else
                {
                    // オーソリ成功
                    frm.setCreditAuthorityNgFlag( false );
                }
            }

            // リマインダーフラグ取得
            reminderFlg = logicCheckIn.getWorkReminderFlg( frm.getWorkId() );
            frm.setReminder( reminderFlg );

            // ワークテーブルに登録されている通常オプション情報取得
            frm = logicCheckIn.getWorkOptionQuantity( frm );

            logicCheckIn.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // 新予約
            {
                if ( frm.getMode().equals( ReserveCommon.MODE_INS ) )
                {
                    // 新規登録
                    logicCheckIn.insReserve( frm.getUserAgent(), ReserveCommon.SCHEMA_NEWRSV );
                }
                else if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) )
                {
                    // 更新
                    logicCheckIn.updReserve( frm.getUserAgent(), ReserveCommon.SCHEMA_NEWRSV );
                }
            }
            else
            {
                if ( frm.getMode().equals( ReserveCommon.MODE_INS ) )
                {
                    // 新規登録
                    logicCheckIn.insReserve( frm.getUserAgent(), ReserveCommon.SCHEMA_OLDRSV );
                }
                else if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) )
                {
                    // 更新
                    logicCheckIn.updReserve( frm.getUserAgent(), ReserveCommon.SCHEMA_OLDRSV );
                }
            }

            if ( frm.getErrMsg().trim().length() != 0 )
            {
                // 登録失敗
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // 画面内容再取得
                frmPC.setSelHotelID( frm.getSelHotelId() );
                frmPC.setWorkId( frm.getWorkId() );
                frm = getRsvWorkData( frmPC );
                frm.setErrMsg( errMsg );
                frm.setMode( mode );

                // オプションワークテーブルから必須オプションデータを取得
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_IMP );
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

                return(frm);
            }
            // 登録成功

            if ( (noshowCreditFlag == true && frm.isCreditAuthorityNgFlag() == false && (frm.getMode().equals( ReserveCommon.MODE_INS ) || frm.isCreditUpdateFlag() == true))
                    || (noshowCreditFlag && (frm.getMode().equals( ReserveCommon.MODE_UPD ) && frm.isCreditUpdateFlag() == false)) )
            {
                byte[] key = "axpol ptmhyeeahl".getBytes();

                // 暗号ベクター（Initialization Vector：初期化ベクトル）
                byte[] ivBytes = "s h t t i s n h ".getBytes();

                String cardno = frm.getCardno();
                // 暗号化
                String encode = EncodeData.encodeString( key, ivBytes, cardno );

                // クレジットデータ登録
                DataRsvCredit datarsv = new DataRsvCredit();
                // クレジットデータ存在確認
                if ( datarsv.getData( frm.getRsvNo() ) )
                {
                    // 存在すればUPDATE
                    datarsv.setCard_no( "" );
                    datarsv.setLimit_date( (frm.getExpireYear() * 100 + frm.getExpireMonth()) );
                    datarsv.setLast_update( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    datarsv.setLast_uptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    if ( datarsv.updateData() == false )
                    {
                        // TODO：どうするか…
                    }
                }
                else
                {
                    datarsv.setReserve_no( frm.getRsvNo() );
                    datarsv.setCard_no( "" );
                    datarsv.setLimit_date( (frm.getExpireYear() * 100 + frm.getExpireMonth()) );
                    datarsv.setId( frm.getSelHotelId() );
                    datarsv.setLast_update( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    datarsv.setLast_uptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    datarsv.setDel_date( 0 );
                    datarsv.setDel_time( 0 );
                    datarsv.setDel_flag( 0 );
                    if ( datarsv.insertData() == false )
                    {
                        // TODO：どうするか…

                    }
                }

            }

            frm = logicCheckIn.getFrm();

            // 登録データの取得
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // ワークテーブルから対象データ削除
            logicPC.deleteRsvWork( frm.getWorkId() );
            logicPC.deleteRsvOptionWork( frm.getWorkId() );

            // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
            frm.setMode( "" );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execRsvFix() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execRsvFix() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * 戻るボタンクリック処理
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @throws
     */
    private FormReservePersonalInfoPC execBack(FormReservePersonalInfoPC frm) throws Exception
    {
        String week = "";
        String reserveDate = "";
        String reserveDateFormat = "";
        int inpMaxQuantity = 0;
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();
        ArrayList<FormReserveOptionSubImp> frmOptSumImpList = new ArrayList<FormReserveOptionSubImp>();
        ArrayList<Integer> selOptImpSubIdList = new ArrayList<Integer>();
        ArrayList<String> optSubRemarksList = new ArrayList<String>();

        try
        {
            // 日付の表示形式
            week = DateEdit.getWeekName( frm.getSelRsvDate() );
            reserveDate = Integer.toString( frm.getSelRsvDate() );
            reserveDateFormat = reserveDate.substring( 0, 4 ) + "年" + reserveDate.substring( 4, 6 ) + "月" + reserveDate.substring( 6, 8 ) + "日（" + week + "）";
            frm.setReserveDateFormat( reserveDateFormat );

            // ホテル基本情報取得
            frm = rsvCmm.getHotelData( frm );

            // 予約基本より駐車場利用区分、駐車場利用台数を取得
            frm = rsvCmm.getParking( frm );
            frm.setParkingUsedKbnInit( 0 );

            // プランマスタより各種情報を取得
            frm = rsvCmm.getPlanData( frm );
            frm.setSelNumAdult( 0 );
            frm.setSelNumChild( 0 );
            frm.setSelNumMan( 0 );
            frm.setSelNumWoman( 0 );

            // 別料金マスタよりチェックイン開始、終了時刻を取得
            frm = rsvCmm.getCiCoTime( frm );

            // 設備情報
            frm = getEquip( frm );

            // システム日付・時刻の取得
            frm.setCurrentDate( Integer.valueOf( DateEdit.getDate( 2 ) ) );

            // 駐車場情報取得
            DataRsvReserve rsvData = new DataRsvReserve();
            if ( !frm.getReserveNo().equals( "" ) )
            {
                rsvData.getData( frm.getSelHotelID(), frm.getReserveNo() );
                frm.setSelParkingCount( rsvData.getParkingCount() );
                frm.setSelHiRoofCount( rsvData.getParkingHiRoofCount() );
            }

            // カレンダー情報取得
            setCalenderInfo( frm, frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelSeq(), frm.getSelRsvDate() );

            // 必須オプション情報取得
            frmOptSumImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );
            frm.setFrmOptSubImpList( frmOptSumImpList );

            // 通常オプション情報取得
            frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelRsvDate() );
            for( int i = 0 ; i < frmOptSub.getUnitPriceList().size() ; i++ )
            {
                optSubRemarksList.add( "" );
            }
            frm.setSelOptSubRemarksList( optSubRemarksList );

            // ワークテーブルから対象データ取得
            logic.setFrm( frm );
            logic.getReserveWorkData_Back( frm.getWorkId() );
            frm = logic.getFrm();

            // 選択されている必須オプション情報をワークテーブルから取得
            selOptImpSubIdList = logic.getSelOptImpSubIdList( frm.getWorkId() );
            frm.setSelOptionImpSubIdList( selOptImpSubIdList );

            // 選択されている通常オプション情報をワークテーブルから取得
            int rsvQuantity = 0;
            int remaindQuantity = 0;
            ArrayList<Integer> newQuantityList = new ArrayList<Integer>();
            frm = logic.getSelOptSubList( frm.getWorkId(), frm );

            // 更新の場合のみオプション数量の再設定を行う。
            for( int i = 0 ; i < frm.getSelOptSubIdList().size() ; i++ )
            {
                // 残数取得
                remaindQuantity = rsvCmm.getRemaindOption( frm.getSelHotelID(), frm.getSelRsvDate(), frm.getSelOptSubIdList().get( i ) );

                if ( remaindQuantity == -1 )
                {
                    // 予約無し時は、1日の最大入力可能数を取得
                    inpMaxQuantity = rsvCmm.getInpMaxQuantity( frm.getSelHotelID(), frm.getSelOptSubIdList().get( i ) );
                    newQuantityList.add( inpMaxQuantity );
                    continue;
                }
                if ( frm.getMode().equals( ReserveCommon.MODE_INS ) )
                {
                    // 新規時
                    newQuantityList.add( remaindQuantity );
                }
                else if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) )
                {
                    // 更新時
                    // 不要な場合は、予約データに登録されている数量を取得
                    rsvQuantity = logic.getRsvOptionQuantity( frm.getSelHotelID(), frm.getSelOptSubIdList().get( i ), frm.getReserveNo() );

                    // 値の比較
                    if ( rsvQuantity >= remaindQuantity )
                    {
                        newQuantityList.add( rsvQuantity );
                    }
                    else
                    {
                        newQuantityList.add( remaindQuantity );
                    }
                }
            }
            frmOptSub.setMaxQuantityList( newQuantityList );
            frm.setFrmOptSub( frmOptSub );

            // 都道府県・市区町村情報取得
            frm = getPref( frm );

            frm.setLastMonth( DateEdit.addMonth( frm.getSelRsvDate(), -1 ) );
            frm.setNextMonth( DateEdit.addMonth( frm.getSelRsvDate(), 1 ) );
            frm.setSelCalYm( frm.getSelRsvDate() );

            // ワークテーブルから対象データ削除
            logic.deleteRsvWork( frm.getWorkId() );
            logic.deleteRsvOptionWork( frm.getWorkId() );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execBack() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execBack() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * 
     * 都道府県情報取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getPref(FormReservePersonalInfoPC frm) throws Exception
    {

        ArrayList<Integer> prefIdList = new ArrayList<Integer>();
        ArrayList<String> prefNMList = new ArrayList<String>();
        ArrayList<Integer> jisCdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();

        // 都道府県リスト取得
        prefIdList = rsvCmm.getPrefIdList();
        prefNMList = rsvCmm.getPrefNmList();

        // 市区町村リスト取得
        jisCdList = rsvCmm.getJisCdList( frm.getSelPrefId() );
        jisNMList = rsvCmm.getJisNmList( frm.getSelPrefId() );

        // フォームにセット
        frm.setPrefIdList( prefIdList );
        frm.setPrefNmList( prefNMList );
        frm.setJisCdList( jisCdList );
        frm.setJisNmList( jisNMList );

        return(frm);
    }

    /**
     * 
     * 設備情報取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getEquip(FormReservePersonalInfoPC frm) throws Exception
    {

        LogicReserveInitPC logic = new LogicReserveInitPC();
        DataRsvRoom drr = new DataRsvRoom();
        String roomRemarks = "";
        String roomPr = "";

        logic.setFrm( frm );

        if ( frm.getOfferKind() == 2 )
        {
            // 部屋提供
            drr.getData( frm.getSelHotelID(), frm.getSelSeq() );
            roomRemarks = drr.getRemarks();
            roomPr = drr.getRoomPr();
            logic.getEquip( frm.getSelHotelID(), frm.getSelSeq() );
            frm = logic.getFrm();
            frm.setRoomRemarks( roomRemarks );
            frm.setRoomPr( roomPr );

            return(frm);
        }

        // プラン提供
        roomRemarks = "";
        roomPr = "";
        logic.getEquipPlan( frm.getSelHotelID(), ReserveCommon.getRoomSeqList( frm.getSelHotelID(), frm.getSelPlanID() ) );

        frm.setRoomRemarks( roomRemarks );
        frm.setRoomPr( roomPr );

        return(frm);

    }

    /**
     * 
     * カレンダー情報
     * 
     * @param frm
     * @param selHotelId
     * @param planId
     * @param seq
     * @throws Exception
     */
    private void setCalenderInfo(FormReservePersonalInfoPC frm, int selHotelId, int planId, int seq, int rsvDate) throws Exception
    {
        LogicOwnerRsvManageCalendar logicCalendar;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        String year = "";
        String month = "";
        int checkDateST = 0;
        int checkDateED = 0;
        int dispCheckDateST = 0;
        int dispCheckDateED = 0;
        int curDate = 0;
        int curTime = 0;
        int minusDay = 0;
        int plusDay = 0;
        int checkDatePremiumST = 0;
        int checkDatePremiumED = 0;
        int checkDateFreeST = 0;
        int checkDateFreeED = 0;
        int checkDateFreeAddED = 0;
        int numchild = 0;
        ReserveCommon rsvCmm = new ReserveCommon();
        logicCalendar = new LogicOwnerRsvManageCalendar();

        try
        {
            // 子供人数制御(4号営業ホテルは強制的に0)
            if ( rsvCmm.checkLoveHotelFlag( frm.getSelHotelID() ) == false )
            {
                numchild = frm.getSelNumChild();
            }
            // カレンダー情報取得
            // 当日取得
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
            year = String.valueOf( rsvDate ).substring( 0, 4 );
            month = String.valueOf( rsvDate ).substring( 4, 6 );
            if ( seq != 0 )
            {
                monthlyList = logicCalendar.getCalendarData( selHotelId, planId, seq, Integer.parseInt( year + month ) );
            }
            else
            {
                monthlyList = logicCalendar.getCalendarData( selHotelId, planId, Integer.parseInt( year + month ) );
            }

            curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            curTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            if ( curTime > frm.getRsvEndTime() )
            {
                plusDay++;
            }
            if ( curTime < frm.getRsvStartTime() )
            {
                minusDay++;
            }
            checkDateST = DateEdit.addDay( curDate, frm.getRsvEndDate() + plusDay );
            checkDateED = DateEdit.addDay( curDate, frm.getRsvStartDate() - minusDay );

            // 販売開始期間が超えている場合は開始日を変える
            if ( checkDateST < frm.getSalesStartDay() )
            {
                dispCheckDateST = frm.getSalesStartDay();
            }
            else
            {
                dispCheckDateST = checkDateST;
            }
            // 販売終了期間を超えている場合は終了日をセット
            if ( checkDateED > frm.getSalesEndDay() )
            {
                dispCheckDateED = frm.getSalesEndDay();
            }
            else
            {
                dispCheckDateED = checkDateED;
            }

            frm.setRsvStartDayStr( String.format( "%1$04d年%2$02d月%3$02d日", dispCheckDateST / 10000, dispCheckDateST % 10000 / 100, dispCheckDateST % 10000 % 100 ) );
            frm.setRsvEndDayStr( String.format( "%1$04d年%2$02d月%3$02d日", dispCheckDateED / 10000, dispCheckDateED % 10000 / 100, dispCheckDateED % 10000 % 100 ) );

            checkDateFreeST = DateEdit.addDay( curDate, frm.getRsvEndDateFree() + plusDay );
            checkDateFreeED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay );
            checkDateFreeAddED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay + 1 );

            checkDatePremiumST = DateEdit.addDay( curDate, frm.getRsvEndDatePremium() + plusDay );
            checkDatePremiumED = DateEdit.addDay( curDate, frm.getRsvStartDatePremium() - minusDay );

            frm.setRsvPremiumEndDayStr( String.format( "%1$04d年%2$02d月%3$02d日", checkDatePremiumED / 10000, checkDatePremiumED % 10000 / 100, checkDatePremiumED % 10000 % 100 ) );

            for( int i = 0 ; i <= monthlyList.size() - 1 ; i++ )
            {
                ArrayList<FormOwnerRsvManageCalendar> oneList = monthlyList.get( i );
                for( int j = 0 ; j <= oneList.size() - 1 ; j++ )
                {
                    FormOwnerRsvManageCalendar frmMC = oneList.get( j );
                    String reserveCharge = rsvCmm.getReserveCharge( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelNumAdult(), numchild, frmMC.getCalDate() );
                    frmMC.setReserveChargeFormat( reserveCharge );
                    if ( frmMC.getVacancyRoomNum() != 0 )
                    {
                        if ( (frmMC.getCalDate() >= checkDateST) && (frmMC.getCalDate() <= checkDateED) &&
                                (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay()) )
                        {
                            // frmMC.setRsvJotaiMark( ReserveCommon.RSV_ON_MARK );
                            // 無料会員が閲覧できる条件か確認して見れない場合はプレミアム有効日フラグをたてる
                            if ( frmMC.getCalDate() < checkDateFreeST || frmMC.getCalDate() > checkDateFreeED )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                        }
                        else
                        {
                            // 有料会員が閲覧できる条件か確認して閲覧できる場合はプレミアム有効日フラグをたてる
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            if ( ((frmMC.getCalDate() >= checkDatePremiumST) && (frmMC.getCalDate() <= checkDatePremiumED) &&
                                    (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay())) )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                        }
                    }
                    else
                    {
                        if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) && (frmMC.getCalDate() == frm.getOrgReserveDate()) )
                        {
                            // 変更の場合、元の予約日は「○」にする。
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_ON_MARK );
                        }
                        else
                        {
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_OFF_MARK );
                            if ( frmMC.getSalesFlag() == 0 )
                            {
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                        if ( (frmMC.getCalDate() >= checkDateST) && (frmMC.getCalDate() <= checkDateED) &&
                                (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay()) )
                        {
                            // 無料会員が閲覧できる条件か確認して見れない場合はプレミアム有効日フラグをたてる
                            if ( frmMC.getCalDate() < checkDateFreeST || frmMC.getCalDate() > checkDateFreeED )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                            if ( frmMC.getRsvJotaiFlg() == 1 && frmMC.getSalesFlag() == 0 )
                            {
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                        else
                        {
                            // 有料会員が閲覧できる条件か確認して閲覧できる場合はプレミアム有効日フラグをたてる
                            if ( ((frmMC.getCalDate() >= checkDatePremiumST) && (frmMC.getCalDate() <= checkDatePremiumED) &&
                                    (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay())) )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                            else
                            {
                                // 期間外表示
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                    }

                }
            }

            // フォームにセット
            frm.setMonthlyList( monthlyList );
            // 有料会員限定の開始日、終了日を追加
            frm.setRsvPremiumStartDay( checkDatePremiumST );
            frm.setRsvPremiumEndDay( checkDatePremiumED );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveInitPC.setRsvManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setRsvManage] " + exception );
        }
    }

    /**
     * 
     * 予約仮データ取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReserveSheetPC オブジェクト
     */
    private FormReserveSheetPC getRsvWorkData(FormReservePersonalInfoPC frm) throws Exception
    {
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        // ▼フォームに必要な値をセット
        // ホテル基本情報の設定
        frm = rsvCmm.getHotelData( frm );

        // ワークテーブルからデータを取得
        frm.getWorkId();
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();
        frmSheet.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );
        frmSheet.setWorkId( frm.getWorkId() );

        return(frmSheet);
    }

}
