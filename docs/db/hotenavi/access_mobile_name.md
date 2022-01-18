# access_mobile_name

## Overview

"ホテル単位でのページ別アクセス解析用ページ名称を管理する。				
"

## Columns

|No.|物理名|データ型|Not Null|デフォルト|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|varchar(10)|Yes (PK)||ホテルID|
|2|page01|varchar(64)|Yes|'ビジターTOP'|ページ名称１|
|3|page02|varchar(64)|Yes|'Members Only'|ページ名称２|
|4|page03|varchar(64)|Yes|'Whats New'|ページ名称３|
|5|page04|varchar(64)|Yes|'客室情報'|ページ名称４|
|6|page05|varchar(64)|Yes|'料金情報'|ページ名称５|
|7|page06|varchar(64)|Yes|'イベント情報'|ページ名称６|
|8|page07|varchar(64)|Yes|'クレジット'|ページ名称７|
|9|page08|varchar(64)|Yes|'サービス・設備情報'|ページ名称８|
|10|page09|varchar(64)|Yes|'アクセス情報'|ページ名称９|
|11|page10|varchar(64)|Yes|'システム情報'|ページ名称１０|
|12|page11|varchar(64)|Yes|'メンバーのご案内'|ページ名称１１|
|13|page12|varchar(64)|Yes|'アンケート'|ページ名称１２|
|14|page13|varchar(64)|Yes|'外観写真'|ページ名称１３|
|15|page14|varchar(64)|Yes|'設備情報'|ページ名称１４|
|16|page15|varchar(64)|Yes|'ルーム予約'|ページ名称１５|
|17|page16|varchar(64)|Yes|'空室情報'|ページ名称１６|
|18|page17|varchar(64)|Yes|'料金シミュレーション'|ページ名称１７|
|19|page18|varchar(64)|Yes|'ホテルへ一言'|ページ名称１８|
|20|page19|varchar(64)|Yes|'掲示板'|ページ名称１９|
|21|page20|varchar(64)|Yes|'Mailmagazine'|ページ名称２０|
|22|page21|varchar(64)|Yes|'本日のシステム'|ページ名称２１|
|23|page22|varchar(64)|Yes||ページ名称２２|
|24|page23|varchar(64)|Yes||ページ名称２３|
|25|page24|varchar(64)|Yes||ページ名称２４|
|26|page25|varchar(64)|Yes||ページ名称２５|
|27|page26|varchar(64)|Yes||ページ名称２６|
|28|page27|varchar(64)|Yes||ページ名称２７|
|29|page28|varchar(64)|Yes||ページ名称２８|
|30|page29|varchar(64)|Yes||ページ名称２９|
|31|page30|varchar(64)|Yes||ページ名称３０|
|32|page31|varchar(64)|Yes||ページ名称３１|
|33|page32|varchar(64)|Yes||ページ名称３２|
|34|page33|varchar(64)|Yes||ページ名称３３|
|35|page34|varchar(64)|Yes||ページ名称３４|
|36|page35|varchar(64)|Yes||ページ名称３５|
|37|page36|varchar(64)|Yes||ページ名称３６|
|38|page37|varchar(64)|Yes||ページ名称３７|
|39|page38|varchar(64)|Yes||ページ名称３８|
|40|page39|varchar(64)|Yes||ページ名称３９|
|41|page40|varchar(64)|Yes||ページ名称４０|
|42|page41|varchar(64)|Yes||ページ名称４１|
|43|page42|varchar(64)|Yes||ページ名称４２|
|44|page43|varchar(64)|Yes||ページ名称４３|
|45|page44|varchar(64)|Yes||ページ名称４４|
|46|page45|varchar(64)|Yes||ページ名称４５|
|47|page46|varchar(64)|Yes||ページ名称４６|
|48|page47|varchar(64)|Yes||ページ名称４７|
|49|page48|varchar(64)|Yes||ページ名称４８|
|50|page49|varchar(64)|Yes||ページ名称４９|
|51|page50|varchar(64)|Yes||ページ名称５０|
|52|page99|varchar(64)|Yes|'不明なページ'|ページ名称９９|
|53|mem_page01|varchar(64)|Yes|'メンバーTOP'|メンバーページ名称１|
|54|mem_page02|varchar(64)|Yes|'Whats New'|メンバーページ名称２|
|55|mem_page03|varchar(64)|Yes|'客室情報'|メンバーページ名称３|
|56|mem_page04|varchar(64)|Yes|'料金情報'|メンバーページ名称４|
|57|mem_page05|varchar(64)|Yes|'イベント情報'|メンバーページ名称５|
|58|mem_page06|varchar(64)|Yes|'クレジット'|メンバーページ名称６|
|59|mem_page07|varchar(64)|Yes|'サービス・設備情報'|メンバーページ名称７|
|60|mem_page08|varchar(64)|Yes|'アクセス情報'|メンバーページ名称８|
|61|mem_page09|varchar(64)|Yes|'システム情報'|メンバーページ名称９|
|62|mem_page10|varchar(64)|Yes|'メンバーのご案内'|メンバーページ名称１０|
|63|mem_page11|varchar(64)|Yes|'アンケート'|メンバーページ名称１１|
|64|mem_page12|varchar(64)|Yes|'外観写真'|メンバーページ名称１２|
|65|mem_page13|varchar(64)|Yes|'設備情報'|メンバーページ名称１３|
|66|mem_page14|varchar(64)|Yes|'空室情報'|メンバーページ名称１４|
|67|mem_page15|varchar(64)|Yes|'料金シミュレーション'|メンバーページ名称１５|
|68|mem_page16|varchar(64)|Yes|'ホテルへ一言'|メンバーページ名称１６|
|69|mem_page17|varchar(64)|Yes|'掲示板'|メンバーページ名称１７|
|70|mem_page18|varchar(64)|Yes|'Mailmagazine'|メンバーページ名称１８|
|71|mem_page19|varchar(64)|Yes|'本日のシステム'|メンバーページ名称１９|
|72|mem_page20|varchar(64)|Yes|'ルーム予約'|メンバーページ名称２０|
|73|mem_page21|varchar(64)|Yes|'メンバー情報確認'|メンバーページ名称２１|
|74|mem_page22|varchar(64)|Yes|'ご利用履歴'|メンバーページ名称２２|
|75|mem_page23|varchar(64)|Yes|'ポイント履歴'|メンバーページ名称２３|
|76|mem_page24|varchar(64)|Yes|'ポイント２履歴'|メンバーページ名称２４|
|77|mem_page25|varchar(64)|Yes|'全室達成'|メンバーページ名称２５|
|78|mem_page26|varchar(64)|Yes|'オーナーズルーム'|メンバーページ名称２６|
|79|mem_page27|varchar(64)|Yes|'ランキング'|メンバーページ名称２７|
|80|mem_page28|varchar(64)|Yes|'メンバー情報変更'|メンバーページ名称２８|
|81|mem_page29|varchar(64)|Yes|'メールアドレス変更'|メンバーページ名称２９|
|82|mem_page30|varchar(64)|Yes|'メッセージ'|メンバーページ名称３０|
|83|mem_page31|varchar(64)|Yes||メンバーページ名称３１|
|84|mem_page32|varchar(64)|Yes||メンバーページ名称３２|
|85|mem_page33|varchar(64)|Yes||メンバーページ名称３３|
|86|mem_page34|varchar(64)|Yes||メンバーページ名称３４|
|87|mem_page35|varchar(64)|Yes||メンバーページ名称３５|
|88|mem_page36|varchar(64)|Yes||メンバーページ名称３６|
|89|mem_page37|varchar(64)|Yes||メンバーページ名称３７|
|90|mem_page38|varchar(64)|Yes||メンバーページ名称３８|
|91|mem_page39|varchar(64)|Yes||メンバーページ名称３９|
|92|mem_page40|varchar(64)|Yes||メンバーページ名称４０|
|93|mem_page41|varchar(64)|Yes||メンバーページ名称４１|
|94|mem_page42|varchar(64)|Yes||メンバーページ名称４２|
|95|mem_page43|varchar(64)|Yes||メンバーページ名称４３|
|96|mem_page44|varchar(64)|Yes||メンバーページ名称４４|
|97|mem_page45|varchar(64)|Yes||メンバーページ名称４５|
|98|mem_page46|varchar(64)|Yes||メンバーページ名称４６|
|99|mem_page47|varchar(64)|Yes||メンバーページ名称４７|
|100|mem_page48|varchar(64)|Yes||メンバーページ名称４８|
|101|mem_page49|varchar(64)|Yes||メンバーページ名称４９|
|102|mem_page50|varchar(64)|Yes||メンバーページ名称５０|
|103|mem_page99|varchar(64)|Yes|'不明なページ'|メンバーページ名称９９|

## Indexes

|No.|インデックス名|カラムリスト|主キー|ユニーク|備考|
|:--|:--|:--|:--|:--|:--|
|1|hotel_id|Yes|Yes||
