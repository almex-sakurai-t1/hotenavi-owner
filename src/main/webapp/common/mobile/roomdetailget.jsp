<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid != null )
    {
        ownerinfo.HotelId = hotelid;
    }

    // ���ݕ����ڍ׏��擾
    if(!ownerinfo.sendPacket0110())
    {
%>
�擾�ł��܂���ł����B<br>
<%
    }
%>
