package jp.happyhotel.touch;

import java.io.Serializable;
import java.net.SocketTimeoutException;

import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * ハピホテタッチチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 * @see リクエスト：1000電文<br>
 *      レスポンス：1001電文
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

    // 送信電文
    int                       seq;
    String                    roomName;
    String                    reserve;
    int                       nowPoint;

    // 受信電文
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
        // ホテルのフロントIPを取得
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
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.nowPoint ), 9 );
                sendData += tcpclient.rightFitFormat( this.reserve, 91 );

                header = tcpclient.getPacketHapihoteHeader( Integer.toString( paramId ), sendData.getBytes( "Windows-31J" ).length );
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

                        // 応答電文コマンドが1000なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            // 返ってきた情報をセット
                            this.result = Integer.valueOf( new String( charData, 36, 2 ) ).intValue();
                            this.errorCode = Integer.valueOf( new String( charData, 38, 4 ) ).intValue();

                            // 正常を返して成功とする
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
                // チェックインエラーのときは、タイムアウト
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
