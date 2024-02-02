package jp.happyhotel.touch;

import java.io.Serializable;
import java.util.ArrayList;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * 料金報取得電文クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see リクエスト：0500電文<br>
 *      レスポンス：0501電文
 */
public class PriceInfo implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID     = 5385130148620657030L;
    final int                 HEADER_LENGTH        = 32;
    final int                 COMMAND_LENGTH       = 4;
    final String              COMMAND              = "0500";
    final String              REPLY_COMMAND        = "0501";
    final int                 DISCOUNT_DATA_LENGTH = 24;

    String                    header;
    // 送信電文
    String                    reserve;

    // 受信電文
    int                       result;

    int                       pricePlanLength;                            // 料金プランデータ数
    ArrayList<Integer>        pricePlanCode;                              // 料金プランコード
    ArrayList<String>         pricePlanName;                              // 料金プラン名称

    int                       priceModeLength;                            // 料金モードデータ数
    ArrayList<Integer>        priceModeCode;                              // 料金モードコード
    ArrayList<String>         priceModeName;                              // 料金モード名称

    public PriceInfo()
    {
        this.header = "";
        this.reserve = "";
        this.result = 0;
        this.pricePlanLength = 0;
        priceModeLength = 0;
    }

    public String getReserve()
    {
        return reserve;
    }

    public int getResult()
    {
        return result;
    }

    public int getPricePlanLength()
    {
        return pricePlanLength;
    }

    public ArrayList<Integer> getPricePlanCode()
    {
        return pricePlanCode;
    }

    public ArrayList<String> getPricePlanName()
    {
        return pricePlanName;
    }

    public int getPriceModeLength()
    {
        return priceModeLength;
    }

    public ArrayList<Integer> getPriceModeCode()
    {
        return priceModeCode;
    }

    public ArrayList<String> getPriceModeName()
    {
        return priceModeName;
    }

    public void setReserve(String reserve)
    {
        this.reserve = reserve;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public void setPricePlanLength(int pricePlanLength)
    {
        this.pricePlanLength = pricePlanLength;
    }

    public void setPricePlanCode(ArrayList<Integer> pricePlanCode)
    {
        this.pricePlanCode = pricePlanCode;
    }

    public void setPricePlanName(ArrayList<String> pricePlanName)
    {
        this.pricePlanName = pricePlanName;
    }

    public void setPriceModeLength(int priceModeLength)
    {
        this.priceModeLength = priceModeLength;
    }

    public void setPriceModeCode(ArrayList<Integer> priceModeCode)
    {
        this.priceModeCode = priceModeCode;
    }

    public void setPriceModeName(ArrayList<String> priceModeName)
    {
        this.priceModeName = priceModeName;
    }

    public boolean sendToHost(int paramId)
    {
        String sendData = "";
        TcpClientEx tcpclient = null;
        String recvData = "";
        char[] charData = null;
        String data = "";
        int retryCount = 0;
        boolean ret = false;
        int i;
        int nIndex = 0;
        ClipString clip = new ClipString();

        // frontIpの取得
        String frontIp = HotelIp.getHotenaviIp( paramId );
        int timeOut = Constants.timeoutForHost;
        int portNo = Constants.portNoHotenavi;

        // ホスト側データ送信
        tcpclient = new TcpClientEx();
        // 指定のipアドレスに接続
        ret = tcpclient.connectServiceByAddr( frontIp, timeOut, portNo );

        if ( ret != false )
        {
            try
            {
                sendData = COMMAND;
                sendData += tcpclient.leftFitFormat( this.reserve, 10 );

                header = tcpclient.getPacketHeader( Integer.toString( paramId ), sendData.getBytes( "Windows-31J" ).length );

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
                        // 応答電文コマンドが0501なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            nIndex = HEADER_LENGTH + COMMAND_LENGTH;

                            // 返ってきた情報をセット
                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 料金プランデータ数を取得
                            this.pricePlanLength = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            pricePlanCode = new ArrayList<Integer>();
                            pricePlanName = new ArrayList<String>();
                            // 料金プランデータ数だけ繰り返す
                            for( i = 0 ; i < this.pricePlanLength ; i++ )
                            {
                                Logging.info( "nIndex" + i + ":" + nIndex );
                                this.pricePlanCode.add( clip.clipNum( charData, nIndex, 2 ) );
                                nIndex = clip.getNextIndex();

                                this.pricePlanName.add( clip.clipWord( charData, nIndex, 20 ) );
                                nIndex = clip.getNextIndex();
                            }
                            // 料金モードデータ数を取得
                            this.priceModeLength = clip.clipNum( charData, nIndex, 2 );

                            nIndex = clip.getNextIndex();

                            priceModeCode = new ArrayList<Integer>();
                            priceModeName = new ArrayList<String>();
                            // 料金モードデータ数だけ繰り返す
                            for( i = 0 ; i < this.priceModeLength ; i++ )
                            {
                                this.priceModeCode.add( clip.clipNum( charData, nIndex, 2 ) );
                                nIndex = clip.getNextIndex();

                                this.priceModeName.add( clip.clipWord( charData, nIndex, 20 ) );
                                nIndex = clip.getNextIndex();
                            }

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
                Logging.error( "[PriceInfo.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }

        return ret;
    }
}
