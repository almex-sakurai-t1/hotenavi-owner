<%@ page contentType="text/html; charset=Shift_JIS" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

	String hotelid = request.getParameter("HotelId");

if  (hotelid.compareTo("all") == 0)
{
	hotelid = request.getParameter("HotelId");
}

    String data_type = request.getParameter("DataType");
    String id = request.getParameter("Id");
    String query;
    DbAccess db = new DbAccess();


    String disp_flg = request.getParameter("col_disp_flg");
    if  (disp_flg == null) disp_flg = "0";


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


//携帯版追加の場合、色指定がされていなければ、色をセットする。
    String    title_color       = ReplaceString.SQLEscape(request.getParameter("col_title_color"));
    String    contents          = ReplaceString.SQLEscape(request.getParameter("col_contents"));
    String    decoration        = ReplaceString.SQLEscape(request.getParameter("col_decoration"));

    if( id.compareTo("0") == 0 )
    {
        query = "INSERT INTO hotenavi.menu SET ";
        query = query + "hotelid='"     + hotelid + "', ";
        query = query + "data_type="    + data_type + ", ";
        query = query + "add_hotelid='" + loginhotel + "', ";
        query = query + "add_date='"    + nowdate + "', ";
        query = query + "add_time='"    + nowtime + "', ";
        query = query + "add_userid='"  + ownerinfo.DbUserId + "', ";
        query = query + "msg='',";
    }
    else
    {
        query = "UPDATE hotenavi.menu SET ";
    }
        query = query + "disp_idx="     + request.getParameter("col_disp_idx") + ", ";
        query = query + "disp_flg="     + disp_flg  + ", ";
        query = query + "start_date="   + start_ymd + ", ";
        query = query + "end_date="     + end_ymd + ", ";
        query = query + "title='"       + ReplaceString.SQLEscape(request.getParameter("col_title")) + "', ";
        query = query + "title_color='" + ReplaceString.SQLEscape(request.getParameter("col_title_color")) + "', ";
        query = query + "contents='"    + ReplaceString.SQLEscape(request.getParameter("col_contents")) + "', ";
        query = query + "decoration='"  + ReplaceString.SQLEscape(request.getParameter("col_decoration")) + "', ";
        query = query + "hpedit_id="    + request.getParameter("col_hpedit_id") + ", ";
        query = query + "upd_hotelid='" + loginhotel + "', ";
        query = query + "upd_userid='"  + ownerinfo.DbUserId + "', ";
        query = query + "last_update='" + nowdate + "', ";
        query = query + "last_uptime='" + nowtime + "' ";
    if( id.compareTo("0") != 0 )
    {
        query = query + " WHERE hotelid='" + hotelid + "' AND data_type=" + data_type + " AND id=" + id;
    }

    // SQLクエリーの実行
    int result = db.execUpdate(query);
		DBSync.publish(query);
		DBSync.publish(query,true);

    db.close();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS">
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
<%
    if( result != 0 )
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
                    <td class="size12"><form action="menu_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>" method="POST">
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
