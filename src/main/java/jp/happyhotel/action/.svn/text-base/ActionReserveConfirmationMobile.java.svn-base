package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.EncodeData;
import jp.happyhotel.common.GMOCcsCredit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataRsvCredit;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvSpid;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;

/**
 * 
 * 申込確認画面 Action
 */

public class ActionReserveConfirmationMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;
    // エラーチェック種類
    private static final int  CHECKERR_INPUT    = 1;   // 入力チェックでエラー
    private static final int  CHECKERR_CRITICAL = 2;   // 致命的エラー

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String carrierUrl = "";
        String loginMail = "";
        ArrayList<String> loginMailList = new ArrayList<String>();
        String userId = "";
        String rsvUserId = "";
        int type = 0;
        String url = "";
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReservePersonalInfoPC frmInfo = null;
        FormReserveSheetPC frmSheet;
        DataLoginInfo_M2 dataLoginInfo_M2;

        String paramUidLink = "";
        int paramHotelId = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        int paramPlanId = 0;
        String paramRsvNo = "";
        int paramCheckAgree = 0;
        int paramWorkId = 0;
        String paramMode = "";
        String errMsg = "";
        int paramChild = 0;
        int paramAdult = 0;
        String paramCardNo = "";
        String paramDispCardNo = "";
        int paramExpireMonth = 0;
        int paramOfferKind = 0;
        int paramExpireYear = 0;
        String paramCreditUpdateFlag = "";

        String checkHotelId = "";

        try
        {
            frmInfo = new FormReservePersonalInfoPC();
            frmSheet = new FormReserveSheetPC();

            // キャリアの判別
            type = UserAgent.getUserAgentType( request );
            if ( type == UserAgent.USERAGENT_AU )
            {
                carrierUrl = "../../au/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                carrierUrl = "../../y/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                carrierUrl = "../../i/reserve/";
            }

            // お気に入りの登録されて呼び出された場合は、受け渡し項目はnullとなる
            // ホテルIDが、nullの場合はエラーページへ遷移させる
            checkHotelId = request.getParameter( "selHotelId" );

            if ( (checkHotelId == null) )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30008" );
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // ログイン情報取得
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            if ( (dataLoginInfo_M2 != null) && (dataLoginInfo_M2.isMemberFlag() == true) )
            {
                // 存在する
                userId = dataLoginInfo_M2.getUserId();

                // メールアドレスのチェック
                if ( dataLoginInfo_M2.getMailAddrMobile().equals( "" ) == false )
                {
                    loginMail = dataLoginInfo_M2.getMailAddrMobile();
                    loginMailList.add( dataLoginInfo_M2.getMailAddrMobile() );
                }
                if ( dataLoginInfo_M2.getMailAddr().equals( "" ) == false )
                {
                    if ( loginMail.equals( "" ) != false )
                    {
                        loginMail = dataLoginInfo_M2.getMailAddr();
                    }
                    loginMailList.add( dataLoginInfo_M2.getMailAddr() );
                }
            }
            else
            {
                url = carrierUrl + "reserve_nomember.jsp";
                response.sendRedirect( url );
                return;
            }

            // 画面のパラメータ取得
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelId" ).toString() );
            paramRsvNo = request.getParameter( "rsvNo" );
            paramRsvDate = Integer.parseInt( request.getParameter( "rsvDate" ).toString() );
            paramWorkId = Integer.parseInt( request.getParameter( "workId" ) );
            paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );
            paramSeq = Integer.parseInt( request.getParameter( "selSeq" ) );
            paramMode = request.getParameter( "mode" );
            paramAdult = Integer.parseInt( request.getParameter( "adult" ) );
            paramChild = Integer.parseInt( request.getParameter( "child" ) );
            paramOfferKind = Integer.parseInt( request.getParameter( "offerkind" ) );
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

            frmInfo.setLoginUserId( userId );
            frmInfo.setLoginMailAddr( loginMail );
            frmInfo.setLoginMailAddrList( loginMailList );
            frmInfo.setSelHotelID( paramHotelId );
            frmInfo.setSeq( paramSeq );
            frmInfo.setSelRsvDate( paramRsvDate );
            frmInfo.setSelPlanID( paramPlanId );

            if ( (request.getParameter( "check" ) != null) && (request.getParameter( "check" ).trim().length() != 0) )
            {
                paramCheckAgree = 1;
            }
            if ( (request.getParameter( "check" ) != null) && (request.getParameter( "check" ).trim().length() != 0) )
            {
                paramCheckAgree = 1;
            }

            // 無料会員＆有料会員の区別をするためにプランデータ取得
            ReserveCommon rsvcomm = new ReserveCommon();
            frmInfo.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
            frmInfo = rsvcomm.getPlanData( frmInfo );

            // 追加
            frmInfo.setOrgReserveDate( paramRsvDate );
            frmInfo = rsvCmm.getRoomZanSuu( frmInfo );

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
                String errMSG = "";
                String premiuminfo = "0";
                if ( ((paramRsvDate > checkDatePremiumST) && (paramRsvDate < checkDatePremiumED) &&
                        (paramRsvDate >= frmInfo.getSalesStartDay()) && (paramRsvDate <= frmInfo.getSalesEndDay())) ||
                        (paramRsvDate == checkDatePremiumST && curTime <= frmInfo.getRsvEndTime()) ||
                        (paramRsvDate == checkDatePremiumED && curTime >= frmInfo.getRsvStartTime()) )
                {
                    // プレミアム会員が有効の場合は表示を変更
                    String err1 = "本プランはハピホテプレミアム会員様のみ予約可能です。";
                    errMsg = err1;
                    premiuminfo = "1";
                }
                else
                {
                    errMSG = "予約受付の期間外です。お手数ですが、もう一度最初からやり直してください。";
                }
                // チェックNGの場合、エラーページへ遷移する。
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMSG );
                request.setAttribute( "premiuminfo", premiuminfo );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
                requestDispatcher.forward( request, response );
                return;
            }

            // パラメータの整合性チェック
            errMsg = rsvCmm.checkParam( paramHotelId, paramPlanId, paramSeq, paramRsvDate, ReserveCommon.USER_KBN_USER, paramMode );
            if ( errMsg.trim().length() != 0 )
            {
                // チェックNGの場合、エラーページへ遷移する。
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // 予約人数の範囲チェック
            errMsg = rsvCmm.checkAdultChildNum( paramHotelId, paramPlanId, paramAdult, paramChild );
            if ( errMsg.trim().length() != 0 )
            {
                // チェックNGの場合、エラーページへ遷移する。
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            frmSheet.setSelHotelId( paramHotelId );
            frmSheet.setRsvNo( paramRsvNo );
            frmSheet.setRsvDate( paramRsvDate );
            frmSheet.setAgree( paramCheckAgree );
            frmSheet.setUserId( userId );
            frmSheet.setMail( loginMail );
            frmSheet.setMailList( loginMailList );
            frmSheet.setWorkId( paramWorkId );
            frmSheet.setSelPlanId( paramPlanId );
            frmSheet.setSeq( paramSeq );
            frmSheet.setMode( paramMode );
            frmSheet.setLoginUserId( userId );
            frmSheet.setCardno( paramCardNo );
            frmSheet.setDispcardno( paramDispCardNo );
            frmSheet.setExpireMonth( paramExpireMonth );
            frmSheet.setExpireYear( paramExpireYear );
            frmSheet.setMode( paramMode );
            if ( paramCreditUpdateFlag.equals( "true" ) )
            {
                frmSheet.setCreditUpdateFlag( true );
            }
            else
            {
                frmSheet.setCreditUpdateFlag( false );
            }

            if ( paramMode.equals( ReserveCommon.MODE_UPD ) )
            {
                // ユーザーIDと予約の作成者が同じか確認
                rsvUserId = rsvCmm.getRsvUserId( frmSheet.getRsvNo() );

                if ( userId.compareTo( rsvUserId ) != 0 )
                {
                    // 違う場合はエラーページ
                    url = carrierUrl + "reserve_error.jsp";
                    request.setAttribute( "err", Message.getMessage( "erro.30004" ) );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                    requestDispatcher.forward( request, response );
                    return;
                }
            }

            // プラン別料金マスタよりチェックイン開始、終了時刻を取得
            frmInfo = rsvCmm.getCiCoTime( frmInfo );

            // 押されたボタンによって処理を振り分ける。
            if ( request.getParameter( "btnFix" ) != null )
            {
                // ▼予約登録・更新
                frmSheet.setUserAgent( request.getHeader( "user-agent" ) );
                frmSheet = execRsvFix( frmSheet );
                switch( frmSheet.getMobileCheckErrKbn() )
                {
                    case CHECKERR_INPUT:
                        // 入力チェックエラー
                        url = carrierUrl + "reserve_confirmation.jsp";
                        request.setAttribute( "dsp", frmSheet );
                        request.setAttribute( "dsp2", frmInfo );
                        break;

                    case CHECKERR_CRITICAL:
                        // 致命的エラー
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                        break;

                    default:
                        // エラーなし
                        url = carrierUrl + "reserve_jump_sheet.jsp";
                        request.setAttribute( "dsp", frmSheet );
                }
            }
            else if ( request.getParameter( "btnRsvCreditFix" ) != null )
            {
                frmSheet = execRsvFix( frmSheet );
                if ( frmSheet.isCreditAuthorityNgFlag() == true )
                {
                    // クレジット認証しているホテルでＮＧだった場合のみクレジット番号入力画面へ戻す
                    url = "reserve_cancel_credit_info.jsp";
                    frmInfo = new FormReservePersonalInfoPC();
                    frmInfo.setSelHotelID( paramHotelId );
                    frmInfo.setSelSeq( paramSeq );
                    frmInfo.setSelPlanID( paramPlanId );
                    frmInfo.setSelRsvDate( paramRsvDate );
                    frmInfo.setMode( paramMode );
                    frmInfo.setWorkId( paramWorkId );

                    frmInfo = execBack( frmInfo );
                    frmInfo.setLoginUserId( userId );
                    frmInfo.setLoginMailAddr( loginMail );
                    frmInfo.setLoginMailAddrList( loginMailList );
                    frmInfo.setMode( paramMode );
                    // さっきのエラーをのせる
                    frmInfo.setErrMsg( frmSheet.getErrMsg() );
                    // プラン別料金マスタよりチェックイン開始、終了時刻を取得
                    frmInfo = rsvCmm.getCiCoTime( frmInfo );

                    request.setAttribute( "dsp", frmInfo );
                }
                else
                {
                    switch( frmSheet.getMobileCheckErrKbn() )
                    {
                        case CHECKERR_INPUT:
                            // 入力チェックエラー
                            url = carrierUrl + "reserve_confirmation.jsp";
                            request.setAttribute( "dsp", frmSheet );
                            request.setAttribute( "dsp2", frmInfo );
                            break;

                        case CHECKERR_CRITICAL:
                            // 致命的エラー
                            url = carrierUrl + "reserve_error.jsp";
                            request.setAttribute( "err", frmSheet.getErrMsg() );
                            break;

                        default:
                            // エラーなし
                            url = carrierUrl + "reserve_jump_sheet.jsp";
                            request.setAttribute( "dsp", frmSheet );
                    }
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // ▼戻る
                frmInfo = new FormReservePersonalInfoPC();
                frmInfo.setReserveNo( paramRsvNo );
                frmInfo.setSelHotelID( paramHotelId );
                frmInfo.setSelSeq( paramSeq );
                frmInfo.setSelPlanID( paramPlanId );
                frmInfo.setSelRsvDate( paramRsvDate );
                frmInfo.setWorkId( paramWorkId );
                frmInfo.setOfferKind( paramOfferKind );
                frmInfo = rsvCmm.getPlanData( frmInfo );
                frmInfo = execBack( frmInfo );

                // ログインユーザー情報取得
                frmInfo.setLoginUserId( userId );
                frmInfo.setLoginMailAddr( loginMail );
                frmInfo.setLoginMailAddrList( loginMailList );
                frmInfo.setMode( paramMode );
                // プラン別料金マスタよりチェックイン開始、終了時刻を取得
                frmInfo = rsvCmm.getCiCoTime( frmInfo );

                request.setAttribute( "dsp", frmInfo );
                url = "reserve_personal_info2.jsp";
            }
            else if ( request.getParameter( "btnRsvCreditBack" ) != null )
            {
                // ▼戻る
                frmInfo = new FormReservePersonalInfoPC();
                frmInfo.setReserveNo( paramRsvNo );
                frmInfo.setSelHotelID( paramHotelId );
                frmInfo.setSelSeq( paramSeq );
                frmInfo.setSelPlanID( paramPlanId );
                frmInfo.setSelRsvDate( paramRsvDate );
                frmInfo.setWorkId( paramWorkId );
                frmInfo.setCardno( paramCardNo );
                frmInfo.setExpiremonth( paramExpireMonth );
                frmInfo.setExpireyear( paramExpireYear );
                frmInfo.setOfferKind( paramOfferKind );
                frmInfo = rsvCmm.getPlanData( frmInfo );
                frmInfo = execBack( frmInfo );

                // ログインユーザー情報取得
                frmInfo.setLoginUserId( userId );
                frmInfo.setLoginMailAddr( loginMail );
                frmInfo.setLoginMailAddrList( loginMailList );
                frmInfo.setMode( paramMode );

                // プラン別料金マスタよりチェックイン開始、終了時刻を取得
                frmInfo = rsvCmm.getCiCoTime( frmInfo );

                request.setAttribute( "dsp", frmInfo );
                url = "reserve_cancel_credit_info.jsp";
            }

            // ここから携帯情報取得
            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            // デバッグ環境かどうか
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                url = "/_debug_" + url;
            }
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveConfirmationMobile.execute() ][hotelId = "
                    + paramHotelId + ",planId = " + paramRsvDate + ",reserveDate = " + paramPlanId + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                request.setAttribute( "dsp", frmInfo );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveConfirmationMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * 戻るボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    private FormReservePersonalInfoPC execBack(FormReservePersonalInfoPC frm) throws Exception
    {
        String zip3 = "";
        String zip4 = "";
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ArrayList<Integer> jisIdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataMasterPref dataPref = new DataMasterPref();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        int numchild = 0;

        // 仮テーブルのデータ取得
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();

        // オプションワークテーブルから通常オプションデータを取得
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

        // 画面表示用Formにセット
        frm.setOrgReserveDate( frmSheet.getRsvDate() );
        frm.setSelSeq( frmSheet.getSeq() );
        frm.setSeq( frmSheet.getSeq() );
        if ( frmSheet.getZip().trim().length() != 0 )
        {
            zip3 = frmSheet.getZip().substring( 0, 3 );
            zip4 = frmSheet.getZip().substring( 4 );
        }
        frm.setZipCd3( zip3 );
        frm.setZipCd4( zip4 );
        frm.setSelPrefId( frmSheet.getPrefId() );
        frm.setSelJisCd( frmSheet.getJisCd() );
        frm.setAddress3( frmSheet.getAddress3() );
        frm.setTel( frmSheet.getTel() );
        frm.setRemainder( frmSheet.getReminder() );
        frm.setRemainderMailAddr( frmSheet.getRemainderMail() );
        frm.setDemands( frmSheet.getDemands() );
        frm.setPlanName( frmSheet.getPlanNm() );
        frm.setReserveDateFormat( frmSheet.getRsvDateView() );
        frm.setSelNumAdult( frmSheet.getAdultNum() );
        if ( rsvCmm.checkLoveHotelFlag( frm.getSelHotelID() ) == false )
        {
            numchild = frmSheet.getChildNum();
        }
        frm.setSelNumChild( numchild );
        frm.setSelNumMan( frmSheet.getManNum() );
        frm.setSelNumWoman( frmSheet.getWomanNum() );
        frm.setBaseChargeView( frmSheet.getBasicTotalView() );
        frm.setChargeTotalView( frmSheet.getChargeTotalView() );
        frm.setRemarks( frmSheet.getRemarks() );
        frm.setQuestionFlg( frmSheet.getQuestionFlg() );
        frm.setQuestion( frmSheet.getQuestion() );
        frm.setSelOptSubNmList( frmSheet.getOptNmList() );
        frm.setSelOptSubChargeTotalList( frmSheet.getOptChargeTotalList() );
        frm.setSelOptSubUnitPriceViewList( frmSheet.getOptUnitPriceViewList() );
        frm.setSelOptNumList( frmSheet.getOptInpMaxQuantityList() );
        frm.setSelOptSubRemarksList( frmSheet.getOptRemarksList() );

        // ホテル名取得
        frm = rsvCmm.getHotelData( frm );

        // 都道府県名取得
        dataPref.getData( frmSheet.getPrefId() );
        frm.setAddress1( dataPref.getName() );

        // 部屋残数取得
        frm = rsvCmm.getRoomZanSuu( frm );

        // 市区町村リスト取得
        jisIdList = rsvCmm.getJisCdList( frm.getSelPrefId() );
        jisNMList = rsvCmm.getJisNmList( frm.getSelPrefId() );

        // フォームにセット
        frm.setJisCdList( jisIdList );
        frm.setJisNmList( jisNMList );

        logic = null;
        rsvCmm = null;
        dataPref = null;
        frmSheet = null;

        return(frm);
    }

    /**
     * 確認処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    private FormReserveSheetPC execRsvFix(FormReserveSheetPC frm) throws Exception
    {
        String errMsg = "";
        String paramRsvNo = "";
        LogicReservePersonalInfoPC logicPC = new LogicReservePersonalInfoPC();
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReservePersonalInfoPC frmPC = new FormReservePersonalInfoPC();
        GMOCcsCredit gmoccs = null;
        DataRsvSpid dataspid = null;
        boolean noshowCreditFlag = false;

        try
        {
            paramRsvNo = frm.getRsvNo();
            frm.setUserKbn( ReserveCommon.USER_KBN_USER );

            // ホテル名取得
            frmPC.setSelHotelID( frm.getSelHotelId() );
            frmPC = rsvCmm.getHotelData( frmPC );

            // データチェック
            frm = logicCheckIn.getWorkOptionQuantity( frm );
            frm = rsvCmm.chkDspMaster( frm );

            // エラーあり時
            if ( frm.getErrMsg().trim().length() != 0 )
            {
                // エラーがあった場合、画面内容再取得
                logicPC.setFrmSheet( frm );
                logicPC.getReserveWorkData( frm.getWorkId() );
                frm = logicPC.getFrmSheet();
                frm.setRsvNo( paramRsvNo );

                // オプションワークテーブルから必須オプションデータを取得
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_IMP );

                // オプションワークテーブルから通常オプションデータを取得
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

                frm.setMobileCheckErrKbn( CHECKERR_CRITICAL );
                return(frm);
            }

            // ▼エラーなし時、確定処理実行
            // 同意チェックがされているか
            if ( frm.getAgree() == 0 )
            {
                errMsg = errMsg + Message.getMessage( "warn.00010" ) + "<br />";
                logicPC.setFrmSheet( frm );
                logicPC.getReserveWorkData( frm.getWorkId() );
                frm = logicPC.getFrmSheet();
                frm.setErrMsg( errMsg );
                frm.setHotelNm( frmPC.getHotelName() );
                frm.setOrgRsvDate( frm.getRsvDate() );

                // オプションワークテーブルからデータを取得
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_IMP );

                // オプションワークテーブルから通常オプションデータを取得
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

                frm.setMobileCheckErrKbn( CHECKERR_INPUT );
                return(frm);
            }

            noshowCreditFlag = rsvCmm.checkNoShowCreditHotel( frm.getSelHotelId() );

            if ( noshowCreditFlag && (frm.getMode().equals( ReserveCommon.MODE_INS ) || frm.isCreditUpdateFlag() == true) )
            {
                // 1円オーソリ
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

            // ホテル名、チェックイン、チェックアウト、ハピーポイント設定
            setRsvWorkData( frm );

            // 処理実行
            frm.setTermKind( ReserveCommon.TERM_KIND_MOBILE );
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
                logicPC.setFrmSheet( frm );
                logicPC.getReserveWorkData( frm.getWorkId() );
                frm = logicPC.getFrmSheet();
                frm.setRsvNo( paramRsvNo );
                frm.setMobileCheckErrKbn( CHECKERR_CRITICAL );
                return(frm);
            }

            if ( noshowCreditFlag == true && frm.isCreditAuthorityNgFlag() == false && (frm.getMode().equals( ReserveCommon.MODE_INS ) || frm.isCreditUpdateFlag() == true) )
            {
                // クレジットデータ登録
                byte[] key = "axpol ptmhyeeahl".getBytes();

                // 暗号ベクター（Initialization Vector：初期化ベクトル）
                byte[] ivBytes = "s h t t i s n h ".getBytes();

                String cardno = frm.getCardno();
                // 暗号化
                String encode = EncodeData.encodeString( key, ivBytes, cardno );
                DataRsvCredit datarsv = new DataRsvCredit();

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
            // 登録成功
            frm = logicCheckIn.getFrm();

            // 登録データの取得
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            frm.setUserKbn( ReserveCommon.USER_KBN_USER );
            frm.setStatus( ReserveCommon.RSV_STATUS_ZUMI );

            // ワークテーブルから対象データ削除
            logicPC.deleteRsvWork( frm.getWorkId() );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionReserveConfirmationMobile.execRsvFix() ] " + e.getMessage() );
            throw new Exception( "[ActionReserveConfirmationMobile.execRsvFix() ] " + e.getMessage() );

        }
        finally
        {
            logicPC = null;
            logicCheckIn = null;
            logicSheet = null;
            rsvCmm = null;
            frmPC = null;
        }

        return(frm);
    }

    /**
     * 
     * 予約仮テーブルに各項目を設定
     * ホテル名、ハピーポイント、チェックイン、チェックアウトを登録する。
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return なし
     */
    private void setRsvWorkData(FormReserveSheetPC frmSheet) throws Exception
    {
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReservePersonalInfoPC frm = new FormReservePersonalInfoPC();
        LogicOwnerRsvCheckIn logic = new LogicOwnerRsvCheckIn();

        frm.setSelHotelID( frmSheet.getSelHotelId() );
        frm.setSelPlanID( frmSheet.getSelPlanId() );
        frm.setBaseChargeTotal( frmSheet.getBasicTotal() );
        frm.setWorkId( frmSheet.getWorkId() );
        frm.setSelRsvDate( frmSheet.getRsvDate() );

        // ホテル名取得
        frm = rsvCmm.getHotelData( frm );

        // ハピーポイント取得
        frm = setHapyPoint( frm );

        // チェックイン、チェックアウト時間取得
        frm = rsvCmm.getCiCoTime( frm );

        // 仮テーブル更新
        logic.setRsvWorkData( frm );
    }

    /**
     * 
     * ハピーポイント
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC setHapyPoint(FormReservePersonalInfoPC frm) throws Exception
    {
        int RESERVE_CODE = 1000007;
        boolean ret = false;
        int pointKind = 0;
        int point = 0;
        double dblCalPoint = 0.0;
        int calPoint = 0;
        int basicCharge = 0;
        LogicOwnerRsvCheckIn logic = new LogicOwnerRsvCheckIn();
        DataMasterPoint dmp = new DataMasterPoint();

        // ワークテーブルの基本料金取得
        basicCharge = logic.getWorkBasicCharge( frm.getWorkId() );

        // プランマスタを取得
        DataRsvPlan dataRsvPlanm = new DataRsvPlan();
        ret = dataRsvPlanm.getData( frm.getSelHotelID(), frm.getSelPlanID() );

        if ( ret )
        {
            pointKind = dataRsvPlanm.getGivingPointKind();
            point = dataRsvPlanm.getGivingPoint();
        }

        // ポイントの計算
        if ( pointKind == 1 )
        {
            calPoint = (basicCharge * point) / 100;
        }
        else if ( pointKind == 2 )
        {
            calPoint = point;
        }

        // 無料だったら無料の倍率を考慮する
        if ( frm.isPaymemberFlg() == false )
        {
            dmp.getData( RESERVE_CODE );
            if ( dmp.getFreeMultiple() != 0 )
            {
                calPoint = (int)(calPoint * dmp.getFreeMultiple());
            }
        }

        dblCalPoint = Math.round( calPoint );
        calPoint = (int)dblCalPoint;

        // フォームにセット
        frm.setHapyPoint( calPoint );

        return(frm);
    }
}
