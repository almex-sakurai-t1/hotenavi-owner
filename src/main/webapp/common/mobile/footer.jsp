<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="com.hotenavi2.common.*" %><jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String    hotelid;
    hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
    int count = Integer.valueOf((String)session.getAttribute("StoreCount")).intValue();
%>
<hr>
<div align="center">
<%
    if( hotelid != null )
    {
%>
<a href="<%= response.encodeURL("ownerindex.jsp?HotelId=" + hotelid) %>">MENU��</a><br>
<%
    }
    if( count > 1 )
    {
%>
<a href="<%= response.encodeURL("storeselect.jsp") %>">�X�ܑI����</a><br>
<%
    }
%>
<a href="<%= response.encodeURL("index.jsp") %>">TOP��</a><br>
</div>
<hr>
<div align="center">
Copyright(C) almex inc. All rights reserved.
</div>
