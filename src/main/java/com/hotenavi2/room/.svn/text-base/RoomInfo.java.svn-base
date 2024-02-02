/*
 * @(#)RoomInfo.java 2.00 2004/03/31
 * Copyright (C) ALMEX Inc. 2004
 * 部屋情報関連通信APクラス
 */

package com.hotenavi2.room;

import java.io.Serializable;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.hotenavi2.common.DbAccess;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClient;

/**
 * AMFWEBサービスとの部屋情報関連電文編集・送受信を行う。
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/31
 */
public class RoomInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID       = 2027771029278186727L;

    // ------------------------------------------------------------------------------
    // 定数定義
    // ------------------------------------------------------------------------------
    /** 部屋最大数 **/
    public static final int   ROOMINFO_ROOMMAX       = 128;
    /** 部屋ランク最大数 **/
    public static final int   ROOMINFO_ROOMRANKMAX   = 99;
    /** 料金ランク最大数 **/
    public static final int   ROOMINFO_CHARGERANKMAX = 99;

    // ------------------------------------------------------------------------------
    // データ領域定義
    // ------------------------------------------------------------------------------
    /** (共通)処理結果 **/
    public int                Result;
    /** (共通)ホテルID **/
    public String             HotelId;
    /** (共通)顧客番号 **/
    public String             CustomId;
    /** (共通)誕生日（月） **/
    public String             Birthday1;
    /** (共通)誕生日（日） **/
    public String             Birthday2;
    /** (共通)ユーザID **/
    public String             UserId;
    /** (共通)パスワード **/
    public String             Password;
    /** (共通)ニックネーム **/
    public String             NickName;

    /** (空室情報)空室数 **/
    public int                RoomEmpty;
    /** (空室情報)準備中数 **/
    public int                RoomClean;
    /** (空室情報)空室部屋コード一覧(MAX128件) **/
    public int                RoomCodeList[];
    /** (空室情報)空室部屋名称一覧(MAX128件) **/
    public String             RoomList[];
    /** (空室情報)空室部屋ランクコード一覧(MAX128件) **/
    public int                RoomRankCodeList[];
    /** (空室情報)空室部屋ランク名称一覧(MAX128件) **/
    public String             RoomRankList[];
    /** (空室情報)準備中部屋コード一覧(MAX128件) **/
    public int                RoomCodeCleanList[];
    /** (空室情報)準備中部屋名称一覧(MAX128件) **/
    public String             RoomCleanList[];
    /** (空室情報)準備中部屋ランクコード一覧(MAX128件) **/
    public int                RoomRankCodeCleanList[];
    /** (空室情報)準備中部屋ランク名称一覧(MAX128件) **/
    public String             RoomRankCleanList[];

    /** (予約)予約可能部屋数 **/
    public int                ReserveListCount;
    /** (予約)予約可能部屋コード一覧(MAX128件) **/
    public int                ReserveListCode[];
    /** (予約)予約可能部屋名称一覧(MAX128件) **/
    public String             ReserveList[];
    /** (予約)予約部屋コード **/
    public int                ReserveRoomCode;
    /** (予約)予約部屋名称 **/
    public String             ReserveRoomName;
    /** (予約)予約番号 **/
    public int                ReserveNo;
    /** (予約)予約時間 **/
    public int                ReserveLimitTime;
    /** (予約)予約申し込み部屋コード **/
    public int                ReserveReqRoomCode;
    /** (予約)メールアドレス **/
    public String             ReserveMailAddress;
    /** (予約)予約処理結果 **/
    public int                ReserveResult;
    /** (予約)部屋ランク数 **/
    public int                ReserveRoomRankCount;
    /** (予約)部屋ランクコード(MAX99) **/
    public int                ReserveRoomRankCode[];
    /** (予約)部屋ランク名称(MAX99) **/
    public String             ReserveRoomRankName[];
    /** (料金シミュレーション)チェックイン日付 **/
    public int                SimulateCheckinDate;
    /** (料金シミュレーション)チェックイン時間 **/
    public int                SimulateCheckinTime;
    /** (料金シミュレーション)チェックアウト日付 **/
    public int                SimulateCheckoutDate;
    /** (料金シミュレーション)チェックアウト時間 **/
    public int                SimulateCheckoutTime;
    /** (料金シミュレーション)予算 **/
    public int                SimulateBudget;
    /** (料金シミュレーション)部屋タイプ **/
    public int                SimulateRoomType;
    /** (料金シミュレーション)利用金額 **/
    public int                SimulateUseTotal;
    /** (料金シミュレーション)滞在時間 **/
    public int                SimulateStayTime;
    /** (料金シミュレーション)料金明細数 **/
    public int                SimulatePlanCount;
    /** (料金シミュレーション)プラン名称(MAX99) **/
    public String             SimulatePlanName[];
    /** (料金シミュレーション)プラン金額(MAX99) **/
    public int                SimulatePlanTotal[];
    /** (料金シミュレーション)部屋ランク数 **/
    public int                SimulateRoomRankCount;
    /** (料金シミュレーション)部屋ランクコード(MAX99) **/
    public int                SimulateRoomRankCode[];
    /** (料金シミュレーション)部屋ランク名称(MAX99) **/
    public String             SimulateRoomRankName[];
    /** (料金シミュレーション)時間超過フラグ **/
    public int                SimulateTimeOver;
    /** (本日料金ランク)料金ランク **/
    public int                TodayChargeRank;
    /** (本日料金ランク)料金ランク名称 **/
    public String             TodayChargeRankName;

    /** (事前予約可能時間)チェックイン日付 **/
    public int                BeforeAcceptInDate;
    /** (事前予約可能時間)チェックイン時刻 **/
    public int                BeforeAcceptInTime;
    /** (事前予約可能時間)チェックアウト日付 **/
    public int                BeforeAcceptOutDate;
    /** (事前予約可能時間)チェックアウト時刻 **/
    public int                BeforeAcceptOutTime;

    /** (事前予約可能部屋一覧)チェックイン日付 **/
    public int                BeforeListInDate;
    /** (事前予約可能部屋一覧)チェックイン時刻 **/
    public int                BeforeListInTime;
    /** (事前予約可能部屋一覧)チェックアウト日付 **/
    public int                BeforeListOutDate;
    /** (事前予約可能部屋一覧)チェックアウト時刻 **/
    public int                BeforeListOutTime;
    /** (事前予約可能部屋一覧)部屋タイプコード **/
    public int                BeforeListRoomType;
    /** (事前予約可能部屋一覧)予約可能部屋数 **/
    public int                BeforeListRoomCount;
    /** (事前予約可能部屋一覧)部屋コード **/
    public int                BeforeListRoomCode[];
    /** (事前予約可能部屋一覧)部屋名称 **/
    public String             BeforeListRoomName[];
    /** (事前予約可能部屋一覧)部屋タイプコード **/
    public int                BeforeListTypeCode[];
    /** (事前予約可能部屋一覧)部屋タイプ名称 **/
    public String             BeforeListTypeName[];
    /** (事前予約可能部屋一覧)チェックイン可能日付 **/
    public int                BeforeListInPossibleDate[];
    /** (事前予約可能部屋一覧)チェックイン可能時刻 **/
    public int                BeforeListInPossibleTime[];
    /** (事前予約可能部屋一覧)チェックアウト可能日付 **/
    public int                BeforeListOutPossibleDate[];
    /** (事前予約可能部屋一覧)チェックアウト可能時刻 **/
    public int                BeforeListOutPossibleTime[];
    /** (事前予約可能部屋一覧)合計金額 **/
    public int                BeforeListTotal[];
    /** (事前予約可能部屋一覧)料金明細数 **/
    public int                BeforeListDetailCount[];
    /** (事前予約可能部屋一覧)料金名称 **/
    public String             BeforeListDetailName[][];
    /** (事前予約可能部屋一覧)金額 **/
    public int                BeforeListDetailPrice[][];

    /** (事前予約承認)チェックイン日付 **/
    public int                BeforeAuthInDate;
    /** (事前予約承認)チェックイン時刻 **/
    public int                BeforeAuthInTime;
    /** (事前予約承認)チェックアウト日付 **/
    public int                BeforeAuthOutDate;
    /** (事前予約承認)チェックアウト時刻 **/
    public int                BeforeAuthOutTime;
    /** (事前予約承認)部屋コード **/
    public int                BeforeAuthRoomCode;
    /** (事前予約承認)部屋名称 **/
    public String             BeforeAuthRoomName;
    /** (事前予約承認)部屋タイプコード **/
    public int                BeforeAuthTypeCode;
    /** (事前予約承認)部屋タイプ名称 **/
    public String             BeforeAuthTypeName;
    /** (事前予約承認)合計金額 **/
    public int                BeforeAuthTotal;
    /** (事前予約承認)カード番号 **/
    public String             BeforeAuthCardNo;
    /** (事前予約承認)有効期限 **/
    public int                BeforeAuthExpire;
    /** (事前予約承認)名前（性） **/
    public String             BeforeAuthName1;
    /** (事前予約承認)名前（名） **/
    public String             BeforeAuthName2;
    /** (事前予約承認)セキュリティコード **/
    public String             BeforeAuthSecurity;
    /** (事前予約承認)取消可能日付 **/
    public int                BeforeAuthCancelDate;
    /** (事前予約承認)取消可能時刻 **/
    public int                BeforeAuthCancelTime;
    /** (事前予約承認)予約番号 **/
    public int                BeforeAuthReserveNo;
    /** (事前予約承認)承認番号 **/
    public String             BeforeAuthApprove;
    /** (事前予約承認)クレジットエラーコード **/
    public String             BeforeAuthErrorCode;

    /** (事前予約一覧)カード番号 **/
    public String             BeforeReserveCardNo;
    /** (事前予約一覧)予約番号 **/
    public int                BeforeReserveNo;
    /** (事前予約一覧)予約数 **/
    public int                BeforeReserveCount;
    /** (事前予約一覧)部屋コード **/
    public int                BeforeReserveRoomCode[];
    /** (事前予約一覧)部屋名称 **/
    public String             BeforeReserveRoomName[];
    /** (事前予約一覧)部屋タイプコード **/
    public int                BeforeReserveTypeCode[];
    /** (事前予約一覧)部屋タイプ名称 **/
    public String             BeforeReserveTypeName[];
    /** (事前予約一覧)チェックイン可能日付 **/
    public int                BeforeReserveInPossibleDate[];
    /** (事前予約一覧)チェックイン可能時刻 **/
    public int                BeforeReserveInPossibleTime[];
    /** (事前予約一覧)チェックアウト可能日付 **/
    public int                BeforeReserveOutPossibleDate[];
    /** (事前予約一覧)チェックアウト可能時刻 **/
    public int                BeforeReserveOutPossibleTime[];
    /** (事前予約一覧)取消可能日付 **/
    public int                BeforeReserveCancelDate[];
    /** (事前予約一覧)取消可能時刻 **/
    public int                BeforeReserveCancelTime[];
    /** (事前予約一覧)予約日付 **/
    public int                BeforeReserveDate[];
    /** (事前予約一覧)予約時刻 **/
    public int                BeforeReserveTime[];
    /** (事前予約一覧)合計金額 **/
    public int                BeforeReserveTotal[];
    /** (事前予約一覧)予約番号 **/
    public int                BeforeReserveReserveNo[];
    /** (事前予約一覧)取消区分(1:取消可,2:取消不可) **/
    public int                BeforeReserveCancel[];
    /** (事前予約一覧)料金明細数 **/
    public int                BeforeReserveDetailCount[];
    /** (事前予約一覧)料金名称 **/
    public String             BeforeReserveDetailName[][];
    /** (事前予約一覧)金額 **/
    public int                BeforeReserveDetailPrice[][];

    /** (事前予約取消)カード番号 **/
    public String             BeforeCancelCardNo;
    /** (事前予約取消)予約番号 **/
    public int                BeforeCancelReserveNo;

    /** (駐車場情報)駐車場種別数 **/
    public int                ParkingKindCount;
    /** (駐車場情報)種別コード(MAX:128) **/
    public int                ParkingKindCode[];
    /** (駐車場情報)種別名称(MAX:128) **/
    public String             ParkingKindName[];
    /** (駐車場情報)空き数(MAX:128) **/
    public int                ParkingSpaceCount[];
    /** (駐車場一覧情報)駐車場種別数 **/
    public int                ParkingListCount;
    /** (駐車場一覧情報)駐車場番号(MAX:128) **/
    public String             ParkingListNo[];
    /** (駐車場一覧情報)種別コード(MAX:128) **/
    public int                ParkingListKindCode[];
    /** (駐車場一覧情報)種別名称(MAX:128) **/
    public String             ParkingListKindName[];
    /** (駐車場一覧情報)ステータス(MAX:128) **/
    public int                ParkingListStatus[];
    /** (駐車場一覧情報)部屋コード(MAX:128) **/
    public int                ParkingListRoomCode[];
    /** (駐車場一覧情報)部屋名称(MAX:128) **/
    public String             ParkingListRoomName[];
    /** (駐車場一覧情報)車番(MAX:128) **/
    public String             ParkingListCarNo[];

    /** (部屋情報一覧)部屋数 **/
    public int                InfoRoomListTotalRooms;
    /** (部屋情報一覧)部屋コード(MAX:128) **/
    public int                InfoRoomListRoomCode[];
    /** (部屋情報一覧)部屋名称(MAX:128) **/
    public String             InfoRoomListRoomName[];
    /** (部屋数情報一覧)部屋ステータス(MAX:128) **/
    public int                InfoRoomListRoomStatus[];

    /** (前受情報取得)チェックインコード **/
    public int                Seq;
    /** (前受情報取得)部屋コード **/
    public int                RoomCode;
    /** (前受情報取得)前受金額 **/
    public int                Deposit;
    /** (前受情報取得)請求金額 **/
    public int                Charge;

    /** ログ出力ライブラリ **/
    private LogLib            log;

    /**
     * 部屋関連情報データの初期化を行います。
     * 
     */
    public RoomInfo()
    {
        HotelId = "";
        CustomId = "0";
        Birthday1 = "0";
        Birthday2 = "0";
        UserId = "";
        Password = "";
        NickName = "";
        RoomEmpty = 0;
        RoomClean = 0;
        RoomCodeList = new int[ROOMINFO_ROOMMAX];
        RoomList = new String[ROOMINFO_ROOMMAX];
        RoomRankCodeList = new int[ROOMINFO_ROOMMAX];
        RoomRankList = new String[ROOMINFO_ROOMMAX];
        RoomCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomCleanList = new String[ROOMINFO_ROOMMAX];
        RoomRankCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomRankCleanList = new String[ROOMINFO_ROOMMAX];

        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;
        ReserveReqRoomCode = 0;
        ReserveMailAddress = "";
        ReserveResult = 0;
        ReserveRoomRankCount = 0;
        ReserveRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        ReserveRoomRankName = new String[ROOMINFO_ROOMRANKMAX];
        SimulateCheckinDate = 0;
        SimulateCheckinTime = 0;
        SimulateCheckoutDate = 0;
        SimulateCheckoutTime = 0;
        SimulateBudget = 0;
        SimulateRoomType = 0;
        SimulateUseTotal = 0;
        SimulateStayTime = 0;
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateRoomRankCount = 0;
        SimulateRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        SimulateRoomRankName = new String[ROOMINFO_ROOMRANKMAX];
        SimulateTimeOver = 0;
        TodayChargeRank = 0;
        TodayChargeRankName = "";

        BeforeAcceptInDate = 0;
        BeforeAcceptInTime = 0;
        BeforeAcceptOutDate = 0;
        BeforeAcceptOutTime = 0;

        BeforeListInDate = 0;
        BeforeListInTime = 0;
        BeforeListOutDate = 0;
        BeforeListOutTime = 0;
        BeforeListRoomType = 0;
        BeforeListRoomCount = 0;
        BeforeListRoomCode = new int[ROOMINFO_ROOMMAX];
        BeforeListRoomName = new String[ROOMINFO_ROOMMAX];
        BeforeListTypeCode = new int[ROOMINFO_ROOMMAX];
        BeforeListTypeName = new String[ROOMINFO_ROOMMAX];
        BeforeListInPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeListInPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeListOutPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeListOutPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeListTotal = new int[ROOMINFO_ROOMMAX];
        BeforeListDetailCount = new int[ROOMINFO_ROOMMAX];
        BeforeListDetailName = new String[ROOMINFO_ROOMMAX][10];
        BeforeListDetailPrice = new int[ROOMINFO_ROOMMAX][10];

        BeforeAuthInDate = 0;
        BeforeAuthInTime = 0;
        BeforeAuthOutDate = 0;
        BeforeAuthOutTime = 0;
        BeforeAuthRoomCode = 0;
        BeforeAuthRoomName = "";
        BeforeAuthTypeCode = 0;
        BeforeAuthTypeName = "";
        BeforeAuthTotal = 0;
        BeforeAuthCardNo = "";
        BeforeAuthExpire = 0;
        BeforeAuthName1 = "";
        BeforeAuthName2 = "";
        BeforeAuthSecurity = "";
        BeforeAuthCancelDate = 0;
        BeforeAuthCancelTime = 0;
        BeforeAuthReserveNo = 0;
        BeforeAuthApprove = "";
        BeforeAuthErrorCode = "";

        BeforeReserveCardNo = "";
        BeforeReserveNo = 0;
        BeforeReserveCount = 0;
        BeforeReserveRoomCode = new int[ROOMINFO_ROOMMAX];
        BeforeReserveRoomName = new String[ROOMINFO_ROOMMAX];
        BeforeReserveTypeCode = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTypeName = new String[ROOMINFO_ROOMMAX];
        BeforeReserveInPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveInPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveOutPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveOutPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancelDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancelTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTotal = new int[ROOMINFO_ROOMMAX];
        BeforeReserveReserveNo = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancel = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDetailCount = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDetailName = new String[ROOMINFO_ROOMMAX][10];
        BeforeReserveDetailPrice = new int[ROOMINFO_ROOMMAX][10];

        BeforeCancelCardNo = "";
        BeforeCancelReserveNo = 0;

        ParkingKindCount = 0;
        ParkingKindCode = new int[ROOMINFO_ROOMMAX];
        ParkingKindName = new String[ROOMINFO_ROOMMAX];
        ParkingSpaceCount = new int[ROOMINFO_ROOMMAX];
        ParkingListCount = 0;
        ParkingListNo = new String[ROOMINFO_ROOMMAX];
        ParkingListKindCode = new int[ROOMINFO_ROOMMAX];
        ParkingListKindName = new String[ROOMINFO_ROOMMAX];
        ParkingListStatus = new int[ROOMINFO_ROOMMAX];
        ParkingListRoomCode = new int[ROOMINFO_ROOMMAX];
        ParkingListRoomName = new String[ROOMINFO_ROOMMAX];
        ParkingListCarNo = new String[ROOMINFO_ROOMMAX];

        InfoRoomListTotalRooms = 0;
        InfoRoomListRoomCode = new int[ROOMINFO_ROOMMAX];
        InfoRoomListRoomName = new String[ROOMINFO_ROOMMAX];
        InfoRoomListRoomStatus = new int[ROOMINFO_ROOMMAX];

        Seq = 0;
        RoomCode = 0;
        Deposit = 0;
        Charge = 0;

        log = new LogLib();
    }

    // ------------------------------------------------------------------------------
    //
    // 電文処理メソッド
    //
    // ------------------------------------------------------------------------------
    /**
     * 電文送信処理(0000)
     * 料金シミュレーション（予算）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0000()
    {
        return(sendPacket0000Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0000)
     * 料金シミュレーション（予算）
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
     * 料金シミュレーション（予算）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0000Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0000";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // チェックイン日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // チェックイン時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // 予算
                nf = new DecimalFormat( "00000" );
                strData = nf.format( SimulateBudget );
                strSend = strSend + strData;
                // 部屋タイプ
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 入室日付
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // 入室時間
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // 退室日付
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // 退室時間
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // 利用金額
                            strData = new String( cRecv, 133, 5 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // 料金明細数
                            strData = new String( cRecv, 138, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // プラン名称
                                strData = new String( cRecv, 140 + (i * 25), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // プラン金額
                                strData = new String( cRecv, 160 + (i * 25), 5 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                            if ( SimulateCheckinDate == SimulateCheckoutDate && SimulateCheckinTime == SimulateCheckoutTime && SimulateUseTotal == 0 && SimulatePlanCount == 0 )
                            {
                                /*
                                 * NEOの場合、予算によるシミュレーションに失敗した場合、チェックアウト日時（SimulateCheckoutDate,SimulateCheckoutTime）にチェックイン日時と同じ値が入っている。
                                 * この場合、予算によるシミュレーションに失敗したとみなし、チェックアウト日時に0をセットする
                                 */

                                SimulateCheckoutDate = 0;
                                SimulateCheckoutTime = 0;
                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0000:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0002)
     * 料金シミュレーション（滞在時間）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0002()
    {
        return(sendPacket0002Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0002)
     * 料金シミュレーション（滞在時間）
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0002(int kind, String value)
    {
        return(sendPacket0002Sub( kind, value ));
    }

    /**
     * 電文送信処理(0002)
     * 料金シミュレーション（滞在時間）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0002Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0002";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // チェックイン日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // チェックイン時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // 滞在時間
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateStayTime );
                strSend = strSend + strData;
                // 部屋タイプ
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0003" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 入室日付
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // 入室時間
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // 退室日付
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // 退室時間
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // 利用金額
                            strData = new String( cRecv, 133, 5 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // 料金明細数
                            strData = new String( cRecv, 138, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // プラン名称
                                strData = new String( cRecv, 140 + (i * 25), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // プラン金額
                                strData = new String( cRecv, 160 + (i * 25), 5 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0002:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0004)
     * 料金シミュレーション（時間指定）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0004()
    {
        return(sendPacket0004Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0004)
     * 料金シミュレーション（時間指定）
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0004(int kind, String value)
    {
        return(sendPacket0004Sub( kind, value ));
    }

    /**
     * 電文送信処理(0004)
     * 料金シミュレーション（時間指定）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0004Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0004";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // チェックイン日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // チェックイン時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // チェックアウト日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckoutDate );
                strSend = strSend + strData;
                // チェックアウト時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckoutTime );
                strSend = strSend + strData;
                // 部屋タイプ
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0005" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 入室日付
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // 入室時間
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // 退室日付
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // 退室時間
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // 利用金額
                            strData = new String( cRecv, 133, 5 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // 料金明細数
                            strData = new String( cRecv, 138, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // プラン名称
                                strData = new String( cRecv, 140 + (i * 25), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // プラン金額
                                strData = new String( cRecv, 160 + (i * 25), 5 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0004:" + e.toString() );
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
     * 部屋ランク取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0006()
    {
        return(sendPacket0006Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0006)
     * 部屋ランク取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0006(int kind, String value)
    {
        return(sendPacket0006Sub( kind, value ));
    }

    /**
     * 電文送信処理(0006)
     * 部屋ランク取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0006Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        SimulateRoomRankCount = 0;
        SimulateRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        SimulateRoomRankName = new String[ROOMINFO_ROOMRANKMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0006";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋ランク明細数
                            strData = new String( cRecv, 109, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulateRoomRankCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 部屋ランクコード
                                strData = new String( cRecv, 111 + (i * 44), 4 );
                                SimulateRoomRankCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋ランク名称
                                strData = new String( cRecv, 115 + (i * 44), 40 );
                                SimulateRoomRankName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0006:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0008)
     * 拡張料金シミュレーション（予算）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0008()
    {
        return(sendPacket0008Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0008)
     * 料金シミュレーション（予算）
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0008(int kind, String value)
    {
        return(sendPacket0008Sub( kind, value ));
    }

    /**
     * 電文送信処理(0008)
     * 料金シミュレーション（予算）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0008Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0008";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // チェックイン日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // チェックイン時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // 予算
                nf = new DecimalFormat( "00000" );
                strData = nf.format( SimulateBudget );
                strSend = strSend + strData;
                // 部屋タイプ
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0009" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 入室日付
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // 入室時間
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // 退室日付
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // 退室時間
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // 利用金額
                            strData = new String( cRecv, 133, 5 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // 時間超過フラグ
                            strData = new String( cRecv, 138, 1 );
                            SimulateTimeOver = Integer.valueOf( strData ).intValue();

                            // 料金明細数
                            strData = new String( cRecv, 139, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // プラン名称
                                strData = new String( cRecv, 141 + (i * 25), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // プラン金額
                                strData = new String( cRecv, 161 + (i * 25), 5 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                            if ( SimulateCheckinDate == SimulateCheckoutDate && SimulateCheckinTime == SimulateCheckoutTime && SimulateUseTotal == 0 && SimulatePlanCount == 0 )
                            {
                                /*
                                 * NEOの場合、予算によるシミュレーションに失敗した場合、チェックアウト日時（SimulateCheckoutDate,SimulateCheckoutTime）にチェックイン日時と同じ値が入っている。
                                 * この場合、予算によるシミュレーションに失敗したとみなし、チェックアウト日時に0をセットする
                                 */

                                SimulateCheckoutDate = 0;
                                SimulateCheckoutTime = 0;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0008:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0010)
     * 予約可能ルーム一覧取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0010()
    {
        return(sendPacket0010Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0010)
     * 予約可能ルーム一覧取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0010(int kind, String value)
    {
        return(sendPacket0010Sub( kind, value ));
    }

    /**
     * 電文送信処理(0010)
     * 予約可能ルーム一覧取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0010Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        RoomEmpty = 0;
        RoomClean = 0;
        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0010";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0011" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 空室部屋数
                            strData = new String( cRecv, 109, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // 準備中数
                            strData = new String( cRecv, 112, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();

                            // 予約可能部屋数
                            strData = new String( cRecv, 115, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ReserveListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 118 + (i * 11), 3 );
                                ReserveListCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 121 + (i * 11), 8 );
                                ReserveList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0010:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0012)
     * 予約申し込み確認
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0012()
    {
        return(sendPacket0012Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0012)
     * 予約申し込み確認
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0012(int kind, String value)
    {
        return(sendPacket0012Sub( kind, value ));
    }

    /**
     * 電文送信処理(0012)
     * 予約申し込み確認
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0012Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveMailAddress = "";
        ReserveResult = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0012";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                strData = nf.format( ReserveReqRoomCode );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0013" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールアドレス
                            strData = new String( cRecv, 109, 63 );
                            ReserveMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋コード
                            strData = new String( cRecv, 172, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // 部屋名称
                            strData = new String( cRecv, 175, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 処理結果
                            strData = new String( cRecv, 183, 2 );
                            ReserveResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0012:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0014)
     * 予約申し込み
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0014()
    {
        return(sendPacket0014Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0014)
     * 予約申し込み
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0014(int kind, String value)
    {
        return(sendPacket0014Sub( kind, value ));
    }

    /**
     * 電文送信処理(0014)
     * 予約申し込み
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0014Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveMailAddress = "";
        ReserveResult = 0;
        ReserveNo = 0;
        ReserveLimitTime = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0014";
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                strData = nf.format( ReserveReqRoomCode );
                strSend = strSend + strData;
                // メールアドレス
                strData = format.leftFitFormat( ReserveMailAddress, 63 );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0015" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールアドレス
                            strData = new String( cRecv, 109, 63 );
                            ReserveMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 予約部屋コード
                            strData = new String( cRecv, 172, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // 予約部屋名称
                            strData = new String( cRecv, 175, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 予約番号
                            strData = new String( cRecv, 183, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // 予約有効時刻
                            strData = new String( cRecv, 191, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0014:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0016)
     * 予約確認
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0016()
    {
        return(sendPacket0016Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0016)
     * 予約確認
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0016(int kind, String value)
    {
        return(sendPacket0016Sub( kind, value ));
    }

    /**
     * 電文送信処理(0016)
     * 予約確認
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0016Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0016";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0017" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 予約部屋コード
                            strData = new String( cRecv, 109, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // 予約部屋名称
                            strData = new String( cRecv, 112, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 予約番号
                            strData = new String( cRecv, 120, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // 予約有効時刻
                            strData = new String( cRecv, 128, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0016:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0018)
     * 予約変更
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0018()
    {
        return(sendPacket0018Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0018)
     * 予約変更
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0018(int kind, String value)
    {
        return(sendPacket0018Sub( kind, value ));
    }

    /**
     * 電文送信処理(0018)
     * 予約変更
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0018Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;
        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0018";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0019" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋コード
                            strData = new String( cRecv, 109, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // 部屋名称
                            strData = new String( cRecv, 112, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 予約番号
                            strData = new String( cRecv, 120, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // 予約有効時刻
                            strData = new String( cRecv, 128, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();

                            // 空室部屋数
                            strData = new String( cRecv, 132, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // 準備中数
                            strData = new String( cRecv, 135, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();

                            // 予約可能部屋数
                            strData = new String( cRecv, 138, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ReserveListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 141 + (i * 11), 3 );
                                ReserveListCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 144 + (i * 11), 8 );
                                ReserveList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0018:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0020)
     * 予約変更確認
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0020()
    {
        return(sendPacket0020Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0020)
     * 予約変更確認
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0020(int kind, String value)
    {
        return(sendPacket0020Sub( kind, value ));
    }

    /**
     * 電文送信処理(0020)
     * 予約変更確認
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0020Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveMailAddress = "";
        ReserveResult = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0020";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                strData = nf.format( ReserveReqRoomCode );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0021" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールアドレス
                            strData = new String( cRecv, 109, 63 );
                            ReserveMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋コード
                            strData = new String( cRecv, 172, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // 部屋名称
                            strData = new String( cRecv, 175, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 処理結果
                            strData = new String( cRecv, 183, 2 );
                            ReserveResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0020:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0022)
     * 予約取消確認
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0022()
    {
        return(sendPacket0022Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0022)
     * 予約取消確認
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0022(int kind, String value)
    {
        return(sendPacket0022Sub( kind, value ));
    }

    /**
     * 電文送信処理(0022)
     * 予約取消確認
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0022Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0022";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0023" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 予約部屋コード
                            strData = new String( cRecv, 109, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // 予約部屋名称
                            strData = new String( cRecv, 112, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 予約番号
                            strData = new String( cRecv, 120, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // 予約有効時刻
                            strData = new String( cRecv, 128, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0022:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0024)
     * 予約取消
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0024()
    {
        return(sendPacket0024Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0024)
     * 予約取消
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0024(int kind, String value)
    {
        return(sendPacket0024Sub( kind, value ));
    }

    /**
     * 電文送信処理(0024)
     * 予約取消
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0024Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveMailAddress = "";
        ReserveResult = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0024";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                strData = nf.format( ReserveReqRoomCode );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0025" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールアドレス
                            strData = new String( cRecv, 109, 63 );
                            ReserveMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋コード
                            strData = new String( cRecv, 172, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // 部屋名称
                            strData = new String( cRecv, 175, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 処理結果
                            strData = new String( cRecv, 183, 2 );
                            ReserveResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0024:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0026)
     * ランク別予約可能ルーム取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0026()
    {
        return(sendPacket0026Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0026)
     * ランク別予約可能ルーム取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0026(int kind, String value)
    {
        return(sendPacket0026Sub( kind, value ));
    }

    /**
     * 電文送信処理(0026)
     * ランク別予約可能ルーム取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0026Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        RoomEmpty = 0;
        RoomClean = 0;
        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];
        ReserveRoomRankCount = 0;
        ReserveRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        ReserveRoomRankName = new String[ROOMINFO_ROOMRANKMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0026";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0027" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 空室部屋数
                            strData = new String( cRecv, 109, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // 準備中数
                            strData = new String( cRecv, 112, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();

                            // 予約可能部屋数
                            strData = new String( cRecv, 115, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ReserveListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 部屋ランクコード
                                strData = new String( cRecv, 118 + (i * 53), 2 );
                                ReserveRoomRankCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋ランク名称
                                strData = new String( cRecv, 120 + (i * 53), 40 );
                                ReserveRoomRankName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 部屋コード
                                strData = new String( cRecv, 160 + (i * 53), 3 );
                                ReserveListCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 163 + (i * 53), 8 );
                                ReserveList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0026:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0028)
     * ランク別予約変更
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0028()
    {
        return(sendPacket0028Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0028)
     * ランク別予約変更
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0028(int kind, String value)
    {
        return(sendPacket0028Sub( kind, value ));
    }

    /**
     * 電文送信処理(0028)
     * ランク別予約変更
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0028Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        RoomEmpty = 0;
        RoomClean = 0;
        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];
        ReserveRoomRankCount = 0;
        ReserveRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        ReserveRoomRankName = new String[ROOMINFO_ROOMRANKMAX];
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0028";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0029" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋コード
                            strData = new String( cRecv, 109, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // 部屋名称
                            strData = new String( cRecv, 112, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 予約番号
                            strData = new String( cRecv, 120, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // 予約有効時刻
                            strData = new String( cRecv, 128, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();

                            // 空室部屋数
                            strData = new String( cRecv, 132, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // 準備中数
                            strData = new String( cRecv, 135, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();

                            // 予約可能部屋数
                            strData = new String( cRecv, 138, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ReserveListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 部屋ランクコード
                                strData = new String( cRecv, 141 + (i * 53), 2 );
                                ReserveRoomRankCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋ランク名称
                                strData = new String( cRecv, 143 + (i * 53), 40 );
                                ReserveRoomRankName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 部屋コード
                                strData = new String( cRecv, 183 + (i * 53), 3 );
                                ReserveListCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 186 + (i * 53), 8 );
                                ReserveList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0028:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0030)
     * 事前予約可能時間
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0030()
    {
        return(sendPacket0030Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0030)
     * 事前予約可能時間
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0030(int kind, String value)
    {
        return(sendPacket0030Sub( kind, value ));
    }

    /**
     * 電文送信処理(0030)
     * 事前予約可能時間
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0030Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // データのクリア
        Result = 0;
        BeforeAcceptInDate = 0;
        BeforeAcceptInTime = 0;
        BeforeAcceptOutDate = 0;
        BeforeAcceptOutTime = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0030";

                for( i = 0 ; i < 10 ; i++ )
                {
                    // 予備
                    strSend = strSend + " ";
                }

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0031" ) == 0 )
                        {
                            // 処理結果
                            strData = new String( cRecv, 36, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // チェックイン可能日付
                            strData = new String( cRecv, 38, 8 );
                            BeforeAcceptInDate = Integer.valueOf( strData ).intValue();

                            // チェックイン可能時刻
                            strData = new String( cRecv, 46, 4 );
                            BeforeAcceptInTime = Integer.valueOf( strData ).intValue();

                            // チェックアウト可能日付
                            strData = new String( cRecv, 50, 8 );
                            BeforeAcceptOutDate = Integer.valueOf( strData ).intValue();

                            // チェックアウト可能時刻
                            strData = new String( cRecv, 58, 4 );
                            BeforeAcceptOutTime = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0030:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0032)
     * 事前予約可能部屋一覧
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0032()
    {
        return(sendPacket0032Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0032)
     * 事前予約可能部屋一覧
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0032(int kind, String value)
    {
        return(sendPacket0032Sub( kind, value ));
    }

    /**
     * 電文送信処理(0032)
     * 事前予約可能部屋一覧
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0032Sub(int kind, String value)
    {
        int i;
        int j;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        BeforeListRoomCount = 0;
        BeforeListRoomCode = new int[ROOMINFO_ROOMMAX];
        BeforeListRoomName = new String[ROOMINFO_ROOMMAX];
        BeforeListTypeCode = new int[ROOMINFO_ROOMMAX];
        BeforeListTypeName = new String[ROOMINFO_ROOMMAX];
        BeforeListInPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeListInPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeListOutPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeListOutPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeListTotal = new int[ROOMINFO_ROOMMAX];
        BeforeListDetailCount = new int[ROOMINFO_ROOMMAX];
        BeforeListDetailName = new String[ROOMINFO_ROOMMAX][10];
        BeforeListDetailPrice = new int[ROOMINFO_ROOMMAX][10];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0032";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // チェックイン日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( BeforeListInDate );
                strSend = strSend + strData;
                // チェックイン時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeListInTime );
                strSend = strSend + strData;
                // チェックアウト日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( BeforeListOutDate );
                strSend = strSend + strData;
                // チェックアウト時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeListOutTime );
                strSend = strSend + strData;
                // 部屋タイプ
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeListRoomType );
                strSend = strSend + strData;

                for( i = 0 ; i < 82 ; i++ )
                {
                    // 予備
                    strSend = strSend + " ";
                }

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0033" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 処理結果
                            strData = new String( cRecv, 109, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // 予約可能部屋数
                            strData = new String( cRecv, 111, 3 );
                            BeforeListRoomCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < BeforeListRoomCount ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 114 + (i * 400), 3 );
                                BeforeListRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 117 + (i * 400), 8 );
                                BeforeListRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 部屋ランクコード
                                strData = new String( cRecv, 125 + (i * 400), 2 );
                                BeforeListTypeCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋ランク名称
                                strData = new String( cRecv, 129 + (i * 400), 40 );
                                BeforeListTypeName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // チェックイン可能日付
                                strData = new String( cRecv, 169 + (i * 400), 8 );
                                BeforeListInPossibleDate[i] = Integer.valueOf( strData ).intValue();

                                // チェックイン可能時刻
                                strData = new String( cRecv, 177 + (i * 400), 4 );
                                BeforeListInPossibleTime[i] = Integer.valueOf( strData ).intValue();

                                // チェックアウト可能日付
                                strData = new String( cRecv, 181 + (i * 400), 8 );
                                BeforeListOutPossibleDate[i] = Integer.valueOf( strData ).intValue();

                                // チェックアウト可能時刻
                                strData = new String( cRecv, 189 + (i * 400), 4 );
                                BeforeListOutPossibleTime[i] = Integer.valueOf( strData ).intValue();

                                // 合計金額
                                strData = new String( cRecv, 193 + (i * 400), 9 );
                                BeforeListTotal[i] = Integer.valueOf( strData ).intValue();

                                // 予備20BYTE

                                // 料金明細数
                                strData = new String( cRecv, 222 + (i * 400), 2 );
                                BeforeListDetailCount[i] = Integer.valueOf( strData ).intValue();

                                for( j = 0 ; j < 10 ; j++ )
                                {
                                    // 料金名称
                                    strData = new String( cRecv, 224 + (i * 400) + (j * 29), 20 );
                                    BeforeListDetailName[i][j] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                    // 料金明細数
                                    strData = new String( cRecv, 244 + (i * 400) + (j * 29), 9 );
                                    BeforeListDetailPrice[i][j] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0032:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0034)
     * 事前予約承認
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0034()
    {
        return(sendPacket0034Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0034)
     * 事前予約承認
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0034(int kind, String value)
    {
        return(sendPacket0034Sub( kind, value ));
    }

    /**
     * 電文送信処理(0034)
     * 事前予約承認
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0034Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        BeforeAuthCancelDate = 0;
        BeforeAuthCancelTime = 0;
        BeforeAuthReserveNo = 0;
        BeforeAuthApprove = "";
        BeforeAuthErrorCode = "";

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0034";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // チェックイン日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( BeforeAuthInDate );
                strSend = strSend + strData;
                // チェックイン時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeAuthInTime );
                strSend = strSend + strData;
                // チェックアウト日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( BeforeAuthOutDate );
                strSend = strSend + strData;
                // チェックアウト時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeAuthOutTime );
                strSend = strSend + strData;
                // 部屋部屋コード
                nf = new DecimalFormat( "000" );
                strData = nf.format( BeforeAuthRoomCode );
                strSend = strSend + strData;
                // 合計金額
                nf = new DecimalFormat( "000000000" );
                strData = nf.format( BeforeAuthTotal );
                strSend = strSend + strData;
                // カード番号
                strSend = strSend + format.leftFitFormat( BeforeAuthCardNo, 19 );
                // 有効期限
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeAuthExpire );
                strSend = strSend + strData;
                // 名前（性）
                strSend = strSend + format.leftFitFormat( BeforeAuthName1, 16 );
                // 名前（名）
                strSend = strSend + format.leftFitFormat( BeforeAuthName2, 16 );
                // セキュリティコード
                strSend = strSend + format.leftFitFormat( BeforeAuthSecurity, 4 );

                for( i = 0 ; i < 15 ; i++ )
                {
                    // 予備
                    strSend = strSend + " ";
                }

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0035" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 処理結果
                            strData = new String( cRecv, 109, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // 部屋コード
                            strData = new String( cRecv, 111, 3 );
                            BeforeAuthRoomCode = Integer.valueOf( strData ).intValue();

                            // 部屋名称
                            strData = new String( cRecv, 114, 8 );
                            BeforeAuthRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋ランクコード
                            strData = new String( cRecv, 122, 4 );
                            BeforeAuthTypeCode = Integer.valueOf( strData ).intValue();

                            // 部屋ランク名称
                            strData = new String( cRecv, 126, 40 );
                            BeforeAuthTypeName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // チェックイン日付
                            strData = new String( cRecv, 166, 8 );
                            BeforeAuthInDate = Integer.valueOf( strData ).intValue();

                            // チェックイン時刻
                            strData = new String( cRecv, 174, 4 );
                            BeforeAuthInTime = Integer.valueOf( strData ).intValue();

                            // チェックアウト日付
                            strData = new String( cRecv, 178, 8 );
                            BeforeAuthOutDate = Integer.valueOf( strData ).intValue();

                            // チェックアウト時刻
                            strData = new String( cRecv, 186, 4 );
                            BeforeAuthOutTime = Integer.valueOf( strData ).intValue();

                            // 取消可能日付
                            strData = new String( cRecv, 190, 8 );
                            BeforeAuthCancelDate = Integer.valueOf( strData ).intValue();

                            // 取消可能時刻
                            strData = new String( cRecv, 198, 4 );
                            BeforeAuthCancelTime = Integer.valueOf( strData ).intValue();

                            // 合計金額
                            strData = new String( cRecv, 202, 9 );
                            BeforeAuthTotal = Integer.valueOf( strData ).intValue();

                            // 予約番号
                            strData = new String( cRecv, 211, 9 );
                            BeforeAuthReserveNo = Integer.valueOf( strData ).intValue();

                            // カード番号
                            strData = new String( cRecv, 220, 19 );
                            BeforeAuthCardNo = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 承認番号
                            strData = new String( cRecv, 239, 7 );
                            BeforeAuthApprove = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // エラーコード
                            strData = new String( cRecv, 246, 3 );
                            BeforeAuthErrorCode = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0034:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0036)
     * 事前予約一覧
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0036()
    {
        return(sendPacket0036Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0036)
     * 事前予約一覧
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0036(int kind, String value)
    {
        return(sendPacket0036Sub( kind, value ));
    }

    /**
     * 電文送信処理(0036)
     * 事前予約一覧
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0036Sub(int kind, String value)
    {
        int i;
        int j;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        BeforeReserveCount = 0;
        BeforeReserveRoomCode = new int[ROOMINFO_ROOMMAX];
        BeforeReserveRoomName = new String[ROOMINFO_ROOMMAX];
        BeforeReserveTypeCode = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTypeName = new String[ROOMINFO_ROOMMAX];
        BeforeReserveInPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveInPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveOutPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveOutPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancelDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancelTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTotal = new int[ROOMINFO_ROOMMAX];
        BeforeReserveReserveNo = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancel = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDetailCount = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDetailName = new String[ROOMINFO_ROOMMAX][10];
        BeforeReserveDetailPrice = new int[ROOMINFO_ROOMMAX][10];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0036";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // カード番号
                strSend = strSend + format.leftFitFormat( BeforeReserveCardNo, 19 );
                // 予約番号
                nf = new DecimalFormat( "000000000" );
                strData = nf.format( BeforeReserveNo );
                strSend = strSend + strData;

                for( i = 0 ; i < 82 ; i++ )
                {
                    // 予備
                    strSend = strSend + " ";
                }

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0037" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 処理結果
                            strData = new String( cRecv, 109, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // 予約数
                            strData = new String( cRecv, 111, 3 );
                            BeforeReserveCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < BeforeReserveCount ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 114 + (i * 500), 3 );
                                BeforeReserveRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 117 + (i * 500), 8 );
                                BeforeReserveRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 部屋ランクコード
                                strData = new String( cRecv, 125 + (i * 500), 4 );
                                BeforeReserveTypeCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋ランク名称
                                strData = new String( cRecv, 129 + (i * 500), 40 );
                                BeforeReserveTypeName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // チェックイン日付
                                strData = new String( cRecv, 169 + (i * 500), 8 );
                                BeforeReserveInPossibleDate[i] = Integer.valueOf( strData ).intValue();

                                // チェックイン時刻
                                strData = new String( cRecv, 177 + (i * 500), 4 );
                                BeforeReserveInPossibleTime[i] = Integer.valueOf( strData ).intValue();

                                // チェックアウト日付
                                strData = new String( cRecv, 181 + (i * 500), 8 );
                                BeforeReserveOutPossibleDate[i] = Integer.valueOf( strData ).intValue();

                                // チェックアウト時刻
                                strData = new String( cRecv, 189 + (i * 500), 4 );
                                BeforeReserveOutPossibleTime[i] = Integer.valueOf( strData ).intValue();

                                // 取消可能日付
                                strData = new String( cRecv, 193 + (i * 500), 8 );
                                BeforeReserveCancelDate[i] = Integer.valueOf( strData ).intValue();

                                // 取消可能時刻
                                strData = new String( cRecv, 201 + (i * 500), 4 );
                                BeforeReserveCancelTime[i] = Integer.valueOf( strData ).intValue();

                                // 予約日付
                                strData = new String( cRecv, 205 + (i * 500), 8 );
                                BeforeReserveDate[i] = Integer.valueOf( strData ).intValue();

                                // 予約時刻
                                strData = new String( cRecv, 213 + (i * 500), 4 );
                                BeforeReserveTime[i] = Integer.valueOf( strData ).intValue();

                                // 合計金額
                                strData = new String( cRecv, 217 + (i * 500), 9 );
                                BeforeReserveTotal[i] = Integer.valueOf( strData ).intValue();

                                // 予約番号
                                strData = new String( cRecv, 226 + (i * 500), 9 );
                                BeforeReserveReserveNo[i] = Integer.valueOf( strData ).intValue();

                                // 取消区分
                                strData = new String( cRecv, 235 + (i * 500), 1 );
                                BeforeReserveCancel[i] = Integer.valueOf( strData ).intValue();

                                // 予備86BYTE

                                // 料金明細数
                                strData = new String( cRecv, 322 + (i * 500), 2 );
                                BeforeReserveDetailCount[i] = Integer.valueOf( strData ).intValue();

                                for( j = 0 ; j < 10 ; j++ )
                                {
                                    // 料金名称
                                    strData = new String( cRecv, 324 + (i * 500) + (j * 29), 20 );
                                    BeforeReserveDetailName[i][j] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                    // 料金明細数
                                    strData = new String( cRecv, 344 + (i * 500) + (j * 29), 9 );
                                    BeforeReserveDetailPrice[i][j] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0036:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0036)
     * 事前予約取消
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0038()
    {
        return(sendPacket0038Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0038)
     * 事前予約取消
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0038(int kind, String value)
    {
        return(sendPacket0038Sub( kind, value ));
    }

    /**
     * 電文送信処理(0038)
     * 事前予約取消
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0038Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0038";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // カード番号
                strSend = strSend + format.leftFitFormat( BeforeCancelCardNo, 19 );
                // 予約番号
                nf = new DecimalFormat( "000000000" );
                strData = nf.format( BeforeCancelReserveNo );
                strSend = strSend + strData;

                for( i = 0 ; i < 82 ; i++ )
                {
                    // 予備
                    strSend = strSend + " ";
                }

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0039" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 処理結果
                            strData = new String( cRecv, 109, 2 );
                            Result = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0038:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0040)
     * 6桁料金シミュレーション（予算）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0040()
    {
        return(sendPacket0040Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0040)
     * 6桁料金シミュレーション（予算）
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0040(int kind, String value)
    {
        return(sendPacket0040Sub( kind, value ));
    }

    /**
     * 電文送信処理(0040)
     * 6桁料金シミュレーション（予算）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0040Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0040";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // チェックイン日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // チェックイン時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // 予算
                nf = new DecimalFormat( "000000" );
                strData = nf.format( SimulateBudget );
                strSend = strSend + strData;
                // 部屋タイプ
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0041" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 入室日付
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // 入室時間
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // 退室日付
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // 退室時間
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // 利用金額
                            strData = new String( cRecv, 133, 6 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // 時間超過フラグ
                            strData = new String( cRecv, 139, 1 );
                            SimulateTimeOver = Integer.valueOf( strData ).intValue();

                            // 料金明細数
                            strData = new String( cRecv, 140, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // プラン名称
                                strData = new String( cRecv, 142 + (i * 26), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // プラン金額
                                strData = new String( cRecv, 162 + (i * 26), 6 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                            if ( SimulateCheckinDate == SimulateCheckoutDate && SimulateCheckinTime == SimulateCheckoutTime && SimulateUseTotal == 0 && SimulatePlanCount == 0 )
                            {
                                /*
                                 * NEOの場合、予算によるシミュレーションに失敗した場合、チェックアウト日時（SimulateCheckoutDate,SimulateCheckoutTime）にチェックイン日時と同じ値が入っている。
                                 * この場合、予算によるシミュレーションに失敗したとみなし、チェックアウト日時に0をセットする
                                 */

                                SimulateCheckoutDate = 0;
                                SimulateCheckoutTime = 0;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0040:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0042)
     * 6桁料金シミュレーション（滞在時間）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0042()
    {
        return(sendPacket0042Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0042)
     * 6桁料金シミュレーション（滞在時間）
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0042(int kind, String value)
    {
        return(sendPacket0042Sub( kind, value ));
    }

    /**
     * 電文送信処理(0042)
     * 6桁料金シミュレーション（滞在時間）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0042Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0042";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // チェックイン日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // チェックイン時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // 滞在時間
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateStayTime );
                strSend = strSend + strData;
                // 部屋タイプ
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0043" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 入室日付
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // 入室時間
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // 退室日付
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // 退室時間
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // 利用金額
                            strData = new String( cRecv, 133, 6 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // 料金明細数
                            strData = new String( cRecv, 139, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // プラン名称
                                strData = new String( cRecv, 141 + (i * 26), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // プラン金額
                                strData = new String( cRecv, 161 + (i * 26), 6 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0042:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0044)
     * 6桁料金シミュレーション（時間指定）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0044()
    {
        return(sendPacket0044Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0044)
     * 6桁料金シミュレーション（時間指定）
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0044(int kind, String value)
    {
        return(sendPacket0044Sub( kind, value ));
    }

    /**
     * 電文送信処理(0044)
     * 6桁料金シミュレーション（時間指定）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0044Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0044";
                // 顧客番号
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // 誕生日（日）
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ユーザID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // パスワード
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // チェックイン日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // チェックイン時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // チェックアウト日付
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckoutDate );
                strSend = strSend + strData;
                // チェックアウト時間
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckoutTime );
                strSend = strSend + strData;
                // 部屋タイプ
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0045" ) == 0 )
                        {
                            // ニックネーム
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 入室日付
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // 入室時間
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // 退室日付
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // 退室時間
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // 利用金額
                            strData = new String( cRecv, 133, 6 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // 料金明細数
                            strData = new String( cRecv, 139, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // プラン名称
                                strData = new String( cRecv, 141 + (i * 26), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // プラン金額
                                strData = new String( cRecv, 161 + (i * 26), 6 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0044:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0200)
     * 空室情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0200()
    {
        return(sendPacket0200Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0200)
     * 空室情報の取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0200(int kind, String value)
    {
        return(sendPacket0200Sub( kind, value ));
    }

    /**
     * 電文送信処理(0200)
     * 空室情報の取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0200Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv = null;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // データのクリア
        RoomEmpty = 0;
        RoomClean = 0;

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0200";
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0201" ) == 0 )
                        {
                            // 空室数
                            strData = new String( cRecv, 36, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // 準備中
                            strData = new String( cRecv, 39, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();
                        }
                        else
                        {
                            RoomEmpty = 0;
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0200:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                if ( strRecv != null )
                {
                    return(true);
                }
                else
                {
                    return(false);
                }
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0202)
     * 空室一覧情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0202()
    {
        return(sendPacket0202Sub( 0, "", "" ));
    }

    /**
     * 電文送信処理Keep対象部屋取得(0202)
     * 空室一覧情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0202ForKeep()
    {
        return(sendPacket0202Sub( 0, "", "K" ));
    }

    /**
     * 電文送信処理(0202)
     * 空室一覧情報の取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0202(int kind, String value)
    {
        return(sendPacket0202Sub( kind, value, "" ));
    }

    /**
     * 電文送信処理(0202)
     * 空室一覧情報の取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0202Sub(int kind, String value, String requestKind)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv = null;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // データのクリア
        RoomEmpty = 0;
        RoomClean = 0;
        RoomCodeList = new int[ROOMINFO_ROOMMAX];
        RoomList = new String[ROOMINFO_ROOMMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0202";
                // 予備
                strSend = strSend + requestKind + "         ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0203" ) == 0 )
                        {
                            // 総部屋数
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            RoomEmpty = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 39 + (i * 11), 3 );
                                RoomCodeList[i] = Integer.valueOf( strData ).intValue();
                                // 部屋名称
                                strData = new String( cRecv, 42 + (i * 11), 8 );
                                RoomList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            RoomEmpty = 0;
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0202:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                if ( strRecv != null )
                {
                    return(true);
                }
                else
                {
                    return(false);
                }
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0204)
     * 部屋ランク別空室一覧情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0204()
    {
        return(sendPacket0204Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0204)
     * 部屋ランク別空室一覧情報の取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0204(int kind, String value)
    {
        return(sendPacket0204Sub( kind, value ));
    }

    /**
     * 電文送信処理(0204)
     * 部屋ランク別空室一覧情報の取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0204Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // データのクリア
        RoomEmpty = 0;
        RoomClean = 0;
        RoomCodeList = new int[ROOMINFO_ROOMMAX];
        RoomList = new String[ROOMINFO_ROOMMAX];
        RoomRankCodeList = new int[ROOMINFO_ROOMMAX];
        RoomRankList = new String[ROOMINFO_ROOMMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0204";
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0205" ) == 0 )
                        {
                            // 総部屋数
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            RoomEmpty = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 部屋ランクコード
                                strData = new String( cRecv, 39 + (i * 53), 2 );
                                RoomRankCodeList[i] = Integer.valueOf( strData ).intValue();

                                // 部屋ランク名称
                                strData = new String( cRecv, 41 + (i * 53), 40 );
                                RoomRankList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 部屋コード
                                strData = new String( cRecv, 81 + (i * 53), 3 );
                                RoomCodeList[i] = Integer.valueOf( strData ).intValue();
                                // 部屋名称
                                strData = new String( cRecv, 84 + (i * 53), 8 );
                                RoomList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            RoomEmpty = 0;
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0204:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                if ( strRecv != null )
                {
                    return(true);
                }
                else
                {
                    return(false);
                }
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0206)
     * 準備中一覧情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0206()
    {
        return(sendPacket0206Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0206)
     * 準備中一覧情報の取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0206(int kind, String value)
    {
        return(sendPacket0206Sub( kind, value ));
    }

    /**
     * 電文送信処理(0206)
     * 空室一覧情報の取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0206Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // データのクリア
        RoomClean = 0;
        RoomCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomCleanList = new String[ROOMINFO_ROOMMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0206";
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0207" ) == 0 )
                        {
                            // 総部屋数
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            RoomClean = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 39 + (i * 11), 3 );
                                RoomCodeCleanList[i] = Integer.valueOf( strData ).intValue();
                                // 部屋名称
                                strData = new String( cRecv, 42 + (i * 11), 8 );
                                RoomCleanList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0206:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0208)
     * 部屋ランク別準備中一覧情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0208()
    {
        return(sendPacket0208Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0208)
     * 部屋ランク別準備中一覧情報の取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0208(int kind, String value)
    {
        return(sendPacket0208Sub( kind, value ));
    }

    /**
     * 電文送信処理(0208)
     * 部屋ランク別準備中一覧情報の取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0208Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // データのクリア
        RoomClean = 0;
        RoomCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomCleanList = new String[ROOMINFO_ROOMMAX];
        RoomRankCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomRankCleanList = new String[ROOMINFO_ROOMMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0208";
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0209" ) == 0 )
                        {
                            // 総部屋数
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            RoomClean = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 部屋ランクコード
                                strData = new String( cRecv, 39 + (i * 53), 2 );
                                RoomRankCodeCleanList[i] = Integer.valueOf( strData ).intValue();

                                // 部屋ランク名称
                                strData = new String( cRecv, 41 + (i * 53), 40 );
                                RoomRankCleanList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 部屋コード
                                strData = new String( cRecv, 81 + (i * 53), 3 );
                                RoomCodeCleanList[i] = Integer.valueOf( strData ).intValue();
                                // 部屋名称
                                strData = new String( cRecv, 84 + (i * 53), 8 );
                                RoomCleanList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0208:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0210)
     * 駐車場空き情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0210()
    {
        return(sendPacket0210Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0210)
     * 駐車場空き情報の取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0210(int kind, String value)
    {
        return(sendPacket0210Sub( kind, value ));
    }

    /**
     * 電文送信処理(0210)
     * 駐車場空き情報の取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0210Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // データのクリア
        ParkingKindCount = 0;
        ParkingKindCode = new int[ROOMINFO_ROOMMAX];
        ParkingKindName = new String[ROOMINFO_ROOMMAX];
        ParkingSpaceCount = new int[ROOMINFO_ROOMMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0210";
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0211" ) == 0 )
                        {
                            // 駐車場種別数
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ParkingKindCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 種別コード
                                strData = new String( cRecv, 39 + (i * 53), 2 );
                                ParkingKindCode[i] = Integer.valueOf( strData ).intValue();
                                // 種別名称
                                strData = new String( cRecv, 41 + (i * 53), 40 );
                                ParkingKindName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // 空き数
                                strData = new String( cRecv, 81 + (i * 53), 3 );
                                ParkingSpaceCount[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                        else
                        {
                            ParkingKindCount = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0210:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0212)
     * 駐車場情報一覧情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0212()
    {
        return(sendPacket0212Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0212)
     * 駐車場情報一覧情報の取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0212(int kind, String value)
    {
        return(sendPacket0212Sub( kind, value ));
    }

    /**
     * 電文送信処理(0212)
     * 駐車場情報一覧情報の取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0212Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // データのクリア
        ParkingListCount = 0;
        ParkingListNo = new String[ROOMINFO_ROOMMAX];
        ParkingListKindCode = new int[ROOMINFO_ROOMMAX];
        ParkingListKindName = new String[ROOMINFO_ROOMMAX];
        ParkingListStatus = new int[ROOMINFO_ROOMMAX];
        ParkingListRoomCode = new int[ROOMINFO_ROOMMAX];
        ParkingListRoomName = new String[ROOMINFO_ROOMMAX];
        ParkingListCarNo = new String[ROOMINFO_ROOMMAX];

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0212";
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0213" ) == 0 )
                        {
                            // 駐車場数
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ParkingListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // 駐車場番号
                                strData = new String( cRecv, 39 + (i * 128), 8 );
                                ParkingListNo[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 種別コード
                                strData = new String( cRecv, 47 + (i * 128), 2 );
                                ParkingListKindCode[i] = Integer.valueOf( strData ).intValue();

                                // 種別名称
                                strData = new String( cRecv, 49 + (i * 128), 40 );
                                ParkingListKindName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ステータス
                                strData = new String( cRecv, 89 + (i * 128), 1 );
                                ParkingListStatus[i] = Integer.valueOf( strData ).intValue();

                                // 部屋コード
                                strData = new String( cRecv, 90 + (i * 128), 3 );
                                ParkingListRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 93 + (i * 128), 8 );
                                ParkingListRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 車番
                                strData = new String( cRecv, 101 + (i * 128), 40 );
                                ParkingListCarNo[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            ParkingListCount = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0212:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0214)
     * 部屋情報一覧情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0214()
    {
        return(sendPacket0214Sub( 0, "" ));
    }

    /**
     * 　電文送信処理(0214)
     * 部屋情報一覧取得要求
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    public boolean sendPacket0214(int kind, String value)
    {
        return(sendPacket0214Sub( kind, value ));
    }

    /**
     * 　電文送信処理
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */

    private boolean sendPacket0214Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        int i = 0;
        int loop = 0;

        // ホテルIDのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0214";
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0215" ) == 0 )
                        {
                            // 部屋数
                            strData = new String( cRecv, 36, 3 );
                            InfoRoomListTotalRooms = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < InfoRoomListTotalRooms ; i++ )
                            {
                                loop = 13 * i;
                                // 部屋コード
                                strData = new String( cRecv, 39 + loop, 3 );
                                InfoRoomListRoomCode[i] = Integer.valueOf( strData ).intValue();
                                // 部屋名称
                                strData = new String( cRecv, 42 + loop, 8 );
                                InfoRoomListRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // 部屋ステータス
                                strData = new String( cRecv, 50 + loop, 2 );
                                InfoRoomListRoomStatus[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                        else
                        {
                            InfoRoomListRoomCode[i] = 0;
                            InfoRoomListRoomName[i] = "";
                            InfoRoomListRoomStatus[i] = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0214:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0216)
     * 前受情報取得要求
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0216()
    {
        return(sendPacket0216Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0216)
     * 前受情報取得要求
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0216(int kind, String value)
    {
        return(sendPacket0216Sub( kind, value ));
    }

    /**
     * 　電文送信処理
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */

    private boolean sendPacket0216Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        int i = 0;
        int loop = 0;
        NumberFormat nf;

        // ホテルIDのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0216";
                // チェックインコード
                nf = new DecimalFormat( "00000000" );
                strSend += nf.format( Seq );

                // 部屋コード
                nf = new DecimalFormat( "000" );
                strSend += nf.format( RoomCode );

                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0217" ) == 0 )
                        {
                            // 結果
                            strData = new String( cRecv, 36, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // 前受金
                            strData = new String( cRecv, 39, 6 );
                            Deposit = Integer.valueOf( strData ).intValue();

                            // 請求金額
                            strData = new String( cRecv, 45, 6 );
                            Charge = Integer.valueOf( strData ).intValue();

                        }
                        else
                        {
                            Result = 99;
                            Deposit = 0;
                            Charge = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0216:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0218)
     * ホスト通信用部屋情報一覧情報の取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0218()
    {
        return(sendPacket0218Sub( 0, "" ));
    }

    /**
     * 　電文送信処理(0218)
     * ホスト通信用部屋情報一覧取得要求
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    public boolean sendPacket0218(int kind, String value)
    {
        return(sendPacket0218Sub( kind, value ));
    }

    /**
     * 　電文送信処理
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */

    private boolean sendPacket0218Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        int i = 0;
        int loop = 0;

        // ホテルIDのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value, 3000 );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0218";
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0219" ) == 0 )
                        {
                            // 部屋数
                            strData = new String( cRecv, 36, 3 );
                            InfoRoomListTotalRooms = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < InfoRoomListTotalRooms ; i++ )
                            {
                                loop = 13 * i;
                                // 部屋コード
                                strData = new String( cRecv, 39 + loop, 3 );
                                InfoRoomListRoomCode[i] = Integer.valueOf( strData ).intValue();
                                // 部屋名称
                                strData = new String( cRecv, 42 + loop, 8 );
                                InfoRoomListRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // 部屋ステータス
                                strData = new String( cRecv, 50 + loop, 2 );
                                InfoRoomListRoomStatus[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                        else
                        {
                            InfoRoomListRoomCode[i] = 0;
                            InfoRoomListRoomName[i] = "";
                            InfoRoomListRoomStatus[i] = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0218:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0300)
     * 本日料金モード取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0300()
    {
        return(sendPacket0300Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0300)
     * 部屋状況履歴取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0300(int kind, String value)
    {
        return(sendPacket0300Sub( kind, value ));
    }

    /**
     * 電文送信処理(0300)
     * 本日料金モード取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果
     */
    private boolean sendPacket0300Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // データのクリア
        TodayChargeRank = 0;
        TodayChargeRankName = "";

        // ホテルＩＤのチェック
        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // コマンド
                strSend = "0300";
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダの取得
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );

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
                        if ( strData.compareTo( "0301" ) == 0 )
                        {
                            // 料金ランクコード
                            strData = new String( cRecv, 36, 3 );
                            TodayChargeRank = Integer.valueOf( strData ).intValue();

                            // 料金ランク名称
                            strData = new String( cRecv, 39, 40 );
                            TodayChargeRankName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0300:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
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
     * @param value タイムアウト値
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

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
        return connect( tcpclient, kind, value, null );
    }

    private boolean connect(TcpClient tcpclient, int kind, String value, Integer timeOut)
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
                        ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ), timeOut );
                    }
                    else
                    {
                        ret = tcpclient.connectService( HotelId, timeOut );
                    }

                    db.close();
                }
                catch ( Exception e )
                {
                    ret = tcpclient.connectService( HotelId, timeOut );
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
