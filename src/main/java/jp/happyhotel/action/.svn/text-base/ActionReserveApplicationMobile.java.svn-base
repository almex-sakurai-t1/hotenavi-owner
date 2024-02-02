package jp.happyhotel.action;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvDayCharge;
import jp.happyhotel.data.DataRsvPlanCharge;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;

/**
 * 
 * 携帯版プラン申込画面 Action Class
 */

public class ActionReserveApplicationMobile extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int type = 1;
        String paramUidLink = "";
        String userId = "";
        String loginMail = "";
        ArrayList<String> loginMailList = new ArrayList<String>();
        FormReserveSheetPC frmSheet;
        FormReservePersonalInfoPC frmInfo;
        ReserveCommon rsvCmm = new ReserveCommon();
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        DataLoginInfo_M2 dataLoginInfo_M2;
        String url = "";
        String carrierUrl = "";
        String rsvUserId = "";

        boolean inputCheck = false;
        int paramHotelId = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        int paramPlanId = 0;
        String paramMode = "";
        String errMsg = "";
        String checkHotelId = "";

        try
        {
            frmInfo = new FormReservePersonalInfoPC();
            frmSheet = new FormReserveSheetPC();

            // キャリアを取得
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

            // お気に入りに登録されて呼び出された場合は、受け渡し項目はnullとなる
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
            frmInfo.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
            if ( request.getParameter( "mode" ) != null )
            {
                paramMode = request.getParameter( "mode" );
            }

            // 無料会員＆有料会員の区別をするためにプランデータ取得
            ReserveCommon rsvcomm = new ReserveCommon();
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

            if ( request.getParameter( "btnNext" ) != null )
            {
                // ▼次へボタンクリック
                // 入力チェック
                inputCheck = rsvCmm.isInputCheck( frmInfo, ReserveCommon.INP_CHECK_PLAN_M );
                if ( inputCheck == false )
                {
                    // エラーあり時
                    frmInfo = getViewData( frmInfo );
                    request.setAttribute( "dsp", frmInfo );
                    url = carrierUrl + "reserve_application.jsp";
                }

                // データのチェック
                if ( inputCheck == true )
                {
                    frmSheet = rsvCmm.margeFormSheetPC( frmSheet, frmInfo );
                    // ワークテーブルに登録されている通常オプション情報取得
                    frmSheet.setWorkId( frmInfo.getWorkId() );
                    frmSheet = logicCheckIn.getWorkOptionQuantity( frmSheet );
                    frmSheet = rsvCmm.chkDspMaster( frmSheet );
                    if ( frmSheet.getErrMsg().trim().length() == 0 )
                    {
                        // エラーなし
                        inputCheck = true;
                    }
                    else
                    {
                        // エラーあり
                        inputCheck = false;
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                    }
                }

                // 新規登録時以外は、ステータスチェック
                if ( (inputCheck == true) && (!frmInfo.getMode().equals( ReserveCommon.MODE_INS )) )
                {
                    inputCheck = rsvCmm.checkStatus( frmInfo.getSelHotelID(), frmInfo.getReserveNo(), frmInfo.getStatus() );
                    if ( inputCheck == false )
                    {
                        // エラーあり
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", Message.getMessage( "warn.00021" ) );
                    }
                }

                if ( inputCheck == true )
                {
                    // 次画面遷移処理
                    frmInfo = registRsvWork( frmInfo );

                    // 次画面情報を取得
                    frmInfo = getPersonlInfo1Data( frmInfo );

                    // 過去に予約したことがあれば予約情報をセットする
                    DataRsvReserve rsv = new DataRsvReserve();
                    rsv.getDataByUserId( userId );
                    if ( !rsv.getReserveNo().equals( "" ) )
                    {
                        frmInfo.setZipCd3( rsv.getZipCd().substring( 0, 3 ) );
                        frmInfo.setZipCd4( rsv.getZipCd().substring( 3, rsv.getZipCd().length() ) );
                        frmInfo.setSelPrefId( rsv.getPrefCode() );
                        frmInfo.setSelJisCd( rsv.getJisCode() );
                        frmInfo.setAddress3( rsv.getAddress3() );
                        frmInfo.setLastName( rsv.getNameLast() );
                        frmInfo.setFirstName( rsv.getNameFirst() );
                        frmInfo.setLastNameKana( rsv.getNameLastKana() );
                        frmInfo.setFirstNameKana( rsv.getNameFirstKana() );
                        frmInfo.setTel( rsv.getTel1() );
                    }
                    // プラン別料金マスタよりチェックイン開始、終了時刻を取得
                    frmInfo = rsvCmm.getCiCoTime( frmInfo );

                    url = carrierUrl + "reserve_personal_info1.jsp";
                    request.setAttribute( "dsp", frmInfo );
                }
            }

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
            Logging.error( "[ActionReserveApplicationMobile.execute() ][hotelId = "
                    + paramHotelId + ",planId = " + paramPlanId + ",reserveDate = " + paramRsvDate + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveApplicationMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * 
     * 画面内容取得
     * 
     * @param req HttpServletRequest
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     * @throws Exception
     */
    private FormReservePersonalInfoPC getRequestData(HttpServletRequest req, FormReservePersonalInfoPC frm) throws Exception
    {
        int paramSelHotelId = 0;
        int paramSelPlanId = 0;
        int paramSelNumAdult = 0;
        int paramSelNumChild = 0;
        int paramSelNumMan = 0;
        int paramSelNumWoman = 0;
        int paramSelParkingUsedKbn = 0;
        int paramSelParkingCount = 0;
        int paramSelHiRoofCount = 0;
        int paramSelEstArrivaltime = -99;
        int paramRsvDate = 0;
        int paramSeq = 0;
        int paramWorkId = 0;
        String paramRsvNo = "";
        int paramHotelParking = 0;

        paramSelHotelId = Integer.parseInt( req.getParameter( "selHotelId" ) );
        paramSelPlanId = Integer.parseInt( req.getParameter( "selPlanId" ) );
        paramSelNumAdult = Integer.parseInt( req.getParameter( "num_adult" ) );
        if ( (req.getParameter( "num_man" ) != null) && (req.getParameter( "num_man" ).trim().length() != 0) )
        {
            paramSelNumMan = Integer.parseInt( req.getParameter( "num_man" ) );
        }
        if ( (req.getParameter( "num_woman" ) != null) && (req.getParameter( "num_woman" ).trim().length() != 0) )
        {
            paramSelNumWoman = Integer.parseInt( req.getParameter( "num_woman" ) );
        }
        if ( (req.getParameter( "num_child" ) != null) && (req.getParameter( "num_child" ).trim().length() != 0) )
        {
            paramSelNumChild = Integer.parseInt( req.getParameter( "num_child" ) );
        }
        if ( (req.getParameter( "est_time_arrival" ) != null) && (req.getParameter( "est_time_arrival" ).trim().length() != 0) )
        {
            paramSelEstArrivaltime = Integer.parseInt( req.getParameter( "est_time_arrival" ) );
        }
        if ( (req.getParameter( "parking_used" ) != null) && (req.getParameter( "parking_used" ).trim().length() != 0) )
        {
            paramSelParkingUsedKbn = Integer.parseInt( req.getParameter( "parking_used" ) );
        }
        if ( (req.getParameter( "parking_count" ) != null) && (req.getParameter( "parking_count" ).trim().length() != 0) )
        {
            paramSelParkingCount = Integer.parseInt( req.getParameter( "parking_count" ) );
        }
        if ( (req.getParameter( "hiroof_count" ) != null) && (req.getParameter( "hiroof_count" ).trim().length() != 0) )
        {
            paramSelHiRoofCount = Integer.parseInt( req.getParameter( "hiroof_count" ) );
        }
        paramRsvDate = Integer.parseInt( req.getParameter( "rsvDate" ) );
        paramSeq = Integer.parseInt( req.getParameter( "selSeq" ) );
        paramWorkId = Integer.parseInt( req.getParameter( "workId" ) );
        if ( (req.getParameter( "rsvNo" ) != null) && (req.getParameter( "rsvNo" ).trim().length() != 0) )
        {
            paramRsvNo = req.getParameter( "rsvNo" );
        }
        if ( (req.getParameter( "hotelParking" ) != null) && (req.getParameter( "hotelParking" ).trim().length() != 0) )
        {
            paramHotelParking = Integer.parseInt( req.getParameter( "hotelParking" ) );
        }

        frm.setMode( req.getParameter( "mode" ) );
        frm.setSelHotelID( paramSelHotelId );
        frm.setSelPlanID( paramSelPlanId );
        frm.setSelNumAdult( paramSelNumAdult );
        frm.setSelNumChild( paramSelNumChild );
        frm.setSelNumMan( paramSelNumMan );
        frm.setSelNumWoman( paramSelNumWoman );
        frm.setSelEstTimeArrival( paramSelEstArrivaltime );
        frm.setSelParkingUsedKbn( paramSelParkingUsedKbn );
        frm.setParkingUsedKbnInit( paramSelParkingUsedKbn );
        frm.setSelParkingCount( paramSelParkingCount );
        frm.setSelHiRoofCount( paramSelHiRoofCount );
        frm.setOrgReserveDate( paramRsvDate );
        frm.setSelRsvDate( paramRsvDate );
        frm.setSeq( paramSeq );
        frm.setSelSeq( paramSeq );
        frm.setWorkId( paramWorkId );
        frm.setReserveNo( paramRsvNo );
        frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );
        frm.setHotelParking( paramHotelParking );

        // 必須オプション取得
        frm = getOptionImpFormData( req, frm );

        // 通常オプション取得
        frm = getOptionSubFormData( req, frm );

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
            optIdList = logic.getPlanOption( frm.getSelHotelID(), frm.getSelPlanID(), ReserveCommon.OPTION_IMP );

            // 画面で選択されているサブオプションIDを取得
            for( int i = 0 ; i < optIdList.size() ; i++ )
            {
                selOptIdList.add( Integer.parseInt( request.getParameter( "optImp" + optIdList.get( i ) ) ) );
            }

            // オプションのリスト取得
            frmOptSubImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );

            // フォームにセット
            frm.setFrmOptSubImpList( frmOptSubImpList );
            frm.setSelOptionImpSubIdList( selOptIdList );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionReserveApplicationMobile.getOptionImpFormData] Exception=" + e.toString() );
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
            optIdList = logic.getPlanOption( frm.getSelHotelID(), frm.getSelPlanID(), ReserveCommon.OPTION_USUAL );

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
     * 画面表示内容再取得
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @return FormReserveSheetPCオブジェクト
     */
    private FormReservePersonalInfoPC getViewData(FormReservePersonalInfoPC frm) throws Exception
    {
        String week = "";
        String rsvDateFormat = "";
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();

        // ホテル名称取得
        frm = rsvCmm.getHotelData( frm );

        // 予約基本より駐車場利用区分、駐車場利用台数を取得
        frm = rsvCmm.getParking( frm );

        // 日別料金マスタより料金モードIDを取得
        frm = rsvCmm.getDayChargeMode( frm );

        // プラン名、プランPR、人数リスト、駐車場リストの作成
        frm = rsvCmm.getPlanData( frm );

        // 到着予定時刻の設定
        frm = rsvCmm.getCiCoTime( frm );

        // 部屋残数取得
        frm = rsvCmm.getRoomZanSuu( frm );

        // 予約日
        week = DateEdit.getWeekName( frm.getOrgReserveDate() );
        rsvDateFormat = Integer.toString( frm.getOrgReserveDate() ).substring( 0, 4 ) + "年"
                + Integer.toString( frm.getOrgReserveDate() ).substring( 4, 6 ) + "月"
                + Integer.toString( frm.getOrgReserveDate() ).substring( 6, 8 ) + "日(" + week + ")";
        frm.setReserveDateFormat( rsvDateFormat );

        // 通常オプション情報取得
        frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), frm.getOrgReserveDate() );
        frm.setFrmOptSub( frmOptSub );

        return(frm);

    }

    /**
     * 
     * ワークテーブルにデータを登録
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @return FormReservePersonalInfoPC オブジェクト
     */
    private FormReservePersonalInfoPC registRsvWork(FormReservePersonalInfoPC frmInfo) throws Exception
    {
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        ;
        String zip3 = "";
        String zip4 = "";
        int hotelParking = 0;
        int optChargeTotal = 0;
        int basicChargeTotal = 0;

        // 料金情報の設定
        frmInfo = setCharge( frmInfo );

        // ホテルの駐車場区分の退避
        hotelParking = frmInfo.getHotelParking();
        // 仮データ存在チェック
        if ( logic.isExistsRsvWork( frmInfo.getWorkId() ) == false )
        {
            // ▼存在しない場合は新規追加
            if ( frmInfo.getMode().equals( ReserveCommon.MODE_UPD ) )
            {
                // 更新の場合、登録済みデータを再取得
                frmInfo = setReserveData( frmInfo );
            }
            if ( hotelParking == ReserveCommon.PARKING_NO_INPUT )
            {
                frmInfo.setSelParkingUsedKbn( hotelParking );
            }
            logic.setFrm( frmInfo );
            logic.insReserveWork();
            frmInfo = logic.getFrm();

            // 必須オプション情報をワークテーブルへ登録
            frmInfo = setRsvWorkOptionImp( frmInfo, logic );

            // 通常オプション情報をワークテーブルへ登録
            frmInfo = setRsvWorkOption( frmInfo, logic );

            // 通常オプション合計金額を取得
            optChargeTotal = logic.getOptionChargeTotal( frmInfo.getWorkId() );

            // 基本料金取得
            basicChargeTotal = logic.getBasicChargeTotal( frmInfo.getWorkId() );

            // 予約ワークテーブルへオプション合計、総合計の反映
            logic.updRsvWorkChargeTotal( frmInfo.getWorkId(), optChargeTotal, optChargeTotal + basicChargeTotal );

            return(frmInfo);
        }

        // ▼存在する場合は更新
        // 仮テーブルのデータ取得
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frmInfo.getWorkId() );
        frmSheet = logic.getFrmSheet();

        // 画面表示用Formにセット
        frmInfo.setSelRsvDate( frmSheet.getRsvDate() );
        frmInfo.setLastName( frmSheet.getLastName() );
        frmInfo.setFirstName( frmSheet.getFirstName() );
        frmInfo.setLastNameKana( frmSheet.getLastNmKana() );
        frmInfo.setFirstNameKana( frmSheet.getFirstNmKana() );
        frmInfo.setSelPrefId( frmSheet.getPrefId() );
        frmInfo.setBaseChargeTotal( frmSheet.getBasicTotal() );
        frmInfo.setOptionCharge( frmSheet.getOptionChargeTotal() );
        frmInfo.setChargeTotal( frmSheet.getChargeTotal() );
        if ( frmSheet.getZip().trim().length() != 0 )
        {
            zip3 = frmSheet.getZip().substring( 0, 3 );
            zip4 = frmSheet.getZip().substring( 4 );
        }
        frmInfo.setZipCd3( zip3 );
        frmInfo.setZipCd4( zip4 );
        frmInfo.setLastName( frmSheet.getLastName() );
        frmInfo.setFirstName( frmSheet.getFirstName() );
        frmInfo.setLastNameKana( frmSheet.getLastNmKana() );
        frmInfo.setFirstNameKana( frmSheet.getFirstNmKana() );
        frmInfo.setSelPrefId( frmSheet.getPrefId() );
        frmInfo.setSelJisCd( frmSheet.getJisCd() );
        frmInfo.setAddress3( frmSheet.getAddress3() );
        frmInfo.setTel( frmSheet.getTel() );
        frmInfo.setRemainder( frmSheet.getReminder() );
        frmInfo.setRemainderMailAddr( frmSheet.getRemainderMail() );
        frmInfo.setDemands( frmSheet.getDemands() );
        frmInfo.setRemarks( frmSheet.getRemarks() );
        if ( hotelParking == ReserveCommon.PARKING_NO_INPUT )
        {
            frmInfo.setSelParkingUsedKbn( hotelParking );
        }

        // 更新処理実行
        logic.setFrm( frmInfo );
        logic.updReserveWork();

        // 必須オプション情報をワークテーブルへ登録
        frmInfo = setRsvWorkOptionImp( frmInfo, logic );

        // 通常オプション情報をワークテーブルへ登録
        frmInfo = setRsvWorkOption( frmInfo, logic );

        // 通常オプション合計金額を取得
        optChargeTotal = logic.getOptionChargeTotal( frmInfo.getWorkId() );

        // 基本料金取得
        basicChargeTotal = logic.getBasicChargeTotal( frmInfo.getWorkId() );

        // 予約ワークテーブルへオプション合計、総合計の反映
        logic.updRsvWorkChargeTotal( frmInfo.getWorkId(), optChargeTotal, optChargeTotal + basicChargeTotal );

        return(frmInfo);
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
                logic.insRsvOptionWork( frmOptSubImp.getOptIdList().get( 0 ), frm.getSelOptionImpSubIdList().get( i ), -1, 0, 0, "", 0 );
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
    private FormReservePersonalInfoPC setRsvWorkOption(FormReservePersonalInfoPC frm, LogicReservePersonalInfoPC logic) throws Exception
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
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();

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
        frm.setSelNumChild( frmSheet.getChildNum() );
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
        frm.setChargeTotalView( frmSheet.getChargeTotalView() );
        frm.setRemarks( frmSheet.getRemarks() );

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
        NumberFormat objNum = NumberFormat.getCurrencyInstance();

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
        frm.setBaseChargeView( objNum.format( baseChargeTotal ) );
        frm.setChargeTotalView( objNum.format( chargeTotal ) );

        objNum = null;
        dataDayCharge = null;
        dataPlanCharge = null;

        return(frm);
    }

    /**
     * 
     * 登録されている予約データを取得し、画面の値とマージ
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC setReserveData(FormReservePersonalInfoPC frm) throws Exception
    {
        int selNumAdult = 0;
        int selNumChild = 0;
        int selParking = 0;
        int selParkingCnt = 0;
        int selArrivalTime = 0;
        int selHiRoofCount = 0;

        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();

        // 画面の内容を退避
        selNumAdult = frm.getSelNumAdult();
        selNumChild = frm.getSelNumChild();
        selParking = frm.getSelParkingUsedKbn();
        selParkingCnt = frm.getSelParkingCount();
        selHiRoofCount = frm.getSelHiRoofCount();
        selArrivalTime = frm.getSelEstTimeArrival();

        // 予約データの抽出
        logic.setFrm( frm );
        logic.getReserveData();
        frm = logic.getFrm();

        // 画面内容とマージ
        frm.setSelNumAdult( selNumAdult );
        frm.setSelNumChild( selNumChild );
        frm.setSelParkingUsedKbn( selParking );
        frm.setSelParkingCount( selParkingCnt );
        frm.setSelHiRoofCount( selHiRoofCount );
        frm.setSelEstTimeArrival( selArrivalTime );

        return(frm);

    }
}
