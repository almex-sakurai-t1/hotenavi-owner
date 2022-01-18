# owner_user_token

## Overview

オーナーサイトユーザ更新を判断するための一時的データ

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|token|varchar(255)|Yes (PK)||Token|
|2|operate_hotelid|varchar(10)|Yes||操作契約先ID(ログインをしているowner_user.hotelid)|
|3|operate_loginid|varchar(32)|Yes||操作ログインID(ログインをしているowner_user.loginid)|
|4|operate_userid|int(11)|Yes|0|操作ユーザID(ログインをしているowner_user.userid)|
|5|operate_staff_id|varchar(32)|Yes||操作社員コード(ログインをしているresearch_staff.staff_id)|
|6|update_hotelid|varchar(10)|Yes||更新契約先ID(更新対象のowner_user.hotelid)|
|7|update_userid|int(11)|Yes|0|更新ユーザID(更新対象のowner_user.userid)|
|8|limit_date|int(8)|Yes|0|有効期限日 (YYYYMMDD)|
|9|limit_time|int(8)|Yes|0|有効期限時刻 (HHMMSS)|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|token|Yes|Yes||
