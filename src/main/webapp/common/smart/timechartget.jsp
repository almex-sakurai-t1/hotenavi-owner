<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String    hotelid;

    hotelid = request.getParameter("HotelId");
    if( hotelid != null )
    {
        ownerinfo.HotelId = hotelid;
    }

    // ���݃^�C���`���[�g���擾
    if(!ownerinfo.sendPacket0158())
    {
%>
�擾�ł��܂���ł����B<br>
<%
    }
%>
