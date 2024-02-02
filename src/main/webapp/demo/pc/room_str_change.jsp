<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
     hotelid = (String)session.getAttribute("SelectHotel");
    }
    if( hotelid == null )
    {
%>
 
        <jsp:forward page="error/error.html" />
<%
    }
    String BackUp        = request.getParameter("BackUp");
    if    (BackUp == null) BackUp="0";
    String disp_type     = request.getParameter("DispType");
    String moto_str      = request.getParameter("moto_str");
    String new_str       = request.getParameter("new_str");
    String moto_date     = request.getParameter("moto_date");
    if    (moto_date.compareTo("") == 0) moto_date = "0";
    String new_date      = request.getParameter("new_date");
    if    (new_date.compareTo("") == 0) new_date = "0";
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    int    motodate      = 0;
    if (CheckString.numCheck(moto_date))
    {
           motodate  = Integer.parseInt(moto_date);
    }
    int    newdate       = 0;
    if (CheckString.numCheck(new_date))
    {
           newdate   = Integer.parseInt(new_date);
    }
    else
    {
           newdate   = motodate;
    }
    String moto_rank      = request.getParameter("moto_rank");
    String new_rank       = request.getParameter("new_rank");
    String moto_img_pc    = request.getParameter("moto_img_pc");
    String new_img_pc     = request.getParameter("new_img_pc");
    String moto_img_m     = request.getParameter("moto_img_m");
    String new_img_m      = request.getParameter("new_img_m");
    String moto_thumb     = request.getParameter("moto_thumb");
    String new_thumb      = request.getParameter("new_thumb");
    String moto_memo_pc   = request.getParameter("moto_memo_pc");
    String new_memo_pc    = request.getParameter("new_memo_pc");


    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    String query   = "";

    int disp_idx_sv = 0;
    java.sql.Date start;
    java.sql.Date end;
    int yy = 0;
    int mm = 0;
    int dd = 0;
    int start_date;
    int end_date;
    String msg1 = "";
    String msg2 = "";
    String msg3 = "";
    String msg4 = "";
    String msg5 = "";
    String msg1_title = "";

    int result = 0;
    int upd_flag = 0;

    DbAccess db_input = new DbAccess();
 
    query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
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
 
            upd_flag = 0;
            msg1       = result_input.getString("msg1");
            msg1_title = result_input.getString("msg1_title");
            msg2       = result_input.getString("msg2");
            msg3       = result_input.getString("msg3");
            msg4       = result_input.getString("msg4");
            msg5       = result_input.getString("msg5");
            if (msg1.indexOf(moto_str) != -1 && moto_str.compareTo("")!=0)
            {
                upd_flag = 1;
                msg1  = msg1.replace(moto_str,new_str);
            }
            if ((msg1.compareTo("") == 0 || msg1.compareTo(" ") == 0) && moto_str.compareTo("") == 0 && new_str.compareTo(moto_str) != 0)
            {
                upd_flag = 1;
                msg1  = new_str;
            }
            if (start_date == motodate && motodate != newdate)
            {
                upd_flag = 1;
                start_date = newdate;
            }
            if (msg1_title.compareTo(moto_rank) == 0 && new_rank.compareTo(moto_rank) != 0)
            {
                upd_flag = 1;
                msg1_title = new_rank;
            }
            if (msg2.indexOf(moto_img_pc) != -1 && moto_img_pc.compareTo("")!=0 )
            {
                upd_flag = 1;
                msg2  = msg2.replace(moto_img_pc,new_img_pc);
            }
            if ((msg2.compareTo("") == 0 || msg2.compareTo(" ") == 0) && moto_img_pc.compareTo("") == 0 && new_img_pc.compareTo(moto_img_pc) != 0)
            {
                upd_flag = 1;
                msg2  = new_img_pc;
            }
            if (msg3.indexOf(moto_img_m) != -1 && moto_img_m.compareTo("")!=0 )
            {
                upd_flag = 1;
                msg3  = msg3.replace(moto_img_m,new_img_m);
            }
            if ((msg3.compareTo("") == 0 || msg3.compareTo(" ") == 0) && moto_img_m.compareTo("") == 0 && new_img_m.compareTo(moto_img_m) != 0)
            {
                upd_flag = 1;
                msg3  = new_img_m;
            }
            if (msg4.indexOf(moto_thumb) != -1 && moto_thumb.compareTo("")!=0 )
            {
                upd_flag = 1;
                msg4  = msg4.replace(moto_thumb,new_thumb);
            }
            if ((msg4.compareTo("") == 0 || msg4.compareTo(" ") == 0) && moto_thumb.compareTo("") == 0 && new_thumb.compareTo(moto_thumb) != 0)
            {
                upd_flag = 1;
                msg4  = new_thumb;
            }
            if (msg5.indexOf(moto_memo_pc) != -1 && moto_memo_pc.compareTo("")!=0 )
            {
                upd_flag = 1;
                msg5  = msg5.replace(moto_memo_pc,new_memo_pc);
            }
            if ((msg5.compareTo("") == 0 || msg5.compareTo(" ") == 0) && moto_memo_pc.compareTo("") == 0 && new_memo_pc.compareTo(moto_memo_pc) != 0)
            {
                upd_flag = 1;
                msg5  = new_memo_pc;
            }

            if (upd_flag == 1)
            {
                if (BackUp.compareTo("1") == 0)
                {
                    query = "INSERT INTO edit_event_info SET ";
                    query = query + "hotelid='"     + hotelid + "', ";
                    query = query + "data_type=100, ";
                    query = query + "disp_idx="     + result_input.getInt("disp_idx")    + ", ";
                    query = query + "disp_flg="     + result_input.getInt("disp_flg")    + ", ";
                    query = query + "start_date="   + start_date  + ", ";
                    query = query + "end_date="     + end_date    + ", ";
                    query = query + "title='"       + ReplaceString.SQLEscape(result_input.getString("title"))       + "', ";
                    query = query + "add_date="     + nowdate + ", ";
                    query = query + "add_time="     + nowtime + ", ";
                    query = query + "add_hotelid='" + loginhotel + "', ";
                    query = query + "add_userid="   + ownerinfo.DbUserId + ", ";
                    query = query + "msg1='"        + ReplaceString.SQLEscape(msg1) + "', ";
                    query = query + "msg1_title='"  + ReplaceString.SQLEscape(msg1_title) + "', ";
                    query = query + "msg2='"        + ReplaceString.SQLEscape(msg2) + "', ";
                    query = query + "msg3='"        + ReplaceString.SQLEscape(msg3) + "', ";
                    query = query + "msg4='"        + ReplaceString.SQLEscape(msg4) + "', ";
                    query = query + "msg5='"        + ReplaceString.SQLEscape(msg5) + "', ";
                    query = query + "upd_hotelid='" + loginhotel + "', ";
                    query = query + "upd_userid="   + ownerinfo.DbUserId + ", ";
                    query = query + "last_update="  + nowdate + ", ";
                    query = query + "last_uptime="  + nowtime + " ";
                }
                else
                {
                    query = "UPDATE edit_event_info SET ";
                    query = query + "msg1='"        + ReplaceString.SQLEscape(msg1) + "', ";
                    query = query + "msg1_title='"  + ReplaceString.SQLEscape(msg1_title) + "', ";
                    query = query + "msg2='"        + ReplaceString.SQLEscape(msg2) + "', ";
                    query = query + "msg3='"        + ReplaceString.SQLEscape(msg3) + "', ";
                    query = query + "msg4='"        + ReplaceString.SQLEscape(msg4) + "', ";
                    query = query + "msg5='"        + ReplaceString.SQLEscape(msg5) + "', ";
                    query = query + "start_date="   + start_date  + ", ";
                    query = query + "upd_hotelid='" + loginhotel + "', ";
                    query = query + "upd_userid="   + ownerinfo.DbUserId + ", ";
                    query = query + "last_update="  + nowdate + ", ";
                    query = query + "last_uptime="  + nowtime + " ";
                    query = query + " WHERE hotelid='"     + hotelid + "' AND data_type=100 AND id=" + result_input.getInt("id");
                }
                DbAccess db = new DbAccess();
                result = db.execUpdate(query);
                db.close();
            }
         }
    }
    db_input.close();

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>ÉNÅ[É|ÉìçÏê¨</title>
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
                <font color="#FFFFFF">ìoò^ämîF</font></td>
              <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- Ç±Ç±Ç©ÇÁï\ -->
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
ìoò^ÇµÇ‹ÇµÇΩÅB<br>
<%
    }
    else
    {
%>
ìoò^Ç…é∏îsÇµÇ‹ÇµÇΩÅB<%=query%>
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
                      <INPUT name="submit_ret" type=submit value=ñﬂÇÈ >
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
      <!-- Ç±Ç±Ç‹Ç≈ -->
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
