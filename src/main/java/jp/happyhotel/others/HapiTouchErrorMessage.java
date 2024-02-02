/*
 * @(#)HapiTouchErrorMessage.java
 * 1.00 2011/10/17 Copyright (C) ALMEX Inc. 2007
 * ハピタッチエラーメッセージクラス
 */
package jp.happyhotel.others;

/**
 * ハピタッチエラーメッセージ
 * 
 * @author S.Tashiro
 * @version 1.00 2011/10/17
 */
public class HapiTouchErrorMessage
{
    public static final int ERR_100   = 100;  // 認証エラー

    // HotelInfo
    public static final int ERR_10101 = 10101; // ライセンスキーホテル取得エラー

    // GetConfig
    public static final int ERR_10201 = 10201; // ホテル部屋情報取得エラー
    public static final int ERR_10202 = 10202; // ハピー倍率取得エラー

    // Visit
    public static final int ERR_10301 = 10301; // チェックインデータ作成エラー
    public static final int ERR_10302 = 10302; // チェックインデータ取得エラー
    public static final int ERR_10303 = 10303; // ホスト連動物件なのに、タッチサーバ経由でない通信

    // Find
    public static final int ERR_10401 = 10400;

    // ModyfiCi
    public static final int ERR_10501 = 10501; // チェックインデータ取得エラー
    public static final int ERR_10502 = 10502; // チェックインデータ更新エラー
    public static final int ERR_10503 = 10503; // チェックインデータ追加エラー
    public static final int ERR_10504 = 10504; // 来店マイルエラー
    public static final int ERR_10505 = 10505; // マイル使用エラー
    public static final int ERR_10506 = 10506; // マイル付与エラー
    public static final int ERR_10507 = 10507; // バックオフィス来店マイル追加エラー
    public static final int ERR_10508 = 10508; // バックオフィスマイル使用更新エラー
    public static final int ERR_10509 = 10509; // バックオフィスマイル使用追加エラー
    public static final int ERR_10510 = 10510; // バックオフィスマイル付与更新エラー
    public static final int ERR_10511 = 10511; // バックオフィスマイル付与追加エラー
    public static final int ERR_10512 = 10512; // 予約チェックイン　マイル使用エラー
    public static final int ERR_10513 = 10513; // 規約未同意ユーザのタッチ
    public static final int ERR_10514 = 10514; // ホスト連動物件なのに、タッチサーバ経由でない通信
    public static final int ERR_10515 = 10515; // modifyCiで在庫が開放されなかった
    public static final int ERR_10516 = 10516; // 連動物件でタッチPCからマイル使用の入力があった

    // CorrectCi
    public static final int ERR_10601 = 10601; // チェックインデータ取得エラー
    public static final int ERR_10602 = 10602; // チェックインデータ更新エラー
    public static final int ERR_10603 = 10603; // チェックインデータ追加エラー
    public static final int ERR_10604 = 10604; // 来店マイルエラー
    public static final int ERR_10605 = 10605; // マイル付与エラー
    public static final int ERR_10606 = 10606; // バックオフィス来店マイル追加エラー
    public static final int ERR_10607 = 10607; // バックオフィスマイル付与更新エラー
    public static final int ERR_10608 = 10608; // バックオフィスマイル付与追加エラー
    public static final int ERR_10615 = 10615; // correctCiで在庫が開放されなかった
    public static final int ERR_10616 = 10616; // 連動物件でタッチPCからマイル使用の入力があった

    // SyncCi
    public static final int ERR_10700 = 10700;

    // VisitCancel
    public static final int ERR_10801 = 10801; // チェックインデータ取得エラー
    public static final int ERR_10802 = 10802; // チェックインデータ更新エラー
    public static final int ERR_10803 = 10803; // マイル使用エラー
    public static final int ERR_10804 = 10804; // マイル使用履歴なし
    public static final int ERR_10805 = 10805; // バックオフィスマイル使用取消エラー
    public static final int ERR_10806 = 10806; // チェックインデータステータスエラー

    // PointCancel
    public static final int ERR_10901 = 10901; // チェックインデータ取得エラー
    public static final int ERR_10902 = 10902; // チェックインデータ更新エラー
    public static final int ERR_10903 = 10903; // マイル使用エラー
    public static final int ERR_10904 = 10904; // マイル使用履歴なし
    public static final int ERR_10905 = 10905; // バックオフィスマイル使用取消エラー
    public static final int ERR_10906 = 10906; // チェックインデータステータスエラー
    public static final int ERR_10907 = 10907; // 連動物件でタッチPCからマイル使用の取消があった

