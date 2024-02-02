/*
 * @(#)TcpClient.java 2.00 2004/03/17
 * Copyright (C) ALMEX Inc. 2004
 * AMFWEBサービスとのソケット通信Bean
 */

package jp.happyhotel.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * AMFWEBサービスとの電文送受信を行う。<BR>
 * 接続先をhostsより参照。(host_hotelid) ポート番号:6981にて接続を行う。<BR>
 * 受信電文の文字コードはShift_JISのまま戻されるので、カナ・漢字等は文字コード変換する必要がある。<BR>
 * new String( strData.getBytes("8859_1"), "Shift_JIS")
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/17
 */
public class TcpClientEx
{
    /** 接続先ポート番号定義 **/
    private static final int   SERVICE_PORTNO  = 7023;
    /** 受信タイムアウト設定値定義 **/
    private static final int   SERVICE_TIMEOUT = 15;

    private Socket             clientSocket;
    private InputStreamReader  recvBuff;
    private BufferedReader     recvBuffRead;
    private OutputStreamWriter sendBuff;

    private static int         seqNo;

    /**
     * TcpClientを初期化します。
     */
    public TcpClientEx()
    {
        seqNo = 0;
    }

    /**
     * AMFWEBサービス回線接続処理<BR>
     * /etc/hostsファイルを参照し、サーバに接続します。<BR>
     * ホスト名はhost_ホテルIDで設定します。
     * 
     * @param serverName 接続サーバ名(ホテルID)
     * @return 接続結果(TRUE:接続完了, FALSE:未接続)
     */
    public boolean connectService(String serverName)
    {

        try
        {
            // サーバ名を編集する
            serverName = "host_" + serverName;

            // AMFWEBサービスに接続する
            clientSocket = new Socket( serverName, SERVICE_PORTNO );

            // 受信用バッファの取得
            recvBuff = new InputStreamReader( clientSocket.getInputStream(), "8859_1" );
            recvBuffRead = new BufferedReader( recvBuff );
            // 送信用バッファの取得
            sendBuff = new OutputStreamWriter( clientSocket.getOutputStream(), "Windows-31J" );

            // 受信タイムアウトの設定(15s)
            clientSocket.setSoTimeout( SERVICE_TIMEOUT );

        }
        catch ( IOException e )
        {
            return(false);
        }
        finally
        {
        }

        return(true);
    }

    /**
     * AMFWEBサービス回線接続処理(IPアドレスでの接続)<BR>
     * IPアドレスを文字列で渡します。("192.168.210.1"など)
     * 
     * @param ipAddr 接続サーバIPアドレス文字列
     * @param timeout タイムアウト時間(ミリ秒)
     * @param portno ﾎﾟｰﾄ番号
     * @return 接続結果(TRUE:接続完了, FALSE:未接続)
     */
    public boolean connectServiceByAddr(String ipAddr, int timeout, int portno)
    {
        try
        {
            // AMFWEBサービスに接続する
            clientSocket = new Socket( ipAddr, portno );
            Logging.info( ipAddr + ", " + portno );

            // 受信用バッファの取得
            recvBuff = new InputStreamReader( clientSocket.getInputStream(), "8859_1" );

            Logging.info( "recvBuff:" + recvBuff.toString() );

            recvBuffRead = new BufferedReader( recvBuff );
            // 送信用バッファの取得
            sendBuff = new OutputStreamWriter( clientSocket.getOutputStream(), "Windows-31J" );
            Logging.info( "sendBuff:" + sendBuff.toString() );

            // 受信タイムアウトの設定(15s)
            if ( timeout > 0 )
            {
                clientSocket.setSoTimeout( timeout );
            }
            else
            {
                clientSocket.setSoTimeout( SERVICE_TIMEOUT );
            }
        }
        catch ( IOException e )
        {
            Logging.error( "[TcpClientEx.connectServiceByAddr()] Exception:" + e.toString() + ",ip:" + ipAddr + ", port:" + portno + ", timeout:" + timeout );
            return(false);
        }
        finally
        {
        }

        return(true);
    }

    /**
     * AMFWEBサービス回線切断処理
     * 
     */
    public void disconnectService()
    {
        try
        {
            clientSocket.close();
        }
        catch ( IOException e )
        {
        }
        finally
        {
        }
    }

    /**
     * 電文送信処理
     * 
     * @param sendData 送信電文
     */
    public void send(String sendData) throws IOException
    {
        char cBuf = 0x03;
        // 最後にETXを付加する
        sendData = sendData + cBuf;
        sendBuff.write( sendData );
        Logging.info( sendData );
        sendBuff.flush();
        Logging.info( "send_fix:" );
    }

    /**
     * 電文受信処理
     * 
     * @return 受信電文(Shift_JISコード)
     */
    public String recv() throws IOException
    {
        int nBuff;
        StringBuffer strRecv;

        strRecv = new StringBuffer();
        Logging.info( "tcp_recv->" );
        try
        {
            // ETXを受信するまで受信する
            while( true )
            {
                nBuff = recvBuffRead.read();
                if ( nBuff == 0x03 || nBuff == -1 )
                {
                    break;
                }

                strRecv.append( (char)nBuff );
            }
            Logging.info( "strRecv:" + strRecv );

        }
        catch ( SocketException e )
        {
            Logging.error( "[TcpClientEx.recv()] Exception:" + e.toString() );
            strRecv.append( e.toString() );
        }
        finally
        {
        }

        // Logging.info( "tcp_recv->" + Debug.dump( strRecv.toString() ) );

        return(strRecv.toString());
    }

