package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlanCharge;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeSub;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlanCharge;

/**
 * 
 * プラン別料金管理画面
 */
public class ActionOwnerRsvPlanChargeManage extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg           = 51;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerRsvPlanCharge frm;
        FormOwnerRsvPlanChargeSub frmSub;
        LogicOwnerRsvPlanCharge logic;
        int paramHotelID = 0;
        int paramUserID = 0;
        int paramPlanID = 0;
        int paramEdit = 0;
        int chashDeposit = 0;
        String errMsg = "";
        String hotenaviId = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            frmMenu = new FormOwnerBkoMenu();
            frm = new FormOwnerRsvPlanCharge();
            frmSub = new FormOwnerRsvPlanChargeSub();
            logic = new LogicOwnerRsvPlanCharge();

            // 画面の値を取得
            paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            paramUserID = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramPlanID = Integer.parseInt( request.getParameter( "planID" ) );
            paramEdit = Integer.parseInt( request.getParameter( "edit" ) );

            // ログインユーザと担当ホテルのチェック
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( (paramHotelID != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, paramUserID, paramHotelID ) == false) )
            {
                // 管理外のホテルはログイン画面へ遷移
                response.sendRedirect( "../../owner/index.jsp" );
                return;
            }
            // 表示権限関係取得
            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( hotenaviId, paramUserID );
            adminflag = logicMenu.getAdminFlg( hotenaviId, paramUserID );
            imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, paramUserID );

            if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ページを閲覧する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // フォームにセット
            frm.setSelHotelId( paramHotelID );
            frm.setSelPlanId( paramPlanID );
            frm.setEditMode( paramEdit );

            // プラン名取得
            DataRsvPlan data = new DataRsvPlan();
            data.getData( paramHotelID, paramPlanID );
            frm.setPlanNm( data.getPlanName() );
            frm.setSalesStopWeekStatus( data.getSalesStopWeekStatus() );
            frm.setSalesStopWeekView( OwnerRsvCommon.createSalesStopWeek( data.getSalesStopWeekStatus() ) );

            // 料金モード取得
            logic.setFrm( frm );
            logic.setFrmSub( frmSub );
            logic.getPlanCharge();

            // 最低予約金額取得
            chashDeposit = logic.getChashDeposit( paramHotelID );
            frm.setCashDeposit( chashDeposit );

            // 予約情報存在チェック
            if ( OwnerRsvCommon.isReservePlan( paramHotelID, paramPlanID ) == true )
            {
                // 存在する場合
                if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
                {
                    frm.setViewMode( 1 );
                }
                frm.setRsvMsg( Message.getMessage( "warn.30012", Integer.toString( paramHotelID ) ) );
            }

            // メニュー設定
            frmMenu.setUserId( paramUserID );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_Charge", frm );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanChargeManage.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvPlanChargeManage.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }
}
