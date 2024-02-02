package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * �O����擾�ʒm�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see ���N�G�X�g�F0216�d��<br>
 *      ���X�|���X�F0217�d��
 */
public class DepositInfo implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 5385130148620657030L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "0216";
    final String              REPLY_COMMAND    = "0217";
    String                    header;
    boolean                   boolUseableMile;
    String                    unavailableMessage;

    // ���M�d��
    int                       seq;
    String                    roomName;
    String                    reserve;

    // ��M�d��
    int                       result;
    int                       deposit;
    int                       charge;

    public DepositInfo()
    {
        seq = 0;
        roomName = "";
        reserve = "";
        result = 0;
        deposit = 0;
        charge = 0;
        boolUseableMile = true;
        unavailableMessage = "";
    }

    public String getHeader()
    {
        return header;
    }

    public boolean isBoolUseableMile()
    {
        return boolUseableMile;
    }

    public String getUnavailableMessage()
    {
        return unavailableMessage;
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

    public int getDeposit()
    {
        return deposit;
    }

    public int getCharge()
    {
        return charge;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setBoolUseableMile(boolean boolUseableMile)
    {
        this.boolUseableMile = boolUseableMile;
    }

    public void setUnavailableMessage(String unavailableMessage)
    {
        this.unavailableMessage = unavailableMessage;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
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

    public void setDeposit(int deposit)
    {
        this.deposit = deposit;
    }

    public void setCharge(int charge)
    {
        this.charge = charge;
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
                sendData += tcpclient.leftFitFormat( this.roomName, 8 );
                sendData += tcpclient.rightFitFormat( this.reserve, 10 );

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

                        // �����d���R�}���h��0217�Ȃ琳��������
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            // �Ԃ��Ă��������Z�b�g
                            this.result = Integer.valueOf( new String( charData, 36, 2 ) ).intValue();
                            this.deposit = Integer.valueOf( new String( charData, 38, 6 ) ).intValue();
                            this.charge = Integer.valueOf( new String( charData, 44, 6 ) ).intValue();
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
                Logging.error( "[MemberAcceptInfo.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }
        return ret;
    }

    /**
     * �O����擾
     * 
     * @param id
     * @param seq
     * @param roomName
     * @param frontIp
     * @param nonrefundableFlag
     */
    public void getDepositInfo(int id, int seq, String roomName, String frontIp, int nonrefundableFlag)
    {
        final int TIMEOUT = 1000;
        final int HOTENAVI_PORT_NO = 7023;
        final int DEPOSIT_NO = 1;
        final int DEPOSIT_CASH = 2;
        final int DEPOSIT_CREDIT = 3;
        final int NONREFUNDABLE = 1; // �ԋ��s��
        final int CREDIT_NONREFUNDABLE = 2; // �N���W�b�g���Z�̕ԋ��s��

        /** �i0216�j�z�e�i�r_�O����擾�d�� **/
        this.setSeq( seq );
        this.setRoomName( roomName );
        this.sendToHost( frontIp, TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( id ) );
        if ( this.getResult() == DEPOSIT_NO || this.getResult() == DEPOSIT_CASH || this.getResult() == DEPOSIT_CREDIT )
        {
            // �ԋ��s�ݒ肩�O�󂪂���ꍇ�A�܂��̓N���W�b�g�ԋ��s�ݒ肩�N���W�b�g�O��̏ꍇ
            if ( (nonrefundableFlag == NONREFUNDABLE && this.getResult() > DEPOSIT_NO) ||
                    (nonrefundableFlag == CREDIT_NONREFUNDABLE && this.getResult() == DEPOSIT_CREDIT) )
            {
                boolUseableMile = false;
                unavailableMessage = "�}�C�����g�p����ۂɂ̓t�����g�܂ł��A���������B";
            }
        }
    }
}
