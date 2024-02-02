<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
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
    String loginHotelId = (String)session.getAttribute("HotelId");
    String hotelid;

    boolean HotelIdCheck = true;

    // ホテルIDのセット
    hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        HotelIdCheck = checkHotelId(loginHotelId, hotelid, ownerinfo.DbUserId, 1);
    }
    String day   = request.getParameter("Day");
    String month = request.getParameter("Month");
    String year  = request.getParameter("Year");
    String endyear  = request.getParameter("EndYear");
    String endmonth = request.getParameter("EndMonth");
    String endday   = request.getParameter("EndDay");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>売上情報(<%= StringEscapeUtils.escapeHtml4(hotelid) %>)<%if(day==null){%>本日分<%}else if(day.equals("-1")){%>前回計上日分<%}else if(month == null){%>本月分<%}else{%><%= StringEscapeUtils.escapeHtml4(year) %>/<%= StringEscapeUtils.escapeHtml4(month) %><%if(!day.equals("0")){%>/<%= StringEscapeUtils.escapeHtml4(day) %><%}%><%if( endyear != null && endmonth != null && endday != null){%>~<%= StringEscapeUtils.escapeHtml4(endyear) %>/<%= StringEscapeUtils.escapeHtml4(endmonth) %>/<%= StringEscapeUtils.escapeHtml4(endday) %><%}%>分<%}%></title>
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

<%-- ここで売上を取得します --%>
<jsp:include page="salesget.jsp" flush="true" />
<%-- ここで売上を取得します（ここまで） --%>
<h1>☆売上情報<%if(day==null){%><font size=1>（本日分）</font><%}else if(day.equals("-1")){%><font size=1>（前回計上日分）</font><%}else if(month == null){%><font size=1>（本月分）</font><%}%><br></h1>
<%
        if( ownerinfo.SalesGetStartDate == 0 )
        {
%>
取得できませんでした<br>
<%
        }
        else
        {
%>
<%-- 売上データの表示 --%>
<jsp:include page="salesdispdata_demo.jsp" flush="true"/>
<%-- 売上データの表示ここまで --%>
<%
        }
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
