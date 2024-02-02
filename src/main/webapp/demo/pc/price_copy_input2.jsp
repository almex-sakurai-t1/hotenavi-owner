<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.DBSync" %>

<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    // 入力ホテルID取得
    String hotelidinput  = request.getParameter("HotelIdInput");

    // コピー先ホテルID
    String hotelid       = request.getParameter("HotelId");

    String disp_type     = request.getParameter("DispType");
    String loginhotel = (String)session.getAttribute("LoginHotelId");

    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    String query   = "";
    String query_ck   = "";
    boolean error_flag = true;

    connection  = DBConnection.getConnection();
    query_ck = "SELECT * FROM price WHERE hotelid='" + hotelid + "'";
    prestate = connection.prepareStatement(query_ck);
    result   = prestate.executeQuery();

    if( result.next() != false )
    {
        error_flag = false;   //コピー先にデータがあったのでエラー
    }
    DBConnection.releaseResources(result,prestate,connection);

    java.sql.Date start;
    java.sql.Date end;
    int yy = 0;
    int mm = 0;
    int dd = 0;
    int start_date;
    int end_date;
    int result_copy = 0;
if (error_flag)
{
    connection  = DBConnection.getConnection();
 
    query = "SELECT * FROM price WHERE hotelid='" + hotelidinput + "'";
    if  (disp_type.compareTo("0") == 0)
    {
        query = query + " AND disp_flg=1";
    }
    prestate = connection.prepareStatement(query);
    result   = prestate.executeQuery();

    while( result.next() != false )
    {
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

        query = "INSERT INTO hotenavi.price SET ";
        query = query + "hotelid='"     + hotelid + "', ";
        query = query + "disp_flg="     + result.getInt("disp_flg")    + ", ";
        query = query + "start_date="   + start_date  + ", ";
        query = query + "end_date="     + end_date    + ", ";
        query = query + "price_name='"  + ReplaceString.SQLEscape(result.getString("price_name"))       + "', ";
        query = query + "roomrank_name='"  + ReplaceString.SQLEscape(result.getString("roomrank_name"))       + "', ";
        query = query + "add_date="     + nowdate + ", ";
        query = query + "add_time="     + nowtime + ", ";
        query = query + "add_hotelid='" + loginhotel + "', ";
        query = query + "add_userid="   + ownerinfo.DbUserId + ", ";
        query = query + "system='"      + ReplaceString.SQLEscape(result.getString("system")) + "', ";
        query = query + "memo='"        + ReplaceString.SQLEscape(result.getString("memo")) + "', ";
        query = query + "price_memo='"  + ReplaceString.SQLEscape(result.getString("price_memo")) + "', ";
        query = query + "detail_explain='"  + ReplaceString.SQLEscape(result.getString("detail_explain")) + "', ";
        query = query + "layout='"      + ReplaceString.SQLEscape(result.getString("layout")) + "', ";
        query = query + "upd_hotelid='" + loginhotel + "', ";
        query = query + "upd_userid="   + ownerinfo.DbUserId + ", ";
        query = query + "last_update="  + nowdate + ", ";
        query = query + "last_uptime="  + nowtime + " ";
        prestate = connection.prepareStatement(query);
        result_copy = prestate.executeUpdate();
        DBSync.publish(prestate.toString().split(":",2)[1]);
        DBSync.publish(prestate.toString().split(":",2)[1],true);
        DBConnection.releaseResources(prestate);
 
    }
    DBConnection.releaseResources(result,prestate,connection);
}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>料金情報作成</title>
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
    if( result_copy != 0 )
    {
%>
登録しました。<br>
<%
    }
    else
    {
%>
登録に失敗しました。<%=query%><br><%=query_ck%>
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="price_list2.jsp?HotelId=<%= hotelid %>" method="POST">
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
