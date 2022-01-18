# goods_category

## Overview

商品カテゴリを登録する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|category_id|int(10)|Yes (PK)|0|カテゴリID|
|3|seq|int(9) auto_increment|Yes (PK)||管理番号|
|4|disp_idx|int(9)|Yes|0|カテゴリ表示順番|
|5|title|varchar(255)|Yes||タイトル|
|6|title_color|varchar(7)|Yes||タイトル色|
|7|msg|text|Yes||説明|
|8|disp_from|int(8)|Yes|0|表示開始日付|
|9|disp_to|int(8)|Yes|0|表示終了日付|
|10|member_only|int(1)|Yes|0|メンバーフラグ  0:両方，1:メンバーのみ|
|11|add_date|int(8)|Yes|0|登録日付|
|12|add_time|int(6)|Yes|0|登録時刻|
|13|add_hotelid|varchar(10)|Yes||登録ホテルID|
|14|add_userid|int(11)|Yes|0|登録ユーザーID|
|15|last_update|int(8)|Yes|0|最終変更日付|
|16|last_uptime|int(6)|Yes|0|最終変更時刻|
|17|upd_hotelid|varchar(10)|Yes||最終変更ホテルID|
|18|upd_userid|int(11)|Yes|0|最終変更ホテルID|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,category_id,seq|Yes|Yes||
