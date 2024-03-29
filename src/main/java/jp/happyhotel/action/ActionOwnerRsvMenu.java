package jp.happyhotel.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerRsvChargeMode;
import jp.happyhotel.owner.FormOwnerRsvEquipManage;
import jp.happyhotel.owner.FormOwnerRsvHotelBasic;
import jp.happyhotel.owner.FormOwnerRsvManage;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvOptionManage;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendarSub;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeManage;
import jp.happyhotel.owner.FormOwnerRsvPlanManage;
import jp.happyhotel.owner.FormOwnerRsvReserveBasic;
import jp.happyhotel.owner.FormOwnerRsvRoomManage;
import jp.happyhotel.owner.LogicOwnerRsvChargeMode;
import jp.happyhotel.owner.LogicOwnerRsvEquipManage;
import jp.happyhotel.owner.LogicOwnerRsvHotelBasic;
import jp.happyhotel.owner.LogicOwnerRsvManage;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvOptionManage;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeManage;
import jp.happyhotel.owner.LogicOwnerRsvPlanManage;
import jp.happyhotel.owner.LogicOwnerRsvReserveBasic;
import jp.happyhotel.owner.LogicOwnerRsvRoomManage;

/**
 * 
 * オーナーメニュー画面
 */
public class ActionOwnerRsvMenu extends BaseAction
{

    private RequestDispatcher requestDispatcher   = null;
    private static final int  PARKING_SEL         = 1;   // 駐車場有無デフォルト
    private static final int  PUBLISHING_NON_DISP = 1;   // 未掲載非表示
    private static final int  PUBLISHING_DISP     = 0;   // 未掲載表示

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerRsvHotelBasic frmHotelBasic;
        FormOwnerRsvReserveBasic frmReserveBasic;
        FormOwnerRsvRoomManage frmRoomManage;
        FormOwnerRsvChargeMode frmChargeMode;
        FormOwnerRsvEquipManage frmEquipManage;
        FormOwnerRsvPlanChargeManage frmPlnChargeManage;
        FormOwnerRsvPlanManage frmPlanManage;
        FormOwnerRsvManage frmManage;
        FormOwnerRsvPlanChargeCalendar frmCalendar;
        FormOwnerRsvOptionManage frmOptionManage;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        int selHotelID = 0;
        int paramModeFlg = 0;
        int paramUserID = 0;
        String errMsg = "";
        String hotenaviId = "";
        int paramPlanId = -1;
        int imediaflag = 0;
        int adminflag = 0;
        boolean dispAllFlag = false;

        try
        {
            // 引数の取得
            if ( (request.getParameter( "modeFlg" ) != null) && (request.getParameter( "modeFlg" ).toString().length() != 0) )
            {
                paramModeFlg = Integer.parseInt( request.getParameter( "modeFlg" ).toString() );
            }

            // 選択されたホテルIDの取得
            if ( (request.getParameter( "selHotelIDValue" ) != null) && (request.getParameter( "selHotelIDValue" ).trim().length() != 0) )
            {
                selHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            if ( selHotelID == 0 )
            {
                paramModeFlg = 0;
            }
            // 選択されたプランIDをセット
            if ( (request.getParameter( "selPlanId" ) != null) && (request.getParameter( "selPlanId" ).toString().length() != 0) )
            {
                paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ).toString() );
            }

