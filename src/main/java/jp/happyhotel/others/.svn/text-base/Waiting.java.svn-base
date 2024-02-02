package jp.happyhotel.others;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.Logging;

/**
 * 
 * ウェイティングデータ送信
 * 
 * @author S.Tashiro
 * @version 1.0 2012/12/17
 */

public class Waiting extends HttpServlet implements javax.servlet.Servlet
{

    /**
     *
     */
    private static final long serialVersionUID = -4904508722068259771L;

    /**
     * Servlet初期処理
     * 
     */
    public void init(ServletConfig config) throws ServletException
    {
        Logging.info( "Waiting:loaded" );
    }

    /**
     * ウェイティングデータ送信
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean ret = false;
        String cmd = request.getParameter( "cmd" );
        if ( cmd != null )
        {
            if ( cmd.equals( "0001" ) != false )
            {
                Waiting0001 wait0001;
                wait0001 = new Waiting0001();

                ret = true;
                wait0001.doGet( request, response );
                return;
            }
            else if ( cmd.equals( "0002" ) != false )
            {
                Waiting0002 wait0002;
                wait0002 = new Waiting0002();

                ret = true;
                wait0002.doGet( request, response );
                return;
            }
        }

        if ( ret == false )
        {
            GenerateXmlWaitingHeader header;
            header = new GenerateXmlWaitingHeader();

            header.setRootNode( "root" );

            header.setCmd( "0000" );
            header.setResult( 99 );

            try
            {
                String xmlOut = header.createXml( "Shift_JIS" );
                ServletOutputStream out = null;
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=Shift_JIS" );
                out.write( xmlOut.getBytes( "Shift_JIS" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[Waiting.doGet() ]Exception:" + e.toString() );

            }
        }
    }

    /**
     * ウェイティングデータ送信
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet( request, response );
    }
}
