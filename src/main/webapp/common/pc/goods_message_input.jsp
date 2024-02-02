<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/goods_message_ini.jsp" %>

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
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    String BackUp        = ReplaceString.getParameter(request,"BackUp");
    if    (BackUp == null) BackUp="0";

    int    disp_idx      = 0;
    int    data_type     = 81; // data_type = disp_idx + 81;

    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    String id = ReplaceString.getParameter(request,"Id");
    String query ="";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int result_new = 0;
    DateEdit  de = new DateEdit();

    // 日付ﾃﾞｰﾀの編集
    int start_ymd = 0;
    int end_ymd = 0;
    int old_ymd = 0;

    int start_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_start_yy"));
    int start_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_start_mm"));
    int start_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_start_dd"));
    start_ymd = start_yy*10000 + start_mm*100 + start_dd;

    int end_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_end_yy"));
    int end_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_end_mm"));
    int end_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_end_dd"));
    end_ymd = end_yy*10000 + end_mm*100 + end_dd;
    old_ymd = de.addDay(start_ymd , -1);
    String  col_disp_flg = ReplaceString.getParameter(request,"col_disp_flg");
    if      (col_disp_flg == null) col_disp_flg = "0";


    String[][]  msg             = new String[10][8];
    for( disp_idx = 0 ; disp_idx <= DispIdxMax ; disp_idx++ )
    {
        data_type = disp_idx + 81;
        for( int i = 0 ; i < 8 ; i++ )
        {
            msg[disp_idx][i] = ReplaceString.getParameter(request,"col"+disp_idx+"_msg"+(i+1));
            if (Decoration[disp_idx][i] == 1)
            {
            msg[disp_idx][i] = msg[disp_idx][i].replace("<br>","<br/>");
            msg[disp_idx][i] = msg[disp_idx][i].replace("<BR>","<br/>");
            msg[disp_idx][i] = msg[disp_idx][i].replace("</strong>\r\n","</strong><br><br/>");
            msg[disp_idx][i] = msg[disp_idx][i].replace("</a>\r\n","</a><br><br/>");
            msg[disp_idx][i] = msg[disp_idx][i].replace("</font>\r\n","</font><br><br/>");
            msg[disp_idx][i] = msg[disp_idx][i].replace("</em>\r\n","</em><br><br/>");
            msg[disp_idx][i] = msg[disp_idx][i].replace("</u>\r\n","</u><br><br/>");
            msg[disp_idx][i] = msg[disp_idx][i].replace(">\r\n","><br/>");
            msg[disp_idx][i] = msg[disp_idx][i].replace("\r\n","<br/><br/>");
            msg[disp_idx][i] = msg[disp_idx][i].replace("><br/>",">\r\n");
            }
        }
    }

    // メンバー専用
    int memberonly = 0;
    if( ReplaceString.getParameter(request,"col_member_only") != null )
    {
        memberonly = Integer.parseInt( ReplaceString.getParameter(request,"col_member_only") );
    }
    connection  = DBConnection.getConnection();

    for( disp_idx = 0 ; disp_idx <= DispIdxMax ; disp_idx++ )
    {
        data_type = disp_idx + 81;
        if( id.compareTo("0") == 0 || BackUp.compareTo("1") == 0)
        {
            query = "INSERT INTO edit_event_info SET ";
            query = query + "hotelid=?, ";
            query = query + "data_type=?, ";
            query = query + "add_hotelid=?, ";
            query = query + "add_userid=?, ";
            query = query + "add_date=?, ";
            query = query + "add_time=?, ";
        }
        else
        {
            query = "UPDATE edit_event_info SET ";
        }
        query = query + "disp_idx=?, ";
        query = query + "disp_flg=?, ";
        query = query + "start_date=?, ";
        query = query + "end_date=?, ";
        query = query + "title=?, ";
        query = query + "title_color=?, ";
        query = query + "msg1_title=?, ";
        query = query + "msg1_title_color=?, ";
        query = query + "msg1=?, ";
        query = query + "msg2_title=?, ";
        query = query + "msg2_title_color=?, ";
        query = query + "msg2=?, ";
        query = query + "msg3_title=?, ";
        query = query + "msg3_title_color=?, ";
        query = query + "msg3=?, ";
        query = query + "msg4_title=?, ";
        query = query + "msg4_title_color=?, ";
        query = query + "msg4=?, ";
        query = query + "msg5_title=?, ";
        query = query + "msg5_title_color=?, ";
        query = query + "msg5=?, ";
        query = query + "msg6_title=?, ";
        query = query + "msg6_title_color=?, ";
        query = query + "msg6=?, ";
        query = query + "msg7_title=?, ";
        query = query + "msg7_title_color=?, ";
        query = query + "msg7=?, ";
        query = query + "msg8_title=?, ";
        query = query + "msg8_title_color=?, ";
        query = query + "msg8=?, ";
        query = query + "member_only=?, ";
        query = query + "upd_hotelid=?, ";
        query = query + "upd_userid=?, ";
        query = query + "last_update=?, ";
        query = query + "last_uptime=? ";
        if( id.compareTo("0") != 0 && BackUp.compareTo("1") != 0)
        {
            query = query + " WHERE hotelid=? AND data_type=? AND id=?";
        }
        prestate    = connection.prepareStatement(query);
        int offset = 0;
        if( id.compareTo("0") == 0 || BackUp.compareTo("1") == 0)
        {
            prestate.setString(1, hotelid);
            prestate.setInt(2, data_type);
            prestate.setString(3, loginhotel);
            prestate.setInt(4, ownerinfo.DbUserId);
            prestate.setInt(5, nowdate);
            prestate.setInt(6, nowtime);
            offset = 6;
        }
        prestate.setInt(offset + 1, disp_idx);
        prestate.setInt(offset + 2, Integer.parseInt(col_disp_flg));
        prestate.setInt(offset + 3, start_ymd);
        prestate.setInt(offset + 4, end_ymd);
        prestate.setString(offset + 5, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_title")));
        prestate.setString(offset + 6, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_title_color")));
        prestate.setString(offset + 7, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg1_title")));
        prestate.setString(offset + 8, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg1_title_color")));
        prestate.setString(offset + 9, ReplaceString.SQLEscape(msg[disp_idx][0]));
        prestate.setString(offset + 10, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg2_title")));
        prestate.setString(offset + 11, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg2_title_color")));
        prestate.setString(offset + 12, ReplaceString.SQLEscape(msg[disp_idx][1]));
        prestate.setString(offset + 13, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg3_title")));
        prestate.setString(offset + 14, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg3_title_color")));
        prestate.setString(offset + 15, ReplaceString.SQLEscape(msg[disp_idx][2]));
        prestate.setString(offset + 16, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg4_title")));
        prestate.setString(offset + 17, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg4_title_color")));
        prestate.setString(offset + 18, ReplaceString.SQLEscape(msg[disp_idx][3]));
        prestate.setString(offset + 19, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg5_title")));
        prestate.setString(offset + 20, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg5_title_color")));
        prestate.setString(offset + 21, ReplaceString.SQLEscape(msg[disp_idx][4]));
        prestate.setString(offset + 22, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg6_title")));
        prestate.setString(offset + 23, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg6_title_color")));
        prestate.setString(offset + 24, ReplaceString.SQLEscape(msg[disp_idx][5]));
        prestate.setString(offset + 25, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg7_title")));
        prestate.setString(offset + 26, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg7_title_color")));
        prestate.setString(offset + 27, ReplaceString.SQLEscape(msg[disp_idx][6]));
        prestate.setString(offset + 28, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg8_title")));
        prestate.setString(offset + 29, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col"+disp_idx+"_msg8_title_color")));
        prestate.setString(offset + 30, ReplaceString.SQLEscape(msg[disp_idx][7]));
        prestate.setInt(offset + 31, memberonly);
        prestate.setString(offset + 32, loginhotel);
        prestate.setInt(offset + 33, ownerinfo.DbUserId);
        prestate.setInt(offset + 34, nowdate);
        prestate.setInt(offset + 35, nowtime);
        if( id.compareTo("0") != 0 && BackUp.compareTo("1") != 0)
        {
            prestate.setString(offset + 36, hotelid);
            prestate.setInt(offset + 37, data_type);
            prestate.setString(offset + 38, id);
        }
        result_new  = prestate.executeUpdate();
        if (BackUp.compareTo("1") == 0)
        {
            query = "UPDATE edit_event_info SET";
            query = query + " end_date=?, ";
            query = query + "last_update=?, ";
            query = query + "last_uptime=? ";
            query = query + " WHERE hotelid=? AND data_type=? AND id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setInt(1, old_ymd);
            prestate.setInt(2, nowdate);
            prestate.setInt(3, nowtime);
            prestate.setString(4, hotelid);
            prestate.setInt(5, data_type);
            prestate.setString(6, id);
            result_new  = prestate.executeUpdate();
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    DBConnection.releaseResources(result,prestate,connection);

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>景品交換メッセージ他設定登録</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/coupon.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">登録確認</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
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
            <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
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
                    <td class="size12">
                    <form action="goods_message.jsp?HotelId=<%= hotelid %>" method="POST">
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
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
