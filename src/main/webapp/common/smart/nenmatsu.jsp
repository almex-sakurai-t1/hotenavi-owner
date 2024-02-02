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
年末年始休業のお知らせ<br>
<hr class="border">
下記の期間､年末年始休暇のため業務をお休みさせていただきます｡<br>
なお､緊急ﾒﾝﾃﾅﾝｽの際は、代理店の株式会社ｱﾙﾒｯｸｽの各支店担当にご連絡いただきますようお願い申し上げます｡<br>
<br>
大変恐縮でございますが､ご了承くださいますようお願い申し上げます｡ <br>
<br>
【休業期間】<br>
平成17年12月30日(金)~<br>
平成18年1月4日(水)<br>
<br>
株式会社ｱｲﾒﾃﾞｨｱ
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
