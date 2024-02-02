/*
 * @(#)TcpClient.java 2.00 2004/03/17
 * Copyright (C) ALMEX Inc. 2004
 * AMFWEB�T�[�r�X�Ƃ̃\�P�b�g�ʐMBean
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
 * AMFWEB�T�[�r�X�Ƃ̓d������M���s���B<BR>
 * �ڑ����hosts���Q�ƁB(host_hotelid) �|�[�g�ԍ�:7023�ɂĐڑ����s���B<BR>
 * ��M�d���̕����R�[�h��Windows-31J�̂܂ܖ߂����̂ŁA�J�i�E�������͕����R�[�h�ϊ�����K�v������B<BR>
 * new String( strData.getBytes("8859_1"), "Windows-31J")
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/17
 */
public class TcpClient implements Serializable
{
    /** �ڑ���|�[�g�ԍ���` **/
    private static final int   SERVICE_PORTNO       = 7023;
    /** �ڑ��^�C���A�E�g�ݒ�l��` **/
    private static final int   SERVICE_CONN_TIMEOUT = 5000;
    /** ��M�^�C���A�E�g�ݒ�l��` **/
    private static final int   SERVICE_RECV_TIMEOUT = 30000;

    private Socket             clientSocket;
    private InputStreamReader  recvBuff;
    private BufferedReader     recvBuffRead;
    private OutputStreamWriter sendBuff;
    private LogLib             log;

    private static int         seqNo;

    /**
     * TcpClient�����������܂��B
     */
    public TcpClient()
    {
        seqNo = 0;
        log = new LogLib();
    }

    /**
     * AMFWEB�T�[�r�X����ڑ�����<BR>
     * /etc/hosts�t�@�C�����Q�Ƃ��A�T�[�o�ɐڑ����܂��B<BR>
     * �z�X�g����host_�z�e��ID�Őݒ肵�܂��B
     * 
     * @param serverName �ڑ��T�[�o��(�z�e��ID)
     * @return �ڑ�����(TRUE:�ڑ�����, FALSE:���ڑ�)
     */
    public boolean connectService(String serverName)
    {
        return connectService( serverName, null );
    }

    public boolean connectService(String serverName, Integer timeOut)
    {

        try
        {
            // �T�[�o����ҏW����
            serverName = "host_" + serverName;

            InetSocketAddress endpoint = new InetSocketAddress( InetAddress.getByName( serverName ), SERVICE_PORTNO );
            System.out.println( "connectService serverName:" + serverName + ",port:" + SERVICE_PORTNO );

            int receiveTimeOut = timeOut == null ? SERVICE_RECV_TIMEOUT : timeOut;

            clientSocket = new Socket();
            // ��M�^�C���A�E�g�̐ݒ�(30s)
            clientSocket.setSoTimeout( receiveTimeOut );
            clientSocket.connect( endpoint, receiveTimeOut );

            // ��M�p�o�b�t�@�̎擾
            recvBuff = new InputStreamReader( clientSocket.getInputStream(), "8859_1" );
            recvBuffRead = new BufferedReader( recvBuff );
            // ���M�p�o�b�t�@�̎擾
            sendBuff = new OutputStreamWriter( clientSocket.getOutputStream(), "Windows-31J" );

            // ��M�^�C���A�E�g�̐ݒ�(30s)
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
     * AMFWEB�T�[�r�X����ڑ�����(IP�A�h���X�ł̐ڑ�)<BR>
     * IP�A�h���X�𕶎���œn���܂��B("192.168.210.1"�Ȃ�)
     * 
     * @param ipAddr �ڑ��T�[�oIP�A�h���X������
     * @return �ڑ�����(TRUE:�ڑ�����, FALSE:���ڑ�)
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
                // ��M�p�o�b�t�@�̎擾
                recvBuff = new InputStreamReader( clientSocket.getInputStream(), "8859_1" );
                recvBuffRead = new BufferedReader( recvBuff );
                // ���M�p�o�b�t�@�̎擾
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
     * AMFWEB�T�[�r�X����ؒf����
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
     * �d�����M����
     * 
     * @param sendData ���M�d��
     */
    public void send(String sendData)
    {
        char cBuf = 0x03;

        try
        {
            // �Ō��ETX��t������
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
     * �d����M����
     * 
     * @return ��M�d��(Windows-31J�R�[�h)
     */
    public String recv() throws IOException
    {
        int nBuff;
        StringBuffer strRecv;

        strRecv = new StringBuffer();

        try
        {
            // ETX����M����܂Ŏ�M����
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
     * �d���w�b�_�쐬����
     * 
     * @param strHotelId �z�e���h�c
     * @param nLength �f�[�^�������O�X
     * @return �d���w�b�_
     */
    public String getPacketHeader(String strHotelId, int nLength)
    {
        String strHeader;
        String strData;
        NumberFormat nf;
        StringFormat strForm;

        // �ʐM�`�o��
        strHeader = "HOTENAVI";

        // �V�[�P���X�ԍ�
        nf = new DecimalFormat( "000000" );
        strData = nf.format( seqNo );
        strHeader = strHeader + strData;

        // �d����(+1��ETX)
        nf = new DecimalFormat( "000000" );
        strData = nf.format( nLength + 32 + 1 );
        strHeader = strHeader + strData;

        // �z�e���R�[�h
        strForm = new StringFormat();
        strData = strForm.leftFitFormat( strHotelId, 10 );
        strHeader = strHeader + strData;

        // �\��
        strData = "  ";
        strHeader = strHeader + strData;

        seqNo = seqNo + 1;

        return(strHeader);
    }

    /**
     * �l�b�g���[�N�A�h���X�擾����
     * 
     * @param ipaddr IP�A�h���X
     * @param netmask �T�u�l�b�g�}�X�N
     * @return �l�b�g���[�N�A�h���X
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
            // �w�肳�ꂽIP�A�h���X�̃o�C�g�I�[�_�[�����߂�
            checkaddress = InetAddress.getByName( ipaddr );
            checkaddrbyte = checkaddress.getAddress();

            // �w�肳�ꂽ�T�u�l�b�g�}�X�N�̃o�C�g�I�[�_�[�����߂�
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
