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

public class ActionYBOrder extends BaseAction
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

        Logging.info( "[ActionYBOrder] Loaded" );
        Logging.info( "[ActionYBOrder] stid:" + stid );
        Logging.info( "[ActionYBOrder] userId:" + userId );
        Logging.info( "[ActionYBOrder] purchaseStatus:" + purchaseStatus );
        Logging.info( "[ActionYBOrder] pid:" + pid );
        Logging.info( "[ActionYBOrder] time:" + time );
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
                timeParse = new TimeParseYahooWallet();
                retParameter = timeParse.parseTime( time );
            }
            break;
        }
        try
        {
            if ( retParameter != false )
            {
                if ( purchaseStatus.equals( "OD1" ) != false )
                {
                    retRequest = true;
                }
                else
                {
                    retRequest = false;
                }
            }

            Logging.info( "[YBORDER] retParameter:" + retParameter );
            Logging.info( "[YBORDER] retRequest:" + retRequest );

            DataUserSp dus;
            dus = new DataUserSp();

            ret = dus.getDataBySuid( userId );
            Logging.info( "[ActionYBOrder] dus.getDataBySuid:" + ret );
            if ( ret != false )
            {
                Logging.info( "[ActionYBOrder] userid:" + dus.getUserId() );
                Logging.info( "[ActionYBOrder] suid:" + dus.getOpenId() );
                Logging.info( "[ActionYBOrder] carrier_flag:" + dus.getCarrierKind() );
                Logging.info( "[ActionYBOrder] charge_flag:" + dus.getChargeFlag() );
                Logging.info( "[ActionYBOrder] del_flag:" + dus.getDelFlag() );
                if ( dus.getChargeFlag() == 1 )
                {
                    retRequest = false;
                }
                if ( dus.getDelFlag() == 1 )
                {
                    retRequest = false;
                }
            }

            ServletOutputStream out = null;

            GenerateXmlYB responseXml = new GenerateXmlYB();

            if ( retRequest != false )
            {
                responseXml.setRootNode( "root" );
                responseXml.setStatus( 0 );
                responseXml.setPurchase( 0 );

            }
            else
            {
                responseXml.setRootNode( "root" );
                responseXml.setStatus( 1 );
                responseXml.setPurchase( 0 );
            }
            String xmlOut = responseXml.createXml();
            Logging.info( xmlOut );
            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionYBOrder execute]Exception:" + e.toString() );
            ServletOutputStream out = null;

            GenerateXmlYB responseXml = new GenerateXmlYB();
            responseXml.setRootNode( "root" );
            responseXml.setStatus( 1 );
            responseXml.setPurchase( 0 );

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
