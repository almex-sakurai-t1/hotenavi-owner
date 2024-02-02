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
    int    ymd;

    String    hotelid;

    hotelid = request.getParameter("HotelId");
    if( hotelid != null )
    {
        ownerinfo.HotelId = hotelid;
    }

    // 本日の締単位IN/OUT組数取得のため日付を０にする
    ownerinfo.AddupInOutGetDate = 0;

    // 締単位IN/OUT組数取得
    if(!ownerinfo.sendPacket0112())
    {
%>
取得できませんでした。<br>
<%
    }
%>
