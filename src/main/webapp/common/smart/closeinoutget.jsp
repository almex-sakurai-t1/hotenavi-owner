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

    // �{���̒��P��IN/OUT�g���擾�̂��ߓ��t���O�ɂ���
    ownerinfo.AddupInOutGetDate = 0;

    // ���P��IN/OUT�g���擾
    if(!ownerinfo.sendPacket0112())
    {
%>
�擾�ł��܂���ł����B<br>
<%
    }
%>
