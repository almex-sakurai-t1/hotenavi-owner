<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ﾊﾟｽﾜｰﾄﾞ変更</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
ようこそ<br>
<%= ownerinfo.DbUserName %>様<br>
ﾊﾟｽﾜｰﾄﾞの変更をお願いします<br>

<form action="<%= response.encodeURL("passwdupdate.jsp") %>" method="post">
  現ﾊﾟｽﾜｰﾄﾞ<input type="password" name="oldpassword" size="8" maxlength="8" istyle="4"><br>
  新ﾊﾟｽﾜｰﾄﾞ<input type="password" name="newpassword" size="8" maxlength="8" istyle="4"><br>
  もう一度<input type="password" name="newpassword2" size="8" maxlength="8" istyle="4"><br>
  <br>
  <input type="submit" value="変更する">
</form>

<hr>
<div align="center">
<a href="<%= response.encodeURL("index.jsp") %>">TOPへ</a><br>
</div>
<hr>
<div align="center">
Copyright(C) almex inc. All rights reserved.
</div>
</body>
</html>
