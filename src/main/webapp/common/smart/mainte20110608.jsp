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
◆システムメンテナンスのお知らせ◆<br>
<hr class="border">
平素より、サービスをご利用いただき、誠にありがとうございます。<br>
ネットワークとサーバの整備を行うため、メンテナンスを実施いたします。<br>
作業時間帯は、ハッピー・ホテル及びホテナビの全てのサービスがご利用できなくなります。<br>
ご迷惑をおかけしますが、何卒ご理解のほどよろしくお願い申し上げます。<br>
<br>
【メンテナンス実施時間】<br>
平成23年6月8日(水)<br>
午前1:00～午前9:00<br>
<br>
株式会社 アルメックス <br>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
