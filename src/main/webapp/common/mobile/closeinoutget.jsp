<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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

    hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
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
