package jp.happyhotel.touch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import jp.happyhotel.common.Logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/***
 * GoogleへのPUSH
 */

public class CheckOutMessage
{

    /**
     *
     */
    private static final long   serialVersionUID = -5216236331970005461L;
    // TODO APIキーはDC社のものなのでリリースまでに自社APIキーを発行すること
    private static final String API_KEY          = "AIzaSyBkCDj2bGigEwxsHqDSnUItHYFMWPfqN24"; // APIキー
    private static final int    OS_TYPE_ANDROID  = 2;                                        // Android

    /**
     * プッシュ送信
     * 
     * @param paramMessage
     * @param paramUrl
     * @see 対象端末はap_user_push_configのAndoroidでpush_flag=1のユーザ
     */
    public void push(String paramMessage, String paramUrl)
    {
        boolean ret = false;
        TokenUser tk = new TokenUser();
        GooglePushMapper gpm = null;
        ArrayList<String> tokenList = new ArrayList<String>();

        try
        {
            gpm = new GooglePushMapper();
            // Androidユーザでpush_flag=1を取得
            ret = tk.getTokenUserList( OS_TYPE_ANDROID, 0 );
            for( int i = 0 ; i < tk.getTokenCount() ; i++ )
            {
                gpm.addRegId( tk.getTokenList()[i].getToken() );
            }

            if ( paramMessage == null || paramMessage.equals( "" ) != false )
            {
                paramMessage = "テスト";
            }
            if ( paramUrl == null )
            {
                paramUrl = "";
            }

            gpm.createData( paramMessage, paramUrl );
            this.push( gpm );

        }
        catch ( Exception e )
        {
            Logging.error( "[Push2Android Exception]:" + e.toString() );
        }
    }

    /**
     * プッシュ送信
     * 
     * @param regIdList
     * @param paramMessage
     * @param paramUrl
     */
    public void push(LinkedList<String> regIdList, String paramMessage, String paramUrl)
    {
        boolean ret = false;
        TokenUser tk = new TokenUser();
        GooglePushMapper gpm = null;
        ArrayList<String> tokenList = new ArrayList<String>();

        try
        {
            gpm = new GooglePushMapper();
            gpm.setRegId( regIdList );

            if ( paramMessage == null || paramMessage.equals( "" ) != false )
            {
                paramMessage = "テスト";
            }
            if ( paramUrl == null )
            {
                paramUrl = null;
            }

            gpm.createData( paramMessage, paramMessage );
            this.push( gpm );

        }
        catch ( Exception e )
        {
            Logging.error( "[Push2Android Exception]:" + e.toString() );
        }
    }

    /***
     * プッシュ送信
     * 
     * @param gpm
     */
    public void push(GooglePushMapper gpm)
    {
        if ( gpm == null )
        {
            return;
        }

        try
        {
            // 1. URL
            URL url = new URL( "https://android.googleapis.com/gcm/send" );

            // 2. URLへアクセス
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod( "POST" );

            // 4. ヘッダー情報のセット
            conn.setRequestProperty( "Content-Type", "application/json" );
            conn.setRequestProperty( "Authorization", "key=" + API_KEY );
            conn.setDoOutput( true );

            // 5. JSONの準備
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable( SerializationFeature.INDENT_OUTPUT );

            DataOutputStream wr = new DataOutputStream( conn.getOutputStream() );

            Logging.info( mapper.writeValueAsString( gpm ) );

            mapper.writeValue( wr, gpm );
            wr.flush();

            wr.close();

            // 6. レスポンスを出力
            int responseCode = conn.getResponseCode();
            Logging.info( "Sending 'POST' request to URL : " + url );
            Logging.info( "Content-Type:" + conn.getRequestProperty( "Content-Type" ) );
            Logging.info( "Response Code : " + responseCode );
            Logging.info( "Response Message : " + conn.getResponseMessage() );

            BufferedReader in = new BufferedReader(
                    new InputStreamReader( conn.getInputStream() ) );
            String inputLine;
            StringBuffer response = new StringBuffer();

            while( (inputLine = in.readLine()) != null )
            {
                response.append( inputLine );
            }
            in.close();

            // 7. 結果を出力
            Logging.info( response.toString() );

        }
        catch ( MalformedURLException e )
        {
            Logging.error( "[Push2Android MalformedURLException]:" + e.toString() );
        }
        catch ( IOException e )
        {
            Logging.error( "[Push2Android IOException]:" + e.toString() );
        }
    }
}
