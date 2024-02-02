<%@ page contentType="text/html; charset=Windows-31J" %>
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
    int         value = 0;
    int         year;
    int         month;
    int         day;
    int         maxday;
    int         ymd;
    String      hotelid;
    String      param_year;
    String      param_month;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    NumberFormat    nf;
    GregorianCalendar    cal;

    hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = session.getAttribute("HotelId").toString();
    }

    param_year = ReplaceString.getParameter(request,"Year");
    param_month = ReplaceString.getParameter(request,"Month");
    String[] dates = new String[]{ param_year, param_month };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }
    if( param_year != null )
    {
        year = Integer.valueOf(param_year).intValue();
    }
    else
    {
        year = 0;
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

<hr>
“ú•Ê±¸¾½”<br>
<br>
<%= year %>/<%= nf.format(month) %>•ª<br>
<%
    for( i = 1 ; i <= maxday ; i++ )
    {
        ymd = (year * 10000) + (month * 100) + i;

        try
        {
            // 1“ú•ª—ÝŒv
            value  = 0;
            connection        = DBConnection.getConnection();
            final String query  = "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id= ? AND acc_date= ?";

            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, ymd);

            result      = prestate.executeQuery();
            if( result.next() )
            {
                value = result.getInt(1);
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

<%= nf.format(i) %>“ú:<%= Kanma.get(value) %><br>

<%
    }
%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="accessmonth.jsp" />
  <jsp:param name="dispname" value="“ú•Ê±¸¾½”" />
</jsp:include>
