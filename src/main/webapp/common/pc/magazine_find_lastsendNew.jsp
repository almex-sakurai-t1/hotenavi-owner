<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int i;

    Calendar cal = Calendar.getInstance();
    int year = cal.get(cal.YEAR);
    int month = cal.get(cal.MONTH) + 1;
    int day = cal.get(cal.DATE);

    String mag_address        = "";
    String hotelid            = "";
    String query              = "";
    String query_hotelid      = "";

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String selecthotel  = (String)session.getAttribute("SelectHotel");
    if    (selecthotel.compareTo("all") == 0)
    {
           hotelid    = (String)session.getAttribute("HotelId");
    }
    else
    {
           hotelid    = selecthotel;
    }

    int    lastsend_start       = 0;
    int    lastsend_end         = 0;
    int    lastsend_start_hhmm  = 0;
    int    lastsend_end_hhmm    = 0;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    int        imedia_user        = 0;
    int        level              = 0;
    int        store_count        = 0;
    String     group_id           = "";

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
    if(imedia_user == 1 && level == 3)
    {
        if (hotelid.compareTo("demo") == 0 || hotelid.compareTo("happyhotel") == 0 || hotelid.compareTo("service") == 0 || hotelid.compareTo("info") == 0)
        {
            query_hotelid = "'" + hotelid + "'";
        }
        else
        {
            query = "SELECT * FROM search_hotel_find WHERE findstr8 =?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if  (result != null)
            {
                if( result.next() != false )
                {
                    group_id = result.getString("findstr7");
                }
            }
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
            if (group_id.compareTo("") == 0)
            {
                query = "SELECT * FROM hotel,search_hotel_find WHERE search_hotel_find.findstr8=?";
            }
            else
            {
                query = "SELECT * FROM hotel,search_hotel_find WHERE search_hotel_find.findstr7='" + group_id + "'";
            }
            query = query + " AND  hotel.hotel_id = search_hotel_find.findstr8";
            query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
            prestate    = connection.prepareStatement(query);
            if (group_id.compareTo("") == 0)
            {
                prestate.setString(1, hotelid);
            }
            result      = prestate.executeQuery();
            if  (result != null)
            {
                while( result.next() != false )
                {
                    store_count++;
                    if (store_count > 1)
                    {
                        query_hotelid = query_hotelid + ",";
                    }
                    query_hotelid = query_hotelid + "'" + result.getString("hotel.hotel_id") + "'";
                }
            }
        }
    }
    else
    {
        query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + loginHotelId + "'";
        if    (selecthotel.compareTo("all") != 0)
        {
            query = query + " AND hotel.hotel_id =?";
        }
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
        query = query + " AND hotel.plan <= 4";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.plan != 2";
        query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
        prestate    = connection.prepareStatement(query);
        if    (selecthotel.compareTo("all") != 0)
        {
            prestate.setString(1, hotelid);
        }
        result      = prestate.executeQuery();
        if  (result != null)
        {
            while( result.next() != false )
            {
                store_count++;
                if (store_count > 1)
                {
                    query_hotelid = query_hotelid + ",";
                }
                query_hotelid = query_hotelid + "'" + result.getString("owner_user_hotel.accept_hotelid") + "'";
            }
        }
    }

    try
    {
        query = "SELECT * FROM mag_address WHERE hotel_id IN(" + query_hotelid + ")";
        query = query + " AND last_senddate <> 0";
        query = query + " AND state = 1 ORDER BY last_senddate,last_sendtime";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           lastsend_start       = result.getInt("last_senddate");
           lastsend_start_hhmm  = result.getInt("last_sendtime");
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
        query = "SELECT * FROM mag_address WHERE hotel_id IN(" + query_hotelid + ")";
        query = query + " AND state = 1 ORDER BY last_senddate DESC,last_sendtime DESC";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           lastsend_end       = result.getInt("last_senddate");
           lastsend_end_hhmm  = result.getInt("last_sendtime");
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


    if(lastsend_start == 0)
    {
       lastsend_start = year*10000 + month*100 + day;
       lastsend_end   = year*10000 + month*100 + day;
    }
%>

  <input name="LastSendCheck" type="checkbox" id="LastSendCheck" value="1" onClick="setConditionCheckOnly(this);">
       以下のメールアドレスに送信しない<br>
　　　【登録メールアドレスの最終送信日時】<br>
　　
<select name="LastSendStartYear">
<%
    for( i = 4 ; i >= 0 ; i-- )
    {
%>
    <option value="<%= year - i %>" <%if (lastsend_start/10000 == year - i){%>selected<%}%>><%= year - i %></option>
<%
    }
%>
    </select>
    年
    <select name="LastSendStartMonth">
<%
    for( i = 0 ; i < 12 ; i++ )
    {
        if( (i + 1) == lastsend_start/100%100 )
        {
%>
    <option value="<%= i + 1 %>" selected><%= i + 1 %></option>
<%
        }
        else
        {
%>
    <option value="<%= i + 1 %>"><%= i + 1 %></option>
<%
        }
    }
%>
    </select>
    月
    <select name="LastSendStartDay">
<%
    for( i = 0 ; i < 31 ; i++ )
    {
        if( i+1 == lastsend_start%100 )
        {
%>
    <option value="<%= i + 1 %>" selected><%= i + 1 %></option>
<%
        }
        else
        {
%>
    <option value="<%= i + 1 %>"><%= i + 1 %></option>
<%
        }
    }
%>
    </select>
    日
    <select name="LastSendStartHour">
<%
    for( i = 0 ; i < 24 ; i++ )
    {
%>
    <option value="<%= i %>" <%if (lastsend_start_hhmm/100 == i){%>selected<%}%>><%= i %></option>
<%
    }
%>
    </select>
    時
    <select name="LastSendStartMinute">
<%
    for( i = 0 ; i < 60 ; i++ )
    {
%>
    <option value="<%= i %>" <%if (lastsend_start_hhmm%100 == i){%>selected<%}%>><%= i %></option>
<%
    }
%>
    </select>
    分～

    <select name="LastSendEndYear">
<%
    for( i = 4 ; i >= 0 ; i-- )
    {
%>
    <option value="<%= year - i %>" <%if (lastsend_end/10000 == year - i){%>selected<%}%>><%= year - i %></option>
<%
    }
%>
    </select>
    年
    <select name="LastSendEndMonth">
<%
    for( i = 0 ; i < 12 ; i++ )
    {
        if( (i + 1) == lastsend_end/100%100 )
        {
%>
    <option value="<%= i + 1 %>" selected><%= i + 1 %></option>
<%
        }
        else
        {
%>
    <option value="<%= i + 1 %>"><%= i + 1 %></option>
<%
        }
    }
%>
    </select>
    月
    <select name="LastSendEndDay">
<%
    for( i = 0 ; i < 31 ; i++ )
    {
        if( i+1 == lastsend_end%100)
        {
%>
    <option value="<%= i + 1 %>" selected><%= i + 1 %></option>
<%
        }
        else
        {
%>
    <option value="<%= i + 1 %>"><%= i + 1 %></option>
<%
        }
    }
%>
    </select>
    日
    <select name="LastSendEndHour">
<%
    for( i = 0 ; i < 24 ; i++ )
    {
%>
    <option value="<%= i %>" <%if (lastsend_end_hhmm/100 == i){%>selected<%}%>><%= i %></option>
<%
    }
%>
    </select>
    時
    <select name="LastSendEndMinute">
<%
    for( i = 0 ; i < 60 ; i++ )
    {
%>
    <option value="<%= i %>" <%if (lastsend_end_hhmm%100 == i){%>selected<%}%>><%= i %></option>
<%
    }
%>
    </select>
    分
