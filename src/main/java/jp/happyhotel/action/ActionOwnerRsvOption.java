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
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvOption;
import jp.happyhotel.owner.FormOwnerRsvOptionManage;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvOption;
import jp.happyhotel.owner.LogicOwnerRsvOptionManage;

public class ActionOwnerRsvOption extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg_ADD       = 91;
    private static final int  menuFlg_ADD_COMM  = 92;
    private static final int  menuFlg_UPD       = 9;
    private static final int  OPTIN_COMM        = 0;
    private static final int  OPTIN_MUST        = 1;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvOptionManage frmManage;
        FormOwnerRsvOption frmOpt;
        FormOwnerRsvOption newFrmOpt;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvOption logic;
        int hotelId = 0;
        int menuFlg = 0;
        boolean frmSetFlg = false;
        int edit_id = 0;
        String memo = "";
        String msg = "";
        String errMsg = "";
        int disptype = 0;
        int imediaflag = 0;
        int paramHotelID = 0;
        int paramUserId = 0;
        String hotenaviId = "";
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
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

            if ( (request.getParameter( "updBtn" ) != null || request.getParameter( "commUpdBtn" ) != null ||
                    request.getParameter( "btnDel" ) != null || request.getParameter( "btnDel_usually" ) != null) &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            frmManage = new FormOwnerRsvOptionManage();
            frmMenu = new FormOwnerBkoMenu();
            frmOpt = new FormOwnerRsvOption();
            newFrmOpt = new FormOwnerRsvOption();
            logic = new LogicOwnerRsvOption();

            if ( request.getParameter( "addRow" ) != null )
            {
                // ▼1行追加ボタン
                menuFlg = menuFlg_ADD;

                // 画面の値を取得
                newFrmOpt = getViewData( frmOpt, request );

                // 対象のオプション情報を取得
                logic.setFrm( newFrmOpt );
                logic.getOpt( 1 );

                // 画面の値を取得
                newFrmOpt = getViewData( logic.getFrm(), request );
                newFrmOpt.setOptionNmMustView( logic.getFrm().getOptionNmMustView() );

                addRow( newFrmOpt );
                request.setAttribute( "FORM_Option", newFrmOpt );
            }
            else if ( request.getParameter( "updBtn" ) != null )
            {
                // ▼必須オプション設定更新ボタン
                menuFlg = menuFlg_UPD;

                // 画面の値を取得
                newFrmOpt = getViewData( frmOpt, request );

                // 入力チェック
                if ( inputCheck( newFrmOpt, 1 ) == true )
                {
                    // 登録処理
                    registOption( newFrmOpt );
                    frmSetFlg = true;

                    // ホテル修正履歴
                    if ( request.getParameter( "optionNmView" ).length() == 0 )
                    {
                        edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_ADD;
                        memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_ADD;
                    }
                    else
                    {
                        edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_UPD;
                        memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_UPD;
                    }
                    OwnerRsvCommon.addAdjustmentHistory( frmOpt.getSelHotelId(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                            frmOpt.getUserId(), edit_id, frmOpt.getSelOptId(), memo );
                }

                if ( frmSetFlg == true )
                {
                    // 管理画面を表示
                    frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                    request.setAttribute( "FORM_OptionManage", frmManage );
                }
                else
                {
                    // 入力エラーの場合
                    menuFlg = menuFlg_ADD;

                    // 対象のオプション情報を取得
                    logic.setFrm( frmOpt );
                    logic.getOpt( 1 );

                    // 入力値を保持
                    newFrmOpt = getViewData( frmOpt, request );
                    request.setAttribute( "FORM_Option", newFrmOpt );
                }

            }
            else if ( request.getParameter( "commUpdBtn" ) != null )
            {
                // ▼通常オプション設定更新ボタン
                menuFlg = menuFlg_UPD;

                // 画面の値を取得
                newFrmOpt = getCommViewData( frmOpt, request );

                // 入力チェック
                if ( inputCheck( newFrmOpt, OPTIN_COMM ) == true )
                {
                    // 登録処理
                    registCommOption( newFrmOpt );
                    frmSetFlg = true;

                    // ホテル修正履歴
                    if ( request.getParameter( "optNmCommView" ).length() == 0 )
                    {
                        edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_ADD;
                        memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_ADD;
                    }
                    else
                    {
                        edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_UPD;
                        memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_UPD;
                    }
                    OwnerRsvCommon.addAdjustmentHistory( frmOpt.getSelHotelId(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                            frmOpt.getUserId(), edit_id, frmOpt.getSelOptId(), memo );

                }

                if ( frmSetFlg == true )
                {
                    // 管理画面を表示
                    frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                    request.setAttribute( "FORM_OptionManage", frmManage );
                }
                else
                {
                    // 入力エラーの場合
                    menuFlg = menuFlg_ADD_COMM;

                    // 対象のオプション情報を取得
                    logic.setFrm( newFrmOpt );
                    logic.getOpt( 0 );

                    // 入力値を保持
                    newFrmOpt = getCommViewData( frmOpt, request );
                    request.setAttribute( "FORM_Option", newFrmOpt );
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // ▼戻る
                menuFlg = menuFlg_UPD;
                newFrmOpt = getCommViewData( frmOpt, request );
                frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                request.setAttribute( "FORM_OptionManage", frmManage );
            }
            else if ( request.getParameter( "btnDel" ) != null )
            {
                // ▼必須オプション削除ボタン
                menuFlg = menuFlg_UPD;

                // 画面の値を取得
                newFrmOpt = getCommViewData( frmOpt, request );

                // オプションがプラン、プラン下書き、予約に登録されているか
                msg = checkDelOption( frmOpt.getSelHotelId(), frmOpt.getSelOptId() );
                if ( msg.trim().length() > 0 )
                {
                    // 予約データが存在する場合は削除不可
                    menuFlg = menuFlg_ADD;

                    // 対象のオプション情報を取得
                    logic.setFrm( newFrmOpt );
                    logic.getOpt( 1 );

                    // 入力値を保
                    newFrmOpt = getCommViewData( logic.getFrm(), request );
                    newFrmOpt.setErrMsg( msg );
                    request.setAttribute( "FORM_Option", newFrmOpt );
                }
                else
                {
                    // 削除処理
                    logic.deleteOption( frmOpt.getSelHotelId(), frmOpt.getSelOptId() );

                    // ホテル修正履歴
                    edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_DEL;
                    memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_DEL;
                    OwnerRsvCommon.addAdjustmentHistory( frmOpt.getSelHotelId(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                            frmOpt.getUserId(), edit_id, frmOpt.getSelOptId(), memo );

                    // 管理画面を表示
                    frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                    request.setAttribute( "FORM_OptionManage", frmManage );
                }
            }
            else if ( request.getParameter( "btnDel_usually" ) != null )
            {
                // ▼削除ボタン
                menuFlg = menuFlg_UPD;

                // 画面の値を取得
                newFrmOpt = getCommViewData( frmOpt, request );

                // オプションがプラン、プラン下書き、予約に登録されているか
                msg = checkDelOption( frmOpt.getSelHotelId(), frmOpt.getSelOptId() );
                if ( msg.trim().length() > 0 )
                {
                    // 予約データが存在する場合は削除不可
                    menuFlg = menuFlg_ADD_COMM;

                    // 対象のオプション情報を取得
                    logic.setFrm( newFrmOpt );
                    logic.getOpt( 0 );

                    // 入力値を保
                    newFrmOpt = getCommViewData( logic.getFrm(), request );
                    newFrmOpt.setErrMsg( Message.getMessage( "warn.30022" ) );
                    request.setAttribute( "FORM_Option", newFrmOpt );
                }
                else
                {
                    // 削除処理
                    logic.deleteOption( frmOpt.getSelHotelId(), frmOpt.getSelOptId() );

                    // ホテル修正履歴
                    edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_DEL;
                    memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_DEL;
                    OwnerRsvCommon.addAdjustmentHistory( frmOpt.getSelHotelId(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                            frmOpt.getUserId(), edit_id, frmOpt.getSelOptId(), memo );

                    // 管理画面を表示
                    frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                    request.setAttribute( "FORM_OptionManage", frmManage );
                }
            }

            // 画面遷移
            OwnerRsvCommon.setMenu( frmMenu, newFrmOpt.getSelHotelId(), menuFlg, request.getCookies() );
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
     * 必須オプション画面の内容をフォームにセット
     * 
     * @param frm FormOwnerRsvOptionオブジェクト
     * @param request HttpServletRequestオブジェクト
     * @return FormOwnerRsvOptionオブジェクト
     */
    private FormOwnerRsvOption getViewData(FormOwnerRsvOption frm, HttpServletRequest request)
    {
        int paramHotelId = 0;
        int paramUserId = 0;
        int paramOptId = 0;
        int paramMaxRow = 0;
        int paramMaxSubOptId = 0;
        String paraOwnerHotelID = "";
        String paramDispIdx = "";
        String paramOptionNm = "";
        String paramOptionNmSearch = "";
        String[] dels;
        ArrayList<Integer> delOptSubIdList = new ArrayList<Integer>();
        ArrayList<Integer> subIdList = new ArrayList<Integer>();
        ArrayList<String> subNmList = new ArrayList<String>();

        if ( request.getParameter( "selHotelIDValue" ) != null )
        {
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
        }
        paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
        paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
        paramOptId = Integer.parseInt( request.getParameter( "optId" ) );
        paramMaxRow = Integer.parseInt( request.getParameter( "maxRow" ) );
        paramMaxSubOptId = Integer.parseInt( request.getParameter( "maxSubOptId" ) );
        paramOptionNm = request.getParameter( "optionNm" );
        paramOptionNmSearch = request.getParameter( "optionNmSearch" );
        paramDispIdx = request.getParameter( "dispIdx" );

        // サブオプション情報取得
        dels = request.getParameterValues( "del" );
        if ( dels != null )
        {
            for( int i = 0 ; i < dels.length ; i++ )
            {
                delOptSubIdList.add( Integer.parseInt( dels[i] ) );
            }
        }
        for( int i = 0 ; i < paramMaxRow ; i++ )
        {
            subIdList.add( Integer.parseInt( request.getParameter( "subOptId" + i ) ) );
            subNmList.add( request.getParameter( "subOptNm" + i ) );
        }

        frm.setSelHotelId( paramHotelId );
        frm.setOwnerHotelID( paraOwnerHotelID );
        frm.setUserId( paramUserId );
        frm.setSelOptId( paramOptId );
        frm.setMaxRow( paramMaxRow );
        frm.setMaxSubOptId( paramMaxSubOptId );
        frm.setOptionNm_Must( paramOptionNm );
        frm.setOptionNm_MustSearch( paramOptionNmSearch );
        frm.setDispIdx( paramDispIdx );
        frm.setOptSubIdMustList( subIdList );
        frm.setOptSubNmMustList( subNmList );
        frm.setDelOptSubIdList( delOptSubIdList );

        return(frm);
    }

    /**
     * 通常オプション画面の内容をフォームにセット
     * 
     * @param frm FormOwnerRsvOptionオブジェクト
     * @param request HttpServletRequestオブジェクト
     * @return FormOwnerRsvOptionオブジェクト
     */
    private FormOwnerRsvOption getCommViewData(FormOwnerRsvOption frm, HttpServletRequest request)
    {
        int paramHotelId = 0;
        int paramUserId = 0;
        int paramOptId = 0;
        String paraOwnerHotelID = "";
        String paramDispIdx = "";
        String paramOptionNm = "";
        String paramOptionNmSearch = "";
        String paramOptCharge = "";
        String paramOptChargeView = "";
        String paramMaxQuantity = "";
        String paramMaxQuantityView = "";
        String paramInpMaxQuantity = "";
        String paramInpMaxQuantityView = "";
        String paramLimitDate = "";
        String paramLimitDateView = "";
        String paramLimitHH = "";
        String paramLimitHHView = "";
        String paramLimitMM = "";
        String paramLimitMMView = "";

        if ( request.getParameter( "selHotelIDValue" ) != null )
        {
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
        }
        paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
        paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
        paramOptId = Integer.parseInt( request.getParameter( "optId" ) );
        paramOptionNm = request.getParameter( "optNm" );
        paramOptionNmSearch = request.getParameter( "optNmSearch" );

        paramDispIdx = request.getParameter( "comDispIdx" );
        paramOptCharge = request.getParameter( "optCharge" );
        paramOptChargeView = request.getParameter( "optChargeView" );
        paramMaxQuantity = request.getParameter( "maxQuantity" );
        paramMaxQuantityView = request.getParameter( "maxQuantityView" );
        paramInpMaxQuantity = request.getParameter( "inpMaxQuantity" );
        paramInpMaxQuantityView = request.getParameter( "inpMaxQuantityView" );
        paramLimitDate = request.getParameter( "cancelLimitDate" );
        paramLimitDateView = request.getParameter( "cancelLimitDateView" );
        paramLimitHH = request.getParameter( "cancelLimitTimeHH" );
        paramLimitHHView = request.getParameter( "cancelLimitTimeHHView" );
        paramLimitMM = request.getParameter( "cancelLimitTimeMM" );
        paramLimitMMView = request.getParameter( "cancelLimitTimeMMView" );

        frm.setSelHotelId( paramHotelId );
        frm.setOwnerHotelID( paraOwnerHotelID );
        frm.setUserId( paramUserId );
        frm.setSelOptId( paramOptId );
        frm.setOptionNm_Comm( paramOptionNm );
        frm.setOptionNm_CommSearch( paramOptionNmSearch );
        frm.setDispIndexComm( paramDispIdx );
        frm.setOptCharge( paramOptCharge );
        frm.setOptChargeView( paramOptChargeView );
        frm.setMaxQuantity( paramMaxQuantity );
        frm.setMaxQuantityView( paramMaxQuantityView );
        frm.setInpMaxQuantity( paramInpMaxQuantity );
        frm.setInpMaxQuantityView( paramInpMaxQuantityView );
        frm.setCancelLimitDate( paramLimitDate );
        frm.setCancelLimitDateView( paramLimitDateView );
        frm.setCancelLimitTimeHH( paramLimitHH );
        frm.setCancelLimitTimeHHView( paramLimitHHView );
        frm.setCancelLimitTimeMM( paramLimitMM );
        frm.setCancelLimitTimeMMView( paramLimitMMView );

        return(frm);
    }

    /**
     * 1行追加処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @return なし
     */
    private void addRow(FormOwnerRsvOption frm) throws Exception
    {
        int newMaxSubOptId = 0;
        ArrayList<Integer> subIdList;
        ArrayList<String> subNmList;

        // フォームの内容を取得
        subIdList = frm.getOptSubIdMustList();
        subNmList = frm.getOptSubNmMustList();

        // 新しいサブオプションIDを取得
        newMaxSubOptId = frm.getMaxSubOptId() + 1;

        subIdList.add( newMaxSubOptId );
        subNmList.add( "" );

        frm.setSelHotelId( frm.getSelHotelId() );
        frm.setMaxRow( frm.getMaxRow() + 1 );
        frm.setMaxSubOptId( newMaxSubOptId );
        frm.setOptSubIdMustList( subIdList );
        frm.setOptSubNmMustList( subNmList );
    }

    /**
     * 必須オプションデータ登録処理
     * 
     * @param frm FormOwnerRsvOptionオブジェクト
     * @return なし
     */
    private void registOption(FormOwnerRsvOption frm) throws Exception
    {
        LogicOwnerRsvOption logic = new LogicOwnerRsvOption();

        // 登録処理
        logic.setFrm( frm );
        logic.registOption();
    }

    /**
     * 通常オプションデータ登録処理
     * 
     * @param frm FormOwnerRsvOptionオブジェクト
     * @return なし
     */
    private void registCommOption(FormOwnerRsvOption frm) throws Exception
    {
        LogicOwnerRsvOption logic = new LogicOwnerRsvOption();

        // 登録処理
        logic.setFrm( frm );
        logic.registCommOption();
    }

    /**
     * 画面の内容をフォームにセット
     * 
     * @param frm FormOwnerRsvOptionオブジェクト
     * @param checkKbn チェック対象(0:通常オプション、1：必須オプション)
     * @return true:チェックOK、False;チェックNG
     */
    private boolean inputCheck(FormOwnerRsvOption frm, int checkKbn) throws Exception
    {
        boolean ret = false;
        String msg = "";
        int lenCheck = 0;
        boolean checkFlg = false;
        boolean maxNumCheck = true;

        if ( checkKbn == OPTIN_MUST )
        {
            // ▼必須オプション
            // 表示順
            if ( CheckString.onlySpaceCheck( frm.getDispIdx() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "表示順" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frm.getDispIdx() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "表示順", "0以上の数値" ) + "<br />";
                }
            }

            // オプション名
            if ( CheckString.onlySpaceCheck( frm.getOptionNm_Must() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "オプション名" ) + "<br />";
            }
            else
            {
                lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptionNm_Must().trim(), 50 );
                if ( lenCheck == 1 )
                {
                    // 桁数Overの場合
                    msg = msg + Message.getMessage( "warn.00038", "オプション名", "25" ) + "<br />";
                }
            }
            // 検索表示オプション名
            if ( CheckString.onlySpaceCheck( frm.getOptionNm_MustSearch() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "検索表示オプション名" ) + "<br />";
            }
            else
            {
                lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptionNm_MustSearch().trim(), 50 );
                if ( lenCheck == 1 )
                {
                    // 桁数Overの場合
                    msg = msg + Message.getMessage( "warn.00038", "検索表示オプション名", "25" ) + "<br />";
                }
            }

            // サブオプション情報
            for( int i = 0 ; i < frm.getOptSubIdMustList().size() ; i++ )
            {
                checkFlg = false;
                for( int j = 0 ; j < frm.getDelOptSubIdList().size() ; j++ )
                {
                    if ( frm.getOptSubIdMustList().get( i ) != frm.getDelOptSubIdList().get( j ) )
                    {
                        // 削除対象ではない場合
                        continue;
                    }

                    // 削除対象の場合
                    checkFlg = true;
                }

                if ( checkFlg == false )
                {
                    if ( CheckString.onlySpaceCheck( frm.getOptSubNmMustList().get( i ) ) == true )
                    {
                        // 未入力・空白文字の場合
                        msg = msg + Message.getMessage( "warn.00001", "サブオプション名" ) + "<br />";
                    }
                    else
                    {
                        lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptSubNmMustList().get( i ), 50 );
                        if ( lenCheck == 1 )
                        {
                            // 桁数Overの場合
                            msg = msg + Message.getMessage( "warn.00038", "サブオプション名", "25" ) + "<br />";
                        }
                    }
                }
            }

            frm.setErrMsg( msg );
            if ( msg.trim().length() == 0 )
            {
                ret = true;
            }
            return(ret);
        }

        // ▼通常オプション
        // 表示順
        if ( CheckString.onlySpaceCheck( frm.getDispIndexComm() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "表示順" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getDispIndexComm() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "表示順", "0以上の数値" ) + "<br />";
            }
        }

        // オプション名
        if ( CheckString.onlySpaceCheck( frm.getOptionNm_Comm() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "オプション名" ) + "<br />";
        }
        else
        {
            lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptionNm_Comm().trim(), 50 );
            if ( lenCheck == 1 )
            {
                // 桁数Overの場合
                msg = msg + Message.getMessage( "warn.00038", "オプション名", "25" ) + "<br />";
            }
        }

        // 検索表示オプション名
        if ( CheckString.onlySpaceCheck( frm.getOptionNm_CommSearch() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "検索表示オプション名" ) + "<br />";
        }
        else
        {
            lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptionNm_CommSearch().trim(), 50 );
            if ( lenCheck == 1 )
            {
                // 桁数Overの場合
                msg = msg + Message.getMessage( "warn.00038", "検索表示オプション名", "25" ) + "<br />";
            }
        }

        // オプション料金
        if ( CheckString.onlySpaceCheck( frm.getOptCharge() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "オプション料金" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getOptCharge() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "オプション料金", "0以上の数値" ) + "<br />";
            }
        }

        // 1日あたり上限数
        if ( CheckString.onlySpaceCheck( frm.getMaxQuantity() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "1日当たり上限数" ) + "<br />";
            maxNumCheck = false;
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMaxQuantity() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "1日当たり上限数", "1以上の数値" ) + "<br />";
                maxNumCheck = false;
            }
            else if ( Integer.parseInt( frm.getMaxQuantity() ) < 1 )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "1日当たり上限数", "1以上の数値" ) + "<br />";
                maxNumCheck = false;
            }
        }

        // 1回当たり上限数
        if ( CheckString.onlySpaceCheck( frm.getInpMaxQuantity() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "1回当たり上限数" ) + "<br />";
            maxNumCheck = false;
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getInpMaxQuantity() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "1回当たり上限数", "1以上の数値" ) + "<br />";
                maxNumCheck = false;
            }
            else if ( Integer.parseInt( frm.getInpMaxQuantity() ) < 1 )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "1回当たり上限数", "1以上の数値" ) + "<br />";
                maxNumCheck = false;
            }
        }

        // 1回あたりの上限数が1日あたりの上限数より多い場合はエラー
        if ( (maxNumCheck == true) && (Integer.parseInt( frm.getMaxQuantity() ) < Integer.parseInt( frm.getInpMaxQuantity() )) )
        {
            msg = msg + Message.getMessage( "warn.30018" ) + "<br />";
        }

        // 手仕舞い日(日にち)
        if ( CheckString.onlySpaceCheck( frm.getCancelLimitDate() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "手仕舞い日の日にち" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getCancelLimitDate() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "手仕舞い日の日にち", "0以上の数値" ) + "<br />";
            }
        }

        // 手仕舞い日(時間)
        if ( (CheckString.onlySpaceCheck( frm.getCancelLimitTimeHH() ) == true) && (CheckString.onlySpaceCheck( frm.getCancelLimitTimeMM() ) == true) )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "手仕舞い日の時間" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getCancelLimitTimeHH() ) == false) || (OwnerRsvCommon.numCheck( frm.getCancelLimitTimeMM() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "手仕舞い日の時間", "整数" ) + "<br />";
            }
            else
            {
                if ( (Integer.parseInt( frm.getCancelLimitTimeHH() ) > 23 || Integer.parseInt( frm.getCancelLimitTimeHH() ) < 0)
                        || (Integer.parseInt( frm.getCancelLimitTimeHH() ) > 23 || Integer.parseInt( frm.getCancelLimitTimeHH() ) < 0) )
                {
                    // 時間が0時～23時以外の場合
                    msg = msg + Message.getMessage( "warn.30007", "手仕舞い日の時間", "0～23" ) + "<br />";
                }
                if ( (Integer.parseInt( frm.getCancelLimitTimeMM() ) > 59 || Integer.parseInt( frm.getCancelLimitTimeMM() ) < 0)
                        || (Integer.parseInt( frm.getCancelLimitTimeMM() ) > 59 || Integer.parseInt( frm.getCancelLimitTimeMM() ) < 0) )
                {
                    // 分が0時～59以外の場合
                    msg = msg + Message.getMessage( "warn.30007", "手仕舞い日の分", "0～59" ) + "<br />";
                }
            }
        }

        frm.setErrMsg( msg );
        if ( msg.trim().length() == 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * オプション管理画面表示
     * 
     * @param hoteiID ホテルID
     * @return FormOwnerRsvOptionManageオブジェクト
     */
    private FormOwnerRsvOptionManage setOptionManage(int hotelID) throws Exception
    {
        LogicOwnerRsvOptionManage logic = new LogicOwnerRsvOptionManage();
        FormOwnerRsvOptionManage frm = new FormOwnerRsvOptionManage();

        frm.setSelHotelId( hotelID );
        logic.setFrm( frm );
        logic.getOption();

        return(logic.getFrm());
    }

    /**
     * オプション登録チェック
     * 
     * @param hoteiID ホテルID
     * @return String エラーメッセージ
     */
    private String checkDelOption(int hotelId, int optId) throws Exception
    {

        String retMsg = "";
        LogicOwnerRsvOption logic = new LogicOwnerRsvOption();

        // 予約データ存在チェック
        if ( logic.existsReserve( hotelId, optId ) == true )
        {
            retMsg = Message.getMessage( "warn.30022" );
            return(retMsg);
        }

        // プラン存在チェック
        if ( logic.existsPlanOption( hotelId, optId, 1 ) == true )
        {
            retMsg = Message.getMessage( "warn.30023" );
            return(retMsg);
        }

        // 下書き存在チェック
        if ( logic.existsPlanOption( hotelId, optId, 2 ) == true )
        {
            retMsg = Message.getMessage( "warn.30024" );
            return(retMsg);
        }

        return(retMsg);
    }
}
