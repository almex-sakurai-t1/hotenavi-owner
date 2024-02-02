<%@ page contentType="text/html;charset=Windows-31J"%><%@ page import="java.sql.*" %><%@ page import="java.net.*" %>
<%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    if  (loginHotelId == null)
    {
         loginHotelId = "demo";
    }

    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    String     selecthotel   = "";
    selecthotel = request.getParameter("Store");
    if  (selecthotel == null)
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
    }

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    String query = "";
    int      trial_date = 99999999;

    query = "SELECT trial_date FROM hotel_element WHERE hotel_id= ?";
    prestate    = connection.prepareStatement(query);
    prestate.setString( 1, selecthotel);
    result      = prestate.executeQuery();
    if (result != null)
    {
        if( result.next() != false )
        {
            trial_date              	= result.getInt("trial_date");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    String paramCount = request.getParameter("count");
    if (paramCount != null)
    {
        for (int i = 0; i < Integer.parseInt(paramCount) ; i++)
        {
            int       room_no= 0;
            int       disp_flg= 0;
            int       end_date = 0;
            java.sql.Date end;
            int yy = 0;
            int mm = 0;
            int dd = 0;
            String    room_name        = "";
            String    equipment        = "";
            int       rank             = 0;
            String    image_pc         = "";
            String    image_mobile     = "";
            String    movie_mobile     = "";
            String    image_thumb      = "";
            int       sync_flag        = 0;
            String    memo             = "";
            String    refer_name       = "";
            query =  "SELECT * FROM room WHERE hotelid = ?";
            query += " AND id = ? AND room_no = ?";
            prestate    = connection.prepareStatement(query);
            prestate.setString( 1, selecthotel);
            prestate.setInt( 2, Integer.parseInt(request.getParameter("id_"+i)));
            prestate.setInt( 3, Integer.parseInt(request.getParameter("room_no_"+i)));
            result      = prestate.executeQuery();
            if (result != null)
            {
                if( result.next() != false )
                {
                    room_no      = result.getInt("room_no");
                    disp_flg     = result.getInt("disp_flg");
                    end = result.getDate("end_date");
                    yy = end.getYear();
                    mm = end.getMonth();
                    dd = end.getDate();
                    end_date = (yy+1900)*10000 + (mm+1)*100 + dd;
                    room_name    = result.getString("room_name");
                    rank         = result.getInt("rank");
                    equipment    = result.getString("equipment");
                    image_pc     = result.getString("image_pc");
                    image_mobile = result.getString("image_mobile");
                    movie_mobile = result.getString("movie_mobile");
                    image_thumb  = result.getString("image_thumb");
                    memo         = result.getString("memo");
                    refer_name   = result.getString("refer_name");
                }
            }
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);

            int img_count = 0;
            if (request.getParameter("img_count_"+i) != null)
            {
                img_count = Integer.parseInt(request.getParameter("img_count_"+i));
            }
            if (img_count == 0)
            {
                image_pc = "<div id=\"roomImages\"><img src=\"/contents/%HOTELID%/img/nowprinting.png\" alt=\"%ROOMTITLE%\" /></div>";
            }
            else
            {
                image_pc = "<div id=\"roomImages\">";
                image_pc = image_pc+"<img src=\"/contents/%HOTELID%/img/%ROOMNO%.png\" alt=\"%ROOMTITLE%\" /><br>";
                for (int j = 2; j <= img_count; j++)
                {
                    image_pc = image_pc+"<a href=\"/contents/%HOTELID%/img/%ROOMNO%-"+j+".png\" class=\"img-popup\" style=\"max-width: calc(100%/"+((img_count-1)==1?2:(img_count-1))+") !important\"><img src=\"/contents/%HOTELID%/img/%ROOMNO%-"+j+".png\" style=\"width:100%\" /></a>";
                }
               image_pc = image_pc+"</div>";
            }
            if (img_count == 0)
            {
                image_thumb = "<a href=\"room%ROOMNO%.jsp\" class=\"link1\"><img src=\"/contents/%HOTELID%/img/nowprinting_s.png\" width=\"100\" class=\"thumbroom\" alt=\"%ROOMTITLE%\"></a><br><a href=\"room%ROOMNO%.jsp\" class=\"link1\">%ROOMNAME%</a>";
            }
            else
            {
                image_thumb = "<a href=\"room%ROOMNO%.jsp\" class=\"link1\"><img src=\"/contents/%HOTELID%/img/thumb%ROOMNO%.png\" width=\"100\" class=\"thumbroom\" alt=\"%ROOMTITLE%\"></a><br><a href=\"room%ROOMNO%.jsp\" class=\"link1\">%ROOMNAME%</a>";
            }
            query = "INSERT INTO room SET";
            query += " hotelid='"      + selecthotel + "'";
            query += ",add_hotelid='"  + loginHotelId + "'";
            query += ",add_date="      + nowdate;
            query += ",add_time="      + nowtime;
            query += ",add_userid="    + ownerinfo.DbUserId;
            query += ",room_no="       + room_no;
            query += ",disp_flg="      + disp_flg;
            query += ",start_date="    + trial_date;
            query += ",end_date="      + end_date;
            query += ",room_name='"    + ReplaceString.SQLEscape(room_name) + "'";
            query += ",rank="          + rank;
            query += ",equipment='"    + ReplaceString.SQLEscape(equipment) + "'";
            query += ",image_pc='"     + ReplaceString.SQLEscape(image_pc) + "'";
            query += ",image_mobile='" + ReplaceString.SQLEscape(image_mobile) + "'";
            query += ",movie_mobile='" + ReplaceString.SQLEscape(movie_mobile) + "'";
            query += ",image_thumb='"  + ReplaceString.SQLEscape(image_thumb) + "'";
            query += ",memo='"         + ReplaceString.SQLEscape(memo) + "'";
            query += ",refer_name='"   + ReplaceString.SQLEscape(refer_name) + "'";
            query += ",upd_hotelid='"  + loginHotelId + "'";
            query += ",upd_userid="    + ownerinfo.DbUserId;
            query += ",last_update="   + nowdate;
            query += ",last_uptime="   + nowtime;
            query += ",sync_flag="     + sync_flag;
            prestate = connection.prepareStatement(query);
            prestate.executeUpdate();
            DBConnection.releaseResources(prestate);
        }
    }
    DBConnection.releaseResources(connection);
    response.sendRedirect("hpedit.jsp?Type=pc&HotelId="+selecthotel+"&Target=room_list2");
%>
