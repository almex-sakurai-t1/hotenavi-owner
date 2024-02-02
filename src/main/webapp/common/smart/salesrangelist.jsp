<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    NumberFormat nf = new DecimalFormat("00");
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
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>期間指定日別売上(<%= StringEscapeUtils.escapeHtml4(hotelid) %>)期間指定</title>
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
<jsp:include page="salesget.jsp" flush="true" />
<hr class="border">
<%
    if (HotelIdCheck)
    {
        if (ownerinfo.Addupdate == 0)
        {
            ownerinfo.sendPacket0100(1, hotelid);
        }
%>
<%-- 期間指定日別売上選択 --%>
<a name="monthly"></a>
<h1>☆期間指定日別売上</h1>
<%-- 日付選択アイテムを表示します --%>
<jsp:include page="salesselectrange.jsp" flush="true">
  <jsp:param name="Contents" value="salesrangelistdisp" />
</jsp:include>
<%-- 日付選択アイテムを表示します（ここまで） --%>
<%-- 月計売上選択End --%>
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
