package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerRsvEquip;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvEquip;
import jp.happyhotel.owner.LogicOwnerBkoMenu;

/**
 * 
 * 設備管理画面
 * 設備設定詳細画面へ遷移するActionクラス
 */
public class ActionOwnerRsvEquipManage extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg           = 81;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvEquip frmEquip;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvEquip logic;
        int paramHotelID = 0;
        int paramEqId = 0;
        int paramUserId = 0;
        String errMsg = "";
        String hotenaviId = "";
        ArrayList<Integer> checkList = new ArrayList<Integer>();
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
            paramEqId = Integer.parseInt( request.getParameter( "equipId" ) );
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

            frmEquip = new FormOwnerRsvEquip();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerRsvEquip();

            // 値をフォームにセット
            frmEquip.setSelHotelID( paramHotelID );
            frmEquip.setEquipID( paramEqId );
            frmEquip.setUserID( paramUserId );

            // 設備情報の取得
            logic.setFrm( frmEquip );
            logic.getEquipData();

            // メニュー、予約情報の設定
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // チェックする管理番号を設定
            checkList = setCheckList( frmEquip.getAllEqIdList(), frmEquip.getEqIdList() );
            frmEquip.setCheckList( checkList );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_Equip", frmEquip );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvEquipManage.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvEquipManage.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * 管理番号にチェックをつける制御のためのListを作成する。
     * 
     * @param allRoomList 対象ホテルの全部屋のリスト
     * @param eqRoomList 登録済みの部屋リスト
     * @return チェックON・OFFが定義されているArrayList
     */
    private ArrayList<Integer> setCheckList(ArrayList<Integer> allRoomList, ArrayList<Integer> eqRoomList)
    {
        ArrayList<Integer> checkList = new ArrayList<Integer>();
        int allEqId = 0;
        int checkValue = 0;

        for( int i = 0 ; i < allRoomList.size() ; i++ )
        {
            allEqId = allRoomList.get( i );
            checkValue = 0;

            for( int j = 0 ; j < eqRoomList.size() ; j++ )
            {
                if ( allEqId == eqRoomList.get( j ) )
                {
                    checkValue = 1;
                    break;
                }
            }
            checkList.add( checkValue );
        }
        return(checkList);
    }
}
