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
import jp.happyhotel.owner.FormOwnerRsvCreditList;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvCreditList;
import jp.happyhotel.owner.LogicOwnerBkoMenu;

/**
 * 
 * ノーショークレジット売上画面 Action Class
 */

public class ActionOwnerRsvCreditList extends BaseAction
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
        String pageRecords = "";
        int objKbn = 0;
        int stRow = 0;
        int endRow = 0;
        String errMsg = "";

        FormOwnerRsvCreditList dsp0; // form
        LogicOwnerRsvCreditList lgLPC; // logic

        dsp0 = new FormOwnerRsvCreditList();
        lgLPC = new LogicOwnerRsvCreditList();

        blnRet = false;
        String hotenaviId = "";
        int userId = 0;
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

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
            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( hotenaviId, userId );
            adminflag = logicMenu.getAdminFlg( hotenaviId, userId );
            imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, userId );
            if ( hotelId != null )
            {
                if ( (Integer.parseInt( hotelId ) != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, userId, Integer.parseInt( hotelId ) ) == false) )
                {
                    // 管理外のホテルはログイン画面へ遷移
                    response.sendRedirect( "../../owner/index.jsp" );
                    return;
                }
                // アイメディアユーザは取消ボタンも使えるようにする
                if ( imediaflag == OwnerRsvCommon.IMEDIAFLG_IMEDIA && adminflag == 1 && hotenaviId.equals( "happyhotel" ) )
                {
                    dsp0.setCancelBtnViewFlag( true );
                }
            }
            if ( (imediaflag == OwnerRsvCommon.IMEDIAFLG_IMEDIA && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO
                    && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ページを閲覧する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( (imediaflag == OwnerRsvCommon.IMEDIAFLG_IMEDIA && hotenaviId.equals( "happyhotel" ) && adminflag == 1) == false &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && dspFlg.equals( "deleteSales" ) == true )
            {
                errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
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
                Calendar cal = Calendar.getInstance();
                cal.add( Calendar.MONTH, -1 );
                cal.set( cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ), 1 );
                dsp0.setDateFrom( DateEdit.getDate( 1, cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ) + 1, cal.get( Calendar.DATE ) ) ); // 前月1日
                dsp0.setDateTo( DateEdit.getDate( 1, cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ) + 1, cal.getActualMaximum( Calendar.DATE ) ) ); // 前月末
                dsp0.setReserveNo( "" );
                dsp0.setPageLink( "" );
                dsp0.setErrMsg( strErr );
                dsp0.setPageRecords( "" );
                dsp0.setPageAct( 0 );

                lgLPC.setDateFrom( DateEdit.getDate( 1 ) );
                lgLPC.setDateTo( DateEdit.getDate( 1 ) );
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
            else
            {
                if ( dspFlg.equals( "deleteSales" ) && request.getParameter( "delReserveNo" ) != null && request.getParameter( "delReserveNo" ).equals( "" ) == false &&
                        request.getParameter( "delReserveSeqNo" ) != null && request.getParameter( "delReserveSeqNo" ).equals( "" ) == false )
                {
                    // アイメディアユーザのみ削除可能
                    if ( OwnerRsvCommon.getImediaFlag( hotenaviId, userId ) == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
                    {
                        // 対象の売上の削除
                        lgLPC.deleteSales( request.getParameter( "delReserveNo" ), request.getParameter( "delReserveSeqNo" ), Integer.parseInt( hotelId ) );
                        if ( lgLPC.getErrMsg().equals( "" ) == false )
                        {
                            strErr = lgLPC.getErrMsg() + "<br/>";
                        }
                        else
                        {
                            // ホストから送信するためのログ吐き出し
                            Logging.info( "[NoshowData] HostSendFile hotelid=" + hotelId + ",reserveNo=" + request.getParameter( "delReserveNo" ) + ",seqno=" + request.getParameter( "delReserveSeqNo" ) + "," );
                        }
                        lgLPC.setErrMsg( "" );
                    }
                }

                // 以降抽出条件取得
                blnCheck = lgLPC.chkDsp( request );
                if ( blnCheck == false )
                {
                    dsp0.setSelHotelID( Integer.parseInt( hotelId ) ); // ホテルID

                    dsp0.setRecCnt( -99 ); // ﾚｺｰﾄﾞ件数
                    getDspHeader( request, dsp0 );
                    dsp0.setPageLink( "" );
                    dsp0.setPageRecords( "" );

                    strErr += lgLPC.getErrMsg();
                    dsp0.setErrMsg( strErr );
                    request.setAttribute( "err", "" );
                }
                else
                {
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
                        strErr += lgLPC.getErrMsg();
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
                        strErr += Message.getMessage( "erro.30001", "指定された条件に一致したデータ" ) + "<br />";
                        dsp0.setRecCnt( -99 );
                        dsp0.setErrMsg( strErr );
                        request.setAttribute( "err", "" );
                    }
                }
            }

            request.setAttribute( "FORM_ReserveCreditListPC", dsp0 );
            strCarrierUrl = "owner_rsv_credit_list.jsp";

            if ( dsp0.getRecCnt() != -99 )
            {
                pageRecords = "<span class=\"current\">" + stRow + "</span>～ <span class=\"current\">" + endRow + "</span>件 / 全<span class=\"current\">" + dsp0.getPageMax() + "</span>件";
                dsp0.setPageRecords( pageRecords );
            }

            if ( dsp0.getRecCnt() > ActionOwnerRsvCreditList.listmax )
            {
                queryString = "actionReserveCreditListPC.act?selHotelIDValue=" + dsp0.getSelHotelID() +
                        "&dspFlg=" + dspFlg + "&objKbn=" + dsp0.getObjKbn() + "&date_f=" + dsp0.getDateFrom() +
                        "&date_t=" + dsp0.getDateTo() + "&rsv_no=" + dsp0.getReserveNo();
                pageLinks = PagingDetails.getPagenationLink( dsp0.getPageAct(), ActionOwnerRsvCreditList.listmax, dsp0.getPageMax(), queryString );
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
    private void getDspHeader(HttpServletRequest request, FormOwnerRsvCreditList frm)
    {
        // 画面の値を取得
        if ( request.getParameter( "objKbn" ).toString().equals( "1" ) )
        {
            frm.setChk1Obj( "CHECKED" );
            frm.setChk2Obj( "" );
            frm.setChk3Obj( "" );
        }
        else if ( request.getParameter( "objKbn" ).toString().equals( "2" ) )
        {
            frm.setChk1Obj( "" );
            frm.setChk2Obj( "CHECKED" );
            frm.setChk3Obj( "" );
        }
        else if ( request.getParameter( "objKbn" ).toString().equals( "3" ) )
        {
            frm.setChk1Obj( "" );
            frm.setChk2Obj( "" );
            frm.setChk3Obj( "CHECKED" );
        }

        frm.setObjKbn( Integer.parseInt( request.getParameter( "objKbn" ) ) );
        frm.setDateFrom( request.getParameter( "date_f" ).toString() );
        frm.setDateTo( request.getParameter( "date_t" ).toString() );
        frm.setReserveNo( request.getParameter( "rsv_no" ).toString() );
    }

}
