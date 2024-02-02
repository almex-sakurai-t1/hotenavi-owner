<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String requestUri = request.getRequestURI();
    boolean DebugMode = false;
    if (requestUri.indexOf("_debug_") != -1)
    {
       DebugMode = true;
    }
    String hotelid = requestUri.replace("_debug_","");
    hotelid = hotelid.replace("i/timeout.jsp","");
    hotelid = hotelid.replace("ez/timeout.jsp","");
    hotelid = hotelid.replace("j/timeout.jsp","");
    hotelid = hotelid.replace("m/timeout.jsp","");
    hotelid = hotelid.replace("smart/timeout.jsp","");
    hotelid = hotelid.replace("/","");
    if (hotelid.indexOf(";jsessionid") != -1)
    {
        hotelid = hotelid.substring(0,hotelid.indexOf(";jsessionid"));
    }

    String paramJSP   = request.getParameter("JSP");
    String paramPARAM = request.getParameter("PARAM");
    String refererURL = "";
    if (paramJSP == null)
    {
       refererURL = "index.jsp";
    }
    else
    {
       refererURL = paramJSP;
    }
    if (paramPARAM != null)
    {
        if (!paramPARAM.equals("null"))
        {
            refererURL = refererURL + "?" + paramPARAM;
        }
    }
%>
<%@ include file="CheckCookie.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ﾛｸﾞｲﾝﾀｲﾑｱｳﾄ</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css"></head>

<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">
<h1 class="border">オーナーサイトログイン</h1>

<div class="form" align="center">
<form action="<%= response.encodeURL("login.jsp") %>" method="post">
<input type="hidden" name="refererURL" value="<%= StringEscapeUtils.escapeHtml4(refererURL) %>"><br>
<%@ include file="../../common/smart/header.jsp" %>


<center>
<h2>タイムアウトしました。</h2>

<%
    if(!loginNidSave.equals("1"))
    {
%>
ネットワークユーザー名:<br>
<input name="nid" id="text" type="text" size="20" maxlength="20" <% if (hotelid.equals("demo")&&!DebugMode){%>value="net"<%}%>><br>
パスワード:<br>
<INPUT name="npasswd" id="text2" type="password" size="20" maxlength="20" class="nyuryoku" <% if (hotelid.equals("demo")&&!DebugMode){%>value="1111"<%}%>><br>
<input type="checkbox" name="nidsave" value="1"/>保存する<br><br>
<%
    }
%>
個人ユーザー名:<br>
<%
    if(loginLidSave.equals("1"))
    {
%>
<input name="LoginId" id="text" type="text" size="20" maxlength="20" value="<%= loginLid %>"><br>
<%
    }
    else
    {
%>
<input name="LoginId" id="text" type="text" size="20" maxlength="20" <% if (hotelid.equals("demo")&&!DebugMode){%>value="demo"<%}%>><br>
<input type="checkbox" name="lidsave" value="1"/>保存する<br><br>
<%
    }
%>
パスワード:<br>
<INPUT name="Password" id="text2" type="password" size="20" maxlength="20" class="nyuryoku" <%if (hotelid.equals("demo") && !DebugMode) {%>value="1111"<%}%>><br>
<input type="submit" value="ログイン" id="button">
<%
    if (hotelid.equals("demo"))
    {
%>
パスワードを変更せずにそのままログインをクリックしてください。
<%
    }
%>
</form>
</div>

<hr class="border">
<div align="center">
<a href="index.jsp">TOPへ</a><br>
</div>
<hr class="border">
<div align="center" class="copyright">
Copyright(C) almex inc. All rights reserved.
</div>
</body>
</html>
