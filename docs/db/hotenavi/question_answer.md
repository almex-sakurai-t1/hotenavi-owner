# question_answer

## Overview

アンケートの回答に関するデータを管理する。

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|id|int(8)|Yes (PK)|0|管理番号|
|3|q_id|varchar(32)|Yes (PK)||質問番号|
|4|seq|int(11) auto_increment|Yes (PK)||質問管理番号|
|5|answer|varchar(255)|||質問回答|
|6|ans_date|date||'0000-00-00'|回答日付|
|7|ans_time|time||'00:00:00'|回答時刻|
|8|user_agent|varchar(255)|||回答者ユーザーエージェント|
|9|custom_id|varchar(32)|Yes||回答者メンバーID|
|10|term_no|varchar(255)|Yes||二重登録チェック用|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,id,q_id,seq|Yes|Yes||
