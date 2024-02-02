<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.CheckString" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/qaedit_ini.jsp" %>
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
    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    String BackUp        = ReplaceString.getParameter(request,"BackUp");
    if    (BackUp == null) BackUp="0";

    // ホテルID取得
    String hotelid = ReplaceString.getParameter(request,"HotelId");
	if( hotelid != null && !CheckString.hotenaviIdCheck(hotelid))
	{
            hotelid = "0";
%>
		<script type="text/javascript">		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
if  (hotelid == null)
{
    hotelid =  (String)session.getAttribute("SelectHotel");
}

    int result_new = 0;

    String paramId   = ReplaceString.getParameter(request,"Id");
    if    (paramId  == null) paramId="0";
    int qid         = Integer.parseInt(paramId);
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();

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

    String  col_disp_flg        = ReplaceString.getParameter(request,"col_disp_flg");
    if      (col_disp_flg == null)        col_disp_flg = "0";
    String  col_member_only     = ReplaceString.getParameter(request,"col_member_only");
    if      (col_member_only == null)     col_member_only = "0";
    String  col_duplicate_check = ReplaceString.getParameter(request,"col_duplicate_check");
    if      (col_duplicate_check == null) col_duplicate_check = "0";
    String  col_qid_max         = ReplaceString.getParameter(request,"col_qid_max");
    if      (col_qid_max == null)         col_qid_max = "0";
%>
<%
    int disp_flg        = Integer.parseInt(col_disp_flg);
    int duplicate_check = Integer.parseInt(col_duplicate_check);
    int member_only     = Integer.parseInt(col_member_only);
    int member_flag     = member_only;
    int qid_max         = Integer.parseInt(col_qid_max);

    String  col_msg_subtitle     = ReplaceString.getParameter(request,"col_msg_subtitle");
    if     (col_msg_subtitle == null)        col_msg_subtitle = "";
    int i           = 0;
    int id[]              = new int[30];
    int required[]        = new int[30];
    String col_msg[]      = new String[30];
    String col_id[]       = new String[30];
    String col_required[] = new String[30];
    String col_data[]     = new String[30];

    for( i = 0 ; i <= qid_max ; i++ )
    {
        col_msg[i]      =  ReplaceString.getParameter(request,"col_msg"+i);
        col_id[i]       =  ReplaceString.getParameter(request,"col_id"+i);
        if (col_id[i] ==  null) col_id[i] = "0";
        id[i]           =  Integer.parseInt(col_id[i]);
        col_data[i]     =  ReplaceString.getParameter(request,"col_data"+i);
        col_required[i] =  ReplaceString.getParameter(request,"col_required"+i);
        if (col_required[i] ==  null) col_required[i] = "0";
        required[i]     =  Integer.parseInt(col_required[i]);
    }
%>
<%

    String query   = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection    = DBConnection.getConnection();
    boolean   NewFlag     = false;
    if( qid == 0 )
    {
        NewFlag    = true;
    }

    int count_record    = 0;

    if(!NewFlag)
    {
        count_record = 0;
        query = "SELECT COUNT(seq),MAX(seq),MIN(seq) FROM question_answer WHERE hotel_id=?";
        query = query + " AND id=?";
        prestate        = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setInt(2,qid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            if (result.getInt(1) != 0)
            {
                count_record = result.getInt(2)-result.getInt(3)+1;  // >=1･･･回答済みなので一部のみの処理
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    member_flag = (member_flag % 2) + (disp_flg * 2);

    if (count_record != 0) // 回答済み
    {
        query = "SELECT member_flag FROM question_master WHERE hotel_id=?";
        query = query + " AND id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, qid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            member_flag =result.getInt("member_flag");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        member_flag = (member_flag % 2) + (disp_flg * 2);
        if (BackUp.compareTo("1") != 0)
        {
            query = "UPDATE question_master SET ";
            query = query + "start="             + start_ymd   + ", ";
            query = query + "end="               + end_ymd     + ", ";
            query = query + "member_flag="       + member_flag + ", ";
            query = query + "msg='"              + col_msg_subtitle  + "', ";
            query = query + "duplicate_check="   + duplicate_check + " ";
            query = query + "WHERE hotel_id=? AND id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, qid);
            result_new  = prestate.executeUpdate();
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }

    if (BackUp.compareTo("1") == 0)
    {
        count_record = 0;
        NewFlag      = true;
    }

    if (count_record == 0)
    {
        if(!NewFlag)
        {
            query = "DELETE FROM question_master ";
            query = query + "WHERE hotel_id=? AND id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, qid);
            result_new  = prestate.executeUpdate();
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
            query = "DELETE FROM question_data ";
            query = query + "WHERE hotel_id=? AND id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, qid);
            result_new  = prestate.executeUpdate();
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }

        query = "INSERT INTO question_master SET ";
        query = query + "hotel_id=?, ";
        for( i = 0 ; i <= qid_max ; i++ )
        {
            if (id[i] != 0)
            {
                query = query + "q" + (i+1) +     "="  + (id[i]+required[i]*10)+ ", ";
                query = query + "q" + (i+1) + "_msg='" + col_msg[i] + "', ";
            }
        }
        query = query + "start="             + start_ymd   + ", ";
        query = query + "end="               + end_ymd     + ", ";
        query = query + "member_flag="       + member_flag + ", ";
        query = query + "msg='"              + col_msg_subtitle  + "', ";
        query = query + "duplicate_check="   + duplicate_check + " ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result_new  = prestate.executeUpdate();
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        query = "SELECT id FROM question_master WHERE hotel_id=?";
        query = query + " ORDER BY id  DESC";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           qid =result.getInt("id");    //新しく書き込んだidを読み込む。
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        for( i = 0 ; i <= qid_max ; i++ )
        {
            if (id[i] == 3)
            {
                query = "INSERT INTO question_data SET ";
                query = query + "hotel_id=?, ";
                query = query + "id=?, ";
                query = query + "q_id='q"             + (i+1)       + "', ";
                query = query + "sub_id=1, ";
                query = query + "value=1, ";
                query = query + "msg='" + ReplaceString.unEscape(col_data[i]) + "' ";
                prestate    = connection.prepareStatement(query);
                prestate.setString(1, hotelid);
                prestate.setInt(2, qid);
                result_new  = prestate.executeUpdate();
                DBConnection.releaseResources(result);
                DBConnection.releaseResources(prestate);
            }
            else if  (id[i] != 9 && id[i] != 0)
            {
                col_data[i] = ReplaceString.unEscape(col_data[i]) + "\r";
                int    value = 0;
                String temp = "";
                StringBuffer buf = new StringBuffer();
                for( int j = 0 ; j < col_data[i].length() ; j++ )
                {
                   char c = col_data[i].charAt(j);
                   switch( c )
                   {
                      case '\r':
                          temp = buf.toString();
                          if (temp.compareTo("") != 0)
                          {
                              value++;
                              query = "INSERT INTO question_data SET ";
                              query = query + "hotel_id=?, ";
                              query = query + "id=?, ";
                              query = query + "q_id='q"             + (i+1)       + "', ";
                              query = query + "sub_id="             + value       + ", ";
                              query = query + "value="              + value       + ", ";
                              query = query + "msg='"               + temp        + "' ";
                              prestate    = connection.prepareStatement(query);
                              prestate.setString(1, hotelid);
                              prestate.setInt(2, qid);
                              result_new  = prestate.executeUpdate();
                              DBConnection.releaseResources(result);
                              DBConnection.releaseResources(prestate);
                          }
                          buf = new StringBuffer();
                          break;
                      case '\n':
                          buf = new StringBuffer();
                          break;
                      default :
                          buf.append(c);
                          break;
                    }
                }
            }
        }
    }
    DBConnection.releaseResources(connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>アンケートメッセージ編集</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
<!--
function func() {
    top.Main.mainFrame.menu.location.href="qaedit_menu.jsp?Id=<%=qid%>";
}
// -->
</script>
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="setTimeout('func()',500)">
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
%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12">
                    <form action="qaedit_view.jsp?HotelId=<%= hotelid %>&Id=<%=qid%>" method="POST">
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
