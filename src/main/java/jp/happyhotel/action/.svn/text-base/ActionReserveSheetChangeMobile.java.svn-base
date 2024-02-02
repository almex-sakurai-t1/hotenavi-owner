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
 * 予約票画面 Action
 */

public class ActionReserveSheetChangeMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String carrierUrl = "";
        String errMsg = "";
        String loginMail = "";
        ArrayList<String> loginMailList = new ArrayList<String>();
        String userId = "";
        int type = 0;
        String paramUidLink = "";
        int paramHotelId = 0;
        String paramRsvNo = "";
        String url = "";
        String rsvUserId = "";
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataLoginInfo_M2 dataLoginInfo_M2;

        String checkRsvNo = "";

        try
        {
            // キャリアの判別
            type = UserAgent.getUserAgentType( request );
            if ( type == UserAgent.USERAGENT_AU )
            {
                carrierUrl = "../au/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                carrierUrl = "../y/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                carrierUrl = "../i/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_SMARTPHONE )
            {
                carrierUrl = "../phone/reserve/";
            }

            // 携帯サイト、PCサイトの予約票のQRコードのどちらから呼ばれたか判断する
            if ( request.getRequestURL().indexOf( "happyhotel.jp/reserve/" ) == -1 )
            {
                // URLに、i/、au/、y/が含まれていると判断して、URLの階層を携帯用に変更
                carrierUrl = "../" + carrierUrl;
            }

            // ログイン情報取得
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            if ( (dataLoginInfo_M2 != null) && (dataLoginInfo_M2.isMemberFlag() == true) )
            {
                // 存在する
                userId = dataLoginInfo_M2.getUserId();
                // メールアドレスをセット
                if ( dataLoginInfo_M2.getMailAddr().equals( "" ) == false )
                {
                    loginMail = dataLoginInfo_M2.getMailAddr();
                    loginMailList.add( dataLoginInfo_M2.getMailAddr() );
                }
                if ( dataLoginInfo_M2.getMailAddrMobile().equals( "" ) == false )
                {
                    loginMail = dataLoginInfo_M2.getMailAddrMobile();
                    loginMailList.add( dataLoginInfo_M2.getMailAddrMobile() );
                }

            }
            else
            {
                url = carrierUrl + "reserve_nomember.jsp";
                response.sendRedirect( url );
                return;
            }

            // URLパラメータを不正に変更された場合は、エラーページへ遷移させる
            // QRコードを読み取った場合は、ホテルIDはnullとなるため、ホテルIDのチェックは除く
            checkRsvNo = request.getParameter( "reserveNo" );

            // パラメータが消された場合のチェック
            if ( checkRsvNo == null )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30009", "パラメータなし" );
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // 引数の取得
            paramRsvNo = request.getParameter( "reserveNo" );
            if ( (request.getParameter( "hotelid" ) == null) && (paramRsvNo != null) )
            {
                // ホテルID取得
                paramHotelId = logic.getHotelId( paramRsvNo );
            }
            else
            {
                // 数値のパラメータに、数値以外が指定された場合のチェック
                try
                {
                    // パラメータ取得
                    paramHotelId = Integer.parseInt( request.getParameter( "hotelid" ) );
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
            }

            paramUidLink = (String)request.getAttribute( "UID-LINK" );

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
            frm.setSelHotelId( paramHotelId );
            frm.setRsvNo( paramRsvNo );

            // 予約データ抽出
            logic.setFrm( frm );
            logic.getData( 2 );
            frm = logic.getFrm();

            frm.setMail( loginMail );
            frm.setMode( ReserveCommon.MODE_DETL );
            frm.setErrMsg( "" );
            frm.setUserId( userId );
            frm.setUserKbn( ReserveCommon.USER_KBN_USER );
            frm.setLoginUserMail( loginMail );
            frm.setMailList( loginMailList );

            url = carrierUrl + "reserve_sheet.jsp";
            request.setAttribute( "dsp", frm );

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
            Logging.error( "[ActionReserveSheetChangeMobile.execute() ][hotelId = "
                    + paramHotelId + ",reserveNo = " + paramRsvNo + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "dsp", null );
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveSheetChangeMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }
}
