<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    String hotelid = request.getParameter("HotelId");
    if    (hotelid == null) hotelid = "demo";

    String BackUp        = request.getParameter("BackUp");
    if    (BackUp == null) BackUp="0";

if  (hotelid.compareTo("all") == 0)
{
	hotelid = request.getParameter("HotelId");
}

    String rank = request.getParameter("Rank");
    if (rank == null) rank="0";
    int    rank_i = Integer.parseInt(rank);
    String id = request.getParameter("Id");
    if (id == null) id="0";
    String query;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    int       old_id = 0;
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
    String    old_rank_name        = "";
    String    old_system           = "";
    String    old_memo             = "";
    String    old_memo_for_happyhotel  = "";
    String    old_system_mobile    = "";
    int       sync_flag            = 0;
    String    old_title_class      = "";
    String    old_price_notation   = "";

    connection  = DBConnection.getConnection();

    if( id.compareTo("0") != 0 )
    {
        query = "SELECT * FROM roomrank WHERE hotelid='" + hotelid + "'";
        query = query + " AND rank=" + rank_i;
        query = query + " AND id=" + id;
        prestate = connection.prepareStatement(query);
        result   = prestate.executeQuery();
        if( result.next() != false )
        {
            old_id            = result.getInt("id");
            old_disp_flg      = result.getInt("disp_flg");
            old_start         = result.getDate("start_date");
            old_end           = result.getDate("end_date");
            old_rank_name     = result.getString("rank_name");
            old_system        = result.getString("system");
            old_memo          = result.getString("memo");
            old_memo_for_happyhotel = result.getString("memo_for_happyhotel");
            old_system_mobile = result.getString("system_mobile");
            old_start_yy     = old_start.getYear();
            old_start_mm     = old_start.getMonth();
            old_start_dd     = old_start.getDate();
            old_start_ymd    = (old_start_yy+1900)*10000 + (old_start_mm+1)*100 + old_start_dd;
            old_end_yy       = old_end.getYear();
            old_end_mm       = old_end.getMonth();
            old_end_dd       = old_end.getDate();
            old_end_ymd      = (old_end_yy+1900)*10000 + (old_end_mm+1)*100 + old_end_dd;
            old_title_class  = result.getString("title_class");
            old_price_notation = result.getString("price_notation");
            if (old_price_notation.equals(""))
            {
                old_price_notation = "ê≈çû";
            }
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

	String    disp_index = request.getParameter("disp_index");
	if       (disp_index == null) disp_index = "0";
	
    if (BackUp.compareTo("1") == 0)
    {
          id = "0";
    }

    String col_rank_name     = ReplaceString.SQLEscape(request.getParameter("col_rank_name"));
    String col_system        = ReplaceString.SQLEscape(request.getParameter("col_system"));
    String col_system_mobile = ReplaceString.SQLEscape(request.getParameter("col_system_mobile"));
    String col_memo          = ReplaceString.SQLEscape(request.getParameter("col_memo"));
    String col_memo_for_happyhotel = ReplaceString.SQLEscape(request.getParameter("col_memo_for_happyhotel"));
    String col_title_class   = ReplaceString.SQLEscape(request.getParameter("col_title_class"));
    String col_price_notation= ReplaceString.SQLEscape(request.getParameter("col_price_notation"));
    if (start_ymd  != old_start_ymd || end_ymd != old_end_ymd || col_rank_name.compareTo(old_rank_name)!=0)
    {
        sync_flag = 1;
    }

    if( id.compareTo("0") == 0 )
    {
        query = "INSERT INTO hotenavi.roomrank SET ";
        query = query + "hotelid='"     + hotelid + "', ";
        query = query + "rank="         + rank_i  + ", ";
        query = query + "add_hotelid='" + loginhotel + "', ";
        query = query + "add_date='"    + nowdate + "', ";
        query = query + "add_time='"    + nowtime + "', ";
        query = query + "add_userid='"  + ownerinfo.DbUserId + "', ";
    }
    else
    {
        query = "UPDATE hotenavi.roomrank SET ";
    }
        query = query + "disp_flg="       + Integer.parseInt(col_disp_flg) + ", ";
        query = query + "start_date="     + start_ymd + ", ";
        query = query + "end_date="       + end_ymd + ", ";
        query = query + "rank_name='"     + ReplaceString.SQLEscape(request.getParameter("col_rank_name")) + "', ";
        query = query + "system='"        + ReplaceString.SQLEscape(request.getParameter("col_system")) + "', ";
        query = query + "class='"         + ReplaceString.SQLEscape(request.getParameter("col_class")) + "', ";
        query = query + "memo='"          + ReplaceString.SQLEscape(request.getParameter("col_memo")) + "', ";
        query = query + "memo_for_happyhotel='" + ReplaceString.SQLEscape(request.getParameter("col_memo_for_happyhotel")) + "', ";
        query = query + "system_mobile='" + ReplaceString.SQLEscape(request.getParameter("col_system_mobile")) + "', ";
        query = query + "title_class='"   + ReplaceString.SQLEscape(request.getParameter("col_title_class")) + "', ";
        query = query + "price_notation='"+ ReplaceString.SQLEscape(request.getParameter("col_price_notation")) + "', ";
        query = query + "upd_hotelid='"   + loginhotel + "', ";
        query = query + "upd_userid='"    + ownerinfo.DbUserId + "', ";
        query = query + "last_update='"   + nowdate + "', ";
        query = query + "last_uptime='"   + nowtime + "', ";
        query = query + "sync_flag="      + sync_flag + ", ";
		query = query + "disp_index="     + Integer.parseInt(disp_index);
    if( id.compareTo("0") != 0 )
    {
        query = query + " WHERE hotelid='" + hotelid + "' AND rank=" + rank_i + " AND id=" + id;
    }

    // SQLÉNÉGÉäÅ[ÇÃé¿çs
    prestate = connection.prepareStatement(query);
    int result_new = prestate.executeUpdate();
    DBSync.publish(prestate.toString().split(":",2)[1]);
    DBSync.publish(prestate.toString().split(":",2)[1],true);

    DBConnection.releaseResources(result,prestate,connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>óøã‡èÓïÒçÏê¨</title>
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
ìoò^Ç…é∏îsÇµÇ‹ÇµÇΩÅB
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="roomrank_list2.jsp?HotelId=<%= hotelid %>" method="POST">
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
