/*
 * @(#)OwnerInfo.java 2.00 2004/03/31
 * Copyright (C) ALMEX Inc. 2004
 * オーナーサイト関連通信APクラス
 */

package com.hotenavi2.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.hotenavi2.common.DBConnection;
import com.hotenavi2.common.DateEdit;
import com.hotenavi2.common.DbAccess;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.Logging;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClient;

/**
 * AMFWEBサービスとのオーナーサイト関連電文編集・送受信を行う。
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/31
 */
public class OwnerInfo implements Serializable
{

    /**
     *
     */
    private static final long  serialVersionUID              = -3827290595500590205L;

    // ------------------------------------------------------------------------------
    // 定数定義
    // ------------------------------------------------------------------------------
    /** 部屋最大数 **/
    public static final int    OWNERINFO_ROOMMAX             = 128;
    /** 部屋設備最大数 **/
    public static final int    OWNERINFO_EQUIPMAX            = 20;
    /** 明細情報最大数 **/
    public static final int    OWNERINFO_DETAILMAX           = 100;
    /** 時間情報最大数 **/
    public static final int    OWNERINFO_TIMEHOURMAX         = 24;
    /** 部屋ステータス情報最大数 **/
    public static final int    OWNERINFO_ROOMSTATUSMAX       = 50;
    /** 店舗最大数 **/
    public static final int    OWNERINFO_TENPOMAX            = 20;
    /** 売上金額データ種別最大数 **/
    public static final int    OWNERINFO_TEXSALESMAX         = 4;
    /** 金庫情報データ種別最大数 **/
    public static final int    OWNERINFO_TEXSAFEMAX          = 4;
    /** 金種最大数 **/
    public static final int    OWNERINFO_TEXSAFETYPEMAX      = 16;
    /** 売上目標明細数 **/
    public static final int    OWNERINFO_SALESTAGETMAX       = 99;
    /** ドア開情報明細数 **/
    public static final int    OWNERINFO_DOORDETAILMAX       = 40;
    /** 日付最大数 **/
    public static final int    OWNERINFO_DAYMAX              = 31;
    /** 料金モード最大数 **/
    public static final int    OWNERINFO_CHAGEMODEMAX        = 20;
    /** 記念日最大数 **/
    public static final int    OWNERINFO_MEMORIALMAX         = 8;
    /** メンバーイベント最大数 **/
    public static final int    OWNERINFO_MEMBEREVENTMAX      = 10;
    /** メンバーランク最大数 **/
    public static final int    OWNERINFO_CUSTOMRANKMAX       = 20;
    /** 精算機ログ情報最大数 **/
    public static final int    OWNERINFO_TEXLOGMAX           = 50;
    /** 売上詳細可変明細最大数 **/
    public static final int    OWNERINFO_SALESDETAILMAX      = 10;
    /** （フロント精算機）売上金額データ種別最大数 **/
    public static final int    OWNERINFO_FRONTTEXSALESMAX    = 8;
    /** （フロント精算機）金庫情報データ種別最大数 **/
    public static final int    OWNERINFO_FRONTTEXSAFEMAX     = 10;
    /** （フロント精算機）金種最大数 **/
    public static final int    OWNERINFO_FRONTTEXSAFETYPEMAX = 16;
    /** （ステータス詳細遷移）タイムテーブル最大数 **/
    public static final int    OWNERINFO_TIMETABLEMAX        = 144;
    /** （可変名称売上詳細）可変明細最大数 **/
    public static final int    OWNERINFO_MANUALSALESMAX      = 99;

    private final String       YYYY_MM                       = "yyyyMM";

    private final String       YYYY_MM_DD                    = "yyyyMMdd";

    /** ホストのバージョンに係わる判断に使用 **/
    public static final String SYSTEM_KIND_NEO               = "N";
    public static final int    SYSTEM_VER1_MIN               = 1;
    public static final int    SYSTEM_VER2_NEO_TO_SIRIUS     = 180;
    public static final String SYSTEM_CUSTOMER_KIND_SIMPLE   = "1";
    public static final String SYSTEM_CUSTOMER_KIND_ALMEX    = "2";

    // ------------------------------------------------------------------------------
    // データ領域定義
    // ------------------------------------------------------------------------------
    /** (共通)処理結果 **/
    public int                 Result;
    /** (共通)ホテルID **/
    public String              HotelId;
    /** (共通)ユーザID(ceritfiedid固定) **/
    public String              TermId;
    /** (共通)パスワード(6268固定) **/
    public String              Password;
    /** (共通)計上日 **/
    public int                 Addupdate;
    /** (共通)担当者名(""固定) **/
    public String              Name;
    /** (共通)取得部屋コード(0:全部屋) **/
    public int                 RoomCode;
    /** (共通)従業員名 **/
    public String              EmployeeName;
    /** (共通)料金モード名称 **/
    public String              ModeName;

    /** (オーナーログイン応答)システム種別 **/
    public String              SystemKind;
    /** (オーナーログイン応答)システムバージョン1 **/
    public int                 SystemVer1;
    /** (オーナーログイン応答)システムバージョン2 **/
    public int                 SystemVer2;
    /** (オーナーログイン応答)システムバージョン3 **/
    public int                 SystemVer3;
    /** (オーナーログイン応答)システムバージョン4 **/
    public int                 SystemVer4;
    /** (オーナーログイン応答)顧客システム種別 **/
    public String              SystemCustomerKind;

    /** (DB共通)ログインユーザID **/
    public String              DbLoginUser;
    /** (DB共通)ログインユーザ名 **/
    public String              DbUserName;
    /** (DB共通)パスワード **/
    public String              DbPassword;
    /** (DB共通)ユーザレベル **/
    public int                 DbUserLevel;
    /** (DB共通)ユーザID **/
    public int                 DbUserId;

    /** (売上情報)取得開始日付 **/
    public int                 SalesGetStartDate;
    /** (売上情報)取得終了日付 **/
    public int                 SalesGetEndDate;
    /** (売上情報)精算機現金売上 **/
    public int                 SalesTex;
    /** (売上情報)精算機クレジット売上 **/
    public int                 SalesTexCredit;
    /** (売上情報)フロント売上 **/
    public int                 SalesFront;
    /** (売上情報)フロントクレジット売上 **/
    public int                 SalesFrontCredit;
    /** (売上情報)売上合計 **/
    public int                 SalesTotal;
    /** (売上情報)休憩組数 **/
    public int                 SalesRestCount;
    /** (売上情報)宿泊組数 **/
    public int                 SalesStayCount;
    /** (売上情報)合計組数 **/
    public int                 SalesTotalCount;
    /** (売上情報)現在入室数 **/
    public int                 SalesNowCheckin;
    /** (売上情報)休憩回転率 **/
    public int                 SalesRestRate;
    /** (売上情報)宿泊回転率 **/
    public int                 SalesStayRate;
    /** (売上情報)合計回転率 **/
    public int                 SalesTotalRate;
    /** (売上情報)休憩客単価 **/
    public int                 SalesRestPrice;
    /** (売上情報)宿泊客単価 **/
    public int                 SalesStayPrice;
    /** (売上情報)合計客単価 **/
    public int                 SalesTotalPrice;
    /** (売上情報)ビジター客単価 **/
    public int                 SalesVisitorPrice;
    /** (売上情報)メンバー客単価 **/
    public int                 SalesMemberPrice;
    /** (売上情報)部屋単価 **/
    public int                 SalesRoomPrice;
    /** (売上情報)累計部屋単価 **/
    public int                 SalesRoomTotalPrice;
    /** (売上情報)休憩売上 **/
    public int                 SalesRestTotal;
    /** (売上情報)宿泊売上 **/
    public int                 SalesStayTotal;
    /** (売上情報)室外売上 **/
    public int                 SalesOtherTotal;
    /** (売上情報)休憩前受済組数 **/
    public int                 SalesPayRestCount;
    /** (売上情報)宿泊前受済組数 **/
    public int                 SalesPayStayCount;
    /** (売上情報)提携ポイント **/
    public int                 SalesPointTotal;

    /** (売上詳細)取得開始日付 **/
    public int                 SalesDetailGetStartDate;
    /** (売上詳細)取得終了日付 **/
    public int                 SalesDetailGetEndDate;
    /** (売上詳細)休憩 **/
    public int                 SalesDetailRest;
    /** (売上詳細)宿泊 **/
    public int                 SalesDetailStay;
    /** (売上詳細)休憩前延長 **/
    public int                 SalesDetailRestBeforeOver;
    /** (売上詳細)休憩後延長 **/
    public int                 SalesDetailRestAfterOver;
    /** (売上詳細)宿泊前延長 **/
    public int                 SalesDetailStayBeforeOver;
    /** (売上詳細)宿泊後延長 **/
    public int                 SalesDetailStayAfterOver;
    /** (売上詳細)飲食 **/
    public int                 SalesDetailMeat;
    /** (売上詳細)出前 **/
    public int                 SalesDetailDelivery;
    /** (売上詳細)コンビニ **/
    public int                 SalesDetailConveni;
    /** (売上詳細)冷蔵庫 **/
    public int                 SalesDetailRef;
    /** (売上詳細)マルチメディア **/
    public int                 SalesDetailMulti;
    /** (売上詳細)販売商品 **/
    public int                 SalesDetailSales;
    /** (売上詳細)レンタル商品 **/
    public int                 SalesDetailRental;
    /** (売上詳細)タバコ **/
    public int                 SalesDetailCigarette;
    /** (売上詳細)電話 **/
    public int                 SalesDetailTel;
    /** (売上詳細)その他 **/
    public int                 SalesDetailEtc;
    /** (売上詳細)内消費税額 **/
    public int                 SalesDetailStaxIn;
    /** (売上詳細)予備区分＊９ **/
    public int                 SalesDetailFiller[];
    /** (売上詳細)割引 **/
    public int                 SalesDetailDiscount;
    /** (売上詳細)割増 **/
    public int                 SalesDetailExtra;
    /** (売上詳細)メンバー割引 **/
    public int                 SalesDetailMember;
    /** (売上詳細)奉仕料売上 **/
    public int                 SalesDetailService;
    /** (売上詳細)消費税売上 **/
    public int                 SalesDetailStax;
    /** (売上詳細)調整金売上 **/
    public int                 SalesDetailAdjust;
    /** (売上詳細)総合計 **/
    public int                 SalesDetailTotal;
    /** (売上詳細)税率1･･･0:適用無し(単位0.1%,例10%⇒100) **/
    public int                 SalesDetailTaxRate1;
    /** (売上詳細)税率1課税対象金額 **/
    public int                 SalesDetailTaxableAmount1;
    /** (売上詳細)税率2･･･0:適用無し(単位0.1%,例10%⇒100) **/
    public int                 SalesDetailTaxRate2;
    /** (売上詳細)税率2課税対象金額 **/
    public int                 SalesDetailTaxableAmount2;

    /** (IN/OUT組数)取得開始日付 **/
    public int                 InOutGetStartDate;
    /** (IN/OUT組数)取得終了日付 **/
    public int                 InOutGetEndDate;
    /** (IN/OUT組数)時間(24件固定) **/
    public int                 InOutTime[];
    /** (IN/OUT組数)IN組数(24件固定) **/
    public int                 InOutIn[];
    /** (IN/OUT組数)OUT組数(24件固定) **/
    public int                 InOutOut[];

    /** (部屋情報)空満運用モード(1:手動,2:自動) **/
    public int                 StatusEmptyFullMode;
    /** (部屋情報)空満状態(1:空室,2:満室) **/
    public int                 StatusEmptyFullState;
    /** (部屋情報)ウェイティング数 **/
    public int                 StatusWaiting;
    /** (部屋情報)ステータス名称(MAX:50) **/
    public String              StatusName[];
    /** (部屋情報)部屋数(MAX:50) **/
    public int                 StatusCount[];

    /** (部屋詳細)部屋数 **/
    public int                 StatusDetailCount;
    /** (部屋詳細)部屋コード(MAX:128) **/
    public int                 StatusDetailRoomCode[];
    /** (部屋詳細)部屋名称(MAX:128) **/
    public String              StatusDetailRoomName[];
    /** (部屋詳細)経過時間(MAX:128) **/
    public int                 StatusDetailElapseTime[];
    /** (部屋詳細)ステータス名称(MAX:128) **/
    public String              StatusDetailStatusName[];
    /** (部屋詳細)部屋ステータス色(MAX:128) **/
    public String              StatusDetailColor[];
    /** (部屋詳細)部屋ステータス文字色(MAX:128) **/
    public String              StatusDetailForeColor[];
    /** (部屋詳細)表示位置X(MAX:128) **/
    public int                 StatusDetailX[];
    /** (部屋詳細)表示位置Y(MAX:128) **/
    public int                 StatusDetailY[];
    /** (部屋詳細)表示位置Z(MAX:128) **/
    public int                 StatusDetailZ[];
    /** (部屋詳細)フロア番号(MAX:128) **/
    public int                 StatusDetailFloor[];
    /** (部屋詳細)予定室料適用区分(MAX:128) **/
    public int                 StatusDetailUserChargeMode[];

    /** (締単位IN/OUT組数)取得日付 **/
    public int                 AddupInOutGetDate;
    /** (締単位IN/OUT組数)締後IN **/
    public int                 AddupInOutAfterIn;
    /** (締単位IN/OUT組数)締前IN **/
    public int                 AddupInOutBeforeIn;
    /** (締単位IN/OUT組数)総OUT **/
    public int                 AddupInOutAllOut;
    /** (締単位IN/OUT組数)締前OUT **/
    public int                 AddupInOutBeforeOut;

    /** (部屋状況)部屋数 **/
    public int                 StateRoomCount;
    /** (部屋状況)部屋コード(MAX:128) **/
    public int                 StateRoomCode[];
    /** (部屋状況)入室日付(MAX:128) **/
    public int                 StateInDate[];
    /** (部屋状況)入室時間(MAX:128) **/
    public int                 StateInTime[];
    /** (部屋状況)退室日付(MAX:128) **/
    public int                 StateOutDate[];
    /** (部屋状況)退室時間(MAX:128) **/
    public int                 StateOutTime[];
    /** (部屋状況)利用人数(MAX:128) **/
    public int                 StatePerson[];
    /** (部屋状況)ドア状態(MAX:128)(0:閉,1:開) **/
    public int                 StateDoor[];
    /** (部屋状況)冷蔵庫利用状態(MAX:128)(0:未使用,1:使用) **/
    public int                 StateRefUse[];
    /** (部屋状況)コンビニ利用状態(MAX:128)(0:未使用,1:使用) **/
    public int                 StateConveniUse[];
    /** (部屋状況)ラッキールーム(MAX:128)(1:ラッキールーム) **/
    public int                 StateLucky[];
    /** (部屋状況)メンバーID(MAX:128) **/
    public String              StateCustomId[];
    /** (部屋状況)ニックネーム(MAX:128) **/
    public String              StateNickName[];
    /** (部屋状況)メンバーランク名(MAX:128) **/
    public String              StateCustomRankName[];
    /** (部屋状況)メンバーイベント(MAX:128)(1:イベントあり) **/
    public int                 StateCustomEvent[];
    /** (部屋状況)警告情報(MAX:128)(1:警告あり) **/
    public int                 StateCustomWarning[];
    /** (部屋状況)連絡情報(MAX:128)(1:連絡あり) **/
    public int                 StateCustomContact[];
    /** (部屋状況)景品情報(MAX:128)(1:景品あり) **/
    public int                 StateCustomPresent[];
    /** (部屋状況)予約時間(MAX:128) **/
    public int                 StateReserveTime[];
    /** (部屋状況)駐車場番号(MAX:128) **/
    public int                 StateParkingNo[];
    /** (部屋状況)ユーザID(MAX:128) **/
    public String              StateCustomUserId[];
    /** (部屋状況)ハピホテタッチ(MAX:128) **/
    public int                 StateHapihoteTouch[];
    /** (部屋状況)ハピホテマイル使用数(MAX:128) **/
    public int                 StateHapihoteMile[];

    /** (管理機状況)部屋数 **/
    public int                 EquipRoomCount;
    /** (管理機状況)部屋コード(MAX:128) **/
    public int                 EquipRoomCode[];
    /** (管理機状況)動作区分(MAX:128)(20固定) **/
    public int                 EquipActMode[][];
    /** (管理機状況)状況警報(MAX:128)(20固定)(0:正常,1:異常) **/
    public int                 EquipStatusAlarm[][];
    /** (管理機状況)状況データ(MAX:128)(20固定) **/
    public int                 EquipStatusData[][];
    /** (管理機状況)動作データ(MAX:128)(20固定) **/
    public int                 EquipActData[][];

    /** (リネン状況)部屋数 **/
    public int                 LinenRoomCount;
    /** (リネン状況)部屋コード(MAX:128) **/
    public int                 LinenRoomCode[];
    /** (リネン状況)冷蔵庫利用状態(MAX:128)(0:未使用,1:使用) **/
    public int                 LinenRefUse[];
    /** (リネン状況)コンビニ利用状態(MAX:128)(0:未使用,1:使用) **/
    public int                 LinenConveniUse[];
    /** (リネン状況)作業班(MAX:128) **/
    public String              LinenGroup[];

    /** (メンバー状況)部屋数 **/
    public int                 MemberRoomCount;
    /** (メンバー状況)部屋コード(MAX:128) **/
    public int                 MemberRoomCode[];
    /** (メンバー状況)メンバーID(MAX:128) **/
    public String              MemberCustomId[];
    /** (メンバー状況)ニックネーム(MAX:128) **/
    public String              MemberNickName[];
    /** (メンバー状況)氏名(MAX:128) **/
    public String              MemberName[];
    /** (メンバー状況)メンバーランク名(MAX:128) **/
    public String              MemberRankName[];
    /** (メンバー状況)総利用回数(MAX:128) **/
    public int                 MemberCount[];
    /** (メンバー状況)ポイント(MAX:128) **/
    public int                 MemberPoint[];
    /** (メンバー状況)ポイント２(MAX:128) **/
    public int                 MemberPoint2[];
    /** (メンバー状況)誕生日１(MAX:128) **/
    public int                 MemberBirthday1[];
    /** (メンバー状況)誕生日２(MAX:128) **/
    public int                 MemberBirthday2[];
    /** (メンバー状況)記念日１(MAX:128) **/
    public int                 MemberMemorial1[];
    /** (メンバー状況)記念日２(MAX:128) **/
    public int                 MemberMemorial2[];
    /** (メンバー状況)登録日(MAX:128) **/
    public int                 MemberEntryDate[];
    /** (メンバー状況)今期ランキング(MAX:128) **/
    public int                 MemberNowRanking[];
    /** (メンバー状況)前期ランキング(MAX:128) **/
    public int                 MemberOldRanking[];
    /** (メンバー状況)ランキング内利用回数(MAX:128) **/
    public int                 MemberRankingCount[];
    /** (メンバー状況)集計期間内利用回数(MAX:128) **/
    public int                 MemberAddupCount[];
    /** (メンバー状況)ランキング内利用金額(MAX:128) **/
    public int                 MemberRankingTotal[];
    /** (メンバー状況)集計期間内利用金額(MAX:128) **/
    public int                 MemberAddupTotal[];
    /** (メンバー状況)繰越利用金額(MAX:128) **/
    public int                 MemberSurplus[];
    /** (メンバー状況)メンバーイベント(MAX:128)(1:イベントあり) **/
    public int                 MemberEvent[];
    /** (メンバー状況)警告情報(MAX:128)(1:警告あり) **/
    public int                 MemberWarning[];
    /** (メンバー状況)連絡情報(MAX:128)(1:連絡あり) **/
    public int                 MemberContact[];
    /** (メンバー状況)景品情報(MAX:128)(1:景品あり) **/
    public int                 MemberPresent[];
    /** (メンバー状況)イベント情報(MAX:128)(10固定) **/
    public int                 MemberEventInfo[][];
    /** (メンバー状況)連絡メモ１(MAX:128) **/
    public String              MemberContact1[];
    /** (メンバー状況)連絡メモ２(MAX:128) **/
    public String              MemberContact2[];
    /** (メンバー状況)警告メモ１(MAX:128) **/
    public String              MemberWarning1[];
    /** (メンバー状況)警告メモ２(MAX:128) **/
    public String              MemberWarning2[];
    /** (メンバー状況)ユーザID(MAX:128) **/
    public String              MemberUserId[];

    /** (車番状況)部屋数 **/
    public int                 CarRoomCount;
    /** (車番状況)部屋コード(MAX:128) **/
    public int                 CarRoomCode[];
    /** (車番状況)車番(地域)(MAX:128) **/
    public String              CarArea[];
    /** (車番状況)車番(種別)(MAX:128) **/
    public String              CarKind[];
    /** (車番状況)車番(車種)(MAX:128) **/
    public String              CarType[];
    /** (車番状況)車番(車番)(MAX:128) **/
    public String              CarNo[];
    /** (車番状況)駐車場番号(MAX:128) **/
    public int                 CarParkingNo[];

