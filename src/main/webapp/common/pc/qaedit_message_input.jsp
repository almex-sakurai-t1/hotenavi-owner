<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/ngword_ini.jsp" %>
<%@ include file="../../common/pc/qaedit_message_ini.jsp" %>
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
    ReplaceString rs = new ReplaceString();

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    String BackUp        = ReplaceString.getParameter(request,"BackUp");
    if    (BackUp == null) BackUp="0";

    boolean    TargetAll  = false;

    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");

if  (hotelid.compareTo("all") == 0)
{
    TargetAll  = true;
    hotelid = ReplaceString.getParameter(request,"HotelId");
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
}
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
//    if  (start_ymd > nowdate)
//    {
//        old_ymd = de.addDay(start_ymd , -1);
//    }
//    else
//    {
//        old_ymd = de.addDay(nowdate , -1);
//    }
    String  col_disp_flg = ReplaceString.getParameter(request,"col_disp_flg");
    if      (col_disp_flg == null) col_disp_flg = "0";


    String    NGword          = "";
    boolean   NGwordCheck     = false;
    String    title           = "";
    String[]  msg             = new String[8];
    for( int i = 0 ; i < 8 ; i++ )
    {
        msg[i] = "";
    }
    String    msg_check       = "";

    msg_check = title.replace(" ","");
    if (!NGwordCheck)
    {
        for( int j = 0 ; j < NGwordList.length ; j++ )
        {
            if (msg_check.indexOf(NGwordList[j]) > 0)
            {
                NGword      = NGwordList[j];
                NGwordCheck = true;
                break;
            }
        }
    }
    if (!NGwordCheck)
    {
        for( int i = 0 ; i < 8 ; i++ )
        {
            if( Explain[0][i].length() != 0 )
            {
                msg[i] = ReplaceString.getParameter(request,"col_msg"+(i+1));
                if (msg[i] == null) msg[i] = "";
                msg[i] = msg[i].replace("<br>","<br/>");
                msg[i] = msg[i].replace("<BR>","<br/>");
                msg[i] = msg[i].replace("</strong>\r\n","</strong><br/>\r\n");
                msg[i] = msg[i].replace("</a>\r\n","</a><br/>\r\n");
                msg[i] = msg[i].replace("</font>\r\n","</font><br/>\r\n");
                msg[i] = msg[i].replace("</em>\r\n","</em><br/>\r\n");
                msg[i] = msg[i].replace("</u>\r\n","</u><br/>\r\n");
                msg[i] = msg[i].replace(">\r\n",">%<br/>%");
                msg[i] = msg[i].replace("\r\n","<br/>\r\n");
                msg[i] = msg[i].replace(">%<br/>%",">\r\n");
                msg_check = msg[i].replace(" ","");
                msg_check =rs.replaceAlphaSmall(msg_check);
                if (!NGwordCheck)
                {
                    for( int j = 0 ; j < NGwordList.length ; j++ )
                    {
                        if (msg_check.indexOf(NGwordList[j]) >= 0)
                        {
                            NGword      = NGwordList[j];
                            NGwordCheck = true;
                            break;
                        }
                    }
                }
                else
                {
                    break;
                }
            }
        }
    }

    if (!NGwordCheck)
    {
       connection  = DBConnection.getConnection();
       if( id.compareTo("0") == 0 || BackUp.compareTo("1") == 0)
       {
           query = "INSERT INTO edit_event_info SET ";
           query = query + "hotelid='"          + hotelid      + "', ";
           query = query + "data_type="         + data_type    + ", ";
           query = query + "add_hotelid='"      + loginHotelId + "', ";
           query = query + "add_userid='"       + ownerinfo.DbUserId + "', ";
           query = query + "add_date='"         + nowdate + "', ";
           query = query + "add_time='"         + nowtime + "', ";
       }
       else
       {
           query = "UPDATE edit_event_info SET ";
       }
           query = query + "disp_flg="          + Integer.parseInt(col_disp_flg)  + ", ";
           query = query + "start_date="        + start_ymd + ", ";
           query = query + "end_date="          + end_ymd + ", ";
           query = query + "msg1='"             + ReplaceString.SQLEscape(msg[0]) + "', ";
           query = query + "msg2='"             + ReplaceString.SQLEscape(msg[1]) + "', ";
           query = query + "msg3='"             + ReplaceString.SQLEscape(msg[2]) + "', ";
           query = query + "msg4='"             + ReplaceString.SQLEscape(msg[3]) + "', ";
           query = query + "msg5='"             + ReplaceString.SQLEscape(msg[4]) + "', ";
           query = query + "msg6='"             + ReplaceString.SQLEscape(msg[5]) + "', ";
           query = query + "msg7='"             + ReplaceString.SQLEscape(msg[6]) + "', ";
           query = query + "msg8='"             + ReplaceString.SQLEscape(msg[7]) + "', ";
           query = query + "upd_hotelid='"      + loginHotelId + "', ";
           query = query + "upd_userid='"       + ownerinfo.DbUserId + "', ";
           query = query + "last_update='"      + nowdate + "', ";
           query = query + "last_uptime='"      + nowtime + "' ";
       if( id.compareTo("0") != 0 && BackUp.compareTo("1") != 0)
       {
           query = query + " WHERE hotelid='"   + hotelid + "' AND data_type=" + data_type + " AND id=" + id;
       }

       prestate    = connection.prepareStatement(query);
       result_new  = prestate.executeUpdate();
//       if (BackUp.compareTo("1") == 0)
//       {
//          query = "UPDATE edit_event_info SET";
//           query = query + " end_date=" + old_ymd + ", ";
//           query = query + "last_update='" + nowdate + "', ";
//           query = query + "last_uptime='" + nowtime + "' ";
//           query = query + " WHERE hotelid='" + hotelid + "' AND data_type=" + data_type + " AND id=" + id;
//           prestate    = connection.prepareStatement(query);
//           result_new  = prestate.executeUpdate();
//        }
        DBConnection.releaseResources(result,prestate,connection);
    }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>アンケートメッセージ編集</title>
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
    else if (NGwordCheck)
        {
%>
        入力できない文字列がありますので、更新できませんでした　・・・　<%=NGword.replace("<","")%><br>
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
                    <form action="qaedit_message.jsp?HotelId=<%= hotelid %>" method="POST">
<%
    if (NGwordCheck)
    {
%>
                      <INPUT type="button" onclick="history.back();" value=戻る>
<%
    }
    else
    {
%>
                      <INPUT name="submit_ret" type=submit value=戻る >
<%
    }
%>
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
