<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String    hotelid;
    String    roomcode;

    hotelid = (String)session.getAttribute("SelectHotel");
    if (hotelid.equals("all")) //多店舗の場合
    {
        String loginHotelId = (String)session.getAttribute("LoginHotelId");
        Connection        connection  = null;
        PreparedStatement prestate    = null;
        ResultSet         result      = null;
        connection        = DBConnection.getConnection();
        int    imedia_user= 0;
        int    level      = 0;
        String query      = "";
        try
        {
            query = "SELECT * FROM owner_user WHERE hotelid=?";
            query = query + " AND userid=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                imedia_user = result.getInt("imedia_user");
                level       = result.getInt("level");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }

        String selecthotel    = "demo";
        String param_hotelid  = ReplaceString.getParameter(request,"HotelId");
        if (param_hotelid != null)
        {
			if(!CheckString.hotenaviIdCheck(param_hotelid))
			{
				param_hotelid = "0";
%>
				<script type="text/javascript">
				<!--
				var dd = new Date();
				setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
				//-->
				</script>
<%
			}
            query = "SELECT * FROM hotel,owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
            query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.accept_hotelid=?";
            query = query + " AND hotel.plan <= 2";
            query = query + " AND hotel.plan >= 1";
            query = query + " AND owner_user_hotel.userid = ? ";
            query = query + " AND owner_user_hotel.hotelid = owner_user_security.hotelid";
            query = query + " AND owner_user_hotel.userid = owner_user_security.userid";
            query = query + " AND (owner_user_security.admin_flag = 1 OR owner_user_security.sec_level02=1)";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,loginHotelId);
            prestate.setString(2, param_hotelid);
            prestate.setInt(3, ownerinfo.DbUserId);
            result      = prestate.executeQuery();
            if(result.next() != false)//管理ホテルのチェック
            {
                selecthotel = param_hotelid;
            }
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
        DBConnection.releaseResources(connection);

        hotelid = selecthotel;

    }

    roomcode = ReplaceString.getParameter(request,"RoomCode");

    // パラメタセット
    if( roomcode != null )
    {
        ownerinfo.RoomCode = Integer.parseInt(roomcode);
    }
    else
    {
        ownerinfo.RoomCode = 0;
    }

    // 電文の送信
    ownerinfo.sendPacket0110(1, hotelid);
    ownerinfo.sendPacket0114(1, hotelid);
    ownerinfo.sendPacket0124(1, hotelid);
    ownerinfo.sendPacket0120(1, hotelid);
    ownerinfo.sendPacket0128(1, hotelid);
    ownerinfo.sendPacket0130(1, hotelid);
    ownerinfo.sendPacket0132(1, hotelid);
    ownerinfo.sendPacket0134(1, hotelid);
    ownerinfo.sendPacket0136(1, hotelid);
%>
