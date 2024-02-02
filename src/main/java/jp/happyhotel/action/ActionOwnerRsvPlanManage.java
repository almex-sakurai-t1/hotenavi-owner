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
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlan;
import jp.happyhotel.owner.FormOwnerRsvPlanManage;
import jp.happyhotel.owner.FormOwnerRsvPlanOptionSub;
import jp.happyhotel.owner.FormOwnerRsvPlanSub;
import jp.happyhotel.owner.LogicOwnerRsvManage;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlan;
import jp.happyhotel.owner.LogicOwnerRsvPlanManage;

/**
 * 
 * プラン設定管理画面
 */
public class ActionOwnerRsvPlanManage extends BaseAction
{
    private RequestDispatcher   requestDispatcher = null;
    private static final int    menuFlg_top       = 3;
    private static final int    menuFlg_new       = 31;

    private static final String BTN_ON            = "一時的に停止する";
    private static final String BTN_OFF           = "販売中にする";
    private static final int    BTN_ON_VALUE      = 0;

    // 押されたボタンの判別値
    private static final int    BTN_VIEW          = 1;
    private static final int    BTN_ADD           = 2;
    private static final int    BTN_VIEWCHANGE    = 3;
    private static final int    BTN_EDIT          = 4;
    private static final int    BTN_DRAFT         = 5;
    private static final int    BTN_NO_DISP       = 6;
    private static final int    BTN_PLAN_SALES    = 7;
    private static final int    BTN_DEL           = 8;

    // 下書きモード
    private static final int    DRAFT_ON          = 1;         // 下書き
    private static final int    DRAFT_OFF         = 0;         // 通常

    private static final int    RESERVE_CODE      = 1000007;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerRsvPlanManage frm;
        LogicOwnerRsvPlanManage logic;
        LogicOwnerRsvPlan logicPlan;
        FormOwnerRsvPlan frmPlan;
        FormOwnerRsvPlanSub frmPlanSub;
        int paramHotelID = 0;
        int paramUserId = 0;
        int paramViewMode = 0;
        int selBtn = 0;
        int menuFlg = 0;
        int imediaFlg = 0;
        int newViewMode = 0;
        String premiumGoAheadDays = "";
        String loginHotelId = "";
        String errMsg = "";
        String hotenaviId = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        DataMasterPoint dmp;

