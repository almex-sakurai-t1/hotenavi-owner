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
    int pageno;
    String param_page = request.getParameter("page");
    if( param_page == null )
    {
        pageno = 0;
    }
    else
    {
        if ( !CheckString.numCheck( param_page ) )
        {
            response.sendError(400);
            return;
        }

        pageno = Integer.parseInt(param_page);
    }
    int count = 0;

    String loginHotelId  = (String)session.getAttribute("HotelId");
    int    user_id       = ownerinfo.DbUserId;
    String username      = ownerinfo.DbUserName;

    String            query       = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    String param_user_id = request.getParameter("UserId");
    if(CheckString.isValidParameter(param_user_id) && !CheckString.numAlphaCheck(param_user_id))
    {
        response.sendError(400);
        return;
    }    if    (param_user_id != null)
    {
        if(CheckString.numCheck(param_user_id))
        {
            user_id = Integer.parseInt(param_user_id);
            query = "SELECT owner_user.name FROM owner_user WHERE owner_user.hotelid= ? ";
            query = query + " AND owner_user.userid = ? ";
            query = query + " AND owner_user.imedia_user = 0";
            try
            {
                connection  = DBConnection.getConnection();
                prestate    = connection.prepareStatement(query);
                prestate.setString(1, loginHotelId);
                prestate.setInt(2, user_id);

                result      = prestate.executeQuery();
                if( result != null )
                {
                    if( result.next())
                    {
                        username = new String(result.getString("owner_user.name").getBytes("Shift_JIS"), "Windows-31J");
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
        }
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ﾛｸﾞｲﾝ履歴</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<div align="center">
ﾛｸﾞｲﾝ履歴
</div>
<hr>
<%
    if (param_user_id != null)
    {
%>
<%= username %>様
[<a href="<%= response.encodeURL("userdetail.jsp?UserId=" + user_id) %>">詳細</a>]<br>
<a href="<%= response.encodeURL("userlist.jsp") %>">ﾕｰｻﾞｰ一覧に戻る</a><br>
<hr>
<%
    }
    try
    {
        query = "SELECT * FROM owner_user_log WHERE hotelid= ? ";
        query = query + " AND userid = ? ";
        query = query + " ORDER BY logid DESC";
        query = query + " LIMIT " + pageno * 10 + "," + 10;
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, user_id);

        result      = prestate.executeQuery();
        if( result != null)
        {
            while( result.next() )
            {
                count++;
%>
<font color=brown><%= result.getDate("login_date") %>
<%= result.getTime("login_time") %></font><br>
<%= ReplaceString.replaceMobile(result.getString("log_detail")) %><br>
<font size=1><%= result.getString("user_agent") %></font><br>
<br>
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
%>
<%
    if( pageno != 0 )
    {
%>
<a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + user_id + "&page=" + (pageno-1) ) %>">←前</a>
<%
    }
    if( count > 9 )
    {
%>
<a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + user_id + "&page=" + (pageno+1) ) %>">次→</a>
<%
    }
%>
<jsp:include page="footer.jsp" flush="true" />
