<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
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
    NumberFormat    nf;
    nf = new DecimalFormat("00");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>管理店舗情報(売上)期間指定</title>
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
<h2>☆管理店舗売上情報</h2>
<jsp:include page="allstoresalesget.jsp" flush="true" />
<%-- 管理店舗売上情報取得ここまで --%>
<%
    // 現在日付
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);

    // 計上日付
    int addup_date  = ownerinfo.Addupdate;
    if (addup_date == 0)
    {
        addup_date = now_date;
    }
%>
<form name="form1" action="<%= response.encodeURL("allstoresales.jsp") %>" method="post">
<input type="hidden" name="Year" value="<%= addup_date/10000%>">
<input type="hidden" name="Month" value="<%= addup_date/100%100%>">
<input type="hidden" name="Day" value="<%= addup_date%100%>">
<input type="hidden" name="EndYear" value="<%=addup_date/10000%>">
<input type="hidden" name="EndMonth" value="<%= addup_date/100%100%>">
<input type="hidden" name="EndDay" value="<%= addup_date%100%>">
<%-- 日付選択画面表示 --%>
<jsp:include page="allstoreselectrange.jsp" flush="true" />
<%-- 日付選択画面表示ここまで --%>
</form>
<hr class="border">

<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
