<%@ page contentType="text/html; charset=Windows-31J" %>
<jsp:useBean id="custominfo" scope="session" class="com.hotenavi2.custom.CustomInfo" />
<%
    String id     = request.getParameter("id");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta NAME="Keywords"CONTENT="">
<meta name="Description" content="">
<title>レジャーホテル調査用フォーム</title>
</head>
  <frameset cols="100,400" frameborder="no" border="0" framespacing="0">
    <frame src="http://neptune.hotenavi.com/<%=id%>/i/roominfo.jsp?HotelId=<%=id%>" name="neptune" frameborder="no" scrolling="auto" noresize marginwidth="0" marginheight="0" id="neptune">
    <frame src="http://www.hotenavi.com/<%=id%>/roominfo.html" name="jupiter" frameborder="no" scrolling="auto" noresize marginwidth="0" marginheight="0" id="jupiter">
  </frameset>
<body>
<noframes>
</noframes>
</body>
</html>
