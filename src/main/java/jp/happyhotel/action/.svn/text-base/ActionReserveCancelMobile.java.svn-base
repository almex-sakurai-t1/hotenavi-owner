package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;

/**
 * 
 * 予約取消画面 Action Class
 */

public class ActionReserveCancelMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int type = 0;
        String paramUidLink = "";
        String userId = "";
        String loginMail = "";
        ArrayList<String> loginMailList = new ArrayList<String>();
        String carrierUrl = "";
        String errMsg = "";
        String url = "";
        String paramMode = "";
        String paramRsvNo = "";
        String rsvUserId = "";
        int paramHotelId = 0;
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataLoginInfo_M2 dataLoginInfo_M2;

        String checkHotelId = "";
        String checkRsvNo = "";
        String checkMode = "";

        try
        {
            // キャリアの判別
            type = UserAgent.getUserAgentType( request );
            if ( type == UserAgent.USERAGENT_AU )
            {
                carrierUrl = "../../au/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                carrierUrl = "../../y/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                carrierUrl = "../../i/reserve/";
            }

            // URLパラメータを不正に変更された場合は、エラーページへ遷移させる
            checkHotelId = request.getParameter( "hotelid" );
            checkRsvNo = request.getParameter( "reserveNo" );
            checkMode = request.getParameter( "mode" );

            // パラメータが消された場合のチェック
            if ( (checkHotelId == null) || (checkRsvNo == null) || (checkMode == null) )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30009", "パラメータなし" );
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // ログイン情報取得
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            if ( (dataLoginInfo_M2 != null) && (dataLoginInfo_M2.isMemberFlag() == true) )
            {
                // 存在する
                userId = dataLoginInfo_M2.getUserId();

                // メールアドレスのチェック
                if ( dataLoginInfo_M2.getMailAddrMobile().equals( "" ) == false )
                {
                    loginMail = dataLoginInfo_M2.getMailAddrMobile();
                    loginMailList.add( dataLoginInfo_M2.getMailAddrMobile() );
                }
                if ( dataLoginInfo_M2.getMailAddr().equals( "" ) == false )
                {
                    if ( loginMail.equals( "" ) != false )
                    {
                        loginMail = dataLoginInfo_M2.getMailAddr();
                    }
                    loginMailList.add( dataLoginInfo_M2.getMailAddr() );
                }
            }
            else
            {
                url = carrierUrl + "reserve_nomember.jsp";
                response.sendRedirect( url );
                return;
            }

            // 処理モードが、既定以外の値の場合のチェック
            if ( !(checkMode.equals( ReserveCommon.MODE_CANCEL )) )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30009", "処理モード不正" );
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // 数値のパラメータに、数値以外が指定された場合のチェック
            try
            {
                // パラメータ取得
                paramHotelId = Integer.parseInt( request.getParameter( "hotelid" ) );
                paramRsvNo = request.getParameter( "reserveNo" );
                paramMode = request.getParameter( "mode" );
                paramUidLink = (String)request.getAttribute( "UID-LINK" );
            }
            catch ( Exception exception )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30009", "パラメータ値不正" );
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // クッキーのユーザーIDと予約の作成者が同じか確認
            rsvUserId = rsvCmm.getRsvUserId( paramRsvNo );

            if ( userId.compareTo( rsvUserId ) != 0 )
            {
                // 違う場合はエラーページ
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30004" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // フォームにセット
            frmSheet.setSelHotelId( paramHotelId );
            frmSheet.setRsvNo( paramRsvNo );
            frmSheet.setMode( paramMode );

            // データを抽出し画面に設定
            logic.setFrm( frmSheet );
            logic.getData( 1 );
            frmSheet = logic.getFrm();

            frmSheet.setUserId( userId );
            frmSheet.setMail( loginMail );
            frmSheet.setMailList( loginMailList );

            // ▼キャンセルボタン
            // データのチェック
            frmSheet = rsvCmm.chkDspMaster( frmSheet );
            if ( frmSheet.getErrMsg().trim().length() != 0 )
            {
                // エラーあり
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", frmSheet.getErrMsg() );

            }
            else
            {
                // エラーなし
                url = carrierUrl + "reserve_cancel.jsp";
                request.setAttribute( "dsp", frmSheet );
            }

            // デバッグ環境かどうか
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                url = "/_debug_" + url;
            }
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveCancelMobile.execute() ][hotelId = "
                    + paramHotelId + ",reserveNo = " + paramRsvNo + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveCancelMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

}
