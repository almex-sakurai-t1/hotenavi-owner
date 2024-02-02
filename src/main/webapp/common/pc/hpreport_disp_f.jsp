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
<%
    String hotelid = (String)session.getAttribute("SelectHotel");
    String year  = ReplaceString.getParameter(request,"Year");
    String month = ReplaceString.getParameter(request,"Month");

    if( !CheckString.numCheck(year) || !CheckString.numCheck(month) )
    {
        year = "0";
        month = "0";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>アクセスレポートダウンロード</title>
</head>

<frameset rows="104,*" frameborder="NO" border="0" framespacing="0">
<%
    if( month == null )
    {
%>
  <frame src="hpreport_dispselect_year.jsp?Year=<%= year %>" name="topFrame" frameborder="no" scrolling="NO" noresize marginwidth="0" marginheight="0" >
<%
    }
    else
    {
%>
  <frame src="hpreport_dispselect_month.jsp?Year=<%= year %>&Month=<%= month %>" name="topFrame" frameborder="no" scrolling="NO" noresize marginwidth="0" marginheight="0" >
<%
    }
%>
  <frame src="page.html" name="mainFrame" frameborder="no" scrolling="auto" marginwidth="0" marginheight="0">
</frameset>
<noframes><body>

</body></noframes>
</html>
