/**
 *
 */
package jp.happyhotel.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.others.GenerateXmlLoginCheck;
import jp.happyhotel.user.UserLoginInfo;

/**
 * @author miura-s2
 * 
 */
public abstract class LoginCheck
{
    protected UserLoginInfo uli;
    protected String        paramMethod        = null;
    protected String        paramUuid          = "";
    protected String        paramUser          = "";
    protected String        paramPassword      = "";
    protected int           paramLoginStatus   = 0;

    int                     kind               = 0;
    String                  message            = "";
    String                  messageUrl         = "";
    String                  payKind            = "";
    int                     errorCode          = 0;
    String                  errorMessage       = "";

    protected static String messagePremium     = "ホテル空室情報対応！\nハピホテプレミアムコース";
    protected static String messageUrlPremium  = Url.getUrl() + "/phone/others/info_premium_app.jsp";

    final int               KIND_NONMEMBER     = 0;
    final int               KIND_FREEMEMBER    = 1;
    final int               KIND_PREMIUMMEMBER = 2;

    abstract public void execute(HttpServletRequest request, HttpServletResponse response);

    protected void ParamCheck(HttpServletRequest request)
    {
        uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
        paramMethod = request.getParameter( "method" );
        paramUuid = request.getParameter( "uuid" );
        paramUser = request.getParameter( "user_id" );
        paramPassword = request.getParameter( "password" );

        if ( CheckString.numCheck( request.getParameter( "loginStatus" ) ) == true )
        {
            paramLoginStatus = Integer.parseInt( request.getParameter( "loginStatus" ) );
        }

        if ( paramUuid == null )
        {
            paramUuid = "";
        }

        Logging.info( "user_id=" + paramUser + ", uuid=" + paramUuid + ", loginStatus=" + paramLoginStatus );

        if ( uli == null )
        {
            uli = new UserLoginInfo();
        }

        if ( paramUser == null || paramPassword == null )
        {
            errorCode = Constants.ERROR_CODE_API3;
            errorMessage = Constants.ERROR_MSG_API3;
        }
    }

    protected void SetNonMember()
    {
        Logging.info( "This user is non-member." );

        // 非会員
        kind = KIND_NONMEMBER;
        message = messagePremium;
        messageUrl = messageUrlPremium;
        errorCode = Constants.ERROR_CODE_API12;
        errorMessage = Constants.ERROR_MSG_API12;
    }

    protected void SetFreeMember()
    {
        Logging.info( "This user is free-member." );

        // 無料会員
        kind = KIND_FREEMEMBER;
        message = messagePremium;
        messageUrl = messageUrlPremium;
        errorCode = 0;
        errorMessage = "";
    }

    protected void SetPremiumMember(String paykind)
    {
        Logging.info( "This user is premium-member." );

        // 有料会員
        kind = KIND_PREMIUMMEMBER;
        payKind = paykind;
        message = messagePremium;
        messageUrl = messageUrlPremium;
        errorCode = 0;
        errorMessage = "";
    }

    protected String CreateResponse()
    {
        GenerateXmlLoginCheck xmlLogin = new GenerateXmlLoginCheck();
        xmlLogin.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
        xmlLogin.setMethod( paramMethod );
        xmlLogin.setName( "ユーザチェック" );
        xmlLogin.setCount( 1 );
        xmlLogin.setErrorCode( errorCode );
        xmlLogin.setErrorMessage( errorMessage );
        xmlLogin.setKind( kind );
        xmlLogin.setKindMessage( payKind );
        xmlLogin.setMessage( message );
        xmlLogin.setMessageUrl( messageUrl );

        String xmlOut = xmlLogin.createXml();

        return xmlOut;
    }
}
