<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.text.*" %><%@ page errorPage="error.jsp" %><%@ page import="java.sql.*" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<jsp:useBean id="roominfo" scope="session" class="com.hotenavi2.room.RoomInfo" /><%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%><jsp:forward page="timeout.jsp" />
<%
    }
    String          now_date;
    String          now_time;
    DateEdit        df;
    df        = new DateEdit();
    now_date  = df.getDate(1);
    now_time  = df.getTime(2);

    String       selecthotel;
    String       hotelid = "";
    String       hotelname = "";
    int          i  = 0;
    boolean      dataExist = false;

    boolean      getresult = true;

    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }
    int line_count  = 0;

    int room_total = 0;
    int empty_total = 0;
    int clean_total = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                           + "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                           + "AND hotel.plan <= 2 "
                           + "AND hotel.plan >= 1 "
                           + "AND owner_user_hotel.userid = ? "
                           + "ORDER BY hotel.sort_num,hotel.hotel_id";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
%><table class="uriage">
<div style="text-align:right">最終更新：<%= now_date %>&nbsp;<%= now_time %></div>
<thead><tr>
  <th label="num">店舗名</th>
  <th style="display:none"></th>
  <th label="num">室数</th>
  <th label="num">在室</th>
  <th label="num">空室</th>
  <th label="num">準備中</th>
</tr></thead>
<tbody>
<%
        Object lock = request.getSession();
        synchronized(lock)
        {
            while(result.next()!= false)
            {
                hotelid   = result.getString("hotel.hotel_id");
                hotelname = result.getString("hotel.name");
                getresult = true;
                int room_count = 0;
                int empty_count = 0;
                int clean_count = 0;
                ownerinfo.sendPacket0114(1, hotelid);
                room_count        = ownerinfo.StateRoomCount;
                roominfo.HotelId = hotelid;
                getresult = roominfo.sendPacket0200(1,hotelid);
                if (getresult)
                {
                    dataExist = true;
                    line_count++;
                    empty_count       = roominfo.RoomEmpty;
                    clean_count       = roominfo.RoomClean;
                    room_total += room_count;
                    empty_total += empty_count;
                    clean_total += clean_count;
                }
                i++;
%><tr class="storeDetail" height="24px" onclick="location.href='roominfo.jsp?HotelId=<%=hotelid%>';">
    <td style="display:none"><%=i%></td>
    <td><a href="roominfo.jsp?HotelId=<%=hotelid%>"><%= hotelname %></a></td>
<%
                if( getresult == false )
                {
%><td colspan="4">取得できません</td></tr>
<%
                }
                else
                {
%>    <td class="roomdetail"><%= room_count%></td>
    <td class="roomdetail"><%= room_count-(empty_count+clean_count) %></td>
    <td class="roomdetail"><%= empty_count %></td>
    <td class="roomdetail"><%= clean_count %></td>
<%
                }
%>
    </tr>
<%
            }
        }
%></tbody>
<%
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }

    if( !dataExist )
    {
%>管理するホテルがありません<br>
<%
    }
    else
    {
%><tr height="24px" class="uriage">
    <td>&nbsp;<%=line_count%>&nbsp;店舗合計</td>
    <td class="roomdetail2"><%= room_total%></td>
    <td class="roomdetail2"><%= room_total-(empty_total+clean_total) %></td>
    <td class="roomdetail2"><%= empty_total %></td>
    <td class="roomdetail2"><%= clean_total %></td>
</tr>
<%
    }
%></table>
