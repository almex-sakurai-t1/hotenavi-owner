# hotel_config

## Overview

空室情報表示、ルーム予約等、ホテルごとのホームページ表示用設定を管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|rec_flag|int(1)|Yes (PK)|0|0:通常、1:メンバー専用|
|3|empty_title|varchar(64)|Yes|'0'|空室情報タイトル名|
"|4|empty_flag|int(1)|Yes|0|0:表示なし,1:表示あり,2:部屋ランク別
3:表示あり（""""以上""""なし）
4:表示あり部屋ランク別（""""以上""""なし）|"
|5|empty_method|int(3)|Yes|0|0:表示なし,999:全て表示,1～998未満表示|
|6|empty_class|varchar(64)|Yes||空室タイトル箇所クラス指定|
|7|no_vacancies|varchar(64)|Yes||満室時メッセージ|
"|8|clean_flag|int(1)|Yes|0|0:表示なし,1:表示あり,2:部屋ランク別
5:表示あり（empty_method以上は表示なし）
6:表示あり部屋ランク別（empty_method以上は表示なし）|"
|9|clean_method|int(3)|Yes|0|0:表示なし,999:全て表示,1～998未満表示|
|10|clean_class|varchar(64)|Yes||作業中タイトル箇所クラス指定|
|11|empty_list_method|int(3)|Yes|0|0:表示なし,999:全て表示,1～998未満表示|
|12|empty_list_class|varchar(64)|Yes||空室一覧内クラス指定|
|13|clean_list_method|int(3)|Yes|0|0:表示なし,999:全て表示,1～998未満表示|
|14|clean_list_class|varchar(64)|Yes||作業一覧内クラス指定|
|15|line_count|int(1)|Yes|0|部屋一覧の列数|
|16|room_link|text|Yes||PC部屋リンク箇所|
|17|allroom_flag|int(1)|Yes|0|1:全部屋表示|
|18|allroom_title|varchar(64)|Yes||全部屋一覧のタイトル|
|19|room_exclude|text|Yes||allroom_flag=1の場合、表示しない部屋(,区切り）|
|20|allroom_empty_link|text|Yes||PC全室表示空室時リンク箇所|
|21|allroom_novacancies_link|text|Yes||PC全室表示在室時リンク箇所|
|22|allroom_clean_link|text|Yes||PC全室表示準備中時リンク箇所|
|23|konzatsu_flag|int(1)|Yes|0|1:混雑状況あり|
|24|reserve_flag|int(1)|Yes|0|1:ルーム予約リンクあり|
|25|crosslink_flag|int(1)|Yes|0|1:系列店空室状況リンクあり|
|26|empty_findstr1|varchar(64)|Yes||予備１|
|27|empty_findstr2|varchar(64)|Yes||予備２|
|28|empty_findstr3|varchar(64)|Yes||予備３|
|29|reserve_title|varchar(64)|Yes||予約タイトル名|
|30|reserve_message|text|Yes||予約メッセージ|
|31|reserve_conductor|text|Yes||予約導線メッセージ|
|32|reserve_conductor_mail|text|Yes||予約導線メール内メッセージ|
|33|reserve_list_class|varchar(16)|Yes||予約リスト適用クラス名|
|34|add_date|int(8)|Yes|0|登録日付|
|35|add_time|int(6)|Yes|0|登録時刻|
|36|add_hotelid|varchar(10)|Yes||登録ホテルID|
|37|add_userid|int(11)|Yes|0|登録ユーザID|
|38|last_update|int(8)|Yes|0|最終更新日|
|39|last_uptime|int(6)|Yes|0|最終更新時刻|
|40|upd_hotelid|varchar(10)|Yes||更新ホテルID|
|41|upd_userid|int(11)|Yes|0|更新ユーザID|
|42|hotel_id_sub1|varchar(10)|Yes||空満取得用等（入力時のみ使用）|
|43|hotel_id_sub2|varchar(10)|Yes||空満取得用等（入力時のみ使用）|
|44|hotel_id_sub3|varchar(10)|Yes||空満取得用等（入力時のみ使用）|
|45|max_budget|int(5)|Yes|0|料金シミュレーション最大金額|
|46|simulate24_flag|int(1)|Yes|1|1:料金シミュレーション24時間で打ち切り|
|47|vacancies_message|varchar(64)|Yes||空室時メッセージ|
|48|empty_message_pc|text|Yes||空室情報メッセージ|
|49|reserve_mail_flag|int(1)|Yes|0|1:必須|
|50|allroom_flag2|int(1)|Yes|0|1:全部屋表示|
|51|allroom_title2|varchar(64)|Yes||全部屋一覧のタイトル|
|52|room_exclude2|text|Yes||allroom_flag2=1の場合、表示しない部屋(,区切り）|
|53|allroom_flag3|int(1)|Yes|0|1:全部屋表示|
|54|allroom_title3|varchar(64)|Yes||全部屋一覧のタイトル|
|55|room_exclude3|text|Yes||allroom_flag3=1の場合、表示しない部屋(,区切り）|
|56|reserve_update_flag|int(1)|Yes|1|1:予約後の部屋変更可|
|57|order_bbs|int(1)|Yes|0|多店舗掲示板並び順。999=除外|
|58|order_crosslink|int(1)|Yes|0|多店舗リンク並び順。999=除外|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,rec_flag|Yes|Yes||
|2|order_crosslink||||
