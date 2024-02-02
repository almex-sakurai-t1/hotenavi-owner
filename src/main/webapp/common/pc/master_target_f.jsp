<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>無題ドキュメント</title>
</head>

<frameset rows="24,*" framespacing="0" frameborder="no" border="0">
  <frame src="master_target_selectmonth.jsp" name="selectFrame" frameborder="no" scrolling="no" noresize marginwidth="0" marginheight="0" id="select">
  <frame src="page.html" name="targetFrame" frameborder="no" scrolling="auto" marginwidth="0" marginheight="0" id="chohyo">
</frameset>
<noframes><body>

</body></noframes>
</html>
