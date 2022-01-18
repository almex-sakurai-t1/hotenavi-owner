# bbs_multi

## Overview

多店舗用掲示板の投稿データを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|msg_id|int(11) auto_increment|Yes (PK)||メッセージID|
|3|sub_id|int(11)|Yes (PK)|0|メッセージサブID|
|4|input_date|datetime|Yes|'0000-00-00 00:00:00'|入力日付|
|5|name|varchar(64)|Yes||ニックネーム|
|6|mail|varchar(64)|||メールアドレス|
|7|subject|varchar(64)|Yes||題名|
|8|body|text|Yes||本文|
|9|del_key|int(11)|Yes|0|削除キー|
|10|del_flag|int(1)|Yes|0|削除フラグ|
|11|user_agent|varchar(255)|||投稿者ユーザエージェント|
|12|remote_ip|varchar(64)|||投稿者IPアドレス|
|13|topic_hotel_id|varchar(10)|||トピック対象ホテルID|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,msg_id,sub_id|Yes|Yes||

