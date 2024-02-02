<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String loginhotel = (String)session.getAttribute("LoginHotelId");

    DbAccess db =  new DbAccess();
    // セキュリティ情報の取得
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db, loginhotel, ownerinfo.DbUserId);

    // セキュリティチェック
    if( DbUserSecurity == null )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.getInt("admin_flag") == 0 )
    {
%>
<jsp:include page="owner_user_formNew.jsp" flush="true" >
    <jsp:param name="UserId" value="<%=ownerinfo.DbUserId%>" />
</jsp:include>
<%
    }
    else
    {
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>マスター設定</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
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
              <td height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td align="left" valign="top" class="size12">
                <div align="right">
                <input name="Submit" type="submit" onClick="MM_goToURL('parent.frames[\'mainFrame\']','user_form.jsp?UserId=0');return document.MM_returnValue" value="新規作成する">
                </div>
                <font color="#000066">★現在の登録内容</font>
              </td>
              <td valign="top">&nbsp;</td>
            </tr>
            <tr>
              <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td height="8" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
            </tr>
            <tr>
              <td width="8" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="100"></td>
              <td align="left" valign="top">

                <%-- ユーザ一覧表示 --%>
                <jsp:include page="owner_user_dispNew.jsp" flush="true" />
                <%-- ユーザ一覧表示ここまで--%>

              </td>
              <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
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
<%
    }
    db.close();
%>
