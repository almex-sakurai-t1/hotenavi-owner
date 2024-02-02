package jp.happyhotel.touch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;

import jp.happyhotel.common.Logging;
import jp.happyhotel.others.AndroidPushResponse;
import jp.happyhotel.others.AndroidPushResponseResults;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/***
 * GoogleへのPUSH
 */

public class Push2Android
{

    /**
     *
     */
    private static final long   serialVersionUID = -5216236331970005461L;
    // TODO APIキーはDC社のものなのでリリースまでに自社APIキーを発行すること
    private static final String configFilePath   = "//etc//happyhotel//push.conf";
    FileInputStream             propfile         = null;
    Properties                  config;
    private static final int    OS_TYPE_ANDROID  = 2;                             // Android

    private int                 apliKind;                                         // 1:ハピホテアプリ,10:予約アプリ
    ArrayList<String>           inactiveToken    = new ArrayList<String>();       // 送信エラートークン配列
    ArrayList<String>           invalidToken     = new ArrayList<String>();       // 不正トークン配列

    /**
    *
    */
    public Push2Android()
    {
        this.apliKind = 1;
    }

    public int getApliKind()
    {
        return apliKind;
    }

    public ArrayList<String> getInactiveToken()
    {
        return inactiveToken;
    }

    public ArrayList<String> getInvalidToken()
    {
        return invalidToken;
    }

    public void setApliKind(int apliKind)
    {
        this.apliKind = apliKind;
    }

    public void setInactiveToken(ArrayList<String> inactiveToken)
    {
        this.inactiveToken = inactiveToken;
    }

    public void setInvalidToken(ArrayList<String> invalidToken)
    {
        this.invalidToken = invalidToken;
    }

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
                paramMessage = "ハピホテからのお知らせ";
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

            gpm.createData( paramMessage, paramUrl );
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
    public boolean push(GooglePushMapper gpm)
    {
        boolean ret = false;
        // Logging.info( "Push2Android START apliKind=" + apliKind );
        if ( gpm == null )
        {
            return ret;
        }

        try
        {
            String ApiKey = "";

            // APIキー情報を取得
            propfile = new FileInputStream( configFilePath );
            config = new Properties();
            config.load( propfile );

            if ( apliKind == 1 )
            {
                ApiKey = String.valueOf( config.getProperty( "apikey" ) );
            }
            else if ( apliKind == 2 )
            {
                ApiKey = String.valueOf( config.getProperty( "apikey.fcm" ) );
            }
            else
            {
                ApiKey = String.valueOf( config.getProperty( "apikey.rsv" ) );
            }

            // 1. URL
            URL url = new URL( "https://fcm.googleapis.com/fcm/send" );

            // 2. URLへアクセス
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod( "POST" );

            // 4. ヘッダー情報のセット
            conn.setRequestProperty( "Content-Type", "application/json" );
            conn.setRequestProperty( "Authorization", "key=" + ApiKey );
            conn.setDoOutput( true );

            // 5. JSONの準備
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable( SerializationFeature.INDENT_OUTPUT );

            DataOutputStream wr = new DataOutputStream( conn.getOutputStream() );

            // Logging.info( "Push2Android " + mapper.writeValueAsString( gpm ) );

            mapper.writeValue( wr, gpm );
            wr.flush();

            wr.close();

            // 6. レスポンスを出力
            int responseCode = conn.getResponseCode();
            if ( responseCode == 200 )
            {
                ret = true;
            }
            // Logging.info( "Sending 'POST' request to URL : " + url ,"Push2Android" );
            // Logging.info( "Content-Type:" + conn.getRequestProperty( "Content-Type" ) ,"Push2Android" );
            // Logging.info( "Response Code : " + responseCode,"Push2Android" );
            // Logging.info( "Response Message : " + conn.getResponseMessage() ,"Push2Android" );

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
            String res = response.toString();
            // Logging.info( res ,"Push2Android" );

            ObjectMapper oMapper = new ObjectMapper();
            AndroidPushResponse AndroidPushResponse = oMapper.readValue( res, AndroidPushResponse.class );
            Logging.info( "multicast_id: " + AndroidPushResponse.multicast_id );// 送信したメッセージのID
            Logging.info( "success: : " + AndroidPushResponse.success );// 送信に成功したメッセージ数
            Logging.info( "failure: " + AndroidPushResponse.failure );// 送信に失敗したメッセージ数
            Logging.info( "canonical_ids: " + AndroidPushResponse.canonical_ids );// 重複したregistration IDの数

            // メッセージの処理状況を表す配列オブジェクト
            // 並び順は送信時のregistrattionIDに対して同様
            int i = 0;
            for( AndroidPushResponseResults result : AndroidPushResponse.results )
            {
                // Logging.info( " result.registration_id: " + result.registration_id );
                // Logging.info( " result.message_id: " + result.message_id );
                // Logging.info( " result.error:" + result.error );
                if ( result.error == null )
                {
                    if ( result.registration_id != null )
                    {
                        if ( !result.registration_id.equals( gpm.registration_ids.get( i ) ) )
                        {
                            // 不正トークン配列に追加(アプリに紐づく古いトークン）
                            invalidToken.add( gpm.registration_ids.get( i ) );
                        }
                    }
                }
                else
                {
                    if ( result.error.equals( "NotRegistered" ) )
                    {
                        // 送信エラートークン配列に追加
                        inactiveToken.add( gpm.registration_ids.get( i ) );
                    }
                    else if ( result.error.equals( "InvalidRegistration" ) )
                    {
                        // 不正トークン配列に追加
                        invalidToken.add( gpm.registration_ids.get( i ) );
                    }
                }
                i++;
            }
        }
        catch ( MalformedURLException e )
        {
            Logging.error( "[Push2Android MalformedURLException]:" + e.toString() );
            ret = false;
        }
        catch ( IOException e )
        {
            Logging.error( "[Push2Android IOException]:" + e.toString() );
            ret = false;
        }
        catch ( Exception e )
        {
            Logging.error( "[Push2Android Exception]:" + e.toString() );
            ret = false;
        }
        return(ret);
    }
}
