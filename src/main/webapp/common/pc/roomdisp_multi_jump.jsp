<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int         now_date;
    int         now_time;
    DateEdit    df;
    df        = new DateEdit();
    now_date  = Integer.valueOf(df.getDate(2)).intValue();
    now_time  = Integer.valueOf(df.getTime(1)).intValue();

    int          count;
    boolean      ret;
    String       query;
    String       hotelname = "";
    String       storecount;
    String       selecthotel;
    String       param_store;
    DbAccess     db;
    ResultSet    result;
    ResultSet    manage;

    String param_cnt = ReplaceString.getParameter(request,"cnt");
    if( param_cnt == null )
    {
        param_cnt = "0";
    }
    if(!CheckString.numCheck(param_cnt))
	{
	param_cnt ="0";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </script>
<%
	}
    int cnt = Integer.parseInt(param_cnt);

    db =  new DbAccess();

    // 管理可能ホテルの取得
    query = "SELECT hotel.*,owner_user_hotel.accept_hotelid FROM hotel,owner_user_hotel";
    query = query + " WHERE hotelid='" + (String)session.getAttribute("HotelId") + "'";
    query = query + " AND userid = " + ownerinfo.DbUserId;
    query = query + " AND hotel.plan <= 2";
    query = query + " AND hotel.plan >= 1";
    query = query + " AND owner_user_hotel.accept_hotelid = hotel.hotel_id";
    query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";

    manage = db.execQuery(query);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>店舗選択</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
<!--
function jumpprevfunc() {
   parent.selectFrame.location.href = "roomdisp_wait.jsp?<%=now_date%><%=now_time%>&cnt=<%= cnt-3 %>";
   parent.dispFrame.location.href = "roomdisp_multi.jsp?<%=now_date%><%=now_time%>&cnt=<%= cnt-3 %>";
   return false;
}

function jumpnextfunc() {
   parent.selectFrame.location.href = "roomdisp_wait.jsp?<%=now_date%><%=now_time%>&cnt=<%= cnt+3 %>";
   parent.dispFrame.location.href = "roomdisp_multi.jsp?<%=now_date%><%=now_time%>&cnt=<%= cnt+3 %>";
   return false;
}
// -->
</script>

</head>

<body bgcolor="#D8CA75" text="#663333" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="40" border="0" cellpadding="0" cellspacing="0">
  <tr>
<%
    if( cnt > 0 )
    {
%>
    <td align="left" valign="middle">
    <a href="#" onclick="return jumpprevfunc()">←前の店舗へ</a>
    </td>
<%
    }

        int store_count = 0;

        // 管理店舗数分ループ
        ret = manage.first();
        ret = manage.relative(cnt);
        while( ret != false )
        {
            DbAccess db_name =  new DbAccess();

            // ホテル名称の取得
            query = "SELECT name FROM hotel WHERE hotel_id='" + manage.getString("accept_hotelid") + "'";
            result = db_name.execQuery(query);
            if( result != null )
            {
                result.next();
                hotelname = result.getString("name");
            }

%>
    <td align="left" valign="middle">
    <a href="roomdisp_multi.jsp?<%=now_date%><%=now_time%>&cnt=<%= cnt %>#<%= manage.getString("accept_hotelid") %>" target="dispFrame">&gt;&gt;<%= hotelname %></a>
    </td>
<%
            db_name.close();

            ret = manage.next();

            store_count++;
            if( store_count >= 3 )
            {
                break;
            }
        }

        if( ret != false )
        {
%>
    <td align="left" valign="middle">
    <a href="#" onclick="return jumpnextfunc()">→次の店舗へ</a>
    </td>
<%
        }
%>
  </tr>
  </table>
<%
    db.close();
%>
</body>
</html>
