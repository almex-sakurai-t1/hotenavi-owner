<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title>部屋状況選択</title>
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/room_datacheck.js"></script>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#663333" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr valign="middle">

<!--
          <td width="80" align="center" nowrap bgcolor="#000000">
            <div class="white12" align="center">店舗選択</div>
          </td>

-->
          <!-- 店舗選択表示 -->
<%--
          <jsp:include page="roomhistory_selectstore.jsp" flush="true" />
--%>
          <!-- 店舗選択表示ここまで -->
          <td width="80" align="center" nowrap bgcolor="#000000">
            <div class="white12" align="center">日付指定</div>
          </td>
          <!-- 店舗選択表示 -->
          <jsp:include page="roomhistory_selectday.jsp" flush="true" />
          <!-- 店舗選択表示ここまで -->
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
