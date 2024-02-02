/*
 * @(#)MemberCardChargeConfirm.java 1.00 2015/11/19 Copyright (C) ALMEX Inc. 2015 メンバーズカード課金確認クラス
 */
package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * メンバーズカード課金確認要求クラス
 * 
 * @author T.Sakurai
 * @version 1.00 2015/11/19
 * @see リクエスト：1052電文<br>
 *      レスポンス：1053電文
 */
public class MemberCardChargeConfirm implements Serializable
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
    final String              COMMAND          = "1052";
    final String              REPLY_COMMAND    = "1053";
    final String              DEFINE_USER_ID   = "ceritfiedid";        // ホテナビ電文の固定ユーザID
    final String              DEFINE_PASSWORD  = "6268";               // ホテナビ電文の固定パスワード
    String                    header;
    // 送信電文
    String                    termId           = "";
    String                    password         = "";
    String                    roomName         = "";
    String                    reserve          = "";

    // 受信電文
    int                       goodsCode;                               // 商品コード
    int                       goodsPrice;                              // 商品金額

    public MemberCardChargeConfirm()
    {
        this.header = "";
        this.termId = "";
        this.password = "";
        this.goodsCode = 0;
        this.goodsPrice = 0;
        this.reserve = "";
    }

    public String getHeader()
    {
        return header;
    }

    public int getGoodsCode()
    {
        return goodsCode;
    }

    public int getGoodsPrice()
    {
        return goodsPrice;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setGoodsCode(int goodsCode)
    {
        this.goodsCode = goodsCode;
    }

    public void setGoodsPrice(int goodsPrice)
    {
        this.goodsPrice = goodsPrice;
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
                sendData += DEFINE_USER_ID;
                sendData += DEFINE_PASSWORD;
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

                        // 応答電文コマンドが1053なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            this.termId = clip.clipWord( charData, nIndex, 11 );
                            nIndex = clip.getNextIndex();

                            this.password = clip.clipWord( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            this.goodsCode = clip.clipNum( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            this.goodsPrice = clip.clipNum( charData, nIndex, 9 );
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
                Logging.error( "[MemberCardChargeConfirm.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }
        return ret;

    }
}
