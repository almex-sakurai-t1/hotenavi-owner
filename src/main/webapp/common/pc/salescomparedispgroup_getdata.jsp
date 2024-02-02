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
    boolean      getresult = true;

    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
    String param_compare          = "false";

    int compare_start_year  = 0;
    int compare_start_month = 0;
    int compare_start_day   = 0;
    int compare_end_year    = 0;
    int compare_end_month   = 0;
    int compare_end_day     = 0;

    String param_compare_year     = ReplaceString.getParameter(request,"CompareStartYear");
    String param_compare_month    = ReplaceString.getParameter(request,"CompareStartMonth");
    String param_compare_day      = ReplaceString.getParameter(request,"CompareStartDay");
    String param_compare_endyear  = ReplaceString.getParameter(request,"CompareEndYear");
    String param_compare_endmonth = ReplaceString.getParameter(request,"CompareEndMonth");
    String param_compare_endday   = ReplaceString.getParameter(request,"CompareEndDay");
    if (param_compare_year != null)
        compare_start_year  = Integer.parseInt(param_compare_year);
    if (param_compare_month != null)
        compare_start_month = Integer.parseInt(param_compare_month);
    if (param_compare_day  != null)
        compare_start_day   = Integer.parseInt(param_compare_day);
    if (param_compare_endyear != null)
        compare_end_year    = Integer.parseInt(param_compare_endyear);
    if (param_compare_endmonth != null)
        compare_end_month   = Integer.parseInt(param_compare_endmonth);
    if (param_compare_endday != null)
        compare_end_day     = Integer.parseInt(param_compare_endday);
    int compare_start_date = compare_start_year * 10000 + compare_start_month * 100 + compare_start_day;
    int compare_end_date   = compare_end_year * 10000   + compare_end_month * 100   + compare_end_day;


    ownerinfo.SalesGetStartDate       = compare_start_date;
    ownerinfo.SalesGetEndDate         = compare_end_date;
    ownerinfo.SalesDetailGetStartDate = compare_start_date;
    ownerinfo.SalesDetailGetEndDate   = compare_end_date;
    ownerinfo.ManualSalesDetailGetStartDate = compare_start_date;
    ownerinfo.ManualSalesDetailGetEndDate   = compare_end_date;

    int date_count  = 0;
    int date_count_total = 0;
    int store_count = 0;
    int line_count  = 0;
    int sales_total = 0;
    int sales_total_count = 0;
    int room_count = 0;
    int room_count_total = 0;

    //再取得時用
    int[]    t_room_count           = new int[50];
    int[]    t_SalesTotal           = new int[50];
    int[]    t_SalesTotalCount      = new int[50];
    int[]    t_SalesTotalRate       = new int[50];
    int[]    t_SalesTotalPrice      = new int[50];
    int[]    t_SalesRoomPrice       = new int[50];
    int[]    t_SalesRoomTotalPrice  = new int[50];
    int      Max               = 0;
    String   paramMax          = ReplaceString.getParameter(request,"Max");
    if      (paramMax         == null)    paramMax = "0";
    Max      = Integer.parseInt(paramMax);
    String   paramData         = "";
    for( i = 0 ; i <= Max ; i++ )
    {
        paramData =  ReplaceString.getParameter(request,"room_count_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_room_count[i]        = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotal_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotal[i]        = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalCount_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalCount[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalRate_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalRate[i]    = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRoomPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRoomPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRoomTotalPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRoomTotalPrice[i]   = Integer.parseInt(paramData);
    }
%>
<tr>
  <td align="center" valign="middle" nowrap class="size14 tableLN">金　額</td>
  <td align="center" valign="middle" nowrap class="size14 tableLN">組数</td>
  <td align="center" valign="middle" nowrap class="size14 tableLN">回転数</td>
  <td align="center" valign="middle" nowrap class="size14 tableLN">客単価</td>
<%  if (ownerinfo.SalesGetStartDate%100 == 0)
    {
%>
  <td align="center" valign="middle" nowrap class="size14 tableLN">部屋単価</td>
  <td align="center" valign="middle" nowrap class="size14 tableRN">(一日当)</td>
<%
    }
    else
    {
%>
  <td align="center" valign="middle" nowrap class="size14 tableRN">部屋単価</td>
<%
    }
%>
</tr>
<%
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int          imedia_user      = 0;
    int          level            = 0;
    connection  = DBConnection.getConnection();
     // imedia_user のチェック
    try
    {
    	final String query = "SELECT * FROM owner_user WHERE hotelid=?"
    	 + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
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
             + " AND owner_user_hotel.sales_disp_flag = 1"
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
        if  (result != null)
        {
          Object lock = request.getSession();
          synchronized(lock){
            while( result.next() != false)
            {
                hotelid   = result.getString("hotel.hotel_id");
                hotelname = result.getString("hotel.name");
                line_count++;
                i = line_count;

                getresult = true;

                if (t_SalesTotal[i] == 0 && t_SalesTotalCount[i]==0)
                {
                    if (ownerinfo.SalesGetStartDate == ownerinfo.SalesGetEndDate || ownerinfo.SalesGetEndDate == 0)
                    {
                        getresult = ownerinfo.sendPacket0102(1, hotelid,1);
                    }
                    else
                    {
                        getresult = ownerinfo.sendPacket0102(1, hotelid);
                    }
                    if (getresult)
                    {
                        t_SalesTotal[i]          = ownerinfo.SalesTotal;
                        t_SalesTotalCount[i]     = ownerinfo.SalesTotalCount;
                        t_SalesTotalRate[i]      = ownerinfo.SalesTotalRate;
                        t_SalesTotalPrice[i]     = ownerinfo.SalesTotalPrice;
                        t_SalesRoomPrice[i]      = ownerinfo.SalesRoomPrice;
                        t_SalesRoomTotalPrice[i] = ownerinfo.SalesRoomTotalPrice;
                    }
                }
//              if((t_SalesTotal[i]!=0 || t_SalesTotalCount[i]!=0) && result.getInt("split_flag") == 0)
                if(getresult && result.getInt("split_flag") == 0)
                {
                    store_count++;
                }

                room_count = 0;
                if (t_SalesRoomTotalPrice[i] != 0)
                {
                    room_count    = Math.round((float)t_SalesTotal[i] / (float)t_SalesRoomTotalPrice[i]);
                }
                t_room_count[i]   = room_count;

                date_count = 0;
                if (t_SalesRoomPrice[i] != 0)
                {
                    date_count    = Math.round((float)t_SalesRoomTotalPrice[i] / (float)t_SalesRoomPrice[i]);
                }
                if(result.getInt("split_flag") == 0)
                {
                    sales_total       = sales_total       + t_SalesTotal[i];
                    sales_total_count = sales_total_count + t_SalesTotalCount[i];
                    room_count_total  = room_count_total + room_count;
                    date_count_total  = date_count_total + date_count*room_count;
                }
%>
<tr height="24px">
<%
//                if (t_SalesTotal[i] ==0 && t_SalesTotalCount[i] ==0)//取得できなかった
                if (!getresult)//取得できなかった
                {
%>
    <td  colspan="<%if (ownerinfo.SalesGetStartDate%100 == 0){%>6<%}else{%>5<%}%>" align="middle" valign="middle" class="tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>" nowrap>　取得できませんでした。<input type="button" value="再取得" onclick="document.form1.submit();"></td>
<%
                }
                else
                {
%>    <td align="right" valign="middle" class="tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><input type="hidden" name="SalesTotal_<%=param_compare%>_<%=i%>" id="SalesTotal_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotal[i]%>"><a href="#" class="size14" onclick="document.form1.HotelIdfromGroup.value='<%=hotelid%>';document.form1.action='salesdisp_select.jsp';document.form1.submit();"><%= Kanma.get(t_SalesTotal[i]) %></a></td>
    <td align="right" valign="middle" class="tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><input type="hidden" name="SalesTotalCount_<%=param_compare%>_<%=i%>" id="SalesTotalCount_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalCount[i]%>"><a href="#" class="size14" onclick="document.form1.HotelIdfromGroup.value='<%=hotelid%>';document.form1.action='salesdisp_select.jsp';document.form1.submit();"><%= Kanma.get(t_SalesTotalCount[i]) %></a></td>
    <td align="right" valign="middle" class="tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><input type="hidden" name="SalesTotalRate_<%=param_compare%>_<%=i%>" id="SalesTotalRate_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalRate[i]%>"><a href="#" class="size14" onclick="document.form1.HotelIdfromGroup.value='<%=hotelid%>';document.form1.action='salesdisp_select.jsp';document.form1.submit();"><%= (float)t_SalesTotalRate[i] / (float)100 %><% if((float)t_SalesTotalRate[i]%10==0){%>0<%}%></a></td>
    <td align="right" valign="middle" class="tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><input type="hidden" name="SalesTotalPrice_<%=param_compare%>_<%=i%>" id="SalesTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalPrice[i]%>"><a href="#" class="size14" onclick="document.form1.HotelIdfromGroup.value='<%=hotelid%>';document.form1.action='salesdisp_select.jsp';document.form1.submit();"><%= Kanma.get(t_SalesTotalPrice[i]) %></a></td>
<%
                    if (ownerinfo.SalesGetStartDate%100 == 0)
                    {
%>
    <td align="right" valign="middle" class="tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><input type="hidden" name="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>" id="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesRoomTotalPrice[i]%>"><a href="#" class="size14" onclick="document.form1.HotelIdfromGroup.value='<%=hotelid%>';document.form1.action='salesdisp_select.jsp';document.form1.submit();"><%= Kanma.get(t_SalesRoomTotalPrice[i]) %></a></td>
    <td align="right" valign="middle" class="tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><input type="hidden" name="SalesRoomPrice_<%=param_compare%>_<%=i%>" id="SalesRoomPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesRoomPrice[i]%>"><a href="#" class="size14" onclick="document.form1.HotelIdfromGroup.value='<%=hotelid%>';document.form1.action='salesdisp_select.jsp';document.form1.submit();"><%= Kanma.get(t_SalesRoomPrice[i]) %></a></td>
<%
                    }
                    else
                    {
%>
    <td align="right" valign="middle" class="tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><input type="hidden" name="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>" id="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesRoomTotalPrice[i]%>"><input type="hidden" name="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>" id="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesRoomTotalPrice[i]%>"><a href="#" class="size14" onclick="document.form1.HotelIdfromGroup.value='<%=hotelid%>';document.form1.action='salesdisp_select.jsp';document.form1.submit();"><%= Kanma.get(t_SalesRoomTotalPrice[i]) %></a></td>
<%
                    }
                }
%>
</tr>
<%
            }
          }
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
    int total_rate = 0;
    if (date_count_total != 0)
    {
       total_rate = Math.round((float)sales_total_count*100 / (float)date_count_total);
    }
%>
<tr height="24px">
    <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(sales_total) %></td>
    <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(sales_total_count) %></td>
    <td align="right" valign="middle" class="size14 tableLB" nowrap><%= (float)total_rate / (float)100 %><% if((float)total_rate%10==0){%>0<%}%></td>
    <td align="right" valign="middle" class="size14 tableLB" nowrap><% if(sales_total_count!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)sales_total_count))%><%}%></td>
