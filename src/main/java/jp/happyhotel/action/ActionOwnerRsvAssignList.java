package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.owner.FormOwnerRsvAssignList;
import jp.happyhotel.owner.LogicOwnerRsvAssignList;

/**
 * 
 * 予約一覧画面 Action Class
 */

public class ActionOwnerRsvAssignList extends BaseAction
{

    private static final int  listmax           = 5;   // 一画面最大明細表示件数
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean blnCheck;
        boolean blnRet;

        String dspFlg = "";
        String strErr = "";
        String strCarrierUrl = "";
        String hotelId = ""; // ホテルID

        String queryString = "";
        String pageLinks = "";
        String optionLinks = "";
        String pageRecords = "";
        int stRow = 0;
        int endRow = 0;
        String errMsg = "";
        int nextday = 0;
        String setToDateVal = "0";

        FormOwnerRsvAssignList dsp0; // form
        LogicOwnerRsvAssignList lgLPC; // logic

        dsp0 = new FormOwnerRsvAssignList();
        lgLPC = new LogicOwnerRsvAssignList();

        blnRet = false;
        String hotenaviId = "";
        int userId = 0;

        try
        {
            //
            if ( (request.getParameter( "dspFlg" ) == null) || (request.getParameter( "dspFlg" ).toString().length() == 0) )
            {
                dspFlg = "";
            }
            else
            {
                dspFlg = request.getParameter( "dspFlg" );
            }

            // ホテルID取得
            dsp0.setSelHotelID( Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() ) );
            hotelId = String.valueOf( dsp0.getSelHotelID() );

