package jp.happyhotel.action;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.DecodeData;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterCity;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataRsvCredit;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;

/**
 * 
 * 個人情報入力２画面 Action Class
 */

public class ActionReservePersonalInfo2Mobile extends BaseAction
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
        int type = 0;
        String url = "";
        String rsvUserId = "";
        FormReservePersonalInfoPC frmInfo = null;
        FormReserveSheetPC frmSheet;
        ReserveCommon rsvCmm = new ReserveCommon();
        DataLoginInfo_M2 dataLoginInfo_M2;

        String paramUidLink = "";
        int paramHotelId = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        int paramPlanId = 0;
        String paramMode = "";
        String errMsg = "";
        String maskCardNo = "";

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

            // 画面内容取得
            frmInfo.setLoginUserId( userId );
            frmInfo.setLoginMailAddr( loginMail );
            frmInfo.setLoginMailAddrList( loginMailList );
            frmInfo = getRequestData( request, frmInfo );
            paramHotelId = frmInfo.getSelHotelID();
            paramSeq = frmInfo.getSeq();
            paramRsvDate = frmInfo.getSelRsvDate();
            paramPlanId = frmInfo.getSelPlanID();
            if ( request.getParameter( "mode" ) != null )
            {
                paramMode = request.getParameter( "mode" );
            }

            // 無料会員＆有料会員の区別をするためにプランデータ取得
            ReserveCommon rsvcomm = new ReserveCommon();
            frmInfo.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
            frmInfo = rsvcomm.getPlanData( frmInfo );

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
                    errMSG = err1;
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
            errMsg = rsvCmm.checkAdultChildNum( paramHotelId, paramPlanId, frmInfo.getSelNumAdult(), frmInfo.getSelNumChild() );
            if ( errMsg.trim().length() != 0 )
            {
                // チェックNGの場合、エラーページへ遷移する。
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( paramMode.equals( ReserveCommon.MODE_UPD ) )
            {
                // ユーザーIDと予約の作成者が同じか確認
                rsvUserId = rsvCmm.getRsvUserId( frmInfo.getReserveNo() );

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

            // 基本料金が上書きされてしまうため、仮保存
            int baseCharge = frmInfo.getBaseChargeTotal();

            // プラン別料金マスタよりチェックイン開始、終了時刻を取得
            frmInfo = rsvCmm.getCiCoTime( frmInfo );

            if ( frmInfo.getBaseChargeTotal() < baseCharge )
            {
                frmInfo.setBaseChargeTotal( baseCharge );
            }

            if ( request.getParameter( "btnConfirm" ) != null )
            {
                // 入力チェック
                frmInfo = inputCheck( frmInfo );

                // 入力チェック
                // 2012/02/02 メールアドレスのリストがなかったらエラーとするように変更
                if ( frmInfo.getErrMsg().trim().length() != 0 || loginMailList.size() == 0 )
                {
                    if ( loginMailList.size() == 0 )
                    {
                        frmInfo.setErrMsg( frmInfo.getErrMsg() + "連絡先メールアドレスが登録されていません。</br>" );
                    }
                    // 入力エラーあり
                    request.setAttribute( "dsp", frmInfo );
                    url = carrierUrl + "reserve_personal_info2.jsp";
                }
                else
                {
                    // ▼確認画面へ
                    frmSheet = setConfirm( frmInfo, frmSheet );
                    frmSheet.setMailList( loginMailList );
                    if ( frmSheet.getMobileCheckErrKbn() == CHECKERR_CRITICAL )
                    {
                        // 致命的エラー
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                    }
                    else
                    {
                        url = carrierUrl + "reserve_confirmation.jsp";
                        request.setAttribute( "dsp", frmSheet );
                        request.setAttribute( "dsp2", frmInfo );
                    }
                }
            }
            else if ( request.getParameter( "btnRsvCreditConfirm" ) != null )
            {
                // カード番号未入力エラー
                if ( request.getParameter( "card_no" ) == null || request.getParameter( "card_no" ).equals( "" ) )
                {
                    errMsg += Message.getMessage( "warn.00001", "カード番号" );
                }
                else
                {
                    String paramCardNo = request.getParameter( "card_no" );
                    // カード番号の有効性判定
                    if ( !rsvCmm.CheckCardNo( paramCardNo ) )
                    {
                        errMsg += Message.getMessage( "warn.00009", "カード番号" );
                    }
                }
                // カード有効期限未入力エラー
                if ( request.getParameter( "expire_year" ) == null || request.getParameter( "expire_year" ).equals( "" ) ||
                        request.getParameter( "expire_month" ) == null || request.getParameter( "expire_month" ).equals( "" ) )
                {
                    errMsg += Message.getMessage( "warn.00001", "有効期限" );
                }
                else
                {
                    // 有効期限の有効性判定
                    if ( !rsvCmm.CheckCardLimit( request.getParameter( "expire_year" ), request.getParameter( "expire_month" ) ) )
                    {
                        errMsg += Message.getMessage( "warn.00009", "有効期限" );
                    }
                }

                if ( errMsg.equals( "" ) == false )
                {
                    frmInfo = setNext( frmInfo, frmSheet );
                    if ( frmInfo.getMobileCheckErrKbn() == CHECKERR_CRITICAL )
                    {
                        // 致命的エラー
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                    }
                    else
                    {
                        // クレジット入力画面へ戻す
                        frmInfo.setErrMsg( errMsg );
                        url = carrierUrl + "reserve_cancel_credit_info.jsp";
                        request.setAttribute( "dsp", frmInfo );
                    }
                }
                else
                {
                    // カード番号と有効期限セット
                    if ( request.getParameter( "card_no" ) != null )
                    {
                        frmSheet.setCardno( request.getParameter( "card_no" ) );
                        String dispCardNo = request.getParameter( "card_no" );
                        for( int i = 0 ; i < dispCardNo.length() ; i++ )
                        {
                            if ( i < dispCardNo.length() - 4 )
                            {
                                maskCardNo += "*";
                            }
                            else
                            {
                                maskCardNo += dispCardNo.charAt( i );
                            }
                        }
                        frmSheet.setDispcardno( maskCardNo );
                    }
                    if ( request.getParameter( "expire_month" ) != null && !request.getParameter( "expire_month" ).equals( "" ) )
                    {
                        frmSheet.setExpireMonth( Integer.parseInt( request.getParameter( "expire_month" ) ) );
                    }
                    if ( request.getParameter( "expire_year" ) != null && !request.getParameter( "expire_year" ).equals( "" ) )
                    {
                        frmSheet.setExpireYear( Integer.parseInt( request.getParameter( "expire_year" ) ) );
                    }

                    // 入力チェック
                    frmInfo = inputCheck( frmInfo );

                    // 入力チェック
                    // 2012/02/02 メールアドレスのリストがなかったらエラーとするように変更
                    if ( frmInfo.getErrMsg().trim().length() != 0 || loginMailList.size() == 0 )
                    {
                        if ( loginMailList.size() == 0 )
                        {
                            frmInfo.setErrMsg( frmInfo.getErrMsg() + "連絡先メールアドレスが登録されていません。</br>" );
                        }
                        // 入力エラーあり
                        request.setAttribute( "dsp", frmInfo );
                        url = carrierUrl + "reserve_personal_info2.jsp";
                    }
                    else
                    {
                        // ▼確認画面へ
                        frmSheet = setConfirm( frmInfo, frmSheet );
                        frmSheet.setMailList( loginMailList );
                        if ( frmSheet.getMobileCheckErrKbn() == CHECKERR_CRITICAL )
                        {
                            // 致命的エラー
                            url = carrierUrl + "reserve_error.jsp";
                            request.setAttribute( "err", frmSheet.getErrMsg() );
                        }
                        else
                        {
                            if ( paramMode.equals( ReserveCommon.MODE_UPD ) && request.getParameter( "btnRsvCreditConfirm" ) != null )
                            {
                                // クレジットカード更新時
                                frmSheet.setCreditUpdateFlag( true );
                            }
                            url = carrierUrl + "reserve_confirmation.jsp";
                            request.setAttribute( "dsp", frmSheet );
                            request.setAttribute( "dsp2", frmInfo );
                        }
                    }
                }
            }
            else if ( request.getParameter( "btnRsvCreditReg" ) != null )
            {
                // 入力チェック
                frmInfo = inputCheck( frmInfo );

                // 入力チェック
                // 2012/02/02 メールアドレスのリストがなかったらエラーとするように変更
                if ( frmInfo.getErrMsg().trim().length() != 0 || loginMailList.size() == 0 )
                {
                    if ( loginMailList.size() == 0 )
                    {
                        frmInfo.setErrMsg( frmInfo.getErrMsg() + "連絡先メールアドレスが登録されていません。</br>" );
                    }
                    // 入力エラーあり
                    request.setAttribute( "dsp", frmInfo );
                    url = carrierUrl + "reserve_personal_info2.jsp";
                }
                else
                {
                    frmInfo = setNext( frmInfo, frmSheet );
                    if ( frmInfo.getMobileCheckErrKbn() == CHECKERR_CRITICAL )
                    {
                        // 致命的エラー
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                    }
                    else
                    {
                        // クレジット入力画面へ
                        url = carrierUrl + "reserve_cancel_credit_info.jsp";
                        request.setAttribute( "dsp", frmInfo );
                    }
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // ▼戻る
                frmInfo = setBack( frmInfo );
                url = carrierUrl + "reserve_personal_info1.jsp";
                request.setAttribute( "dsp", frmInfo );
            }
            else if ( request.getParameter( "btnInfo2Back" ) != null )
            {
                // ▼戻る
                frmInfo = setBack( frmInfo );
                url = carrierUrl + "reserve_personal_info2.jsp";
                request.setAttribute( "dsp", frmInfo );
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
            Logging.error( "[ActionReservePersonalInfo2Mobile.execute() ][hotelId = "
                    + paramHotelId + ",planId = " + paramPlanId + ",reserveDate = " + paramRsvDate + "] Exception", exception );
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
                Logging.error( "[ActionReservePersonalInfo2Mobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * 
     * 入力チェック
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC inputCheck(FormReservePersonalInfoPC frm) throws Exception
    {

        boolean ret = false;
        ReserveCommon rsvCmm = new ReserveCommon();

        // 入力チェック
        ret = rsvCmm.isInputCheck( frm, ReserveCommon.INP_CHECK_MOBILE2 );
        if ( ret == false )
        {
            // エラーあり時
            frm = getViewData( frm );
            frm.setMobileCheckErrKbn( CHECKERR_INPUT );
        }
        return(frm);
    }

    /**
     * 
     * 確認画面へボタン処理
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReserveSheetPCオブジェクト
     */
    private FormReserveSheetPC setConfirm(FormReservePersonalInfoPC frm, FormReserveSheetPC frmSheet) throws Exception
    {
        boolean inputCheck = false;
        int workId = 0;
        ReserveCommon rsvCmm = new ReserveCommon();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        String maskCardNo = "";

        // データのチェック
        frmSheet = rsvCmm.margeFormSheetPC( frmSheet, frm );
        frmSheet.setWorkId( frm.getWorkId() );
        frmSheet = logicCheckIn.getWorkOptionQuantity( frmSheet );
        frmSheet = rsvCmm.chkDspMaster( frmSheet );
        if ( frmSheet.getErrMsg().trim().length() != 0 )
        {
            // エラーあり
            inputCheck = false;
            frmSheet.setMobileCheckErrKbn( CHECKERR_CRITICAL );
            return(frmSheet);
        }

        // 新規登録時以外は、ステータスチェック
        if ( !frm.getMode().equals( ReserveCommon.MODE_INS ) )
        {
            inputCheck = rsvCmm.checkStatus( frm.getSelHotelID(), frm.getReserveNo(), frm.getStatus() );
            if ( inputCheck == false )
            {
                // エラーあり
                frmSheet.setMobileCheckErrKbn( CHECKERR_CRITICAL );
                frmSheet.setErrMsg( Message.getMessage( "warn.00021" ) );
                return(frmSheet);
            }
        }

        // 次画面遷移処理
        workId = registRsvWork( frm );
        frm.setWorkId( workId );

        // 次の画面内容を取得
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();
        frmSheet.setMail( frm.getLoginMailAddr() );
        frmSheet.setWorkId( frm.getWorkId() );
        frmSheet.setOrgRsvDate( frm.getSelRsvDate() );
        frmSheet.setRsvNo( frm.getReserveNo() );

        // 要望表示
        frmSheet.setDispRequestFlag( frm.getDispRequestFlg() );

        // ホテル名取得
        frm = rsvCmm.getHotelData( frm );
        frmSheet.setHotelNm( frm.getHotelName() );

        // 部屋残数取得
        frm = rsvCmm.getRoomZanSuu( frm );
        frmSheet.setRoomZansuu( frm.getRoomZanSuu() );

        // 提供区分を取得
        frm = rsvCmm.getPlanData( frm );
        frmSheet.setOfferKind( frm.getOfferKind() );

        // オプションワークテーブルから必須オプションデータを取得
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_IMP );

        // オプションワークテーブルから通常オプションデータを取得
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

        // カード番号を取得してマスク値をセット
        if ( frmSheet.getCardno().equals( "" ) && ReserveCommon.checkNoShowCreditHotel( frm.getSelHotelID() ) == true )
        {
            DataRsvCredit rsvcredit = new DataRsvCredit();
            rsvcredit.getData( frm.getReserveNo() );
            String dispCardNo = DecodeData.decodeString( "axpol ptmhyeeahl".getBytes( "8859_1" ), "s h t t i s n h ".getBytes( "8859_1" ), new String( rsvcredit.getCard_no() ) );
            frmSheet.setCardno( dispCardNo );
            for( int i = 0 ; i < dispCardNo.length() ; i++ )
            {
                if ( i < dispCardNo.length() - 4 )
                {
                    maskCardNo += "*";
                }
                else
                {
                    maskCardNo += dispCardNo.charAt( i );
                }
            }
            frmSheet.setDispcardno( maskCardNo );
            frmSheet.setExpireYear( 20 * 100 + rsvcredit.getLimit_date() / 100 );
            frmSheet.setExpireMonth( rsvcredit.getLimit_date() % 100 );
        }

        return(frmSheet);
    }

    /**
     * 
     * 次へボタン処理
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC setNext(FormReservePersonalInfoPC frm, FormReserveSheetPC frmSheet) throws Exception
    {
        boolean inputCheck = false;
        int workId = 0;
        ReserveCommon rsvCmm = new ReserveCommon();
        LogicOwnerRsvCheckIn logic = new LogicOwnerRsvCheckIn();

        // 入力チェック
        inputCheck = rsvCmm.isInputCheck( frm, ReserveCommon.INP_CHECK_MOBILE1 );
        if ( inputCheck == false )
        {
            // エラーあり時
            frm = getViewData( frm );
            frm.setMobileCheckErrKbn( CHECKERR_INPUT );
            return(frm);
        }

        // データのチェック
        frmSheet = rsvCmm.margeFormSheetPC( frmSheet, frm );
        frmSheet.setWorkId( frm.getWorkId() );
        frmSheet = logic.getWorkOptionQuantity( frmSheet );
        frmSheet = rsvCmm.chkDspMaster( frmSheet );
        if ( frmSheet.getErrMsg().trim().length() != 0 )
        {
            // エラーあり
            inputCheck = false;
            frm.setErrMsg( frmSheet.getErrMsg() );
            frm.setMobileCheckErrKbn( CHECKERR_CRITICAL );
            return(frm);
        }

        // 新規登録時以外は、ステータスチェック
        if ( !frm.getMode().equals( ReserveCommon.MODE_INS ) )
        {
            inputCheck = rsvCmm.checkStatus( frm.getSelHotelID(), frm.getReserveNo(), frm.getStatus() );
            if ( inputCheck == false )
            {
                // エラーあり
                frm.setErrMsg( Message.getMessage( "warn.00021" ) );
                frm.setMobileCheckErrKbn( CHECKERR_CRITICAL );
                return(frm);
            }
        }

        // 次画面遷移処理
        workId = registRsvWork( frm );
        frm.setWorkId( workId );

        // ホテル名取得
        frm = rsvCmm.getHotelData( frm );
        // 次の画面内容を取得
        // frm = getPersonlInfo2Data( frm );

        return(frm);
    }

    /**
     * 
     * 画面内容取得
     * 
     * @param req HttpServletRequest
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getRequestData(HttpServletRequest req, FormReservePersonalInfoPC frm) throws Exception
    {
        int paramSelHotelId = 0;
        int paramSelPlanId = 0;
        int paramSelJisCd = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        String paramAddress3 = "";
        String paramTel = "";
        int paramReminder = 0;
        String paramReminderMail = "";
        String paramDemands = "";
        int paramWorkId = 0;
        String paramRsvNo = "";
        String paramRemarks = "";

        paramSelHotelId = Integer.parseInt( req.getParameter( "selHotelId" ) );
        paramSelPlanId = Integer.parseInt( req.getParameter( "selPlanId" ) );
        paramSeq = Integer.parseInt( req.getParameter( "selSeq" ) );
        paramRsvDate = Integer.parseInt( req.getParameter( "rsvDate" ) );
        paramWorkId = Integer.parseInt( req.getParameter( "workId" ) );
        paramAddress3 = req.getParameter( "address3" );
        paramTel = req.getParameter( "tel" );
        paramReminderMail = req.getParameter( "other_mail_addr" );

        if ( (req.getParameter( "address2" ) != null) && (req.getParameter( "address2" ).trim().length() != 0) )
        {
            paramSelJisCd = Integer.parseInt( req.getParameter( "address2" ) );
        }
        if ( (req.getParameter( "remainder" ) != null) && (req.getParameter( "remainder" ).trim().length() != 0) )
        {
            paramReminder = Integer.parseInt( req.getParameter( "remainder" ) );
        }
        if ( (req.getParameter( "reserveNo" ) != null) && (req.getParameter( "reserveNo" ).trim().length() != 0) )
        {
            paramRsvNo = req.getParameter( "reserveNo" );
        }
        if ( (req.getParameter( "remarks" ) != null) && (req.getParameter( "remarks" ).trim().length() != 0) )
        {
            paramRemarks = req.getParameter( "remarks" );
        }
        if ( (req.getParameter( "demands" ) != null) && (req.getParameter( "demands" ).trim().length() != 0) )
        {
            paramDemands = req.getParameter( "demands" );
        }

        // 仮データ取得
        frm.setWorkId( paramWorkId );
        frm = getRsvWorkData( frm );

        // フォームへセット
        frm.setMode( req.getParameter( "mode" ) );
        frm.setSelHotelID( paramSelHotelId );
        frm.setSelPlanID( paramSelPlanId );
        frm.setOrgReserveDate( paramRsvDate );
        frm.setSelRsvDate( paramRsvDate );
        frm.setSeq( paramSeq );
        frm.setSelSeq( paramSeq );
        frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );
        frm.setSelJisCd( paramSelJisCd );
        frm.setAddress3( paramAddress3 );
        frm.setTel( paramTel );
        frm.setRemainder( paramReminder );
        frm.setRemainderMailAddr( paramReminderMail );
        frm.setDemands( paramDemands );
        frm.setReserveNo( paramRsvNo );
        frm.setRemarks( paramRemarks );

        return(frm);
    }

    /**
     * 
     * プラン仮データ取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getRsvWorkData(FormReservePersonalInfoPC frm) throws Exception
    {
        String week = "";
        String rsvDateFormat = "";
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        ReserveCommon rsvcomm = new ReserveCommon();
        int numchild = 0;

        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();

        // 画面表示用Formにセット
        frm.setSelHotelID( frmSheet.getSelHotelId() );
        frm.setSelPlanID( frmSheet.getSelPlanId() );
        frm.setSelEstTimeArrival( frmSheet.getEstTimeArrival() );
        frm.setSelNumAdult( frmSheet.getAdultNum() );
        if ( rsvcomm.checkLoveHotelFlag( frmSheet.getSelHotelId() ) == false )
        {
            numchild = frmSheet.getChildNum();
        }
        frm.setSelNumChild( numchild );
        frm.setSelNumMan( frmSheet.getManNum() );
        frm.setSelNumWoman( frmSheet.getWomanNum() );
        frm.setSelParkingUsedKbn( frmSheet.getParking() );
        frm.setSelParkingCount( frmSheet.getParkingCnt() );
        frm.setSelHiRoofCount( frmSheet.getHiRoofCnt() );
        frm.setSelRsvDate( frmSheet.getRsvDate() );
        week = DateEdit.getWeekName( frmSheet.getRsvDate() );
        rsvDateFormat = Integer.toString( frmSheet.getRsvDate() ).substring( 0, 4 ) + "年"
                + Integer.toString( frmSheet.getRsvDate() ).substring( 4, 6 ) + "月"
                + Integer.toString( frmSheet.getRsvDate() ).substring( 6, 8 ) + "日(" + week + ")";
        frm.setReserveDateFormat( rsvDateFormat );
        frm.setSelParkingUsedKbn( frmSheet.getParking() );
        frm.setSelParkingCount( frmSheet.getParkingCnt() );
        frm.setParkingUsedKbnInit( frmSheet.getParking() );
        frm.setBaseChargeTotal( frmSheet.getBasicTotal() );
        frm.setOptionCharge( frmSheet.getOptionChargeTotal() );
        frm.setChargeTotal( frmSheet.getChargeTotal() );
        frm.setBaseChargeView( objNum.format( frmSheet.getBasicTotal() ) );
        frm.setChargeTotalView( objNum.format( frmSheet.getChargeTotal() ) );
        frm.setLastName( frmSheet.getLastName() );
        frm.setFirstName( frmSheet.getFirstName() );
        frm.setLastNameKana( frmSheet.getLastNmKana() );
        frm.setFirstNameKana( frmSheet.getFirstNmKana() );
        frm.setZipCd3( frmSheet.getZip().substring( 0, 3 ) );
        frm.setZipCd4( frmSheet.getZip().substring( 4 ) );
        frm.setSelPrefId( frmSheet.getPrefId() );

        return(frm);
    }

    /**
     * 
     * 戻るボタン処理
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC setBack(FormReservePersonalInfoPC frm) throws Exception
    {
        int workId = 0;

        // ワークテーブルへ格納
        workId = registRsvWork( frm );
        frm.setWorkId( workId );

        // 前の画面内容を取得
        frm = getPersonlInfo1Data( frm );

        return(frm);
    }

    /**
     * 
     * ワークテーブルにデータを登録
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @return int ワークID
     */
    private int registRsvWork(FormReservePersonalInfoPC frm) throws Exception
    {
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        DataMasterPref dataPref = new DataMasterPref();
        DataMasterCity dataCity = new DataMasterCity();

        // 都道府県名設定
        dataPref.getData( frm.getSelPrefId() );
        frm.setAddress1( dataPref.getName() );

        // 市区町村名設定
        dataCity.getData( frm.getSelJisCd() );
        frm.setAddress2( dataCity.getName() );

        // 更新処理実行
        logic.setFrm( frm );
        logic.updReserveWork();
        frm = logic.getFrm();

        logic = null;
        dataPref = null;
        dataCity = null;

        return(frm.getWorkId());
    }

    /**
     * 
     * 個人情報入力1画面へ遷移するためのデータ取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getPersonlInfo1Data(FormReservePersonalInfoPC frm) throws Exception
    {
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ArrayList<Integer> prefIdList = new ArrayList<Integer>();
        ArrayList<String> prefNMList = new ArrayList<String>();
        ArrayList<Integer> jisIdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        int numchild = 0;

        // 仮テーブルのデータ取得
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();

        // オプションワークテーブルから通常オプションデータを取得
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

        // 画面表示用Formにセット
        frm.setSelHotelID( frmSheet.getSelHotelId() );
        frm.setSelPlanID( frmSheet.getSelPlanId() );
        frm.setSelEstTimeArrival( frmSheet.getEstTimeArrival() );
        frm.setSelNumAdult( frmSheet.getAdultNum() );
        if ( rsvCmm.checkLoveHotelFlag( frmSheet.getSelHotelId() ) == false )
        {
            numchild = frmSheet.getChildNum();
        }
        frm.setSelNumChild( numchild );
        frm.setSelNumMan( frmSheet.getManNum() );
        frm.setSelNumWoman( frmSheet.getWomanNum() );
        frm.setSelParkingUsedKbn( frmSheet.getParking() );
        frm.setSelParkingCount( frmSheet.getParkingCnt() );
        frm.setSelHiRoofCount( frmSheet.getHiRoofCnt() );
        frm.setSelRsvDate( frmSheet.getRsvDate() );
        frm.setReserveDateFormat( frmSheet.getRsvDateView() );
        frm.setPlanName( frmSheet.getPlanNm() );
        frm.setSelOptSubNmList( frmSheet.getOptNmList() );
        frm.setSelOptSubChargeTotalList( frmSheet.getOptChargeTotalList() );
        frm.setSelOptSubUnitPriceViewList( frmSheet.getOptUnitPriceViewList() );
        frm.setSelOptNumList( frmSheet.getOptInpMaxQuantityList() );
        frm.setSelOptSubRemarksList( frmSheet.getOptRemarksList() );
        frm.setSelOptSubNumList( frmSheet.getOptInpMaxQuantityList() );

        // ホテル名取得
        frm = rsvCmm.getHotelData( frm );

        // 部屋残数取得
        frm = rsvCmm.getRoomZanSuu( frm );

        // 都道府県リスト取得
        prefIdList = rsvCmm.getPrefIdList();
        prefNMList = rsvCmm.getPrefNmList();

        // フォームにセット
        frm.setPrefIdList( prefIdList );
        frm.setPrefNmList( prefNMList );

        // 市区町村リスト取得
        jisIdList = rsvCmm.getJisCdList( frm.getSelPrefId() );
        jisNMList = rsvCmm.getJisNmList( frm.getSelPrefId() );

        // フォームにセット
        frm.setJisCdList( jisIdList );
        frm.setJisNmList( jisNMList );

        return(frm);
    }

    /**
     * 
     * 自画面表示処理
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getViewData(FormReservePersonalInfoPC frm) throws Exception
    {
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ArrayList<Integer> jisIdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataMasterPref dataPref = new DataMasterPref();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();

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
        frm.setZipCd3( frmSheet.getZip().substring( 0, 3 ) );
        frm.setZipCd4( frmSheet.getZip().substring( 4 ) );
        frm.setSelPrefId( frmSheet.getPrefId() );
        frm.setPlanName( frmSheet.getPlanNm() );
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

        return(frm);
    }

}
