<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
    // �z�e��ID�擾
    String hotelid = (String)session.getAttribute("SelectHotel");
     String    msg="";

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        final String query = "SELECT * FROM edit_coupon_attention WHERE hotelid=?";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            msg = result.getString("msg");
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>���ӎ����ҏW</title>
</head>

<body background="../../common/pc/image/bg.gif">

<form action="coupon_attention_input.jsp?HotelId=<%= hotelid %>" method="post" target="_self">

  �{�� <TEXTAREA class="size" name="col_msg" rows="10" cols="64" ><%= msg %></TEXTAREA><br>


<INPUT name="atnsubmit" type="submit" value="�o�^">

</form>

<form action="coupon_attention_delete.jsp?HotelId=<%= hotelid %>" method="POST">
<INPUT name="submit_del" type="submit" value="�폜" >
</form>

<form action="coupon_edit.jsp?HotelId=<%= hotelid %>" method="POST" target="mainFrame">
<INPUT name="submit_ret" type="submit" value="����" onClick="window.close()">
</form>
</body>
</html>
