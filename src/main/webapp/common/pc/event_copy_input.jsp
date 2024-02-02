<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // ƒZƒbƒVƒ‡ƒ“‚ÌŠm”F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
    String str = "";
    String han = "±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏĞÑÒÓÔÕÖ×ØÙÚÛÜ¦İ§¨©ª«¬­®¯°¢£!?()abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'1234567890$%&=<>@{}¤¡-¥*+_:;\\#/.\" ";
    han = han + "³¶·¸¹º»¼½¾¿ÀÁÂÃÄÊËÌÍÎ";
    han = han + "ÊËÌÍÎ";
//  0`148
    String txt = "ƒAƒCƒEƒGƒIƒJƒLƒNƒPƒRƒTƒVƒXƒZƒ\ƒ^ƒ`ƒcƒeƒgƒiƒjƒkƒlƒmƒnƒqƒtƒwƒzƒ}ƒ~ƒ€ƒƒ‚ƒ„ƒ†ƒˆƒ‰ƒŠƒ‹ƒŒƒƒƒ’ƒ“ƒ@ƒBƒDƒFƒHƒƒƒ…ƒ‡ƒb[uvIHij‚‚‚‚ƒ‚„‚…‚†‚‡‚ˆ‚‰‚Š‚‹‚Œ‚‚‚‚‚‘‚’‚“‚”‚•‚–‚—‚˜‚™‚š‚`‚a‚b‚c‚d‚e‚f‚g‚h‚i‚j‚k‚l‚m‚n‚o‚p‚q‚r‚s‚t‚u‚v‚w‚x‚yf‚P‚Q‚R‚S‚T‚U‚V‚W‚X‚O“•ƒ„—opAB|E–{QFG”^Dh@";
//  149`169
    txt = txt + "ƒ”ƒKƒMƒOƒQƒSƒUƒWƒYƒ[ƒ]ƒ_ƒaƒdƒfƒhƒoƒrƒuƒxƒ{";
