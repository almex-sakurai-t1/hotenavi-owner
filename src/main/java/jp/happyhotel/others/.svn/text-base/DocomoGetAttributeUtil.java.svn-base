package jp.happyhotel.others;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.happyhotel.common.EncodeData;
import jp.happyhotel.common.Logging;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * 属性情報取得ユーティリティクラス.
 */
public class DocomoGetAttributeUtil implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -7717235111204152179L;
    private static String     FREE_CODE        = "00073361101";
    private static String     PAY_CODE         = "00073361102";

    /**
     * 属性情報取得.
     * 
     * @param openid OpenID.
     * @param nonce Nonce.
     * @return 属性情報マップ.
     * @throws Exception 例外.
     */
    public static Map get(final String openid, final String nonce, int kind) throws Exception
    {
        String memberSiteId = "";
        String password = "hapihote12";
        String basic = "";

        if ( openid == null || openid.length() == 0 || nonce == null || nonce.length() == 0 )
            throw new Exception( "parameter is null or blank." );

        Logging.info( "start get." );

        if ( kind == 0 || kind == 1 || kind == 3 )
        {
            memberSiteId = FREE_CODE;
        }
        else if ( kind == 2 || kind == 4 )
        {
            memberSiteId = PAY_CODE;
        }
        Logging.info( "kind" + kind );
        basic = memberSiteId + ":" + password;
        Logging.info( "base64_old:" + basic );
        basic = EncodeData.encodeBase64( basic.getBytes() );
        Logging.info( "base64_new:" + basic );

        // サーバ間通信で属性情報を取得する
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod( "https://i.mydocomo.com/api/imode/o-info" );
        String queryString = "";

        queryString = makeQuery( openid, nonce );

        method.addRequestHeader( "Authorization", "Basic " + basic );
        // クエリパラメータの生成
        method.setQueryString( queryString );

        String responseMessage = null;
        // 要求
        try
        {
            int status = client.executeMethod( method );
            // 応答メッセージのHTTPステータスコードを検証する
            if ( status != 200 )
            {
                throw new Exception( "Connection failed." );
            }
            // 応答メッセージを文字列形式で取得
            responseMessage = method.getResponseBodyAsString();
            Logging.info( "responseMessage:" + responseMessage );
        }
        finally
        {
            method.releaseConnection();
        }
        // 応答をパース
        Map result = parseResponse( responseMessage );
        result.put( "verified.openid", openid );
        // とび先を追加
        result.put( "kind", Integer.toString( kind ) );

        Logging.info( "end get." );
        return result;
    }

    /**
     * 要求クエリの生成.
     * 
     * 
     * 
     * @param openid OpenID.
     * @param nonce Nonce.
     * @return 要求クエリ文字列.
     */
    private static String makeQuery(String openid, String nonce)
    {
        // クエリパラメータの生成
        StringBuffer query;
        try
        {
            query = new StringBuffer();
            query.append( "ver=1.0" );
            query.append( "&openid=" );
            query.append( URLEncoder.encode( openid, "UTF-8" ) );
            query.append( "&nonce=" );
            query.append( URLEncoder.encode( nonce, "UTF-8" ) );
            query.append( "&SUID=" );
            Logging.debug( "query : [" + query.toString() + "]" );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new RuntimeException( e );
        }
        return query.toString();
    }

    /**
     * 応答の解析.
     * 
     * @param responseMessage 応答メッセージ.
     * @return 解析結果.
     */
    private static Map parseResponse(String responseMessage)
    {
        String[] userInfo;
        // responceメッセージを改行で分割する
        userInfo = responseMessage.split( "\r\n" );
        Pattern checkPattern = Pattern.compile( "(ver|result|SUID|UA):.*" );
        HashMap resultdata = new HashMap();
        Matcher matcher = null;
        for( int i = 0 ; i < userInfo.length ; i++ )
        {
            matcher = checkPattern.matcher( userInfo[i] );
            if ( matcher.matches() )
            {
                int spritpoint = userInfo[i].indexOf( ":" );
                String key = userInfo[i].substring( 0, spritpoint );
                String value = userInfo[i].substring( spritpoint + 1, userInfo[i].length() );
                resultdata.put( key, value.trim() );
                Logging.debug( "result : key=" + key + " value=" + value );
            }
        }
        return resultdata;
    }
}
