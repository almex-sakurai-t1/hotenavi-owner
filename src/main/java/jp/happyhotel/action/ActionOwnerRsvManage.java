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
import jp.happyhotel.data.DataRsvRoom;
import jp.happyhotel.owner.FormOwnerRsvManage;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlanCharge;
import jp.happyhotel.owner.FormOwnerRsvPlanManage;
import jp.happyhotel.owner.FormOwnerRsvRoomManage;
import jp.happyhotel.owner.LogicOwnerRsvManage;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlanCharge;
import jp.happyhotel.owner.LogicOwnerRsvPlanManage;
import jp.happyhotel.owner.LogicOwnerRsvRoomManage;

/**
 * 
 * 予約管理画面
 */
public class ActionOwnerRsvManage extends BaseAction
{

    private RequestDispatcher   requestDispatcher = null;
    private static final String MODE_HOTEL        = "hotelSales";
    private static final String MODE_CALENDAR     = "calendarSales";
    private static final String MODE_ROOM         = "roomSales";
    private static final String MODE_PLAN         = "planSales";
    private static final String MODE_PREMONTH     = "preMonth";
    private static final String MODE_NEXTMONTH    = "nextMonth";
    // 部屋残数ステータス
    private static final int    STATUS_EMP        = 1;              // 空き
    private static final int    STATUS_STOP       = 3;              // 売り止め

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvManage frmManage;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvManage logic;
        int paramHotelID = 0;
        int paramSeq = 0;
        int paramSalesFlg = 0;
        int paramPlanId = 0;
        String paramBtnMode = "";
        int paramUserId = 0;
        String paramTargetYM = "";
        int hotelId = 0;
        int paramYYYY = 0;
        int paramMM = 0;
        String paramTopMonth = "";
        String targetYYYYMM = "";
        Calendar orgCal;
        String errMsg = "";
        int selPlanId = -1;
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            frmManage = new FormOwnerRsvManage();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerRsvManage();
            // 画面の値を取得
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( hotenaviId, paramUserId );
            adminflag = logicMenu.getAdminFlg( hotenaviId, paramUserId );
            imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, paramUserId );

            if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ページを閲覧する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            // 選択されたプランIDをセット
            if ( (request.getParameter( "selPlanId" ) != null) && (request.getParameter( "selPlanId" ).toString().length() != 0) )
            {
                selPlanId = Integer.parseInt( request.getParameter( "selPlanId" ).toString() );
            }

            paramBtnMode = request.getParameter( "mode" );
            paramSalesFlg = Integer.parseInt( request.getParameter( "saleFlg" ) );

            if ( (paramBtnMode.equals( MODE_HOTEL ) || paramBtnMode.equals( MODE_CALENDAR ) || paramBtnMode.equals( MODE_ROOM ) || paramBtnMode.equals( MODE_PLAN )) &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( paramBtnMode.equals( MODE_HOTEL ) )
            {
                // ▼ホテルの販売フラグ更新
                paramTargetYM = request.getParameter( "currentYM" );
                updHotelSalesFlg( logic, paramHotelID, paramSalesFlg, paramUserId );
                // ホテル修正履歴
                OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                        paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_RSV, paramSalesFlg == 0 ? 1 : 0,
                        paramSalesFlg == 0 ? OwnerRsvCommon.ADJUST_MEMO_RSV_START : OwnerRsvCommon.ADJUST_MEMO_RSV_STOP );
            }
            else if ( paramBtnMode.equals( MODE_CALENDAR ) )
            {
                // ▼カレンダー情報の販売フラグ更新
                paramTopMonth = request.getParameter( "topMonth" );
                paramTargetYM = request.getParameter( "currentYM" );

                // プランが選択されていない
                if ( selPlanId == -1 )
                {
                    updCalendarSalesFlg( logic, paramHotelID, Integer.parseInt( paramTargetYM ), paramSalesFlg, paramUserId );
                }
                else
                {
                    updChargeMode( logic, paramHotelID, Integer.parseInt( paramTargetYM ), selPlanId, paramSalesFlg, paramUserId );
                }
                // ホテル修正履歴
                OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                        paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_RSV_DAY,
                        Integer.parseInt( paramTargetYM ) * 10 + (paramSalesFlg == 0 ? 1 : 0),
                        paramSalesFlg == 0 ? OwnerRsvCommon.ADJUST_MEMO_RSV_START_DAY : OwnerRsvCommon.ADJUST_MEMO_RSV_STOP_DAY );
                paramTargetYM = paramTopMonth.substring( 0, 6 );
            }
            else if ( paramBtnMode.equals( MODE_ROOM ) )
            {
                // ▼部屋状況の販売フラグ更新
                paramSeq = Integer.parseInt( request.getParameter( "seq" ) );
                paramTargetYM = request.getParameter( "currentYM" );
                updRoomSalesFlg( logic, paramHotelID, paramSeq, paramSalesFlg, paramUserId );
            }
            else if ( paramBtnMode.equals( MODE_PLAN ) )
            {
                // ▼プラン状況の販売フラグ更新
                paramPlanId = Integer.parseInt( request.getParameter( "planId" ) );
                paramTargetYM = request.getParameter( "currentYM" );
                updPlanSalesFlg( logic, paramHotelID, paramPlanId, paramSalesFlg, paramUserId );
                // ホテル修正履歴
                OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                        paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_RSV_PLAN,
                        paramPlanId * 10 + (paramSalesFlg == 0 ? 1 : 0),
                        paramSalesFlg == 0 ? OwnerRsvCommon.ADJUST_MEMO_RSV_START_PLAN : OwnerRsvCommon.ADJUST_MEMO_RSV_STOP_PLAN );
            }
            else if ( paramBtnMode.equals( MODE_PREMONTH ) )
            {
                // ▼前月ボタン
                paramTargetYM = request.getParameter( "currentYM" );
                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal = Calendar.getInstance();
                orgCal.set( paramYYYY, paramMM - 1, 1 );

                // 2ヶ月前取得
                orgCal.add( Calendar.MONTH, -2 );
                targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
                paramTargetYM = targetYYYYMM;
            }
            else if ( paramBtnMode.equals( MODE_NEXTMONTH ) )
            {
                // ▼翌月ボタン
                paramTargetYM = request.getParameter( "currentYM" );
                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal = Calendar.getInstance();
                orgCal.set( paramYYYY, paramMM - 1, 1 );

                // 2ヶ月前取得
                orgCal.add( Calendar.MONTH, 2 );
                targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
                paramTargetYM = targetYYYYMM;
            }

            // メニュー、施設情報の設定
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, 0, request.getCookies() );
            setPage( paramHotelID, selPlanId, paramUserId, frmManage, paramTargetYM );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_Manage", frmManage );
            request.setAttribute( "selPlanId", selPlanId );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvManage.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvManage.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }

    /**
     * カレンダーの販売フラグ更新
     * 
     * @param logic LogicOwnerRsvManageオブジェクト
     * @param hotelId ホテルID
     * @param calDate 対象日付
     * @param salesFlg 販売フラグ
     * @param userId ユーザーID
     * @return なし
     */
    private void updCalendarSalesFlg(LogicOwnerRsvManage logic, int hotelId, int calDate, int salesFlg, int userId) throws Exception
    {
        int newSalesFlg = 0;
        int status = 0;
        int condStatus = 0;

        try
        {
            // 販売フラグの更新
            if ( salesFlg == 0 )
            {
                newSalesFlg = 1;
            }

            // 存在する場合は更新
            logic.updCalendarSalesFlg( hotelId, calDate, newSalesFlg, userId );

            // 部屋残数データの更新
            if ( salesFlg == 1 )
            {
                // 停止クリック時
                status = STATUS_STOP;
                condStatus = STATUS_EMP;
            }
            else
            {
                // 開始クリック
                status = STATUS_EMP;
                condStatus = STATUS_STOP;
            }
            logic.updRoomRemainder( hotelId, calDate, status, condStatus );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updCalendarSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updCalendarSalesFlg()] " + exception );
        }
    }

    /**
     * 部屋状況の販売フラグ更新
     * 
     * @param logic LogicOwnerRsvManageオブジェクト
     * @para, hotelId ホテルID
     * @param seq 管理番号
     * @param salesFlg 販売フラグ
     * @@aram userId ユーザーID
     * @return なし
     */
    private void updRoomSalesFlg(LogicOwnerRsvManage logic, int hotelId, int seq, int salesFlg, int userId) throws Exception
    {
        boolean isExists = false;
        int newSalesFlg = 0;
        DataRsvRoom rsvData = new DataRsvRoom();

        try
        {
            // 販売フラグの更新
            if ( salesFlg == 0 )
            {
                newSalesFlg = 1;
            }

            // 予約部屋マスタが存在するか
            isExists = rsvData.getData( hotelId, seq );
            if ( isExists == true )
            {
                // 存在する場合は更新
                logic.updRoomSalesFlg( hotelId, seq, newSalesFlg, userId );
            }
            else
            {
                // 存在しない場合は追加
                logic.execInsRsvRoom( hotelId, seq, newSalesFlg, userId );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updRoomSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updRoomSalesFlg()] " + exception );
        }
    }

    /**
     * プラン状況の販売フラグ更新
     * 
     * @param logic LogicOwnerRsvPlanManageオブジェクト
     * @para, hotelId ホテルID
     * @param planId プランID
     * @param salesFlg 販売フラグ
     * @@aram userId ユーザーID
     * @return なし
     */
    private void updPlanSalesFlg(LogicOwnerRsvManage logic, int hotelId, int planId, int salesFlg, int userId) throws Exception
    {
        int newSalesFlg = 0;

        try
        {
            // 販売フラグの更新
            if ( salesFlg == 0 )
            {
                newSalesFlg = 1;
            }
            logic.updPlanSalesFlg( hotelId, planId, newSalesFlg, userId );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updRoomSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updRoomSalesFlg()] " + exception );
        }
    }

    /**
     * ホテルの販売フラグ更新
     * 
     * @param logic LogicOwnerRsvPlanManageオブジェクト
     * @para, hotelId ホテルID
     * @param salesFlg 販売フラグ
     * @@aram userId ユーザーID
     * @return なし
     */
    private void updHotelSalesFlg(LogicOwnerRsvManage logic, int hotelId, int salesFlg, int userId) throws Exception
    {
        int newSalesFlg = 0;

        try
        {
            // 販売フラグの更新
            if ( salesFlg == 0 )
            {
                newSalesFlg = 1;
            }
            logic.updHotelSalesFlg( hotelId, newSalesFlg, userId );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updHotelSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updHotelSalesFlg()] " + exception );
        }
    }

    /**
     * 
     * 画面表示
     */
    private void setPage(int hotelId, int planId, int userId, FormOwnerRsvManage frm, String targetYM) throws Exception
    {
        int year = 0;
        int month = 0;
        String targetYYYYMM = "";
        FormOwnerRsvRoomManage frmRoom;
        FormOwnerRsvPlanManage frmPlan;
        LogicOwnerRsvManage logicManage;
        LogicOwnerRsvRoomManage logicRoom;
        LogicOwnerRsvPlanManage logicPlan;
        LogicOwnerRsvManageCalendar logicCalendar;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList2 = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();

        frmRoom = new FormOwnerRsvRoomManage();
        frmPlan = new FormOwnerRsvPlanManage();
        logicManage = new LogicOwnerRsvManage();
        logicRoom = new LogicOwnerRsvRoomManage();
        logicPlan = new LogicOwnerRsvPlanManage();
        logicCalendar = new LogicOwnerRsvManageCalendar();

        try
        {
            // ホテル未選択時
            if ( hotelId == 0 )
            {
                frm.setSelHotelErrMsg( Message.getMessage( "warn.30010" ) );
                return;
            }

            // ▼1つ目のカレンダー情報取得
            if ( planId == -1 )
            {
                monthlyList = logicCalendar.getCalendarData( hotelId, planId, Integer.parseInt( targetYM ) );
            }
            // プランIDは他で使っているロジックとは別のものを取得する（予約管理だけで使用するため）
            else
            {
                monthlyList = logicCalendar.getPlanCalendarData( hotelId, planId, Integer.parseInt( targetYM ) );
            }

            // ▼2つ目のカレンダー情報取得
            // 次月取得
            Calendar calendar = Calendar.getInstance();
            calendar = Calendar.getInstance();
            year = Integer.parseInt( targetYM.toString().substring( 0, 4 ) );
            month = Integer.parseInt( targetYM.toString().substring( 4 ) );
            calendar.set( year, month - 1, 1 );
            calendar.add( Calendar.MONTH, 1 );
            targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( calendar.getTime() );

            if ( planId == -1 )
            {
                monthlyList2 = logicCalendar.getCalendarData( hotelId, planId, Integer.parseInt( targetYYYYMM ) );
            }
            // プランIDが入っていたら他で使っているロジックとは別のものを取得する（予約管理だけで使用するため）
            else
            {
                monthlyList2 = logicCalendar.getPlanCalendarData( hotelId, planId, Integer.parseInt( targetYYYYMM ) );
            }

            // ▼ホテル情報取得
            frm.setSelHotelID( hotelId );
            logicManage.setFrm( frm );
            logicManage.getRsvManage();

            // ▼プラン情報取得
            frmPlan.setSelHotelId( hotelId );
            frmPlan.setUserId( userId );
            logicPlan.setFrm( frmPlan );
            // 未掲載は表示しないように変更
            logicPlan.getPlan( 1 );

            // ▼部屋情報取得
            frmRoom.setSelHotelID( hotelId );
            frmRoom.setUserId( userId );
            logicRoom.setFrm( frmRoom );
            logicRoom.getRoomData();

            // フォームにセット
            frm.setNowTopMonth( Integer.parseInt( targetYM ) );
            frm.setMonthlyList( monthlyList );
            frm.setMonthlyList2( monthlyList2 );
            frm.setFrmPlan( frmPlan );
            frm.setFrmRoom( logicRoom.getFrm() );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvManage.setPage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvManage.setPage()]] " + exception );
        }
    }

    /**
     * カレンダーの販売フラグ更新
     * 
     * @param logic LogicOwnerRsvManageオブジェクト
     * @param hotelId ホテルID
     * @param calDate 対象日付
     * @param planId 予約番号
     * @param chargeMode 料金番号
     * @param userId ユーザーID
     * @return なし
     */
    private void updChargeMode(LogicOwnerRsvManage logic, int hotelId, int calDate, int planId, int chargeMode, int userId) throws Exception
    {
        int newChargeMode = 0;
        int status = 0;
        int condStatus = 0;
        FormOwnerRsvPlanCharge frm;
        LogicOwnerRsvPlanCharge planCharge;

        try
        {
            if ( chargeMode == 1 )
            {
                frm = new FormOwnerRsvPlanCharge();
                frm.setSelHotelId( hotelId );
                frm.setSelPlanId( planId );
                planCharge = new LogicOwnerRsvPlanCharge();
                planCharge.setFrm( frm );
                // 対応する料金モードを取得する
                newChargeMode = planCharge.getChargeMode( hotelId, planId, calDate );
            }

            // 存在する場合は更新
            logic.updChargeMode( hotelId, calDate, planId, newChargeMode, userId );

            // // 部屋残数データの更新
            // if ( chargeMode == 0 )
            // {
            // // 停止クリック時
            // status = STATUS_STOP;
            // condStatus = STATUS_EMP;
            // }
            // else
            // {
            // // 開始クリック
            // status = STATUS_EMP;
            // condStatus = STATUS_STOP;
            // }
            // logic.updRoomRemainder( hotelId, calDate, status, condStatus );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updCalendarSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updCalendarSalesFlg()] " + exception );
        }
    }
}
