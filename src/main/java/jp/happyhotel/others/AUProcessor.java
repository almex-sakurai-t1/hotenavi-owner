package jp.happyhotel.others;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.data.DataLoginInfo_M2;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;

public class AUProcessor implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID       = 5733961850816142711L;

    /** String SES_KEY_DISCOVERY_INFO. */
    private static final String SES_KEY_DISCOVERY_INFO = "openid.discovery.information";
    /** ConsumerManager _manager. */
    private ConsumerManager     manager;
    /** String _realm. */
    private String              realm;
    /** String _returnTo. */
    private String              returnTo;

    /**
     * �R���X�g���N�^.
     * 
     * @param manager ConsumerManager.
     * @param realm ������.
     * @param returnTo �߂��URL.
     */
    public AUProcessor(ConsumerManager cManager, String strRealm, String strReturnTo)
    {
        manager = cManager;
        realm = strRealm;
        returnTo = strReturnTo;
    }

    /**
     * �F�ؗv��.
     * 
     * @param request HttpServletRequest.
     * @param response HttpServletResponse.
     * @param context ServletContext.
     * @param identifier ���ʎq.
     * @throws Exception ��O.
     */
    public void doAuthRequest(HttpServletRequest request, HttpServletResponse response,
            String identifier) throws Exception
    {

        Cookie[] cookies = null;
        Cookie cookie = null;
        int i;
        try
        {

            // �f�B�X�J�o���̎��s
            List discoveries = manager.discover( identifier );
            // �A�\�V�G�[�V�����̊m��
            DiscoveryInformation discoveryInfomation = manager.associate( discoveries );

            // Discovery���ʌ���
            if ( discoveryInfomation == null )
            {
                throw new Exception( "can't found OP EndPoint." );
            }
            else if ( !discoveryInfomation.isVersion2() )
            {
                // OpenID 2.0��OpenID provider���Ή����Ă��Ȃ��ꍇ
                throw new Exception( "don't support OpenID 2.0." );
            }

            // discovery�����Z�b�V�����Ɋi�[����
            request.setAttribute( SES_KEY_DISCOVERY_INFO, discoveryInfomation );

            // OpenID provider�ɑ��M����AuthRequest message�𐶐�����
            AuthRequest authRequest = manager.authenticate( discoveryInfomation, returnTo, realm );

            // 9. �F�ؗv��
            // OP End Point��AuthRequest��POST�ő��M����
            RequestDispatcher dispatcher = request.getRequestDispatcher( "/phone/mypage/au/au_formpost.jsp" );
            request.setAttribute( "message", authRequest );
            request.setAttribute( "destinationUrl", authRequest.getDestinationUrl( false ) );
            dispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            throw e;
        }
    }

    /**
     * �F�؃A�T�[�V��������.
     * 
     * @param request HttpServletRequest.
     * @param response HttpServletResponse.
     * @param context ServletContext.
     * 
     * 
     * @throws Exception ��O.
     */
    public String doVerifyResponse(HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        int i;
        int kind = 1;
        String paramKind;
        String parseServiceUserId;
        DataLoginInfo_M2 dataLoginInfo;
        String op_endpoint = "";

        try
        {
            parseServiceUserId = "";
            dataLoginInfo = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            // OpenID Provider����̔F�؉������b�Z�[�W�𒊏o����
            ParameterList openidResponseParam = new ParameterList( request.getParameterMap() );

            op_endpoint = request.getParameter( "openid.op_endpoint" );
            if ( op_endpoint != null )
            {
                op_endpoint = URLDecoder.decode( op_endpoint, "UTF-8" );
            }

            URL url = new URL( op_endpoint );
            DiscoveryInformation discoveryInfomation = new DiscoveryInformation( url );

            // manager���擾�ł��Ă��邩���m�F����
            if ( manager == null )
            {
                throw new Exception( "manager is null." );
            }

            // HTTP Request����A��M����URL�𒊏o����
            StringBuffer returnUrl = request.getRequestURL();

            // �N�G�����擾
            String query = request.getQueryString();

            // �N�G��������ꍇ�̂݃A�y���h
            if ( query != null && query.length() > 0 )
            {
                returnUrl.append( "?" ).append( request.getQueryString() );
            }

            if ( request.getAttribute( "kind" ) != null )
            {
                kind = (Integer)request.getAttribute( "kind" );
            }

            // �F�؉��������؂���
            VerificationResult verification = manager.verify( returnUrl.toString(), openidResponseParam, discoveryInfomation );

            Identifier verified = verification.getVerifiedId();
            if ( verified != null )
            {

                AuthSuccess authSuccess = (AuthSuccess)verification.getAuthResponse();

                // ���������擾����
                parseServiceUserId = this.parseServiceUserId( verified );
            }
            return parseServiceUserId;
        }
        catch ( Exception e )
        {
            throw e;
        }
    }

    /**
     * ����ID�𒊏o����<br>
     * (�N�G���X�g�����O����id�ɑΉ�����l���擾����)
     * 
     * @param verified ���،���
     * @return ����ID
     */
    private String parseServiceUserId(Identifier verified)
    {

        String tmp = verified.getIdentifier();
        String result = tmp.replaceAll( "^http.*\\?id=", "" );

        return result;
    }

}
