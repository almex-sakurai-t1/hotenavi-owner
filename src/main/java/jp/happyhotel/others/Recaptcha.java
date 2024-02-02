package jp.happyhotel.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jp.happyhotel.common.Logging;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * reCAPTCHA検証用クラス<br>
 * <br>
 * Googleが提供する「reCAPTCHA」の利用時に使用するクラスです。<br>
 * reCAPTCHAの認証により生成されたコードが、正しいものかどうかを検証します。<br>
 * 
 * @author koshiba-y1
 * @version 1.00 2017/10/02
 * @see <a href="https://developers.google.com/recaptcha/intro">reCAPTCHA Developer's Guide<a>
 */
public class Recaptcha
{
    /** POSTリクエスト送信用インナークラス */
    private static class PostRequest
    {
        /**
         * コンストラクタ
         */
        private PostRequest()
        {
        }

        /**
         * POSTリクエスト送信<br>
         * <br>
         * POSTリクエストを送信します。<br>
         * 
         * @param url 送信先のURL
         * @param contentType コンテキストタイプに指定する文字列
         * @param params パラメータ名と値を格納したマップ
         * @return POSTリクエストした結果
         * @throws IOException
         */
        public static String sendRequest(final String url, final String contentType, final String requestParam) throws IOException
        {
            HttpURLConnection conn = (HttpURLConnection)(new URL( url ).openConnection());
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", contentType );
            conn.setDoOutput( true );

            OutputStreamWriter outputStream = new OutputStreamWriter( conn.getOutputStream() );
            outputStream.write( requestParam );
            outputStream.close();

            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );
            StringBuilder strBuilder = new StringBuilder();

            String line;
            while( (line = reader.readLine()) != null )
            {
                strBuilder.append( line );
            }
            reader.close();
            inputStream.close();
            conn.disconnect();

            return strBuilder.toString();
        }

        /**
         * リクエストパラメータ生成<br>
         * <br>
         * リクエストに付加するためのパラメータを生成します。<br>
         * リクエストパラメータは、パラメータ名と値を"="で結合し、それらを"&"で連結したものとなります。<br>
         * 
         * @param params パラメータ名と値を格納したマップ
         * @return リクエストパラメータ
         */
        public static String generateRequestParam(final Map<String, String> params)
        {
            StringBuilder strBuilder = new StringBuilder();

            for( Map.Entry<String, String> param : params.entrySet() )
            {
                if ( strBuilder.length() > 0 )
                {
                    strBuilder.append( "&" );
                }
                strBuilder.append( param.getKey() + "=" + param.getValue() );
            }

            return strBuilder.toString();
        }
    }

    /** 公開鍵 */
    private static final String siteKey  = "6LcWPDIUAAAAADMf019p1fuEwwL-sY3pOiLaHFMi";

    /** エラーメッセージ */
    private String              errorMsg = null;

    /**
     * コンストラクタ
     */
    public Recaptcha()
    {
        this.errorMsg = "";
    }

    /**
     * siteKeyのゲッター
     * 
     * @return siteKeyの値
     */
    public static String getSiteKey()
    {
        return Recaptcha.siteKey;
    }

    /**
     * errorMsgのゲッター
     * 
     * @return errorMsgの値
     */
    public String getErrorMsg()
    {
        return this.errorMsg;
    }

    /**
     * 検証処理<br>
     * <br>
     * Google宛にPOSTメソッドを投げ、キャプチャが正しく行われたかどうかを検証してもらいます。<br>
     * 
     * @param gRecaptchaResponse g-recaptcha-responseパラメータから取得した文字列
     * @return 検証結果を示すJSONストリング
     * @throws IOException
     */
    private static String verify(final String gRecaptchaResponse) throws IOException
    {
        final String secretKey = "6LcWPDIUAAAAAOkGzGSlf5HY2PrJoQ-L2IQmSiGt";

        Map<String, String> params = new HashMap<String, String>();
        params.put( "secret", secretKey );
        params.put( "response", gRecaptchaResponse );

        return PostRequest.sendRequest(
                "https://www.google.com/recaptcha/api/siteverify",
                "application/x-www-form-urlencoded",
                PostRequest.generateRequestParam( params ) );
    }

    /**
     * メッセージ抽出<br>
     * <br>
     * JSONArrayに格納された文字列を、カンマ＋スペース区切りの文字列に変換します。<br>
     * 
     * @param msgs 文字列を格納したJSONArray
     * @return 配列に格納された文字列を、カンマ＋スペース区切りで結合した文字列
     */
    private static String extractMsgs(final JSONArray msgs)
    {
        StringBuilder strBuilder = new StringBuilder();
        for( int i = 0 ; i < msgs.length() ; i++ )
        {
            if ( strBuilder.length() > 0 )
            {
                strBuilder.append( ", " );
            }
            strBuilder.append( msgs.getString( i ) );
        }

        return strBuilder.toString();
    }

    /**
     * キャプチャの検証<br>
     * <br>
     * キャプチャが正しく行われたかどうかを検証します。<br>
     * 引き数にg-recaptcha-responseパラメータから取得した文字列を与えてください。<br>
     * 検証の結果、正しく行われた場合はtrueを、正しく行われなかった場合はfalseを返します。<br>
     * 検証結果がfalseの場合、errorMsgにエラーの内容が記録されます。<br>
     * 
     * @param gRecaptchaResponse g-recaptcha-responseパラメータから取得した文字列
     * @return 検証結果を示すブール値
     */
    public boolean verifyCapture(final String gRecaptchaResponse)
    {
        // エラーメッセージの初期化
        this.errorMsg = "";

        // 空文字判定
        if ( StringUtils.isEmpty( gRecaptchaResponse ) )
        {
            this.errorMsg = "input is empty";
            Logging.warn( "[Recaptcha.verifyCapture] " + getErrorMsg() );

            return false;
        }

        // キャプチャが正しく行われたか検証
        String jsonStr;
        try
        {
            jsonStr = Recaptcha.verify( gRecaptchaResponse );
        }
        catch ( IOException e )
        {
            this.errorMsg = "post request error";
            Logging.error( "[Recaptcha.verify] Exception=" + e.toString() );

            return false;
        }

        // レスポンスJSON解析
        JSONObject jsonObj = JSONObject.fromObject( jsonStr );
        boolean success = (Boolean)(jsonObj.get( "success" ));
        if ( !success )
        {
            JSONArray errorMsgs = (JSONArray)(jsonObj.get( "error-codes" ));
            this.errorMsg = Recaptcha.extractMsgs( errorMsgs );
            Logging.warn( "[Recaptcha.verifyCapture] error-codes: " + getErrorMsg() );
        }

        return success;
    }
}