        try
        {
            frmMenu = new FormOwnerBkoMenu();
            frm = new FormOwnerRsvPlanManage();
            logic = new LogicOwnerRsvPlanManage();
            frmPlan = new FormOwnerRsvPlan();
            logicPlan = new LogicOwnerRsvPlan();
            frmPlanSub = new FormOwnerRsvPlanSub();

            // 画面の値を取得
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

            if ( request.getParameter( "planView" ) != null )
            {
                // 未掲載プランボタンクリック
                selBtn = BTN_VIEW;
            }
            else if ( request.getParameter( "planAdd" ) != null )
            {
                // 新規登録ボタンクリック
                selBtn = BTN_ADD;
            }
            else if ( request.getParameter( "viewChange" ) != null )
            {
                // 表示順変更ボタンクリック
                selBtn = BTN_VIEWCHANGE;
            }
            else if ( request.getParameter( "draftEdit" ) != null )
            {
                // 下書き編集ボタンクリック
                selBtn = BTN_DRAFT;
            }
            else if ( (request.getParameter( "planId" ) != null) && (Integer.parseInt( request.getParameter( "edit" ) ) == 1) )
            {
                // 編集リンククリック
                selBtn = BTN_EDIT;
            }
            else if ( (request.getParameter( "planId" ) != null) && (request.getParameter( "planNoDisp" ) != null) )
            {
                // 非掲載ボタンクリック
                selBtn = BTN_NO_DISP;
            }
            else if ( request.getParameter( "planSales" ) != null )
            {
                // 販売・停止ボタンクリック
                selBtn = BTN_PLAN_SALES;
            }
            else if ( (request.getParameter( "planId" ) != null) && (request.getParameter( "planDel" ) != null) )
            {
                // 非掲載ボタンクリック
                selBtn = BTN_DEL;
            }

            // 値をフォームにセット
            frm.setSelHotelId( paramHotelID );
            frm.setUserId( paramUserId );
            frmPlan.setMode( selBtn );
            frmPlan.setDraftMode( DRAFT_OFF );

            // メニュー、予約情報の設定
            frmMenu.setUserId( paramUserId );

            // 事務局か取得する
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

            // プレミアム先行予約日数取得
            premiumGoAheadDays = OwnerRsvCommon.getPremiumGoAheadDays();
            frmPlan.setPremiumGoAheadDays( premiumGoAheadDays );

            // プラン編集等に反映させるためにセット（2013/01/07）
            dmp = new DataMasterPoint();
            dmp.getData( RESERVE_CODE );
            frmPlan.setDefaultPoint( dmp.getAddPoint() );
            frmPlan.setImediaFlg( imediaflag );

            switch( selBtn )
            {
                case BTN_ADD:
                    // 新規登録ボタンクリック時
                    menuFlg = menuFlg_new;
                    frmPlan.setSelHotelID( paramHotelID );
                    setNewPage( frmPlan, frmPlanSub, logicPlan );
                    request.setAttribute( "FORM_Plan", frmPlan );
                    break;

                case BTN_EDIT:
                    // 編集リンククリック時
                    menuFlg = menuFlg_new;
                    frmPlan.setSelHotelID( paramHotelID );
                    frmPlan.setSelPlanID( Integer.parseInt( request.getParameter( "planId" ) ) );
                    setEditPage( frmPlan, frmPlanSub, logicPlan, Integer.parseInt( request.getParameter( "planId" ) ) );
                    request.setAttribute( "FORM_Plan", frmPlan );
                    break;

                case BTN_VIEWCHANGE:
                    // 表示順変更ボタンクリック時
                    changeDispIndex( frm, logic, request );
                    menuFlg = menuFlg_top;
                    request.setAttribute( "FORM_PlanManage", frm );
                    break;

                case BTN_DRAFT:
                    // 下書き編集ボタンクリック時
                    menuFlg = menuFlg_new;
                    frmPlan.setSelHotelID( paramHotelID );
                    setDraftEditPage( frmPlan, frmPlanSub, logicPlan );
                    request.setAttribute( "FORM_Plan", frmPlan );
                    break;

                case BTN_NO_DISP:

                    // プランIDの削除前に、予約データのチェックを行う
                    // 予約が入っていないか？
                    if ( logicPlan.existReserve( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ) ) == false )
                    {
                        if ( logicPlan.noPublishPlan( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ) ) == false )
                        {
                            errMsg = Message.getMessage( "warn.30044", "プラン非掲載エラー" );
                        }
                    }
                    else
                    {
                        // 予約データがあるため、削除できないエラーをセット
                        errMsg = Message.getMessage( "warn.30043", "予約ありエラー" );
                    }
                    // 再表示
                    frm.setErrMsg( errMsg );
                    logic.setFrm( frm );
                    logic.getPlan( 1 );
                    frm = logic.getFrm();
                    frm.setViewMode( 1 );

                    // プラン設定
                    menuFlg = menuFlg_top;
                    request.setAttribute( "FORM_PlanManage", frm );

                    break;

