<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String       selecthotel;
    String       hotelid = "";
    String       hotelname = "";
    int          i  = 0;

    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
%>
<tr>
  <td align="center" valign="middle" nowrap class="size14 tableLN">店舗名</td>
  <td align="center" valign="middle" nowrap class="size14 tableLN">対象</td>
</tr>
<%
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int          imedia_user      = 0;
    int          level            = 0;
    connection  = DBConnection.getConnection();

    String TargetHotelId          = ReplaceString.getParameter(request,"TargetHotelId");
    String SalesDispFlag          = ReplaceString.getParameter(request,"SalesDispFlag");
    if (TargetHotelId != null && SalesDispFlag != null)
    {
        try
        {
            String query = "UPDATE owner_user_hotel SET ";
            if (SalesDispFlag.equals("false"))
            {
                query = query + "sales_disp_flag=0 ";
            }
            else
            {
                query = query + "sales_disp_flag=1 ";
            }
            query = query + " WHERE hotelid=?";
            query = query + " AND userid=?";
            query = query + " AND accept_hotelid=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,loginHotelId);
            prestate.setInt(2,ownerinfo.DbUserId);
            prestate.setString(3,TargetHotelId);
            int retresult   = prestate.executeUpdate();
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
     // imedia_user のチェック
    try
    {
    	final String query = "SELECT * FROM owner_user WHERE hotelid=?"
        					+ " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
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
    	final String query;
        if (imedia_user == 1 && level == 3 && !selecthotel.equals("all_manage"))
        {
            query = "SELECT * FROM hotel WHERE hotel.group_id =?"
	             + " AND hotel.plan <= 2"
	             + " AND hotel.plan >= 1"
	             + " ORDER BY hotel.sort_num,hotel.hotel_id";
        }
        else
        {
            query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?"
	             + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid"
	             + " AND hotel.plan <= 2"
	             + " AND hotel.plan >= 1"
	             + " AND owner_user_hotel.userid = ?"
	             + " ORDER BY hotel.sort_num,hotel.hotel_id";
        }
            prestate    = connection.prepareStatement(query);
            if (imedia_user == 1 && level == 3 && !selecthotel.equals("all_manage"))
            {
                prestate.setString(1,selecthotel);
            }
            else
            {
                prestate.setString(1,ownerinfo.HotelId);
                prestate.setInt(2,ownerinfo.DbUserId);
            }
        result      = prestate.executeQuery();
            while( result.next() )
            {
                hotelid   = result.getString("hotel.hotel_id");
                hotelname = result.getString("hotel.name");
                int sales_disp_flag = 1;
                if (imedia_user != 1 || level != 3 || selecthotel.equals("all_manage"))
                {
                    sales_disp_flag =  result.getInt("owner_user_hotel.sales_disp_flag");
                }
                i++;
%>
<tr height="24px">
    <td align="left" valign="middle"  class="tableL<%if (i%2==0){%>W<%}else{%>G<%}%> size14" nowrap><%= hotelname %></td>
    <td align="center" valign="middle" class="tableR<%if (i%2==0){%>W<%}else{%>G<%}%>"><input onclick="document.form1.TargetHotelId.value='<%=hotelid%>';document.form1.SalesDispFlag.value=this.checked;document.form1.submit();" type="checkbox" name="SalesDispFlag_<%=i%>" id="SalesDispFlag_<%=i%>" value="<%=hotelid%>" <%if(sales_disp_flag==1){%>checked<%}%>></td>
</tr>
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
<tr height="24px">
    <td align="left" valign="middle"  class="size12" colspan=2>「管理店舗売上一覧」の集計店舗をご確認ください。チェックをはずすと集計から除外されます。</td>
</tr>

            <input name="Max"                 type="hidden" value="<%=i%>">
            <input name="TargetHotelId"       type="hidden" value="">
            <input name="SalesDispFlag"       type="hidden" value="">

