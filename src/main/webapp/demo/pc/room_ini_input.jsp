<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

	String hotelid = request.getParameter("HotelId");

    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    int       sync_flag            = 1;
    int       result_new           = 0;
    connection  = DBConnection.getConnection();

    // ì˙ït√ﬁ∞¿ÇÃï“èW
    int start_ymd = 0;
    int end_ymd = 0;

    int start_yy = Integer.parseInt(request.getParameter("col_start_yy"));
    int start_mm = Integer.parseInt(request.getParameter("col_start_mm"));
    int start_dd = Integer.parseInt(request.getParameter("col_start_dd"));
    start_ymd = start_yy*10000 + start_mm*100 + start_dd;

    int end_yy = Integer.parseInt(request.getParameter("col_end_yy"));
    int end_mm = Integer.parseInt(request.getParameter("col_end_mm"));
    int end_dd = Integer.parseInt(request.getParameter("col_end_dd"));
    end_ymd = end_yy*10000 + end_mm*100 + end_dd;

    String room_text      = request.getParameter("col_room_text");
    String image_pc       = request.getParameter("col_image_pc");
    String image_thumb    = request.getParameter("col_image_thumb");
    String image_mobile   = request.getParameter("col_image_mobile");
    String rank_i         = request.getParameter("rank_i");
    String room_i         = request.getParameter("room_i");


    
    int    i   = 0;
    for( i = 1; i <= Integer.parseInt(rank_i) ; i++)
    {
        query = "INSERT INTO roomrank SET ";
        query = query + "hotelid='"       + hotelid + "', ";
        query = query + "rank="           + i  + ", ";
        query = query + "add_hotelid='"   + loginhotel + "', ";
        query = query + "add_date='"      + nowdate + "', ";
        query = query + "add_time='"      + nowtime + "', ";
        query = query + "add_userid='"    + ownerinfo.DbUserId + "', ";
        query = query + "disp_flg=1,";
        query = query + "start_date="     + start_ymd + ", ";
        query = query + "end_date="       + end_ymd + ", ";
        query = query + "rank_name='"     + ReplaceString.SQLEscape(request.getParameter("rank_name_" + i)) + "', ";
        query = query + "upd_hotelid='"   + loginhotel + "', ";
        query = query + "upd_userid='"    + ownerinfo.DbUserId + "', ";
        query = query + "last_update='"   + nowdate + "', ";
        query = query + "last_uptime='"   + nowtime + "', ";
        query = query + "system='', ";
        query = query + "memo='', ";
        query = query + "system_mobile='', ";
        query = query + "sync_flag="      + sync_flag;
        prestate = connection.prepareStatement(query);
        result_new = prestate.executeUpdate();
    }
    for( i = 1; i <= Integer.parseInt(room_i) ; i++)
    {
        query = "INSERT INTO room SET ";
        query = query + "hotelid='"      + hotelid + "', ";
        query = query + "add_hotelid='"  + loginhotel + "', ";
        query = query + "add_date='"     + nowdate + "', ";
        query = query + "add_time='"     + nowtime + "', ";
        query = query + "add_userid='"   + ownerinfo.DbUserId + "', ";
        query = query + "room_no="       + request.getParameter("room_no_" + i) + ", ";
        query = query + "disp_flg=1,";
        query = query + "start_date="    + start_ymd + ", ";
        query = query + "end_date="      + end_ymd + ", ";
        query = query + "room_name='"    + ReplaceString.SQLEscape(request.getParameter("room_name_" + i)) + "', ";
        query = query + "rank="          + Integer.parseInt(request.getParameter("room_rank_" + i)) + ", ";
        query = query + "equipment='',";
        query = query + "image_pc='"     + ReplaceString.SQLEscape(image_pc) + "', ";
        if ( request.getParameter("room_image_" + i) != null)
        {
            query = query + "image_mobile='" + ReplaceString.SQLEscape(image_mobile) + "', ";
        }
        else
        {
            query = query + "image_mobile='',";
        }
        query = query + "image_thumb='"  + ReplaceString.SQLEscape(image_thumb) + "', ";
        query = query + "memo='', ";
        query = query + "upd_hotelid='"  + loginhotel + "', ";
        query = query + "upd_userid='"   + ownerinfo.DbUserId + "', ";
        query = query + "last_update='"  + nowdate + "', ";
        query = query + "last_uptime='"  + nowtime + "', ";
        query = query + "movie_mobile='', ";
        query = query + "sync_flag="     + sync_flag;
        prestate = connection.prepareStatement(query);
        result_new = prestate.executeUpdate();
    }
    DBConnection.releaseResources(result,prestate,connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>ïîâÆèÓïÒçÏê¨</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
<script type="text/javascript" src="/common/pc/scripts/coupon.js"></script>
</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">ìoò^ämîF</font></td>
              <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- Ç±Ç±Ç©ÇÁï\ -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="/common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if( result_new != 0 )
    {
%>
ìoò^ÇµÇ‹ÇµÇΩÅB<br>
<%
    }
    else
    {
%>
ìoò^Ç…é∏îsÇµÇ‹ÇµÇΩÅB<br><%=query%>
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="room_list2.jsp?HotelId=<%= hotelid %>" method="POST">
                      <INPUT name="submit_ret" type=submit value=ñﬂÇÈ >
                    </form></td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="/common/pc/image/tab_kado.gif" width="3" height="3"></td>
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
          </table>
        </td>
        <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- Ç±Ç±Ç‹Ç≈ -->
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="/common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; imedia
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
