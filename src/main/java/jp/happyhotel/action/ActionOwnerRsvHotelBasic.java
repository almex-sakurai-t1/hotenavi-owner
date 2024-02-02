package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.owner.FormOwnerRsvHotelBasic;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvHotelBasic;
import jp.happyhotel.owner.LogicOwnerBkoMenu;

/**
 * 
 * 施設基本情報画面
 */

public class ActionOwnerRsvHotelBasic extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvHotelBasic frmHotelBasic;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvHotelBasic logic;
        int paramHotelID = 0;
        String paramRsvPR = "";
        String paraOwnerHotelID = "";
        String paramChildCharge = "";
        int paramUserId = 0;
        int hotelId = 0;
        boolean frmSetFlg = false;
        String errMsg = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {

            logic = new LogicOwnerRsvHotelBasic();

            // 画面の値を取得
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramRsvPR = ((String)request.getParameter( "reservePR" )).trim();
            paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramChildCharge = ((String)request.getParameter( "childCharge" )).trim();

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

            frmHotelBasic = new FormOwnerRsvHotelBasic();
            frmMenu = new FormOwnerBkoMenu();

            // 値をフォームにセット
            frmHotelBasic.setHotelId( paramHotelID );
            frmHotelBasic.setReservePr( paramRsvPR );
            frmHotelBasic.setOwnerHotelID( paraOwnerHotelID );
            frmHotelBasic.setUserID( paramUserId );
            frmHotelBasic.setChildCharge( paramChildCharge );

            // 入力チェック
            if ( isInputCheckMsg( frmHotelBasic ) != false )
            {
                if ( disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && paraOwnerHotelID.equals( "happyhotel" )) == false )
                {
                    errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
                // データの更新
                logic.setFrm( frmHotelBasic );
                logic.registHotelBase();
                frmSetFlg = true;
                // ホテル修正履歴
                OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_HOTEL_BASIC, 0, OwnerRsvCommon.ADJUST_MEMO_HOTEL_BASIC );
            }

            // メニュー、施設情報の設定
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, 1, request.getCookies() );
            setPage( frmHotelBasic, paramHotelID );

            // 入力エラーの場合、入力値は保持する
            if ( frmSetFlg == false )
            {
                frmHotelBasic.setReservePr( paramRsvPR );
            }

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_HotelBasic", frmHotelBasic );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvHotelBasic.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvHotelBasic.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * 入力値チェック
     * 
     * @param input FormOwnerU010301オブジェクト
     * @return true:正常、false:エラーあり
     */
    private boolean isInputCheckMsg(FormOwnerRsvHotelBasic frm) throws Exception
    {
        String msg;
        int ret;
        boolean isCheck = false;
        ReserveCommon rsvcomm = new ReserveCommon();

        msg = "";
        ret = 0;

        // 予約PR
        if ( CheckString.onlySpaceCheck( frm.getReservePr() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "予約PR" ) + "<br />";
        }
        else
        {
            ret = OwnerRsvCommon.LengthCheck( frm.getReservePr().trim(), 50 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                msg = msg + Message.getMessage( "warn.00038", "予約PR", "25" ) + "<br />";
            }
        }

        // 子供料金の定義(旅館業法のみ)
        if ( rsvcomm.checkLoveHotelFlag( frm.getHotelId() ) == false )
        {
            if ( CheckString.onlySpaceCheck( frm.getChildCharge() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "子供料金定義" ) + "<br />";
            }
            else
            {
                ret = OwnerRsvCommon.LengthCheck( frm.getChildCharge(), 60 );
                if ( ret == 1 )
                {
                    // 桁数Overの場合
                    msg = msg + Message.getMessage( "warn.00038", "子供料金定義", "30" ) + "<br />";
                }
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
     * 施設基本情報を設定する
     * 
     * @param frm FormOwnerU010301オブジェクト
     * @param selHotelID 選択されているホテルID
     * @return なし
     */
    private void setPage(FormOwnerRsvHotelBasic frm, int selHotelID) throws Exception
    {
        LogicOwnerRsvHotelBasic logic = new LogicOwnerRsvHotelBasic();

        try
        {
            frm.setSelHotelID( selHotelID );
            logic.setFrm( frm );
            logic.getHotelRsv();

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvHotelBasic.setMenu() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvHotelBasic.setMenu() ] " + e.getMessage() );
        }
    }
}
