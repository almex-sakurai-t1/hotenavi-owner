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
    String BackUp        = request.getParameter("BackUp");
    if    (BackUp == null) BackUp="0";

if  (hotelid.compareTo("all") == 0)
{
	hotelid = request.getParameter("HotelId");
}

    String id = request.getParameter("Id");
    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    int       old_id = 0;
    int       old_room_no= 0;
    int       old_disp_flg= 0;
    int       old_start_ymd        = 0;
    int       old_end_ymd          = 0;
    java.util.Date old_start = new java.util.Date();
    java.util.Date old_end   = new java.util.Date();
    int       old_start_yy         = 0;
    int       old_start_mm         = 0;
    int       old_start_dd         = 0;
    int       old_end_yy           = 0;
    int       old_end_mm           = 0;
    int       old_end_dd           = 0;
    String    old_room_name        = "";
    String    old_equipment        = "";
    int       old_rank             = 0;
    String    old_image_pc         = "";
    String    old_image_mobile     = "";
    String    old_movie_mobile     = "";
    String    old_image_thumb      = "";
    String    old_memo             = "";
    String    old_refer_name       = "";
    int       sync_flag            = 0;
    int       result_new           = 0;
    connection  = DBConnection.getConnection();

    if( id.compareTo("0") != 0 )
    {
        query = "SELECT * FROM room WHERE hotelid='" + hotelid + "'";
        query = query + " AND id=" + id;
        prestate = connection.prepareStatement(query);
        result   = prestate.executeQuery();
        if( result.next() != false )
        {
            old_id           = result.getInt("id");
            old_room_no      = result.getInt("room_no");
            old_disp_flg     = result.getInt("disp_flg");
            old_start        = result.getDate("start_date");
            old_end          = result.getDate("end_date");
            old_room_name    = result.getString("room_name");
            old_rank         = result.getInt("rank");
            old_equipment    = result.getString("equipment");
            old_image_pc     = result.getString("image_pc");
            old_image_mobile = result.getString("image_mobile");
            old_movie_mobile = result.getString("movie_mobile");
            old_image_thumb  = result.getString("image_thumb");
            old_memo         = result.getString("memo");
            old_refer_name   = result.getString("refer_name");
            old_start_yy     = old_start.getYear();
            old_start_mm     = old_start.getMonth();
            old_start_dd     = old_start.getDate();
            old_start_ymd    = (old_start_yy+1900)*10000 + (old_start_mm+1)*100 + old_start_dd;
            old_end_yy       = old_end.getYear();
            old_end_mm       = old_end.getMonth();
            old_end_dd       = old_end.getDate();
            old_end_ymd      = (old_end_yy+1900)*10000 + (old_end_mm+1)*100 + old_end_dd;
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }


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

    String    col_disp_flg = request.getParameter("col_disp_flg");
    if       (col_disp_flg == null) col_disp_flg = "0";
    String    col_rank     = request.getParameter("col_rank");
    if       (col_rank     == null) col_rank = "0";

    if (BackUp.compareTo("1") == 0)
    {
          id = "0";
    }

    String col_room_name = ReplaceString.SQLEscape(request.getParameter("col_room_name"));
    String col_equipment = ReplaceString.SQLEscape(request.getParameter("col_equipment"));
    String col_image_thumb  = ReplaceString.SQLEscape(request.getParameter("col_image_thumb"));
    String col_image_mobile = ReplaceString.SQLEscape(request.getParameter("col_image_mobile"));
    String col_movie_mobile = ReplaceString.SQLEscape(request.getParameter("col_movie_mobile"));
    String col_refer_name = "";
    if (col_image_thumb.indexOf("thumb%ROOMNAME%") != -1)
    {
        col_refer_name = col_room_name.replace("çÜé∫","");
    }
    if (start_ymd  != old_start_ymd || end_ymd != old_end_ymd || Integer.parseInt(col_rank) != old_rank || col_image_mobile.compareTo(old_image_mobile)!=0 || col_refer_name.compareTo(old_refer_name)!=0)
    {
        sync_flag = 1;
    }

    if( id.compareTo("0") == 0 )
    {
        query = "INSERT INTO room SET ";
        query = query + "hotelid='"      + hotelid + "', ";
        query = query + "add_hotelid='"  + loginhotel + "', ";
        query = query + "add_date='"     + nowdate + "', ";
        query = query + "add_time='"     + nowtime + "', ";
        query = query + "add_userid='"   + ownerinfo.DbUserId + "', ";
    }
    else
    {
        query = "UPDATE room SET ";
    }
        query = query + "room_no="       + request.getParameter("col_room_no") + ", ";
        query = query + "disp_flg="      + Integer.parseInt(col_disp_flg) + ", ";
        query = query + "start_date="    + start_ymd + ", ";
        query = query + "end_date="      + end_ymd + ", ";
        query = query + "room_name='"    + ReplaceString.SQLEscape(request.getParameter("col_room_name")) + "', ";
        query = query + "rank="          + Integer.parseInt(col_rank) + ", ";
        query = query + "equipment='"    + ReplaceString.SQLEscape(request.getParameter("col_equipment")) + "', ";
        query = query + "image_pc='"     + ReplaceString.SQLEscape(request.getParameter("col_image_pc")) + "', ";
        query = query + "image_mobile='" + ReplaceString.SQLEscape(request.getParameter("col_image_mobile")) + "', ";
        query = query + "movie_mobile='" + ReplaceString.SQLEscape(request.getParameter("col_movie_mobile")) + "', ";
        query = query + "image_thumb='"  + ReplaceString.SQLEscape(request.getParameter("col_image_thumb")) + "', ";
        query = query + "memo='"         + ReplaceString.SQLEscape(request.getParameter("col_memo")) + "', ";
        query = query + "refer_name='"   + ReplaceString.SQLEscape(col_refer_name) + "', ";
        query = query + "upd_hotelid='"  + loginhotel + "', ";
        query = query + "upd_userid='"   + ownerinfo.DbUserId + "', ";
        query = query + "last_update='"  + nowdate + "', ";
        query = query + "last_uptime='"  + nowtime + "', ";
        query = query + "sync_flag="     + sync_flag;
    if( id.compareTo("0") != 0 )
    {
        query = query + " WHERE hotelid='" + hotelid + "' AND id=" + id;
    }

    // SQLÉNÉGÉäÅ[ÇÃé¿çs
    prestate = connection.prepareStatement(query);
    result_new = prestate.executeUpdate();

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
ìoò^Ç…é∏îsÇµÇ‹ÇµÇΩÅB<%=id%><br><%=query%>
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
