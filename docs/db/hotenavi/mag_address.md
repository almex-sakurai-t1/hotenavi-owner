# mag_address

## Overview

ホテルごとのメールマガジンに関するデータを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|address|varchar(128)|Yes (PK)||メールアドレス|
|3|custom_id|varchar(9)|Yes||顧客番号|
|4|user_id|varchar(32)|Yes||ユーザID（店舗側）|
|5|password|varchar(8)|Yes||パスワード|
|6|birthday1|int(8)|Yes|0|誕生日１|
|7|birthday2|int(8)|Yes|0|誕生日２|
|8|memorial1|int(8)|Yes|0|記念日１|
|9|memorial2|int(8)|Yes|0|記念日２|
|10|last_date|int(8)|Yes|0|最終来店日|
|11|use_count|int(9)|Yes|0|利用回数|
|12|use_total|int(9)|Yes|0|利用金額|
|13|point|int(9)|Yes|0|ポイント|
|14|point2|int(9)|Yes|0|ポイント２|
|15|rank|int(4)|Yes|0|メンバーランク|
|16|state|int(2)|Yes|0|メール配信状態(1:配信OK,2:配信NG)|
|17|unknown_flag|int(2)|Yes|0|配信不能フラグ(1:配信不能)|
|18|mail_md5|varchar(255)|Yes||メールアドレスMD5|
|19|last_update|int(8)|Yes|0|最終更新日(YYYYMMDD)|
|20|last_uptime|int(6)|Yes|0|最終更新日(HHMMSS)|
|21|last_senddate|int(8)|Yes|0|最終送信日(YYYYMMDD)|
|22|last_sendtime|int(6)|Yes|0|最終送信日(HHMM)|
|23|sync_flag|int(1)|Yes|0|ホテル同期フラグ(1:同期待)|
|24|update_flag|int(1)|Yes|0|1:変更対象メルアド|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,address|Yes|Yes||
|2|address||||
|3|custom_id||||
