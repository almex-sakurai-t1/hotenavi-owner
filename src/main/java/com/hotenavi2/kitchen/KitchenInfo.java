package com.hotenavi2.kitchen;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClientCommon;

/**
 * キッチンサービスとのキッチン端末情報関連電文編集・送受信を行う<br>
 * 
 * 初期電文 sendPacket0000(int kind, String value)<br>
 * 履歴取得電文 sendPacket0006(int kind, String value, int slipNo, int getCount)<br>
 * 伝票情報のソート SortOrderInfo(byte orderInfoType, byte type)<br>
 * 
 * @author Y.Tanabe
 * @version 2.00 2011/06/30
 */
@SuppressWarnings("serial")
public class KitchenInfo implements Serializable
{
    // ------------------------------------------------------------------------------
    // 定数定義
    // ------------------------------------------------------------------------------
    /** キッチンサービス接続ポート番号 **/
    public static final int     KITCHENSERVICE_PORTNO  = 6984;
    /** 配膳完了最大件数 **/
    public static final int     SETTABLE_MAXCOUNT      = 10;
    /** オーダー履歴最大件数 **/
    public static final int     ORDER_HISTORY_MAXCOUNT = 10;
    /** 初期起動時オーダー最大件数 **/
    public static final int     INITIAL_ORDER_MAXCOUNT = 128;
    /** オーダー最大件数 **/
    public static final int     ORDER_MAXCOUNT         = 32;

    /** ソート種別伝票番号 **/
    public static final byte    SORTYPE_SLIP           = 0;
    /** ソート種別受付日時 **/
    public static final byte    SORTTYPE_ACCEPT        = 1;
    /** ソート種別配膳指定日時 **/
    public static final byte    SORTTYPE_SETTABLE      = 2;
    /** ソート種別部屋名称 **/
    public static final byte    SORTTYPE_ROOM          = 3;
    /** ソート種別オーダー分類名称 **/
    public static final byte    SORTTYPE_ORDERCLASS    = 4;
    /** ソート種別商品名称 **/
    public static final byte    SORTTYPE_GOODS         = 5;
    /** ソート種別商品数量 **/
    public static final byte    SORTTYPE_COUNT         = 6;
    /** ソート種別配膳完了日時 **/
    public static final byte    SORTTYPE_SETTABLEFIN   = 7;
    /** ソート種別商品コード **/
    public static final byte    SORTTYPE_GOODSCODE     = 8;

    /** 初期電文結果オーダー情報 **/
    public static final byte    ORDERINFOTYPE_INITIAL  = 1;
    /** 履歴オーダー情報 **/
    public static final byte    ORDERINFOTYPE_HISTORY  = 2;
    // ------------------------------------------------------------------------------
    // データ領域定義
    // ------------------------------------------------------------------------------
    /** (共通)処理結果 **/
    public int                  Result;
    /** (共通)ホテルID **/
    public String               HotelId;

    /** (厨房端末起動初期処理)現在日付(YYYYMMDD) **/
    public int                  InitialNowDate;
    /** (厨房端末起動初期処理)現在曜日(0:sunday～6:Saturday) **/
    public int                  InitialNowDayofweek;
    /** (厨房端末起動初期処理)現在時刻(HHMM) **/
    public int                  InitialNowTime;
    /** (厨房端末起動初期処理)次データフラグ **/
    public int                  InitialNextDataFlag;
    /** (厨房端末起動初期処理)オーダー件数 **/
    public int                  InitialOrderCount;
    /** (厨房端末起動初期処理)伝票番号 **/
    public int                  InitialSlipNo[];
    /** (厨房端末起動初期処理)受付日付(YYYYMMDD) **/
    public int                  InitialAcceptDate[];
    /** (厨房端末起動初期処理)受付時刻(HHMM) **/
    public int                  InitialAcceptTime[];
    /** (厨房端末起動初期処理)配膳日付(YYYYMMDD) **/
    public int                  InitialSettableDate[];
    /** (厨房端末起動初期処理)配膳時刻(HHMM) **/
    public int                  InitialSettableTime[];
    /** (厨房端末起動初期処理)部屋コード **/
    public int                  InitialRoomCode[];
    /** (厨房端末起動初期処理)部屋名称 **/
    public String               InitialRoomName[];
    /** (厨房端末起動初期処理)オーダー分類コード **/
    public int                  InitialOrderClassCode[];
    /** (厨房端末起動初期処理)オーダー分類名称 **/
    public String               InitialOrderClassName[];
    /** (厨房端末起動初期処理)商品コード **/
    public int                  InitialGoodsCode[];
    /** (厨房端末起動初期処理)商品名称 **/
    public String               InitialGoodsName[];
    /** (厨房端末起動初期処理)数量 **/
    public int                  InitialGoodsCount[];

