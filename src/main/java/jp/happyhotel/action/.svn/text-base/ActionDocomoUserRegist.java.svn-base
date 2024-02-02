package jp.happyhotel.action;

import java.net.URLDecoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CreateToken;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataUserSp;

/**
 * 
 * 受付完了IF
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionDocomoUserRegist extends BaseAction
{
    final String TOP_URL    = Url.getUrl() + "/phone/";
    final String REGIST_URL = Url.getSslUrl() + "/phone/mypage/mypage_regist.jsp";
    final String ERROR_URL  = Url.getSslUrl() + "/phone/mypage/mypage_regist_sp_error.jsp";

    /**
     * 受付完了IF
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int CHARGE = 1;
        final int REGIST_POINT = 1000001; // 有料入会ポイントのポイントコード
        final int DOCOMO = 1;
        final int RS_MEMBER = 9;
        final String OPENID = "";
        boolean ret = false;
        boolean retins = false;
        boolean addFlag = false;
        int nContentLength = 0;
        String sendURL = "";
        ServletOutputStream stream;
        DataLoginInfo_M2 dataLoginInfo;
        DataUserSp dus;
        CreateToken cToken = new CreateToken();
        Cookie cookieSp = null;
        String sTerkn = "";
        String sStkn = "";
        String strSuid = "";

        try
        {
            sTerkn = request.getParameter( "sTerkn" );
            sStkn = request.getParameter( "sStkn" );
            dataLoginInfo = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            dus = new DataUserSp();
            cookieSp = this.getCookies( request, "hhihappy" );

            Logging.info( "sTerkn:" + sTerkn );
            Logging.info( "sStkn:" + sStkn );

            if ( sTerkn == null )
            {
                sTerkn = "";
            }
            if ( sStkn == null )
            {
                sStkn = "";
            }
            while( true )
            {
                if ( sStkn.equals( "" ) != false || sTerkn.equals( "01" ) != false )
                {
                    ret = false;
                }
                ret = true;
                break;
            }

            sStkn = URLDecoder.decode( sStkn, "Shift_JIS" );
            Logging.info( "sStkn2:" + sStkn );
            if ( cookieSp != null )
            {
                strSuid = cookieSp.getValue();
            }

            // 取得したトークンからデータが取得できたか
            if ( dus.getDataByToken( sStkn ) != false )
            {
                if ( dus.getDelFlag() == 1 )
                {
                    sendURL = TOP_URL;
                }
                else
                {
                    if ( dus.getUserId().equals( dus.getOpenId() ) != false )
                    {
                        sendURL = REGIST_URL;
                    }
                    else
                    {
                        sendURL = TOP_URL;
                    }
                }
            }
            else
            {
                sendURL = ERROR_URL;
            }

            if ( ret != false )
            {
                response.sendRedirect( sendURL );
            }
            else
            {
                response.sendRedirect( ERROR_URL );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOrderInfoDocomo.excute()] Exception:" + e.toString() );
        }
        finally
        {
        }
    }

    /**
     * クッキー取得メソッド
     * 
     * @param request リクエスト
     * @param indexStr 検索文字列
     * @return
     */
    public Cookie getCookies(HttpServletRequest request, String indexStr)
    {
        Cookie[] cookies = null;
        Cookie cookieSp = null;
        int loop = 0;

        cookies = request.getCookies();
        if ( cookies != null )
        {
            for( loop = 0 ; loop < cookies.length ; loop++ )
            {
                if ( cookies[loop].getName().compareTo( indexStr ) == 0 )
                {
                    cookieSp = cookies[loop];
                    break;
                }
            }
        }
        return cookieSp;
    }

}
