package jp.happyhotel.touch;

import java.io.Serializable;
import java.net.SocketTimeoutException;

import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;
import jp.happyhotel.dto.DtoRsvListData;

/**
 * �n�s�z�e�^�b�`�\�񗈓X�����N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 * @see ���N�G�X�g�F1004�d��<br>
 *      ���X�|���X�F1005�d��
 */
public class RsvFix extends DtoRsvListData implements Serializable
{

    private static final long serialVersionUID = 8021989400153209488L;
    final int                 MAX_ROOM_LENGTH  = 60;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1004";
    final String              REPLY_COMMAND    = "1005";

    public RsvFix()
    {
        new DtoRsvListData();
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
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getSeq() ), 9 );
                sendData += tcpclient.leftFitFormat( getRoomName(), 8 );
                sendData += tcpclient.leftFitFormat( getRsvNo(), 32 );
                sendData += tcpclient.leftFitFormat( getDispRsvNo(), 6 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getRoomNameListLength() ), 2 );
                // �v�f�������J��Ԃ�
                for( int i = 0 ; i < getRoomNameListLength() ; i++ )
                {
                    sendData += tcpclient.leftFitFormat( getRoomNameList()[i], 8 );
                }
                if ( getRoomNameListLength() < MAX_ROOM_LENGTH )
                {
                    sendData += tcpclient.leftFitFormat( "", (MAX_ROOM_LENGTH - getRoomNameListLength()) * 8 );
                }
                sendData += tcpclient.leftFitFormat( getTouchRoomName(), 8 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getPayment() ), 9 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getUsedMile() ), 9 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getPossibleTime() ), 4 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getCoTime() ), 4 );
                sendData += tcpclient.leftFitFormat( getNameKana(), 40 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getNumMan() ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getNumWoman() ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getNumChild() ), 2 );
                sendData += tcpclient.leftFitFormat( getOption1(), 80 );
                sendData += tcpclient.leftFitFormat( getOption0(), 80 );
                sendData += tcpclient.leftFitFormat( getCountry(), 32 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getPaymentAll() ), 9 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( getReserveDateTo() ), 8 );
                sendData += tcpclient.leftFitFormat( getReserve(), 391 );

                setHeader( tcpclient.getPacketHapihoteHeader( hotelId, sendData.getBytes( "Windows-31J" ).length ) );
                sendData = getHeader() + sendData;
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

                        // �����d���R�}���h��1005�Ȃ琳��������
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            // �Ԃ��Ă��������Z�b�g
                            setResult( Integer.valueOf( new String( charData, 36, 2 ) ).intValue() );
                            setErrorCode( Integer.valueOf( new String( charData, 38, 4 ) ).intValue() );
                            setGuidRoomName( new String( charData, 42, 8 ) );
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
                Logging.error( "[RsvFix.sendToHost()]Exception:" + e.toString() );
                // �`�F�b�N�C���G���[�̂Ƃ��́A�^�C���A�E�g
                setErrorCode( 30304 );
                ret = false;
            }
            catch ( Exception e )
            {
                Logging.error( "[RsvFix.sendToHost()]Exception:" + e.toString() );
                // �`�F�b�N�C���G���[�̂Ƃ��́A�^�C���A�E�g
                setErrorCode( 30305 );
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
