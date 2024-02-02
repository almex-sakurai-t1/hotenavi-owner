<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    // 本日の日付取得
    Calendar calnd;
    calnd = Calendar.getInstance();
    int now_year   = calnd.get(calnd.YEAR);
    int now_month  = calnd.get(calnd.MONTH) + 1;
    int now_day    = calnd.get(calnd.DATE);
    int now_hour   = calnd.get(calnd.HOUR_OF_DAY);
    int now_minute = calnd.get(calnd.MINUTE);
    int now_second = calnd.get(calnd.SECOND);
    int now_date   = now_year * 10000 + now_month  * 100 + now_day;
    NumberFormat  nf2;
    nf2    = new DecimalFormat("00");

    String loginHotelId =  (String)session.getAttribute("HotelId");

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    int          userlevel;
    String       username;
    // ユーザレベルの取得
    userlevel = ownerinfo.DbUserLevel;
    // 名前の取得
    username = ownerinfo.DbUserName;

    boolean PastMode   = false;
    String  param_past   = ReplaceString.getParameter(request,"past");
    if (param_past == null)
    {
        param_past = "";
    }
    else if(param_past.equals("y"))
    {
        PastMode = true;
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>個別ﾒｯｾｰｼﾞ</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
ようこそ<br>
<%= username %>様<br>
<br>
<%-- 個別メッセージ --%>
<%
    try
    {
        String query = "SELECT * FROM hh_owner_message"
            + " WHERE  hotelid    = ?"
            + " AND    userid     = ? "
            + " AND    start_date <= ? "
            + " AND    end_date   >= ? "
            + " AND    hotenavi_flag != 0"
            + " AND    mobile_flag   != 0";
        if (!PastMode)
        {
            query += " AND    msg_disp_flag = 0";
        }
        connection  = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        prestate.setInt(3, now_date);
        prestate.setInt(4, now_date);

        result   = prestate.executeQuery();
        if( result != null)
        {
            while( result.next() != false )
            {
%>
[<%= result.getInt("start_date") / 10000 %>/<%= nf2.format(result.getInt("start_date") / 100 % 100) %>/<%= nf2.format(result.getInt("start_date") % 100) %>]<br/>
<a href="<%= response.encodeURL("info_message_data.jsp?id=" + result.getInt("id") + "&seq=" + result.getInt("seq") ) %>"><%= new String(ReplaceString.replaceMobile(result.getString("title")).getBytes("Shift_JIS"), "Windows-31J" ) %></a>
<%
                if (PastMode)
                {
                    if( result.getInt("msg_disp_flag")==0)
                    {%>
<strong><font color=red>未確認</font></strong>
<%
                    }
                    else
                    {%>
<font color=green>確認済</font>
<%
                    }
                }%><br>
<%
            }
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
    if (!PastMode)
    {
%>
<hr>
<a href="<%= response.encodeURL("info_message_list.jsp?past=y") %>">確認済のﾒｯｾｰｼﾞも表示</a>
<%
    }
%>
<hr>
<div align="center">
<a href="<%= response.encodeURL("index.jsp") %>">TOPへ</a><br>
</div>
<hr>
<div align="center">
Copyright(C) almex inc. All rights reserved.
</div>
</body>
</html>
