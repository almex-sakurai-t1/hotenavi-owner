<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%@ include file="../csrf/checkCsrfForPost.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String loginHotelId =  (String)session.getAttribute("LoginHotelId");
    String  userid = request.getParameter("UserId");
    if( userid == null )
    {
        userid = "";
    }
    String Type = request.getParameter("Type");
    if( Type   == null )
    {
        Type = "hotenavi";
    }
    if( !CheckString.alphabetCheck(Type) )
    {
        response.sendError(400);
        return;
    }
   String EDIT = request.getParameter("EDIT");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>ログイン履歴</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/user_datacheck.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/jquery-latest.js"></script>
<script>
    $(document).ready(function () {
        window.history.pushState("", "", "#");
    });
    window.addEventListener("popstate", function (e) {
        location.href = "../../common/pc/owner_userNew.jsp"; 
    })
</script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">ユーザ管理</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td bgcolor="#E2D8CF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="40" height="12"></td>
              <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"><input type=button onclick="history.back(); " value="戻る"></td>
              <td align="left" valign="top">						
				</td>
              <td width="8" valign="top">	<img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
              <%-- ログイン履歴表示 --%>
                <jsp:include page="owner_user_loginhistory_disp.jsp" flush="true" />
             <%-- ログイン履歴表示ここまで--%>
            <tr>
              <td height="4" valign="top">&nbsp;</td>
              <td align="left" valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
            </tr>
          </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
