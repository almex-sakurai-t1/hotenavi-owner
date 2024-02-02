/*
 * @(#)HotelCheclIn.java 1.00 2011/05/19 Copyright (C) ALMEX Inc. 2011 �z�e���`�F�b�N�C�����擾�N���X
 */
package jp.happyhotel.touch;

import java.io.Serializable;
import java.util.ArrayList;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * �����o�[�`�F�b�N�C�����ђʒm�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see ���N�G�X�g�F1048�d��<br>
 *      ���X�|���X�F1049�d��
 */
public class MemberCheckInResults implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 7921459974422804536L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1048";
    final String              REPLY_COMMAND    = "1049";
    final String              DEFINE_USER_ID   = "ceritfiedid";       // �z�e�i�r�d���̌Œ胆�[�UID
    final String              DEFINE_PASSWORD  = "6268";              // �z�e�i�r�d���̌Œ�p�X���[�h
    String                    header;
    // ���M�d��
    String                    termId           = "";
    String                    password         = "";
    int                       startDate;
    int                       endDate;

    // ��M�d��
    int                       result;
    int                       dataLength;
    ArrayList<Integer>        collectDate;                            // �W�v��
    ArrayList<Integer>        allCheckIn;                             // �S�`�F�b�N�C����
    ArrayList<Integer>        memberCheckIn;                          // �n�s�z�e�����o�[�`�F�b�N�C����
    ArrayList<Integer>        allMember;                              // �S�����o�[��

    public MemberCheckInResults()
    {
        this.header = "";
        this.termId = "";
        this.password = "";
        this.result = 0;
    }

    public String getHeader()
    {
        return header;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getResult()
    {
        return result;
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public ArrayList<Integer> getCollectDate()
    {
        return collectDate;
    }

    public ArrayList<Integer> getAllCheckIn()
    {
        return allCheckIn;
    }

    public ArrayList<Integer> getMemberCheckIn()
    {
        return memberCheckIn;
    }

    public ArrayList<Integer> getAllMember()
    {
        return allMember;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setCollectDate(ArrayList<Integer> collectDate)
    {
        this.collectDate = collectDate;
    }

    public void setAllCheckIn(ArrayList<Integer> allCheckIn)
    {
        this.allCheckIn = allCheckIn;
    }

    public void setMemberCheckIn(ArrayList<Integer> memberCheckIn)
    {
        this.memberCheckIn = memberCheckIn;
    }

    public void setAllMember(ArrayList<Integer> allMember)
    {
        this.allMember = allMember;
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
        int dataLength = 0;

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
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.startDate ), 8 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.endDate ), 8 );

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
                        dataLength = data.length();

                        // �����d���R�}���h��1049�Ȃ琳��������
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            this.termId = clip.clipWord( charData, nIndex, 11 );
                            nIndex = clip.getNextIndex();

                            this.password = clip.clipWord( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            while( dataLength > nIndex )
                            {
                                // ���ږ��ɔ��o
                                this.collectDate.add( clip.clipNum( charData, nIndex, 8 ) );
                                nIndex = clip.getNextIndex();

                                this.allCheckIn.add( clip.clipNum( charData, nIndex, 5 ) );
                                nIndex = clip.getNextIndex();

                                this.memberCheckIn.add( clip.clipNum( charData, nIndex, 5 ) );
                                nIndex = clip.getNextIndex();

                                this.allMember.add( clip.clipNum( charData, nIndex, 5 ) );
                                nIndex = clip.getNextIndex();
                            }

                            // �����Ԃ��Đ����Ƃ���
                            ret = true;
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
                Logging.error( "[MemberCheckInResults.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }
        return ret;

    }
}
