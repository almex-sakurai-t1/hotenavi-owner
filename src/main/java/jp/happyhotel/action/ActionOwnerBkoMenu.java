package jp.happyhotel.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoAnalysisCardless;
import jp.happyhotel.owner.FormOwnerBkoBillDay;
import jp.happyhotel.owner.FormOwnerBkoBillMonth;
import jp.happyhotel.owner.FormOwnerBkoBillPrint;
import jp.happyhotel.owner.FormOwnerBkoBillToday;
import jp.happyhotel.owner.FormOwnerBkoComingDay;
import jp.happyhotel.owner.FormOwnerBkoComingDetails;
import jp.happyhotel.owner.FormOwnerBkoComingMonth;
import jp.happyhotel.owner.FormOwnerBkoComingToday;
import jp.happyhotel.owner.FormOwnerBkoCreditDetail;
import jp.happyhotel.owner.FormOwnerBkoGroupBill;
import jp.happyhotel.owner.FormOwnerRsvManage;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoAnalysisCardless;
import jp.happyhotel.owner.LogicOwnerBkoBillDay;
import jp.happyhotel.owner.LogicOwnerBkoBillMonth;
import jp.happyhotel.owner.LogicOwnerBkoBillPrint;
import jp.happyhotel.owner.LogicOwnerBkoBillToday;
import jp.happyhotel.owner.LogicOwnerBkoComingDay;
import jp.happyhotel.owner.LogicOwnerBkoComingMonth;
import jp.happyhotel.owner.LogicOwnerBkoComingToday;
import jp.happyhotel.owner.LogicOwnerBkoCreditDetail;
import jp.happyhotel.owner.LogicOwnerBkoGroupBill;

/**
 * 
 * バックオフィスメニュー画面
 */
