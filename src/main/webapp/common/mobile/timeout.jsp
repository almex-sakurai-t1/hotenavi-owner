<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="com.hotenavi2.common.*" %>
<%
//    String hotelid;
//    hotelid = ReplaceString.getParameter(request,"id");
    String requestUri = request.getRequestURI();
    String hotelid    = requestUri.replace("_debug_","");
    hotelid           = hotelid.replace("i/timeout.jsp","");
    hotelid           = hotelid.replace("ez/timeout.jsp","");
    hotelid           = hotelid.replace("j/timeout.jsp","");
    hotelid           = hotelid.replace("m/timeout.jsp","");
    hotelid           = hotelid.replace("/","");
    if (hotelid.indexOf(";jsessionid") != -1)
    {
        hotelid = hotelid.substring(0,hotelid.indexOf(";jsessionid"));
    }

    String paramJSP   = ReplaceString.getParameter(request,"JSP");
    String paramPARAM = ReplaceString.getParameter(request,"PARAM");
    String refererURL = "";
    if     (paramJSP == null)
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>۸޲���ѱ��</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
۸޲݂���ѱ�Ă��܂����B<br>
<hr>
<%=hotelid%>
��Ű���۸޲�<br>

<form action="<%= response.encodeURL("login.jsp") %>" method="post" utn>
  <div align="center">
    <input type="hidden"     name="HotelId"  value="<%= hotelid %>">
    <input type="password"   name="Password" size="8" maxlength="8" istyle="4">
    <input type="hidden"     name="refererURL" value="<%=refererURL%>"><br>

    ��۸޲����݂���������[��ID�̑��M�m�F��ʂ��\������܂��̂ŁA���M�����I�����Ă��������B<br>
    <input type="submit" value="���O�C��">
  </div>
</form>
<hr>
<div align="center">
<a href="<%= response.encodeURL("index.jsp") %>">TOP��</a><br>
</div>
<hr>
<div align="center">
Copyright(C) almex inc. All rights reserved.
</div>
</body>
</html>