    /**
     * 電文ヘッダ作成処理
     * 
     * @param strHotelId ホテルＩＤ
     * @param nLength データ部レングス
     * @return 電文ヘッダ
     */
    public String getPacketHeader(String strHotelId, int nLength)
    {
        String strHeader;
        String strData;
        NumberFormat nf;

        // 通信ＡＰ名
        strHeader = "HOTENAVI";

        // シーケンス番号
        nf = new DecimalFormat( "000000" );
        strData = nf.format( seqNo );
        strHeader = strHeader + strData;

        // 電文長(+1はETX)
        nf = new DecimalFormat( "000000" );
        strData = nf.format( nLength + 32 + 1 );
        strHeader = strHeader + strData;

        // ホテルコード
        strData = leftFitFormat( strHotelId, 10 );
        strHeader = strHeader + strData;

        // 予備
        strData = "  ";
        strHeader = strHeader + strData;

        seqNo = seqNo + 1;

        return(strHeader);
    }

    /**
     * 電文ヘッダ作成処理
     * 
     * @param strHotelId ホテルＩＤ
     * @param nLength データ部レングス
     * @return 電文ヘッダ
     */
    public String getPacketHapihoteHeader(String strHotelId, int nLength)
    {
        String strHeader;
        String strData;
        NumberFormat nf;

        // 通信ＡＰ名
        strHeader = "HAPIHOTE";

        // シーケンス番号
        nf = new DecimalFormat( "000000" );
        strData = nf.format( seqNo );
        strHeader = strHeader + strData;

        // 電文長(+1はETX)
        nf = new DecimalFormat( "000000" );
        strData = nf.format( nLength + 32 + 1 );
        strHeader = strHeader + strData;

        // ホテルコード
        strData = leftFitFormat( strHotelId, 10 );
        strHeader = strHeader + strData;

        // 予備
        strData = "  ";
        strHeader = strHeader + strData;

        seqNo = seqNo + 1;

        return(strHeader);
    }

    /**
     * 左詰めスペース埋めフォーマット
     * 編集後桁数が元の桁数より小さい場合はカットする
     * 
     * @param strOrg 編集前文字列
     * @param nLength 編集後文字列長
     * @return String 編集後文字列
     */
    public String leftFitFormat(String strOrg, int nLength)
    {
        int i;
        byte bytebuff[];
        byte cutbuff[];

        cutbuff = new byte[nLength];

        try
        {
            bytebuff = strOrg.getBytes( "Windows-31J" );
            for( i = 0 ; i < nLength ; i++ )
            {
                if ( i < bytebuff.length )
                {
                    cutbuff[i] = bytebuff[i];
                }
                else
                {
                    cutbuff[i] = ' ';
                }
            }

            return(new String( cutbuff, "Windows-31J" ));
        }
        catch ( Exception e )
        {
            return("");
        }
    }

    /**
     * 左詰め全角スペース埋めフォーマット
     * 
     * @param strOrg 編集前文字列
     * @param nLength 編集後文字列長
     * @return String 編集後文字列
     */
    public String leftFitFullFormat(String strOrg, int nLength)
    {
        int i;
        int strlen;
        String buff;

        buff = new String( strOrg );

        strlen = buff.getBytes().length;

        for( i = 0 ; i < nLength - strlen ; i += 2 )
        {
            buff = buff + "　";
        }

        return(buff);
    }

    /**
     * 右詰めスペース埋めフォーマット
     * 
     * @param strOrg 編集前文字列
     * @param nLength 編集後文字列長
     * @return String 編集後文字列
     */
    public String rightFitFormat(String strOrg, int nLength)
    {
        int i;
        int strlen;
        String buff;
        String space;

        buff = new String( strOrg );
        space = "";

        strlen = buff.getBytes().length;

        for( i = 0 ; i < nLength - strlen ; i++ )
        {
            space = space + " ";
        }

        buff = space + buff;

        return(buff);
    }

    /**
     * 右詰め0埋めフォーマット
     * 
     * @param strOrg 編集前文字列
     * @param nLength 編集後文字列長
     * @return String 編集後文字列
     */
    public String rightFitZeroFormat(String strOrg, int nLength)
    {
        int i;
        int strlen;
        String buff;
        String space;

        buff = new String( strOrg );
        space = "";

        strlen = buff.getBytes().length;

        for( i = 0 ; i < nLength - strlen ; i++ )
        {
            space = space + "0";
        }

        buff = space + buff;

        return(buff);
    }

    /**
     * 右詰め全角スペース埋めフォーマット
     * 
     * @param strOrg 編集前文字列
     * @param nLength 編集後文字列長
     * @return String 編集後文字列
     */
    public String rightFitFullFormat(String strOrg, int nLength)
    {
        int i;
        int strlen;
        String buff;
        String space;

        buff = new String( strOrg );
        space = "";

        strlen = buff.getBytes().length;

        for( i = 0 ; i < nLength - strlen ; i += 2 )
        {
            space = space + "　";
        }

        buff = space + buff;

        return(buff);
    }

    /**
     * 有効文字列取得（全角スペースカット）
     * 
     * @param strOrg 編集前文字列
     * @return String 編集後文字列
     */
    public String cutFullSpace(String strOrg) throws IOException
    {
        int i;
        int point;
        String data;
        String buff;

        point = -1;

        // 全角位置をチェックする
        for( i = 0 ; i < strOrg.length() ; i += 2 )
        {
            data = new String( strOrg.substring( i, i + 2 ).getBytes( "8859_1" ), "Windows-31J" );
            if ( data.compareTo( "　" ) == 0 )
            {
                if ( point == -1 )
                {
                    point = i;
                }
            }
            else
            {
                point = -1;
            }
        }

        if ( point >= 0 )
        {
            buff = strOrg.substring( 0, point );
        }
        else
        {
            buff = strOrg;
        }

        return(buff);
    }
}
