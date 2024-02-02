<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String year  = request.getParameter("Year");
    String month = request.getParameter("Month");
    String day   = request.getParameter("Day");
    String[] dates = new String[]{ year, month, day };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String selecthotel = (String)session.getAttribute("SelectHotel");

    boolean GroupMode = false;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection        = DBConnection.getConnection();
    int          imedia_user      = 0;
    int          level            = 0;
    String       query            = "";

     // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    if (imedia_user == 1 && level == 3)
    {
        try
        {
            query = "SELECT * FROM hotel WHERE hotel_id =?";
            query = query + " AND  hotel.plan = 98";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,selecthotel);
            result      = prestate.executeQuery();
            if(result.next())
            {
               GroupMode = true;
            }
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }
    DBConnection.releaseResources(connection);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>売上管理帳票ダウンロード</title>
</head>

<frameset rows="150,*" frameborder="NO" border="0" framespacing="0">
<%
    // 多店舗チェック
    if( selecthotel.compareTo("all") == 0 || GroupMode)
    {
        if( day == null )
        {
            // 月報
%>
  <frame src="report_center_dispselect_month.jsp?Year=<%= year %>&Month=<%= month %>" name="topFrame" frameborder="no" scrolling="NO" noresize marginwidth="0" marginheight="0" >
<%
        }
        else
        {
            // 日報
%>
  <frame src="report_center_dispselect_day.jsp?Year=<%= year %>&Month=<%= month %>&Day=<%= day %>" name="topFrame" frameborder="no" scrolling="NO" noresize marginwidth="0" marginheight="0" >
<%
        }
    }
    else
    {
        if( day == null )
        {
            // 月報
%>
  <frame src="report_dispselect_month.jsp?Year=<%= year %>&Month=<%= month %>" name="topFrame" frameborder="no" scrolling="NO" noresize marginwidth="0" marginheight="0" >
<%
        }
        else
        {
            // 日報
%>
  <frame src="report_dispselect_day.jsp?Year=<%= year %>&Month=<%= month %>&Day=<%= day %>" name="topFrame" frameborder="no" scrolling="NO" noresize marginwidth="0" marginheight="0" >
<%
        }
    }
%>
  <frame src="page.html" name="mainFrame" frameborder="no" scrolling="no" marginwidth="0" marginheight="0" style="height:100%">
</frameset>
<noframes><body>

</body></noframes>
</html>