    /** (精算機状況)部屋数 **/
    public int                 TexRoomCount;
    /** (精算機状況)部屋コード(MAX:128) **/
    public int                 TexRoomCode[];
    /** (精算機状況)精算機モード(MAX:128) **/
    public int                 TexMode[];
    /** (精算機状況)補充状態(MAX:128) **/
    public int                 TexSupplyStat[];
    /** (精算機状況)セキュリティ状態(MAX:128) **/
    public int                 TexSecurityStat[];
    /** (精算機状況)扉状態(MAX:128) **/
    public int                 TexDoorStat[];
    /** (精算機状況)回線状態(MAX:128) **/
    public int                 TexLineStat[];
    /** (精算機状況)エラー状態(MAX:128) **/
    public int                 TexErrorStat[];
    /** (精算機状況)精算状態(MAX:128) **/
    public int                 TexPayStat[];
    /** (精算機状況)釣銭状態(MAX:128) **/
    public int                 TexChargeStat[];
    /** (精算機状況)エラーコード(MAX:128) **/
    public String              TexErrorCode[];
    /** (精算機状況)エラー内容(MAX:128) **/
    public String              TexErrorMsg[];
    /** (精算機状況)売上金額データ（金額）(MAX:128)(0:現金,1:クレジット,2:予備１,3:予備２) **/
    public int                 TexSalesTotal[][];
    /** (精算機状況)売上金額データ（回数）(MAX:128)(0:現金,1:クレジット,2:予備１,3:予備２) **/
    public int                 TexSalesCount[][];
    /** (精算機状況)金庫情報データ（枚数）(MAX:128)(0:トータル入金,1:トータル出金,2:トータル入出金差引,3:残枚数) **/
    public int                 TexSafeCount[][][];
    /** (精算機状況)金庫情報データ（合計金額）(MAX:128)(0:トータル入金,1:トータル出金,2:トータル入出金差引,3:残枚数) **/
    public int                 TexSafeTotal[][];
    /** (精算機状況)金庫枚数状況(MAX:128)(16金種) **/
    public int                 TexSafeStat[][];
    /** (精算機状況)かぎ無し補充金額(MAX:128) **/
    public int                 TexSupplyTotal[];
    /** (精算機状況)かぎあり補充日付(MAX:128) **/
    public int                 TexSupplyDate[];
    /** (精算機状況)かぎ無し補充時間(MAX:128) **/
    public int                 TexSupplyTime[];
    /** (精算機状況)余剰金(MAX:128) **/
    public int                 TexSurplus[];
    /** (精算機状況)カード状態(MAX:128) **/
    public int                 TexCardStat[];

    /** (マルチメディア状況)部屋数 **/
    public int                 MultiRoomCount;
    /** (マルチメディア状況)部屋コード(MAX:128) **/
    public int                 MultiRoomCode[];
    /** (マルチメディア状況)回線状態(MAX:128) **/
    public int                 MultiLineStat[];
    /** (マルチメディア状況)エラー状態(MAX:128) **/
    public int                 MultiErrorStat[];
    /** (マルチメディア状況)電源状態(MAX:128) **/
    public int                 MultiPowerStat[];
    /** (マルチメディア状況)エラーコード(MAX:128) **/
    public String              MultiErrorCode[];
    /** (マルチメディア状況)エラー内容(MAX:128) **/
    public String              MultiErrorMsg[];
    /** (マルチメディア状況)視聴チャンネル番号(MAX:128) **/
    public int                 MultiChannelNo[];
    /** (マルチメディア状況)視聴チャンネル名称(MAX:128) **/
    public String              MultiChannelName[];

    /** (部屋詳細・利用明細)部屋コード **/
    public int                 DetailUseRoomCode;
    /** (部屋詳細・利用明細)利用明細数 **/
    public int                 DetailUseCount;
    /** (部屋詳細・利用明細)利用明細名(MAX:100) **/
    public String              DetailUseGoodsName[];
    /** (部屋詳細・利用明細)数量(MAX:100) **/
    public int                 DetailUseGoodsCount[];
    /** (部屋詳細・利用明細)正規単価(MAX:100) **/
    public int                 DetailUseGoodsRegularPrice[];
    /** (部屋詳細・利用明細)単価(MAX:100) **/
    public int                 DetailUseGoodsPrice[];
    /** (部屋詳細・利用明細)割引率(MAX:100) **/
    public int                 DetailUseGoodsDiscount[];

    /** (部屋詳細・支払い状況)部屋コード **/
    public int                 DetailPayRoomCode;
    /** (部屋詳細・支払い状況)利用合計 **/
    public int                 DetailPayTotal;
    /** (部屋詳細・支払い状況)請求金額 **/
    public int                 DetailPayClaim;
    /** (部屋詳細・支払い状況)支払い明細数 **/
    public int                 DetailPayCount;
    /** (部屋詳細・支払い状況)利用明細名(MAX:100) **/
    public String              DetailPayName[];
    /** (部屋詳細・支払い状況)数量(MAX:100) **/
    public int                 DetailPayAmount[];
    /** (部屋詳細・支払い状況)金額(MAX:100) **/
    public int                 DetailPayMoney[];

    /** (部屋詳細・商品明細)部屋コード **/
    public int                 DetailGoodsRoomCode;
    /** (部屋詳細・商品明細)商品明細数 **/
    public int                 DetailGoodsCount;
    /** (部屋詳細・商品明細)利用明細名(MAX:100) **/
    public String              DetailGoodsName[];
    /** (部屋詳細・商品明細)数量(MAX:100) **/
    public int                 DetailGoodsAmount[];
    /** (部屋詳細・商品明細)単価(MAX:100) **/
    public int                 DetailGoodsPrice[];
    /** (部屋詳細・商品明細)冷蔵庫フラグ(MAX:100)(1:冷蔵庫(コンビニ),2:自動,3:手動) **/
    public int                 DetailGoodsRef[];

    /** (売上目標)計上年月 **/
    public int                 TargetMonth;
    /** (売上目標)累計組数 **/
    public int                 TargetCount;
    /** (売上目標)累計売上額 **/
    public int                 TargetTotal;
    /** (売上目標)料金モード数 **/
    public int                 TargetModeCount;
    /** (売上目標)料金モード(MAX:99) **/
    public int                 TargetModeCode[];
    /** (売上目標)料金モード名称(MAX:99) **/
    public String              TargetModeName[];
    /** (売上目標)休憩組数(MAX:99) **/
    public int                 TargetModeRestCount[];
    /** (売上目標)宿泊組数(MAX:99) **/
    public int                 TargetModeStayCount[];
    /** (売上目標)休憩売上金額(MAX:99) **/
    public int                 TargetModeRestTotal[];
    /** (売上目標)宿泊売上金額(MAX:99) **/
    public int                 TargetModeStayTotal[];

    /** (売上実績)計上年月 **/
    public int                 ResultMonth;
    /** (売上実績)累計組数 **/
    public int                 ResultCount;
    /** (売上実績)累計売上額 **/
    public int                 ResultTotal;
    /** (売上実績)料金モード数 **/
    public int                 ResultModeCount;
    /** (売上実績)料金モード(MAX:99) **/
    public int                 ResultModeCode[];
    /** (売上実績)料金モード名称(MAX:99) **/
    public String              ResultModeName[];
    /** (売上実績)休憩組数(MAX:99) **/
    public int                 ResultModeRestCount[];
    /** (売上実績)宿泊組数(MAX:99) **/
    public int                 ResultModeStayCount[];
    /** (売上実績)休憩売上金額(MAX:99) **/
    public int                 ResultModeRestTotal[];
    /** (売上実績)宿泊売上金額(MAX:99) **/
    public int                 ResultModeStayTotal[];

    /** (操作イベント)次ページ取得日付 **/
    public int                 EventGetNextDate;
    /** (操作イベント)次ページ取得時刻 **/
    public int                 EventGetNextTime;
    /** (操作イベント)次ページ時刻補助 **/
    public int                 EventGetNextTimeSub;
    /** (操作イベント)次ページ時刻補助 **/
    public int                 EventGetNextRoomCode;
    /** (操作イベント)次ページイベントコード **/
    public int                 EventGetNextEventCode;
    /** (操作イベント)前ページ取得日付 **/
    public int                 EventGetPrevDate;
    /** (操作イベント)前ページ取得時刻 **/
    public int                 EventGetPrevTime;
    /** (操作イベント)前ページ時刻補助 **/
    public int                 EventGetPrevTimeSub;
    /** (操作イベント)前ページ時刻補助 **/
    public int                 EventGetPrevRoomCode;
    /** (操作イベント)前ページイベントコード **/
    public int                 EventGetPrevEventCode;
    /** (操作イベント)取得件数 **/
    public int                 EventCount;
    /** (操作イベント)日付(40件固定) **/
    public int                 EventDate[];
    /** (操作イベント)時刻(40件固定) **/
    public int                 EventTime[];
    /** (操作イベント)時刻補助(40件固定) **/
    public int                 EventTimeSub[];
    /** (操作イベント)部屋コード(40件固定) **/
    public int                 EventRoomCode[];
    /** (操作イベント)部屋名称(40件固定) **/
    public String              EventRoomName[];
    /** (操作イベント)系統番号(40件固定) **/
    public int                 EventLineNo[];
    /** (操作イベント)端末ID(40件固定) **/
    public int                 EventTermId[];
    /** (操作イベント)従業員コード(40件固定) **/
    public int                 EventEmployeeCode[];
    /** (操作イベント)イベントコード(40件固定) **/
    public int                 EventEventCode[];
    /** (操作イベント)システムエラーコード(40件固定) **/
    public int                 EventSystemErrCode[];
    /** (操作イベント)付帯情報(40件固定)(6固定) **/
    public int                 EventNumData[][];
    /** (操作イベント)付帯文字情報(40件固定) **/
    public String              EventStrData[];

    /** (オートカレンダー)取得日付 **/
    public int                 CalGetDate;
    /** (オートカレンダー)料金モード数 **/
    public int                 CalModeCount;
    /** (オートカレンダー)料金モード番号(20件固定) **/
    public int                 CalModeCode[];
    /** (オートカレンダー)料金モード名称(20件固定) **/
    public String              CalModeName[];
    /** (オートカレンダー)日付(31件固定) **/
    public int                 CalDayDate[];
    /** (オートカレンダー)料金モード(31件固定) **/
    public int                 CalDayMode[];
    /** (オートカレンダー)料金モード名称(31件固定) **/
    public String              CalDayModeName[];
    /** (オートカレンダー)曜日種別(31件固定)(1:日～7:土) **/
    public int                 CalDayWeekKind[];
    /** (オートカレンダー)休日種別(31件固定)(1:平日,2:休日(祝祭日),3:休前日) **/
    public int                 CalDayHolidayKind[];
    /** (オートカレンダー)特記事項１(31件固定) **/
    public String              CalDayMemo1[];
    /** (オートカレンダー)特記事項２(31件固定) **/
    public String              CalDayMemo2[];

    /** (部屋状況履歴)取得日付 **/
    public int                 RoomHistoryDate;
    /** (部屋状況履歴)総部屋数 **/
    public int                 RoomHistoryRoomCount;
    /** (部屋状況履歴)取得件数 **/
    public int                 RoomHistoryCount;
    /** (部屋状況履歴)時刻(24件固定) **/
    public int                 RoomHistoryTime[];
    /** (部屋状況履歴)空室数(24件固定) **/
    public int                 RoomHistoryEmpty[];
    /** (部屋状況履歴)在室数(24件固定) **/
    public int                 RoomHistoryExist[];
    /** (部屋状況履歴)準備数(24件固定) **/
    public int                 RoomHistoryClean[];
    /** (部屋状況履歴)売止数(24件固定) **/
    public int                 RoomHistoryStop[];

    /** (メンバーランク)メンバーランク数 **/
    public int                 CustomRankCount;
    /** (メンバーランク)メンバーランクコード **/
    public int                 CustomRankCode[];
    /** (メンバーランク)メンバーランク名称 **/
    public String              CustomRankName[];

    /** (精算機ログ)ログレベル **/
    public int                 TexlogGetLogLevel;
    /** (精算機ログ)次ページ発生日付 **/
    public int                 TexlogNextDate;
    /** (精算機ログ)次ページ発生時刻 **/
    public int                 TexlogNextTime;
    /** (精算機ログ)次ページ部屋コード **/
    public int                 TexlogNextRoomCode;
    /** (精算機ログ)次ページ系統番号 **/
    public int                 TexlogNextLineNo;
    /** (精算機ログ)次ページ端末ID **/
    public int                 TexlogNextTermId;
    /** (精算機ログ)次ページ発生日付 **/
    public int                 TexlogPrevDate;
    /** (精算機ログ)次ページ発生時刻 **/
    public int                 TexlogPrevTime;
    /** (精算機ログ)次ページ部屋コード **/
    public int                 TexlogPrevRoomCode;
    /** (精算機ログ)次ページ系統番号 **/
    public int                 TexlogPrevLineNo;
    /** (精算機ログ)次ページ端末ID **/
    public int                 TexlogPrevTermId;
    /** (精算機ログ)ログ件数 **/
    public int                 TexlogCount;
    /** (精算機ログ)発生日付 **/
    public int                 TexlogDate[];
    /** (精算機ログ)発生時間 **/
    public int                 TexlogTime[];
    /** (精算機ログ)部屋コード **/
    public int                 TexlogRoomCode[];
    /** (精算機ログ)部屋名称 **/
    public String              TexlogRoomName[];
    /** (精算機ログ)系統番号 **/
    public int                 TexlogLineNo[];
    /** (精算機ログ)端末ID **/
    public int                 TexlogTermId[];
    /** (精算機ログ)請求金額 **/
    public int                 TexlogClaimed[];
    /** (精算機ログ)余剰金 **/
    public int                 TexlogSurplus[];
    /** (精算機ログ)金庫枚数 **/
    public int                 TexlogSafeCount[][][];
    /** (精算機ログ)金庫合計金額 **/
    public int                 TexlogSafeTotal[][];
    /** (精算機ログ)ログレベル **/
    public int                 TexlogLogLevel[];
    /** (精算機ログ)ログ内容 **/
    public String              TexlogLogContent[];
    /** (精算機ログ)ログ詳細 **/
    public String              TexlogLogDetail[];
    /** (精算機ログ)取引状態 **/
    public int                 TexlogStatTrade[];
    /** (精算機ログ)入出金状態 **/
    public int                 TexlogStatInOut[];
    /** (精算機ログ)セキュリティ状態 **/
    public int                 TexlogStatSecurity[];

    /** (売上詳細)取得開始日付 **/
    public int                 AscSalesDetailGetStartDate;
    /** (売上詳細)取得終了日付 **/
    public int                 AscSalesDetailGetEndDate;
    /** (売上詳細)宿泊 **/
    public int                 AscSalesDetailStay;
    /** (売上詳細)宿泊前延長 **/
    public int                 AscSalesDetailStayBeforeOver;
    /** (売上詳細)宿泊後延長 **/
    public int                 AscSalesDetailStayAfterOver;
    /** (売上詳細)休憩 **/
    public int                 AscSalesDetailRest;
    /** (売上詳細)休憩後延長 **/
    public int                 AscSalesDetailRestOver;
    /** (売上詳細)電話 **/
    public int                 AscSalesDetailTel;
    /** (売上詳細)立替金 **/
    public int                 AscSalesDetailAdvance;
    /** (売上詳細)小計 **/
    public int                 AscSalesDetailSubTotal;
    /** (売上詳細)奉仕料売上 **/
    public int                 AscSalesDetailService;
    /** (売上詳細)税金（外税） **/
    public int                 AscSalesDetailTaxOut;
    /** (売上詳細)税金（内税） **/
    public int                 AscSalesDetailTaxIn;
    /** (売上詳細)割引 **/
    public int                 AscSalesDetailDiscount;
    /** (売上詳細)割増 **/
    public int                 AscSalesDetailExtra;
    /** (売上詳細)メンバー割引 **/
    public int                 AscSalesDetailMember;
    /** (売上詳細)調整金売上 **/
    public int                 AscSalesDetailAdjust;
    /** (売上詳細)予備区分＊6 **/
    public int                 AscSalesDetailFiller[];
    /** (売上詳細)総合計 **/
    public int                 AscSalesDetailTotal;

    /** (売上詳細)可変明細明細数 **/
    public int                 AscSalesDetailCount;
    /** (売上詳細)可変明細明細名称＊１０ **/
    public String              AscSalesDetailName[];
    /** (売上詳細)可変明細金額＊１０ **/
    public int                 AscSalesDetailAmount[];

    /** (フロント精算機状況)端末コード(IN) **/
    public int                 FrontTexTermCodeIn;
    /** (フロント精算機状況)端末数 **/
    public int                 FrontTexTermCount;
    /** (フロント精算機状況)端末コード(MAX:128) **/
    public int                 FrontTexTermCode[];
    /** (フロント精算機状況)端末名称(MAX:128) **/
    public String              FrontTexTermName[];
    /** (フロント精算機状況)取扱状態(MAX:128) **/
    public int                 FrontTexServiceStat[];
    /** (フロント精算機状況)キーSW状態(MAX:128) **/
    public int                 FrontTexKeySwStat[];
    /** (フロント精算機状況)セキュリティ状態(MAX:128) **/
    public int                 FrontTexSecurityStat[];
    /** (フロント精算機状況)扉状態(MAX:128) **/
    public int                 FrontTexDoorStat[];
    /** (フロント精算機状況)回線状態(MAX:128) **/
    public int                 FrontTexLineStat[];
    /** (フロント精算機状況)エラー状態(MAX:128) **/
    public int                 FrontTexErrorStat[];
    /** (フロント精算機状況)エラーコード(MAX:128) **/
    public String              FrontTexErrorCode[];
    /** (フロント精算機状況)エラー内容(MAX:128) **/
    public String              FrontTexErrorMsg[];
    /** (フロント精算機状況)売上金額データ（金額）(MAX:128)(0:現金,1:クレジット,2:予備１,3:予備２) **/
    public int                 FrontTexSalesTotal[][];
    /** (フロント精算機状況)売上金額データ（回数）(MAX:128)(0:現金,1:クレジット,2:予備１,3:予備２) **/
    public int                 FrontTexSalesCount[][];
    /** (フロント精算機状況)金庫情報データ（枚数）(MAX:128)(0:トータル入金,1:トータル出金,2:トータル入出金差引,3:残枚数) **/
    public int                 FrontTexSafeCount[][][];
    /** (フロント精算機状況)金庫情報データ（合計金額）(MAX:128)(0:トータル入金,1:トータル出金,2:トータル入出金差引,3:残枚数) **/
    public int                 FrontTexSafeTotal[][];
    /** (フロント精算機状況)金庫枚数状況(MAX:128)(16金種) **/
    public int                 FrontTexSafeStat[][];

    /** (部屋ステータス詳細遷移)部屋コード **/
    public int                 TimeChartRoomCodeOne;
    /** (部屋ステータス詳細遷移)タイムチャート基準時間 **/
    public int                 TimeChartStartTime;
    /** (部屋ステータス詳細遷移)ステータス名称（52個固定） **/
    public String              TimeChartStatusName[];
    /** (部屋ステータス詳細遷移)ステータス色（52個固定） **/
    public String              TimeChartStatusColor[];
    /** (部屋ステータス詳細遷移)ステータス文字色（52個固定） **/
    public String              TimeChartStatusForeColor[];
    /** (部屋ステータス詳細遷移)部屋数 **/
    public int                 TimeChartRoomCount;
    /** (部屋ステータス詳細遷移)部屋コード（MAX128） **/
    public int                 TimeChartRoomCode[];
    /** (部屋ステータス詳細遷移)部屋名称（MAX128） **/
    public String              TimeChartRoomName[];
    /** (部屋ステータス詳細遷移)フロア番号（MAX128） **/
    public int                 TimeChartRoomFloor[];
    /** (部屋ステータス詳細遷移)部屋ステータス（MAX128）（144固定） **/
    public int                 TimeChartRoomStatus[][];

    /** (可変名称売上明細)取得開始日付 **/
    public int                 ManualSalesDetailGetStartDate;
    /** (可変名称売上明細)取得終了日付 **/
    public int                 ManualSalesDetailGetEndDate;
    /** (可変名称売上明細)税金（内税） **/
    public int                 ManualSalesDetailTaxIn;
    /** (可変名称売上明細)総合計 **/
    public int                 ManualSalesDetailTotal;
    /** (可変名称売上明細)可変明細明細数 **/
    public int                 ManualSalesDetailCount;
    /** (可変名称売上明細)可変明細明細名称 **/
    public String              ManualSalesDetailName[];
    /** (可変名称売上明細)可変明細金額 **/
    public int                 ManualSalesDetailAmount[];

    /** ログ出力クラス **/
    private LogLib             log;

