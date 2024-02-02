<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<style>
.lineDetail:hover{
  text-decoration:underline !important;
  cursor:pointer;
}
</style>
<script type="text/javascript" src="../../common/pc/scripts/prototype.js"></script>
<%
    String       selecthotel;
    String       hotelid = "";
    String       hotelname = "";
    int          i  = 0;
    boolean      getresult = true;

    String paramKind = ReplaceString.getParameter(request,"Kind");
    if (paramKind == null)
    {
        paramKind = "DATE";
    }
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("demo"))
    {
        DemoMode = true;
    }
    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;

    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
    String param_compare          = ReplaceString.getParameter(request,"Compare");
    if (param_compare == null)
    {
        param_compare = "false";
    }
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
    String paramDay   = "0";
    if  (startDay != null)
    {
        paramDay = startDay;
        if(startDay.equals("0")) startDay = "1";
    }
    String endYear  = ReplaceString.getParameter(request,"EndYear");
	if( endYear != null && !CheckString.numCheck(endYear))
	{
		dateError = true;
	}
    String endMonth = ReplaceString.getParameter(request,"EndMonth");
	if( endMonth != null && !CheckString.numCheck(endMonth))
	{
		dateError = true;
	}
    String endDay   = ReplaceString.getParameter(request,"EndDay");
	if( endDay != null && !CheckString.numCheck(endDay))
	{
		dateError = true;
	}
	if(dateError)
	{
        startYear="0";
        startMonth="0";
        startDay="0";
        endYear="0";
        endMonth="0";
        endDay="0";
%>
		<script type="text/javascript">
		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
    String paramGroup = ReplaceString.getParameter(request,"Group");
    if (paramGroup!=null)
    {
        if( !CheckString.hotenaviIdCheck(paramGroup) )
        {
            paramGroup = "";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
        }
    }

    if( startYear != null &&startMonth != null && startDay != null && paramGroup != null)
    {
       ownerinfo.SalesGetStartDate = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    if( endYear != null &&endMonth != null && endDay != null  && paramGroup != null)
    {
       ownerinfo.SalesGetEndDate = (Integer.valueOf(endYear).intValue() * 10000) + (Integer.valueOf(endMonth).intValue() * 100) + Integer.valueOf(endDay).intValue();
    }


    //再取得時用
    int[]    t_room_count           = new int[50];
    int[]    t_SalesTotal           = new int[50];
    int[]    t_SalesTotalCount      = new int[50];
    int[]    t_SalesTotalRate       = new int[50];
    int[]    t_SalesTotalPrice      = new int[50];
    int[]    t_SalesRoomPrice       = new int[50];
    int[]    t_SalesRoomTotalPrice  = new int[50];
    int[]    t_SalesRestTotal       = new int[50];
    int[]    t_SalesRestCount      = new int[50];
    int[]    t_SalesRestRate       = new int[50];
    int[]    t_SalesRestPrice = new int[50];
    int[]    t_SalesRestRoomPrice  = new int[50];
    int[]    t_SalesRestRoomTotalPrice  = new int[50];
    int[]    t_SalesStayTotal      = new int[50];
    int[]    t_SalesStayCount      = new int[50];
    int[]    t_SalesStayRate       = new int[50];
    int[]    t_SalesStayPrice  = new int[50];
    int[]    t_SalesStayRoomPrice  = new int[50];
    int[]    t_SalesStayRoomTotalPrice  = new int[50];
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
        paramData =  ReplaceString.getParameter(request,"SalesTotalPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalCount_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalCount[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalRate_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalRate[i]    = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRoomPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRoomPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRoomTotalPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRoomTotalPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"room_count_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_room_count[i]        = Integer.parseInt(paramData);

        paramData =  ReplaceString.getParameter(request,"SalesRestTotal_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRestTotal[i]        = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRestPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRestPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRestCount_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRestCount[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRestRate_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRestRate[i]    = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRestRoomPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRestRoomPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRestRoomTotalPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRestRoomTotalPrice[i]   = Integer.parseInt(paramData);

        paramData =  ReplaceString.getParameter(request,"SalesStayTotal_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesStayTotal[i]        = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesStayPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesStayPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesStayCount_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesStayCount[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesStayRate_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesStayRate[i]    = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesStayRoomPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesStayRoomPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesStayRoomTotalPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesStayRoomTotalPrice[i]   = Integer.parseInt(paramData);
    }
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int          imedia_user      = 0;
    int          level            = 0;
    int          sales_cache_flag = 0;
    connection  = DBConnection.getConnection();
     // imedia_user のチェック
    try
    {
        final String query = "SELECT owner_user.imedia_user,owner_user.level,owner_user_security.sales_cache_flag"
                           + " FROM owner_user"
                           + " INNER JOIN owner_user_security ON owner_user.hotelid = owner_user_security.hotelid AND owner_user.userid = owner_user_security.userid"
                           + " WHERE owner_user.hotelid = ? AND owner_user.userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
            sales_cache_flag = result.getInt("sales_cache_flag");
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


    int date_count  = 0;
    int date_count_total = 0;
    int store_count = 0;
    int line_count  = 0;
    int sales_total = 0;
    int sales_total_count = 0;
    int room_count = 0;
    int room_count_total = 0;
    int sales_rest_total = 0;
    int sales_rest_total_count = 0;
    int sales_stay_total = 0;
    int sales_stay_total_count = 0;
    boolean SplitFlag = false;
    if (param_compare.equals("false"))
    {
%><div style="text-align:right;margin: -25px 50px 5px 0;" class="size14">
<input id="sales_cache"  type="checkbox" onclick="salesCacheChange(this);" value="1" onMouseover="document.getElementById('arrow_box').style.display='block';" onMouseout="document.getElementById('arrow_box').style.display='none';" <%if (sales_cache_flag == 1){%> checked <%}%>>キャッシュ
<p id="arrow_box">チェックをはずすとホテルに接続して売上情報を取得します</p>
<select onchange="titleChange(this);"><option selected value="Total">売上合計<option value="Rest">休憩のみ<option value="Stay">宿泊のみ</select>
</div>
<style>
#arrow_box {
  display: none;
  position: absolute;
  padding: 16px;
  -webkit-border-radius: 8px;
  -moz-border-radius: 8px;  
  border-radius: 8px;
  background: #333;
  color: #fff;
  left: 267px;
  top: 50px;
  opacity: 80%;
}

#arrow_box:after {
  position: absolute;
  bottom: 100%;
  left: 50%;
  width: 0;
  height: 0;
  margin-left: -10px;
  border: solid transparent;
  border-color: rgba(51, 51, 51, 0);
  border-bottom-color: #333;
  border-width: 10px;
  pointer-events: none;
  content: " ";
}
</style>
<%
    }
%>
<script type="text/javascript">
function titleChange(obj){
  for (var i = 0;i < document.getElementsByClassName("Total").length;i++)
  {
     document.getElementsByClassName("Total")[i].style.display='none';
     document.getElementsByClassName("Rest")[i].style.display='none';
     document.getElementsByClassName("Stay")[i].style.display='none';
     document.getElementsByClassName(obj.value)[i].style.display='';
  }
}
</script>
<tr>
  <th align="center" valign="middle" nowrap class="size14 tableLN" label="num">店舗名</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN" style="display:none"></th>
  <th align="center" valign="middle" nowrap class="size14 tableLN" label="num">室数</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Total" label="num">金額</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Total" label="num">組数</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Total" label="num">回転数</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Total" label="num">客単価</th>
<%  if (ownerinfo.SalesGetStartDate%100 == 0)
    {
%>
  <th align="center" valign="middle" nowrap class="size14 tableLN Total" label="num">部屋単価</th>
  <th align="center" valign="middle" nowrap class="size14 tableRN Total" label="num">(一日当)</th>
<%
    }
    else
    {
%>
  <th align="center" valign="middle" nowrap class="size14 tableRN Total" label="num">部屋単価</th>
<%
    }
%>
  <th align="center" valign="middle" nowrap class="size14 tableLN Rest" label="num" style="display:none">金額</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Rest" label="num" style="display:none">組数</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Rest" label="num" style="display:none">回転数</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Rest" label="num" style="display:none">客単価</th>
<%  if (ownerinfo.SalesGetStartDate%100 == 0)
    {
%>
  <th align="center" valign="middle" nowrap class="size14 tableLN Rest" label="num" style="display:none">部屋単価</th>
  <th align="center" valign="middle" nowrap class="size14 tableRN Rest" label="num" style="display:none">(一日当)</th>
<%
    }
    else
    {
%>
  <th align="center" valign="middle" nowrap class="size14 tableRN Rest" label="num" style="display:none">部屋単価</th>
<%
    }
%>
  <th align="center" valign="middle" nowrap class="size14 tableLN Stay" label="num" style="display:none">金額</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Stay" label="num" style="display:none">組数</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Stay" label="num" style="display:none">回転数</th>
  <th align="center" valign="middle" nowrap class="size14 tableLN Stay" label="num" style="display:none">客単価</th>
<%  if (ownerinfo.SalesGetStartDate%100 == 0)
    {
%>
  <th align="center" valign="middle" nowrap class="size14 tableLN Stay" label="num" style="display:none">部屋単価</th>
  <th align="center" valign="middle" nowrap class="size14 tableRN Stay" label="num" style="display:none">(一日当)</th>
<%
    }
    else
    {
%>
  <th align="center" valign="middle" nowrap class="size14 tableRN Stay" label="num" style="display:none">部屋単価</th>
<%
    }
%></tr>
<%
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
          Object lock = request.getSession();
          synchronized(lock){
            while( result.next())
            {
                hotelid   = result.getString("hotel.hotel_id");
                hotelname = result.getString("hotel.name");
                line_count++;
                i = line_count;

                room_count = 0;
                ownerinfo.sendPacket0100(1, hotelid);
                getresult = ownerinfo.sendPacket0114(1, hotelid);
                room_count        = ownerinfo.StateRoomCount;
                t_room_count[i]   = room_count;


                if (t_SalesTotal[i] == 0 && t_SalesTotalCount[i]==0)
                {
                    getresult = true;

                    if (ownerinfo.SalesGetStartDate == ownerinfo.SalesGetEndDate || ownerinfo.SalesGetEndDate == 0)
                    {
                        getresult = ownerinfo.sendPacket0102(1, hotelid,sales_cache_flag);
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
                        t_SalesRestTotal[i]          = ownerinfo.SalesRestTotal;
                        t_SalesRestCount[i]     = ownerinfo.SalesRestCount;
                        t_SalesRestRate[i]      = ownerinfo.SalesRestRate;
                        t_SalesRestPrice[i]     = ownerinfo.SalesRestPrice;
                        t_SalesRestRoomPrice[i] = Math.round((float)t_SalesRestTotal[i] / (float)t_room_count[i]);
                        t_SalesStayTotal[i]          = ownerinfo.SalesStayTotal;
                        t_SalesStayCount[i]     = ownerinfo.SalesStayCount;
                        t_SalesStayRate[i]      = ownerinfo.SalesStayRate;
                        t_SalesStayPrice[i]     = ownerinfo.SalesStayPrice;
                        t_SalesStayRoomPrice[i] = Math.round((float)t_SalesStayTotal[i] / (float)t_room_count[i]);
                        if (ownerinfo.SalesGetStartDate%100 != 0 && ownerinfo.SalesGetEndDate==0)
                        {
                            t_SalesRoomTotalPrice[i] = ownerinfo.SalesRoomPrice;
                        }
                        else
                        {
                            t_SalesRoomTotalPrice[i] = ownerinfo.SalesRoomTotalPrice;
                        }
                    }
                }
                date_count = 0;
                if (t_SalesRoomPrice[i] != 0)
                {
                    date_count    = Math.round((float)t_SalesRoomTotalPrice[i] / (float)t_SalesRoomPrice[i]);
                }
                if (ownerinfo.SalesGetStartDate%100 != 0 && ownerinfo.SalesGetEndDate==0)
                {
                    t_SalesRestRoomTotalPrice[i] = t_SalesRestRoomPrice[i];
                    t_SalesStayRoomTotalPrice[i] = t_SalesStayRoomPrice[i];
                }
                else
                {
                    t_SalesRoomTotalPrice[i] = ownerinfo.SalesRoomTotalPrice;
                    t_SalesRestRoomTotalPrice[i] = Math.round((float)t_SalesRestTotal[i] / ((float)t_room_count[i] * date_count));
                    t_SalesStayRoomTotalPrice[i] = Math.round((float)t_SalesStayTotal[i] / ((float)t_room_count[i] * date_count));
                }

                if(result.getInt("split_flag")==1)
                {
                    SplitFlag = true;
                }

                if((t_SalesTotal[i]!=0 || t_SalesTotalCount[i]!=0) && result.getInt("split_flag") == 0)
                {
                    store_count++;
                }
                if(result.getInt("split_flag") == 0)
                {
                    sales_total       = sales_total       + t_SalesTotal[i];
                    sales_total_count = sales_total_count + t_SalesTotalCount[i];
                    room_count_total  = room_count_total + room_count;
                    date_count_total  = date_count_total + date_count*room_count;
                    sales_rest_total       = sales_rest_total       + t_SalesRestTotal[i];
                    sales_rest_total_count = sales_rest_total_count + t_SalesRestCount[i];
                    sales_stay_total       = sales_stay_total       + t_SalesStayTotal[i];
                    sales_stay_total_count = sales_stay_total_count + t_SalesStayCount[i];
                }
%><tr class="lineDetail" height="24px" <%if (getresult){%>onclick="document.form1.HotelIdfromGroup.value='<%=hotelid%>';document.form1.action='salesdisp_select.jsp';document.form1.submit();"<%}%> >
    <td style="display:none"><%=i%></td>
    <td align="left" valign="middle"  class="listName size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>" nowrap><%= hotelname %><%if (result.getInt("split_flag") == 1){%><font size=1>(*)</font><%}%></td>
<%
//              if (t_SalesTotal[i] ==0 && t_SalesTotalCount[i] ==0)//取得できなかった
                if (!getresult)//取得できなかった
                {
%>    <td  colspan="<%if (ownerinfo.SalesGetStartDate%100 == 0){%>7<%}else{%>6<%}%>" align="middle" valign="middle" class="tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>" nowrap>　取得できませんでした。<input type="button" value="再取得" onclick="document.form1.submit();"></td>
<%
                }
                else
                {
%>    <input type="hidden" name="room_count_<%=param_compare%>_<%=i%>" id="room_count_<%=param_compare%>_<%=i%>" value="<%=t_room_count[i]%>">
    <input type="hidden" name="SalesTotal_<%=param_compare%>_<%=i%>" id="SalesTotal_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotal[i]%>">
    <input type="hidden" name="SalesTotalCount_<%=param_compare%>_<%=i%>" id="SalesTotalCount_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalCount[i]%>">
    <input type="hidden" name="SalesTotalRate_<%=param_compare%>_<%=i%>" id="SalesTotalRate_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalRate[i]%>">
    <input type="hidden" name="SalesTotalPrice_<%=param_compare%>_<%=i%>" id="SalesTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalPrice[i]%>">
    <input type="hidden" name="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>" id="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesRoomTotalPrice[i]%>">
    <input type="hidden" name="SalesRoomPrice_<%=param_compare%>_<%=i%>" id="SalesRoomPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesRoomPrice[i]%>">
    <td align="right" valign="middle" class="listRoomCount size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%>20<%}else{%><%=room_count%><%}%></td>
    <td align="right" valign="middle" class="Total listSalesTotal size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per)))%><%}else{%><%= Kanma.get(t_SalesTotal[i]) %><%}%></td>
    <td align="right" valign="middle" class="Total listSalesTotalCount size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(60*per)))%><%}else{%><%= Kanma.get(t_SalesTotalCount[i]) %><%}%></td>
    <td align="right" valign="middle" class="Total listSalesTotalRate size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%>3.00<%}else{%><%= (float)t_SalesTotalRate[i] / (float)100 %><% if((float)t_SalesTotalRate[i]%10==0){%>0<%}%><%}%></td>
    <td align="right" valign="middle" class="Total listSalesTotalPrice size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8750*per)))%><%}else{%><%= Kanma.get(t_SalesTotalPrice[i]) %><%}%></td>
<%
                    if (ownerinfo.SalesGetStartDate%100 == 0)
                    {
%>    <td align="right" valign="middle" class="Total listSalesRoomTotalPrice size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(t_SalesRoomTotalPrice[i]) %><%}%></td>
    <td align="right" valign="middle" class="Total listSalesRoomPrice size14 tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per*30)))%><%}else{%><%= Kanma.get(t_SalesRoomPrice[i]) %><%}%></td>
<%
                    }
                    else
                    {
%>    <td align="right" valign="middle" class="Total listSalesRoomTotalPrice size14 tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(t_SalesRoomTotalPrice[i]) %><%}%></td>
<%
                    }
%>
<!--休憩のみ-->
    <td align="right" valign="middle"label="num" style="display:none" class="Rest listSalesTotal size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per)))%><%}else{%><%= Kanma.get(t_SalesRestTotal[i]) %><%}%></td>
    <td align="right" valign="middle"label="num" style="display:none" class="Rest listSalesTotalCount size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(60*per)))%><%}else{%><%= Kanma.get(t_SalesRestCount[i]) %><%}%></td>
    <td align="right" valign="middle"label="num" style="display:none" class="Rest listSalesTotalRate size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%>3.00<%}else{%><%= (float)t_SalesRestRate[i] / (float)100 %><% if((float)t_SalesRestRate[i]%10==0){%>0<%}%><%}%></td>
    <td align="right" valign="middle"label="num" style="display:none" class="Rest listSalesTotalPrice size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8750*per)))%><%}else{%><%= Kanma.get(t_SalesRestPrice[i]) %><%}%></td>
<%
                    if (ownerinfo.SalesGetStartDate%100 == 0)
                    {
%>    <td align="right" valign="middle"label="num" style="display:none" class="Rest listSalesRoomTotalPrice size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(t_SalesRestRoomPrice[i]) %><%}%></td>
    <td align="right" valign="middle"label="num" style="display:none" class="Rest listSalesRoomPrice size14 tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per*30)))%><%}else{%><%= Kanma.get(t_SalesRestRoomTotalPrice[i]) %><%}%></td>
<%
                    }
                    else
                    {
%>    <td align="right" valign="middle"label="num" style="display:none" class="Rest listSalesRoomTotalPrice size14 tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(t_SalesRestRoomTotalPrice[i]) %><%}%></td>
<%
                    }
%>
<!--宿泊のみ-->
    <td align="right" valign="middle" style="display:none" class="Stay listSalesTotal size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per)))%><%}else{%><%= Kanma.get(t_SalesStayTotal[i]) %><%}%></td>
    <td align="right" valign="middle" style="display:none" class="Stay listSalesTotalCount size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(60*per)))%><%}else{%><%= Kanma.get(t_SalesStayCount[i]) %><%}%></td>
    <td align="right" valign="middle" style="display:none" class="Stay listSalesTotalRate size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%>3.00<%}else{%><%= (float)t_SalesStayRate[i] / (float)100 %><% if((float)t_SalesStayRate[i]%10==0){%>0<%}%><%}%></td>
    <td align="right" valign="middle" style="display:none" class="Stay listSalesTotalPrice size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8750*per)))%><%}else{%><%= Kanma.get(t_SalesStayPrice[i]) %><%}%></td>
<%
                    if (ownerinfo.SalesGetStartDate%100 == 0)
                    {
%>    <td align="right" valign="middle" style="display:none" class="Stay listSalesRoomTotalPrice size14 tableL<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(t_SalesStayRoomPrice[i]) %><%}%></td>
    <td align="right" valign="middle" style="display:none" class="Stay listSalesRoomPrice size14 tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per*30)))%><%}else{%><%= Kanma.get(t_SalesStayRoomTotalPrice[i]) %><%}%></td>
<%
                    }
                    else
                    {
%>    <td align="right" valign="middle" style="display:none" class="Stay listSalesRoomTotalPrice size14 tableR<%if (line_count%2==0){%>W<%}else{%>G<%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(t_SalesStayRoomTotalPrice[i]) %><%}%></td>
<%
                    }
                }
%></tr>
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
        DBConnection.releaseResources(result,prestate,connection);
    }
    int sales_total_rate = 0;
    if (date_count_total != 0)
    {
       sales_total_rate = Math.round((float)sales_total_count*100 / (float)date_count_total);
    }
    int sales_rest_total_rate = 0;
    if (date_count_total != 0)
    {
       sales_rest_total_rate = Math.round((float)sales_rest_total_count*100 / (float)date_count_total);
    }
    int sales_stay_total_rate = 0;
    if (date_count_total != 0)
    {
       sales_stay_total_rate = Math.round((float)sales_stay_total_count*100 / (float)date_count_total);
    }
%>
<tr height="24px">
    <td align="left" valign="middle"  class="size14 tableLB" nowrap>&nbsp;<%=store_count%>&nbsp;店舗合計</td>
    <td align="right" valign="middle" class="size14 tableLB" nowrap><%if(DemoMode){%><%=20*line_count%><%}else{%><%= Kanma.get(room_count_total) %><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Total" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per*line_count)))%><%}else{%><%= Kanma.get(sales_total) %><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Total" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(60*per*line_count)))%><%}else{%><%= Kanma.get(sales_total_count) %><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Total" nowrap><%if(DemoMode){%>3.00<%}else{%><%= (float)sales_total_rate / (float)100 %><% if((float)sales_total_rate%10==0){%>0<%}%><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Total" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8750*per)))%><%}else{%><% if(sales_total_count!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)sales_total_count))%><%}%><%}%></td>
