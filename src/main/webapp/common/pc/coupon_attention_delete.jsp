<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>注意事項編集</title>
</head>
<%
    // ﾊﾟﾗﾒｰﾀ取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    int       retresult = 0;
    boolean   Exist = false;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    try
    {
        final String query = "DELETE FROM edit_coupon_attention WHERE hotelid=?";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        retresult = prestate.executeUpdate();
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }

    if( retresult != 0 )
    {
%>
<body background="../../common/pc/image/bg.gif" onload="window.opener.location.reload();window.close();">
<%
    }
    else
    {
%>

<body background="../../common/pc/image/bg.gif">
<INPUT name="submit_ret" type=submit value=戻る onClick="history.back()">
削除に失敗しました。
<%
    }
%>
</body>
</html>

