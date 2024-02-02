package jp.happyhotel.action;

import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.owner.FormOwnerRsvList;
import jp.happyhotel.owner.LogicOwnerRsvList;

/**
 * 
 * 予約一覧画面 Action Class
 */

public class ActionOwnerRsvList extends BaseAction
{

    private static final int  listmax           = 20;  // 一画面最大明細表示件数
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
        String pageRecords = "";
        int objKbn = 0;
        int stRow = 0;
        int endRow = 0;
        String errMsg = "";

        FormOwnerRsvList dsp0; // form
        LogicOwnerRsvList lgLPC; // logic

        dsp0 = new FormOwnerRsvList();
        lgLPC = new LogicOwnerRsvList();

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
                // 初期表示時
                dsp0.setSelHotelID( Integer.parseInt( hotelId ) );
                dsp0.setChk1Obj( "CHECKED" );
                dsp0.setChk2Obj( "" );
                dsp0.setChk3Obj( "" );
                dsp0.setChk4Obj( "" );
                dsp0.setChk5Obj( "" );
                dsp0.setDateFrom( DateEdit.getDate( 1 ) ); // システム日付
                Calendar cal = Calendar.getInstance();
                cal.add( Calendar.MONTH, 1 );
                dsp0.setDateTo( DateEdit.getDate( 1, cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ) + 1, cal.get( Calendar.DATE ) ) ); // システム日付+1ヶ月
                dsp0.setReserveNo( "" );
                dsp0.setPageLink( "" );
                dsp0.setErrMsg( strErr );
                dsp0.setPageRecords( "" );
                dsp0.setPageAct( 0 );

                lgLPC.setDateFrom( DateEdit.getDate( 1 ) );
                lgLPC.setDateTo( DateEdit.getDate( 1, cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ) + 1, cal.get( Calendar.DATE ) ) );// システム日付+1ヶ月
                lgLPC.setRsvNo( "" );
                lgLPC.setHotelId( Integer.parseInt( hotelId ) );

                // データ抽出(一部分のみ)
                blnRet = lgLPC.getData( dsp0, 1, 0, "PARTS" );

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
            else if ( dspFlg.equals( "history" ) )
            {
                // 履歴表示処理
                String reserveNo = request.getParameter( "rsvNo" );
                // 予約番号に紐付く予約履歴を取得
                dsp0.setSelHotelID( Integer.parseInt( hotelId ) );
                lgLPC.getHistoryData( dsp0, Integer.parseInt( hotelId ), reserveNo );

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
                        ActionOwnerRsvArrivalList action = new ActionOwnerRsvArrivalList();
                        action.execute( request, response );
                        return;
                    }

                    dsp0.setSelHotelID( Integer.parseInt( hotelId ) );

                    // エラーでなければデータを抽出する
                    getDspHeader( request, dsp0 );
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

                    objKbn = Integer.parseInt( request.getParameter( "objKbn" ).toString() );

                    // データ抽出(一部分のみ)
                    blnRet = lgLPC.getData( dsp0, objKbn, dsp0.getPageAct(), "PARTS" );

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
            if ( dspFlg.equals( "history" ) )
            {
                strCarrierUrl = "owner_rsv_history_list.jsp";
            }
            else
            {
                strCarrierUrl = "owner_rsv_list.jsp";
            }

            if ( dsp0.getRecCnt() != -99 )
            {
                pageRecords = "<span class=\"current\">" + stRow + "</span>～ <span class=\"current\">" + endRow + "</span>件 / 全<span class=\"current\">" + dsp0.getPageMax() + "</span>件";
                dsp0.setPageRecords( pageRecords );
            }

            if ( dsp0.getRecCnt() > ActionOwnerRsvList.listmax )
            {
                if ( dspFlg.equals( "init" ) != false )
                {
                    dspFlg = "dsp";
                }
                queryString = "actionReserveListPC.act?selHotelIDValue=" + dsp0.getSelHotelID() +
                        "&dspFlg=" + dspFlg + "&objKbn=" + dsp0.getObjKbn() + "&date_f=" + dsp0.getDateFrom() +
                        "&date_t=" + dsp0.getDateTo() + "&rsv_no=" + dsp0.getReserveNo();
                pageLinks = PagingDetails.getPagenationLink( dsp0.getPageAct(), ActionOwnerRsvList.listmax, dsp0.getPageMax(), queryString );
                dsp0.setPageLink( pageLinks );
            }

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
    private void getDspHeader(HttpServletRequest request, FormOwnerRsvList frm)
    {
        // 画面の値を取得
        if ( request.getParameter( "objKbn" ).toString().equals( "1" ) )
        {
            frm.setChk1Obj( "CHECKED" );
            frm.setChk2Obj( "" );
            frm.setChk3Obj( "" );
            frm.setChk4Obj( "" );
            frm.setChk5Obj( "" );
        }
        else if ( request.getParameter( "objKbn" ).toString().equals( "2" ) )
        {
            frm.setChk1Obj( "" );
            frm.setChk2Obj( "CHECKED" );
            frm.setChk3Obj( "" );
            frm.setChk4Obj( "" );
            frm.setChk5Obj( "" );
        }
        else if ( request.getParameter( "objKbn" ).toString().equals( "3" ) )
        {
            frm.setChk1Obj( "" );
            frm.setChk2Obj( "" );
            frm.setChk3Obj( "CHECKED" );
            frm.setChk4Obj( "" );
            frm.setChk5Obj( "" );
        }
        else if ( request.getParameter( "objKbn" ).toString().equals( "4" ) )
        {
            frm.setChk1Obj( "" );
            frm.setChk2Obj( "" );
            frm.setChk3Obj( "" );
            frm.setChk4Obj( "CHECKED" );
            frm.setChk5Obj( "" );
        }
        else if ( request.getParameter( "objKbn" ).toString().equals( "5" ) )
        {
            frm.setChk1Obj( "" );
            frm.setChk2Obj( "" );
            frm.setChk3Obj( "" );
            frm.setChk4Obj( "" );
            frm.setChk5Obj( "CHECKED" );
        }
        frm.setObjKbn( Integer.parseInt( request.getParameter( "objKbn" ) ) );
        frm.setDateFrom( request.getParameter( "date_f" ).toString() );
        frm.setDateTo( request.getParameter( "date_t" ).toString() );
        frm.setReserveNo( request.getParameter( "rsv_no" ).toString() );
    }

}
