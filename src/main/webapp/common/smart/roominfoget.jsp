<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    String    hotelid;

    hotelid = request.getParameter("HotelId");
    if( hotelid != null )
    {
        ownerinfo.HotelId = hotelid;
    }

    // 現在部屋情報取得
    if(!ownerinfo.sendPacket0108())
    {
%>
<b>取得できませんでした。</b><br>
<%
    }
%>
