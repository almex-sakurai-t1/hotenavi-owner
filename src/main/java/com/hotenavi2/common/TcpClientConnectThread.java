/*
 * @(#)TcpClient.java 2.00 2004/03/17
 * Copyright (C) ALMEX Inc. 2004
 * AMFWEB�T�[�r�X�Ƃ̃\�P�b�g�ʐMBean
 */

package com.hotenavi2.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * AMFWEB�T�[�r�X�Ƃ̓d������M���s���B<BR>
 * �ڑ����hosts���Q�ƁB(host_hotelid) �|�[�g�ԍ�:7023�ɂĐڑ����s���B<BR>
 * ��M�d���̕����R�[�h��Windows-31J�̂܂ܖ߂����̂ŁA�J�i�E�������͕����R�[�h�ϊ�����K�v������B<BR>
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
     * TcpClientConnectThread�����������܂��B
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
     * AMFWEB�T�[�r�X����ڑ�����<BR>
     * /etc/hosts�t�@�C�����Q�Ƃ��A�T�[�o�ɐڑ����܂��B<BR>
     * �z�X�g����host_�z�e��ID�Őݒ肵�܂��B
     * 
     * @param serverName �ڑ��T�[�o��(�z�e��ID)
     * @return �ڑ�����(TRUE:�ڑ�����, FALSE:���ڑ�)
     */
    public void run()
    {
        try
        {
            clientSocket = new Socket();
            // ��M�^�C���A�E�g�̐ݒ�(30s)
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
