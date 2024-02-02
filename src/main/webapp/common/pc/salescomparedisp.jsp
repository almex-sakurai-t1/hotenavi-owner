<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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

    //管理店舗一覧からのリンク
    String param_hotelid  = ReplaceString.getParameter(request,"HotelIdfromGroup");
    if(CheckString.isValidParameter(param_hotelid) && !CheckString.numAlphaCheck(param_hotelid))
    {
        response.sendError(400);
        return;
    }
    if (param_hotelid != null)
    {
        storeselect = param_hotelid;
    }

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
<title>売上情報</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
        </tr>
      </table>

      <a name="pagetop"></a>

      <%-- 売上・部屋情報データ取得表示パーツ --%>
      <jsp:include page="salescomparedisp_getdata.jsp" flush="true" />

    </td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10">
      <!-- #BeginLibraryItem "/Library/footer.lbi" -->
      <IMG src="../../common/pc/image/imedia.gif" width="96" height="15" align="absmiddle">
      <IMG src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex inc . All Rights Reserved.
      <!-- #EndLibraryItem -->
    </td> 
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><IMG src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
<%
if( storeselect.compareTo("all") == 0 || storeselect.compareTo("all_manage") == 0)
    {
    // 全店舗を選択
%>
<script>
   parent.selectFrame.location.href = "salescomparedisp_jump.jsp?cnt=<%= cnt %>";
</script>

<%
     }
%>
</BODY>
</html>

