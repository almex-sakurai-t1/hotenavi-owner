package jp.happyhotel.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.data.DataRsvPlanCharge;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlan;
import jp.happyhotel.owner.FormOwnerRsvPlanManage;
import jp.happyhotel.owner.FormOwnerRsvPlanOptionSub;
import jp.happyhotel.owner.FormOwnerRsvPlanSub;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlan;
import jp.happyhotel.owner.LogicOwnerRsvPlanManage;

/**
 * 
 * プラン設定画面
 */
public class ActionOwnerRsvPlan extends BaseAction
{
    private RequestDispatcher   requestDispatcher = null;

    // 下書き区分
    private static final int    DRAFT_OFF         = 0;         // 通常
    private static final int    DRAFT_ON          = 1;         // 下書き

    // 登録モード(ActionOwnerRsvPlanManageで設定してある「押されたボタンの判別値」と同値)
    private static final int    MODE_NEW          = 2;         // 新規
    private static final int    MODE_EDIT         = 4;         // 更新

    private static final int    menuFlg_MANAGE    = 3;
    private static final int    menuFlg_REGIST    = 31;
    private static final String BTN_ON            = "一時的に停止する";
    private static final String BTN_OFF           = "販売中にする";

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerRsvPlan frm;
        FormOwnerRsvPlanManage frmManage;
        LogicOwnerRsvPlan logic;
        LogicOwnerRsvPlanManage logicManage;
        int hotelID = 0;
        boolean inputCheck = false;
        int selBtn = 0;
        int paramPlanId = 0;
        boolean checkplancharge = false;
        int roomseq = 0;
        String errMsg = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        int paramUserId = 0;

        Logging.info( "[ActionOwnerRsvPlan.execute() ][request.getParameter( 'mode' ) = " + request.getParameter( "mode" ) + "]" );

        try
        {
            frmMenu = new FormOwnerBkoMenu();
            frm = new FormOwnerRsvPlan();
            logic = new LogicOwnerRsvPlan();

            // 画面の値を取得
            setFormInputParam( request, frm );
            hotelID = frm.getSelHotelID();
            logic.setFrm( frm );

            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
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

            if ( request.getParameter( "btnSales" ) != null )
            {
                // 一時的に停止するボタンクリック
                selBtn = OwnerRsvCommon.BTN_SALES;
            }
            else if ( request.getParameter( "btnRegist" ) != null )
            {
                // プラン設定更新ボタンクリック
                selBtn = OwnerRsvCommon.BTN_REGIST;
            }
            else if ( request.getParameter( "btnCopyRegist" ) != null )
            {
                // プラン設定コピー
                selBtn = OwnerRsvCommon.BTN_COPYREGIST;
            }
            else if ( request.getParameter( "btnCopy" ) != null )
            {
                selBtn = OwnerRsvCommon.BTN_COPY;
            }
            else if ( request.getParameter( "btnDraftSave" ) != null )
            {
                // 下書き保存ボタンクリック
                selBtn = OwnerRsvCommon.BTN_DRAFT;
            }
            else if ( request.getParameter( "changePlan" ) != null )
            {
                // 表示ボタンクリック
                selBtn = OwnerRsvCommon.BTN_VIEW;
            }
            else if ( request.getParameter( "btnDraftDel" ) != null )
            {
                // 下書き削除ボタンクリック
                selBtn = OwnerRsvCommon.BTN_DEL;
            }
            else if ( request.getParameter( "btnDraftRegist" ) != null )
            {
                // 下書き プラン設定更新ボタンクリック
                selBtn = OwnerRsvCommon.BTN_DRAFTUPD;
            }
            else if ( request.getParameter( "btnPreView" ) != null && request.getParameter( "btnPreView" ).equals( "" ) == false )
            {
                // プレビューボタンクリック
                selBtn = OwnerRsvCommon.BTN_PREVIEW;
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // 戻るボタンクリック
                selBtn = OwnerRsvCommon.BTN_BACK;
            }
            else if ( request.getParameter( "previewDetail" ) != null )
            {
                // プレビュー詳細表示
                selBtn = OwnerRsvCommon.BTN_PREVIEW_DETAIL;
                if ( request.getParameter( "roomSeq" ) != null )
                {
                    if ( request.getParameter( "roomSeq" ).equals( "" ) == false && CheckString.numCheck( request.getParameter( "roomSeq" ) ) == true )
                    {
                        roomseq = Integer.parseInt( request.getParameter( "roomSeq" ) );
                    }
                }
            }

            if ( disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false &&
                    (selBtn == OwnerRsvCommon.BTN_PREVIEW || selBtn == OwnerRsvCommon.BTN_PREVIEW_DETAIL) == false )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // 処理実行
            switch( selBtn )
            {
                case OwnerRsvCommon.BTN_SALES:
                    // ▼一時的に停止するボタンクリック
                    if ( frm.getSelPlanID() == 0 )
                    {
                        // プランIDが未設定の場合は、新規登録と同様の扱い
                        frm = execReviewPlan( request, logic, frm, DRAFT_OFF );

                    }
                    else
                    {
                        // 販売フラグ更新
                        frm = execSalesMode( request, logic, frm, DRAFT_OFF );
                        // ホテル修正履歴
                        OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                frm.getUserId(), OwnerRsvCommon.ADJUST_EDIT_ID_RSV_PLAN,
                                frm.getSelPlanID() * 10 + frm.getSalesFlag(),
                                frm.getSalesFlag() == 0 ? OwnerRsvCommon.ADJUST_MEMO_RSV_STOP_PLAN : OwnerRsvCommon.ADJUST_MEMO_RSV_START_PLAN );
                    }
                    request.setAttribute( "FORM_Plan", frm );
                    OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    break;

                case OwnerRsvCommon.BTN_DRAFT:
                    // ▼下書き保存
                    // 入力チェック
                    frm.setDraftMode( DRAFT_ON );
                    if ( isInputCheckMsg( frm ) == true )
                    {
                        // 入力チェック成功時
                        paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );

                        // 登録・更新
                        if ( paramPlanId == 0 )
                        {
                            // 新規登録
                            logic.registPlan( MODE_NEW, DRAFT_ON, OwnerRsvCommon.BTN_REGIST );
                        }
                        else
                        {
                            // 更新
                            logic.registPlan( MODE_EDIT, DRAFT_ON, OwnerRsvCommon.BTN_REGIST );
                        }
                        inputCheck = true;
                    }

                    // メニュー設定
                    frmMenu.setUserId( frm.getUserId() );

                    // 入力エラーの場合、入力値は保持する
                    if ( inputCheck == false )
                    {
                        // 画面再表示
                        frm = execReviewPlan( request, logic, frm, DRAFT_ON );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    }
                    else
                    {
                        // 成功時も画面表示
                        frm = execReviewPlan( request, logic, frm, DRAFT_ON );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                        // frm.setDraftMode( DRAFT_OFF );
                        // // 登録成功時
                        // logicManage = new LogicOwnerRsvPlanManage();
                        // frmManage = new FormOwnerRsvPlanManage();
                        // frmManage.setSelHotelId( hotelID );
                        // logicManage.setFrm( frmManage );
                        // logicManage.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
                        // frmManage.setViewMode( OwnerRsvCommon.PLAN_VIEW_PART );
                        // request.setAttribute( "FORM_PlanManage", frmManage );
                        // OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                    }
                    break;

                case OwnerRsvCommon.BTN_VIEW:
                    // ▼表示
                    frm = execPlanView( request, logic, frm );
                    request.setAttribute( "FORM_Plan", frm );
                    OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    break;

                case OwnerRsvCommon.BTN_DEL:
                    // ▼下書き削除
                    if ( execDelDraft( hotelID, Integer.parseInt( request.getParameter( "selPlanId" ) ), logic, frm ) == false )
                    {
                        // エラーの場合、画面再表示
                        frm = execReviewPlan( request, logic, frm, DRAFT_ON );
                        frm.setDraftMode( DRAFT_ON );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );

                    }
                    else
                    {
                        // 削除成功
                        logicManage = new LogicOwnerRsvPlanManage();
                        frmManage = new FormOwnerRsvPlanManage();
                        frmManage.setSelHotelId( hotelID );
                        logicManage.setFrm( frmManage );
                        logicManage.getPlan( 1 );
                        request.setAttribute( "FORM_PlanManage", frmManage );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                    }
                    break;

                case OwnerRsvCommon.BTN_DRAFTUPD:
                    // ▼下書き　プラン設定更新
                    if ( execRegDraftRelease( hotelID, Integer.parseInt( request.getParameter( "selPlanId" ) ), logic, frm ) == false )
                    {
                        // エラーの場合、画面再表示
                        frm = execReviewPlan( request, logic, frm, DRAFT_ON );
                        frm.setDraftMode( DRAFT_ON );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    }
                    else
                    {
                        // 処理成功
                        // ホテル修正履歴
                        OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                frm.getUserId(), OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_ADD,
                                frm.getSelPlanID() * 10 + frm.getSalesFlag(), OwnerRsvCommon.ADJUST_MEMO_PLAN_ADD );

