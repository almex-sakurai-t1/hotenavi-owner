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

    // �p�����^�Z�b�g
    hotelid = request.getParameter("HotelId");
    // �z�e��ID�̊m�F
    if( hotelid != null )
    {
        ownerinfo.HotelId = hotelid;
    }
    String roomcode = request.getParameter("rc");

    if( roomcode != null )
    {
        ownerinfo.RoomCode = Integer.parseInt(roomcode);
    }
    else
    {
        ownerinfo.RoomCode = 0;
    }

    // �d���̑��M
    ownerinfo.sendPacket0110(1, hotelid);
    ownerinfo.sendPacket0114(1, hotelid);
    ownerinfo.sendPacket0120(1, hotelid);
    ownerinfo.sendPacket0128(1, hotelid);
    ownerinfo.sendPacket0130(1, hotelid);
    ownerinfo.sendPacket0132(1, hotelid);
    ownerinfo.sendPacket0134(1, hotelid);
    ownerinfo.sendPacket0136(1, hotelid);
%>
