package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoBillMonth;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoBillMonth;

public class ActionOwnerBkoBillMonthPrint extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 150; // 月別請求明細印刷

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoBillMonth frmBill;
        // LogicOwnerBkoBillMonth logic;
        String paraOwnerHotelID = "";
        int paramHotelID = 0;
        int paramUserId = 0;
        int hotelId = 0;
        int menuFlg = 0;
        int imediaFlg = 0;
        String errMsg = "";
        int paramrsvKind = 0;// 0:全て 1:ハピホテタッチ 2:ラブイン予約のみ

        try
        {
            frmBill = new FormOwnerBkoBillMonth();
            frmMenu = new FormOwnerBkoMenu();

            // ▼画面の値を取得
            // ホテルID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );

            // 値をフォームにセット
            frmBill.setSelHotelID( paramHotelID );
            menuFlg = MENU_FLG;

            // 事務局フラグ
            imediaFlg = OwnerRsvCommon.getImediaFlag( paraOwnerHotelID, paramUserId );

            frmBill.setSelYearFrom( Integer.parseInt( request.getParameter( "selYearFrom" ).toString() ) );
            frmBill.setSelMonthFrom( Integer.parseInt( request.getParameter( "selMonthFrom" ).toString() ) );
            frmBill.setSelYearTo( Integer.parseInt( request.getParameter( "selYearTo" ).toString() ) );
            frmBill.setSelMonthTo( Integer.parseInt( request.getParameter( "selMonthTo" ).toString() ) );
            frmBill.setRsvKind( Integer.parseInt( request.getParameter( "rsvKind" ).toString() ) );

            // 入力チェック
            if ( errMsg.trim().length() != 0 )
            {
                // エラーあり
                frmBill.setErrMsg( errMsg );

                // メニューの設定
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
                frmBill.setBlankCloseFlg( true );

                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_billMonth", frmBill );
                requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
                requestDispatcher.forward( request, response );

                return;
            }

            LogicOwnerBkoBillMonth logic = new LogicOwnerBkoBillMonth();

            // データ取得
            logic.setFrm( frmBill );
            logic.getAccountRecv();
            if ( !frmBill.getErrMsg().trim().equals( "" ) )
            {

                // メニューの設定
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
                frmBill.setBlankCloseFlg( true );

                // 画面遷移
                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_billMonth", frmBill );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // クレジット明細出力
            logic.doPost( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoBillMonth.execute() ][hotelId = " + hotelId + "] Exception", exception );
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