                case BTN_PLAN_SALES:
                    paramViewMode = Integer.parseInt( request.getParameter( "view" ).toString() );
                    updPlanSalesFlg( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ), Integer.parseInt( request.getParameter( "salesFlag" ) ), paramUserId );
                    logic.setFrm( frm );
                    logic.getPlan( 1 );
                    frm = logic.getFrm();
                    frm.setViewMode( 1 );

                    // プラン設定
                    menuFlg = menuFlg_top;
                    request.setAttribute( "FORM_PlanManage", frm );

                    break;

                case BTN_DEL:

                    // プランIDの削除前に、予約データのチェックを行う
                    // 予約が入っていないか？
                    if ( logicPlan.existReserve( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ) ) == false )
                    {
                        if ( logicPlan.delPublishPlan( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ) ) == false )
                        {
                            errMsg = Message.getMessage( "warn.30044", "プラン削除エラー" );
                        }
                    }
                    else
                    {
                        // 予約データがあるため、削除できないエラーをセット
                        errMsg = Message.getMessage( "warn.30043", "予約ありエラー" );
                    }
                    // 再表示
                    frm.setErrMsg( errMsg );
                    logic.setFrm( frm );
                    logic.getPlan( 0 );// 期間外のみ削除のため
                    frm = logic.getFrm();
                    frm.setViewMode( 1 );

                    // プラン設定
                    menuFlg = menuFlg_top;
                    request.setAttribute( "FORM_PlanManage", frm );

                    break;

                default:
                    // 未掲載ボタンクリック時
                    paramViewMode = Integer.parseInt( request.getParameter( "viewMode" ).toString() );
                    menuFlg = menuFlg_top;
                    frm.setSelHotelId( paramHotelID );
                    logic.setFrm( frm );
                    if ( paramViewMode == OwnerRsvCommon.PLAN_VIEW_PART )
                    {
                        // 未掲載プランも表示する
                        logic.getPlan( OwnerRsvCommon.PLAN_VIEW_ALL );
                        newViewMode = OwnerRsvCommon.PLAN_VIEW_ALL;
                    }
                    else
                    {
                        // 未掲載プランは表示しない
                        logic.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
                        newViewMode = OwnerRsvCommon.PLAN_VIEW_PART;
                    }
                    frm.setViewMode( newViewMode );
                    request.setAttribute( "FORM_PlanManage", frm );
                    break;
            }

            // 画面遷移
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
            request.setAttribute( "FORM_Menu", frmMenu );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanManage.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvPlanManage.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logicPlan = null;
        }
    }

    /**
     * 新規登録ページへ遷移するための画面設定処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param logic LogicOwnerRsvPlanオブジェクト
     * @return なし
     */
    private void setNewPage(FormOwnerRsvPlan frm, FormOwnerRsvPlanSub frmPlanSub, LogicOwnerRsvPlan logic) throws Exception
    {
        int hapiPoint = 0;
        String salesEndDate = "";
        ArrayList<Integer> selSeqList = new ArrayList<Integer>();

        // プラン・部屋設定データ取得
        logic.setFrm( frm );
        logic.setFrmSub( frmPlanSub );
        logic.getNewPlanRoom();

        // ハピー付与の初期値
        hapiPoint = OwnerRsvCommon.getInitHapyPoint( 1 );
        frm.setDefaultPoint( hapiPoint );
        if ( frm.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
        {
            frm.setPointKbn( OwnerRsvCommon.POINT_KIND_FIX );
            frm.setPointFix( Integer.toString( hapiPoint ) );
        }

        // 必須オプションの設定
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 1 );
        frm.setMustOptions( "" );

        // 通常オプションの設定
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 0 );
        frm.setCommOptions( "" );

        // 部屋指定の初期値
        frm.setOfferKbn( 2 );

        // プランの掲載
        frm.setPublishingFlg( 1 );

        // 要望入力項目表示
        frm.setUserRequestFlg( 0 );

        // 選択済み管理番号リストの設定
        for( int i = 0 ; i < frm.getPlanSeq().size() ; i++ )
        {
            selSeqList.add( 0 );
        }

        // 料金情報取得
        logic.getPlanCharge( frm );

        // フォームにセット
        frm.setDispIndex( "" );
        frm.setSalesBtnValue( BTN_OFF );
        frm.setSalesFlag( BTN_ON_VALUE );
        frm.setSalesEndDate( salesEndDate );
        frm.setFrmSubList( logic.getSubFormList() );
        frm.setFrmMustOptionSubList( logic.getSubMustOptionFormList() );
        frm.setFrmCommOptionSubList( logic.getSubCommOptionFormList() );
    }

    /**
     * プラン変更ページへ遷移するための画面設定処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param logic LogicOwnerRsvPlanオブジェクト
     * @param planId プランID
     * @return なし
     */
    private void setEditPage(FormOwnerRsvPlan frm, FormOwnerRsvPlanSub frmPlanSub, LogicOwnerRsvPlan logic, int planId) throws Exception
    {
        int salesFlg = 0;
        int baseSeq = 0;
        String mustOpt = "";
        String commOpt = "";
        boolean isExistsRsv = false;
        ArrayList<Integer> selSeqList = new ArrayList<Integer>();
        ArrayList<FormOwnerRsvPlanSub> regFrmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        ArrayList<FormOwnerRsvPlanSub> newFrmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();

        // ▼登録済みプラン情報の取得
        logic.setFrm( frm );
        logic.setFrmSub( frmPlanSub );
        logic.getPlan_PlanId( frm.getSelPlanID(), DRAFT_OFF );
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
        logic.getOption( 1, DRAFT_OFF );
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
        logic.getOption( 0, DRAFT_OFF );
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

        // ▼料金情報取得
        logic.getPlanCharge( frm );

        // ▼予約データが存在するか
        isExistsRsv = OwnerRsvCommon.isExistsRsvPlan( frm.getSelHotelID(), frm.getSelPlanID() );
        frm.setExistsRsv( isExistsRsv );
        if ( isExistsRsv == true )
        {
            frm.setInfoMsg( Message.getMessage( "warn.30019" ) );
        }

        // ▼フォームにセット
        frm.setSalesFlag( salesFlg );
        if ( salesFlg == 0 )
        {
            frm.setSalesBtnValue( BTN_OFF );
        }
        else
        {
            frm.setSalesBtnValue( BTN_ON );
        }
        frm.setFrmSubList( logic.getSubFormList() );
        frm.setFrmMustOptionSubList( newMustOptFrmSubList );
        frm.setFrmCommOptionSubList( newCommOptFrmSubList );
    }

    /**
     * 表示順変更処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param logic LogicOwnerRsvPlanManageオブジェクト
     * @param req HttpServletRequest
     * @return なし
     */
    private void changeDispIndex(FormOwnerRsvPlanManage frm, LogicOwnerRsvPlanManage logic, HttpServletRequest req) throws Exception
    {

        ArrayList<Integer> planIdList = new ArrayList<Integer>();
        ArrayList<String> planNmList = new ArrayList<String>();
        ArrayList<String> dispIdxList = new ArrayList<String>();
        String msg = "";

        // 画面のプランIDの取得
        String planIds[] = req.getParameterValues( "planId" );
        String planNms[] = req.getParameterValues( "planNm" );
        if ( planIds != null )
        {
            for( int i = 0 ; i < planIds.length ; i++ )
            {
                planIdList.add( Integer.parseInt( planIds[i] ) );
                planNmList.add( planNms[i] );
            }
        }

        // 画面の表示順の取得
        String dispIdxs[] = req.getParameterValues( "dispIndex" );
        if ( dispIdxs != null )
        {
            for( int i = 0 ; i < dispIdxs.length ; i++ )
            {
                dispIdxList.add( dispIdxs[i] );
            }
        }

        // 入力値チェック
        msg = checkInputValue( dispIdxList, planNmList );
        if ( msg.trim().length() == 0 )
        {
            // 更新処理
            msg = "";
            for( int i = 0 ; i < dispIdxList.size() ; i++ )
            {
                if ( logic.changeDispIdx( frm.getSelHotelId(), planIdList.get( i ), Integer.parseInt( dispIdxList.get( i ) ), frm.getUserId() ) == false )
                {
                    // 更新失敗
                    msg = msg + Message.getMessage( "erro.30001", "更新対象" );
                }
            }
        }

        // 再表示
        frm.setErrMsg( msg );
        logic.setFrm( frm );
        logic.getPlan( 1 );
        frm = logic.getFrm();
        frm.setViewMode( 1 );
    }

    /**
     * 下書き編集画面遷移するための画面設定処理
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @param logic LogicOwnerRsvPlanオブジェクト
     * @return なし
     */
    private void setDraftEditPage(FormOwnerRsvPlan frm, FormOwnerRsvPlanSub frmPlanSub, LogicOwnerRsvPlan logic) throws Exception
    {
        int hapiPoint = 0;
        String salesEndDate = "";
        ArrayList<Integer> selSeqList = new ArrayList<Integer>();

        // 下書きモード設定
        frm.setDraftMode( DRAFT_ON );

        // 下書きプラン情報取得
        logic.setFrm( frm );
        logic.getDraftPlanIdList();
        logic.getDraftPlanNmList();

        // プラン・部屋設定データ取得
        logic.setFrmSub( frmPlanSub );
        logic.getNewPlanRoom();

        // ハピー付与の初期値
        hapiPoint = OwnerRsvCommon.getInitHapyPoint( 1 );
        frm.setDefaultPoint( hapiPoint );
        if ( frm.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
        {
            frm.setPointKbn( OwnerRsvCommon.POINT_KIND_FIX );
            frm.setPointFix( Integer.toString( hapiPoint ) );
        }

        // 必須オプションの設定
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 1 );
        frm.setMustOptions( "" );

        // 通常オプションの設定
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 0 );
        frm.setCommOptions( "" );

        // 部屋指定の初期値
        frm.setOfferKbn( 2 );

        // プランの掲載
        frm.setPublishingFlg( 1 );

        // 要望入力項目表示
        frm.setUserRequestFlg( 0 );

        // 選択済み管理番号リストの設定
        for( int i = 0 ; i < frm.getPlanSeq().size() ; i++ )
        {
            selSeqList.add( 0 );
        }

        // フォームにセット
        frm.setDispIndex( "" );
        /*
         * frm.setSalesBtnValue( BTN_OFF );
         * frm.setSalesFlag( BTN_ON_VALUE );
         */
        frm.setSalesEndDate( salesEndDate );
        frm.setFrmSubList( logic.getSubFormList() );
        frm.setFrmMustOptionSubList( logic.getSubMustOptionFormList() );
        frm.setFrmCommOptionSubList( logic.getSubCommOptionFormList() );
    }

    /**
     * 表示順変更時の入力値チェック
     * 
     * @param dispIdxList 画面で入力された表示順
     * @param plnNmList プラン名
     * @return エラーメッセージ
     */
    private String checkInputValue(ArrayList<String> dispIdxList, ArrayList<String> plnNmList) throws Exception
    {
        String msg = "";

        for( int i = 0 ; i < dispIdxList.size() ; i++ )
        {
            if ( CheckString.onlySpaceCheck( dispIdxList.get( i ) ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "【" + plnNmList.get( i ) + "】の" + "表示順" ) + "<br />";
            }
            else
            {

                if ( (OwnerRsvCommon.numCheck( dispIdxList.get( i ) ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "【" + plnNmList.get( i ) + "】の" + "表示順", "1以上の整数" ) + "<br />";
                }
            }
        }

        return(msg);
    }

    /**
     * プラン状況の販売フラグ更新
     * 
     * @param logic LogicOwnerRsvPlanManageオブジェクト
     * @para, hotelId ホテルID
     * @param planId プランID
     * @param salesFlg 販売フラグ
     * @param userId ユーザーID
     * @return なし
     */
    private void updPlanSalesFlg(int hotelId, int planId, int salesFlg, int userId) throws Exception
    {
        LogicOwnerRsvManage logic = new LogicOwnerRsvManage();
        int newSalesFlg = 0;

        try
        {
            // 販売フラグの更新
            if ( salesFlg == 0 )
            {
                newSalesFlg = 1;
            }
            logic.updPlanSalesFlg( hotelId, planId, newSalesFlg, userId );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updRoomSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updRoomSalesFlg()] " + exception );
        }
    }

}
