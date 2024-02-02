<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.net.URLEncoder" %> 
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    int                i;
    int                value;
    int                year;
    int                month;
    int                day;
    int                maxday;
    String             ymd;
    String             endymd;
    String             hotelid;
    String             param_year;
    String             param_month;
    NumberFormat       nf;
    GregorianCalendar  cal;

    hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = session.getAttribute("HotelId").toString();
    }

    param_year = request.getParameter("Year");
    if( param_year != null )
    {
        if ( !CheckString.numCheck( param_year ) )
        {
            response.sendError(400);
            return;
        }
        year = Integer.valueOf(param_year).intValue();
    }
    else
    {
        year = 0;
    }

    nf = new DecimalFormat("00");
%>

<hr>
ŒŽ•Ê±¸¾½”<br>
<br>
<%= year %>”N•ª<br>
<%
    for( i = 1 ; i <= 12 ; i++ )
    {
        ymd    = year + "-" + i + "-01";
        endymd = year + "-" + i + "-31";

        // 1ƒ–ŒŽ•ª—ÝŒv
        value  = 0;

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            final String query = "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id = ? AND acc_date BETWEEN ? AND ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setString(2, ymd);
            prestate.setString(3, endymd);

            result = prestate.executeQuery();
            if( result.next() )
            {
                value = result.getInt(1);
            }
        }
        catch (Exception e)
        {
            Logging.error(e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }
%>
<a href="<%= response.encodeURL("accessmonth.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year=" + year + "&Month=" + i) %>"><%= nf.format(i) %>ŒŽ:<%= Kanma.get(value) %></a><br>

<%
    }
%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="accessyear.jsp" />
  <jsp:param name="dispname" value="ŒŽ•Ê±¸¾½”" />
</jsp:include>
