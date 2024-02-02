<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.jsp" />
<%
    }

    String hotelid = (String)session.getAttribute("SelectHotel");
    boolean TexExist      = true;

//  部屋数の取得
    ownerinfo.RoomCode = 0;
    ownerinfo.sendPacket0114(1, hotelid);
    int RoomCount = ownerinfo.StateRoomCount;
%>

<%-- 部屋詳細データの取得 --%>
<jsp:include page="roomdetail_get.jsp" flush="true" />
<%
    if(ownerinfo.TexSupplyDate[0]==0)           TexExist      = false;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<META http-equiv="Content-Style-Type" content="text/css">
<title>部屋詳細</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/room.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('../../common/pc/image/yajirushiGreyL_f2.gif','../../common/pc/image/yajirushiGrey_f2.gif')">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="6" align="center"><img src="../../common/pc/image/spacer.gif" width="300" height="6"></td>
  </tr>
  <tr>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20">
            <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="120" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">利用状況</font></td>
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
          <jsp:include page="roomdetail_disp.jsp" flush="true" >
            <jsp:param name="TexExist"     value="<%= TexExist %>" />
            <jsp:param name="RoomCount"    value="<%= RoomCount %>" />
          </jsp:include>
        </tr>
      <!-- ここまで -->
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
      </table>
    </td>
<!--
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
-->
  </tr>
<!--
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
-->
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
    <td align="center" valign="middle" class="size10"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>


