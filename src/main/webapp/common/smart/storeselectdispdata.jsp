<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<ul class="link">
<%
    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;

    int count = 0;
    String loginHotelId = (String)session.getAttribute("HotelId");
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
        if(result.next())
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
    if (count > 1)
    {
%>
<li><a href="<%= response.encodeURL("allstoremenu.jsp") %>">ŠÇ—“X•Üî•ñ</a></li>
<%
    }
    try
    {
        final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                           + "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                           + "AND owner_user_hotel.userid = ? "
                           + "AND hotel.plan <= 4 "
                           + "AND hotel.plan >= 1 "
                           + "ORDER BY hotel.sort_num,hotel.hotel_id";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        while(result.next())
        {
%>
<li><a href="<%= response.encodeURL("ownerindex.jsp?HotelId=" + result.getString("owner_user_hotel.accept_hotelid")) %>"><%= result.getString("hotel.name") %>ÒÆ­°‚Ö</a></li>
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
</ul>
