<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<%
    boolean firstView    =  false;
    String  paramReferer =  request.getHeader("Referer");
    if (paramReferer != null)
    {
        if (paramReferer.indexOf("ownerindex.jsp") != -1 || paramReferer.indexOf("ownermenu.jsp") != -1 || paramReferer.indexOf("page.html") != -1 || paramReferer.indexOf("page.jsp") != -1)
        {
            firstView    = true;   //ログイン時に表示
        }
    }
%>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
  <td valign="top">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr valign="top">
        <td bgcolor="#E2D8CF" valign="top">
          <img src="../../common/pc/image/spacer.gif" width="3" height="3">
        </td>
        <td width="3"valign="top" align="left">
          <img src="../../common/pc/image/tab_kado.gif" width="3" height="3">
        </td>
      </tr>
      <%-- パスワード変更処理 --%>
      <jsp:include page="passwd_change.jsp" flush="true" />
      <%-- パスワード変更処理ここまで--%>
<%
    if (firstView)
    {
%>
      <%-- メッセージ表示 --%>
      <jsp:include page="info_message.jsp" flush="true" />
      <%-- メッセージ表示ここまで--%>
      <%-- ユーザー表示 --%>
      <jsp:include page="info_user.jsp" flush="true" />
      <%-- ユーザー表示ここまで--%>
<%
    }
%>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="3" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
    </table>
    </td>
  </tr>

  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle">
      <img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex inc . All Rights Reserved.<!-- #EndLibraryItem -->
    </td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
