# question_data

## Overview

アンケートの質問表示に関するデータを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|id|int(8)|Yes (PK)|0|管理番号|
|3|q_id|varchar(32)|Yes (PK)||質問番号|
|4|sub_id|int(2)|Yes (PK)|0|質問サブID|
|5|value|int(5)|||質問回答値|
|6|msg|text|Yes||表示メッセージ|
|7|text_flag|int(1)|Yes|0|テキストフラグ|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,id,q_id,sub_id|Yes|Yes||
