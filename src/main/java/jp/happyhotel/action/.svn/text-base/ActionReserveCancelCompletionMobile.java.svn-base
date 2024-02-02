package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;

/**
 * 
 * キャンセル完了画面 Action
 */

public class ActionReserveCancelCompletionMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int type = 0;
        String paramUidLink = "";
        String carrierUrl = "";
        String url = "";
        String paramMode = "";
        String paramRsvNo = "";
        int paramHotelId = 0;
        int paramStatus = 0;
        int paramRsvDate = 0;
        int paramCancelCheck = 0;
        String userId = "";
        String loginMail = "";
        String rsvUserId = "";
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataLoginInfo_M2 dataLoginInfo_M2;

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
            else if ( type == UserAgent.USERAGENT_SMARTPHONE )
            {
                carrierUrl = "../../phone/reserve/";
            }

            // ログイン情報取得
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            if ( (dataLoginInfo_M2 != null) && (dataLoginInfo_M2.isMemberFlag() == true) )
            {
                // 存在する
                userId = dataLoginInfo_M2.getUserId();
                loginMail = dataLoginInfo_M2.getMailAddr();
            }
            else
            {
                url = carrierUrl + "reserve_nomember.jsp";
                response.sendRedirect( url );
                return;
            }

            // パラメータ取得
            paramMode = request.getParameter( "mode" );
            paramRsvNo = request.getParameter( "reserveNo" );
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelid" ) );
            paramStatus = Integer.parseInt( request.getParameter( "status" ) );
            paramRsvDate = Integer.parseInt( request.getParameter( "reserveDate" ) );
            if ( (request.getParameter( "cancelCheck" ) != null) && (request.getParameter( "cancelCheck" ).trim().length() != 0) )
            {
                paramCancelCheck = Integer.parseInt( request.getParameter( "cancelCheck" ) );
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
            frmSheet.setRsvDate( paramRsvDate );
            frmSheet.setCancelCheck( paramCancelCheck );
            frmSheet.setLoginUserId( userId );
            frmSheet.setUserId( userId );
            frmSheet.setMail( loginMail );

            if ( request.getParameter( "btnCancel" ) != null )
            {
                // ▼キャンセルボタン
                // ステータスチェック
                if ( rsvCmm.checkStatus( paramHotelId, paramRsvNo, paramStatus ) == false )
                {
                    // エラーあり
                    url = carrierUrl + "reserve_error.jsp";
                    request.setAttribute( "err", Message.getMessage( "warn.00019" ) );
                    request.setAttribute( "msg", "" );

                }
                else if ( frmSheet.getCancelCheck() == 0 )
                {
                    // キャンセルチェックが未チェック
                    frmSheet.setErrMsg( Message.getMessage( "warn.00032" ) );
                    logic.setFrm( frmSheet );
                    logic.getData( 1 );
                    frmSheet = logic.getFrm();

                    url = carrierUrl + "reserve_cancel.jsp";
                    request.setAttribute( "dsp", frmSheet );
                }
                else
                {
                    // エラーなし
                    frmSheet = execCancel( frmSheet );
                    if ( frmSheet.getErrMsg().trim().length() == 0 )
                    {
                        // 正常終了
                        url = carrierUrl + "reserve_cancel_completion.jsp";
                        request.setAttribute( "hotelId", paramHotelId );
                        request.setAttribute( "err", "" );
                        request.setAttribute( "msg", "予約を取り消しました。" );
                    }
                    else
                    {
                        // 取消失敗
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                        request.setAttribute( "msg", "" );
                    }
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                if ( type == UserAgent.USERAGENT_SMARTPHONE )
                {
                    url = "../../phone/mypage/mypage_rsv_list.jsp";
                }
                else
                {
                    url = "../free/mypage/mypage_rsv_list.jsp?" + paramUidLink;
                }
                response.sendRedirect( url );
                return;
            }

            // ここから携帯情報取得
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            type = UserAgent.getUserAgentType( request );

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
            Logging.error( "[ActionReserveCancelCompletionMobile.execute() ][hotelId = "
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
                Logging.error( "[ActionReserveCancelCompletionMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * キャンセルボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    private FormReserveSheetPC execCancel(FormReserveSheetPC frm) throws Exception
    {
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet;

        try
        {
            // データ更新（キャンセル処理）
            logic = new LogicOwnerRsvCheckIn();
            logic.setFrm( frm );

            if ( frm.getAdultNum() == -1 ) // 新予約
            {
                ret = logic.execRsvCancel( ReserveCommon.TERM_KIND_MOBILE, ReserveCommon.SCHEMA_NEWRSV );
            }
            else
            {
                ret = logic.execRsvCancel( ReserveCommon.TERM_KIND_MOBILE, ReserveCommon.SCHEMA_NEWRSV );
            }
            if ( ret == false )
            {
                frm.setErrMsg( Message.getMessage( "warn.00015" ) );
                return(frm);
            }

            // 登録データの取得
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            frm.setUserKbn( ReserveCommon.USER_KBN_USER );
            frm.setStatus( ReserveCommon.RSV_STATUS_CANCEL );
            frm.setMode( ReserveCommon.MODE_CANCEL );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionReserveCancelCompletionMobile.execCancel() ] " + e.getMessage() );
            throw new Exception( "[ActionReserveCancelCompletionMobile.execCancel() ] " + e.getMessage() );
        }
        finally
        {
            logic = null;
            logicSheet = null;
        }

        return(frm);
    }

}
