<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page errorPage="error.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>
<%@ include file="checkHotelId.jsp" %>
<%
    String loginHotelId =  (String)session.getAttribute("HotelId");
    String hotelid;

    boolean    HotelIdCheck = true;

    // ホテルIDのセット
    hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
        {
            response.sendError(400);
            return;
        }
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,2);
    }
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>締単位IN/OUT組数(<%=hotelid%>)</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
%>
☆締単位IN/OUT組数<br>
<%-- ここで締単位IN/OUT組数を取得します --%>
<jsp:include page="closeinoutget.jsp" flush="true" />
<%-- ここで締単位IN/OUT組数を取得します（ここまで） --%>
<%-- 締単位IN/OUT組数データの表示 --%>
<jsp:include page="closeinoutdispdata.jsp" flush="true" />
<%-- 締単位IN/OUT組数データの表示ここまで --%>
<%
    }
    else
    {
%>
管理店舗ではありません。
<%
    }
%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
