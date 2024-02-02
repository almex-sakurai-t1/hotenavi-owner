package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;

/**
 * 
 * 決済中止IF
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionDocomoUserRegistStop extends BaseAction
{
    // final String STOP_URL = "https://demossl.happyhotel.jp/phone/mypage/mypage_regist_sp_error.jsp";
    final String              STOP_URL          = "/phone/mypage/mypage_regist_sp_error.jsp";
    private RequestDispatcher requestDispatcher = null;

    /**
     * 決済中止IF
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        boolean ret = true;
        int nContentLength = 0;
        ServletOutputStream stream;

        String sSpcd = "";
        String sCptok = "";

        try
        {
            sSpcd = request.getParameter( "sSpcd" );
            sCptok = request.getParameter( "sCptok" );

            // requestDispatcher = request.getRequestDispatcher( STOP_URL );
            // requestDispatcher.forward( request, response );
            response.sendRedirect( STOP_URL );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOrderInfoDocomo.excute()] Exception:" + e.toString() );
        }
        finally
        {
        }
    }
}
