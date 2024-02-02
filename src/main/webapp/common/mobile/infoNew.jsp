<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    DateEdit dateedit = new DateEdit();
    int nowdate   =  Integer.parseInt(dateedit.getDate(2));
    String loginHotelId = (String)session.getAttribute("HotelId");
    int    user_id      = ownerinfo.DbUserId;
    String username     = ownerinfo.DbUserName;
    boolean           AdminFlag   = false;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        final String query = "SELECT * FROM owner_user_security WHERE hotelid= ? "
            + " AND userid = ? "
            + " AND admin_flag = 1 ";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, user_id);

        result      = prestate.executeQuery();
        if( result.next() )
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
    String            title = "";
    int               id         = 0;
    try
    {
        final String query = "SELECT * FROM hh_system_info WHERE data_type=31"
            + " AND start_date <= ? "
            + " AND end_date   >= ? "
            + " AND (disp_flag =1 OR disp_flag =3) "
            + " ORDER BY disp_idx DESC";
        prestate       = connection.prepareStatement(query);
        prestate.setInt(1, nowdate);
        prestate.setInt(2, nowdate);

        result         = prestate.executeQuery();
        if( result.next() )
        {
            id         = result.getInt("id");
            title      = result.getString("title");
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
    if (id != 0)
    {
%>
<a href="<%= response.encodeURL("../../common/mobile/mainte.jsp?id=" + id) %>" ><font color="#FF0000"><%=title%></font></a><br>
<%
    }
%>
<a href="<%= response.encodeURL("passwdchange.jsp") %>">Êß½Ü°ÄÞ•ÏX</a><br>
<a href="<%= response.encodeURL("loginhistory.jsp") %>">Û¸Þ²Ý—š—ð</a><br>
<a href="<%= response.encodeURL("info_message_list.jsp?past=y") %>"><%=username%>—l‚Ö‚ÌÒ¯¾°¼Þ</a><br>
<!--
<a href="<%= response.encodeURL("info_update.jsp") %>" >‚¨’m‚ç‚¹</a><font color=red>NEW</font><br>
-->
<%
    if (AdminFlag)
    {
%>
<a href="<%= response.encodeURL("userlist.jsp") %>">Õ°»Þ°ŠÇ—</a><br>
<%
    }
%>
