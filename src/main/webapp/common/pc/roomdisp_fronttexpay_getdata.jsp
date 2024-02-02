<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String       selecthotel;
    String       hotelid;
    String       query;
    String       hotelname = "";
    boolean      ret;
    DbAccess     db;
    ResultSet    result;

    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    // 端末番号の初期化（全端末取得）
    ownerinfo.FrontTexTermCodeIn = 999;

    // パラメタはセット済みなのでデータ取得のみを行う
    if( selecthotel.compareTo("all") == 0 )
    {
        int store_count = 0;

        String param_cnt = ReplaceString.getParameter(request,"cnt");
        if( param_cnt == null )
        {
            param_cnt = "0";
        }
        if(!CheckString.numCheck(param_cnt))
		{
			param_cnt="0";
%>
	        <script type="text/javascript">
	        <!--
	        var dd = new Date();
	        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
	        //-->
	        </script>
<%
		}
        int cnt = Integer.parseInt(param_cnt);

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
        ret = DbManageHotel.relative(cnt);
        while( ret != false )
        {
            hotelid = DbManageHotel.getString("accept_hotelid");
            hotelname = DbManageHotel.getString("name");

            ownerinfo.sendPacket0110(1, hotelid);
            ownerinfo.sendPacket0114(1, hotelid);
            ownerinfo.sendPacket0156(1, hotelid);
%>
<jsp:include page="roomdisp_fronttexpay_disp.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= hotelid %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
</jsp:include>
<%
            ret = DbManageHotel.next();

            store_count++;
            if( store_count >= 3 )
            {
                break;
            }
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
        ownerinfo.sendPacket0110(1, selecthotel);
        ownerinfo.sendPacket0114(1, selecthotel);
        ownerinfo.sendPacket0156(1, selecthotel);
%>
<jsp:include page="roomdisp_fronttexpay_disp.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= selecthotel %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
</jsp:include>
<%
        db.close();
    }
%>


