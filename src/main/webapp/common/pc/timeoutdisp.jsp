<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="com.hotenavi2.common.*" %>
<%
   String loginHotelId = ReplaceString.getParameter(request,"hotelid");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<META HTTP-EQUIV="Expires" CONTENT="Mon, 04 Dec 1999 21:29:02 GMT"> 
<meta http-equiv="Pragma" content="no-cache">
<title>ログインタイムアウト</title>
<script type="text/javascript">
<!--
    setTimeout("window.open('../../<%=loginHotelId%>/pc/index.jsp','_top')",5000);
//-->
</SCRIPT>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20">
            <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="180" height="20" nowrap bgcolor="#FF0000" class="tab"><p><font color="#FFFFFF"><strong>ログインタイムアウト</strong></font></p></td>
                <td width="15" height="20"><IMG src="../../common/pc/image/tab_2.gif" width="15" height="20"></td>
                <td height="20"><div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div></td>
              </tr>
            </table>
          </td>
          <td width="3">&nbsp;</td>
        </tr>
      <!-- ここから表 -->
        <tr>
          <td align="center" valign="top" bgcolor="#FFFFFF">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
              </tr>
              <tr>
                <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                <td valign="top">
                  <div class="size12">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td colspan="2">
                          <font color="#CC0000">
                            <strong>
                              ログインタイムアウトまたはログアウトしました。再度ログインしなおしてください。<br>
                              <br>
                              ５秒後にログイン画面に戻ります。<br>
                            </strong>
                          </font>
                        </td>
                        <td align="right">&nbsp;</td>
                      </tr>
                    </table>
                  </div>
                </td>
              </tr>
              <tr>
                <td colspan="2"><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
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
      </table>
    </td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10">
      <!-- #BeginLibraryItem "/Library/footer.lbi" -->
      <img src="../../common/pc/image/imedia.gif" width="96" height="15" align="absmiddle">
      <img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex inc . All Rights Reserved.
      <!-- #EndLibraryItem -->
    </td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>

