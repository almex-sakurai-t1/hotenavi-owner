<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String       hotelid;
    String       hotelname = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    // �z�e��ID�̃Z�b�g
    hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
    // �z�e�����擾
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT name FROM hotel WHERE hotel_id= ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result      = prestate.executeQuery();
        if (result != null)
        {
            if(result.next()!= false)
            {
                hotelname = result.getString("name");
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

    if (!hotelname.equals(""))
    {
%>
<div align="center">
<%= hotelname %>
</div>
<%
    }
%>
