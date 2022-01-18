# reserve_log

## Overview

ホテナビ契約ホテルのホテナビ直前予約の履歴を登録する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|seq|int(11) auto_increment|Yes (PK)||連番(AUTO_INCREMENT)|
|3|custom_id|varchar(9)|Yes||顧客番号|
|4|user_id|varchar(32)|Yes||ユーザID|
|5|room_code|int(4)|Yes|0|ルームコード|
|6|room_name|varchar(128)|Yes||部屋名称|
|7|reserve_no|int(8)|Yes|0|予約番号|
|8|input_date|int(8)|Yes|0|入力日付|
|9|input_time|int(6)|Yes|0|入力時刻|
|10|limit_time|int(4)|Yes|0|予約有効時刻|
|11|mail_address|varchar(255)|Yes||メールアドレス|
|12|career|varchar(2)|Yes||入力媒体 (pc, i　,ez , j)|
|13|user_agent|varchar(255)|Yes||ユーザエージェント|
|14|remote_ip|varchar(64)|Yes||リモートIPアドレス|
|15|cancel_flag|int(1)|Yes|0|1:キャンセル|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,seq|Yes|Yes||
