<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    Calendar cal = Calendar.getInstance();
    int now_year  = cal.get(cal.YEAR);
    int now_month = cal.get(cal.MONTH) + 1;
    int now_day   = cal.get(cal.DATE);
    int nowdate   = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime   = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    int    hapihote_id      = 0;
    String loginHotelId     = (String)session.getAttribute("LoginHotelId");
    int    loginUserId      = ownerinfo.DbUserId;
    String hotelid          = (String)session.getAttribute("SelectHotel");
    if (hotelid.compareTo("all")==0)
    {
       hotelid = loginHotelId;
    }

    // ﾊﾟﾗﾒｰﾀ取得
    int    coupon_no        = 0;
    int    used_flag        = 0;
    int    seq              = 0;
    String query = "";
    int    edit_id          = 46;   //来店チェック取り消し
    String param_coupon_no  = ReplaceString.getParameter(request,"coupon_no");
    String param_used_flag  = ReplaceString.getParameter(request,"used");
    String param_seq        = ReplaceString.getParameter(request,"seq");
    if    (param_coupon_no != null) coupon_no = Integer.parseInt(param_coupon_no);
    if    (param_used_flag != null)
    {
        if(param_used_flag.compareTo("true") == 0)
        {
           edit_id   = 45;          //来店チェック
           used_flag = 1;
        }
    }
    if    (param_seq       != null) seq       = Integer.parseInt(param_seq);

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int retresult     = 0;
    connection  = DBConnection.getConnection();
    try
    {
        //ハピホテのIDを調べる
        query = "SELECT * FROM hh_hotel_basic WHERE hotenavi_id=?";
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
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    try
    {
        //クーポントランザクション書き換え
        query = "UPDATE hh_user_coupon SET ";
        query = query + "used_flag="   + used_flag;
        query = query + " WHERE id=" + hapihote_id + " AND coupon_no=" + coupon_no + " AND seq=" + seq;
        prestate    = connection.prepareStatement(query);
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

    try
    {
        // 履歴への書き込み処理
        query = "INSERT INTO hotenavi.hh_hotel_adjustment_history SET ";
        query = query + "id="                  + hapihote_id                             + ", ";
        query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)   + "', ";
        query = query + "user_id="             + loginUserId                             + ", ";
        query = query + "input_date="          + nowdate                                 + ", ";
        query = query + "input_time="          + nowtime                                 + ", ";
        query = query + "edit_id="             + edit_id                                 + ", ";
        query = query + "edit_sub="            + seq                                     + ", ";
        query = query + "memo='"               + Integer.toString(coupon_no)             + "' ";
        prestate    = connection.prepareStatement(query);
        retresult   = prestate.executeUpdate();
    DBSync.publish(query);

    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }
%>
<%= query %>
