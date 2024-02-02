<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String       selecthotel;
    String       hotelid;
    String       hotelname = "";
    boolean      ret;
    int          hostkind = 0;
    int          newsales = 0;

    int start_date = Integer.parseInt(new DateEdit().getDate(2));
	boolean dateError = false;
    String startYear  = ReplaceString.getParameter(request,"StartYear");
	if( startYear != null && !CheckString.numCheck(startYear))
	{
		dateError = true;
	}
    String startMonth = ReplaceString.getParameter(request,"StartMonth");
	if( startMonth != null && !CheckString.numCheck(startMonth))
	{
		dateError = true;
	}
    String startDay   = ReplaceString.getParameter(request,"StartDay");
	if( startDay != null && !CheckString.numCheck(startDay))
	{
		dateError = true;
	}
	if(dateError)
	{
        startYear="0";
        startMonth="0";
        startDay="0";
%>
		<script type="text/javascript">
		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
    if( startYear != null && startMonth != null && startDay != null )
    {
        start_date = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;


    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
    ownerinfo.CalGetDate = start_year * 100 + start_month;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    // パラメタはセット済みなのでデータ取得のみを行う
    try
	{
    	final String query;
	    if( selecthotel.compareTo("all") == 0 || selecthotel.compareTo("all_manage") == 0)
	    {
	        int store_count = 0;

	        String param_cnt = ReplaceString.getParameter(request,"cnt");
	        if( param_cnt == null )
	        {
	            param_cnt = "0";
	        }
	        if(!CheckString.numCheck(param_cnt))
			{
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

	        query = "SELECT * FROM hotel"
					+ " INNER JOIN owner_user_hotel "
					+ " ON hotel.hotel_id = owner_user_hotel.accept_hotelid"
					+ " AND owner_user_hotel.userid = ?"
					+ " AND owner_user_hotel.hotelid = ?"
					+ " WHERE hotel.plan IN (1,2)"
					+ " ORDER BY hotel.sort_num,hotel.hotel_id";
	        prestate    = connection.prepareStatement(query);
	        prestate.setInt(1, ownerinfo.DbUserId);
	        prestate.setString(2, ownerinfo.HotelId);
	         result      = prestate.executeQuery();
	           while( result.next() )
	           {
	            hotelid = result.getString("owner_user_hotel.accept_hotelid");
	            hotelname = result.getString("hotel.name");
	            hostkind  = result.getInt("hotel.host_kind");
	            newsales  = result.getInt("hotel.host_detail");
	            ownerinfo.sendPacket0142(1, hotelid);
%>
<jsp:include page="salesdisp_calendardisp.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= hotelid %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
</jsp:include>
<%
            store_count++;
            if( store_count >= 3 )
            {
                break;
            }
            }
	    }
	    else
	    {
	    	// ホテル名称の取得
	        query = "SELECT * FROM hotel WHERE hotel_id=?";
	        prestate    = connection.prepareStatement(query);
	        prestate.setString(1, selecthotel);
	        result      = prestate.executeQuery();
           	if( result.next() )
           	{
	            hotelname = result.getString("hotel.name");
	            hostkind  = result.getInt("hotel.host_kind");
	            newsales  = result.getInt("hotel.host_detail");
	     	}
	    // 指定店舗のみ取得
	        ownerinfo.sendPacket0142(1, selecthotel);
%>
<jsp:include page="salesdisp_calendardisp.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= selecthotel %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
</jsp:include>
<%
    	}
	}
	catch( Exception e )
	{
	    Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
	}
	finally
	{
	    DBConnection.releaseResources(result,prestate,connection);
	}
%>
