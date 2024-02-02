<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%!
    private static boolean checkHotelId(String loginHotelId, String hotelid, int user_id, int sec_level)
    {
        boolean HotelIdCheck = true;

        // 管理店舗のチェック
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            final String query;
            if (sec_level <= 3)
            {
                query = "SELECT * FROM hotel,owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                      + "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                      + "AND owner_user_hotel.userid = ? "
                      + "AND owner_user_hotel.accept_hotelid = ? "
                      + "AND hotel.plan <= 2 "
                      + "AND hotel.plan >= 1 "
                      + "AND owner_user_hotel.hotelid = owner_user_security.hotelid "
                      + "AND owner_user_hotel.userid = owner_user_security.userid "
                      // ToDo: テーブルのカラムに数字3桁のやつが出てきたらもう知らん・・・。
                      + "AND (owner_user_security.admin_flag = 1 OR owner_user_security.sec_level" + String.format("%02d", sec_level) + " = 1)";
            }
            else
            {
                query = "SELECT * FROM hotel,owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                      + "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                      + "AND owner_user_hotel.userid = ? "
                      + "AND owner_user_hotel.accept_hotelid = ? "
                      + "AND (hotel.plan = 1 OR hotel.plan = 3 OR hotel.plan = 4) "
                      + "AND owner_user_hotel.hotelid = owner_user_security.hotelid "
                      + "AND owner_user_hotel.userid = owner_user_security.userid "
                      // ToDo: テーブルのカラムに数字3桁のやつが出てきたらもう知らん・・・。
                      + "AND (owner_user_security.admin_flag = 1 OR owner_user_security.sec_level" + String.format("%02d", sec_level) + " = 1)";
            }

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, loginHotelId );
            prestate.setInt( 2, user_id );
            prestate.setString( 3, hotelid );

            result = prestate.executeQuery();
            if(!result.next())
            {
                HotelIdCheck = false;
            }
        }
        catch( Exception e )
        {
            // ToDo: 例外が発生したらHotelIdCheckがtrueで返るけど、自分が設計したメソッドじゃないし良いのかは分かりません。
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result, prestate, connection);
        }
        return HotelIdCheck;
    }
%>
