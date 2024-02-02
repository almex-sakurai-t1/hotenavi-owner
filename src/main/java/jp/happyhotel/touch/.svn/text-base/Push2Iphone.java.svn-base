package jp.happyhotel.touch;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jp.happyhotel.common.Logging;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsDelegateAdapter;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.DeliveryError;
import com.notnoop.apns.PayloadBuilder;

/**
 * ハピホテタッチチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 * @see リクエスト：1000電文<br>
 *      レスポンス：1001電文
 */
public class Push2Iphone implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID = 8544511852177631618L;
    private static final int    OS_TYPE_IPHONE   = 1;                             // iPhone
    private static final String configFilePath   = "//etc//happyhotel//push.conf";
    FileInputStream             propfile         = null;
    Properties                  config;
    private int                 apliKind;                                         // 1:ハピホテアプリ,10:予約アプリ
    int                         ErrorIdentifier;                                  // 不正トークンの配列識別
    int                         SentIdentifier;                                   // 送信済みトークンの配列識別
    ArrayList<String>           inactiveToken    = new ArrayList<String>();       // 送信エラートークン配列
    ArrayList<String>           invalidToken     = new ArrayList<String>();       // 不正トークン配列

    ApnsService                 apnsService      = null;

    /**
     *
     */
    public Push2Iphone()
    {
        this.apliKind = 1;
        ErrorIdentifier = -1;
        SentIdentifier = -1;
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
     * @see 対象端末はap_user_push_configのpush_flag=1のユーザ
     */
    public void getTokenList(String paramMessage, String paramUrl)
    {
        boolean ret = false;
        TokenUser tk = new TokenUser();
        GooglePushMapper gpm = null;
        ArrayList<String> tokenList = new ArrayList<String>();

        try
        {
            gpm = new GooglePushMapper();
            // Androidユーザでpush_flag=1を取得
            ret = tk.getTokenUserList( OS_TYPE_IPHONE, 0 );

            if ( paramMessage == null || paramMessage.equals( "" ) != false )
            {
                paramMessage = "";
            }
            if ( paramUrl == null )
            {
                paramUrl = "";
            }

            if ( ret != false )
            {
                for( int i = 0 ; i < tk.getTokenList().length ; i++ )
                {
                    Logging.info( "user_id:" + tk.getTokenList()[i].getUserId() );
                    this.push( tk.getTokenList()[i].getToken(), paramMessage );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[Push2IPhone Exception]:" + e.toString() );
        }
    }

    public boolean push(String deviceToken, String msg)
    {
        String url = "";
        boolean ret;

        ret = this.push( deviceToken, msg, url );
        return ret;

    }

    public boolean push(String deviceToken, String msg, String url)
    {
        boolean ret = false;

        // Logging.info( "[Push2IPhone]push()" ,"Push2Iphone");

        // TODO certFilePathとcertPasswordはDC社の証明書のため、リリースまでに必ず自社アカウントを用意すること

        String certFilePath = ""; // Appleのサーバーと通信するための証明書ファイルのPathを取得する
        String certPassword = ""; // 証明書のパスワードを取得する

        // 証明書情報を取得
        try
        {
            propfile = new FileInputStream( configFilePath );
            config = new Properties();
            config.load( propfile );

            if ( apliKind == 1 || apliKind == 2 )
            {
                certFilePath = String.valueOf( config.getProperty( "cert.filepath" ) );
                certPassword = String.valueOf( config.getProperty( "cert.password" ) );
            }
            else
            {
                certFilePath = String.valueOf( config.getProperty( "cert.filepath.rsv" ) );
                certPassword = String.valueOf( config.getProperty( "cert.password.rsv" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[Push2Iphone]Exception:" + e.toString() );
            return false;
        }

        ApnsServiceBuilder serviceBuilder =
                APNS.newService().withCert( certFilePath, certPassword );
        // 注:↓を指定しないと内部でThreadを生成しようとしてコケる
        // ※指定するとconnectionClosedを通らず配信エラーが取得できない
        // .withNoErrorDetection();

        // 接続先としてSandbox(開発用環境)を指定する場合
        // serviceBuilder.withSandboxDestination();
        // 接続先として本番用環境を指定する場合
        serviceBuilder.withProductionDestination();

        // PUSH配信のステータス通知を取得するため追加
        serviceBuilder.withDelegate( new MyApnsDelegate() );

        // ApnsService apnsService = null;
        try
        {
            apnsService = serviceBuilder.build();
            PayloadBuilder payloadBuilder = APNS.newPayload();

            // alert文字列
            payloadBuilder.alertBody( msg );

            // badge
            payloadBuilder.badge( 1 );

            payloadBuilder.customField( "url", url );

            // Push通知の送信(複数のデバイストークンをまとめて送信も可能)
            String[] token = deviceToken.split( ",", 0 );

            ArrayList<String> ids = new ArrayList<String>();
            for( int i = 0 ; i < token.length ; i++ )
            {
                ids.add( token[i] );
            }

            // 複数トークンをまとめてPUSH ※Identifierは、1～
            Collection<? extends ApnsNotification> result = null;
            try
            {
                result = apnsService.push( ids, payloadBuilder.build() );
            }
            catch ( Exception e )
            {
                Logging.error( "[Push2IPhone] push() Exception:" + e.toString() );
                if ( SentIdentifier > -1 )
                {
                    ErrorIdentifier = SentIdentifier + 1;
                }
            }

            // Collection<? extends EnhancedApnsNotification> result = null;
            // Date d = new Date();
            // result = apnsService.push( ids, payloadBuilder.build(), d );

            /*
             * //一件ずつPUSHする方法 ※Identifierは、0～
             * try {
             * String payload = payloadBuilder.build();
             * for (int i = 0 ; i < token.length ; i++){
             * int now = (int)(new Date().getTime()/1000);
             * EnhancedApnsNotification notification = new EnhancedApnsNotification(i,
             * now + 60 * 60 ,
             * token[i] ,
             * payload);
             * apnsService.push(notification);
             * }
             * } catch (Exception e1) {
             * Logging.info( "IosPush :" + e1.getMessage());
             * }
             */

            // これがないと、connectionClosed処理が検知できない
            Thread.sleep( 1000 );

            // エラーがあったときは再帰処理
            // Logging.info( "ErrorIdentifier=" + ErrorIdentifier );
            if ( ErrorIdentifier >= 0 )
            {
                // 不正トークン配列に追加
                invalidToken.add( ids.get( ErrorIdentifier - 1 ) );
                // invalidToken.add(ids.get(ErrorIdentifier));

                // トークン配列再作成
                String newDeviceToken = "";
                if ( ids.size() > ErrorIdentifier )
                // if( ids.size() > ErrorIdentifier + 1)
                {
                    for( int i = ErrorIdentifier ; i < ids.size() ; i++ )
                    {
                        // for (int i = ErrorIdentifier + 1 ; i < ids.size() ; i++){
                        newDeviceToken += "," + ids.get( i );
                    }
                    newDeviceToken = newDeviceToken.substring( 1, newDeviceToken.length() );
                    // push関数再帰処理
                    Logging.info( "pushSAISOU ErrorIdentifier=" + ErrorIdentifier + " newDeviceToken=" + newDeviceToken );
                    ErrorIdentifier = -1;
                    push( newDeviceToken, msg, url );
                }
            }

            // フィードバックサービスに誤配信されたと報告されたデバイスリストを取得する方法
            // ・apnsService.getInactiveDevices()を使用して取得する
            // ・APNｓが、トークンとアプリケーションがデバイスにもはや存在しないと判断したタイムスタンプを位置づけたMap
            // ・デバイスリストは1度取得するとクリアされる
            // ・デバイスリストに登録されていても、アプリを再インストールしpush()が成功するとリストから消える
            // ・APNｓが保持していないデバイスはリストに登録されない（例：aaaaaa）

            Map<String, Date> InactiveDevices = new HashMap<String, Date>();
            InactiveDevices = apnsService.getInactiveDevices();
            // Logging.info( "map.size()  " + InactiveDevices.size() );
            for( String str : InactiveDevices.keySet() )
            {
                // Logging.info( "Map " + str + ":" + InactiveDevices.get( str ), "Push2Iphone" );
                inactiveToken.add( str );
            }

            // Logging.info( "[Push2IPhone]:end to send ids.size() =" + ids.size() + " push.token=" + deviceToken );
            // Logging.info( "ErrorIdentifier! =" + ErrorIdentifier );

            ret = true;

        }
        catch ( Exception e )
        {
            Logging.error( "[Push2IPhone] Exception:" + e.toString() );
            ret = false;
        }
        finally
        {
            // Connectionを解放
            if ( apnsService != null )
            {
                apnsService.stop();
            }
        }

        return ret;
    }

    public class MyApnsDelegate extends ApnsDelegateAdapter
    {

        // Logger log = LoggerFactory.getLogger(MyApnsDelegate.class);

        @Override
        public void messageSent(ApnsNotification apnsNotification, boolean b)
        {

            SentIdentifier = apnsNotification.getIdentifier();
            /*
             * byte[] bytes1 = apnsNotification.getDeviceToken();
             * String tmp1 = "";
             * tmp1 = new String( Hex.encodeHex( bytes1 ) );
             * for( byte b1 : bytes1 )
             * {
             * tmp1 += b1;
             * }
             * Logging.info( "[messageSent] messageSent called! [" + apnsNotification.getIdentifier() + "] DeviceToken=" + tmp1 );
             */
            // Logging.info( "[messageSent] apnsNotification.getDeviceToken()=" + tmp1 );
            // Logging.info( "[messageSent] apnsNotification..marshall() =" + apnsNotification.marshall() );
            // Logging.info( "[messageSent] apnsNotification..getIdentifier() =" + apnsNotification.getIdentifier() );
            // Logging.info( "[messageSent] apnsNotification..getExpiry() =" + apnsNotification.getExpiry() );

        }

        @Override
        public void messageSendFailed(ApnsNotification message, Throwable e)
        {

            /*
             * byte[] bytes1 = message.getDeviceToken();
             * String tmp1 = "";
             * tmp1 = new String( Hex.encodeHex( bytes1 ) );
             * for( byte b1 : bytes1 )
             * {
             * tmp1 += b1;
             * }
             * Logging.info( "messageSendFailed called! [" + message.getIdentifier() + "] DeviceToken=" + tmp1 );
             */
            // Logging.info( "[messageSendFailed] message.getDeviceToken()=" + tmp1 );
            // Logging.info( "[messageSendFailed] message.marshall() =" + message.marshall() );
            // Logging.info( "[messageSendFailed] message.getIdentifier() =" + message.getIdentifier() );
            // Logging.info( "[messageSendFailed] message.getExpiry() =" + message.getExpiry() );
            // Logging.info( "[messageSendFailed] e=" + e );

        }

        @Override
        public void connectionClosed(DeliveryError e, int messageIdentifier)
        {

            // .withNoErrorDetection()をコメントアウトすると通るようになる
            // Logging.info("MyConnectionClosed! messageIdentifier="+messageIdentifier,"Push2Iphone");
            // push処理後初めてコネクションが切れるときはmessageIdentifierに不正トークンの識別子が入る。（0以上）
            // messageIdentifier が -1以下のときは、既にコネクションが切れているときなので、無視する。
            //
            if ( messageIdentifier > -1 )
            {
                ErrorIdentifier = messageIdentifier;

                // Connectionを解放
                if ( apnsService != null )
                {
                    apnsService.stop();
                }
            }

            // Logging.info( "[connectionClosed] code()=" + e.code() );
            // Logging.info( "[connectionClosed] ofCode(int code)=" + e.ofCode( e.code() ) );
            // Logging.info( "[connectionClosed] messageIdentifier()=" + messageIdentifier );

        }
    }

}
