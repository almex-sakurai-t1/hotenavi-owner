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
    DbAccess db_output = new DbAccess();
    boolean error_flag = true;

    queryck = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
    queryck = queryck + " AND data_type=100";
    ResultSet result_output = db_output.execQuery(queryck);
    if( result_output.next() != false )
    {
        error_flag = false;   //コピー先にデータがあったのでエラー
    }
    db_output.close();

    int disp_idx_sv = 0;
    java.sql.Date start;
    java.sql.Date end;
    int yy = 0;
    int mm = 0;
    int dd = 0;
    int start_date;
    int end_date;

    int result = 0;

if (error_flag)
{
    DbAccess db_input = new DbAccess();
 
    query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelidinput + "'";
    query = query + " AND data_type=100";
    if  (disp_type.compareTo("0") == 0)
    {
		query = query + " AND start_date<=" + nowdate;
		query = query + " AND end_date>=" + nowdate;
		query = query + " AND disp_flg=1";
    }
    query = query + " ORDER BY disp_idx,id DESC";
    ResultSet result_input = db_input.execQuery(query);
    while( result_input.next() != false )
    {
        if(result_input.getInt("disp_idx") != disp_idx_sv)
        {
            disp_idx_sv = result_input.getInt("disp_idx");
            start = result_input.getDate("start_date");
            yy = start.getYear();
            mm = start.getMonth();
            dd = start.getDate();
            start_date = (yy+1900)*10000 + (mm+1)*100 + dd;
            end = result_input.getDate("end_date");
            yy = end.getYear();
            mm = end.getMonth();
            dd = end.getDate();
            end_date = (yy+1900)*10000 + (mm+1)*100 + dd;
            query = "INSERT INTO edit_event_info SET ";
            query = query + "hotelid='"     + hotelid + "', ";
            query = query + "data_type=100, ";
            query = query + "id="           + result_input.getInt("id")          + ", ";
            query = query + "disp_idx="     + result_input.getInt("disp_idx")    + ", ";
            query = query + "disp_flg="     + result_input.getInt("disp_flg")    + ", ";
            query = query + "start_date="   + start_date  + ", ";
            query = query + "end_date="     + end_date    + ", ";
            query = query + "title='"       + ReplaceString.SQLEscape(result_input.getString("title"))       + "', ";
            query = query + "add_date="     + nowdate + ", ";
            query = query + "add_time="     + nowtime + ", ";
            query = query + "add_hotelid='" + loginhotel + "', ";
            query = query + "add_userid="   + ownerinfo.DbUserId + ", ";
            query = query + "msg1_title='"  + ReplaceString.SQLEscape(result_input.getString("msg1_title")) + "', ";
            query = query + "msg1='"        + ReplaceString.SQLEscape(result_input.getString("msg1")) + "', ";
            query = query + "msg2='"        + ReplaceString.SQLEscape(result_input.getString("msg2")) + "', ";
            query = query + "msg3='"        + ReplaceString.SQLEscape(result_input.getString("msg3")) + "', ";
            query = query + "msg4='"        + ReplaceString.SQLEscape(result_input.getString("msg4")) + "', ";
            query = query + "msg5='"        + ReplaceString.SQLEscape(result_input.getString("msg5")) + "', ";
            query = query + "upd_hotelid='" + loginhotel + "', ";
            query = query + "upd_userid="   + ownerinfo.DbUserId + ", ";
            query = query + "last_update="  + nowdate + ", ";
            query = query + "last_uptime="  + nowtime + " ";
            DbAccess db = new DbAccess();
            result = db.execUpdate(query);
            db.close();
         }
    }
    db_input.close();
}
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
    if( result != 0 )
    {
%>
登録しました。<br>
<%
    }
    else
    {
%>
登録に失敗しました。<%=query%><br><%=queryck%>
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="room_list.jsp?HotelId=<%= hotelid %>" method="POST">
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