    /** (配膳履歴)現在日付(YYYYMMDD) **/
    public int                  HistoryNowDate;
    /** (配膳履歴)現在曜日(0:sunday～6:Saturday) **/
    public int                  HistoryNowDayofweek;
    /** (配膳履歴)現在時刻(HHMM) **/
    public int                  HistoryNowTime;
    /** (配膳履歴)次ページ伝票番号 0:次ページなし **/
    public int                  HistoryNextPageSlipNo;
    /** (配膳履歴)オーダー件数 **/
    public int                  HistoryOrderCount;
    /** (配膳履歴)伝票番号 **/
    public int                  HistorySlipNo[];
    /** (配膳履歴)受付日付 **/
    public int                  HistoryAcceptDate[];
    /** (配膳履歴)受付時刻 **/
    public int                  HistoryAcceptTime[];
    /** (配膳履歴)配膳指定日付 **/
    public int                  HistorySettableDate[];
    /** (配膳履歴)配膳指定時刻 **/
    public int                  HistorySettableTime[];
    /** (配膳履歴)部屋コード **/
    public int                  HistoryRoomCode[];
    /** (配膳履歴)部屋名称 **/
    public String               HistoryRoomName[];
    /** (配膳履歴)オーダー分類コード **/
    public int                  HistoryOrderClassCode[];
    /** (配膳履歴)オーダー分類名称 **/
    public String               HistoryOrderClassName[];
    /** (配膳履歴)商品コード **/
    public int                  HistoryGoodsCode[];
    /** (配膳履歴)商品名称 **/
    public String               HistoryGoodsName[];
    /** (配膳履歴)数量 **/
    public int                  HistoryGoodsCount[];
    /** (配膳履歴)配膳完了日付 **/
    public int                  HistorySettableFinDate[];
    /** (配膳履歴)配膳完了時刻 **/
    public int                  HistorySettableFinTime[];

    /** (厨房端末起動初期処理)オーダー情報 **/
    public ArrayList<OrderInfo> InitialOrderList;
    /** (配膳履歴)オーダー情報 **/
    public ArrayList<OrderInfo> HistoryOrderList;

    /** ログ出力ライブラリ **/
    private LogLib              log;

    /**
     * キッチン関連情報データの初期化を行います。
     * 
     */
    public KitchenInfo()
    {
        HotelId = "";

        InitialNowDate = 0;
        InitialNowDayofweek = 0;
        InitialNowTime = 0;
        InitialNextDataFlag = 0;
        InitialOrderCount = 0;
        InitialSlipNo = new int[INITIAL_ORDER_MAXCOUNT];
        InitialAcceptDate = new int[INITIAL_ORDER_MAXCOUNT];
        InitialAcceptTime = new int[INITIAL_ORDER_MAXCOUNT];
        InitialSettableDate = new int[INITIAL_ORDER_MAXCOUNT];
        InitialSettableTime = new int[INITIAL_ORDER_MAXCOUNT];
        InitialRoomCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialRoomName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialOrderClassCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialOrderClassName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsCount = new int[INITIAL_ORDER_MAXCOUNT];

        InitialOrderList = new ArrayList<OrderInfo>();

        HistoryNowDate = 0;
        HistoryNowDayofweek = 0;
        HistoryNowTime = 0;
        HistoryNextPageSlipNo = 0;
        HistoryOrderCount = 0;
        HistorySlipNo = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryAcceptDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryAcceptTime = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableTime = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryRoomCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryRoomName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryOrderClassCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryOrderClassName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsCount = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableFinDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableFinTime = new int[ORDER_HISTORY_MAXCOUNT];

        HistoryOrderList = new ArrayList<OrderInfo>();

        log = new LogLib();
    }

