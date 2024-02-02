<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>管理店舗情報(部屋履歴)日付指定</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
管理店舗部屋履歴情報<br>
<jsp:include page="allstoreroomget.jsp" flush="true" />
<%-- 管理店舗部屋履歴情報取得ここまで --%>
<%-- 日付選択画面表示 --%>
<jsp:include page="allstoreroomselectday.jsp" flush="true" />
<%-- 日付選択画面表示ここまで --%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
