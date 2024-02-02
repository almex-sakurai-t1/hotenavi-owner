<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/owner_ini.jsp" %>

<%

    int          count = 0;
    boolean      ret;
    String       query     = "";
    String       query_sub = "";
    String       hotelname = "";
    int          storecount = 0;
    String       selecthotel;
    String       param_store;
    int          imedia_user        = 0;
    int          level              = 0;
    String       loginId   = "";
    boolean      SelectExist = true;

    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    DbAccess db_security = new DbAccess();
    // セキュリティ情報の取得
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db_security, loginHotelId, ownerinfo.DbUserId);

    // セキュリティチェック
    if( DbUserSecurity == null )
    {
    db_security.close();
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
    db_security.close();
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
 
    connection  = DBConnection.getConnection();
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
            loginId     = result.getString("loginid");
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


    String paramURL        = request.getParameter("URL");
    String paramTarget     = request.getParameter("Target");

    selecthotel = (String)session.getAttribute("SelectHotel");

    if( selecthotel == null )
    {
        selecthotel = "all";
    }
    if( selecthotel.compareTo("all") == 0 )
    {
        if (imedia_user == 1 && level == 3 && paramURL.compareTo("hpedit_select") != 0)
        {
            selecthotel = "";
        }
    }
    if( selecthotel.compareTo("") == 0 )
    {
        if (imedia_user == 1 && level == 3 && paramURL.equals("hpedit_select"))
        {
            selecthotel = "all";
        }
    }
    

    String paramStoreCount = request.getParameter("StoreCount");
    storecount             = Integer.parseInt(paramStoreCount);

    param_store            = request.getParameter("Store");
    if( param_store != null )
    {
        selecthotel = param_store;
    }
    if(CheckString.isValidParameter(selecthotel) && !CheckString.numAlphaCheck(selecthotel))
    {
        response.sendError(400);
        return;
    }
    if (paramURL.compareTo("hpedit_select") == 0 || paramURL.compareTo("magazine_select") == 0|| paramURL.compareTo("hpreport_select") == 0)
    {
        query_sub = query_sub + " AND hotel.plan <= 4";
        query_sub = query_sub + " AND hotel.plan >= 1";
        query_sub = query_sub + " AND hotel.plan != 2";
    }
    else if (paramURL.compareTo("room_select") == 0 || paramURL.compareTo("sales_select") == 0 )
    {
        query_sub = query_sub + " AND hotel.plan >= 1";
        query_sub = query_sub + " AND hotel.plan <= 2";
    }
    else if (paramURL.compareTo("report_main") == 0 || paramURL.compareTo("master_select") == 0)
    {
        query_sub = query_sub + " AND hotel.plan >= 1";
        query_sub = query_sub + " AND hotel.plan <= 2";
        query_sub = query_sub + " AND hotel.split_flag =0";
    }

    if (paramURL.compareTo("sales_select") == 0)
    {
        if( selecthotel.compareTo("") != 0 )
        // 選択ﾎﾃﾙの計上日取得
        {
        ownerinfo.sendPacket0100(1,selecthotel);
        }
    }

    // セッション属性に選択ホテルをセットする
    session.setAttribute("SelectHotel", selecthotel);

    if (imedia_user == 1 && level == 3 && paramURL.equals("magazine_select") && !selecthotel.equals(""))
    {
        storecount= 1;
    }

    if  (storecount == 1)
    {  // 1件だけだった場合
        try
        {
            if( paramURL.compareTo("magazine_select") == 0)
            {
                query = "SELECT * FROM hotel,mag_hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
                query = query + " AND hotel.hotel_id = mag_hotel.hotel_id";
                query = query + " AND mag_hotel.mag_address <> ''";
            }
            else
            {
                query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
            }
            query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
            query = query + query_sub;
            prestate    = connection.prepareStatement(query);
            result      = prestate.executeQuery();
            if  (result != null)
            {
                if( result.next() != false )
                {
                    selecthotel = result.getString("hotel.hotel_id");
                    session.setAttribute("SelectHotel", selecthotel);
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
    }
    // セッション済みホテルが閲覧可能かどうかをチェックし、NGの場合はセッション内容を変更する。
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id =?";
        query = query + query_sub;
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if  (result != null)
        {
            if(!result.next())
            {
                if (storecount > 1)
                {
                    session.setAttribute("SelectHotel", "all");
                }
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
%><%@ include file="../csrf/csrf.jsp" %><%
    if (imedia_user == 1 && level == 3)
    {
%>
<td width="80" <%if (paramURL.equals("sales_select")){%>rowspan="5"<%}else if (!paramURL.equals("bbs_select")){%>rowspan="2"<%}%> align="left" valign="middle" nowrap bgcolor="#000000">
  <div class="white12" align="center">店舗選択</div>
</td>
<form action="<%= paramURL %>.jsp" method="post" name="selectstore" <%if (paramTarget!=null){%>target="<%=paramTarget%>"<%}%>>
  <td <%if (paramURL.compareTo("hpedit_select") == 0 || paramURL.compareTo("room_select") == 0|| paramURL.compareTo("sales_select") == 0){%> width="100"<%}else if(paramURL.compareTo("report_main") == 0){%>width="100%"<%}else{%>width="25%"<%}%> <%if (paramURL.equals("sales_select")){%>rowspan="5"<%}else if (!paramURL.equals("bbs_select")){%>rowspan="2"<%}%> align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <input name="Store" size="14" maxlength="10" onChange="document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';" value="<%= selecthotel %>">
    <input type='hidden' name='csrf' value='<%=token%>'>
    <input type="submit" value="店舗選択" name="submitstore" onclick="top.Main.mainFrame.location.href='page.html';">
  </td> 
</form>
<%
    }
    else
    {
    // 2店舗以上の場合表示する
        if( storecount > 1 )
        {
%>
<td width="80" <%if (paramURL.compareTo("sales_select") == 0){%>rowspan="5"<%}else{%>rowspan="2"<%}%> align="left" valign="middle" nowrap bgcolor="#000000">
  <div class="white12" align="center">店舗選択</div>
</td>
<form action="<%= paramURL %>.jsp" method="post" name="selectstore" <%if (paramTarget!=null){%>target="<%=paramTarget%>"<%}%>>
    <input type='hidden' name='csrf' value='<%=token%>'>
    <td <%if (paramURL.compareTo("hpedit_select") == 0 || paramURL.compareTo("room_select") == 0 || paramURL.compareTo("sales_select") == 0 ){%> width="100"<%}else if(paramURL.compareTo("report_main") == 0){%>width="100%"<%}else{%>width="25%"<%}%> <%if ( paramURL.compareTo("sales_select") == 0){%>rowspan="5"<%}else{%>rowspan="2"<%}%> align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <select name="Store" onChange="document.selectstore.elements['submitstore'].click();<%if (paramURL.compareTo("report_main") != 0){%>top.Main.mainFrame.location.href='page.html';<%}%>">
<%
            if( selecthotel.compareTo("") == 0)
            {
%>
		<option value="" selected>選択してください</option>
<%
            }
          if (paramURL.compareTo("magazine_select") == 0)
            {

                if( selecthotel.compareTo("all") == 0 )
                {
%>
		<option value="all" selected>全店共通</option>
<%
                }
                else
                {
%>
		<option value="all">全店共通</option>
<%
                }
            }
          else if ( paramURL.compareTo("room_select") == 0 ||  paramURL.compareTo("sales_select") == 0 || (paramURL.compareTo("report_main") == 0 && DbUserSecurity.getInt("sec_level10") == 1))
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
                query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
                query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
                query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
                query = query + query_sub;
                query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
                prestate    = connection.prepareStatement(query);
                result      = prestate.executeQuery();
                if  (result != null)
                {
                    while( result.next() != false )
                    {
                        
                        if( selecthotel.compareTo(result.getString("owner_user_hotel.accept_hotelid")) == 0 )
                        {
%>
		<option value="<%= result.getString("owner_user_hotel.accept_hotelid") %>" selected><%= result.getString("hotel.name") %></option>
<%
                        }
                        else
                        {
%>
		<option value="<%= result.getString("owner_user_hotel.accept_hotelid") %>"><%=result.getString("hotel.name") %></option>
<%
                        }
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
    </select>
    <input type="submit" value="店舗選択" name="submitstore" onclick="top.Main.mainFrame.location.href='page.html';">
  </td> 
</form>

<%
        }
    }
    DBConnection.releaseResources(connection);
    db_security.close();
%>

