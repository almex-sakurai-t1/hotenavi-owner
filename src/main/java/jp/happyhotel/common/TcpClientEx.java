/*
 * @(#)TcpClient.java 2.00 2004/03/17
 * Copyright (C) ALMEX Inc. 2004
 * AMFWEB�T�[�r�X�Ƃ̃\�P�b�g�ʐMBean
 */

package jp.happyhotel.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * AMFWEB�T�[�r�X�Ƃ̓d������M���s���B<BR>
 * �ڑ����hosts���Q�ƁB(host_hotelid) �|�[�g�ԍ�:6981�ɂĐڑ����s���B<BR>
 * ��M�d���̕����R�[�h��Shift_JIS�̂܂ܖ߂����̂ŁA�J�i�E�������͕����R�[�h�ϊ�����K�v������B<BR>
 * new String( strData.getBytes("8859_1"), "Shift_JIS")
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/17
 */
public class TcpClientEx
{
    /** �ڑ���|�[�g�ԍ���` **/
    private static final int   SERVICE_PORTNO  = 7023;
    /** ��M�^�C���A�E�g�ݒ�l��` **/
    private static final int   SERVICE_TIMEOUT = 15;

    private Socket             clientSocket;
    private InputStreamReader  recvBuff;
    private BufferedReader     recvBuffRead;
    private OutputStreamWriter sendBuff;

    private static int         seqNo;

    /**
     * TcpClient�����������܂��B
     */
    public TcpClientEx()
    {
        seqNo = 0;
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

        try
        {
            // �T�[�o����ҏW����
            serverName = "host_" + serverName;

            // AMFWEB�T�[�r�X�ɐڑ�����
            clientSocket = new Socket( serverName, SERVICE_PORTNO );

            // ��M�p�o�b�t�@�̎擾
            recvBuff = new InputStreamReader( clientSocket.getInputStream(), "8859_1" );
            recvBuffRead = new BufferedReader( recvBuff );
            // ���M�p�o�b�t�@�̎擾
            sendBuff = new OutputStreamWriter( clientSocket.getOutputStream(), "Windows-31J" );

            // ��M�^�C���A�E�g�̐ݒ�(15s)
            clientSocket.setSoTimeout( SERVICE_TIMEOUT );

        }
        catch ( IOException e )
        {
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
     * @param timeout �^�C���A�E�g����(�~���b)
     * @param portno �߰Ĕԍ�
     * @return �ڑ�����(TRUE:�ڑ�����, FALSE:���ڑ�)
     */
    public boolean connectServiceByAddr(String ipAddr, int timeout, int portno)
    {
        try
        {
            // AMFWEB�T�[�r�X�ɐڑ�����
            clientSocket = new Socket( ipAddr, portno );
            Logging.info( ipAddr + ", " + portno );

            // ��M�p�o�b�t�@�̎擾
            recvBuff = new InputStreamReader( clientSocket.getInputStream(), "8859_1" );

            Logging.info( "recvBuff:" + recvBuff.toString() );

            recvBuffRead = new BufferedReader( recvBuff );
            // ���M�p�o�b�t�@�̎擾
            sendBuff = new OutputStreamWriter( clientSocket.getOutputStream(), "Windows-31J" );
            Logging.info( "sendBuff:" + sendBuff.toString() );

            // ��M�^�C���A�E�g�̐ݒ�(15s)
            if ( timeout > 0 )
            {
                clientSocket.setSoTimeout( timeout );
            }
            else
            {
                clientSocket.setSoTimeout( SERVICE_TIMEOUT );
            }
        }
        catch ( IOException e )
        {
            Logging.error( "[TcpClientEx.connectServiceByAddr()] Exception:" + e.toString() + ",ip:" + ipAddr + ", port:" + portno + ", timeout:" + timeout );
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
            clientSocket.close();
        }
        catch ( IOException e )
        {
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
    public void send(String sendData) throws IOException
    {
        char cBuf = 0x03;
        // �Ō��ETX��t������
        sendData = sendData + cBuf;
        sendBuff.write( sendData );
        Logging.info( sendData );
        sendBuff.flush();
        Logging.info( "send_fix:" );
    }

    /**
     * �d����M����
     * 
     * @return ��M�d��(Shift_JIS�R�[�h)
     */
    public String recv() throws IOException
    {
        int nBuff;
        StringBuffer strRecv;

        strRecv = new StringBuffer();
        Logging.info( "tcp_recv->" );
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
            Logging.info( "strRecv:" + strRecv );

        }
        catch ( SocketException e )
        {
            Logging.error( "[TcpClientEx.recv()] Exception:" + e.toString() );
            strRecv.append( e.toString() );
        }
        finally
        {
        }

        // Logging.info( "tcp_recv->" + Debug.dump( strRecv.toString() ) );

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
        strData = leftFitFormat( strHotelId, 10 );
        strHeader = strHeader + strData;

        // �\��
        strData = "  ";
        strHeader = strHeader + strData;

        seqNo = seqNo + 1;

        return(strHeader);
    }

    /**
     * �d���w�b�_�쐬����
     * 
     * @param strHotelId �z�e���h�c
     * @param nLength �f�[�^�������O�X
     * @return �d���w�b�_
     */
    public String getPacketHapihoteHeader(String strHotelId, int nLength)
    {
        String strHeader;
        String strData;
        NumberFormat nf;

        // �ʐM�`�o��
        strHeader = "HAPIHOTE";

        // �V�[�P���X�ԍ�
        nf = new DecimalFormat( "000000" );
        strData = nf.format( seqNo );
        strHeader = strHeader + strData;

        // �d����(+1��ETX)
        nf = new DecimalFormat( "000000" );
        strData = nf.format( nLength + 32 + 1 );
        strHeader = strHeader + strData;

        // �z�e���R�[�h
        strData = leftFitFormat( strHotelId, 10 );
        strHeader = strHeader + strData;

        // �\��
        strData = "  ";
        strHeader = strHeader + strData;

        seqNo = seqNo + 1;

        return(strHeader);
    }

    /**
     * ���l�߃X�y�[�X���߃t�H�[�}�b�g
     * �ҏW�㌅�������̌�����菬�����ꍇ�̓J�b�g����
     * 
     * @param strOrg �ҏW�O������
     * @param nLength �ҏW�㕶����
     * @return String �ҏW�㕶����
     */
    public String leftFitFormat(String strOrg, int nLength)
    {
        int i;
        byte bytebuff[];
        byte cutbuff[];

        cutbuff = new byte[nLength];

        try
        {
            bytebuff = strOrg.getBytes( "Windows-31J" );
            for( i = 0 ; i < nLength ; i++ )
            {
                if ( i < bytebuff.length )
                {
                    cutbuff[i] = bytebuff[i];
                }
                else
                {
                    cutbuff[i] = ' ';
                }
            }

            return(new String( cutbuff, "Windows-31J" ));
        }
        catch ( Exception e )
        {
            return("");
        }
    }

    /**
     * ���l�ߑS�p�X�y�[�X���߃t�H�[�}�b�g
     * 
     * @param strOrg �ҏW�O������
     * @param nLength �ҏW�㕶����
     * @return String �ҏW�㕶����
     */
    public String leftFitFullFormat(String strOrg, int nLength)
    {
        int i;
        int strlen;
        String buff;

        buff = new String( strOrg );

        strlen = buff.getBytes().length;

        for( i = 0 ; i < nLength - strlen ; i += 2 )
        {
            buff = buff + "�@";
        }

        return(buff);
    }

    /**
     * �E�l�߃X�y�[�X���߃t�H�[�}�b�g
     * 
     * @param strOrg �ҏW�O������
     * @param nLength �ҏW�㕶����
     * @return String �ҏW�㕶����
     */
    public String rightFitFormat(String strOrg, int nLength)
    {
        int i;
        int strlen;
        String buff;
        String space;

        buff = new String( strOrg );
        space = "";

        strlen = buff.getBytes().length;

        for( i = 0 ; i < nLength - strlen ; i++ )
        {
            space = space + " ";
        }

        buff = space + buff;

        return(buff);
    }

    /**
     * �E�l��0���߃t�H�[�}�b�g
     * 
     * @param strOrg �ҏW�O������
     * @param nLength �ҏW�㕶����
     * @return String �ҏW�㕶����
     */
    public String rightFitZeroFormat(String strOrg, int nLength)
    {
        int i;
        int strlen;
        String buff;
        String space;

        buff = new String( strOrg );
        space = "";

        strlen = buff.getBytes().length;

        for( i = 0 ; i < nLength - strlen ; i++ )
        {
            space = space + "0";
        }

        buff = space + buff;

        return(buff);
    }

    /**
     * �E�l�ߑS�p�X�y�[�X���߃t�H�[�}�b�g
     * 
     * @param strOrg �ҏW�O������
     * @param nLength �ҏW�㕶����
     * @return String �ҏW�㕶����
     */
    public String rightFitFullFormat(String strOrg, int nLength)
    {
        int i;
        int strlen;
        String buff;
        String space;

        buff = new String( strOrg );
        space = "";

        strlen = buff.getBytes().length;

        for( i = 0 ; i < nLength - strlen ; i += 2 )
        {
            space = space + "�@";
        }

        buff = space + buff;

        return(buff);
    }

    /**
     * �L��������擾�i�S�p�X�y�[�X�J�b�g�j
     * 
     * @param strOrg �ҏW�O������
     * @return String �ҏW�㕶����
     */
    public String cutFullSpace(String strOrg) throws IOException
    {
        int i;
        int point;
        String data;
        String buff;

        point = -1;

        // �S�p�ʒu���`�F�b�N����
        for( i = 0 ; i < strOrg.length() ; i += 2 )
        {
            data = new String( strOrg.substring( i, i + 2 ).getBytes( "8859_1" ), "Windows-31J" );
            if ( data.compareTo( "�@" ) == 0 )
            {
                if ( point == -1 )
                {
                    point = i;
                }
            }
            else
            {
                point = -1;
            }
        }

        if ( point >= 0 )
        {
            buff = strOrg.substring( 0, point );
        }
        else
        {
            buff = strOrg;
        }

        return(buff);
    }
}
