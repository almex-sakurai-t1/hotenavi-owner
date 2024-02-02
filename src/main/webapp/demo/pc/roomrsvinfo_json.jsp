<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="jp.happyhotel.common.*" %>

<%
	String paramHotenaviId = request.getParameter("hotenaviId");
	String paramSeq = request.getParameter("seq");
    if     (paramSeq.compareTo("") == 0) paramSeq = "0";
    if     (!CheckString.numCheck( paramSeq ))  paramSeq = "0";
    int    seq = Integer.parseInt(paramSeq);
	
    if (paramHotenaviId == null || paramSeq == null)
    {
%>
				<jsp:forward page="error.jsp" />
<%
    }
%>
{
    "RsvExist" :
       {
<%
    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
%>
<%
	//ホテルの部屋の予約の有無を調べる
    query = "SELECT COUNT(*) AS rsvCount ";
    query +=" FROM newRsvDB.hh_rsv_room_remainder  room_remainder";
    query +=" INNER JOIN hh_hotel_basic hotel_basic";
    query +="   ON hotel_basic.id = room_remainder.id";
    query +=" WHERE hotel_basic.hotenavi_id = ? ";
    query +=" AND cal_date >= ?";
    query +=" AND seq = ? ";
    query +=" AND (";
    query +=" ( stay_reserve_no <> '' ) OR ";
    query +=" ( stay_reserve_temp_no <> '' AND (stay_reserve_limit_day*1000000 + stay_reserve_limit_time) > ? * 1000000 + ? AND (stay_reserve_no is null OR stay_reserve_no = '' )) OR";
	query +=" ( rest_reserve_no <> '' ) OR ";
    query +=" ( rest_reserve_temp_no <> '' AND (rest_reserve_limit_day*1000000 + rest_reserve_limit_time) > ? * 1000000 + ? AND (rest_reserve_no is null OR rest_reserve_no = '' )) ";
    query +=" )"; 

	prestate = connection.prepareStatement(query);
	int collectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
	int collectTime = Integer.parseInt( DateEdit.getTime( 1 ) );
	prestate.setString( 1, paramHotenaviId );
	prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ));
	prestate.setInt( 3, seq );
	prestate.setInt( 4, collectDate );
	prestate.setInt( 5, collectTime );
	prestate.setInt( 6, collectDate );
	prestate.setInt( 7, collectTime );
	result   = prestate.executeQuery();
	 
	if ( result != null )
	{
		if ( result.next() && result.getInt( "rsvCount" ) > 0 )
		{
%>
			"Exist" : true
<%
		}
		else
		{
%>
			"Exist" : false
<%			
		}
	}
	else
	{
%>
			"Exist" : true
<%			
	}
	DBConnection.releaseResources(result,prestate,connection);
%>
     }
}





