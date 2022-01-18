# roomrank

## Overview

部屋ランクごとの料金情報を管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|rank|int(2)|Yes (PK)|0|部屋ランクNO|
|3|id|int(11) auto_increment|Yes (PK)||管理番号|
|4|disp_flg|int(1)|Yes|0|表示フラグ|
|5|start_date|date|Yes|'0000-00-00'|表示開始日付|
|6|end_date|date|Yes|'0000-00-00'|表示終了日付|
|7|rank_name|varchar(128)|Yes||部屋ランク名称|
|8|class|varchar(128)|||部屋詳細クラス指定|
|9|system|text|Yes||料金システム|
|10|memo|text|Yes||備考|
|11|system_mobile|text|Yes||携帯部屋詳細用料金|
|12|add_date|int(8)|Yes|0|追加日付|
|13|add_time|int(6)|Yes|0|追加時刻|
|14|add_hotelid|varchar(10)|Yes||追加ユーザーログインホテル|
|15|add_userid|int(11)||0|追加ユーザー|
|16|last_update|int(8)||0|更新日付|
|17|last_uptime|int(6)||0|更新時刻|
|18|upd_hotelid|varchar(10)|||更新ユーザーログインホテル|
|19|upd_userid|int(11)||0|更新ユーザー|
|20|sync_flag|int(1)||0|更新対象フラグ|
|21|title_class|varchar(128)|||料金表タイトルクラス指定|
|22|price_notation|varchar(16)|Yes|||
|23|disp_index|int(2)|Yes|0|表示順（新ホテナビのみ適用）|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,rank,id|Yes|Yes||
