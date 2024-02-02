<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String selecthotel;
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("demo"))
    {
        DemoMode = true;
    }
    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
%>
<%!
    private static Calendar getCalendar(String currentDate) {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        if (currentDate == null || currentDate.length() != 8) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            int year = Integer.parseInt(currentDate.substring(0, 4));
            int month = Integer.parseInt(currentDate.substring(4, 6)) - 1;
            int date = Integer.parseInt(currentDate.substring(6, 8));
            calendar.set(year, month, date);
        }
        return calendar;
    }
    private static String formatDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        return simpleDateFormat.format(calendar.getTime());
    }
    private static String getCurrentDateString() {
        return formatDate(getCalendar(null));
    }
    private static ArrayList<String> getDateList(Calendar calendar,Calendar calendar2) {
        ArrayList<String> list = new ArrayList<String>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        int dateInt2 =   Integer.parseInt(simpleDateFormat.format(calendar2.getTime()));
        while (dateInt2 >=   Integer.parseInt(simpleDateFormat.format(calendar.getTime()))) {
                list.add(simpleDateFormat.format(calendar.getTime()));
                calendar.add(Calendar.DATE, 1);
        }
        return list;
    }
%>
<!--祝日名調査-->
<%@ include file="../../common/pc/getNationalHolidayName.jsp" %>
<%
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    String param_compare          = ReplaceString.getParameter(request,"Compare");
    if (param_compare == null)
    {
        param_compare = "false";
    }
    String paramKind   = ReplaceString.getParameter(request,"Kind");
    if (paramKind == null)
    {
        paramKind = "MONTHLIST";
    }

    boolean TotalMode = false;
    if (paramKind.indexOf("LISTR") != -1)
    {
        TotalMode = true;
    }
