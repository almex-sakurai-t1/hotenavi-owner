package jp.happyhotel.action;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlanCharge;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendarSub;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeManage;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeSub;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlanCharge;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeManage;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;

/**
 * 
 * プラン別料金設定画面Actionクラス
 */
public class ActionOwnerRsvPlanCharge extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg_MNG       = 5;
    private static final int  menuFlg_DETAIL    = 51;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerRsvPlanCharge frm;
        FormOwnerRsvPlanChargeSub frmSub;
        FormOwnerRsvPlanChargeManage frmManage;
        LogicOwnerRsvPlanCharge logic;
        LogicOwnerRsvPlanChargeManage logicManage;
        int paramHotelID = 0;
        int paramUserID = 0;
        String paramBtnNm = "";
        boolean inptCheckFlg = false;
        boolean checkDeposit = false;
        int chashDeposit = 0;
        int menuFlg = 0;
        String errMsg = "";
        String targetYYYYMM = "";
        String paraOwnerHotelID = "";
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        ReserveCommon rsvcomm = new ReserveCommon();
        FormReservePersonalInfoPC frmInfo = new FormReservePersonalInfoPC();
        int planView = 1; // 1:非掲載分は表示しない（default）

        // ホテル修正履歴で利用する
        ArrayList<FormOwnerRsvPlanChargeSub> frmSubList;
        int adjust_edit_id = 0;
        String adjust_edit_memo = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        int adminImediaFlag = 0;

        try
        {
            frmMenu = new FormOwnerBkoMenu();
            frm = new FormOwnerRsvPlanCharge();
            frmSub = new FormOwnerRsvPlanChargeSub();
            frmManage = new FormOwnerRsvPlanChargeManage();
            logic = new LogicOwnerRsvPlanCharge();
            logicManage = new LogicOwnerRsvPlanChargeManage();

            // 画面の値を取得し、フォームにセット
            paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ) );
            paramUserID = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramBtnNm = request.getParameter( "btnNm" );
            if ( paramBtnNm == null )
            {
                paramBtnNm = "";
            }
            setFrmParam( request, frm, 1 );

            // 非掲載分を掲載するか否か
            if ( request.getParameter( "planView" ) != null )
            {
                planView = Integer.parseInt( request.getParameter( "planView" ) );
            }

            // 表示権限関係取得
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( hotenaviId, paramUserID );
            adminflag = logicMenu.getAdminFlg( hotenaviId, paramUserID );
            imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, paramUserID );

            if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) != false )
            {
                adminImediaFlag = 1;
            }

            if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ページを閲覧する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( paramBtnNm.equals( "btnBack" ) == false && paramBtnNm.equals( "btnPreview" ) == false && paramBtnNm.equals( "btnPreview2" ) == false &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // コールセンターの更新は許可しない
                errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            Logging.info( paramBtnNm );
            if ( paramBtnNm.equals( "btnReg" ) )
            {
                // ▼登録
                // 入力チェック
                if ( isInputCheckMsg( frm, adminImediaFlag ) == true )
                {
                    // データ登録
                    logic.setFrm( frm );

                    // 最低予約金額取得
                    chashDeposit = logic.getChashDeposit( paramHotelID );
                    frm.setCashDeposit( chashDeposit );

                    // 新規追加・更新処理
                    logic.addPlanCharge();
                    adjust_edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_CHARGE_ADDUPD;
                    adjust_edit_memo = OwnerRsvCommon.ADJUST_MEMO_PLAN_CHARGE_ADDUPD;
                    inptCheckFlg = true;

                    // 更新後プラン販売停止曜日再取得
                    DataRsvPlan data = new DataRsvPlan();
                    data.getData( paramHotelID, frm.getSelPlanId() );
                    frm.setSalesStopWeekStatus( data.getSalesStopWeekStatus() );
                    frm.setSalesStopWeekView( OwnerRsvCommon.createSalesStopWeek( data.getSalesStopWeekStatus() ) );

                    // データ登録時にプラン別カレンダーの作成を行う
                    logic.addPlanCalendar( data.getSalesStopWeekStatus() );

                    // ホテル修正履歴の登録
                    frmSubList = frm.getFrmSubList();
                    for( int i = 0 ; i < frmSubList.size() ; i++ )
                    {
                        FormOwnerRsvPlanChargeSub frmSubWk = frm.getFrmSubList().get( i );
                        OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                frm.getUserId(), adjust_edit_id,
                                frm.getSelPlanId() * 1000 + frmSubWk.getChargeModeId(),
                                adjust_edit_memo );
                    }

                    // 最低金額チェック
                    checkDeposit = checkCashDeposit( frm, logic, chashDeposit );
                    if ( checkDeposit == true )
                    {
                        // 最低予約金額未満あり
                        frm.setInfoMsg( Message.getMessage( "warn.30009", objNum.format( chashDeposit ) ) );
                        // 一旦空にする
                        frm.setFrmSubList( new ArrayList<FormOwnerRsvPlanChargeSub>() );
                        getDetailData( request, logic, frm, frmSub );
                        menuFlg = menuFlg_DETAIL;
                        request.setAttribute( "FORM_Charge", frm );

                    }
                    else
                    {
                        // プラン設定管理画面データ取得
                        menuFlg = menuFlg_MNG;
                        frmManage.setSelHotelId( paramHotelID );
                        logicManage.setFrm( frmManage );
                        logicManage.getPlanChargeData( planView );
                        request.setAttribute( "FORM_ChargeManage", logicManage.getFrm() );
                    }
                }

                // 入力チェックがエラーの場合
                if ( inptCheckFlg == false )
                {
                    getDetailData( request, logic, frm, frmSub );

                    // 編集時、予約データ存在チェック
                    if ( OwnerRsvCommon.isReservePlan( paramHotelID, frm.getSelPlanId() ) == true )
                    {
                        if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
                        {
                            frm.setViewMode( 1 );
                        }
                        frm.setRsvMsg( Message.getMessage( "warn.30012", Integer.toString( paramHotelID ) ) );
                    }
                    menuFlg = menuFlg_DETAIL;
                    request.setAttribute( "FORM_Charge", frm );
                }
                // メニュー設定
                frmMenu.setUserId( paramUserID );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

                // 画面遷移
                request.setAttribute( "FORM_Menu", frmMenu );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                requestDispatcher.forward( request, response );
                return;

            }
            else if ( paramBtnNm.equals( "btnBack" ) )
            {
                // ▼戻る
                // プラン料金設定管理画面データ取得
                menuFlg = menuFlg_MNG;
                frmManage.setSelHotelId( paramHotelID );
                logicManage.setFrm( frmManage );
                logicManage.getPlanChargeData( planView );
                request.setAttribute( "FORM_ChargeManage", logicManage.getFrm() );
                // メニュー設定
                frmMenu.setUserId( paramUserID );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

                // 画面遷移
                request.setAttribute( "FORM_Menu", frmMenu );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                requestDispatcher.forward( request, response );

                return;
            }
            else if ( paramBtnNm.equals( "btnPreview" ) )
            {
                // プレビュー画面の表示
                paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
                FormOwnerRsvPlanChargeCalendar frmCalendar;
                frmCalendar = new FormOwnerRsvPlanChargeCalendar();
                Calendar orgCal;

                frmCalendar.setSelHotelId( frm.getSelHotelId() );
                frmCalendar.setSelPlanId( frm.getSelPlanId() );
                frmCalendar.setOwnerHotelID( paraOwnerHotelID );
                frmCalendar.setUserId( paramUserID );
                frmCalendar.setSelPlanNm( frm.getPlanNm() );
                orgCal = Calendar.getInstance();

                targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
                getMonth( frmCalendar, paramHotelID, targetYYYYMM, frm.getSelPlanId(), imediaflag );
                if ( frmCalendar.getMonthlyList() != null && frmCalendar.getMonthlyList().get( 0 ) != null &&
                        frmCalendar.getMonthlyList().get( 0 ).get( 0 ) != null && frmCalendar.getMonthlyList().get( 0 ).get( 0 ).getErrMsg().equals( "" ) != true )
                {
                    // 全体のカレンダーで取得
                    getMonth( frmCalendar, paramHotelID, targetYYYYMM, 0, imediaflag );
                }
                frmInfo.setSelHotelID( frm.getSelHotelId() );
                frmInfo.setSelPlanID( frm.getSelPlanId() );
                frmInfo = rsvcomm.getPlanData( frmInfo );
                request.setAttribute( "FORM_Charge", frm );
                request.setAttribute( "FORM_Calendar", frmCalendar );
                request.setAttribute( "FORM_Personal", frmInfo );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_plan_charge_preview.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( paramBtnNm.equals( "btnPreview2" ) )
            {
                if ( request.getParameter( "targetyyyymm" ) != null )
                {
                    paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
                    FormOwnerRsvPlanChargeCalendar frmCalendar;
                    frmCalendar = new FormOwnerRsvPlanChargeCalendar();
                    frmCalendar.setSelHotelId( frm.getSelHotelId() );
                    frmCalendar.setSelPlanId( frm.getSelPlanId() );
                    frmCalendar.setOwnerHotelID( paraOwnerHotelID );
                    frmCalendar.setSelPlanNm( frm.getPlanNm() );
                    frmCalendar.setUserId( paramUserID );
                    targetYYYYMM = request.getParameter( "targetyyyymm" );
                    getMonth( frmCalendar, paramHotelID, targetYYYYMM, frm.getSelPlanId(), imediaflag );
                    if ( frmCalendar.getMonthlyList() != null && frmCalendar.getMonthlyList().get( 0 ) != null &&
                            frmCalendar.getMonthlyList().get( 0 ).get( 0 ) != null && frmCalendar.getMonthlyList().get( 0 ).get( 0 ).getErrMsg().equals( "" ) != true )
                    {
                        // 全体のカレンダーで取得
                        getMonth( frmCalendar, paramHotelID, targetYYYYMM, 0, imediaflag );
                    }
                    frmInfo.setSelHotelID( frm.getSelHotelId() );
                    frmInfo.setSelPlanID( frm.getSelPlanId() );
                    frmInfo = rsvcomm.getPlanData( frmInfo );
                    request.setAttribute( "FORM_Charge", frm );
                    request.setAttribute( "FORM_Calendar", frmCalendar );
                    request.setAttribute( "FORM_Personal", frmInfo );
                    requestDispatcher = request.getRequestDispatcher( "owner_rsv_plan_charge_preview.jsp" );
                    requestDispatcher.forward( request, response );
                }
                return;
            }

            // ▼削除(予約データ存在時は削除できないようにする)
            // 予約データ存在チェック
            if ( (OwnerRsvCommon.isReservePlan( frm.getSelHotelId(), frm.getSelPlanId() ) == true) &&
                    (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // 存在する
                frm.setViewMode( 1 );
                frm.setRsvMsg( Message.getMessage( "warn.30012", Integer.toString( frm.getSelHotelId() ) ) );
                getDetailData( request, logic, frm, frmSub );
                menuFlg = menuFlg_DETAIL;
                request.setAttribute( "FORM_Charge", frm );
            }
            else
            {
                frmSubList = new ArrayList<FormOwnerRsvPlanChargeSub>();
                // 削除前チェック
                if ( frm.getFrmDelList().size() == 0 )
                {
                    frm.setErrMsg( Message.getMessage( "warn.00002", "削除対象の料金モード" ) );
                    getDetailData( request, logic, frm, frmSub );

                    menuFlg = menuFlg_DETAIL;
                    request.setAttribute( "FORM_Charge", frm );
                }
                else
                {
                    // 削除処理実行
                    // 予約データ存在チェック
                    frmSubList = frm.getFrmDelList();
                    for( int i = 0 ; i < frmSubList.size() ; i++ )
                    {
                        FormOwnerRsvPlanChargeSub frmSubDel = frmSubList.get( i );
                        // 予約が存在したら事務所権限のみ許可する
                        if ( (OwnerRsvCommon.existsRsvChargeMode( paramHotelID, frm.getSelPlanId(), frmSubDel.getChargeModeId() ) == true)
                                && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
                        {
                            getDetailData( request, logic, frm, frmSub );

                            frm.setViewMode( 1 );
                            frm.setRsvMsg( Message.getMessage( "warn.30012", Integer.toString( paramHotelID ) ) );

                            menuFlg = menuFlg_DETAIL;
                            request.setAttribute( "FORM_Charge", frm );
                        }
                        else
                        {
                            // 存在しない場合は削除
                            logic.execDelChargeMode( paramHotelID, frm.getSelPlanId(), frmSubDel.getChargeModeId() );

                            // ホテル修正履歴の登録
                            adjust_edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_CHARGE_DEL;
                            adjust_edit_memo = OwnerRsvCommon.ADJUST_MEMO_PLAN_CHARGE_DEL;

                            FormOwnerRsvPlanChargeSub frmSubWk = frm.getFrmDelList().get( i );
                            OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                    frm.getUserId(), adjust_edit_id,
                                    frm.getSelPlanId() * 1000 + frmSubWk.getChargeModeId(),
                                    adjust_edit_memo );

                            // プラン設定管理画面データ取得
                            menuFlg = menuFlg_MNG;
                            frmManage.setSelHotelId( paramHotelID );
                            logicManage.setFrm( frmManage );
                            logicManage.getPlanChargeData( planView );
                            request.setAttribute( "FORM_ChargeManage", logicManage.getFrm() );
                        }
                    }
                }
            }

            // メニュー設定
            frmMenu.setUserId( paramUserID );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanCharge.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvPlanCharge.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * カレンダー情報取得
     * 
     * @param frm
     * @para, hotelId ホテルID
     * @param targetYM 指定年月
     * @param paramPlanId プランID
     * @param imediaFlag アイメディアフラグ
     * @return なし
     */
    private void getMonth(FormOwnerRsvPlanChargeCalendar frm, int hotelId, String targetYM, int paramPlanId, int imediaFlag) throws Exception
    {
        int planId = 0;
        LogicOwnerRsvPlanChargeCalendar logic = new LogicOwnerRsvPlanChargeCalendar();
        ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>>();

        try
        {
            frm.setSelHotelId( hotelId );

            // プラン情報取得
            logic.setFrm( frm );
            logic.getPlanList();
            frm = logic.getFrm();

            if ( paramPlanId == -1 )
            {
                // 先頭行のカレンダー情報を表示
                planId = frm.getPlanIdList().get( 0 );
            }
            else
            {
                planId = paramPlanId;
            }

            // カレンダー情報取得
            if ( paramPlanId == 0 )
            {
                // ホテル全体の場合
                logic.getHotelCalendar( frm.getSelHotelId(), Integer.parseInt( targetYM ), imediaFlag );
            }
            else
            {
                logic.getCalendar( frm.getSelHotelId(), planId, Integer.parseInt( targetYM ), imediaFlag );
            }

            monthlyList = logic.getFrm().getMonthlyList();
            frm.setMonthlyList( monthlyList );

            // フォームにセット
            frm.setSelPlanId( planId );
            frm.setMonthlyList( logic.getFrm().getMonthlyList() );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanCharge.getMonth() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvPlanCharge.getMonth()] " + exception );
        }
    }

    /**
     * 料金モードデータ取得
     * 
     * @param HttpServletRequest
     * @param logic LogicOwnerRsvPlanChargeオブジェクト
     * @param frm FormOwnerRsvPlanChargeオブジェクト
     * @param frmSub FormOwnerRsvPlanChargeSubオブジェクト
     * @return なし
     */
    private void getDetailData(HttpServletRequest request, LogicOwnerRsvPlanCharge logic, FormOwnerRsvPlanCharge frm, FormOwnerRsvPlanChargeSub frmSub) throws Exception
    {
        int chashDeposit = 0;
        boolean isChecked = false;

        if ( frm.getFrmSubList().size() > 0 )
        {
            isChecked = true;
        }

        // データ再検索
        logic.setFrm( frm );
        logic.setFrmSub( frmSub );
        logic.getPlanCharge();

        // 最低予約金額取得
        chashDeposit = logic.getChashDeposit( frm.getSelHotelId() );
        frm.setCashDeposit( chashDeposit );

        // 入力値をセット
        if ( isChecked == true )
        {
            setFrmParam( request, frm, 2 );
        }
    }

    /**
     * 画面の値を取得し、Formにセットする。
     * 
     * @param HttpServletRequest
     * @param frm FormOwnerRsvPlanChargeオブジェクト
     * @param setKbn フォームセット区分(1:画面起動時、2:再表示時)
     * @return なし
     */
    private void setFrmParam(HttpServletRequest request, FormOwnerRsvPlanCharge frm, int setKbn)
    {

        int chargeModeId = 0;
        ArrayList<Integer> modeList = new ArrayList<Integer>();
        ArrayList<Integer> delList = new ArrayList<Integer>();
        ArrayList<FormOwnerRsvPlanChargeSub> frmSubList = new ArrayList<FormOwnerRsvPlanChargeSub>();
        ArrayList<FormOwnerRsvPlanChargeSub> frmDelList = new ArrayList<FormOwnerRsvPlanChargeSub>();
        FormOwnerRsvPlanChargeSub frmSub;
        int weekStatus = 0;
        int salesStopWeekStatus = 0;
        int mondayCount = 0;
        int tuesdayCount = 0;
        int wednesdayCount = 0;
        int thursdayCount = 0;
        int fridayCount = 0;
        int saturdayCount = 0;
        int sundayCount = 0;
        int holidayCount = 0;
        int beforeHolidayCount = 0;
        boolean delflag = false;

        frm.setSelHotelId( Integer.parseInt( request.getParameter( "selHotelIDValue" ) ) );
        frm.setOwnerHotelID( OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ) );
        frm.setUserId( OwnerRsvCommon.getCookieLoginUserId( request.getCookies() ) );
        frm.setSelPlanId( Integer.parseInt( request.getParameter( "planId" ) ) );

        // プラン名取得
        DataRsvPlan data = new DataRsvPlan();
        data.getData( frm.getSelHotelId(), frm.getSelPlanId() );
        frm.setPlanNm( data.getPlanName() );
        frm.setSalesStopWeekStatus( data.getSalesStopWeekStatus() );
        frm.setSalesStopWeekView( OwnerRsvCommon.createSalesStopWeek( data.getSalesStopWeekStatus() ) );

        // 選択されているモード
        if ( request.getParameterValues( "sel" ) == null )
        {
            // 未チェック
            frm.setFrmSubList( frmSubList );
            return;
        }

        String[] selModes = request.getParameterValues( "sel" );
        if ( selModes != null )
        {
            for( int i = 0 ; i < selModes.length ; i++ )
            {
                modeList.add( Integer.parseInt( selModes[i] ) );
            }
        }

        String[] delModes = request.getParameterValues( "del" );
        if ( delModes != null )
        {
            for( int i = 0 ; i < delModes.length ; i++ )
            {
                delList.add( Integer.parseInt( delModes[i] ) );
            }
        }

        if ( setKbn == 1 )
        {
            // ■初期起動時
            for( int i = 0 ; i < modeList.size() ; i++ )
            {
                frmSub = new FormOwnerRsvPlanChargeSub();
                chargeModeId = modeList.get( i );

                frmSub.setChargeModeId( chargeModeId );
                frmSub.setChargeModeNm( request.getParameter( "chargeModeNm" + chargeModeId ) );
                frmSub.setCiTimeFromHH( request.getParameter( "ciFromHH" + chargeModeId ) );
                frmSub.setCiTimeFromMM( request.getParameter( "ciFromMM" + chargeModeId ) );
                frmSub.setCiTimeToHH( request.getParameter( "ciToHH" + chargeModeId ) );
                frmSub.setCiTimeToMM( request.getParameter( "ciToMM" + chargeModeId ) );
                frmSub.setAdultTwo( request.getParameter( "adultTwo" + chargeModeId ) );
                frmSub.setAdultOne( request.getParameter( "adultOne" + chargeModeId ) );
                frmSub.setAdultAdd( request.getParameter( "adultAdd" + chargeModeId ) );
                frmSub.setChildAdd( request.getParameter( "childAdd" + chargeModeId ) );
                frmSub.setCoRemarks( request.getParameter( "coRemarks" + chargeModeId ) );
                frmSub.setRemarks( request.getParameter( "remarks" + chargeModeId ) );
                frmSub.setRegistStatus( Integer.parseInt( request.getParameter( "registStatus" + chargeModeId ) ) );
                weekStatus = 0;
                if ( request.getParameter( "chkmon" + chargeModeId ) != null && request.getParameter( "chkmon" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_MONDAY;
                    mondayCount++;
                }
                if ( request.getParameter( "chktue" + chargeModeId ) != null && request.getParameter( "chktue" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_TUESDAY;
                    tuesdayCount++;
                }
                if ( request.getParameter( "chkwed" + chargeModeId ) != null && request.getParameter( "chkwed" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_WEDNESDAY;
                    wednesdayCount++;
                }
                if ( request.getParameter( "chkthi" + chargeModeId ) != null && request.getParameter( "chkthi" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_THIRTHDAY;
                    thursdayCount++;
                }
                if ( request.getParameter( "chkfri" + chargeModeId ) != null && request.getParameter( "chkfri" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_FRIDAY;
                    fridayCount++;
                }
                if ( request.getParameter( "chksat" + chargeModeId ) != null && request.getParameter( "chksat" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_SATURDAY;
                    saturdayCount++;
                }
                if ( request.getParameter( "chksun" + chargeModeId ) != null && request.getParameter( "chksun" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_SUNDAY;
                    sundayCount++;
                }
                if ( request.getParameter( "chkholi" + chargeModeId ) != null && request.getParameter( "chkholi" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_HOLIDAY;
                    holidayCount++;
                }
                if ( request.getParameter( "chkbeforeholi" + chargeModeId ) != null && request.getParameter( "chkbeforeholi" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
                    beforeHolidayCount++;
                }
                frmSub.setWeekStatus( weekStatus );
                if ( request.getParameter( "coKind" + chargeModeId ) != null )
                {
                    frmSub.setCoKind( Integer.parseInt( request.getParameter( "coKind" + chargeModeId ) ) );
                }
                frmSub.setCoTimeHH( request.getParameter( "coTimeHH" + chargeModeId ) );
                frmSub.setCoTimeMM( request.getParameter( "coTimeMM" + chargeModeId ) );

                delflag = false;
                for( int j = 0 ; j < delList.size() ; j++ )
                {
                    if ( delList.get( j ) == chargeModeId )
                    {
                        delflag = true;
                        break;
                    }
                }
                if ( delflag == true )
                {
                    // 削除リストに追加
                    frmDelList.add( frmSub );
                }
                else
                {
                    // 登録・更新リストへ追加
                    frmSubList.add( frmSub );
                }
            }
            if ( mondayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_MONDAY;
            }
            if ( tuesdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_TUESDAY;
            }
            if ( wednesdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_WEDNESDAY;
            }
            if ( thursdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_THIRTHDAY;
            }
            if ( fridayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_FRIDAY;
            }
            if ( saturdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_SATURDAY;
            }
            if ( sundayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_SUNDAY;
            }
            if ( holidayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_HOLIDAY;
            }
            if ( beforeHolidayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
            }
        }
        else
        {
            // ■再読込時
            frmSubList = frm.getFrmSubList();
            for( int i = 0 ; i < frmSubList.size() ; i++ )
            {
                frmSub = frmSubList.get( i );

                // 入力値セット
                for( int j = 0 ; j < modeList.size() ; j++ )
                {
                    chargeModeId = modeList.get( j );

                    if ( frmSub.getChargeModeId() == chargeModeId )
                    {
                        frmSub.setCheck( 1 );
                        frmSub.setChargeModeId( chargeModeId );
                        frmSub.setCiTimeFromHH( request.getParameter( "ciFromHH" + chargeModeId ) );
                        frmSub.setCiTimeFromMM( request.getParameter( "ciFromMM" + chargeModeId ) );
                        frmSub.setCiTimeToHH( request.getParameter( "ciToHH" + chargeModeId ) );
                        frmSub.setCiTimeToMM( request.getParameter( "ciToMM" + chargeModeId ) );
                        frmSub.setCoTimeHH( request.getParameter( "coTimeHH" + chargeModeId ) );
                        frmSub.setCoTimeMM( request.getParameter( "coTimeMM" + chargeModeId ) );
                        frmSub.setAdultTwo( request.getParameter( "adultTwo" + chargeModeId ) );
                        frmSub.setAdultOne( request.getParameter( "adultOne" + chargeModeId ) );
                        frmSub.setAdultAdd( request.getParameter( "adultAdd" + chargeModeId ) );
                        frmSub.setChildAdd( request.getParameter( "childAdd" + chargeModeId ) );
                        frmSub.setCoRemarks( request.getParameter( "coRemarks" + chargeModeId ) );
                        frmSub.setRemarks( request.getParameter( "remarks" + chargeModeId ) );
                        frmSub.setRegistStatus( Integer.parseInt( request.getParameter( "registStatus" + chargeModeId ) ) );
                        weekStatus = 0;
                        if ( request.getParameter( "chkmon" + chargeModeId ) != null && request.getParameter( "chkmon" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_MONDAY;
                            mondayCount++;
                        }
                        if ( request.getParameter( "chktue" + chargeModeId ) != null && request.getParameter( "chktue" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_TUESDAY;
                            tuesdayCount++;
                        }
                        if ( request.getParameter( "chkwed" + chargeModeId ) != null && request.getParameter( "chkwed" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_WEDNESDAY;
                            wednesdayCount++;
                        }
                        if ( request.getParameter( "chkthi" + chargeModeId ) != null && request.getParameter( "chkthi" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_THIRTHDAY;
                            thursdayCount++;
                        }
                        if ( request.getParameter( "chkfri" + chargeModeId ) != null && request.getParameter( "chkfri" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_FRIDAY;
                            fridayCount++;
                        }
                        if ( request.getParameter( "chksat" + chargeModeId ) != null && request.getParameter( "chksat" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_SATURDAY;
                            saturdayCount++;
                        }
                        if ( request.getParameter( "chksun" + chargeModeId ) != null && request.getParameter( "chksun" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_SUNDAY;
                            sundayCount++;
                        }
                        if ( request.getParameter( "chkholi" + chargeModeId ) != null && request.getParameter( "chkholi" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_HOLIDAY;
                            holidayCount++;
                        }
                        if ( request.getParameter( "chkbeforeholi" + chargeModeId ) != null && request.getParameter( "chkbeforeholi" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
                            beforeHolidayCount++;
                        }
                        frmSub.setWeekStatus( weekStatus );
                        frmSub.setCoKind( Integer.parseInt( request.getParameter( "coKind" + chargeModeId ) ) );
                    }
                }

            }
            if ( mondayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_MONDAY;
            }
            if ( tuesdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_TUESDAY;
            }
            if ( wednesdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_WEDNESDAY;
            }
            if ( thursdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_THIRTHDAY;
            }
            if ( fridayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_FRIDAY;
            }
            if ( saturdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_SATURDAY;
            }
            if ( sundayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_SUNDAY;
            }
            if ( holidayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_HOLIDAY;
            }
            if ( beforeHolidayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
            }
        }
        frm.setSalesStopWeekStatus( salesStopWeekStatus );
        frm.setFrmSubList( frmSubList );
        frm.setFrmDelList( frmDelList );
    }

    /**
     * 入力値チェック
     * 
     * @param frm FormOwnerRsvPlanChargeオブジェクト
     * @param adminImediaFlag 事務局権限
     * @return true:正常、false:エラーあり
     */
    private boolean isInputCheckMsg(FormOwnerRsvPlanCharge frm, int adminImediaFlag) throws Exception
    {
        String msg;
        boolean isCheck = false;
        boolean isCiFromHHCheck = true;
        boolean isCiFromMMCheck = true;
        boolean isCiToHHCheck = true;
        boolean isCiToMMCheck = true;
        boolean isCheckOutHHCheck = true;
        boolean isCheckOutMMCheck = true;
        int mondayCount = 0;
        int tuesdayCount = 0;
        int wednesdayCount = 0;
        int thirthdayCount = 0;
        int fridayCount = 0;
        int saturdayCount = 0;
        int sundayCount = 0;
        int holidayCount = 0;
        int beforeholidayCount = 0;
        String chargeModeNm = "";
        int ciFrom = 0;
        int ciTo = 0;
        int ret = 0;
        int coTime = 0;
        msg = "";
        ReserveCommon rsvcomm = new ReserveCommon();

        if ( frm.getFrmSubList().size() == 0 )
        {
            msg = msg + Message.getMessage( "warn.00002", "登録対象の料金モード" );
            frm.setErrMsg( msg );
            return(isCheck);
        }

        // 事務局権限でなければ予約データ存在チェック
        if ( adminImediaFlag == 0 )
        {
            // 予約データ存在チェック
            if ( OwnerRsvCommon.isReservePlan( frm.getSelHotelId(), frm.getSelPlanId() ) == true )
            {
                // 存在する
                frm.setViewMode( 1 );
                frm.setRsvMsg( Message.getMessage( "warn.30012", Integer.toString( frm.getSelHotelId() ) ) );
                return(isCheck);
            }
        }

        for( int i = 0 ; i < frm.getFrmSubList().size() ; i++ )
        {
            FormOwnerRsvPlanChargeSub frmSub = frm.getFrmSubList().get( i );
            chargeModeNm = ReplaceString.HTMLEscape( frmSub.getChargeModeNm() );

            // ▼チェックインFrom　時間
            if ( CheckString.onlySpaceCheck( frmSub.getCiTimeFromHH() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】のチェックインFromの時間" ) + "<br />";
                isCiFromHHCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCiTimeFromHH() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックインFromの時間", "0以上の整数" ) + "<br />";
                    isCiFromHHCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCiTimeFromHH() ) > 29 || Integer.parseInt( frmSub.getCiTimeFromHH() ) < 0) )
                    {
                        // 時間が0時〜23時以外の場合
                        msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックインFromの時間", "0〜29" ) + "<br />";
                        isCiFromHHCheck = false;
                    }
                }
            }

            // ▼チェックインFrom　分
            if ( CheckString.onlySpaceCheck( frmSub.getCiTimeFromMM() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】のチェックインFromの分" ) + "<br />";
                isCiFromMMCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCiTimeFromMM() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックインFromの分", "0以上の整数" ) + "<br />";
                    isCiFromMMCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCiTimeFromMM() ) > 59 || Integer.parseInt( frmSub.getCiTimeFromMM() ) < 0) )
                    {
                        // 時間が0時〜23時以外の場合
                        msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックインFromの分", "0〜59" ) + "<br />";
                        isCiFromMMCheck = false;
                    }
                }
            }

            // ▼チェックインTo　時間
            if ( CheckString.onlySpaceCheck( frmSub.getCiTimeToHH() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】のチェックインToの時間" ) + "<br />";
                isCiToHHCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCiTimeToHH() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックインToの時間", "0以上の整数" ) + "<br />";
                    isCiToHHCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCiTimeToHH() ) > 29 || Integer.parseInt( frmSub.getCiTimeToHH() ) < 0) )
                    {
                        // 時間が0時〜29時以外の場合
                        msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックインToの時間", "0〜29" ) + "<br />";
                        isCiToHHCheck = false;
                    }
                }
            }

            // ▼チェックインTo　分
            if ( CheckString.onlySpaceCheck( frmSub.getCiTimeToMM() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】のチェックインToの分" ) + "<br />";
                isCiToMMCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCiTimeToMM() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックインToの分", "0以上の整数" ) + "<br />";
                    isCiToMMCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCiTimeToMM() ) > 59 || Integer.parseInt( frmSub.getCiTimeToMM() ) < 0) )
                    {
                        // 時間が0時〜23時以外の場合
                        msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックインToの分", "0〜59" ) + "<br />";
                        isCiToMMCheck = false;
                    }
                }
            }

            // ▼チェックインFrom−Toの範囲チェック
            if ( isCiFromHHCheck == true && isCiFromMMCheck == true && isCiToHHCheck == true && isCiToMMCheck == true )
            {
                ciFrom = Integer.parseInt( String.format( "%02d", Integer.parseInt( frmSub.getCiTimeFromHH() ) ) + String.format( "%02d", Integer.parseInt( frmSub.getCiTimeFromMM() ) ) );
                ciTo = Integer.parseInt( String.format( "%02d", Integer.parseInt( frmSub.getCiTimeToHH() ) ) + String.format( "%02d", Integer.parseInt( frmSub.getCiTimeToMM() ) ) );
                if ( ciFrom > ciTo )
                {
                    msg = msg + Message.getMessage( "warn.00009", "【" + chargeModeNm + "】のチェックインの範囲指定" ) + "<br />";
                }
            }

            // ▼チェックアウト 時間
            if ( CheckString.onlySpaceCheck( frmSub.getCoTimeHH() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】のチェックアウトの時間" ) + "<br />";
                isCheckOutHHCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCoTimeHH() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックアウトの時間", "0以上の整数" ) + "<br />";
                    isCheckOutHHCheck = false;
                }
                else
                {
                    if ( frmSub.getCoKind() == 0 )
                    {
                        if ( Integer.parseInt( frmSub.getCoTimeHH() ) > 23 || Integer.parseInt( frmSub.getCoTimeHH() ) < 0 )
                        {
                            // 時間が0時〜23時以外の場合
                            msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックアウトの時間", "0〜23" ) + "<br />";
                            isCheckOutHHCheck = false;
                        }
                    }
                    // チェックアウト区分が、チェックインから○時間の場合は数値の24時間までOKとする
                    else
                    {
                        if ( Integer.parseInt( frmSub.getCoTimeHH() ) > 24 || Integer.parseInt( frmSub.getCoTimeHH() ) < 0 )
                        {
                            // 時間が0時〜24時以外の場合
                            msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックアウトの時間", "0〜24" ) + "<br />";
                            isCheckOutHHCheck = false;
                        }

                    }
                }
            }

            // ▼チェックアウト 分
            if ( CheckString.onlySpaceCheck( frmSub.getCoTimeMM() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】のチェックアウトの分" ) + "<br />";
                isCheckOutMMCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCoTimeMM() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックアウトの分", "0以上の整数" ) + "<br />";
                    isCheckOutMMCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCoTimeMM() ) > 59 || Integer.parseInt( frmSub.getCoTimeMM() ) < 0) )
                    {
                        // 時間が0時〜23時以外の場合
                        msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】のチェックアウトの分", "0〜59" ) + "<br />";
                        isCheckOutMMCheck = false;
                    }
                }
            }

            // ▼チェックインToとチェックアウトのチェック(チェックアウト区分が0の場合のみチェックする)
            if ( frmSub.getCoKind() == 0 && isCiFromHHCheck == true && isCiFromMMCheck == true && isCiToHHCheck == true && isCiToMMCheck == true && isCheckOutHHCheck == true && isCheckOutMMCheck == true )
            {
                ciTo = Integer.parseInt( String.format( "%02d", Integer.parseInt( frmSub.getCiTimeToHH() ) ) + String.format( "%02d", Integer.parseInt( frmSub.getCiTimeToMM() ) ) );
                coTime = Integer.parseInt( String.format( "%02d", Integer.parseInt( frmSub.getCoTimeHH() ) ) + String.format( "%02d", Integer.parseInt( frmSub.getCoTimeMM() ) ) );
                if ( OwnerRsvCommon.checkCiCoTime( ciTo, coTime ) == false )
                {
                    msg = msg + Message.getMessage( "warn.00009", "【" + chargeModeNm + "】のチェックイン時間Toとチェックアウト時間の範囲指定" ) + "<br />";
                }
            }

            // ▼大人二人
            if ( CheckString.onlySpaceCheck( frmSub.getAdultTwo() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】の大人2人料金" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getAdultTwo() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】の大人2人料金", "0以上の整数" ) + "<br />";
                }
            }

            // ▼大人1人
            if ( CheckString.onlySpaceCheck( frmSub.getAdultOne() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】の大人1人料金" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getAdultOne() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】の大人1人料金", "0以上の整数" ) + "<br />";
                }
            }

            // ▼大人追加
            if ( CheckString.onlySpaceCheck( frmSub.getAdultAdd() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】の大人追加料金" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getAdultAdd() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】の大人追加料金", "0以上の整数" ) + "<br />";
                }
            }

            if ( rsvcomm.checkLoveHotelFlag( frm.getSelHotelId() ) == false )
            {
                // ▼子供追加
                if ( CheckString.onlySpaceCheck( frmSub.getChildAdd() ) == true )
                {
                    // 未入力・空白文字の場合
                    msg = msg + Message.getMessage( "warn.00001", "【" + chargeModeNm + "】の子供追加料金" ) + "<br />";
                }
                else
                {
                    if ( (OwnerRsvCommon.numCheck( frmSub.getChildAdd() ) == false) )
                    {
                        // 半角数字以外が入力されている場合
                        msg = msg + Message.getMessage( "warn.30007", "【" + chargeModeNm + "】の子供追加料金", "0以上の整数" ) + "<br />";
                    }
                }
            }

            // 時間備考(全角50文字以内)
            ret = OwnerRsvCommon.LengthCheck( frmSub.getCoRemarks().trim(), 100 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                msg = msg + Message.getMessage( "warn.00038", "【" + chargeModeNm + "】の時間備考", "50" ) + "<br />";
            }

            // 料金備考(全角50文字以内)
            ret = OwnerRsvCommon.LengthCheck( frmSub.getRemarks().trim(), 100 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                msg = msg + Message.getMessage( "warn.00038", "【" + chargeModeNm + "】の料金備考", "50" ) + "<br />";
            }

            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_MONDAY) == OwnerRsvCommon.CALENDAR_MONDAY )
            {
                mondayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_TUESDAY) == OwnerRsvCommon.CALENDAR_TUESDAY )
            {
                tuesdayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_WEDNESDAY) == OwnerRsvCommon.CALENDAR_WEDNESDAY )
            {
                wednesdayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_THIRTHDAY) == OwnerRsvCommon.CALENDAR_THIRTHDAY )
            {
                thirthdayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_FRIDAY) == OwnerRsvCommon.CALENDAR_FRIDAY )
            {
                fridayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_SATURDAY) == OwnerRsvCommon.CALENDAR_SATURDAY )
            {
                saturdayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_SUNDAY) == OwnerRsvCommon.CALENDAR_SUNDAY )
            {
                sundayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_HOLIDAY) == OwnerRsvCommon.CALENDAR_HOLIDAY )
            {
                holidayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY) == OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY )
            {
                beforeholidayCount++;
            }
        }
        if ( mondayCount != 1 )
        {
            if ( mondayCount > 0 )
            {
                msg = msg + "カレンダー月曜割当を複数選択することはできません。<br />";
            }
        }
        if ( tuesdayCount != 1 )
        {
            if ( tuesdayCount > 0 )
            {
                msg = msg + "カレンダー火曜割当を複数選択することはできません。<br />";
            }
        }
        if ( wednesdayCount != 1 )
        {
            if ( wednesdayCount > 0 )
            {
                msg = msg + "カレンダー水曜割当を複数選択することはできません。<br />";
            }
        }
        if ( thirthdayCount != 1 )
        {
            if ( thirthdayCount > 0 )
            {
                msg = msg + "カレンダー木曜割当を複数選択することはできません。<br />";
            }
        }
        if ( fridayCount != 1 )
        {
            if ( fridayCount > 0 )
            {
                msg = msg + "カレンダー金曜割当を複数選択することはできません。<br />";
            }
        }
        if ( saturdayCount != 1 )
        {
            if ( saturdayCount > 0 )
            {
                msg = msg + "カレンダー土曜割当を複数選択することはできません。<br />";
            }
        }
        if ( sundayCount != 1 )
        {
            if ( sundayCount > 0 )
            {
                msg = msg + "カレンダー日曜割当を複数選択することはできません。<br />";
            }
        }
        if ( holidayCount != 1 )
        {
            if ( holidayCount > 0 )
            {
                msg = msg + "カレンダー祝日割当を複数選択することはできません。<br />";
            }
        }
        if ( beforeholidayCount != 1 )
        {
            if ( beforeholidayCount > 0 )
            {
                msg = msg + "カレンダー祝前日割当を複数選択することはできません。<br />";
            }
        }

        frm.setErrMsg( msg );

        if ( msg.trim().length() == 0 )
        {
            isCheck = true;
        }

        return isCheck;
    }

    /**
     * 最低予約金額チェック(0円の時はチェックしない)
     * 
     * @param frm FormOwnerRsvPlanChargeオブジェクト
     * @param logic LogicOwnerRsvPlanChargeオブジェクト
     * @param chashDeposit 最低予約金額
     * @return true:最低予約金額未満、false:最低予約金額以上
     */
    private boolean checkCashDeposit(FormOwnerRsvPlanCharge frm, LogicOwnerRsvPlanCharge logic, int chashDeposit) throws Exception
    {
        boolean isCheckDeposit = false;
        ArrayList<FormOwnerRsvPlanChargeSub> frmSubList;
        FormOwnerRsvPlanChargeSub frmSub;
        ReserveCommon rsvcomm = new ReserveCommon();

        frmSubList = frm.getFrmSubList();
        for( int i = 0 ; i < frmSubList.size() ; i++ )
        {
            frmSub = frmSubList.get( i );

            // 最低予約金額未満か
            if ( Integer.parseInt( frmSub.getAdultTwo() ) != 0 && Integer.parseInt( frmSub.getAdultTwo() ) < chashDeposit )
            {
                // 大人2人料金
                isCheckDeposit = true;
                break;
            }
            if ( Integer.parseInt( frmSub.getAdultOne() ) != 0 && Integer.parseInt( frmSub.getAdultOne() ) < chashDeposit )
            {
                // 大人一人料金
                isCheckDeposit = true;
                break;
            }
            if ( Integer.parseInt( frmSub.getAdultAdd() ) != 0 && Integer.parseInt( frmSub.getAdultAdd() ) < chashDeposit )
            {
                // 大人追加料金
                isCheckDeposit = true;
                break;
            }
            if ( rsvcomm.checkLoveHotelFlag( frm.getSelHotelId() ) == false )
            {
                if ( Integer.parseInt( frmSub.getChildAdd() ) != 0 && Integer.parseInt( frmSub.getChildAdd() ) < chashDeposit )
                {
                    // 子供追加料金
                    isCheckDeposit = true;
                    break;
                }
            }
        }

        return(isCheckDeposit);
    }
}
