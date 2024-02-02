<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    int pageno;
    String param_page = request.getParameter("page");
    if( param_page == null )
    {
        pageno = 0;
    }
    else
    {
        if (!CheckString.numCheck( param_page ))
        {
            response.sendError(400);
            return;
        }
        pageno = Integer.parseInt(param_page);
    }
    int count = 0;

    String loginHotelId = (String)session.getAttribute("HotelId");
    int    user_id      = ownerinfo.DbUserId;
    String username     = ownerinfo.DbUserName;

    String param_user_id = request.getParameter("UserId");
    if(CheckString.isValidParameter(param_user_id) && !CheckString.numAlphaCheck(param_user_id))
    {
        response.sendError(400);
        return;
    }
    if (param_user_id != null)
    {
        if (CheckString.numCheck(param_user_id))
        {
            Connection connection = null;
            PreparedStatement prestate = null;
            ResultSet result = null;
            try
            {
                final String query = "SELECT owner_user.name FROM owner_user WHERE owner_user.hotelid = ? "
                                   + "AND owner_user.userid = ? "
                                   + "AND owner_user.imedia_user = 0";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement(query);
                prestate.setString(1, loginHotelId);
                prestate.setInt(2,Integer.parseInt(param_user_id));

                result = prestate.executeQuery();
                if( result.next())
                {
                    username = new String(result.getString("owner_user.name").getBytes("Shift_JIS"), "Windows-31J");
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
        }
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ﾛｸﾞｲﾝ履歴</title>
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
<h2>ﾛｸﾞｲﾝ履歴</h2>
</div>
<hr class="border">
<ul class="link">
<%
    if (param_user_id != null)
    {
%>
<h2 class="link"><%= username %>様
<a href="<%= response.encodeURL("userdetail.jsp?UserId=" +param_user_id) %>">[詳細]</a></h2>
<div align="right">
<li><a href="<%= response.encodeURL("userlist.jsp") %>">ﾕｰｻﾞｰ一覧に戻る</a></li>
</div>
<br>
<%
    }
    if (param_user_id != null)
    {
        if (CheckString.numCheck(param_user_id))
        {
            Connection connection = null;
            PreparedStatement prestate = null;
            ResultSet result = null;
            try
            {
                final String query = "SELECT * FROM owner_user_log WHERE hotelid = ? "
                                   + "AND userid = ? "
                                   + "ORDER BY logid DESC "
                                   + "LIMIT ?, 10";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement(query);
                prestate.setString(1, loginHotelId);
                prestate.setInt(2, Integer.parseInt(param_user_id));
                prestate.setInt(3, pageno * 10);

                result = prestate.executeQuery();
                while( result.next() )
                {
                    count++;
%>
<table class="login">
<tr>
<td><font color=brown><%= result.getDate("login_date") %> <%= result.getTime("login_time") %></font></td>
</tr>
<tr>
<td><%= ReplaceString.replaceMobile(result.getString("log_detail")) %></td>
</tr>
<tr>
<td><font size=1><%= result.getString("user_agent") %></font></td>
</tr>
</table>
<%
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
        }
    }

%>
<hr class="border">
<%
    if( pageno != 0 )
    {
%>
<div align="right">
<li><a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + user_id + "&page=" + (pageno-1) ) %>">前へ</a></li>
</div>
<%
    }
    if( count > 9 )
    {
%>
<div align="right">
<li><a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + user_id + "&page=" + (pageno+1) ) %>">次へ</a></li>
</div>
<%
    }
%>
</ul>

<jsp:include page="footer.jsp" flush="true" />
