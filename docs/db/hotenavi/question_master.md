# question_master

## Overview

アンケートの質問表示に関するデータを管理する

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|id|int(8) auto_increment|Yes (PK)||管理番号|
|3|start|date|Yes|'0000-00-00'|開始日付|
|4|end|date|Yes|'0000-00-00'|終了日付|
|5|q1|int(2)|Yes|0|アンケート種別１|
|6|q2|int(2)|Yes|0|アンケート種別２|
|7|q3|int(2)|Yes|0|アンケート種別３|
|8|q4|int(2)|Yes|0|アンケート種別４|
|9|q5|int(2)|Yes|0|アンケート種別５|
|10|q6|int(2)|Yes|0|アンケート種別６|
|11|q7|int(2)|Yes|0|アンケート種別７|
|12|q8|int(2)|Yes|0|アンケート種別８|
|13|q9|int(2)|Yes|0|アンケート種別９|
|14|q10|int(2)|Yes|0|アンケート種別１０|
|15|q1_msg|varchar(255)|Yes||アンケート質問内容１|
|16|q2_msg|varchar(255)|Yes||アンケート質問内容２|
|17|q3_msg|varchar(255)|Yes||アンケート質問内容３|
|18|q4_msg|varchar(255)|Yes||アンケート質問内容４|
|19|q5_msg|varchar(255)|Yes||アンケート質問内容５|
|20|q6_msg|varchar(255)|Yes||アンケート質問内容６|
|21|q7_msg|varchar(255)|Yes||アンケート質問内容７|
|22|q8_msg|varchar(255)|Yes||アンケート質問内容８|
|23|q9_msg|varchar(255)|Yes||アンケート質問内容９|
|24|q10_msg|varchar(255)|Yes||アンケート質問内容１０|
|25|msg|text|Yes||表示メッセージ|
|26|q11|int(2)|Yes|0|アンケート種別１１|
|27|q12|int(2)|Yes|0|アンケート種別１２|
|28|q13|int(2)|Yes|0|アンケート種別１３|
|29|q14|int(2)|Yes|0|アンケート種別１４|
|30|q15|int(2)|Yes|0|アンケート種別１５|
|31|q16|int(2)|Yes|0|アンケート種別１６|
|32|q17|int(2)|Yes|0|アンケート種別１７|
|33|q18|int(2)|Yes|0|アンケート種別１８|
|34|q19|int(2)|Yes|0|アンケート種別１９|
|35|q20|int(2)|Yes|0|アンケート種別２０|
|36|q21|int(2)|Yes|0|アンケート種別２１|
|37|q22|int(2)|Yes|0|アンケート種別２２|
|38|q23|int(2)|Yes|0|アンケート種別２３|
|39|q24|int(2)|Yes|0|アンケート種別２４|
|40|q25|int(2)|Yes|0|アンケート種別２５|
|41|q26|int(2)|Yes|0|アンケート種別２６|
|42|q27|int(2)|Yes|0|アンケート種別２７|
|43|q28|int(2)|Yes|0|アンケート種別２８|
|44|q29|int(2)|Yes|0|アンケート種別２９|
|45|q30|int(2)|Yes|0|アンケート種別３０|
|46|q11_msg|varchar(255)|Yes||アンケート質問内容１１|
|47|q12_msg|varchar(255)|Yes||アンケート質問内容１２|
|48|q13_msg|varchar(255)|Yes||アンケート質問内容１３|
|49|q14_msg|varchar(255)|Yes||アンケート質問内容１４|
|50|q15_msg|varchar(255)|Yes||アンケート質問内容１５|
|51|q16_msg|varchar(255)|Yes||アンケート質問内容１６|
|52|q17_msg|varchar(255)|Yes||アンケート質問内容１７|
|53|q18_msg|varchar(255)|Yes||アンケート質問内容１８|
|54|q19_msg|varchar(255)|Yes||アンケート質問内容１９|
|55|q20_msg|varchar(255)|Yes||アンケート質問内容２０|
|56|q21_msg|varchar(255)|Yes||アンケート質問内容２１|
|57|q22_msg|varchar(255)|Yes||アンケート質問内容２２|
|58|q23_msg|varchar(255)|Yes||アンケート質問内容２３|
|59|q24_msg|varchar(255)|Yes||アンケート質問内容２４|
|60|q25_msg|varchar(255)|Yes||アンケート質問内容２５|
|61|q26_msg|varchar(255)|Yes||アンケート質問内容２６|
|62|q27_msg|varchar(255)|Yes||アンケート質問内容２７|
|63|q28_msg|varchar(255)|Yes||アンケート質問内容２８|
|64|q29_msg|varchar(255)|Yes||アンケート質問内容２９|
|65|q30_msg|varchar(255)|Yes||アンケート質問内容３０|
|66|member_flag|int(1)|Yes|0|メンバー表示フラグ（１：メンバーのみ）|
|67|duplicate_check|int(1)|Yes|0|二重登録チェック（１：あり）|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id,id|Yes|Yes||
