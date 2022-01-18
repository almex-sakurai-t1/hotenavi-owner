# equipment

## Overview

ホテルの設備情報及び、設備ごとの部屋を登録する。

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|id|int(11) auto_increment|Yes (PK)||管理番号|
|3|disp_idx|int(2)|Yes|0|表示順番 0:全室完備|
|4|disp_flg|int(1)|Yes|0|表示フラグ 1:表示する|
|5|start_date|date|Yes|'0000-00-00'|表示開始日付|
|6|end_date|date|Yes|'0000-00-00'|表示終了日付|
|7|equipment_name|varchar(128)|Yes||設備名称|
|8|class|varchar(128)|Yes||見出し箇所クラス指定|
|9|class_detail|varchar(128)|Yes||明細箇所クラス指定|
|10|detail|text|Yes||部屋明細（disp_idx:0の場合は全室設備明細）|
|11|memo_all|text|Yes||disp_idx:0の場合、全室設備備考（pc)|
|12|memo_limit|text|Yes||disp_idx:0の場、合限定設備備考(pc)|
|13|memo_all_mobile|text|Yes||disp_idx:0の場合、全室設備備考（携帯)|
|14|memo_limit_mobile|text|Yes||disp_idx:0の場合、限定設備備考(携帯)|
|15|add_date|int(8)|Yes|0|追加日付|
|16|add_time|int(6)|Yes|0|追加時刻|
|17|add_hotelid|varchar(10)|Yes||追加ユーザーログインホテル|
|18|add_userid|int(11)|Yes|0|追加ユーザー|
|19|last_update|int(8)|Yes|0|更新日付|
|20|last_uptime|int(6)|Yes|0|更新時刻|
|21|upd_hotelid|varchar(10)|Yes||更新ユーザーログインホテル|
|22|upd_userid|int(11)|Yes|0|更新ユーザー|
|23|limit_name|varchar(128)|Yes||disp_idx:0の場合、限定設備名称|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,id|Yes|Yes||
