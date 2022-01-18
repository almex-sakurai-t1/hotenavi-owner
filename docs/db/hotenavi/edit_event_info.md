# edit_event_info

## Overview

ホテルごとのイベント情報などホームページ掲載情報を管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|data_type|int(2)|Yes (PK)|0|表示タイプ|
|3|id|int(11) auto_increment|Yes (PK)||管理番号|
|4|disp_idx|int(2)|Yes|0|表示順序|
|5|disp_flg|int(1)|Yes|0|表示フラグ（１：表示する）|
|6|start_date|date|Yes|'0000-00-00'|表示開始日付|
|7|end_date|date|Yes|'0000-00-00'|表示終了日付|
|8|title|varchar(128)|Yes||表示タイトル|
|9|title_color|varchar(7)|Yes||表示タイトル色|
|10|msg1_title|varchar(128)|Yes||メッセージ１タイトル|
|11|msg1_title_color|varchar(7)|Yes||メッセージ１タイトル色|
|12|msg1|text|Yes||メッセージ１内容|
|13|msg2_title|varchar(128)|Yes||メッセージ２タイトル|
|14|msg2_title_color|varchar(7)|Yes||メッセージ２タイトル色|
|15|msg2|text|Yes||メッセージ２内容|
|16|msg3_title|varchar(128)|Yes||メッセージ３タイトル|
|17|msg3_title_color|varchar(7)|Yes||メッセージ３タイトル色|
|18|msg3|text|Yes||メッセージ３内容|
|19|msg4_title|varchar(128)|Yes||メッセージ４タイトル|
|20|msg4_title_color|varchar(7)|Yes||メッセージ４タイトル色|
|21|msg4|text|Yes||メッセージ４内容|
|22|msg5_title|varchar(128)|Yes||メッセージ５タイトル|
|23|msg5_title_color|varchar(7)|Yes||メッセージ５タイトル色|
|24|msg5|text|Yes||メッセージ５内容|
|25|msg6_title|varchar(128)|Yes||メッセージ６タイトル|
|26|msg6_title_color|varchar(7)|Yes||メッセージ６タイトル色|
|27|msg6|text|Yes||メッセージ６内容|
|28|msg7_title|varchar(128)|Yes||メッセージ７タイトル|
|29|msg7_title_color|varchar(7)|Yes||メッセージ７タイトル色|
|30|msg7|text|Yes||メッセージ７内容|
|31|msg8_title|varchar(128)|Yes||メッセージ８タイトル|
|32|msg8_title_color|varchar(7)|Yes||メッセージ８タイトル色|
|33|msg8|text|Yes||メッセージ８内容|
|34|member_only|int(1)|Yes|0|メンバー限定フラグ（１：メンバー限定）|
|35|add_date|int(8)|Yes|0|追加日付|
|36|add_time|int(6)|Yes|0|追加時刻|
|37|add_hotelid|varchar(10)|Yes||追加ユーザーログインホテル|
|38|add_userid|int(11)|Yes|0|追加ユーザー|
|39|last_update|int(8)|Yes|0|更新日付|
|40|last_uptime|int(6)|Yes|0|更新時刻|
|41|upd_hotelid|varchar(10)|Yes||更新ユーザーログインホテル|
|42|upd_userid|int(11)|Yes|0|更新ユーザー|
|43|start_time|int(6)|Yes|0|入力は　時、分のみ　下2桁00|
|44|end_time|int(6)|Yes|0|入力は　時、分のみ　下2桁59|
|45|smart_flg|int(1)|Yes|0||

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,data_type,id|Yes|Yes||
