<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/ngword_ini.jsp" %>

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
    String requestUri = request.getRequestURI();
    boolean DebugMode = false;
    if (requestUri.indexOf("_debug_/demo/") != -1)
    {
       DebugMode = true;
    }

    ReplaceString rs = new ReplaceString();

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    DateEdit  de = new DateEdit();
    int    nowdate        = Integer.parseInt(de.getDate(2));
    int    nowtime        = Integer.parseInt(de.getTime(1));
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
			hotelid="0";
%>
			<script type="text/javascript">			<!--
			var dd = new Date();
			setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
			//-->
			</script>
<%
		}
	}
    String data_type = ReplaceString.getParameter(request,"DataType");
    String id = ReplaceString.getParameter(request,"Id");
    String query ="";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int result_new = 0;

    // 日付ﾃﾞｰﾀの編集
    int start_ymd = 0;
    int end_ymd = 0;
    int old_ymd = 0;
    int start_time = 0;
    int end_time = 0;
    int old_time = 0;

    int start_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_start_yy"));
    int start_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_start_mm"));
    int start_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_start_dd"));
    start_ymd = start_yy*10000 + start_mm*100 + start_dd;

    int end_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_end_yy"));
    int end_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_end_mm"));
    int end_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_end_dd"));
    end_ymd = end_yy*10000 + end_mm*100 + end_dd;

    int start_hour  = Integer.parseInt(ReplaceString.getParameter(request,"col_start_hour"));
    int start_minute= Integer.parseInt(ReplaceString.getParameter(request,"col_start_minute"));
    start_time = start_hour*10000 + start_minute*100;

    int end_hour  = Integer.parseInt(ReplaceString.getParameter(request,"col_end_hour"));
    int end_minute= Integer.parseInt(ReplaceString.getParameter(request,"col_end_minute"));
    end_time   = end_hour*10000 + end_minute*100 + 59;




    if  (start_ymd > nowdate)//先の日付
    {
        if (start_time == 0)//開始時刻が入っていない場合は前日で終了
        {
            old_ymd  = de.addDay(start_ymd , -1);
            old_time = start_time;
        }
        else
        {
            old_ymd = start_ymd;
            if (start_time / 100 % 100 == 0)
            {
                old_time = start_time - 4100; //00分なので、59分にするため
            }
            else
            {
                old_time = start_time - 100;
            }
        }
    }
    else //当日以前の場合
    {
        old_ymd  = nowdate;
        old_time = (nowtime / 100 ) * 100;
    }

    String  old_disp_flg = ReplaceString.getParameter(request,"old_disp_flg");
    if      (old_disp_flg == null) old_disp_flg = "1";
    String  col_disp_flg = ReplaceString.getParameter(request,"col_disp_flg");
    if      (col_disp_flg == null) col_disp_flg = "0";

    String    NGword          = "";
    boolean   NGwordCheck     = false;
    String    title           = "";
    String[]  msg_title       = new String[8];
    String[]  msg             = new String[8];
    String    msg_check       = "";

    title     = ReplaceString.getParameter(request,"col_title");
    msg_check = title.replace(" ","");
    if (!NGwordCheck)
    {
        for( int j = 0 ; j < NGwordList.length ; j++ )
        {
            if (msg_check.indexOf(NGwordList[j]) > 0 && !hotelid.equals("alm-ts"))
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
            msg_title[i] = ReplaceString.getParameter(request,"col_msg"+(i+1)+ "_title");
            msg[i] = ReplaceString.getParameter(request,"col_msg"+(i+1));
            msg[i] = msg[i].replace("&lt;br&gt;","<br/>");
            msg[i] = msg[i].replace("<br>","\r\n");
            msg[i] = msg[i].replace("&lt;BR&gt;","<br/>");
            msg[i] = msg[i].replace("<BR>","<br/>");
            msg[i] = msg[i].replace("&lt;/strong&gt;\r\n","</strong><br/>\r\n");
            msg[i] = msg[i].replace("</strong>\r\n","</strong><br/>\r\n");
            msg[i] = msg[i].replace("&lt;/a&gt;\r\n","</a><br/>\r\n");
            msg[i] = msg[i].replace("</a>\r\n","</a><br/>\r\n");
            msg[i] = msg[i].replace("&lt;/font&gt;\r\n","</font><br/>\r\n");
            msg[i] = msg[i].replace("</font>\r\n","</font><br/>\r\n");
            msg[i] = msg[i].replace("&lt;/em&gt;\r\n","</em><br/>\r\n");
            msg[i] = msg[i].replace("</em>\r\n","</em><br/>\r\n");
            msg[i] = msg[i].replace("&lt;u&gt;\r\n","</u><br/>\r\n");
            msg[i] = msg[i].replace("/u>\r\n","</u><br/>\r\n");
            msg[i] = msg[i].replace("&gt;\r\n",">%<br/>%");
            msg[i] = msg[i].replace(">\r\n",">%<br/>%");
            msg[i] = msg[i].replace("\r\n","<br/>\r\n");
            msg[i] = msg[i].replace("&gt;%&lt;br/&gt;%",">\r\n");
            msg[i] = msg[i].replace(">%<br/>%",">\r\n");
            msg[i] = msg[i].replace("TARGET","target");
            msg[i] = msg[i].replace("BLANK","blank");
            msg[i] = msg[i].replace("SELF","self");
            msg[i] = msg[i].replace("target=\"_blank\"","");
            msg[i] = msg[i].replace("target=_blank","");
            msg[i] = msg[i].replace("target=\"_self\"","");
            msg[i] = msg[i].replace("target=_self","");
            msg[i] = msg[i].replace("HTTP","http");
            msg[i] = msg[i].replace("HTTPS","https");
            msg[i] = msg[i].replace("HREF","href");
            msg[i] = msg[i].replace("href=\"http","target=\"_blank\" href=\"http");
            msg_check = msg[i].replace(" ","") + msg_title[i].replace(" ","");
            msg_check =rs.replaceAlphaSmall(ReplaceString.SQLEscape(msg_check));
            if (!NGwordCheck)
            {
                for( int j = 0 ; j < NGwordList.length ; j++ )
                {
                    if (msg_check.indexOf(NGwordList[j]) >= 0 && !hotelid.equals("alm-ts") && (!DebugMode || msg_check.indexOf("<imedia>")!=0))
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

    // メンバー専用
    int memberonly = 0;
    if( ReplaceString.getParameter(request,"col_member_only") != null )
    {
        memberonly = Integer.parseInt( ReplaceString.getParameter(request,"col_member_only") );
    }

    // スマホ/PC
    int smart_flg = 0;
    if( ReplaceString.getParameter(request,"col_smart_flg") != null )
    {
        smart_flg = Integer.parseInt( ReplaceString.getParameter(request,"col_smart_flg") );
    }


    if (!NGwordCheck)
    {
       connection  = DBConnection.getConnection();
       if( id.compareTo("0") == 0 || BackUp.compareTo("1") == 0)
       {
           query = "INSERT INTO hotenavi.edit_event_info SET ";
           query = query + "hotelid=?, ";
           query = query + "data_type=?, ";
           query = query + "add_hotelid=?, ";
           query = query + "add_userid=?, ";
           query = query + "add_date=?, ";
           query = query + "add_time=?, ";
       }
       else
       {
           query = "UPDATE hotenavi.edit_event_info SET ";
       }
           query = query + "disp_idx=?, ";
           query = query + "disp_flg=?, ";
           query = query + "start_date=?, ";
           query = query + "end_date=?, ";
           query = query + "start_time=?, ";
           query = query + "end_time=?, ";
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
           query = query + "smart_flg=?, ";
           query = query + "upd_hotelid=?, ";
           query = query + "upd_userid=?, ";
           query = query + "last_update=?, ";
           query = query + "last_uptime=? ";
       if( id.compareTo("0") != 0 && BackUp.compareTo("1") != 0)
       {
           query = query + "WHERE hotelid=? AND data_type=? AND id=?";
       }
       prestate    = connection.prepareStatement(query);
       int offset = 0;
       if( id.compareTo("0") == 0 || BackUp.compareTo("1") == 0)
       {
            prestate.setString(1, hotelid);
            prestate.setString(2, data_type);
            prestate.setString(3, loginHotelId);
            prestate.setInt(4, ownerinfo.DbUserId);
            prestate.setInt(5, nowdate);
            prestate.setInt(6, nowtime);
            offset = 6;
        }
        prestate.setInt(offset + 1, Integer.parseInt(ReplaceString.getParameter(request,"col_disp_idx")));
        prestate.setInt(offset + 2, Integer.parseInt(col_disp_flg));
        prestate.setInt(offset + 3, start_ymd);
        prestate.setInt(offset + 4, end_ymd);
        prestate.setInt(offset + 5, start_time);
        prestate.setInt(offset + 6, end_time);
        prestate.setString(offset + 7, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_title")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 8, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_title_color")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 9, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg1_title")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 10, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg1_title_color")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 11, ReplaceString.SQLEscape(msg[0]).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 12, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg2_title")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 13, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg2_title_color")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 14, ReplaceString.SQLEscape(msg[1]).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 15, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg3_title")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 16, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg3_title_color")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 17, ReplaceString.SQLEscape(msg[2]).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 18, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg4_title")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 19, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg4_title_color")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 20, ReplaceString.SQLEscape(msg[3]).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 21, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg5_title")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 22, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg5_title_color")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 23, ReplaceString.SQLEscape(msg[4]).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 24, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg6_title")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 25, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg6_title_color")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 26, ReplaceString.SQLEscape(msg[5]).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 27, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg7_title")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 28, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg7_title_color")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 29, ReplaceString.SQLEscape(msg[6]).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 30, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg8_title")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 31, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg8_title_color")).replace("\\\\","\\").replace("''","'"));
        prestate.setString(offset + 32, ReplaceString.SQLEscape(msg[7]).replace("\\\\","\\").replace("''","'"));
        prestate.setInt(offset + 33, memberonly);
        prestate.setInt(offset + 34, smart_flg);
        prestate.setString(offset + 35, loginHotelId);
        prestate.setInt(offset + 36, ownerinfo.DbUserId);
        prestate.setInt(offset + 37, nowdate);
        prestate.setInt(offset + 38, nowtime);
        if( id.compareTo("0") != 0 && BackUp.compareTo("1") != 0)
        {
            prestate.setString(offset + 39, hotelid);
            prestate.setString(offset + 40, data_type);
            prestate.setInt(offset + 41, Integer.parseInt(id));
        }
       result_new  = prestate.executeUpdate();
