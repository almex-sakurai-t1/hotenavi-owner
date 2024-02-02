<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>売上情報ﾒﾆｭｰ</title>
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
<jsp:include page="header_hotelname.jsp" flush="true" />

<hr class="border">
<%
    if (HotelIdCheck)
    {
%>
<ul class="link">
<h1 class="title">売上情報</h1>
<li><a href="<%= response.encodeURL("salesdisp.jsp?HotelId="+ hotelid) %>">本日分</a></li>
<li><a href="<%= response.encodeURL("salesdisp.jsp?HotelId="+ hotelid + "&Day=-1") %>">前回計上日分</a></li>
<li><a href="<%= response.encodeURL("salesday.jsp?HotelId="+ hotelid) %>">日付指定</a></li>
<li><a href="<%= response.encodeURL("salesdisp.jsp?HotelId="+ hotelid + "&Day=0") %>">本月分</a></li>
<li><a href="<%= response.encodeURL("salesmonth.jsp?HotelId="+ hotelid) %>">年月指定</a></li>
<li><a href="<%= response.encodeURL("salesrange.jsp?HotelId=" + hotelid) %>">期間指定</a></li>
<h1 class="title">日別売上情報<br></h1>
<li><a href="<%= response.encodeURL("salesmonthlistdisp.jsp?HotelId=" + hotelid) %>">月次本月分</a></li>
<li><a href="<%= response.encodeURL("salesmonthlist.jsp?HotelId=" + hotelid) %>">月次年月指定</a></li>
<li><a href="<%= response.encodeURL("salesrangelist.jsp?HotelId=" + hotelid) %>">期間指定</a></li>
<%
    }
    else
    {
%>
管理店舗ではありません。
<%
    }
%>
</ul>

<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