    /**
     * オーナーサイト関連情報データの初期化を行います
     * 
     */
    public OwnerInfo()
    {
        // 共通項目
        Result = 0;
        HotelId = "";
        TermId = "ceritfiedid";
        Password = "6268";
        Addupdate = 0;
        Name = "";
        RoomCode = 0;
        EmployeeName = "";
        ModeName = "";

        // DB共通項目
        DbLoginUser = "";
        DbUserName = "";
        DbPassword = "";
        DbUserLevel = 0;
        DbUserId = 0;

        // オーナーログイン応答
        SystemKind = "";
        SystemVer1 = 0;
        SystemVer2 = 0;
        SystemVer3 = 0;
        SystemVer4 = 0;
        SystemCustomerKind = "";

        // 売上情報
        SalesGetStartDate = 0;
        SalesGetEndDate = 0;
        SalesTex = 0;
        SalesTexCredit = 0;
        SalesFront = 0;
        SalesFrontCredit = 0;
        SalesTotal = 0;
        SalesRestCount = 0;
        SalesStayCount = 0;
        SalesTotalCount = 0;
        SalesNowCheckin = 0;
        SalesRestRate = 0;
        SalesStayRate = 0;
        SalesTotalRate = 0;
        SalesRestPrice = 0;
        SalesStayPrice = 0;
        SalesTotalPrice = 0;
        SalesVisitorPrice = 0;
        SalesMemberPrice = 0;
        SalesRoomPrice = 0;
        SalesRoomTotalPrice = 0;
        SalesRestTotal = 0;
        SalesStayTotal = 0;
        SalesOtherTotal = 0;
        SalesPayRestCount = 0;
        SalesPayStayCount = 0;
        SalesPointTotal = 0;

        // 売上詳細
        SalesDetailGetStartDate = 0;
        SalesDetailGetEndDate = 0;
        SalesDetailRest = 0;
        SalesDetailStay = 0;
        SalesDetailRestBeforeOver = 0;
        SalesDetailRestAfterOver = 0;
        SalesDetailStayBeforeOver = 0;
        SalesDetailStayAfterOver = 0;
        SalesDetailMeat = 0;
        SalesDetailDelivery = 0;
        SalesDetailConveni = 0;
        SalesDetailRef = 0;
        SalesDetailMulti = 0;
        SalesDetailSales = 0;
        SalesDetailRental = 0;
        SalesDetailCigarette = 0;
        SalesDetailTel = 0;
        SalesDetailEtc = 0;
        SalesDetailStaxIn = 0;
        SalesDetailFiller = new int[9];
        SalesDetailDiscount = 0;
        SalesDetailExtra = 0;
        SalesDetailMember = 0;
        SalesDetailService = 0;
        SalesDetailStax = 0;
        SalesDetailAdjust = 0;
        SalesDetailTotal = 0;
        SalesDetailTaxRate1 = 0;
        SalesDetailTaxableAmount1 = 0;
        SalesDetailTaxRate2 = 0;
        SalesDetailTaxableAmount2 = 0;

        // IN/OUT組数
        InOutGetStartDate = 0;
        InOutGetEndDate = 0;
        InOutTime = new int[OWNERINFO_TIMEHOURMAX];
        InOutIn = new int[OWNERINFO_TIMEHOURMAX];
        InOutOut = new int[OWNERINFO_TIMEHOURMAX];

        // 部屋情報
        StatusEmptyFullMode = 0;
        StatusEmptyFullState = 0;
        StatusWaiting = 0;
        StatusName = new String[OWNERINFO_ROOMSTATUSMAX];
        StatusCount = new int[OWNERINFO_ROOMSTATUSMAX];

        // 部屋詳細
        StatusDetailCount = 0;
        StatusDetailRoomCode = new int[OWNERINFO_ROOMMAX];
        StatusDetailRoomName = new String[OWNERINFO_ROOMMAX];
        StatusDetailElapseTime = new int[OWNERINFO_ROOMMAX];
        StatusDetailStatusName = new String[OWNERINFO_ROOMMAX];
        StatusDetailColor = new String[OWNERINFO_ROOMMAX];
        StatusDetailForeColor = new String[OWNERINFO_ROOMMAX];
        StatusDetailX = new int[OWNERINFO_ROOMMAX];
        StatusDetailY = new int[OWNERINFO_ROOMMAX];
        StatusDetailZ = new int[OWNERINFO_ROOMMAX];
        StatusDetailFloor = new int[OWNERINFO_ROOMMAX];

        // 締単位IN/OUT組数
        AddupInOutGetDate = 0;
        AddupInOutAfterIn = 0;
        AddupInOutBeforeIn = 0;
        AddupInOutAllOut = 0;
        AddupInOutBeforeOut = 0;

        // 部屋状況
        StateRoomCount = 0;
        StateRoomCode = new int[OWNERINFO_ROOMMAX];
        StateInDate = new int[OWNERINFO_ROOMMAX];
        StateInTime = new int[OWNERINFO_ROOMMAX];
        StateOutDate = new int[OWNERINFO_ROOMMAX];
        StateOutTime = new int[OWNERINFO_ROOMMAX];
        StatePerson = new int[OWNERINFO_ROOMMAX];
        StateDoor = new int[OWNERINFO_ROOMMAX];
        StateRefUse = new int[OWNERINFO_ROOMMAX];
        StateConveniUse = new int[OWNERINFO_ROOMMAX];
        StateLucky = new int[OWNERINFO_ROOMMAX];
        StateCustomId = new String[OWNERINFO_ROOMMAX];
        StateNickName = new String[OWNERINFO_ROOMMAX];
        StateCustomRankName = new String[OWNERINFO_ROOMMAX];
        StateCustomEvent = new int[OWNERINFO_ROOMMAX];
        StateCustomWarning = new int[OWNERINFO_ROOMMAX];
        StateCustomContact = new int[OWNERINFO_ROOMMAX];
        StateCustomPresent = new int[OWNERINFO_ROOMMAX];
        StateReserveTime = new int[OWNERINFO_ROOMMAX];
        StateParkingNo = new int[OWNERINFO_ROOMMAX];
        StateCustomUserId = new String[OWNERINFO_ROOMMAX];
        StateHapihoteTouch = new int[OWNERINFO_ROOMMAX];
        StateHapihoteMile = new int[OWNERINFO_ROOMMAX];

        // 管理機状況
        EquipRoomCount = 0;
        EquipRoomCode = new int[OWNERINFO_ROOMMAX];
        EquipActMode = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipStatusAlarm = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipStatusData = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipActData = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];

        // リネン状況
        LinenRoomCount = 0;
        LinenRoomCode = new int[OWNERINFO_ROOMMAX];
        LinenRefUse = new int[OWNERINFO_ROOMMAX];
        LinenConveniUse = new int[OWNERINFO_ROOMMAX];
        LinenGroup = new String[OWNERINFO_ROOMMAX];

        // メンバー状況
        MemberRoomCount = 0;
        MemberRoomCode = new int[OWNERINFO_ROOMMAX];
        MemberCustomId = new String[OWNERINFO_ROOMMAX];
        MemberNickName = new String[OWNERINFO_ROOMMAX];
        MemberName = new String[OWNERINFO_ROOMMAX];
        MemberRankName = new String[OWNERINFO_ROOMMAX];
        MemberCount = new int[OWNERINFO_ROOMMAX];
        MemberPoint = new int[OWNERINFO_ROOMMAX];
        MemberPoint2 = new int[OWNERINFO_ROOMMAX];
        MemberBirthday1 = new int[OWNERINFO_ROOMMAX];
        MemberBirthday2 = new int[OWNERINFO_ROOMMAX];
        MemberMemorial1 = new int[OWNERINFO_ROOMMAX];
        MemberMemorial2 = new int[OWNERINFO_ROOMMAX];
        MemberEntryDate = new int[OWNERINFO_ROOMMAX];
        MemberNowRanking = new int[OWNERINFO_ROOMMAX];
        MemberOldRanking = new int[OWNERINFO_ROOMMAX];
        MemberRankingCount = new int[OWNERINFO_ROOMMAX];
        MemberAddupCount = new int[OWNERINFO_ROOMMAX];
        MemberRankingTotal = new int[OWNERINFO_ROOMMAX];
        MemberAddupTotal = new int[OWNERINFO_ROOMMAX];
        MemberSurplus = new int[OWNERINFO_ROOMMAX];
        MemberEvent = new int[OWNERINFO_ROOMMAX];
        MemberWarning = new int[OWNERINFO_ROOMMAX];
        MemberContact = new int[OWNERINFO_ROOMMAX];
        MemberPresent = new int[OWNERINFO_ROOMMAX];
        MemberEventInfo = new int[OWNERINFO_ROOMMAX][OWNERINFO_MEMBEREVENTMAX];
        MemberContact1 = new String[OWNERINFO_ROOMMAX];
        MemberContact2 = new String[OWNERINFO_ROOMMAX];
        MemberWarning1 = new String[OWNERINFO_ROOMMAX];
        MemberWarning2 = new String[OWNERINFO_ROOMMAX];
        MemberUserId = new String[OWNERINFO_ROOMMAX];

        // 車番状況
        CarRoomCount = 0;
        CarRoomCode = new int[OWNERINFO_ROOMMAX];
        CarArea = new String[OWNERINFO_ROOMMAX];
        CarKind = new String[OWNERINFO_ROOMMAX];
        CarType = new String[OWNERINFO_ROOMMAX];
        CarNo = new String[OWNERINFO_ROOMMAX];
        CarParkingNo = new int[OWNERINFO_ROOMMAX];

