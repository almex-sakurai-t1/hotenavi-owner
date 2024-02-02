/*
 * @(#)MemberCardCharge.java 1.00 2015/11/19 Copyright (C) ALMEX Inc. 2015 �����o�[�Y�J�[�h�ۋ��ʒm�N���X
 */
package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * �����o�[�Y�J�[�h�ۋ��ʒm�v���N���X
 * 
 * @author T.Sakurai
 * @version 1.00 2015/11/19
 * @see ���N�G�X�g�F1054�d��<br>
 *      ���X�|���X�F1055�d��
 */
public class MemberCardCharge implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -2147877940949724578L;
    /**
     *
     */
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1054";
    final String              REPLY_COMMAND    = "1055";
    final String              DEFINE_USER_ID   = "ceritfiedid";        // �z�e�i�r�d���̌Œ胆�[�UID
    final String              DEFINE_PASSWORD  = "6268";               // �z�e�i�r�d���̌Œ�p�X���[�h
    String                    header;
    // ���M�d��
    String                    termId           = "";
    String                    password         = "";
    int                       goodsCode;                               // ���i�R�[�h
    int                       goodsPrice;                              // ���i���z
    String                    roomName         = "";                   // ��������
    String                    reserve          = "";                   // �\��

    // ��M�d��
    int                       result;                                  // �������� 1:����,2:���s

    public MemberCardCharge()
    {
        this.header = "";
        this.termId = "";
        this.password = "";
        this.goodsCode = 0;
        this.goodsPrice = 0;
        this.result = 0;
        this.roomName = "";
        this.reserve = "";
    }

    public String getHeader()
    {
        return header;
    }

    public int getGoodsCode()
    {
        return goodsCode;
    }

    public int getGoodsPrice()
    {
        return goodsPrice;
    }

    public int getResult()
    {
        return result;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setGoodsCode(int goodsCode)
    {
        this.goodsCode = goodsCode;
    }

    public void setGoodsPrice(int goodsPrice)
    {
        this.goodsPrice = goodsPrice;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public boolean sendToHost(String frontIp, int timeOut, int portNo, String hotelId)
    {
        String sendData = "";
        TcpClientEx tcpclient = null;
        String recvData = "";
        char[] charData = null;
        String data = "";
        int retryCount = 0;
        boolean ret = false;

        int nIndex = 0;
        ClipString clip = new ClipString();

        // ���o�f�[�^�̊J�n�ʒu
        nIndex = HEADER_LENGTH + COMMAND_LENGTH;

        // �z�X�g���f�[�^���M
        tcpclient = new TcpClientEx();
        // �w���ip�A�h���X�ɐڑ�
        ret = tcpclient.connectServiceByAddr( frontIp, timeOut, portNo );

        if ( ret != false )
        {
            try
            {
                sendData = COMMAND;
                sendData += DEFINE_USER_ID;
                sendData += DEFINE_PASSWORD;
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.goodsCode ), 9 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.goodsPrice ), 9 );
                sendData += tcpclient.leftFitFormat( this.roomName, 8 );
                sendData += tcpclient.leftFitFormat( this.reserve, 10 );

                header = tcpclient.getPacketHeader( hotelId, sendData.getBytes( "Windows-31J" ).length );
                sendData = header + sendData;
                int roop = 0;
                while( true )
                {
                    // �d�����M
                    tcpclient.send( sendData );

                    // ��M�ҋ@
                    recvData = tcpclient.recv();

                    roop++;
                    if ( recvData.indexOf( "exception" ) >= 0 )
                    {
                        Logging.error( "�d����MException " + recvData );
                    }
                    else
                    {
                        charData = new char[recvData.length()];
                        charData = recvData.toCharArray();

                        // �R�}���h�擾
                        data = new String( charData, HEADER_LENGTH, COMMAND_LENGTH );

                        // �����d���R�}���h��1055�Ȃ琳��������
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            this.termId = clip.clipWord( charData, nIndex, 11 );
                            nIndex = clip.getNextIndex();

                            this.password = clip.clipWord( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // �����Ԃ��Đ����Ƃ���
                            if ( this.result == 1 )
                            {
                                ret = true;
                            }
                        }
                    }
                    if ( roop >= retryCount )
                    {
                        break;
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[MemberCardCharge.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }
        return ret;

    }
}