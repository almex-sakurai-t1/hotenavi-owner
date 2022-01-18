# edit_coupon

## Overview

ホテナビ・ホテルごとのクーポン情報を管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|id|int(11) auto_increment|Yes (PK)||管理番号|
|3|disp_idx|int(2)|Yes|0|表示順番|
|4|coupon_type|int(6)|Yes|0|クーポン種別|
|5|start_date|date|Yes|'0000-00-00'|表示開始日付|
|6|end_date|date|Yes|'0000-00-00'|表示終了日付|
|7|hotel_name|varchar(64)|Yes||ホテル名称|
|8|coupon_name|varchar(64)|Yes||クーポン名称|
|9|contents1|varchar(64)|Yes||表示コンテンツ１|
|10|contents2|varchar(64)|Yes||表示コンテンツ２|
|11|restrict1|varchar(255)|Yes||クーポン利用条件１|
|12|restrict2|varchar(255)|Yes||クーポン利用条件２|
|13|use_limit|varchar(64)|Yes||使用期限|
|14|teleno|varchar(16)|Yes||電話番号|
|15|member_only|int(1)|Yes|0|メンバー限定フラグ（１：メンバー限定）|
|16|all_seq|int(10)|Yes|0|シリアル番号（新規作成都度に採番）|
|17|disp_mobile|int(1)|Yes|0|1:携帯に表示|
|18|disp_mobile_message|varchar(255)|Yes||携帯用メッセージ|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,id|Yes|Yes||