            // ログインユーザIDの取得
            userId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );

            // ログインユーザと担当ホテルのチェック
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( hotelId != null )
            {
                if ( (Integer.parseInt( hotelId ) != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, userId, Integer.parseInt( hotelId ) ) == false) )
                {
                    // 管理外のホテルはログイン画面へ遷移
                    response.sendRedirect( "../../owner/index.jsp" );
                    return;
                }
            }

            // 選択ホテル情報を得る
            blnRet = lgLPC.getHotelInfo( dsp0 );

            if ( dspFlg.equals( "init" ) )
            {
                int deadLineTime = OwnerRsvCommon.getDeadLineTime( Integer.parseInt( hotelId ) );
                // 初期表示時
                dsp0.setSelHotelID( Integer.parseInt( hotelId ) );
                dsp0.setChk1Obj( "CHECKED" );
                dsp0.setChk2Obj( "" );
                dsp0.setChk3Obj( "" );
                dsp0.setChk4Obj( "" );
                dsp0.setChk5Obj( "" );
                dsp0.setDateFrom( DateEdit.getDate( 1 ) ); // システム日付
                dsp0.setDateFromVal( Integer.parseInt( DateEdit.getDate( 2 ) ) );// システム日付
                dsp0.setTimeFromHour( String.valueOf( deadLineTime / 10000 ) );// 締時刻(時)
                dsp0.setTimeFromMin( String.valueOf( deadLineTime % 10000 / 100 ) );// 締時刻(分)
                nextday = DateEdit.addDay( dsp0.getDateFromVal(), 1 );
                dsp0.setDateTo( String.format( "%1$02d/%2$02d/%3$02d", nextday / 10000, nextday % 10000 / 100, nextday % 10000 % 100 ) ); // システム日付+1日
                dsp0.setDateToVal( nextday );// システム日付+1日
                dsp0.setTimeToHour( String.valueOf( deadLineTime / 10000 ) );// 締時刻(時)
                dsp0.setTimeToMin( String.valueOf( deadLineTime % 10000 / 100 ) );// 締時刻(分)
                dsp0.setReserveNo( "" );
                dsp0.setPageLink( "" );
                dsp0.setErrMsg( strErr );
                dsp0.setPageRecords( "" );
                dsp0.setPageAct( 0 );

                lgLPC.setDateFrom( dsp0.getDateFrom() );
                lgLPC.setDateTo( dsp0.getDateTo() );
                lgLPC.setRsvNo( "" );
                lgLPC.setHotelId( Integer.parseInt( hotelId ) );

                // データ抽出(一部分のみ)
                blnRet = lgLPC.getData( dsp0, 0, "PARTS" );

                if ( blnRet )
                {
                    strErr = lgLPC.getErrMsg();
                    dsp0.setErrMsg( strErr );
                    request.setAttribute( "err", "" );
                    stRow = dsp0.getPageSt();
                    endRow = dsp0.getPageEd();
                }
                else
                {
                    strErr = Message.getMessage( "erro.30001", "指定された条件に一致したデータ" ) + "<br />";
                    dsp0.setRecCnt( -99 );
                    dsp0.setErrMsg( strErr );
                    request.setAttribute( "err", "" );
                }

                request.setAttribute( "err", "" );
            }
            else
            {
                // 以降抽出条件取得
                blnCheck = lgLPC.chkDsp( request );
                if ( blnCheck == false )
                {
                    dsp0.setSelHotelID( Integer.parseInt( hotelId ) ); // ホテルID

                    dsp0.setRecCnt( -99 ); // ﾚｺｰﾄﾞ件数
                    getDspHeader( request, dsp0 );
                    // Toの日付セットし直し
                    if ( dsp0.getDateTo().equals( "1" ) )
                    {
                        setToDateVal = "1";
                        // 翌日
                        nextday = DateEdit.addDay( Integer.valueOf( dsp0.getDateFrom().replaceAll( "/", "" ) ), 1 );
                        dsp0.setDateTo( String.format( "%1$02d/%2$02d/%3$02d", nextday / 10000, nextday % 10000 / 100, nextday % 10000 % 100 ) );
                    }
                    else
                    {
                        setToDateVal = "0";
                        // 同日
                        dsp0.setDateTo( dsp0.getDateFrom() );
                    }
                    dsp0.setDateFromVal( Integer.valueOf( dsp0.getDateFrom().replaceAll( "/", "" ) ) );
                    dsp0.setDateToVal( Integer.valueOf( dsp0.getDateTo().replaceAll( "/", "" ) ) );
                    dsp0.setPageLink( "" );
                    dsp0.setPageRecords( "" );

                    strErr = lgLPC.getErrMsg();
                    dsp0.setErrMsg( strErr );
                    request.setAttribute( "err", "" );
                }
                else
                {

                    // 一覧印刷ボタンか？
                    if ( request.getParameter( "print" ) != null )
                    {
                        ActionOwnerRsvArrivalAssignList action = new ActionOwnerRsvArrivalAssignList();
                        action.execute( request, response );
                        return;
                    }

                    dsp0.setSelHotelID( Integer.parseInt( hotelId ) );
                    // エラーでなければデータを抽出する
                    getDspHeader( request, dsp0 );
                    // Toの日付セットし直し
                    if ( dsp0.getDateTo().equals( "1" ) )
                    {
                        setToDateVal = "1";
                        // 翌日
                        nextday = DateEdit.addDay( Integer.valueOf( dsp0.getDateFrom().replaceAll( "/", "" ) ), 1 );
                        dsp0.setDateTo( String.format( "%1$02d/%2$02d/%3$02d", nextday / 10000, nextday % 10000 / 100, nextday % 10000 % 100 ) );
                    }
                    else
                    {
                        setToDateVal = "0";
                        // 同日
                        dsp0.setDateTo( dsp0.getDateFrom() );
                    }
                    dsp0.setDateFromVal( Integer.valueOf( dsp0.getDateFrom().replaceAll( "/", "" ) ) );
                    dsp0.setDateToVal( Integer.valueOf( dsp0.getDateTo().replaceAll( "/", "" ) ) );
                    lgLPC.setDateFrom( dsp0.getDateFrom() );
                    lgLPC.setDateTo( dsp0.getDateTo() );
                    lgLPC.setRsvNo( dsp0.getReserveNo() );
                    lgLPC.setHotelId( Integer.parseInt( hotelId ) );

                    if ( (request.getParameter( "page" ) == null) || (request.getParameter( "page" ).toString().length() == 0) )
                    {
                        dsp0.setPageAct( 0 );
                    }
                    else
                    {
                        dsp0.setPageAct( Integer.parseInt( request.getParameter( "page" ).toString() ) );
                    }

                    // データ抽出(一部分のみ)
                    blnRet = lgLPC.getData( dsp0, dsp0.getPageAct(), "PARTS" );

                    if ( blnRet )
                    {
                        strErr = lgLPC.getErrMsg();
                        dsp0.setErrMsg( strErr );
                        request.setAttribute( "err", "" );
                        if ( dsp0.getPageSt() == 0 )
                        {
                            stRow = dsp0.getPageSt();
                            endRow = dsp0.getPageEd();
                        }
                        else
                        {
                            stRow = dsp0.getPageSt();
                            endRow = dsp0.getPageEd();
                        }
                    }
                    else
                    {
                        strErr = Message.getMessage( "erro.30001", "指定された条件に一致したデータ" ) + "<br />";
                        dsp0.setRecCnt( -99 );
                        dsp0.setErrMsg( strErr );
                        request.setAttribute( "err", "" );
                    }
                }
            }

            request.setAttribute( "FORM_ReserveListPC", dsp0 );
            strCarrierUrl = "owner_rsv_assign_list.jsp";

            if ( dsp0.getRecCnt() != -99 )
            {
                pageRecords = "<span class=\"current\">" + stRow + "</span>～ <span class=\"current\">" + endRow + "</span>件 / 全<span class=\"current\">" + dsp0.getPageMax() + "</span>件";
                dsp0.setPageRecords( pageRecords );
            }

            if ( dsp0.getRecCnt() > ActionOwnerRsvAssignList.listmax )
            {
                queryString = "actionReserveAssignListPC.act?selHotelIDValue=" + dsp0.getSelHotelID() +
                        "&dspFlg=" + dspFlg + "&date_f=" + dsp0.getDateFrom() +
                        "&date_t=" + setToDateVal + "&time_f_h=" + dsp0.getTimeFromHour() + "&time_f_m=" + dsp0.getTimeFromMin() + "&time_t_h=" + dsp0.getTimeToHour() + "&time_t_m=" + dsp0.getTimeToMin() + "&rsv_no=" + dsp0.getReserveNo();
                pageLinks = PagingDetails.getPagenationLink( dsp0.getPageAct(), ActionOwnerRsvAssignList.listmax, dsp0.getPageMax(), queryString );
                dsp0.setPageLink( pageLinks );
            }
            optionLinks = "actionReserveOptionListPC.act?selHotelIDValue=" + dsp0.getSelHotelID() +
                    "&dspFlg=" + dspFlg + "&date_f=" + dsp0.getDateFrom() +
                    "&date_t=" + setToDateVal + "&time_f_h=" + dsp0.getTimeFromHour() + "&time_f_m=" + dsp0.getTimeFromMin() + "&time_t_h=" + dsp0.getTimeToHour() + "&time_t_m=" + dsp0.getTimeToMin() + "&rsv_no=" + dsp0.getReserveNo();
            dsp0.setOptionLink( optionLinks );

            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveListPC.execute() ][hotelId = "
                    + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveListPC.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * 予約一覧のヘッダ部情報取得
     * 
     * @param request
     * @param frm
     */
    private void getDspHeader(HttpServletRequest request, FormOwnerRsvAssignList frm)
    {
        // 画面の値を取得
        frm.setDateFrom( request.getParameter( "date_f" ).toString() );
        frm.setTimeFromHour( request.getParameter( "time_f_h" ) );
        frm.setTimeFromMin( request.getParameter( "time_f_m" ) );
        frm.setDateTo( request.getParameter( "date_t" ).toString() );
        frm.setTimeToHour( request.getParameter( "time_t_h" ) );
        frm.setTimeToMin( request.getParameter( "time_t_m" ) );
        frm.setReserveNo( request.getParameter( "rsv_no" ).toString() );
    }

}
