package jp.happyhotel.action;

import java.text.DateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoBillPrint;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoBillPrint;

public class ActionOwnerBkoBillPrint extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 170; // 請求書発行画面

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoBillPrint frmBill;
        String paraOwnerHotelID = "";
        int paramHotelID = 0;
        int paramUserId = 0;
        int hotelId = 0;
        int menuFlg = 0;
        int imediaFlg = 0;
        String errMsg = "";

        try
        {
            frmBill = new FormOwnerBkoBillPrint();
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
            frmBill.setImediaFlg( imediaFlg );

            frmBill.setBillYear( Integer.parseInt( request.getParameter( "billYear" ).toString() ) );
            frmBill.setBillMonth( Integer.parseInt( request.getParameter( "billMonth" ).toString() ) );

            if ( imediaFlg == 1 )
            {
                frmBill.setIssueYear( Integer.parseInt( request.getParameter( "issueYear" ).toString() ) );
                frmBill.setIssueMonth( Integer.parseInt( request.getParameter( "issueMonth" ).toString() ) );
                frmBill.setIssueDay( Integer.parseInt( request.getParameter( "issueDay" ).toString() ) );

                if ( request.getParameter( "chkReissue" ) != null )
                {
                    frmBill.setChkReissue( request.getParameter( "chkReissue" ) );
                }
            }

            // 入力チェック
            errMsg = inputCheck( frmBill );
            if ( errMsg.trim().length() != 0 )
            {
                // エラーあり
                frmBill.setErrMsg( errMsg );
                frmBill.setBlankCloseFlg( true );

                // メニューの設定
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_billPrint", frmBill );
                requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
                requestDispatcher.forward( request, response );

                return;
            }

            LogicOwnerBkoBillPrint logic = new LogicOwnerBkoBillPrint();

            // 請求書発行
            logic.setFrm( frmBill );

            if ( !logic.getData() )
            {

                // メニューの設定
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

                // 請求書発行
                frmBill.setErrMsg( "今月の請求はありません。" );
                frmBill.setBlankCloseFlg( true );

                // 画面遷移
                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_billPrint", frmBill );
                requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            // 請求書発行
            logic.doPost( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoBillPrint.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoBillPrint.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }

    private String inputCheck(FormOwnerBkoBillPrint frm)
    {
        String errMsg = "";
        String date = "";
        DateFormat format = DateFormat.getDateInstance();

        date = Integer.toString( frm.getIssueYear() ) + "/" + String.format( "%02d", frm.getIssueMonth() ) + "/" + String.format( "%02d", frm.getIssueDay() );

        // ▼日付の妥当性チェック
        // 発行日付
        format.setLenient( false );
        try
        {
            format.parse( date );
        }
        catch ( Exception e )
        {
            errMsg = errMsg + Message.getMessage( "erro.30007", "発行日付" ) + "<br>";
        }

        return(errMsg);
    }
}
