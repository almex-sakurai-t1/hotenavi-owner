package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DecodeData;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvCredit;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.user.UserBasicInfo;

/**
 * 
 * 予約詳細画面 Action Class
 */

public class ActionReserveSheetPC extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String strCarrierUrl = "";
        int hotelId = 0;
        String rsvNo = "";
        int rsvSubNo = 0;
        String mailAddr = "";
        String procMode = "";
        String rsvUserId = "";
        String userId = "";
        int ownerUserId = 0;
        String errMsg = "";
        String userKbn = ReserveCommon.USER_KBN_USER;
        ArrayList<String> mailAddrList = new ArrayList<String>();
        FormReserveSheetPC frm = new FormReserveSheetPC();
        FormReserveSheetPC frmOld = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataLoginInfo_M2 dataLoginInfo_M2;
        boolean isLoginUser = false;

        String checkHotelId = "";
        String checkRsvNo = "";
        String checkRsvSubNo = "";
        String checkMode = "";
        String checkUserKbn = "";
        String maskCardNo = "";

        try
        {
            // URLパラメータを不正に変更された場合は、エラーページへ遷移させる
            checkHotelId = request.getParameter( "id" );
            checkRsvNo = request.getParameter( "rsvno" );
            checkRsvSubNo = request.getParameter( "rsvsubno" );
            checkMode = request.getParameter( "mode" );
            checkUserKbn = request.getParameter( "usr" );

            // パラメータが消された場合のチェック
            if ( (checkHotelId == null) || (checkRsvNo == null) || (checkMode == null) || (checkUserKbn == null) || (checkRsvSubNo == null && checkMode.equals( ReserveCommon.MODE_HISTORY )) )
            {
                errMsg = Message.getMessage( "erro.30009", "パラメータなし" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // 処理モードが、既定以外の値の場合のチェック
            if ( !(checkMode.equals( ReserveCommon.MODE_CANCEL )) && !(checkMode.equals( ReserveCommon.MODE_DETL )) &&
                    !(checkMode.equals( ReserveCommon.MODE_RAITEN )) && !(checkMode.equals( ReserveCommon.MODE_UNDO_CANCEL )) && !(checkMode.equals( ReserveCommon.MODE_HISTORY )) )
            {
                errMsg = Message.getMessage( "erro.30009", "処理モード不正" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // ユーザ区分が、既定以外の値の場合のチェック
            if ( !(checkUserKbn.equals( ReserveCommon.USER_KBN_USER )) && !(checkUserKbn.equals( ReserveCommon.USER_KBN_OWNER )) )
            {
                errMsg = Message.getMessage( "erro.30009", "ユーザ区分不正" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // 数値のパラメータに、数値以外が指定された場合のチェック
            try
            {
                // パラメータ取得
                hotelId = Integer.parseInt( request.getParameter( "id" ) );
                rsvNo = request.getParameter( "rsvno" );
                procMode = request.getParameter( "mode" );
                userKbn = request.getParameter( "usr" );
                if ( checkMode.equals( ReserveCommon.MODE_HISTORY ) )
                {
                    rsvSubNo = Integer.parseInt( request.getParameter( "rsvsubno" ) );
                }
            }
            catch ( Exception exception )
            {
                errMsg = Message.getMessage( "erro.30009", "パラメータ値不正" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // ログインユーザー情報取得
            if ( userKbn.equals( ReserveCommon.USER_KBN_USER ) )
            {
                // ユーザーの場合
                dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

                if ( dataLoginInfo_M2 == null )
                {
                    // ログアウト状態の場合はエラー
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_nomember.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }

                if ( dataLoginInfo_M2.isMemberFlag() == true )
                {
                    // 存在する
                    isLoginUser = true;
                    userId = dataLoginInfo_M2.getUserId();
                }
            }
            else if ( userKbn.equals( ReserveCommon.USER_KBN_OWNER ) )
            {
                // オーナーの場合
                ownerUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
                if ( ownerUserId != 0 )
                {
                    // 存在する
                    isLoginUser = true;
                }
                userId = Integer.toString( ownerUserId );
            }

            if ( (isLoginUser == false) || ((!userKbn.equals( ReserveCommon.USER_KBN_USER )) && (!userKbn.equals( ReserveCommon.USER_KBN_OWNER ))) )
            {
                // ログインユーザーが存在しない場合はエラー
                errMsg = Message.getMessage( "erro.30004" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // クッキーのユーザーIDと予約の作成者が同じか確認
            if ( userKbn.equals( ReserveCommon.USER_KBN_USER ) )
            {
                rsvUserId = rsvCmm.getRsvUserId( rsvNo );
                if ( userId.compareTo( rsvUserId ) != 0 )
                {
                    // 違う場合はエラーページ
                    errMsg = Message.getMessage( "erro.30004" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
            }

            // フォームにセット
            frm.setSelHotelId( hotelId );
            frm.setRsvNo( rsvNo );
            if ( checkMode.equals( ReserveCommon.MODE_HISTORY ) )
            {
                frm.setRsvSubNo( rsvSubNo );
            }

            // 予約データ抽出
            logic.setFrm( frm );
            if ( checkMode.equals( ReserveCommon.MODE_HISTORY ) )
            {
                logic.getData( 99 );
                frmOld.setSelHotelId( hotelId );
                frmOld.setRsvNo( rsvNo );
                if ( rsvSubNo > 0 )
                {
                    // 前回データ取得
                    rsvSubNo--;
                }
                frmOld.setRsvSubNo( rsvSubNo );
                logic.setFrm( frmOld );
                logic.getData( 99 );
            }
            else
            {
                logic.getData( 2 );
            }

            frm.setMail( mailAddr );
            if ( procMode.equals( ReserveCommon.MODE_DETL ) )
            {
                frm.setMode( "" ); // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
            }
            else
            {
                frm.setMode( procMode );
            }
            frm.setErrMsg( "" );
            frm.setUserKbn( userKbn );

            if ( frm.getUserId().equals( "" ) == false )
            {
                // 予約登録したユーザーのメールアドレス取得
                UserBasicInfo ubi = new UserBasicInfo();
                ubi.getUserBasic( frm.getUserId() );
                frm.setLoginUserId( frm.getUserId() );
                frm.setLoginUserTel( ubi.getUserInfo().getTel1() );

                // メールアドレスをセット
                if ( ubi.getUserInfo().getMailAddr().equals( "" ) == false )
                {
                    mailAddr = ubi.getUserInfo().getMailAddr();
                    mailAddrList.add( ubi.getUserInfo().getMailAddr() );
                }
                if ( ubi.getUserInfo().getMailAddrMobile().equals( "" ) == false )
                {
                    mailAddr = ubi.getUserInfo().getMailAddrMobile();
                    mailAddrList.add( ubi.getUserInfo().getMailAddrMobile() );
                }
                frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                frm.setMailList( mailAddrList );
            }

            // カード番号取得
            if ( ReserveCommon.checkNoShowCreditHotel( frm.getSelHotelId() ) == true )
            {
                DataRsvCredit rsvcredit = new DataRsvCredit();
                rsvcredit.getData( frm.getRsvNo() );
                String dispCardNo = DecodeData.decodeString( "axpol ptmhyeeahl".getBytes( "8859_1" ), "s h t t i s n h ".getBytes( "8859_1" ), new String( rsvcredit.getCard_no() ) );
                frm.setCardno( dispCardNo );
                for( int i = 0 ; i < dispCardNo.length() ; i++ )
                {
                    if ( i < dispCardNo.length() - 4 )
                    {
                        maskCardNo += "*";
                    }
                    else
                    {
                        maskCardNo += dispCardNo.charAt( i );
                    }
                }
                frm.setCardno( frm.getCardno() );
                frm.setDispcardno( maskCardNo );
                frm.setExpireMonth( rsvcredit.getLimit_date() % 100 );
                frm.setExpireYear( rsvcredit.getLimit_date() / 100 );
            }

            request.setAttribute( "FORM_ReserveSheetPC", frm );
            request.setAttribute( "FORM_ReserveSheetPC_OLD", frmOld );
            if ( userKbn.equals( ReserveCommon.USER_KBN_OWNER ) )
            {
                if ( checkMode.equals( ReserveCommon.MODE_HISTORY ) )
                {
                    strCarrierUrl = "/owner/owner_rsv_history_sheet_PC.jsp";
                }
                else
                {
                    strCarrierUrl = "/owner/owner_rsv_sheet_PC.jsp";
                }
            }
            else
            {
                strCarrierUrl = "/reserve/reserve_sheet_PC.jsp";
            }

            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveSheetPC.execute() ][hotelId = "
                    + hotelId + " reserveNo = " + rsvNo + " ] Exception", exception );
            try
            {
                if ( userKbn.equals( ReserveCommon.USER_KBN_USER ) )
                {
                    errMsg = Message.getMessage( "erro.30005" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                }
                else
                {
                    errMsg = Message.getMessage( "erro.30005" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                }
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveSheetPC.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

}