<%
    if (ownerinfo.SalesGetStartDate%100 == 0)
    {
%>
    <td align="right" valign="middle" class="size14 tableLB Total" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)room_count_total))%><%}%><%}%></td>
    <td align="right" valign="middle" class="size14 tableRB Total" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per*30)))%><%}else{%><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)date_count_total))%><%}%><%}%></td>
<%
    }
    else
    {
%>
    <td align="right" valign="middle" class="size14 tableRB Total" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)room_count_total))%><%}%><%}%></td>
<%
    }
%>
    <td align="right" valign="middle" class="size14 tableLB Rest" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per*line_count)))%><%}else{%><%= Kanma.get(sales_rest_total) %><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Rest" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(60*per*line_count)))%><%}else{%><%= Kanma.get(sales_rest_total_count) %><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Rest" style="display:none" nowrap><%if(DemoMode){%>3.00<%}else{%><%= (float)sales_rest_total_rate / (float)100 %><% if((float)sales_rest_total_rate%10==0){%>0<%}%><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Rest" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8750*per)))%><%}else{%><% if(sales_rest_total_count!=0){%><%=Kanma.get(Math.round((float)sales_rest_total /(float)sales_rest_total_count))%><%}%><%}%></td>
<%
    if (ownerinfo.SalesGetStartDate%100 == 0)
    {
%>
    <td align="right" valign="middle" class="size14 tableLB Rest" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_rest_total /(float)room_count_total))%><%}%><%}%></td>
    <td align="right" valign="middle" class="size14 tableRB Rest" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per*30)))%><%}else{%><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_rest_total /(float)date_count_total))%><%}%><%}%></td>
