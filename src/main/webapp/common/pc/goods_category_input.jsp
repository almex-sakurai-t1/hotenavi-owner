<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    String BackUp        = ReplaceString.getParameter(request,"BackUp");
    if    (BackUp == null) BackUp="0";

    String col_disp_idx  = ReplaceString.getParameter(request,"col_disp_idx");
    int    disp_idx      = Integer.parseInt(col_disp_idx);

    // ホテルID取得
    String hotelid    = (String)session.getAttribute("SelectHotel");
    String CategoryId = ReplaceString.getParameter(request,"DataType");
    String Seq        = ReplaceString.getParameter(request,"Id");
    int    new_seq    = 1;
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

    String msg1;
    msg1 = ReplaceString.getParameter(request,"col_msg1");
    msg1 = msg1.replace("<br>","<br/>");
    msg1 = msg1.replace("<BR>","<br/>");
    msg1 = msg1.replace("</strong>\r\n","</strong><br><br/>");
    msg1 = msg1.replace("</a>\r\n","</a><br><br/>");
    msg1 = msg1.replace("</font>\r\n","</font><br><br/>");
    msg1 = msg1.replace("</em>\r\n","</em><br><br/>");
    msg1 = msg1.replace("</u>\r\n","</u><br><br/>");
    msg1 = msg1.replace(">\r\n","><br/>");
    msg1 = msg1.replace("\r\n","<br/><br/>");
    msg1 = msg1.replace("><br/>",">\r\n");

    // メンバー専用
    int memberonly = 0;
    if( ReplaceString.getParameter(request,"col_member_only") != null )
    {
        memberonly = Integer.parseInt( ReplaceString.getParameter(request,"col_member_only") );
    }
    connection  = DBConnection.getConnection();

    if( Seq.compareTo("0") == 0 || BackUp.compareTo("1") == 0)
    {
        try
        {
            query = "SELECT * FROM goods_category WHERE hotelid = ? "
                               + "ORDER BY seq DESC";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                new_seq = result.getInt("seq") + 1;
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
        query = "INSERT INTO goods_category SET "
              + "hotelid = ?, "
              + "category_id = ?, "
              + "seq = ?, "
              + "add_hotelid = ?, "
              + "add_userid = ?, "
              + "add_date = ?, "
              + "add_time = ?, ";
    }
    else
    {
        query = "UPDATE goods_category SET ";
    }
        query += "disp_idx = ?, "
               + "disp_from = ?, "
               + "disp_to = ?, "
               + "title = ?, "
               + "title_color = ?, "
               + "msg = ?, "
               + "member_only = ?, "
               + "upd_hotelid = ?, "
               + "upd_userid = ?, "
               + "last_update = ?, "
               + "last_uptime = ? ";

    if( Seq.compareTo("0") != 0 && BackUp.compareTo("1") != 0)
    {
        query = query + " WHERE hotelid='" + hotelid + "' AND category_id=" + CategoryId + " AND seq=" + Seq;
    }

    // SQLクエリーの実行
    prestate    = connection.prepareStatement(query);

    if( Seq.compareTo("0") == 0 || BackUp.compareTo("1") == 0)
    {
        prestate.setString(1, hotelid);
        prestate.setInt(2, Integer.parseInt(CategoryId));
        prestate.setInt(3, new_seq);
        prestate.setString(4, loginhotel);
        prestate.setInt(5, ownerinfo.DbUserId);
        prestate.setInt(6, nowdate);
        prestate.setInt(7, nowdate);

        prestate.setInt(8, disp_idx);
        prestate.setInt(9, start_ymd);
        prestate.setInt(10, end_ymd);
        prestate.setString(11, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_title")));
        prestate.setString(12, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_title_color")));
        prestate.setString(13, ReplaceString.SQLEscape(msg1));
        prestate.setInt(14, memberonly);
        prestate.setString(15, loginhotel);
        prestate.setInt(16, ownerinfo.DbUserId);
        prestate.setInt(17, nowdate);
        prestate.setInt(18, nowtime);
    }
    else
    {
        prestate.setInt(1, disp_idx);
        prestate.setInt(2, start_ymd);
        prestate.setInt(3, end_ymd);
        prestate.setString(4, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_title")));
        prestate.setString(5, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_title_color")));
        prestate.setString(6, ReplaceString.SQLEscape(msg1));
        prestate.setInt(7, memberonly);
        prestate.setString(8, loginhotel);
        prestate.setInt(9, ownerinfo.DbUserId);
        prestate.setInt(10, nowdate);
        prestate.setInt(11, nowtime);
    }

    result_new  = prestate.executeUpdate();
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    if (BackUp.compareTo("1") == 0)
    {
        query = "UPDATE goods_category SET "
              + "disp_idx= ?, "
              + "disp_to= ?, "
              + "last_update= ?, "
              + "last_uptime= ? "
              + "WHERE hotelid= ? AND category_id= ? AND seq= ?";
        prestate    = connection.prepareStatement(query);
        prestate.setInt(1, disp_idx);
        prestate.setInt(2, old_ymd);
        prestate.setInt(3, nowdate);
        prestate.setInt(4, nowtime);
        prestate.setString(5, hotelid);
        prestate.setString(6, CategoryId);
        prestate.setString(7, Seq);
        result_new  = prestate.executeUpdate();
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

   //同じカテゴリIDはすべて同じdisp_idxにする。
    query = "UPDATE goods_category SET"
          + " disp_idx= ? "
          + " WHERE hotelid= ? AND category_id= ?";
    prestate    = connection.prepareStatement(query);
    prestate.setInt(1, disp_idx);
    prestate.setString(2, hotelid);
    prestate.setString(3, CategoryId);
    result_new  = prestate.executeUpdate();

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
                    <form action="goods_list.jsp?HotelId=<%= hotelid %>" method="POST">
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
