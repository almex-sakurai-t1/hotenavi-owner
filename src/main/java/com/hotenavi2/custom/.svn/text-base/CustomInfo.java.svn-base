/*
 * @(#)CustomInfo.java 2.00 2004/03/31
 * Copyright (C) ALMEX Inc. 2004
 * 顧客情報関連通信APクラス
 */

package com.hotenavi2.custom;

import java.io.Serializable;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.hotenavi2.common.DbAccess;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClient;

/**
 * AMFWEBサービスとの顧客情報関連電文編集・送受信を行う。
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/31
 */
public class CustomInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID           = -9152132824736895499L;

    // ------------------------------------------------------------------------------
    // 定数定義
    // ------------------------------------------------------------------------------
    /** 部屋最大数 **/
    public static final int   CUSTOMINFO_ROOMMAX         = 128;
    /** 記念日最大数 **/
    public static final int   CUSTOMINFO_MEMORIALMAX     = 8;
    /** 利用履歴最大数 **/
    public static final int   CUSTOMINFO_USEHISTORYMAX   = 10;
    /** ポイント履歴最大数 **/
    public static final int   CUSTOMINFO_POINTHISTORYMAX = 10;
    /** 前後ランキング最大数 **/
    public static final int   CUSTOMINFO_RANKINGORDERMAX = 3;
    /** ランキング最大数 **/
    public static final int   CUSTOMINFO_RANKINGMAX      = 5;
    /** 店舗最大数 **/
    public static final int   CUSTOMINFO_TENPOMAX        = 10;
    /** メッセージ最大数 **/
    public static final int   CUSTOMINFO_MESSAGEMAX      = 5;
    /** 通信販売明細最大数 **/
    public static final int   CUSTOMINFO_ORDERMAX        = 10;
    /** イベント文字情報最大数 **/
    public static final int   CUSTOMINFO_EVENTMAX        = 10;

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
    /** (共通)ニックネーム **/
    public String             NickName;
    /** (共通)ユーザID **/
    public String             UserId;
    /** (共通)パスワード **/
    public String             Password;

    /** (メンバー情報)名前 **/
    public String             InfoName;
    /** (メンバー情報)フリガナ **/
    public String             InfoKana;
    /** (メンバー情報)性別(1:男性,2:女性) **/
    public int                InfoSex;
    /** (メンバー情報)住所１ **/
    public String             InfoAddress1;
    /** (メンバー情報)住所２ **/
    public String             InfoAddress2;
    /** (メンバー情報)電話番号１ **/
    public String             InfoTel1;
    /** (メンバー情報)電話番号２ **/
    public String             InfoTel2;
    /** (メンバー情報)メンバーランクコード **/
    public int                InfoRankCode;
    /** (メンバー情報)メンバーランク名称 **/
    public String             InfoRankName;
    /** (メンバー情報)利用回数 **/
    public int                InfoUseCount;
    /** (メンバー情報)利用金額 **/
    public int                InfoUseTotal;
    /** (メンバー情報)ポイント１ **/
    public int                InfoPoint;
    /** (メンバー情報)ポイント２ **/
    public int                InfoPoint2;
    /** (メンバー情報)最終来店日 **/
    public int                InfoLastDay;
    /** (メンバー情報)誕生日１(MMDD) **/
    public int                InfoBirthday1;
    /** (メンバー情報)誕生日２(MMDD) **/
    public int                InfoBirthday2;
    /** (メンバー情報)誕生日１有効フラグ(0:有効,1:無効) **/
    public int                InfoBirthday1Flag;
    /** (メンバー情報)誕生日２有効フラグ(0:有効,1:無効) **/
    public int                InfoBirthday2Flag;
    /** (メンバー情報)記念日１(MMDD) **/
    public int                InfoMemorial1;
    /** (メンバー情報)記念日２(MMDD) **/
    public int                InfoMemorial2;
    /** (メンバー情報)記念日１有効フラグ(0:有効,1:無効) **/
    public int                InfoMemorialFlag1;
    /** (メンバー情報)記念日２有効フラグ(0:有効,1:無効) **/
    public int                InfoMemorialFlag2;
    /** (メンバー情報)本日割引率 **/
    public int                InfoDiscount;
    /** (メンバー情報)メールアドレス **/
    public String             InfoMailAddress;
    /** (メンバー情報)メールマガジン配信(1:配信する,2:配信しない) **/
    public int                InfoMailMag;
    /** (メンバー情報)メール配信状態(0:OK,1:配信不能) **/
    public int                InfoMailStatus;
    /** (メンバー情報)車番（地域） **/
    public String             InfoCarArea;
    /** (メンバー情報)車番（種別） **/
    public String             InfoCarKana;
    /** (メンバー情報)車番（車種） **/
    public String             InfoCarType;
    /** (メンバー情報)車番 **/
    public String             InfoCarNo;
    /** (メンバー情報)景品交換有無(0:なし,1:あり) **/
    public int                InfoGiftChange;
    /** (メンバー情報)顧客情報保護区分(0:保護なし,1:保護あり,2:表示しない) **/
    public int                InfoProtect;
    /** (メンバー情報)暗証番号変更許可区分(0:変更禁止,1:変更許可) **/
    public int                InfoSecretChange;
    /** (メンバー情報)暗証番号桁数 **/
    public int                InfoSecretDigit;
    /** (メンバー情報)総利用回数 **/
    public int                InfoUseCountAll;
    /** (メンバー情報)総利用金額 **/
    public int                InfoUseTotalAll;

    /** (利用履歴)ページ開始日付 **/
    public String             UsePageDate;
    /** (利用履歴)ページ開始時刻 **/
    public String             UsePageTime;
    /** (利用履歴)ページ拡張データ **/
    public String             UsePageData;
    /** (利用履歴)次ページ開始日付 **/
    public String             UseNextDate;
    /** (利用履歴)次ページ開始時刻 **/
    public String             UseNextTime;
    /** (利用履歴)次ページ拡張データ **/
    public String             UseNextData;
    /** (利用履歴)前ページ開始日付 **/
    public String             UsePrevDate;
    /** (利用履歴)前ページ開始時刻 **/
    public String             UsePrevTime;
    /** (利用履歴)前ページ拡張データ **/
    public String             UsePrevData;
    /** (利用履歴)有効利用回数 **/
    public int                UseCount;
    /** (利用履歴)日付(MAX:10) **/
    public int                UseDate[];
    /** (利用履歴)部屋名称(MAX:10) **/
    public String             UseRoom[];
    /** (利用履歴)利用金額(MAX:10) **/
    public int                UseTotal[];
    /** (利用履歴)獲得ポイント(MAX:10) **/
    public int                UsePoint[];
    /** (利用履歴)店舗コード(MAX:10) **/
    public int                UseStoreCode[];
    /** (利用履歴)店舗名称(MAX:10) **/
    public String             UseStoreName[];

    /** (ポイント履歴)ページ開始日付 **/
    public String             PointPageDate;
    /** (ポイント履歴)ページ開始時刻 **/
    public String             PointPageTime;
    /** (ポイント履歴)次ページ開始日付 **/
    public String             PointNextDate;
    /** (ポイント履歴)次ページ開始時刻 **/
    public String             PointNextTime;
    /** (ポイント履歴)前ページ開始日付 **/
    public String             PointPrevDate;
    /** (ポイント履歴)前ページ開始時刻 **/
    public String             PointPrevTime;
    /** (ポイント履歴)ポイント種別(1:ポイント１,2:ポイント２) **/
    public int                PointKind;
    /** (ポイント履歴)有効ポイント数 **/
    public int                Point;
    /** (ポイント履歴)日付(MAX:10) **/
    public int                PointDate[];
    /** (ポイント履歴)ポイント(MAX:10) **/
    public int                PointCount[];
    /** (ポイント履歴)ポイント名称(MAX:10) **/
    public String             PointName[];
    /** (ポイント履歴)有効期限(YYYYMMDD)(MAX:10) **/
    public int                PointLimit[];
    /** (ポイント履歴)店舗コード(MAX:10) **/
    public int                PointStoreCode[];
    /** (ポイント履歴)店舗名称(MAX:10) **/
    public String             PointStoreName[];

    /** (メールマガジン)メールアドレス **/
    public String             MailmagAddress;
    /** (メールマガジン)処理種別(1:登録,2:解除) **/
    public int                MailmagKind;

    /** (メールマガジン)メールアドレス **/
    public String             MailReplaceAddress;
    /** (メールマガジン削除)メールアドレス **/
    public String             MailDeleteAddress;

    /** (全室達成)部屋数 **/
    public int                AllRoomCount;
    /** (全室達成)部屋コード(MAX:128) **/
    public int                AllRoomCode[];
    /** (全室達成)部屋名称(MAX:128) **/
    public String             AllRoomName[];
    /** (全室達成)部屋利用数(MAX:128) **/
    public int                AllRoomUse[];
    /** (全室達成)最終利用日(YYYYMMDD)(MAX:128) **/
    public int                AllRoomLast[];
    /** (全室達成)表示位置X(MAX:128) **/
    public int                AllRoomDispX[];
    /** (全室達成)表示位置Y(MAX:128) **/
    public int                AllRoomDispY[];
    /** (全室達成)表示位置Z(MAX:128) **/
    public int                AllRoomDispZ[];

    /** (オーナーズルーム)部屋数 **/
    public int                OwnerRoomCount;
    /** (オーナーズルーム)部屋コード(MAX:128) **/
    public int                OwnerRoomCode[];
    /** (オーナーズルーム)部屋名称(MAX:128) **/
    public String             OwnerRoomName[];
    /** (オーナーズルーム)残り回数(MAX:128) **/
    public int                OwnerRoomUse[];
    /** (オーナーズルーム)有効期限(YYYYMMDD 未達成時:0)(MAX:128) **/
    public int                OwnerRoomLimit[];
    /** (オーナーズルーム)表示位置X(MAX:128) **/
    public int                OwnerRoomDispX[];
    /** (オーナーズルーム)表示位置X(MAX:128) **/
    public int                OwnerRoomDispY[];
    /** (オーナーズルーム)表示位置X(MAX:128) **/
    public int                OwnerRoomDispZ[];

    /** (ランキング)取得区分(1:今回,2:前回) **/
    public int                RankingKind;
    /** (ランキング)取得開始ランキング **/
    public int                RankingGetStart;
    /** (ランキング)最終更新日付(YYYYMMDD) **/
    public int                RankingUpdate;
    /** (ランキング)最終更新時間(HHMM) **/
    public int                RankingUptime;
    /** (ランキング)開催期間開始(YYYYMMDD) **/
    public int                RankingStart;
    /** (ランキング)開催期間終了(YYYYMMDD) **/
    public int                RankingEnd;
    /** (ランキング)次ページ取得開始ランキング **/
    public int                RankingGetNext;
    /** (ランキング)前後ランキング(順位)(配列0:自分,1:一つ前,2:一つ後) **/
    public int                RankingOrderNo[];
    /** (ランキング)前後ランキング(ポイント)(配列0:自分,1:一つ前,2:一つ後) **/
    public int                RankingOrderPoint[];
    /** (ランキング)前後ランキング(利用回数)(配列0:自分,1:一つ前,2:一つ後) **/
    public int                RankingOrderCount[];
    /** (ランキング)前後ランキング(利用金額)(配列0:自分,1:一つ前,2:一つ後) **/
    public int                RankingOrderTotal[];
    /** (ランキング)ランキング(順位)(5件固定) **/
    public int                RankingNo[];
    /** (ランキング)ランキング(ポイント)(5件固定) **/
    public int                RankingPoint[];
    /** (ランキング)ランキング(利用回数)(5件固定) **/
    public int                RankingCount[];
    /** (ランキング)ランキング(利用金額)(5件固定) **/
    public int                RankingTotal[];

    /** (全店全室達成)店舗数 **/
    public int                AllAllTenpoCount;
    /** (全店全室達成)店舗番号(MAX:10) **/
    public int                AllAllTenpoNo[];
    /** (全店全室達成)店舗名称(MAX:10) **/
    public String             AllAllTenpoName[];
    /** (全店全室達成)店舗名称(MAX:10) **/
    public int                AllAllRoomCount[];
    /** (全店全室達成)部屋コード(MAX:10)(MAX:128) **/
    public int                AllAllRoomCode[][];
    /** (全店全室達成)部屋名称(MAX:10)(MAX:128) **/
    public String             AllAllRoomName[][];
    /** (全店全室達成)利用数(MAX:10)(MAX:128) **/
    public int                AllAllRoomUse[][];
    /** (全店全室達成)最終利用日(MAX:10)(MAX:128) **/
    public int                AllAllRoomLast[][];

    /** (メッセージ)共通メッセージ(5件固定) **/
    public String             MessageCommon[];
    /** (メッセージ)メンバーメッセージ(5件固定) **/
    public String             MessageAllMember[];
    /** (メッセージ)メンバー個別メッセージ(5件固定) **/
    public String             MessageMember[];

    /** (通信販売)情報数 **/
    public int                OrderCount;
    /** (通信販売)受付日付(YYYYMMDD) **/
    public int                OrderReceiptDate[];
    /** (通信販売)受付時間(HHMM) **/
    public int                OrderReceiptTime[];
    /** (通信販売)入荷予定日付(YYYYMMDD) **/
    public int                OrderArrivePlanDate[];
    /** (通信販売)入荷予定時間(HHMM) **/
    public int                OrderArrivePlanTime[];
    /** (通信販売)入荷日付(YYYYMMDD) **/
    public int                OrderArriveDate[];
    /** (通信販売)入荷時間(HHMM) **/
    public int                OrderArriveTime[];
    /** (通信販売)受渡日付(YYYYMMDD) **/
    public int                OrderDeliveryDate[];
    /** (通信販売)受渡時間(HHMM) **/
    public int                OrderDeliveryTime[];
    /** (通信販売)状態区分(1:受付,2:入荷待,3:入荷済,4:受渡待,5:受渡済,6:受付取消,7:入荷取消) **/
    public int                OrderOrderMode[];
    /** (通信販売)受渡区分(1:未定,2:即時,3:退室時,4:次回来店時) **/
    public int                OrderDeliveryMode[];
    /** (通信販売)商品名 **/
    public String             OrderGoodsName[];
    /** (通信販売)数量 **/
    public int                OrderGoodsCount[];

    /** (暗証番号)暗証番号 **/
    public String             SecretCode;
    /** (暗証番号)暗証番号取得結果 **/
    public int                SecretResult;
    /** (暗証番号)旧暗証番号 **/
    public String             SecretOldCode;
    /** (暗証番号)顧客情報保護区分 **/
    public int                SecretType;

    /** (ユーザID変更)変更後ユーザID **/
    public String             ChangeUserId;
    /** (ユーザID変更)変更後パスワード **/
    public String             ChangePassword;
    /** (ユーザID変更)ユーザID変更結果 **/
    public int                ChangeResult;

    /** (メッセージ)共通メッセージ１ **/
    public String             MessageCommon1;
    /** (メッセージ)共通メッセージ２ **/
    public String             MessageCommon2;
    /** (メッセージ)共通メッセージ３ **/
    public String             MessageCommon3;
    /** (メッセージ)共通メッセージ４ **/
    public String             MessageCommon4;
    /** (メッセージ)共通メッセージ５ **/
    public String             MessageCommon5;
    /** (メッセージ)メンバーメッセージ１ **/
    public String             MessageMember1;
    /** (メッセージ)メンバーメッセージ２ **/
    public String             MessageMember2;
    /** (メッセージ)メンバーメッセージ３ **/
    public String             MessageMember3;
    /** (メッセージ)メンバーメッセージ４ **/
    public String             MessageMember4;
    /** (メッセージ)メンバーメッセージ５ **/
    public String             MessageMember5;
    /** (メッセージ)個別メッセージ１ **/
    public String             MessageOne1;
    /** (メッセージ)個別メッセージ２ **/
    public String             MessageOne2;
    /** (メッセージ)個別メッセージ３ **/
    public String             MessageOne3;
    /** (メッセージ)個別メッセージ４ **/
    public String             MessageOne4;
    /** (メッセージ)個別メッセージ５ **/
    public String             MessageOne5;

    /** (部屋ビンゴ)部屋数 **/
    public int                BingoRoomCount;
    /** (部屋ビンゴ)部屋コード(MAX:128) **/
    public int                BingoRoomCode[];
    /** (部屋ビンゴ)部屋名称(MAX:128) **/
    public String             BingoRoomName[];
    /** (部屋ビンゴ)部屋利用数(MAX:128) **/
    public int                BingoRoomUse[];
    /** (部屋ビンゴ)最終利用日(YYYYMMDD)(MAX:128) **/
    public int                BingoRoomLast[];
    /** (部屋ビンゴ)表示位置X(MAX:128) **/
    public int                BingoRoomDispX[];
    /** (部屋ビンゴ)表示位置Y(MAX:128) **/
    public int                BingoRoomDispY[];
    /** (部屋ビンゴ)表示位置Z(MAX:128) **/
    public int                BingoRoomDispZ[];
    /** (部屋ビンゴ)達成区分(MAX:128) **/
    public int                BingoRoomFlag[];

    /** (オーナーズセレクション)部屋数 **/
    public int                SelectionRoomCount;
    /** (オーナーズセレクション)部屋コード(MAX:128) **/
    public int                SelectionRoomCode[];
    /** (オーナーズセレクション)部屋名称(MAX:128) **/
    public String             SelectionRoomName[];

    /** (イベント情報)イベント区分 **/
    public String             EventKind;
    /** (イベント情報)表示選択区分 **/
    public int                EventDispKind;
    /** (イベント情報)メッセージ表示区分 **/
    public int                EventMessageKind[];
    /** (イベント情報)表示メッセージ **/
    public String             EventMessage[];
    /** (イベント情報)部屋イベント名称 **/
    public String             EventRoomEventName;
    /** (イベント情報)部屋数 **/
    public int                EventRoomCount;
    /** (イベント情報)部屋コード **/
    public int                EventRoomCode[];
    /** (イベント情報)部屋名称 **/
    public String             EventRoomName[];

    /** (未交換景品一覧)未交換景品数 **/
    public int                GiftCount;
    /** (未交換景品一覧)発生日付 **/
    public int                GiftDate[];
    /** (未交換景品一覧)発生時刻 **/
    public int                GiftTime[];
    /** (未交換景品一覧)有効期限 **/
    public int                GiftExpireDate[];
    /** (未交換景品一覧)景品名称 **/
    public String             GiftName[];

    /** (QRメンバー)誕生日（年） **/
    public String             BirthdayYear;
    /** (QRメンバー)記念日（年） **/
    public String             AnniversaryYear;
    /** (メンバー情報変更／QRメンバー)記念日（月） **/
    public String             Anniversary1;
    /** (メンバー情報変更／QRメンバー)記念日（日） **/
    public String             Anniversary2;
    /** (メンバー情報変更／QRメンバー)記念日２（年） **/
    public String             Anniversary2Year;
    /** (メンバー情報変更／QRメンバー)記念日２（月） **/
    public String             Anniversary2_1;
    /** (メンバー情報変更／QRメンバー)記念日２（日） **/
    public String             Anniversary2_2;
    /** (メンバー情報変更／QRメンバー)誕生日２（年） **/
    public String             Birthday2Year;
    /** (メンバー情報変更／QRメンバー)誕生日２（月） **/
    public String             Birthday2_1;
    /** (メンバー情報変更／QRメンバー)誕生日２（日） **/
    public String             Birthday2_2;

    // 以下タッチ端末で使用するデータ
    /** (メンバー受付情報)部屋コード **/
    public int                TouchRoomNo;
    /** (メンバー受付情報)誕生日(年） **/
    public int                TouchBirthYear1;
    /** (メンバー受付情報)誕生日(月） **/
    public int                TouchBirthMonth1;
    /** (メンバー受付情報)誕生日(日） **/
    public int                TouchBirthDate1;
    /** (メンバー受付情報)名前 **/
    public String             TouchName;
    /** (メンバー受付情報)名前 **/
    public String             TouchNameKana;
    /** (メンバー受付情報)性別 **/
    public int                TouchSex;
    /** (メンバー受付情報)住所1 **/
    public String             TouchAddr1;
    /** (メンバー受付情報)住所2 **/
    public String             TouchAddr2;
    /** (メンバー受付情報)電話1 **/
    public String             TouchTel1;
    /** (メンバー受付情報)電話2 **/
    public String             TouchTel2;
    /** (メンバー受付情報)メルアド **/
    public String             TouchMailAddr;
    /** (メンバー受付情報)メルマガ **/
    public int                TouchMailMag;
    /** (メンバー受付情報)記念年1(年） **/
    public int                TouchMemorialYear1;
    /** (メンバー受付情報)記念月1(月） **/
    public int                TouchMemorialMonth1;
    /** (メンバー受付情報)記念日1(日） **/
    public int                TouchMemorialDate1;
    /** (メンバー受付情報)誕生日2(年） **/
    public int                TouchBirthYear2;
    /** (メンバー受付情報)誕生日2(月） **/
    public int                TouchBirthMonth2;
    /** (メンバー受付情報)誕生日2(日） **/
    public int                TouchBirthDate2;
    /** (メンバー受付情報)記念年2 **/
    public int                TouchMemorialYear2;
    /** (メンバー受付情報)記念月2 **/
    public int                TouchMemorialMonth2;
    /** (メンバー受付情報)記念日2 **/
    public int                TouchMemorialDate2;
    /** (メンバー受付情報)セキュリティコード **/
    public String             TouchSecurityCode;
    /** (メンバー受付情報)ハピホテチェックインコード **/
    public int                TouchSeq;
    /** (メンバー受付情報)ハピホテチェックインコード **/
    public String             TouchRoomName;
    /** (メンバー受付情報)ハピホテチェックインコード **/
    public int                StartDate;
    /** (メンバー受付情報)ハピホテチェックインコード **/
    public int                EndDate;
    /** (メンバー受付情報)タッチデータ数 **/
    public int                TouchData;
    /** (メンバー受付情報)集計日 **/
    public int[]              CollectDate;
    /** (メンバー受付情報)全チェックイン数 **/
    public int[]              AllCheckIn;
    /** (メンバー受付情報)ハピホテメンバーチェックイン数 **/
    public int[]              MemberCheckIn;
    /** (メンバー受付情報)全メンバー数 **/
    public int[]              AllMember;
    /** （メンバーカード課金) 商品コード **/
    public int                GoodsCode;
    /** （メンバーカード課金 商品金額 **/
    public int                GoodsPrice;
    /** （メンバーチェックイン実績要求 **/
    public String             TermId;

    /** デバッグログ **/
    public LogLib             log;

    /**
     * 顧客情報データの初期化を行います。
     * 
     */
    public CustomInfo()
    {
        Result = 0;
        HotelId = "";
        CustomId = "";
        Birthday1 = "";
        Birthday2 = "";
        NickName = "";
        UserId = "";
        Password = "";

        InfoName = "";
        InfoKana = "";
        InfoSex = 0;
        InfoAddress1 = "";
        InfoAddress2 = "";
        InfoTel1 = "";
        InfoTel2 = "";
        InfoRankCode = 0;
        InfoRankName = "";
        InfoUseCount = 0;
        InfoUseTotal = 0;
        InfoPoint = 0;
        InfoPoint2 = 0;
        InfoLastDay = 0;
        InfoBirthday1 = 0;
        InfoBirthday2 = 0;
        InfoBirthday1Flag = 0;
        InfoBirthday2Flag = 0;
        InfoMemorial1 = 0;
        InfoMemorial2 = 0;
        InfoMemorialFlag1 = 0;
        InfoMemorialFlag2 = 0;
        InfoDiscount = 0;
        InfoMailAddress = "";
        InfoMailMag = 0;
        InfoMailStatus = 0;
        InfoCarArea = "";
        InfoCarKana = "";
        InfoCarType = "";
        InfoCarNo = "";
        InfoGiftChange = 0;
        InfoProtect = 0;
        InfoSecretChange = 0;
        InfoSecretDigit = 0;
        InfoUseCountAll = 0;
        InfoUseTotalAll = 0;

        UsePageDate = "";
        UsePageTime = "";
        UsePageData = "";
        UseNextDate = "";
        UseNextTime = "";
        UseNextData = "";
        UsePrevDate = "";
        UsePrevTime = "";
        UsePrevData = "";
        UseCount = 0;
        UseDate = new int[CUSTOMINFO_USEHISTORYMAX];
        UseRoom = new String[CUSTOMINFO_USEHISTORYMAX];
        UseTotal = new int[CUSTOMINFO_USEHISTORYMAX];
        UsePoint = new int[CUSTOMINFO_USEHISTORYMAX];
        UseStoreCode = new int[CUSTOMINFO_USEHISTORYMAX];
        UseStoreName = new String[CUSTOMINFO_USEHISTORYMAX];

        PointPageDate = "";
        PointPageTime = "";
        PointNextDate = "";
        PointNextTime = "";
        PointPrevDate = "";
        PointPrevTime = "";
        PointKind = 0;
        Point = 0;
        PointDate = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointCount = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointName = new String[CUSTOMINFO_POINTHISTORYMAX];
        PointLimit = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointStoreCode = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointStoreName = new String[CUSTOMINFO_POINTHISTORYMAX];

        MailmagAddress = "";
        MailmagKind = 0;

        MailReplaceAddress = "";
        MailDeleteAddress = "";

        AllRoomCount = 0;
        AllRoomCode = new int[CUSTOMINFO_ROOMMAX];
        AllRoomName = new String[CUSTOMINFO_ROOMMAX];
        AllRoomUse = new int[CUSTOMINFO_ROOMMAX];
        AllRoomLast = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispZ = new int[CUSTOMINFO_ROOMMAX];

        OwnerRoomCount = 0;
        OwnerRoomCode = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomName = new String[CUSTOMINFO_ROOMMAX];
        OwnerRoomUse = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomLimit = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispZ = new int[CUSTOMINFO_ROOMMAX];

        RankingKind = 0;
        RankingGetStart = 0;
        RankingUpdate = 0;
        RankingUptime = 0;
        RankingStart = 0;
        RankingEnd = 0;
        RankingGetNext = 0;
        RankingOrderNo = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderPoint = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderCount = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderTotal = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingNo = new int[CUSTOMINFO_RANKINGMAX];
        RankingPoint = new int[CUSTOMINFO_RANKINGMAX];
        RankingCount = new int[CUSTOMINFO_RANKINGMAX];
        RankingTotal = new int[CUSTOMINFO_RANKINGMAX];

        AllAllTenpoCount = 0;
        AllAllTenpoNo = new int[CUSTOMINFO_TENPOMAX];
        AllAllTenpoName = new String[CUSTOMINFO_TENPOMAX];
        AllAllRoomCount = new int[CUSTOMINFO_TENPOMAX];
        AllAllRoomCode = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomName = new String[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomUse = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomLast = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];

        MessageCommon = new String[CUSTOMINFO_MESSAGEMAX];
        MessageAllMember = new String[CUSTOMINFO_MESSAGEMAX];
        MessageMember = new String[CUSTOMINFO_MESSAGEMAX];

        OrderCount = 0;
        OrderReceiptDate = new int[CUSTOMINFO_ORDERMAX];
        OrderReceiptTime = new int[CUSTOMINFO_ORDERMAX];
        OrderArrivePlanDate = new int[CUSTOMINFO_ORDERMAX];
        OrderArrivePlanTime = new int[CUSTOMINFO_ORDERMAX];
        OrderArriveDate = new int[CUSTOMINFO_ORDERMAX];
        OrderArriveTime = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryDate = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryTime = new int[CUSTOMINFO_ORDERMAX];
        OrderOrderMode = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryMode = new int[CUSTOMINFO_ORDERMAX];
        OrderGoodsName = new String[CUSTOMINFO_ORDERMAX];
        OrderGoodsCount = new int[CUSTOMINFO_ORDERMAX];

        SecretCode = "";
        SecretResult = 0;
        SecretOldCode = "";
        SecretType = 0;

        ChangeUserId = "";
        ChangePassword = "";
        ChangeResult = 0;

        MessageCommon1 = "";
        MessageCommon2 = "";
        MessageCommon3 = "";
        MessageCommon4 = "";
        MessageCommon5 = "";
        MessageMember1 = "";
        MessageMember2 = "";
        MessageMember3 = "";
        MessageMember4 = "";
        MessageMember5 = "";
        MessageOne1 = "";
        MessageOne2 = "";
        MessageOne3 = "";
        MessageOne4 = "";
        MessageOne5 = "";

        BingoRoomCount = 0;
        BingoRoomCode = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomName = new String[CUSTOMINFO_ROOMMAX];
        BingoRoomUse = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomLast = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispZ = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomFlag = new int[CUSTOMINFO_ROOMMAX];

        SelectionRoomCount = 0;
        SelectionRoomCode = new int[CUSTOMINFO_ROOMMAX];
        SelectionRoomName = new String[CUSTOMINFO_ROOMMAX];

        EventKind = "00";
        EventDispKind = 0;
        EventMessageKind = new int[CUSTOMINFO_EVENTMAX];
        EventMessage = new String[CUSTOMINFO_EVENTMAX];
        EventRoomEventName = "";
        EventRoomCount = 0;
        EventRoomCode = new int[CUSTOMINFO_ROOMMAX];
        EventRoomName = new String[CUSTOMINFO_ROOMMAX];

        GiftCount = 0;
        GiftDate = new int[0];
        GiftTime = new int[0];
        GiftExpireDate = new int[0];
        GiftName = new String[0];

        BirthdayYear = "";
        AnniversaryYear = "0000";
        Anniversary1 = "00";
        Anniversary2 = "00";
        Anniversary2Year = "0000";
        Anniversary2_1 = "00";
        Anniversary2_2 = "00";
        Birthday2Year = "0000";
        Birthday2_1 = "00";
        Birthday2_2 = "00";

        // 以下タッチ端末で使用するデータ
        TouchRoomNo = 0;
        TouchBirthYear1 = 0;
        TouchBirthMonth1 = 0;
        TouchBirthDate1 = 0;
        TouchName = "";
        TouchNameKana = "";
        TouchSex = 0;
        TouchAddr1 = "";
        TouchAddr2 = "";
        TouchTel1 = "";
        TouchTel2 = "";
        TouchMailAddr = "";
        TouchMailMag = 0;
        TouchMemorialYear1 = 0;
        TouchMemorialMonth1 = 0;
        TouchMemorialDate1 = 0;
        TouchBirthYear2 = 0;
        TouchBirthMonth2 = 0;
        TouchBirthDate2 = 0;
        TouchMemorialYear2 = 0;
        TouchMemorialMonth2 = 0;
        TouchMemorialDate2 = 0;
        TouchSecurityCode = "";
        TouchSeq = 0;
        TouchRoomName = "";
        StartDate = 0;
        EndDate = 0;
        TouchData = 0;

        log = new LogLib();
        TermId = "ceritfiedid";
    }

    // ------------------------------------------------------------------------------
    //
    // 電文処理
    //
    // ------------------------------------------------------------------------------
    /**
     * 電文送信処理(1000)
     * メンバーログイン
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1000()
    {
        return(sendPacket1000Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1000)
     * メンバーログイン
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1000(int kind, String value)
    {
        return(sendPacket1000Sub( kind, value ));
    }

    /**
     * 電文送信処理(1000)
     * メンバーログイン
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1000Sub(int kind, String value)
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

        // データのクリア
        Result = 0;
        NickName = "";
        InfoRankCode = 0;
        InfoRankName = "";
        InfoProtect = 0;
        InfoSecretChange = 0;
        InfoSecretDigit = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1000";

                // メンバーID
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                // else if ( !UserId.equals( "    " ) && UserId.length() >= 6 )
                // {
                // strSend = strSend + format.leftFitFormat( "    ", 9 );
                // }
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1001" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ログイン時のみ戻り値を取得する
                            strData = new String( cRecv, 36, 9 );
                            CustomId = strData.trim();
                            strData = new String( cRecv, 45, 2 );
                            Birthday1 = strData.trim();
                            strData = new String( cRecv, 47, 2 );
                            Birthday2 = strData.trim();
                            strData = new String( cRecv, 49, 32 );
                            UserId = strData.trim();
                            strData = new String( cRecv, 81, 8 );
                            Password = strData.trim();

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 景品交換有無
                            strData = new String( cRecv, 111, 2 );
                            InfoGiftChange = Integer.valueOf( strData ).intValue();

                            // メンバーランクコード
                            strData = new String( cRecv, 113, 3 );
                            InfoRankCode = Integer.valueOf( strData ).intValue();

                            // メンバーランク名称
                            strData = new String( cRecv, 116, 40 );
                            InfoRankName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 顧客情報保護区分
                            strData = new String( cRecv, 156, 2 );
                            InfoProtect = Integer.valueOf( strData ).intValue();

                            // 暗証番号変更許可区分
                            strData = new String( cRecv, 158, 2 );
                            InfoSecretChange = Integer.valueOf( strData ).intValue();

                            // 暗証番号桁数
                            strData = new String( cRecv, 160, 2 );
                            InfoSecretDigit = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1000:" + e.toString() );
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
     * 電文送信処理(1002)
     * メンバー情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1002()
    {
        return(sendPacket1002Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1002)
     * メンバー情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1002(int kind, String value)
    {
        return(sendPacket1002Sub( kind, value ));
    }

    /**
     * 電文送信処理(1002)
     * メンバー情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1002Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        InfoName = "";
        InfoKana = "";
        InfoSex = 0;
        InfoAddress1 = "";
        InfoAddress2 = "";
        InfoTel1 = "";
        InfoTel2 = "";
        InfoRankName = "";
        InfoUseCount = 0;
        InfoUseTotal = 0;
        InfoPoint = 0;
        InfoPoint2 = 0;
        InfoLastDay = 0;
        InfoBirthday1 = 0;
        InfoBirthday2 = 0;
        InfoBirthday1Flag = 0;
        InfoBirthday2Flag = 0;
        InfoMemorial1 = 0;
        InfoMemorial2 = 0;
        InfoMemorialFlag1 = 0;
        InfoMemorialFlag2 = 0;
        InfoDiscount = 0;
        InfoMailAddress = "";
        InfoMailMag = 0;
        InfoMailStatus = 0;
        InfoCarArea = "";
        InfoCarKana = "";
        InfoCarType = "";
        InfoCarNo = "";
        InfoGiftChange = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1002";

                // メンバーID
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        // コマンド取得
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1003" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 性別
                            strData = new String( cRecv, 111, 2 );
                            InfoSex = Integer.valueOf( strData ).intValue();

                            // メンバーランク名取得
                            strData = new String( cRecv, 113, 40 );
                            InfoRankName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 利用回数
                            strData = new String( cRecv, 153, 9 );
                            InfoUseCount = Integer.valueOf( strData ).intValue();

                            // ポイント
                            strData = new String( cRecv, 162, 9 );
                            InfoPoint = Integer.valueOf( strData ).intValue();

                            // ポイント２
                            strData = new String( cRecv, 171, 9 );
                            InfoPoint2 = Integer.valueOf( strData ).intValue();

                            // 誕生日１
                            strData = new String( cRecv, 180, 4 );
                            InfoBirthday1 = Integer.valueOf( strData ).intValue();

                            // 誕生日２
                            strData = new String( cRecv, 184, 4 );
                            InfoBirthday2 = Integer.valueOf( strData ).intValue();

                            // 誕生日１有効フラグ
                            strData = new String( cRecv, 188, 2 );
                            InfoBirthday1Flag = Integer.valueOf( strData ).intValue();

                            // 誕生日２有効フラグ
                            strData = new String( cRecv, 190, 2 );
                            InfoBirthday2Flag = Integer.valueOf( strData ).intValue();

                            // 記念日１
                            strData = new String( cRecv, 192, 4 );
                            InfoMemorial1 = Integer.valueOf( strData ).intValue();

                            // 記念日２
                            strData = new String( cRecv, 196, 4 );
                            InfoMemorial2 = Integer.valueOf( strData ).intValue();

                            // 記念日１有効フラグ
                            strData = new String( cRecv, 200, 2 );
                            InfoMemorialFlag1 = Integer.valueOf( strData ).intValue();

                            // 記念日２有効フラグ
                            strData = new String( cRecv, 202, 2 );
                            InfoMemorialFlag2 = Integer.valueOf( strData ).intValue();

                            // 本日割引率
                            strData = new String( cRecv, 204, 3 );
                            InfoDiscount = Integer.valueOf( strData ).intValue();

                            // メールアドレス
                            strData = new String( cRecv, 207, 63 );
                            InfoMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールマガジン配信
                            strData = new String( cRecv, 270, 2 );
                            InfoMailMag = Integer.valueOf( strData ).intValue();

                            // 車番（地域）
                            strData = new String( cRecv, 272, 8 );
                            InfoCarArea = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 車番（種別）
                            strData = new String( cRecv, 280, 2 );
                            InfoCarKana = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 車番（車種）
                            strData = new String( cRecv, 282, 3 );
                            InfoCarType = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 車番
                            strData = new String( cRecv, 285, 4 );
                            InfoCarNo = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メール配信状態
                            strData = new String( cRecv, 289, 2 );
                            InfoMailStatus = Integer.valueOf( strData ).intValue();

                            // 名前
                            strData = new String( cRecv, 291, 40 );
                            InfoName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // フリガナ
                            strData = new String( cRecv, 331, 20 );
                            InfoKana = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 住所１
                            strData = new String( cRecv, 351, 40 );
                            InfoAddress1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 住所２
                            strData = new String( cRecv, 391, 40 );
                            InfoAddress2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 電話番号１
                            strData = new String( cRecv, 431, 15 );
                            InfoTel1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 電話番号２
                            strData = new String( cRecv, 446, 15 );
                            InfoTel2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 利用金額
                            strData = new String( cRecv, 461, 9 );
                            InfoUseTotal = Integer.valueOf( strData ).intValue();

                            // 最終来店日
                            strData = new String( cRecv, 470, 8 );
                            InfoLastDay = Integer.valueOf( strData ).intValue();

                            try
                            {
                                // 総利用回数
                                strData = new String( cRecv, 478, 9 );
                                InfoUseCountAll = Integer.valueOf( strData ).intValue();
                            }
                            catch ( Exception e )
                            {
                                InfoUseCountAll = 0;
                            }

                            try
                            {
                                // 総利用金額
                                strData = new String( cRecv, 487, 9 );
                                InfoUseTotalAll = Integer.valueOf( strData ).intValue();
                            }
                            catch ( Exception e )
                            {
                                InfoUseTotalAll = 0;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1002:" + e.toString() );
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
     * 電文送信処理(1004)
     * 利用履歴取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1004()
    {
        return(sendPacket1004Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1004)
     * 利用履歴取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1004(int kind, String value)
    {
        return(sendPacket1004Sub( kind, value ));
    }

    /**
     * 電文送信処理(1004)
     * 利用履歴取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1004Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        UseNextDate = "";
        UseNextTime = "";
        UseNextData = "";
        UsePrevDate = "";
        UsePrevTime = "";
        UsePrevData = "";
        UseCount = 0;
        UseDate = new int[CUSTOMINFO_USEHISTORYMAX];
        UseRoom = new String[CUSTOMINFO_USEHISTORYMAX];
        UseTotal = new int[CUSTOMINFO_USEHISTORYMAX];
        UsePoint = new int[CUSTOMINFO_USEHISTORYMAX];
        UseStoreCode = new int[CUSTOMINFO_USEHISTORYMAX];
        UseStoreName = new String[CUSTOMINFO_USEHISTORYMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1004";
                // メンバーID
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
                // 誕生日（ページ開始日付）
                strSend = strSend + format.leftFitFormat( UsePageDate, 8 );
                // 誕生日（ページ開始時間）
                strSend = strSend + format.leftFitFormat( UsePageTime, 8 );
                // 誕生日（ページ拡張データ）
                strSend = strSend + format.leftFitFormat( UsePageData, 8 );
                // 予備
                strSend = strSend + "          ";

                // 電文のヘッダ取得
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
                        if ( strData.compareTo( "1005" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 次ページ開始日付
                            strData = new String( cRecv, 111, 8 );
                            UseNextDate = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 次ページ開始時刻
                            strData = new String( cRecv, 119, 8 );
                            UseNextTime = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 次ページ拡張データ
                            strData = new String( cRecv, 127, 8 );
                            UseNextData = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 前ページ開始日付
                            strData = new String( cRecv, 135, 8 );
                            UsePrevDate = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 前ページ開始時刻
                            strData = new String( cRecv, 143, 8 );
                            UsePrevTime = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 前ページ拡張データ
                            strData = new String( cRecv, 151, 8 );
                            UsePrevData = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 利用回数
                            strData = new String( cRecv, 159, 9 );
                            UseCount = Integer.valueOf( strData ).intValue();

                            // 利用履歴
                            for( i = 0 ; i < CUSTOMINFO_USEHISTORYMAX ; i++ )
                            {
                                // 日付
                                strData = new String( cRecv, 168 + (i * 78), 8 );
                                if ( strData != null )
                                {
                                    UseDate[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 部屋名称
                                strData = new String( cRecv, 176 + (i * 78), 8 );
                                if ( strData != null )
                                {
                                    UseRoom[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }

                                // 利用金額
                                strData = new String( cRecv, 184 + (i * 78), 9 );
                                if ( strData != null )
                                {
                                    UseTotal[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 獲得ポイント
                                strData = new String( cRecv, 193 + (i * 78), 9 );
                                if ( strData != null )
                                {
                                    UsePoint[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 店舗コード
                                strData = new String( cRecv, 202 + (i * 78), 4 );
                                if ( strData != null )
                                {
                                    UseStoreCode[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 店舗名称
                                strData = new String( cRecv, 206 + (i * 78), 30 );
                                if ( strData != null )
                                {
                                    UseStoreName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1004:" + e.toString() );
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
     * 電文送信処理(1006)
     * ポイント履歴取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1006()
    {
        return(sendPacket1006Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1006)
     * ポイント履歴取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1006(int kind, String value)
    {
        return(sendPacket1006Sub( kind, value ));
    }

    /**
     * 電文送信処理(1006)
     * ポイント履歴取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1006Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        String strSign;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        PointNextDate = "";
        PointNextTime = "";
        PointPrevDate = "";
        PointPrevTime = "";
        Point = 0;
        PointDate = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointCount = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointName = new String[CUSTOMINFO_POINTHISTORYMAX];
        PointLimit = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointStoreCode = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointStoreName = new String[CUSTOMINFO_POINTHISTORYMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1006";
                // メンバーID
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
                // ページ開始日付
                strSend = strSend + format.leftFitFormat( PointPageDate, 8 );
                // ページ開始時刻
                strSend = strSend + format.leftFitFormat( PointPageTime, 8 );
                // ポイント種別
                nf = new DecimalFormat( "00" );
                strData = nf.format( PointKind );
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1007" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 次ページ開始日付
                            strData = new String( cRecv, 111, 8 );
                            PointNextDate = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 次ページ開始時刻
                            strData = new String( cRecv, 119, 8 );
                            PointNextTime = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 前ページ開始日付
                            strData = new String( cRecv, 127, 8 );
                            PointPrevDate = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 前ページ開始時刻
                            strData = new String( cRecv, 135, 8 );
                            PointPrevTime = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ポイント種別
                            strData = new String( cRecv, 143, 2 );
                            PointKind = Integer.valueOf( strData ).intValue();

                            // 有効ポイント数
                            strData = new String( cRecv, 145, 9 );
                            Point = Integer.valueOf( strData ).intValue();

                            // ポイント履歴
                            for( i = 0 ; i < CUSTOMINFO_POINTHISTORYMAX ; i++ )
                            {
                                // 日付
                                strData = new String( cRecv, 154 + (i * 128), 8 );
                                if ( strData != null )
                                {
                                    PointDate[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 符号
                                strSign = new String( cRecv, 162 + (i * 128), 1 );
                                // ポイント
                                strData = new String( cRecv, 163 + (i * 128), 8 );
                                if ( strData != null )
                                {
                                    PointCount[i] = Integer.parseInt( strData );
                                }
                                if ( strSign.compareTo( "-" ) == 0 )
                                {
                                    PointCount[i] *= -1;
                                }

                                // ポイント名称
                                strData = new String( cRecv, 171 + (i * 128), 40 );
                                PointName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ポイント有効期限
                                strData = new String( cRecv, 211 + (i * 128), 8 );
                                if ( strData != null )
                                {
                                    PointLimit[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 店舗コード
                                strData = new String( cRecv, 219 + (i * 128), 4 );
                                if ( strData != null )
                                {
                                    PointStoreCode[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 店舗名称
                                strData = new String( cRecv, 223 + (i * 128), 30 );
                                if ( strData != null )
                                {
                                    PointStoreName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1006:" + e.toString() );
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
     * 電文送信処理(1008)
     * 全室達成情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1008()
    {
        return(sendPacket1008Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1008)
     * 全室達成情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1008(int kind, String value)
    {
        return(sendPacket1008Sub( kind, value ));
    }

    /**
     * 電文送信処理(1008)
     * 全室達成情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1008Sub(int kind, String value)
    {
        int i;
        int count;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        AllRoomCount = 0;
        AllRoomCode = new int[CUSTOMINFO_ROOMMAX];
        AllRoomName = new String[CUSTOMINFO_ROOMMAX];
        AllRoomUse = new int[CUSTOMINFO_ROOMMAX];
        AllRoomLast = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispZ = new int[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1008";
                // メンバーID
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1009" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋数
                            strData = new String( cRecv, 111, 3 );
                            count = Integer.valueOf( strData ).intValue();
                            AllRoomCount = count;

                            // 部屋利用状況
                            for( i = 0 ; i < count ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 114 + (i * 32), 3 );
                                if ( strData != null )
                                {
                                    AllRoomCode[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 部屋名称
                                strData = new String( cRecv, 117 + (i * 32), 8 );
                                if ( strData != null )
                                {
                                    AllRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }

                                // 利用数
                                strData = new String( cRecv, 125 + (i * 32), 3 );
                                if ( strData != null )
                                {
                                    AllRoomUse[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 最終利用日
                                strData = new String( cRecv, 128 + (i * 32), 8 );
                                if ( strData != null )
                                {
                                    AllRoomLast[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 表示位置X
                                strData = new String( cRecv, 136 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    AllRoomDispX[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 表示位置Y
                                strData = new String( cRecv, 138 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    AllRoomDispY[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 表示位置Z
                                strData = new String( cRecv, 140 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    AllRoomDispZ[i] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1008:" + e.toString() );
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
     * 電文送信処理(1010)
     * オーナーズルーム情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1010()
    {
        return(sendPacket1010Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1010)
     * オーナーズルーム情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1010(int kind, String value)
    {
        return(sendPacket1010Sub( kind, value ));
    }

    /**
     * 電文送信処理(1010)
     * オーナーズルーム情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1010Sub(int kind, String value)
    {
        int i;
        int count;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        OwnerRoomCount = 0;
        OwnerRoomCode = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomName = new String[CUSTOMINFO_ROOMMAX];
        OwnerRoomUse = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomLimit = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispZ = new int[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1010";
                // メンバーID
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1011" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋数
                            strData = new String( cRecv, 111, 3 );
                            count = Integer.valueOf( strData ).intValue();
                            OwnerRoomCount = count;

                            // 部屋利用状況
                            for( i = 0 ; i < count ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 114 + (i * 32), 3 );
                                if ( strData != null )
                                {
                                    OwnerRoomCode[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 部屋名称
                                strData = new String( cRecv, 117 + (i * 32), 8 );
                                if ( strData != null )
                                {
                                    OwnerRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }

                                // 利用数
                                strData = new String( cRecv, 125 + (i * 32), 3 );
                                if ( strData != null )
                                {
                                    OwnerRoomUse[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 有効期限
                                strData = new String( cRecv, 128 + (i * 32), 8 );
                                if ( strData != null )
                                {
                                    OwnerRoomLimit[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 表示位置X
                                strData = new String( cRecv, 136 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    OwnerRoomDispX[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 表示位置Y
                                strData = new String( cRecv, 138 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    OwnerRoomDispY[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 表示位置Z
                                strData = new String( cRecv, 140 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    OwnerRoomDispZ[i] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1010:" + e.toString() );
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
     * 電文送信処理(1012)
     * ランキング情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1012()
    {
        return(sendPacket1012Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1012)
     * ランキング情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1012(int kind, String value)
    {
        return(sendPacket1012Sub( kind, value ));
    }

    /**
     * 電文送信処理(1012)
     * ランキング情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1012Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        RankingUpdate = 0;
        RankingUptime = 0;
        RankingStart = 0;
        RankingEnd = 0;
        RankingGetNext = 0;
        RankingOrderNo = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderPoint = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderCount = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderTotal = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingNo = new int[CUSTOMINFO_RANKINGMAX];
        RankingPoint = new int[CUSTOMINFO_RANKINGMAX];
        RankingCount = new int[CUSTOMINFO_RANKINGMAX];
        RankingTotal = new int[CUSTOMINFO_RANKINGMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1012";
                // メンバーID
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
                // 取得区分
                nf = new DecimalFormat( "00" );
                strData = nf.format( RankingKind );
                strSend = strSend + strData;
                // 取得開始ランキング
                nf = new DecimalFormat( "000000" );
                strData = nf.format( RankingGetStart );
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1013" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 最終更新日付
                            strData = new String( cRecv, 111, 8 );
                            RankingUpdate = Integer.valueOf( strData ).intValue();

                            // 最終更新時間
                            strData = new String( cRecv, 119, 4 );
                            RankingUptime = Integer.valueOf( strData ).intValue();

                            // 開催期間（開始）
                            strData = new String( cRecv, 123, 8 );
                            RankingStart = Integer.valueOf( strData ).intValue();

                            // 開催期間（終了）
                            strData = new String( cRecv, 131, 8 );
                            RankingEnd = Integer.valueOf( strData ).intValue();

                            // 取得区分
                            strData = new String( cRecv, 139, 2 );
                            RankingKind = Integer.valueOf( strData ).intValue();

                            // 次頁取得開始ランキング
                            strData = new String( cRecv, 141, 6 );
                            RankingGetNext = Integer.valueOf( strData ).intValue();

                            // 前後ランキング情報
                            for( i = 0 ; i < CUSTOMINFO_RANKINGORDERMAX ; i++ )
                            {
                                // 前後ランキング順位
                                strData = new String( cRecv, 147 + (i * 33), 6 );
                                RankingOrderNo[i] = Integer.valueOf( strData ).intValue();

                                // 前後ランキングポイント数
                                strData = new String( cRecv, 153 + (i * 33), 9 );
                                RankingOrderPoint[i] = Integer.valueOf( strData ).intValue();

                                // 前後ランキング利用回数
                                strData = new String( cRecv, 162 + (i * 33), 9 );
                                RankingOrderCount[i] = Integer.valueOf( strData ).intValue();

                                // 前後ランキング利用金額
                                strData = new String( cRecv, 171 + (i * 33), 9 );
                                RankingOrderTotal[i] = Integer.valueOf( strData ).intValue();
                            }

                            // ランキング情報
                            for( i = 0 ; i < CUSTOMINFO_RANKINGMAX ; i++ )
                            {
                                // ランキング順位
                                strData = new String( cRecv, 246 + (i * 33), 6 );
                                RankingNo[i] = Integer.valueOf( strData ).intValue();

                                // ランキングポイント数
                                strData = new String( cRecv, 252 + (i * 33), 9 );
                                RankingPoint[i] = Integer.valueOf( strData ).intValue();

                                // ランキング利用回数
                                strData = new String( cRecv, 261 + (i * 33), 9 );
                                RankingCount[i] = Integer.valueOf( strData ).intValue();

                                // ランキング利用金額
                                strData = new String( cRecv, 270 + (i * 33), 9 );
                                RankingTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1012:" + e.toString() );
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
     * 電文送信処理(1014)
     * メンバー情報変更
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1014()
    {
        return(sendPacket1014Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1014)
     * メンバー情報変更
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1014(int kind, String value)
    {
        return(sendPacket1014Sub( kind, value ));
    }

    /**
     * 電文送信処理(1014)
     * メンバー情報変更
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1014Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        InfoMailAddress = "";

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1014";
                // メンバーID
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
                // ニックネーム
                strData = format.leftFitFormat( NickName, 20 );
                strSend = strSend + strData;
                // 名前
                strData = format.leftFitFormat( InfoName, 40 );
                strSend = strSend + strData;
                // フリガナ
                strData = format.leftFitFormat( InfoKana, 20 );
                strSend = strSend + strData;
                // 性別
                nf = new DecimalFormat( "00" );
                strData = nf.format( InfoSex );
                strSend = strSend + strData;
                // 住所１
                strData = format.leftFitFormat( InfoAddress1, 40 );
                strSend = strSend + strData;
                // 住所２
                strData = format.leftFitFormat( InfoAddress2, 40 );
                strSend = strSend + strData;
                // 電話番号１
                strData = format.leftFitFormat( InfoTel1, 15 );
                strSend = strSend + strData;
                // 電話番号２
                strData = format.leftFitFormat( InfoTel2, 15 );
                strSend = strSend + strData;
                // メールアドレス
                strData = format.leftFitFormat( InfoMailAddress, 63 );
                strSend = strSend + strData;
                // メールマガジン
                nf = new DecimalFormat( "00" );
                strData = nf.format( InfoMailMag );
                strSend = strSend + strData;
                // 車番（地域）
                strData = format.leftFitFormat( InfoCarArea, 8 );
                strSend = strSend + strData;
                // 車番（種別）
                strData = format.leftFitFormat( InfoCarKana, 2 );
                strSend = strSend + strData;
                // 車番（車種）
                strData = format.leftFitFormat( InfoCarType, 3 );
                strSend = strSend + strData;
                // 車番
                strData = format.leftFitFormat( InfoCarNo, 4 );
                strSend = strSend + strData;

                try
                {
                    // 記念日（年）
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( AnniversaryYear ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // 記念日（月）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 記念日（日）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 誕生日２（年）
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( Birthday2Year ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // 誕生日２（月）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2_1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 誕生日２（日）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2_2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 記念日２（年）
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( Anniversary2Year ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // 記念日２（月）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2_1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 記念日２（日）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2_2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }

                // 予備
                for( i = 0 ; i < 124 ; i++ )
                {
                    strSend = strSend + " ";
                }

                try
                {
                    // 電文ヘッダの取得
                    strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );
                }
                catch ( Exception e )
                {
                    strHeader = "";
                }

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
                        if ( strData.compareTo( "1015" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールアドレス
                            strData = new String( cRecv, 111, 63 );
                            InfoMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1014:" + e.toString() );
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
     * 電文送信処理(1016)
     * メールマガジン登録・解除
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1016()
    {
        return(sendPacket1016Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1016)
     * メールマガジン登録・解除
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1016(int kind, String value)
    {
        return(sendPacket1016Sub( kind, value ));
    }

    /**
     * 電文送信処理(1016)
     * メールマガジン登録・解除
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1016Sub(int kind, String value)
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

        // データのクリア
        Result = 0;
        NickName = "";

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1016";
                // メンバーID
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
                // メールアドレス
                strData = format.leftFitFormat( MailmagAddress, 63 );
                strSend = strSend + strData;
                // 処理区分
                nf = new DecimalFormat( "00" );
                strData = nf.format( MailmagKind );
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1017" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールアドレス
                            strData = new String( cRecv, 111, 63 );
                            MailmagAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1016:" + e.toString() );
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
     * 電文送信処理(1018)
     * メールアドレス変更
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1018()
    {
        return(sendPacket1018Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1018)
     * メールアドレス変更
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1018(int kind, String value)
    {
        return(sendPacket1018Sub( kind, value ));
    }

    /**
     * 電文送信処理(1018)
     * メールアドレス変更
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1018Sub(int kind, String value)
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

        // データのクリア
        Result = 0;
        NickName = "";

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1018";
                // メンバーID
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
                // メールアドレス
                strData = format.leftFitFormat( MailReplaceAddress, 63 );
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1019" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールアドレス
                            strData = new String( cRecv, 111, 63 );
                            MailReplaceAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1019:" + e.toString() );
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
     * 電文送信処理(1020)
     * 全店全室達成情報
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1020()
    {
        return(sendPacket1020Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1020)
     * 全店全室達成情報
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1020(int kind, String value)
    {
        return(sendPacket1020Sub( kind, value ));
    }

    /**
     * 電文送信処理(1020)
     * 全店全室達成情報
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1020Sub(int kind, String value)
    {
        int i;
        int j;
        int count;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        AllAllTenpoCount = 0;
        AllAllTenpoNo = new int[CUSTOMINFO_TENPOMAX];
        AllAllTenpoName = new String[CUSTOMINFO_TENPOMAX];
        AllAllRoomCount = new int[CUSTOMINFO_TENPOMAX];
        AllAllRoomCode = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomName = new String[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomUse = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomLast = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1020";
                // メンバーID
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

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "1021" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 店舗数
                            strData = new String( cRecv, 111, 3 );
                            count = Integer.valueOf( strData ).intValue();
                            AllAllTenpoCount = count;

                            // 達成状況
                            for( i = 0 ; i < count ; i++ )
                            {
                                // 店舗番号
                                strData = new String( cRecv, 114 + (i * 2862), 3 );
                                if ( strData != null )
                                {
                                    AllAllTenpoNo[i] = Integer.valueOf( strData ).intValue();
                                }

                                // 店舗名称
                                strData = new String( cRecv, 117 + (i * 2862), 40 );
                                if ( strData != null )
                                {
                                    AllAllTenpoName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }

                                // 部屋数（１２８固定）
                                strData = new String( cRecv, 157 + (i * 2862), 3 );
                                if ( strData != null )
                                {
                                    AllAllRoomCount[i] = Integer.valueOf( strData ).intValue();
                                }

                                for( j = 0 ; j < CUSTOMINFO_ROOMMAX ; j++ )
                                {

                                    // 部屋コード
                                    strData = new String( cRecv, 160 + (i * 2862) + (j * 22), 3 );
                                    if ( strData != null )
                                    {
                                        AllAllRoomCode[i][j] = Integer.valueOf( strData ).intValue();
                                    }

                                    // 部屋名称
                                    strData = new String( cRecv, 163 + (i * 2862) + (j * 22), 8 );
                                    if ( strData != null )
                                    {
                                        AllAllRoomName[i][j] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                    }

                                    // 利用数
                                    strData = new String( cRecv, 171 + (i * 2862) + (j * 22), 3 );
                                    if ( strData != null )
                                    {
                                        AllAllRoomUse[i][j] = Integer.valueOf( strData ).intValue();
                                    }

                                    // 最終利用日
                                    strData = new String( cRecv, 174 + (i * 2862) + (j * 22), 8 );
                                    if ( strData != null )
                                    {
                                        AllAllRoomLast[i][j] = Integer.valueOf( strData ).intValue();
                                    }

                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1020:" + e.toString() );
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
     * 電文送信処理(1022)
     * 通販販売情報
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1022()
    {
        return(sendPacket1022Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1022)
     * 通販販売情報
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1022(int kind, String value)
    {
        return(sendPacket1022Sub( kind, value ));
    }

    /**
     * 電文送信処理(1022)
     * 通販販売情報
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1022Sub(int kind, String value)
    {
        int i;
        int result;
        int count;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        OrderCount = 0;
        OrderReceiptDate = new int[CUSTOMINFO_ORDERMAX];
        OrderReceiptTime = new int[CUSTOMINFO_ORDERMAX];
        OrderArrivePlanDate = new int[CUSTOMINFO_ORDERMAX];
        OrderArrivePlanTime = new int[CUSTOMINFO_ORDERMAX];
        OrderArriveDate = new int[CUSTOMINFO_ORDERMAX];
        OrderArriveTime = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryDate = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryTime = new int[CUSTOMINFO_ORDERMAX];
        OrderOrderMode = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryMode = new int[CUSTOMINFO_ORDERMAX];
        OrderGoodsName = new String[CUSTOMINFO_ORDERMAX];
        OrderGoodsCount = new int[CUSTOMINFO_ORDERMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1022";
                // メンバーID
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

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "1023" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 情報数
                            strData = new String( cRecv, 111, 3 );
                            count = Integer.valueOf( strData ).intValue();
                            OrderCount = count;

                            // 通販データ
                            for( i = 0 ; i < count ; i++ )
                            {
                                // 受付日付
                                strData = new String( cRecv, 114 + (i * 96), 8 );
                                if ( strData != null )
                                {
                                    OrderReceiptDate[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 受付時間
                                strData = new String( cRecv, 122 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderReceiptTime[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 入荷予定日付
                                strData = new String( cRecv, 126 + (i * 96), 8 );
                                if ( strData != null )
                                {
                                    OrderArrivePlanDate[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 入荷予定時間
                                strData = new String( cRecv, 134 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderArrivePlanTime[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 入荷日付
                                strData = new String( cRecv, 138 + (i * 96), 8 );
                                if ( strData != null )
                                {
                                    OrderArriveDate[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 入荷時間
                                strData = new String( cRecv, 146 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderArriveTime[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 受渡日付
                                strData = new String( cRecv, 150 + (i * 96), 8 );
                                if ( strData != null )
                                {
                                    OrderDeliveryDate[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 受渡時間
                                strData = new String( cRecv, 158 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderDeliveryTime[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 状態区分
                                strData = new String( cRecv, 162 + (i * 96), 2 );
                                if ( strData != null )
                                {
                                    OrderOrderMode[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 受渡区分
                                strData = new String( cRecv, 164 + (i * 96), 2 );
                                if ( strData != null )
                                {
                                    OrderDeliveryMode[i] = Integer.valueOf( strData ).intValue();
                                }
                                // 商品名
                                strData = new String( cRecv, 166 + (i * 96), 40 );
                                if ( strData != null )
                                {
                                    OrderGoodsName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                                // 数量
                                strData = new String( cRecv, 206 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderGoodsCount[i] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1022:" + e.toString() );
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
     * 電文送信処理(1024)
     * 暗証番号取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1024()
    {
        return(sendPacket1024Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1024)
     * 暗証番号取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1024(int kind, String value)
    {
        return(sendPacket1024Sub( kind, value ));
    }

    /**
     * 電文送信処理(1024)
     * 暗証番号取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1024Sub(int kind, String value)
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

        // データのクリア
        Result = 0;
        NickName = "";
        SecretResult = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1024";
                // メンバーID
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
                // 暗証番号
                strData = format.leftFitFormat( SecretCode, 8 );
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1025" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 暗証番号結果取得
                            strData = new String( cRecv, 111, 2 );
                            SecretResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1024:" + e.toString() );
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
     * 電文送信処理(1026)
     * 暗証番号設定
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1026()
    {
        return(sendPacket1026Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1026)
     * 暗証番号設定
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1026(int kind, String value)
    {
        return(sendPacket1026Sub( kind, value ));
    }

    /**
     * 電文送信処理(1026)
     * 暗証番号設定
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1026Sub(int kind, String value)
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

        // データのクリア
        Result = 0;
        NickName = "";
        SecretResult = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1026";
                // メンバーID
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
                // 旧暗証番号
                strData = format.leftFitFormat( SecretOldCode, 8 );
                strSend = strSend + strData;
                // 新暗証番号
                strData = format.leftFitFormat( SecretCode, 8 );
                strSend = strSend + strData;
                // 顧客情報保護区分
                nf = new DecimalFormat( "00" );
                strData = nf.format( SecretType );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "1027" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 暗証番号結果取得
                            strData = new String( cRecv, 111, 2 );
                            SecretResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1026:" + e.toString() );
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
     * 電文送信処理(1028)
     * ユーザID変更
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1028()
    {
        return(sendPacket1028Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1028)
     * ユーザID変更
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1028(int kind, String value)
    {
        return(sendPacket1028Sub( kind, value ));
    }

    /**
     * 電文送信処理(1028)
     * ユーザID変更
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1028Sub(int kind, String value)
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

        // データのクリア
        Result = 0;
        NickName = "";
        ChangeResult = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1028";
                // メンバーID
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
                // 変更後ユーザID
                strData = format.leftFitFormat( ChangeUserId, 32 );
                strSend = strSend + strData;
                // 変更後パスワード
                strData = format.leftFitFormat( ChangePassword, 8 );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "1029" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ユーザID変更結果取得
                            strData = new String( cRecv, 111, 2 );
                            ChangeResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1028:" + e.toString() );
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
     * 電文送信処理(1030)
     * 部屋ビンゴ達成情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1030()
    {
        return(sendPacket1030Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1030)
     * 部屋ビンゴ達成情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1030(int kind, String value)
    {
        return(sendPacket1030Sub( kind, value ));
    }

    /**
     * 電文送信処理(1030)
     * 部屋ビンゴ達成情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1030Sub(int kind, String value)
    {
        boolean blnRet;
        int i;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        BingoRoomCount = 0;
        BingoRoomCode = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomName = new String[CUSTOMINFO_ROOMMAX];
        BingoRoomUse = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomLast = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispZ = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomFlag = new int[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1030";
                // メンバーID
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

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "1031" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋数
                            strData = new String( cRecv, 111, 3 );
                            BingoRoomCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < BingoRoomCount ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 114 + (i * 32), 3 );
                                BingoRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 117 + (i * 32), 8 );
                                BingoRoomName[i] = strData.trim();

                                // 利用数
                                strData = new String( cRecv, 125 + (i * 32), 3 );
                                BingoRoomUse[i] = Integer.valueOf( strData ).intValue();

                                // 最終利用日
                                strData = new String( cRecv, 128 + (i * 32), 8 );
                                BingoRoomLast[i] = Integer.valueOf( strData ).intValue();

                                // 表示位置X
                                strData = new String( cRecv, 136 + (i * 32), 2 );
                                BingoRoomDispX[i] = Integer.valueOf( strData ).intValue();

                                // 表示位置Y
                                strData = new String( cRecv, 138 + (i * 32), 2 );
                                BingoRoomDispY[i] = Integer.valueOf( strData ).intValue();

                                // 表示位置Z
                                strData = new String( cRecv, 140 + (i * 32), 2 );
                                BingoRoomDispZ[i] = Integer.valueOf( strData ).intValue();

                                // 達成区分
                                strData = new String( cRecv, 142 + (i * 32), 2 );
                                BingoRoomFlag[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1030:" + e.toString() );
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
     * 電文送信処理(1032)
     * オーナーズセレクション情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1032()
    {
        return(sendPacket1032Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1032)
     * オーナーズセレクション情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1032(int kind, String value)
    {
        return(sendPacket1032Sub( kind, value ));
    }

    /**
     * 電文送信処理(1032)
     * オーナーズセレクション情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1032Sub(int kind, String value)
    {
        boolean blnRet;
        int i;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        SelectionRoomCount = 0;
        SelectionRoomCode = new int[CUSTOMINFO_ROOMMAX];
        SelectionRoomName = new String[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1032";
                // メンバーID
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

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "1033" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋数
                            strData = new String( cRecv, 111, 3 );
                            SelectionRoomCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < SelectionRoomCount ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 124 + (i * 20), 3 );
                                SelectionRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 127 + (i * 20), 8 );
                                SelectionRoomName[i] = strData.trim();

                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1032:" + e.toString() );
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
     * 電文送信処理(1034)
     * メールアドレス削除
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1034()
    {
        return(sendPacket1034Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1034)
     * メールアドレス削除
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1034(int kind, String value)
    {
        return(sendPacket1034Sub( kind, value ));
    }

    /**
     * 電文送信処理(1034)
     * メールアドレス削除
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1034Sub(int kind, String value)
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

        // データのクリア
        Result = 0;
        NickName = "";

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1034";
                // メンバーID
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
                // メールアドレス
                strData = format.leftFitFormat( MailDeleteAddress, 63 );
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1035" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールアドレス
                            strData = new String( cRecv, 111, 63 );
                            MailDeleteAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1035:" + e.toString() );
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
     * 電文送信処理(1036)
     * 未交換景品一覧取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1036()
    {
        return(sendPacket1036Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1036)
     * 未交換景品一覧情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1036(int kind, String value)
    {
        return(sendPacket1036Sub( kind, value ));
    }

    /**
     * 電文送信処理(1032)
     * 未交換景品一覧情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1036Sub(int kind, String value)
    {
        boolean blnRet;
        int i;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        GiftCount = 0;
        GiftDate = new int[0];
        GiftTime = new int[0];
        GiftExpireDate = new int[0];
        GiftName = new String[0];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1036";
                // メンバーID
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

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "1037" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 景品交換数
                            strData = new String( cRecv, 111, 3 );
                            GiftCount = Integer.valueOf( strData ).intValue();

                            GiftDate = new int[GiftCount];
                            GiftTime = new int[GiftCount];
                            GiftExpireDate = new int[GiftCount];
                            GiftName = new String[GiftCount];

                            for( i = 0 ; i < GiftCount ; i++ )
                            {
                                // 発生日付
                                strData = new String( cRecv, 178 + (i * 128), 8 );
                                GiftDate[i] = Integer.valueOf( strData ).intValue();

                                // 発生時刻
                                strData = new String( cRecv, 186 + (i * 128), 4 );
                                GiftTime[i] = Integer.valueOf( strData ).intValue();

                                // 有効期限
                                strData = new String( cRecv, 190 + (i * 128), 8 );
                                GiftExpireDate[i] = Integer.valueOf( strData ).intValue();

                                // 景品名称
                                strData = new String( cRecv, 198 + (i * 128), 40 );
                                GiftName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1036:" + e.toString() );
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
     * 電文送信処理(1038)
     * QRメンバーデータ確認要求
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1038()
    {
        return(sendPacket1038Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1038)
     * QRメンバーデータ確認要求
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1038(int kind, String value)
    {
        return(sendPacket1038Sub( kind, value ));
    }

    /**
     * 電文送信処理(1038)
     * QRメンバーデータ確認要求
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1038Sub(int kind, String value)
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

        // データのクリア
        Result = 0;
        NickName = "";
        InfoRankCode = 0;
        InfoRankName = "";
        InfoProtect = 0;
        InfoSecretChange = 0;
        InfoSecretDigit = 0;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1038";
                // メンバーID
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

                try
                {
                    // 誕生日（月）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 誕生日（日）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
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

                    // 受信待機
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // コマンド取得
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1039" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ログイン時のみ戻り値を取得する
                            strData = new String( cRecv, 36, 9 );
                            CustomId = strData.trim();
                            strData = new String( cRecv, 45, 2 );
                            Birthday1 = strData.trim();
                            strData = new String( cRecv, 47, 2 );
                            Birthday2 = strData.trim();
                            strData = new String( cRecv, 49, 32 );
                            UserId = strData.trim();
                            strData = new String( cRecv, 81, 8 );
                            Password = strData.trim();

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 景品交換有無
                            strData = new String( cRecv, 111, 2 );
                            InfoGiftChange = Integer.valueOf( strData ).intValue();

                            // メンバーランクコード
                            strData = new String( cRecv, 113, 3 );
                            InfoRankCode = Integer.valueOf( strData ).intValue();

                            // メンバーランク名称
                            strData = new String( cRecv, 116, 40 );
                            InfoRankName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 顧客情報保護区分
                            strData = new String( cRecv, 156, 2 );
                            InfoProtect = Integer.valueOf( strData ).intValue();

                            // 暗証番号変更許可区分
                            strData = new String( cRecv, 158, 2 );
                            InfoSecretChange = Integer.valueOf( strData ).intValue();

                            // 暗証番号桁数
                            strData = new String( cRecv, 160, 2 );
                            InfoSecretDigit = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1038:" + e.toString() );
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
     * 電文送信処理(1040)
     * QRメンバーデータ登録
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1040()
    {
        return(sendPacket1040Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1040)
     * QRメンバーデータ登録
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1040(int kind, String value)
    {
        return(sendPacket1040Sub( kind, value ));
    }

    /**
     * 電文送信処理(1040)
     * QRメンバーデータ登録
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1040Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

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
                strSend = "1040";
                // メンバーID
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
                // 誕生日（年）
                nf = new DecimalFormat( "0000" );
                strData = nf.format( Integer.valueOf( BirthdayYear ).intValue() );
                strSend = strSend + strData;
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
                // ニックネーム
                strData = format.leftFitFormat( NickName, 20 );
                strSend = strSend + strData;
                // 名前
                strData = format.leftFitFormat( InfoName, 40 );
                strSend = strSend + strData;
                // フリガナ
                strData = format.leftFitFormat( InfoKana, 20 );
                strSend = strSend + strData;
                // 性別
                nf = new DecimalFormat( "00" );
                strData = nf.format( InfoSex );
                strSend = strSend + strData;
                // 住所１
                strData = format.leftFitFormat( InfoAddress1, 40 );
                strSend = strSend + strData;
                // 住所２
                strData = format.leftFitFormat( InfoAddress2, 40 );
                strSend = strSend + strData;
                // 電話番号１
                strData = format.leftFitFormat( InfoTel1, 15 );
                strSend = strSend + strData;
                // 電話番号２
                strData = format.leftFitFormat( InfoTel2, 15 );
                strSend = strSend + strData;
                // メールアドレス
                strData = format.leftFitFormat( InfoMailAddress, 63 );
                strSend = strSend + strData;
                // メールマガジン
                nf = new DecimalFormat( "00" );
                strData = nf.format( InfoMailMag );
                strSend = strSend + strData;
                // 車番（地域）
                strData = format.leftFitFormat( InfoCarArea, 8 );
                strSend = strSend + strData;
                // 車番（種別）
                strData = format.leftFitFormat( InfoCarKana, 2 );
                strSend = strSend + strData;
                // 車番（車種）
                strData = format.leftFitFormat( InfoCarType, 3 );
                strSend = strSend + strData;
                // 車番
                strData = format.leftFitFormat( InfoCarNo, 4 );
                strSend = strSend + strData;

                try
                {
                    // 記念日（年）
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( AnniversaryYear ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // 記念日（月）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 記念日（日）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 誕生日２（年）
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( Birthday2Year ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // 誕生日２（月）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2_1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 誕生日２（日）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2_2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 記念日２（年）
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( Anniversary2Year ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // 記念日２（月）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2_1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // 記念日２（日）
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2_2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                // 予備
                for( i = 0 ; i < 120 ; i++ )
                {
                    strSend = strSend + " ";
                }

                try
                {
                    // 電文ヘッダの取得
                    strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );
                }
                catch ( Exception e )
                {
                    strHeader = "";
                }

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
                        if ( strData.compareTo( "1041" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メールアドレス
                            strData = new String( cRecv, 111, 63 );
                            InfoMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1040:" + e.toString() );
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
     * 電文送信処理(0400)
     * メッセージ情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0400()
    {
        return(sendPacket0400Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0400)
     * メッセージ情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0400(int kind, String value)
    {
        return(sendPacket0400Sub( kind, value ));
    }

    /**
     * 電文送信処理(0400)
     * メッセージ情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0400Sub(int kind, String value)
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

        // データのクリア
        Result = 0;
        NickName = "";
        MessageCommon1 = "";
        MessageCommon2 = "";
        MessageCommon3 = "";
        MessageCommon4 = "";
        MessageCommon5 = "";
        MessageMember1 = "";
        MessageMember2 = "";
        MessageMember3 = "";
        MessageMember4 = "";
        MessageMember5 = "";
        MessageOne1 = "";
        MessageOne2 = "";
        MessageOne3 = "";
        MessageOne4 = "";
        MessageOne5 = "";

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0400";
                // メンバーID
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

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "0401" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 共通メッセージ１
                            strData = new String( cRecv, 111, 80 );
                            MessageCommon1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 共通メッセージ２
                            strData = new String( cRecv, 191, 80 );
                            MessageCommon2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 共通メッセージ３
                            strData = new String( cRecv, 271, 80 );
                            MessageCommon3 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 共通メッセージ４
                            strData = new String( cRecv, 351, 80 );
                            MessageCommon4 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 共通メッセージ５
                            strData = new String( cRecv, 431, 80 );
                            MessageCommon5 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メンバーメッセージ１
                            strData = new String( cRecv, 511, 80 );
                            MessageMember1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メンバーメッセージ２
                            strData = new String( cRecv, 591, 80 );
                            MessageMember2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メンバーメッセージ３
                            strData = new String( cRecv, 671, 80 );
                            MessageMember3 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メンバーメッセージ４
                            strData = new String( cRecv, 751, 80 );
                            MessageMember4 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メンバーメッセージ５
                            strData = new String( cRecv, 831, 80 );
                            MessageMember5 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 個別メッセージ１
                            strData = new String( cRecv, 911, 80 );
                            MessageOne1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 個別メッセージ２
                            strData = new String( cRecv, 991, 80 );
                            MessageOne2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 個別メッセージ３
                            strData = new String( cRecv, 1071, 80 );
                            MessageOne3 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 個別メッセージ４
                            strData = new String( cRecv, 1151, 80 );
                            MessageOne4 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 個別メッセージ５
                            strData = new String( cRecv, 1231, 80 );
                            MessageOne5 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0400:" + e.toString() );
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
     * 電文送信処理(0402)
     * イベント情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0402()
    {
        return(sendPacket0402Sub( 0, "" ));
    }

    /**
     * 電文送信処理(0402)
     * イベント情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket0402(int kind, String value)
    {
        return(sendPacket0402Sub( kind, value ));
    }

    /**
     * 電文送信処理(0402)
     * イベント情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket0402Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        EventDispKind = 0;
        EventMessageKind = new int[CUSTOMINFO_EVENTMAX];
        EventMessage = new String[CUSTOMINFO_EVENTMAX];
        EventRoomEventName = "";
        EventRoomCount = 0;
        EventRoomCode = new int[CUSTOMINFO_ROOMMAX];
        EventRoomName = new String[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "0402";
                // メンバーID
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
                // イベント区分
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( EventKind ).intValue() );
                strSend = strSend + strData;
                // 予備
                strSend = strSend + "          ";

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "0403" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ニックネーム取得
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 表示選択区分
                            strData = new String( cRecv, 175, 1 );
                            EventDispKind = Integer.valueOf( strData ).intValue();

                            // イベント文字情報
                            for( i = 0 ; i < CUSTOMINFO_EVENTMAX ; i++ )
                            {
                                // 表示区分
                                strData = new String( cRecv, 176 + (i * 41), 1 );
                                EventMessageKind[i] = Integer.valueOf( strData ).intValue();

                                // 表示メッセージ
                                strData = new String( cRecv, 177 + (i * 41), 40 );
                                EventMessage[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }

                            // 部屋イベント名称
                            strData = new String( cRecv, 586, 40 );
                            EventRoomEventName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 部屋数
                            strData = new String( cRecv, 626, 3 );
                            EventRoomCount = Integer.valueOf( strData ).intValue();

                            // イベント部屋情報
                            for( i = 0 ; i < EventRoomCount ; i++ )
                            {
                                // 部屋コード
                                strData = new String( cRecv, 629 + (i * 11), 3 );
                                EventRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // 部屋名称
                                strData = new String( cRecv, 632 + (i * 11), 8 );
                                EventRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0402:" + e.toString() );
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
     * 電文送信処理(1042)
     * メンバー受付情報取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1042()
    {
        return(sendPacket1042Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1042)
     * メンバー受付情報取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1042(int kind, String value)
    {
        return(sendPacket1042Sub( kind, value ));
    }

    /**
     * 電文送信処理(1042)
     * イベント情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1042Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        EventDispKind = 0;
        EventMessageKind = new int[CUSTOMINFO_EVENTMAX];
        EventMessage = new String[CUSTOMINFO_EVENTMAX];
        EventRoomEventName = "";
        EventRoomCount = 0;
        EventRoomCode = new int[CUSTOMINFO_ROOMMAX];
        EventRoomName = new String[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1042";

                // 部屋名称
                strSend += format.leftFitFormat( TouchRoomName, 8 );

                // 予備
                strSend += format.leftFitFormat( "", 120 );

                try
                {
                    // 電文ヘッダ取得
                    strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );
                    // 電文の結合
                    strSend = strHeader + strSend;

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
                        if ( strData.compareTo( "1043" ) == 0 )
                        {
                            // 処理結果取得
                            strData = new String( cRecv, 36, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // 部屋名称
                            strData = new String( cRecv, 38, 8 );
                            TouchRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // メンバーID
                            strData = new String( cRecv, 46, 9 );
                            CustomId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // セキュリティコード
                            strData = new String( cRecv, 55, 5 );
                            TouchSecurityCode = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 誕生日（月）
                            strData = new String( cRecv, 60, 2 );
                            Birthday1 = strData;

                            // 誕生日（日）
                            strData = new String( cRecv, 62, 4 );
                            Birthday2 = strData;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1042:" + e.toString() );
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
     * 電文送信処理(1044)
     * 新規メンバー登録通知取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1044()
    {
        return(sendPacket1044Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1044)
     * 新規メンバー登録通知取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1044(int kind, String value)
    {
        return(sendPacket1044Sub( kind, value ));
    }

    /**
     * 電文送信処理(1044)
     * イベント情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1044Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1044";
                // メンバーID
                strSend += format.leftFitFormat( CustomId, 9 );
                // 予備
                strSend += format.leftFitFormat( "", 140 );

                // 電文ヘッダ取得
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
                        //
                        if ( strData.compareTo( "1045" ) == 0 )
                        {

                            // メンバーID
                            strData = new String( cRecv, 36, 9 );
                            CustomId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // セキュリティコード
                            strData = new String( cRecv, 45, 5 );
                            TouchSecurityCode = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 処理結果取得
                            strData = new String( cRecv, 50, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;
                        }
                    }

                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1044:" + e.toString() );
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
     * 電文送信処理(1046)
     * メンバー登録通知取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1046()
    {
        return(sendPacket1046Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1046)
     * メンバー登録通知取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1046(int kind, String value)
    {
        return(sendPacket1046Sub( kind, value ));
    }

    /**
     * 電文送信処理(1046)
     * イベント情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1046Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        boolean blnCommand;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;

        blnCommand = false;

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1046";
                // メンバーID
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
                // 誕生日（年）
                nf = new DecimalFormat( "0000" );
                strSend += nf.format( TouchBirthYear1 );
                // 誕生日（月）
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchBirthMonth1 );
                // 誕生日（日）
                strSend += nf.format( TouchBirthDate1 );

                // ユーザID
                strSend += format.leftFitFormat( UserId, 32 );

                // パスワード
                strSend += format.leftFitFormat( Password, 8 );

                // ニックネーム
                strSend += format.leftFitFormat( NickName, 20 );

                // 名前
                strSend += format.leftFitFormat( TouchName, 40 );

                // フリガナ
                strSend += format.leftFitFormat( TouchNameKana, 20 );

                // 性別
                strSend += nf.format( TouchSex );

                // 住所1
                strSend += format.leftFitFormat( TouchAddr1, 40 );

                // 住所2
                strSend += format.leftFitFormat( TouchAddr2, 40 );

                // 電話番号1
                strSend += format.leftFitFormat( TouchTel1, 15 );

                // 電話番号2
                strSend += format.leftFitFormat( TouchTel2, 15 );

                // メルアド
                strSend += format.leftFitFormat( TouchMailAddr, 63 );

                // メルマガ
                strSend += nf.format( TouchMailMag );

                // 記念日（年）
                nf = new DecimalFormat( "0000" );
                strSend += nf.format( TouchMemorialYear1 );

                // 記念日（月）
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchMemorialMonth1 );
                // 記念日（日）
                strSend += nf.format( TouchMemorialDate1 );

                // 誕生日2（年）
                nf = new DecimalFormat( "0000" );
                strSend += nf.format( TouchBirthYear2 );

                // 誕生日2（月）
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchBirthMonth2 );
                // 誕生日2（日）
                strSend += nf.format( TouchBirthDate2 );

                // 記念日2（年）
                nf = new DecimalFormat( "0000" );
                strSend += nf.format( TouchMemorialYear2 );
                // 記念日2（月）
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchMemorialMonth2 );
                // 記念日2（日）
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchMemorialDate2 );
                // 予備
                strSend += format.leftFitFormat( "", 140 );

                try
                {
                    strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );

                    // 電文の結合
                    strSend = strHeader + strSend;

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
                        //
                        if ( strData.compareTo( "1047" ) == 0 )
                        {
                            blnCommand = true;

                            // メンバーID
                            strData = new String( cRecv, 36, 9 );
                            CustomId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // セキュリティコード
                            strData = new String( cRecv, 45, 5 );
                            TouchSecurityCode = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // 処理結果取得
                            strData = new String( cRecv, 50, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;
                        }
                    }

                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1044:" + e.toString() );
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
     * 電文送信処理(1048)
     * メンバーチェックイン実績取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1048()
    {
        return(sendPacket1048Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1048)
     * メンバーチェックイン実績取得
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1048(int kind, String value)
    {
        return(sendPacket1048Sub( kind, value ));
    }

    /**
     * 電文送信処理(1048)
     * イベント情報取得
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1048Sub(int kind, String value)
    {
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // データのクリア
        Result = 0;
        NickName = "";
        TermId = "ceritfiedid";
        Password = "6268";

        if ( HotelId != null )
        {
            // ホスト接続処理
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // コマンド
                strSend = "1048";

                // ユーザID
                strSend += format.leftFitFormat( TermId, 11 );

                // パスワード
                strSend += format.leftFitFormat( Password, 4 );

                // ハピホテチェックインコード
                nf = new DecimalFormat( "00000000" );
                // 開始日付
                strSend += nf.format( StartDate );
                // 終了日付
                strSend += nf.format( EndDate );

                // 予備
                strSend += format.leftFitFormat( "", 120 );

                // 電文ヘッダ取得
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
                        if ( strData.compareTo( "1049" ) == 0 )
                        {
                            // ユーザーID
                            strData = new String( cRecv, 36, 11 );
                            TermId = strData;

                            // パスワード
                            strData = new String( cRecv, 47, 4 );
                            Password = strData;

                            // 処理結果取得
                            strData = new String( cRecv, 51, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // 実績データ数
                            strData = new String( cRecv, 53, 3 );
                            TouchData = Integer.valueOf( strData ).intValue();

                            CollectDate = new int[TouchData];
                            AllCheckIn = new int[TouchData];
                            MemberCheckIn = new int[TouchData];
                            AllMember = new int[TouchData];
                            for( int i = 0 ; i < TouchData ; i++ )
                            {
                                // 集計日
                                CollectDate[i] = Integer.valueOf( new String( cRecv, 56 + i * 23, 8 ) ).intValue();

                                // 全チェックイン数
                                AllCheckIn[i] = Integer.valueOf( new String( cRecv, 64 + i * 23, 5 ) ).intValue();

                                // webメンバーチェックイン数
                                MemberCheckIn[i] = Integer.valueOf( new String( cRecv, 69 + i * 23, 5 ) ).intValue();

                                // 全メンバーチェックイン数
                                AllMember[i] = Integer.valueOf( new String( cRecv, 74 + i * 23, 5 ) ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1048:" + e.toString() );
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
     * 電文送信処理(1050)
     * 登録前メンバー情報要求
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1050()
    {
        return(sendPacket1050Sub( 0, "" ));
    }

    /**
     * 電文送信処理(1050)
     * 登録前メンバー情報要求
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1050(int kind, String value)
    {
        return(sendPacket1050Sub( kind, value ));
    }

    /**
     * 電文送信処理(1050)
     * 登録前メンバー情報要求
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1050Sub(int kind, String value)
    {
        this.Result = 0;
        this.TouchBirthYear1 = 0;
        this.TouchBirthMonth1 = 0;
        this.TouchBirthDate1 = 0;
        this.NickName = "";
        this.TouchName = "";
        this.TouchNameKana = "";
        this.TouchSex = 0;
        this.TouchAddr1 = "";
        this.TouchAddr2 = "";
        this.TouchTel1 = "";
        this.TouchTel2 = "";
        this.TouchMailAddr = "";
        this.TouchMailMag = 0;
        this.TouchMemorialYear1 = 0;
        this.TouchMemorialMonth1 = 0;
        this.TouchMemorialDate1 = 0;
        this.TouchBirthYear2 = 0;
        this.TouchBirthMonth2 = 0;
        this.TouchBirthDate2 = 0;
        this.TouchMemorialYear2 = 0;
        this.TouchMemorialMonth2 = 0;
        this.TouchMemorialDate2 = 0;

        if ( this.HotelId != null )
        {
            TcpClient tcpClient = new TcpClient();
            boolean blnRet = connect( tcpClient, kind, value );
            if ( blnRet )
            {
                StringFormat format = new StringFormat();

                String strSend = "1050";

                // メンバーID
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

                NumberFormat nf = new DecimalFormat( "00" );
                String strData = nf.format( Integer.valueOf( this.Birthday1 ).intValue() );
                strSend = strSend + strData;

                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( this.Birthday2 ).intValue() );
                strSend = strSend + strData;

                strSend = strSend + format.leftFitFormat( this.UserId, 32 );

                strSend = strSend + format.leftFitFormat( this.Password, 8 );

                strSend = strSend + "          ";

                String strHeader = tcpClient.getPacketHeader( this.HotelId, strSend.length() );

                strSend = strHeader + strSend;
                try
                {
                    tcpClient.send( strSend );

                    String strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        char[] cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1051" ) == 0 )
                        {
                            strData = new String( cRecv, 36, 2 );
                            this.Result = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 38, 9 );
                            this.CustomId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 47, 4 );
                            this.TouchBirthYear1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 51, 2 );
                            this.TouchBirthMonth1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 53, 2 );
                            this.TouchBirthDate1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 55, 32 );
                            this.UserId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 87, 8 );
                            this.Password = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 95, 20 );
                            this.NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 115, 40 );
                            this.TouchName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 155, 20 );
                            this.TouchNameKana = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 175, 2 );
                            this.TouchSex = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 177, 40 );
                            this.TouchAddr1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 217, 40 );
                            this.TouchAddr2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 257, 15 );
                            this.TouchTel1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 272, 15 );
                            this.TouchTel2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 287, 63 );
                            this.TouchMailAddr = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 350, 2 );
                            this.TouchMailMag = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 352, 4 );
                            this.TouchMemorialYear1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 356, 2 );
                            this.TouchMemorialMonth1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 358, 2 );
                            this.TouchMemorialDate1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 360, 4 );
                            this.TouchBirthYear2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 364, 2 );
                            this.TouchBirthMonth2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 366, 2 );
                            this.TouchBirthDate2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 368, 4 );
                            this.TouchMemorialYear2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 372, 2 );
                            this.TouchMemorialMonth2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 374, 2 );
                            this.TouchMemorialDate2 = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    this.log.error( "sendPacket1050:" + e.toString() );
                    return false;
                }
                tcpClient.disconnectService();

                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 電文送信処理(1052)
     * メンバーズカード課金確認要求
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1052()
    {
        return sendPacket1052Sub( 0, "" );
    }

    /**
     * 電文送信処理(1052)
     * メンバーズカード課金確認要求
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1052(int kind, String value)
    {
        return sendPacket1052Sub( kind, value );
    }

    /**
     * 電文送信処理(1052)
     * メンバーズカード課金確認要求
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1052Sub(int kind, String value)
    {
        this.Result = 0;
        this.NickName = "";

        if ( this.HotelId != null )
        {
            TcpClient tcpClient = new TcpClient();
            boolean blnRet = connect( tcpClient, kind, value );

            if ( blnRet )
            {
                StringFormat format = new StringFormat();

                String strSend = "1052";

                strSend = strSend + format.leftFitFormat( this.TermId, 11 );

                strSend = strSend + format.leftFitFormat( this.Password, 4 );

                strSend = strSend + format.leftFitFormat( "", 10 );

                String strHeader = tcpClient.getPacketHeader( this.HotelId, strSend.length() );

                strSend = strHeader + strSend;
                try
                {
                    tcpClient.send( strSend );

                    String strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        char[] cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        String strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1053" ) == 0 )
                        {
                            strData = new String( cRecv, 36, 11 );
                            this.TermId = strData;

                            strData = new String( cRecv, 47, 4 );
                            this.Password = strData;

                            strData = new String( cRecv, 51, 9 );
                            this.GoodsCode = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 60, 9 );
                            this.GoodsPrice = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    this.log.error( "sendPacket1052:" + e.toString() );
                    return false;
                }
                String strRecv;
                tcpClient.disconnectService();

                return true;
            }

            return false;
        }

        return false;
    }

    /**
     * 電文送信処理(1054)
     * メンバーズカード課金通知要求
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1054()
    {
        return sendPacket1054Sub( 0, "" );
    }

    /**
     * 電文送信処理(1054)
     * メンバーズカード課金通知要求
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1054(int kind, String value)
    {
        return sendPacket1054Sub( kind, value );
    }

    /**
     * 電文送信処理(1054)
     * メンバーズカード課金通知要求
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1054Sub(int kind, String value)
    {
        this.Result = 0;

        if ( this.HotelId != null )
        {
            TcpClient tcpClient = new TcpClient();
            boolean blnRet = connect( tcpClient, kind, value );

            if ( blnRet )
            {
                StringFormat format = new StringFormat();

                String strSend = "1054";

                strSend = strSend + format.leftFitFormat( this.TermId, 11 );

                strSend = strSend + format.leftFitFormat( this.Password, 4 );

                NumberFormat nf = new DecimalFormat( "000000000" );
                strSend = strSend + nf.format( Integer.valueOf( this.GoodsCode ).intValue() );

                nf = new DecimalFormat( "000000000" );
                strSend = strSend + nf.format( Integer.valueOf( this.GoodsPrice ).intValue() );

                strSend = strSend + format.leftFitFormat( this.TouchRoomName, 8 );

                strSend = strSend + format.leftFitFormat( "", 10 );

                String strHeader = tcpClient.getPacketHeader( this.HotelId, strSend.length() );

                strSend = strHeader + strSend;
                try
                {
                    tcpClient.send( strSend );

                    String strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        char[] cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        String strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1055" ) == 0 )
                        {
                            strData = new String( cRecv, 36, 11 );
                            this.TermId = strData;

                            strData = new String( cRecv, 47, 4 );
                            this.Password = strData;

                            strData = new String( cRecv, 51, 2 );
                            int result = Integer.valueOf( strData ).intValue();
                            this.Result = result;
                        }
                    }
                }
                catch ( Exception e )
                {
                    this.log.error( "sendPacket1054:" + e.toString() );
                    return false;
                }
                String strRecv;
                tcpClient.disconnectService();

                return true;
            }

            return false;
        }

        return false;
    }

    /**
     * 電文送信処理(1056)
     * メンバー登録確認要求
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1056()
    {
        return sendPacket1056Sub( 0, "" );
    }

    /**
     * 電文送信処理(1056)
     * メンバー登録確認要求
     * 接続方法指定(1:HotelId指定,2:IPアドレス指定)
     * 
     * @param kind 接続方法(1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean sendPacket1056(int kind, String value)
    {
        return sendPacket1056Sub( kind, value );
    }

    /**
     * 電文送信処理(1056)
     * メンバー登録確認要求
     * 
     * @param kind 接続方法(0:指定なし,1:HotelId指定,2:IPアドレス指定)
     * @param value 指定接続方法
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean sendPacket1056Sub(int kind, String value)
    {
        this.Result = 0;

        if ( this.HotelId != null )
        {
            TcpClient tcpClient = new TcpClient();
            boolean blnRet = connect( tcpClient, kind, value );

            if ( blnRet )
            {
                StringFormat format = new StringFormat();

                String strSend = "1056";

                strSend = strSend + format.leftFitFormat( this.TermId, 11 );

                strSend = strSend + format.leftFitFormat( this.Password, 4 );

                strSend = strSend + format.leftFitFormat( this.TouchRoomName, 8 );

                strSend = strSend + format.leftFitFormat( "", 10 );

                try
                {
                    String strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );

                    strSend = strHeader + strSend;

                    tcpClient.send( strSend );

                    String strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        char[] cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        String strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1057" ) == 0 )
                        {
                            strData = new String( cRecv, 36, 11 );
                            this.TermId = strData;

                            strData = new String( cRecv, 47, 4 );
                            this.Password = strData;

                            strData = new String( cRecv, 51, 2 );
                            int result = Integer.valueOf( strData ).intValue();
                            this.Result = result;
                        }
                    }
                }
                catch ( Exception e )
                {
                    this.log.error( "sendPacket1056:" + e.toString() );
                    return false;
                }
                String strRecv;
                tcpClient.disconnectService();

                return true;
            }

            return false;
        }

        return false;
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