    // RsvData
    public static final int ERR_20100 = 20100;

    // RsvDataDetail
    public static final int ERR_20201 = 20201; // パラメータエラー
    public static final int ERR_20202 = 20202; // 予約データなし

    // GetRsvRoom
    public static final int ERR_20301 = 20301; // パラメータエラー
    public static final int ERR_20302 = 20302; // 予約データなし

    // RsvFix
    public static final int ERR_20400 = 20400; // 予約データ不一致エラー
    public static final int ERR_20401 = 20401; // パラメータエラー
    public static final int ERR_20402 = 20402; // 予約データなし
    public static final int ERR_20403 = 20403; // 来店確定失敗
    public static final int ERR_20404 = 20404; // 予約マイル追加失敗
    public static final int ERR_20405 = 20405; // ポイント履歴更新更新失敗エラー
    public static final int ERR_20406 = 20406; // バックオフィス追加エラー
    public static final int ERR_20407 = 20407; // ホスト連動物件なのに、タッチサーバ経由でない通信
    public static final int ERR_20408 = 20408; // 同じ予約番号ですでにチェックイン処理済

    // RsvCancel
    public static final int ERR_20501 = 20501;
    public static final int ERR_20502 = 20502; // 予約データなし
    public static final int ERR_20503 = 20503; // 来店キャンセル失敗
    public static final int ERR_20504 = 20504; // 予約マイル追加失敗
    public static final int ERR_20505 = 20505; // ポイント履歴更新更新失敗エラー
    public static final int ERR_20506 = 20506; // バックオフィス追加エラー
    public static final int ERR_20507 = 20507; // OTAからの予約

    // RsvModifyArrivalTime
    public static final int ERR_20601 = 20601; // 予約番号なし
    public static final int ERR_20602 = 20602; // 予約データなし
    public static final int ERR_20603 = 20603; // 予約データ更新失敗
    public static final int ERR_20604 = 20604; // 到着時刻変更不可（ステータス）
    public static final int ERR_20605 = 20605; // チェックイン時間範囲外

    // RsvUndoCancel
    public static final int ERR_20701 = 20701; // パラメーターエラー
    public static final int ERR_20702 = 20702; // 予約データなし
    public static final int ERR_20703 = 20703; // ステータス異常
    public static final int ERR_20704 = 20704; // キャンセル失敗

    //
    // RsvRoomChange
    public static final int ERR_20801 = 20801; // パラメーターエラー
    public static final int ERR_20802 = 20802; // 予約データなし
    public static final int ERR_20803 = 20803; // ステータス異常
    public static final int ERR_20804 = 20804; // 部屋の状態異常
    public static final int ERR_20805 = 20805; // 予約データ異常
    public static final int ERR_20806 = 20806; // 予約データ更新失敗
    public static final int ERR_20807 = 20807; // 変更先部屋割り当て済み

    // RsvUndoFix
    public static final int ERR_20901 = 20901; // パラメーターエラー
    public static final int ERR_20902 = 20902; // 予約データなし
    public static final int ERR_20903 = 20903; // ステータス異常
    public static final int ERR_20904 = 20904; // 部屋の状態異常
    public static final int ERR_20905 = 20905; // 予約データ異常
    public static final int ERR_20906 = 20906; // チェックインデータ取得エラー
    public static final int ERR_20907 = 20907; // チェックインデータ更新エラー
    public static final int ERR_20908 = 20908; // マイル使用エラー
    public static final int ERR_20909 = 20909; // マイル使用履歴なし
    public static final int ERR_20910 = 20910; // バックオフィスマイル使用取消エラー
    public static final int ERR_20911 = 20911; // チェックインデータステータスエラー
    public static final int ERR_20912 = 20912; // バックオフィスデータ取消エラー
    public static final int ERR_20913 = 20913; // 予約マイル取消エラー

