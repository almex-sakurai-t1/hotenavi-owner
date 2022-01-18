# mag_data

## Overview

ホテルごとにメールマガジン送信内容に関するデータを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|history_id|int(11) auto_increment|Yes (PK)||管理番号|
|3|subject|varchar(128)|Yes||題名|
|4|body|text|Yes||本文|
|5|count|int(8)|Yes|0|配信件数|
|6|state|int(1)|Yes|0|配信状態(0:配信待ち,1:配信済み,2:削除)|
|7|send_flag|int(1)|Yes|0|配信フラグ(1:時間指定,2:すぐ送信)|
|8|send_date|int(8)|Yes|0|配信日付(YYYYMMDD)|
|9|send_time|int(8)|Yes|0|配信時刻(HHMM)|
|10|condition_all|int(1)|Yes|0|配信条件(0:全て,1:ビジター,2:メンバー,3:条件検索)|
|11|birthday_flag|int(1)|Yes|0|配信条件(誕生日指定)|
|12|birthday_start|int(8)|Yes|0|配信条件(誕生日開始)|
|13|birthday_end|int(8)|Yes|0|配信条件(誕生日終了)|
|14|memorial_flag|int(1)|Yes|0|配信条件(記念日指定)|
|15|memorial_start|int(8)|Yes|0|配信条件(記念日開始)|
|16|memorial_end|int(8)|Yes|0|配信条件(記念日終了)|
|17|lastdate_flag|int(1)|Yes|0|配信条件(最終来店日指定)|
|18|lastdate_start|int(8)|Yes|0|配信条件(最終来店日開始)|
|19|lastdate_end|int(8)|Yes|0|配信条件(最終来店日終了)|
|20|use_flag|int(1)|Yes|0|配信条件(利用回数指定)|
|21|use_start|int(9)|Yes|0|配信条件(利用回数開始)|
|22|use_end|int(9)|Yes|0|配信条件(利用回数終了)|
|23|total_flag|int(1)|Yes|0|配信条件(利用金額指定)|
|24|total_start|int(9)|Yes|0|配信条件(利用金額開始)|
|25|total_end|int(9)|Yes|0|配信条件(利用金額終了)|
|26|point_flag|int(1)|Yes|0|配信条件(ポイント指定)|
|27|point_start|int(9)|Yes|0|配信条件(ポイント開始)|
|28|point_end|int(9)|Yes|0|配信条件(ポイント終了)|
|29|rank_flag|int(1)|Yes|0|配信条件(ランク指定)|
|30|rank_start|int(4)|Yes|0|配信条件(ランク開始)|
|31|rank_end|int(4)|Yes|0|配信条件(ランク終了)|
|32|lastsend_flag|int(1)|Yes|0|配信条件(最終送信日指定)|
|33|lastsend_start|int(8)|Yes|0|配信条件(最終送信日開始)|
|34|lastsend_end|int(8)|Yes|0|配信条件(最終送信日終了)|
|35|lastsend_starttime|int(8)|Yes|0|配信条件(最終送信日開始時刻)|
|36|lastsend_endtime|int(8)|Yes|0|配信条件(最終送信日終了時刻)|
|37|mailboss_state|int(1)|Yes|0|MailBOSS配信ステータス(1:配信済み,2:完了)|
|38|param_query|text|Yes||mag_address抽出のクエリ文|
|39|add_date|timestamp|Yes|CURRENT_TIMESTAMP|入力日時|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,history_id|Yes|Yes||
