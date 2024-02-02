/*
 * @(#)CustomInfo.java 2.00 2004/03/31
 * Copyright (C) ALMEX Inc. 2004
 * 顧客情報関連通信APクラス
 */

package com.hotenavi2.discount;

import java.io.Serializable;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.hotenavi2.common.DbAccess;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClient;

/**
 * AMFWEBサービスとのクーポン情報関連電文編集・送受信を行う。
 * 
 * @author S.Tashiro
 * @version 2.00 2014/09/22
 */
public class DiscountInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID      = -9152132824736895499L;

    // ------------------------------------------------------------------------------
    // 定数定義
    // ------------------------------------------------------------------------------
    /** 割引情報最大数 **/
    public static final int   DISCOUNT_INFO_MAX     = 400;

    /** 料金プラン最大数 **/
    public static final int   DISCOUNT_INFO_PLANMAX = 20;
    /** 料金モード最大数 **/
    public static final int   DISCOUNT_INFO_MODEMAX = 20;

    // ------------------------------------------------------------------------------
    // データ領域定義
    // ------------------------------------------------------------------------------
    /** (共通)処理結果 **/
    public int                Result;
    /** (共通)ホテルID **/
    public String             HotelId;

    /** (共通)割引コード **/
    public int                DiscountCode;
    /** (共通)割引名称 **/
    public String             DiscountName;

    /** (共通)付帯情報1コード **/
    public int[]              SupplementaryCode1;
    /** (共通)付帯情報1名称 **/
    public String[]           SupplementaryName1;
    /** (共通)付帯情報2コード **/
    public int[]              SupplementaryCode2;
    /** (共通)付帯情報2名称 **/
    public String[]           SupplementaryName2;

    /** (割引情報取得)設定データ数 **/
    public int                DataLength;
    /** (割引情報取得)割引金額 **/
    public int[]              Discount;
    /** (割引情報取得)割引率 **/
    public int[]              DiscountRate;

    /** (付帯割引情報取得)割引データ **/
    public int                DiscountData;
    /** (付帯割引情報取得)割引コード **/
    public int[]              DiscountCodeList;
    /** (付帯割引情報取得)割引デ名称 **/
    public String[]           DiscountNameList;
    /** (付帯割引情報取得)付帯情報1データ数 **/
    public int                SupplementaryData1;
    /** (付帯割引情報取得)付帯情報2データ数 **/
    public int                SupplementaryData2;

    /** (割引実績取得)開始日 **/
    public int                StartDate;
    /** (割引実績取得)終了日 **/
    public int                EndDate;

    /** (割引実績取得)割引実績データ数 **/
    public int                DiscountResultData;
    /** (割引実績取得)日付 **/
    public int[]              CollectDate;
    /** (割引実績取得)割引総数 **/
    public int[]              DiscountAll;
    /** (割引実績取得)割引実数 **/
    public int[]              DiscountReal;
    /** (割引実績取得)割引総額 **/
    public int[]              DiscountTotal;
    /** (割引実績取得)割引実額 **/
    public int[]              DiscountResult;

    /** デバッグログ **/
    public LogLib             log;

    /**
     * 顧客情報データの初期化を行います。
     * 
     */
    public DiscountInfo()
    {
        Result = 0;
        HotelId = "";
        DiscountCode = 0;
        DiscountName = "";
        DataLength = 0;
        DiscountData = 0;
        SupplementaryData1 = 0;
        SupplementaryData2 = 0;

        log = new LogLib();
    }

    // ------------------------------------------------------------------------------
    //
    // 電文処理
    //
    // ------------------------------------------------------------------------------
    /**
     * 電文送信処理(0500)
     * 割引情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0500()
    {
        return(sendPacket0500Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0500)
     * 割引情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0500(int kind, String value)
    {
        return(sendPacket0500Sub( kind, value ));
    }

    /**
     * 割引情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0500Sub(int kind, String value)
    {
        boolean blnRet;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;
        int count = 0;
        int i = 0;
        // データのクリア
        Result = 0;
        SupplementaryCode1 = new int[DISCOUNT_INFO_MAX];
        SupplementaryCode2 = new int[DISCOUNT_INFO_MAX];
        Discount = new int[DISCOUNT_INFO_MAX];
        DiscountRate = new int[DISCOUNT_INFO_MAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0500";
                // 割引コード
                nf = new DecimalFormat( "0000" );
                strData = Integer.toString( DiscountCode );

                strSend = strSend + format.leftFitFormat( strData, 4 );
                strSend += format.leftFitFormat( "", 10 );

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
                // 電文の結合
                strSend = strHeader + strSend;

                try
                {
                    // 電文送信
                    tcpClient.send( strSend );

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0501" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 36, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            strData = new String( cRecv, 38, 4 );
                            DiscountCode = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 42, 20 );
                            DiscountName = strData.trim();

                            strData = new String( cRecv, 62, 2 );
                            count = Integer.valueOf( strData ).intValue();
                            DataLength = count;

                            for( i = 0 ; i < count ; i++ )
                            {
                                // 付帯情報1コード
                                strData = new String( cRecv, 65 + (i * 12), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryCode1[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 付帯情報2コード
                                strData = new String( cRecv, 67 + (i * 12), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryCode2[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 割引金額
                                strData = new String( cRecv, 69 + (i * 12), 5 );
                                if ( strData != null )
                                {
                                    Discount[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 割引金額
                                strData = new String( cRecv, 74 + (i * 12), 5 );
                                if ( strData != null )
                                {
                                    DiscountRate[i] = Integer.valueOf( strData ).intValue();
                                }

                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0500:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }

            return(false);
        }

        return(false);
    }

    /**
     * 電文送信処理(0502)
     * 割引情報登録要求
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0502()
    {
        return(sendPacket0502Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0502)
     * 割引情報登録要求
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0502(int kind, String value)
    {
        return(sendPacket0502Sub( kind, value ));
    }

    /**
     * 割引情報登録要求
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0502Sub(int kind, String value)
    {
        boolean blnRet;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;
        int count = 0;
        int i = 0;
        // データのクリア
        Result = 0;
        SupplementaryCode1 = new int[DISCOUNT_INFO_MAX];
        SupplementaryCode2 = new int[DISCOUNT_INFO_MAX];
        Discount = new int[DISCOUNT_INFO_MAX];
        DiscountRate = new int[DISCOUNT_INFO_MAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0502";
                // 割引コード
                nf = new DecimalFormat( "0000" );
                // 割引コード
                strData = Integer.toString( DiscountCode );
                strSend += format.leftFitFormat( strData, 4 );
                // 割引名称
                strSend += format.leftFitFormat( DiscountName, 20 );

                // 設定データ数
                nf = new DecimalFormat( "000" );
                strSend += nf.format( Integer.valueOf( DataLength ).intValue() );

                for( i = 0 ; i < DataLength ; i++ )
                {
                    nf = new DecimalFormat( "00" );
                    // 付帯情報1をセット
                    strSend += nf.format( SupplementaryCode1[i] );

                    // 付帯情報2をセット
                    strSend += nf.format( SupplementaryCode2[i] );

                    // 割引金額をセット
                    nf = new DecimalFormat( "00000" );
                    strSend += nf.format( Discount[i] );

                    // 割引率をセット
                    nf = new DecimalFormat( "000" );
                    strSend += nf.format( DiscountRate );

                }

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
                // 電文の結合
                strSend = strHeader + strSend;

                try
                {
                    // 電文送信
                    tcpClient.send( strSend );

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0503" ) == 0 )
                        {

                            // 割引コード
                            strData = new String( cRecv, 36, 4 );
                            DiscountCode = Integer.valueOf( strData ).intValue();

                            // 処理結果取得
                            strData = new String( cRecv, 40, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0502:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }

            return(false);
        }

        return(false);
    }

    /**
     * 電文送信処理(0504)
     * 付帯割引情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0504()
    {
        return(sendPacket0504Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0504)
     * 付帯割引情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0504(int kind, String value)
    {
        return(sendPacket0504Sub( kind, value ));
    }

    /**
     * 付帯割引情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0504Sub(int kind, String value)
    {
        boolean blnRet;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;
        int count = 0;
        int i = 0;
        int addIndex = 0;

        // データのクリア
        Result = 0;
        DiscountCodeList = new int[DISCOUNT_INFO_MAX];
        DiscountNameList = new String[DISCOUNT_INFO_MAX];
        SupplementaryCode1 = new int[DISCOUNT_INFO_PLANMAX];
        SupplementaryName1 = new String[DISCOUNT_INFO_PLANMAX];
        SupplementaryCode2 = new int[DISCOUNT_INFO_PLANMAX];
        SupplementaryName2 = new String[DISCOUNT_INFO_PLANMAX];
        Discount = new int[DISCOUNT_INFO_MAX];
        DiscountRate = new int[DISCOUNT_INFO_MAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0504";
                strSend += format.leftFitFormat( "", 10 );

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
                // 電文の結合
                strSend = strHeader + strSend;

                try
                {
                    // 電文送信
                    tcpClient.send( strSend );

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0505" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 36, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            strData = new String( cRecv, 38, 4 );
                            DiscountData = Integer.valueOf( strData ).intValue();

                            // 初期化
                            DiscountCodeList = new int[DiscountData];
                            DiscountNameList = new String[DiscountData];

                            for( i = 0 ; i < DiscountData ; i++ )
                            {
                                strData = new String( cRecv, 40 + i * 24, 4 );
                                DiscountCodeList[i] = Integer.valueOf( strData ).intValue();

                                strData = new String( cRecv, 44 + i * 24, 20 );
                                DiscountNameList[i] = strData.trim();

                                // 繰り返した分だけ取得位置をずらす。
                                addIndex += 24;
                            }

                            // 付帯情報1データ数
                            strData = new String( cRecv, 64 + addIndex, 2 );
                            count = Integer.valueOf( strData ).intValue();
                            SupplementaryData1 = count;

                            // 初期化
                            SupplementaryCode1 = new int[SupplementaryData1];
                            SupplementaryName1 = new String[SupplementaryData1];
                            for( i = 0 ; i < SupplementaryData1 ; i++ )
                            {
                                // 付帯情報1コード
                                strData = new String( cRecv, 66 + (i * 52), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryCode1[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 付帯情報1名称
                                strData = new String( cRecv, 70 + (i * 52), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryName1[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                                // 繰り返した分だけ取得位置をずらす。
                                addIndex += 24;
                            }

                            // 付帯情報2データ数
                            strData = new String( cRecv, 90 + addIndex, 2 );
                            count = Integer.valueOf( strData ).intValue();
                            SupplementaryData2 = count;

                            // 初期化
                            SupplementaryCode2 = new int[SupplementaryData2];
                            SupplementaryName2 = new String[SupplementaryData2];
                            for( i = 0 ; i < SupplementaryData2 ; i++ )
                            {
                                // 付帯情報2コード
                                strData = new String( cRecv, 92 + (i * 52), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryCode2[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 付帯情報2名称
                                strData = new String( cRecv, 96 + (i * 52), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryName2[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                            }

                        }

                    }

                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0504:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }

            return(false);
        }

        return(false);
    }

    /**
     * 電文送信処理(0506)
     * 割引実績取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0506()
    {
        return(sendPacket0504Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0506)
     * 割引実績取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0506(int kind, String value)
    {
        return(sendPacket0506Sub( kind, value ));
    }

    /**
     * 付帯割引情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0506Sub(int kind, String value)
    {
        boolean blnRet;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;
        int count = 0;
        int i = 0;
        int addIndex = 0;

        // データのクリア
        Result = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0506";
                nf = new DecimalFormat( "00000000" );
                strSend += nf.format( StartDate );
                strSend += nf.format( EndDate );

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
                // 電文の結合
                strSend = strHeader + strSend;

                try
                {
                    // 電文送信
                    tcpClient.send( strSend );

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0507" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 36, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            strData = new String( cRecv, 38, 3 );
                            DiscountResultData = Integer.valueOf( strData ).intValue();

                            // 初期化
                            CollectDate = new int[DiscountResultData];
                            DiscountAll = new int[DiscountResultData];
                            DiscountReal = new int[DiscountResultData];
                            DiscountTotal = new int[DiscountResultData];
                            DiscountResult = new int[DiscountResultData];

                            for( i = 0 ; i < DiscountData ; i++ )
                            {
                                // 日付
                                strData = new String( cRecv, 41 + i * 40, 8 );
                                CollectDate[i] = Integer.valueOf( strData ).intValue();

                                // 割引総数
                                strData = new String( cRecv, 49 + i * 40, 8 );
                                DiscountAll[i] = Integer.valueOf( strData ).intValue();

                                // Web割引数
                                strData = new String( cRecv, 57 + i * 40, 8 );
                                DiscountReal[i] = Integer.valueOf( strData ).intValue();

                                // 割引総額
                                strData = new String( cRecv, 65 + i * 40, 8 );
                                DiscountTotal[i] = Integer.valueOf( strData ).intValue();

                                // Web割引額
                                strData = new String( cRecv, 65 + i * 40, 8 );
                                DiscountResult[i] = Integer.valueOf( strData ).intValue();
                            }
                        }

                    }

                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0504:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }

            return(false);
        }

        return(false);
    }

    // ------------------------------------------------------------------------------
    // 内部処理関数
    // ------------------------------------------------------------------------------
    /**
     * ホスト接続処理
     * 
     * @param tcpclient TCP接続クライアント識別子
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean connect(TcpClient tcpclient, int kind, String value)
    {
        boolean ret;
        String query;
        ResultSet result;
        DbAccess db;

        switch( kind )
        {
        // セットされたホテルIDで接続
            case 0:

                try
                {
                    db = new DbAccess();
                    query = "SELECT * FROM hotel WHERE hotel_id='" + HotelId + "'";
                    result = db.execQuery( query );
                    if ( result.next() != false )
                    {
                        ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ) );
                    }
                    else
                    {
                        ret = tcpclient.connectService( HotelId );
                    }

                    db.close();
                }
                catch ( Exception e )
                {
                    ret = tcpclient.connectService( HotelId );
                    log.error( "connect(0):" + e.toString() );
                }
                break;

            // 指定されたホテルIDで接続
            case 1:

                try
                {
                    db = new DbAccess();
                    query = "SELECT * FROM hotel WHERE hotel_id='" + HotelId + "'";
                    result = db.execQuery( query );
                    if ( result.next() != false )
                    {
                        ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ) );
                    }
                    else
                    {
                        ret = tcpclient.connectService( value );
                    }

                    db.close();
                }
                catch ( Exception e )
                {
                    ret = tcpclient.connectService( value );
                    log.error( "connect(1):" + e.toString() );
                }

                break;

            // IPアドレスで接続
            case 2:

                ret = tcpclient.connectServiceByAddr( value );
                break;

            // セットされたホテルIDで接続
            default:

                ret = tcpclient.connectService( HotelId );
                break;
        }

        return(ret);
    }
}