    // ------------------------------------------------------------------------------
    //
    // 電文処理メソッド
    //
    // ------------------------------------------------------------------------------
    /**
     * 電文送信処理(0000)
     * 厨房端末起動初期処理電文
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0000()
    {
        return(sendPacket0000Sub( TcpClientCommon.TCPCONNECTTYPE_COMMONHOTELID, "" ));
    }

    /**
     * 電文送信処理(0000)
     * 厨房端末起動初期処理電文
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0000(int kind, String value)
    {
        return(sendPacket0000Sub( kind, value ));
    }

    /**
     * 電文送信処理(0000)
     * 厨房端末起動初期処理電文
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0000Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClientCommon tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        InitialNowDate = 0;
        InitialNowDayofweek = 0;
        InitialNowTime = 0;
        InitialNextDataFlag = 0;
        InitialOrderCount = 0;
        InitialSlipNo = new int[INITIAL_ORDER_MAXCOUNT];
        InitialAcceptDate = new int[INITIAL_ORDER_MAXCOUNT];
        InitialAcceptTime = new int[INITIAL_ORDER_MAXCOUNT];
        InitialSettableDate = new int[INITIAL_ORDER_MAXCOUNT];
        InitialSettableTime = new int[INITIAL_ORDER_MAXCOUNT];
        InitialRoomCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialRoomName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialOrderClassCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialOrderClassName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsCount = new int[INITIAL_ORDER_MAXCOUNT];

        InitialOrderList = new ArrayList<OrderInfo>();

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClientCommon( KITCHENSERVICE_PORTNO );
            blnRet = tcpClient.connect( kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0000";

                // 電文ヘッダの取得(端末IDは使わないので0をセット)
                strHeader = tcpClient.getPacketHeader( TcpClientCommon.CONNECTAPTYPE_KITCHEN, "0", strSend.length() );
                // 電文の結合
                strSend = strHeader + strSend;

                try
                {
                    // 電文送信
                    tcpClient.send( strSend );

                    // 電文送信後受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0001" ) == 0 )
                        {
                            // 現在日付
                            strData = new String( cRecv, 36, 8 );
                            InitialNowDate = Integer.valueOf( strData ).intValue();

                            // 現在曜日
                            strData = new String( cRecv, 44, 2 );
                            InitialNowDayofweek = Integer.valueOf( strData ).intValue();

                            // 現在時刻
                            strData = new String( cRecv, 46, 4 );
                            InitialNowTime = Integer.valueOf( strData ).intValue();

                            // 次データフラグ
                            strData = new String( cRecv, 50, 1 );
                            InitialNextDataFlag = Integer.valueOf( strData ).intValue();

                            // オーダー件数
                            strData = new String( cRecv, 51, 3 );
                            InitialOrderCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < InitialOrderCount ; i++ )
                            {
                                OrderInfo item = new OrderInfo();
                                // 伝票番号
                                strData = new String( cRecv, 54 + 134 * i, 8 );
                                InitialSlipNo[i] = Integer.valueOf( strData ).intValue();
                                item.SlipNo = InitialSlipNo[i];
                                // 受付日付
                                strData = new String( cRecv, 62 + 134 * i, 8 );
                                InitialAcceptDate[i] = Integer.valueOf( strData ).intValue();
                                // 受付時刻
                                strData = new String( cRecv, 70 + 134 * i, 4 );
                                InitialAcceptTime[i] = Integer.valueOf( strData ).intValue();
                                item.AcceptDatetime = String.format( "%08d", InitialAcceptDate[i] ) + String.format( "%04d", InitialAcceptTime[i] );
                                // 配膳指定日付
                                strData = new String( cRecv, 74 + 134 * i, 8 );
                                InitialSettableDate[i] = Integer.valueOf( strData ).intValue();
                                // 配膳指定時刻
                                strData = new String( cRecv, 82 + 134 * i, 4 );
                                InitialSettableTime[i] = Integer.valueOf( strData ).intValue();
                                item.SettableDatetime = String.format( "%08d", InitialSettableDate[i] ) + String.format( "%04d", InitialSettableTime[i] );
                                // 部屋コード
                                strData = new String( cRecv, 86 + 134 * i, 3 );
                                InitialRoomCode[i] = Integer.valueOf( strData ).intValue();
                                item.RoomCode = InitialRoomCode[i];
                                // 部屋名称
                                strData = new String( cRecv, 89 + 134 * i, 8 );
                                InitialRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                item.RoomName = InitialRoomName[i];
                                // オーダー分類コード
                                strData = new String( cRecv, 97 + 134 * i, 4 );
                                InitialOrderClassCode[i] = Integer.valueOf( strData ).intValue();
                                item.OrderClassCode = InitialOrderClassCode[i];
                                // オーダー分類名称
                                strData = new String( cRecv, 101 + 134 * i, 40 );
                                InitialOrderClassName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                item.OrderClassName = InitialOrderClassName[i];
                                // 商品コード
                                strData = new String( cRecv, 141 + 134 * i, 4 );
                                InitialGoodsCode[i] = Integer.valueOf( strData ).intValue();
                                item.GoodsCode = InitialGoodsCode[i];
                                // 商品名称
                                strData = new String( cRecv, 145 + 134 * i, 40 );
                                InitialGoodsName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                item.GoodsName = InitialGoodsName[i];
                                // 数量
                                strData = new String( cRecv, 185 + 134 * i, 3 );
                                InitialGoodsCount[i] = Integer.valueOf( strData ).intValue();
                                item.GoodsCount = InitialGoodsCount[i];
                                InitialOrderList.add( item );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendKitchenPacket0000:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0006)
     * 配膳履歴電文
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0006()
    {
        return(sendPacket0006Sub( TcpClientCommon.TCPCONNECTTYPE_COMMONHOTELID, "", 0, 10 ));
    }

    /**
     * 電文送信処理(0006)
     * 配膳履歴電文
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @param slipNo 伝票番号
     * @param getCount 取得要求件数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0006(int kind, String value, int slipNo, int getCount)
    {
        return(sendPacket0006Sub( kind, value, slipNo, getCount ));
    }

    /**
     * 電文送信処理(0006)
     * 配膳履歴電文
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @param slipNo 伝票番号
     * @param getCount 取得要求件数
     * @return 処理結果
     */
    private boolean sendPacket0006Sub(int kind, String value, int slipNo, int getCount)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClientCommon tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        HistoryNowDate = 0;
        HistoryNowDayofweek = 0;
        HistoryNowTime = 0;
        HistoryNextPageSlipNo = 0;
        HistoryOrderCount = 0;
        HistorySlipNo = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryAcceptDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryAcceptTime = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableTime = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryRoomCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryRoomName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryOrderClassCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryOrderClassName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsCount = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableFinDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableFinTime = new int[ORDER_HISTORY_MAXCOUNT];

