# hotel

## Overview

ホテナビ契約ホテルに関するデータを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|name|varchar(64)|Yes||ホテル名称|
|3|password|varchar(16)|Yes||ホテルパスワード|
|4|mail|varchar(64)|Yes||メールアドレス|
|5|url|varchar(64)|Yes||ホテルTOPURL|
|6|ftp_server|varchar(64)|Yes||FTPサーバ名|
|7|ftp_passwd|varchar(16)|Yes||FTPパスワード|
|8|sales_dir|varchar(255)|Yes||売上帳票アップロード先|
|9|access_dir|varchar(255)|Yes||アクセスレポートアップロード先|
|10|center_id|varchar(10)|Yes||他店舗ホテルID|
|11|front_ip|varchar(64)|Yes||フロントPCIPアドレス|
|12|report_ip|varchar(64)|Yes||レポート作成PCIPアドレス|
|13|object_no|varchar(16)|Yes||物件番号|
|14|owner_user|varchar(32)|Yes||オーナーサイトログインユーザ名|
|15|owner_password|varchar(32)|Yes||オーナーサイトログインパスワード|
|16|plan|int(2)|Yes|0|契約プラン（1:カスタム,2:オーナー,3:エクストラ）|
|17|memo|varchar(255)|Yes||メモ|
|18|report_user|varchar(32)|Yes||レポート作成PCログインユーザ名|
|19|report_password|varchar(32)|Yes||レポート作成PCログインパスワード|
|20|report_center|int(2)|Yes|0|他店舗センター区分|
|21|host_kind|int(2)|Yes|0|ホスト種別|
|22|group_id|varchar(10)|Yes||グループホテルID|
|23|group_mag|varchar(10)|Yes||メールマガジングループホテルID|
|24|host_detail|int(1)|Yes|0|ホスト詳細区分(1:新シリ売上対応)|
|25|tenpoid|varchar(4)|Yes||店舗ID|
|26|group_flag|int(1)|Yes|0|グループフラグ(0:単店舗,1:多店舗)|
|27|midlength|int(1)|Yes|9|メンバーID桁数|
|28|entry_method|int(1)|Yes|0||
|29|report_times|int(4)|Yes|0|帳票送信可能開始時間(共通設定)(HHMM) 0:指定なし|
|30|report_timee|int(4)|Yes|0|帳票送信可能終了時間(共通設定)(HHMM) 0:指定なし|
|31|close_day|int(2)|Yes|0|締日(99:末日)|
|32|sort_num|int(4)|Yes|0|グループ店舗ソートキー|
|33|split_flag|int(1)|Yes|0|1:売上分割専用|
|34|sales_layout|int(1)|Yes|0|0:旧,1:新,2,旧固定,3:新固定|
|35|mode_color|text|Yes||料金モード色指定|
|36|mail_reserve|varchar(64)|Yes||予約用送信元メールアドレス（設定なしの場合はmailから送信)|
|37|mail_reserve_info|varchar(64)|Yes||予約時オーナー宛確認メールアドレス|
|38|mail_bbs|text|Yes||掲示板投稿時メールアドレス（設定なしの場合はmailから送信）|
|39|appearance|varchar(256)|Yes||外観用URL|
|40|timechart_flag|int(1)|Yes|0|タイムチャート有り|
|41|pointtotal_flag|int(1)|Yes|0|売上管理提携ポイントあり|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|Yes|Yes||
