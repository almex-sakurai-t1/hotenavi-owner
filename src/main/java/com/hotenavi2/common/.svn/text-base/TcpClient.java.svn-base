/*
 * @(#)TcpClient.java 2.00 2004/03/17
 * Copyright (C) ALMEX Inc. 2004
 * AMFWEBサービスとのソケット通信Bean
 */

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
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * AMFWEBサービスとの電文送受信を行う。<BR>
 * 接続先をhostsより参照。(host_hotelid) ポート番号:7023にて接続を行う。<BR>
 * 受信電文の文字コードはWindows-31Jのまま戻されるので、カナ・漢字等は文字コード変換する必要がある。<BR>
 * new String( strData.getBytes("8859_1"), "Windows-31J")
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/17
 */
public class TcpClient implements Serializable
{
    /** 接続先ポート番号定義 **/
    private static final int   SERVICE_PORTNO       = 7023;
    /** 接続タイムアウト設定値定義 **/
    private static final int   SERVICE_CONN_TIMEOUT = 5000;
    /** 受信タイムアウト設定値定義 **/
    private static final int   SERVICE_RECV_TIMEOUT = 30000;

    private Socket             clientSocket;
    private InputStreamReader  recvBuff;
    private BufferedReader     recvBuffRead;
    private OutputStreamWriter sendBuff;
    private LogLib             log;

    private static int         seqNo;

    /**
     * TcpClientを初期化します。
     */
    public TcpClient()
    {
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
        return connectService( serverName, null );
    }

    public boolean connectService(String serverName, Integer timeOut)
    {

        try
        {
            // サーバ名を編集する
            serverName = "host_" + serverName;

            InetSocketAddress endpoint = new InetSocketAddress( InetAddress.getByName( serverName ), SERVICE_PORTNO );
            System.out.println( "connectService serverName:" + serverName + ",port:" + SERVICE_PORTNO );

            int receiveTimeOut = timeOut == null ? SERVICE_RECV_TIMEOUT : timeOut;

            clientSocket = new Socket();
            // 受信タイムアウトの設定(30s)
            clientSocket.setSoTimeout( receiveTimeOut );
            clientSocket.connect( endpoint, receiveTimeOut );

            // 受信用バッファの取得
            recvBuff = new InputStreamReader( clientSocket.getInputStream(), "8859_1" );
            recvBuffRead = new BufferedReader( recvBuff );
            // 送信用バッファの取得
            sendBuff = new OutputStreamWriter( clientSocket.getOutputStream(), "Windows-31J" );

            // 受信タイムアウトの設定(30s)
            clientSocket.setSoTimeout( receiveTimeOut );

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
        return connectServiceByAddr( ipAddr, null );
    }

    public boolean connectServiceByAddr(String ipAddr, Integer timeOut)
    {
        try
        {
            InetSocketAddress endpoint = new InetSocketAddress( InetAddress.getByName( ipAddr ), SERVICE_PORTNO );
            System.out.println( "connectServiceByAddr:" + ipAddr + ",port:" + SERVICE_PORTNO );

            int receiveTimeOut = timeOut == null ? SERVICE_RECV_TIMEOUT : timeOut;

            TcpClientConnectThread connThread = new TcpClientConnectThread( endpoint, SERVICE_CONN_TIMEOUT, receiveTimeOut );
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
     * @param strHotelId ホテルＩＤ
     * @param nLength データ部レングス
     * @return 電文ヘッダ
     */
    public String getPacketHeader(String strHotelId, int nLength)
    {
        String strHeader;
        String strData;
        NumberFormat nf;
        StringFormat strForm;

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
        strData = strForm.leftFitFormat( strHotelId, 10 );
        strHeader = strHeader + strData;

        // 予備
        strData = "  ";
        strHeader = strHeader + strData;

        seqNo = seqNo + 1;

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
