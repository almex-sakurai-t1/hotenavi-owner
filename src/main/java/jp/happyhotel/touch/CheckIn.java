package jp.happyhotel.touch;

import java.io.Serializable;
import java.net.SocketTimeoutException;

import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * �n�s�z�e�^�b�`�`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 * @see ���N�G�X�g�F1000�d��<br>
 *      ���X�|���X�F1001�d��
 */
public class CheckIn implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4403385171619391147L;
    final int                 TIMEOUT          = 10000;
    final int                 HOTENAVI_PORT_NO = 7023;
    final int                 HAPIHOTE_PORT_NO = 7046;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1000";
    final String              REPLY_COMMAND    = "1001";
    String                    header;

    // ���M�d��
    int                       seq;
    String                    roomName;
    String                    reserve;
    int                       nowPoint;

    // ��M�d��
    int                       result;
    int                       errorCode;

    public CheckIn()
    {
        seq = 0;
        roomName = "0";
        reserve = "";
        result = 0;
        errorCode = 0;
        nowPoint = 0;
    }

    public String getHeader()
    {
        return header;
    }

    public int getSeq()
    {
        return seq;
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

    public int getErrorCode()
    {
        return errorCode;
    }

    public int getNowPoint()
    {
        return nowPoint;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setRoomName(String roomNo)
    {
        this.roomName = roomNo;
    }

    public void setReserve(String reserve)
    {
        this.reserve = reserve;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public void setNowPoint(int nowPoint)
    {
        this.nowPoint = nowPoint;
    }

    public boolean sendToHost(int paramId)
    {
        // �z�e���̃t�����gIP���擾
        String frontIp = HotelIp.getFrontIp( paramId );
        int timeOut = TIMEOUT;
        int portNo = HAPIHOTE_PORT_NO;

        String sendData = "";
        TcpClientEx tcpclient = null;
        String recvData = "";
        char[] charData = null;
        String data = "";
        int retryCount = 0;
        boolean ret = false;

        // �z�X�g���f�[�^���M
        tcpclient = new TcpClientEx();
        // �w���ip�A�h���X�ɐڑ�
        ret = tcpclient.connectServiceByAddr( frontIp, timeOut, portNo );
        if ( ret != false )
        {
            try
            {
                sendData = COMMAND;
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.seq ), 9 );
                sendData += tcpclient.leftFitFormat( this.roomName, 8 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.nowPoint ), 9 );
                sendData += tcpclient.rightFitFormat( this.reserve, 91 );

                header = tcpclient.getPacketHapihoteHeader( Integer.toString( paramId ), sendData.getBytes( "Windows-31J" ).length );
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

                        // �����d���R�}���h��1000�Ȃ琳��������
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            // �Ԃ��Ă��������Z�b�g
                            this.result = Integer.valueOf( new String( charData, 36, 2 ) ).intValue();
                            this.errorCode = Integer.valueOf( new String( charData, 38, 4 ) ).intValue();

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
            catch ( SocketTimeoutException e )
            {
                Logging.error( "[HtCheckIn.sendToHost()]Exception:" + e.toString() );
                // �`�F�b�N�C���G���[�̂Ƃ��́A�^�C���A�E�g
                this.errorCode = 30118;
                ret = false;
            }
            catch ( Exception e )
            {
                Logging.error( "[HtCheckIn.sendToHost()]Exception:" + e.toString() );
                this.errorCode = 30119;
                ret = false;
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }

        return ret;

    }
}
