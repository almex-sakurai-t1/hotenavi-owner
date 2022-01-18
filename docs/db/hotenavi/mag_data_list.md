# mag_data_list

## Overview

"メールマガジン送信ごとの有無を管理する
"

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|history_id|int(11)|Yes (PK)|0|管理番号|
|3|address|varchar(128)|Yes (PK)||メールアドレス|
|4|send_flag|int(1)|Yes|0|送信済みフラグ(1:送信済み)|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,history_id,address|Yes|Yes||