public class ActionOwnerBkoMenu extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoComingToday frmComToday;
        FormOwnerBkoComingDay frmComDay;
        FormOwnerBkoComingMonth frmComMonth;
        FormOwnerBkoBillToday frmBillToday;
        FormOwnerBkoBillDay frmBillDay;
        FormOwnerBkoBillMonth frmBillMonth;
        FormOwnerBkoGroupBill frmGroupBill;
        FormOwnerBkoComingDetails frmBillComAddInput;
        FormOwnerBkoBillPrint frmBillPrint;
        FormOwnerRsvManage frmManage;
        FormOwnerBkoAnalysisCardless frmCardless;
        FormOwnerBkoCreditDetail frmCreditDetail;

        int selHotelID = 0;
        int paramModeFlg = 0;
        int paramUserID = 0;
        String errMsg = "";
        String hotenaviId = "";
        int imediaFlg = 0;

        try
        {
            // 引数の取得
            if ( (request.getParameter( "modeFlg" ) != null) && (request.getParameter( "modeFlg" ).toString().length() != 0) )
            {
                paramModeFlg = Integer.parseInt( request.getParameter( "modeFlg" ).toString() );
            }

            // 選択されたホテルIDの取得
            if ( (request.getParameter( "selHotelIDValue" ) != null) && (request.getParameter( "selHotelIDValue" ).trim().length() != 0) )
            {
                selHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            if ( selHotelID == 0 && paramModeFlg != 180 )
            {
                paramModeFlg = 100;
            }

            // ログインユーザIDの取得
            paramUserID = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );

            // ログインユーザと担当ホテルのチェック
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( (selHotelID != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, paramUserID, selHotelID ) == false) )
            {
                // 管理外のホテルはログイン画面へ遷移
                response.sendRedirect( "../../owner/index.jsp" );
                return;
            }

            // TOPページ設定
            frmMenu = new FormOwnerBkoMenu();
            OwnerRsvCommon.setMenu( frmMenu, selHotelID, paramModeFlg, request.getCookies() );
            frmMenu.setUserId( paramUserID );
            request.setAttribute( "FORM_Menu", frmMenu );

            // ホテルが1件しかない場合は、選択ホテルIDとする
            if ( frmMenu.getHotelIDList().size() == 1 )
            {
                selHotelID = frmMenu.getHotelIDList().get( 0 );
                frmMenu.setSelHotelID( selHotelID );
            }

            // 画面遷移
            switch( paramModeFlg )
            {
                case 100:
                    // ハピホテマイル履歴
                    frmComToday = new FormOwnerBkoComingToday();
                    frmComToday.setSelHotelID( selHotelID );
                    frmComToday = setComingToday( frmComToday );
                    request.setAttribute( "FORM_comToday", frmComToday );
                    break;

                case 110:
                    // 日別来店状況
                    frmComDay = new FormOwnerBkoComingDay();
                    frmComDay.setSelHotelID( selHotelID );
                    setComingDay( frmComDay, hotenaviId, paramUserID );
                    request.setAttribute( "FORM_comDay", frmComDay );
                    break;

                case 120:
                    // 月別来店状況
                    frmComMonth = new FormOwnerBkoComingMonth();
                    frmComMonth.setSelHotelID( selHotelID );
                    setComingMonth( frmComMonth, hotenaviId, paramUserID );
                    request.setAttribute( "FORM_comMonth", frmComMonth );
                    break;

                case 130:
                    // 収支明細
                    frmBillToday = new FormOwnerBkoBillToday();
                    frmBillToday.setSelHotelID( selHotelID );
                    frmBillToday = setBillToday( frmBillToday );
                    request.setAttribute( "FORM_billToday", frmBillToday );
                    break;

                case 140:
                    // 日別収支明細
                    frmBillDay = new FormOwnerBkoBillDay();
                    frmBillDay.setSelHotelID( selHotelID );
                    setBillDay( frmBillDay );
                    request.setAttribute( "FORM_billDay", frmBillDay );
                    break;

                case 150:
                    // 月別収支明細
                    frmBillMonth = new FormOwnerBkoBillMonth();
                    frmBillMonth.setSelHotelID( selHotelID );
                    setBillMonth( frmBillMonth );
                    request.setAttribute( "FORM_billMonth", frmBillMonth );
                    break;

                case 101:
                    // 来店追加入力
                    frmBillComAddInput = new FormOwnerBkoComingDetails();
                    frmBillComAddInput.setSelHotelID( selHotelID );
                    // frmBillComAddInput.setOwnerHotelID( paraOwnerHotelID );
                    frmBillComAddInput.setUserId( paramUserID );
                    setComingAddInput( frmBillComAddInput );
                    request.setAttribute( "FORM_comDetail", frmBillComAddInput );
                    break;

                case 170:
                    // 請求書発行
                    frmBillPrint = new FormOwnerBkoBillPrint();
                    frmBillPrint.setSelHotelID( selHotelID );
                    imediaFlg = OwnerRsvCommon.getImediaFlag( hotenaviId, paramUserID );
                    frmBillPrint.setImediaFlg( imediaFlg );
                    setBillPrint( frmBillPrint );
                    request.setAttribute( "FORM_billPrint", frmBillPrint );
                    break;
                case 180:
                    // グループ店収支
                    frmGroupBill = new FormOwnerBkoGroupBill();
                    frmGroupBill.setSelHotelID( selHotelID );
                    frmGroupBill.setSelContractID( hotenaviId );
                    frmGroupBill.setSelUserID( paramUserID );
                    setGroupBill( frmGroupBill );
                    request.setAttribute( "FORM_groupBill", frmGroupBill );
                    break;
                case 190:
                    // カードレスメンバー
                    frmCardless = new FormOwnerBkoAnalysisCardless();
                    frmCardless.setSelHotelID( selHotelID );
                    setCardless( frmCardless );
                    request.setAttribute( "FORM_cardless", frmCardless );
                    break;
                case 200:
                    // クレジット明細
                    frmCreditDetail = new FormOwnerBkoCreditDetail();
                    frmCreditDetail.setSelHotelID( selHotelID );
                    setCreditDetail( frmCreditDetail );
                    request.setAttribute( "FORM_creditDetail", frmCreditDetail );
                    break;
                default:
                    // TOPへ戻る
                    frmManage = new FormOwnerRsvManage();
                    frmManage.setSelHotelID( selHotelID );
                    frmManage.setUserId( paramUserID );
                    // setRsvManage( frmManage, selHotelID, paramUserID );
                    paramModeFlg = 0;
                    frmManage.setSelHotelErrMsg( Message.getMessage( "warn.30010" ) );
                    frmMenu.setModeFlg( paramModeFlg );
                    request.setAttribute( "FORM_Manage", frmManage );
                    break;
            }

            // TOPページ設定 - 選択ホテルIDの設定
            if ( frmMenu.getSelHotelID() == 0 )
            {
                selHotelID = 0;
            }
            else if ( frmMenu.getSelHotelID() != 0 )
            {
                // 指定されたホテル
                selHotelID = frmMenu.getSelHotelID();
            }
            else
            {
                // ホテルリストの先頭を選択済みとする。
                selHotelID = frmMenu.getHotelIDList().get( 0 );
            }
            frmMenu.setSelHotelID( selHotelID );

            requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.execute() ] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoMenu.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            frmMenu = null;
        }
    }

    /**
     * 
     * ハピホテマイル履歴呼び出し
     */
    private FormOwnerBkoComingToday setComingToday(FormOwnerBkoComingToday frmLogic) throws Exception
    {
        int year = 0;
        int month = 0;
        int date = 0;
        String ciTime = "";
        String ciTimeTo = "";
        LogicOwnerBkoComingToday logic = new LogicOwnerBkoComingToday();
        FormOwnerBkoComingToday frm = new FormOwnerBkoComingToday();

        try
        {
            // システム日付のセット
            Calendar cal = Calendar.getInstance();
            year = cal.get( Calendar.YEAR );
            month = cal.get( Calendar.MONTH ) + 1;
            date = cal.get( Calendar.DATE );

            // 前回締日(11日)
            int yearfrom = 0;
            int monthfrom = 0;
            int datefrom = 0;
            // 11日以前なら前月11日〜となる
            if ( date < 11 )
            {
                cal.add( Calendar.MONTH, -1 );
            }
            yearfrom = cal.get( Calendar.YEAR );
            monthfrom = cal.get( Calendar.MONTH ) + 1;
            datefrom = 11;

            frm.setYearFrom( yearfrom );
            frm.setMonthFrom( monthfrom );
            frm.setDateFrom( datefrom );
            frm.setYearTo( year );
            frm.setMonthTo( month );
            frm.setDateTo( date );
            frm.setSelCustomerId( -99 );
            frmLogic.setYearFrom( year );
            frmLogic.setMonthFrom( month );
            frmLogic.setDateFrom( date );
            frmLogic.setYearTo( year );
            frmLogic.setMonthTo( month );
            frmLogic.setDateTo( date );
            frmLogic.setSelCustomerId( -99 );

            ciTime = Integer.toString( yearfrom ) + String.format( "%1$02d", monthfrom ) + String.format( "%1$02d", datefrom );
            ciTimeTo = Integer.toString( year ) + String.format( "%1$02d", month ) + String.format( "%1$02d", date );
            frm.setCiTimeFrom( Integer.parseInt( ciTime ) );
            frm.setCiTimeTo( Integer.parseInt( ciTimeTo ) );
            frmLogic.setCiTimeFrom( Integer.parseInt( ciTime ) );
            frmLogic.setCiTimeTo( Integer.parseInt( ciTimeTo ) );

            // データ取得
            logic.setFrm( frmLogic );
            logic.getAccountRecv();
            logic.getAccountRecvDetail();
            frmLogic = logic.getFrm();

            frm.setSelHotelID( frmLogic.getSelHotelID() );
            if ( frmLogic.getErrMsg().trim().length() == 0 )
            {
                frm = ActionOwnerBkoComingToday.setPageData( frmLogic, frm, 0 );
            }
            else
            {
                frm.setErrMsg( frmLogic.getErrMsg() );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setComingToday ] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setComingToday] " + exception );
        }

        return(frm);
    }

    /**
     * 
     * 日別来店状況呼び出し
     */
    private void setComingDay(FormOwnerBkoComingDay frm, String hotenaviId, int userId) throws Exception
    {
        LogicOwnerBkoComingDay logic = new LogicOwnerBkoComingDay();
        int year = 0;
        int month = 0;
        int billOwnFlg = 0;

        try
        {
            // システム日付のセット
            Calendar cal = Calendar.getInstance();
            year = cal.get( Calendar.YEAR );
            month = cal.get( Calendar.MONTH ) + 1;

            frm.setSelYear( year );
            frm.setSelMonth( month );

            // 請求閲覧可能フラグ
            billOwnFlg = OwnerBkoCommon.getBillOwnFlg( hotenaviId, userId );
            frm.setBillFlg( billOwnFlg );

            logic.setFrm( frm );
            logic.getAccountRecv();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setComingDay ] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setComingDay] " + exception );
        }
    }

    /**
     * 
     * 月別来店状況呼び出し
     */
    private void setComingMonth(FormOwnerBkoComingMonth frm, String hotenaviId, int userId) throws Exception
    {
        LogicOwnerBkoComingMonth logic = new LogicOwnerBkoComingMonth();
        int year = 0;
        int billFlg = 0;

        try
        {
            // システム日付のセット
            Calendar cal = Calendar.getInstance();
            year = cal.get( Calendar.YEAR );
            frm.setSelYear( year );

            // 請求閲覧可能フラグ
            billFlg = OwnerBkoCommon.getBillOwnFlg( hotenaviId, userId );
            frm.setBillFlg( billFlg );

            logic.setFrm( frm );
            logic.getAccountRecv();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setComingMonth] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setComingMonth] " + exception );
        }
    }

    /**
     * 
     * 収支明細画面
     */
    private FormOwnerBkoBillToday setBillToday(FormOwnerBkoBillToday frm) throws Exception
    {
        LogicOwnerBkoBillToday logic = new LogicOwnerBkoBillToday();
        int year = 0;
        int month = 0;
        int billDate = 0;

        try
        {
            // システム日付のセット
            Calendar cal = Calendar.getInstance();
            year = cal.get( Calendar.YEAR );
            month = cal.get( Calendar.MONTH ) + 1;
            billDate = Integer.parseInt( year + String.format( "%02d", month ) );

            frm.setSelYear( year );
            frm.setSelMonth( month );
            frm.setIntBillDate( billDate );

            logic.setFrm( frm );
            logic.getAccountRecv();
            frm = logic.getFrm();

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setBillToday] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setBillToday] " + exception );
        }

        return(frm);
    }

    /**
     * 
     * 日別請求明細画面
     */
    private void setBillDay(FormOwnerBkoBillDay frm) throws Exception
    {
        LogicOwnerBkoBillDay logic = new LogicOwnerBkoBillDay();
        int year = 0;
        int month = 0;
        String today = "";
        String lastMonth = "";
        String simeKikan = "";

        try
        {
            // システム日付の取得
            Calendar cal = Calendar.getInstance();
            year = cal.get( Calendar.YEAR );
            month = cal.get( Calendar.MONTH ) + 1;
            today = new SimpleDateFormat( "yyyy年MM月" ).format( cal.getTime() );

            // 先月の取得
            cal.add( Calendar.MONTH, -1 );
            lastMonth = new SimpleDateFormat( "yyyy年MM月" ).format( cal.getTime() );
            simeKikan = lastMonth + (OwnerBkoCommon.SIME_DATE + 1) + "日〜" + today + OwnerBkoCommon.SIME_DATE + "日";

            frm.setSelYear( year );
            frm.setSelMonth( month );
            frm.setSimeKikan( simeKikan );
            logic.setFrm( frm );
            logic.getAccountRecv();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setBillDay] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setBillDay] " + exception );
        }
    }

    /**
     * 
     * 月別請求明細画面
     */

    /*
     * private void setBillMonth(FormOwnerBkoBillMonth frm) throws Exception
     * {
     * LogicOwnerBkoBillMonth logic = new LogicOwnerBkoBillMonth();
     * int year = 0;
     * int closingMonth = OwnerBkoCommon.getClosingMonth();
     * Logging.info( "[ActionOwnerBkoMenu.setBillMonth] closingMonth=" + closingMonth );
     * try
     * {
     * // システム日付の取得
     * Calendar cal = Calendar.getInstance();
     * year = cal.get( Calendar.YEAR );
     * frm.setSelYearTo( year );
     * frm.setSelMonthTo( cal.get( Calendar.MONTH ) + 1 );
     * // １年前の年月取得
     * cal.add( Calendar.YEAR, -1 );
     * // １年分の場合、月は同月ではなく翌月となるため
     * cal.add( Calendar.MONTH, 1 );
     * year = cal.get( Calendar.YEAR );
     * frm.setSelYearFrom( year );
     * frm.setSelMonthFrom( cal.get( Calendar.MONTH ) + 1 );
     * logic.setFrm( frm );
     * logic.getAccountRecv();
     * }
     * catch ( Exception exception )
     * {
     * Logging.error( "[ActionOwnerBkoMenu.setBillMonth] Exception", exception );
     * throw new Exception( "[ActionOwnerBkoMenu.setBillMonth] " + exception );
     * }
     * }
     */

    private void setBillMonth(FormOwnerBkoBillMonth frm) throws Exception
    {
        LogicOwnerBkoBillMonth logic = new LogicOwnerBkoBillMonth();
        int closingMonth = OwnerBkoCommon.getClosingMonth();
        Logging.info( "[ActionOwnerBkoMenu.setBillMonth] closingMonth=" + closingMonth );
        int prevYear = closingMonth / 100 - 1;
        int prevMonth = closingMonth % 100 + 1;
        if ( prevMonth > 12 )
        {
            prevMonth = 1;
            prevYear = prevYear + 1;
        }
        try
        {
            // システム日付の取得
            frm.setSelYearTo( closingMonth / 100 );
            frm.setSelMonthTo( closingMonth % 100 );
            frm.setSelYearFrom( prevYear );
            frm.setSelMonthFrom( prevMonth );
            logic.setFrm( frm );
            logic.getAccountRecv();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setBillMonth] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setBillMonth] " + exception );
        }
    }

    /**
     * 
     * グループ店収支画面
     */
    private void setGroupBill(FormOwnerBkoGroupBill frm) throws Exception
    {
        LogicOwnerBkoGroupBill logic = new LogicOwnerBkoGroupBill();
        int year = 0;
        int month = 0;
        // システム日付の取得
        Calendar cal = Calendar.getInstance();
        year = cal.get( Calendar.YEAR );
        month = cal.get( Calendar.MONTH ) + 1;

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";

        int closing_date = year * 100 + month;
        // 仮締め年月
        try
        {
            connection = DBConnection.getConnection();
            query = "SELECT closing_date FROM hh_bko_closing_control WHERE closing_kind >=2 ORDER BY closing_date DESC";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    closing_date = result.getInt( "closing_date" );
                }
            }

            DBConnection.releaseResources( result, prestate, connection );

            frm.setSelYear( closing_date / 100 );
            frm.setSelMonth( closing_date % 100 );
            logic.setFrm( frm );
            logic.getGroupBill();

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setGroupBill] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setGroupBill] " + exception );
        }
    }

    /**
     * 
     * カードレスメンバー分析画面
     */
    private void setCardless(FormOwnerBkoAnalysisCardless frm) throws Exception
    {
        LogicOwnerBkoAnalysisCardless logic = new LogicOwnerBkoAnalysisCardless();
        int year = 0;
        int month = 0;

        try
        {

            // システム日付の取得
            Calendar cal = Calendar.getInstance();
            year = cal.get( Calendar.YEAR );
            month = cal.get( Calendar.MONTH ) + 1;
            frm.setSelYear( year );
            frm.setSelMonth( month );

            String ymdStr = "";
            int ymdInt = 0;
            ymdStr = DateEdit.getDate( 2, frm.getSelYear(), frm.getSelMonth(), 1 );
            frm.setDateFrom( Integer.parseInt( ymdStr ) );
            ymdInt = DateEdit.addMonth( Integer.parseInt( ymdStr ), 1 );
            ymdInt = DateEdit.addDay( ymdInt, -1 );
            frm.setDateTo( ymdInt );
            String simeKikan = frm.getDateFrom() / 10000 + "年" + (frm.getDateFrom() % 10000) / 100 + "月" + frm.getDateFrom() % 100 + "日〜" + frm.getDateTo() / 10000 + "年" + (frm.getDateTo() % 10000) / 100 + "月" + (frm.getDateTo() % 100) + "日";
            frm.setSimeKikan( simeKikan );

            logic.setFrm( frm );
            logic.getAnalysisCardless();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setCardless] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setCardless] " + exception );
        }
    }

    private void setCreditDetail(FormOwnerBkoCreditDetail frm) throws Exception
    {
        LogicOwnerBkoCreditDetail logic = new LogicOwnerBkoCreditDetail();
        int closingMonth = OwnerBkoCommon.getClosingMonth();
        int prevYear = closingMonth / 100;
        int prevMonth = closingMonth % 100;
        try
        {

            // システム日付の取得
            frm.setSelYearTo( closingMonth / 100 );
            frm.setSelMonthTo( closingMonth % 100 );
            frm.setSelYearFrom( prevYear );
            frm.setSelMonthFrom( prevMonth );
            logic.setFrm( frm );
            logic.getCreditDetail();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setCreditDetail] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setCreditDetail] " + exception );
        }
    }

    /**
     * 
     * 来店追加入力画面
     */
    private void setComingAddInput(FormOwnerBkoComingDetails frm) throws Exception
    {
        // int imediaFlg = 0;
        // int billOwnFlg = 0;
        int year = 0;
        int month = 0;
        int date = 0;

        try
        {
            // // 事務局フラグ
            // imediaFlg = OwnerRsvCommon.getImediaFlag( frm.getOwnerHotelID(), frm.getUserId() );
            // frm.setImediaFlg( imediaFlg );
            //
            // // 請求閲覧可能フラグ
            // billOwnFlg = OwnerBkoCommon.getBillOwnFlg( frm.getOwnerHotelID(), frm.getUserId() );
            // frm.setBillOwnFlg( billOwnFlg );

            // システム日付の取得
            Calendar cal = Calendar.getInstance();
            year = cal.get( Calendar.YEAR );
            month = cal.get( Calendar.MONTH ) + 1;
            date = cal.get( Calendar.DATE );

            frm.setSelYear( Integer.toString( year ) );
            frm.setSelMonth( Integer.toString( month ) );
            frm.setSelDate( Integer.toString( date ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setComingAddInput] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setComingAddInput] " + exception );
        }
    }

    /**
     * 
     * 請求書発行画面
     */
    private void setBillPrint(FormOwnerBkoBillPrint frm) throws Exception
    {
        LogicOwnerBkoBillPrint logic = new LogicOwnerBkoBillPrint();

        try
        {
            logic.setFrm( frm );
            // logic.getHotelRsv();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoMenu.setBillPrint] Exception", exception );
            throw new Exception( "[ActionOwnerBkoMenu.setsetBillPrint] " + exception );
        }
    }
}
