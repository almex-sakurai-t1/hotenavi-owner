<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>管理店舗情報ﾒﾆｭｰ</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
</head>
<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">

<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= loginHotelId %>" />
</jsp:include>
<hr class="border">
<div align="center">
<h1 class="title">管理店舗情報</h1>
</div>
<hr class="border">
<%
    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT * FROM owner_user_security WHERE hotelid = ? AND userid = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if( result.next() )
        {
            if( result.getInt("sec_level01") == 1 )
            {
%>
<h1>☆売上情報</h1>
<ul class="link">
<li><a href="<%= response.encodeURL("allstoresales.jsp") %>">本日分</a></li>
<li><a href="<%= response.encodeURL("allstoresales.jsp") %>?Day=-1">前回計上日分</a></li>
<li><a href="<%= response.encodeURL("allstoresalesday.jsp") %>">日付指定</a></li>
<li><a href="<%= response.encodeURL("allstoresales.jsp") %>?Day=0">本月分</a></li>
<li><a href="<%= response.encodeURL("allstoresalesmonth.jsp") %>">年月指定</a></li>
<li><a href="<%= response.encodeURL("allstoresalesrange.jsp") %>">期間指定</a></li>
<li><a href="<%= response.encodeURL("allstoresalesmonthlist.jsp?Month=0") %>">日別売上（本月分）</a></li>
<li><a href="<%= response.encodeURL("allstoresalesmonthlist.jsp") %>">日別売上（年月指定）</a></li>
<%
            }
            if( result.getInt("sec_level02") == 1 )
            {
%>
<br>
<li>
<h1><a href="<%= response.encodeURL("allstoreroomnow.jsp") %>" style="padding: 8px 15px 18px 0px;">☆現在部屋情報</a></h1>
</li>
<br>
<h1>☆部屋情報履歴</h1>
<li><a href="<%= response.encodeURL("allstoreroomday.jsp") %>">日付指定</a></li>

<h2>[本日分(1時間おき)]</h2>
<li><a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=3">3店舗単位</a></li>
<li><a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=5">5店舗単位</a></li>
<li><a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=99">全店舗表示</a></li>
<h2>(店舗数/時間指定)</h2>
</ul>
<hr class="border">
<div class="form" align="center">
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
<input type="submit" value="本日分指定表示" id="button2">
</form>
</div>
<%
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }
%>
</ul>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
