package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvOption;
import jp.happyhotel.owner.FormOwnerRsvOptionManage;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvOption;
import jp.happyhotel.owner.LogicOwnerRsvOptionManage;

/**
 * 
 * オプション管理画面
 */
public class ActionOwnerRsvOptionManage extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg_MANAGE    = 9;
    private static final int  menuFlg_MUST      = 91;
    private static final int  menuFlg_COMM      = 92;
    private static final int  OPTIN_COMM        = 0;
    private static final int  OPTIN_MUST        = 1;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvOptionManage frmManage;
        FormOwnerRsvOption frmOpt;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvOption logicOpt;
        LogicOwnerRsvOptionManage logicOptMng;
        int paramHotelID = 0;
        int paramUserId = 0;
        int hotelId = 0;
        int menuFlg = 0;
        String errMsg = "";
        String hotenaviId = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            frmManage = new FormOwnerRsvOptionManage();
            frmMenu = new FormOwnerBkoMenu();
            frmOpt = new FormOwnerRsvOption();
            logicOpt = new LogicOwnerRsvOption();
            logicOptMng = new LogicOwnerRsvOptionManage();

            // 画面の値を取得
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            frmOpt.setSelHotelId( paramHotelID );
            frmOpt.setUserId( paramUserId );

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

            if ( (request.getParameter( "dispChangeMust" ) != null || request.getParameter( "dispChangeComm" ) != null) &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( request.getParameter( "mustOptAddBtn" ) != null )
            {
                // ▼必須オプション追加
                menuFlg = menuFlg_MUST;
                setNewPage( frmOpt, logicOpt );
                request.setAttribute( "FORM_Option", frmOpt );

            }
            else if ( request.getParameter( "dispChangeMust" ) != null )
            {
                // ▼必須オプション表示順変更
                menuFlg = menuFlg_MANAGE;
                frmManage.setSelHotelId( paramHotelID );
                FormOwnerRsvOptionManage newFrmManage = new FormOwnerRsvOptionManage();
                newFrmManage = updateDispIndex( request, frmManage, logicOptMng, OPTIN_MUST );
                request.setAttribute( "FORM_OptionManage", newFrmManage );

            }
            else if ( request.getParameter( "commOptAddBtn" ) != null )
            {
                // ▼通常オプション追加
                menuFlg = menuFlg_COMM;
                setNewCommPage( frmOpt, logicOpt );
                request.setAttribute( "FORM_Option", frmOpt );

            }
            else if ( request.getParameter( "dispChangeComm" ) != null )
            {
                // ▼通常オプション表示順変更
                menuFlg = menuFlg_MANAGE;
                frmManage.setSelHotelId( paramHotelID );
                FormOwnerRsvOptionManage newFrmManage = new FormOwnerRsvOptionManage();
                newFrmManage = updateDispIndex( request, frmManage, logicOptMng, OPTIN_COMM );
                request.setAttribute( "FORM_OptionManage", newFrmManage );

            }
            else if ( (request.getParameter( "edit" ) != null) && (Integer.parseInt( request.getParameter( "edit" ) ) == 1) )
            {
                // ▼必須オプション編集リンク
                menuFlg = menuFlg_MUST;
                frmOpt.setSelOptId( Integer.parseInt( request.getParameter( "optId" ) ) );
                setEditMustPage( frmOpt, logicOpt, OPTIN_MUST );
                request.setAttribute( "FORM_Option", frmOpt );

            }
            else if ( (request.getParameter( "edit" ) != null) && (Integer.parseInt( request.getParameter( "edit" ) ) == 0) )
            {
                // ▼通常オプション編集リンク
                menuFlg = menuFlg_COMM;
                frmOpt.setSelOptId( Integer.parseInt( request.getParameter( "optId" ) ) );
                setEditMustPage( frmOpt, logicOpt, OPTIN_COMM );
                request.setAttribute( "FORM_Option", frmOpt );
            }

            // 画面遷移
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
            request.setAttribute( "FORM_Menu", frmMenu );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvOptionManage.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvOptionManage.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }

    /**
     * 必須オプション新規追加ページへ遷移するための画面設定処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param logic LogicOwnerRsvOptionオブジェクト
     * @return なし
     */
    private void setNewPage(FormOwnerRsvOption frm, LogicOwnerRsvOption logic) throws Exception
    {
        int newOptId = 0;
        int newSubOptId = 0;
        ArrayList<Integer> subIdList = new ArrayList<Integer>();
        ArrayList<String> subNmList = new ArrayList<String>();
        ArrayList<Integer> delOptSubIdList = new ArrayList<Integer>();

        // 新しいプランIDを取得
        logic.setFrm( frm );
        newOptId = logic.getNewOptId();
        frm.setSelOptId( newOptId );

        // 新しいサブプランIDを取得
        newSubOptId = logic.getNewOptSubId();
        delOptSubIdList.add( 0 );
        subIdList.add( newSubOptId );
        subNmList.add( "" );

        frm.setMaxRow( 1 );
        frm.setMaxSubOptId( 1 );
        frm.setDelOptSubIdList( delOptSubIdList );
        frm.setOptSubIdMustList( subIdList );
        frm.setOptSubNmMustList( subNmList );
        frm.setDispIdx( "0" );
    }

    /**
     * 必須オプション編集ページへ遷移するための画面設定処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param logic LogicOwnerRsvOptionオブジェクト
     * @param optFlg オプションフラグ
     * @return なし
     */
    private void setEditMustPage(FormOwnerRsvOption frm, LogicOwnerRsvOption logic, int optFlg) throws Exception
    {
        // 対象のオプション情報を取得
        logic.setFrm( frm );
        logic.getOpt( optFlg );
    }

    /**
     * 通常オプション新規追加ページへ遷移するための画面設定処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param logic LogicOwnerRsvOptionオブジェクト
     * @return なし
     */
    private void setNewCommPage(FormOwnerRsvOption frm, LogicOwnerRsvOption logic) throws Exception
    {
        int newOptId = 0;

        // 新しいプランIDを取得
        logic.setFrm( frm );
        newOptId = logic.getNewOptId();
        frm.setSelOptId( newOptId );
        frm.setDispIndexComm( "0" );
    }

    /**
     * 表示順変更
     * 
     * @param request HttpServletRequest
     * @param frm FormOwnerRsvOptionManageオブジェクト
     * @param logic LogicOwnerRsvOptionManageオブジェクト
     * @param optFlg オプションフラグ
     * @return なし
     */
    private FormOwnerRsvOptionManage updateDispIndex(HttpServletRequest request, FormOwnerRsvOptionManage frm, LogicOwnerRsvOptionManage logic, int optFlg) throws Exception
    {

        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<String> optNmList = new ArrayList<String>();
        ArrayList<String> dispList = new ArrayList<String>();
        String[] disps;
        String[] optIds;
        String[] optNms;
        String msg = "";
        FormOwnerRsvOptionManage newFrm = new FormOwnerRsvOptionManage();

        // 画面のオプションID、表示順取得
        switch( optFlg )
        {
            case OPTIN_MUST:
                optIds = request.getParameterValues( "mustOptId" );
                optNms = request.getParameterValues( "mustOptNm" );
                disps = request.getParameterValues( "dispIdx" );
                if ( optIds != null )
                {
                    for( int i = 0 ; i < optIds.length ; i++ )
                    {
                        dispList.add( disps[i] );
                        optIdList.add( Integer.parseInt( optIds[i] ) );
                        optNmList.add( optNms[i] );
                    }
                }
                frm.setDispIndexList( dispList );
                frm.setOptionIdList( optIdList );
                frm.setComOptionNmList( optNmList );
                break;

            case OPTIN_COMM:
                optIds = request.getParameterValues( "commOptId" );
                optNms = request.getParameterValues( "commOptNm" );
                disps = request.getParameterValues( "commDispIdx" );
                if ( optIds != null )
                {
                    for( int i = 0 ; i < optIds.length ; i++ )
                    {
                        dispList.add( disps[i] );
                        optIdList.add( Integer.parseInt( optIds[i] ) );
                        optNmList.add( optNms[i] );
                    }
                }
                frm.setComDispIndexList( dispList );
                frm.setComOptionIdList( optIdList );
                frm.setComOptionNmList( optNmList );
                break;
        }

        // 入力値チェック
        logic.setFrm( frm );
        msg = checkInputValue( dispList, optNmList );
        if ( msg.trim().length() == 0 )
        {
            // 更新処理
            msg = "";
            for( int i = 0 ; i < dispList.size() ; i++ )
            {
                if ( logic.updDispIndex( Integer.parseInt( dispList.get( i ) ), optIdList.get( i ) ) == false )
                {
                    // 更新失敗
                    msg = msg + Message.getMessage( "erro.30001", "更新対象" );
                }
            }
        }

        // 再表示
        frm.setErrMsg( msg );
        logic.setFrm( frm );
        logic.getOption();
        newFrm = logic.getFrm();
        return(newFrm);
    }

    /**
     * 表示順変更時の入力値チェック
     * 
     * @param dispIdxList 画面で入力された表示順
     * @param NmList オプション名
     * @return エラーメッセージ
     */
    private String checkInputValue(ArrayList<String> dispIdxList, ArrayList<String> nmList) throws Exception
    {
        String msg = "";

        for( int i = 0 ; i < dispIdxList.size() ; i++ )
        {
            if ( CheckString.onlySpaceCheck( dispIdxList.get( i ) ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + ReplaceString.HTMLEscape( nmList.get( i ) ) + "】の" + "表示順" ) + "<br />";
            }
            else
            {

                if ( (OwnerRsvCommon.numCheck( dispIdxList.get( i ) ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + ReplaceString.HTMLEscape( nmList.get( i ) ) + "】の" + "表示順", "0以上の整数" ) + "<br />";
                }
            }
        }

        return(msg);
    }
}