    // HtCheckIn
    public static final int ERR_30101 = 30101; // チェックインデータ作成エラー
    public static final int ERR_30102 = 30102; // 非会員でのタッチ
    public static final int ERR_30103 = 30103; // RTCタイマー不正
    public static final int ERR_30104 = 30104; // マイル使用済みユーザがすでに割当済みなのでエラー
    public static final int ERR_30105 = 30105; // ホテル顧客メンバー受付済みエラー
    public static final int ERR_30106 = 30106; // 同一ユーザ部屋違いエラー
    public static final int ERR_30107 = 30107; // フロントタッチ受付不可エラー
    public static final int ERR_30108 = 30108; // すでにチェックアウトされている場合
    public static final int ERR_30109 = 30109; // すでに別ユーザーがチェックインしている場合
    public static final int ERR_30110 = 30110; // 端末IDが取得できない場合
    public static final int ERR_30111 = 30111; // 端末が有効ではない場合
    public static final int ERR_30112 = 30112; // チェックインが取り消しされている場合
    public static final int ERR_30113 = 30113; // チェックインが無効になっている場合
    public static final int ERR_30114 = 30114; // ホテルマスタを取得できない場合
    public static final int ERR_30115 = 30115; // 部屋マスタを取得できない場合
    public static final int ERR_30116 = 30116; // ホテル設定ファイルを取得できない場合
    public static final int ERR_30117 = 30117; // ホテルコンピュータに接続できない場合
    public static final int ERR_30118 = 30118; // ホテルコンピュータに接続できない。（タイムアウト）
    public static final int ERR_30119 = 30119; // ホテルコンピュータに接続できない。（通信エラー）
    public static final int ERR_30120 = 30120; // 予約チェックイン済み
    public static final int ERR_30121 = 30121; // 予約確保部屋と違う部屋に入ってチェックインしようとした
    public static final int ERR_30122 = 30122; // マイル加盟店ではない

    // HtMileUse
    public static final int ERR_30201 = 30201; // ハピホテマイル不足
    public static final int ERR_30202 = 30202; //
    public static final int ERR_30203 = 30203; //
    public static final int ERR_30204 = 30204; // ホテルコンピュータに接続できない。（タイムアウト）
    public static final int ERR_30205 = 30205; // ホテルコンピュータに接続できない。（通信エラー）

    // HtRsvFix
    public static final int ERR_30301 = 30301; // 予約データなし
    public static final int ERR_30302 = 30302; // ホスト予約来店通知失敗
    public static final int ERR_30303 = 30303; // 来店確定失敗
    public static final int ERR_30304 = 30304; // ホテルコンピュータに接続できない。（タイムアウト）
    public static final int ERR_30305 = 30305; // ホテルコンピュータに接続できない。（通信エラー）

    // 顧客メンバーチェックイン関係
    public static final int ERR_30401 = 30401; // 顧客メンバーチェックイン失敗
    public static final int ERR_30402 = 30402; // 顧客メンバー自動採番エラー
    public static final int ERR_30403 = 30403; // すでにメンバー登録済み
    public static final int ERR_30404 = 30404; // 顧客データ登録エラー
    public static final int ERR_30405 = 30405; // 接続エラー
    public static final int ERR_30406 = 30406; // その他エラー
    public static final int ERR_30407 = 30407; // メンバーカード発行有無取得エラー
    public static final int ERR_30408 = 30408; // メンバーカード課金有無取得エラー
    public static final int ERR_30409 = 30409; // セキュリティコードエラー

    // HtFrontTouch
    public static final int ERR_30501 = 30501; // 更新エラー
    public static final int ERR_30502 = 30502; // 取得エラー

    // HtOverWriteCustom
    public static final int ERR_30601 = 30601; // ホテナビ顧客更新エラー

    // NonAutoCheckIn
    public static final int ERR_30701 = 30701; // ホテルコンピュータに接続できない。（タイムアウト）
    public static final int ERR_30702 = 30702; // ホテルコンピュータに接続できない。（通信エラー）

    // カードレスメンバー取得
    public static final int ERR_30801 = 30801; // 顧客コード不正
    public static final int ERR_30802 = 30802; // 顧客コードパラメータなし

    // カードレスメンバー一覧取得
    public static final int ERR_30811 = 30811; // 取得エラー
    public static final int ERR_30812 = 30812; // 取得時異常
    public static final int ERR_30813 = 30813; // SocketTimeoutException
    public static final int ERR_30814 = 30814; // その他の送信エラー
    public static final int ERR_30815 = 30815; // その他Exceptionによるエラー
    public static final int ERR_30816 = 30816; // その他未送信エラー
    public static final int ERR_30817 = 30817; // 検索最大値を超えた
    public static final int ERR_30818 = 30818; // 接続エラー
    public static final int ERR_30819 = 30819; // 一部送信エラーあり

    // カードレスメンバー解除
    public static final int ERR_30821 = 30821; // 顧客コード不正
    public static final int ERR_30822 = 30822; // 顧客コードパラメータなし
    public static final int ERR_30823 = 30823; // 該当顧客なし もしくは削除済み
    public static final int ERR_30824 = 30824; // 削除エラー

    // RsvList
    public static final int ERR_40001 = 40001; // ホテルコンピュータに接続できない。（タイムアウト）
    public static final int ERR_40002 = 40002; // ホテルコンピュータに接続できない。（通信エラー）

}
