/*
 * @(#)TcpClient.java 2.00 2004/03/17
 * Copyright (C) ALMEX Inc. 2004
 * AMFWEBサービスとのソケット通信Bean
 */

package com.hotenavi2.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * AMFWEBサービスとの電文送受信を行う。<BR>
 * 接続先をhostsより参照。(host_hotelid) ポート番号:7023にて接続を行う。<BR>
 * 受信電文の文字コードはWindows-31Jのまま戻されるので、カナ・漢字等は文字コード変換する必要がある。<BR>
 * new String( strData.getBytes("8859_1"), "Windows-31J")
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/17
 */
public class TcpClientConnectThread extends Thread implements Serializable
{
    private int                connTimeout;
    private int                recvTimeout;
    private InetSocketAddress  endpoint;
    private Socket             clientSocket;
    private InputStreamReader  recvBuff;
    private BufferedReader     recvBuffRead;
    private OutputStreamWriter sendBuff;

    private LogLib             log;

    private static int         seqNo;

    /**
     * TcpClientConnectThreadを初期化します。
     */
    public TcpClientConnectThread(InetSocketAddress endpointParam, int connTimeoutParam, int recvTimeoutParam)
    {
        clientSocket = null;
        connTimeout = 5000;
        recvTimeout = 30000;

        endpoint = endpointParam;

        if ( connTimeoutParam != 0 )
        {
            connTimeout = connTimeoutParam;
        }
        if ( recvTimeoutParam != 0 )
        {
            recvTimeout = recvTimeoutParam;
        }

        log = new LogLib();
    }

    public Socket getClientSocket()
    {
        return(clientSocket);
    }

    /**
     * AMFWEBサービス回線接続処理<BR>
     * /etc/hostsファイルを参照し、サーバに接続します。<BR>
     * ホスト名はhost_ホテルIDで設定します。
     * 
     * @param serverName 接続サーバ名(ホテルID)
     * @return 接続結果(TRUE:接続完了, FALSE:未接続)
     */
    public void run()
    {
        try
        {
            clientSocket = new Socket();
            // 受信タイムアウトの設定(30s)
            clientSocket.setSoTimeout( recvTimeout );
            clientSocket.connect( endpoint, connTimeout );

        }
        catch ( Exception e )
        {
            clientSocket = null;

            log.error( "TcpClientConnectThread.run:(" + endpoint + ")" + e.toString() );
            return;
        }
        finally
        {
        }
    }
}
