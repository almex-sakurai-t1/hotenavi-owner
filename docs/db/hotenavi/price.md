# price

## Overview

ホテナビの料金システム情報を登録する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|id|int(11) auto_increment|Yes (PK)||管理番号|
|3|disp_flg|int(1)|Yes|0|表示フラグ|
|4|start_date|date|Yes|'0000-00-00'|表示開始日付|
|5|end_date|date|Yes|'0000-00-00'|表示終了日付|
|6|price_name|varchar(128)|Yes||料金システム名称|
|7|system|text|Yes||料金システム|
|8|memo|text|Yes||備考|
|9|layout|text|Yes||料金情報レイアウト|
|10|add_date|int(8)|Yes|0|追加日付|
|11|add_time|int(6)|Yes|0|追加時刻|
|12|add_hotelid|varchar(10)|Yes||追加ユーザーログインホテル|
|13|add_userid|int(11)|Yes|0|追加ユーザー|
|14|last_update|int(8)|Yes|0|更新日付|
|15|last_uptime|int(6)|Yes|0|更新時刻|
|16|upd_hotelid|varchar(10)|Yes||更新ユーザーログインホテル|
|17|upd_userid|int(11)|Yes|0|更新ユーザー|
|18|roomrank_name|varchar(128)|Yes|'0'|ランク別一覧表名称|
|19|price_memo|text|Yes||サービス詳細下備考|
|20|detail_explain|text|Yes||「客室詳細」見出し・詳細|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,id|Yes|Yes||
