<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.room.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<jsp:useBean id="roominfo" scope="session" class="com.hotenavi2.room.RoomInfo" />
<style>
.lineDetail:hover{
  text-decoration:underline !important;
  cursor:pointer;
}
</style>
<%
    String       selecthotel;
    String       hotelid = "";
    String       query = "";
    String       hotelname = "";
    int          i  = 0;
    boolean      getresult = true;

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("demo"))
    {
        DemoMode = true;
    }
    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
    int line_count  = 0;
%>
<thead><tr>
  <th align="center" valign="middle" nowrap class="size14 tableLN" label="num">店舗名</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN" style="display:none"></th>
  <th align="center" valign="middle" nowrap class="size14 tableLN" label="num">室数</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Total" label="num">在室</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Total" label="num">空室</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Total" label="num">準備中</th>
</tr></thead>
<tbody><%
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int          imedia_user      = 0;
    int          level            = 0;
    int room_total = 0;
    int empty_total = 0;
    int clean_total = 0;

    connection  = DBConnection.getConnection();
     // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
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

    try
    {
        if (imedia_user == 1 && level == 3 && !selecthotel.equals("all_manage"))
        {
            query = "SELECT * FROM hotel WHERE hotel.group_id =?";
            query = query + " AND hotel.plan <= 2";
            query = query + " AND hotel.plan >= 1";
            query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
        }
        else
        {
            query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
            query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND hotel.plan <= 2";
            query = query + " AND hotel.plan >= 1";
            query = query + " AND owner_user_hotel.userid = ?";
            query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
        }
        prestate    = connection.prepareStatement(query);
        if (imedia_user == 1 && level == 3 && !selecthotel.equals("all_manage"))
        {
            prestate.setString(1,selecthotel);
        }
        else
        {
            prestate.setString(1,ownerinfo.HotelId);
            prestate.setInt(2,ownerinfo.DbUserId);
        }
        result      = prestate.executeQuery();
        if  (result != null)
        {
            while( result.next() != false)
            {
                hotelid   = result.getString("hotel.hotel_id");
                hotelname = result.getString("hotel.name");
                line_count++;
                getresult = true;
                int room_count = 0;
                int empty_count = 0;
                int clean_count = 0;
                ownerinfo.sendPacket0114(1, hotelid);
                room_count        = ownerinfo.StateRoomCount;
                roominfo.HotelId = hotelid;
                roominfo.sendPacket0200();
                empty_count       = roominfo.RoomEmpty;
                clean_count       = roominfo.RoomClean;
                room_total += room_count;
                empty_total += empty_count;
                clean_total += clean_count;
%><tr class="lineDetail" height="24px" onclick="location.href='roomdisp_use.jsp?Store=<%=hotelid%>';">
    <td style="display:none"><%=i%></td>
    <td align="left" valign="middle"  class="listName size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>" nowrap><%= hotelname %><%if (result.getInt("split_flag") == 1){%><font size=1>(*)</font><%}%></td>
    <td align="right" valign="middle" class="listRoomCount size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%=room_count%></td>
    <td align="right" valign="middle" class="Total listSalesTotal size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%= room_count-(empty_count+clean_count) %></td>
    <td align="right" valign="middle" class="Total listSalesTotal size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%= empty_count %></td>
    <td align="right" valign="middle" class="Total listSalesTotal size14 tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%= clean_count %></td>
    </tr>
<%
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
</tbody>
<tr height="24px">
    <td align="left" valign="middle"  class="size14 tableLB" nowrap>&nbsp;<%=line_count%>&nbsp;店舗合計</td>
    <td align="right" valign="middle" class="size14 tableLB"><%= room_total%></td>
    <td align="right" valign="middle" class="size14 tableLB"><%= room_total-(empty_total+clean_total) %></td>
    <td align="right" valign="middle" class="size14 tableLB"><%= empty_total %></td>
    <td align="right" valign="middle" class="size14 tableRB"><%= clean_total %></td>
</tr>