<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    DateEdit dateedit = new DateEdit();
    int nowdate = Integer.parseInt(dateedit.getDate(2));
    String  loginHotelId = (String)session.getAttribute("HotelId");
    int     userId       = ownerinfo.DbUserId;
    String  username     = ownerinfo.DbUserName;
    boolean adminFlag    = false;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT * FROM owner_user_security WHERE hotelid = ? "
                           + "AND userid = ? "
                           + "AND admin_flag = 1";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, userId);

        result = prestate.executeQuery();
        if( result.next() )
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

    String title = "";
    int    id    = 0;
    try
    {
        final String query = "SELECT * FROM hh_system_info WHERE data_type = 31 "
                           + "AND start_date <= ? "
                           + "AND end_date   >= ? "
                           + "AND (disp_flag = 1 OR disp_flag = 3) "
                           + "ORDER BY disp_idx DESC";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setInt(1, nowdate);
        prestate.setInt(2, nowdate);

        result = prestate.executeQuery();
        if( result.next() )
        {
            id    = result.getInt("id");
            title = result.getString("title");
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
<ul class="link">
<%
    if (id != 0)
    {
%>
<li><a href="<%= response.encodeURL("../../common/smart/mainte.jsp?id=" + id) %>" ><font color="#FF0000"><%=title%></font></a></li>
<%
    }
%>
<li><a href="<%= response.encodeURL("passwdchange.jsp") %>">パスワード変更</a></li>
<li><a href="<%= response.encodeURL("loginhistory.jsp") %>">ログイン履歴</a></li>
<li><a href="<%= response.encodeURL("info_message_list.jsp?past=y") %>"><%=username%>様へのメッセージ</a></li>
<!--
<li><a href="<%= response.encodeURL("info_update.jsp") %>" >お知らせ　<font color=red>NEW</font></a></li>
-->
<%
    if (adminFlag)
    {
%>
<li><a href="<%= response.encodeURL("userlist.jsp") %>">ユーザー管理</a></li>
<%
    }
%>
</ul>
