package jp.happyhotel.action;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;

/**
 * 
 * 検索結果flashデモページ表示クラス
 * 
 * @author N.Ide
 * @version 1.0 2009/10/13
 */

public class ActionSearchFlashDemo extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    /**
     * 住所検索の結果一覧をFlashで表示する
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     * @see "../search_result_flash_error.jsp Flfastがエラーを出力した際に表示"
     * @see "../../index.jsp 予期しないエラーの場合に表示"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int count, xmlCount, loopCount = 0;
        String paramUidLink = null;
        String userAgent = "";
        String errCode = "";
        InputStream in = null, xmlIn = null;
        OutputStream out = null, xmlOut = null;
        byte[] readBuff = new byte[1024];
        byte[] readBuffXml = new byte[1024];
        final String XMLFilePass = "/usr/local/tomcat/temp/searchdemo.xml";
        final String confFilePass = "/etc/happyhotel/test.conf";
        FileInputStream propfile = null;
        Properties config;
        String baseDir = "/happyhotel/flash/flfast";
        String flfastCmd = "/bin/flfast";
        String flfastConfig = "/bin/flfast.conf";
        String cmd;
        Runtime rt;
        Process pr;

        try
        {
            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            // ユーザーエージェントの取得
            userAgent = URLEncoder.encode( UserAgent.getUserAgent( request ), "SHIFT_JIS" );

            // Flfast起動時のコマンドを生成
            flfastConfig = baseDir + flfastConfig;
            flfastCmd = baseDir + flfastCmd;
            cmd = flfastCmd + " -u" + userAgent + " -f" + flfastConfig;

            // コマンドを実行
            rt = Runtime.getRuntime();
            pr = rt.exec( cmd );
            xmlOut = pr.getOutputStream();
            in = pr.getInputStream();

            try
            {
                // xmlファイルの読み込み
                xmlIn = new BufferedInputStream( new FileInputStream( XMLFilePass ) );
                // xmlIn = new BufferedInputStream(new FileInputStream("/usr/local/tomcat/temp/05001014503955_me.ezweb.ne.jp.txt"));
                while( true )
                {
                    xmlCount = xmlIn.read( readBuffXml );
                    if ( xmlCount == -1 )
                    {
                        break;
                    }
                    // xmlデータをプロセスに流し込む
                    for( int j = 0 ; j < xmlCount ; j++ )
                    {
                        xmlOut.write( readBuffXml[j] );
                    }
                }
                // streamをclose
                xmlIn.close();
                xmlOut.close();
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionAreaSearchMobile_M2_Flash] Exception 1 = " + e.toString() );
            }
            // Flfastで生成されたswfを表示
            try
            {
                while( true )
                {
                    count = in.read( readBuff );
                    // ERRCODEで始まっていたらエラーとして処理
                    if ( loopCount == 0 )
                    {
                        if ( new String( readBuff ).substring( 0, 7 ).compareTo( "ERRCODE" ) == 0 )
                        {
                            errCode = new String( readBuff ).substring( 10, 14 );
                            Logging.error( "[ActionAreaSearchMobile_M2_Flash] ERRCODE = " + errCode );
                            /*
                             * request.setAttribute( "ERROR-CODE", errCode );
                             * requestDispatcher = request.getRequestDispatcher( "../search_result_flash_error.jsp?" + paramUidLink );
                             * requestDispatcher.forward( request, response );
                             */
                            response.sendRedirect( "search_result_flash_error.jsp?" + paramUidLink + "&code=" + errCode );
                            break;
                        }
                        else
                        {
                            out = response.getOutputStream();
                        }
                        loopCount++;
                    }

                    if ( count == -1 )
                    {
                        break;
                    }
                    response.setContentType( "application/x-shockwave-flash" );
                    for( int i = 0 ; i < count ; i++ )
                    {
                        out.write( readBuff[i] );
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionAreaSearchMobile_M2_Flash] Exception 2 = " + e.toString() );
            }
            finally
            {
                out.close();
                in.close();
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearchMobile_M2_Flash.execute() ] Exception", exception );
            try
            {
                response.sendRedirect( "../../index.jsp?" + paramUidLink );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionAreaSearchMobile_M2_Flash.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
        }
    }
}
