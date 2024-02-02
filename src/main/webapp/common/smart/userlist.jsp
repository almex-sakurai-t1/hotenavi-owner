<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>
<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    int    user_id      = ownerinfo.DbUserId;
    String username     = ownerinfo.DbUserName;

    boolean adminFlag = false;
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
    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
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

    try
    {
        final String query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid = ? "
                           + "AND owner_user_security.hotelid = owner_user.hotelid "
                           + "AND owner_user_security.userid = owner_user.userid "
                           + "AND owner_user.imedia_user = 0";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);

        result = prestate.executeQuery();
        
        Connection connectionh = null;
        try
        {
            connectionh = DBConnection.getConnection();
            while(result.next())
            {
                if(adminFlag)
                {
%>
<li><a href="<%= response.encodeURL("userdetail.jsp?UserId=" + result.getInt("owner_user.userid")) %>">
<%=  new String(result.getString("owner_user.name").getBytes("Shift_JIS"), "Windows-31J") %>
</a></li>
<h2><% if  (result.getInt("owner_user_security.admin_flag") == 1)  {%><font size="-1">【管理者】</font><%}%></h2>

<%
                    PreparedStatement prestateh = null;
                    ResultSet resulth = null;
                    try
                    {
                        prestateh = connectionh.prepareStatement(
                            "SELECT * FROM owner_user_login WHERE hotelid = ? AND userid = ?"
                        );
                        prestateh.setString(1, loginHotelId);
                        prestateh.setInt(2, result.getInt("owner_user.userid"));

                        resulth = prestateh.executeQuery();
                        if (resulth.next())
                        {
%>
<table class="roomdetail2">
<tr>
<th>最終ログイン</th>
</tr>
<tr><td><%= resulth.getDate("login_date") %> <%= resulth.getTime("login_time") %></td></tr>
</table>
<div align="right">
<li class="some"><a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + result.getInt("owner_user.userid")) %>">履歴</a></li>
</div>
<%
                        }
                    }
                    finally
                    {
                        DBConnection.releaseResources(resulth);
                        DBConnection.releaseResources(prestateh);
                    }
%>
<h2>
<% if (result.getInt("owner_user.unknown_flag_pc") ==1){%><font color=red>PCメールアドレスをご確認願います</font>:<%=result.getString("owner_user.mailaddr_pc")%><br><%}%>
<% if (result.getInt("owner_user.unknown_flag_mobile") ==1){%><font color=red>携帯メールアドレスをご確認願います</font>:<%=result.getString("owner_user.mailaddr_mobile")%><br><%}%>
</h2>
<%
                }
            }
        }
        finally
        {
            DBConnection.releaseResources(connectionh);
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
%>
</ul>

<jsp:include page="footer.jsp" flush="true" />
