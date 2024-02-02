<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
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
<title>ManageEye店舗選択</title>
<script type="text/javascript" src="scripts/report_datacheck.js"></script>
<link href="style/contents.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#666666" background="image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr valign="middle">

          <!-- 店舗選択表示 -->
          <jsp:include page="manageeye_selectstore.jsp" flush="true" />
          <!-- 店舗選択表示ここまで -->

        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
