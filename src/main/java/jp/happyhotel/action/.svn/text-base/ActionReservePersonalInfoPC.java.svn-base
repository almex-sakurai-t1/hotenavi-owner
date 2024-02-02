package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.DecodeData;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterCity;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataRsvCredit;
import jp.happyhotel.data.DataRsvDayCharge;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvPlanCharge;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataRsvRoom;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveInitPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;

/**
 * 
 * 個人情報入力画面（PC版） Action Class
 */

public class ActionReservePersonalInfoPC extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    // 処理モード
    private static final int  MODE_CHECK_PC     = 1;   // PC版の入力チェック用

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String week = "";
        String rsvDate = "";
        String rsvDateFormat = "";
        FormReservePersonalInfoPC frm;
        FormReserveSheetPC frmSheet;
        ReserveCommon rsvCmm;
        DataLoginInfo_M2 dataLoginInfo_M2;
        String url = "";
        int paramHotelId = 0;
        int paramPlanId = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        int paramOrgRsvDate = 0;
        String paramRsvNo = "";
        String paramMode = "";
        String paramUsrKbn = "";
        String targetYm = "";
        String paramHapihoteId = "";
        String paramHapihoteMail = "";
        String[] paramHapihoteMailList = null;
        ArrayList<String> hapihoteMailList = new ArrayList<String>();
        String paramHapihoteTel = "";
        int ownerUserId = 0;
        boolean isLoginUser = false;
        String errMsg = "";
        boolean payMemberFlg = false;
        String userId = "";
        String maskCardNo = "";

        String checkHotelId = "";

        DataRsvReserveBasic drrb = new DataRsvReserveBasic();
        try
        {
            // お気に入りの登録されて呼び出された場合は、受け渡し項目はnullとなる
            // ホテルIDが、nullの場合はエラーページへ遷移させる
            checkHotelId = request.getParameter( "hidId" );

            if ( (checkHotelId == null) )
            {
                errMsg = Message.getMessage( "erro.30008" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            frm = new FormReservePersonalInfoPC();
            // 画面内容取得
            paramHotelId = Integer.parseInt( request.getParameter( "hidId" ) );
            paramPlanId = Integer.parseInt( request.getParameter( "hidPlanId" ) );
            paramSeq = Integer.valueOf( request.getParameter( "hidSeq" ) );
            paramRsvDate = Integer.parseInt( request.getParameter( "hidRsvDate" ) );
            paramOrgRsvDate = Integer.parseInt( request.getParameter( "orgRsvDate" ) );
            paramRsvNo = request.getParameter( "rsvNo" );
            paramMode = request.getParameter( "mode" );
            paramUsrKbn = request.getParameter( "userKbn" );
            paramHapihoteId = request.getParameter( "hapihoteUserId" );
            // メールアドレスの処理
            paramHapihoteMailList = request.getParameterValues( "hapihoteAddr" );
            if ( paramHapihoteMailList != null )
            {
                for( int l = 0 ; l < paramHapihoteMailList.length ; l++ )
                {
                    hapihoteMailList.add( paramHapihoteMailList[l] );
                }
            }
            paramHapihoteTel = request.getParameter( "hapihoteTel" );

            // ログインユーザー情報取得
            if ( paramUsrKbn.equals( ReserveCommon.USER_KBN_USER ) )
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

                payMemberFlg = dataLoginInfo_M2.isPaymemberFlag();
                if ( dataLoginInfo_M2.isMemberFlag() == true )
                {
                    // 存在する
                    isLoginUser = true;
                    userId = dataLoginInfo_M2.getUserId();
                }
            }
            else if ( paramUsrKbn.equals( ReserveCommon.USER_KBN_OWNER ) )
            {
                // オーナーの場合
                ownerUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
                if ( ownerUserId != 0 )
                {
                    // 存在する
                    isLoginUser = true;
                }
                userId = Integer.toString( ownerUserId );
                payMemberFlg = true;
            }

            if ( (isLoginUser == false) || ((!paramUsrKbn.equals( ReserveCommon.USER_KBN_USER )) && (!paramUsrKbn.equals( ReserveCommon.USER_KBN_OWNER ))) )
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

            frm.setLoginUserId( paramHapihoteId );
            frm.setLoginMailAddr( paramHapihoteMail );
            // メールアドレス
            frm.setLoginMailAddrList( hapihoteMailList );
            frm.setLoginTel( paramHapihoteTel );
            frm.setSelHotelID( paramHotelId );
            frm.setSelPlanID( paramPlanId );
            frm.setSelSeq( paramSeq );
            frm.setOrgReserveDate( paramOrgRsvDate );
            frm.setSelRsvDate( paramRsvDate );
            frm.setReserveNo( paramRsvNo );
            frm.setMode( paramMode );
            frm.setLoginUserKbn( paramUsrKbn );
            frm.setPaymemberFlg( payMemberFlg );
            frm.setSelHotelID( paramHotelId );
            frm.setSelPlanID( paramPlanId );

            frm = setFormData( request, frm );
            // 予約可能かチェック
            // 無料会員＆有料会員の区別をするためにプランデータ取得
            rsvCmm = new ReserveCommon();
            if ( paramRsvDate > 0 )
            {
                frm = rsvCmm.getPlanData( frm );

                int curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                int curTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                int checkDateST = DateEdit.addDay( curDate, (frm.getRsvEndDate()) );
                int checkDateED = DateEdit.addDay( curDate, (frm.getRsvStartDate()) );
                int checkDatePremiumED = DateEdit.addDay( curDate, (frm.getRsvStartDatePremium()) );
                int checkDatePremiumST = DateEdit.addDay( curDate, (frm.getRsvEndDatePremium()) );

                // 予約日のチェック
                errMsg = rsvCmm.checkReserveDuplicate( paramHapihoteId, paramRsvDate );
                frm.setErrMsg( errMsg );

                // 予約可能かチェック
                if ( (paramRsvDate > checkDateST && paramRsvDate < checkDateED &&
                        paramRsvDate >= frm.getSalesStartDay() && paramRsvDate <= frm.getSalesEndDay()) ||
                        (paramRsvDate == checkDateST && curTime <= frm.getRsvEndTime() &&
                                paramRsvDate >= frm.getSalesStartDay() && paramRsvDate <= frm.getSalesEndDay()) ||
                        (paramRsvDate == checkDateED && curTime >= frm.getRsvStartTime() &&
                                paramRsvDate >= frm.getSalesStartDay() && paramRsvDate <= frm.getSalesEndDay()) )
                {
                }
                else
                {
                    if ( ((paramRsvDate > checkDatePremiumST) && (paramRsvDate < checkDatePremiumED) &&
                            (paramRsvDate >= frm.getSalesStartDay()) && (paramRsvDate <= frm.getSalesEndDay())) ||
                            (paramRsvDate == checkDatePremiumST && curTime <= frm.getRsvEndTime()) ||
                            (paramRsvDate == checkDatePremiumED && curTime >= frm.getRsvStartTime()) )
                    {
                        // プレミアム会員が有効の場合は表示を変更
                        String err1 = "本プランはハピホテプレミアム会員様のみ予約可能です。";
                        errMsg = err1;
                    }
                    else
                    {
                        // 仮オープンじゃないとエラー
                        if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                        {
                            errMsg = "予約受付の期間外です。お手数ですが、もう一度最初からやり直してください。";
                        }
                    }
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

            // 予約人数の範囲チェック
            errMsg = rsvCmm.checkAdultChildNum( paramHotelId, paramPlanId, frm.getSelNumAdult(), frm.getSelNumChild() );
            if ( errMsg.trim().length() != 0 )
            {
                // チェックNGの場合、エラーページへ遷移する。
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // 過去に予約データが存在すれば住所などはセットする
            if ( frm.getZipCd3().equals( "" ) && frm.getZipCd4().equals( "" ) &&
                    frm.getSelPrefId() == 0 && frm.getSelJisCd() == 0 &&
                    frm.getAddress3().equals( "" ) && frm.getLastName().equals( "" ) &&
                    frm.getFirstName().equals( "" ) && frm.getLastNameKana().equals( "" ) &&
                    frm.getFirstNameKana().equals( "" ) && frm.getTel().equals( "" ) )
            {
                DataRsvReserve rsv = new DataRsvReserve();
                rsv.getDataByUserId( userId );
                if ( !rsv.getReserveNo().equals( "" ) )
                {
                    frm.setZipCd3( rsv.getZipCd().substring( 0, 3 ) );
                    frm.setZipCd4( rsv.getZipCd().substring( 3, rsv.getZipCd().length() ) );
                    frm.setSelPrefId( rsv.getPrefCode() );
                    frm.setSelJisCd( rsv.getJisCode() );
                    frm.setAddress3( rsv.getAddress3() );
                    frm.setLastName( rsv.getNameLast() );
                    frm.setFirstName( rsv.getNameFirst() );
                    frm.setLastNameKana( rsv.getNameLastKana() );
                    frm.setFirstNameKana( rsv.getNameFirstKana() );
                    frm.setTel( rsv.getTel1() );
                }
            }

            // 押されたボタンの判別
            url = "/reserve/reserve_personal_info.jsp";
            if ( request.getParameter( "btnRsvCreditReg" ) != null )
            {

                // tashiro追加
                // 日付が空白で入っていたためエラーになっていた
                targetYm = request.getParameter( "calDate" );
                frm.setLastMonth( DateEdit.addMonth( Integer.parseInt( targetYm ), -1 ) );
                frm.setNextMonth( DateEdit.addMonth( Integer.parseInt( targetYm ), 1 ) );
                frm.setSelCalYm( Integer.parseInt( targetYm ) );

                // クレジットカード登録画面へ遷移
                if ( rsvCmm.isInputCheck( frm, MODE_CHECK_PC ) == false )
                {
                    // エラーあり
                    frm = setViewData( frm );
                    frm = setCalenderInfo( frm, targetYm );
                    frm.setReserveNo( paramRsvNo );

                    if ( hapihoteMailList.size() == 0 )
                    {
                        frm.setErrMsg( frm.getErrMsg() + "連絡先メールアドレスが登録されていません。</br>" );
                    }
                    frm = setViewData( frm );
                    request.setAttribute( "dsp", frm );
                }
                else
                {
                    // エラーなし
                    // frm = setViewData( frm );
                    url = "/reserve/reserve_cancel_credit_info.jsp";
                    request.setAttribute( "dsp", frm );
                }
            }
            else if ( request.getParameter( "btnRsvConfirm" ) != null || request.getParameter( "btnRsvCreditConfirm" ) != null )
            {
                if ( request.getParameter( "btnRsvCreditConfirm" ) != null )
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
                        // エラーなので画面を戻す
                        frm.setErrMsg( errMsg );
                        frm = setViewData( frm );
                        url = "/reserve/reserve_cancel_credit_info.jsp";
                        request.setAttribute( "dsp", frm );
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
                        requestDispatcher.forward( request, response );
                        return;
                    }

                    // カード番号と有効期限セット
                    if ( request.getParameter( "card_no" ) != null )
                    {
                        frm.setCardno( request.getParameter( "card_no" ) );
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
                        frm.setDispcardno( maskCardNo );
                    }
                    if ( request.getParameter( "expire_month" ) != null && !request.getParameter( "expire_month" ).equals( "" ) )
                    {
                        frm.setExpiremonth( Integer.parseInt( request.getParameter( "expire_month" ) ) );
                    }
                    if ( request.getParameter( "expire_year" ) != null && !request.getParameter( "expire_year" ).equals( "" ) )
                    {
                        frm.setExpireyear( Integer.parseInt( request.getParameter( "expire_year" ) ) );
                    }
                }

                // ▼予約を確認する
                // パラメータの整合性チェック
                errMsg = rsvCmm.checkParam( paramHotelId, paramPlanId, paramSeq, paramRsvDate, paramUsrKbn, paramMode );
                if ( errMsg.trim().length() != 0 )
                {
                    // 仮オープンじゃないとエラー
                    if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                    {

                        // チェックNGの場合、エラーページへ遷移する。
                        request.setAttribute( "errMsg", errMsg );
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                        requestDispatcher.forward( request, response );
                        return;
                    }
                }

                // データチェック
                FormReserveSheetPC frmSheetPC = new FormReserveSheetPC();
                frmSheetPC.setSelHotelId( paramHotelId );
                frmSheetPC.setSelPlanId( paramPlanId );
                frmSheetPC.setRsvNo( paramRsvNo );
                frmSheetPC.setSeq( paramSeq );
                frmSheetPC.setMode( paramMode );
                frmSheetPC.setRsvDate( paramRsvDate );
                frmSheetPC.setUserId( userId );
                frmSheetPC = rsvCmm.chkDspMaster( frmSheetPC );
                if ( frmSheetPC.getErrMsg().trim().length() != 0 )
                {
                    // チェックNGの場合、エラーページへ遷移する。
                    request.setAttribute( "errMsg", frmSheetPC.getErrMsg() );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }

                targetYm = request.getParameter( "calDate" );
                frm.setLastMonth( DateEdit.addMonth( Integer.parseInt( targetYm ), -1 ) );
                frm.setNextMonth( DateEdit.addMonth( Integer.parseInt( targetYm ), 1 ) );
                frm.setSelCalYm( Integer.parseInt( targetYm ) );

                // 入力チェック
                // 2012/02/02 メールアドレスのリストがなかったらエラーとするように変更
                if ( rsvCmm.isInputCheck( frm, MODE_CHECK_PC ) == false || hapihoteMailList.size() == 0 )
                {
                    // エラーあり
                    frm = setViewData( frm );
                    frm = setCalenderInfo( frm, targetYm );
                    frm.setReserveNo( paramRsvNo );

                    // 開始時刻がなかったら
                    if ( frm.getCiTimeFromView().equals( "" ) != false )
                    {
                        frm = rsvCmm.getCiCoTime( frm );
                    }

                    if ( hapihoteMailList.size() == 0 )
                    {
                        frm.setErrMsg( frm.getErrMsg() + "連絡先メールアドレスが登録されていません。</br>" );
                    }
                    request.setAttribute( "dsp", frm );
                }
                else
                {
                    // エラーなし
                    // 仮テーブルへ登録
                    frmSheet = new FormReserveSheetPC();
                    frmSheet = setConfirm( frm );
                    frmSheet.setRsvNo( paramRsvNo );
                    frmSheet.setUserKbn( paramUsrKbn );
                    frmSheet.setMail( paramHapihoteMail );
                    frmSheet.setMailList( hapihoteMailList );
                    frmSheet.setMode( paramMode );
                    frmSheet.setOrgRsvDate( paramOrgRsvDate );
                    frmSheet.setLoginUserId( paramHapihoteId );
                    frmSheet.setLoginUserTel( paramHapihoteTel );
                    frmSheet.setLoginUserMail( paramHapihoteMail );
                    if ( paramMode.equals( ReserveCommon.MODE_UPD ) && request.getParameter( "btnRsvCreditConfirm" ) != null )
                    {
                        // クレジットカード更新時
                        frmSheet.setCreditUpdateFlag( true );
                    }
                    // カード番号を取得してマスク値をセット
                    if ( frm.getCardno().equals( "" ) && ReserveCommon.checkNoShowCreditHotel( paramHotelId ) == true )
                    {
                        DataRsvCredit rsvcredit = new DataRsvCredit();
                        rsvcredit.getData( paramRsvNo );
                        String dispCardNo = DecodeData.decodeString( "axpol ptmhyeeahl".getBytes( "8859_1" ), "s h t t i s n h ".getBytes( "8859_1" ), new String( rsvcredit.getCard_no() ) );
                        frm.setCardno( dispCardNo );
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
                        frm.setExpireyear( rsvcredit.getLimit_date() / 100 );
                        frm.setExpiremonth( rsvcredit.getLimit_date() % 100 );
                    }
                    frmSheet.setCardno( frm.getCardno() );
                    frmSheet.setDispcardno( maskCardNo );
                    frmSheet.setExpireMonth( frm.getExpiremonth() );
                    frmSheet.setExpireYear( frm.getExpireyear() );

                    url = "/reserve/reserve_sheet_PC.jsp";
                    request.setAttribute( "FORM_ReserveSheetPC", frmSheet );
                    request.setAttribute( "ViewMode", 1 );
                }
            }
            else if ( (request.getParameter( "calMode" ) != null) && (request.getParameter( "calMode" ).trim().length() != 0) )
            {
                // ▼ カレンダー前月・次月リンククリック
                if ( Integer.parseInt( request.getParameter( "calMode" ) ) == 1 )
                {
                    // ▼前月リンククリック
                    targetYm = request.getParameter( "lastMonth" );
                }
                else
                {
                    // ▼次月リンククリック
                    targetYm = request.getParameter( "nextMonth" );
                }

                frm.setLastMonth( DateEdit.addMonth( Integer.parseInt( targetYm ), -1 ) );
                frm.setNextMonth( DateEdit.addMonth( Integer.parseInt( targetYm ), 1 ) );
                frm.setSelCalYm( Integer.parseInt( targetYm ) );
                // frm.setSelRsvDate( Integer.parseInt( targetYm ) );

                // 画面内容再設定
                frm = setViewData( frm );
                frm = setCalenderInfo( frm, targetYm );
                request.setAttribute( "dsp", frm );
            }
            else
            {
                // ▼日付リンククリック
                // パラメータの整合性チェック
                errMsg = rsvCmm.checkParam( paramHotelId, paramPlanId, paramSeq, paramRsvDate, paramUsrKbn, paramMode );
                if ( errMsg.trim().length() != 0 )
                {
                    // 仮オープンじゃないとエラー
                    if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                    {
                        // チェックNGの場合、エラーページへ遷移する。
                        request.setAttribute( "errMsg", errMsg );
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                        requestDispatcher.forward( request, response );
                        return;
                    }
                }

                targetYm = request.getParameter( "calDate" );
                frm.setSelRsvDate( Integer.parseInt( targetYm ) );
                week = DateEdit.getWeekName( frm.getSelRsvDate() );
                rsvDate = String.valueOf( frm.getSelRsvDate() );
                rsvDateFormat = rsvDate.substring( 0, 4 ) + "年" + rsvDate.substring( 4, 6 ) + "月" + rsvDate.substring( 6, 8 ) + "日（" + week + "）";
                frm.setReserveDateFormat( rsvDateFormat );

                // 画面内容再設定
                frm = setViewData( frm );

                frm.setLastMonth( DateEdit.addMonth( Integer.parseInt( targetYm ), -1 ) );
                frm.setNextMonth( DateEdit.addMonth( Integer.parseInt( targetYm ), 1 ) );
                frm.setSelCalYm( Integer.parseInt( targetYm ) );

                request.setAttribute( "dsp", frm );
            }

            // 次画面へ遷移
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReservePersonalInfoPC.execute() ][hotelId = "
                    + paramHotelId + ",planId = " + paramPlanId + ",reserveDate = " + paramRsvDate + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReservePersonalInfoPC.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * 
     * カレンダー情報
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @param targetDate 対象年月
     * @return FormReservePersonalInfoPCオブジェクト
     * @throws Exception
     */
    private FormReservePersonalInfoPC setCalenderInfo(FormReservePersonalInfoPC frm, String targetDate) throws Exception
    {
        LogicOwnerRsvManageCalendar logicCalendar;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        String year = "";
        String month = "";
        int checkDateST = 0;
        int checkDateED = 0;
        int checkDatePremiumST = 0;
        int checkDatePremiumED = 0;
        int checkDateFreeST = 0;
        int checkDateFreeED = 0;
        int checkDateFreeAddED = 0;
        int dispCheckDateST = 0;
        int dispCheckDateED = 0;
        int curDate = 0;
        int curTime = 0;
        int minusDay = 0;
        int plusDay = 0;
        ReserveCommon rsvCmm = new ReserveCommon();
        logicCalendar = new LogicOwnerRsvManageCalendar();
        int numchild = 0;

        try
        {
            // 子供人数制御(4号営業ホテルは強制的に0)
            if ( rsvCmm.checkLoveHotelFlag( frm.getSelHotelID() ) == false )
            {
                numchild = frm.getSelNumChild();
            }
            // カレンダー情報取得
            year = targetDate.substring( 0, 4 );
            month = targetDate.substring( 4, 6 );
            if ( frm.getSelSeq() != 0 )
            {
                monthlyList = logicCalendar.getCalendarData( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelSeq(), Integer.parseInt( year + month ) );
            }
            else
            {
                monthlyList = logicCalendar.getCalendarData( frm.getSelHotelID(), frm.getSelPlanID(), Integer.parseInt( year + month ) );
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

            if ( checkDateFreeED == checkDatePremiumED )
            {
                // 無料会員と有料会員の終了日が同じ場合は、開始表示をなしにする
                frm.setRsvPremiumStartDayStr( "" );
            }
            else
            {
                frm.setRsvPremiumStartDayStr( String.format( "%1$04d年%2$02d月%3$02d日", checkDateFreeAddED / 10000, checkDateFreeAddED % 10000 / 100, checkDateFreeAddED % 10000 % 100 ) );
            }

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
            Logging.error( "[ActionReservePersonalInfoPC.setCalenderInfo() ] Exception", exception );
            throw new Exception( "[ActionReservePersonalInfoPC.setCalenderInfo] " + exception );
        }

        return(frm);
    }

    /**
     * 
     * 画面内容取得
     * 
     * @param HttpServletRequest
     * @param FormReservePersonalInfoPC オブジェクト
     * @return FormReservePersonalInfoPC オブジェクト
     */
    private FormReservePersonalInfoPC setFormData(HttpServletRequest request, FormReservePersonalInfoPC frm) throws Exception
    {

        String week = "";
        String rsvDate = "";
        String rsvDateFormat = "";
        int paramParkingUsed = 0;
        int paramParkingCnt = 0;
        int paramHiRoofCnt = 0;
        int paramEstTimeArrival = -99;
        // int estTime = -99;
        int paramNumAdult = 2;
        int paramNumChild = 0;
        int paramNumMan = 0;
        int paramNumWoman = 0;
        String paramLastNm = "";
        String paramFirstNm = "";
        String paramLastNmKana = "";
        String paramFirstNmKana = "";
        String paramZip3 = "";
        String paramZip4 = "";
        int paramPrefId = 0;
        int paramJusyoCd = 0;
        String paramAddr3 = "";
        String paramTel = "";
        int paramRemainds = 0;
        String paramMailAddr = "";
        String demandsHtml = "";
        int paramSelCalDate = 0;
        String paramRemarks = "";
        ReserveCommon rsvcomm = new ReserveCommon();

        // 画面内容取得
        week = DateEdit.getWeekName( frm.getSelRsvDate() );
        rsvDate = String.valueOf( frm.getSelRsvDate() );
        if ( rsvDate.compareTo( "0" ) == 0 )
        {
            rsvDateFormat = "日付未選択（下記のカレンダーより日付を選択して下さい）";
        }
        else
        {
            rsvDateFormat = rsvDate.substring( 0, 4 ) + "年" + rsvDate.substring( 4, 6 ) + "月" + rsvDate.substring( 6, 8 ) + "日（" + week + "）";
        }
        if ( (request.getParameter( "parking_used" ) != null) && (request.getParameter( "parking_used" ).toString().length() != 0) )
        {
            paramParkingUsed = Integer.valueOf( request.getParameter( "parking_used" ).toString() );
        }
        if ( (request.getParameter( "parking_count" ) != null) && (request.getParameter( "parking_count" ).toString().length() != 0) )
        {
            paramParkingCnt = Integer.valueOf( request.getParameter( "parking_count" ).toString() );
        }
        if ( (request.getParameter( "hiroof_count" ) != null) && (request.getParameter( "hiroof_count" ).toString().length() != 0) )
        {
            paramHiRoofCnt = Integer.valueOf( request.getParameter( "hiroof_count" ).toString() );
        }
        if ( (request.getParameter( "est_time_arrival" ) != null) && (request.getParameter( "est_time_arrival" ).toString().length() != 0) )
        {
            paramEstTimeArrival = Integer.parseInt( request.getParameter( "est_time_arrival" ) );
            // estTime = Integer.valueOf( paramEstTimeArrival );
        }
        if ( (request.getParameter( "num_adult" ) != null) && (request.getParameter( "num_adult" ).toString().length() != 0) )
        {
            paramNumAdult = Integer.parseInt( request.getParameter( "num_adult" ) );
        }
        // 4号営業ホテルはセットしない
        if ( rsvcomm.checkLoveHotelFlag( frm.getSelHotelID() ) == false )
        {
            if ( (request.getParameter( "num_child" ) != null) && (request.getParameter( "num_child" ).toString().length() != 0) )
            {
                paramNumChild = Integer.parseInt( request.getParameter( "num_child" ) );
            }
        }
        if ( (request.getParameter( "num_man" ) != null) && (request.getParameter( "num_man" ).toString().length() != 0) )
        {
            paramNumMan = Integer.parseInt( request.getParameter( "num_man" ) );
        }
        if ( (request.getParameter( "num_woman" ) != null) && (request.getParameter( "num_woman" ).toString().length() != 0) )
        {
            paramNumWoman = Integer.parseInt( request.getParameter( "num_woman" ) );
        }
        if ( (request.getParameter( "name_last" ) != null) && (request.getParameter( "name_last" ).toString().length() != 0) )
        {
            paramLastNm = request.getParameter( "name_last" ).toString();
        }
        if ( (request.getParameter( "name_first" ) != null) && (request.getParameter( "name_first" ).toString().length() != 0) )
        {
            paramFirstNm = request.getParameter( "name_first" ).toString();
        }
        if ( (request.getParameter( "name_last_kana" ) != null) && (request.getParameter( "name_last_kana" ).toString().length() != 0) )
        {
            paramLastNmKana = request.getParameter( "name_last_kana" ).toString();
        }
        if ( (request.getParameter( "name_first_kana" ) != null) && (request.getParameter( "name_first_kana" ).toString().length() != 0) )
        {
            paramFirstNmKana = request.getParameter( "name_first_kana" ).toString();
        }
        if ( (request.getParameter( "zip_cd3" ) != null) && (request.getParameter( "zip_cd3" ).toString().length() != 0) )
        {
            paramZip3 = request.getParameter( "zip_cd3" );
        }
        if ( (request.getParameter( "zip_cd4" ) != null) && (request.getParameter( "zip_cd4" ).toString().length() != 0) )
        {
            paramZip4 = request.getParameter( "zip_cd4" );
        }
        if ( (request.getParameter( "prefId" ) != null) && (request.getParameter( "prefId" ).toString().length() != 0) )
        {
            paramPrefId = Integer.parseInt( request.getParameter( "prefId" ) );
        }
        if ( (request.getParameter( "address2" ) != null) && (request.getParameter( "address2" ).toString().length() != 0) )
        {
            paramJusyoCd = Integer.parseInt( request.getParameter( "address2" ).toString() );
        }
        if ( (request.getParameter( "address3" ) != null) && (request.getParameter( "address3" ).toString().length() != 0) )
        {
            paramAddr3 = request.getParameter( "address3" ).toString();
        }
        if ( (request.getParameter( "tel" ) != null) && (request.getParameter( "tel" ).toString().length() != 0) )
        {
            paramTel = request.getParameter( "tel" ).toString();
        }
        if ( (request.getParameter( "remainder" ) != null) && (request.getParameter( "remainder" ).toString().length() != 0) )
        {
            paramRemainds = 1;
        }
        if ( (request.getParameter( "other_mail_addr" ) != null) && (request.getParameter( "other_mail_addr" ).toString().length() != 0) )
        {
            paramMailAddr = request.getParameter( "other_mail_addr" ).toString();
        }
        if ( (request.getParameter( "demands" ) != null) && (request.getParameter( "demands" ).toString().length() != 0) )
        {
            demandsHtml = request.getParameter( "demands" ).trim();
        }
        if ( (request.getParameter( "selCalRsvDate" ) != null) && (request.getParameter( "selCalRsvDate" ).toString().length() != 0) )
        {
            paramSelCalDate = Integer.parseInt( request.getParameter( "demands" ) );
        }
        if ( (request.getParameter( "remarks" ) != null) && (request.getParameter( "remarks" ).toString().length() != 0) )
        {
            paramRemarks = request.getParameter( "remarks" );
        }

        // フォームにセット
        frm.setReserveDateFormat( rsvDateFormat );
        frm.setSelParkingUsedKbn( paramParkingUsed );
        frm.setParkingUsedKbnInit( paramParkingUsed );
        frm.setSelParkingCount( paramParkingCnt );
        frm.setSelHiRoofCount( paramHiRoofCnt );
        frm.setSelEstTimeArrival( paramEstTimeArrival );
        frm.setSelNumAdult( paramNumAdult );
        frm.setSelNumChild( paramNumChild );
        frm.setSelNumMan( paramNumMan );
        frm.setSelNumWoman( paramNumWoman );
        frm.setLastName( paramLastNm );
        frm.setFirstName( paramFirstNm );
        frm.setLastNameKana( paramLastNmKana );
        frm.setFirstNameKana( paramFirstNmKana );
        frm.setZipCd3( paramZip3 );
        frm.setZipCd4( paramZip4 );
        frm.setSelPrefId( paramPrefId );
        frm.setSelJisCd( paramJusyoCd );
        frm.setAddress3( paramAddr3 );
        frm.setTel( paramTel );
        frm.setRemainder( paramRemainds );
        frm.setRemainderMailAddr( paramMailAddr );
        frm.setDemands( demandsHtml );
        frm.setSelCalDate( paramSelCalDate );
        frm.setRemarks( paramRemarks );

        // 必須オプション取得
        frm = getOptionImpFormData( request, frm );

        // 通常オプション取得
        frm = getOptionSubFormData( request, frm );

        return(frm);
    }

    /**
     * 
     * 画面で選択されている必須オプション取得
     * 
     * @param request HttpServletRequest オブジェクト
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getOptionImpFormData(HttpServletRequest request, FormReservePersonalInfoPC frm) throws Exception
    {
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<Integer> selOptIdList = new ArrayList<Integer>();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        ArrayList<FormReserveOptionSubImp> frmOptSubImpList = new ArrayList<FormReserveOptionSubImp>();

        try
        {
            // プランIDで対象のオプションIDを取得
            optIdList = logic.getPlanOption( frm.getSelHotelID(), frm.getSelPlanID(), 1 );

            Logging.info( "[ActionReservePersonalInfoPC.getOptionImpFormData] hotelid=" + frm.getSelHotelID() + ",planid=" + frm.getSelPlanID() + ",optIdList.size()=" + optIdList.size() );

            // オプションのリスト取得
            frmOptSubImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );

            // 画面で選択されているサブオプションIDを取得
            for( int i = 0 ; i < optIdList.size() ; i++ )
            {
                if ( request.getParameter( "optImp" + optIdList.get( i ) ) != null )
                {
                    selOptIdList.add( Integer.parseInt( request.getParameter( "optImp" + optIdList.get( i ) ) ) );
                }
                else
                {
                    selOptIdList.add( 1 );
                    for( int j = 0 ; j < frmOptSubImpList.size() ; j++ )
                    {
                        if ( frmOptSubImpList.get( j ).getOptIdList().get( 0 ) == optIdList.get( i ) )
                        {
                            // 最初のサブオプションIDをセット
                            selOptIdList.add( frmOptSubImpList.get( j ).getOptSubIdList().get( 0 ) );
                            break;
                        }
                    }

                }
            }

            // フォームにセット
            frm.setFrmOptSubImpList( frmOptSubImpList );
            frm.setSelOptionImpSubIdList( selOptIdList );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionReservePersonalInfoPC.getOptionImpFormData] Exception=" + e.toString() );
            throw e;
        }

        return(frm);
    }

    /**
     * 
     * 画面で選択されている通常オプション取得
     * 
     * @param request HttpServletRequest オブジェクト
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getOptionSubFormData(HttpServletRequest request, FormReservePersonalInfoPC frm) throws Exception
    {
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<Integer> optNumList = new ArrayList<Integer>();
        ArrayList<String> optRemarksList = new ArrayList<String>();
        ArrayList<Integer> optUnitPriceList = new ArrayList<Integer>();

        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();

        try
        {
            // プランIDで対象のオプションIDを取得
            optIdList = logic.getPlanOption( frm.getSelHotelID(), frm.getSelPlanID(), 0 );

            // 画面の通常オプション情報を取得
            for( int i = 0 ; i < optIdList.size() ; i++ )
            {
                if ( request.getParameter( "optNum" + optIdList.get( i ) ) == null )
                {
                    optNumList.add( -1 );
                    optRemarksList.add( "" );
                    optUnitPriceList.add( 0 );
                }
                else
                {
                    optNumList.add( Integer.parseInt( request.getParameter( "optNum" + optIdList.get( i ) ) ) );
                    optRemarksList.add( request.getParameter( "optSubRemarks" + optIdList.get( i ) ) );
                    optUnitPriceList.add( Integer.parseInt( request.getParameter( "unitPrice" + optIdList.get( i ) ) ) );
                }
            }

            // フォームにセット
            frm.setSelOptSubIdList( optIdList );
            frm.setSelOptSubNumList( optNumList );
            frm.setSelOptSubRemarksList( optRemarksList );
            frm.setSelOptSubUnitPriceList( optUnitPriceList );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionReservePersonalInfoPC.getOptionSubFormData] Exception=" + e.toString() );
            throw e;
        }

        return(frm);
    }

    /**
     * 
     * 画面再表示
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC setViewData(FormReservePersonalInfoPC frm) throws Exception
    {
        String week = "";
        String rsvDate = "";
        String orgRsvDate = "";
        String reserveDateFormat = "";
        int currentDate = 0;
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();
        ArrayList<FormReserveOptionSubImp> frmOptSubImpList = new ArrayList<FormReserveOptionSubImp>();

        // 日付の表示形式
        week = DateEdit.getWeekName( frm.getSelRsvDate() );
        rsvDate = String.valueOf( frm.getSelRsvDate() );
        orgRsvDate = String.valueOf( frm.getOrgReserveDate() );
        if ( rsvDate.compareTo( "0" ) == 0 || orgRsvDate.compareTo( "0" ) == 0 )
        {
            reserveDateFormat = "日付未選択（下記のカレンダーより日付を選択して下さい）";
        }
        else
        {
            reserveDateFormat = rsvDate.substring( 0, 4 ) + "年" + rsvDate.substring( 4, 6 ) + "月" + rsvDate.substring( 6, 8 ) + "日（" + week + "）";
        }
        frm.setReserveDateFormat( reserveDateFormat );
        frm.setReserveDateDay( frm.getSelRsvDate() );

        // ホテル基本情報取得
        frm = rsvCmm.getHotelData( frm );

        // 予約基本より駐車場利用区分、駐車場利用台数を取得
        frm = rsvCmm.getParking( frm );

        // プラン名、プランPR、人数リスト、駐車場リストの作成
        frm = rsvCmm.getPlanData( frm );

        // プラン別料金マスタよりチェックイン開始、終了時刻、料金を取得
        frm = rsvCmm.getCiCoTime( frm, frm.getSelNumAdult(), frm.getSelNumChild(), frm.getSelRsvDate() );

        // 都道府県情報取得
        frm = getPref( frm );

        // 設備情報
        frm = getEquip( frm );

        // システム日付・時刻の取得
        currentDate = Integer.valueOf( DateEdit.getDate( 2 ) );
        frm.setCurrentDate( currentDate );

        // カレンダー情報取得
        if ( frm.getSelRsvDate() != 0 )
        {
            frm = setCalenderInfo( frm, Integer.toString( frm.getSelRsvDate() ) );
        }
        // 必須オプション情報取得
        frmOptSubImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );
        frm.setFrmOptSubImpList( frmOptSubImpList );

        // 通常オプション情報取得
        frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelRsvDate() );
        frm.setFrmOptSub( frmOptSub );

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
     * 予約確認画面へ遷移
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReserveSheetPC オブジェクト
     */
    private FormReserveSheetPC setConfirm(FormReservePersonalInfoPC frm) throws Exception
    {
        int optChargeTotal = 0;
        int basicChargeTotal = 0;
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataRsvPlan dataPlan = new DataRsvPlan();

        // ▼フォームに必要な値をセット
        // ホテル基本情報の設定
        frm = rsvCmm.getHotelData( frm );

        // 都道府県名、市区町村名取得
        frm = setJusyo( frm );

        // プランマスタからチェックイン、チェックアウト、料金を取得
        frm = rsvCmm.getCiCoTime( frm );

        // 料金情報の設定
        frm = setCharge( frm );

        // ハピーポイント
        frm = setHapyPoint( frm );

        // ワークテーブルへ登録
        logic.setFrm( frm );
        logic.insReserveWork();
        frm = logic.getFrm();

        // 必須オプション情報をワークテーブルへ登録
        frm = setRsvWorkOptionImp( frm, logic );

        // 通常オプション情報をワークテーブルへ登録
        frm = setRsvWorkOptionSub( frm, logic );

        // 通常オプション合計金額を取得
        optChargeTotal = logic.getOptionChargeTotal( frm.getWorkId() );

        // 基本料金取得
        basicChargeTotal = logic.getBasicChargeTotal( frm.getWorkId() );

        // 予約ワークテーブルへオプション合計、総合計の反映
        logic.updRsvWorkChargeTotal( frm.getWorkId(), optChargeTotal, optChargeTotal + basicChargeTotal );

        // ワークテーブルからデータを取得
        frm.getWorkId();
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();
        frmSheet.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );
        frmSheet.setWorkId( frm.getWorkId() );

        // オプションワークテーブルから必須オプションデータを取得
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_IMP );

        // オプションワークテーブルから通常オプションデータを取得
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

        // 提供区分を取得
        dataPlan.getData( frm.getSelHotelID(), frm.getSelPlanID() );
        frmSheet.setOfferKind( dataPlan.getOfferKind() );

        return(frmSheet);
    }

    /**
     * 
     * 必須オプション情報を予約仮テーブルへ登録
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return なし
     */
    private FormReservePersonalInfoPC setRsvWorkOptionImp(FormReservePersonalInfoPC frm, LogicReservePersonalInfoPC logic) throws Exception
    {
        // ワークテーブル削除
        logic.deleteRsvOptionWork( frm.getWorkId() );

        for( int i = 0 ; i < frm.getFrmOptSubImpList().size() ; i++ )
        {
            FormReserveOptionSubImp frmOptSubImp = frm.getFrmOptSubImpList().get( i );

            if ( (frmOptSubImp.getOptIdList() != null) && (frmOptSubImp.getOptIdList().size() != 0) )
            {
                // オプションあり
                logic.insRsvOptionWork( frmOptSubImp.getOptIdList().get( 0 ), frm.getSelOptionImpSubIdList().get( i ), -1, 0, 0, "", ReserveCommon.QUANTITY_NEED );
            }
        }

        return(frm);
    }

    /**
     * 
     * 通常オプション情報を予約仮テーブルへ登録
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return なし
     */
    private FormReservePersonalInfoPC setRsvWorkOptionSub(FormReservePersonalInfoPC frm, LogicReservePersonalInfoPC logic) throws Exception
    {
        int num = 0;
        int unitPrice = 0;
        int chargeTotal = 0;
        int quantityFlg = 0;
        String remarks = "";

        for( int i = 0 ; i < frm.getSelOptSubIdList().size() ; i++ )
        {
            num = 0;
            unitPrice = 0;
            chargeTotal = 0;
            quantityFlg = 0;
            remarks = "";

            if ( frm.getSelOptSubNumList().get( i ) == 0 )
            {
                // 不要の場合の数量フラグ設定
                quantityFlg = ReserveCommon.QUANTITY_NEED_NO;
            }

            if ( frm.getSelOptSubNumList().get( i ) > 0 )
            {
                // 数量選択している場合
                num = frm.getSelOptSubNumList().get( i );
                unitPrice = frm.getSelOptSubUnitPriceList().get( i );
                chargeTotal = num * unitPrice;
                remarks = frm.getSelOptSubRemarksList().get( i );
            }

            logic.insRsvOptionWork( frm.getSelOptSubIdList().get( i ), 1, num, unitPrice, chargeTotal, remarks, quantityFlg );
        }

        return(frm);
    }

    /**
     * 
     * 料金情報算出
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC setCharge(FormReservePersonalInfoPC frm)
    {
        int chargeId = 0;
        int adultTwoPrice = 0;
        int adultOnePrice = 0;
        int adultAddPrice = 0;
        int childAddPrice = 0;
        int sumAdult = 0;
        int sumChild = 0;
        int baseChargeTotal = 0;
        int chargeTotal = 0;
        int optCharge = 0;
        boolean ret = false;

        // 日別料金の取得
        DataRsvDayCharge dataDayCharge = new DataRsvDayCharge();
        dataDayCharge.getData( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelRsvDate() );
        chargeId = dataDayCharge.getChargeModeId();

        // プラン別料金の取得
        DataRsvPlanCharge dataPlanCharge = new DataRsvPlanCharge();
        ret = dataPlanCharge.getData( frm.getSelHotelID(), frm.getSelPlanID(), chargeId );
        if ( ret )
        {
            adultTwoPrice = dataPlanCharge.getAdultTwoCharge();
            adultOnePrice = dataPlanCharge.getAdultOneCharge();
            adultAddPrice = dataPlanCharge.getAdultAddCharge();
            childAddPrice = dataPlanCharge.getChildAddCharge();
        }
        // 基本料金算出
        switch( frm.getSelNumAdult() )
        {
            case 1:
                sumAdult = adultOnePrice;
                break;
            case 2:
                sumAdult = adultTwoPrice;
                break;
            default:
                sumAdult = adultTwoPrice + (adultAddPrice * (frm.getSelNumAdult() - 2));
                break;
        }
        // 子供料金
        sumChild = (childAddPrice * frm.getSelNumChild());
        baseChargeTotal = sumAdult + sumChild;

        // 総合計 = オプション料金 + 基本料金
        chargeTotal = optCharge + baseChargeTotal;

        // フォームにセット
        frm.setBaseChargeTotal( baseChargeTotal );
        frm.setOptionCharge( optCharge );
        frm.setChargeTotal( chargeTotal );

        return(frm);
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
        LogicReservePersonalInfoPC logic;
        DataMasterPoint dmp = new DataMasterPoint();

        if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) )
        {
            // 更新の場合は、予約情報のaddPointを取得
            logic = new LogicReservePersonalInfoPC();
            calPoint = logic.getAddPoint( frm.getSelHotelID(), frm.getReserveNo() );
            frm.setHapyPoint( calPoint );
            return(frm);
        }

        // 有料会員の場合
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
            calPoint = (frm.getBaseChargeTotal() * point) / 100;
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

    /**
     * 
     * 都道府県名セット
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC setJusyo(FormReservePersonalInfoPC frm)
    {
        String prefNm = "";
        String cityNm = "";

        // 都道府県名取得
        DataMasterPref dataPref = new DataMasterPref();
        dataPref.getData( frm.getSelPrefId() );
        prefNm = dataPref.getName();

        // 市区町村名取得
        DataMasterCity dataCity = new DataMasterCity();
        dataCity.getData( frm.getSelJisCd() );
        cityNm = dataCity.getName();

        // フォームにセット
        frm.setAddress1( prefNm );
        frm.setAddress2( cityNm );

        return(frm);
    }

}
