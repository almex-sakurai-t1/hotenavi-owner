package jp.happyhotel.action;

import java.text.DateFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.owner.FormOwnerBkoComingToday;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoComingToday;

public class ActionOwnerBkoComingToday extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 100; // ハピホテマイル履歴
    private static final int  listmax           = 20;  // 一画面最大明細表示件数

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoComingToday frmToday;
        FormOwnerBkoComingToday frmLogic; // ロジックで使用するフォームオブジェクト
        LogicOwnerBkoComingToday logic;
        int paramHotelID = 0;
        int paramUserId = 0;
        String paramYearFrom = "";
        String paramMonthFrom = "";
        String paramDateFrom = "";
        String paramYearTo = "";
        String paramMonthTo = "";
        String paramDateTo = "";
        int paramCustomerId = -99;
        int paramPageAct = 0;
        int hotelId = 0;
        int menuFlg = 0;
        String errMsg = "";

        try
        {
            frmToday = new FormOwnerBkoComingToday();
            frmLogic = new FormOwnerBkoComingToday();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerBkoComingToday();

            // ▼画面の値を取得
            // ホテルID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramYearFrom = request.getParameter( "selYearFrom" );
            paramMonthFrom = String.format( "%02d", Integer.parseInt( request.getParameter( "selMonthFrom" ) ) );
            paramDateFrom = String.format( "%02d", Integer.parseInt( request.getParameter( "selDateFrom" ) ) );
            paramYearTo = request.getParameter( "selYearTo" );
            paramMonthTo = String.format( "%02d", Integer.parseInt( request.getParameter( "selMonthTo" ) ) );
            paramDateTo = String.format( "%02d", Integer.parseInt( request.getParameter( "selDateTo" ) ) );
            if ( request.getParameter( "page" ) != null )
            {
                paramPageAct = Integer.parseInt( request.getParameter( "page" ) );
            }
            if ( request.getParameter( "customId" ) != null )
            {
                paramCustomerId = Integer.parseInt( request.getParameter( "customId" ) );
            }

            // 値をフォームにセット
            frmToday.setSelHotelID( paramHotelID );
            frmToday.setYearFrom( Integer.parseInt( paramYearFrom ) );
            frmToday.setMonthFrom( Integer.parseInt( paramMonthFrom ) );
            frmToday.setDateFrom( Integer.parseInt( paramDateFrom ) );
            frmToday.setYearTo( Integer.parseInt( paramYearTo ) );
            frmToday.setMonthTo( Integer.parseInt( paramMonthTo ) );
            frmToday.setDateTo( Integer.parseInt( paramDateTo ) );
            frmToday.setCiTimeFrom( Integer.parseInt( paramYearFrom + paramMonthFrom + paramDateFrom ) );
            frmToday.setCiTimeTo( Integer.parseInt( paramYearTo + paramMonthTo + paramDateTo ) );
            frmToday.setErrMsg( "" );
            frmToday.setPageAct( paramPageAct );
            frmToday.setSelCustomerId( paramCustomerId );

            frmLogic.setSelHotelID( paramHotelID );
            frmLogic.setYearFrom( Integer.parseInt( paramYearFrom ) );
            frmLogic.setMonthFrom( Integer.parseInt( paramMonthFrom ) );
            frmLogic.setDateFrom( Integer.parseInt( paramDateFrom ) );
            frmLogic.setYearTo( Integer.parseInt( paramYearTo ) );
            frmLogic.setMonthTo( Integer.parseInt( paramMonthTo ) );
            frmLogic.setDateTo( Integer.parseInt( paramDateTo ) );
            frmLogic.setCiTimeFrom( Integer.parseInt( paramYearFrom + paramMonthFrom + paramDateFrom ) );
            frmLogic.setCiTimeTo( Integer.parseInt( paramYearTo + paramMonthTo + paramDateTo ) );
            frmLogic.setErrMsg( "" );
            frmLogic.setPageAct( paramPageAct );
            frmLogic.setSelCustomerId( paramCustomerId );
            menuFlg = MENU_FLG;

            // 入力チェック
            errMsg = inputCheck( frmToday );
            if ( errMsg.trim().length() != 0 )
            {
                // エラーあり
                frmToday.setErrMsg( errMsg );

                // メニューの設定
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_comToday", frmToday );
                requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
                requestDispatcher.forward( request, response );

                return;
            }

            // ヘッダー情報取得
            logic.setFrm( frmLogic );
            logic.getAccountRecv();

            // 明細の加算、減算情報取得
            logic.getAccountRecvDetail();
            frmLogic = logic.getFrm();

            // 抽出した件数のうち一部分のみフォームへ編集する
            if ( frmLogic.getErrMsg().trim().length() == 0 )
            {
                frmToday = setPageData( frmLogic, frmToday, paramPageAct );
            }
            else
            {
                frmToday.setErrMsg( frmLogic.getErrMsg() );
            }

            // メニューの設定
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // 画面遷移
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_comToday", frmToday );
            requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoComingToday.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoComingToday.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }

    /**
     * 入力チェック
     * 
     * @param frm FormOwnerBkoComingTodayオブジェクト
     * @return エラーメッセージ
     */
    private String inputCheck(FormOwnerBkoComingToday frm)
    {
        String errMsg = "";
        String dateFrom = "";
        String dateTo = "";
        DateFormat format = DateFormat.getDateInstance();

        dateFrom = Integer.toString( frm.getYearFrom() ) + "/" + String.format( "%02d", frm.getMonthFrom() ) + "/" + String.format( "%02d", frm.getDateFrom() );
        dateTo = Integer.toString( frm.getYearTo() ) + "/" + String.format( "%02d", frm.getMonthTo() ) + "/" + String.format( "%02d", frm.getDateTo() );

        // ▼日付の妥当性チェック
        // 開始年月日
        format.setLenient( false );
        try
        {
            format.parse( dateFrom );
        }
        catch ( Exception e )
        {
            errMsg = errMsg + Message.getMessage( "erro.30007", "開始年月日" ) + "<br>";
        }

        // 開始年月日
        try
        {
            format.parse( dateTo );
        }
        catch ( Exception e )
        {
            errMsg = errMsg + Message.getMessage( "erro.30007", "終了年月日" ) + "<br>";
        }

        // ▼日付の大小チェック
        if ( Integer.parseInt( dateFrom.replaceAll( "/", "" ) ) > Integer.parseInt( dateTo.replaceAll( "/", "" ) ) )
        {
            errMsg = errMsg + Message.getMessage( "warn.00012" ) + "<br>";
        }

        return(errMsg);
    }

    /**
     * 対象ページのデータのみ抽出する。
     * 
     * @param frmLogic FormOwnerBkoComingTodayオブジェクト
     * @param frmToday FormOwnerBkoComingTodayオブジェクト
     * @param pageAct 対象のページ番号
     * @return 抽出されたFormOwnerBkoComingTodayオブジェクト
     */
    public static FormOwnerBkoComingToday setPageData(FormOwnerBkoComingToday frmLogic, FormOwnerBkoComingToday frmToday, int pageAct)
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
        }
        else
        {
            frmToday.setRecCnt( 0 );
            frmToday.setPageMax( 0 );
        }

        if ( frmToday.getRecCnt() != -99 )
        {
            pageRecords = "<span class=\"current\">" + stRow + "</span> 〜 <span class=\"current\">" + endRow + "</span>件 / 全<span class=\"current\">" + frmToday.getPageMax() + "</span>件";
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
}
