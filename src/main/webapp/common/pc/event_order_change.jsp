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
    String data_type         = ReplaceString.getParameter(request,"DataType");
    String disp_type         = ReplaceString.getParameter(request,"DispType");
    String smart_flg         = ReplaceString.getParameter(request,"SmartFlg");

    String line_count        = ReplaceString.getParameter(request,"LineCount");
    if    (line_count       == null)                {line_count = "0";}
    int    max_i             = Integer.parseInt(line_count);
    String disp_idx          = "0";
    String id                = "0";
    String query             = "";
    int    retresult    = 0;

    for( int i = 1 ; i <= max_i ; i++ )
    {
        disp_idx     = ReplaceString.getParameter(request,"DispIdx" + i);
        id           = ReplaceString.getParameter(request,"Id" + i);
        if (disp_idx == null)                  disp_idx = "0";
        if (!CheckString.numCheck( disp_idx )) disp_idx = "0";
        query = "UPDATE edit_event_info SET ";
        query = query + " disp_idx="        + Integer.parseInt(disp_idx) + " ";
        query = query + " WHERE hotelid='"  + hotelid   + "'";
        query = query + "   AND data_type=" + Integer.parseInt(data_type);
        query = query + "   AND id="        + Integer.parseInt(id);
        try
        {
            DbAccess dbwrite =  new DbAccess();
            retresult = dbwrite.execUpdate(query);
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
    response.sendRedirect("event_list.jsp?HotelId=" + hotelid + "&DataType=" + data_type + "&DispType=" + disp_type + "&SmartFlg=" + smart_flg );
%>
temp