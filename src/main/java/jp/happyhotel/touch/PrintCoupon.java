package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * ハピホテタッチチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 * @see リクエスト：1006電文<br>
 *      レスポンス：1007電文
 */
public class PrintCoupon implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4403385171619391147L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1006";
    final String              REPLY_COMMAND    = "1007";
    String                    header;

    // 送信電文
    int                       seq;
    String                    roomName;
    String                    couponNo;
    int                       printDate;
    int                       printTime;
    String                    memo;
    String                    reserve;

    // 受信電文
    int                       result;
    int                       errorCode;

    public PrintCoupon()
    {
        seq = 0;
        roomName = "0";
        couponNo = "";
        printDate = 0;
        printTime = 0;
        memo = "";
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

    public String getRoomName()
    {
        return roomName;
    }

    public String getCouponNo()
    {
        return couponNo;
    }

    public int getPrintDate()
    {
        return printDate;
    }

    public int getPrintTime()
    {
        return printTime;
    }

    public String getMemo()
    {
        return memo;
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

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setCouponNo(String couponNo)
    {
        this.couponNo = couponNo;
    }

    public void setPrintDate(int printDate)
    {
        this.printDate = printDate;
    }

    public void setPrintTime(int printTime)
    {
        this.printTime = printTime;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
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
                sendData += tcpclient.leftFitFormat( this.couponNo, 12 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.printDate ), 8 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.printTime ), 6 );
                sendData += tcpclient.leftFitFormat( this.memo, 200 );
                sendData += tcpclient.rightFitZeroFormat( this.reserve, 200 );

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
            catch ( Exception e )
            {
                Logging.error( "[PrintCoupon.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }

        return ret;

    }
}
