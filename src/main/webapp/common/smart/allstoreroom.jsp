<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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

<%
    String param_count = request.getParameter("count");
    if( param_count == null )
    {
        param_count = "3";
    }
    String param_tmcount = request.getParameter("tmcount");
    if( param_tmcount == null )
    {
        param_tmcount = "1";
    }
    String day   = request.getParameter("Day");
    String month = request.getParameter("Month");
    String year  = request.getParameter("Year");
    String[] dates = new String[]{ year, month, day, param_count, param_tmcount };
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
<title>管理店舗情報(部屋)<%if(day==null){%>本日分<%}else{%><%=year%>/<%=month%><%if(!day.equals("0")){%>/<%=day%><%}%>分<%}%></title>
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

<a name="Top"></a>
<h2>管理店舗情報(部屋)
<font size="-1"><a href="#DateSelect">[日付選択]</a></font></h2>
<hr class="border">
<%-- 管理店舗部屋履歴情報取得 --%>
<jsp:include page="allstoreroomget.jsp" flush="true" />
<%-- 管理店舗部屋履歴情報取得ここまで --%>

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
<%-- 管理店舗部屋履歴情報表示 --%>
<jsp:include page="allstoreroomdispdata.jsp" flush="true" />
<%-- 管理店舗部屋履歴情報表示ここまで --%>
<%
	}
%>

<hr class="border">
<a name="DateSelect"></a>
<%-- 日付選択画面表示 --%>
<jsp:include page="allstoreroomselectday.jsp" flush="true" />
<%-- 日付選択画面表示ここまで --%>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
