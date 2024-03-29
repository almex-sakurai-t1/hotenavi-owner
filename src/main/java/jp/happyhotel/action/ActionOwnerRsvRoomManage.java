package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvRoom;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvRoom;

/**
 * 
 * 部屋管理画面
 * 部屋設定画面へ遷移するActionクラス
 */
public class ActionOwnerRsvRoomManage extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg           = 71;
    private static final int  SEARCH_ALL        = 1;
    private static final int  SEARCH_SEQ        = 2;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvRoom frmRoom;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvRoom logic;
        String hotenaviID = "";
        int paramHotelID = 0;
        int paramRoomRank = 0;
        int paramSeq = 0;
        int paramUserId = 0;
        int hotelId = 0;
        String errMsg = "";
        String hotenaviId = "";
        ArrayList<Integer> selEqIdList = new ArrayList<Integer>();
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            // 画面の値を取得
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramRoomRank = Integer.parseInt( request.getParameter( "roomRank" ) );
            paramSeq = Integer.parseInt( request.getParameter( "seq" ) );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );

            // ログインユーザと担当ホテルのチェック
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( (paramHotelID != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, paramUserId, paramHotelID ) == false) )
            {
                // 管理外のホテルはログイン画面へ遷移
                response.sendRedirect( "../../owner/index.jsp" );
                return;
            }
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

            frmRoom = new FormOwnerRsvRoom();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerRsvRoom();

            // 値をフォームにセット
            frmRoom.setSelHotelID( paramHotelID );
            frmRoom.setRoomRank( paramRoomRank );
            frmRoom.setSeq( paramSeq );
            frmRoom.setUserID( paramUserId );

            // ホテナビID取得
            hotenaviID = logic.getHotenaviID( paramHotelID );
            frmRoom.setHotenaviID( hotenaviID );

            // 部屋情報の取得
            logic.setFrm( frmRoom );
            logic.getRoomDetailData();

            // ホテルの設備情報の取得
            logic.getEquip( SEARCH_ALL );

            // 部屋の設備情報の取得
            logic.getEquip( SEARCH_SEQ );

            // 選択済み設備の設定
            selEqIdList = OwnerRsvCommon.setSelEqList( frmRoom.getAllEquipIdList(), frmRoom.getEquipIdList() );
            frmRoom.setSelEqCheckList( selEqIdList );

            // メニュー、予約情報の設定
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_Room", frmRoom );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvRoomManage.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvRoomManage.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

}
