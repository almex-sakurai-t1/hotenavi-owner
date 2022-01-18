# mag_address_history

## Overview

ホテルごとメールマガジン登録・解除の履歴を管理する。

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|address|varchar(128)|Yes (PK)||メールアドレス|
|3|seq|int(8) auto_increment|Yes (PK)||連番|
|4|state|int(2)|Yes|0|メール配信状態(1:配信OK,2:配信NG)|
|5|update|int(8)|Yes|0|更新日(YYYYMMDD)|
|6|uptime|int(6)|Yes|0|更新時刻(HHMMSS)|
|7|user_agent|varchar(255)|Yes||登録／解除ユーザエージェント|
|8|remote_ip|varchar(64)|Yes||登録／解除IPアドレス|
|9|custom_id|varchar(9)|Yes||顧客番号|
|10|user_id|varchar(32)|Yes||ユーザーID(顧客側)|
|11|password|varchar(8)|Yes||パスワード|
|12|birthday1|int(8)|Yes|0|誕生日1|
|13|birthday2|int(8)|Yes|0|誕生日2|
|14|unknown_flag|int(2)|Yes|0|配信不能フラグ(1:配信不能)|
|15|mail_md5|varchar(255)|Yes||メールアドレスMD5|
|16|url|varchar(255)|Yes||変更URL|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,address,seq|Yes|Yes||
