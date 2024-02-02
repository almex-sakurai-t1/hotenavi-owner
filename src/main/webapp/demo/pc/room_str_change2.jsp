<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="com.hotenavi2.common.*" %>
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
    String param_moto_rank      = request.getParameter("moto_rank");
    if    (param_moto_rank == null) param_moto_rank = "0";
    if    (param_moto_rank.compareTo("")  == 0) param_moto_rank = "0";
    int    moto_rank = Integer.parseInt(param_moto_rank);
    String param_new_rank      = request.getParameter("new_rank");
    if    (param_new_rank == null) param_new_rank = "0";
    if    (param_new_rank.compareTo("")  == 0) param_new_rank = "0";
    int    new_rank = Integer.parseInt(param_new_rank);

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

    int room_no_sv = 0;
    java.sql.Date start;
    java.sql.Date end;
    int yy = 0;
    int mm = 0;
    int dd = 0;
    int start_date;
    int end_date;
    String equipment = "";
    String imame_pc = "";
    String image_mobile = "";
    String image_thumb = "";
    String memo = "";
    int    rank = 0;
    String refer_name = "";

    int result   = 0;
    int upd_flag = 0;
    int sync_flag = 1;

    DbAccess db_input = new DbAccess();
 
    query = "SELECT * FROM room WHERE hotelid='" + hotelid + "'";
    if  (disp_type.compareTo("0") == 0)
    {
		query = query + " AND start_date<=" + nowdate;
		query = query + " AND end_date>=" + nowdate;
		query = query + " AND disp_flg=1";
    }
    query = query + " ORDER BY room_no,id DESC";
    ResultSet result_input = db_input.execQuery(query);
    while( result_input.next() != false )
    {
        if(result_input.getInt("room_no") != room_no_sv)
        {
            room_no_sv = result_input.getInt("room_no");
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
            equipment      = result_input.getString("equipment");
            rank           = result_input.getInt("rank");
            imame_pc       = result_input.getString("image_pc");
            image_mobile   = result_input.getString("image_mobile");
            image_thumb    = result_input.getString("image_thumb");
            refer_name     = "";
            if (image_thumb.indexOf("thumb%ROOMNAME%") != -1)
            {
                refer_name = result_input.getString("room_name").replace("çÜé∫","");
            }

            memo           = result_input.getString("memo");
            if (equipment.indexOf(moto_str) != -1 && moto_str.compareTo("")!=0)
            {
                upd_flag = 1;
                equipment  = equipment.replace(moto_str,new_str);
            }
            if ((equipment.compareTo("") == 0 || equipment.compareTo(" ") == 0) && moto_str.compareTo("") == 0 && new_str.compareTo(moto_str) != 0)
            {
                upd_flag = 1;
                equipment  = new_str;
            }
            if (start_date == motodate && motodate != newdate)
            {
                upd_flag = 1;
                start_date = newdate;
            }
            if (rank == moto_rank && new_rank != moto_rank)
            {
                upd_flag = 1;
                rank = new_rank;
            }
            if (imame_pc.indexOf(moto_img_pc) != -1 && moto_img_pc.compareTo("")!=0 )
            {
                upd_flag = 1;
                imame_pc  = imame_pc.replace(moto_img_pc,new_img_pc);
            }
            if ((imame_pc.compareTo("") == 0 || imame_pc.compareTo(" ") == 0) && moto_img_pc.compareTo("") == 0 && new_img_pc.compareTo(moto_img_pc) != 0)
            {
                upd_flag = 1;
                imame_pc  = new_img_pc;
            }
            if (image_mobile.indexOf(moto_img_m) != -1 && moto_img_m.compareTo("")!=0 )
            {
                upd_flag = 1;
                image_mobile  = image_mobile.replace(moto_img_m,new_img_m);
            }
            if ((image_mobile.compareTo("") == 0 || image_mobile.compareTo(" ") == 0) && moto_img_m.compareTo("") == 0 && new_img_m.compareTo(moto_img_m) != 0)
            {
                upd_flag = 1;
                image_mobile  = new_img_m;
            }
            if (image_thumb.indexOf(moto_thumb) != -1 && moto_thumb.compareTo("")!=0 )
            {
                upd_flag = 1;
                image_thumb  = image_thumb.replace(moto_thumb,new_thumb);
            }
            if ((image_thumb.compareTo("") == 0 || image_thumb.compareTo(" ") == 0) && moto_thumb.compareTo("") == 0 && new_thumb.compareTo(moto_thumb) != 0)
            {
                upd_flag = 1;
                image_thumb  = new_thumb;
            }
            if (memo.indexOf(moto_memo_pc) != -1 && moto_memo_pc.compareTo("")!=0 )
            {
                upd_flag = 1;
                memo  = memo.replace(moto_memo_pc,new_memo_pc);
            }
            if ((memo.compareTo("") == 0 || memo.compareTo(" ") == 0) && moto_memo_pc.compareTo("") == 0 && new_memo_pc.compareTo(moto_memo_pc) != 0)
            {
                upd_flag = 1;
                memo  = new_memo_pc;
            }

            if (upd_flag == 1)
            {
                if (BackUp.compareTo("1") == 0)
                {
                    query = "INSERT INTO room SET ";
                    query = query + "hotelid='"      + hotelid + "', ";
                    query = query + "room_no="       + result_input.getInt("room_no")    + ", ";
                    query = query + "disp_flg="      + result_input.getInt("disp_flg")    + ", ";
                    query = query + "start_date="    + start_date  + ", ";
                    query = query + "end_date="      + end_date    + ", ";
                    query = query + "room_name='"    + ReplaceString.SQLEscape(result_input.getString("room_name"))       + "', ";
                    query = query + "add_date="      + nowdate + ", ";
                    query = query + "add_time="      + nowtime + ", ";
                    query = query + "add_hotelid='"  + loginhotel + "', ";
                    query = query + "add_userid="    + ownerinfo.DbUserId + ", ";
                    query = query + "equipment='"    + ReplaceString.SQLEscape(equipment) + "', ";
                    query = query + "rank="          + rank + ", ";
                    query = query + "image_pc='"     + ReplaceString.SQLEscape(imame_pc) + "', ";
                    query = query + "image_mobile='" + ReplaceString.SQLEscape(image_mobile) + "', ";
                    query = query + "image_thumb='"  + ReplaceString.SQLEscape(image_thumb) + "', ";
                    query = query + "memo='"         + ReplaceString.SQLEscape(memo) + "', ";
                    query = query + "upd_hotelid='"  + loginhotel + "', ";
                    query = query + "upd_userid="    + ownerinfo.DbUserId + ", ";
                    query = query + "last_update="   + nowdate + ", ";
                    query = query + "last_uptime="   + nowtime + ", ";
                    query = query + "refer_name='"   + ReplaceString.SQLEscape(refer_name) + "', ";
                    query = query + "movie_mobile='', ";
                    query = query + "sync_flag="     + sync_flag;
                }
                else
                {
                    query = "UPDATE room SET ";
                    query = query + "equipment='"    + ReplaceString.SQLEscape(equipment) + "', ";
                    query = query + "rank="          + rank + ", ";
                    query = query + "image_pc='"     + ReplaceString.SQLEscape(imame_pc) + "', ";
                    query = query + "image_mobile='" + ReplaceString.SQLEscape(image_mobile) + "', ";
                    query = query + "image_thumb='"  + ReplaceString.SQLEscape(image_thumb) + "', ";
                    query = query + "memo='"         + ReplaceString.SQLEscape(memo) + "', ";
                    query = query + "start_date="    + start_date  + ", ";
                    query = query + "upd_hotelid='"  + loginhotel + "', ";
                    query = query + "upd_userid="    + ownerinfo.DbUserId + ", ";
                    query = query + "last_update="   + nowdate + ", ";
                    query = query + "last_uptime="   + nowtime + ", ";
                    query = query + "refer_name='"   + ReplaceString.SQLEscape(refer_name) + "', ";
                    query = query + "sync_flag="     + sync_flag;
                    query = query + " WHERE hotelid='"     + hotelid + "' AND id=" + result_input.getInt("id");
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
ìoò^ÇµÇ‹ÇµÇΩÅB<%=query%><br>
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
                    <td class="size12"><form action="room_list2.jsp?HotelId=<%= hotelid %>" method="POST">
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
