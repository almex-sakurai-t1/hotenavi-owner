package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoBillToday;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoBillToday;

public class ActionOwnerBkoBillToday extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 130; // 請求明細画面

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoBillToday frmBill;
        LogicOwnerBkoBillToday logic;
        int paramHotelID = 0;
        int paramUserId = 0;
        String paramYear = "";
        String paramMonth = "";
        int paramCallKbn = 0;
        int paramUsageDate = 0;
        int paramBillDate = 0;
        int paramrsvKind = 0;// 0:全て 1:ハピホテタッチ 2:ラブイン予約のみ
        int hotelId = 0;
        int menuFlg = 0;
        String errMsg = "";

        try
        {
            frmBill = new FormOwnerBkoBillToday();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerBkoBillToday();

            // ▼画面の値を取得
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            if ( request.getParameter( "callKbn" ) != null )
            {
                paramCallKbn = 1;
            }
            if ( paramCallKbn != 1 )
            {
                // メニュー、検索クリックから呼び出された場合
                paramYear = request.getParameter( "selYearFrom" );
                paramMonth = String.format( "%02d", Integer.parseInt( request.getParameter( "selMonthFrom" ) ) );
                paramBillDate = Integer.parseInt( paramYear + paramMonth );
            }
            else
            {
                // 費別請求から呼び出された場合
                paramYear = request.getParameter( "billDate" ).toString().substring( 0, 4 );
                paramMonth = request.getParameter( "billDate" ).toString().substring( 4, 6 );
                paramUsageDate = Integer.parseInt( request.getParameter( "usageDate" ) );
                paramBillDate = Integer.parseInt( request.getParameter( "billDate" ) );
            }
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            if ( request.getParameter( "rsvKind" ) != null )
            {
                paramrsvKind = Integer.parseInt( request.getParameter( "rsvKind" ).toString() );
            }

            // 値をフォームにセット
            frmBill.setSelHotelID( paramHotelID );
            frmBill.setSelYear( Integer.parseInt( paramYear ) );
            frmBill.setSelMonth( Integer.parseInt( paramMonth ) );
            frmBill.setUsageDate( paramUsageDate );
            frmBill.setIntBillDate( paramBillDate );
            frmBill.setRsvKind( paramrsvKind );
            menuFlg = MENU_FLG;

            // データ取得
            logic.setFrm( frmBill );
            logic.getAccountRecv();
            frmBill = logic.getFrm();

            // メニューの設定
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_billToday", frmBill );
            requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoBillToday.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoBillToday.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }
}
