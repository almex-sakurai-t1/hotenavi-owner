# owner_user_login

## Overview

オーナーサイトへのログイン履歴を管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|userid|int(11)|Yes (PK)|0|ユーザID|
|3|kind|int(1)|Yes (PK)|0|ログイン種別（１：ホテナビオーナー、２：ハピホテオーナー）|
|4|login_date|date|Yes|'0000-00-00'|最新ログイン日付|
|5|login_time|time|Yes|'00:00:00'|最新ログイン時刻|
|6|user_agent|varchar(255)|Yes||最新ユーザエージェント|
|7|remote_ip|varchar(64)|Yes||最新リモートIPアドレス|
|8|last_login_date|date|Yes|'0000-00-00'|前回ログイン日付|
|9|last_login_time|time|Yes|'00:00:00'|前回ログイン時刻|
|10|last_user_agent|varchar(255)|Yes||前回ユーザエージェント|
|11|last_remote_ip|varchar(64)|Yes||前回リモートIPアドレス|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,userid,kind|Yes|Yes||
