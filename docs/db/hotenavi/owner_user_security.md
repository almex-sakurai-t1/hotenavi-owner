# owner_user_security

## Overview

オーナーサイトへのログインするユーザのアクセス権限を管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid|varchar(10)|Yes (PK)||ホテルID|
|2|userid|int(11)|Yes (PK)|0|ユーザID|
|3|admin_flag|int(1)|Yes|0|Administrator権限|
|4|sec_level01|int(1)|Yes|0|権限Level1（売上管理）|
|5|sec_level02|int(1)|Yes|0|権限Level2（部屋情報）|
|6|sec_level03|int(1)|Yes|0|権限Level3（帳票管理）|
|7|sec_level04|int(1)|Yes|0|権限Level4（HPレポート）|
|8|sec_level05|int(1)|Yes|0|権限Level5（メルマガ作成）|
|9|sec_level06|int(1)|Yes|0|権限Level6（HP編集）|
|10|sec_level07|int(1)|Yes|0|権限Level7（設定メニュー）|
|11|sec_level08|int(1)|Yes|0|権限Level8（ManageEye）|
|12|sec_level09|int(1)|Yes|0|権限Level9（帳票ダウンロード）|
|13|sec_level10|int(1)|Yes|0|権限Level10（多店舗帳票選択）|
|14|sec_level11|int(1)|Yes|0|権限Level11（掲示板預かり上げ555用）|
|15|sec_level12|int(1)|Yes|0|権限Level12（掲示板許可555用）|
|16|sec_level13|int(1)|Yes|0|権限Level13（予約時にPCにメール）|
|17|sec_level14|int(1)|Yes|0|権限Level14（予約時に携帯にメール）|
|18|sec_level15|int(1)|Yes|0|権限Level15（ハピホテ編集）|
|19|sec_level16|int(1)|Yes|0|権限Level16（クチコミ回答）|
|20|sec_level17|int(1)|Yes|0|権限Level17（クチコミお知らせメールPC）|
|21|sec_level18|int(1)|Yes|0|権限Level18（クチコミお知らせメール携帯）|
|22|sec_level19|int(1)|Yes|0|権限Level19（予約フロントサイト）|
|23|sec_level20|int(1)|Yes|0|権限Level20（請求明細表示）|
|24|sec_level21|int(1)|Yes|0||
|25|sec_level22|int(1)|Yes|0||
|26|sec_level23|int(1)|Yes|0||
|27|sec_level24|int(1)|Yes|0||
|28|sec_level25|int(1)|Yes|0||
|29|sec_level26|int(1)|Yes|0||
|30|sec_level27|int(1)|Yes|0||
|31|sec_level28|int(1)|Yes|0||
|32|sec_level29|int(1)|Yes|0||
|33|sec_level30|int(1)|Yes|0||
|34|sales_cache_flag|int(1)|Yes|1|1:売上情報をキャッシュする|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotelid,userid|Yes|Yes||
