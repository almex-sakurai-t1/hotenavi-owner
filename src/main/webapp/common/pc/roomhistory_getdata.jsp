<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int          ymd;
    String       selecthotel;
    String       hotelid;
    String       query;
    String       hotelname = "";
    String       param_year  = ReplaceString.getParameter(request,"Year");
    String       param_month = ReplaceString.getParameter(request,"Month");
    String       param_day   = ReplaceString.getParameter(request,"Day");
    String[] dates = new String[]{ param_year, param_month, param_day };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }
    boolean      ret;
    DbAccess     db;
    ResultSet    result;

    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    if( param_year != null && param_month != null && param_day != null )
    {
        ymd = Integer.parseInt(param_year) * 10000 + Integer.parseInt(param_month) * 100 + Integer.parseInt(param_day);
    }
    else
    {
        ymd = 0;
    }
    ownerinfo.InOutGetStartDate = ymd;
    ownerinfo.InOutGetEndDate   = 0;
    ownerinfo.RoomHistoryDate   = ymd;

    // パラメタはセット済みなのでデータ取得のみを行う
    if( selecthotel.compareTo("all") == 0 )
    {
    // 全店取得
        DbAccess db_manage =  new DbAccess();
        query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND hotel.plan <= 2";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
        query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";

        ResultSet DbManageHotel = db_manage.execQuery(query);

        // 管理店舗数分ループ
//        ResultSet DbManageHotel = ownerinfo.getManageHotel(db_manage, ownerinfo.HotelId, ownerinfo.DbUserId);
        ret = DbManageHotel.first();
        while( ret != false )
        {
            hotelid = DbManageHotel.getString("accept_hotelid");

            db =  new DbAccess();

            // ホテル名称の取得
            query = "SELECT name FROM hotel WHERE hotel_id=?";
            result = db.execQuery(query,hotelid);
            if( result != null )
            {
                result.next();
                hotelname = result.getString("name");
            }

            ownerinfo.sendPacket0106(1, hotelid);
            ownerinfo.sendPacket0146(1, hotelid);
%>
<jsp:include page="roomhistory_disp.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= hotelid %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
</jsp:include>
<%
            db.close();

            ret = DbManageHotel.next();
        }

        db_manage.close();
    }
    else
    {
        db =  new DbAccess();

        // ホテル名称の取得
        query = "SELECT name FROM hotel WHERE hotel_id=?";
        result = db.execQuery(query,selecthotel);
        if( result != null )
        {
            result.next();
            hotelname = result.getString("name");
        }
    // 指定店舗のみ取得
        ownerinfo.sendPacket0106(1, selecthotel);
        ownerinfo.sendPacket0146(1, selecthotel);
%>
<jsp:include page="roomhistory_disp.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= selecthotel %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
</jsp:include>
<%
        db.close();

    }
%>

