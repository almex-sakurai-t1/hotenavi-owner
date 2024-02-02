package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * ハピホテクーポン割引クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 * @see リクエスト：1008電文<br>
 *      レスポンス：1009電文
 */
public class DiscountCoupon implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4403385171619391147L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1008";
    final String              REPLY_COMMAND    = "1009";
    String                    header;

    // 送信電文
    int                       seq;
    String                    roomName;
    String                    couponNo;
    int                       discountKind;
    int                       combinedUse;
    int                       discountPart;
    int                       discountDataLength;
    int[]                     planCode;
    int[]                     modeCode;
    int[]                     value;
    String                    reserve;

    // 受信電文
    int                       result;
    int                       errorCode;

    public DiscountCoupon()
    {
        seq = 0;
        roomName = "0";
        couponNo = "";
        discountPart = 0;
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

    public int getDiscountKind()
    {
        return discountKind;
    }

    public int getCombinedUse()
    {
        return combinedUse;
    }

    public int getDiscountPart()
    {
        return discountPart;
    }

    public int getDiscountDataLength()
    {
        return discountDataLength;
    }

    public int[] getPlanCode()
    {
        return planCode;
    }

    public int[] getModeCode()
    {
        return modeCode;
    }

    public int[] getValue()
    {
        return value;
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

    public void setDiscountKind(int discountKind)
    {
        this.discountKind = discountKind;
    }

    public void setCombinedUse(int combinedUse)
    {
        this.combinedUse = combinedUse;
    }

    public void setDiscountPart(int discountPart)
    {
        this.discountPart = discountPart;
    }

    public void setDiscountDataLength(int discountDataLength)
    {
        this.discountDataLength = discountDataLength;
    }

    public void setPlanCode(int[] planCode)
    {
        this.planCode = planCode;
    }

    public void setModeCode(int[] modeCode)
    {
        this.modeCode = modeCode;
    }

    public void setValue(int[] value)
    {
        this.value = value;
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

    /**
     * 
     * @param frontIp
     * @param timeOut
     * @param portNo
     * @param hotelId
     * @return
     */
    public boolean sendToHost(String frontIp, int timeOut, int portNo, String hotelId)
    {
        String sendData = "";
        TcpClientEx tcpclient = null;
        String recvData = "";
        char[] charData = null;
        String data = "";
        int retryCount = 0;
        boolean ret = false;
        String numberString = "0";

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
                // DBでは0:%割引き、1:金額割引、電文定義では1:%割引き、2:金額割引のため+1する
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.discountKind ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.combinedUse ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.discountPart ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.discountDataLength ), 3 );
                for( int i = 0 ; i < discountDataLength ; i++ )
                {
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.planCode[i] ), 2 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.modeCode[i] ), 2 );
                    if ( this.value[i] < 0 )
                    {
                        numberString = String.format( "%1$04d", Math.abs( this.value[i] ) );
                        numberString = "-" + numberString;
                    }
                    else
                    {
                        numberString = Integer.toString( this.value[i] );
                    }

                    sendData += tcpclient.rightFitZeroFormat( numberString, 5 );

                }

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
                Logging.error( "[DiscountCoupon.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }

        return ret;

    }
}
