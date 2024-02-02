package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TimeParseYahooWallet;
import jp.happyhotel.data.DataUserSp;
import jp.happyhotel.others.GenerateXmlYB;
import jp.happyhotel.user.UserRegistYWallet;

public class ActionYBFix extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int YWALLET = 4;
        int nIndex = 0;
        int nIndex2 = 0;
        String stid;
        String userId;
        String purchaseStatus;
        String pid;
        String time;
        boolean retParameter = false;
        boolean retRequest = false;
        boolean retResult = false;
        boolean ret = false;
        DataUserSp dus;
        TimeParseYahooWallet timeParse;

        stid = request.getParameter( "stid" );
        userId = request.getParameter( "userid" );
        purchaseStatus = request.getParameter( "purchase_status" );
        pid = request.getParameter( "pid" );
        time = request.getParameter( "time" );

        timeParse = new TimeParseYahooWallet();
        Logging.info( "[ActionYBFix] Loaded" );
        Logging.info( "[ActionYBFix] stid:" + stid );
        Logging.info( "[ActionYBFix] userId:" + userId );
        Logging.info( "[ActionYBFix] purchaseStatus:" + purchaseStatus );
        Logging.info( "[ActionYBFix] pid:" + pid );
        Logging.info( "[ActionYBFix] time:" + time );

        while( true )
        {
            if ( stid == null || stid.equals( "" ) != false || CheckString.numCheck( stid ) == false || stid.length() != 12 )
            {
                break;
            }
            if ( userId == null || userId.equals( "" ) != false )
            {
                break;
            }
            if ( purchaseStatus == null || purchaseStatus.equals( "" ) != false )
            {
                break;
            }
            if ( pid == null || pid.equals( "" ) != false || CheckString.numCheck( pid ) == false )
            {
                break;
            }
            if ( time == null || time.equals( "" ) != false )
            {
                break;
            }
            else
            {
                retParameter = timeParse.parseTime( time );
            }
            break;
        }
        try
        {
            if ( retParameter != false )
            {
                if ( purchaseStatus.equals( "ST1" ) != false )
                {
                    retRequest = true;
                }
                else
                {
                    retRequest = false;
                }
            }

            Logging.info( "[ActionYBFix] retParameter:" + retParameter + ", retRequest" + retRequest );

            ServletOutputStream out = null;
            GenerateXmlYB responseXml = new GenerateXmlYB();

            if ( retRequest != false )
            {
                // 注文番号をセットする
                request.setAttribute( "ORDER_NO", stid );
                request.setAttribute( "DATE", timeParse.getDate() );
                request.setAttribute( "TIME", timeParse.getTime() );
                request.setAttribute( "OPEN_ID", userId );
                request.setAttribute( "TOKEN", purchaseStatus );

                UserRegistYWallet registYW = new UserRegistYWallet();
                ret = registYW.registPay( request );

                // ユーザの登録ができた場合
                if ( ret != false )
                {
                    responseXml.setRootNode( "root" );
                    responseXml.setStatus( 0 );
                    responseXml.setPurchase( 0 );
                }
                else
                {
                    responseXml.setRootNode( "root" );
                    responseXml.setStatus( 1 );
                    responseXml.setPurchase( 1 );
                }
            }
            else
            {
                responseXml.setRootNode( "root" );
                responseXml.setStatus( 1 );
                responseXml.setPurchase( 1 );
            }
            String xmlOut = responseXml.createXml();
            Logging.info( xmlOut );
            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionYBFix execute]Exception:" + e.toString() );
            ServletOutputStream out = null;

            GenerateXmlYB responseXml = new GenerateXmlYB();
            responseXml.setRootNode( "root" );
            responseXml.setStatus( 1 );
            responseXml.setPurchase( 1 );

            String xmlOut = responseXml.createXml();
            Logging.info( xmlOut );
            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e1 )
            {
                Logging.error( e1.toString() );
            }
        }
        finally
        {
        }

    }
}