        HistoryOrderList = new ArrayList<OrderInfo>();

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClientCommon( KITCHENSERVICE_PORTNO );
            blnRet = tcpClient.connect( kind, value );

            if ( blnRet != false )
            {
                int roopCount = 0;
                while( true )
                {

                    format = new StringFormat();

                    // コマンド
                    strSend = "0006";
                    // 伝票番号
                    nf = new DecimalFormat( "00000000" );
                    strData = nf.format( Integer.valueOf( slipNo ).intValue() );
                    strSend = strSend + strData;

                    // 電文ヘッダの取得(端末IDは使わないので0をセット)
                    strHeader = tcpClient.getPacketHeader( TcpClientCommon.CONNECTAPTYPE_KITCHEN, "0", strSend.length() );
                    // 電文の結合
                    strSend = strHeader + strSend;

                    try
                    {
                        // 電文送信
                        tcpClient.send( strSend );

                        // 電文送信後受信待機
                        strRecv = tcpClient.recv();
                        if ( strRecv != null )
                        {
                            cRecv = new char[strRecv.length()];
                            cRecv = strRecv.toCharArray();

                            // コマンド取得
                            strData = new String( cRecv, 32, 4 );
                            if ( strData.compareTo( "0007" ) == 0 )
                            {
                                // 現在日付
                                strData = new String( cRecv, 36, 8 );
                                HistoryNowDate = Integer.valueOf( strData ).intValue();

                                // 現在曜日
                                strData = new String( cRecv, 44, 2 );
                                HistoryNowDayofweek = Integer.valueOf( strData ).intValue();

                                // 現在時刻
                                strData = new String( cRecv, 46, 4 );
                                HistoryNowTime = Integer.valueOf( strData ).intValue();

                                // 次ページ伝票番号
                                strData = new String( cRecv, 50, 8 );
                                HistoryNextPageSlipNo = Integer.valueOf( strData ).intValue();

                                // オーダー件数
                                strData = new String( cRecv, 58, 3 );
                                HistoryOrderCount = Integer.valueOf( strData ).intValue();

                                for( i = 0 ; i < HistoryOrderCount ; i++ )
                                {
                                    // 要求件数を超えたら処理終了
                                    if ( roopCount * ORDER_HISTORY_MAXCOUNT + i >= getCount )
                                    {
                                        break;
                                    }
                                    OrderInfo item = new OrderInfo();
                                    // 伝票番号
                                    strData = new String( cRecv, 61 + 146 * i, 8 );
                                    HistorySlipNo[i] = Integer.valueOf( strData ).intValue();
                                    item.SlipNo = HistorySlipNo[i];
                                    // 受付日付
                                    strData = new String( cRecv, 69 + 146 * i, 8 );
                                    HistoryAcceptDate[i] = Integer.valueOf( strData ).intValue();
                                    // 受付時刻
                                    strData = new String( cRecv, 77 + 146 * i, 4 );
                                    HistoryAcceptTime[i] = Integer.valueOf( strData ).intValue();
                                    item.AcceptDatetime = String.format( "%08d", HistoryAcceptDate[i] ) + String.format( "%04d", HistoryAcceptTime[i] );
                                    // 配膳指定日付
                                    strData = new String( cRecv, 81 + 146 * i, 8 );
                                    HistorySettableDate[i] = Integer.valueOf( strData ).intValue();
                                    // 配膳指定時刻
                                    strData = new String( cRecv, 89 + 146 * i, 4 );
                                    HistorySettableTime[i] = Integer.valueOf( strData ).intValue();
                                    item.SettableDatetime = String.format( "%08d", HistorySettableDate[i] ) + String.format( "%04d", HistorySettableTime[i] );
                                    // 部屋コード
                                    strData = new String( cRecv, 93 + 146 * i, 3 );
                                    HistoryRoomCode[i] = Integer.valueOf( strData ).intValue();
                                    item.RoomCode = HistoryRoomCode[i];
                                    // 部屋名称
                                    strData = new String( cRecv, 96 + 146 * i, 8 );
                                    HistoryRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                    item.RoomName = HistoryRoomName[i];
                                    // オーダー分類コード
                                    strData = new String( cRecv, 104 + 146 * i, 4 );
                                    HistoryOrderClassCode[i] = Integer.valueOf( strData ).intValue();
                                    item.OrderClassCode = HistoryOrderClassCode[i];
                                    // オーダー分類名称
                                    strData = new String( cRecv, 108 + 146 * i, 40 );
                                    HistoryOrderClassName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                    item.OrderClassName = HistoryOrderClassName[i];
                                    // 商品コード
                                    strData = new String( cRecv, 148 + 146 * i, 4 );
                                    HistoryGoodsCode[i] = Integer.valueOf( strData ).intValue();
                                    item.GoodsCode = HistoryGoodsCode[i];
                                    // 商品名称
                                    strData = new String( cRecv, 152 + 146 * i, 40 );
                                    HistoryGoodsName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                    item.GoodsName = HistoryGoodsName[i];
                                    // 数量
                                    strData = new String( cRecv, 192 + 146 * i, 3 );
                                    HistoryGoodsCount[i] = Integer.valueOf( strData ).intValue();
                                    item.GoodsCount = HistoryGoodsCount[i];
                                    // 配膳完了日付
                                    strData = new String( cRecv, 195 + 146 * i, 8 );
                                    HistorySettableFinDate[i] = Integer.valueOf( strData ).intValue();
                                    // 配膳完了時刻
                                    strData = new String( cRecv, 203 + 146 * i, 4 );
                                    HistorySettableFinTime[i] = Integer.valueOf( strData ).intValue();
                                    item.SettableFinDatetime = String.format( "%08d", HistorySettableFinDate[i] ) + String.format( "%04d", HistorySettableFinTime[i] );

                                    HistoryOrderList.add( item );

                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        log.error( "sendKitchenPacket0006:" + e.toString() );
                        return(false);
                    }
                    // 次ページなし 又は取得要求件数を超えたら終了
                    if ( HistoryNextPageSlipNo <= 0 || roopCount * ORDER_HISTORY_MAXCOUNT + HistoryOrderCount >= getCount || roopCount > 1000 )
                    {
                        break;
                    }
                    slipNo = HistoryNextPageSlipNo;
                    roopCount++;
                }
                tcpClient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * オーダー情報ソート処理
     * 
     * @param orderInfoType オーダー情報種別　ORDERINFOTYPE_XXXXで定数を用意しています
     * @param type ソート種別　SORTTYPE_XXXXで定数を用意しています
     * 
     */
    @SuppressWarnings("unchecked")
    public void SortOrderInfo(byte orderInfoType, byte type)
    {
        ArrayList<OrderInfo> list = null;

        try
        {
            switch( orderInfoType )
            {
                case ORDERINFOTYPE_INITIAL:
                    list = InitialOrderList;
                    break;
                case ORDERINFOTYPE_HISTORY:
                    list = HistoryOrderList;
                    break;
                default:
                    break;
            }

            if ( list != null )
            {
                switch( type )
                {
                    case SORTYPE_SLIP:
                        Collections.sort( list, new SlipComparator() );
                        break;
                    case SORTTYPE_ACCEPT:
                        Collections.sort( list, new AcceptComparator() );
                        break;
                    case SORTTYPE_SETTABLE:
                        Collections.sort( list, new SettableComparator() );
                        break;
                    case SORTTYPE_ROOM:
                        Collections.sort( list, new RoomComparator() );
                        break;
                    case SORTTYPE_ORDERCLASS:
                        Collections.sort( list, new OrderClassComparator() );
                        break;
                    case SORTTYPE_GOODS:
                        Collections.sort( list, new GoodsComparator() );
                        break;
                    case SORTTYPE_COUNT:
                        Collections.sort( list, new CountComparator() );
                        break;
                    case SORTTYPE_SETTABLEFIN:
                        Collections.sort( list, new SettableFinComparator() );
                        break;
                    case SORTTYPE_GOODSCODE:
                        Collections.sort( list, new GoodsCodeComparator() );
                        break;
                    default:
                        break;
                }
            }
        }
        catch ( Exception e )
        {
            log.error( "SortOrderInfo:" + e.toString() );
        }

    }

}