<%
    if (ownerinfo.SalesGetStartDate%100 == 0)
    {
%>
    <td align="right" valign="middle" class="size14 tableLB" nowrap><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)room_count_total))%><%}%></td>
    <td align="right" valign="middle" class="size14 tableRB" nowrap><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)date_count_total))%><%}%></td>
<%
    }
    else
    {
%>
    <td align="right" valign="middle" class="size14 tableRB" nowrap><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)room_count_total))%><%}%></td>
<%
    }
%>
</tr>
<tr height="30px">
    <td align="left" valign="middle"  class="size14">
        <input name="CompareStartYear"  type="hidden" value="<%=param_compare_year%>">
        <input name="CompareStartMonth" type="hidden" value="<%=param_compare_month%>">
        <input name="CompareStartDay"   type="hidden" value="<%=param_compare_day%>">
<%
    if( param_compare_endyear != null && param_compare_endmonth != null && param_compare_endday != null )
    {
%>
        <input name="CompareEndYear"    type="hidden" value="<%=param_compare_endyear%>">
        <input name="CompareEndMonth"   type="hidden" value="<%=param_compare_endmonth%>">
        <input name="CompareEndDay"     type="hidden" value="<%=param_compare_endday%>">
<%
    }
%>
        &nbsp;
    </td>
</tr>

