<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
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
    String loginHotelId = (String)session.getAttribute("HotelId");
    String hotelid;

    boolean HotelIdCheck = true;

    // ホテルIDのセット
    hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        HotelIdCheck = checkHotelId(loginHotelId, hotelid, ownerinfo.DbUserId, 2);
    }
    String day   = request.getParameter("Day");
    String month = request.getParameter("Month");
    String year  = request.getParameter("Year");
    String[] dates = new String[]{ year, month, day };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>部屋ｽﾃｰﾀｽ遷移(<%= StringEscapeUtils.escapeHtml4(hotelid) %>)<%if(day==null){%>本日分<%}else if(day.equals("-1")){%>前回計上日分<%}else{%><%= StringEscapeUtils.escapeHtml4(year) %>/<%= StringEscapeUtils.escapeHtml4(month) %>/<%= StringEscapeUtils.escapeHtml4(day) %>分<%}%></title>
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
<%-- 部屋ｽﾃｰﾀｽ遷移情報取得 --%>
<jsp:include page="roomhistoryget.jsp" flush="true"/>
<%-- 部屋ｽﾃｰﾀｽ遷移情報取得ここまで --%>
<h1 class="title">部屋ｽﾃｰﾀｽ遷移<%if(day==null){%><font size="-1">（本日分）</font><%}else if(day.equals("-1")){%><font size=1>（前回計上日分）</font><%}%></h1>
<%
        if( ownerinfo.InOutGetStartDate == 0 )
        {
%>
取得できませんでした<br>
<%
        }
        else
        {
%>
<%-- 部屋ｽﾃｰﾀｽ遷移情報表示 --%>
<jsp:include page="roomhistorydispdata.jsp" flush="true" />
<%-- 部屋ｽﾃｰﾀｽ遷移情報表示ここまで --%>
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
