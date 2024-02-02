package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * 前受情報取得通知クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see リクエスト：0216電文<br>
 *      レスポンス：0217電文
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

    // 送信電文
    int                       seq;
    String                    roomName;
    String                    reserve;

    // 受信電文
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
                sendData += tcpclient.rightFitFormat( this.reserve, 10 );

                header = tcpclient.getPacketHeader( hotelId, sendData.getBytes( "Windows-31J" ).length );
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

                        // 応答電文コマンドが0217なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            // 返ってきた情報をセット
                            this.result = Integer.valueOf( new String( charData, 36, 2 ) ).intValue();
                            this.deposit = Integer.valueOf( new String( charData, 38, 6 ) ).intValue();
                            this.charge = Integer.valueOf( new String( charData, 44, 6 ) ).intValue();
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
     * 前受情報取得
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
        final int NONREFUNDABLE = 1; // 返金不可
        final int CREDIT_NONREFUNDABLE = 2; // クレジット精算の返金不可

        /** （0216）ホテナビ_前受情報取得電文 **/
        this.setSeq( seq );
        this.setRoomName( roomName );
        this.sendToHost( frontIp, TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( id ) );
        if ( this.getResult() == DEPOSIT_NO || this.getResult() == DEPOSIT_CASH || this.getResult() == DEPOSIT_CREDIT )
        {
            // 返金不可設定かつ前受がある場合、またはクレジット返金不可設定かつクレジット前受の場合
            if ( (nonrefundableFlag == NONREFUNDABLE && this.getResult() > DEPOSIT_NO) ||
                    (nonrefundableFlag == CREDIT_NONREFUNDABLE && this.getResult() == DEPOSIT_CREDIT) )
            {
                boolUseableMile = false;
                unavailableMessage = "マイルを使用する際にはフロントまでご連絡下さい。";
            }
        }
    }
}
