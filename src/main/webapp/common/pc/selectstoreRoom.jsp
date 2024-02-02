<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %><%@ page import="com.hotenavi2.common.*" %>
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

    DbAccess db_security =  new DbAccess();
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
    db_security.close();

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
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }


    String paramTarget     = ReplaceString.getParameter(request,"Target");

    selecthotel = (String)session.getAttribute("SelectHotel");

    if( selecthotel == null )
    {
        selecthotel = "all";
    }
    if( selecthotel.compareTo("all") == 0 )
    {
        if (imedia_user == 1 && level == 3)
        {
  //          selecthotel = "";
        }
    }

    String paramStoreCount = ReplaceString.getParameter(request,"StoreCount");
    storecount             = Integer.parseInt(paramStoreCount);

    param_store            = ReplaceString.getParameter(request,"Store");

    if( param_store != null )
    {
        if( !CheckString.hotenaviIdCheck(param_store) )
        {
            param_store = "";
            response.sendError(400);
            return;
        }
        selecthotel = param_store;
    }
    query_sub = query_sub + " AND hotel.plan >= 1";
    query_sub = query_sub + " AND hotel.plan <= 2";

    // セッション属性に選択ホテルをセットする
    session.setAttribute("SelectHotel", selecthotel);
    if  (storecount == 1)
    {  // 1件だけだった場合
        try
        {
            query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
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
    if (imedia_user == 1 && level == 3)
    {
%>
<td width="80" rowspan="2" align="left" valign="middle" nowrap bgcolor="#000000">
  <div class="white12" align="center">店舗選択</div>
</td>
<form action=room_select.jsp method="post" name="selectstore" <%if (paramTarget!=null){%>target="<%=paramTarget%>"<%}%>>
  <td width="100" rowspan="2" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <input name="Store" size="14" maxlength="10" onChange="document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';" value="<%= selecthotel %>">
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
<td width="80" rowspan="2" align="left" valign="middle" nowrap bgcolor="#000000">
  <div class="white12" align="center">店舗選択</div>
</td>
<form action=room_select.jsp method="post" name="selectstore" <%if (paramTarget!=null){%>target="<%=paramTarget%>"<%}%>>
  <td width="100" rowspan="2" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <select name="Store" onChange="document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';">
<%
            if( selecthotel.compareTo("") == 0)
            {
%>
		<option value="" selected>選択してください</option>
<%
            }
            if( selecthotel.compareTo("all_manage") == 0)
            {
%>
		<option value="all_manage" selected>管理店舗状況一覧</option>
<%
            }
            else
            {
%>
		<option value="all_manage">管理店舗状況一覧</option>
<%
            }
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
%>