        // 精算機状況
        TexRoomCount = 0;
        TexRoomCode = new int[OWNERINFO_ROOMMAX];
        TexMode = new int[OWNERINFO_ROOMMAX];
        TexSupplyStat = new int[OWNERINFO_ROOMMAX];
        TexSecurityStat = new int[OWNERINFO_ROOMMAX];
        TexDoorStat = new int[OWNERINFO_ROOMMAX];
        TexLineStat = new int[OWNERINFO_ROOMMAX];
        TexErrorStat = new int[OWNERINFO_ROOMMAX];
        TexPayStat = new int[OWNERINFO_ROOMMAX];
        TexChargeStat = new int[OWNERINFO_ROOMMAX];
        TexErrorCode = new String[OWNERINFO_ROOMMAX];
        TexErrorMsg = new String[OWNERINFO_ROOMMAX];
        TexSalesTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSALESMAX];
        TexSalesCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSALESMAX];
        TexSafeCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFEMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexSafeTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFEMAX];
        TexSafeStat = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexSupplyTotal = new int[OWNERINFO_ROOMMAX];
        TexSupplyDate = new int[OWNERINFO_ROOMMAX];
        TexSupplyTime = new int[OWNERINFO_ROOMMAX];
        TexSurplus = new int[OWNERINFO_ROOMMAX];
        TexCardStat = new int[OWNERINFO_ROOMMAX];

        // マルチメディア状況
        MultiRoomCount = 0;
        MultiRoomCode = new int[OWNERINFO_ROOMMAX];
        MultiLineStat = new int[OWNERINFO_ROOMMAX];
        MultiErrorStat = new int[OWNERINFO_ROOMMAX];
        MultiPowerStat = new int[OWNERINFO_ROOMMAX];
        MultiErrorCode = new String[OWNERINFO_ROOMMAX];
        MultiErrorMsg = new String[OWNERINFO_ROOMMAX];
        MultiChannelNo = new int[OWNERINFO_ROOMMAX];
        MultiChannelName = new String[OWNERINFO_ROOMMAX];

        // 部屋詳細・利用履歴
        DetailUseRoomCode = 0;
        DetailUseCount = 0;
        DetailUseGoodsName = new String[OWNERINFO_DETAILMAX];
        DetailUseGoodsCount = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsRegularPrice = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsPrice = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsDiscount = new int[OWNERINFO_DETAILMAX];

        // 部屋詳細・支払い状況
        DetailPayRoomCode = 0;
        DetailPayTotal = 0;
        DetailPayClaim = 0;
        DetailPayCount = 0;
        DetailPayName = new String[OWNERINFO_DETAILMAX];
        DetailPayAmount = new int[OWNERINFO_DETAILMAX];
        DetailPayMoney = new int[OWNERINFO_DETAILMAX];

        // 部屋詳細・商品明細
        DetailGoodsRoomCode = 0;
        DetailGoodsCount = 0;
        DetailGoodsName = new String[OWNERINFO_DETAILMAX];
        DetailGoodsAmount = new int[OWNERINFO_DETAILMAX];
        DetailGoodsPrice = new int[OWNERINFO_DETAILMAX];
        DetailGoodsRef = new int[OWNERINFO_DETAILMAX];

        // 売上目標
        TargetMonth = 0;
        TargetCount = 0;
        TargetTotal = 0;
        TargetModeCount = 0;
        TargetModeCode = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeName = new String[OWNERINFO_SALESTAGETMAX];
        TargetModeRestCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeRestTotal = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayTotal = new int[OWNERINFO_SALESTAGETMAX];

        // 売上実績
        ResultMonth = 0;
        ResultCount = 0;
        ResultTotal = 0;
        ResultModeCount = 0;
        ResultModeCode = new int[OWNERINFO_SALESTAGETMAX];
        ResultModeName = new String[OWNERINFO_SALESTAGETMAX];
        ResultModeRestCount = new int[OWNERINFO_SALESTAGETMAX];
        ResultModeStayCount = new int[OWNERINFO_SALESTAGETMAX];
        ResultModeRestTotal = new int[OWNERINFO_SALESTAGETMAX];
        ResultModeStayTotal = new int[OWNERINFO_SALESTAGETMAX];

        // 操作イベント
        EventGetNextDate = 0;
        EventGetNextTime = 0;
        EventGetNextTimeSub = 0;
        EventGetNextRoomCode = 0;
        EventGetNextEventCode = 0;
        EventGetPrevDate = 0;
        EventGetPrevTime = 0;
        EventGetPrevTimeSub = 0;
        EventGetPrevRoomCode = 0;
        EventGetPrevEventCode = 0;
        EventCount = 0;
        EventDate = new int[OWNERINFO_DOORDETAILMAX];
        EventTime = new int[OWNERINFO_DOORDETAILMAX];
        EventTimeSub = new int[OWNERINFO_DOORDETAILMAX];
        EventRoomCode = new int[OWNERINFO_DOORDETAILMAX];
        EventRoomName = new String[OWNERINFO_DOORDETAILMAX];
        EventLineNo = new int[OWNERINFO_DOORDETAILMAX];
        EventTermId = new int[OWNERINFO_DOORDETAILMAX];
        EventEmployeeCode = new int[OWNERINFO_DOORDETAILMAX];
        EventEventCode = new int[OWNERINFO_DOORDETAILMAX];
        EventSystemErrCode = new int[OWNERINFO_DOORDETAILMAX];
        EventNumData = new int[OWNERINFO_DOORDETAILMAX][6];
        EventStrData = new String[OWNERINFO_DOORDETAILMAX];

        // オートカレンダー
        CalGetDate = 0;
        CalModeCount = 0;
        CalModeCode = new int[OWNERINFO_CHAGEMODEMAX];
        CalModeName = new String[OWNERINFO_CHAGEMODEMAX];
        CalDayDate = new int[OWNERINFO_DAYMAX];
        CalDayMode = new int[OWNERINFO_DAYMAX];
        CalDayModeName = new String[OWNERINFO_DAYMAX];
        CalDayWeekKind = new int[OWNERINFO_DAYMAX];
        CalDayHolidayKind = new int[OWNERINFO_DAYMAX];
        CalDayMemo1 = new String[OWNERINFO_DAYMAX];
        CalDayMemo2 = new String[OWNERINFO_DAYMAX];

        // 部屋状況履歴
        RoomHistoryDate = 0;
        RoomHistoryRoomCount = 0;
        RoomHistoryCount = 0;
        RoomHistoryTime = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryEmpty = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryExist = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryClean = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryStop = new int[OWNERINFO_TIMEHOURMAX];

        // メンバーランク
        CustomRankCount = 0;
        CustomRankCode = new int[OWNERINFO_CUSTOMRANKMAX];
        CustomRankName = new String[OWNERINFO_CUSTOMRANKMAX];

        // 精算機ログ
        TexlogGetLogLevel = 0;
        TexlogNextDate = 0;
        TexlogNextTime = 0;
        TexlogNextRoomCode = 0;
        TexlogNextLineNo = 0;
        TexlogNextTermId = 0;
        TexlogPrevDate = 0;
        TexlogPrevTime = 0;
        TexlogPrevRoomCode = 0;
        TexlogPrevLineNo = 0;
        TexlogPrevTermId = 0;
        TexlogCount = 0;
        TexlogDate = new int[OWNERINFO_TEXLOGMAX];
        TexlogTime = new int[OWNERINFO_TEXLOGMAX];
        TexlogRoomCode = new int[OWNERINFO_TEXLOGMAX];
        TexlogRoomName = new String[OWNERINFO_TEXLOGMAX];
        TexlogLineNo = new int[OWNERINFO_TEXLOGMAX];
        TexlogTermId = new int[OWNERINFO_TEXLOGMAX];
        TexlogClaimed = new int[OWNERINFO_TEXLOGMAX];
        TexlogSurplus = new int[OWNERINFO_TEXLOGMAX];
        TexlogSafeCount = new int[OWNERINFO_TEXLOGMAX][OWNERINFO_TEXSAFEMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexlogSafeTotal = new int[OWNERINFO_TEXLOGMAX][OWNERINFO_TEXSAFEMAX];
        TexlogLogLevel = new int[OWNERINFO_TEXLOGMAX];
        TexlogLogContent = new String[OWNERINFO_TEXLOGMAX];
        TexlogLogDetail = new String[OWNERINFO_TEXLOGMAX];
        TexlogStatTrade = new int[OWNERINFO_TEXLOGMAX];
        TexlogStatInOut = new int[OWNERINFO_TEXLOGMAX];
        TexlogStatSecurity = new int[OWNERINFO_TEXLOGMAX];

        AscSalesDetailGetStartDate = 0;
        AscSalesDetailGetEndDate = 0;
        AscSalesDetailStay = 0;
        AscSalesDetailStayBeforeOver = 0;
        AscSalesDetailStayAfterOver = 0;
        AscSalesDetailRest = 0;
        AscSalesDetailRestOver = 0;
        AscSalesDetailTel = 0;
        AscSalesDetailAdvance = 0;
        AscSalesDetailSubTotal = 0;
        AscSalesDetailService = 0;
        AscSalesDetailTaxOut = 0;
        AscSalesDetailTaxIn = 0;
        AscSalesDetailDiscount = 0;
        AscSalesDetailExtra = 0;
        AscSalesDetailMember = 0;
        AscSalesDetailAdjust = 0;
        AscSalesDetailFiller = new int[6];
        AscSalesDetailTotal = 0;

        AscSalesDetailCount = 0;
        AscSalesDetailName = new String[OWNERINFO_SALESDETAILMAX];
        AscSalesDetailAmount = new int[OWNERINFO_SALESDETAILMAX];

        FrontTexTermCodeIn = 999;
        FrontTexTermCount = 0;
        FrontTexTermCode = new int[OWNERINFO_ROOMMAX];
        FrontTexTermName = new String[OWNERINFO_ROOMMAX];
        FrontTexServiceStat = new int[OWNERINFO_ROOMMAX];
        FrontTexKeySwStat = new int[OWNERINFO_ROOMMAX];
        FrontTexSecurityStat = new int[OWNERINFO_ROOMMAX];
        FrontTexDoorStat = new int[OWNERINFO_ROOMMAX];
        FrontTexLineStat = new int[OWNERINFO_ROOMMAX];
        FrontTexErrorStat = new int[OWNERINFO_ROOMMAX];
        FrontTexErrorCode = new String[OWNERINFO_ROOMMAX];
        FrontTexErrorMsg = new String[OWNERINFO_ROOMMAX];
        FrontTexSalesTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSALESMAX];
        FrontTexSalesCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSALESMAX];
        FrontTexSafeCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFEMAX][OWNERINFO_FRONTTEXSAFETYPEMAX];
        FrontTexSafeTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFEMAX];
        FrontTexSafeStat = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFETYPEMAX];

        TimeChartRoomCodeOne = 0;
        TimeChartStartTime = 0;
        TimeChartStatusName = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartStatusColor = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartStatusForeColor = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartRoomCount = 0;
        TimeChartRoomCode = new int[OWNERINFO_ROOMMAX];
        TimeChartRoomName = new String[OWNERINFO_ROOMMAX];
        TimeChartRoomFloor = new int[OWNERINFO_ROOMMAX];
        TimeChartRoomStatus = new int[OWNERINFO_ROOMMAX][OWNERINFO_TIMETABLEMAX];

        ManualSalesDetailGetStartDate = 0;
        ManualSalesDetailGetEndDate = 0;
        ManualSalesDetailTaxIn = 0;
        ManualSalesDetailTotal = 0;
        ManualSalesDetailCount = 0;
        ManualSalesDetailName = new String[OWNERINFO_MANUALSALESMAX];
        ManualSalesDetailAmount = new int[OWNERINFO_MANUALSALESMAX];

        log = new LogLib();
    }

    // ------------------------------------------------------------------------------
    //
    // 電文処理
    //
    // ------------------------------------------------------------------------------
    /**
     * 電文送信処理(0100)
     * オーナーサイトログイン
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0100()
    {
        return(sendPacket0100Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0100)
     * オーナーサイトログイン
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0100(int kind, String value)
    {
        return(sendPacket0100Sub( kind, value ));
    }

    /**
     * 電文送信処理(0100)
     * オーナーサイトログイン
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0100Sub(int kind, String value)
    {
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;

        // データのクリア
        Result = 0;
        Name = "";
        Addupdate = 0;
        SystemKind = "";
        SystemVer1 = 0;
        SystemVer2 = 0;
        SystemVer3 = 0;
        SystemVer4 = 0;
        SystemCustomerKind = "";

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0100";
                // 端末ID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0101" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 担当者名
                            data = new String( cdata, 53, 20 );
                            Name = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 計上日付
                            data = new String( cdata, 73, 8 );
                            Addupdate = Integer.valueOf( data ).intValue();

                            // システム種別
                            data = new String( cdata, 81, 1 );
                            SystemKind = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // システムバージョン1
                            data = new String( cdata, 82, 5 );
                            SystemVer1 = Integer.valueOf( data.replace( " ", "0" ) ).intValue();

                            // システムバージョン2
                            data = new String( cdata, 87, 5 );
                            SystemVer2 = Integer.valueOf( data.replace( " ", "0" ) ).intValue();

                            // システムバージョン3
                            data = new String( cdata, 92, 5 );
                            SystemVer3 = Integer.valueOf( data.replace( " ", "0" ) ).intValue();

                            // システムバージョン4
                            data = new String( cdata, 97, 5 );
                            SystemVer4 = Integer.valueOf( data.replace( " ", "0" ) ).intValue();

                            // 顧客システム種別
                            data = new String( cdata, 102, 1 );
                            SystemCustomerKind = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0100:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0102)
     * 売上情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0102()
    {
        return(sendPacket0102Sub( 0, "", 0 ));
    }

    /**
     * 電文送信処理(0102)
     * 売上情報取得
     * 
     * @param updateFlag 更新方法(0:必ずDB更新する、1:DBに登録がなければ更新する）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0102(int updateFlag)
    {
        return(sendPacket0102Sub( 0, "", updateFlag ));
    }

    /**
     * 電文送信処理(0102)
     * 売上情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0102(int kind, String value)
    {
        return(sendPacket0102Sub( kind, value, 0 ));
    }

    /**
     * 電文送信処理(0102)
     * 売上情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @param updateFlag 更新方法(0:必ずDB更新する、1:DBに登録がなければ更新する）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0102(int kind, String value, int updateFlag)
    {
        return(sendPacket0102Sub( kind, value, updateFlag ));
    }

    /**
     * 電文送信処理(0102)
     * 売上情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @param updateFlag 更新方法(0:必ずDB更新する、1:DBに登録がなければ更新する）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0102Sub(int kind, String value, int updateFlag)
    {

        // 異常日付があるかどうかをチェック
        if ( !dateCheck( SalesGetStartDate, SalesGetEndDate, Addupdate ) )
        {
            return(false);
        }

        boolean connectFlag = true;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;
        String element_data[] = new String[25];
        int element_value[] = new int[25];
        DateEdit date = new DateEdit();

        // データのクリア
        Result = 0;
        SalesTex = 0;
        SalesTexCredit = 0;
        SalesFront = 0;
        SalesFrontCredit = 0;
        SalesRestTotal = 0;
        SalesStayTotal = 0;
        SalesOtherTotal = 0;
        SalesTotal = 0;
        SalesRestCount = 0;
        SalesStayCount = 0;
        SalesTotalCount = 0;
        SalesNowCheckin = 0;
        SalesRestRate = 0;
        SalesStayRate = 0;
        SalesTotalRate = 0;
        SalesRestPrice = 0;
        SalesStayPrice = 0;
        SalesTotalPrice = 0;
        SalesVisitorPrice = 0;
        SalesMemberPrice = 0;
        SalesRoomPrice = 0;
        SalesRoomTotalPrice = 0;
        SalesPayRestCount = 0;
        SalesPayStayCount = 0;
        SalesPointTotal = 0;
        element_data[0] = "SalesTex";
        element_data[1] = "SalesTexCredit";
        element_data[2] = "SalesFront";
        element_data[3] = "SalesFrontCredit";
        element_data[4] = "SalesRestTotal";
        element_data[5] = "SalesStayTotal";
        element_data[6] = "SalesOtherTotal";
        element_data[7] = "SalesTotal";
        element_data[8] = "SalesRestCount";
        element_data[9] = "SalesStayCount";
        element_data[10] = "SalesTotalCount";
        element_data[11] = "SalesNowCheckin";
        element_data[12] = "SalesRestRate";
        element_data[13] = "SalesStayRate";
        element_data[14] = "SalesTotalRate";
        element_data[15] = "SalesRestPrice";
        element_data[16] = "SalesStayPrice";
        element_data[17] = "SalesTotalPrice";
        element_data[18] = "SalesVisitorPrice";
        element_data[19] = "SalesMemberPrice";
        element_data[20] = "SalesRoomPrice";
        element_data[21] = "SalesRoomTotalPrice";
        element_data[22] = "SalesPayRestCount";
        element_data[23] = "SalesPayStayCount";
        element_data[24] = "SalesPointTotal";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String paramHotelId = (kind == 1 ? value : HotelId);

        if ( updateFlag == 1 )
        {

            // hotel_salesからデータ読み込み
            String query = "SELECT (SELECT value FROM hotel_sales WHERE hotel_id = ? AND manage_date=? AND field_name=? AND sub = 0)";
            for( int i = 1 ; i < element_data.length ; i++ )
            {
                query += ",(SELECT value FROM hotel_sales WHERE hotel_id = ? AND manage_date=? AND field_name=? AND sub = 0)";
            }
            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                for( int i = 0 ; i < element_data.length ; i++ )
                {
                    prestate.setString( i * 3 + 1, paramHotelId );
                    prestate.setInt( i * 3 + 2, SalesGetStartDate );
                    prestate.setString( i * 3 + 3, element_data[i] );
                }
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        SalesTex = result.getString( 1 ) == null ? 0 : result.getInt( 1 );
                        SalesTexCredit = result.getString( 2 ) == null ? 0 : result.getInt( 2 );
                        SalesFront = result.getString( 3 ) == null ? 0 : result.getInt( 3 );
                        SalesFrontCredit = result.getString( 4 ) == null ? 0 : result.getInt( 4 );
                        SalesRestTotal = result.getString( 5 ) == null ? 0 : result.getInt( 5 );
                        SalesStayTotal = result.getString( 6 ) == null ? 0 : result.getInt( 6 );
                        SalesOtherTotal = result.getString( 7 ) == null ? 0 : result.getInt( 7 );
                        SalesTotal = result.getString( 8 ) == null ? 0 : result.getInt( 8 );
                        SalesRestCount = result.getString( 9 ) == null ? 0 : result.getInt( 9 );
                        SalesStayCount = result.getString( 10 ) == null ? 0 : result.getInt( 10 );
                        SalesTotalCount = result.getString( 11 ) == null ? 0 : result.getInt( 11 );
                        SalesNowCheckin = result.getString( 12 ) == null ? 0 : result.getInt( 12 );
                        SalesRestRate = result.getString( 13 ) == null ? 0 : result.getInt( 13 );
                        SalesStayRate = result.getString( 14 ) == null ? 0 : result.getInt( 14 );
                        SalesTotalRate = result.getString( 15 ) == null ? 0 : result.getInt( 15 );
                        SalesRestPrice = result.getString( 16 ) == null ? 0 : result.getInt( 16 );
                        SalesStayPrice = result.getString( 17 ) == null ? 0 : result.getInt( 17 );
                        SalesTotalPrice = result.getString( 18 ) == null ? 0 : result.getInt( 18 );
                        SalesVisitorPrice = result.getString( 19 ) == null ? 0 : result.getInt( 19 );
                        SalesMemberPrice = result.getString( 20 ) == null ? 0 : result.getInt( 20 );
                        SalesRoomPrice = result.getString( 21 ) == null ? 0 : result.getInt( 21 );
                        SalesRoomTotalPrice = result.getString( 22 ) == null ? 0 : result.getInt( 22 );
                        SalesPayRestCount = result.getString( 23 ) == null ? 0 : result.getInt( 23 );
                        SalesPayStayCount = result.getString( 24 ) == null ? 0 : result.getInt( 24 );
                        SalesPointTotal = result.getString( 25 ) == null ? 0 : result.getInt( 25 );
                        if ( SalesTotal != 0 )
                            connectFlag = false;
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[ownerInfo.sendPacket0102() SELECT hotel_sales ERROR] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }
        if ( connectFlag )
        {
            if ( paramHotelId != null )
            {
                if ( SalesGetStartDate <= 0 )
                {
                    Result = 1;
                    return(true);
                }

                // ホスト接続処理
                tcpclient = new TcpClient();
                ret = connect( tcpclient, kind, value );

                if ( ret != false )
                {
                    format = new StringFormat();

                    // コマンド
                    senddata = "0102";
                    // ユーザID
                    senddata = senddata + format.leftFitFormat( TermId, 11 );
                    // パスワード
                    senddata = senddata + format.leftFitFormat( Password, 4 );
                    // 取得開始日付
                    nf = new DecimalFormat( "00000000" );
                    senddata = senddata + nf.format( SalesGetStartDate );
                    // 取得終了日付
                    nf = new DecimalFormat( "00000000" );
                    senddata = senddata + nf.format( SalesGetEndDate );
                    // 予備
                    senddata = senddata + "          ";

                    // 電文ヘッダの取得
                    if ( kind == 1 )
                    {
                        header = tcpclient.getPacketHeader( value, senddata.length() );
                    }
                    else
                    {
                        header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                    }
                    // 電文の結合
                    senddata = header + senddata;

                    try
                    {
                        // 電文送信
                        tcpclient.send( senddata );

                        // 受信待機
                        recvdata = tcpclient.recv();
                        if ( recvdata != null )
                        {
                            cdata = new char[recvdata.length()];
                            cdata = recvdata.toCharArray();

                            // コマンド取得
                            data = new String( cdata, 32, 4 );
                            if ( data.compareTo( "0103" ) == 0 )
                            {
                                // 処理結果
                                data = new String( cdata, 51, 2 );
                                Result = Integer.valueOf( data ).intValue();

                                // 開始日付
                                data = new String( cdata, 53, 8 );
                                SalesGetStartDate = Integer.valueOf( data ).intValue();

                                // 終了日付
                                data = new String( cdata, 61, 8 );
                                SalesGetEndDate = Integer.valueOf( data ).intValue();

                                // 精算機現金売上
                                data = new String( cdata, 69, 9 );
                                SalesTex = Integer.valueOf( data ).intValue();

                                // 精算機クレジット売上
                                data = new String( cdata, 78, 9 );
                                SalesTexCredit = Integer.valueOf( data ).intValue();

                                // フロント現金売上
                                data = new String( cdata, 87, 9 );
                                SalesFront = Integer.valueOf( data ).intValue();

                                // フロントクレジット売上
                                data = new String( cdata, 96, 9 );
                                SalesFrontCredit = Integer.valueOf( data ).intValue();

                                // 売上合計
                                data = new String( cdata, 105, 9 );
                                SalesTotal = Integer.valueOf( data ).intValue();

                                // 休憩組数
                                data = new String( cdata, 114, 4 );
                                SalesRestCount = Integer.valueOf( data ).intValue();

                                // 宿泊組数
                                data = new String( cdata, 118, 4 );
                                SalesStayCount = Integer.valueOf( data ).intValue();

                                // 合計組数
                                data = new String( cdata, 122, 5 );
                                SalesTotalCount = Integer.valueOf( data ).intValue();

                                // 現在入室数
                                data = new String( cdata, 127, 3 );
                                SalesNowCheckin = Integer.valueOf( data ).intValue();

                                // 休憩回転率
                                data = new String( cdata, 130, 5 );
                                SalesRestRate = Integer.valueOf( data ).intValue();

                                // 宿泊回転率
                                data = new String( cdata, 135, 5 );
                                SalesStayRate = Integer.valueOf( data ).intValue();

                                // 合計回転率
                                data = new String( cdata, 140, 5 );
                                SalesTotalRate = Integer.valueOf( data ).intValue();

                                // 休憩客単価
                                data = new String( cdata, 145, 9 );
                                SalesRestPrice = Integer.valueOf( data ).intValue();

                                // 宿泊客単価
                                data = new String( cdata, 154, 9 );
                                SalesStayPrice = Integer.valueOf( data ).intValue();

                                // 合計客単価
                                data = new String( cdata, 163, 9 );
                                SalesTotalPrice = Integer.valueOf( data ).intValue();

                                // ビジター客単価
                                data = new String( cdata, 172, 9 );
                                SalesVisitorPrice = Integer.valueOf( data ).intValue();

                                // メンバー客単価
                                data = new String( cdata, 181, 9 );
                                SalesMemberPrice = Integer.valueOf( data ).intValue();

                                // 部屋単価
                                data = new String( cdata, 190, 9 );
                                SalesRoomPrice = Integer.valueOf( data ).intValue();

                                try
                                {
                                    // 累計部屋単価
                                    data = new String( cdata, 199, 9 );
                                    SalesRoomTotalPrice = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesRoomTotalPrice = 0;
                                }
                                try
                                {
                                    // 休憩売上
                                    data = new String( cdata, 208, 9 );
                                    SalesRestTotal = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesRestTotal = 0;
                                }
                                try
                                {
                                    // 宿泊売上
                                    data = new String( cdata, 217, 9 );
                                    SalesStayTotal = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesStayTotal = 0;
                                }
                                try
                                {
                                    // 室外売上
                                    data = new String( cdata, 226, 9 );
                                    SalesOtherTotal = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesOtherTotal = 0;
                                }
                                try
                                {
                                    // 休憩前受済組数
                                    data = new String( cdata, 235, 4 );
                                    SalesPayRestCount = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesPayRestCount = 0;
                                }
                                try
                                {
                                    // 宿泊前受済組数
                                    data = new String( cdata, 239, 4 );
                                    SalesPayStayCount = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesPayStayCount = 0;
                                }
                                try
                                {
                                    // 提携ポイント
                                    data = new String( cdata, 243, 9 );
                                    SalesPointTotal = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesPointTotal = 0;
                                }
                                // DB書き込み ただし当日は書き込まない。
                                if ( (SalesGetEndDate == 0 || SalesGetStartDate == SalesGetEndDate)
                                        && Integer.parseInt( date.getDate( 2 ) ) > date.addDay( SalesGetStartDate, 1 ) && Addupdate > SalesGetStartDate
                                        && !(SalesGetStartDate % 100 == 0 && (SalesGetStartDate / 100 >= Addupdate / 100 || SalesGetStartDate / 100 >= Integer.parseInt( date.getDate( 2 ) ))) )
                                {
                                    element_value[0] = SalesTex;
                                    element_value[1] = SalesTexCredit;
                                    element_value[2] = SalesFront;
                                    element_value[3] = SalesFrontCredit;
                                    element_value[4] = SalesRestTotal;
                                    element_value[5] = SalesStayTotal;
                                    element_value[6] = SalesOtherTotal;
                                    element_value[7] = SalesTotal;
                                    element_value[8] = SalesRestCount;
                                    element_value[9] = SalesStayCount;
                                    element_value[10] = SalesTotalCount;
                                    element_value[11] = SalesNowCheckin;
                                    element_value[12] = SalesRestRate;
                                    element_value[13] = SalesStayRate;
                                    element_value[14] = SalesTotalRate;
                                    element_value[15] = SalesRestPrice;
                                    element_value[16] = SalesStayPrice;
                                    element_value[17] = SalesTotalPrice;
                                    element_value[18] = SalesVisitorPrice;
                                    element_value[19] = SalesMemberPrice;
                                    element_value[20] = SalesRoomPrice;
                                    element_value[21] = SalesRoomTotalPrice;
                                    element_value[22] = SalesPayRestCount;
                                    element_value[23] = SalesPayStayCount;
                                    element_value[24] = SalesPointTotal;
                                    String query = "DELETE FROM `hotel_sales` WHERE `hotel_id` = ? AND `manage_date` = ?";
                                    try
                                    {
                                        connection = DBConnection.getConnection();
                                        prestate = connection.prepareStatement( query );
                                        prestate.setString( 1, paramHotelId );
                                        prestate.setInt( 2, SalesGetStartDate );
                                        prestate.executeUpdate();
                                    }
                                    catch ( Exception e )
                                    {
                                        Logging.error( "[ownerInfo.sendPacket0102() DELETE hotel_sales ERROR] Exception=" + e.toString() );
                                    }
                                    finally
                                    {
                                        DBConnection.releaseResources( prestate );
                                    }

                                    query = "INSERT INTO `hotel_sales` (`hotel_id`, `manage_date`, `field_name`, `sub`, `value`, `text`, `last_update`, `last_uptime`) VALUES";
                                    query += " ( ?, ?, ?, 0, ?, '', ?, ?)";
                                    for( int i = 1 ; i < element_data.length ; i++ )
                                    {
                                        query += ",( ?, ?, ?, 0, ?, '', ?, ?)";
                                    }
                                    try
                                    {
                                        prestate = connection.prepareStatement( query );
                                        for( int i = 0 ; i < element_data.length ; i++ )
                                        {
                                            prestate.setString( i * 6 + 1, paramHotelId );
                                            prestate.setInt( i * 6 + 2, SalesGetStartDate );
                                            prestate.setString( i * 6 + 3, element_data[i] );
                                            prestate.setInt( i * 6 + 4, element_value[i] );
                                            prestate.setInt( i * 6 + 5, Integer.parseInt( date.getDate( 2 ) ) );
                                            prestate.setInt( i * 6 + 6, Integer.parseInt( date.getTime( 1 ) ) );
                                        }
                                        prestate.executeUpdate();
                                    }
                                    catch ( Exception e )
                                    {
                                        Logging.error( "[ownerInfo.sendPacket0102() INSERT hotel_sales ERROR] Exception=" + e.toString() );
                                    }
                                    finally
                                    {
                                        DBConnection.releaseResources( prestate );
                                        DBConnection.releaseResources( connection );
                                    }
                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        log.error( "sendPacket0102:" + e.toString() );
                        return(false);
                    }

                    tcpclient.disconnectService();

                    Logging.info( "[ownerInfo.sendPacket0102() Addupdate=" + Addupdate + ",SalesGetStartDate=" + SalesGetStartDate + ",SalesGetEndDate=" + SalesGetEndDate + ",HotelId=" + HotelId + ",value=" + value + ",SalesTotal" + SalesTotal );

                    return(true);
                }
            }
            return(false);
        }
        return(true);
    }

    /**
     * 電文送信処理(0104)
     * 売上詳細情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0104()
    {
        return(sendPacket0104Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0104)
     * 売上詳細情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0104(int kind, String value)
    {
        return(sendPacket0104Sub( kind, value ));
    }

    /**
     * 電文送信処理(0104)
     * 売上詳細情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0104Sub(int kind, String value)
    {

        if ( !dateCheck( SalesDetailGetStartDate, SalesDetailGetEndDate ) )
        {
            return(false);
        }

        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        SalesDetailRest = 0;
        SalesDetailStay = 0;
        SalesDetailRestBeforeOver = 0;
        SalesDetailRestAfterOver = 0;
        SalesDetailStayBeforeOver = 0;
        SalesDetailStayAfterOver = 0;
        SalesDetailMeat = 0;
        SalesDetailDelivery = 0;
        SalesDetailConveni = 0;
        SalesDetailRef = 0;
        SalesDetailMulti = 0;
        SalesDetailSales = 0;
        SalesDetailRental = 0;
        SalesDetailCigarette = 0;
        SalesDetailTel = 0;
        SalesDetailEtc = 0;
        SalesDetailStaxIn = 0;
        SalesDetailFiller = new int[9];
        SalesDetailDiscount = 0;
        SalesDetailExtra = 0;
        SalesDetailMember = 0;
        SalesDetailService = 0;
        SalesDetailStax = 0;
        SalesDetailAdjust = 0;
        SalesDetailTotal = 0;
        SalesDetailTaxRate1 = 0;
        SalesDetailTaxableAmount1 = 0;
        SalesDetailTaxRate2 = 0;
        SalesDetailTaxableAmount2 = 0;

        if ( HotelId != null )
        {
            if ( SalesDetailGetStartDate <= 0 )
            {
                Result = 1;
                return(true);
            }

            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0104";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 取得開始日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( SalesDetailGetStartDate );
                // 取得終了日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( SalesDetailGetEndDate );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0105" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 開始日付
                            data = new String( cdata, 53, 8 );
                            SalesDetailGetStartDate = Integer.valueOf( data ).intValue();

                            // 終了日付
                            data = new String( cdata, 61, 8 );
                            SalesDetailGetEndDate = Integer.valueOf( data ).intValue();

                            // 休憩
                            data = new String( cdata, 69, 9 );
                            SalesDetailRest = Integer.valueOf( data ).intValue();

                            // 宿泊
                            data = new String( cdata, 78, 9 );
                            SalesDetailStay = Integer.valueOf( data ).intValue();

                            // 休憩前延長
                            data = new String( cdata, 87, 9 );
                            SalesDetailRestBeforeOver = Integer.valueOf( data ).intValue();

                            // 休憩後延長
                            data = new String( cdata, 96, 9 );
                            SalesDetailRestAfterOver = Integer.valueOf( data ).intValue();

                            // 宿泊前延長
                            data = new String( cdata, 105, 9 );
                            SalesDetailStayBeforeOver = Integer.valueOf( data ).intValue();

                            // 宿泊後延長
                            data = new String( cdata, 114, 9 );
                            SalesDetailStayAfterOver = Integer.valueOf( data ).intValue();

                            // 飲食売上
                            data = new String( cdata, 123, 9 );
                            SalesDetailMeat = Integer.valueOf( data ).intValue();

                            // 出前売上
                            data = new String( cdata, 132, 9 );
                            SalesDetailDelivery = Integer.valueOf( data ).intValue();

                            // コンビニ売上
                            data = new String( cdata, 141, 9 );
                            SalesDetailConveni = Integer.valueOf( data ).intValue();

                            // 冷蔵庫売上
                            data = new String( cdata, 150, 9 );
                            SalesDetailRef = Integer.valueOf( data ).intValue();

                            // マルチメディア売上
                            data = new String( cdata, 159, 9 );
                            SalesDetailMulti = Integer.valueOf( data ).intValue();

                            // 販売商品売上
                            data = new String( cdata, 168, 9 );
                            SalesDetailSales = Integer.valueOf( data ).intValue();

                            // レンタル売上
                            data = new String( cdata, 177, 9 );
                            SalesDetailRental = Integer.valueOf( data ).intValue();

                            // たばこ売上
                            data = new String( cdata, 186, 9 );
                            SalesDetailCigarette = Integer.valueOf( data ).intValue();

                            // 電話売上
                            data = new String( cdata, 195, 9 );
                            SalesDetailTel = Integer.valueOf( data ).intValue();

                            // その他売上
                            data = new String( cdata, 204, 9 );
                            SalesDetailEtc = Integer.valueOf( data ).intValue();

                            // 内消費税額
                            data = new String( cdata, 213, 9 );
                            SalesDetailStaxIn = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 222, 9 );
                            SalesDetailFiller[0] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 231, 9 );
                            SalesDetailFiller[1] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 240, 9 );
                            SalesDetailFiller[2] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 249, 9 );
                            SalesDetailFiller[3] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 258, 9 );
                            SalesDetailFiller[4] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 267, 9 );
                            SalesDetailFiller[5] = Integer.valueOf( data ).intValue();

                            // 割引売上
                            data = new String( cdata, 276, 9 );
                            SalesDetailDiscount = Integer.valueOf( data ).intValue();

                            // 割増売上
                            data = new String( cdata, 285, 9 );
                            SalesDetailExtra = Integer.valueOf( data ).intValue();

                            // メンバー割引売上
                            data = new String( cdata, 294, 9 );
                            SalesDetailMember = Integer.valueOf( data ).intValue();

                            // 奉仕料売上
                            data = new String( cdata, 303, 9 );
                            SalesDetailService = Integer.valueOf( data ).intValue();

                            // 消費税売上
                            data = new String( cdata, 312, 9 );
                            SalesDetailStax = Integer.valueOf( data ).intValue();

                            // 調整金売上
                            data = new String( cdata, 321, 9 );
                            SalesDetailAdjust = Integer.valueOf( data ).intValue();

                            // 総合計
                            data = new String( cdata, 330, 9 );
                            SalesDetailTotal = Integer.valueOf( data ).intValue();

                            // 税率1 ･･･ 0:適用無し(単位0.1%,例10%⇒100)
                            data = new String( cdata, 339, 4 );
                            SalesDetailTaxRate1 = Integer.valueOf( data ).intValue();

                            // 税率1課税対象金額
                            data = new String( cdata, 343, 9 );
                            SalesDetailTaxableAmount1 = Integer.valueOf( data ).intValue();

                            // 税率2 ･･･ 0:適用無し(単位0.1%,例8%⇒80)
                            data = new String( cdata, 352, 4 );
                            SalesDetailTaxRate2 = Integer.valueOf( data ).intValue();

                            // 税率2課税対象金額
                            data = new String( cdata, 356, 9 );
                            SalesDetailTaxableAmount2 = Integer.valueOf( data ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0104:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0106)
     * IN/OUT組数情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0106()
    {
        return(sendPacket0106Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0106)
     * IN/OUT組数情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0106(int kind, String value)
    {
        return(sendPacket0106Sub( kind, value ));
    }

    /**
     * 電文送信処理(0106)
     * IN/OUT組数情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0106Sub(int kind, String value)
    {

        if ( !dateCheck( InOutGetStartDate, InOutGetEndDate ) )
        {
            return(false);
        }

        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        InOutTime = new int[OWNERINFO_TIMEHOURMAX];
        InOutIn = new int[OWNERINFO_TIMEHOURMAX];
        InOutOut = new int[OWNERINFO_TIMEHOURMAX];

        if ( HotelId != null )
        {
            if ( InOutGetStartDate <= 0 )
            {
                Result = 1;
                return(true);
            }

            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0106";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 取得開始日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( InOutGetStartDate );
                // 取得終了日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( InOutGetEndDate );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0107" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 開始日付
                            data = new String( cdata, 53, 8 );
                            InOutGetStartDate = Integer.valueOf( data ).intValue();

                            // 終了日付
                            data = new String( cdata, 61, 8 );
                            InOutGetEndDate = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < OWNERINFO_TIMEHOURMAX ; i++ )
                            {
                                // 時間
                                data = new String( cdata, 69 + (i * 12), 4 );
                                InOutTime[i] = Integer.valueOf( data ).intValue();

                                // ＩＮ組数
                                data = new String( cdata, 73 + (i * 12), 4 );
                                InOutIn[i] = Integer.valueOf( data ).intValue();

                                // ＯＵＴ組数
                                data = new String( cdata, 77 + (i * 12), 4 );
                                InOutOut[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0106:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0108)
     * 部屋ステータス情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0108()
    {
        return(sendPacket0108Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0108)
     * 部屋ステータス情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0108(int kind, String value)
    {
        return(sendPacket0108Sub( kind, value ));
    }

    /**
     * 電文送信処理(0108)
     * 部屋ステータス情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0108Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;

        // データのクリア
        Result = 0;
        StatusEmptyFullMode = 0;
        StatusEmptyFullState = 0;
        StatusWaiting = 0;
        StatusName = new String[OWNERINFO_ROOMSTATUSMAX];
        StatusCount = new int[OWNERINFO_ROOMSTATUSMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0108";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0109" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 空満運用モード
                            data = new String( cdata, 53, 1 );
                            StatusEmptyFullMode = Integer.valueOf( data ).intValue();

                            // 空満状態
                            data = new String( cdata, 54, 1 );
                            StatusEmptyFullState = Integer.valueOf( data ).intValue();

                            // ウェイティング数
                            data = new String( cdata, 55, 3 );
                            StatusWaiting = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < OWNERINFO_ROOMSTATUSMAX ; i++ )
                            {
                                // ステータス名
                                data = new String( cdata, 127 + (i * 15), 12 );
                                StatusName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 部屋数
                                data = new String( cdata, 139 + (i * 15), 3 );
                                StatusCount[i] = Integer.valueOf( data ).intValue();
                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0108:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0110)
     * 部屋ステータス詳細情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0110()
    {
        return(sendPacket0110Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0110)
     * 部屋ステータス詳細情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0110(int kind, String value)
    {
        return(sendPacket0110Sub( kind, value ));
    }

    /**
     * 電文送信処理(0110)
     * 部屋ステータス詳細情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0110Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        StatusDetailCount = 0;
        StatusDetailRoomCode = new int[OWNERINFO_ROOMMAX];
        StatusDetailRoomName = new String[OWNERINFO_ROOMMAX];
        StatusDetailElapseTime = new int[OWNERINFO_ROOMMAX];
        StatusDetailStatusName = new String[OWNERINFO_ROOMMAX];
        StatusDetailColor = new String[OWNERINFO_ROOMMAX];
        StatusDetailForeColor = new String[OWNERINFO_ROOMMAX];
        StatusDetailX = new int[OWNERINFO_ROOMMAX];
        StatusDetailY = new int[OWNERINFO_ROOMMAX];
        StatusDetailZ = new int[OWNERINFO_ROOMMAX];
        StatusDetailFloor = new int[OWNERINFO_ROOMMAX];
        StatusDetailUserChargeMode = new int[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0110";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0111" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋数
                            data = new String( cdata, 53, 3 );
                            StatusDetailCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < StatusDetailCount ; i++ )
                            {
                                // 部屋コード
                                data = new String( cdata, 56 + (i * 128), 3 );
                                StatusDetailRoomCode[i] = Integer.valueOf( data ).intValue();

                                // 部屋名称
                                data = new String( cdata, 59 + (i * 128), 8 );
                                StatusDetailRoomName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 経過時間
                                data = new String( cdata, 67 + (i * 128), 6 );
                                StatusDetailElapseTime[i] = Integer.valueOf( data ).intValue();

                                // ステータス名
                                data = new String( cdata, 73 + (i * 128), 12 );
                                StatusDetailStatusName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ステータス色
                                data = new String( cdata, 85 + (i * 128), 6 );
                                StatusDetailColor[i] = data.trim();

                                // ステータス文字色
                                data = new String( cdata, 91 + (i * 128), 6 );
                                StatusDetailForeColor[i] = data.trim();

                                // 表示位置X
                                data = new String( cdata, 97 + (i * 128), 2 );
                                StatusDetailX[i] = Integer.valueOf( data ).intValue();

                                // 表示位置Y
                                data = new String( cdata, 99 + (i * 128), 2 );
                                StatusDetailY[i] = Integer.valueOf( data ).intValue();

                                // 表示位置Z
                                data = new String( cdata, 101 + (i * 128), 2 );
                                StatusDetailZ[i] = Integer.valueOf( data ).intValue();

                                // フロア番号
                                data = new String( cdata, 103 + (i * 128), 2 );
                                StatusDetailFloor[i] = Integer.valueOf( data ).intValue();

                                try
                                {
                                    // 予定室料適用区分
                                    data = new String( cdata, 105 + (i * 128), 2 );
                                    StatusDetailUserChargeMode[i] = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    StatusDetailUserChargeMode[i] = 0;
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0110:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0112)
     * 締単位IN/OUT組数情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0112()
    {
        return(sendPacket0112Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0112)
     * 締単位IN/OUT組数情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0112(int kind, String value)
    {
        return(sendPacket0112Sub( kind, value ));
    }

    /**
     * 電文送信処理(0112)
     * 締単位IN/OUT組数情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0112Sub(int kind, String value)
    {

        if ( !dateCheck( AddupInOutGetDate ) )
        {
            return(false);
        }

        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        AddupInOutAfterIn = 0;
        AddupInOutBeforeIn = 0;
        AddupInOutAllOut = 0;
        AddupInOutBeforeOut = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0112";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 取得日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( AddupInOutGetDate );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0113" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 日付
                            data = new String( cdata, 53, 8 );
                            AddupInOutGetDate = Integer.valueOf( data ).intValue();

                            // 締後ＩＮ数
                            data = new String( cdata, 61, 4 );
                            AddupInOutAfterIn = Integer.valueOf( data ).intValue();

                            // 締前ＩＮ数
                            data = new String( cdata, 65, 4 );
                            AddupInOutBeforeIn = Integer.valueOf( data ).intValue();

                            // 総ＯＵＴ組数
                            data = new String( cdata, 69, 4 );
                            AddupInOutAllOut = Integer.valueOf( data ).intValue();

                            // 締前ＯＵＴ数
                            data = new String( cdata, 73, 4 );
                            AddupInOutBeforeOut = Integer.valueOf( data ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0112:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0114)
     * 部屋利用状況
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0114()
    {
        return(sendPacket0114Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0114)
     * 部屋利用状況
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0114(int kind, String value)
    {
        return(sendPacket0114Sub( kind, value ));
    }

    /**
     * 電文送信処理(0114)
     * 部屋利用状況
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0114Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        EmployeeName = "";
        ModeName = "";
        StateRoomCount = 0;
        StateRoomCode = new int[OWNERINFO_ROOMMAX];
        StateInDate = new int[OWNERINFO_ROOMMAX];
        StateInTime = new int[OWNERINFO_ROOMMAX];
        StateOutDate = new int[OWNERINFO_ROOMMAX];
        StateOutTime = new int[OWNERINFO_ROOMMAX];
        StatePerson = new int[OWNERINFO_ROOMMAX];
        StateDoor = new int[OWNERINFO_ROOMMAX];
        StateRefUse = new int[OWNERINFO_ROOMMAX];
        StateConveniUse = new int[OWNERINFO_ROOMMAX];
        StateLucky = new int[OWNERINFO_ROOMMAX];
        StateCustomId = new String[OWNERINFO_ROOMMAX];
        StateNickName = new String[OWNERINFO_ROOMMAX];
        StateCustomRankName = new String[OWNERINFO_ROOMMAX];
        StateCustomEvent = new int[OWNERINFO_ROOMMAX];
        StateCustomWarning = new int[OWNERINFO_ROOMMAX];
        StateCustomContact = new int[OWNERINFO_ROOMMAX];
        StateCustomPresent = new int[OWNERINFO_ROOMMAX];
        StateReserveTime = new int[OWNERINFO_ROOMMAX];
        StateParkingNo = new int[OWNERINFO_ROOMMAX];
        StateCustomUserId = new String[OWNERINFO_ROOMMAX];
        StateHapihoteTouch = new int[OWNERINFO_ROOMMAX];
        StateHapihoteMile = new int[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0114";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0115" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 従業員名
                            data = new String( cdata, 53, 20 );
                            EmployeeName = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 料金モード名称
                            data = new String( cdata, 73, 20 );
                            ModeName = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋数
                            data = new String( cdata, 93, 3 );
                            StateRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < StateRoomCount ; i++ )
                            {
                                // 部屋コード
                                data = new String( cdata, 96 + (i * 256), 3 );
                                StateRoomCode[i] = Integer.valueOf( data ).intValue();

                                // 入室日付
                                data = new String( cdata, 99 + (i * 256), 8 );
                                StateInDate[i] = Integer.valueOf( data ).intValue();

                                // 入室時間
                                data = new String( cdata, 107 + (i * 256), 4 );
                                StateInTime[i] = Integer.valueOf( data ).intValue();

                                // 退室日付
                                data = new String( cdata, 111 + (i * 256), 8 );
                                StateOutDate[i] = Integer.valueOf( data ).intValue();

                                // 退室時間
                                data = new String( cdata, 119 + (i * 256), 4 );
                                StateOutTime[i] = Integer.valueOf( data ).intValue();

                                // 利用人数
                                data = new String( cdata, 123 + (i * 256), 2 );
                                StatePerson[i] = Integer.valueOf( data ).intValue();

                                // ドア状態
                                data = new String( cdata, 125 + (i * 256), 1 );
                                StateDoor[i] = Integer.valueOf( data ).intValue();

                                // 冷蔵庫利用状態
                                data = new String( cdata, 126 + (i * 256), 1 );
                                StateRefUse[i] = Integer.valueOf( data ).intValue();

                                // コンビニ利用状態
                                data = new String( cdata, 127 + (i * 256), 1 );
                                StateConveniUse[i] = Integer.valueOf( data ).intValue();

                                // ラッキールーム
                                data = new String( cdata, 128 + (i * 256), 1 );
                                StateLucky[i] = Integer.valueOf( data ).intValue();

                                // メンバーＩＤ
                                data = new String( cdata, 129 + (i * 256), 9 );
                                StateCustomId[i] = data.trim();

                                // ニックネーム
                                data = new String( cdata, 138 + (i * 256), 20 );
                                StateNickName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // メンバーランク名
                                data = new String( cdata, 158 + (i * 256), 40 );
                                StateCustomRankName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // メンバーイベント
                                data = new String( cdata, 198 + (i * 256), 1 );
                                StateCustomEvent[i] = Integer.valueOf( data ).intValue();

                                // 警告情報
                                data = new String( cdata, 199 + (i * 256), 1 );
                                StateCustomWarning[i] = Integer.valueOf( data ).intValue();

                                // 連絡情報
                                data = new String( cdata, 200 + (i * 256), 1 );
                                StateCustomContact[i] = Integer.valueOf( data ).intValue();

                                // 景品情報
                                data = new String( cdata, 201 + (i * 256), 1 );
                                StateCustomPresent[i] = Integer.valueOf( data ).intValue();

                                // 予約時間
                                data = new String( cdata, 202 + (i * 256), 4 );
                                StateReserveTime[i] = Integer.valueOf( data ).intValue();

                                // 駐車場番号
                                data = new String( cdata, 206 + (i * 256), 3 );
                                StateParkingNo[i] = Integer.valueOf( data ).intValue();

                                // ユーザID
                                data = new String( cdata, 209 + (i * 256), 32 );
                                StateCustomUserId[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                try
                                {
                                    // ハピホテタッチ
                                    data = new String( cdata, 241 + (i * 256), 1 );
                                    StateHapihoteTouch[i] = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    StateHapihoteTouch[i] = 0;
                                }
                                try
                                {
                                    // ハピホテマイル使用数
                                    data = new String( cdata, 242 + (i * 256), 9 );
                                    StateHapihoteMile[i] = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    StateHapihoteMile[i] = 0;
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0114:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0116)
     * 管理機状況
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0116()
    {
        return(sendPacket0116Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0116)
     * 管理機状況
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0116(int kind, String value)
    {
        return(sendPacket0116Sub( kind, value ));
    }

    /**
     * 電文送信処理(0116)
     * 管理機状況
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0116Sub(int kind, String value)
    {
        int i;
        int j;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        EquipRoomCount = 0;
        EquipRoomCode = new int[OWNERINFO_ROOMMAX];
        EquipActMode = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipStatusAlarm = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipStatusData = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipActData = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0116";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0117" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋数
                            data = new String( cdata, 53, 3 );
                            EquipRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < EquipRoomCount ; i++ )
                            {
                                // 部屋コード
                                data = new String( cdata, 56 + (i * 256), 3 );
                                EquipRoomCode[i] = Integer.valueOf( data ).intValue();

                                // 部屋設備状況
                                for( j = 0 ; j < OWNERINFO_EQUIPMAX ; j++ )
                                {
                                    // 動作区分
                                    data = new String( cdata, 59 + (i * 256) + (j * 9), 2 );
                                    EquipActMode[i][j] = Integer.valueOf( data ).intValue();

                                    // 状況警報
                                    data = new String( cdata, 61 + (i * 256) + (j * 9), 1 );
                                    EquipStatusAlarm[i][j] = Integer.valueOf( data ).intValue();

                                    // 状況データ
                                    data = new String( cdata, 62 + (i * 256) + (j * 9), 2 );
                                    EquipStatusData[i][j] = Integer.valueOf( data ).intValue();

                                    // 動作データ
                                    data = new String( cdata, 64 + (i * 256) + (j * 9), 4 );
                                    EquipActData[i][j] = Integer.valueOf( data ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0116:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0118)
     * リネン状況
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0118()
    {
        return(sendPacket0118Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0118)
     * リネン状況
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0118(int kind, String value)
    {
        return(sendPacket0118Sub( kind, value ));
    }

    /**
     * 電文送信処理(0118)
     * リネン状況
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0118Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        LinenRoomCount = 0;
        LinenRoomCode = new int[OWNERINFO_ROOMMAX];
        LinenRefUse = new int[OWNERINFO_ROOMMAX];
        LinenConveniUse = new int[OWNERINFO_ROOMMAX];
        LinenGroup = new String[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0118";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0119" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋数
                            data = new String( cdata, 53, 3 );
                            LinenRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < LinenRoomCount ; i++ )
                            {
                                // 部屋コード
                                data = new String( cdata, 56 + (i * 64), 3 );
                                LinenRoomCode[i] = Integer.valueOf( data ).intValue();

                                // 冷蔵庫利用状態
                                data = new String( cdata, 59 + (i * 64), 1 );
                                LinenRefUse[i] = Integer.valueOf( data ).intValue();

                                // コンビニ利用状態
                                data = new String( cdata, 60 + (i * 64), 1 );
                                LinenConveniUse[i] = Integer.valueOf( data ).intValue();

                                // 作業班
                                data = new String( cdata, 61 + (i * 64), 8 );
                                LinenGroup[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0118:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0120)
     * メンバー状況
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0120()
    {
        return(sendPacket0120Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0120)
     * メンバー状況
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0120(int kind, String value)
    {
        return(sendPacket0120Sub( kind, value ));
    }

    /**
     * 電文送信処理(0120)
     * メンバー状況
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0120Sub(int kind, String value)
    {
        int i;
        int j;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        MemberRoomCount = 0;
        MemberRoomCode = new int[OWNERINFO_ROOMMAX];
        MemberCustomId = new String[OWNERINFO_ROOMMAX];
        MemberNickName = new String[OWNERINFO_ROOMMAX];
        MemberName = new String[OWNERINFO_ROOMMAX];
        MemberRankName = new String[OWNERINFO_ROOMMAX];
        MemberCount = new int[OWNERINFO_ROOMMAX];
        MemberPoint = new int[OWNERINFO_ROOMMAX];
        MemberPoint2 = new int[OWNERINFO_ROOMMAX];
        MemberBirthday1 = new int[OWNERINFO_ROOMMAX];
        MemberBirthday2 = new int[OWNERINFO_ROOMMAX];
        MemberMemorial1 = new int[OWNERINFO_ROOMMAX];
        MemberMemorial2 = new int[OWNERINFO_ROOMMAX];
        MemberEntryDate = new int[OWNERINFO_ROOMMAX];
        MemberNowRanking = new int[OWNERINFO_ROOMMAX];
        MemberOldRanking = new int[OWNERINFO_ROOMMAX];
        MemberRankingCount = new int[OWNERINFO_ROOMMAX];
        MemberAddupCount = new int[OWNERINFO_ROOMMAX];
        MemberRankingTotal = new int[OWNERINFO_ROOMMAX];
        MemberAddupTotal = new int[OWNERINFO_ROOMMAX];
        MemberSurplus = new int[OWNERINFO_ROOMMAX];
        MemberEvent = new int[OWNERINFO_ROOMMAX];
        MemberWarning = new int[OWNERINFO_ROOMMAX];
        MemberContact = new int[OWNERINFO_ROOMMAX];
        MemberPresent = new int[OWNERINFO_ROOMMAX];
        MemberEventInfo = new int[OWNERINFO_ROOMMAX][OWNERINFO_MEMBEREVENTMAX];
        MemberContact1 = new String[OWNERINFO_ROOMMAX];
        MemberContact2 = new String[OWNERINFO_ROOMMAX];
        MemberWarning1 = new String[OWNERINFO_ROOMMAX];
        MemberWarning2 = new String[OWNERINFO_ROOMMAX];
        MemberUserId = new String[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0120";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0121" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋数
                            data = new String( cdata, 53, 3 );
                            MemberRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < MemberRoomCount ; i++ )
                            {
                                // 部屋コード
                                data = new String( cdata, 56 + (i * 512), 3 );
                                MemberRoomCode[i] = Integer.valueOf( data ).intValue();

                                // メンバーＩＤ
                                data = new String( cdata, 59 + (i * 512), 9 );
                                MemberCustomId[i] = data.trim();

                                // ニックネーム
                                data = new String( cdata, 68 + (i * 512), 20 );
                                MemberNickName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 氏名
                                data = new String( cdata, 88 + (i * 512), 40 );
                                MemberName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // メンバーランク名
                                data = new String( cdata, 128 + (i * 512), 40 );
                                MemberRankName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 総利用回数
                                data = new String( cdata, 168 + (i * 512), 9 );
                                MemberCount[i] = Integer.valueOf( data ).intValue();

                                // メンバーポイント
                                data = new String( cdata, 177 + (i * 512), 9 );
                                MemberPoint[i] = Integer.valueOf( data ).intValue();

                                // メンバーポイント２
                                data = new String( cdata, 186 + (i * 512), 9 );
                                MemberPoint2[i] = Integer.valueOf( data ).intValue();

                                // 誕生日１
                                data = new String( cdata, 195 + (i * 512), 8 );
                                MemberBirthday1[i] = Integer.valueOf( data ).intValue();

                                // 誕生日２
                                data = new String( cdata, 203 + (i * 512), 8 );
                                MemberBirthday2[i] = Integer.valueOf( data ).intValue();

                                // 記念日１
                                data = new String( cdata, 211 + (i * 512), 8 );
                                MemberMemorial1[i] = Integer.valueOf( data ).intValue();

                                // 記念日２
                                data = new String( cdata, 219 + (i * 512), 8 );
                                MemberMemorial2[i] = Integer.valueOf( data ).intValue();

                                // 登録日
                                data = new String( cdata, 227 + (i * 512), 8 );
                                MemberEntryDate[i] = Integer.valueOf( data ).intValue();

                                // 今期ランキング
                                data = new String( cdata, 235 + (i * 512), 6 );
                                MemberNowRanking[i] = Integer.valueOf( data ).intValue();

                                // 前期ランキング
                                data = new String( cdata, 241 + (i * 512), 6 );
                                MemberOldRanking[i] = Integer.valueOf( data ).intValue();

                                // ランキング内利用回数
                                data = new String( cdata, 247 + (i * 512), 6 );
                                MemberRankingCount[i] = Integer.valueOf( data ).intValue();

                                // 集計期間内利用回数
                                data = new String( cdata, 253 + (i * 512), 6 );
                                MemberAddupCount[i] = Integer.valueOf( data ).intValue();

                                // ランキング内利用金額
                                data = new String( cdata, 259 + (i * 512), 9 );
                                MemberRankingTotal[i] = Integer.valueOf( data ).intValue();

                                // 集計期間内利用金額
                                data = new String( cdata, 268 + (i * 512), 9 );
                                MemberAddupTotal[i] = Integer.valueOf( data ).intValue();

                                // 繰越利用金額
                                data = new String( cdata, 277 + (i * 512), 6 );
                                MemberSurplus[i] = Integer.valueOf( data ).intValue();

                                // メンバーイベント
                                data = new String( cdata, 283 + (i * 512), 1 );
                                MemberEvent[i] = Integer.valueOf( data ).intValue();

                                // 警告情報
                                data = new String( cdata, 284 + (i * 512), 1 );
                                MemberWarning[i] = Integer.valueOf( data ).intValue();

                                // 連絡情報
                                data = new String( cdata, 285 + (i * 512), 1 );
                                MemberContact[i] = Integer.valueOf( data ).intValue();

                                // 景品情報
                                data = new String( cdata, 286 + (i * 512), 1 );
                                MemberPresent[i] = Integer.valueOf( data ).intValue();

                                // イベント情報
                                for( j = 0 ; j < OWNERINFO_MEMBEREVENTMAX ; j++ )
                                {
                                    data = new String( cdata, 287 + (i * 512) + j, 1 );
                                    MemberEventInfo[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // 連絡メモ１
                                data = new String( cdata, 297 + (i * 512), 40 );
                                MemberContact1[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 連絡メモ２
                                data = new String( cdata, 337 + (i * 512), 40 );
                                MemberContact2[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 警告メモ１
                                data = new String( cdata, 377 + (i * 512), 40 );
                                MemberWarning1[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 警告メモ２
                                data = new String( cdata, 417 + (i * 512), 40 );
                                MemberWarning2[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ユーザID
                                data = new String( cdata, 457 + (i * 512), 40 );
                                MemberUserId[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0120:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0122)
     * 車番状況
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0122()
    {
        return(sendPacket0122Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0122)
     * 車番状況
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0122(int kind, String value)
    {
        return(sendPacket0122Sub( kind, value ));
    }

    /**
     * 電文送信処理(0122)
     * 車番状況
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0122Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        CarRoomCount = 0;
        CarRoomCode = new int[OWNERINFO_ROOMMAX];
        CarArea = new String[OWNERINFO_ROOMMAX];
        CarKind = new String[OWNERINFO_ROOMMAX];
        CarType = new String[OWNERINFO_ROOMMAX];
        CarNo = new String[OWNERINFO_ROOMMAX];
        CarParkingNo = new int[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0122";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0123" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋数
                            data = new String( cdata, 53, 3 );
                            CarRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < CarRoomCount ; i++ )
                            {
                                // 部屋コード
                                data = new String( cdata, 56 + (i * 64), 3 );
                                CarRoomCode[i] = Integer.valueOf( data ).intValue();

                                // 車番（地域）
                                data = new String( cdata, 59 + (i * 64), 8 );
                                CarArea[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 車番（種別）
                                data = new String( cdata, 67 + (i * 64), 2 );
                                CarKind[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 車番（車種）
                                data = new String( cdata, 69 + (i * 64), 3 );
                                CarType[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 車番
                                data = new String( cdata, 72 + (i * 64), 4 );
                                CarNo[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 駐車場番号
                                data = new String( cdata, 76 + (i * 64), 3 );
                                CarParkingNo[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0112:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0124)
     * 精算機状況
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0124()
    {
        return(sendPacket0124Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0124)
     * 精算機状況
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0124(int kind, String value)
    {
        return(sendPacket0124Sub( kind, value ));
    }

    /**
     * 電文送信処理(0124)
     * 精算機状況
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0124Sub(int kind, String value)
    {
        int i;
        int j;
        int k;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        TexRoomCount = 0;
        TexRoomCode = new int[OWNERINFO_ROOMMAX];
        TexMode = new int[OWNERINFO_ROOMMAX];
        TexSupplyStat = new int[OWNERINFO_ROOMMAX];
        TexSecurityStat = new int[OWNERINFO_ROOMMAX];
        TexDoorStat = new int[OWNERINFO_ROOMMAX];
        TexLineStat = new int[OWNERINFO_ROOMMAX];
        TexErrorStat = new int[OWNERINFO_ROOMMAX];
        TexPayStat = new int[OWNERINFO_ROOMMAX];
        TexChargeStat = new int[OWNERINFO_ROOMMAX];
        TexErrorCode = new String[OWNERINFO_ROOMMAX];
        TexErrorMsg = new String[OWNERINFO_ROOMMAX];
        TexSalesTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSALESMAX];
        TexSalesCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSALESMAX];
        TexSafeCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFEMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexSafeTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFEMAX];
        TexSafeStat = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexSupplyTotal = new int[OWNERINFO_ROOMMAX];
        TexSupplyDate = new int[OWNERINFO_ROOMMAX];
        TexSupplyTime = new int[OWNERINFO_ROOMMAX];
        TexSurplus = new int[OWNERINFO_ROOMMAX];
        TexCardStat = new int[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0124";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0125" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋数
                            data = new String( cdata, 53, 3 );
                            TexRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TexRoomCount ; i++ )
                            {
                                // 部屋コード
                                data = new String( cdata, 56 + (i * 700), 3 );
                                TexRoomCode[i] = Integer.valueOf( data ).intValue();

                                // 精算機モード
                                data = new String( cdata, 59 + (i * 700), 1 );
                                TexMode[i] = Integer.valueOf( data ).intValue();

                                // 補充状態
                                data = new String( cdata, 60 + (i * 700), 1 );
                                TexSupplyStat[i] = Integer.valueOf( data ).intValue();

                                // セキュリティ状態
                                data = new String( cdata, 61 + (i * 700), 1 );
                                TexSecurityStat[i] = Integer.valueOf( data ).intValue();

                                // 扉状態
                                data = new String( cdata, 62 + (i * 700), 1 );
                                TexDoorStat[i] = Integer.valueOf( data ).intValue();

                                // 回線状態
                                data = new String( cdata, 63 + (i * 700), 1 );
                                TexLineStat[i] = Integer.valueOf( data ).intValue();

                                // エラー状態
                                data = new String( cdata, 64 + (i * 700), 1 );
                                TexErrorStat[i] = Integer.valueOf( data ).intValue();

                                // 精算状態
                                data = new String( cdata, 65 + (i * 700), 1 );
                                TexPayStat[i] = Integer.valueOf( data ).intValue();

                                // 釣銭状態
                                data = new String( cdata, 66 + (i * 700), 3 );
                                TexChargeStat[i] = Integer.valueOf( data ).intValue();

                                // エラーコード
                                data = new String( cdata, 69 + (i * 700), 4 );
                                TexErrorCode[i] = data.trim();

                                // エラー内容
                                data = new String( cdata, 73 + (i * 700), 32 );
                                TexErrorMsg[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 売上金額データ
                                for( j = 0 ; j < OWNERINFO_TEXSALESMAX ; j++ )
                                {
                                    // 売上金額
                                    data = new String( cdata, 105 + (i * 700) + (j * 18), 9 );
                                    TexSalesTotal[i][j] = Integer.valueOf( data ).intValue();
                                    // 売上回数
                                    data = new String( cdata, 114 + (i * 700) + (j * 18), 9 );
                                    TexSalesCount[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // 金庫情報データ
                                for( j = 0 ; j < OWNERINFO_TEXSAFEMAX ; j++ )
                                {
                                    for( k = 0 ; k < OWNERINFO_TEXSAFETYPEMAX ; k++ )
                                    {
                                        // 枚数
                                        data = new String( cdata, 177 + (i * 700) + (j * 105) + (k * 6), 6 );
                                        TexSafeCount[i][j][k] = Integer.valueOf( data ).intValue();
                                    }

                                    // 合計金額
                                    data = new String( cdata, 273 + (i * 700) + (j * 105), 9 );
                                    TexSafeTotal[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // 金庫枚数状況
                                for( j = 0 ; j < OWNERINFO_TEXSAFETYPEMAX ; j++ )
                                {
                                    // 合計金額
                                    data = new String( cdata, 597 + (i * 700) + (j * 1), 1 );
                                    TexSafeStat[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // 鍵なし補充金額
                                data = new String( cdata, 613 + (i * 700), 9 );
                                TexSupplyTotal[i] = Integer.valueOf( data ).intValue();

                                // 鍵あり補充日付
                                data = new String( cdata, 622 + (i * 700), 8 );
                                TexSupplyDate[i] = Integer.valueOf( data ).intValue();

                                // 鍵あり補充時刻
                                data = new String( cdata, 630 + (i * 700), 8 );
                                TexSupplyTime[i] = Integer.valueOf( data ).intValue();

                                // 余剰金
                                data = new String( cdata, 638 + (i * 700), 9 );
                                TexSurplus[i] = Integer.valueOf( data ).intValue();

                                // カード状態
                                data = new String( cdata, 647 + (i * 700), 1 );
                                TexCardStat[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0124:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0126)
     * マルチメディア状況
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0126()
    {
        return(sendPacket0126Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0126)
     * マルチメディア状況
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0126(int kind, String value)
    {
        return(sendPacket0126Sub( kind, value ));
    }

    /**
     * 電文送信処理(0126)
     * マルチメディア状況
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0126Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        MultiRoomCount = 0;
        MultiRoomCode = new int[OWNERINFO_ROOMMAX];
        MultiLineStat = new int[OWNERINFO_ROOMMAX];
        MultiErrorStat = new int[OWNERINFO_ROOMMAX];
        MultiPowerStat = new int[OWNERINFO_ROOMMAX];
        MultiErrorCode = new String[OWNERINFO_ROOMMAX];
        MultiErrorMsg = new String[OWNERINFO_ROOMMAX];
        MultiChannelNo = new int[OWNERINFO_ROOMMAX];
        MultiChannelName = new String[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0126";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0127" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋数
                            data = new String( cdata, 53, 3 );
                            MultiRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < MultiRoomCount ; i++ )
                            {
                                // 部屋コード
                                data = new String( cdata, 56 + (i * 128), 3 );
                                MultiRoomCode[i] = Integer.valueOf( data ).intValue();

                                // 回線状態
                                data = new String( cdata, 59 + (i * 128), 1 );
                                MultiLineStat[i] = Integer.valueOf( data ).intValue();

                                // エラー状態
                                data = new String( cdata, 60 + (i * 128), 1 );
                                MultiErrorStat[i] = Integer.valueOf( data ).intValue();

                                // 電源状態
                                data = new String( cdata, 61 + (i * 128), 1 );
                                MultiPowerStat[i] = Integer.valueOf( data ).intValue();

                                // エラーコード
                                data = new String( cdata, 62 + (i * 128), 4 );
                                MultiErrorCode[i] = data.trim();

                                // エラー内容
                                data = new String( cdata, 66 + (i * 128), 32 );
                                MultiErrorMsg[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // チャンネル番号
                                data = new String( cdata, 98 + (i * 128), 3 );
                                MultiChannelNo[i] = Integer.valueOf( data ).intValue();

                                // チャンネル名称
                                data = new String( cdata, 101 + (i * 128), 40 );
                                MultiChannelName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0126:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0128)
     * 部屋詳細（利用明細）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0128()
    {
        return(sendPacket0128Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0128)
     * 部屋詳細（利用明細）
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0128(int kind, String value)
    {
        return(sendPacket0128Sub( kind, value ));
    }

    /**
     * 電文送信処理(0128)
     * 部屋詳細（利用明細）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0128Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        DetailUseRoomCode = 0;
        DetailUseCount = 0;
        DetailUseGoodsName = new String[OWNERINFO_DETAILMAX];
        DetailUseGoodsCount = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsRegularPrice = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsPrice = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsDiscount = new int[OWNERINFO_DETAILMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0128";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0129" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋コード
                            data = new String( cdata, 53, 3 );
                            DetailUseRoomCode = Integer.valueOf( data ).intValue();

                            // 利用明細数
                            data = new String( cdata, 56, 3 );
                            DetailUseCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < DetailUseCount ; i++ )
                            {
                                // 利用明細名
                                data = new String( cdata, 59 + (i * 128), 40 );
                                DetailUseGoodsName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 数量
                                data = new String( cdata, 99 + (i * 128), 3 );
                                DetailUseGoodsCount[i] = Integer.valueOf( data ).intValue();

                                // 正規単価
                                data = new String( cdata, 102 + (i * 128), 9 );
                                DetailUseGoodsRegularPrice[i] = Integer.valueOf( data ).intValue();

                                // 単価
                                data = new String( cdata, 111 + (i * 128), 9 );
                                DetailUseGoodsPrice[i] = Integer.valueOf( data ).intValue();

                                // 割引率
                                data = new String( cdata, 120 + (i * 128), 3 );
                                DetailUseGoodsDiscount[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0128:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0130)
     * 部屋詳細（支払い状況）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0130()
    {
        return(sendPacket0130Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0130)
     * 部屋詳細（支払い状況）
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0130(int kind, String value)
    {
        return(sendPacket0130Sub( kind, value ));
    }

    /**
     * 電文送信処理(0130)
     * 部屋詳細（支払い状況）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0130Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        DetailPayRoomCode = 0;
        DetailPayTotal = 0;
        DetailPayClaim = 0;
        DetailPayCount = 0;
        DetailPayName = new String[OWNERINFO_DETAILMAX];
        DetailPayAmount = new int[OWNERINFO_DETAILMAX];
        DetailPayMoney = new int[OWNERINFO_DETAILMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0130";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0131" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋コード
                            data = new String( cdata, 53, 3 );
                            DetailPayRoomCode = Integer.valueOf( data ).intValue();

                            // 利用合計
                            data = new String( cdata, 56, 9 );
                            DetailPayTotal = Integer.valueOf( data ).intValue();

                            // 請求金額
                            data = new String( cdata, 65, 9 );
                            DetailPayClaim = Integer.valueOf( data ).intValue();

                            // 支払い明細数
                            data = new String( cdata, 74, 3 );
                            DetailPayCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < DetailPayCount ; i++ )
                            {
                                // 利用明細名
                                data = new String( cdata, 77 + (i * 128), 40 );
                                DetailPayName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 数量
                                data = new String( cdata, 117 + (i * 128), 3 );
                                DetailPayAmount[i] = Integer.valueOf( data ).intValue();

                                // 金額
                                data = new String( cdata, 120 + (i * 128), 9 );
                                DetailPayMoney[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0130:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0132)
     * 部屋詳細（商品明細）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0132()
    {
        return(sendPacket0132Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0132)
     * 部屋詳細（商品明細）
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0132(int kind, String value)
    {
        return(sendPacket0132Sub( kind, value ));
    }

    /**
     * 電文送信処理(0132)
     * 部屋詳細（商品明細）
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0132Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        DetailGoodsRoomCode = 0;
        DetailGoodsCount = 0;
        DetailGoodsName = new String[OWNERINFO_DETAILMAX];
        DetailGoodsAmount = new int[OWNERINFO_DETAILMAX];
        DetailGoodsPrice = new int[OWNERINFO_DETAILMAX];
        DetailGoodsRef = new int[OWNERINFO_DETAILMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0132";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0133" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 部屋コード
                            data = new String( cdata, 53, 3 );
                            DetailGoodsRoomCode = Integer.valueOf( data ).intValue();

                            // 商品明細数
                            data = new String( cdata, 56, 3 );
                            DetailGoodsCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < DetailGoodsCount ; i++ )
                            {
                                // 利用明細名
                                data = new String( cdata, 59 + (i * 128), 40 );
                                DetailGoodsName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 数量
                                data = new String( cdata, 99 + (i * 128), 3 );
                                DetailGoodsAmount[i] = Integer.valueOf( data ).intValue();

                                // 単価
                                data = new String( cdata, 102 + (i * 128), 9 );
                                DetailGoodsPrice[i] = Integer.valueOf( data ).intValue();

                                // 冷蔵庫フラグ
                                data = new String( cdata, 111 + (i * 128), 1 );
                                DetailGoodsRef[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0132:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0134)
     * 売上目標情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0134()
    {
        return(sendPacket0134Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0134)
     * 売上目標情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0134(int kind, String value)
    {
        return(sendPacket0134Sub( kind, value ));
    }

    /**
     * 電文送信処理(0134)
     * 売上目標情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0134Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        TargetCount = 0;
        TargetTotal = 0;
        TargetModeCount = 0;
        TargetModeCode = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeName = new String[OWNERINFO_SALESTAGETMAX];
        TargetModeRestCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeRestTotal = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayTotal = new int[OWNERINFO_SALESTAGETMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0134";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 計上年月
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( TargetMonth );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0135" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 計上年月
                            data = new String( cdata, 53, 6 );
                            TargetMonth = Integer.valueOf( data ).intValue();

                            // 累計組数
                            data = new String( cdata, 59, 9 );
                            TargetCount = Integer.valueOf( data ).intValue();

                            // 累計売上額
                            data = new String( cdata, 68, 9 );
                            TargetTotal = Integer.valueOf( data ).intValue();

                            // 料金モード数
                            data = new String( cdata, 77, 2 );
                            TargetModeCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TargetModeCount ; i++ )
                            {
                                // 料金モード
                                data = new String( cdata, 79 + (i * 54), 4 );
                                TargetModeCode[i] = Integer.valueOf( data ).intValue();

                                // 料金モード名称
                                data = new String( cdata, 83 + (i * 54), 20 );
                                TargetModeName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 休憩組数
                                data = new String( cdata, 103 + (i * 54), 6 );
                                TargetModeRestCount[i] = Integer.valueOf( data ).intValue();

                                // 宿泊組数
                                data = new String( cdata, 109 + (i * 54), 6 );
                                TargetModeStayCount[i] = Integer.valueOf( data ).intValue();

                                // 休憩売上
                                data = new String( cdata, 115 + (i * 54), 9 );
                                TargetModeRestTotal[i] = Integer.valueOf( data ).intValue();

                                // 宿泊売上
                                data = new String( cdata, 124 + (i * 54), 9 );
                                TargetModeStayTotal[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0134:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0136)
     * 売上目標設定
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0136()
    {
        return(sendPacket0136Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0136)
     * 売上目標設定
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0136(int kind, String value)
    {
        return(sendPacket0136Sub( kind, value ));
    }

    /**
     * 電文送信処理(0136)
     * 売上目標設定
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0136Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        Result = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0136";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 計上年月
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( ResultMonth );
                // 累計組数
                nf = new DecimalFormat( "000000000" );
                senddata = senddata + nf.format( ResultCount );
                // 累計売上額
                nf = new DecimalFormat( "000000000" );
                senddata = senddata + nf.format( ResultTotal );
                // 料金モード数
                nf = new DecimalFormat( "00" );
                senddata = senddata + nf.format( ResultModeCount );

                for( i = 0 ; i < TargetModeCount ; i++ )
                {
                    // 料金モード
                    nf = new DecimalFormat( "0000" );
                    senddata = senddata + nf.format( ResultModeCode[i] );
                    // 休憩組数
                    nf = new DecimalFormat( "000000" );
                    senddata = senddata + nf.format( ResultModeRestCount[i] );
                    // 宿泊組数
                    nf = new DecimalFormat( "000000" );
                    senddata = senddata + nf.format( ResultModeStayCount[i] );
                    // 休憩売上金額
                    nf = new DecimalFormat( "000000000" );
                    senddata = senddata + nf.format( ResultModeRestTotal[i] );
                    // 宿泊売上金額
                    nf = new DecimalFormat( "000000000" );
                    senddata = senddata + nf.format( ResultModeStayTotal[i] );
                }

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0137" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 計上年月
                            data = new String( cdata, 53, 6 );
                            ResultMonth = Integer.valueOf( data ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0136:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0138)
     * 売上実績情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0138()
    {
        return(sendPacket0138Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0138)
     * 売上実績情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0138(int kind, String value)
    {
        return(sendPacket0138Sub( kind, value ));
    }

    /**
     * 電文送信処理(0138)
     * 売上実績情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0138Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        TargetCount = 0;
        TargetTotal = 0;
        TargetModeCount = 0;
        TargetModeCode = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeName = new String[OWNERINFO_SALESTAGETMAX];
        TargetModeRestCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeRestTotal = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayTotal = new int[OWNERINFO_SALESTAGETMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0138";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 計上年月
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( TargetMonth );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0139" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 計上年月
                            data = new String( cdata, 53, 6 );
                            TargetMonth = Integer.valueOf( data ).intValue();

                            // 累計組数
                            data = new String( cdata, 59, 9 );
                            TargetCount = Integer.valueOf( data ).intValue();

                            // 累計売上額
                            data = new String( cdata, 68, 9 );
                            TargetTotal = Integer.valueOf( data ).intValue();

                            // 料金モード数
                            data = new String( cdata, 77, 2 );
                            TargetModeCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TargetModeCount ; i++ )
                            {
                                // 料金モード
                                data = new String( cdata, 79 + (i * 54), 4 );
                                TargetModeCode[i] = Integer.valueOf( data ).intValue();

                                // 料金モード名称
                                data = new String( cdata, 83 + (i * 54), 20 );
                                TargetModeName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 休憩組数
                                data = new String( cdata, 103 + (i * 54), 6 );
                                TargetModeRestCount[i] = Integer.valueOf( data ).intValue();

                                // 宿泊組数
                                data = new String( cdata, 109 + (i * 54), 6 );
                                TargetModeStayCount[i] = Integer.valueOf( data ).intValue();

                                // 休憩売上
                                data = new String( cdata, 115 + (i * 54), 9 );
                                TargetModeRestTotal[i] = Integer.valueOf( data ).intValue();

                                // 宿泊売上
                                data = new String( cdata, 124 + (i * 54), 9 );
                                TargetModeStayTotal[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0138:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0140)
     * 操作イベント情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0140()
    {
        return(sendPacket0140Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0140)
     * 操作イベント情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0140(int kind, String value)
    {
        return(sendPacket0140Sub( kind, value ));
    }

    /**
     * 電文送信処理(0140)
     * 操作イベント情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0140Sub(int kind, String value)
    {
        int i;
        int j;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        EventCount = 0;
        EventDate = new int[OWNERINFO_DOORDETAILMAX];
        EventTime = new int[OWNERINFO_DOORDETAILMAX];
        EventTimeSub = new int[OWNERINFO_DOORDETAILMAX];
        EventRoomCode = new int[OWNERINFO_DOORDETAILMAX];
        EventRoomName = new String[OWNERINFO_DOORDETAILMAX];
        EventLineNo = new int[OWNERINFO_DOORDETAILMAX];
        EventTermId = new int[OWNERINFO_DOORDETAILMAX];
        EventEmployeeCode = new int[OWNERINFO_DOORDETAILMAX];
        EventEventCode = new int[OWNERINFO_DOORDETAILMAX];
        EventSystemErrCode = new int[OWNERINFO_DOORDETAILMAX];
        EventNumData = new int[OWNERINFO_DOORDETAILMAX][6];
        EventStrData = new String[OWNERINFO_DOORDETAILMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0140";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 取得日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( EventGetNextDate );
                // 取得時刻
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( EventGetNextTime );
                // 時刻補助
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( EventGetNextTimeSub );
                // 部屋コード
                nf = new DecimalFormat( "0000" );
                senddata = senddata + nf.format( EventGetNextRoomCode );
                // イベントコード
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( EventGetNextEventCode );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0147" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 次ページ取得日付
                            data = new String( cdata, 53, 8 );
                            EventGetNextDate = Integer.valueOf( data ).intValue();

                            // 次ページ取得時間
                            data = new String( cdata, 61, 8 );
                            EventGetNextTime = Integer.valueOf( data ).intValue();

                            // 次ページ取得時間補助
                            data = new String( cdata, 69, 8 );
                            EventGetNextTimeSub = Integer.valueOf( data ).intValue();

                            // 次ページ部屋コード
                            data = new String( cdata, 77, 4 );
                            EventGetNextRoomCode = Integer.valueOf( data ).intValue();

                            // 次ページイベントコード
                            data = new String( cdata, 81, 8 );
                            EventGetNextEventCode = Integer.valueOf( data ).intValue();

                            // 前ページ取得日付
                            data = new String( cdata, 89, 8 );
                            EventGetPrevDate = Integer.valueOf( data ).intValue();

                            // 前ページ取得時間
                            data = new String( cdata, 97, 8 );
                            EventGetPrevTime = Integer.valueOf( data ).intValue();

                            // 前ページ取得時間補助
                            data = new String( cdata, 105, 8 );
                            EventGetPrevTimeSub = Integer.valueOf( data ).intValue();

                            // 前ページ部屋コード
                            data = new String( cdata, 113, 4 );
                            EventGetPrevRoomCode = Integer.valueOf( data ).intValue();

                            // 前ページイベントコード
                            data = new String( cdata, 117, 8 );
                            EventGetPrevEventCode = Integer.valueOf( data ).intValue();

                            // 取得件数
                            data = new String( cdata, 125, 3 );
                            EventCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < EventCount ; i++ )
                            {
                                // 日付
                                data = new String( cdata, 128 + (i * 256), 8 );
                                EventDate[i] = Integer.valueOf( data ).intValue();

                                // 時間
                                data = new String( cdata, 136 + (i * 256), 8 );
                                EventTime[i] = Integer.valueOf( data ).intValue();

                                // 時刻補助
                                data = new String( cdata, 144 + (i * 256), 8 );
                                EventTimeSub[i] = Integer.valueOf( data ).intValue();

                                // 部屋コード
                                data = new String( cdata, 152 + (i * 256), 4 );
                                EventRoomCode[i] = Integer.valueOf( data ).intValue();

                                // 部屋名称
                                data = new String( cdata, 156 + (i * 256), 8 );
                                EventRoomName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 系統番号
                                data = new String( cdata, 164 + (i * 256), 4 );
                                EventLineNo[i] = Integer.valueOf( data ).intValue();

                                // 端末ID
                                data = new String( cdata, 168 + (i * 256), 4 );
                                EventTermId[i] = Integer.valueOf( data ).intValue();

                                // 従業員コード
                                data = new String( cdata, 172 + (i * 256), 4 );
                                EventEmployeeCode[i] = Integer.valueOf( data ).intValue();

                                // イベントコード
                                data = new String( cdata, 176 + (i * 256), 8 );
                                EventEventCode[i] = Integer.valueOf( data ).intValue();

                                // システムエラーコード
                                data = new String( cdata, 184 + (i * 256), 9 );
                                EventSystemErrCode[i] = Integer.valueOf( data ).intValue();

                                for( j = 0 ; j < 6 ; j++ )
                                {
                                    // 付帯情報
                                    data = new String( cdata, 193 + (i * 256) + (j * 9), 9 );
                                    EventNumData[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // 付帯文字情報
                                data = new String( cdata, 247 + (i * 256), 32 );
                                EventStrData[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0140:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0142)
     * オートカレンダー情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0142()
    {
        return(sendPacket0142Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0142)
     * オートカレンダー情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0142(int kind, String value)
    {
        return(sendPacket0142Sub( kind, value ));
    }

    /**
     * 電文送信処理(0142)
     * オートカレンダー情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0142Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        CalModeCount = 0;
        CalModeCode = new int[OWNERINFO_CHAGEMODEMAX];
        CalModeName = new String[OWNERINFO_CHAGEMODEMAX];
        CalDayDate = new int[OWNERINFO_DAYMAX];
        CalDayMode = new int[OWNERINFO_DAYMAX];
        CalDayModeName = new String[OWNERINFO_DAYMAX];
        CalDayWeekKind = new int[OWNERINFO_DAYMAX];
        CalDayHolidayKind = new int[OWNERINFO_DAYMAX];
        CalDayMemo1 = new String[OWNERINFO_DAYMAX];
        CalDayMemo2 = new String[OWNERINFO_DAYMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0142";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 取得年月
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( CalGetDate );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0143" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 取得年月
                            data = new String( cdata, 53, 6 );
                            CalGetDate = Integer.valueOf( data ).intValue();

                            // 料金モード数
                            data = new String( cdata, 59, 2 );
                            CalModeCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < OWNERINFO_CHAGEMODEMAX ; i++ )
                            {
                                // 料金モード番号
                                data = new String( cdata, 61 + (i * 24), 4 );
                                CalModeCode[i] = Integer.valueOf( data ).intValue();

                                // 料金モード名称
                                data = new String( cdata, 65 + (i * 24), 20 );
                                CalModeName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }

                            for( i = 0 ; i < OWNERINFO_DAYMAX ; i++ )
                            {
                                // 日付
                                data = new String( cdata, 541 + (i * 108), 2 );
                                CalDayDate[i] = Integer.valueOf( data ).intValue();

                                // 料金モード
                                data = new String( cdata, 543 + (i * 108), 4 );
                                CalDayMode[i] = Integer.valueOf( data ).intValue();

                                // 料金モード名称
                                data = new String( cdata, 547 + (i * 108), 20 );
                                CalDayModeName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 曜日種別
                                data = new String( cdata, 567 + (i * 108), 1 );
                                CalDayWeekKind[i] = Integer.valueOf( data ).intValue();

                                // 休日種別
                                data = new String( cdata, 568 + (i * 108), 1 );
                                CalDayHolidayKind[i] = Integer.valueOf( data ).intValue();

                                // 特記事項１
                                data = new String( cdata, 569 + (i * 108), 40 );
                                CalDayMemo1[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 特記事項２
                                data = new String( cdata, 609 + (i * 108), 40 );
                                CalDayMemo2[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0142:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0144)
     * オートカレンダー設定
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0144()
    {
        return(sendPacket0144Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0144)
     * オートカレンダー設定
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0144(int kind, String value)
    {
        return(sendPacket0144Sub( kind, value ));
    }

    /**
     * 電文送信処理(0144)
     * オートカレンダー設定
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0144Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        Result = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0144";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 設定年月
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( CalGetDate );

                for( i = 0 ; i < OWNERINFO_DAYMAX ; i++ )
                {
                    // 日付
                    nf = new DecimalFormat( "00" );
                    senddata = senddata + nf.format( CalDayDate[i] );
                    // 料金モード
                    nf = new DecimalFormat( "0000" );
                    senddata = senddata + nf.format( CalDayMode[i] );
                    // 曜日種別
                    nf = new DecimalFormat( "0" );
                    senddata = senddata + nf.format( CalDayWeekKind[i] );
                    // 休日種別
                    nf = new DecimalFormat( "0" );
                    senddata = senddata + nf.format( CalDayHolidayKind[i] );
                    // 特記事項１
                    data = format.leftFitFormat( CalDayMemo1[i], 40 );
                    senddata = senddata + data;
                    // 特記事項２
                    data = format.leftFitFormat( CalDayMemo2[i], 40 );
                    senddata = senddata + data;
                }
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0145" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 取得年月
                            data = new String( cdata, 53, 6 );
                            CalGetDate = Integer.valueOf( data ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0144:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0146)
     * 部屋状況履歴取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0146()
    {
        return(sendPacket0146Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0146)
     * 部屋状況履歴取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0146(int kind, String value)
    {
        return(sendPacket0146Sub( kind, value ));
    }

    /**
     * 電文送信処理(0146)
     * 部屋状況履歴取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0146Sub(int kind, String value)
    {

        if ( !dateCheck( RoomHistoryDate ) )
        {
            return(false);
        }

        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        RoomHistoryRoomCount = 0;
        RoomHistoryCount = 0;
        RoomHistoryTime = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryEmpty = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryExist = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryClean = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryStop = new int[OWNERINFO_TIMEHOURMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0146";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 取得日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( RoomHistoryDate );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0147" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 総部屋数
                            data = new String( cdata, 53, 3 );
                            RoomHistoryRoomCount = Integer.valueOf( data ).intValue();

                            // 取得件数
                            data = new String( cdata, 56, 2 );
                            RoomHistoryCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < OWNERINFO_TIMEHOURMAX ; i++ )
                            {
                                // 時刻
                                data = new String( cdata, 58 + (i * 16), 4 );
                                RoomHistoryTime[i] = Integer.valueOf( data ).intValue();

                                // 空室数
                                data = new String( cdata, 62 + (i * 16), 3 );
                                RoomHistoryEmpty[i] = Integer.valueOf( data ).intValue();

                                // 在室数
                                data = new String( cdata, 65 + (i * 16), 3 );
                                RoomHistoryExist[i] = Integer.valueOf( data ).intValue();

                                // 準備数
                                data = new String( cdata, 68 + (i * 16), 3 );
                                RoomHistoryClean[i] = Integer.valueOf( data ).intValue();

                                // 売止数
                                data = new String( cdata, 71 + (i * 16), 3 );
                                RoomHistoryStop[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0146:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0148)
     * メンバーランク取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0148()
    {
        return(sendPacket0148Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0148)
     * メンバーランク取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0148(int kind, String value)
    {
        return(sendPacket0148Sub( kind, value ));
    }

    /**
     * 電文送信処理(0148)
     * メンバーランク取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0148Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;

        // データのクリア
        Result = 0;
        CustomRankCount = 0;
        CustomRankCode = new int[OWNERINFO_CUSTOMRANKMAX];
        CustomRankName = new String[OWNERINFO_CUSTOMRANKMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0148";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0149" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // メンバーランク数
                            data = new String( cdata, 53, 2 );
                            CustomRankCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < CustomRankCount ; i++ )
                            {
                                // メンバーランクコード
                                data = new String( cdata, 55 + (i * 64), 3 );
                                CustomRankCode[i] = Integer.valueOf( data ).intValue();

                                // メンバーランク名称
                                data = new String( cdata, 58 + (i * 64), 40 );
                                CustomRankName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0148:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0152)
     * 精算機ログ取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0152()
    {
        return(sendPacket0152Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0152)
     * 精算機ログ取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0152(int kind, String value)
    {
        return(sendPacket0152Sub( kind, value ));
    }

    /**
     * 電文送信処理(0152)
     * 精算機ログ取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0152Sub(int kind, String value)
    {

        if ( !dateCheck( TexlogNextDate ) )
        {
            return(false);
        }

        int i;
        int j;
        int k;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        TexlogCount = 0;
        TexlogDate = new int[OWNERINFO_TEXLOGMAX];
        TexlogTime = new int[OWNERINFO_TEXLOGMAX];
        TexlogRoomCode = new int[OWNERINFO_TEXLOGMAX];
        TexlogRoomName = new String[OWNERINFO_TEXLOGMAX];
        TexlogLineNo = new int[OWNERINFO_TEXLOGMAX];
        TexlogTermId = new int[OWNERINFO_TEXLOGMAX];
        TexlogClaimed = new int[OWNERINFO_TEXLOGMAX];
        TexlogSurplus = new int[OWNERINFO_TEXLOGMAX];
        TexlogSafeCount = new int[OWNERINFO_TEXLOGMAX][OWNERINFO_TEXSAFEMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexlogSafeTotal = new int[OWNERINFO_TEXLOGMAX][OWNERINFO_TEXSAFEMAX];
        TexlogLogLevel = new int[OWNERINFO_TEXLOGMAX];
        TexlogLogContent = new String[OWNERINFO_TEXLOGMAX];
        TexlogLogDetail = new String[OWNERINFO_TEXLOGMAX];
        TexlogStatTrade = new int[OWNERINFO_TEXLOGMAX];
        TexlogStatInOut = new int[OWNERINFO_TEXLOGMAX];
        TexlogStatSecurity = new int[OWNERINFO_TEXLOGMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0152";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 取得日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( TexlogNextDate );
                // 取得時刻
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( TexlogNextTime );
                // 部屋コード
                nf = new DecimalFormat( "0000" );
                senddata = senddata + nf.format( TexlogNextRoomCode );
                // 系統番号
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( TexlogNextLineNo );
                // 端末ID
                nf = new DecimalFormat( "0000" );
                senddata = senddata + nf.format( TexlogNextTermId );
                // ログレベル
                nf = new DecimalFormat( "00" );
                senddata = senddata + nf.format( TexlogGetLogLevel );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0153" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 次ページ日付
                            data = new String( cdata, 53, 8 );
                            TexlogNextDate = Integer.valueOf( data ).intValue();

                            // 次ページ時間
                            data = new String( cdata, 61, 8 );
                            TexlogNextTime = Integer.valueOf( data ).intValue();

                            // 次ページ部屋コード
                            data = new String( cdata, 69, 4 );
                            TexlogNextRoomCode = Integer.valueOf( data ).intValue();

                            // 次ページ系統番号
                            data = new String( cdata, 73, 3 );
                            TexlogNextLineNo = Integer.valueOf( data ).intValue();

                            // 次ページ端末ID
                            data = new String( cdata, 76, 4 );
                            TexlogNextTermId = Integer.valueOf( data ).intValue();

                            // 前ページ日付
                            data = new String( cdata, 80, 8 );
                            TexlogPrevDate = Integer.valueOf( data ).intValue();

                            // 前ページ時間
                            data = new String( cdata, 88, 8 );
                            TexlogPrevTime = Integer.valueOf( data ).intValue();

                            // 前ページ部屋コード
                            data = new String( cdata, 96, 4 );
                            TexlogPrevRoomCode = Integer.valueOf( data ).intValue();

                            // 前ページ系統番号
                            data = new String( cdata, 100, 3 );
                            TexlogPrevLineNo = Integer.valueOf( data ).intValue();

                            // 前ページ端末ID
                            data = new String( cdata, 103, 4 );
                            TexlogPrevTermId = Integer.valueOf( data ).intValue();

                            // 取得件数
                            data = new String( cdata, 107, 3 );
                            TexlogCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TexlogCount ; i++ )
                            {
                                // 日付
                                data = new String( cdata, 110 + (i * 700), 8 );
                                TexlogDate[i] = Integer.valueOf( data ).intValue();

                                // 時間
                                data = new String( cdata, 118 + (i * 700), 8 );
                                TexlogTime[i] = Integer.valueOf( data ).intValue();

                                // 部屋コード
                                data = new String( cdata, 126 + (i * 700), 4 );
                                TexlogRoomCode[i] = Integer.valueOf( data ).intValue();

                                // 部屋名称
                                data = new String( cdata, 130 + (i * 700), 8 );
                                TexlogRoomName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 系統番号
                                data = new String( cdata, 138 + (i * 700), 3 );
                                TexlogLineNo[i] = Integer.valueOf( data ).intValue();

                                // 端末ID
                                data = new String( cdata, 141 + (i * 700), 4 );
                                TexlogTermId[i] = Integer.valueOf( data ).intValue();

                                // 請求金額
                                data = new String( cdata, 145 + (i * 700), 9 );
                                TexlogClaimed[i] = Integer.valueOf( data ).intValue();

                                // 余剰金
                                data = new String( cdata, 154 + (i * 700), 9 );
                                TexlogSurplus[i] = Integer.valueOf( data ).intValue();

                                for( j = 0 ; j < 4 ; j++ )
                                {
                                    for( k = 0 ; k < 16 ; k++ )
                                    {
                                        // 金庫情報枚数
                                        data = new String( cdata, 163 + (i * 700) + (j * 105) + (k * 6), 6 );
                                        TexlogSafeCount[i][j][k] = Integer.valueOf( data ).intValue();
                                    }

                                    // 金庫情報合計金額
                                    data = new String( cdata, 259 + (i * 700) + (j * 105), 9 );
                                    TexlogSafeTotal[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // ログレベル
                                data = new String( cdata, 583 + (i * 700), 2 );
                                TexlogLogLevel[i] = Integer.valueOf( data ).intValue();

                                // ログ内容
                                data = new String( cdata, 585 + (i * 700), 40 );
                                TexlogLogContent[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ログ詳細
                                data = new String( cdata, 625 + (i * 700), 80 );
                                TexlogLogDetail[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 取引状態
                                data = new String( cdata, 705 + (i * 700), 2 );
                                TexlogStatTrade[i] = Integer.valueOf( data ).intValue();

                                // 入出金状態
                                data = new String( cdata, 707 + (i * 700), 1 );
                                TexlogStatInOut[i] = Integer.valueOf( data ).intValue();

                                // セキュリティ状態
                                data = new String( cdata, 708 + (i * 700), 1 );
                                TexlogStatSecurity[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0152:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0154)
     * 新シリ売上詳細情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0154()
    {
        return(sendPacket0154Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0154)
     * 新シリ売上詳細情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0154(int kind, String value)
    {
        return(sendPacket0154Sub( kind, value ));
    }

    /**
     * 電文送信処理(0154)
     * 新シリ売上詳細情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0154Sub(int kind, String value)
    {

        if ( !dateCheck( AscSalesDetailGetStartDate, AscSalesDetailGetEndDate ) )
        {
            return(false);
        }

        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        AscSalesDetailStay = 0;
        AscSalesDetailStayBeforeOver = 0;
        AscSalesDetailStayAfterOver = 0;
        AscSalesDetailRest = 0;
        AscSalesDetailRestOver = 0;
        AscSalesDetailTel = 0;
        AscSalesDetailAdvance = 0;
        AscSalesDetailSubTotal = 0;
        AscSalesDetailService = 0;
        AscSalesDetailTaxOut = 0;
        AscSalesDetailTaxIn = 0;
        AscSalesDetailDiscount = 0;
        AscSalesDetailExtra = 0;
        AscSalesDetailMember = 0;
        AscSalesDetailAdjust = 0;
        AscSalesDetailFiller = new int[6];
        AscSalesDetailTotal = 0;

        AscSalesDetailCount = 0;
        AscSalesDetailName = new String[OWNERINFO_SALESDETAILMAX];
        AscSalesDetailAmount = new int[OWNERINFO_SALESDETAILMAX];

        if ( HotelId != null )
        {
            if ( AscSalesDetailGetStartDate <= 0 )
            {
                Result = 1;
                return(true);
            }

            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0154";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 取得開始日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( AscSalesDetailGetStartDate );
                // 取得終了日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( AscSalesDetailGetEndDate );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0155" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 開始日付
                            data = new String( cdata, 53, 8 );
                            AscSalesDetailGetStartDate = Integer.valueOf( data ).intValue();

                            // 終了日付
                            data = new String( cdata, 61, 8 );
                            AscSalesDetailGetEndDate = Integer.valueOf( data ).intValue();

                            // 宿泊
                            data = new String( cdata, 69, 9 );
                            AscSalesDetailStay = Integer.valueOf( data ).intValue();

                            // 宿泊前延長
                            data = new String( cdata, 78, 9 );
                            AscSalesDetailStayBeforeOver = Integer.valueOf( data ).intValue();

                            // 宿泊後延長
                            data = new String( cdata, 87, 9 );
                            AscSalesDetailStayAfterOver = Integer.valueOf( data ).intValue();

                            // 休憩
                            data = new String( cdata, 96, 9 );
                            AscSalesDetailRest = Integer.valueOf( data ).intValue();

                            // 休憩延長
                            data = new String( cdata, 105, 9 );
                            AscSalesDetailRestOver = Integer.valueOf( data ).intValue();

                            // 電話売上
                            data = new String( cdata, 114, 9 );
                            AscSalesDetailTel = Integer.valueOf( data ).intValue();

                            // 立替金売上
                            data = new String( cdata, 123, 9 );
                            AscSalesDetailAdvance = Integer.valueOf( data ).intValue();

                            // 小計
                            data = new String( cdata, 132, 9 );
                            AscSalesDetailSubTotal = Integer.valueOf( data ).intValue();

                            // 奉仕料売上
                            data = new String( cdata, 141, 9 );
                            AscSalesDetailService = Integer.valueOf( data ).intValue();

                            // 税金（外税）売上
                            data = new String( cdata, 150, 9 );
                            AscSalesDetailTaxOut = Integer.valueOf( data ).intValue();

                            // 税金（内税）売上
                            data = new String( cdata, 159, 9 );
                            AscSalesDetailTaxIn = Integer.valueOf( data ).intValue();

                            // 割増売上
                            data = new String( cdata, 168, 9 );
                            AscSalesDetailExtra = Integer.valueOf( data ).intValue();

                            // 割引売上
                            data = new String( cdata, 177, 9 );
                            AscSalesDetailDiscount = Integer.valueOf( data ).intValue();

                            // メンバー割引売上
                            data = new String( cdata, 186, 9 );
                            AscSalesDetailMember = Integer.valueOf( data ).intValue();

                            // 調整金売上
                            data = new String( cdata, 195, 9 );
                            AscSalesDetailAdjust = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 204, 9 );
                            AscSalesDetailFiller[0] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 213, 9 );
                            AscSalesDetailFiller[1] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 222, 9 );
                            AscSalesDetailFiller[2] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 231, 9 );
                            AscSalesDetailFiller[3] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 240, 9 );
                            AscSalesDetailFiller[4] = Integer.valueOf( data ).intValue();

                            // 予備売上
                            data = new String( cdata, 249, 9 );
                            AscSalesDetailFiller[5] = Integer.valueOf( data ).intValue();

                            // 総合計
                            data = new String( cdata, 258, 9 );
                            AscSalesDetailTotal = Integer.valueOf( data ).intValue();

                            data = new String( cdata, 267, 2 );
                            AscSalesDetailCount = Integer.valueOf( data ).intValue();
                            for( i = 0 ; i < OWNERINFO_SALESDETAILMAX ; i++ )
                            {
                                // 明細名称
                                data = new String( cdata, 269 + (i * 64), 40 );
                                AscSalesDetailName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 総合計
                                data = new String( cdata, 309 + (i * 64), 9 );
                                AscSalesDetailAmount[i] = Integer.valueOf( data ).intValue();
                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0154:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0156)
     * フロント精算機状況
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0156()
    {
        return(sendPacket0156Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0156)
     * フロント精算機状況
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0156(int kind, String value)
    {
        return(sendPacket0156Sub( kind, value ));
    }

    /**
     * 電文送信処理(0156)
     * フロント精算機状況
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0156Sub(int kind, String value)
    {
        int i;
        int j;
        int k;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        FrontTexTermCount = 0;
        FrontTexTermCode = new int[OWNERINFO_ROOMMAX];
        FrontTexTermName = new String[OWNERINFO_ROOMMAX];
        FrontTexServiceStat = new int[OWNERINFO_ROOMMAX];
        FrontTexKeySwStat = new int[OWNERINFO_ROOMMAX];
        FrontTexSecurityStat = new int[OWNERINFO_ROOMMAX];
        FrontTexDoorStat = new int[OWNERINFO_ROOMMAX];
        FrontTexLineStat = new int[OWNERINFO_ROOMMAX];
        FrontTexErrorStat = new int[OWNERINFO_ROOMMAX];
        FrontTexErrorCode = new String[OWNERINFO_ROOMMAX];
        FrontTexErrorMsg = new String[OWNERINFO_ROOMMAX];
        FrontTexSalesTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSALESMAX];
        FrontTexSalesCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSALESMAX];
        FrontTexSafeCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFEMAX][OWNERINFO_FRONTTEXSAFETYPEMAX];
        FrontTexSafeTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFEMAX];
        FrontTexSafeStat = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFETYPEMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0156";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 端末番号
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( FrontTexTermCodeIn );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0157" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 端末数
                            data = new String( cdata, 53, 3 );
                            FrontTexTermCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < FrontTexTermCount ; i++ )
                            {
                                // 端末番号
                                data = new String( cdata, 56 + (i * 1400), 3 );
                                FrontTexTermCode[i] = Integer.valueOf( data ).intValue();

                                // 端末名称
                                data = new String( cdata, 59 + (i * 1400), 40 );
                                FrontTexTermName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 取扱状態
                                data = new String( cdata, 99 + (i * 1400), 1 );
                                FrontTexServiceStat[i] = Integer.valueOf( data ).intValue();

                                // キーSW状態
                                data = new String( cdata, 100 + (i * 1400), 1 );
                                FrontTexKeySwStat[i] = Integer.valueOf( data ).intValue();

                                // セキュリティ状態
                                data = new String( cdata, 101 + (i * 1400), 1 );
                                FrontTexSecurityStat[i] = Integer.valueOf( data ).intValue();

                                // 扉状態
                                data = new String( cdata, 102 + (i * 1400), 1 );
                                FrontTexDoorStat[i] = Integer.valueOf( data ).intValue();

                                // 回線状態
                                data = new String( cdata, 103 + (i * 1400), 1 );
                                FrontTexLineStat[i] = Integer.valueOf( data ).intValue();

                                // エラー状態
                                data = new String( cdata, 104 + (i * 1400), 1 );
                                FrontTexErrorStat[i] = Integer.valueOf( data ).intValue();

                                // エラーコード
                                data = new String( cdata, 111 + (i * 1400), 4 );
                                FrontTexErrorCode[i] = data.trim();

                                // エラー内容
                                data = new String( cdata, 115 + (i * 1400), 32 );
                                FrontTexErrorMsg[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 売上金額データ
                                for( j = 0 ; j < OWNERINFO_FRONTTEXSALESMAX ; j++ )
                                {
                                    // 売上金額
                                    data = new String( cdata, 147 + (i * 1400) + (j * 18), 9 );
                                    FrontTexSalesTotal[i][j] = Integer.valueOf( data ).intValue();
                                    // 売上回数
                                    data = new String( cdata, 156 + (i * 1400) + (j * 18), 9 );
                                    FrontTexSalesCount[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // 金庫情報データ
                                for( j = 0 ; j < OWNERINFO_FRONTTEXSAFEMAX ; j++ )
                                {
                                    for( k = 0 ; k < OWNERINFO_FRONTTEXSAFETYPEMAX ; k++ )
                                    {
                                        // 枚数
                                        data = new String( cdata, 291 + (i * 1400) + (j * 105) + (k * 6), 6 );
                                        FrontTexSafeCount[i][j][k] = Integer.valueOf( data ).intValue();
                                    }

                                    // 合計金額
                                    data = new String( cdata, 387 + (i * 1400) + (j * 105), 9 );
                                    FrontTexSafeTotal[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // 金庫枚数状況
                                for( j = 0 ; j < OWNERINFO_FRONTTEXSAFETYPEMAX ; j++ )
                                {
                                    // 合計金額
                                    data = new String( cdata, 1341 + (i * 1400) + (j * 1), 1 );
                                    FrontTexSafeStat[i][j] = Integer.valueOf( data ).intValue();
                                }

                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0156:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0158)
     * 部屋ステータス詳細遷移
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0158()
    {
        return(sendPacket0158Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0158)
     * 部屋ステータス詳細遷移
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0158(int kind, String value)
    {
        return(sendPacket0158Sub( kind, value ));
    }

    /**
     * 電文送信処理(0158)
     * 部屋ステータス詳細遷移
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0158Sub(int kind, String value)
    {
        int i;
        int j;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        TimeChartStartTime = 0;
        TimeChartStatusName = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartStatusColor = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartStatusForeColor = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartRoomCount = 0;
        TimeChartRoomCode = new int[OWNERINFO_ROOMMAX];
        TimeChartRoomName = new String[OWNERINFO_ROOMMAX];
        TimeChartRoomFloor = new int[OWNERINFO_ROOMMAX];
        TimeChartRoomStatus = new int[OWNERINFO_ROOMMAX][OWNERINFO_TIMETABLEMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0158";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 部屋コード
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( TimeChartRoomCodeOne );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0159" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // タイムチャート基準時間
                            data = new String( cdata, 53, 4 );
                            TimeChartStartTime = Integer.valueOf( data ).intValue();

                            // 部屋ステータス情報
                            for( i = 0 ; i < OWNERINFO_ROOMSTATUSMAX + 2 ; i++ )
                            {
                                // ステータス名称
                                data = new String( cdata, 57 + (i * 24), 12 );
                                TimeChartStatusName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // ステータス色
                                data = new String( cdata, 69 + (i * 24), 6 );
                                TimeChartStatusColor[i] = data.trim();
                                // ステータス文字色
                                data = new String( cdata, 75 + (i * 24), 6 );
                                TimeChartStatusForeColor[i] = data.trim();
                            }

                            // 部屋数
                            data = new String( cdata, 1305, 3 );
                            TimeChartRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TimeChartRoomCount ; i++ )
                            {
                                // 部屋コード
                                data = new String( cdata, 1308 + (i * 311), 3 );
                                TimeChartRoomCode[i] = Integer.valueOf( data ).intValue();
                                // 部屋名称
                                data = new String( cdata, 1311 + (i * 311), 8 );
                                TimeChartRoomName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // フロア番号
                                data = new String( cdata, 1319 + (i * 311), 2 );
                                TimeChartRoomFloor[i] = Integer.valueOf( data ).intValue();

                                for( j = 0 ; j < OWNERINFO_TIMETABLEMAX ; j++ )
                                {
                                    // 部屋ステータス
                                    data = new String( cdata, 1331 + (i * 311) + (j * 2), 2 );
                                    TimeChartRoomStatus[i][j] = Integer.valueOf( data ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0158:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * 電文送信処理(0160)
     * 可変名称売上詳細情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0160()
    {
        return(sendPacket0160Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0160)
     * 可変名称売上詳細情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0160(int kind, String value)
    {
        return(sendPacket0160Sub( kind, value ));
    }

    /**
     * 電文送信処理(0160)
     * 可変名称売上詳細情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0160Sub(int kind, String value)
    {

        if ( !dateCheck( ManualSalesDetailGetStartDate, ManualSalesDetailGetEndDate ) )
        {
            return(false);
        }

        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        ManualSalesDetailTaxIn = 0;
        ManualSalesDetailTotal = 0;

        ManualSalesDetailCount = 0;
        ManualSalesDetailName = new String[OWNERINFO_MANUALSALESMAX];
        ManualSalesDetailAmount = new int[OWNERINFO_MANUALSALESMAX];

        if ( HotelId != null )
        {
            if ( ManualSalesDetailGetStartDate <= 0 )
            {
                Result = 1;
                return(true);
            }

            // ホスト接続処理
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // コマンド
                senddata = "0160";
                // ユーザID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // パスワード
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // 取得開始日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( ManualSalesDetailGetStartDate );
                // 取得終了日付
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( ManualSalesDetailGetEndDate );
                // 予備
                senddata = senddata + "          ";

                // 電文ヘッダの取得
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // 電文の結合
                senddata = header + senddata;

                try
                {
                    // 電文送信
                    tcpclient.send( senddata );

                    // 受信待機
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // コマンド取得
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0161" ) == 0 )
                        {
                            // 処理結果
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // 開始日付
                            data = new String( cdata, 53, 8 );
                            ManualSalesDetailGetStartDate = Integer.valueOf( data ).intValue();

                            // 終了日付
                            data = new String( cdata, 61, 8 );
                            ManualSalesDetailGetEndDate = Integer.valueOf( data ).intValue();

                            // 税金（内税）
                            data = new String( cdata, 114, 9 );
                            ManualSalesDetailTaxIn = Integer.valueOf( data ).intValue();

                            // 総合計
                            data = new String( cdata, 123, 9 );
                            ManualSalesDetailTotal = Integer.valueOf( data ).intValue();

                            data = new String( cdata, 132, 2 );
                            ManualSalesDetailCount = Integer.valueOf( data ).intValue();
                            for( i = 0 ; i < ManualSalesDetailCount ; i++ )
                            {
                                // 明細名称
                                data = new String( cdata, 134 + (i * 64), 40 );
                                ManualSalesDetailName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // 合計
                                data = new String( cdata, 174 + (i * 64), 9 );
                                ManualSalesDetailAmount[i] = Integer.valueOf( data ).intValue();
                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0160:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * ﾕｰｻﾞ情報取得
     * 
     * @param hotelid ﾎﾃﾙID
     * @param loginid ﾛｸﾞｲﾝID
     * @param loginpasswd ﾊﾟｽﾜｰﾄﾞ
     * @param useragent ﾕｰｻﾞｰｴｰｼﾞｪﾝﾄ
     * @param remoteip ﾘﾓｰﾄIPｱﾄﾞﾚｽ
     * @param loginkind ログイン種別(1:PC,2:携帯)
     * @return 処理結果(1:正常、2:ﾕｰｻﾞ名なし,3:ﾊﾟｽﾜｰﾄﾞ違い,4:その他)
     */
    public int getUserDbInfo(String hotelid, String loginid, String loginpasswd, String useragent, String remoteip, int loginkind)
    {
//        System.out.println( "hotelid:"+hotelid+",loginid:"+loginid+",loginpasswd:"+loginpasswd);

        int ret;
        int userid;
        String query;
        String logdetail;
        ResultSet result;
        DbAccess db;
        DbAccess db_update;
        DateEdit date;

        ret = 1;
        logdetail = "ログイン完了";
        userid = 0;

        // パラメタチェック
        if ( hotelid == null )
        {
            hotelid = "";
        }
        if ( loginid == null )
        {
            loginid = "";
        }
        if ( loginpasswd == null )
        {
            loginpasswd = "";
        }

        for( ; ; )
        {
            try
            {
                // データベースへの接続
                db = new DbAccess();

                if ( loginkind == 1 )
                {
                    query = "SELECT * FROM owner_user WHERE hotelid='" + hotelid + "' AND loginid='" + loginid + "'";
                }
                else
                {
                    query = "SELECT * FROM owner_user WHERE hotelid='" + hotelid + "' AND machineid='" + loginid + "'";
                }
                // SQL文の実行
                result = db.execQuery( query );
                if ( result.next() != false )
                {
                    // ユーザIDの取得
                    userid = result.getInt( "userid" );
                    DbUserId = userid;

                    // 取得結果の評価
                    // ﾊﾟｽﾜｰﾄﾞﾁｪｯｸ
                    if ( loginkind == 1 )
                    {
                        if ( loginpasswd.compareTo( result.getString( "passwd_pc" ) ) != 0 )
                        {
                            ret = 3;
                            logdetail = "パスワードが違います";
                        }
                    }
                    else
                    {
                        if ( loginpasswd.compareTo( result.getString( "passwd_mobile" ) ) != 0 )
                        {
                            ret = 3;
                            logdetail = "パスワードが違います";
                        }
                    }
                    if ( ret != 3 )
                    { // ﾛｸﾞｲﾝIDの取得
                      // ﾊﾟｽﾜｰﾄﾞの取得
                        if ( loginkind == 1 )
                        {
                            DbLoginUser = result.getString( "loginid" );
                            DbPassword = result.getString( "passwd_pc" );
                        }
                        else
                        {
                            DbLoginUser = result.getString( "machineid" );
                            DbPassword = result.getString( "passwd_mobile" );
                        }
                        // ﾕｰｻﾞｰ名の取得
                        DbUserName = result.getString( "name" );
                        // ユーザレベルの取得
                        DbUserLevel = result.getInt( "level" );
                    }
                }
                else
                {
                    ret = 2;
                    logdetail = "ユーザIDがありません";
                }
                db.close();

            }
            catch ( Exception e )
            {
                ret = 4;
                logdetail = e.toString();
                log.error( "getUserDbInfo:" + e.toString() );
            }

            break;
        }

        // ホテルID・ログインIDがない場合はエラーとする
        if ( hotelid.compareTo( "" ) == 0 ||
                loginid.compareTo( "" ) == 0 ||
                loginpasswd.compareTo( "" ) == 0 )
        {
            ret = 2;
        }

        try
        {
            db_update = new DbAccess();
            date = new DateEdit();

            // ログイン履歴の書き込み
            query = "INSERT INTO owner_user_log VALUES (";
            query = query + "'" + hotelid + "',";
            query = query + "0,";
            query = query + "'" + date.getDate( 0 ) + "',";
            query = query + "'" + date.getTime( 0 ) + "',";
            query = query + userid + ",";
            query = query + "'" + loginid + "',";
            query = query + "'" + loginpasswd + "',";
            query = query + ret + ",";
            query = query + "'" + logdetail + "',";
            query = query + "'" + useragent + "',";
            query = query + "'" + remoteip + "'";
            query = query + ")";

            // SQLクエリーの実行
            db_update.execUpdate( query );
            db_update.close();
        }
        catch ( Exception e )
        {
            log.error( "getUserDbInfo:" + e.toString() );
        }

        return(ret);
    }

    /**
     * セキュリティ情報取得
     * 
     * @param db DbAccessハンドル
     * @param hotelid ﾎﾃﾙID
     * @param userid ユーザID
     * @return 処理結果
     */
    public ResultSet getUserSecurity(DbAccess db, String hotelid, int userid)
    {
        String query;

        // セキュリティ情報取得
        query = "SELECT * FROM owner_user_security WHERE hotelid='" + hotelid + "'";
        query = query + " AND userid=" + userid;

        return(db.execQuery( query ));
    }

    /**
     * ﾎﾃﾙ情報の取得
     * 
     * @param db DbAccessハンドル
     * @param hotelid ﾎﾃﾙID
     * @param userid ユーザID
     * @return 処理結果
     */
    public ResultSet getHotelInfo(DbAccess db, String hotelid)
    {
        String query;

        // ﾎﾃﾙ情報の取得
        query = "SELECT * FROM hotel WHERE hotel_id='" + hotelid + "'";

        return(db.execQuery( query ));
    }

    /**
     * 管理可能ホテルの取得
     * 
     * @param db DbAccessハンドル
     * @param hotelid ﾎﾃﾙID
     * @param userid ユーザID
     * @return 処理結果
     */
    public ResultSet getManageHotel(DbAccess db, String hotelid, int userid)
    {
        String query;

        // 管理可能ホテルの取得
        query = "SELECT hotel.*,owner_user_hotel.accept_hotelid FROM hotel,owner_user_hotel";
        query = query + " WHERE hotelid='" + hotelid + "'";
        query = query + " AND userid = " + userid;
        query = query + " AND owner_user_hotel.accept_hotelid = hotel.hotel_id";

        return(db.execQuery( query ));
    }

    /**
     * 最終ログイン履歴の取得
     * （2件取得：1件目=現在ログイン,2件目=前回ログイン）
     * 
     * @param db DbAccessハンドル
     * @param hotelid ﾎﾃﾙID
     * @param userid ユーザID
     * @return 処理結果
     */
    public ResultSet getLoginHistory(DbAccess db, String hotelid, int userid)
    {
        String query;

        // 最終ログイン履歴の取得（2件取得：1件目=現在ログイン,2件目=前回ログイン）
        query = "SELECT * FROM owner_user_log";
        query = query + " WHERE hotelid = '" + hotelid + "'";
        query = query + " AND userid = " + userid;
        query = query + " AND log_level = 1";
        query = query + " ORDER BY logid DESC LIMIT 2";

        return(db.execQuery( query ));
    }

    /**
     * ホテルID有効性確認処理
     * 
     * @param session_hotel セッション格納ホテルID
     * @param hotelid ﾎﾃﾙID
     * @param userid ユーザID
     * @return 処理結果(1:有効,2:無効)
     */
    public int checkHotelId(String session_hotel, String hotelid, int userid)
    {
        int ret;
        int count;
        DbAccess dbmanage;
        ResultSet resultmanage;

        if ( session_hotel.compareTo( hotelid ) == 0 )
        {
            return(1);
        }

        ret = 2;
        count = 0;

        try
        {
            dbmanage = new DbAccess();
            resultmanage = this.getManageHotel( dbmanage, session_hotel, userid );
            if ( resultmanage != null )
            {
                while( resultmanage.next() != false )
                {
                    if ( resultmanage.getString( "hotel_id" ).compareTo( hotelid ) == 0 )
                    {
                        count++;
                        break;
                    }
                }
            }
            else
            {
            }

            dbmanage.close();
        }
        catch ( Exception e )
        {
        }

        if ( count > 0 )
        {
            ret = 1;
        }

        return(ret);
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
                        if ( result.getString( "front_ip" ).equals( "255.255.255.255" ) )
                        {
                            ret = true;
                        }
                        else
                        {
                            ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ) );
                        }
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
                    query = "SELECT * FROM hotel WHERE hotel_id='" + value + "'";
                    result = db.execQuery( query );
                    if ( result.next() != false )
                    {
                        if ( result.getString( "front_ip" ).equals( "255.255.255.255" ) )
                        {
                            ret = true;
                        }
                        else
                        {
                            ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ) );
                        }
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
                if ( value.equals( "255.255.255.255" ) )
                {
                    ret = true;
                }
                else
                {
                    ret = tcpclient.connectServiceByAddr( value );
                }
                break;

            // セットされたホテルIDで接続
            default:

                ret = tcpclient.connectService( value );
                break;
        }

        return(ret);
    }

    /**
     * 日付チェック
     * 
     * @param intDates
     * @return
     */
    private boolean dateCheck(int... intDates)
    {
        return dateCheck( this.YYYY_MM_DD, intDates );
    }

    /**
     * 日付チェック
     * 
     * @param pattern
     * @param intDates
     * @return
     */
    private boolean dateCheck(String pattern, int... intDates)
    {
        boolean returnFlg = false;

        SimpleDateFormat sdf;

        String tmpStr;
        int tmpDate = 0;

        boolean yearMonthCheck = false;

        try
        {
            if ( intDates != null )
            {
                for( int intDate : intDates )
                {
                    if ( intDate > 0 )
                    {
                        tmpStr = Integer.toString( intDate );

                        // intDateの桁数が6より小さい、または8より大きい場合、異常日付とみなす
                        if ( tmpStr.length() < 6 || tmpStr.length() > 8 )
                        {
                            Logging.error( "[ownerInfo.dateCheck()] 結果 ： 「" + tmpStr + "」は異常日付であるため、電文送信を中断します。 " );
                            return(false);
                        }

                        // intDateの末尾は00であるかどうかチェック
                        yearMonthCheck =
                                (tmpStr.length() == 8 && (intDate % 100 == 0)) // YYYYMM00の場合
                                        || tmpStr.length() == 6; // YYYYMMの場合

                        if ( yearMonthCheck )
                        {
                            pattern = this.YYYY_MM;
                        }

                        if ( this.YYYY_MM.equals( pattern ) ) // 年月チェック
                        {

                            intDate = Integer.parseInt( tmpStr.substring( 0, 4 ) + tmpStr.substring( 4, 6 ) );
                        }

                        tmpDate = intDate;
                        sdf = new SimpleDateFormat( pattern );
                        sdf.setLenient( false );
                        sdf.parse( Integer.toString( intDate ) );

                    }
                }
                returnFlg = true;
            }
        }
        catch ( ParseException e )
        {
            Logging.error( "[ownerInfo.dateCheck()] 結果 ： 「" + tmpDate + "」は異常日付であるため、電文送信を中断します。 Exception = " + e.toString() );
        }

        return returnFlg;
    }

}
// OwnerInfo Class End

