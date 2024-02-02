package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoAnalysisCardless;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoAnalysisCardless;

public class ActionOwnerBkoAnalysisCardless extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 190; // カードレスメンバー利用分析画面

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoAnalysisCardless frm;
        LogicOwnerBkoAnalysisCardless logic;
        int paramHotelID = 0;
        int paramUserId = 0;
        String paramYear = "";
        String paramMonth = "";
        int hotelId = 0;
        int menuFlg = 0;
        String simeKikan = "";
        String errMsg = "";

        try
        {
            frm = new FormOwnerBkoAnalysisCardless();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerBkoAnalysisCardless();

            // ▼画面の値を取得
            // ホテルID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramYear = request.getParameter( "selYear" );
            paramMonth = String.format( "%02d", Integer.parseInt( request.getParameter( "selMonth" ) ) );

            // 値をフォームにセット
            frm.setSelHotelID( paramHotelID );
            frm.setSelYear( Integer.parseInt( paramYear ) );
            frm.setSelMonth( Integer.parseInt( paramMonth ) );

            menuFlg = MENU_FLG;

            // 画面の日付から期間を取得(月末取得（月初～月末））
            String ymdStr = "";
            int ymdInt = 0;
            ymdStr = DateEdit.getDate( 2, frm.getSelYear(), frm.getSelMonth(), 1 );
            frm.setDateFrom( Integer.parseInt( ymdStr ) );
            ymdInt = DateEdit.addMonth( Integer.parseInt( ymdStr ), 1 );
            ymdInt = DateEdit.addDay( ymdInt, -1 );
            frm.setDateTo( ymdInt );

            simeKikan = frm.getDateFrom() / 10000 + "年" + (frm.getDateFrom() % 10000) / 100 + "月" + frm.getDateFrom() % 100 + "日～" + frm.getDateTo() / 10000 + "年" + (frm.getDateTo() % 10000) / 100 + "月" + (frm.getDateTo() % 100) + "日";
            frm.setSimeKikan( simeKikan );

            // データ取得
            logic.setFrm( frm );
            logic.getAnalysisCardless();
            frm = logic.getFrm();

            // メニューの設定
            frmMenu.setUserId( paramUserId );
            frm.setSelHotelID( paramHotelID );
            frm.setSelYear( Integer.parseInt( paramYear ) );
            frm.setSelMonth( Integer.parseInt( paramMonth ) );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_cardless", frm );
            requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoAnalysisCardless.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }
}
