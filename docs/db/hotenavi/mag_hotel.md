# mag_hotel

## Overview

ホテルごとにメールマガジンに関するホテル管理データを管理する。

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|address|varchar(128)|Yes||ホテルメールアドレス|
|3|mag_address|varchar(128)|Yes||ホテルメールアドレス（メールマガジン用）|
|4|username|varchar(32)|Yes||ユーザ名|
|5|password|varchar(32)|Yes||パスワード|
|6|message|varchar(255)|Yes||メールマガジン用メッセージ|
|7|last_update|int(8)|Yes|0|最終更新日付|
|8|last_uptime|int(8)|Yes|0|最終更新時刻|
|9|update_flag|int(1)|Yes|0|更新フラグ(0:更新なし,1:自動更新)|
|10|update_week|int(1)|Yes|0|更新曜日(0:日曜～6:土曜)|
|11|update_time|int(4)|Yes|0|更新時刻(HHMM)|
|12|update_force|int(1)|Yes|0|強制更新フラグ(1:強制更新)|
|13|mailboss|int(1)|Yes|0|MailBOSS使用フラグ(1:MailBOSS)|
|14|unknown_delivery|int(1)|Yes|0|Unknown配信フラグ(1:配信する)|
|15|key_word|varchar(255)|Yes||キーワード|
"|16|member_only|int(1)|Yes|0|一の位：0:通常、1:メンバーのみ、9:一時停止
十の位：0:QRメンバー受付，9:受付停止
百の位：0：PC版QRコード表示,9：表示無
千の位：0:PC版メンバー入力通常,9:なし|"
|17|change_flag|int(1)|Yes|0|1:メルマガ変更リンクあり|
|18|delete_flag|int(1)|Yes|0|1:メルマガ削除リンクあり|
|19|add_flag|int(1)|Yes|0|0:登録済会員への追加あり|
|20|group_add_flag|int(1)|Yes|0|0:単店舗のみ,1:多店舗一括,2:店舗選択式|
|21|group_cancel_flag|int(1)|Yes|0|0:単店舗のみ,1:多店舗一括,2:店舗選択式|
|22|report_mail_flag|int(1)|Yes|0|1:メンバーが登録時にオーナー宛メール|
|23|now_sync|int(8)|Yes|0|現在更新中フラグ（日付セット）|
"|24|address_mailto|varchar(128)|Yes||ホテルへ一言専用メールアドレス
（入力なしの場合は、address がホテルへ一言に利用される|"
|25|target_hotelid|varchar(10)|Yes||接続先ホテルID（ここに設定してある場合には、このホテルIDをcustominfo.HotelIdにセットする）|
|26|reg_address|varchar(128)|Yes||ホテルメールアドレス（QRメンバー登録用）|
|27|reg_username|varchar(32)|Yes||ユーザ名（QRメンバー登録用）|
|28|reg_password|varchar(32)|Yes||パスワード（QRメンバー登録用）|
|29|reg_message|varchar(255)|Yes||返信用メッセージ（QRメンバー登録用）|
|30|qr_identifier|varchar(5)|Yes||店舗識別コード（QRメンバー登録用）|
|31|input_send_flag|int(1)|Yes|0|1:メールアドレス入力で登録メールを送る|
|32|input_send_message|varchar(255)|Yes||メアド入力方式の場合の登録画面メッセージ|
|33|request_message|varchar(255)|Yes||メルマガ配信画面のメッセージ|
|34|address_say|varchar(128)|Yes|||
|35|subject_hotelname|varchar(32)|Yes|||
|36|update_delivery_flag|int(1)|Yes|0|1:店舗側のメルマガ配信フラグを反映させない|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|Yes|Yes||
