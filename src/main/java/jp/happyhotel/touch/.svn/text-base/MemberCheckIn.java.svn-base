package jp.happyhotel.touch;

import java.io.Serializable;
import java.net.SocketTimeoutException;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;
import jp.happyhotel.dto.DtoApHotelCustomerData;

/**
 * ハピホテタッチメンバーチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 * @see リクエスト：1010電文<br>
 *      レスポンス：1011電文
 */
public class MemberCheckIn implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4403385171619391147L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1010";
    final String              REPLY_COMMAND    = "1011";
    String                    header;
    boolean                   boolMemberCheckIn;
    DtoApHotelCustomerData    apHotelCustomerData;
    String                    customRank;

    // 送信電文
    int                       seq;
    String                    roomName;
    String                    customId;
    String                    securityCode;
    String                    reserve;

    // 受信電文
    int                       result;
    int                       errorCode;
    String                    returnCustomId;
    String                    returnSecurityCode;
    String                    rank;
    int                       point;
    int                       point2;

    public MemberCheckIn()
    {
        this.header = "";
        this.seq = 0;
        this.roomName = "";
        this.customId = "";
        this.securityCode = "";
        this.reserve = "";
        this.result = 0;
        this.errorCode = 0;
        this.returnCustomId = "";
        this.returnSecurityCode = "";
        this.rank = "";
        this.point = 0;
        this.point2 = 0;
        this.boolMemberCheckIn = false;
        this.apHotelCustomerData = null;
        this.customRank = "";
    }

    public String getHeader()
    {
        return header;
    }

    public boolean isBoolMemberCheckIn()
    {
        return boolMemberCheckIn;
    }

    public DtoApHotelCustomerData getApHotelCustomerData()
    {
        return apHotelCustomerData;
    }

    public String getCustomRank()
    {
        return customRank;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getCustomId()
    {
        return customId;
    }

    public String getSecurityCode()
    {
        return securityCode;
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

    public String getReturnCustomId()
    {
        return returnCustomId;
    }

    public String getReturnSecurityCode()
    {
        return returnSecurityCode;
    }

    public String getRank()
    {
        return rank;
    }

    public int getPoint()
    {
        return point;
    }

    public int getPoint2()
    {
        return point2;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setBoolMemberCheckIn(boolean boolMemberCheckIn)
    {
        this.boolMemberCheckIn = boolMemberCheckIn;
    }

    public void setApHotelCustomerData(DtoApHotelCustomerData apHotelCustomerData)
    {
        this.apHotelCustomerData = apHotelCustomerData;
    }

    public void setCustomRank(String customRank)
    {
        this.customRank = customRank;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
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

    public void setReturnCustomId(String returnCustomId)
    {
        this.returnCustomId = returnCustomId;
    }

    public void setReturnSecurityCode(String returnSecurityCode)
    {
        this.returnSecurityCode = returnSecurityCode;
    }

    public void setRank(String rank)
    {
        this.rank = rank;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setPoint2(int point2)
    {
        this.point2 = point2;
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

        // 抜出データの開始位置
        nIndex = HEADER_LENGTH + COMMAND_LENGTH;

        // ホスト側データ送信
        tcpclient = new TcpClientEx();
        // 指定のipアドレスに接続
        ret = tcpclient.connectServiceByAddr( frontIp, timeOut, portNo );
        if ( ret != false )
        {
            try
            {
                sendData = COMMAND;
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.seq ), 9 );
                sendData += tcpclient.leftFitFormat( this.roomName, 8 );
                sendData += tcpclient.leftFitFormat( this.customId, 9 );
                sendData += tcpclient.leftFitFormat( this.securityCode, 5 );
                sendData += tcpclient.leftFitFormat( this.reserve, 92 );

                header = tcpclient.getPacketHapihoteHeader( hotelId, sendData.getBytes( "Windows-31J" ).length );
                sendData = header + sendData;
                int roop = 0;

                while( true )
                {
                    // 電文送信
                    tcpclient.send( sendData );

                    // 受信待機
                    recvData = tcpclient.recv();

                    roop++;
                    if ( recvData.indexOf( "exception" ) >= 0 )
                    {
                        Logging.error( "電文受信Exception " + recvData );
                    }
                    else
                    {
                        charData = new char[recvData.length()];
                        charData = recvData.toCharArray();

                        // コマンド取得
                        data = new String( charData, HEADER_LENGTH, COMMAND_LENGTH );

                        // 応答電文コマンドが1011なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            // 返ってきた情報をセット
                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            this.errorCode = clip.clipNum( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            this.rank = clip.clipWord( charData, nIndex, 40 );
                            nIndex = clip.getNextIndex();

                            this.point = clip.clipNum( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            this.point2 = clip.clipNum( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();
                            // 正常を返して成功とする
                            ret = true;

                            Logging.info( "result:" + this.result + ",errorCode:" + this.errorCode + ",securityCode:" + this.securityCode, "MemberCheckIn.sendToHost()" );
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
                Logging.error( "[[MemberCheckIn.sendToHost()]Exception:" + e.toString() );
                this.errorCode = 30410;
                ret = false;
            }
            catch ( Exception e )
            {
                Logging.error( "[MemberCheckIn.sendToHost()]Exception:" + e.toString() );
                this.errorCode = 30411;
                ret = false;
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }
        return ret;
    }

    public void getMemberCheckIn(int id, int seq, String roomName, String frontIp, String customId, String securityCode, int birthDay)
    {
        final int TIMEOUT = 10000;
        final int HOTENAVI_PORT_NO = 7023;
        final int RESULT_OK = 1;
        final int RESULT_NG = 2;
        final int HAPIHOTE_PORT_NO = 7046;
        MemberInfo memberInfo = new MemberInfo();

        /** （1010）ハピホテ_メンバーチェックイン電文 **/
        this.setSeq( seq );
        this.setRoomName( roomName );
        this.setCustomId( customId );
        this.setSecurityCode( securityCode );
        this.sendToHost( frontIp, TIMEOUT, HAPIHOTE_PORT_NO, Integer.toString( id ) );
        if ( this.getResult() == RESULT_OK )
        {
            boolMemberCheckIn = true;

            memberInfo.getMemberInfo( id, customId, birthDay, frontIp );
            apHotelCustomerData = memberInfo.getMemberInfo();

            customRank = this.getRank();
            point = this.getPoint();
            point2 = this.getPoint2();
        }
        else
        {
            Logging.info( "errorCode:" + this.errorCode + ",seq:" + seq + ",customId:" + customId + ",securityCode:" + securityCode, "MemberCheckIn.getMemberCheckIn()" );
            errorCode = this.getErrorCode();
        }
    }

}
