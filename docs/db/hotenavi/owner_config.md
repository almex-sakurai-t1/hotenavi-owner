# owner_config

## Overview

オーナーサイトの動作をコントロールする

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|roomdisp_interval|varchar(255)|Yes|'30,60,180,300,600'|表示間隔（秒）|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|Yes|Yes||
