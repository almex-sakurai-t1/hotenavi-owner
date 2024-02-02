<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.custom.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int State = 2;
    if (ReplaceString.getParameter(request,"Restore") != null)
    {
       if (ReplaceString.getParameter(request,"Restore").equals("Y"))
       {
           State = 1;  //配信する
       }
    }

    int i;
    int result_up = 0;
    boolean result_packet;
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String hotelid = (String)session.getAttribute("SelectHotel");

    String query;
    String hotelquery;
    String selecthotel;
    String unknown_list[];

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    Connection        connection_update  = null;
    PreparedStatement prestate_update    = null;
    ResultSet         result_update      = null;
    connection_update    = DBConnection.getConnection();


    CustomInfo custominfo = new CustomInfo();

    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    unknown_list = request.getParameterValues("UnknownAddress");

    for( i = 0 ; i < unknown_list.length ; i++ )
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
        if( selecthotel.compareTo("all") == 0 )
        {
            hotelquery = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
            hotelquery = hotelquery + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            hotelquery = hotelquery + " AND owner_user_hotel.userid = ?";
            hotelquery = hotelquery + " AND hotel.plan <= 4";
            hotelquery = hotelquery + " AND hotel.plan >= 1";
            hotelquery = hotelquery + " AND hotel.plan != 2";

            query = "UPDATE mag_address SET state=" + State;
            if (State == 1)
            {
                query = query + ", unknown_flag = 0 ";
            }
            query = query + ", last_update=" + nowdate + ", last_uptime=" + nowtime + " ";
            query = query + " WHERE (hotel_id=?";

            prestate    = connection.prepareStatement(hotelquery);//管理店舗の読み込み
            prestate.setString(1,loginHotelId );
            prestate.setInt(2,ownerinfo.DbUserId);
            result      = prestate.executeQuery();
            if( result != null )
            {
                while( result.next() != false )
                {
                    query = query + " OR hotel_id='" +  result.getString("owner_user_hotel.accept_hotelid") + "'"; 
                }
            }

            query = query + " ) ";
            query = query + " AND " + "address='" + ReplaceString.SQLEscape(unknown_list[i]) + "'";
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
        else
        {
            query = "UPDATE mag_address SET state=" + State;
            if (State == 1)
            {
                query = query + ", unknown_flag = 0 ";
            }
            query = query + ", last_update=" + nowdate + ", last_uptime=" + nowtime + " ";
            query = query + " WHERE hotel_id=?" + " AND " + "address='" + ReplaceString.SQLEscape(unknown_list[i]) + "'";
        }
        prestate   = connection.prepareStatement(query);
        prestate.setString(1,hotelid );
        result_up  = prestate.executeUpdate();
        DBConnection.releaseResources(prestate);


        // ホテル側に通知
        selecthotel = (String)session.getAttribute("SelectHotel");
        if( selecthotel.compareTo("all") == 0 )
        {
            hotelquery = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
            hotelquery = hotelquery + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            hotelquery = hotelquery + " AND owner_user_hotel.userid = ?";
            hotelquery = hotelquery + " AND hotel.plan <= 4";
            hotelquery = hotelquery + " AND hotel.plan >= 1";
            hotelquery = hotelquery + " AND hotel.plan != 2";

            query = "SELECT * FROM mag_address WHERE (hotel_id=?";

            prestate    = connection.prepareStatement(hotelquery);//管理店舗の読み込み
            prestate.setString(1,loginHotelId );
            prestate.setInt(2,ownerinfo.DbUserId);
            result      = prestate.executeQuery();
            if( result != null )
            {
                while( result.next() != false )
                {
                    query = query + " OR hotel_id='" +  result.getString("owner_user_hotel.accept_hotelid") + "'"; 
                }
            }
            query = query + " ) ";
            query = query + " AND address='"+ unknown_list[i] + "'";
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
        else
        {
            query = "SELECT * FROM mag_address WHERE hotel_id=?";
            query = query + " AND address='"+ unknown_list[i] + "'";
        }

        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid );
        result      = prestate.executeQuery();
        if( result != null )
        {
            while( result.next() != false )
            {
                if ( State == 2)
                {

                    if( result.getString("custom_id").compareTo("") == 0 )
                    {
                        custominfo.CustomId = "    ";
                    }
                    else
                    {
                        custominfo.CustomId = result.getString("custom_id");
                    }

                    if( result.getInt("birthday1") == 0 )
                    {
                        custominfo.Birthday1 = "00";
                    }
                    else
                    {
                        custominfo.Birthday1 = Integer.toString(result.getInt("birthday1") / 100);
                    }

                    if( result.getInt("birthday1") == 0 )
                    {
                        custominfo.Birthday2 = "00";
                    }
                    else
                    {
                        custominfo.Birthday2 = Integer.toString(result.getInt("birthday1") % 100);
                    }

                    if( result.getString("user_id").compareTo("") == 0 )
                    {
                        custominfo.UserId = "    ";
                    }
                    else
                    {
                        custominfo.UserId = result.getString("user_id");
                    }

                    if( result.getString("password").compareTo("") == 0 )
                    {
                        custominfo.Password = "    ";
                    }
                    else
                    {
                        custominfo.Password = result.getString("password");
                    }

                    custominfo.HotelId = result.getString("hotel_id");
                    custominfo.MailmagAddress = MailAddressEncrypt.decrypt(unknown_list[i]);
                    custominfo.MailmagKind = 2;

                    result_packet = custominfo.sendPacket1016();
                }
                query = "INSERT INTO mag_address_history SET";
                query = query + " hotel_id = '" + custominfo.HotelId + "'";
                query = query + ",address = '" + ReplaceString.SQLEscape(unknown_list[i])  + "'";
                query = query + ",custom_id = '" + custominfo.CustomId + "'";
                query = query + ",user_id = '" + custominfo.UserId + "'";
                query = query + ",password = '" + custominfo.Password + "'";
                query = query + ",state =" + State;
                if (State == 1)
                {
                query = query + ",unknown_flag = 0";
                }
                query = query + ",regist_date =" + nowdate;
                query = query + ",regist_time =" + nowtime;
                query = query + ",user_agent ='" + request.getHeader("user-agent") + "'";
                query = query + ",remote_ip ='" + ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() ) + "'";
                query = query + ",url ='" + request.getRequestURL().toString() + "'";
                prestate_update    = connection_update.prepareStatement(query);
                prestate_update.executeUpdate();
                DBConnection.releaseResources(prestate_update);

            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    DBConnection.releaseResources(connection_update);
    DBConnection.releaseResources(connection);
%>