            // ログインユーザIDの取得
            paramUserID = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );

            // ログインユーザと担当ホテルのチェック
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( (selHotelID != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, paramUserID, selHotelID ) == false) )
            {
                // 管理外のホテルはログイン画面へ遷移
                response.sendRedirect( "../../owner/index.jsp" );
                return;
            }

            imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, paramUserID );
            logicMenu.setFrm( new FormOwnerBkoMenu() );
            adminflag = logicMenu.getAdminFlg( hotenaviId, paramUserID );
            if ( imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" ) )
            {
                dispAllFlag = true;
            }

            // TOPページ設定
            frmMenu = new FormOwnerBkoMenu();
            OwnerRsvCommon.setMenu( frmMenu, selHotelID, paramModeFlg, request.getCookies() );
            frmMenu.setUserId( paramUserID );
            request.setAttribute( "FORM_Menu", frmMenu );

            // ホテルが1件しかない場合は、選択ホテルIDとする
            if ( frmMenu.getHotelIDList().size() == 1 )
            {
                selHotelID = frmMenu.getHotelIDList().get( 0 );
                frmMenu.setSelHotelID( selHotelID );
            }

            // 画面遷移
            switch( paramModeFlg )
            {
                case 0:
                    // 予約管理TOP
                    frmManage = new FormOwnerRsvManage();
                    frmManage.setSelHotelID( selHotelID );
                    frmManage.setUserId( paramUserID );
                    setRsvManage( frmManage, selHotelID, paramUserID, paramPlanId, dispAllFlag );
                    request.setAttribute( "FORM_Manage", frmManage );
                    request.setAttribute( "selPlanId", paramPlanId );
                    break;
                case 1:
                    // 施設基本情報設定
                    frmHotelBasic = new FormOwnerRsvHotelBasic();
                    frmHotelBasic.setSelHotelID( selHotelID );
                    frmHotelBasic.setUserID( paramUserID );
                    setHotelBasic( frmHotelBasic );
                    request.setAttribute( "FORM_HotelBasic", frmHotelBasic );
                    break;

                case 2:
                    // 予約基本情報設定
                    frmReserveBasic = new FormOwnerRsvReserveBasic();
                    frmReserveBasic.setSelHotelID( selHotelID );
                    frmReserveBasic.setUserID( paramUserID );
                    setReservBasic( frmReserveBasic );
                    request.setAttribute( "FORM_ReserveBasic", frmReserveBasic );
                    break;

                case 3:
                    // プラン設定
                    frmPlanManage = new FormOwnerRsvPlanManage();
                    frmPlanManage.setSelHotelId( selHotelID );
                    setPlanManage( frmPlanManage );
                    request.setAttribute( "FORM_PlanManage", frmPlanManage );
                    break;

                case 4:
                    // 料金モード設定
                    frmChargeMode = new FormOwnerRsvChargeMode();
                    frmChargeMode.setSelHotelID( selHotelID );
                    frmChargeMode.setUserId( paramUserID );
                    setChargeMode( frmChargeMode );
                    request.setAttribute( "FORM_ChargeMode", frmChargeMode );
                    break;

                case 5:
                    // プラン別料設定
                    frmPlnChargeManage = new FormOwnerRsvPlanChargeManage();
                    frmPlnChargeManage.setSelHotelId( selHotelID );
                    frmPlnChargeManage.setUserId( paramUserID );
                    setPlanChargeManage( frmPlnChargeManage );
                    request.setAttribute( "FORM_ChargeManage", frmPlnChargeManage );
                    break;
                case 7:
                    // 部屋情報管理
                    frmRoomManage = new FormOwnerRsvRoomManage();
                    frmRoomManage.setSelHotelID( selHotelID );
                    frmRoomManage.setUserId( paramUserID );
                    setRoomManage( frmRoomManage );
                    request.setAttribute( "FORM_RoomManage", frmRoomManage );
                    break;

                case 8:
                    // 設備情報管理
                    frmEquipManage = new FormOwnerRsvEquipManage();
                    frmEquipManage.setSelHotelID( selHotelID );
                    frmEquipManage.setUserId( paramUserID );
                    setEquip( frmEquipManage );
                    request.setAttribute( "FORM_EquipManage", frmEquipManage );
                    break;

                case 9:
                    // オプション設定
                    frmOptionManage = new FormOwnerRsvOptionManage();
                    frmOptionManage.setSelHotelId( selHotelID );
                    frmOptionManage.setUserId( paramUserID );
                    setOption( frmOptionManage );
                    request.setAttribute( "FORM_OptionManage", frmOptionManage );
                    break;

                case 10:
                    // プラン別カレンダー管理
                    frmCalendar = new FormOwnerRsvPlanChargeCalendar();
                    frmCalendar.setSelHotelId( selHotelID );
                    frmCalendar.setUserId( paramUserID );
                    setPlanChargeCalendar( frmCalendar, imediaflag );
                    request.setAttribute( "FORM_Calendar", frmCalendar );
                    break;

                case 100:
                    // 本日来店管理
                    frmCalendar = new FormOwnerRsvPlanChargeCalendar();
                    frmCalendar.setSelHotelId( selHotelID );
                    frmCalendar.setUserId( paramUserID );
                    setPlanChargeCalendar( frmCalendar, imediaflag );
                    request.setAttribute( "FORM_Calendar", frmCalendar );
                    break;

                default:
                    // TOPへ戻る
                    frmManage = new FormOwnerRsvManage();
                    frmManage.setSelHotelID( selHotelID );
                    frmManage.setUserId( paramUserID );
                    setRsvManage( frmManage, selHotelID, paramUserID, paramPlanId, dispAllFlag );
                    paramModeFlg = 0;
                    frmMenu.setModeFlg( paramModeFlg );
                    request.setAttribute( "FORM_Manage", frmManage );
                    request.setAttribute( "selPlanId", paramPlanId );
                    break;
            }

            // TOPページ設定 - 選択ホテルIDの設定
            if ( frmMenu.getSelHotelID() == 0 )
            {
                selHotelID = 0;
            }
            else if ( frmMenu.getSelHotelID() != 0 )
            {
                // 指定されたホテル
                selHotelID = frmMenu.getSelHotelID();
            }
            else
            {
                // ホテルリストの先頭を選択済みとする。
                selHotelID = frmMenu.getHotelIDList().get( 0 );
            }
            frmMenu.setSelHotelID( selHotelID );

            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.execute() ] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvMenu.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            frmMenu = null;
            frmHotelBasic = null;
            frmReserveBasic = null;
        }
    }

    /**
     * 
     * 予約管理呼び出し
     */
    private void setRsvManage(FormOwnerRsvManage frm, int selHotelId, int userId, int planId, boolean dispAllFlag) throws Exception
    {
        LogicOwnerRsvManage logicManage;
        LogicOwnerRsvRoomManage logicRoom;
        LogicOwnerRsvPlanManage logicPlan;
        LogicOwnerRsvManageCalendar logicCalendar;
        FormOwnerRsvRoomManage frmRoom;
        FormOwnerRsvPlanManage frmPlan;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList2 = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        ArrayList<Integer> planIdList = new ArrayList<Integer>();
        ArrayList<String> planIdNmList = new ArrayList<String>();
        String year = "";
        String month = "";
        String targetYYYYMM = "";
        String paramPlanId;

        logicManage = new LogicOwnerRsvManage();
        logicRoom = new LogicOwnerRsvRoomManage();
        logicPlan = new LogicOwnerRsvPlanManage();
        logicCalendar = new LogicOwnerRsvManageCalendar();
        frmRoom = new FormOwnerRsvRoomManage();
        frmPlan = new FormOwnerRsvPlanManage();
        try
        {
            // 管理情報取得
            frm.setSelHotelID( selHotelId );
            logicManage.setFrm( frm );
            logicManage.getRsvManage();

            // ホテル未選択時
            if ( selHotelId == 0 )
            {
                frm.setSelHotelErrMsg( Message.getMessage( "warn.30010" ) );
                return;
            }

            // プラン状況取得
            frmPlan.setSelHotelId( selHotelId );
            frmPlan.setUserId( userId );
            logicPlan.setFrm( frmPlan );
            logicPlan.getPlan( PUBLISHING_NON_DISP );

            if ( dispAllFlag == false && planId == -1 )
            {
                if ( frmPlan.getExPlanId().size() > 0 )
                {
                    planId = frmPlan.getExPlanId().get( 0 );
                }
            }

            // 1つ目のカレンダー情報取得
            // 当日取得
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
            if ( planId == -1 )
            {
                monthlyList = logicCalendar.getCalendarData( selHotelId, planId, Integer.parseInt( year + month ) );
            }
            // プランIDが入っていたら他で使っているロジックとは別のものを取得する（予約管理だけで使用するため）
            else
            {
                monthlyList = logicCalendar.getPlanCalendarData( selHotelId, planId, Integer.parseInt( year + month ) );
            }

            // 2つ目のカレンダー情報取得
            // 次月取得
            calendar = Calendar.getInstance();
            calendar.add( Calendar.MONTH, 1 );
            targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( calendar.getTime() );
            if ( planId == -1 )
            {
                monthlyList2 = logicCalendar.getCalendarData( selHotelId, planId, Integer.parseInt( targetYYYYMM ) );
            }
            // プランIDが入っていたら他で使っているロジックとは別のものを取得する（予約管理だけで使用するため）
            else
            {
                monthlyList2 = logicCalendar.getPlanCalendarData( selHotelId, planId, Integer.parseInt( targetYYYYMM ) );
            }

            // 部屋情報取得
            frmRoom.setSelHotelID( selHotelId );
            frmRoom.setUserId( userId );
            logicRoom.setFrm( frmRoom );
            logicRoom.getRoomData();

            // フォームにセット
            frm.setNowTopMonth( Integer.parseInt( year + month ) );
            frm.setMonthlyList( monthlyList );
            frm.setMonthlyList2( monthlyList2 );
            frm.setFrmPlan( logicPlan.getFrm() );
            frm.setFrmRoom( logicRoom.getFrm() );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setRsvManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setRsvManage] " + exception );
        }
    }

    /**
     * 
     * 施設基本情報設定画面呼び出し
     */
    private void setHotelBasic(FormOwnerRsvHotelBasic frm) throws Exception
    {
        LogicOwnerRsvHotelBasic logic;

        logic = new LogicOwnerRsvHotelBasic();

        try
        {
            logic.setFrm( frm );
            logic.getHotelRsv();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setHotelBasic() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setHotelBasic] " + exception );
        }
    }

    /**
     * 
     * 施設基本情報設定画面呼び出し
     */
    private void setReservBasic(FormOwnerRsvReserveBasic frm) throws Exception
    {
        LogicOwnerRsvReserveBasic logic;

        logic = new LogicOwnerRsvReserveBasic();

        try
        {
            frm.setParking( PARKING_SEL );
            logic.setFrm( frm );
            logic.getHotelRsv();
            if ( logic.getFrm().getParking() <= 0 )
            {
                // DBのデフォルト値が0なので初期値を再セットするように修正
                logic.getFrm().setParking( PARKING_SEL );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setReservBasic() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setReservBasic] " + exception );
        }
    }

    /**
     * 
     * 部屋情報呼び出し
     */
    private void setRoomManage(FormOwnerRsvRoomManage frm) throws Exception
    {
        LogicOwnerRsvRoomManage logic;

        logic = new LogicOwnerRsvRoomManage();

        try
        {
            logic.setFrm( frm );
            logic.getRoomData();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setRoomManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setRoomManage] " + exception );
        }
    }

    /**
     * 
     * 設備情報呼び出し
     */
    private void setEquip(FormOwnerRsvEquipManage frm) throws Exception
    {
        LogicOwnerRsvEquipManage logic;

        logic = new LogicOwnerRsvEquipManage();

        try
        {
            logic.setFrm( frm );
            logic.getEquipData();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setEquip() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setEquip] " + exception );
        }
    }

    /**
     * 
     * オプション管理呼び出し
     */
    private void setOption(FormOwnerRsvOptionManage frm) throws Exception
    {
        LogicOwnerRsvOptionManage logic;

        logic = new LogicOwnerRsvOptionManage();

        try
        {
            logic.setFrm( frm );
            logic.getOption();

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setOption() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setOption] " + exception );
        }
    }

    /**
     * 
     * 料金モード設定呼び出し
     */
    private void setChargeMode(FormOwnerRsvChargeMode frm) throws Exception
    {
        String errMsg = "";
        LogicOwnerRsvChargeMode logic;
        LogicOwnerRsvPlanChargeManage logicPlnCharge;

        logic = new LogicOwnerRsvChargeMode();
        logicPlnCharge = new LogicOwnerRsvPlanChargeManage();

        try
        {
            // 料金モードが存在するか
            if ( logicPlnCharge.existsChargeMode( frm.getSelHotelID() ) == false )
            {
                // 料金モードが存在しない
                errMsg = errMsg + Message.getMessage( "warn.30017" );
                frm.setErrMsg( errMsg );
            }

            logic.setFrm( frm );
            logic.getInit();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setChargeMode() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setChargeMode] " + exception );
        }
    }

    /**
     * 
     * プラン別料金設定呼び出し
     */
    private void setPlanChargeManage(FormOwnerRsvPlanChargeManage frm) throws Exception
    {
        LogicOwnerRsvPlanChargeManage logic;
        String errMsg = "";
        logic = new LogicOwnerRsvPlanChargeManage();

        try
        {
            logic.setFrm( frm );

            // 料金モードが存在するか
            if ( logic.existsChargeMode( frm.getSelHotelId() ) == true )
            {
                // プラン別料金情報取得
                logic.getPlanChargeData( 1 );
                return;
            }

            // 料金モードが存在しない
            errMsg = errMsg + Message.getMessage( "warn.30004" );

            // プラン情報が存在するか
            if ( frm.getPlanIdList().size() == 0 )
            {
                if ( errMsg.trim().length() != 0 )
                {
                    errMsg = errMsg + "<br /> ";
                }
                errMsg = errMsg + Message.getMessage( "erro.30001", "登録されているプラン情報" );
            }
            frm.setErrMsg( errMsg );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setPlanChargeManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setPlanChargeManage] " + exception );
        }
    }

    /**
     * 
     * プラン設定呼び出し
     */
    private void setPlanManage(FormOwnerRsvPlanManage frm) throws Exception
    {
        LogicOwnerRsvPlanManage logic;

        logic = new LogicOwnerRsvPlanManage();

        try
        {
            logic.setFrm( frm );
            logic.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
            frm.setViewMode( OwnerRsvCommon.PLAN_VIEW_PART );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setPlanManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setPlanManage()] " + exception );
        }
    }

    /**
     * 
     * カレンダー管理呼び出し
     */
    private void setPlanChargeCalendar(FormOwnerRsvPlanChargeCalendar frm, int imediaFlag) throws Exception
    {
        LogicOwnerRsvPlanChargeCalendar logic = new LogicOwnerRsvPlanChargeCalendar();
        int planId = 0;
        String year = "";
        String month = "";
        String planNm = "";
        ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>>();

        try
        {
            // プラン情報取得
            logic.setFrm( frm );
            logic.getPlanList();
            if ( frm.getPlanIdList().size() == 0 )
            {
                // プラン情報なし
                frm.setErrMsg( Message.getMessage( "erro.30001", "表示可能なカレンダー情報" ) );
                return;
            }

            // 先頭行のカレンダー情報を表示
            planId = frm.getPlanIdList().get( 0 );

            // プラン名取得
            planNm = frm.getPlanNmList().get( 0 );
            frm.setSelPlanNm( planNm );
            frm.setSelPlanId( planId );

            // 予約が入っているかチェック
            if ( OwnerRsvCommon.isExistsRsvPlan( frm.getSelHotelId(), planId ) != false )
            {
                frm.setWarnMsg( Message.getMessage( "warn.30041", Integer.toString( frm.getSelHotelId() ) ) );
            }

            // 当日取得
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );

            // カレンダー情報取得
            logic.getCalendar( frm.getSelHotelId(), planId, Integer.parseInt( year + month ), imediaFlag );
            monthlyList = logic.getFrm().getMonthlyList();

            frm.setMonthlyList( monthlyList );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setPlanChargeCalendar() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setPlanChargeCalendar()] " + exception );
        }
    }
}
