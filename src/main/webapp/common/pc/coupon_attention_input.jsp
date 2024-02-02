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

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>���ӎ����ҏW</title>
</head>

<body background="../../common/pc/image/bg.gif">

<%
    // ���Ұ��擾
    String hotelid = (String)session.getAttribute("SelectHotel");
    int       retresult = 0;
    String    query = "";
    boolean   Exist = false;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
       	query = "SELECT * FROM edit_coupon_attention WHERE hotelid=?";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            Exist = true;
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
    if( Exist )
    {
        query = "UPDATE edit_coupon_attention SET msg='" + ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg")) + "'";
        query = query + " WHERE hotelid=?";
    }
    else
    {
        query = "INSERT INTO edit_coupon_attention ( hotelid, msg ) VALUES( ?, '" + ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_msg")) + "' )";
    }
    try
    {
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        retresult   = prestate.executeUpdate();
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

<%
    if( retresult == 1  )
    {
%>
<body background="../../common/pc/image/bg.gif" onload="window.opener.location.reload();window.close();">
<%
    }
    else
    {
%>
<body background="../../common/pc/image/bg.gif" >
        �o�^�Ɏ��s���܂����B<br>
<INPUT name="submit_ret" type=submit value=�߂� onClick="history.back();">
<%
    }
%>
</body>
</html>

