# hotel_element

## Overview

ホテル別設定ファイル

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)|||
|2|chk_age_flg|int(1)|Yes|0|年齢確認フラグ （1:クッションページあり）|
|3|html_head|text|||HTMLヘッダ情報|
|4|html_login_form|text|||ログインフォーム|
|5|offlineflg|varchar(1)|Yes|'0'|ログイン有無フラグ（0:メンバーページなし 1:メンバーページあり）|
|6|mailmagazineflg|varchar(1)|Yes|'1'|メルマガ有無フラグ（0：無し 1:有）|
|7|mailtoflg|varchar(1)|Yes|'1'|ホテルへ一言有無フラグ（0：無し 1:有）|
|8|mailnameflg|varchar(1)|Yes|'0'|ホテルへ一言ホテル件名有無（0：件名にホテル名無し(default) 1:件名にホテル名有）|
|9|mailname|varchar(64)|Yes||ホテルへ一言ホテル名|
|10|viewflg|varchar(1)|Yes|'0'|0:通常　1:参照バージョン  2:参照バージョン（メンバーも） 9:休止中|
|11|bbsgroupflg|varchar(1)|Yes|'0'|多店舗掲示板の使用(0:通常　1:多店舗バージョン)|
|12|prize_hotelid|varchar(10)|Yes||商品交換用ホテルID（未入力の場合はhotel_idを使用）|
|13|coupon_map_flg|int(1)|Yes|0|0:Yahoo!MAPを使用,1:画像ファイルを使用|
|14|coupon_map_img1|varchar(50)|Yes||クーポン画像1|
|15|coupon_map_img2|varchar(50)|Yes||クーポン画像2|
|16|bbs_temp_flg|int(1)|Yes|0|0:通常掲示板,1:仮投稿掲示板（掲示板追加時にdel_flagに1をセット）|
|17|ranking_hidden_flg|int(1)|Yes|0|0:通常,1:ランキング情報を出力しない|
|18|ownercount|int(2)|Yes|0|オーナーズルーム達成回数|
|19|trial_date|int(8)|Yes|99999999|リニューアル開始日付(YYYYMMDD)　|
|20|start_date|int(8)|Yes|99999999|稼動日付(YYYYMMDD)　|
|21|attention|text|||注意（社内管理用）|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|Yes|Yes||
