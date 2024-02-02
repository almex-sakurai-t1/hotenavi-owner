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
 * @see ���N�G�X�g�F0506�d��<br>
 *      ���X�|���X�F0507�d��
 */
public class DiscountResults implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 7921459974422804536L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "0502";
    final String              REPLY_COMMAND    = "0503";
    final String              DEFINE_USER_ID   = "ceritfiedid";       // �z�e�i�r�d���̌Œ胆�[�UID
    final String              DEFINE_PASSWORD  = "6268";              // �z�e�i�r�d���̌Œ�p�X���[�h
    String                    header;
    // ���M�d��
    String                    termId;
    String                    password;
    int                       startDate;
    int                       endDate;

    // ��M�d��
    int                       result;
    int                       discountLength;
    ArrayList<Integer>        collectDate;                            // �W�v��
    ArrayList<Integer>        discountAll;                            // ��������
    ArrayList<Integer>        discountReal;                           // ��������
    ArrayList<Integer>        discountTotal;                          // �������z
    ArrayList<Integer>        discountResult;                         // �������z

    public DiscountResults()
    {
        this.header = "";
        this.termId = "";
        this.password = "";
        this.startDate = 0;
        this.endDate = 0;
        this.result = 0;
        this.discountLength = 0;
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
        int i = 0;

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

                        // �����d���R�}���h��0503�Ȃ琳��������
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            this.termId = clip.clipWord( charData, nIndex, 11 );
                            nIndex = clip.getNextIndex();

                            this.password = clip.clipWord( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            this.discountLength = clip.clipNum( charData, nIndex, 3 );
                            nIndex = clip.getNextIndex();

                            while( this.discountLength < i )
                            {
                                // ���ږ��ɔ��o
                                this.collectDate.add( clip.clipNum( charData, nIndex, 8 ) );
                                nIndex = clip.getNextIndex();

                                this.discountAll.add( clip.clipNum( charData, nIndex, 8 ) );
                                nIndex = clip.getNextIndex();

                                this.discountReal.add( clip.clipNum( charData, nIndex, 8 ) );
                                nIndex = clip.getNextIndex();

                                this.discountTotal.add( clip.clipNum( charData, nIndex, 8 ) );
                                nIndex = clip.getNextIndex();

                                this.discountResult.add( clip.clipNum( charData, nIndex, 8 ) );
                                nIndex = clip.getNextIndex();

                                i++;
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
