package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.CreateUuid;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApUuid;
import jp.happyhotel.others.GenerateXmlIssueId;
import jp.happyhotel.user.UserLoginInfo;

/****
 * UUIDî≠çsèàóù
 * 
 * @author tashiro-s1
 * @version 1.0 2014/12/14
 */
public class ActionApiIssueUuid extends BaseAction
{
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramMethod = null;
        UserLoginInfo uli;
        int errorCode = 0;
        String errorMessage = "";
        DataApUuid dau = new DataApUuid();
        boolean ret = false;
        String uuid = "";
        try
        {
            Logging.info( "ActionApiIssueUuid.execute start" );

            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( uli.isMemberFlag() != false || uli.isNonmemberFlag() != false )
            {
                uuid = CreateUuid.getUuid();
                dau.setUuid( uuid );
                dau.setReceipt( "" );
                dau.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dau.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                ret = dau.insertData();

                if ( ret == false )
                {
                    errorCode = Constants.ERROR_CODE_API18;
                    errorMessage = Constants.ERROR_MSG_API18;
                }
            }
            else
            {
                errorCode = Constants.ERROR_CODE_API3;
                errorMessage = Constants.ERROR_MSG_API3;
            }

            GenerateXmlIssueId xmlIssue = new GenerateXmlIssueId();
            xmlIssue.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            xmlIssue.setMethod( paramMethod );
            xmlIssue.setName( "UUIDìoò^" );
            xmlIssue.setCount( 1 );
            xmlIssue.setErrorCode( errorCode );
            xmlIssue.setErrorMessage( errorMessage );
            xmlIssue.setUuid( uuid );

            String xmlOut = xmlIssue.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiIssueId.execute() ] Exception:", exception );

            // ÉGÉâÅ[ÇèoóÕ
            GenerateXmlIssueId xmlIssue = new GenerateXmlIssueId();
            xmlIssue.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            xmlIssue.setMethod( paramMethod );
            xmlIssue.setName( "UUIDìoò^" );
            xmlIssue.setCount( 1 );
            xmlIssue.setErrorCode( Constants.ERROR_CODE_API10 );
            xmlIssue.setErrorMessage( Constants.ERROR_MSG_API10 );
            xmlIssue.setUuid( "" );

            String xmlOut = xmlIssue.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiIssueId response]Exception:" + e.toString() );
            }
        }
        finally
        {
            Logging.info( "ActionApiIssueUuid.execute end" );
        }

    }
}
