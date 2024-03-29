<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
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
<%@ include file="checkHotelId.jsp" %>
<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    String hotelid;

    boolean HotelIdCheck = false;

    // ホテルIDのセット
    hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }

    int storecount = 1;
    String param_count = request.getParameter("count");
    if (param_count != null)
    {
        storecount = Integer.parseInt(param_count);
    }

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;

    // ホテル名,プラン取得
    String hotelname = "";
    int plan = 0;
    try
    {
        final String query = "SELECT name,plan FROM hotel WHERE hotel_id = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result = prestate.executeQuery();
        if(result.next())
        {
            hotelname = result.getString("name");
            plan      = result.getInt("plan");
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

    int    userlevel;
    String username;
    String lastlogin;

    // 代表ホテルにログイン（計上日取得）
    ownerinfo.sendPacket0100(1, hotelid);
    // ユーザレベルの取得
    userlevel = ownerinfo.DbUserLevel;
    // 名前の取得
    username = ownerinfo.DbUserName;
    // 前回ログイン時刻
    lastlogin = "なし";
    try
    {
        final String query = "SELECT * FROM owner_user_log WHERE hotelid = ? "
                           + "AND userid = ? "
                           + "ORDER BY logid DESC "
                           + "LIMIT 1, 1";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if(result.next())
        {
            lastlogin = result.getDate("login_date") + "&nbsp;" + result.getTime("login_time");
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

    //売上管理・部屋情報管理店舗件数
    int count = 0;
    try
    {
        final String query = "SELECT count(*) FROM hotel,owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                           + "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                           + "AND owner_user_hotel.userid = ? "
                           + "AND hotel.plan <= 2 "
                           + "AND hotel.plan >= 1 "
                           + "AND owner_user_hotel.hotelid = owner_user_security.hotelid "
                           + "AND owner_user_hotel.userid = owner_user_security.userid "
                           + "AND (owner_user_security.admin_flag = 1 OR owner_user_security.sec_level01 = 1 OR owner_user_security.sec_level02 = 1)";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if(result.next()!= false)
        {
            count = result.getInt(1);
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ｵｰﾅｰｻｲﾄﾒﾆｭｰ</title>
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
<%
    if (!hotelname.equals(""))
    {
%>
<div align="center">
<%= hotelname %>
</div>
<hr class="border">
<%
    }
%>
<h1 class="border">ようこそ<br><%= username %>様<br>前回ﾛｸﾞｲﾝ<br><%= lastlogin %><br></h1>
<ul class="link">
<%
    try
    {
        final String query = "SELECT * FROM owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                           + "AND owner_user_hotel.userid = ? "
                           + "AND owner_user_hotel.accept_hotelid = ? "
                           + "AND owner_user_hotel.hotelid = owner_user_security.hotelid "
                           + "AND owner_user_hotel.userid = owner_user_security.userid";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        prestate.setString(3, hotelid);

        result = prestate.executeQuery();
        if(result.next())
        {
            if( result.getInt("sec_level01") == 1 && (plan == 1 || plan == 2) )
            {
                HotelIdCheck = true;
%>
<li><a href="<%= response.encodeURL("salesmenu.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>" accesskey="1">売上情報</a></li><br>
<%
            }
            if( result.getInt("sec_level02") == 1 && (plan == 1 || plan == 2))
            {
                HotelIdCheck = true;
%>
<li><a href="<%= response.encodeURL("roominfo.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>" accesskey="2">現在部屋情報</a></li><br>
<%
            }
            if( result.getInt("sec_level04") == 1 && (plan == 1 || plan == 3 || plan == 4) )
            {
                HotelIdCheck = true;
%>
<li><a href="<%= response.encodeURL("access.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>" accesskey="4">ｻｲﾄｱｸｾｽ情報</a></li><br>
<%
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
    if(!HotelIdCheck)
    {
        if(storecount == 0)
        {
%>
<font color=red>管理店舗が登録されていません。</font><br>
管理者にお問合せください。<br>
<%
        }
        else
        {
%>
<font color=red>管理店舗ではありません。</font><br>
<%
        }
    }
    if( count > 1 )
    {
%>
<li><a href="<%= response.encodeURL("allstoremenu.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>" accesskey="3">管理店舗情報</a></li><br>
<%
    }
%>
</ul>

<jsp:include page="infoNew.jsp" flush="true" />
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
