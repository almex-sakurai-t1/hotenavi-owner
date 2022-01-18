# owner_user_lock

## Overview

オーナーサイトログインロックを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)|'0'|契約先ID|
|2|userid|int(11)|Yes (PK)|0|ユーザID|
|3|lock_status|int(1)|Yes|0|1:ロック状態 データを読み込んでロック期限を過ぎていれば0にする|
|4|lock_date|int(8)|Yes|0|yyyymmdd ロック日時の期限　データを読み込んで期限を過ぎていれば0にする|
|5|lock_time|int(6)|Yes|0|hhmmss ロック日時の期限　データを読み込んで期限を過ぎていれば0にする|
|6|mistake_count|int(1)|Yes|0|累計のログイン失敗回数 （参考値であって特に他で使わない）|
|7|mistake_date|int(8)|Yes|0|yyyymmdd 最終ログイン失敗日付|
|8|mistake_time|int(6)|Yes|0|hhmmss 最終ログイン失敗時刻|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,userid|Yes|Yes||
