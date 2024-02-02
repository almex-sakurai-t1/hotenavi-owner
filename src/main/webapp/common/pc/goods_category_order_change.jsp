<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="error/error.html" />
<%
    }
    String line_count        = ReplaceString.getParameter(request,"LineCount");
    if    (line_count       == null)                {line_count = "0";}
    int    max_i             = Integer.parseInt(line_count);

    String idx               = "0";
    String category_id       = "0";
    String seq               = "0";
    String query             = "";
    int    retresult         = 0;

    for( int i = 1 ; i <= max_i ; i++ )
    {
        idx          = ReplaceString.getParameter(request,"DispIdx" + i);
        category_id  = ReplaceString.getParameter(request,"Category" + i);
        if (idx == null)                idx = "999";
        if (!CheckString.numCheck(idx)) idx = "999";

        query = "UPDATE goods_category SET ";
        query = query + " disp_idx= ? ";
        query = query + " WHERE hotelid= ? ";
        query = query + "   AND category_id= ? ";

        ArrayList<Object> list = new ArrayList<Object>();
        list.add(Integer.parseInt(idx));
        list.add(hotelid);
        list.add(Integer.parseInt(category_id));
%>
<%=query%><br>

<%
        try
        {
            DbAccess dbwrite =  new DbAccess();
            retresult = dbwrite.execUpdate(query, list);
            dbwrite.close();
        }
        catch( Exception e )
        {
%>
            <%= e.toString() %>
<%
        }
    }
%>

<%
    response.sendRedirect("goods_list.jsp?HotelId=" + hotelid);
%>