%><%-- 日別売上一覧表示処理 --%><%

    int             i;
    StringFormat    sf;
    NumberFormat    nf;

    sf = new StringFormat();
    nf = new DecimalFormat("00");

    int       target_date;
    int       target_day;
    int       wday;
    String[]  arrWday = {"日", "月", "火", "水", "木", "金", "土"};

    // 現在日付
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;

    // 計上日付
    int cal_date  = ownerinfo.Addupdate;
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;
    if  (cal_date == 0)
    {
        cal_date = now_date;
    }
    int   start_date = cal_date;
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
    if( startYear != null && startMonth != null && startDay != null )
    {
        start_date = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    else if (start_date == 0)
    {
        start_date = now_date;
    }
    String paramDate = Integer.toString(start_date);

    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;

    int end_date    = 0;
    String paramEndDate = paramDate;

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
    if( endYear != null && endMonth != null && endDay != null )
    {
        end_date = (Integer.valueOf(endYear).intValue() * 10000) + (Integer.valueOf(endMonth).intValue() * 100) + Integer.valueOf(endDay).intValue();
    }
    if (end_date == 0)
    {
        if (cal_date/100 == start_date/100)
        {
            end_date  = start_date + cal_day - 1;//当月なので計上日付
        }
        else
        {
            end_date  = start_date + getCalendar(paramDate).getActualMaximum(Calendar.DATE) - 1;//月末日付
        }
    }
    int end_year    = end_date/10000;
    int end_month   = end_date/100%100;
    int end_day     = end_date%100;
    paramEndDate    = Integer.toString(end_date);

    ArrayList<String> list = getDateList(getCalendar(paramDate),getCalendar(paramEndDate));

%>
<style>
  *.holiday {background-color: #FFDDDD!important;}
  *.saturday {background-color: #DDDDFF!important;}
</style>
            <tr>
              <td align="center" valign="middle" nowrap class="size14 tableLN" colspan=2>日付</td>
<%
    if (TotalMode)
    {
%>
              <td align="center" valign="middle" nowrap class="size14 tableLN">売上金額</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">（累計）</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">組数</td>
              <td align="center" valign="middle" nowrap class="size14 tableRN">（累計）</td>
<%
    }
    else
    {
%>
              <td align="center" valign="middle" nowrap class="size14 tableLN">売上金額</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">組数</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">回転数</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">客単価</td>
              <td align="center" valign="middle" nowrap class="size14 tableRN">部屋単価</td>
<%
    }
%>

            </tr>
<%
    //再取得時用
    int[]    t_target_date     = new int[61];
    int[]    t_SalesTotal      = new int[61];
    int[]    t_SalesTotalCount = new int[61];
    int[]    t_SalesTotalRate  = new int[61];
    int[]    t_SalesTotalPrice = new int[61];
    int[]    t_SalesRoomPrice  = new int[61];
    int[]    t_RoomCount       = new int[61];
    int      Max               = 0;
    String   paramMax          = ReplaceString.getParameter(request,"Max");
    if      (paramMax         == null)    paramMax = "0";
    Max      = Integer.parseInt(paramMax);
    String   paramData         = "";

    for( i = 0 ; i <= Max ; i++ )
    {
        paramData =  ReplaceString.getParameter(request,"target_date_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_target_date[i]       = Integer.parseInt(paramData);
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
        paramData =  ReplaceString.getParameter(request,"RoomCount_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_RoomCount[i]   = Integer.parseInt(paramData);
    }

    //月合計
    int      sales_total = 0;
    int      sales_total_count =0 ;
    int      room_count  = 0;
    int      room_count_total = 0 ;
    int      date_count = 0 ;
    int      total_rate = 0;

    //料金モードの取得
    boolean      getresult = true;
    for( i = 0 ; i < list.size() ; i++ )
    {
         // 曜日の算出
        wday = getCalendar(list.get(i)).get(Calendar.DAY_OF_WEEK)-1;

        target_date   = Integer.parseInt(list.get(i));
        target_day    = target_date%100;
        // 売上取得日付のセット
        ownerinfo.SalesGetStartDate = target_date;
        ownerinfo.SalesGetEndDate = 0;

        String nationalHolidayName = null;
        nationalHolidayName = getNationalHolidayName(Integer.toString(target_date));
        int    CalDayMode      = 0;
        String CalDayModeName  = "";
        String tdTitle         = "";
        String backgroundColor = "";
        if (DemoMode && CalDayModeName.equals(""))
        {
            if(wday == 0)
            {
               CalDayModeName = "日曜日";
            }
            else if(wday == 6)
            {
               CalDayModeName = "土曜日";
            }
            else
            {
               CalDayModeName = "平日";
            }
        }
        String tdColor  = "";
        if (nationalHolidayName.length() != 0) {
            tdColor = " holiday";
            tdTitle =  tdTitle + " " + nationalHolidayName;
            } else if (wday == 0) {
            tdColor = " holiday";
            } else if (wday == 6) {
            tdColor = " saturday";
            }
%>
            <tr height="24px" onclick="location.href='salesdispgroup.jsp?Group&StartYear=<%=target_date/10000%>&StartMonth=<%=target_date/100%100%>&StartDay=<%=target_date%100%>'">
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%> colspan=2><input type="hidden" name="target_date_<%=param_compare%>_<%=i%>" id="target_date_<%=param_compare%>_<%=i%>" value="<%=target_date%>"><a href="#" class="size14"><%= target_date/100%100 %>月<%if (target_day < 10){%>&nbsp;<%}%><%= target_day %>日(<small><%= arrWday[wday] %></small>)</a></td>
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
            prestate.setString(1,loginHotelId);
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
            if (imedia_user == 1 && level == 3)
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
	                 + " AND owner_user_hotel.userid =?"
	                 + " ORDER BY hotel.sort_num,hotel.hotel_id";
            }
            prestate    = connection.prepareStatement(query);
            if (imedia_user == 1 && level == 3)
            {
                prestate.setString(1, selecthotel);
            }
            else
            {
                prestate.setString(1, loginHotelId);
                prestate.setInt(2, ownerinfo.DbUserId);
            }
            result      = prestate.executeQuery();
              Object lock = request.getSession();
              synchronized(lock)
              {
                while( result.next())
                {
                    hotelid = result.getString("hotel.hotel_id");
                    getresult = ownerinfo.sendPacket0102(1, hotelid,1);
                    if (getresult)
                    {
                        t_SalesTotal[i]      += ownerinfo.SalesTotal;
                        sales_total          += ownerinfo.SalesTotal;
                        t_SalesTotalCount[i] += ownerinfo.SalesTotalCount;
                        sales_total_count    += ownerinfo.SalesTotalCount;

                        room_count = 0;
                        if (ownerinfo.SalesRoomPrice != 0)
                        {
                            room_count    = Math.round((float)ownerinfo.SalesTotal / (float)ownerinfo.SalesRoomPrice);
                        }
                        t_RoomCount[i]       += room_count;
                        room_count_total     += room_count;
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
        //回転率
        if (t_RoomCount[i] != 0)
        {
           t_SalesTotalRate[i] = Math.round((float)t_SalesTotalCount[i]*100 / (float)t_RoomCount[i]);
        }
        //客単価
        if (t_SalesTotalCount[i] != 0)
        {
           t_SalesTotalPrice[i] = Math.round((float)t_SalesTotal[i] / (float)t_SalesTotalCount[i]);
        }
        //部屋単価
        if (t_RoomCount[i] != 0)
        {
           t_SalesRoomPrice[i] = Math.round((float)t_SalesTotal[i] / (float)t_RoomCount[i]);
        }

        if(t_SalesTotal[i]!=0 || t_SalesTotalCount[i]!=0)
        {
           date_count++;
        }
        total_rate = 0;
        if (room_count_total != 0)
        {
           total_rate = Math.round((float)sales_total_count*100 / (float)room_count_total);
        }

        if (TotalMode)
        {
%>              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotal_<%=param_compare%>_<%=i%>"           id="SalesTotal_<%=param_compare%>_<%=i%>"           value="<%=t_SalesTotal[i]%>">     <a href="#" class="size14"><%= Kanma.get(t_SalesTotal[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalTotal_<%=param_compare%>_<%=i%>"      id="SalesTotalTotal_<%=param_compare%>_<%=i%>"      value="<%=sales_total%>">         <a href="#" class="size14"><%= Kanma.get(sales_total) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalCount_<%=param_compare%>_<%=i%>"      id="SalesTotalCount_<%=param_compare%>_<%=i%>"      value="<%=t_SalesTotalCount[i]%>"><a href="#" class="size14"><%= Kanma.get(t_SalesTotalCount[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableRW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalTotalCount_<%=param_compare%>_<%=i%>" id="SalesTotalTotalCount_<%=param_compare%>_<%=i%>" value="<%=sales_total_count%>">   <a href="#" class="size14"><%= Kanma.get(sales_total_count) %></a></td>
              <input type="hidden" name="SalesTotalRate_<%=param_compare%>_<%=i%>"  id="SalesTotalRate_<%=param_compare%>_<%=i%>"  value="<%=t_SalesTotalRate[i]%>">
              <input type="hidden" name="SalesTotalPrice_<%=param_compare%>_<%=i%>" id="SalesTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalPrice[i]%>">
              <input type="hidden" name="SalesRoomPrice_<%=param_compare%>_<%=i%>"  id="SalesRoomPrice_<%=param_compare%>_<%=i%>"  value="<%=t_SalesRoomPrice[i]%>">
<%
        }
        else
        {
%>              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotal_<%=param_compare%>_<%=i%>"      id="SalesTotal_<%=param_compare%>_<%=i%>"      value="<%=t_SalesTotal[i]%>">     <a href="#" class="size14"><%= Kanma.get(t_SalesTotal[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalCount_<%=param_compare%>_<%=i%>" id="SalesTotalCount_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalCount[i]%>"><a href="#" class="size14"><%= Kanma.get(t_SalesTotalCount[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalRate_<%=param_compare%>_<%=i%>"  id="SalesTotalRate_<%=param_compare%>_<%=i%>"  value="<%=t_SalesTotalRate[i]%>"> <a href="#" class="size14"><%= (float)t_SalesTotalRate[i] / (float)100 %><% if((float)t_SalesTotalRate[i]%10==0){%>0<%}%></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalPrice_<%=param_compare%>_<%=i%>" id="SalesTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalPrice[i]%>"><a href="#" class="size14"><%= Kanma.get(t_SalesTotalPrice[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableRW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesRoomPrice_<%=param_compare%>_<%=i%>"  id="SalesRoomPrice_<%=param_compare%>_<%=i%>"  value="<%=t_SalesRoomPrice[i]%>"> <a href="#" class="size14"><%= Kanma.get(t_SalesRoomPrice[i]) %></a></td>
<%
        }
%>            </tr>
<%
    }
%>
<%
    if(!TotalMode)
    {
%>
            <tr>
              <td align="center" valign="middle" class="size14 tableLB" nowrap rowspan="2"><%=date_count%>日間</td>
              <td align="center" valign="middle" class="size14 tableLB" nowrap>合計</td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(sales_total) %></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(sales_total_count) %></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= (float)total_rate / (float)100 %><% if((float)total_rate%10==0){%>0<%}%></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><% if(sales_total_count!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)sales_total_count))%><%}else{%>－<%}%></td>
              <td align="right" valign="middle" class="size14 tableRB" nowrap><% if(room_count_total!=0){%><%=Kanma.get(Math.round(((float)sales_total*(float)date_count)/(float)room_count_total))%><%}else{%>－<%}%></td>
            </tr>
            <tr>
              <td align="center" valign="middle" class="size14 tableLB" nowrap>平均</td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(Math.round((float)sales_total/(float)date_count)) %></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(Math.round((float)sales_total_count/(float)date_count)) %></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap>－</td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap>－</td>
              <td align="right" valign="middle" class="size14 tableRB" nowrap><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_total/(float)room_count_total))%><%}else{%>－<%}%></td>
            </tr>
<%
    }
%>
            <tr height="24px">
              <td align="left" valign="middle"  class="size14"  colspan=4>
                <input name="Kind"         type="hidden" value="<%=paramKind%>">
                <input name="KindfromList" type="hidden" value="DATE">
                <input name="Compare"      type="hidden" value="<%=param_compare%>">
                <input name="StartYear"    type="hidden" value="<%=startYear%>">
                <input name="StartMonth"   type="hidden" value="<%=startMonth%>">
                <input name="StartDay"     type="hidden" value="<%=paramDay%>">
                <input name="StartYearfromList"    type="hidden" value="<%=startYear%>">
                <input name="StartMonthfromList"   type="hidden" value="<%=startMonth%>">
                <input name="StartDayfromList"     type="hidden" value="<%=paramDay%>">
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
                <input name="Max"        type="hidden" value="<%=i%>">
                <input type="button" onclick="location.href='sales_target_edit.jsp'" value="管理店舗選択">
              </td>
<%
    if(TotalMode)
    {
%>
              <td align="left" valign="middle"  class="size14"  colspan=2>
                <input type="button" onclick="document.form1.Kind.value='<%=paramKind.replace("LISTR","LIST")%>';document.form1.submit();" value="通常表示">
<%
    }
    else
    {
%>
              <td align="left" valign="middle"  class="size14"  colspan=3>
                <input type="button" onclick="document.form1.Kind.value='<%=paramKind%>R';document.form1.submit();" value="日別累計表示">
<%
    }
%>
              </td>
            </tr>
