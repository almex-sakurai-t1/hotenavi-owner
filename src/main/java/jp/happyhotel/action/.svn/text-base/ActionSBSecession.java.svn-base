package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.SelectCookie;
import jp.happyhotel.common.Url;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataUserSp;
import jp.happyhotel.user.UserRegistSBSp;

public class ActionSBSecession extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String actionType = "";
        Cookie[] cks = null;
        // 中間ID
        String openId = "";
        String returnURL = "";
        DataLoginInfo_M2 dataLoginInfo_M2;
        DataUserSp dus;

        try
        {
            // ユーザ情報取得
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            if ( dataLoginInfo_M2 == null )
            {
                // ログアウト状態の場合はエラー
                response.sendRedirect( Url.getUrl() + "/phone/index.jsp" );
                return;
            }

            UserRegistSBSp userRegistSB = new UserRegistSBSp();
            dus = new DataUserSp();
            // クッキーかopenIdから取得
            openId = SelectCookie.getCookieValue( request, "hhsbhappy" );
            Logging.info( "[ActionSBSecession]openId:" + openId );

            if ( openId.equals( "" ) == false )
            {
                dus.getDataBySuid( openId );
                userRegistSB.secession( request );

            }

            // 退会はかならずOKページへ
            returnURL = "ses_ok.jsp";
            requestDispatcher = request.getRequestDispatcher( returnURL );
            requestDispatcher.forward( request, response );
            return;
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionSoftBankCharge execute]Exception:" + e.toString() );
            try
            {
                request.setAttribute( "errMsg", e.toString() );
                requestDispatcher = request.getRequestDispatcher( "../mysoftbank_login_sp.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception ex )
            {
            }
        }
        finally
        {
        }

    }

}
