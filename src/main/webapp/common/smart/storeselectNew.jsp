<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;

    int    userlevel;
    String username;

    // ユーザレベルの取得
    userlevel = ownerinfo.DbUserLevel;
    // 名前の取得
    username = ownerinfo.DbUserName;
    // 最終ログイン日時取得
    String lastlogin = "";
    try
    {
        final String query = "SELECT * FROM owner_user_login WHERE hotelid = ? AND userid = ? AND kind = 1";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if(result.next())
        {
            lastlogin = result.getDate("last_login_date") +
                        "&nbsp;" +
                        result.getTime("last_login_time");
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
    if (lastlogin.equals(""))
    {
        try
        {
            final String query = "SELECT * FROM owner_user_log WHERE hotelid = ? AND userid = ? "
                               + "ORDER BY logid DESC LIMIT 1, 1";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);

            result = prestate.executeQuery();
            if(result.next())
            {
                lastlogin = result.getDate("login_date") +
                            "&nbsp;" +
                            result.getTime("login_time");
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
    if (lastlogin.equals(""))
    {
        lastlogin = "なし";
    }

    DateEdit de = new DateEdit();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>店舗選択ﾒﾆｭｰ</title>
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

<center>
<h2>ようこそ<br><%= username %>様</h2>
<font size="1">前回ﾛｸﾞｲﾝ:<%= lastlogin %></font>
</center>

<%-- 個別メッセージ --%>
<jsp:include page="info_message.jsp" flush="true" />
<hr class="border">
<%-- 店舗選択 --%>
<jsp:include page="storeselectdispdata.jsp" flush="true" />

<%-- Information --%>
<jsp:include page="infoNew.jsp" flush="true" />

<div align="center">
<a href="<%= response.encodeURL("index.jsp") %>">TOPへ</a><br>
</div>

<% if (request.getHeader("X-FORWARDED-FOR") != null && !request.getHeader("X-FORWARDED-FOR").equals("125.63.42.196")){%>
<hr class="border">
<center>
<a href="<%= response.encodeURL("../smartpc/ownerindex.jsp?"+de.getDate(2)+de.getTime(1)) %>">PC版へ</a>
</center>
<%}%>
<hr class="border">
<div align="center" class="copyright">
Copyright(C) almex inc. All rights reserved.
</div>
</body>
</html>
