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
     * コンストラクタ.
     * 
     * @param manager ConsumerManager.
     * @param realm レルム.
     * @param returnTo 戻り先URL.
     */
    public AUProcessor(ConsumerManager cManager, String strRealm, String strReturnTo)
    {
        manager = cManager;
        realm = strRealm;
        returnTo = strReturnTo;
    }

    /**
     * 認証要求.
     * 
     * @param request HttpServletRequest.
     * @param response HttpServletResponse.
     * @param context ServletContext.
     * @param identifier 識別子.
     * @throws Exception 例外.
     */
    public void doAuthRequest(HttpServletRequest request, HttpServletResponse response,
            String identifier) throws Exception
    {

        Cookie[] cookies = null;
        Cookie cookie = null;
        int i;
        try
        {

            // ディスカバリの実行
            List discoveries = manager.discover( identifier );
            // アソシエーションの確立
            DiscoveryInformation discoveryInfomation = manager.associate( discoveries );

            // Discovery結果検証
            if ( discoveryInfomation == null )
            {
                throw new Exception( "can't found OP EndPoint." );
            }
            else if ( !discoveryInfomation.isVersion2() )
            {
                // OpenID 2.0にOpenID providerが対応していない場合
                throw new Exception( "don't support OpenID 2.0." );
            }

            // discovery情報をセッションに格納する
            request.setAttribute( SES_KEY_DISCOVERY_INFO, discoveryInfomation );

            // OpenID providerに送信するAuthRequest messageを生成する
            AuthRequest authRequest = manager.authenticate( discoveryInfomation, returnTo, realm );

            // 9. 認証要求
            // OP End PointにAuthRequestをPOSTで送信する
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
     * 認証アサーション検証.
     * 
     * @param request HttpServletRequest.
     * @param response HttpServletResponse.
     * @param context ServletContext.
     * 
     * 
     * @throws Exception 例外.
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

            // OpenID Providerからの認証応答メッセージを抽出する
            ParameterList openidResponseParam = new ParameterList( request.getParameterMap() );

            op_endpoint = request.getParameter( "openid.op_endpoint" );
            if ( op_endpoint != null )
            {
                op_endpoint = URLDecoder.decode( op_endpoint, "UTF-8" );
            }

            URL url = new URL( op_endpoint );
            DiscoveryInformation discoveryInfomation = new DiscoveryInformation( url );

            // managerが取得できているかを確認する
            if ( manager == null )
            {
                throw new Exception( "manager is null." );
            }

            // HTTP Requestから、受信側のURLを抽出する
            StringBuffer returnUrl = request.getRequestURL();

            // クエリを取得
            String query = request.getQueryString();

            // クエリがある場合のみアペンド
            if ( query != null && query.length() > 0 )
            {
                returnUrl.append( "?" ).append( request.getQueryString() );
            }

            if ( request.getAttribute( "kind" ) != null )
            {
                kind = (Integer)request.getAttribute( "kind" );
            }

            // 認証応答を検証する
            VerificationResult verification = manager.verify( returnUrl.toString(), openidResponseParam, discoveryInfomation );

            Identifier verified = verification.getVerifiedId();
            if ( verified != null )
            {

                AuthSuccess authSuccess = (AuthSuccess)verification.getAuthResponse();

                // 属性情報を取得する
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
     * 中間IDを抽出する<br>
     * (クエリストリングからidに対応する値を取得する)
     * 
     * @param verified 検証結果
     * @return 中間ID
     */
    private String parseServiceUserId(Identifier verified)
    {

        String tmp = verified.getIdentifier();
        String result = tmp.replaceAll( "^http.*\\?id=", "" );

        return result;
    }

}
