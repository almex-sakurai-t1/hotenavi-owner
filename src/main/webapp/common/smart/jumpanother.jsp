<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    boolean ret = false;
    String hotelname = "";

    String year      = request.getParameter("Year");
    String month     = request.getParameter("Month");
    String day       = request.getParameter("Day");
    String endyear   = request.getParameter("EndYear");
    String endmonth  = request.getParameter("EndMonth");
    String endday    = request.getParameter("EndDay");
    String ymd       = request.getParameter("Ymd");
    String parameter = "";
    if (year != null)
    {
        parameter = parameter + "&Year=" + URLEncoder.encode(year, "Windows-31J");
    }
    if (month != null)
    {
        parameter = parameter + "&Month=" + URLEncoder.encode(month, "Windows-31J");
    }
    if (day != null)
    {
        parameter = parameter + "&Day=" + URLEncoder.encode(day, "Windows-31J");
    }
    if (endyear != null)
    {
        parameter = parameter + "&EndYear=" + URLEncoder.encode(endyear, "Windows-31J");
    }
    if (endmonth != null)
    {
        parameter = parameter + "&EndMonth=" + URLEncoder.encode(endmonth, "Windows-31J");
    }
    if (endday != null)
    {
        parameter = parameter + "&EndDay=" + URLEncoder.encode(endday, "Windows-31J");
    }
    if (ymd != null)
    {
        parameter = parameter + "&Ymd=" +  URLEncoder.encode(ymd, "Windows-31J");
    }

    String url       = request.getParameter("jumpurl");
    String sec_level = request.getParameter("sec_level");
    String dispname  = request.getParameter("dispname");

    String loginHotelId = (String)session.getAttribute("HotelId");
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = loginHotelId;
    }

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        String query =  "SELECT * FROM hotel,owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                     +  "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                     +  "AND hotel.hotel_id != ? ";
        if (url.indexOf("access") != -1)
        {
            query    += "AND (hotel.plan = 1 OR hotel.plan = 3 OR hotel.plan = 4) ";
        }
        else
        {
            query    += "AND hotel.plan <= 2 "
                     +  "AND hotel.plan >= 1 ";
        }
        if (url.indexOf("timechart") != -1)
        {
            query    += "AND hotel.timechart_flag = 1 ";
        }
        query        += "AND owner_user_hotel.hotelid = owner_user_security.hotelid "
                     +  "AND owner_user_hotel.userid = owner_user_security.userid "
                     +  "AND owner_user_hotel.userid = ? "
                     +  "ORDER BY hotel.sort_num,hotel.hotel_id";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setString(2, hotelid);
        prestate.setInt(3, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        // ŠÇ—“X•Ü”•ªƒ‹[ƒv
        while(result.next())
        {
            ret = true;
            hotelname = result.getString("hotel.name");
%>
<ul class="link">
<li><a href="<%= response.encodeURL(url + "?HotelId=" + result.getString("hotel.hotel_id")) + parameter %>"><%= hotelname %><%= StringEscapeUtils.escapeHtml4(dispname) %></a></li>
</ul>
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
%>
