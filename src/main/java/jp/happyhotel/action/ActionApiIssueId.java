package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.ConvertString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.RandomString;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.others.GenerateXmlIssueId;
import jp.happyhotel.user.UserLogin;
import jp.happyhotel.user.UserLoginInfo;

/****
 * アプリ会員ID発行処理
 * 
 * @author tashiro-s1
 * @version 1.0 2014/06/04
 */
public class ActionApiIssueId extends BaseAction
{
    final int REGIST_STATUS = 9;
    final int SP_LOGIN      = 1;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        Logging.info( "ActionApiIssueId.execute start" );

        String paramMethod = null;
        UserLoginInfo uli;
        int errorCode = 0;
        String errorMessage = "";
        String userId = "";
        String pass = "";
        String strUaType = "";
        String strPrefix = "";
        DataUserBasic dub = new DataUserBasic();
        boolean ret = false;

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            strUaType = UserAgent.getUserAgentTypeString( request );
            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( strUaType.equals( "ipa" ) != false )
            {
                strPrefix = "ip_";
            }
            else if ( strUaType.equals( "ada" ) != false )
            {
                strPrefix = "ad_";
            }

            userId = RandomString.getUserId( strPrefix );
            pass = RandomString.getRandomString( 8 );
            // 仮登録を行う。
            dub.setUserId( userId );
            dub.setPasswd( UserLogin.encrypt( ConvertString.convert2md5( pass ) ) );
            dub.setSex( 2 );
            dub.setRegistStatus( REGIST_STATUS );
            dub.setTempDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dub.setTempTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dub.setRegistDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dub.setRegistTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dub.setSmartPhoneFlag( SP_LOGIN );

            // 会員の場合
            if ( uli.isMemberFlag() == true )
            {
                dub.setAccessTicket( uli.getUserInfo().getUserId() );
            }

            ret = dub.insertData();
            if ( ret == false )
            {
                Logging.info( "ActionApiIssueId.execute ret=false" );
                errorCode = Constants.ERROR_CODE_API14;
                errorMessage = Constants.ERROR_MSG_API14;
            }

            GenerateXmlIssueId xmlIssue = new GenerateXmlIssueId();
            xmlIssue.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            xmlIssue.setMethod( paramMethod );
            xmlIssue.setName( "アプリ会員仮登録" );
            xmlIssue.setCount( 1 );
            xmlIssue.setErrorCode( errorCode );
            xmlIssue.setErrorMessage( errorMessage );
            if ( ret != false )
            {
                xmlIssue.setUserId( userId );
                // xmlIssue.setUserPassword( ConvertString.convert2md5( pass ) );
                xmlIssue.setUserPassword( UserLogin.encrypt( ConvertString.convert2md5( pass ) ) );
            }
            else
            {
                xmlIssue.setUserId( "" );
                xmlIssue.setUserPassword( "" );
            }

            String xmlOut = xmlIssue.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiIssueId.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlIssueId xmlIssue = new GenerateXmlIssueId();
            xmlIssue.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            xmlIssue.setMethod( paramMethod );
            xmlIssue.setName( "アプリ会員仮登録" );
            xmlIssue.setCount( 1 );
            xmlIssue.setErrorCode( Constants.ERROR_CODE_API10 );
            xmlIssue.setErrorMessage( Constants.ERROR_MSG_API10 );
            xmlIssue.setUserId( "" );
            xmlIssue.setUserPassword( "" );

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
            Logging.info( "ActionApiIssueId.execute end" );
        }

    }
}
