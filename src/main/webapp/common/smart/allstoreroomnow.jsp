<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>管理店舗情報(現在部屋状況)</title>
<script type="text/javascript" src="../../common/smart/scripts/tableSort.js"></script>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
<style>
.storeCheck {
    float: right;
    width: 20px;
    height: 20px;
}
.Selects {
    font-size:18px;
}
</style>
</head>
<body class="portrait" text="#555555">
<jsp:include page="header.jsp" flush="true" />
<h1 class="title">管理店舗 現在部屋情報</h1>
<%-- 管理店舗部屋情報取得 --%>
<jsp:include page="allstoreroomnowget.jsp" flush="true" />
<%-- 管理店舗部屋情報取得ここまで --%>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