<%
    }
    else
    {
%>
    <td align="right" valign="middle" class="size14 tableRB Rest" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_rest_total /(float)room_count_total))%><%}%><%}%></td>
<%
    }
%>
    <td align="right" valign="middle" class="size14 tableLB Stay" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per*line_count)))%><%}else{%><%= Kanma.get(sales_stay_total) %><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Stay" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(60*per*line_count)))%><%}else{%><%= Kanma.get(sales_stay_total_count) %><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Stay" style="display:none" nowrap><%if(DemoMode){%>3.00<%}else{%><%= (float)sales_stay_total_rate / (float)100 %><% if((float)sales_stay_total_rate%10==0){%>0<%}%><%}%></td>
    <td align="right" valign="middle" class="size14 tableLB Stay" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8750*per)))%><%}else{%><% if(sales_stay_total_count!=0){%><%=Kanma.get(Math.round((float)sales_stay_total /(float)sales_stay_total_count))%><%}%><%}%></td>
<%
    if (ownerinfo.SalesGetStartDate%100 == 0)
    {
%>
    <td align="right" valign="middle" class="size14 tableLB Stay" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_stay_total /(float)room_count_total))%><%}%><%}%></td>
    <td align="right" valign="middle" class="size14 tableRB Stay" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per*30)))%><%}else{%><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_stay_total /(float)date_count_total))%><%}%><%}%></td>
