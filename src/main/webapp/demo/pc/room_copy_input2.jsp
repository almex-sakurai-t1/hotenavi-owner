<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // 入力ホテルID取得
    String hotelidinput  = request.getParameter("HotelIdInput");

    // コピー先ホテルID
    String hotelid       = request.getParameter("HotelId");

    String disp_type     = request.getParameter("DispType");
    String loginhotel = (String)session.getAttribute("LoginHotelId");

    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    String queryck = "";
    String query   = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    Connection        connection_update  = null;
    PreparedStatement prestate_update    = null;
    ResultSet         result_update      = null;
    connection_update    = DBConnection.getConnection();


    boolean error_flag = true;

    query = "SELECT * FROM room WHERE hotelid='" + hotelid + "'";
    prestate    = connection.prepareStatement(query);
    result      = prestate.executeQuery();
    if( result.next() != false )
    {
        error_flag = false;   //コピー先にデータがあったのでエラー
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    int room_no_sv = 0;
    java.sql.Date start;
    java.sql.Date end;
    int yy = 0;
    int mm = 0;
    int dd = 0;
    int start_date;
    int end_date;

    int sync_flag = 1;

    int result_up = 0;

if (error_flag)
{
 
    query = "SELECT * FROM room WHERE hotelid='" + hotelidinput + "'";
    if  (disp_type.compareTo("0") == 0)
    {
		query = query + " AND start_date<=" + nowdate;
		query = query + " AND end_date>=" + nowdate;
		query = query + " AND disp_flg=1";
    }
    query = query + " ORDER BY room_no,id DESC";
    prestate    = connection.prepareStatement(query);
    result      = prestate.executeQuery();
    while( result.next() != false )
    {
        if(result.getInt("room_no") != room_no_sv)
        {
            room_no_sv = result.getInt("room_no");
            start = result.getDate("start_date");
            yy = start.getYear();
            mm = start.getMonth();
            dd = start.getDate();
            start_date = (yy+1900)*10000 + (mm+1)*100 + dd;
            end = result.getDate("end_date");
            yy = end.getYear();
            mm = end.getMonth();
            dd = end.getDate();
            end_date = (yy+1900)*10000 + (mm+1)*100 + dd;
            query = "INSERT INTO room SET ";
            query = query + "hotelid='"      + hotelid + "', ";
            query = query + "id="            + result.getInt("id")          + ", ";
            query = query + "room_no="       + result.getInt("room_no")    + ", ";
            query = query + "disp_flg="      + result.getInt("disp_flg")    + ", ";
            query = query + "start_date="    + start_date  + ", ";
            query = query + "end_date="      + end_date    + ", ";
            query = query + "room_name='"    + ReplaceString.SQLEscape(result.getString("room_name"))       + "', ";
            query = query + "add_date="      + nowdate + ", ";
            query = query + "add_time="      + nowtime + ", ";
            query = query + "add_hotelid='"  + loginhotel + "', ";
            query = query + "add_userid="    + ownerinfo.DbUserId + ", ";
            query = query + "rank="          + result.getInt("rank")        + ", ";
            query = query + "equipment='"    + ReplaceString.SQLEscape(result.getString("equipment")) + "', ";
            query = query + "image_pc='"     + ReplaceString.SQLEscape(result.getString("image_pc")) + "', ";
            query = query + "image_mobile='" + ReplaceString.SQLEscape(result.getString("image_mobile")) + "', ";
            query = query + "image_thumb='"  + ReplaceString.SQLEscape(result.getString("image_thumb")) + "', ";
            query = query + "memo='"         + ReplaceString.SQLEscape(result.getString("memo")) + "', ";
            query = query + "upd_hotelid='"  + loginhotel + "', ";
            query = query + "upd_userid="    + ownerinfo.DbUserId + ", ";
            query = query + "last_update="   + nowdate + ", ";
            query = query + "last_uptime="   + nowtime + ", ";
            query = query + "sync_flag="     + sync_flag + ",";
            query = query + "refer_name='"   + ReplaceString.SQLEscape(result.getString("refer_name")) + "', ";
            query = query + "movie_mobile='" + ReplaceString.SQLEscape(result.getString("movie_mobile")) + "'";
            prestate_update   = connection_update.prepareStatement(query);
            result_up  = prestate_update.executeUpdate();
            DBConnection.releaseResources(prestate_update);
         }
    }
}
    DBConnection.releaseResources(connection_update);
    DBConnection.releaseResources(connection);
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
<%
    if( result_up == 1 )
    {
%>
登録しました。<br><%=query%><br><%=result_up%>
<%
    }
    else
    {
%>
登録に失敗しました。<%=query%><br><%=result_up%>
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
