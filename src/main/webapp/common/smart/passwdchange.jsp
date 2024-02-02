<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
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
<title>ﾊﾟｽﾜｰﾄﾞ変更</title>
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

<center>
<h2>ようこそ<br><%= ownerinfo.DbUserName %>様</h2>
<h2>個人パスワードの変更をお願いします</h2>
</center>

<hr class="border">

<div class="form" align="center">
<form action="<%= response.encodeURL("passwdupdate.jsp") %>" method="post">
  現パスワード：<br>
  <input id="text" type="password" name="oldpassword" size="20" maxlength="20" istyle="4"><br>
  新パスワード：<br>
  <input id="text2" type="password" name="newpassword" size="20" maxlength="20" istyle="4"><br>
  もう一度:<br>
  <input id="text3" type="password" name="newpassword2" size="20" maxlength="20" istyle="4"><br>
  <input type="submit" value="変更する" id="button">
</form>
</div>
<hr class="border">
<div align="center">
<a href="<%= response.encodeURL("index.jsp") %>">TOPへ</a><br>
</div>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />

</body>
</html>
