<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String param_cnt = ReplaceString.getParameter(request,"cnt");
    if( param_cnt == null )
    {
        param_cnt = "0";
    }
    if(!CheckString.numCheck(param_cnt))
    {
        response.sendError(400);
        return;
    }
    int cnt = Integer.parseInt(param_cnt);

    String storeselect = (String)session.getAttribute("SelectHotel");

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
<META http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title></title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/tableSort.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="600px" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="90" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF">管理店舗売上</font></td>
          <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td height="20">
            <div class="size10">
              <font color="#FFFFFF">&nbsp;
              </font>
            </div>
          </td>
        </tr>
      </table></td>
    <td width="3">&nbsp;</td>
  </tr>
  <tr> 
    <td bgcolor="#BBBBBB">
      <table border="0" cellspacing="0" width="100%" cellpadding="0">
        <tr>
          <td height="3" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td height="3" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="3"><img src="../../common/pc/image/bg.gif" width="3" height="3"></td>
        </tr>
        <tr> 
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="20"></td>
          <td height="20">
            <div class="size14">
              <font color="#FFFFFF">
              <%-- 日付表示パーツ --%>
                  <jsp:include page="../../common/pc/salesdisp_datedisp.jsp" flush="true" />
              </font>
            <input type="button" onclick="document.form1.action='../../common/pc/salesdispgroup_csv.jsp';document.form1.submit();" value="CSV" style="float:right">
            </div>
          </td>
          <td width="8" valign="top">&nbsp;</td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3" height="20"></td>
        </tr>
        <tr> 
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="80"></td>
          <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <%-- 売上情報データ取得表示パーツ --%>
              <form name=form1 method=post action="salesdispgroup.jsp">
              <jsp:include page="salesdispgroup_getdata.jsp" flush="true" />
              </form>
            </table>
          </td>
          <td width="8" valign="top">&nbsp;</td>
          <td bgcolor="#666666" height="100%" ><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
        <tr>
          <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3" height="8"></td>
       </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="3" bgcolor="#BBBBBB">
      <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="3"><img src="../../common/pc/image/bg.gif" width="3" height="3"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
          <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
        </tr>
      </table>
    </td>
  </tr>
  
  <!-- ここまで -->
</table>

<table width="500px" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="340" height="8"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10">Copyrights&copy; almex inc.
    All Rights Reserved.</td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="340" height="12"></td>
  </tr>
</table>
</body>
</html>
