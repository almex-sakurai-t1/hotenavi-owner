<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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
    NumberFormat nf = new DecimalFormat("00");
    DateEdit df = new DateEdit();
    String now_date = df.getDate(1);
    String now_time = df.getTime(2);

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection        = DBConnection.getConnection();

    int          imedia_user      = 0;
    int          level            = 0;
    String       query            = "";

     // imedia_user のチェック
    try
    {
       query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    // セッション属性より選択されたホテルを取得
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
    //管理店舗一覧からのリンク
    String param_hotelid  = request.getParameter("HotelId");
    if (param_hotelid != null)
    {
		if(!CheckString.hotenaviIdCheck(param_hotelid))
		{
            response.sendError(400);
            return;
		}
        if (imedia_user == 1 && level == 3)
        {
            selecthotel = param_hotelid;
        }
        else
        {
            query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ?";
            query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND hotel.plan <= 2";
            query = query + " AND hotel.plan >= 1";
            query = query + " AND owner_user_hotel.userid = ?";
            query = query + " AND owner_user_hotel.accept_hotelid=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
            prestate.setString(3, param_hotelid);
            result      = prestate.executeQuery();
            if(result.next() != false)//管理ホテルのチェック
            {
                selecthotel = param_hotelid;
            }
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }
    DBConnection.releaseResources(connection);

    ownerinfo.RoomCode = 0;
    ownerinfo.sendPacket0110(1, selecthotel);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>部屋詳細</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="90" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF">部屋詳細</font></td>
          <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
          <td height="20">
            <div class="size10">
              <font color="#FFFFFF">
              <%= now_date %>&nbsp;<%= now_time %>現在
              </font>
            </div>
          </td>
        </tr>
      </table></td>
    <td width="3">&nbsp;</td>
  </tr>
  
  <!-- ここから表 -->
  <tr> 
    <td bgcolor="#999999">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="8" align="left" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" align="left" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="8"></td>
          <td width="8" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="8"></td>
        </tr>
        <tr> 
          <td width="8" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="80"></td>
          <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td align="center" valign="middle" nowrap class="tableLN">部屋番号</td>
              <td align="center" valign="middle" nowrap class="tableRN">ステータス</td>
              <td align="center" valign="middle" nowrap class="tableLN2">経過時間</td>
            </tr>
<%
    for( int i = 0 ; i < ownerinfo.StatusDetailCount ; i++ )
    {
%>
            <tr>
              <td align="center" valign="middle" class="tableLW" nowrap><%= ownerinfo.StatusDetailRoomName[i] %></td>
              <td align="center" valign="middle" class="tableLW" nowrap><%= ownerinfo.StatusDetailStatusName[i] %></td>
              <td align="center" valign="middle" class="tableRW" nowrap><%= ownerinfo.StatusDetailElapseTime[i] / 60 %>:<%= nf.format(ownerinfo.StatusDetailElapseTime[i] % 60) %></td>
            </tr>
<%
    }
%>
          </table>
          </td>
          <td width="8" valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td height="8" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" align="left" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="8"></td>
        </tr>
      </table>
    </td>
      <td width="3" valign="top" align="left" height="100%">
        <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td><img src="../image/tab_kado.gif" width="3" height="3"></td>
        </tr>
        <tr> 
          <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
        </tr>
        </table>
      </td>
    </tr>
  <tr>
    <td height="3" bgcolor="#999999">
      <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="3"><img src="/common/pc/image/tab_kado2.gif" width="3" height="3"></td>
          <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
        </tr>
      </table></td>
    <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
  </tr>
  
  <!-- ここまで -->
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="340" height="8"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10">Copyrights&copy; almex inc.
    All Rights Reserved.</td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="340" height="12"></td>
  </tr>
</table>
</body>
</html>
