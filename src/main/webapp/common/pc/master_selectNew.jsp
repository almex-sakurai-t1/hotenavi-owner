<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    String     selecthotel   = "";
    selecthotel = request.getParameter("Store");
    if  (selecthotel == null)
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
    }
    if  (selecthotel == null || selecthotel.equals(""))
    {
        selecthotel = "all";
    }
    if(!CheckString.numAlphaCheck(selecthotel))
    {
        response.sendError(400);
        return;
    }
    int storecount = Integer.parseInt((String)session.getAttribute("StoreCount"));

    String     query              = "";

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    //  店舗数のチェック
    try
    {
        query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND owner_user_hotel.userid = ?";
        query = query + " AND hotel.plan <= 2";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.split_flag=0";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginHotelId );
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            storecount   = result.getInt(1);
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title>設定店舗選択</title>
<script type="text/javascript" src="../../common/pc/scripts/report_datacheck.js"></script>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" <% if(storecount > 1){%>onload="if(document.selectstore.Store.value !='<%=selecthotel%>'){document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';}"<%}%>>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr valign="middle">

          <!-- 店舗選択表示 -->
    <jsp:include page="../../common/pc/selectstore.jsp" flush="true">
       <jsp:param name="URL" value="master_select" />
       <jsp:param name="StoreCount" value="<%=storecount%>" />
    </jsp:include>
          <!-- 店舗選択表示ここまで -->


        </tr>
      </table>
    </td>
    <td width="80" align="left" valign="middle" nowrap bgcolor="#000000">
      <div class="white12" align="center">設定メニュー</div>
    </td>
    <td width="100%" align="left" valign="middle" nowrap class="size12" bgcolor="#FFFFFF">
      <a href="master_target_f.jsp" <%if (storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;売上目標設定</a>
    </td>
  </tr>
</table>
</body>
</html>
