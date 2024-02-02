<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String id     = request.getParameter("id");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>レジャーホテル情報サイト ホテルサーチデータ修正</title>
<link href="lovehosearch.css" rel="stylesheet" type="text/css">
<link href="contents.css" rel="stylesheet" type="text/css">
</head>

<body>
<%

    int rank    = 0;
    int rank_sv = 999;
    int    ret = 0;
    int    pageno = 0;
    String query = "";
    boolean    flag      = false;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection        = DBConnection.getConnection();
    Connection        connection_upd  = null;
    PreparedStatement prestate_upd    = null;
    ResultSet         result_upd      = null;
    connection_upd  = DBConnection.getConnection();

    int    count       = 0;

    query = "SELECT * FROM hh_hotel_roomrank,hh_hotel_basic WHERE hh_hotel_roomrank.id=" + id;
    query = query + " AND hh_hotel_roomrank.id = hh_hotel_basic.id";
    query = query + " ORDER BY hh_hotel_roomrank.disp_index";
    prestate = connection.prepareStatement(query);
    result   = prestate.executeQuery();
    while( result.next() != false )
    {
%>
	<div>
		<table style="float:left" width="600px">
			<tr>
				<td class="hoteldetail_cel" width="100"><%=result.getString("hh_hotel_roomrank.rank_name")%></td>
				<td class="hoteldetail_cel">
				<table>
					<tr>
<%
        query = "SELECT * FROM hh_hotel_room_more WHERE hh_hotel_room_more.id = " + id;
        query = query + " AND hh_hotel_room_more.disp_flag <=1";
        query = query + " AND hh_hotel_room_more.room_rank = " + result.getInt("hh_hotel_roomrank.room_rank");
        prestate_upd = connection_upd.prepareStatement(query);
        result_upd   = prestate_upd.executeQuery();
        count = 0;
        while( result_upd.next() != false )
        {
            if (count % 5 == 0 && count != 0)
            {
%>
					</tr>
					<tr>
<%
            }
            count++;
%>
						<td>
							<%= result_upd.getString("hh_hotel_room_more.room_name") %><br>
<%
            if (result_upd.getString("hh_hotel_room_more.room_name").compareTo("") == 0)
            {
%>
							部屋画像なし<br>
<%
            }
            else
            {
%>
							<img src="http://happyhotel.jp/hotenavi/<%= result.getString("hh_hotel_basic.hotenavi_id") %>/i/image/r<%= result_upd.getString("hh_hotel_room_more.refer_name") %>l.gif" width="90px" height="60px"><br>
<%
			}
%>
						</td>
<%
        }
        DBConnection.releaseResources(result_upd);
        DBConnection.releaseResources(prestate_upd);
%>
					</tr>
				</table>
				</td>
			</tr>
		</table><br>
		</div>
<%
    }
    DBConnection.releaseResources(connection_upd);
    DBConnection.releaseResources(result,prestate,connection);
%>
</body>
</html>
