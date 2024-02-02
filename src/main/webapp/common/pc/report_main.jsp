<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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

<%
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    String param_store = request.getParameter("Store");
    if( param_store != null )
    {
        if( !CheckString.hotenaviIdCheck(param_store) )
	    {
            response.sendError(400);
            return;
        }
        selecthotel = param_store;
    }

    // セッション属性に選択ホテルをセットする
    session.setAttribute("SelectHotel", selecthotel);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>アクセスレポート</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/room.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('../../common/pc/image/yajirushiGrey_f2.gif')">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="36" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td height="1" class="tab" nowrap>&nbsp;</td>
              <td height="1" valign="bottom">&nbsp;</td>
              <td height="1">&nbsp;</td>
            </tr>
            <tr>
              <td width="140" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF">帳票管理</font></td>
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
        <td align="left" valign="top" bgcolor="#D5D8CB"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="4"><img src="../../common/pc/image/spacer.gif" width="200" height="12"></td>
            </tr>
            <tr>
              <td colspan="4"><img src="../../common/pc/image/spacer.gif" width="200" height="30"></td>
            </tr>
            <tr>
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td width="378"><div class="size12">日付を選択してください。
			  </div>
              </td>
              <td width="12"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
              <td><div class="size12">月を選択してください。</div>
              </td>
            </tr>
            <tr>
              <td colspan="4"><img src="../../common/pc/image/spacer.gif" width="200" height="12"></td>
            </tr>
          </table>
            <table border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr align="left">
                <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="80"></td>
                <td valign="top">
		  <!-- カレンダー表示 -->
		  <jsp:include page="report_selectday.jsp" flush="true" />
		  <!-- カレンダー表示 -->
		</td>
                <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="80"></td>
                <td valign="top">
		  <!-- 年カレンダー表示 -->
		  <jsp:include page="report_selectmonth.jsp" flush="true" />
		  <!-- 年カレンダー表示 -->
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
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
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="96" height="15" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
