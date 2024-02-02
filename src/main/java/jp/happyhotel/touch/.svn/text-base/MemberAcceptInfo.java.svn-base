package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * メンバー受付情報取得通知クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see リクエスト：1042電文<br>
 *      レスポンス：1043電文
 */
public class MemberAcceptInfo implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 5385130148620657030L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1042";
    final String              REPLY_COMMAND    = "1043";
    String                    header;
    boolean                   boolMemberAccept;
    String                    customId;

    // 送信電文
    String                    roomName;
    String                    reserve;

    // 受信電文
    int                       result;
    String                    memberId;
    String                    securityCode;
    String                    birthMonth;
    String                    birthDay;

    public MemberAcceptInfo()
    {
        this.header = "";
        this.roomName = "";
        this.reserve = "";
        this.result = 0;
        this.memberId = "";
        this.securityCode = "";
        this.birthMonth = "";
        this.birthDay = "";
        this.boolMemberAccept = false;
        this.customId = "";
    }

    public String getHeader()
    {
        return header;
    }

    public boolean isBoolMemberAccept()
    {
        return boolMemberAccept;
    }

    public String getCustomId()
    {
        return customId;
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

    public String getMemberId()
    {
        return memberId;
    }

    public String getSecurityCode()
    {
        return securityCode;
    }

    public String getBirthMonth()
    {
        return birthMonth;
    }

    public String getBirthDay()
    {
        return birthDay;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setBoolMemberAccept(boolean boolMemberAccept)
    {
        this.boolMemberAccept = boolMemberAccept;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
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

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }

    public void setBirthMonth(String birthMonth)
    {
        this.birthMonth = birthMonth;
    }

    public void setBirthDay(String birthDay)
    {
        this.birthDay = birthDay;
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
            ret = false;
            try
            {
                sendData = COMMAND;
                sendData += tcpclient.leftFitFormat( this.roomName, 8 );
                sendData += tcpclient.leftFitFormat( this.reserve, 120 );

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

                        // 応答電文コマンドが1043なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {

                            // 返ってきた情報をセット

                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();
                            // 部屋番号
                            this.roomName = clip.clipWord( charData, nIndex, 8 );
                            nIndex = clip.getNextIndex();
                            // メンバーID
                            this.memberId = clip.clipWord( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();
                            // セキュリティコード
                            this.securityCode = clip.clipWord( charData, nIndex, 5 );
                            nIndex = clip.getNextIndex();
                            // 誕生月
                            this.birthMonth = clip.clipWord( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();
                            // 誕生日
                            this.birthDay = clip.clipWord( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();
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
                Logging.error( "[this.sendToHost()]Exception:" + e.toString() );
            }

            tcpclient.disconnectService();
        }
        return ret;
    }

    public void getMemberAcceptInfo(int id, String roomName, String frontIp)
    {
        final int TIMEOUT = 10000;
        final int HOTENAVI_PORT_NO = 7023;
        final int RESULT_OK = 1;
        final int RESULT_NG = 2;

        // メンバーなし→部屋に対して磁気カード挿入チェック
        this.setRoomName( roomName );
        this.sendToHost( frontIp, TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( id ) );
        // カード受付済み
        if ( this.getResult() == RESULT_OK )
        {
            boolMemberAccept = true;
            // メンバーIDを取得
            // カード受付済み→「新規会員登録受付」と、「このカードを使用する」
            customId = this.getMemberId();
        }
        else
        {
            // カード未受付→「新規会員登録受付」と「既にカードを持っている」
            boolMemberAccept = false;
        }
    }

}
