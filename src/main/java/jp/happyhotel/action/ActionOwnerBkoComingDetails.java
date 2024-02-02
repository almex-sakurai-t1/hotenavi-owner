package jp.happyhotel.action;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.owner.FormOwnerBkoBillToday;
import jp.happyhotel.owner.FormOwnerBkoComingDetails;
import jp.happyhotel.owner.FormOwnerBkoComingToday;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoBillToday;
import jp.happyhotel.owner.LogicOwnerBkoComingDetails;
import jp.happyhotel.owner.LogicOwnerBkoComingToday;

public class ActionOwnerBkoComingDetails extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_DETAIL       = 101;
    private static final int  MENU_LIST         = 100;
    private static final int  MENU_SEIKYU       = 130;
    private static final int  listmax           = 20;  // ハピホテマイル履歴 一画面最大明細表示件数

    // 呼び出し元画面
    private static final int  CALLPAGE_HAPIHOTE = 1;   // ハピホテマイル履歴
    private static final int  CALLPAGE_SYUSHI   = 2;   // 収支明細

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoComingDetails frmDetail;
        FormOwnerBkoComingToday frmToday;
        FormOwnerBkoBillToday frmBillToday;
        LogicOwnerBkoComingDetails logic;
        LogicOwnerBkoComingToday logicToday;
        String paramHotenaviId = "";
        String paramOccur2 = "";
        String paramOccur3 = "";
        int menuFlg = 0;
        String ciTime = "";
        int imediaFlg = 0;
        int billOwnFlg = 0;
        String errMsg = "";
        String hotenaviId = "";
        int hotelId = 0;

        try
        {
            logic = new LogicOwnerBkoComingDetails();
            frmDetail = new FormOwnerBkoComingDetails();
            frmMenu = new FormOwnerBkoMenu();
            frmToday = new FormOwnerBkoComingToday();
            logicToday = new LogicOwnerBkoComingToday();

            // ▼画面の値を取得
            paramHotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( request.getParameter( "selYear" ) != null )
            {
                paramOccur2 = request.getParameter( "occur2" );
            }
            if ( request.getParameter( "occur3" ) != null )
            {
                paramOccur3 = request.getParameter( "occur3" );
            }
            frmDetail = getRequestParam( frmDetail, request );

            // ログインユーザと担当ホテルのチェック
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            frmDetail.setHotenaviID( hotenaviId );
            if ( (frmDetail.getSelHotelID() != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, frmDetail.getUserId(), frmDetail.getSelHotelID() ) == false) )
            {
                // 管理外のホテルはログイン画面へ遷移
                response.sendRedirect( "../../owner/index.jsp" );
                return;
            }

            // 事務局フラグ
            imediaFlg = OwnerRsvCommon.getImediaFlag( paramHotenaviId, frmDetail.getUserId() );
            frmDetail.setImediaFlg( imediaFlg );

            // 請求閲覧可能フラグ
            billOwnFlg = OwnerBkoCommon.getBillOwnFlg( paramHotenaviId, frmDetail.getUserId() );
            frmDetail.setBillOwnFlg( billOwnFlg );

            if ( request.getParameter( "btnUpdate" ) != null )
            {
                // ▼更新処理
                // 入力チェック
                if ( checkInput( frmDetail, 1 ) == false )
                {
                    // エラーあり
                    getDetailData( request, logic, frmDetail );
                    menuFlg = MENU_DETAIL;
                    request.setAttribute( "FORM_comDetail", frmDetail );
                }
                else
                {
                    // 更新
                    if ( (paramOccur3 != null) && (paramOccur3.trim().length() != 0) )
                    {
                        frmDetail.sethWaribiki( Integer.parseInt( paramOccur3 ) );
                    }
                    if ( (paramOccur2 != null) && (paramOccur2.trim().length() != 0) )
                    {
                        frmDetail.sethSeisan( Integer.parseInt( paramOccur2 ) );
                    }
                    logic.setFrm( frmDetail );
                    if ( frmDetail.getClosingKind() == OwnerBkoCommon.CLOSING_KBN_HON )
                    {
                        // 本締めデータの場合
                        logic.registAccountRecvHon();
                    }
                    else
                    {
                        // 本締め以外の場合
                        logic.registAccountRecv();
                    }

                    // 画面内容取得
                    frmDetail = logic.getFrm();
                    frmDetail.setAccrecvSlipNo( frmDetail.getNewAccSLipNo() );
                    logic.setFrm( frmDetail );
                    logic.getAccountRecv();
                    logic.existsBillData();
                    frmDetail = logic.getFrm();
                    menuFlg = MENU_DETAIL;
                    request.setAttribute( "FORM_comDetail", frmDetail );
                }
            }
            else if ( request.getParameter( "btnDetail" ) != null )
            {
                // ▼来店明細表示
                // 画面内容取得
                logic.setFrm( frmDetail );
                logic.getAccountRecv();
                logic.existsBillData();
                frmDetail = logic.getFrm();
                menuFlg = MENU_DETAIL;
                request.setAttribute( "FORM_comDetail", frmDetail );

            }
            else if ( request.getParameter( "btnAddInput" ) != null )
            {
                // ▼来店追加入力更新ボタン
                Logging.error( "追加" );
                // 入力チェック
                if ( checkInput( frmDetail, 0 ) == false )
                {
                    // エラーあり
                    menuFlg = MENU_DETAIL;
                    request.setAttribute( "FORM_comDetail", frmDetail );
                }
                else
                {
                    // 追加
                    frmDetail.setInpUsageDate( Integer.parseInt( frmDetail.getSelYear() + frmDetail.getSelMonth() + frmDetail.getSelDate() ) );
                    frmDetail.sethWaribiki( Integer.parseInt( paramOccur3 ) );

                    // 担当者名取得
                    frmDetail.setInpPersonNm( "担当　太郎" );
                    logic.setFrm( frmDetail );
                    logic.addAccountRecv();

                    // 一覧データ取得
                    menuFlg = MENU_LIST;
                    frmToday = new FormOwnerBkoComingToday();
                    frmToday.setSelHotelID( frmDetail.getSelHotelID() );

                    // システム日付のセット
                    Calendar cal = Calendar.getInstance();
                    frmToday.setYearFrom( cal.get( Calendar.YEAR ) );
                    frmToday.setMonthFrom( cal.get( Calendar.MONTH ) + 1 );
                    frmToday.setDateFrom( cal.get( Calendar.DATE ) );
                    frmToday.setYearTo( cal.get( Calendar.YEAR ) );
                    frmToday.setMonthTo( cal.get( Calendar.MONTH ) + 1 );
                    frmToday.setDateTo( cal.get( Calendar.DATE ) );
                    ciTime = Integer.toString( cal.get( Calendar.YEAR ) ) + String.format( "%1$02d", cal.get( Calendar.MONTH ) + 1 ) + String.format( "%1$02d", cal.get( Calendar.DATE ) );
                    frmToday.setCiTimeFrom( Integer.parseInt( ciTime ) );
                    frmToday.setCiTimeTo( Integer.parseInt( ciTime ) );

                    logicToday.setFrm( frmToday );
                    logicToday.getAccountRecv();
                    logicToday.getAccountRecvDetail();
                    request.setAttribute( "FORM_comToday", frmToday );
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // ▼戻る
                switch( frmDetail.getCallPage() )
                {
                    case CALLPAGE_HAPIHOTE:
                        // ハピホテマイル履歴へ戻る
                        menuFlg = MENU_LIST;
                        frmToday = new FormOwnerBkoComingToday();
                        frmToday = setComingToday( frmToday, frmDetail );
                        request.setAttribute( "FORM_comToday", frmToday );
                        break;
                    case CALLPAGE_SYUSHI:
                        // 収支明細へ戻る
                        menuFlg = MENU_SEIKYU;
                        frmBillToday = new FormOwnerBkoBillToday();
                        frmBillToday = setBillToday( frmBillToday, frmDetail );
                        request.setAttribute( "FORM_billToday", frmBillToday );
                        break;
                }
            }

            // メニューの設定
            frmMenu.setUserId( frmDetail.getUserId() );
            OwnerRsvCommon.setMenu( frmMenu, frmDetail.getSelHotelID(), menuFlg, request.getCookies() );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoComingDetails.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoComingDetails.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * 前画面の内容を取得
     * 
     * @param frm FormOwnerBkoComingDetailsオブジェクト
     * @param request HttpServletRequestオブジェクト
     * @return FormOwnerBkoComingDetailsオブジェクト
     */
    private FormOwnerBkoComingDetails getRequestParam(FormOwnerBkoComingDetails frm, HttpServletRequest request)
    {
        int paramHotelID = 0;
        int paramAccrecvSlipNo = 0;
        int paramUserId = 0;
        String paramUseAmount = "";
        String paramReserveAmount = "";
        String paramOccur2 = "";
        String paramOccur3 = "";
        String paramYear = "";
        String paramMonth = "";
        String paramDate = "";
        String paramRoomNo = "";
        int paramClosingFlg = 0;
        int paramModeFlg = 0;
        int paramCallPage = 0;
        String paramSelYearFrom = "";
        String paramSelMonthFrom = "";
        String paramSelDateFrom = "";
        String paramSelYearTo = "";
        String paramSelMonthTo = "";
        String paramSelDateTo = "";
        int paramPage = 0;

        if ( request.getParameter( "selHotelIDValue" ) != null )
        {
            paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
        }
        paramAccrecvSlipNo = Integer.parseInt( request.getParameter( "slipNo" ).toString() );
        paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
        paramUseAmount = request.getParameter( "use_amount" );
        paramReserveAmount = request.getParameter( "reserve_amount" );
        paramModeFlg = Integer.parseInt( request.getParameter( "modeFlg" ) );
        if ( request.getParameter( "occur2" ) != null )
        {
            paramOccur2 = request.getParameter( "occur2" );
        }
        if ( request.getParameter( "occur3" ) != null )
        {
            paramOccur3 = request.getParameter( "occur3" );
        }
        if ( request.getParameter( "selYear" ) != null )
        {
            paramYear = request.getParameter( "selYear" );
        }
        if ( request.getParameter( "selMonth" ) != null )
        {
            paramMonth = String.format( "%02d", Integer.parseInt( request.getParameter( "selMonth" ) ) );
        }
        if ( request.getParameter( "selDate" ) != null )
        {
            paramDate = String.format( "%02d", Integer.parseInt( request.getParameter( "selDate" ) ) );
        }
        if ( request.getParameter( "htRoomNm" ) != null )
        {
            paramRoomNo = request.getParameter( "htRoomNm" );
        }
        if ( request.getParameter( "closingFlg" ) != null )
        {
            paramClosingFlg = Integer.parseInt( request.getParameter( "closingFlg" ) );
        }
        if ( request.getParameter( "callPage" ) != null )
        {
            paramCallPage = Integer.parseInt( request.getParameter( "callPage" ) );
        }
        if ( request.getParameter( "selYearFrom" ) != null )
        {
            paramSelYearFrom = request.getParameter( "selYearFrom" );
        }
        if ( request.getParameter( "selMonthFrom" ) != null )
        {
            paramSelMonthFrom = String.format( "%02d", Integer.parseInt( request.getParameter( "selMonthFrom" ) ) );
        }
        if ( (request.getParameter( "selDateFrom" ) != null) && (request.getParameter( "selDateFrom" ).toString().trim().length() != 0) )
        {
            paramSelDateFrom = String.format( "%02d", Integer.parseInt( request.getParameter( "selDateFrom" ) ) );
        }
        if ( (request.getParameter( "selYearTo" ) != null) && (request.getParameter( "selYearTo" ).toString().trim().length() != 0) )
        {
            paramSelYearTo = request.getParameter( "selYearTo" );
        }
        if ( (request.getParameter( "selMonthTo" ) != null) && (request.getParameter( "selMonthTo" ).toString().trim().length() != 0) )
        {
            paramSelMonthTo = String.format( "%02d", Integer.parseInt( request.getParameter( "selMonthTo" ) ) );
        }
        if ( (request.getParameter( "selDateTo" ) != null) && (request.getParameter( "selDateTo" ).toString().trim().length() != 0) )
        {
            paramSelDateTo = String.format( "%02d", Integer.parseInt( request.getParameter( "selDateTo" ) ) );
        }
        if ( request.getParameter( "page" ) != null )
        {
            paramPage = Integer.parseInt( request.getParameter( "page" ).toString() );
        }

        // 値をフォームにセット
        frm.setSelHotelID( paramHotelID );
        frm.setUserId( paramUserId );
        frm.setAccrecvSlipNo( paramAccrecvSlipNo );
        frm.setUsageCharge( paramUseAmount );
        frm.setReceiveCharge( paramReserveAmount );
        frm.sethWaribiki_Inp( paramOccur3 );
        frm.sethWaribiki_View( paramOccur3 );
        frm.setSelYear( paramYear );
        frm.setSelMonth( paramMonth );
        frm.setSelDate( paramDate );
        frm.setInpHtRoomNo( paramRoomNo );
        frm.setInpUsageCharge( paramUseAmount );
        frm.setInpRsvCharge( paramReserveAmount );
        frm.setInpWaribiki( paramOccur3 );
        frm.sethSeisan_Inp( paramOccur2 );
        frm.sethSeisan_View( paramOccur2 );
        if ( paramOccur2 == null || paramOccur2.trim().length() == 0 )
        {
            frm.sethSeisan( 0 );
        }
        else
        {
            frm.sethSeisan( Integer.parseInt( paramOccur2 ) );
        }
        frm.setModeFlg( paramModeFlg );
        frm.setClosingKind( paramClosingFlg );
        frm.setCallPage( paramCallPage );
        frm.setSelYearFrom( paramSelYearFrom );
        frm.setSelMonthFrom( paramSelMonthFrom );
        frm.setSelDateFrom( paramSelDateFrom );
        frm.setSelYearTo( paramSelYearTo );
        frm.setSelMonthTo( paramSelMonthTo );
        frm.setSelDateTo( paramSelDateTo );
        frm.setPage( paramPage );

        return frm;

    }

    /**
     * 入力チェック
     * 
     * @param frm FormOwnerRsvPlanChargeオブジェクト
     * @param chkMode int チェックモード(0:来店追加、1:来店明細)
     * @return true:入力チェックOK、false:入力チェックエラー
     */
    private boolean checkInput(FormOwnerBkoComingDetails frm, int chkMode) throws Exception
    {

        boolean isCheck = false;
        String msg = "";
        int billDate = 0;
        LogicOwnerBkoComingDetails logic = new LogicOwnerBkoComingDetails();
        int billDateFrom = 0;
        int billDateTo = 0;
        int targetDate = 0;
        String billDateFromStr = "";
        String billDateToStr = "";

        if ( chkMode == 0 )
        {
            // 部屋
            if ( CheckString.onlySpaceCheck( frm.getInpHtRoomNo() ) == true )
            {
                // 未入力・空白文字の場合
                msg = msg + Message.getMessage( "warn.00001", "部屋" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frm.getInpHtRoomNo() ) == false) )
                {
                    // 半角数字以外が入力されている場合
                    msg = msg + Message.getMessage( "warn.30007", "部屋", "0以上の数値" ) + "<br />";
                }
            }

            // 利用日の整合性チェック
            DateFormat format = DateFormat.getDateInstance();
            // 日付/時刻解析を厳密に行うかどうかを設定する。
            format.setLenient( false );
            try
            {
                format.parse( frm.getSelYear() + "/" + frm.getSelMonth() + "/" + frm.getSelDate() );

            }
            catch ( Exception e )
            {
                msg = msg + Message.getMessage( "warn.00009", "利用日" ) + "<br />";
            }

            // 対象年月取得
            billDate = logic.getClosingCntrlDate();
            Calendar cal = Calendar.getInstance();
            cal.set( Integer.parseInt( Integer.toString( billDate ).substring( 0, 4 ) ),
                    Integer.parseInt( Integer.toString( billDate ).substring( 4, 6 ) ),
                    OwnerBkoCommon.SIME_DATE );
            billDateTo = Integer.parseInt( Integer.toString( cal.get( Calendar.YEAR ) )
                    + String.format( "%1$02d", cal.get( Calendar.MONTH ) )
                    + Integer.toString( OwnerBkoCommon.SIME_DATE )
                    );
            // 先月の取得
            cal.add( Calendar.MONTH, -1 );
            billDateFrom = Integer.parseInt( Integer.toString( cal.get( Calendar.YEAR ) )
                    + String.format( "%1$02d", cal.get( Calendar.MONTH ) )
                    + Integer.toString( OwnerBkoCommon.SIME_DATE + 1 )
                    );
            targetDate = Integer.parseInt( frm.getSelYear() + frm.getSelMonth() + frm.getSelDate() );
            if ( (billDateFrom > targetDate) || (billDateTo < targetDate) )
            {
                billDateFromStr = Integer.toString( billDateFrom ).substring( 0, 4 ) + "年"
                        + Integer.toString( billDateFrom ).substring( 4, 6 ) + "月"
                        + Integer.toString( OwnerBkoCommon.SIME_DATE + 1 ) + "日";
                billDateToStr = Integer.toString( billDateTo ).substring( 0, 4 ) + "年"
                        + Integer.toString( billDateTo ).substring( 4, 6 ) + "月"
                        + Integer.toString( OwnerBkoCommon.SIME_DATE ) + "日";
                msg = msg + Message.getMessage( "warn.30034", billDateFromStr, billDateToStr ) + "<br />";
            }
        }

        // 予約金額
        if ( CheckString.onlySpaceCheck( frm.getReceiveCharge() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "予約金額" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getReceiveCharge() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "予約金額", "0以上の整数" ) + "<br />";
            }
        }

        // 使用
        if ( CheckString.onlySpaceCheck( frm.gethWaribiki_Inp() ) == true )
        {
            // 未入力・空白文字の場合
            msg = msg + Message.getMessage( "warn.00001", "使用" ) + "<br />";
        }
        else
        {
            if ( (CheckString.numCheck( frm.gethWaribiki_Inp() ) == false) )
            {
                // 半角数字以外が入力されている場合
                msg = msg + Message.getMessage( "warn.30007", "使用", "0以下の整数" ) + "<br />";
            }
            else
            {
                // 0より小さい値が入力されている場合
                if ( Integer.parseInt( frm.gethWaribiki_Inp() ) < 0 )
                {
                    msg = msg + Message.getMessage( "warn.30007", "使用", "0以上の整数" ) + "<br />";
                }
            }
        }

        // 本締めデータの場合、仮データを締め処理が行われているか。
        if ( frm.getClosingKind() == OwnerBkoCommon.CLOSING_KBN_HON )
        {
            if ( logic.existsClosingCtrlKari() == false )
            {
                // 仮データなし時はエラー
                msg = msg + Message.getMessage( "erro.30012" );
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
     * 来店明細データ取得
     * 
     * @param HttpServletRequest
     * @param logic LogicOwnerBkoComingDetailsオブジェクト
     * @param frm FormOwnerBkoComingDetailsオブジェクト
     * @return なし
     */
    private void getDetailData(HttpServletRequest request, LogicOwnerBkoComingDetails logic, FormOwnerBkoComingDetails frm) throws Exception
    {
        // データ再検索
        logic.setFrm( frm );
        logic.getAccountRecv();

        // 入力値をセット
        frm.setUsageCharge( request.getParameter( "use_amount" ) );
        frm.setReceiveCharge( request.getParameter( "reserve_amount" ) );
        frm.sethWaribiki_Inp( request.getParameter( "occur3" ) );
        frm.sethWaribiki( Integer.parseInt( request.getParameter( "occur3" ) ) );
    }

    /**
     * ハピホテマイル履歴画面を表示
     * 
     * @param frm FormOwnerBkoComingTodayオブジェクト
     * @param frmDetail FormOwnerBkoComingDetailsオブジェクト
     * @return FormOwnerBkoComingTodayオブジェクト
     * @throws Exception
     */
    private FormOwnerBkoComingToday setComingToday(FormOwnerBkoComingToday frm, FormOwnerBkoComingDetails frmDetail) throws Exception
    {
        String ciTime = "";
        String ciTimeTo = "";
        LogicOwnerBkoComingToday logic = new LogicOwnerBkoComingToday();

        try
        {
            frm.setSelHotelID( frmDetail.getSelHotelID() );
            frm.setYearFrom( Integer.parseInt( frmDetail.getSelYearFrom() ) );
            frm.setMonthFrom( Integer.parseInt( frmDetail.getSelMonthFrom() ) );
            frm.setDateFrom( Integer.parseInt( frmDetail.getSelDateFrom() ) );
            frm.setYearTo( Integer.parseInt( frmDetail.getSelYearTo() ) );
            frm.setMonthTo( Integer.parseInt( frmDetail.getSelMonthTo() ) );
            frm.setDateTo( Integer.parseInt( frmDetail.getSelDateTo() ) );
            frm.setSelCustomerId( -99 );

            ciTime = frmDetail.getSelYearFrom() + frmDetail.getSelMonthFrom() + frmDetail.getSelDateFrom();
            ciTimeTo = frmDetail.getSelYearTo() + frmDetail.getSelMonthTo() + frmDetail.getSelDateTo();

            frm.setCiTimeFrom( Integer.parseInt( ciTime ) );
            frm.setCiTimeTo( Integer.parseInt( ciTimeTo ) );

            // データ取得
            logic.setFrm( frm );
            logic.getAccountRecv();
            logic.getAccountRecvDetail();
            frm = logic.getFrm();

            // ページ番号取得
            FormOwnerBkoComingToday newFrm = new FormOwnerBkoComingToday();
            newFrm.setPageAct( frmDetail.getPage() );
            frm = setPageData( frm, newFrm, frmDetail.getPage() );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerComingDetail.setComingToday ] Exception", exception );
            throw new Exception( "[ActionOwnerComingDetail.setComingToday] " + exception );
        }

        return(frm);
    }

    /**
     * 対象ページのデータのみ抽出する。
     * 
     * @param frmLogic FormOwnerBkoComingTodayオブジェクト
     * @param frmToday FormOwnerBkoComingTodayオブジェクト
     * @param pageAct 対象のページ番号
     * @return 抽出されたFormOwnerBkoComingTodayオブジェクト
     */
    public FormOwnerBkoComingToday setPageData(FormOwnerBkoComingToday frmLogic, FormOwnerBkoComingToday frmToday, int pageAct)
    {
        int maxCnt = 0;
        int stLine = 0;
        int enLine = 0;
        int stRow = 0;
        int endRow = 0;
        String pageRecords = "";
        String querStr = "";
        String pageLinks = "";
        ArrayList<String> ciDateList = new ArrayList<String>();
        ArrayList<String> ciTimeList = new ArrayList<String>();
        ArrayList<String> personNmList = new ArrayList<String>();
        ArrayList<Integer> customerIdList = new ArrayList<Integer>();
        ArrayList<Integer> slipNoList = new ArrayList<Integer>();
        ArrayList<String> seqList = new ArrayList<String>();
        ArrayList<Integer> htSlipNoList = new ArrayList<Integer>();
        ArrayList<String> accrecvAmountList = new ArrayList<String>();
        ArrayList<String> raitenList = new ArrayList<String>();
        ArrayList<String> huyoList = new ArrayList<String>();
        ArrayList<String> siyouList = new ArrayList<String>();
        ArrayList<String> yoyakuList = new ArrayList<String>();
        ArrayList<String> bonusList = new ArrayList<String>();
        ArrayList<String> addAmountList = new ArrayList<String>();
        ArrayList<String> subtractList = new ArrayList<String>();
        ArrayList<String> closingList = new ArrayList<String>();

        maxCnt = frmLogic.getRecMaxCnt();
        if ( maxCnt != 0 )
        {
            frmLogic.setRecCnt( maxCnt );
            if ( pageAct == 0 )
            {
                stLine = 0;
                if ( listmax > maxCnt )
                {
                    enLine = maxCnt - 1;
                }
                else
                {
                    enLine = listmax - 1;
                }
            }
            else
            {
                stLine = (listmax * pageAct) + 1;
                enLine = (listmax * (pageAct + 1));
            }

            if ( enLine > maxCnt )
            {
                enLine = maxCnt;
            }

            // 表示対象ページ分のみ
            if ( stLine == 0 )
            {
                stRow = stLine + 1;
                endRow = enLine + 1;
            }
            else
            {
                stRow = stLine;
                endRow = enLine;
            }
            for( int i = stRow - 1 ; i < endRow ; i++ )
            {
                ciDateList.add( frmLogic.getUsageDate().get( i ) );
                ciTimeList.add( frmLogic.getUsageTime().get( i ) );
                personNmList.add( frmLogic.getPersonNm().get( i ) );
                customerIdList.add( frmLogic.getCustomerId().get( i ) );
                slipNoList.add( frmLogic.getSlipNoList().get( i ) );
                seqList.add( frmLogic.getSeq().get( i ) );
                htSlipNoList.add( frmLogic.getHtSlipNo().get( i ) );
                accrecvAmountList.add( frmLogic.getAccrecvAmount().get( i ) );
                raitenList.add( frmLogic.getRaitenList().get( i ) );
                huyoList.add( frmLogic.getHuyoList().get( i ) );
                siyouList.add( frmLogic.getSiyouList().get( i ) );
                yoyakuList.add( frmLogic.getReserveList().get( i ) );
                bonusList.add( frmLogic.getBonusList().get( i ) );
                addAmountList.add( frmLogic.getAddAmountList().get( i ) );
                subtractList.add( frmLogic.getSubtractAmountList().get( i ) );
                closingList.add( frmLogic.getClosingList().get( i ) );
            }
            frmToday.setSelHotelID( frmLogic.getSelHotelID() );
            frmToday.setUsageDate( ciDateList );
            frmToday.setUsageTime( ciTimeList );
            frmToday.setPersonNm( personNmList );
            frmToday.setCustomerId( customerIdList );
            frmToday.setSlipNoList( slipNoList );
            frmToday.setSeq( seqList );
            frmToday.setAccrecvAmount( accrecvAmountList );
            frmToday.setHtSlipNo( htSlipNoList );
            frmToday.setSumUsageCharrge( frmLogic.getSumUsageCharrge() );
            frmToday.setSumAddAmount( frmLogic.getSumAddAmount() );
            frmToday.setSumSubtractAmount( frmLogic.getSumSubtractAmount() );
            frmToday.setRecMaxCnt( maxCnt );
            frmToday.setPageMax( maxCnt );
            frmToday.setRecCnt( maxCnt );
            frmToday.setRaitenList( raitenList );
            frmToday.setHuyoList( huyoList );
            frmToday.setSiyouList( siyouList );
            frmToday.setReserveList( yoyakuList );
            frmToday.setBonusList( bonusList );
            frmToday.setAddAmountList( addAmountList );
            frmToday.setSubtractAmountList( subtractList );
            frmToday.setClosingList( closingList );
            frmToday.setYearFrom( frmLogic.getYearFrom() );
            frmToday.setMonthFrom( frmLogic.getMonthFrom() );
            frmToday.setDateFrom( frmLogic.getDateFrom() );
            frmToday.setYearTo( frmLogic.getYearTo() );
            frmToday.setMonthTo( frmLogic.getMonthTo() );
            frmToday.setDateTo( frmLogic.getDateTo() );
        }
        else
        {
            frmToday.setRecCnt( 0 );
            frmToday.setPageMax( 0 );
        }

        if ( frmToday.getRecCnt() != -99 )
        {
            pageRecords = "<span class=\"current\">" + stRow + "</span> ～ <span class=\"current\">" + endRow + "</span>件 / 全<span class=\"current\">" + frmToday.getPageMax() + "</span>件";
            frmToday.setPageRecords( pageRecords );
        }

        if ( frmToday.getRecCnt() > listmax )
        {
            querStr = "ownerBkoComingToday.act?selHotelIDValue="
                    + frmToday.getSelHotelID()
                    + "&selYearFrom=" + frmToday.getYearFrom() + "&selMonthFrom=" + frmToday.getMonthFrom() + "&selDateFrom=" + frmToday.getDateFrom()
                    + "&selYearTo=" + frmToday.getYearTo() + "&selMonthTo=" + frmToday.getMonthTo() + "&selDateTo=" + frmToday.getDateTo();
            pageLinks = PagingDetails.getPagenationLink( frmToday.getPageAct(), listmax, frmToday.getPageMax(), querStr );
            frmToday.setPageLink( pageLinks );
        }

        return(frmToday);
    }

    /**
     * 収支明細を表示する。
     * 
     * @param frmBill FormOwnerBkoBillTodayオブジェクト
     * @param frmDetail FormOwnerBkoComingDetailsオブジェクト
     * @return FormOwnerBkoBillTodayオブジェクト
     * @throws Exception
     */
    private FormOwnerBkoBillToday setBillToday(FormOwnerBkoBillToday frmBill, FormOwnerBkoComingDetails frmDetail) throws Exception
    {
        LogicOwnerBkoBillToday logic = new LogicOwnerBkoBillToday();
        int billDate = 0;

        try
        {
            // 日付の設定
            billDate = Integer.parseInt( frmDetail.getSelYearFrom() + frmDetail.getSelMonthFrom() );

            frmBill.setSelHotelID( frmDetail.getSelHotelID() );
            frmBill.setSelYear( Integer.parseInt( frmDetail.getSelYearFrom() ) );
            frmBill.setSelMonth( Integer.parseInt( frmDetail.getSelMonthFrom() ) );
            frmBill.setIntBillDate( billDate );
            logic.setFrm( frmBill );
            logic.getAccountRecv();
            frmBill = logic.getFrm();

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoComingDetail.setBillToday] Exception", exception );
            throw new Exception( "[ActionOwnerBkoComingDetail.setBillToday] " + exception );
        }

        return(frmBill);
    }

}
