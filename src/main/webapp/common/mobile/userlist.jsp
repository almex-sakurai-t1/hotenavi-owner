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
%>
<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    int    user_id      = ownerinfo.DbUserId;
    String username     = ownerinfo.DbUserName;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connectionh = null;
    PreparedStatement prestateh   = null;
    ResultSet         resulth     = null;
    boolean           AdminFlag   = false;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ﾕｰｻﾞｰ管理</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<div align="center">
ﾕｰｻﾞｰ管理
</div>
<hr>
<%
    try
    {
        final String query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid = ? "
            + " AND owner_user_security.hotelid = owner_user.hotelid"
            + " AND owner_user_security.userid  = owner_user.userid"
            + " AND owner_user.userid = ? "
            + " AND owner_user_security.admin_flag = 1";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, user_id);

        result      = prestate.executeQuery();
        if( result.next())
        {
            AdminFlag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    try
    {
        final String query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid = ? "
            + " AND owner_user_security.hotelid = owner_user.hotelid"
            + " AND owner_user_security.userid  = owner_user.userid"
            + " AND owner_user.imedia_user = 0";

        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);

        result      = prestate.executeQuery();
        connectionh = DBConnection.getConnection();
        if( result != null )
        {
            while( result.next())
            {
                if(AdminFlag)
                {
%>
<a href="<%= response.encodeURL("userdetail.jsp?UserId=" + result.getInt("owner_user.userid")) %>">
<%=  new String(result.getString("owner_user.name").getBytes("Shift_JIS"), "Windows-31J") %>
</a>
<% if  (result.getInt("owner_user_security.admin_flag") == 1)  {%><font size=1>【管理者】</font><%}%><br>
<%
                    prestateh = connection.prepareStatement("SELECT * FROM owner_user_login WHERE hotelid = ? AND userid = ? ");
                    prestate.setString(1, loginHotelId);
                    prestate.setInt(2, result.getInt("owner_user.userid"));

                    resulth   = prestateh.executeQuery();
                    if (resulth.next())
                    {
%>
[最終ﾛｸﾞｲﾝ]<br>
<%= resulth.getDate("login_date") %>
<%= resulth.getTime("login_time") %>
<a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + result.getInt("owner_user.userid")) %>">履歴</a><br>
<%
                    }
                    else
                    {
%>
<%
                    }
                    DBConnection.releaseResources(resulth);
                    DBConnection.releaseResources(prestateh);
%>
<% if (result.getInt("owner_user.unknown_flag_pc") ==1){%><font color=red>PCﾒｰﾙｱﾄﾞﾚｽをご確認願います</font>:<%=result.getString("owner_user.mailaddr_pc")%><br><%}%>
<% if (result.getInt("owner_user.unknown_flag_mobile") ==1){%><font color=red>携帯ﾒｰﾙｱﾄﾞﾚｽをご確認願います</font>:<%=result.getString("owner_user.mailaddr_mobile")%><br><%}%>
<br>
<%
                }
            }
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(connectionh);
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
<jsp:include page="footer.jsp" flush="true" />
