# access_counter_sp

## Overview

ホテルTOPへのクリック数を管理する。

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテナビID|
|2|access_cnt|int(11)|Yes|0|累計のアクセスカウント。アクセスするごとに1加算される|
|3|access_today|int(11)|Yes|0|本日のアクセスカウント。last_update が当日日付と違う場合は1に更新|
|4|access_prev|int(11)|Yes|0|前日のアクセスカウント。last_updateが当日日付と違う場合は、access_todayがセット|
|5|last_update|int(8)|Yes|0|最終更新日(YYYYMMDD) |
|6|contents_pc|text|Yes||PCスマホ向けhtmlコード　%TOTAL%→access_cntに変換、%TODAY%→access_todayに変換、%YESTERDAY%→access_prev に変換|
|7|contents_mobile|text|Yes||ガラケー向けhtmlコード　%TOTAL%→access_cntに変換、%TODAY%→access_todayに変換、%YESTERDAY%→access_prev に変換|
|8|disp_flag|int(1)|Yes|0|1:表示（旧ホテナビのみ適用）　新ホテナビの表示有無はcssで調整|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|PRIMARY|hotel_id|Yes|Yes||
