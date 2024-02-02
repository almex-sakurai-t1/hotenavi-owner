<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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

<%
    String loginHotelId =  (String)session.getAttribute("HotelId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>管理店舗情報ﾒﾆｭｰ</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= loginHotelId %>" />
</jsp:include>
<div align="center">
管理店舗情報
</div>
<%
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT * FROM owner_user_security WHERE hotelid = ?  AND userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result      = prestate.executeQuery();
        if (result != null)
        {
            if(result.next())
            {
                if( result.getInt("sec_level01") == 1 )
                {
%>
<hr>
☆売上情報<br>
<a href="<%= response.encodeURL("allstoresales.jsp") %>">本日分</a><br>
<a href="<%= response.encodeURL("allstoresales.jsp") %>?Day=-1">前回計上日分</a><br>
<a href="<%= response.encodeURL("allstoresalesday.jsp") %>">日付指定</a><br>
<a href="<%= response.encodeURL("allstoresales.jsp") %>?Day=0">本月分</a><br>
<a href="<%= response.encodeURL("allstoresalesmonth.jsp") %>">年月指定</a><br>
<a href="<%= response.encodeURL("allstoresalesrange.jsp") %>">期間指定</a><br>
<%
                }
                if( result.getInt("sec_level02") == 1 )
                {
%>
<hr>
☆部屋情報履歴<br>
<a href="<%= response.encodeURL("allstoreroomday.jsp") %>">日付指定</a><br>
<br>
[本日分(1時間おき)]<br>
<a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=3">3店舗単位</a><br>
<a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=5">5店舗単位</a><br>
<a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=99">全店舗表示</a><br>
(店舗数/時間指定)<br>
<div align="center">
<form action="<%= response.encodeURL("allstoreroom.jsp") %>" method="post">
<select name="count">
<option value="3" selected>3
<option value="5">5
<option value="99">全て
</select>店舗単位<br>
<select name="tmcount">
<option value="1" selected>1
<option value="2">2
<option value="3">3
</select>時間おき<br>
<input type="submit" value="本日分指定表示">
</form>
</div>
<%
                }
            }
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