<%
    }
    else
    {
%>
    <td align="right" valign="middle" class="size14 tableRB Stay" style="display:none" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_stay_total /(float)room_count_total))%><%}%><%}%></td>
<%
    }
%>
</tr>
<tr height="30px">
    <td align="left" valign="middle"  class="size14" colspan="5">
        <input name="Compare"    type="hidden" value="<%=param_compare%>">
        <input name="StartYear"  type="hidden" value="<%=startYear%>">
        <input name="StartMonth" type="hidden" value="<%=startMonth%>">
        <input name="StartDay"   type="hidden" value="<%=paramDay%>">
<%
    if( endYear != null && endMonth != null && endDay != null )
    {
%>
        <input name="EndYear"    type="hidden" value="<%=endYear%>">
        <input name="EndMonth"   type="hidden" value="<%=endMonth%>">
        <input name="EndDay"     type="hidden" value="<%=endDay%>">
<%
    }
%>
        <input name="Kind"                type="hidden" value="<%=paramKind%>">
        <input name="Max"                 type="hidden" value="<%=i%>">
        <input name="HotelIdfromGroup"    type="hidden" value="<%=selecthotel%>">
<%
    if (paramGroup == null)
    {
%>
        <input type="button" onclick="location.href='sales_target_edit.jsp'" value="管理店舗選択">
<%
    }
    else
    {
%>
        <input type="button" onclick="history.back();" value="戻る">
<%
    }
%>


<%
    if(SplitFlag)
    {
%>
       <font size=1>(*)の店舗は合計に加算されません。</font>
<%
    }
%>
    </td>
</tr>
<script>
function salesCacheChange(obj)
{
    var flag = obj.checked ? 1 : 0;
    Event.observe(
        'hotel_id',
        '',
        new Ajax.Request( '../../common/pc/salesCacheChange.jsp',
            {
                method: 'get',
                parameters: 'flag=' +flag
            }
        )
    );
}
</script>