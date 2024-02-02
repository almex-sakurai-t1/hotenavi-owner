package jp.happyhotel.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataRsvMonthlyConfirm;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlanCharge;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendarSub;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlanCharge;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeCalendar;

public class ActionOwnerRsvPlanChargeCalendar extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvPlanChargeCalendar frmCalendar;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvPlanChargeCalendar logicCalendar;
        int paramHotelID = 0;
        int paramUserId = 0;
        int paramPlanId = -1;
        String paraOwnerHotelID = "";
        String paramTargetYM = "";
        int paramYYYY = 0;
        int paramMM = 0;
        String targetYYYYMM = "";
        Calendar orgCal;
        String planNm = "";
        String errMsg = "";
        ArrayList<Integer> chargeModeIdList = new ArrayList<Integer>();
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        boolean ret = false;

        try
        {
            frmCalendar = new FormOwnerRsvPlanChargeCalendar();
            frmMenu = new FormOwnerBkoMenu();
            orgCal = Calendar.getInstance();

            // 画面の値を取得
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramTargetYM = request.getParameter( "currentYM" );

            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( paraOwnerHotelID, paramUserId );
            adminflag = logicMenu.getAdminFlg( paraOwnerHotelID, paramUserId );
            imediaflag = OwnerRsvCommon.getImediaFlag( paraOwnerHotelID, paramUserId );

            if ( (imediaflag == 1 && adminflag == 1 && paraOwnerHotelID.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ページを閲覧する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( request.getParameter( "updCalendar" ) != null && disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && paraOwnerHotelID.equals( "happyhotel" )) == false )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( request.getParameter( "selPlanId" ) != null )
            {
                paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );
            }

            if ( request.getParameter( "preMonth" ) != null )
            {
                // ▼前月ボタン
                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );
                // 1ヶ月前取得
                orgCal.add( Calendar.MONTH, -1 );
            }
            else if ( request.getParameter( "nextMonth" ) != null )
            {
                // ▼翌月ボタン
                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );

                // 1ヶ月後取得
                orgCal.add( Calendar.MONTH, 1 );
            }
            else if ( request.getParameter( "updCalendar" ) != null )
            {
                // ▼カレンダー設定更新ボタン
                String[] ids = request.getParameterValues( "selChargeMode" );
                for( int i = 0 ; i < ids.length ; i++ )
                {
                    chargeModeIdList.add( Integer.parseInt( ids[i] ) );
                }

                // // 未選択のカレンダーが存在したらエラー
                // for( int i = 0 ; i < chargeModeIdList.size() ; i++ )
                // {
                // if ( chargeModeIdList.get( i ) == 0 )
                // {
                // frmCalendar.setInptErrMsg( Message.getMessage( "warn.00002", "料金モード" ) );
                // // プラン名取得
                // logicCalendar = new LogicOwnerRsvPlanChargeCalendar();
                // if ( paramPlanId == 0 )
                // {
                // // ホテル全体
                // planNm = OwnerRsvCommon.PLAN_CAL_HOTELNM;
                // }
                // else
                // {
                // planNm = logicCalendar.getPlanNm( paramHotelID, paramPlanId );
                // }
                // frmCalendar.setSelPlanNm( planNm );
                //
                // // カレンダー情報取得
                // targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
                // getMonth( frmCalendar, paramHotelID, targetYYYYMM, paramPlanId );
                // frmCalendar.setSelPlanId( paramPlanId );
                //
                // // メニュー、施設情報の設定
                // frmMenu.setUserId( paramUserId );
                // OwnerRsvCommon.setMenu( frmMenu, paramHotelID, 10, request.getCookies() );
                //
                // // 画面遷移
                // request.setAttribute( "FORM_Menu", frmMenu );
                // request.setAttribute( "FORM_Calendar", frmCalendar );
                // requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                // requestDispatcher.forward( request, response );
                // return;
                // }
                // }

                // データ更新
                frmCalendar.setSelHotelId( paramHotelID );
                frmCalendar.setSelPlanId( paramPlanId );
                frmCalendar.setOwnerHotelID( paraOwnerHotelID );
                frmCalendar.setUserId( paramUserId );
                logicCalendar = new LogicOwnerRsvPlanChargeCalendar();
                logicCalendar.setFrm( frmCalendar );
                if ( paramPlanId == 0 )
                {
                    // ホテル全体のカレンダー更新
                    logicCalendar.registHotelCalendar( chargeModeIdList, Integer.parseInt( paramTargetYM ) );

                    // ホテル修正履歴
                    String[] idOld = request.getParameterValues( "selChargeModeOld" );
                    for( int i = 0 ; i < ids.length ; i++ )
                    { // 変更があった日を調べる(edit_subは、cal_date)
                        if ( Integer.parseInt( ids[i] ) != Integer.parseInt( idOld[i] ) )
                        {
                            OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                    paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_CALENDAR,
                                    Integer.parseInt( paramTargetYM ) * 100 + i + 1, OwnerRsvCommon.ADJUST_MEMO_CALENDAR );
                        }
                    }
                }
                else
                {
                    // プランの日別料金モード更新
                    logicCalendar.registPlanCalendar( chargeModeIdList, Integer.parseInt( paramTargetYM ) );
                }

                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );

            }
            else if ( request.getParameter( "addCalendar" ) != null )
            {
                LogicOwnerRsvPlanCharge logic;
                DataRsvPlan data = new DataRsvPlan();
                FormOwnerRsvPlanCharge frm;

                data.getData( paramHotelID, paramPlanId );

                frm = new FormOwnerRsvPlanCharge();
                logic = new LogicOwnerRsvPlanCharge();
                frm.setSelHotelId( paramHotelID );
                frm.setSelPlanId( paramPlanId );
                logic.setFrm( frm );
                // データ登録時にプラン別カレンダーの作成を行う
                logic.addPlanCalendar( data.getSalesStopWeekStatus() );

                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );
            }
            else if ( request.getParameter( "calConfirm" ) != null )
            {
                DataRsvMonthlyConfirm drmc = new DataRsvMonthlyConfirm();
                ret = drmc.getData( paramHotelID, paramPlanId, Integer.parseInt( paramTargetYM ), paraOwnerHotelID, paramUserId );
                drmc.setConfirmDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                drmc.setConfirmTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                if ( ret == false )
                {
                    drmc.setId( paramHotelID );
                    drmc.setPlanId( paramPlanId );
                    drmc.setCalDate( Integer.parseInt( paramTargetYM ) );
                    drmc.setHotelId( paraOwnerHotelID );
                    drmc.setUserId( paramUserId );
                    drmc.setDispDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    drmc.setDispTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    drmc.insertData();
                }
                else
                {
                    drmc.updateData( paramHotelID, paramPlanId, Integer.parseInt( paramTargetYM ), paraOwnerHotelID, paramUserId );
                }

                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal.set( paramYYYY, paramMM - 1, 1 );
            }

            // プラン名取得
            logicCalendar = new LogicOwnerRsvPlanChargeCalendar();
            if ( paramPlanId == 0 )
            {
                // ホテル全体
                planNm = OwnerRsvCommon.PLAN_CAL_HOTELNM;
            }
            else
            {
                planNm = logicCalendar.getPlanNm( paramHotelID, paramPlanId );
            }
            frmCalendar.setSelPlanNm( planNm );

            // カレンダー情報取得
            targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
            getMonth( frmCalendar, paramHotelID, targetYYYYMM, paramPlanId, imediaflag );
            frmCalendar.setSelPlanId( paramPlanId );

            // メニュー、施設情報の設定
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, 10, request.getCookies() );

            // 予約が入っているかチェック
            if ( OwnerRsvCommon.isExistsRsvPlan( paramHotelID, paramPlanId ) != false )
            {
                frmCalendar.setWarnMsg( Message.getMessage( "warn.30041", Integer.toString( paramHotelID ) ) );
            }

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_Calendar", frmCalendar );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanChargeCalendar.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvPlanChargeCalendar.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }

    /**
     * 翌月、次月のカレンダー情報取得
     * 
     * @param logic FormOwnerRsvPlanChargeCalendarオブジェクト
     * @para, hotelId ホテルID
     * @param planId プランID
     * @param salesFlg 販売フラグ
     * @param planId プランID(未指定は-1)
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
            Logging.error( "[ActionOwnerRsvPlanChargeCalendar.getMonth() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvPlanChargeCalendar.getMonth] " + exception );
        }
    }

}
