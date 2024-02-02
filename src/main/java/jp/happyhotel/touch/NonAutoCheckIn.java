package jp.happyhotel.touch;

import java.io.Serializable;
import java.net.SocketTimeoutException;

import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.TcpClientEx;
import jp.happyhotel.others.HapiTouchErrorMessage;

/**
 * �n�s�z�e�^�b�`���j���[�Ճ`�F�b�N�C�������N���X
 * 
 * @author T.Sakurai
 * @version 1.00 2016/11/07
 * @see ���N�G�X�g�F1014�d��<br>
 *      ���X�|���X�F1015�d��
 */
public class NonAutoCheckIn implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -8671735443616648606L;
    final int                 MAX_ROOM_LENGTH  = 60;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1014";
    final String              REPLY_COMMAND    = "1015";
    String                    header;

    // ���M�d��
    String                    userId;
    String                    customId;
    String                    securityCode;
    String                    roomName;
    String                    rsvNo;
    String                    dispRsvNo;
    int                       roomNameListLength;
    String[]                  roomNameList;
    String                    ipAddress;
    int                       errorNo;

    // ��M�d��
    int                       result;
    int                       errorCode;

    public NonAutoCheckIn()
    {
        userId = "";
        customId = "";
        securityCode = "";
        roomName = "";
        rsvNo = "";
        dispRsvNo = "";
        roomNameListLength = 0;
        roomNameList = null;
        ipAddress = "";
        errorNo = 0;

        result = 0;
        errorCode = 0;
    }

    public String getHeader()
    {
        return header;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getCustomId()
    {
        return customId;
    }

    public String getSecurityCode()
    {
        return securityCode;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRsvNo()
    {
        return rsvNo;
    }

    public String getDispRsvNo()
    {
        return dispRsvNo;
    }

    public int getRoomNameListLength()
    {
        return roomNameListLength;
    }

    public String[] getRoomNameList()
    {
        return roomNameList;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public int getErrorNo()
    {
        return errorNo;
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

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = rsvNo;
    }

    public void setDispRsvNo(String dispRsvNo)
    {
        this.dispRsvNo = dispRsvNo;
    }

    public void setRoomNameListLength(int roomNameListLength)
    {
        this.roomNameListLength = roomNameListLength;
    }

    public void setRoomNameList(String[] roomNameList)
    {
        this.roomNameList = roomNameList;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public void setErrorNo(int errorNo)
    {
        this.errorNo = errorNo;
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
                sendData += tcpclient.leftFitFormat( this.userId, 32 );
                sendData += tcpclient.leftFitFormat( this.customId, 9 );
                sendData += tcpclient.leftFitFormat( this.securityCode, 5 );
                sendData += tcpclient.leftFitFormat( this.roomName, 8 );
                sendData += tcpclient.leftFitFormat( this.rsvNo, 32 );
                sendData += tcpclient.leftFitFormat( this.dispRsvNo, 6 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.roomNameListLength ), 2 );
                // �v�f�������J��Ԃ�
                for( int i = 0 ; i < roomNameListLength ; i++ )
                {
                    sendData += tcpclient.leftFitFormat( this.roomNameList[i], 8 );
                }
                if ( roomNameListLength < MAX_ROOM_LENGTH )
                {
                    sendData += tcpclient.leftFitFormat( "", (MAX_ROOM_LENGTH - roomNameListLength) * 8 );
                }
                sendData += tcpclient.leftFitFormat( ReplaceString.replaceHexIp( this.ipAddress ), 8 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.errorNo ), 5 );

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

                        // �����d���R�}���h��1015�Ȃ琳��������
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
                Logging.error( "[NonAutoCheckIn.sendToHost()]Exception:" + e.toString() );
                // �`�F�b�N�C���G���[�̂Ƃ��́A�^�C���A�E�g
                this.errorCode = HapiTouchErrorMessage.ERR_30701;
                ret = false;
            }
            catch ( Exception e )
            {
                Logging.error( "[NonAutoCheckIn.sendToHost()]Exception:" + e.toString() );
                // �`�F�b�N�C���G���[�̂Ƃ��́A�^�C���A�E�g
                this.errorCode = HapiTouchErrorMessage.ERR_30702;
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
