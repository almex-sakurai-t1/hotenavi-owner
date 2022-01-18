# bbs_ngword

## Overview

掲示板投稿における禁止ワードデータを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|id|int(9) auto_increment|Yes (PK)||管理番号|
|3|ng_word|varchar(255)|Yes||禁止ワード|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,id|Yes|Yes||

