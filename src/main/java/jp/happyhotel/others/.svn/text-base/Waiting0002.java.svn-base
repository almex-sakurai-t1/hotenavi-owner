package jp.happyhotel.others;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMTenpo;

/**
 * 
 * ウェイティングデータ送信
 * 
 * @author S.Tashiro
 * @version 1.0 2012/12/17
 */

public class Waiting0002 extends HttpServlet implements javax.servlet.Servlet
{
    /**
     * Servlet初期処理
     * 
     */
    public void init(ServletConfig config) throws ServletException
    {
        Logging.info( "Waiting0001:loaded" );
    }

    /**
     * ウェイティングデータ送信
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int result = 1;
        String cmd;
        String tenpoid;
        String issuemode;
        String nowwaitcount;
        String nowwaittime;
        boolean ret = true;

        cmd = request.getParameter( "cmd" );
        tenpoid = request.getParameter( "tenpoid" );
        issuemode = request.getParameter( "issuemode" );
        nowwaitcount = request.getParameter( "nowwaitcount" );
        nowwaittime = request.getParameter( "nowwaittime" );

        if ( cmd == null || cmd.equals( "" ) != false )
        {
            ret = false;
        }
        if ( tenpoid == null || tenpoid.equals( "" ) != false )
        {
            ret = false;
        }
        if ( issuemode == null || issuemode.equals( "" ) != false || CheckString.numAlphaCheck( issuemode ) == false )
        {
            ret = false;

        }
        if ( nowwaitcount == null || nowwaitcount.equals( "" ) != false || CheckString.numCheck( nowwaitcount ) == false )
        {
            ret = false;

        }
        if ( nowwaittime == null || nowwaittime.equals( "" ) != false || CheckString.numCheck( nowwaittime ) == false )
        {
            ret = false;

        }
        Logging.info( "[Waiting0002.checkParam] ret=" + ret );

        // チェックがOKだった場合のみ
        if ( ret != false )
        {
            DataMTenpo dmt;
            dmt = new DataMTenpo();
            ret = dmt.getData( tenpoid );

            Logging.info( "[Waiting0002.getData]tenpoid=" + tenpoid + ", ret=" + ret );
            if ( ret != false )
            {
                dmt.setIssuemode( Integer.parseInt( issuemode ) );
                dmt.setNowwaitcount( Integer.parseInt( nowwaitcount ) );
                dmt.setNowwaittime( Integer.parseInt( nowwaittime ) );

                dmt.setLastupdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dmt.setLastuptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                ret = dmt.updateData( tenpoid );

                Logging.info( "[Waiting0002.update] tenpoid=" + tenpoid + ", ret=" + ret );
            }
        }
        if ( ret != false )
        {
            result = 0;
        }
        else
        {
            result = 1;
        }

        GenerateXmlWaitingHeader header;
        header = new GenerateXmlWaitingHeader();

        try
        {
            header.setRootNode( "root" );
            header.setCmd( cmd );
            header.setResult( result );

            String xmlOut = header.createXml( "Shift_JIS" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=Shift_JIS" );
            out.write( xmlOut.getBytes( "Shift_JIS" ) );
        }
        catch ( Exception exception )
        {
            header.setRootNode( "root" );
            header.setCmd( "0002" );
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
                Logging.error( "[Waiting0002.doGet() ]Exception:" + e.toString() );
            }
        }
    }
}
