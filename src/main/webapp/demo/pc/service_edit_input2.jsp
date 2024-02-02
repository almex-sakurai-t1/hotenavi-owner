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
    String query;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    int       old_id = 0;
    int       old_disp_idx= 0;
    int       old_disp_flg= 0;
    int       old_ymd     = 0;
    java.util.Date old_start = new java.util.Date();
    java.util.Date old_end = new java.util.Date();
    String    old_equipment_name = "";
    String    old_detail         = "";
    String    old_limit_name     = "";
    connection  = DBConnection.getConnection();

    if( id.compareTo("0") != 0 )
    {
        query = "SELECT * FROM equipment WHERE hotelid='" + hotelid + "'";
        query = query + " AND id=" + id;
        prestate = connection.prepareStatement(query);
        result   = prestate.executeQuery();
        if( result.next() != false )
        {
            old_id              = result.getInt("id");
            old_disp_idx        = result.getInt("disp_idx");
            old_disp_flg        = result.getInt("disp_flg");
            old_start           = result.getDate("start_date");
            old_end             = result.getDate("end_date");
            old_equipment_name  = result.getString("equipment_name");
            old_detail          = result.getString("detail");
            old_limit_name      = result.getString("limit_name");
            int yy = old_start.getYear();
            int mm = old_start.getMonth();
            int dd = old_start.getDate();
            old_ymd = (yy+1900)*10000 + (mm+1)*100 + dd;
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }


    // 日付ﾃﾞｰﾀの編集
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

    if (BackUp.compareTo("1") == 0)
    {
          id = "0";
    }

    if( id.compareTo("0") == 0 )
    {
        query = "INSERT INTO equipment SET ";
        query = query + "hotelid='"      + hotelid + "', ";
        query = query + "add_hotelid='"  + loginhotel + "', ";
        query = query + "add_date='"     + nowdate + "', ";
        query = query + "add_time='"     + nowtime + "', ";
        query = query + "add_userid='"   + ownerinfo.DbUserId + "', ";
    }
    else
    {
        query = "UPDATE equipment SET ";
    }
        query = query + "disp_idx="      + request.getParameter("col_disp_idx") + ", ";
        query = query + "disp_flg="      + Integer.parseInt(col_disp_flg) + ", ";
        query = query + "start_date="    + start_ymd + ", ";
        query = query + "end_date="      + end_ymd + ", ";
        query = query + "equipment_name='"    + ReplaceString.SQLEscape(request.getParameter("col_equipment_name")) + "', ";
        query = query + "class='"             + ReplaceString.SQLEscape(request.getParameter("col_class")) + "', ";
        query = query + "class_detail='"      + ReplaceString.SQLEscape(request.getParameter("col_class_detail")) + "', ";
        query = query + "detail='"            + ReplaceString.SQLEscape(request.getParameter("col_detail")) + "', ";
        query = query + "memo_all='"          + ReplaceString.SQLEscape(request.getParameter("col_memo_all")) + "', ";
        query = query + "memo_limit='"        + ReplaceString.SQLEscape(request.getParameter("col_memo_limit")) + "', ";
        query = query + "memo_all_mobile='"   + ReplaceString.SQLEscape(request.getParameter("col_memo_all_mobile")) + "', ";
        query = query + "memo_limit_mobile='" + ReplaceString.SQLEscape(request.getParameter("col_memo_limit_mobile")) + "', ";
        query = query + "limit_name='"        + ReplaceString.SQLEscape(request.getParameter("col_limit_name")) + "', ";
        query = query + "upd_hotelid='"  + loginhotel + "', ";
        query = query + "upd_userid='"   + ownerinfo.DbUserId + "', ";
        query = query + "last_update='"  + nowdate + "', ";
        query = query + "last_uptime='"  + nowtime + "' ";
    if( id.compareTo("0") != 0 )
    {
        query = query + " WHERE hotelid='" + hotelid + "'  AND id=" + id;
    }

    // SQLクエリーの実行
    prestate = connection.prepareStatement(query);
    int result_new = prestate.executeUpdate();

    DBConnection.releaseResources(result,prestate,connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>クーポン作成</title>
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
                <font color="#FFFFFF">登録確認</font></td>
              <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
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
<%=BackUp%>
<%
    if( result_new != 0 )
    {
%>
登録しました。<br>
<%
    }
    else
    {
%>
登録に失敗しました。
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="service_list2.jsp?HotelId=<%= hotelid %>" method="POST">
                      <INPUT name="submit_ret" type=submit value=戻る >
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
      <!-- ここまで -->
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
