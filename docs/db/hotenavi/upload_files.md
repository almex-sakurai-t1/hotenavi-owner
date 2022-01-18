# upload_files

## Overview

オーナーサイトからアップロードされた画像やファイルを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|data_type|int(1)|Yes (PK)|0|データタイプ 0:画像 1:画像以外|
|3|id|int(11) auto_increment|Yes (PK)||連番(AUTO_INCREMENT)|
|4|originalfilename|varchar(255)|Yes||オリジナルファイル名|
|5|filename|varchar(255)|Yes||アップロードファイル名|
|6|subject|varchar(255)|Yes||画像名称|
|7|extension|varchar(8)|Yes||拡張子|
|8|filesize|int(11)|Yes|0|ファイルサイズ|
|9|originalwidth|varchar(8)|Yes||オリジナル画像サイズ(横）|
|10|originalheight|varchar(8)|Yes||オリジナル画像サイズ（縦）|
|11|imgwidth|varchar(8)|Yes||表示画像サイズ（横）|
|12|imgheight|varchar(8)|Yes||表示画像サイズ(縦）|
|13|message|text|Yes||メッセージ|
|14|del_flag|int(1)|Yes|0|削除フラグ 1:削除済|
|15|add_date|int(8)|Yes|0|入力日付|
|16|add_time|int(6)|Yes|0|入力時刻|
|17|add_hotelid|varchar(10)|Yes||入力ユーザーホテルid|
|18|add_userid|int(11)|Yes|0|入力ユーザーid|
|19|user_agent|varchar(255)|Yes||操作端末ユーザーエージェント|
|20|host|varchar(255)|Yes||登録ホスト|
|21|imedia_user|int(1)|Yes|0|1:アイメディアユーザーが登録|
|22|target_hotelid|varchar(10)|Yes||画像掲載対象ホテルID|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,data_type,id|Yes|Yes||
|2|target_hotelid||||
