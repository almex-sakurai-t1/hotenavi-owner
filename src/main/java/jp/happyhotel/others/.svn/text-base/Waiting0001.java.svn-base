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
import jp.happyhotel.data.DataTWaiting;

/**
 * 
 * ウェイティングデータ送信
 * 
 * @author S.Tashiro
 * @version 1.0 2012/12/17
 */

public class Waiting0001 extends HttpServlet implements javax.servlet.Servlet
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
        int result = 0;
        String cmd;
        String tenpoid;
        String managedate;
        String waitingcode;
        String receivetime;
        String callno;
        String waitingmode;
        String replacedate;
        String replacetime;
        String numguest;
        String holddate;
        String holdtime;
        String holdmode;
        String holdcount;
        String seattype;
        String waittime;
        boolean ret = true;

        cmd = request.getParameter( "cmd" );
        tenpoid = request.getParameter( "tenpoid" );
        managedate = request.getParameter( "managedate" );
        waitingcode = request.getParameter( "waitingcode" );
        receivetime = request.getParameter( "receivetime" );
        callno = request.getParameter( "callno" );
        waitingmode = request.getParameter( "waitingmode" );
        replacedate = request.getParameter( "replacedate" );
        replacetime = request.getParameter( "replacetime" );
        numguest = request.getParameter( "numguest" );
        holddate = request.getParameter( "holddate" );
        holdtime = request.getParameter( "holdtime" );
        holdmode = request.getParameter( "holdmode" );
        holdcount = request.getParameter( "holdcount" );
        seattype = request.getParameter( "seattype" );
        waittime = request.getParameter( "waittime" );

        if ( cmd == null || cmd.equals( "" ) != false )
        {
            Logging.info( "cmd is false" );
            ret = false;
        }
        if ( tenpoid == null || tenpoid.equals( "" ) != false )
        {
            Logging.info( "tenpoid is false" );
            ret = false;
        }
        if ( waitingcode == null || waitingcode.equals( "" ) != false || CheckString.numCheck( waitingcode ) == false )
        {
            Logging.info( "waitingcode is false" );
            ret = false;

        }
        if ( receivetime == null || receivetime.equals( "" ) != false || CheckString.numCheck( receivetime ) == false )
        {
            Logging.info( "receivetime is false" );
            ret = false;

        }
        if ( callno == null || callno.equals( "" ) != false || CheckString.numCheck( callno ) == false )
        {
            Logging.info( "callno is false" );
            ret = false;

        }
        if ( waitingmode == null || waitingmode.equals( "" ) != false || CheckString.numCheck( waitingmode ) == false )
        {
            Logging.info( "waitingmode is false" );
            ret = false;

        }
        if ( numguest == null || numguest.equals( "" ) != false || CheckString.numCheck( numguest ) == false )
        {
            Logging.info( "numguest is false" );
            ret = false;

        }
        if ( seattype == null || seattype.equals( "" ) != false )
        {
            Logging.info( "seattype is false" );
            ret = false;
        }

        // 必須以外のパラメータ
        if ( replacedate == null || replacedate.equals( "" ) != false || CheckString.numCheck( replacedate ) == false )
        {
            replacedate = "-1";
        }

        // managedateがなかったらreplacedateを使用する
        if ( managedate == null || managedate.equals( "" ) != false || CheckString.numAlphaCheck( managedate ) == false )
        {
            if ( replacedate.equals( "-1" ) == false )
            {
                managedate = replacedate;
            }
            else
            {
                Logging.info( "managedate is false" );
                ret = false;
            }
        }

        if ( replacetime == null || replacetime.equals( "" ) != false || CheckString.numCheck( replacetime ) == false )
        {
            replacetime = "-1";
        }
        if ( holddate == null || holddate.equals( "" ) != false || CheckString.numCheck( holddate ) == false )
        {
            holddate = "-1";
        }
        if ( holdtime == null || holdtime.equals( "" ) != false || CheckString.numCheck( holdtime ) == false )
        {
            holdtime = "-1";
        }
        if ( holdmode == null || holdmode.equals( "" ) != false || CheckString.numCheck( holdmode ) == false )
        {
            holdmode = "-1";
        }
        if ( holdcount == null || holdcount.equals( "" ) != false || CheckString.numCheck( holdcount ) == false )
        {
            holdcount = "-1";
        }
        if ( waittime == null || waittime.equals( "" ) != false || CheckString.numCheck( waittime ) == false )
        {
            waittime = "-1";
        }
        Logging.info( "[Waiting0001.checkParameter]  ret=" + ret );

        if ( ret != false )
        {

            DataTWaiting dtw;
            dtw = new DataTWaiting();
            ret = dtw.getData( tenpoid, Integer.parseInt( managedate ), Integer.parseInt( waitingcode ) );
            Logging.info( "[Waiting0001.getData]tenpoid=" + tenpoid + ", managedate=" + managedate + ", waitingcode=" + waitingcode + ", ret=" + ret );

            dtw.setTenpoid( tenpoid );
            dtw.setManagedate( Integer.parseInt( managedate ) );
            dtw.setWaitingcode( Integer.parseInt( waitingcode ) );
            dtw.setReceivetime( Integer.parseInt( receivetime ) );
            dtw.setCallno( Integer.parseInt( callno ) );
            dtw.setWaitingmode( Integer.parseInt( waitingmode ) );

            // データチェックして問題なければセット
            if ( Integer.parseInt( replacedate ) >= 0 )
            {
                dtw.setReplacedate( Integer.parseInt( replacedate ) );
            }

            if ( Integer.parseInt( replacetime ) >= 0 )
            {
                dtw.setReplacetime( Integer.parseInt( replacetime ) );
            }

            dtw.setNumguest( Integer.parseInt( numguest ) );
            if ( Integer.parseInt( holddate ) >= 0 )
            {
                dtw.setHolddate( Integer.parseInt( holddate ) );
            }

            if ( Integer.parseInt( holdtime ) >= 0 )
            {
                dtw.setHoldtime( Integer.parseInt( holdtime ) );
            }

            if ( Integer.parseInt( holdmode ) >= 0 )
            {
                dtw.setHoldmode( Integer.parseInt( holdmode ) );
            }

            if ( Integer.parseInt( holdcount ) >= 0 )
            {
                dtw.setHoldcount( Integer.parseInt( holdcount ) );
            }

            dtw.setSeattype( seattype );

            if ( Integer.parseInt( waittime ) >= 0 )
            {
                dtw.setWaittime( Integer.parseInt( waittime ) );
            }

            dtw.setLastupdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dtw.setLastuptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            if ( ret != false )
            {
                ret = dtw.updateData( tenpoid, Integer.parseInt( managedate ), Integer.parseInt( waitingcode ) );
                Logging.info( "[Waiting0001.update] tenpoid=" + tenpoid + ", managedate=" + managedate + ", waitingcode=" + waitingcode + ", ret=" + ret );
            }
            else
            {
                ret = dtw.insertData();
                Logging.info( "[Waiting0001.insert] ret=" + ret );
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

            header.setCmd( "0001" );
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
                Logging.error( "[Waiting0001.doGet() ]Exception:" + e.toString() );
            }
        }
    }
}
