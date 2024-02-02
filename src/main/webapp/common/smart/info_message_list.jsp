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

    int          userlevel;
    String       username;
    // ユーザレベルの取得
    userlevel = ownerinfo.DbUserLevel;
    // 名前の取得
    username = ownerinfo.DbUserName;

    boolean PastMode   = false;
    String  param_past   = request.getParameter("past");
    if (param_past == null)
    {
        param_past = "";
    }
    else if(param_past.equals("y"))
    {
        PastMode = true;
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>個別ﾒｯｾｰｼﾞ</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
</head>
<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">
<jsp:include page="header.jsp" flush="true" />
<h2>ようこそ<br>
<%= username %>様</h2>
<hr class="border">

<ul class="link">
<%-- 個別メッセージ --%>
<%
    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        String query = "SELECT * FROM hh_owner_message "
                     + "WHERE  hotelid    = ? "
                     + "AND    userid     = ? "
                     + "AND    start_date <= ? "
                     + "AND    end_date   >= ? "
                     + "AND    hotenavi_flag != 0 "
                     + "AND    mobile_flag   != 0";
        if (!PastMode)
        {
            query += " AND msg_disp_flag = 0";
        }

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        prestate.setInt(3, now_date);
        prestate.setInt(4, now_date);

        result = prestate.executeQuery();
        while( result.next() )
        {
%>
<h2>[<%= result.getInt("start_date") / 10000 %>/<%= nf2.format(result.getInt("start_date") / 100 % 100) %>/<%= nf2.format(result.getInt("start_date") % 100) %>]</h2>
<li class="some"><a href="<%= response.encodeURL("info_message_data.jsp?id=" + result.getInt("id") + "&seq=" + result.getInt("seq") ) %>"><%= new String(result.getString("title").getBytes("Shift_JIS"), "Windows-31J" ) %></a></li>
<%
            if (PastMode)
            {
                if( result.getInt("msg_disp_flag")==0)
                {
%>
<strong><font color=red>未確認</font></strong>
<hr class="border">
<%
                }
                else
                {
%>
<font color=green>確認済</font>
<hr class="border">
<%
                }
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }
    if (!PastMode)
    {
%>

<div align="right">
<li><a href="<%= response.encodeURL("info_message_list.jsp?past=y") %>">確認済のメッセージも表示</a></li>
</div>
<%
    }
%>
</ul>

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>
