package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;

/**
 * 
 * 認証キャンセルIF
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionDocomoUserRegistCancel extends BaseAction
{
    final String CANCEL_URL = "/phone/mypage/mypage_regist_sp_cancel.jsp";

    /**
     * 認証キャンセルIF
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

            response.sendRedirect( CANCEL_URL );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionDocomoUserRegist.excute()] Exception:" + e.toString() );
        }
        finally
        {
        }
    }
}
