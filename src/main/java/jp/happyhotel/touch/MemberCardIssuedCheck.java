/*
 * @(#)MemberCardIssuedCheck.java 1.00 2015/11/18 Copyright (C) ALMEX Inc. 2011 メンバーカード発行確認クラス
 */
package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * メンバーカード発行確認通知クラス
 * 
 * @author T.Sakurai
 * @version 1.00 2015/11/18
 * @see リクエスト：1056電文<br>
 *      レスポンス：1057電文
 */
public class MemberCardIssuedCheck implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -2147877940949724578L;
    /**
     *
     */
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1056";
    final String              REPLY_COMMAND    = "1057";
    final String              DEFINE_USER_ID   = "ceritfiedid";        // ホテナビ電文の固定ユーザID
    final String              DEFINE_PASSWORD  = "6268";               // ホテナビ電文の固定パスワード
    String                    header;
    // 送信電文
    String                    termId           = "";
    String                    password         = "";
    String                    roomName         = "";
    String                    reserve          = "";                   // 予備

    // 受信電文
    int                       result;                                  // 処理結果 0:未発行,1:発行済み

    public MemberCardIssuedCheck()
    {
        this.header = "";
        this.termId = "";
        this.password = "";
        this.roomName = "";
        this.result = 0;
        this.reserve = "";
    }

    public String getHeader()
    {
        return header;
    }

    public int getResult()
    {
        return result;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setResult(int result)
    {
        this.result = result;
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

        Logging.info( "tcpclient.connectServiceByAddr() ret:" + ret + ",roomName=" + this.roomName + ",reserve=" + this.reserve );

        if ( ret != false )
        {

            try
            {
                sendData = COMMAND;
                sendData += DEFINE_USER_ID;
                sendData += DEFINE_PASSWORD;
                sendData += tcpclient.leftFitFormat( this.roomName, 8 );
                sendData += tcpclient.leftFitFormat( this.reserve, 10 );

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

                        // 応答電文コマンドが1057なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            this.termId = clip.clipWord( charData, nIndex, 11 );
                            nIndex = clip.getNextIndex();

                            this.password = clip.clipWord( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            this.result = clip.clipNum( charData, nIndex, 2 );
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
                Logging.error( "[MemberCardIssuedCheck.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }
        Logging.info( "[MemberCardIssuedCheck.sendToHost()]ret:" + ret );
        return ret;
    }
}
