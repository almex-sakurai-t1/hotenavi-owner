<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.sql.*" %><%@ page import="jp.happyhotel.common.*" %><jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" /><%
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String paramFlag = request.getParameter("flag");
    if (paramFlag == null) paramFlag = "1";
    if (!CheckString.numCheck( paramFlag))  paramFlag = "1";
    int  sales_cache_flag = Integer.parseInt(paramFlag);
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    connection  = DBConnection.getConnection();
    try
    {
        final String query = "UPDATE hotenavi.owner_user_security SET sales_cache_flag = ? WHERE hotelid = ? AND userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setInt(1, sales_cache_flag);
        prestate.setString(2, loginHotelId);
        prestate.setInt(3, ownerinfo.DbUserId);
        prestate.executeUpdate();
        DBSync.publish(prestate.toString().split(":",2)[1]);
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }
%>