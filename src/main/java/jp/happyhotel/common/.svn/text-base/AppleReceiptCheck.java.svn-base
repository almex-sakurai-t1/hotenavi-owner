package jp.happyhotel.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import jp.happyhotel.data.DataApUuid;
import jp.happyhotel.others.AppleReceiptInfo;
import jp.happyhotel.others.AppleReceiptStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AppleReceiptCheck
{
    public final int     PRODUCTION_ID      = 100000;
    public final int     SANDBOX_ID         = 200000;
    private final String CONF_PATH          = "/etc/happyhotel/common.conf";
    private final String PRODUCTION_URL_KEY = "apple.receiptcheck.url.production";
    private final String SANDBOX_URL_KEY    = "apple.receiptcheck.url.sandbox";
    private final String APPLE_PASSWORD_KEY = "apple.receiptcheck.password";

    public int execute(String paramUuid) throws Exception
    {
        String receipt;
        String urlProduction;
        String urlSandbox;
        String password;
        Properties prop;

        Logging.info( "AppleReceiptCheck.execute start" );

        // confファイルからキーと値のリストを読み込む
        FileInputStream conffile = new FileInputStream( CONF_PATH );
        prop = new Properties();
        prop.load( conffile );

        // 設定値を取得
        urlProduction = prop.getProperty( PRODUCTION_URL_KEY );
        urlSandbox = prop.getProperty( SANDBOX_URL_KEY );
        password = prop.getProperty( APPLE_PASSWORD_KEY );

        // confファイルクローズ
        prop = null;
        conffile.close();

        try
        {
            DataApUuid dau = new DataApUuid();
            dau.getData( paramUuid );
            receipt = dau.getReceipt();
        }
        // レシートがDBから取得できない場合
        catch ( Exception exception )
        {
            Logging.info( "AppleReceiptCheck.execute receipt cannot read." );

            throw exception;
        }

        AppleReceiptStatus productionMapper;
        productionMapper = ReceiptCheckToAppStore( urlProduction, password, receipt );
        Logging.info( "production status=" + productionMapper.status );

        // レシートはサンドボックス製
        if ( productionMapper.status == 21007 )
        {
            Logging.info( "This receipt was built on sandbox." );
            AppleReceiptStatus sandboxMapper;
            sandboxMapper = ReceiptCheckToAppStore( urlSandbox, password, receipt );
            Logging.info( "sandbox status=" + sandboxMapper.status );

            if ( sandboxMapper.status == 0 )
            {
                // 有効期限を確認する
                CheckExpire( sandboxMapper );
            }

            Logging.info( "AppleReceiptCheck.execute end" );

            return SANDBOX_ID + sandboxMapper.status;
        }
        else
        {
            if ( productionMapper.status == 0 )
            {
                // 有効期限を確認する
                CheckExpire( productionMapper );
            }

            Logging.info( "AppleReceiptCheck.execute end" );

            return PRODUCTION_ID + productionMapper.status;
        }
    }

    public int execute(String paramUuid,String paramReceipt) throws Exception
    {
        String receipt;
        String urlProduction;
        String urlSandbox;
        String password;
        Properties prop;

        Logging.info( "AppleReceiptCheck.execute start" );

        // confファイルからキーと値のリストを読み込む
        FileInputStream conffile = new FileInputStream( CONF_PATH );
        prop = new Properties();
        prop.load( conffile );

        // 設定値を取得
        urlProduction = prop.getProperty( PRODUCTION_URL_KEY );
        urlSandbox = prop.getProperty( SANDBOX_URL_KEY );
        password = prop.getProperty( APPLE_PASSWORD_KEY );

        // confファイルクローズ
        prop = null;
        conffile.close();

        AppleReceiptStatus productionMapper;
        productionMapper = ReceiptCheckToAppStore( urlProduction, password, paramReceipt );
        Logging.info( "production status=" + productionMapper.status );

        // レシートはサンドボックス製
        if ( productionMapper.status == 21007 )
        {
            Logging.info( "This receipt was built on sandbox." );
            AppleReceiptStatus sandboxMapper;
            sandboxMapper = ReceiptCheckToAppStore( urlSandbox, password, paramReceipt );
            Logging.info( "sandbox status=" + sandboxMapper.status );

            if ( sandboxMapper.status == 0 )
            {
                // 有効期限を確認する
                CheckExpire( sandboxMapper );
            }

            Logging.info( "AppleReceiptCheck.execute end" );

            return SANDBOX_ID + sandboxMapper.status;
        }
        else
        {
            if ( productionMapper.status == 0 )
            {
                // 有効期限を確認する
                CheckExpire( productionMapper );
            }

            Logging.info( "AppleReceiptCheck.execute end" );

            return PRODUCTION_ID + productionMapper.status;
        }
    }

    public AppleReceiptStatus ReceiptCheckToAppStore(String url, String password, String receipt) throws Exception
    {
        // 1. URL
        URL connecturl = new URL( url );

        // 2. URLへアクセス
        HttpURLConnection conn = (HttpURLConnection)connecturl.openConnection();
        conn.setRequestMethod( "POST" );

        // 3. ヘッダー情報のセット
        conn.setRequestProperty( "Content-Type", "application/json" );
        conn.setDoOutput( true );

        // 4. JSONの準備
        String param = "{\"receipt-data\":\"" + receipt + "\", \"password\":\"" + password + "\"}";

        // 5. データ送信
        PrintWriter pw = new PrintWriter( conn.getOutputStream() );
        pw.print( param );
        pw.close();

        // 6. レスポンスを取得
        int responseCode = conn.getResponseCode();
        BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
        String strLine;
        String response = new String();

        while( (strLine = br.readLine()) != null )
        {
            response += strLine;
        }
        br.close();

        // 7. レスポンスを出力
        Logging.info( "Sending 'POST' request to URL : " + url );
        Logging.info( "Content-Type:" + conn.getRequestProperty( "Content-Type" ) );
        Logging.info( "Response Code : " + responseCode );
        Logging.info( "Response Message : " + conn.getResponseMessage() );
        Logging.info( response.toString() );

        // 8. 切断
        conn.disconnect();

        // 9. レスポンスのJSONをマッピング
        ObjectMapper mapper = new ObjectMapper();
        AppleReceiptStatus arsm = mapper.readValue( response, AppleReceiptStatus.class );

        return arsm;
    }

    public void CheckExpire(AppleReceiptStatus mapper)
    {
        long latestExpiresMs = 0;

        for( AppleReceiptInfo info : mapper.latest_receipt_info )
        {
            if ( latestExpiresMs < info.expires_date_ms )
            {
                latestExpiresMs = info.expires_date_ms;
            }
        }

        long currentMs = System.currentTimeMillis();

        Logging.info( "latestExpiresMs=" + latestExpiresMs + ", currentMs=" + currentMs );

        if ( currentMs > latestExpiresMs )
        {
            mapper.status = 21006;
        }
    }
}
