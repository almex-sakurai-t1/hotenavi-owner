<%@ page contentType="text/html; charset=Windows-31J" %>
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
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,1);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>売上情報ﾒﾆｭｰ</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
%>
☆売上情報<br>
<a href="<%= response.encodeURL("salesdisp.jsp?HotelId="+ hotelid) %>">本日分</a><br>
<a href="<%= response.encodeURL("salesdisp.jsp?HotelId="+ hotelid + "&Day=-1") %>">前回計上日分</a><br>
<a href="<%= response.encodeURL("salesday.jsp?HotelId="+ hotelid) %>">日付指定</a><br>
<a href="<%= response.encodeURL("salesdisp.jsp?HotelId="+ hotelid + "&Day=0") %>">本月分</a><br>
<a href="<%= response.encodeURL("salesmonth.jsp?HotelId="+ hotelid) %>">年月指定</a><br>
<a href="<%= response.encodeURL("salesrange.jsp?HotelId=" + hotelid) %>">期間指定</a><br>
☆日別売上情報<br>
<a href="<%= response.encodeURL("salesmonthlistdisp.jsp?HotelId=" + hotelid) %>">月次本月分</a><br>
<a href="<%= response.encodeURL("salesmonthlist.jsp?HotelId=" + hotelid) %>">月次年月指定</a><br>
<a href="<%= response.encodeURL("salesrangelist.jsp?HotelId=" + hotelid) %>">期間指定</a><br>
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
