<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/mag_hpedit_ini.jsp" %>

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
    int enddate = 29991231;
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    int               imedia_user = 0;
    int               level       = 0;
    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginhotel);
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    int  ret = 0;
    try
    {
        query = "UPDATE mag_hotel SET ";
        query = query + "mag_address=?, ";
        query = query + "message=?, ";
        if (imedia_user == 1)
        {
            query = query + "username=?, ";
            query = query + "password=?, ";
            query = query + "reg_address=?, ";
            query = query + "reg_username=?, ";
            query = query + "reg_password=?, ";
            query = query + "reg_message=?, ";
            query = query + "qr_identifier=?, ";
            query = query + "input_send_flag=?, ";
            query = query + "input_send_message=?, ";
            query = query + "request_message=?, ";
            query = query + "subject_hotelname=?, ";
        }
        query = query + "key_word=?, ";
        query = query + "member_only=?, ";
        query = query + "group_cancel_flag=?, ";
        query = query + "group_add_flag=?, ";
        query = query + "change_flag=?, ";
        query = query + "delete_flag=?, ";
        query = query + "add_flag=?, ";
        query = query + "report_mail_flag=? ";
        query = query + " WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);

        int i = 1;
        prestate.setString(i++,ReplaceString.getParameter(request,"mag_address"));
        prestate.setString(i++,request.getParameter("message"));
        if (imedia_user == 1)
        {
            prestate.setString(i++,ReplaceString.getParameter(request,"username"));
            prestate.setString(i++,ReplaceString.getParameter(request,"password"));
            prestate.setString(i++,ReplaceString.getParameter(request,"reg_address"));
            prestate.setString(i++,ReplaceString.getParameter(request,"reg_username"));
            prestate.setString(i++,ReplaceString.getParameter(request,"reg_password"));
            prestate.setString(i++,request.getParameter("reg_message"));
            prestate.setString(i++,ReplaceString.getParameter(request,"qr_identifier"));
            prestate.setInt(i++,Integer.parseInt(ReplaceString.getParameter(request,"input_send_flag")));
            prestate.setString(i++,request.getParameter("input_send_message"));
            prestate.setString(i++,ReplaceString.getParameter(request,"request_message"));
            prestate.setString(i++,ReplaceString.getParameter(request,"subject_hotelname"));
        }
        prestate.setString(i++,ReplaceString.getParameter(request,"key_word"));
        prestate.setInt(i++,Integer.parseInt(ReplaceString.getParameter(request,"search_member_only"))*1000 + Integer.parseInt(ReplaceString.getParameter(request,"qr_member_only"))*100 + Integer.parseInt(ReplaceString.getParameter(request,"reg_member_only"))*10 + Integer.parseInt(ReplaceString.getParameter(request,"member_only")));
        prestate.setInt(i++,Integer.parseInt(ReplaceString.getParameter(request,"group_cancel_flag")));
        prestate.setInt(i++,Integer.parseInt(ReplaceString.getParameter(request,"group_add_flag")));
        prestate.setInt(i++,Integer.parseInt(ReplaceString.getParameter(request,"change_flag")));
        prestate.setInt(i++,Integer.parseInt(ReplaceString.getParameter(request,"delete_flag")));
        prestate.setInt(i++,Integer.parseInt(ReplaceString.getParameter(request,"add_flag")));
        prestate.setInt(i++,Integer.parseInt(ReplaceString.getParameter(request,"report_mail_flag")));
        prestate.setString(i++,hotelid);

        ret         = prestate.executeUpdate();
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        if (imedia_user == 1)
        {
%>
<%=prestate.toString()%><br>
<%= e.toString() %><br>
<%
        }
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
%>
<%
    
    String[]  msg_title       = new String[8];
    String[]  msg             = new String[8];
    for( int i = 0 ; i < 8 ; i++ )
    {
        msg[i] = ReplaceString.getParameter(request,"col_msg"+(i+1));
        msg_title[i] = ReplaceString.getParameter(request,"col_msg"+(i+1) + "_title");
        if (msg_title[i] == null)
        {
            msg_title[i] = "";
        }
        else if (msg_title[i].equals(""))
        {
            msg_title[i] = "null";
        }
        if (Decoration[0][i] == 1)
        {
            msg[i] = msg[i].replace("<br>","<br/>");
            msg[i] = msg[i].replace("<BR>","<br/>");
            msg[i] = msg[i].replace("</strong>\r\n","</strong><br><br/>");
            msg[i] = msg[i].replace("</a>\r\n","</a><br><br/>");
            msg[i] = msg[i].replace("</font>\r\n","</font><br><br/>");
            msg[i] = msg[i].replace("</em>\r\n","</em><br><br/>");
            msg[i] = msg[i].replace("</u>\r\n","</u><br><br/>");
            msg[i] = msg[i].replace(">\r\n","><br/>");
            msg[i] = msg[i].replace("\r\n","<br/><br/>");
            msg[i] = msg[i].replace("><br/>",">\r\n");
            msg[i] = msg[i].replace("<br/><br/>","<br/>");
            msg[i] = msg[i].replace("&lt;br/&gt;<br/>","<br/>");
            msg[i] = msg[i].replace("&lt;br/&gt;<br/>","<br/>");

        }
     }

    boolean    magazine_flag      = false;
    int  data_type = 61;
    try
    {
        query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
        query = query + " AND data_type=" + data_type;
        query = query + " AND id=1";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if (result != null)
        {
            if( result.next() != false )
            {
                magazine_flag = true;
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        if (imedia_user == 1)
        {
%>
<%=query%><br>
<%= e.toString() %><br>
<%
        }
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    try
    {
        if(magazine_flag)
        {
            query = "UPDATE edit_event_info SET ";
        }
        else
        {
            query = "INSERT INTO edit_event_info SET ";
            query = query + "hotelid='"  + hotelid    + "', ";
            query = query + "data_type=" + data_type + ", ";
            query = query + "id=1, ";
        }
        query = query + "disp_idx=0, ";
        query = query + "disp_flg=1, ";
        query = query + "start_date=" + nowdate + ", ";
        query = query + "end_date="   + enddate + ", ";
        query = query + "msg1='" + ReplaceString.SQLEscape(msg[0]) + "', ";
        query = query + "msg2='" + ReplaceString.SQLEscape(msg[1]) + "', ";
        query = query + "msg3='" + ReplaceString.SQLEscape(msg[2]) + "', ";
        query = query + "msg4='" + ReplaceString.SQLEscape(msg[3]) + "', ";
        query = query + "msg5='" + ReplaceString.SQLEscape(msg[4]) + "', ";
        query = query + "msg6='" + ReplaceString.SQLEscape(msg[5]) + "', ";
        query = query + "msg7='" + ReplaceString.SQLEscape(msg[6]) + "', ";
        query = query + "msg8='" + ReplaceString.SQLEscape(msg[7]) + "', ";
        query = query + "msg1_title='" + ReplaceString.SQLEscape(msg_title[0]) + "', ";
        query = query + "msg2_title='" + ReplaceString.SQLEscape(msg_title[1]) + "', ";
        query = query + "msg3_title='" + ReplaceString.SQLEscape(msg_title[2]) + "', ";
        query = query + "msg4_title='" + ReplaceString.SQLEscape(msg_title[3]) + "', ";
        query = query + "msg5_title='" + ReplaceString.SQLEscape(msg_title[4]) + "', ";
        query = query + "msg6_title='" + ReplaceString.SQLEscape(msg_title[5]) + "', ";
        query = query + "msg7_title='" + ReplaceString.SQLEscape(msg_title[6]) + "', ";
        query = query + "msg8_title='" + ReplaceString.SQLEscape(msg_title[7]) + "', ";
        query = query + "upd_hotelid='" + loginhotel + "', ";
        query = query + "upd_userid='" + ownerinfo.DbUserId + "', ";
        query = query + "last_update='" + nowdate + "', ";
        query = query + "last_uptime='" + nowtime + "' ";
        if(magazine_flag)
        {
           query = query + " WHERE hotelid='" + hotelid + "' AND data_type=" + data_type + " AND id=1";
        }
        prestate    = connection.prepareStatement(query);
        ret         = prestate.executeUpdate();
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        if (imedia_user == 1)
        {
%>
<%=query%><br>
<%= e.toString() %><br>
<%
        }
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>メルマガ管理HP編集</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
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
    if( ret == 1 )
    {
%>
登録しました。<br>

<%
    }
    else
    {
%>
<script> alert("登録に失敗しました。")</script>
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="mag_hotel_hpedit.jsp?HotelId=<%= hotelid %>" method="POST">
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
