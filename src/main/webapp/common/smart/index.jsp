<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String requestUri = request.getRequestURI();
    String hotelid    = requestUri.replace("_debug_","");
    hotelid           = hotelid.replace("i/index.jsp","");
    hotelid           = hotelid.replace("ez/index.jsp","");
    hotelid           = hotelid.replace("j/index.jsp","");
    hotelid           = hotelid.replace("/","");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>��Ű���۸޲�</title>
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
<!-- #include virtual="/mainte.html" --> 
<%= StringEscapeUtils.escapeHtml4(hotelid) %>
��Ű���۸޲�<br>

<form action="<%= response.encodeURL("login.jsp") %>" method="post" utn>
  <div align="center">
    <input type="hidden" name="HotelId" value="<%= StringEscapeUtils.escapeHtml4(hotelid) %>">
    <input type="password" name="Password" size="8" maxlength="8" istyle="4" <%if(hotelid.equals("demo")){%>value="1111"<%}%>><br>

    ��۸޲����݂���������[��ID�̑��M�m�F��ʂ��\������܂��̂ŁA���M�����I�����Ă��������B<br>
    <input type="submit" value="���O�C��">
  </div>
<%
    if (hotelid.equals("demo"))
    {
%>
�߽ܰ�ނ�ύX�����ɂ��̂܂�۸޲݂�د����Ă��������B
<%
    }
%>
</form>

<hr class="border">
<div align="center">
Copyright(C) almex inc. All rights reserved.
</div>

</body>
</html>
