<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>日別売上一覧 （比較）<%= ownerinfo.HotelId %></title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="90" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF">日別売上一覧</font></td>
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
          <td height="3" align="left" valign="top" colspan="3"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="3"><img src="../../common/pc/image/bg.gif" width="3" height="3"></td>
        </tr>
        <form name=form1 method=post action="salescomparedisp_yearlist.jsp">
        <tr> 
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="20"></td>
          <td height="20">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td valign="top">
                  <div class="size14">
                    <font color="#FFFFFF">
                    <%-- 日付表示パーツ --%>
                        <jsp:include page="../../common/pc/salesdisp_yeardisp.jsp" flush="true" />
                    </font>
                  </div>
                </td>
              </tr>
              <tr> 
                <td valign="top">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <%-- 日別売上明細の表示 --%>
                     <jsp:include page="../../common/pc/salesdisp_yearlistdata.jsp" flush="true" />
                  </table>
                </td>
              </tr>
            </table>
          </td>
          <td bgcolor="#A8BEBC"><img src="../../common/pc/image/grey.gif" width="3"></td>
          <td height="20" bgcolor="#A8BEBC">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td valign="top">
                  <div class="size14">
                    <font color="#FFFFFF">
                    <%-- 比較先日付表示パーツ --%>
                        <jsp:include page="../../common/pc/salescomparedisp_yeardisp.jsp" flush="true" />
                    </font>
                  </div>
                </td>
              </tr>
              <tr> 
                <td valign="top">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <%-- 比較日別売上明細の表示 --%>
                     <jsp:include page="../../common/pc/salescomparedisp_yearlistdata.jsp" flush="true" />
                  </table>
                </td>
              </tr>
            </table>
          </td>
          <td width="8" valign="top"  bgcolor="#A8BEBC">&nbsp;</td>
          <td bgcolor="#666666" height="100%" ><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
        </form>
        <tr>
          <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" align="left" valign="top" colspan="2" bgcolor="#A8BEBC"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" valign="top" bgcolor="#A8BEBC"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
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

<table width="70%" border="0" cellspacing="0" cellpadding="0">
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
