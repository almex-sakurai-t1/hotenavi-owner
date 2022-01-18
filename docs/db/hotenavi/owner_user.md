# owner_user

## Overview

オーナーサイトへのログインするユーザに関するデータを管理する。

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|userid|int(11) auto_increment|Yes (PK)||ユーザID|
|3|loginid|varchar(32)|Yes (PK)||PCログインID|
|4|machineid|varchar(32)|Yes||携帯ログインID|
|5|name|varchar(64)|Yes||ユーザ名|
|6|passwd_pc|varchar(255)|Yes||PCパスワード|
|7|passwd_mobile|varchar(255)|Yes||携帯パスワード|
|8|mailaddr_pc|varchar(255)|Yes||PCメールアドレス|
|9|mailaddr_mobile|varchar(255)|Yes||携帯メールアドレス|
|10|level|int(2)|Yes|0|ユーザレベル|
|11|imedia_user|int(1)|Yes|0|アイメディア専用ユーザ(1:専用)|
|12|report_flag|int(1)|Yes|0|日報メール送信フラグ(1:個別設定)|
|13|mail_starttime|int(4)|Yes|0|メール受信可能開始時間|
|14|mail_endtime|int(4)|Yes|0|メール受信可能終了時間|
|15|unknown_flag_pc|int(1)|Yes|0|PCメールアドレスUnknown|
|16|unknown_flag_mobile|int(1)|Yes|0|携帯メールアドレスUnknown|
|17|passwd_pc_update|int(8)|Yes|0|PCパスワード修正日：仮発行時は0|
|18|passwd_mobile_update|int(8)|Yes|0|携帯パスワード修正日：仮発行時は0|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,userid,loginid|Yes|Yes||
