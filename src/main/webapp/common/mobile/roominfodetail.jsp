<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
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
    hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,2);
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>部屋詳細情報(<%=hotelid%>)</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
%>
<%-- ここで部屋詳細情報を取得します --%>
<jsp:include page="roominfodetailget.jsp" flush="true" />
<%-- ここで部屋詳細情報を取得します（ここまで） --%>
☆部屋詳細情報<br>
<%-- 部屋詳細情報データの表示 --%>
<jsp:include page="roominfodetaildispdata.jsp" flush="true" />
<%-- 部屋詳細情報データの表示ここまで --%>
<%
    }
    else
    {
%>
管理店舗ではありません。
<%
    }
%>
<jsp:include page="/common/mobile/footer.jsp" flush="true" />
</body>
</html>
