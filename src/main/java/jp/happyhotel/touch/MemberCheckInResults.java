/*
 * @(#)HotelCheclIn.java 1.00 2011/05/19 Copyright (C) ALMEX Inc. 2011 ホテルチェックイン情報取得クラス
 */
package jp.happyhotel.touch;

import java.io.Serializable;
import java.util.ArrayList;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * メンバーチェックイン実績通知クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see リクエスト：1048電文<br>
 *      レスポンス：1049電文
 */
public class MemberCheckInResults implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 7921459974422804536L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1048";
    final String              REPLY_COMMAND    = "1049";
    final String              DEFINE_USER_ID   = "ceritfiedid";       // ホテナビ電文の固定ユーザID
    final String              DEFINE_PASSWORD  = "6268";              // ホテナビ電文の固定パスワード
    String                    header;
    // 送信電文
    String                    termId           = "";
    String                    password         = "";
    int                       startDate;
    int                       endDate;

    // 受信電文
    int                       result;
    int                       dataLength;
    ArrayList<Integer>        collectDate;                            // 集計日
    ArrayList<Integer>        allCheckIn;                             // 全チェックイン数
    ArrayList<Integer>        memberCheckIn;                          // ハピホテメンバーチェックイン数
    ArrayList<Integer>        allMember;                              // 全メンバー数

    public MemberCheckInResults()
    {
        this.header = "";
        this.termId = "";
        this.password = "";
        this.result = 0;
    }

    public String getHeader()
    {
        return header;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getResult()
    {
        return result;
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public ArrayList<Integer> getCollectDate()
    {
        return collectDate;
    }

    public ArrayList<Integer> getAllCheckIn()
    {
        return allCheckIn;
    }

    public ArrayList<Integer> getMemberCheckIn()
    {
        return memberCheckIn;
    }

    public ArrayList<Integer> getAllMember()
    {
        return allMember;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setCollectDate(ArrayList<Integer> collectDate)
    {
        this.collectDate = collectDate;
    }

    public void setAllCheckIn(ArrayList<Integer> allCheckIn)
    {
        this.allCheckIn = allCheckIn;
    }

    public void setMemberCheckIn(ArrayList<Integer> memberCheckIn)
    {
        this.memberCheckIn = memberCheckIn;
    }

    public void setAllMember(ArrayList<Integer> allMember)
    {
        this.allMember = allMember;
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
        int dataLength = 0;

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
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.startDate ), 8 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.endDate ), 8 );

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
                        dataLength = data.length();

                        // 応答電文コマンドが1049なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            this.termId = clip.clipWord( charData, nIndex, 11 );
                            nIndex = clip.getNextIndex();

                            this.password = clip.clipWord( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            while( dataLength > nIndex )
                            {
                                // 項目毎に抜出
                                this.collectDate.add( clip.clipNum( charData, nIndex, 8 ) );
                                nIndex = clip.getNextIndex();

                                this.allCheckIn.add( clip.clipNum( charData, nIndex, 5 ) );
                                nIndex = clip.getNextIndex();

                                this.memberCheckIn.add( clip.clipNum( charData, nIndex, 5 ) );
                                nIndex = clip.getNextIndex();

                                this.allMember.add( clip.clipNum( charData, nIndex, 5 ) );
                                nIndex = clip.getNextIndex();
                            }

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
                Logging.error( "[MemberCheckInResults.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }
        return ret;

    }
}
