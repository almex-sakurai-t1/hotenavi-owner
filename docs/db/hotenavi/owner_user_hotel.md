# owner_user_hotel

## Overview

オーナーサイトへのログインするユーザに対して管理できるホテルIDを管理する。

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|userid|int(11)|Yes (PK)|0|ユーザID|
|3|accept_hotelid|varchar(10)|Yes (PK)||利用可能ホテルID|
|4|report_times|int(4)|Yes|0|送信可能開始時刻(HHMM) 0:即時|
|5|report_timee|int(4)|Yes|0|送信可能終了時刻(HHMM) 0:即時|
|6|report_daily_mobile|int(1)|Yes|0|日報携帯メールフラグ(0:送信なし,1:毎日送信する,2:締日に送信)|
|7|report_daily_pc|int(1)|Yes|0|日報PCメールフラグ(0:送信なし,1:送信する,2:締日に送信)|
|8|report_month_mobile|int(1)|Yes|0|月報携帯メールフラグ(0:送信なし,1:送信する,2:締日に送信)|
|9|report_month_pc|int(1)|Yes|0|月報PCメールフラグ(0:送信なし,1:送信する,2:締日に送信)|
|10|report_lastdate|int(8)|Yes|0|最終送信日付(YYYYMMDD)|
|11|report_lasttime|int(6)|Yes|0|最終送信時刻(HHMMSS)|
|12|report_credit|int(1)|Yes|0|クレジット帳票メールフラグ(0:送信なし,1:送信する,2:締日に送信)|
|13|report_center|int(1)|Yes|0|多店舗帳票メールフラグ(0:送信なし,1:送信する,2:締日に送信)|
|14|report_center_mobile|int(1)|Yes|0|多店舗帳票携帯メールフラグ(0:送信なし,1:送信する,2:締日に送信)|
|15|sales_disp_flag|int(1)|Yes|1|1:売上管理一覧表示時、表示対象|
|16|report_tex|int(1)|Yes|0||

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,userid,accept_hotelid|Yes|Yes||