%>
<script>console.log("<%=hotelid%>");console.log("<%=prestate.toString()%>");</script>
<%
       if (hotelid.equals("happyhotel"))
       {
           DBSync.publish(prestate.toString().split(":",2)[1]);
           DBSync.publish(prestate.toString().split(":",2)[1],true);
       }
       if (BackUp.compareTo("1") == 0 && col_disp_flg.compareTo("1")==0)
       {
           query = "UPDATE hotenavi.edit_event_info SET";
           query = query + " end_date=?, ";
           query = query + " end_time=?, ";
           query = query + " disp_flg=?, ";
           query = query + "last_update=?, ";
           query = query + "last_uptime=? ";
           query = query + " WHERE hotelid=? AND data_type=? AND id=?";
           prestate    = connection.prepareStatement(query);
           prestate.setInt(1, old_ymd);
           prestate.setInt(2, old_time);
           prestate.setString(3, old_disp_flg);
           prestate.setInt(4, nowdate);
           prestate.setInt(5, nowtime);
           prestate.setString(6, hotelid);
           prestate.setString(7, data_type);
           prestate.setInt(8, Integer.parseInt(id));
           result_new  = prestate.executeUpdate();
           if (hotelid.equals("happyhotel"))
           {
               DBSync.publish(prestate.toString().split(":",2)[1]);
               DBSync.publish(prestate.toString().split(":",2)[1],true);
           }
        }
        DBConnection.releaseResources(result,prestate,connection);
    }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>HP編集</title>
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
<%
    if (TargetAll)
    {
%>
                    <form action="event_list_all.jsp?DataType=<%= data_type %>&DispType=0&SortType=0" method="POST">
<%
    }
    else
    {
%>
                    <form action="event_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>" method="POST">
<%
    }
%>
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
