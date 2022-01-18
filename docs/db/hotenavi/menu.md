# menu

## Overview

旧ホテナビ用ホテルごとのホテナビホームページメニューを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|data_type|int(2)|Yes (PK)|0|表示タイプ 1:PCビジターメニュー 2:PCメンバーメニュー 3:携帯ビジターメニュー 4:携帯メンバーメニュー 11:PCグループTOPリンク 12:PCオフィシャルリンク 13:携帯グループTOPリンク 14:携帯オフィシャルリンク|
|3|id|int(11)|Yes (PK)|0|管理番号|
|4|disp_idx|int(2)|Yes|0|表示順序|
|5|disp_flg|int(1)|Yes|0|表示フラグ（１：表示する）|
|6|start_date|date|Yes|'0000-00-00'|表示開始日付|
|7|end_date|date|Yes|'0000-00-00'|表示終了日付|
|8|title|varchar(256)|Yes||表示タイトル|
|9|contents|varchar(128)|Yes||コンテンツ|
|10|add_date|int(8)|Yes|0|登録日付|
|11|add_time|int(6)|Yes|0|登録時刻|
|12|add_hotelid|varchar(10)|Yes||登録ホテルID|
|13|add_userid|int(11)|Yes|0|登録ユーザID|
|14|last_update|int(8)|Yes|0|最終更新日|
|15|last_uptime|int(6)|Yes|0|最終更新時刻|
|16|upd_hotelid|varchar(10)|Yes||更新ホテルID|
|17|upd_userid|int(11)|Yes|0|更新ユーザID|
|18|title_color|varchar(10)|Yes||タイトルの色|
|19|decoration|varchar(128)|Yes||装飾|
|20|hpedit_id|int(3)|Yes|0|オーナーサイトのHP編集対応メニューID 1:TOPページ 2:Members Only 3:WhatsNew 4:イベント情報 5:サービス情報 6:アクセス情報 7:掲示板 8:ホテルに一言 9:FAQ 10:求人情報 11:メール予約 12:クーポン|
|21|msg|text|Yes||フリーメッセージ|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,data_type,id|Yes|Yes||
