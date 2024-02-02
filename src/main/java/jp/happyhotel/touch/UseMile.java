package jp.happyhotel.touch;

import java.io.Serializable;
import java.net.SocketTimeoutException;

import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * �n�s�z�e�^�b�`�`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 * @see ���N�G�X�g�F1002�d��<br>
 *      ���X�|���X�F1003�d��
 */
public class UseMile implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4403385171619391147L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1002";
    final String              REPLY_COMMAND    = "1003";
    String                    header;

    // ���M�d��
    int                       seq;
    int                       mile;
    int                       nowPoint;
    String                    reserve;

    // ��M�d��
    int                       result;
    int                       errorCode;

    public UseMile()
    {
        seq = 0;
        mile = 0;
        nowPoint = 0;
        reserve = "";
        result = 0;
        errorCode = 0;
    }

    public String getHeader()
    {
        return header;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getMile()
    {
        return mile;
    }

    public int getNowPoint()
    {
        return nowPoint;
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

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setMile(int mile)
    {
        this.mile = mile;
    }

    public void setNowPoint(int nowPoint)
    {
        this.nowPoint = nowPoint;
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

    public boolean sendToHost(String frontIp, int timeOut, int portNo, String hotelId)
    {
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
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.mile ), 6 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.nowPoint ), 9 );
                sendData += tcpclient.leftFitFormat( this.reserve, 91 );

                header = tcpclient.getPacketHapihoteHeader( hotelId, sendData.getBytes( "Windows-31J" ).length );
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

                        // �����d���R�}���h��1003�Ȃ琳��������
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
                Logging.error( "[UseMile.sendToHost()]Exception:" + e.toString() );
                this.errorCode = 30204;
                ret = false;
            }
            catch ( Exception e )
            {
                Logging.error( "[UseMile.sendToHost()]Exception:" + e.toString() );
                this.errorCode = 30205;
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
