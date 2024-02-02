<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
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
    int         i;
    int         value;
    int         year;
    int         month;
    int         day;
    int         maxday;
    int         ymd;
    String      hotelid;
    String      param_year;
    String      param_month;
    NumberFormat    nf;
    GregorianCalendar    cal;

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
    if(CheckString.isValidParameter(param_year) && !CheckString.numCheck(param_year))
    {
        response.sendError(400);
        return;
    }
    if( param_year != null )
    {
        year = Integer.valueOf(param_year).intValue();
    }
    else
    {
        year = 0;
    }
    param_month = request.getParameter("Month");
    if(CheckString.isValidParameter(param_month) && !CheckString.numCheck(param_month))
    {
        response.sendError(400);
        return;
    }
    if( param_month != null )
    {
        month = Integer.valueOf(param_month).intValue();
    }
    else
    {
        month = 0;
    }

    if( year != 0 && month != 0 )
    {
        cal = new GregorianCalendar( year, month-1, 1);
    }
    else
    {
        cal = new GregorianCalendar();
    }

    // Å‘å“úŽæ“¾
    maxday = cal.getActualMaximum(cal.DAY_OF_MONTH);

    nf = new DecimalFormat("00");
%>

<hr class="border">
<h2>“ú•Ê±¸¾½”</h2>
<h2><%= year %>/<%= nf.format(month) %>•ª</h2>
<%
    for( i = 1 ; i <= maxday ; i++ )
    {
        ymd = (year * 10000) + (month * 100) + i;

        // 1“ú•ª—ÝŒv
        value  = 0;

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            final String query = "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id = ? AND acc_date = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, ymd);

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

<table class="uriage">
<tr>
<td width="50%"><%= nf.format(i) %>“ú</td><td class="uriage"><%= Kanma.get(value) %></td>
</tr>
</table>

<%
    }
%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="accessmonth.jsp" />
  <jsp:param name="dispname" value="“ú•Ê±¸¾½”" />
</jsp:include>
