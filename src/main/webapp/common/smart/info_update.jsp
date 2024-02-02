<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page isErrorPage="true" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>error</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
</head>
<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">

<jsp:include page="header.jsp" flush="true" />
■日報・月報メール配信サービス遅延障害発生のご報告とお詫び【復旧済】<br>
2013-08-27<br>
平素より、ホテナビをご利用いただき誠にありがとうございます。<br>
2013年8月23日(金)から8月25日（日）にかけまして、オーナープランの日報・月報メール配信サービスにおきまして、一部のお客様に遅延が発生しておりました。<br>
<br>
本障害につきまして、現在は復旧しておりますことをご報告申し上げます。<br>
今後はシステム監視体制を強化し再発防止に努めてまいります。<br>
<br>
お客様には大変ご迷惑をお掛けしましたことを深くお詫び申し上げます。<br>
何卒よろしくお願い申し上げます。<br>
<hr>
2012-07-26<br>
･「売上情報」-「本日分」閲覧時に現在部屋情報も同時に表示するようにしました。<br>
※部屋情報閲覧権限がある場合のみ表示されます。<br>
2010-11-16<br>
･ｱﾙﾒｯｸｽからﾒｯｾｰｼﾞがある場合に表示されるようになりました。<br>
･管理者がﾛｸﾞｲﾝしたときに、各ﾕｰｻﾞｰのﾛｸﾞｲﾝ状況を確認できるようになりました。<br>
<hr>
2010-05-10<br>
･管理店舗が複数店舗ある場合､管理店舗の売上一覧とその合計表示しました。<br>
･各表示ﾍﾟｰｼﾞをﾌﾞｯｸﾏｰｸした場合､ﾊﾟｽﾜｰﾄﾞ入力で直接そのﾍﾟｰｼﾞを表示可能としました｡<br>
･情報表示後､前年（同曜日）､前週等のﾘﾝｸを追加しました。<br>
<hr>
2007-01-26<br>
･新ｼﾘをお使いのﾎﾃﾙも「部屋情報」に空満看板制御情報を表示しました｡<br>
<hr>
2005-09-07<br>
･売上情報、売上詳細、IN/OUT組数に、前日・翌日のﾘﾝｸを表示しました｡<br>
･売上情報の表示項目に、客単価と部屋単価を追加しました。<br>
･「月別日付売上一覧」を追加し､１ヶ月単位でﾎﾃﾙの売上を日別に閲覧できるようにしました。<br>
<hr>
2005-08-25<br>
･ありえない日付を入力した場合に、次の画面でｴﾗｰﾒｯｾｰｼﾞを表示し、現地のｺﾝﾋﾟｭｰﾀとの通信をしないようにしました。<br>
<hr>
2005-08-19<br>
･「現在部屋情報」からIN/OUT組数を表示した場合、日付箇所が0/0計上分と表示される不具合を修正しました。<br>
･「売上情報」で閲覧後、「部屋情報」でIN/OUT組数を表示した場合、日付箇所が「売上情報」で入力した日付になってしまう不具合を修正しました。<br>
<hr>
2005-08-17<br>
･売上情報・部屋情報で表示されているホテル一覧から、現在操作しているホテルの表示をなくしました。<br>
･操作しているホテル名を表示するようにしました。<br>
･「部屋詳細を見る」の中で、部屋明細を参照後、再度「部屋詳細を見る」で一覧表示した場合に、参照した部屋分のみしか表示されなくなる現象を修正しました。<br>
･IN/OUT組数の表示で計上前の時刻にみたときにすべて数値が0になることがあることに対する修正しました。<br>
<hr class="border">

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>
