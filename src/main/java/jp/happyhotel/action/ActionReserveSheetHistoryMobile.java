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
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;

/**
 *
 * 履歴詳細画面 Action
 */

public class ActionReserveSheetHistoryMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int type = 0;
        String carrierUrl = "";
        String paramUidLink = "";
        String url = "";
        String paramRsvNo = "";
        String rsvUserId = "";
        String userId = "";
        int paramHotelId = 0;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataLoginInfo_M2 dataLoginInfo_M2;

        try
        {

            //ログイン情報取得
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            if ((dataLoginInfo_M2 != null) && (dataLoginInfo_M2.isMemberFlag() == true)) {
                //存在する
                userId = dataLoginInfo_M2.getUserId();
            }

            //キャリアの判別
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

            // 引数の取得
            paramHotelId =  Integer.parseInt( request.getParameter( "hotelid" ) );
            paramRsvNo = request.getParameter( "reserveNo" );

            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            //クッキーのユーザーIDと予約の作成者が同じか確認
            rsvUserId = rsvCmm.getRsvUserId( paramRsvNo );

            if (userId.compareTo( rsvUserId ) != 0) {
              //違う場合はエラーページ
              url = carrierUrl + "reserve_error.jsp";
              request.setAttribute( "err", Message.getMessage( "erro.30004" ) );
              requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
              requestDispatcher.forward( request, response );
              return;
            }

            // ホテルID、予約番号を元に抽出
            frm.setSelHotelId( paramHotelId );
            frm.setRsvNo( paramRsvNo );
            logic.setFrm( frm );
            logic.getData( 1 );
            frm = logic.getFrm();

            request.setAttribute( "dsp", frm );

            // ここから携帯情報取得
            //キャリアの判別
            type = UserAgent.getUserAgentType( request );
            if ( type == UserAgent.USERAGENT_AU )
            {
                url = "../../au/reserve/reserve_sheet_history.jsp";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                url = "../../y/reserve/reserve_sheet_history.jsp";
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                url = "../../i/reserve/reserve_sheet_history.jsp";
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
            Logging.error( "[ActionReserveSheetHistoryMobile.execute() ][hotelId = "
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
                Logging.error( "[ActionReserveSheetHistoryMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

}
