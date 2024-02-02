<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");

    ReplaceString rs = new ReplaceString();

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query       = "";
    connection  = DBConnection.getConnection();
    // imedia_user のチェック
    int          imedia_user      = 0;
    int          level            = 0;
    try
    {
        query = "SELECT imedia_user,level FROM owner_user WHERE hotelid=?";
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

    //管理店舗数の取得
    int hotel_count = 0;
    String[] t_hotelid = null;
    String[] t_name = null;
    int[] count = null;
    String inHotelId = "";
    int     i     = 0;

    try
    {
        if (imedia_user == 1 && level == 3 && !hotelid.equals("all_manage"))
        {
            query = "SELECT hotel.hotel_id,hotel.name FROM hotel WHERE (hotel.group_id =? OR hotel.hotel_id=?)";
            query+= " AND hotel.plan IN (1,3,4)";
        }
        else
        {
            query = "SELECT hotel.hotel_id,hotel.name FROM hotel";
            query+= " INNER JOIN owner_user_hotel ON hotel.hotel_id = owner_user_hotel.accept_hotelid";
            query+= " WHERE owner_user_hotel.hotelid =?";
            query+= " AND owner_user_hotel.userid =?";
            query+= " AND hotel.plan IN (1,3,4)";
            query+= " ORDER BY hotel.sort_num,hotel.hotel_id";
        }
        prestate    = connection.prepareStatement(query);
        if (imedia_user == 1 && level == 3 && !hotelid.equals("all_manage"))
        {
            prestate.setString(1, hotelid);
            prestate.setString(2, hotelid);
        }
        else
        {
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
        }
        result      = prestate.executeQuery();
        if ( result.last() != false )
        {
            hotel_count = result.getRow();
        }
        t_hotelid = new String[hotel_count];
        t_name = new String[hotel_count];
        count = new int[hotel_count];
  
        result.beforeFirst();
        i = -1;
        while( result.next() != false )
        {
            i++;
            t_hotelid[i] = result.getString("hotel_id");
            t_name[i] =  result.getString("name");
            inHotelId += "'" + result.getString("hotel_id") + "'";
            if (hotel_count != (i+1))
            {
               inHotelId += ",";
            }
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
%>
