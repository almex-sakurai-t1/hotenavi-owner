<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
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

    String loginHotelId = (String)session.getAttribute("HotelId");
    int    user_id      = ownerinfo.DbUserId;
    String username     = ownerinfo.DbUserName;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;

    boolean adminFlag = false;
    try
    {
        final String query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid = ? "
                           + "AND owner_user_security.hotelid = owner_user.hotelid "
                           + "AND owner_user_security.userid = owner_user.userid "
                           + "AND owner_user.userid = ? "
                           + "AND owner_user_security.admin_flag = 1";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, user_id);

        result = prestate.executeQuery();
        if(result.next())
        {
            adminFlag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }

    String param_user_id = request.getParameter("UserId");
    if (param_user_id != null)
    {
        if(CheckString.numCheck(param_user_id))
        {
            user_id = Integer.parseInt(param_user_id);
        }
    }

    int     admin_flag          = 0;
    int     unknown_flag_pc     = 0;
    int     unknown_flag_mobile = 0;
    String  mailaddr_pc         = "";
    String  mailaddr_mobile     = "";
    int     sec_level01         = 0;
    int     sec_level02         = 0;
    int     sec_level03         = 0;
    int     sec_level04         = 0;
    int     sec_level05         = 0;
    int     sec_level06         = 0;
    int     sec_level07         = 0;
    int     sec_level15         = 0;
    int     sec_level16         = 0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ユーザー管理</title>
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
<div align="center">
<h1>ユーザー管理</h1>
</div>
<hr class="border">

<ul class="link">
<%
    if (adminFlag)
    {
        try
        {
            final String query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid = ? "
                               + "AND owner_user_security.hotelid = owner_user.hotelid "
                               + "AND owner_user_security.userid = owner_user.userid "
                               + "AND owner_user.userid = ? "
                               + "AND owner_user.imedia_user = 0";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, user_id);

            result = prestate.executeQuery();
            if(result.next())
            {
                username            = new String(result.getString("owner_user.name").getBytes("Shift_JIS"), "Windows-31J");
                admin_flag          = result.getInt("owner_user_security.admin_flag");
                unknown_flag_pc     = result.getInt("owner_user.unknown_flag_pc");
                unknown_flag_mobile = result.getInt("owner_user.unknown_flag_mobile");
                mailaddr_pc         = result.getString("owner_user.mailaddr_pc");
                mailaddr_mobile     = result.getString("owner_user.mailaddr_mobile");
                sec_level01         = result.getInt("owner_user_security.sec_level01");
                sec_level02         = result.getInt("owner_user_security.sec_level02");
                sec_level03         = result.getInt("owner_user_security.sec_level03");
                sec_level04         = result.getInt("owner_user_security.sec_level04");
                sec_level05         = result.getInt("owner_user_security.sec_level05");
                sec_level06         = result.getInt("owner_user_security.sec_level06");
                sec_level07         = result.getInt("owner_user_security.sec_level07");
                sec_level15         = result.getInt("owner_user_security.sec_level15");
                sec_level16         = result.getInt("owner_user_security.sec_level16");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }
%>
<h2><%= username %>様</h2>
<%
        try
        {
            final String query = "SELECT * FROM owner_user_login WHERE hotelid = ? AND userid = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, user_id);

            result = prestate.executeQuery();
            if(result.next())
            {
%>
<table class="roomdetail2">
<tr>
<th>最終ログイン</th>
</tr>
<tr><td><%= result.getDate("login_date") %>　<%= result.getTime("login_time") %></td></tr>
</table>
<div align="right">
<li class="some"><a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + user_id) %>">履歴</a></li>
</div>
<%
            }
            else
            {
%>
<table class="roomdetail2">
<tr>
<th>ログイン履歴</th>
</tr>
<tr><td>ログイン履歴はありません。</td></tr>
</table>
<hr class="border">
<%
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }
%>
<table class="roomdetail2">
<tr>
<th>メールアドレス</th>
</tr>
<tr><td>
<% if (mailaddr_pc.equals("") && mailaddr_mobile.equals("")){%>未登録<br><%}%>
<% if (!mailaddr_pc.equals(""))    {%><%=mailaddr_pc%><% if(unknown_flag_pc ==1)       {%>　<font color=red>PCメールアドレスをご確認願います</font><%}%><br><%}%>
<% if (!mailaddr_mobile.equals("")){%><%=mailaddr_mobile%><% if(unknown_flag_mobile ==1){%>　<font color=red>携帯メールアドレスをご確認願います</font><%}%><br><%}%>
</td></tr>
<tr><th>権限</th></tr>
<tr><td><font size="-1">
<% if  (admin_flag  == 1) {%><strong>管理者</strong><%}%>
<% if  (sec_level01 == 1) {%>売上管理<%}%>
<% if  (sec_level02 == 1) {%>部屋情報<%}%>
<% if  (sec_level03 == 1) {%>帳票管理<%}%>
<% if  (sec_level04 == 1) {%>ＨＰレポート<%}%>
<% if  (sec_level05 == 1) {%>メルマガ作成<%}%>
<% if  (sec_level06 == 1) {%>ＨＰ編集<%}%>
<% if  (sec_level07 == 1) {%>設定メニュー<%}%>
<% if  (sec_level15 == 1) {%>ハピホテ編集<%}%>
<% if  (sec_level16 == 1) {%>クチコミ権限<%}%><br>
</font></td></tr>
<tr><th>管理店舗</th></tr>
<%
        try
        {
            final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                               + "AND owner_user_hotel.accept_hotelid = hotel.hotel_id "
                               + "AND owner_user_hotel.userid = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, user_id);

            result = prestate.executeQuery();
            while(result.next())
            {
%>
<tr><td><%= new String(result.getString("hotel.name").getBytes("Shift_JIS"), "Windows-31J") %></td></tr>
<%
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }
    }
%>
</table>
</ul>

<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