                        logicManage = new LogicOwnerRsvPlanManage();
                        frmManage = new FormOwnerRsvPlanManage();
                        frmManage.setSelHotelId( hotelID );
                        logicManage.setFrm( frmManage );
                        logicManage.getPlan( 1 );
                        request.setAttribute( "FORM_PlanManage", frmManage );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                    }
                    break;

                case OwnerRsvCommon.BTN_BACK:
                    // ▼戻る
                    logicManage = new LogicOwnerRsvPlanManage();
                    frmManage = new FormOwnerRsvPlanManage();

                    frmManage.setSelHotelId( hotelID );
                    logicManage.setFrm( frmManage );
                    logicManage.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
                    frmManage.setViewMode( OwnerRsvCommon.PLAN_VIEW_PART );
                    request.setAttribute( "FORM_PlanManage", frmManage );
                    OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                    break;
                case OwnerRsvCommon.BTN_PREVIEW:
                    // プレビュー画面表示
                    if ( isInputCheckMsg( frm ) == true )
                    {
                        if ( frm.getSelPlanID() > 0 )
                        {
                            // 最大・最小人数取得
                            logic.getPlanCharge( frm );
                            // プラン料金ﾓｰﾄﾞ名称取得
                            logic.getPlanChargeModeName( frm );
                            // プラン料金ﾓｰﾄﾞのﾁｪｯｸｲﾝﾁｪｯｸｱｳﾄ時刻の取得
                            logic.getPlanChargeDetail( frm );
                        }
                        request.setAttribute( "FORM_Plan", frm );
                        requestDispatcher = request.getRequestDispatcher( "owner_rsv_plan_preview1.jsp" );
                        requestDispatcher.forward( request, response );
                        return;
                    }
                    else
                    {
                        // 画面再表示
                        frm = execReviewPlan( request, logic, frm, DRAFT_OFF );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    }
                    break;
                case OwnerRsvCommon.BTN_PREVIEW_DETAIL:
                    // プレビュー詳細画面表示
                    if ( frm.getSelPlanID() > 0 )
                    {
                        // 最大・最小人数取得
                        logic.getPlanCharge( frm );
                        // プラン料金ﾓｰﾄﾞ名称取得
                        logic.getPlanChargeModeName( frm );
                        // プラン料金ﾓｰﾄﾞのﾁｪｯｸｲﾝﾁｪｯｸｱｳﾄ時刻の取得
                        logic.getPlanChargeDetail( frm );
                    }
                    frm.setPlanNm( new String( frm.getPlanNm().getBytes( "8859_1" ), "Windows-31J" ) );
                    frm.setPlanInfo( ReplaceString.DBEscape( new String( frm.getPlanInfo().getBytes( "8859_1" ), "Windows-31J" ) ) );
                    frm.setQuestion( ReplaceString.DBEscape( new String( frm.getQuestion().getBytes( "8859_1" ), "Windows-31J" ) ) );
                    frm.setRemarks( ReplaceString.DBEscape( new String( frm.getRemarks().getBytes( "8859_1" ), "Windows-31J" ) ) );
                    request.setAttribute( "FORM_Plan", frm );
                    request.setAttribute( "roomseq", roomseq );
                    requestDispatcher = request.getRequestDispatcher( "owner_rsv_plan_preview2.jsp" );
                    requestDispatcher.forward( request, response );
                    return;

                default:
                    // プラン設定更新
                    // 入力チェック
                    if ( isInputCheckMsg( frm ) == true )
                    {
                        // コピーのみの場合は元データの更新は行わない
                        if ( selBtn != OwnerRsvCommon.BTN_COPY )
                        {
                            // 登録・更新
                            // プラン設定更新の場合
                            logic.registPlan( Integer.parseInt( request.getParameter( "mode" ) ), DRAFT_OFF, OwnerRsvCommon.BTN_REGIST );
                            // ホテル修正履歴
                            int edit_id = 0;
                            int edit_sub = frm.getSelPlanID();
                            String memo = "";
                            if ( Integer.parseInt( request.getParameter( "mode" ) ) == MODE_NEW ) // 新規
                            {
                                edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_ADD;
                                memo = OwnerRsvCommon.ADJUST_MEMO_PLAN_ADD;
                            }
                            else
                            {
                                edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_UPD;
                                memo = OwnerRsvCommon.ADJUST_MEMO_PLAN_UPD;
                                // 掲載・非掲載の変更かチェック
                                if ( frm.getPublishingFlg() != replaceIntValue( request.getParameter( "planDispOld" ) ) )
                                {
                                    OwnerRsvCommon.addAdjustmentHistory( hotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                            frm.getUserId(), edit_id, edit_sub, memo );
                                    edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_UPDWN;
                                    edit_sub = edit_sub * 10 + frm.getPublishingFlg();
                                    memo = (frm.getPublishingFlg() == 0) ? OwnerRsvCommon.ADJUST_MEMO_PLAN_DWN : OwnerRsvCommon.ADJUST_MEMO_PLAN_UP;
                                }
                            }
                            OwnerRsvCommon.addAdjustmentHistory( hotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                    frm.getUserId(), edit_id, edit_sub, memo );
                        }
                        inputCheck = true;
                    }
                    // メニュー、施設情報の設定
                    frmMenu.setUserId( frm.getUserId() );
                    // 入力エラーの場合、入力値は保持する
                    if ( inputCheck == false )
                    {
                        // 画面再表示
                        frm = execReviewPlan( request, logic, frm, DRAFT_OFF );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    }
                    else
                    {
                        // 登録成功時
                        if ( selBtn == OwnerRsvCommon.BTN_COPYREGIST || selBtn == OwnerRsvCommon.BTN_COPY )
                        {
                            int oldPlanId = frm.getSelPlanID();
                            // 売上フラグのみFalseにする
                            frm.setSalesFlag( 0 );
                            logic.setFrm( frm );
                            // コピー処理(同じ内容で新規登録)
                            logic.registPlan( 2, DRAFT_OFF, OwnerRsvCommon.BTN_REGIST );
                            OwnerRsvCommon.addAdjustmentHistory( hotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                    frm.getUserId(), OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_ADD, 0, OwnerRsvCommon.ADJUST_MEMO_PLAN_ADD );
                            // プランに紐づく設定をコピー
                            copySetting( frm, oldPlanId );
                        }
                        logicManage = new LogicOwnerRsvPlanManage();
                        frmManage = new FormOwnerRsvPlanManage();
                        frmManage.setSelHotelId( hotelID );
                        logicManage.setFrm( frmManage );
                        logicManage.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
                        frmManage.setViewMode( OwnerRsvCommon.PLAN_VIEW_PART );
                        for( int i = 0 ; i < frmManage.getExPlanId().size() ; i++ )
                        {
                            if ( frmManage.getExPlanId().get( i ) != null && frmManage.getExPlanId().get( i ) == Integer.parseInt( request.getParameter( "selPlanId" ) ) )
                            {
                                checkplancharge = true;
                                break;
                            }
                        }
                        if ( checkplancharge == true || Integer.parseInt( request.getParameter( "selPlanId" ) ) == 0 )
                        {
                            request.setAttribute( "FORM_PlanManage", frmManage );
                            OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                        }
                        else
                        {
                            // 直接料金編集ページへ遷移
                            response.sendRedirect( "ownerRsvPlanChargeManage.act?selHotelIDValue=" + hotelID + "&planID=" + request.getParameter( "selPlanId" ) + "&edit=1" );
                            return;
                        }
                    }
                    break;
            }
            // 画面遷移
            frmMenu.setUserId( frm.getUserId() );
            request.setAttribute( "FORM_Menu", frmMenu );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlan.execute() ][hotelId = " + hotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvPlan.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * プランに紐付く設定コピー処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param oldPlanID コピー元プランID
     * @return なし
     */
    private void copySetting(FormOwnerRsvPlan frm, int oldPlanID)
    {
        try
        {
            // プラン別料金マスタコピー
            copyRsvPlanCharge( frm, oldPlanID );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvPlan.copySetting() ][hotelId = " + frm.getSelHotelID() + "] Exception", e );
        }
        finally
        {
        }

        return;
    }

    /**
     * プラン別料金マスタコピー処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param newPlanId コピー元プランID
     * @return なし
     */
    private void copyRsvPlanCharge(FormOwnerRsvPlan frm, int oldPlanId)
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataRsvPlanCharge dataPlanCharge = null;

        try
        {
            query = "select charge_mode_id from hh_rsv_plan_charge where id = ? and plan_id = ? ";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, oldPlanId );
            result = prestate.executeQuery();

            while( result.next() != false )
            {
                dataPlanCharge = new DataRsvPlanCharge();
                dataPlanCharge.getData( frm.getSelHotelID(), oldPlanId, result.getInt( "charge_mode_id" ) );
                // 新しいプランIDでINSERT
                dataPlanCharge.setPlanId( frm.getSelPlanID() );
                dataPlanCharge.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvPlan.copyRsvPlanCharge() ][hotelId = " + frm.getSelHotelID() + "] Exception", e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return;
    }

    /**
     * 一時的に停止するボタンクリック処理
     * 
     * @param request HttpServletRequest
     * @param logic LogicOwnerRsvPlan オブジェクト
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param draftKbn 0:通常、1:下書き
     * @return なし
     */
    private FormOwnerRsvPlan execSalesMode(HttpServletRequest request, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm, int draftKbn) throws Exception
    {
        int salesFlag = 0;
        int salesFlagDB = 0;

        salesFlag = Integer.parseInt( request.getParameter( "salesFlag" ) );
        salesFlag = setSalesFlg( logic, salesFlag );
        logic.getPlan_PlanId( frm.getSelPlanID(), draftKbn );

        // 入力値を保持
        setFormInputParam( request, frm );
        logic.setSubFormList( new ArrayList<FormOwnerRsvPlanSub>() );

        if ( salesFlagDB != -1 )
        {
            // 更新した販売フラグをセット
            frm.setSalesFlag( salesFlag );
        }
        else
        {
            frm.setSalesFlag( 0 );
        }

        // ボタン名をセット
        frm.setSalesBtnValue( BTN_OFF );
        if ( salesFlag == 1 )
        {
            // 販売中にする
            frm.setSalesBtnValue( BTN_ON );
        }

        // 料金情報取得
        logic.getPlanCharge( frm );
        // オプション設定
        createOption( frm, logic, draftKbn );

        return(logic.getFrm());
    }

    /**
     * プラン設定更新クリック処理(入力チェックエラー時)
     * 
     * @param request HttpServletRequest
     * @param logic LogicOwnerRsvPlan オブジェクト
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param draftKbn 0:通常、1:下書き
     * @return なし
     */
    private FormOwnerRsvPlan execReviewPlan(HttpServletRequest request, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm, int draftKbn) throws Exception
    {
        boolean isExistsRsv = false;

        // データ再取得
        logic.getPlan_PlanId( frm.getSelPlanID(), draftKbn );

        // 入力値を保持
        setFormInputParam( request, frm );
        logic.setSubFormList( new ArrayList<FormOwnerRsvPlanSub>() );

        if ( draftKbn == 0 )
        {
            // 通常の場合、料金情報取得
            logic.getPlanCharge( frm );
        }
        else
        {
            // 下書きの場合、下書きプラン情報取得
            logic.getDraftPlanIdList();
            logic.getDraftPlanNmList();
        }

        // オプション設定
        createOption( frm, logic, draftKbn );

        // 部屋情報設定
        createRoomList( frm, logic );

        // 予約データが存在するか
        isExistsRsv = OwnerRsvCommon.isExistsRsvPlan( frm.getSelHotelID(), frm.getSelPlanID() );
        frm.setExistsRsv( isExistsRsv );
        frm.setInfoMsg( "" );
        if ( isExistsRsv == true && draftKbn == 0 )
        {
            frm.setInfoMsg( Message.getMessage( "warn.30019" ) );
        }

        return(logic.getFrm());
    }

    /**
     * プラン・部屋設定データ取得
     * 
     * @param request HttpServletRequest
     * @param frm FormOwnerRsvPlanオブジェクト
     * @return なし
     * @throws Exception
     */
    private void setFormInputParam(HttpServletRequest request, FormOwnerRsvPlan frm) throws Exception
    {
        int paramHotelId = 0;
        int paramUserId = 0;
        int paramPlanId = 0;
        String paraOwnerHotelID = "";
        String paramDispIndex = "";
        String paramPlanNm = "";
        String paramStrDispFrom = "";
        String paramStrDispTo = "";
        String paramStrSalesFrom = "";
        String paramStrOrgDispFrom = "";
        String paramStrOrgDispTo = "";
        String paramStrOrgSalesFrom = "";
        String paramRsvEndDay = "";
        String paramRsvEndHH = "";
        String paramRsvEndMM = "";
        String paramRsvStartDay = "";
        String paramRsvStartHH = "";
        String paramRsvStartMM = "";
        String paramAdultNum = "";
        String paramChildNum = "";
        String paramMinAdultNum = "";
        String paramMinChildNum = "";
        String paramLongStayNum = "";
        String paramMinManNum = "0";
        String paramMaxManNum = "0";
        String paramMinWomanNum = "0";
        String paramMaxWomanNum = "0";
        int paramManCountFlg = 0;
        int paramHapyKbn = 0;
        String paramRoomPoint = "";
        String paramFixPoint = "";
        String paramQuestion = "";
        int paramQuestionFlag = 0;
        String paramPlanInfo = "";
        String paramRemarks = "";
        String paramQuantity = "";
        String paramFileNm = "";
        int paramImediaFlg = 0;
        int paramMode = 0;
        int paramOfferKind = 0;
        int paramSalesFlg = 0;
        int paramDefaultPoint = 0;
        int planDisp = 0;
        int userRequest = 0;
        int paramSalesStopWeekStatus = 0;
        int selSeq = 0;
        boolean isExistsRsv = false;
        FormOwnerRsvPlanSub frmSub;
        ArrayList<Integer> seqList = new ArrayList<Integer>();
        ArrayList<Integer> mustOptList = new ArrayList<Integer>();
        ArrayList<Integer> commOptList = new ArrayList<Integer>();
        ArrayList<FormOwnerRsvPlanSub> frmSubList = new ArrayList<FormOwnerRsvPlanSub>();

        // 画面の値を取得
        if ( request.getParameter( "selHotelIDValue" ) != null )
        {
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
        }
        paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
        paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
        if ( request.getParameter( "mode" ) != null )
        {
            paramMode = Integer.parseInt( request.getParameter( "mode" ) );
        }
        if ( request.getParameter( "selPlanId" ) != null )
        {
            paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );
        }
        // 表示順
        if ( request.getParameter( "dispIndex" ) != null )
        {
            paramDispIndex = request.getParameter( "dispIndex" ).trim();
        }
        // プラン名
        if ( request.getParameter( "planNm" ) != null )
        {
            paramPlanNm = request.getParameter( "planNm" );
        }
        // 表示期間
        if ( request.getParameter( "dispDateFrom" ) != null )
        {
            paramStrDispFrom = request.getParameter( "dispDateFrom" );
        }
        if ( request.getParameter( "dispDateTo" ) != null )
        {
            paramStrDispTo = request.getParameter( "dispDateTo" );
        }
        if ( request.getParameter( "orgDispStartDate" ) != null )
        {
            paramStrOrgDispFrom = request.getParameter( "orgDispStartDate" );
        }
        if ( request.getParameter( "orgDispEndDate" ) != null )
        {
            paramStrOrgDispTo = request.getParameter( "orgDispEndDate" );
        }
        // 販売期間
        if ( request.getParameter( "salesDateFrom" ) != null )
        {
            paramStrSalesFrom = request.getParameter( "salesDateFrom" );
        }
        if ( request.getParameter( "orgSalesStartDate" ) != null )
        {
            paramStrOrgSalesFrom = request.getParameter( "orgSalesStartDate" );
        }
        // 予約受付開始
        if ( request.getParameter( "startDayFrom" ) != null )
        {
            paramRsvStartDay = request.getParameter( "startDayFrom" );
        }
        if ( request.getParameter( "startHHFrom" ) != null )
        {
            paramRsvStartHH = request.getParameter( "startHHFrom" );
        }
        if ( request.getParameter( "startMMFrom" ) != null )
        {
            paramRsvStartMM = request.getParameter( "startMMFrom" );
        }
        if ( request.getParameter( "endDayTo" ) != null )
        {
            paramRsvEndDay = request.getParameter( "endDayTo" );
        }
        if ( request.getParameter( "endHHTo" ) != null )
        {
            paramRsvEndHH = request.getParameter( "endHHTo" );
        }
        if ( request.getParameter( "endMMTo" ) != null )
        {
            paramRsvEndMM = request.getParameter( "endMMTo" );
        }
        // 最大人数
        if ( request.getParameter( "adultNum" ) != null )
        {
            paramAdultNum = request.getParameter( "adultNum" ).trim();
        }
        if ( request.getParameter( "childNum" ) != null )
        {
            paramChildNum = request.getParameter( "childNum" ).trim();
        }
        // 最少人数
        if ( request.getParameter( "minAdultNum" ) != null )
        {
            paramMinAdultNum = request.getParameter( "minAdultNum" ).trim();
        }
        if ( request.getParameter( "minChildNum" ) != null )
        {
            paramMinChildNum = request.getParameter( "minChildNum" ).trim();
        }
        // 男女範囲人数
        if ( request.getParameter( "minManNum" ) != null )
        {
            if ( request.getParameter( "minManNum" ).trim().equals( "" ) == false )
            {
                paramMinManNum = request.getParameter( "minManNum" ).trim();
            }
        }
        if ( request.getParameter( "minWomanNum" ) != null )
        {
            if ( request.getParameter( "minWomanNum" ).trim().equals( "" ) == false )
            {
                paramMinWomanNum = request.getParameter( "minWomanNum" ).trim();
            }
        }
        // 販売停止曜日
        if ( request.getParameter( "salesStopWeekStatus" ) != null )
        {
            paramSalesStopWeekStatus = Integer.parseInt( request.getParameter( "salesStopWeekStatus" ) );
        }

        if ( request.getParameter( "maxManNum" ) != null )
        {
            if ( request.getParameter( "maxManNum" ).trim().equals( "" ) == false )
            {
                paramMaxManNum = request.getParameter( "maxManNum" ).trim();
            }
        }
        if ( request.getParameter( "maxWomanNum" ) != null )
        {
            if ( request.getParameter( "maxWomanNum" ).trim().equals( "" ) == false )
            {
                paramMaxWomanNum = request.getParameter( "maxWomanNum" ).trim();
            }
        }

        // 連泊可能数
        if ( request.getParameter( "lognStayDays" ) != null )
        {
            paramLongStayNum = request.getParameter( "lognStayDays" ).trim();
        }
        // imediaFlg
        if ( request.getParameter( "imediaFlg" ) != null )
        {
            paramImediaFlg = Integer.parseInt( request.getParameter( "imediaFlg" ) );
        }
        // ハピー付与
        if ( request.getParameter( "defaultPoint" ) != null )
        {
            paramDefaultPoint = Integer.parseInt( request.getParameter( "defaultPoint" ) );
        }
        if ( paramImediaFlg == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
        {
            // ALMEX社員
            paramHapyKbn = Integer.parseInt( request.getParameter( "point" ) );
            paramRoomPoint = request.getParameter( "roomPoint" ).trim();
            paramFixPoint = request.getParameter( "fixPointNm" ).trim();
        }
        else
        {
            // ホテルオーナー
            paramHapyKbn = OwnerRsvCommon.POINT_KIND_FIX;
            paramRoomPoint = "0";
            paramFixPoint = Integer.toString( paramDefaultPoint );
        }
        // 男性多数許可フラグ
        if ( request.getParameter( "manCountJudge" ) != null && request.getParameter( "manCountJudge" ).equals( "1" ) )
        {
            paramManCountFlg = 1;
        }
        // 予約者へ質問
        if ( request.getParameter( "question" ) != null )
        {
            paramQuestion = request.getParameter( "question" );
        }
        // 予約者へ質問フラグ
        if ( request.getParameter( "questionFlag" ) != null && request.getParameter( "questionFlag" ).equals( "on" ) )
        {
            paramQuestionFlag = 1;
        }
        // プラン紹介
        if ( request.getParameter( "planInfo" ) != null )
        {
            paramPlanInfo = request.getParameter( "planInfo" );
        }
        // 備考
        if ( request.getParameter( "remarks" ) != null )
        {
            paramRemarks = request.getParameter( "remarks" );
        }
        // プラン画像
        if ( request.getParameter( "setFileNm" ) != null )
        {
            paramFileNm = request.getParameter( "setFileNm" );
        }
        // 最大予約受付数
        if ( request.getParameter( "quantity" ) != null )
        {
            paramQuantity = request.getParameter( "quantity" ).trim();
        }
        // 部屋指定
        if ( request.getParameter( "offerKind" ) != null )
        {
            paramOfferKind = replaceIntValue( request.getParameter( "offerKind" ) );
        }
        // プラン掲載
        if ( request.getParameter( "planDisp" ) != null )
        {
            planDisp = replaceIntValue( request.getParameter( "planDisp" ) );
        }
        // 要望入力項目
        if ( request.getParameter( "userRequest" ) != null )
        {
            userRequest = replaceIntValue( request.getParameter( "userRequest" ) );
        }
        // 販売フラグ
        if ( request.getParameter( "salesFlag" ) != null )
        {
            paramSalesFlg = Integer.parseInt( request.getParameter( "salesFlag" ) );
        }
        // 適用部屋
        String[] seqs = request.getParameterValues( "roomNum" );
        if ( seqs != null )
        {
            for( int i = 0 ; i < seqs.length ; i++ )
            {
                seqList.add( Integer.parseInt( seqs[i] ) );
            }
        }
        // 適用部屋に設定されている開始日・終了日
        for( int i = 0 ; i < seqList.size() ; i++ )
        {
            selSeq = seqList.get( i );
            frmSub = new FormOwnerRsvPlanSub();
            frmSub.setCheck( 1 );
            frmSub.setSeq( selSeq );
            // frmSub.setTekiyoDateFrom( request.getParameter( "dateFrom" + selSeq ) );
            // frmSub.setTekiyoDateTo( request.getParameter( "dateTo" + selSeq ) );

            // 必ず当日日付をセットするようにする
            frmSub.setTekiyoDateFrom( DateEdit.getDate( 1 ) );
            // 必ず99999999をセットするようにする
            frmSub.setTekiyoDateTo( "9999/99/99" );
            frmSubList.add( frmSub );
        }

        // 必須オプション
        String[] mustOpts = request.getParameterValues( "mustOpt" );
        if ( mustOpts != null )
        {
            for( int i = 0 ; i < mustOpts.length ; i++ )
            {
                mustOptList.add( Integer.parseInt( mustOpts[i] ) );
            }
        }

        // 通常オプション
        String[] commOpts = request.getParameterValues( "commOpt" );
        if ( commOpts != null )
        {
            for( int i = 0 ; i < commOpts.length ; i++ )
            {
                commOptList.add( Integer.parseInt( commOpts[i] ) );
            }
        }
        // フォームにセット
        frm.setSelHotelID( paramHotelId );
        frm.setOwnerHotelID( paraOwnerHotelID );
        frm.setUserId( paramUserId );
        frm.setSelPlanID( paramPlanId );
        frm.setPlanNm( paramPlanNm );
        frm.setDispIndex( paramDispIndex );
        frm.setMode( paramMode );
        // 表示期間
        frm.setDispStartDate( paramStrDispFrom );
        frm.setDispEndDate( paramStrDispTo );
        frm.setOrgDispStartDate( paramStrOrgDispFrom );
        frm.setOrgDispEndDate( paramStrOrgDispTo );
        // 販売期間
        frm.setSalesStartDate( paramStrSalesFrom );
        frm.setOrgSalesStartDate( paramStrOrgSalesFrom );
        // 予約受付
        frm.setRsvStartDay( paramRsvStartDay );
        frm.setRsvStartTimeHH( paramRsvStartHH );
        frm.setRsvStartTimeMM( paramRsvStartMM );
        frm.setRsvEndDay( paramRsvEndDay );
        frm.setRsvEndTimeHH( paramRsvEndHH );
        frm.setRsvEndTimeMM( paramRsvEndMM );
        // 最大人数
        frm.setMaxNumAdult( paramAdultNum );
        frm.setMaxNumChild( paramChildNum );
        // 最少人数
        frm.setMinNumAdult( paramMinAdultNum );
        frm.setMinNumChild( paramMinChildNum );
        // 男女人数範囲
        frm.setMinNumMan( paramMinManNum );
        frm.setMinNumWoman( paramMinWomanNum );
        frm.setMaxNumMan( paramMaxManNum );
        frm.setMaxNumWoman( paramMaxWomanNum );
        // 男性多数許可
        frm.setManCountJudgeFlg( paramManCountFlg );
        // 販売停止曜日
        frm.setSalesStopWeekStatus( paramSalesStopWeekStatus );
        // 連泊可能数
        frm.setRenpakuNum( paramLongStayNum );
        // imediaflg
        frm.setImediaFlg( paramImediaFlg );
        // ハピーポイント初期値
        frm.setDefaultPoint( paramDefaultPoint );
        // ハピー付与
        frm.setPointKbn( paramHapyKbn );
        frm.setPointRoom( paramRoomPoint );
        frm.setPointFix( paramFixPoint );
        // 予約者へ質問
        frm.setQuestion( paramQuestion );
        // 予約者へ質問必須フラグ
        frm.setQuestionFlag( paramQuestionFlag );
        // プラン紹介
        frm.setPlanInfo( paramPlanInfo );
        // 備考
        frm.setRemarks( paramRemarks );
        // プラン画像
        frm.setPlanImg( paramFileNm );
        // プラン掲載
        frm.setPublishingFlg( planDisp );
        // 要望入力項目表示
        frm.setUserRequestFlg( userRequest );
        // 最大予約受付数
        frm.setMaxQuantity( paramQuantity );
        // 部屋指定
        frm.setOfferKbn( paramOfferKind );
        // 適用部屋
        frm.setFrmSubList( frmSubList );
        // 販売フラグ
        frm.setSalesFlag( paramSalesFlg );
        if ( paramSalesFlg == 0 )
        {
            frm.setSalesBtnValue( BTN_OFF );
        }
        else
        {
            frm.setSalesBtnValue( BTN_ON );
        }
        // 必須オプション
        frm.setMustOptIdList( mustOptList );
        // 通常オプション
        frm.setComOptIdList( commOptList );

        // ▼予約データが存在するか
        isExistsRsv = OwnerRsvCommon.isExistsRsvPlan( frm.getSelHotelID(), frm.getSelPlanID() );
        frm.setExistsRsv( isExistsRsv );
        frm.setInfoMsg( Message.getMessage( "warn.30019" ) );

    }

    /**
     * Int型の値に変換する
     * 
     * @param value 変換対象の値
     * @return 変換後の値
     */
    private int replaceIntValue(String value)
    {
        int ret = 0;

        if ( (value == null) || (value.trim().length() == 0) )
        {
            ret = 0;
        }
        else
        {
            ret = Integer.parseInt( value );
        }
        return(ret);
    }

    /**
     * 入力値チェック
     * 
     * @param input FormOwnerRsvPlanオブジェクト
     * @return true:正常、false:エラーあり
     */
    private boolean isInputCheckMsg(FormOwnerRsvPlan frm) throws Exception
    {
        String msg;
        int ret;
        boolean isCheck = false;
        boolean isDispFromCheck = true;
        boolean isDispToCheck = true;
        boolean isSalesFromCheck = true;
        boolean isFromCheck = true;
        boolean isToCheck = true;
        int maxnumChild = 0;
        int maxnumAdult = 0;
        int minnumChild = 0;
        int minnumAdult = 0;
        int maxnumMan = 0;
        int minnumMan = 0;
        int maxnumWoman = 0;
        int minnumWoman = 0;
        int orgDate = 0;
        int frmDate = 0;
        FormOwnerRsvPlanSub frmSub;
        ArrayList<FormOwnerRsvPlanSub> frmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        boolean maxnumcheckAdult = false;
        boolean maxnumcheckChild = false;
        msg = "";
        ret = 0;

        // 表示順(下書きモード時はチェックしない)
        if ( frm.getDraftMode() == DRAFT_OFF )
        {
            if ( CheckString.onlySpaceCheck( frm.getDispIndex() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "表示順" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frm.getDispIndex() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "表示順", "0以上の数値" ) + "<br />";
                }
            }
        }
        else
        {
            frm.setDispIndex( "0" );
        }

        // プラン名
        if ( CheckString.onlySpaceCheck( frm.getPlanNm() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "プラン名" ) + "<br />";
        }
        else
        {
            ret = OwnerRsvCommon.LengthCheck( frm.getPlanNm().trim(), 60 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                msg = msg + Message.getMessage( "warn.00038", "プラン名", "30" ) + "<br />";
            }
        }

        // 表示期間
        if ( CheckString.onlySpaceCheck( frm.getDispStartDate() ) == true )
        {
            // 開始日が未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "表示期間 開始日" ) + "<br />";
            isDispFromCheck = false;
        }

        if ( CheckString.onlySpaceCheck( frm.getDispEndDate() ) == true )
        {
            // 終了日が未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "表示期間 終了" ) + "<br />";
            isDispToCheck = false;

        }
        else
        {
            // 変更前から変更後の期間の間に予約データがあるか
            if ( frm.getOrgDispEndDate().trim().length() != 0 )
            {
                orgDate = Integer.parseInt( frm.getOrgDispEndDate().replace( "/", "" ) );
                frmDate = Integer.parseInt( frm.getDispEndDate().replace( "/", "" ) );
                if ( orgDate > frmDate )
                {
                    if ( OwnerRsvCommon.isReservePlanTargetDate( frm.getSelHotelID(), frm.getSelPlanID(), frmDate, orgDate ) == true )
                    {
                        msg = msg + Message.getMessage( "warn.30013" ) + "<br />";
                        isDispToCheck = false;
                    }
                }
            }
        }

        if ( (isDispFromCheck == true) && (isDispToCheck == true) )
        {
            // 開始日・終了日のFrom-Toチェック
            if ( frm.getDispStartDate().compareTo( frm.getDispEndDate() ) > 0 )
            {
                msg = msg + Message.getMessage( "warn.00009", "表示期間の範囲指定" ) + "<br />";
            }
        }

        // 販売期間
        if ( CheckString.onlySpaceCheck( frm.getSalesStartDate() ) == true )
        {
            // 開始日が未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "販売開始日" ) + "<br />";
            isSalesFromCheck = false;
        }
        else
        {
            // 変更前から変更後の期間の間に予約データがあるか
            if ( frm.getOrgSalesStartDate().trim().length() != 0 )
            {
                orgDate = Integer.parseInt( frm.getOrgSalesStartDate().replace( "/", "" ) );
                frmDate = Integer.parseInt( frm.getSalesStartDate().replace( "/", "" ) );
                if ( orgDate < frmDate )
                {
                    if ( OwnerRsvCommon.isReservePlanTargetDate( frm.getSelHotelID(), frm.getSelPlanID(), orgDate, frmDate ) == true )
                    {
                        msg = msg + Message.getMessage( "warn.30013" ) + "<br />";
                        isSalesFromCheck = false;
                    }
                }
            }
        }
        if ( (isDispFromCheck == true) && (isSalesFromCheck == true) )
        {
            // 販売開始日、表示期間Fromチェック
            if ( frm.getDispStartDate().compareTo( frm.getSalesStartDate() ) > 0 )
            {
                msg = msg + Message.getMessage( "warn.30005", "販売期間", "表示期間開始日" ) + "<br />";
            }
        }

        // 表示期間(終了) < 販売期間(開始)の場合はエラー
        if ( (isDispToCheck == true) && (isSalesFromCheck == true) )
        {
            if ( frm.getSalesStartDate().compareTo( frm.getDispEndDate() ) > 0 )
            {
                msg = msg + Message.getMessage( "warn.30011", "販売期間", "表示期間終了日" ) + "<br />";
            }
        }

        // 予約受付
        if ( (CheckString.onlySpaceCheck( frm.getRsvEndDay() ) == true) && (CheckString.onlySpaceCheck( frm.getRsvEndTimeHH() ) == true)
                && (CheckString.onlySpaceCheck( frm.getRsvEndTimeMM() ) == true) && (CheckString.onlySpaceCheck( frm.getRsvStartDay() ) == true)
                && (CheckString.onlySpaceCheck( frm.getRsvStartTimeHH() ) == true) && (CheckString.onlySpaceCheck( frm.getRsvStartTimeMM() ) == true) )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "予約受付開始" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getRsvEndDay() ) == false) || (OwnerRsvCommon.numCheck( frm.getRsvEndTimeHH() ) == false)
                    || (OwnerRsvCommon.numCheck( frm.getRsvEndTimeMM() ) == false) || (OwnerRsvCommon.numCheck( frm.getRsvStartDay() ) == false)
                    || (OwnerRsvCommon.numCheck( frm.getRsvStartTimeHH() ) == false) || (OwnerRsvCommon.numCheck( frm.getRsvStartTimeMM() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "予約受付開始の日付、時間", "整数" ) + "<br />";
            }
            else
            {
                if ( (replaceIntValue( frm.getRsvStartDay() ) == 0) || (replaceIntValue( frm.getRsvEndDay() ) == 0) || (replaceIntValue( frm.getRsvStartDay() ) < 0) || (replaceIntValue( frm.getRsvEndDay() ) < 0) )
                {
                    // 開始日、または終了日が0
                    msg = msg + Message.getMessage( "warn.30006", "予約受付開始", "0" ) + "<br />";
                }
                // 60日を超えていないかどうかの判定
                if ( replaceIntValue( frm.getRsvStartDay() ) > 60 || replaceIntValue( frm.getRsvEndDay() ) > 60 )
                {
                    msg = msg + Message.getMessage( "warn.30007", "予約受付開始", "60以下の整数" ) + "<br />";
                }
                if ( (Integer.parseInt( frm.getRsvEndTimeHH() ) > 23 || Integer.parseInt( frm.getRsvEndTimeHH() ) < 0)
                        || (Integer.parseInt( frm.getRsvStartTimeHH() ) > 23 || Integer.parseInt( frm.getRsvStartTimeHH() ) < 0) )
                {
                    // 時間が0時～23時以外の場合
                    msg = msg + Message.getMessage( "warn.30007", "予約受付開始の時間", "0～23" ) + "<br />";
                }
                if ( (Integer.parseInt( frm.getRsvEndTimeMM() ) > 59 || Integer.parseInt( frm.getRsvEndTimeMM() ) < 0)
                        || (Integer.parseInt( frm.getRsvStartTimeMM() ) > 59 || Integer.parseInt( frm.getRsvStartTimeMM() ) < 0) )
                {
                    // 分が0時～59以外の場合
                    msg = msg + Message.getMessage( "warn.30007", "予約受付開始の分", "0～59" ) + "<br />";
                }
                if ( ((Integer.parseInt( frm.getRsvStartDay() ) > 0) && (Integer.parseInt( frm.getRsvStartTimeHH() ) >= 0) && (Integer.parseInt( frm.getRsvStartTimeMM() ) >= 0))
                        && ((Integer.parseInt( frm.getRsvEndDay() ) > 0) && (Integer.parseInt( frm.getRsvEndTimeHH() ) >= 0) && (Integer.parseInt( frm.getRsvEndTimeMM() ) >= 0)) )
                {
                    // 前後関係チェック
                    if ( Integer.parseInt( frm.getRsvStartDay() ) < Integer.parseInt( frm.getRsvEndDay() ) )
                    {
                        // 日の前後関係が不正
                        msg = msg + Message.getMessage( "warn.00009", "予約受付開始の日付の範囲指定" ) + "<br />";
                    }
                    else if ( Integer.parseInt( frm.getRsvStartDay() ) == Integer.parseInt( frm.getRsvEndDay() ) )
                    {
                        // 同一日付の場合、時間の前後関係チェック
                        if ( Integer.parseInt( frm.getRsvStartTimeHH() ) > Integer.parseInt( frm.getRsvEndTimeHH() ) )
                        {
                            msg = msg + Message.getMessage( "warn.00009", "予約受付開始の時間の範囲指定" ) + "<br />";
                        }
                        else if ( Integer.parseInt( frm.getRsvStartTimeHH() ) == Integer.parseInt( frm.getRsvEndTimeHH() ) )
                        {
                            // 同一時間の場合、分の前後関係チェック
                            if ( Integer.parseInt( frm.getRsvStartTimeMM() ) > Integer.parseInt( frm.getRsvEndTimeMM() ) )
                            {
                                msg = msg + Message.getMessage( "warn.00009", "予約受付開始の分の範囲指定" ) + "<br />";
                            }
                        }
                    }
                }
            }
        }

        // 最大人数
        if ( (CheckString.onlySpaceCheck( frm.getMaxNumAdult() ) == true) || (CheckString.onlySpaceCheck( frm.getMaxNumChild() ) == true) )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "最大人数" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMaxNumAdult() ) == false) || (OwnerRsvCommon.numCheck( frm.getMaxNumChild() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "最大人数", "1以上の整数" ) + "<br />";
            }
            else
            {
                maxnumcheckAdult = true;
                if ( Integer.parseInt( frm.getMaxNumAdult() ) <= 0 )
                {
                    msg = msg + Message.getMessage( "warn.30007", "大人の最大人数", "1以上の整数" ) + "<br />";
                }
                else
                {
                    maxnumAdult = Integer.parseInt( frm.getMaxNumAdult() );
                }
                maxnumcheckChild = true;
                if ( (frm.getMaxNumChild().trim().length() != 0) && (Integer.parseInt( frm.getMaxNumChild() ) < 0) )
                {
                    msg = msg + Message.getMessage( "warn.30007", "子供の最大人数", "0以上の整数" ) + "<br />";
                }
                else
                {
                    maxnumChild = Integer.parseInt( frm.getMaxNumChild() );
                }
            }
        }

        // 最少人数
        if ( (CheckString.onlySpaceCheck( frm.getMinNumAdult() ) == true) || (CheckString.onlySpaceCheck( frm.getMinNumChild() ) == true) )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "最少人数" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMinNumAdult() ) == false) || (OwnerRsvCommon.numCheck( frm.getMinNumChild() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "最少人数", "1以上の整数" ) + "<br />";
            }
            else
            {
                if ( Integer.parseInt( frm.getMinNumAdult() ) <= 0 )
                {
                    msg = msg + Message.getMessage( "warn.30007", "大人の最少人数", "1以上の整数" ) + "<br />";
                }
                else
                {
                    minnumAdult = Integer.parseInt( frm.getMinNumAdult() );
                }
                if ( (frm.getMinNumChild().trim().length() != 0) && (Integer.parseInt( frm.getMinNumChild() ) < 0) )
                {
                    msg = msg + Message.getMessage( "warn.30007", "子供の最少人数", "0以上の整数" ) + "<br />";
                }
                else
                {
                    minnumChild = Integer.parseInt( frm.getMinNumChild() );
                }
            }
        }

        // 最大最少人数の数
        if ( maxnumcheckAdult == true && minnumAdult > maxnumAdult )
        {
            msg = msg + Message.getMessage( "warn.30006", "大人の最大人数", "大人の最少人数" ) + "<br />";
        }

        if ( maxnumcheckChild == true && minnumChild > maxnumChild )
        {
            msg = msg + Message.getMessage( "warn.30006", "子供の最大人数", "子供の最少人数" ) + "<br />";
        }

        // 連泊可能数
        if ( CheckString.onlySpaceCheck( frm.getRenpakuNum() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "連泊可能数" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getRenpakuNum() ) == false) || (OwnerRsvCommon.numCheck( frm.getRenpakuNum() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "連泊可能数", "1以上の整数" ) + "<br />";
            }
            else
            {
                if ( (frm.getRenpakuNum().trim().length() == 0) || (Integer.parseInt( frm.getRenpakuNum() ) <= 0) )
                {
                    msg = msg + Message.getMessage( "warn.30006", "連泊可能数", "0" ) + "<br />";
                }
            }
        }

        // 性別人数範囲の数値チェック
        if ( (CheckString.onlySpaceCheck( frm.getMinNumMan() ) == true) || (CheckString.onlySpaceCheck( frm.getMinNumWoman() ) == true) ||
                (CheckString.onlySpaceCheck( frm.getMaxNumMan() ) == true) || (CheckString.onlySpaceCheck( frm.getMaxNumWoman() ) == true) )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "性別人数範囲" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMinNumMan() ) == false) || (OwnerRsvCommon.numCheck( frm.getMinNumWoman() ) == false) ||
                    (OwnerRsvCommon.numCheck( frm.getMaxNumMan() ) == false) || (OwnerRsvCommon.numCheck( frm.getMaxNumWoman() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "性別人数範囲", "0以上の整数" ) + "<br />";
            }
            else
            {
                if ( (frm.getMinNumMan().trim().length() != 0) && (Integer.parseInt( frm.getMinNumMan() ) < 0) ||
                        (frm.getMinNumWoman().trim().length() != 0) && (Integer.parseInt( frm.getMinNumWoman() ) < 0) ||
                        (frm.getMaxNumMan().trim().length() != 0) && (Integer.parseInt( frm.getMaxNumMan() ) < 0) ||
                        (frm.getMaxNumWoman().trim().length() != 0) && (Integer.parseInt( frm.getMaxNumWoman() ) < 0) )
                {
                    msg = msg + Message.getMessage( "warn.30007", "性別人数範囲", "0以上の整数" ) + "<br />";
                }
                else
                {
                    minnumMan = Integer.parseInt( frm.getMinNumMan() );
                    minnumWoman = Integer.parseInt( frm.getMinNumWoman() );
                    maxnumMan = Integer.parseInt( frm.getMaxNumMan() );
                    maxnumWoman = Integer.parseInt( frm.getMaxNumWoman() );
                }
            }
        }

        // 性別人数範囲の最大最少チェック
        if ( minnumMan > maxnumMan )
        {
            msg = msg + Message.getMessage( "warn.30006", "男性の最大人数", "男性の最少人数" ) + "<br />";
        }

        if ( minnumWoman > maxnumWoman )
        {
            msg = msg + Message.getMessage( "warn.30006", "女性の最大人数", "女性の最少人数" ) + "<br />";
        }

        // 男性・女性合計の最大人数設定チェック
        if ( maxnumMan > 0 || maxnumWoman > 0 )
        {
            if ( maxnumAdult > maxnumMan + maxnumWoman )
            {
                msg = msg + Message.getMessage( "warn.30006", "男性の最大人数と女性の最大人数の合計", "大人の最大人数" ) + "<br />";
            }
            // 男性・女性合計の最低人数設定チェック
            if ( minnumAdult > maxnumMan + maxnumWoman )
            {
                msg = msg + Message.getMessage( "warn.30006", "男性の最大人数と女性の最大人数の合計", "大人の最少人数" ) + "<br />";
            }
        }

        // 男性多数不許可の場合は女性の最大人数より男性最大人数が多いことを認めない
        if ( frm.getManCountJudgeFlg() == 0 && maxnumMan > maxnumWoman )
        {
            msg = msg + Message.getMessage( "warn.30006", "男性多数を許可しない場合、女性の最大人数", "男性の最大人数" ) + "<br />";
        }

        // ハピー付与
        if ( frm.getPointKbn() == 1 )
        {
            // 室料選択
            if ( CheckString.onlySpaceCheck( frm.getPointRoom() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "ハピー付与ポイント" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frm.getPointRoom() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "ハピー付与ポイント", "0以上の整数" ) + "<br />";
                }
                else
                {
                    if ( (frm.getPointKbn() == 1) && ((frm.getPointFix().trim().length() != 0) && ((Integer.parseInt( frm.getPointFix() ) > 0) || (Integer.parseInt( frm.getPointFix() ) < 0))) )
                    {
                        msg = msg + Message.getMessage( "warn.30008", "室利用に対する割合", "固定ポイント" ) + "<br />";
                    }
                }
            }
        }
        else
        {
            // 部屋選択
            if ( CheckString.onlySpaceCheck( frm.getPointFix() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "ハピー付与ポイント" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frm.getPointFix() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "ハピー付与ポイント", "0以上の整数" ) + "<br />";
                }
                else
                {
                    if ( (frm.getPointKbn() == 2) && ((frm.getPointRoom().trim().length() != 0) && ((Integer.parseInt( frm.getPointRoom() ) > 0) || (Integer.parseInt( frm.getPointRoom() ) < 0))) )
                    {
                        msg = msg + Message.getMessage( "warn.30008", "固定ポイント", "室利用に対する割合" ) + "<br />";
                    }
                }
            }
        }

        // プラン紹介
        if ( CheckString.onlySpaceCheck( frm.getPlanInfo() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "プラン紹介" ) + "<br />";
        }
        else
        {
            ret = OwnerRsvCommon.LengthCheck( frm.getPlanInfo().trim(), 300 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                msg = msg + Message.getMessage( "warn.00038", "プラン紹介", "150" ) + "<br />";
            }
        }

        // 予約者へ質問(必須の場合は空白は許可しない)
        if ( frm.getQuestionFlag() == 1 )
        {
            if ( CheckString.onlySpaceCheck( frm.getQuestion() ) == true )
            {
                msg = msg + Message.getMessage( "warn.00001", "予約者へ質問" ) + "<br />";
            }
        }

        // 備考
        ret = OwnerRsvCommon.LengthCheck( frm.getRemarks().trim(), 200 );
        if ( ret == 1 )
        {
            // 桁数Overの場合
            msg = msg + Message.getMessage( "warn.00038", "備考", "100" ) + "<br />";
        }

        // 最大予約受付数
        if ( CheckString.onlySpaceCheck( frm.getMaxQuantity() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "最大予約受付数" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMaxQuantity() ) == false) || (OwnerRsvCommon.numCheck( frm.getMaxQuantity() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "最大予約受付数", "1以上の整数" ) + "<br />";
            }
        }

        // 適用部屋
        boolean isChecked = false;
        frmSubList = frm.getFrmSubList();
        for( int i = 0 ; i < frmSubList.size() ; i++ )
        {
            frmSub = frmSubList.get( i );
            if ( frmSub.getCheck() == 1 )
            {
                isChecked = true;
                break;
            }
        }
        if ( isChecked == false )
        {
            // 未選択
            msg = msg + Message.getMessage( "warn.00002", "適用部屋" ) + "<br />";
        }
        else
        {
            for( int i = 0 ; i < frmSubList.size() ; i++ )
            {
                frmSub = frmSubList.get( i );
                isFromCheck = true;
                isToCheck = true;

                if ( CheckString.onlySpaceCheck( frmSub.getTekiyoDateFrom() ) == true )
                {
                    // 開始日が未入力の場合
                    isFromCheck = false;
                }
                if ( CheckString.onlySpaceCheck( frmSub.getTekiyoDateTo() ) == true )
                {
                    // 終了日が未入力の場合
                    isFromCheck = false;
                }
                if ( (isFromCheck == true) && (isToCheck == true) )
                {
                    // 開始日・終了日のFrom-Toチェック
                    if ( Integer.parseInt( frmSub.getTekiyoDateFrom().replace( "/", "" ) ) > Integer.parseInt( frmSub.getTekiyoDateTo().replace( "/", "" ) ) )
                    {
                        msg = msg + Message.getMessage( "warn.00009", "適用部屋で選択されている、管理番号[" + frmSub.getSeq() + "]の販売期間の範囲指定" ) + "<br />";
                    }
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
     * オプション情報作成処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param logic LogicOwnerRsvPlanオブジェクト
     * @param draftKbn 0:通常、1:下書き
     * @return なし
     */
    private void createOption(FormOwnerRsvPlan frm, LogicOwnerRsvPlan logic, int draftKbn) throws Exception
    {
        String mustOpt = "";
        String commOpt = "";
        FormOwnerRsvPlanOptionSub newFrmOptSub;
        ArrayList<FormOwnerRsvPlanOptionSub> allMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();

        // ▼必須オプション情報取得
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 1 );
        allMustOptFrmSubList = logic.getSubMustOptionFormList();

        // 登録済み必須オプションの取得
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getOption( 1, draftKbn );
        regMustOptFrmSubList = logic.getSubMustOptionFormList();
        for( int i = 0 ; i < regMustOptFrmSubList.size() ; i++ )
        {
            if ( mustOpt.trim().length() != 0 )
            {
                mustOpt = mustOpt + "<br />";
            }
            mustOpt = mustOpt + regMustOptFrmSubList.get( i ).getOptionNm()
                    + "(" + regMustOptFrmSubList.get( i ).getOptionSubNm() + ")";
        }
        frm.setMustOptions( mustOpt );

        // 画面の内容とマージ
        for( int i = 0 ; i < allMustOptFrmSubList.size() ; i++ )
        {
            FormOwnerRsvPlanOptionSub optionSub = allMustOptFrmSubList.get( i );
            newFrmOptSub = new FormOwnerRsvPlanOptionSub();
            newFrmOptSub.setCheck( 0 );
            newFrmOptSub.setOptionID( optionSub.getOptionID() );
            newFrmOptSub.setOptionNm( optionSub.getOptionNm() );
            newFrmOptSub.setOptionSubNm( optionSub.getOptionSubNm() );

            for( int j = 0 ; j < frm.getMustOptIdList().size() ; j++ )
            {
                if ( optionSub.getOptionID() == frm.getMustOptIdList().get( j ) )
                {
                    newFrmOptSub.setCheck( 1 );
                    newFrmOptSub.setOptionID( optionSub.getOptionID() );
                    newFrmOptSub.setOptionNm( optionSub.getOptionNm() );
                    newFrmOptSub.setOptionSubNm( optionSub.getOptionSubNm() );
                    break;
                }
            }
            newMustOptFrmSubList.add( newFrmOptSub );
        }

        // ▼通常オプション情報取得
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 0 );
        allCommOptFrmSubList = logic.getSubCommOptionFormList();

        // 登録済み必須オプションの取得
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getOption( 0, draftKbn );
        regCommOptFrmSubList = logic.getSubCommOptionFormList();
        for( int i = 0 ; i < regCommOptFrmSubList.size() ; i++ )
        {
            if ( commOpt.trim().length() != 0 )
            {
                commOpt = commOpt + "<br />";
            }
            commOpt = commOpt + regCommOptFrmSubList.get( i ).getOptionNm();
        }
        frm.setCommOptions( commOpt );

        // 画面のデータとマージ
        for( int i = 0 ; i < allCommOptFrmSubList.size() ; i++ )
        {
            FormOwnerRsvPlanOptionSub optionSub = allCommOptFrmSubList.get( i );
            newFrmOptSub = new FormOwnerRsvPlanOptionSub();
            newFrmOptSub.setCheck( 0 );
            newFrmOptSub.setOptionID( optionSub.getOptionID() );
            newFrmOptSub.setOptionNm( optionSub.getOptionNm() );
            newFrmOptSub.setOptionSubNm( optionSub.getOptionSubNm() );

            for( int j = 0 ; j < frm.getComOptIdList().size() ; j++ )
            {
                if ( optionSub.getOptionID() == frm.getComOptIdList().get( j ) )
                {
                    newFrmOptSub.setCheck( 1 );
                    newFrmOptSub.setOptionID( optionSub.getOptionID() );
                    newFrmOptSub.setOptionNm( optionSub.getOptionNm() );
                    newFrmOptSub.setOptionSubNm( optionSub.getOptionSubNm() );
                    break;
                }
            }
            newCommOptFrmSubList.add( newFrmOptSub );
        }

        frm.setFrmMustOptionSubList( newMustOptFrmSubList );
        frm.setFrmCommOptionSubList( newCommOptFrmSubList );
    }

    /**
     * 適用部屋作成処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param logic LogicOwnerRsvPlanオブジェクト
     * @return なし
     */
    private void createRoomList(FormOwnerRsvPlan frm, LogicOwnerRsvPlan logic) throws Exception
    {
        FormOwnerRsvPlanSub logicFormSub;
        FormOwnerRsvPlanSub frmSub;
        FormOwnerRsvPlanSub newFrmSub;
        ArrayList<FormOwnerRsvPlanSub> frmSubList = new ArrayList<FormOwnerRsvPlanSub>();

        // プラン・部屋設定データ取得
        logic.setFrm( frm );
        logic.getNewPlanRoom();

        // プラン・部屋設定データ作成
        for( int i = 0 ; i < logic.getSubFormList().size() ; i++ )
        {
            // DBから取得したSeqのリスト
            logicFormSub = logic.getSubFormList().get( i );
            // 新しいリストに初期値設定
            newFrmSub = new FormOwnerRsvPlanSub();
            newFrmSub.setCheck( 0 );
            newFrmSub.setSeq( logicFormSub.getSeq() );
            newFrmSub.setRoomNm( logicFormSub.getRoomNm() );
            for( int j = 0 ; j < frm.getFrmSubList().size() ; j++ )
            {
                // 画面でセットされているSeqのリスト
                frmSub = frm.getFrmSubList().get( j );
                if ( logicFormSub.getSeq() == frmSub.getSeq() )
                {
                    newFrmSub.setCheck( 1 );
                    newFrmSub.setTekiyoDateFrom( frmSub.getTekiyoDateFrom() );
                    newFrmSub.setTekiyoDateTo( frmSub.getTekiyoDateTo() );
                    break;
                }
            }

            frmSubList.add( newFrmSub );
        }

        // フォームにセット
        frm.setFrmSubList( frmSubList );
    }

    /**
     * 販売フラグ設定
     * 
     * @param LogicOwnerRsvPlan ビジネスロジック
     * @return 更新後の販売フラグ
     */
    private int setSalesFlg(LogicOwnerRsvPlan logic, int salesFlag) throws Exception
    {
        int newSalesFlg = 0;

        try
        {
            if ( salesFlag == -1 )
            {
                // 対象データなし
                return(newSalesFlg);
            }
            else if ( salesFlag == 0 )
            {
                // 販売中止にする
                newSalesFlg = 1;
            }
            logic.setSalesFlg( newSalesFlg );

            return(newSalesFlg);
        }
        catch ( Exception e )
        {
            throw new Exception( "ActionOwnerRscPlan.setSalesFlg " + e.toString() );
        }
    }

    /**
     * 表示ボタンクリック
     * 
     * @param request HttpServletRequest
     * @param logic LogicOwnerRsvPlan オブジェクト
     * @param frm FormOwnerRsvPlanオブジェクト
     * @return なし
     */
    private FormOwnerRsvPlan execPlanView(HttpServletRequest request, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm) throws Exception
    {
        int selPlanId = 0;
        int salesFlg = 0;
        int baseSeq = 0;
        String mustOpt = "";
        String commOpt = "";
        String selPlanNm = "";
        ArrayList<Integer> selSeqList = new ArrayList<Integer>();
        ArrayList<FormOwnerRsvPlanSub> regFrmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        ArrayList<FormOwnerRsvPlanSub> newFrmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        FormOwnerRsvPlanSub frmPlanSub = new FormOwnerRsvPlanSub();

        // ▼選択されているプランIDを取得
        String[] plans = request.getParameterValues( "selPlan" );
        for( int i = 0 ; i < plans.length ; i++ )
        {
            selPlanId = Integer.parseInt( plans[i] );
        }
        frm.setSelPlanID( selPlanId );

        // プラン名を取得
        selPlanNm = logic.getDraftPlanNm( frm.getSelHotelID(), selPlanId );

        // ▼下書きプランリスト取得
        logic.setFrm( frm );
        logic.getDraftPlanIdList();
        logic.getDraftPlanNmList();

        // ▼登録済みプラン情報の取得
        logic.setFrmSub( frmPlanSub );
        logic.getPlan_PlanId( selPlanId, DRAFT_ON );
        salesFlg = logic.getSalesFlg();
        regFrmSubList = logic.getSubFormList();

        // ▼プラン・部屋設定データ取得
        logic.setSubFormList( new ArrayList<FormOwnerRsvPlanSub>() );
        logic.getNewPlanRoom();

        // ▼適用部屋のデータ設定
        for( int i = 0 ; i < logic.getSubFormList().size() ; i++ )
        {
            FormOwnerRsvPlanSub frmSub = logic.getSubFormList().get( i );
            baseSeq = frmSub.getSeq();

            for( int j = 0 ; j < regFrmSubList.size() ; j++ )
            {
                FormOwnerRsvPlanSub regFrmSub = regFrmSubList.get( j );
                if ( baseSeq == regFrmSub.getSeq() )
                {
                    frmSub.setCheck( 1 );
                    frmSub.setTekiyoDateFrom( regFrmSub.getTekiyoDateFrom() );
                    frmSub.setTekiyoDateTo( regFrmSub.getTekiyoDateTo() );
                }
            }
            newFrmSubList.add( frmSub );
        }

        // 選択済み管理番号リストの設定
        for( int i = 0 ; i < frm.getPlanSeq().size() ; i++ )
        {
            selSeqList.add( 0 );
        }

        // ▼必須オプション情報取得
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 1 );
        allMustOptFrmSubList = logic.getSubMustOptionFormList();

        // 登録済み必須オプションの取得
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getOption( 1, DRAFT_ON );
        regMustOptFrmSubList = logic.getSubMustOptionFormList();
        for( int i = 0 ; i < regMustOptFrmSubList.size() ; i++ )
        {
            if ( mustOpt.trim().length() != 0 )
            {
                mustOpt = mustOpt + "<br />";
            }
            mustOpt = mustOpt + regMustOptFrmSubList.get( i ).getOptionNm()
                    + "(" + regMustOptFrmSubList.get( i ).getOptionSubNm() + ")";
        }
        frm.setMustOptions( mustOpt );

        // データマージ
        for( int i = 0 ; i < allMustOptFrmSubList.size() ; i++ )
        {
            FormOwnerRsvPlanOptionSub optionSub = allMustOptFrmSubList.get( i );
            if ( optionSub.getErrMsg().trim().length() != 0 )
            {
                newMustOptFrmSubList.add( optionSub );
                break;
            }
            for( int j = 0 ; j < regMustOptFrmSubList.size() ; j++ )
            {
                FormOwnerRsvPlanOptionSub regOptFrmSub = regMustOptFrmSubList.get( j );
                if ( optionSub.getOptionID() == regOptFrmSub.getOptionID() )
                {
                    optionSub.setCheck( 1 );
                }
            }
            newMustOptFrmSubList.add( optionSub );
        }

        // ▼通常オプション情報取得
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 0 );
        allCommOptFrmSubList = logic.getSubCommOptionFormList();

        // 登録済み必須オプションの取得
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getOption( 0, DRAFT_ON );
        regCommOptFrmSubList = logic.getSubCommOptionFormList();
        for( int i = 0 ; i < regCommOptFrmSubList.size() ; i++ )
        {
            if ( commOpt.trim().length() != 0 )
            {
                commOpt = commOpt + "<br />";
            }
            commOpt = commOpt + regCommOptFrmSubList.get( i ).getOptionNm();
        }
        frm.setCommOptions( commOpt );

        // データマージ
        for( int i = 0 ; i < allCommOptFrmSubList.size() ; i++ )
        {
            FormOwnerRsvPlanOptionSub optionSub = allCommOptFrmSubList.get( i );
            if ( optionSub.getErrMsg().trim().length() != 0 )
            {
                newCommOptFrmSubList.add( optionSub );
                break;
            }
            for( int j = 0 ; j < regCommOptFrmSubList.size() ; j++ )
            {
                FormOwnerRsvPlanOptionSub regOptFrmSub = regCommOptFrmSubList.get( j );
                if ( optionSub.getOptionID() == regOptFrmSub.getOptionID() )
                {
                    optionSub.setCheck( 1 );
                }
            }
            newCommOptFrmSubList.add( optionSub );
        }

        // ▼フォームにセット
        frm = logic.getFrm();
        frm.setSalesFlag( salesFlg );
        if ( salesFlg == 0 )
        {
            frm.setSalesBtnValue( BTN_OFF );
        }
        else
        {
            frm.setSalesBtnValue( BTN_ON );
        }
        frm.setSelDraftPlanId( selPlanId );
        frm.setSelDraftPlanNm( selPlanNm );
        frm.setDraftMode( DRAFT_ON );
        frm.setFrmSubList( logic.getSubFormList() );
        frm.setFrmMustOptionSubList( newMustOptFrmSubList );
        frm.setFrmCommOptionSubList( newCommOptFrmSubList );
        frm.setExistsRsv( false );
        return(frm);
    }

    /**
     * 下書き保存ボタンクリック
     * 
     * @param id ホテルID
     * @param planID プランID
     * @param frm FormOwnerRsvPlanオブジェクト
     * @return なし
     */
    private boolean execDelDraft(int id, int planId, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm) throws Exception
    {
        boolean ret = false;
        String msg = "";

        // プラン未選択時はエラー
        if ( planId == 0 )
        {
            msg = msg + Message.getMessage( "warn.00002", "削除対象のプラン" ) + "<br />";
            frm.setErrMsg( msg );
            return(ret);
        }

        // 削除実行
        logic.execDelDraftPlan( id, planId );
        ret = true;

        return(ret);
    }

    /**
     * 下書きプラン設定更新ボタンクリック
     * 
     * @param id ホテルID
     * @param planID プランID
     * @param frm FormOwnerRsvPlanオブジェクト
     * @return なし
     */
    private boolean execRegDraftRelease(int id, int planId, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm) throws Exception
    {
        boolean ret = false;

        // 入力チェック
        if ( isInputCheckMsg( frm ) == false )
        {
            return(ret);
        }

        // プラン設定更新
        frm.setSalesFlag( 1 );
        logic.setFrm( frm );
        logic.registDraftPlan();
        ret = true;

        return(ret);
    }

}
