# goods_entry

## Overview

商品申込みデータ

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)|||
|2|category_id|int(11)|Yes (PK)|0||
|3|seq|int(9)|Yes (PK)|0||
|4|entry_id|int(11)|Yes (PK)|0||
|5|entry_branch|int(9)|Yes (PK)|0||
|6|name|varchar(32)|Yes|||
|7|mail_address|varchar(128)|Yes|||
|8|mail_address_mobile|varchar(128)|Yes|||
|9|point|int(9)|Yes|0||
|10|use_point|int(9)|Yes|0||
|11|count|int(9)|Yes|0||
|12|use_total|int(9)|Yes|0||
|13|method|int(1)|Yes|0||
|14|send_name|varchar(32)|Yes|||
|15|zip_code|varchar(10)|Yes|||
|16|pref_name|varchar(32)|Yes|||
|17|address1|varchar(128)|Yes|||
|18|address2|varchar(128)|Yes|||
|19|tel1|varchar(32)|Yes|||
|20|tel2|varchar(32)|Yes|||
|21|memo|text|Yes|||
|22|save_flag|int(1)|Yes|0||
|23|input_date|int(8)|Yes|0||
|24|input_time|int(6)|Yes|0||
|25|emit_date|int(8)|Yes|0||
|26|emit_time|int(6)|Yes|0||
|27|emit_hotelid|varchar(10)|Yes|||
|28|emit_userid|int(11)|Yes|0||
|29|status|int(1)|Yes|0||
|30|custom_id|varchar(9)|Yes|||
|31|user_id|varchar(32)|Yes|||
|32|user_agent|varchar(255)|Yes|||
|33|remote_ip|varchar(255)|Yes|||
|34|custom_md5|varchar(255)|Yes|||
|35|nick_name|varchar(32)|Yes|||
|36|topic_hotelid|varchar(10)|Yes|||
|37|inquiry_no|int(11)|Yes|0||
|38|address3|varchar(128)|Yes|||
|39|gift_number|varchar(32)|Yes|||
|40|request_flag|int(1)|Yes|0||

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,category_id,seq,entry_id,entry_branch|Yes|Yes||
|2|entry_id,entry_branch||||
