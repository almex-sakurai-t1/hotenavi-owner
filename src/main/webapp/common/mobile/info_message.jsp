<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String loginHotelId =  (String)session.getAttribute("HotelId");
    String DispMessage  = ReplaceString.getParameter(request,"DispMessage");

    // √{⌠З┌л⌠З∙t▌Ф⌠╬
    Calendar calnd;
    calnd = Calendar.getInstance();
    int now_year   = calnd.get(calnd.YEAR);
    int now_month  = calnd.get(calnd.MONTH) + 1;
    int now_day    = calnd.get(calnd.DATE);
    int now_hour   = calnd.get(calnd.HOUR_OF_DAY);
    int now_minute = calnd.get(calnd.MINUTE);
    int now_second = calnd.get(calnd.SECOND);
    int now_date   = now_year * 10000 + now_month  * 100 + now_day;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    int  count_msg = 0;
    connection  = DBConnection.getConnection();
    try
    {
        final String query = "SELECT count(*) FROM hh_owner_message"
            + " WHERE  hotelid    = ?"
            + " AND    userid     = ? "
            + " AND    start_date <= ? "
            + " AND    end_date   >= ? "
            + " AND    hotenavi_flag != 0"
            + " AND    mobile_flag   != 0"
            + " AND    msg_disp_flag  = 0";
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        prestate.setInt(3, now_date);
        prestate.setInt(4, now_date);

        result   = prestate.executeQuery();
        if( result.next() )
        {
            count_msg = result.getInt(1);
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
    if (count_msg > 0)
    {
%>
<a href="<%= response.encodeURL("info_message_list.jsp")%>"><%if(DispMessage== null){%>│ <%=ownerinfo.DbUserName%>≈l┌ир╞╬╟╪ч┌╙┌═┌Х┌э┌╥</a><%}else{%><%=DispMessage%><%}%>
<hr>
<%
    }
%>
