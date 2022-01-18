# goods

## Overview

商品マスタを登録する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID　event_edit_info.hotelid|
|2|category_id|int(11)|Yes (PK)|0|カテゴリID　event_edit_info.disp_idx(data_type=31)|
|3|seq|int(9) auto_increment|Yes (PK)||管理番号|
|4|title|varchar(255)|Yes||表示順番|
|5|disp_idx|int(9)|Yes|0|タイトル|
|6|title_color|varchar(7)|Yes||タイトル色|
|7|title_sub1|varchar(255)|Yes||サブタイトル1|
|8|title_sub1_color|varchar(7)|Yes||サブタイトル1色|
|9|title_sub2|varchar(255)|Yes||サブタイトル2|
|10|title_sub2_color|varchar(7)|Yes||サブタイトル2色|
|11|msg|text|||商品説明|
|12|point|int(8)|Yes|0|使用ポイント|
|13|count_flag|int(1)|Yes|0|数量入力方法　0:無し|
|14|count_max|int(5)|Yes|10|最大数量|
|15|count_unit|int(5)|Yes|1|数量単位|
|16|picture_pc|varchar(255)|Yes||画像PC|
|17|picture_gif|varchar(255)|Yes||画像GIF|
|18|picture_png|varchar(255)|Yes||画像PNG|
|19|disp_from|int(8)|Yes|0|表示開始日付|
|20|disp_to|int(8)|Yes|0|表示終了日付|
|21|limit_from|int(8)|Yes|0|有効期限開始日|
|22|limit_to|int(8)|Yes|0|有効期限終了日|
|23|deliverly_possible|int(3)|Yes|0|受け渡し可能時期　確認後○日以降,0:当日|
|24|memo|text|||備考|
|25|member_only|int(1)|Yes|0|メンバーフラグ|
|26|elect_number|int(5)|Yes|0|提供数|
|27|delivery_flag|int(1)|Yes|0|配送有無|
|28|add_date|int(8)|Yes|0|登録日付|
|29|add_time|int(6)|Yes|0|登録時刻|
|30|add_hotelid|varchar(10)|Yes||登録ホテルID|
|31|add_userid|int(11)|Yes|0|登録ユーザーID|
|32|last_update|int(8)|Yes|0|最終変更日付|
|33|last_uptime|int(6)|Yes|0|最終変更時刻|
|34|upd_hotelid|varchar(10)|Yes||最終変更ホテルID|
|35|upd_userid|int(11)|Yes|0|最終変更ユーザーID|
|36|suggest_flag|int(1)|Yes|0|おすすめフラグ　1:おすすめ商品|
|37|suggest_idx|int(9)|Yes|0|おすすめ表示順番|
|38|gift_number_flag|int(1)|Yes|0|ギフト番号フラグ|
|39|msg_user|text|||ギフト番号時メッセージ|
|40|msg_mail|text|||ギフト番号時メール|
|41|supplier_id|int(11)|Yes|0|仕入れ先　1:ユナイテッドスペース、2:DMM、3:グッディポイント|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,category_id,seq|Yes|Yes||
