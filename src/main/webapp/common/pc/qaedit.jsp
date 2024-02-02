<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/qaedit_ini.jsp" %>

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String selecthotel = ReplaceString.getParameter(request,"HotelId");
	if( selecthotel != null && !CheckString.hotenaviIdCheck(selecthotel))
	{
		selecthotel="0";
%>
		<script type="text/javascript">
		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
    if    (selecthotel == null) selecthotel = "";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>HPレポート</title>
</head>
<frameset cols="300,*" frameborder="no" border="0" framespacing="0">
  <frame src="qaedit_menu.jsp?Year=<%= Year %>&Month=<%= Month %>&Day=<%= Day %>"  name="menu" frameborder="no" scrolling="auto" noresize marginwidth="0" marginheight="0" id="menu">
  <frame src="qaedit_view.jsp?Year=<%= Year %>&Month=<%= Month %>&Day=<%= Day %>" name="contents" frameborder="no" scrolling="auto" noresize marginwidth="0" marginheight="0" id="contents">
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
