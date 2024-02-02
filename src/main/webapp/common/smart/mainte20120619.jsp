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
◆回線接続障害のお知らせ◆<br>
<hr class="border">
平素より、サービスをご利用いただき、誠にありがとうございます。<br>
6月19日（火）15：30ごろ、センター機器の一部に不具合が発生し、一部の店舗にてホテナビサービスが利用できない状態が発生しました。<br>
現在は復旧しておりますが、接続回復までに時間を要し、ご迷惑をおかけしましたことを深くお詫び申し上げます。<br>
<br>
株式会社 アルメックス <br>
</body>
</html>
