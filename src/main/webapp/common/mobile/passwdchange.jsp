<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �Z�b�V�����̊m�F
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
<title>�߽ܰ�ޕύX</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
�悤����<br>
<%= ownerinfo.DbUserName %>�l<br>
�߽ܰ�ނ̕ύX�����肢���܂�<br>

<form action="<%= response.encodeURL("passwdupdate.jsp") %>" method="post">
  ���߽ܰ��<input type="password" name="oldpassword" size="8" maxlength="8" istyle="4"><br>
  �V�߽ܰ��<input type="password" name="newpassword" size="8" maxlength="8" istyle="4"><br>
  ������x<input type="password" name="newpassword2" size="8" maxlength="8" istyle="4"><br>
  <br>
  <input type="submit" value="�ύX����">
</form>

<hr>
<div align="center">
<a href="<%= response.encodeURL("index.jsp") %>">TOP��</a><br>
</div>
<hr>
<div align="center">
Copyright(C) almex inc. All rights reserved.
</div>
</body>
</html>
