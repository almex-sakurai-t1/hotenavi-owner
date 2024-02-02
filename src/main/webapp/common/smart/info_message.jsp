<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    String dispMessage = request.getParameter("DispMessage");

    // 本日の日付取得
    Calendar calnd = Calendar.getInstance();
    int now_year   = calnd.get(calnd.YEAR);
    int now_month  = calnd.get(calnd.MONTH) + 1;
    int now_day    = calnd.get(calnd.DATE);
    int now_hour   = calnd.get(calnd.HOUR_OF_DAY);
    int now_minute = calnd.get(calnd.MINUTE);
    int now_second = calnd.get(calnd.SECOND);
    int now_date   = now_year * 10000 + now_month  * 100 + now_day;

    int count_msg = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT count(*) FROM hh_owner_message "
                           + "WHERE  hotelid    = ? "
                           + "AND    userid     = ? "
                           + "AND    start_date <= ? "
                           + "AND    end_date   >= ? "
                           + "AND    hotenavi_flag != 0 "
                           + "AND    mobile_flag   != 0 "
                           + "AND    msg_disp_flag  = 0";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        prestate.setInt(3, now_date);
        prestate.setInt(4, now_date);

        result = prestate.executeQuery();
        if( result.next() )
        {
            count_msg = result.getInt(1);
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
    if (count_msg > 0)
    {
%>
<hr class="border">
<a href="<%= response.encodeURL("info_message_list.jsp") %>"><%if(dispMessage == null){%>★<%= ownerinfo.DbUserName %>様にメッセージがあります</a><%}else{%><%= StringEscapeUtils.escapeHtml4(dispMessage) %><%}%>
<%
    }
%>
