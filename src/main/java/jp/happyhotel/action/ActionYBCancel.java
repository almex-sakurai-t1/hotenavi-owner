package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TimeParseYahooWallet;
import jp.happyhotel.others.GenerateXmlYB;
import jp.happyhotel.user.UserRegistYWallet;

public class ActionYBCancel extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int nIndex = 0;
        int nIndex2 = 0;
        String stid;
        String userId;
        String purchaseStatus;
        String pid;
        String time;
        String cutDate;
        String cutTime;
        boolean retParameter = false;
        boolean retRequest = false;
        boolean retResult = false;
        boolean ret = false;
        TimeParseYahooWallet timeParse;
        stid = request.getParameter( "stid" );
        userId = request.getParameter( "userid" );
        purchaseStatus = request.getParameter( "purchase_status" );
        pid = request.getParameter( "pid" );
        time = request.getParameter( "time" );

        timeParse = new TimeParseYahooWallet();

        Logging.info( "[ActionYBCancel] Loaded" );
        Logging.info( "[ActionYBCancel] stid:" + stid );
        Logging.info( "[ActionYBCancel] userId:" + userId );
        Logging.info( "[ActionYBCancel] purchaseStatus:" + purchaseStatus );
        Logging.info( "[ActionYBCancel] pid:" + pid );
        Logging.info( "[ActionYBCancel] time:" + time );

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
                if ( purchaseStatus.equals( "CA1" ) != false )
                {
                    retRequest = true;
                }
                else
                {
                    retRequest = false;
                }
            }

            Logging.info( "YBCANCEL" );

            ServletOutputStream out = null;
            GenerateXmlYB responseXml = new GenerateXmlYB();

            if ( retRequest != false )
            {
                // �����ԍ����Z�b�g����
                request.setAttribute( "ORDER_NO", stid );
                request.setAttribute( "DATE", timeParse.getDate() );
                request.setAttribute( "TIME", timeParse.getTime() );
                request.setAttribute( "OPEN_ID", userId );

                UserRegistYWallet registYW = new UserRegistYWallet();
                ret = registYW.secession( request );

                // ���[�U�̓o�^���ł����ꍇ
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
            Logging.error( "[ActionYBCancel execute]Exception:" + e.toString() );
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
