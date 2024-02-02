package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerRsvChargeMode;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvChargeMode;
import jp.happyhotel.owner.LogicOwnerBkoMenu;

/**
 * 
 * 施設基本情報画面
 */
public class ActionOwnerRsvChargeMode extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg           = 4;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvChargeMode frmChargeMode;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvChargeMode logic;
        int paramHotelID = 0;
        String paramOwnerHotelID = "";
        int paramUserId = 0;
        int hotelId = 0;
        ArrayList<Integer> paramModeIdList = new ArrayList<Integer>();
        ArrayList<String> paramNmList = new ArrayList<String>();
        ArrayList<String> paramRemarksList = new ArrayList<String>();
        ArrayList<Integer> paramEditFlgList = new ArrayList<Integer>();
        ArrayList<Integer> paramDelList = new ArrayList<Integer>();
        ArrayList<Integer> paramDispList = new ArrayList<Integer>();
        boolean frmSetFlg = false;
        String errMsg = "";
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            logic = new LogicOwnerRsvChargeMode();

            // ▼画面の値を取得
            // ホテルID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            // 料金モードID、備考取得
            for( int i = 1 ; i <= 20 ; i++ )
            {
                paramModeIdList.add( i );
                paramRemarksList.add( "" );
            }
            // 料金モード名、編集フラグ、削除チェック値取得
            for( int i = 0 ; i < 20 ; i++ )
            {
                paramNmList.add( request.getParameter( "modeNm" + Integer.toString( i ) ).toString() );
                paramEditFlgList.add( Integer.parseInt( request.getParameter( "editFlg" + Integer.toString( i ) ).toString() ) );
                if ( request.getParameter( "del" + Integer.toString( i ) ) == null )
                {
                    paramDelList.add( 0 );
                }
                else
                {
                    paramDelList.add( Integer.parseInt( request.getParameter( "del" + Integer.toString( i ) ).toString() ) );
                }
                paramDispList.add( Integer.parseInt( request.getParameter( "dispIndex" + Integer.toString( i ) ) ) );
            }
            paramOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );

            frmMenu = new FormOwnerBkoMenu();
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
            logicMenu = new LogicOwnerBkoMenu();
            logicMenu.setFrm( new FormOwnerBkoMenu() );
            adminflag = logicMenu.getAdminFlg( paramOwnerHotelID, paramUserId );

            if ( (frmMenu.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA && paramOwnerHotelID.equals( "happyhotel" ) && adminflag == 1) == false && frmMenu.getUserAuth() != OwnerRsvCommon.USER_AUTH_OWNER
                    && frmMenu.getUserAuth() != OwnerRsvCommon.USER_AUTH_DEMO
                    && frmMenu.getUserAuth() != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ページを閲覧する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            frmChargeMode = new FormOwnerRsvChargeMode();

            // 値をフォームにセット
            frmChargeMode.setSelHotelID( paramHotelID );
            frmChargeMode.setChargeModeIdList( paramModeIdList );
            frmChargeMode.setChargeModeNmList( paramNmList );
            frmChargeMode.setRemarks( paramRemarksList );
            frmChargeMode.setEditFlag( paramEditFlgList );
            frmChargeMode.setCheckDel( paramDelList );
            frmChargeMode.setOwnerHotelID( paramOwnerHotelID );
            frmChargeMode.setUserId( paramUserId );
            frmChargeMode.setDispList( paramDispList );

            // 入力チェック
            if ( isValidate( frmChargeMode ) != false )
            {
                if ( (frmMenu.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA && paramOwnerHotelID.equals( "happyhotel" ) && adminflag == 1) == false && frmMenu.getUserAuth() == OwnerRsvCommon.USER_AUTH_CALLCENTER )
                {
                    // 権限のないユーザはエラーページを表示する
                    errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
                // データの更新
                logic.setFrm( frmChargeMode );
                logic.registChargeMode();
                frmSetFlg = true;

                // ホテル修正履歴
                int edit_id;
                int edit_sub;
                String memo = "";
                // 件数分の繰り返し
                for( int i = 0 ; i < 20 ; i++ )
                {
                    edit_id = 0;
                    edit_sub = i + 1;
                    if ( paramDelList.get( i ) == 0 )
                    { // 追加か変更か無修正
                        String name = paramNmList.get( i );
                        String nameOld = request.getParameter( "modeNm" + Integer.toString( i ) + "_old" ).toString();
                        // 追加か？
                        if ( nameOld.length() == 0 )
                        {
                            if ( name.length() > 0 )
                            {
                                edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_CHARGE_ADD;
                                memo = OwnerRsvCommon.ADJUST_MEMO_CHAGE_ADD;
                            }
                        }
                        else
                        { // 修正かどうか料金名称をチェックする
                            if ( name.compareTo( nameOld ) != 0 )
                            {
                                edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_CHARGE_UPD;
                                memo = OwnerRsvCommon.ADJUST_MEMO_CHAGE_UPD;
                            }
                        }
                    }
                    else
                    { // 削除
                        edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_CHARGE_DEL;
                        memo = OwnerRsvCommon.ADJUST_MEMO_CHAGE_DEL;
                    }
                    if ( edit_id != 0 )
                    {
                        OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                paramUserId, edit_id, edit_sub, memo );
                    }
                }
            }

            // メニュー、施設情報の設定
            setPage( frmChargeMode, paramHotelID );

            // 入力エラーの場合、入力値は保持する
            if ( frmSetFlg == false )
            {
                frmChargeMode.setChargeModeNmList( paramNmList );
                frmChargeMode.setCheckDel( paramDelList );
            }

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_ChargeMode", frmChargeMode );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvChargeMode.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvChargeMode.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * 料金モード画面を設定する
     * 
     * @param frm FormOwnerO10504オブジェクト
     * @param selHotelID 選択されているホテルID
     * @return なし
     */
    private void setPage(FormOwnerRsvChargeMode frm, int selHotelID) throws Exception
    {
        LogicOwnerRsvChargeMode logic = new LogicOwnerRsvChargeMode();

        try
        {
            frm.setSelHotelID( selHotelID );
            logic.setFrm( frm );
            logic.getInit();

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvChargeMode.setPage() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvChargeMode.setPage() ] " + e.getMessage() );
        }
    }

    /**
     * 入力チェック
     * 
     * @param frm FormOwnerO10504オブジェクト
     * @return true:正常、 false:エラーあり
     */
    private boolean isValidate(FormOwnerRsvChargeMode frm) throws Exception
    {
        boolean ret = false;
        int lenRet = 0;
        int modeId = 0;
        String modeNm = "";
        String msg = "";
        LogicOwnerRsvChargeMode logic = new LogicOwnerRsvChargeMode();

        try
        {
            for( int i = 0 ; i < frm.getChargeModeIdList().size() ; i++ )
            {
                modeId = frm.getChargeModeIdList().get( i );
                modeNm = frm.getChargeModeNmList().get( i );

                // ▼桁数チェック
                lenRet = OwnerRsvCommon.LengthCheck( modeNm, 32 );
                if ( lenRet == 1 )
                {
                    // 桁数Over
                    msg = msg + "No." + modeId + "の" + Message.getMessage( "warn.00038", "料金モード名", "16" ) + "<br />";
                    break;
                }

                // ▼プラン別料金マスタ存在チェック
                if ( (frm.getCheckDel().get( i ) != 0) || (modeNm.trim().length() == 0) )
                {
                    if ( logic.isExistsPlanCharge( frm.getSelHotelID(), modeId ) == true )
                    {
                        // 存在する場合
                        msg = msg + "No." + modeId + "は、" + Message.getMessage( "warn.30003" );
                        break;
                    }
                }

                // ▼名称重複チェック
                if ( modeNm.trim().length() == 0 )
                {
                    // 名称未設定は次へ
                    continue;
                }

                for( int j = 0 ; j < frm.getChargeModeIdList().size() ; j++ )
                {
                    if ( modeId == frm.getChargeModeIdList().get( j ) )
                    {
                        // 比較対象と同じ料金モードIDの場合は次へ
                        continue;
                    }
                    if ( modeNm.compareTo( frm.getChargeModeNmList().get( j ) ) == 0 )
                    {
                        // 同じ文字列が存在した場合
                        msg = msg + Message.getMessage( "warn.30002" );
                        break;
                    }
                }
            }

            frm.setErrMsg( msg );

            if ( msg.trim().length() == 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvChargeMode.isValidate() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvChargeMode.isValidate() ] " + e.getMessage() );
        }

        return(ret);
    }
}
