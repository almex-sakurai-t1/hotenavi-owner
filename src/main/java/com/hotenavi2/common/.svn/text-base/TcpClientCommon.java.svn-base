package com.hotenavi2.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TcpClientCommon implements Serializable
{
    /** 接続タイムアウト設定値定義 **/
    private static final int   SERVICE_CONN_TIMEOUT         = 5000;
    /** 受信タイムアウト設定値定義 **/
    private static final int   SERVICE_RECV_TIMEOUT         = 30000;

    /** 通信ＡＰホテナビ **/
    public static final int    CONNECTAPTYPE_HOTENAVI       = 0;
    /** 通信ＡＰキッチン **/
    public static final int    CONNECTAPTYPE_KITCHEN        = 1;

    /** 接続種別共通ホテルID **/
    public static final int    TCPCONNECTTYPE_COMMONHOTELID = 0;
    /** 接続種別指定ホテルID **/
    public static final int    TCPCONNECTTYPE_HOTELID       = 1;
    /** 接続種別指定IPアドレス **/
    public static final int    TCPCONNECTTYPE_IPADDRESS     = 2;

    private Socket             clientSocket;
    private InputStreamReader  recvBuff;
    private BufferedReader     recvBuffRead;
    private OutputStreamWriter sendBuff;
    private int                connectPortNo;
    private LogLib             log;

    private static int         seqNo;

    /**
     * TcpClientを初期化します。
     */
    public TcpClientCommon(int portNo)
    {
        connectPortNo = portNo;
        seqNo = 0;
        log = new LogLib();
    }

    /**
     * AMFWEBサービス回線接続処理<BR>
     * /etc/hostsファイルを参照し、サーバに接続します。<BR>
     * ホスト名はhost_ホテルIDで設定します。
     * 
     * @param serverName 接続サーバ名(ホテルID)
     * @return 接続結果(TRUE:接続完了, FALSE:未接続)
     */
    public boolean connectService(String serverName)
    {

        try
        {
            // サーバ名を編集する
            serverName = "host_" + serverName;

            InetSocketAddress endpoint = new InetSocketAddress( InetAddress.getByName( serverName ), connectPortNo );

            clientSocket = new Socket();
            // 受信タイムアウトの設定(30s)
            clientSocket.setSoTimeout( SERVICE_RECV_TIMEOUT );
            clientSocket.connect( endpoint, SERVICE_RECV_TIMEOUT );

            // 受信用バッファの取得
            recvBuff = new InputStreamReader( clientSocket.getInputStream(), "8859_1" );
            recvBuffRead = new BufferedReader( recvBuff );
            // 送信用バッファの取得
            sendBuff = new OutputStreamWriter( clientSocket.getOutputStream(), "Windows-31J" );

            // 受信タイムアウトの設定(30s)
            clientSocket.setSoTimeout( SERVICE_RECV_TIMEOUT );

        }
        catch ( IOException e )
        {
            log.error( "connectService:" + e.toString() );
            return(false);
        }
        finally
        {
        }

        return(true);
    }

    /**
     * AMFWEBサービス回線接続処理(IPアドレスでの接続)<BR>
     * IPアドレスを文字列で渡します。("192.168.210.1"など)
     * 
     * @param ipAddr 接続サーバIPアドレス文字列
     * @return 接続結果(TRUE:接続完了, FALSE:未接続)
     */
    public boolean connectServiceByAddr(String ipAddr)
    {
        try
        {
            InetSocketAddress endpoint = new InetSocketAddress( InetAddress.getByName( ipAddr ), connectPortNo );

            TcpClientConnectThread connThread = new TcpClientConnectThread( endpoint, SERVICE_CONN_TIMEOUT, SERVICE_RECV_TIMEOUT );
            connThread.join( SERVICE_CONN_TIMEOUT );
            connThread.run();

            clientSocket = connThread.getClientSocket();
            if ( clientSocket != null )
            {
                // 受信用バッファの取得
                recvBuff = new InputStreamReader( clientSocket.getInputStream(), "8859_1" );
                recvBuffRead = new BufferedReader( recvBuff );
                // 送信用バッファの取得
                sendBuff = new OutputStreamWriter( clientSocket.getOutputStream(), "Windows-31J" );
            }
            else
            {
                return(false);
            }

        }
        catch ( Exception e )
        {
            log.error( "connectServiceByAddr:(" + ipAddr + ")" + e.toString() );
            return(false);
        }
        finally
        {
        }

        return(true);
    }

    /**
     * ホスト接続処理
     * 
     * @param tcpclient TCP接続クライアント識別子
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean connect(int kind, String value)
    {
        boolean ret = false;
        String query;
        ResultSet result;
        DbAccess db;

        switch( kind )
        {
            // セットされたホテルIDで接続
            case TCPCONNECTTYPE_COMMONHOTELID:
                try
                {
                    db = new DbAccess();
                    query = "SELECT * FROM hotel WHERE hotel_id='" + value + "'";
                    result = db.execQuery( query );
                    if ( result.next() != false )
                    {
                        ret = connectServiceByAddr( result.getString( "front_ip" ) );
                    }
                    else
                    {
                        ret = connectService( value );
                    }

                    db.close();
                }
                catch ( Exception e )
                {
                    ret = connectService( value );
                    log.error( "connect(0):" + e.toString() );
                }
                break;

            // 指定されたホテルIDで接続
            case TCPCONNECTTYPE_HOTELID:

                try
                {
                    db = new DbAccess();
                    query = "SELECT * FROM hotel WHERE hotel_id='" + value + "'";
                    result = db.execQuery( query );
                    if ( result.next() != false )
                    {
                        ret = connectServiceByAddr( result.getString( "front_ip" ) );
                    }
                    else
                    {
                        ret = connectService( value );
                    }

                    db.close();
                }
                catch ( Exception e )
                {
                    ret = connectService( value );
                    log.error( "connect(1):" + e.toString() );
                }
                break;

            // IPアドレスで接続
            case TCPCONNECTTYPE_IPADDRESS:

                ret = connectServiceByAddr( value );
                break;

            // セットされたホテルIDで接続
            default:

                ret = connectService( value );
                break;
        }

        return(ret);

    }

    /**
     * AMFWEBサービス回線切断処理
     * 
     */
    public void disconnectService()
    {
        try
        {
            recvBuff.close();
            recvBuffRead.close();
            sendBuff.close();
            clientSocket.close();
        }
        catch ( IOException e )
        {
            log.error( "disconnectService:" + e.toString() );
        }
        finally
        {
        }
    }

    /**
     * 電文送信処理
     * 
     * @param sendData 送信電文
     */
    public void send(String sendData)
    {
        char cBuf = 0x03;

        try
        {
            // 最後にETXを付加する
            sendData = sendData + cBuf;

            log.info( "send(" + sendData + ")" );
            log.debug( "send:hex(" + Debug.hex( sendData ) + ")" );

            sendBuff.write( sendData );
            sendBuff.flush();
        }
        catch ( Exception e )
        {
            log.error( "send:" + e.toString() );
        }
    }

    /**
     * 電文受信処理
     * 
     * @return 受信電文(Windows-31Jコード)
     */
    public String recv() throws IOException
    {
        int nBuff;
        StringBuffer strRecv;

        strRecv = new StringBuffer();

        try
        {
            // ETXを受信するまで受信する
            while( true )
            {
                nBuff = recvBuffRead.read();
                if ( nBuff == 0x03 || nBuff == -1 )
                {
                    break;
                }

                strRecv.append( (char)nBuff );
            }

        }
        catch ( SocketException e )
        {
            strRecv.append( e.toString() );
            log.error( "recv:" + e.toString() );
        }
        finally
        {
        }

        log.info( "recv(" + strRecv + ")" );
        log.debug( "recv:hex(" + Debug.hex( strRecv.toString() ) + ")" );

        return(strRecv.toString());
    }

    /**
     * 電文ヘッダ作成処理
     * 
     * @param strId 端末ＩＤ/ホテルＩＤ
     * @param nLength データ部レングス
     * @return 電文ヘッダ
     */
    public String getPacketHeader(int type, String strId, int nLength)
    {
        String strHeader = "";
        String strData;
        NumberFormat nf;
        StringFormat strForm;

        switch( type )
        {
            case CONNECTAPTYPE_HOTENAVI:
                // 通信ＡＰ名
                strHeader = "HOTENAVI";

                // シーケンス番号
                nf = new DecimalFormat( "000000" );
                strData = nf.format( seqNo );
                strHeader = strHeader + strData;

                // 電文長(+1はETX)
                nf = new DecimalFormat( "000000" );
                strData = nf.format( nLength + 32 + 1 );
                strHeader = strHeader + strData;

                // ホテルコード
                strForm = new StringFormat();
                strData = strForm.leftFitFormat( strId, 10 );
                strHeader = strHeader + strData;

                // 予備
                strData = "  ";
                strHeader = strHeader + strData;

                seqNo = seqNo + 1;
                break;
            case CONNECTAPTYPE_KITCHEN:
                // 通信ＡＰ名
                strHeader = "KITCHEN";

                // シーケンス番号
                nf = new DecimalFormat( "000000" );
                strData = nf.format( seqNo );
                strHeader = strHeader + strData;

                // 電文長(+1はETX)
                nf = new DecimalFormat( "00000" );
                strData = nf.format( nLength + 32 + 1 );
                strHeader = strHeader + strData;

                // 端末ID
                strForm = new StringFormat();
                strData = strForm.leftFitFormat( strId, 5 );
                strHeader = strHeader + strData;

                // 予備
                strData = "         ";
                strHeader = strHeader + strData;

                seqNo = seqNo + 1;
                break;
            default:
                break;
        }

        return(strHeader);
    }

    /**
     * ネットワークアドレス取得処理
     * 
     * @param ipaddr IPアドレス
     * @param netmask サブネットマスク
     * @return ネットワークアドレス
     */
    public String getNetworkAddress(String ipaddr, String netmask)
    {
        int i;
        byte[] checkaddrbyte;
        byte[] checkmaskbyte;
        byte[] checkafterbyte;
        InetAddress checkaddress;
        InetAddress checknetmask;
        InetAddress checkafter = null;

        try
        {
            // 指定されたIPアドレスのバイトオーダーを求める
            checkaddress = InetAddress.getByName( ipaddr );
            checkaddrbyte = checkaddress.getAddress();

            // 指定されたサブネットマスクのバイトオーダーを求める
            checknetmask = InetAddress.getByName( netmask );
            checkmaskbyte = checknetmask.getAddress();

            checkafterbyte = new byte[checkaddrbyte.length];
            for( i = 0 ; i < checkaddrbyte.length ; i++ )
            {
                checkafterbyte[i] = (byte)(checkaddrbyte[i] & checkmaskbyte[i]);

            }
            checkafter = InetAddress.getByAddress( checkafterbyte );

            return(checkafter.getHostAddress());
        }
        catch ( Exception e )
        {
            return(ipaddr);
        }
    }
}
