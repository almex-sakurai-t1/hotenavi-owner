package jp.happyhotel.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * SPID取得クラス
 * 
 * <pre>
 *  /etc/happyhotel/gmoccscredit.conf にてSPID取得先の設定を行う。
 *  例）gmo.getspidurl=http://172.25.5.19:8080/gethotelspid.jsp
 * </pre>
 * 
 * @author S.Shiiya
 * @version 1.00 2012/03/20
 */
public class GetHotelSPID
{
    // GMOクレジット設定ファイル
    private final String PROPFILE   = Constants.configFilesPath + "gmoccscredit.conf";

    private String       connectUrl = "";

    // コンストラクタ
    public GetHotelSPID()
    {
        Properties prop;

        FileInputStream propfile;
        try
        {
            propfile = new FileInputStream( PROPFILE );
            prop = new Properties();
            // ﾌﾟﾛﾊﾟﾃｨﾌｧｲﾙからｷｰと値のﾘｽﾄを読み込みます
            prop.load( propfile );
            // "gmo.getspidurl"に設定されている値を取得します
            connectUrl = prop.getProperty( "gmo.getspidurl" );
            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "HotelGetSPID Error=" + e.toString() );
        }
    }

    /**
     * SPIDリクエスト処理（WEBサーバからホテルSPIDの確認）
     * 
     * @return SPID
     */
    public String requestSPID(String frontIp)
    {
        int count;
        URL url;
        HttpURLConnection urlConn;
        String spid;

        spid = "";

        // 内部WEBサーバ（uranus）にSPIDの問わせを行う
        // 接続するURLの作成
        try
        {
            if ( connectUrl.compareTo( "" ) != 0 )
            {
                url = new URL( connectUrl + "?frontip=" + frontIp );
                // 接続するオブジェクトの作成
                urlConn = (HttpURLConnection)url.openConnection();
                urlConn.setConnectTimeout( 15 * 1000 );
                // POSTメソッドをセット
                urlConn.setRequestMethod( "POST" );
                // 接続
                urlConn.connect();

                BufferedReader reader = new BufferedReader( new InputStreamReader( urlConn.getInputStream() ) );

                while( true )
                {
                    String line = reader.readLine();
                    if ( line == null )
                    {
                        break;
                    }

                    System.out.println( line );

                    // 読み取ったデータにspid=があった
                    count = line.indexOf( "spid=" );
                    if ( count >= 0 )
                    {
                        spid = line.substring( count + 5 );
                        break;
                    }
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "HotelGetSPID.requestSPID Error=" + e.toString() );
        }
        return(spid);
    }
}
