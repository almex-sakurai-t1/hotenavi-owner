# room

## Overview

部屋情報を登録する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|id|int(11) auto_increment|Yes (PK)||管理番号|
|3|room_no|int(4)|Yes|0|部屋番号|
|4|disp_flg|int(1)|Yes|0|表示フラグ|
|5|start_date|date|Yes|'0000-00-00'|表示開始日付|
|6|end_date|date|Yes|'0000-00-00'|表示終了日付|
|7|room_name|varchar(128)|Yes||部屋名称|
|8|rank|int(2)|Yes|0|部屋ランク番号|
|9|equipment|text|Yes||設備情報|
|10|image_pc|text|Yes||PC部屋画像|
|11|image_mobile|text|Yes||携帯画像リンク|
|12|image_thumb|text|Yes||PCサムネイル|
|13|memo|text|Yes||備考|
|14|add_date|int(8)|Yes|0|追加日付|
|15|add_time|int(6)|Yes|0|追加時刻|
|16|add_hotelid|varchar(10)|Yes||追加ユーザーログインホテル|
|17|add_userid|int(11)|Yes|0|追加ユーザー|
|18|last_update|int(8)|Yes|0|更新日付|
|19|last_uptime|int(6)|Yes|0|更新時刻|
|20|upd_hotelid|varchar(10)|Yes||更新ユーザーログインホテル|
|21|upd_userid|int(11)|Yes|0|更新ユーザー|
|22|sync_flag|int(1)|Yes|0||
|23|refer_name|varchar(32)|Yes|||
|24|movie_mobile|text|Yes|||

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,id|Yes|Yes||
