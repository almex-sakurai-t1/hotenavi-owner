# owner_user_log

## Overview

オーナーサイトへのログインするユーザのログイン履歴を管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|logid|int(11) auto_increment|Yes (PK)||ログID|
|3|login_date|date|Yes|'0000-00-00'|ログイン日付|
|4|login_time|time|Yes|'00:00:00'|ログイン時刻|
|5|userid|int(11)|Yes|0|ユーザID(成功時のみ|
|6|login_name|varchar(32)|Yes||ログイン名|
|7|password|varchar(16)|Yes||パスワード|
|8|log_level|int(2)|Yes|0|ログレベル|
|9|log_detail|varchar(255)|Yes||ログ詳細|
|10|user_agent|varchar(255)|Yes||ユーザエージェント|
|11|remote_ip|varchar(64)|Yes||リモートIPアドレス|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,logid|Yes|Yes||
|2|hotelid,userid||||
|3|log_level||||
|4|hotelid,userid,log_level||||
|5|hotelid,userid,logid||||
