<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
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

    // パラメタセット
    hotelid = ReplaceString.getParameter(request,"HotelId");
    // ホテルIDの確認
    if( hotelid != null )
    {
        ownerinfo.HotelId = hotelid;
    }
    String roomcode = ReplaceString.getParameter(request,"rc");

    if( roomcode != null )
    {
        ownerinfo.RoomCode = Integer.parseInt(roomcode);
    }
    else
    {
        ownerinfo.RoomCode = 0;
    }

    // 電文の送信
    ownerinfo.sendPacket0110(1, hotelid);
    ownerinfo.sendPacket0114(1, hotelid);
    ownerinfo.sendPacket0120(1, hotelid);
    ownerinfo.sendPacket0128(1, hotelid);
    ownerinfo.sendPacket0130(1, hotelid);
    ownerinfo.sendPacket0132(1, hotelid);
    ownerinfo.sendPacket0134(1, hotelid);
    ownerinfo.sendPacket0136(1, hotelid);
%>
