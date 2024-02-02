<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String loginHotelId =  (String)session.getAttribute("LoginHotelId");
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String limit_flag = ReplaceString.getParameter(request,"limit_flag");
    if(limit_flag   == null) limit_flag = "true";
    if(limit_flag.compareTo("") == 0) limit_flag ="true";
    String  InputCustomId = ReplaceString.getParameter(request,"custom_id");
    String  InputEntryId  = ReplaceString.getParameter(request,"EntryId");
    if     (InputEntryId == null) InputEntryId = "0";
    int     entry_id      = Integer.parseInt(InputEntryId);
    String  InputStatus   = ReplaceString.getParameter(request,"Status");

    DateEdit dateedit = new DateEdit();

    int nowdate   =  Integer.parseInt(dateedit.getDate(2));
    int nowtime   =  Integer.parseInt(dateedit.getTime(1));

    int pageno;
    String param_page = ReplaceString.getParameter(request,"page");
    if( param_page == null )
    {
        pageno = 0;
    }
    else
    {
        pageno = Integer.parseInt(param_page);
    }

    String     query              = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    query = "UPDATE goods_entry SET ";
    query = query + " status=? , ";
    query = query + " emit_date="     + nowdate                       + ", ";
    query = query + " emit_time="     + nowtime                       + ", ";
    query = query + " emit_hotelid='" + loginHotelId                  + "', ";
    query = query + " emit_userid="   + ownerinfo.DbUserId            + " ";
    query = query + " WHERE hotelid='" + hotelid   + "'";
    query = query + "  AND  entry_id=" + entry_id;

    try
    {
        prestate      = connection.prepareStatement(query);
        prestate.setInt(1, Integer.parseInt(InputStatus));
        int retresult = prestate.executeUpdate();
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
<%= query%>
<%
    response.sendRedirect("goods_report.jsp?page=" + pageno + "&limit_flag=" + limit_flag);
%>
