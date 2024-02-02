<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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

<%
    // ﾊﾟﾗﾒｰﾀ取得
    String hotelid = ReplaceString.getParameter(request,"HotelId");
	if( hotelid != null && !CheckString.hotenaviIdCheck(hotelid))
	{
        response.sendError(400);
        return;
	}
    String id = ReplaceString.getParameter(request,"Id");
    int    hapihote_id         = 0;
    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int retresult     = 0;
    connection  = DBConnection.getConnection();
    try
    {
        //ハピホテのIDを調べる
      	query  = "SELECT * FROM hh_hotel_basic WHERE hotenavi_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
           hapihote_id = result.getInt("id");
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(result);
    }

    try
    {
        //クーポンの削除
        query = "DELETE FROM edit_coupon WHERE hotelid=? AND id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, Integer.parseInt(id));
        prestate.executeUpdate();
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(prestate);
    }

    try
    {
        //クーポンマスターの削除
        query = "DELETE FROM hh_master_coupon ";
        query += " WHERE id=? AND coupon_no=? AND service_flag=2";
        prestate    = connection.prepareStatement(query);
        prestate.setInt(1, hapihote_id);
        prestate.setInt(2, Integer.parseInt(id));
        retresult   = prestate.executeUpdate();
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
    	DBConnection.releaseResources(prestate);
    }

    DBConnection.releaseResources(connection);
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>クーポン削除結果</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/coupon.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/coupon.js"></script>
</head>


<%
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
        削除に失敗しました。
<input name="submit_ret" type="button" value="戻る" onClick="history.back()">
<%
    }
%>
</body>
</html>

