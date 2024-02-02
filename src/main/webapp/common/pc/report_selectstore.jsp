<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int          count = 0;
    boolean      ret;
    String       hotelname = "";
    String       storecount;
    String       selecthotel;
    String       param_store;
   	String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    param_store = ReplaceString.getParameter(request,"Store");
    if( param_store != null )
    {
        if( !CheckString.hotenaviIdCheck(param_store) )
        {
            param_store = "";
%>
            <script type="text/javascript">
            <!--
            var dd = new Date();
            setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
            //-->
            </SCRIPT>
<%
        }
        selecthotel = param_store;
    }

    // セッション属性に選択ホテルをセットする
    session.setAttribute("SelectHotel", selecthotel);
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    DbAccess db_security =  new DbAccess();
    // セキュリティ情報の取得
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db_security, loginhotel, ownerinfo.DbUserId);

    // セキュリティチェック
    if( DbUserSecurity == null )
    {
%>
        <jsp:forward page="/common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
%>
        <jsp:forward page="/common/pc/error/notlogin.html" />
<%
    }
    try
    {
	    query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
	    query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
	    query = query + " AND hotel.plan <= 2";
	    query = query + " AND hotel.plan >= 1";
	    query = query + " AND owner_user_hotel.userid = ?";
	    prestate    = connection.prepareStatement(query);
	    prestate.setString(1,  ownerinfo.HotelId);
	    prestate.setInt(2, ownerinfo.DbUserId);
	    result      = prestate.executeQuery();
	    if( result.next() )
	    {
	       count = result.getInt(1);
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

if  (count == 1)
{
	try
	{
	  	query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
	    query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
	    query = query + " AND hotel.plan <= 2";
	    query = query + " AND hotel.plan >= 1";
	    query = query + " AND owner_user_hotel.userid =? ";
	    prestate    = connection.prepareStatement(query);
	    prestate.setString(1,  ownerinfo.HotelId);
	    prestate.setInt(2, ownerinfo.DbUserId);
	    result      = prestate.executeQuery();
		if( result.next() )
		{
		   selecthotel = result.getString("hotel.hotel_id");
		   session.setAttribute("SelectHotel", selecthotel);
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
}

try
{
    // セッション済みホテルが売上情報閲覧可能かどうかをチェックし、NGの場合はセッション内容を変更する。
  	query = "SELECT * FROM hotel WHERE hotel_id =?";
    query = query + " AND plan <= 2";
    query = query + " AND plan >= 1";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,  selecthotel);
    result      = prestate.executeQuery();
    if( result.next() )
    {
       if (count >= 2)
       {
       session.setAttribute("SelectHotel", "all");
       }
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
%>

<%
    // 2店舗以上の場合表示する
    if( count > 1 )
    {
%>

<td width="70" align="center" nowrap bgcolor="#000000">
  <div class="white12" align="center">&nbsp;店舗選択&nbsp;</div>
</td>
<form action="report_main.jsp" method="post" name="selectstore" target="mainFrame">
  <td width="100%" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <select name="Store" onChange="document.selectstore.elements['submitstore'].click()">

<%
        if( selecthotel.compareTo("") == 0 )
        {
%>
      <option value="" selected>選択してください</option>
<%
        }
%>
<%
    if( DbUserSecurity.getInt("sec_level10") == 1)
    {
        if( selecthotel.compareTo("all") == 0 )
        {
%>
      <option value="all" selected>多店舗</option>
<%
        }
        else
        {
%>
      <option value="all">多店舗</option>
<%
        }
    }
	try
	{
	      	query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ?";
	        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
	        query = query + " AND hotel.plan <= 2";
	        query = query + " AND hotel.plan >= 1";
	        query = query + " AND owner_user_hotel.userid = ?";
	        query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
	        prestate    = connection.prepareStatement(query);
	        prestate.setString(1, ownerinfo.HotelId);
	        prestate.setInt(2, ownerinfo.DbUserId);
	        result = prestate.executeQuery();
	        // 管理店舗数分ループ
	        while( result.next() )
	        {
	            if( selecthotel.compareTo(result.getString("accept_hotelid")) == 0 )
	            {
	%>
	      <option value="<%= result.getString("accept_hotelid") %>" selected><%= hotelname %></option>
	<%
	            }
	            else
	            {
	%>
	      <option value="<%= result.getString("accept_hotelid") %>"><%= hotelname %></option>
	<%
	            }
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
%>

    </select>
    <input type="submit" value="店舗選択" name="submitstore" onClick="return datacheck()">
  </td>
</form>

<%
    }
    else
    {
%>
<script type="text/javascript">
<!--
    setTimeout("window.open('report_main.jsp','mainFrame')",0000);
//-->
</script>
<%
    }
    DBConnection.releaseResources(connection);
%>

