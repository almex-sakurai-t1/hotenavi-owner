<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    boolean      ret = false;
	boolean      dateError = false;
    String       hotelname = "";

    String year     = ReplaceString.getParameter(request,"Year");
    String month    = ReplaceString.getParameter(request,"Month");
    String day      = ReplaceString.getParameter(request,"Day");
    String endyear  = ReplaceString.getParameter(request,"EndYear");
    String endmonth = ReplaceString.getParameter(request,"EndMonth");
    String endday   = ReplaceString.getParameter(request,"EndDay");
    String ymd      = ReplaceString.getParameter(request,"Ymd");
    String[] dates = new String[]{ year, month, day, endyear, endmonth, endday, ymd };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    String Parameter= "";
    if (year != null)
    {
        Parameter = Parameter + "&Year=" + year;
    }
    if (month != null)
    {
        Parameter = Parameter + "&Month=" + month;
    }
    if (day != null)
    {
        Parameter = Parameter + "&Day=" + day;
    }
    if (endyear != null)
    {
        Parameter = Parameter + "&EndYear=" + endyear;
    }
    if (endmonth != null)
    {
        Parameter = Parameter + "&EndMonth=" + endmonth;
    }
    if (endday != null)
    {
        Parameter = Parameter + "&EndDay=" + endday;
    }
    if (ymd != null)
    {
        Parameter = Parameter + "&Ymd=" + ymd;
    }

    String url      = ReplaceString.getParameter(request,"jumpurl");
    String sec_level= ReplaceString.getParameter(request,"sec_level");
    String dispname = ReplaceString.getParameter(request,"dispname");

    String loginHotelId =  (String)session.getAttribute("HotelId");
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = loginHotelId;
    }
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String  query     = "";
    try
    {
        connection        = DBConnection.getConnection();
        query = "SELECT * FROM hotel,owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? ";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND hotel.hotel_id != ? ";
        if (url.indexOf("access") != -1)
        {
            query = query + " AND (hotel.plan = 1 OR hotel.plan = 3 OR hotel.plan = 4)";
        }
        else
        {
            query = query + " AND hotel.plan <= 2";
            query = query + " AND hotel.plan >= 1";
        }
        if (url.indexOf("timechart") != -1)
        {
            query = query + " AND hotel.timechart_flag = 1";
        }
        query = query + " AND owner_user_hotel.hotelid = owner_user_security.hotelid";
        query = query + " AND owner_user_hotel.userid = owner_user_security.userid";
        query = query + " AND owner_user_hotel.userid = ? ";
        query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setString(2, hotelid);
        prestate.setInt(3, ownerinfo.DbUserId);

        result      = prestate.executeQuery();
        // ŠÇ—“X•Ü”•ªƒ‹[ƒv
        if( result != null )
        {
            while(result.next() != false)
            {
                if (!ret){
%>
<hr>
<%
                }
                ret = true;
                hotelname = result.getString("hotel.name");
%>
<a href="<%= response.encodeURL(url + "?HotelId=" + result.getString("hotel.hotel_id")) + Parameter %>"><%= hotelname %><%= dispname %></a><br>
<%
            }
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
