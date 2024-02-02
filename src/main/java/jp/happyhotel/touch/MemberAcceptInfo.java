package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * �����o�[��t���擾�ʒm�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see ���N�G�X�g�F1042�d��<br>
 *      ���X�|���X�F1043�d��
 */
public class MemberAcceptInfo implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 5385130148620657030L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1042";
    final String              REPLY_COMMAND    = "1043";
    String                    header;
    boolean                   boolMemberAccept;
    String                    customId;

    // ���M�d��
    String                    roomName;
    String                    reserve;

    // ��M�d��
    int                       result;
    String                    memberId;
    String                    securityCode;
    String                    birthMonth;
    String                    birthDay;

    public MemberAcceptInfo()
    {
        this.header = "";
        this.roomName = "";
        this.reserve = "";
        this.result = 0;
        this.memberId = "";
        this.securityCode = "";
        this.birthMonth = "";
        this.birthDay = "";
        this.boolMemberAccept = false;
        this.customId = "";
    }

    public String getHeader()
    {
        return header;
    }

    public boolean isBoolMemberAccept()
    {
        return boolMemberAccept;
    }

    public String getCustomId()
    {
        return customId;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getReserve()
    {
        return reserve;
    }

    public int getResult()
    {
        return result;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public String getSecurityCode()
    {
        return securityCode;
    }

    public String getBirthMonth()
    {
        return birthMonth;
    }

    public String getBirthDay()
    {
        return birthDay;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setBoolMemberAccept(boolean boolMemberAccept)
    {
        this.boolMemberAccept = boolMemberAccept;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setReserve(String reserve)
    {
        this.reserve = reserve;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }

    public void setBirthMonth(String birthMonth)
    {
        this.birthMonth = birthMonth;
    }

    public void setBirthDay(String birthDay)
    {
        this.birthDay = birthDay;
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
            ret = false;
            try
            {
                sendData = COMMAND;
                sendData += tcpclient.leftFitFormat( this.roomName, 8 );
                sendData += tcpclient.leftFitFormat( this.reserve, 120 );

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

                        // �����d���R�}���h��1043�Ȃ琳��������
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {

                            // �Ԃ��Ă��������Z�b�g

                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();
                            // �����ԍ�
                            this.roomName = clip.clipWord( charData, nIndex, 8 );
                            nIndex = clip.getNextIndex();
                            // �����o�[ID
                            this.memberId = clip.clipWord( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();
                            // �Z�L�����e�B�R�[�h
                            this.securityCode = clip.clipWord( charData, nIndex, 5 );
                            nIndex = clip.getNextIndex();
                            // �a����
                            this.birthMonth = clip.clipWord( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();
                            // �a����
                            this.birthDay = clip.clipWord( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();
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
                Logging.error( "[this.sendToHost()]Exception:" + e.toString() );
            }

            tcpclient.disconnectService();
        }
        return ret;
    }

    public void getMemberAcceptInfo(int id, String roomName, String frontIp)
    {
        final int TIMEOUT = 10000;
        final int HOTENAVI_PORT_NO = 7023;
        final int RESULT_OK = 1;
        final int RESULT_NG = 2;

        // �����o�[�Ȃ��������ɑ΂��Ď��C�J�[�h�}���`�F�b�N
        this.setRoomName( roomName );
        this.sendToHost( frontIp, TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( id ) );
        // �J�[�h��t�ς�
        if ( this.getResult() == RESULT_OK )
        {
            boolMemberAccept = true;
            // �����o�[ID���擾
            // �J�[�h��t�ς݁��u�V�K����o�^��t�v�ƁA�u���̃J�[�h���g�p����v
            customId = this.getMemberId();
        }
        else
        {
            // �J�[�h����t���u�V�K����o�^��t�v�Ɓu���ɃJ�[�h�������Ă���v
            boolMemberAccept = false;
        }
    }

}
