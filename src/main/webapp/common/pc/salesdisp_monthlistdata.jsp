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
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("demo"))
    {
        DemoMode = true;
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

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    String  ModeColor     = "#FFFFFF";
    boolean ModeColorFlag = true;
    // 色データの読み込み
    try
    {
    	final String query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            ModeColor   = result.getString("mode_color");
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
    if (ModeColor.equals(""))
    {
        ModeColor = "#FFFFFF";
    }
    ModeColor = ModeColor + ",#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF"; //何も入ってないときを考慮して分割用の(,)を付加。
    if (ModeColor.replace(",#FFFFFF","").equals("#FFFFFF"))
    {
        ModeColorFlag = false;
    }
    String[] colorAry = ModeColor.split(",");
%>
<%-- 日別売上一覧表示処理 --%>
<%

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
              <td align="center" valign="middle" nowrap class="size14 tableLN" <%if (param_compare.equals("true")){%>colspan=2<%}%>>日付</td>
<%
    if (param_compare.equals("false"))
    {
%>
              <td align="center" valign="middle" nowrap class="size14 tableLN">料金モード</td>
<%
    }
%>
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
    int[]    t_SalesDetailTotal  = new int[61];
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
    }

    //月合計
    int      sales_total = 0;
    int      sales_total_count =0 ;
    int      room_count  = 0;
    int      room_count_total = 0 ;
    int      date_count = 0 ;
    int      total_rate = 0;

    //料金モードの取得
    ownerinfo.CalGetDate = start_year * 100 + start_month;
    ownerinfo.sendPacket0142(1,hotelid);
    boolean      getresult = true;

    int    CalModeCount   = ownerinfo.CalModeCount;
%>
<%
    for( i = 0 ; i < list.size() ; i++ )
    {
         // 曜日の算出
        wday = getCalendar(list.get(i)).get(Calendar.DAY_OF_WEEK)-1;

        target_date   = Integer.parseInt(list.get(i));
        target_day    = target_date%100;
        if (target_day == 1)
        {
            //料金モードの取得
            ownerinfo.CalGetDate = target_date / 100;
            ownerinfo.sendPacket0142(1,hotelid);
        }
        // 売上取得日付のセット
        ownerinfo.SalesGetStartDate = target_date;
        ownerinfo.SalesGetEndDate = 0;
        ownerinfo.SalesDetailGetStartDate = target_date;
        ownerinfo.SalesDetailGetEndDate = 0;

        getresult = true;
        if (t_SalesTotal[i] == 0 && t_SalesTotalCount[i]==0)
        {
            if (DemoMode)
            {
                float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;
                t_SalesTotal[i]      = (int)(Math.round(525000*per));
                t_SalesTotalCount[i] = (int)(Math.round(60*per));
                t_SalesTotalRate[i]  = 300;
                t_SalesTotalPrice[i] = (int)(Math.round(8750*per));
                t_SalesRoomPrice[i]  = (int)(Math.round(26250*per));
            }
            else
            {
            // 売上情報取得
                getresult = ownerinfo.sendPacket0102(1,hotelid,1);
                t_SalesTotal[i]      = ownerinfo.SalesTotal;
                t_SalesTotalCount[i] = ownerinfo.SalesTotalCount;
                t_SalesTotalRate[i]  = ownerinfo.SalesTotalRate;
                t_SalesTotalPrice[i] = ownerinfo.SalesTotalPrice;
                t_SalesRoomPrice[i]  = ownerinfo.SalesRoomPrice;
                ownerinfo.sendPacket0104(1,hotelid);
                t_SalesDetailTotal[i]      = ownerinfo.SalesDetailTotal;
            }
        }

        sales_total        += t_SalesTotal[i];
        sales_total_count  += t_SalesTotalCount[i];

        room_count = 0;
        if (t_SalesRoomPrice[i] != 0)
        {
            room_count    = Math.round((float)t_SalesTotal[i] / (float)t_SalesRoomPrice[i]);
        }
        room_count_total  = room_count_total + room_count;
        if(t_SalesTotal[i]!=0 || t_SalesTotalCount[i]!=0)
        {
           date_count++;
        }
        total_rate = 0;
        if (room_count_total != 0)
        {
           total_rate = Math.round((float)sales_total_count*100 / (float)room_count_total);
        }

        String nationalHolidayName = null;
        nationalHolidayName = getNationalHolidayName(Integer.toString(target_date));
        int    CalDayMode      = 0;
        String CalDayModeName  = "";
        String tdTitle         = "";
        String backgroundColor = "";
        if (ownerinfo.CalDayModeName[target_day-1] != null)
        {
           CalDayMode     = ownerinfo.CalDayMode[target_day-1];
           CalDayModeName = ownerinfo.CalDayModeName[target_day-1];
           tdTitle        = ownerinfo.CalDayModeName[target_day-1];
        }
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
        if (ModeColorFlag && CalDayMode != 0)
        {
           backgroundColor = "style=\"background-color:" + colorAry[CalDayMode-1] + " !important\"";
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
            <tr height="24px">
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%> <%if (param_compare.equals("true")){%>colspan=2<%}%>><input type="hidden" name="target_date_<%=param_compare%>_<%=i%>" id="target_date_<%=param_compare%>_<%=i%>" value="<%=target_date%>"><a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= target_date/100%100 %>月<%if (target_day < 10){%>&nbsp;<%}%><%= target_day %>日(<small><%= arrWday[wday] %></small>)</a></td>
<%
        if (param_compare.equals("false"))
        {
%>
              <td align="left" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="CalDayModeName_<%=param_compare%>_<%=i%>" id="CalDayModeName_<%=param_compare%>_<%=i%>" value="<%=CalDayModeName%>"><a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%=CalDayModeName%></a></td>
<%
        }
%>
<%
        if (!getresult)//取得できなかった
        {
%>
              <td  colspan="5" align="middle" valign="middle" class="size14 tableRW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>>　取得できませんでした。<input type="button" value="再取得" onclick="document.form1.submit();"></td>
<%
        }
        else
        {
            if (TotalMode)
            {
%>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotal_<%=param_compare%>_<%=i%>"           id="SalesTotal_<%=param_compare%>_<%=i%>"           value="<%=t_SalesTotal[i]%>">     <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotal[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalTotal_<%=param_compare%>_<%=i%>"      id="SalesTotalTotal_<%=param_compare%>_<%=i%>"      value="<%=sales_total%>">         <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= Kanma.get(sales_total) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalCount_<%=param_compare%>_<%=i%>"      id="SalesTotalCount_<%=param_compare%>_<%=i%>"      value="<%=t_SalesTotalCount[i]%>"><a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotalCount[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableRW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalTotalCount_<%=param_compare%>_<%=i%>" id="SalesTotalTotalCount_<%=param_compare%>_<%=i%>" value="<%=sales_total_count%>">   <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= Kanma.get(sales_total_count) %></a></td>
              <input type="hidden" name="SalesTotalRate_<%=param_compare%>_<%=i%>"  id="SalesTotalRate_<%=param_compare%>_<%=i%>"  value="<%=t_SalesTotalRate[i]%>">
              <input type="hidden" name="SalesTotalPrice_<%=param_compare%>_<%=i%>" id="SalesTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalPrice[i]%>">
              <input type="hidden" name="SalesRoomPrice_<%=param_compare%>_<%=i%>"  id="SalesRoomPrice_<%=param_compare%>_<%=i%>"  value="<%=t_SalesRoomPrice[i]%>">
<%
            }
            else
            {
%>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotal_<%=param_compare%>_<%=i%>"      id="SalesTotal_<%=param_compare%>_<%=i%>"      value="<%=t_SalesTotal[i]%>">     <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotal[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalCount_<%=param_compare%>_<%=i%>" id="SalesTotalCount_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalCount[i]%>"><a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotalCount[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalRate_<%=param_compare%>_<%=i%>"  id="SalesTotalRate_<%=param_compare%>_<%=i%>"  value="<%=t_SalesTotalRate[i]%>"> <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= (float)t_SalesTotalRate[i] / (float)100 %><% if((float)t_SalesTotalRate[i]%10==0){%>0<%}%></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalPrice_<%=param_compare%>_<%=i%>" id="SalesTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalPrice[i]%>"><a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotalPrice[i]) %></a></td>
              <td align="right" valign="middle" class="size14 tableRW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesRoomPrice_<%=param_compare%>_<%=i%>"  id="SalesRoomPrice_<%=param_compare%>_<%=i%>"  value="<%=t_SalesRoomPrice[i]%>"> <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.StartDayfromList.value=<%=target_date%100%>;document.form1.submit();"><%= Kanma.get(t_SalesRoomPrice[i]) %></a></td>
<%
            }
        }
%>
            </tr> 
<%
    }
%>
<%
    if (param_compare.equals("true") && (paramKind.equals("MONTHLIST") || paramKind.equals("MONTHLISTR")))
    {
        for( i = list.size() ; i < 31 ; i++ )
        {
%>
            <tr height="24px">
              <td align="right" valign="middle" class="size14 tableLW" nowrap <%if (param_compare.equals("true")){%>colspan=2<%}%>><a href="#" class="size14"></a></td>
<%
            if (param_compare.equals("false"))
            {
%>
              <td align="left" valign="middle" class="size14 tableLW" nowrap >　</td>
<%
            }
%>
              <td align="right" valign="middle" class="size14 tableLW" nowrap ><a href="#" class="size14"></a></td>
              <td align="right" valign="middle" class="size14 tableLW" nowrap ><a href="#" class="size14"></a> 　</td>
              <td align="right" valign="middle" class="size14 tableLW" nowrap ><a href="#" class="size14"></a> 　</td>
<%
    if(!TotalMode)
    {
%>
              <td align="right" valign="middle" class="size14 tableLW" nowrap><a href="#" class="size14"></a>　</td>
<%
    }
%>
              <td align="right" valign="middle" class="size14 tableRW" nowrap > 　</td>
            </tr>
<%
        }
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
                <input type="button" onclick="document.form1.action='sales_mode_edit.jsp';document.form1.submit();" value="料金モード別色指定">
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
