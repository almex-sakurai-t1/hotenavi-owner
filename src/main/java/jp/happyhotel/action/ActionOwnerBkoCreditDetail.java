package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoCreditDetail;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoCreditDetail;

public class ActionOwnerBkoCreditDetail extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 200; // クレジット明細

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoCreditDetail frmCredit;
        LogicOwnerBkoCreditDetail logic;
        int paramHotelID = 0;
        int paramUserId = 0;
        int hotelId = 0;
        int paramYearFrom = 0;
        int paramMonthFrom = 0;
        int paramYearTo = 0;
        int paramMonthTo = 0;
        int paramrsvKind = 0;// 0:全て 1:ハピホテタッチ 2:ラブイン予約のみ 3:OTA
        String errMsg = "";

        try
        {
            frmCredit = new FormOwnerBkoCreditDetail();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerBkoCreditDetail();

            // ▼画面の値を取得
            // ホテルID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramYearFrom = Integer.parseInt( request.getParameter( "selYearFrom" ) );
            paramMonthFrom = Integer.parseInt( request.getParameter( "selMonthFrom" ) );
            paramYearTo = Integer.parseInt( request.getParameter( "selYearTo" ) );
            paramMonthTo = Integer.parseInt( request.getParameter( "selMonthTo" ) );
            if ( request.getParameter( "rsvKind" ) != null )
            {
                paramrsvKind = Integer.parseInt( request.getParameter( "rsvKind" ).toString() );
            }

            // 値をフォームにセット
            frmCredit.setSelHotelID( paramHotelID );
            frmCredit.setSelYearFrom( paramYearFrom );
            frmCredit.setSelMonthFrom( paramMonthFrom );
            frmCredit.setSelYearTo( paramYearTo );
            frmCredit.setSelMonthTo( paramMonthTo );
            frmCredit.setRsvKind( paramrsvKind );

            // データ取得
            logic.setFrm( frmCredit );
            logic.getCreditDetail();

            // メニューの設定
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, MENU_FLG, request.getCookies() );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_creditDetail", frmCredit );
            requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoCreditDetail.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoBillMonth.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }
}