//@170`174
    txt = txt + "ƒpƒsƒvƒyƒ|";
    int   point_daku    =149;
    int   point_handaku =170;

    String StoreCount  = request.getParameter("StoreCount");
    if    (StoreCount == null) StoreCount="0";
    int    store_count = Integer.parseInt(StoreCount);
    String TypeCount   = request.getParameter("TypeCount");
    if    (TypeCount  == null) TypeCount="0";
    int    type_count  = Integer.parseInt(TypeCount);

    String loginhotel = (String)session.getAttribute("LoginHotelId");
    String hotelid    = (String)session.getAttribute("SelectHotel");

    Calendar cal = Calendar.getInstance();
    int nowdate  = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime  = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    int input_data_type  = Integer.parseInt(request.getParameter("DataType"));
    int input_id         = Integer.parseInt(request.getParameter("Id"));
    String  InputDispFlg = request.getParameter("col_disp_flg");
    if  (InputDispFlg   == null) InputDispFlg = "2";
    int input_disp_flg   = Integer.parseInt(InputDispFlg);
    int input_disp_idx   = Integer.parseInt(request.getParameter("col_disp_idx"));
    int input_memberonly = Integer.parseInt(request.getParameter("col_member_only"));
    int input_smart_flg  = Integer.parseInt(request.getParameter("col_smart_flg"));

    int input_start_ymd = 0;
    int input_end_ymd = 0;
    int input_start_yy = Integer.parseInt(request.getParameter("col_start_yy"));
    int input_start_mm = Integer.parseInt(request.getParameter("col_start_mm"));
    int input_start_dd = Integer.parseInt(request.getParameter("col_start_dd"));
    input_start_ymd = input_start_yy*10000 + input_start_mm*100 + input_start_dd;
    int input_end_yy = Integer.parseInt(request.getParameter("col_end_yy"));
    int input_end_mm = Integer.parseInt(request.getParameter("col_end_mm"));
    int input_end_dd = Integer.parseInt(request.getParameter("col_end_dd"));
    input_end_ymd = input_end_yy*10000 + input_end_mm*100 + input_end_dd;

    int start_time = 0;
    int end_time = 0;
    int start_hour  = Integer.parseInt(request.getParameter("col_start_hour"));
    int start_minute= Integer.parseInt(request.getParameter("col_start_minute"));
    start_time = start_hour*10000 + start_minute*100;

    int end_hour  = Integer.parseInt(request.getParameter("col_end_hour"));
    int end_minute= Integer.parseInt(request.getParameter("col_end_minute"));
    end_time   = end_hour*10000 + end_minute*100 + 59;


    String    input_title           = "";
    String    input_title_color     = "";
    String[]  input_msg_title       = new String[8];
    String[]  input_msg_title_color = new String[8];
    String[]  input_msg             = new String[8];
    String    han_title             = "";
    String[]  han_msg_title         = new String[8];
    String[]  han_msg               = new String[8];

    int   i = 0;
    int   j = 0;
    int   k = 0;
    char  c;
    int   n = 0;
    int   m = 0;
    String query ="";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int result_new = 0;
    DateEdit  de = new DateEdit();

    connection  = DBConnection.getConnection();
    query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
    query = query + " AND data_type=" + input_data_type;
    query = query + " AND id="        + input_id;
    prestate    = connection.prepareStatement(query);
    result      = prestate.executeQuery();
    if (result != null)
    {
        if (result.next() != false )
        {
            input_title       = result.getString("title");
            input_title_color = result.getString("title_color");
            for( i = 0 ; i < 8 ; i++ )
            {
                input_msg_title[i]       = result.getString("msg" + Integer.toString(i+1) + "_title");
                input_msg_title_color[i] = result.getString("msg" + Integer.toString(i+1) + "_title_color");
                input_msg[i]             = result.getString("msg" + Integer.toString(i+1));
//                input_msg[i] = input_msg[i].replace("<br>","<br/>");
//                input_msg[i] = input_msg[i].replace("<BR>","<br/>");
//                input_msg[i] = input_msg[i].replace("<br/>\r\n","<br/><br/>");
//                input_msg[i] = input_msg[i].replace("\r\n","<br/><br/>");
//                input_msg[i] = input_msg[i].replace("<br/><br/>","<br/>\r\n");
            }
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    if (input_data_type%2 != 0)  //”¼Šp•ÏŠ·
    {
        str = "";
        for (k=0; k<input_title.length() ; k++)
        {
            c = input_title.charAt(k);
            n = txt.indexOf(c);
            if (n >= 0) 
            {
                c = han.charAt(n);
            }
            str += c;
            if (n >= point_handaku) 
            {
                str += "ß";
            }
            else if (n  >= point_daku)
            {
                str += "Ş";
            }
        }
        han_title = str;

        for (m=0; m<8 ; m++)
        {
            str = "";
            for (k=0; k<input_msg[m].length() ; k++)
            {
                c = input_msg[m].charAt(k);
                n = txt.indexOf(c);
                if (n >= 0) 
                {
                    c = han.charAt(n);
                }
                str += c;
                if (n >= point_handaku) 
                {
                    str += "ß";
                }
                else if (n  >= point_daku)
                {
                    str += "Ş";
                }
            }
            han_msg[m] = str;

            str = "";
            for (k=0; k<input_msg_title[m].length() ; k++)
            {
                c = input_msg_title[m].charAt(k);
                n = txt.indexOf(c);
                if (n >= 0) 
                {
                    c = han.charAt(n);
                }
                str += c;
                if (n >= point_handaku) 
                {
                    str += "ß";
                }
                else if (n  >= point_daku)
                {
                    str += "Ş";
                }
            }
            if((input_msg[m].compareTo(" ")!= 0 && input_msg[m].compareTo("")!= 0)&&
               (str.compareTo("") == 0 || str.compareTo(" ") == 0))
            {
                han_msg_title[m] = han_title;
            }
            else
            {
                han_msg_title[m] = str;
            }
        }
    }

    String  CopyStore  = "";
    String  CopyType   = "";
    int     copy_type  = 0;
    boolean Exist      = false;

    for( i = 1 ; i <= store_count ; i++ )
    {
        for( j = 1 ; j <= type_count ; j++ )
        {
            CopyStore = request.getParameter("CopyStore"+i);
            CopyType  = request.getParameter("CopyType"+j);
            String[] datas = new String[]{ CopyStore, CopyType };
            for( String data : datas )
            {
                if(CheckString.isValidParameter(data) && !CheckString.numAlphaCheck(data))
                {
                    response.sendError(400);
                    return;
                }
            }
            Exist = false;
            if (CopyStore != null && CopyType != null)
            {
                if (CopyStore.compareTo("") != 0)
                {
                    copy_type  = Integer.parseInt(CopyType);
                    query = "SELECT * FROM edit_event_info WHERE hotelid='" + CopyStore + "'";
                    query = query + " AND data_type=" + copy_type;
                    query = query + " AND disp_idx="  + input_disp_idx;
                    query = query + " AND start_date="+ input_start_ymd;
                    query = query + " AND end_date="  + input_end_ymd;
                    prestate    = connection.prepareStatement(query);
                    result      = prestate.executeQuery();
                    if (result != null)
                    {
                        if (result.next() != false )
                        {
                            Exist = true;
                        }
                    }
                    DBConnection.releaseResources(result);
                    DBConnection.releaseResources(prestate);
                    if(!Exist)
                    {
                        query = "INSERT INTO hotenavi.edit_event_info SET ";
                        query = query + "hotelid='"     + CopyStore + "', ";
                        query = query + "data_type="    + copy_type  + ", ";
                        query = query + "disp_idx="     + input_disp_idx + ", ";
                        query = query + "disp_flg="     + input_disp_flg + ", ";
                        query = query + "start_date="   + input_start_ymd + ", ";
                        query = query + "end_date="     + input_end_ymd + ", ";
                        query = query + "start_time="        + start_time + ", ";
                        query = query + "end_time="          + end_time + ", ";
                        if (input_data_type%2 != 0 && copy_type%2 == 0)
                        {
                            query = query + "title='"             + ReplaceString.SQLEscape(han_title) + "', ";
                            query = query + "title_color='#000000', ";
                            query = query + "msg1_title='"        + ReplaceString.SQLEscape(han_msg_title[0]) + "', ";
                            query = query + "msg1_title_color='#663333', ";
                            query = query + "msg1='"              + ReplaceString.SQLEscape(han_msg[0]) + "', ";
                            query = query + "msg2_title='"        + ReplaceString.SQLEscape(han_msg_title[1]) + "', ";
                            query = query + "msg2_title_color='#663333', ";
                            query = query + "msg2='"              + ReplaceString.SQLEscape(han_msg[1]) + "', ";
                            query = query + "msg3_title='"        + ReplaceString.SQLEscape(han_msg_title[2]) + "', ";
                            query = query + "msg3_title_color='#663333', ";
                            query = query + "msg3='"              + ReplaceString.SQLEscape(han_msg[2]) + "', ";
                            query = query + "msg4_title='"        + ReplaceString.SQLEscape(han_msg_title[3]) + "', ";
                            query = query + "msg4_title_color='#663333', ";
                            query = query + "msg4='"              + ReplaceString.SQLEscape(han_msg[3]) + "', ";
                            query = query + "msg5_title='"        + ReplaceString.SQLEscape(han_msg_title[4]) + "', ";
                            query = query + "msg5_title_color='#663333', ";
                            query = query + "msg5='"              + ReplaceString.SQLEscape(han_msg[4]) + "', ";
                            query = query + "msg6_title='"        + ReplaceString.SQLEscape(han_msg_title[5]) + "', ";
                            query = query + "msg6_title_color='#663333', ";
                            query = query + "msg6='"              + ReplaceString.SQLEscape(han_msg[5]) + "', ";
                            query = query + "msg7_title='"        + ReplaceString.SQLEscape(han_msg_title[6]) + "', ";
                            query = query + "msg7_title_color='#663333', ";
                            query = query + "msg7='"              + ReplaceString.SQLEscape(han_msg[6]) + "', ";
                            query = query + "msg8_title='"        + ReplaceString.SQLEscape(han_msg_title[7]) + "', ";
                            query = query + "msg8_title_color='#663333', ";
                            query = query + "msg8='"              + ReplaceString.SQLEscape(han_msg[7]) + "', ";
                        }
                        else
                        {
                            query = query + "title='"             + ReplaceString.SQLEscape(input_title) + "', ";
                            query = query + "title_color='"       + ReplaceString.SQLEscape(input_title_color) + "', ";
                            query = query + "msg1_title='"        + ReplaceString.SQLEscape(input_msg_title[0]) + "', ";
                            query = query + "msg1_title_color='"  + ReplaceString.SQLEscape(input_msg_title_color[0]) + "', ";
                            query = query + "msg1='"              + ReplaceString.SQLEscape(input_msg[0]) + "', ";
                            query = query + "msg2_title='"        + ReplaceString.SQLEscape(input_msg_title[1]) + "', ";
                            query = query + "msg2_title_color='"  + ReplaceString.SQLEscape(input_msg_title_color[1]) + "', ";
                            query = query + "msg2='"              + ReplaceString.SQLEscape(input_msg[1]) + "', ";
                            query = query + "msg3_title='"        + ReplaceString.SQLEscape(input_msg_title[2]) + "', ";
                            query = query + "msg3_title_color='"  + ReplaceString.SQLEscape(input_msg_title_color[2]) + "', ";
                            query = query + "msg3='"              + ReplaceString.SQLEscape(input_msg[2]) + "', ";
                            query = query + "msg4_title='"        + ReplaceString.SQLEscape(input_msg_title[3]) + "', ";
                            query = query + "msg4_title_color='"  + ReplaceString.SQLEscape(input_msg_title_color[3]) + "', ";
                            query = query + "msg4='"              + ReplaceString.SQLEscape(input_msg[3]) + "', ";
                            query = query + "msg5_title='"        + ReplaceString.SQLEscape(input_msg_title[4]) + "', ";
                            query = query + "msg5_title_color='"  + ReplaceString.SQLEscape(input_msg_title_color[4]) + "', ";
                            query = query + "msg5='"              + ReplaceString.SQLEscape(input_msg[4]) + "', ";
                            query = query + "msg6_title='"        + ReplaceString.SQLEscape(input_msg_title[5]) + "', ";
                            query = query + "msg6_title_color='"  + ReplaceString.SQLEscape(input_msg_title_color[5]) + "', ";
                            query = query + "msg6='"              + ReplaceString.SQLEscape(input_msg[5]) + "', ";
                            query = query + "msg7_title='"        + ReplaceString.SQLEscape(input_msg_title[6]) + "', ";
                            query = query + "msg7_title_color='"  + ReplaceString.SQLEscape(input_msg_title_color[6]) + "', ";
                            query = query + "msg7='"              + ReplaceString.SQLEscape(input_msg[6]) + "', ";
                            query = query + "msg8_title='"        + ReplaceString.SQLEscape(input_msg_title[7]) + "', ";
                            query = query + "msg8_title_color='"  + ReplaceString.SQLEscape(input_msg_title_color[7]) + "', ";
                            query = query + "msg8='"              + ReplaceString.SQLEscape(input_msg[7]) + "', ";
                        }
                        query = query + "member_only="  + input_memberonly + ", ";
                        query = query + "smart_flg="    + input_smart_flg + ", ";
                        query = query + "add_hotelid='" + loginhotel + "', ";
                        query = query + "add_date='"    + nowdate + "', ";
                        query = query + "add_time='"    + nowtime + "', ";
                        query = query + "add_userid='"  + ownerinfo.DbUserId + "', ";
                        query = query + "upd_hotelid='" + loginhotel + "', ";
                        query = query + "upd_userid='"  + ownerinfo.DbUserId + "', ";
                        query = query + "last_update='" + nowdate + "', ";
                        query = query + "last_uptime='" + nowtime + "' ";
                        prestate    = connection.prepareStatement(query);
                        result_new  = prestate.executeUpdate();
       if (CopyStore.equals("happyhotel"))
       {
           DBSync.publish(prestate.toString().split(":",2)[1]);
           DBSync.publish(prestate.toString().split(":",2)[1],true);
       }
                        DBConnection.releaseResources(result);
                        DBConnection.releaseResources(prestate);
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
<title>HP•ÒW</title>
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
                <font color="#FFFFFF">“o˜^Šm”F</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ‚±‚±‚©‚ç•\ -->
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
“o˜^‚µ‚Ü‚µ‚½B<br>
<%
    }
    else
    {
%>
“o˜^‚É¸”s‚µ‚Ü‚µ‚½B<br>
<br/>
<br/>
*•\¦‡”ÔEŠJn“ú•tEI—¹“ú•t‚ª‚·‚×‚Ä“¯‚¶ê‡‚É‚ÍƒRƒs[‚³‚ê‚Ü‚¹‚ñB<br/>
İ’è“à—e‚ğ‚²Šm”F‚­‚¾‚³‚¢B<br>
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="event_list.jsp?HotelId=<%= hotelid %>&DataType=<%= input_data_type %>" method="POST">
                      <INPUT name="submit_ret" type=submit value=–ß‚é >
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
      <!-- ‚±‚±‚Ü‚Å -->
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
