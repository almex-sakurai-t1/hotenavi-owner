<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String     selecthotel   = (String)session.getAttribute("SelectHotel");
    if  (selecthotel == null)
    {
        selecthotel = "all_manage";
    }
%><frameset cols="200,*" framespacing="0" frameborder="no" border="0">
    <frame src="sales_select.jsp" name="selectFrame" frameborder="no" scrolling="auto" marginwidth="0" marginheight="0" id="select" noresize>
<% 
    if (selecthotel.equals("all_manage"))
    {
%>
  <frame src="sales_page.html" name="mainFrame" frameborder="no" scrolling="auto" marginwidth="0" marginheight="0" id="sales" noresize>
<%
    }
    else
    {
%>
  <frame src="sales_wait.html" name="mainFrame" frameborder="no" scrolling="auto" marginwidth="0" marginheight="0" id="sales" noresize>
<%
    }
%>
</frameset>
