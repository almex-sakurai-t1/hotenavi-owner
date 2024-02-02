<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
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
    if  (startDay != null)
    {
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>日別売上一覧 <%= ownerinfo.HotelId %></title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="90" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF">日別売上一覧</font></td>
          <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td height="20">
            <div class="size10">
              <font color="#FFFFFF">&nbsp;
              </font>
            </div>
          </td>
        </tr>
      </table></td>
    <td width="3">&nbsp;</td>
  </tr>
  
  <!-- ここから表 -->
        <style>
            *.holiday {background-color: pink!important;}
            *.saturday {background-color: skyblue!important;}
        </style>
  <tr> 
    <td bgcolor="#999999">
      <table border="0" cellspacing="0" width="100%" cellpadding="0">
        <tr>
          <td height="3" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td height="3" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="3"><img src="../../common/pc/image/bg.gif" width="3" height="3"></td>
        </tr>
        <tr> 
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="20"></td>
          <td height="20">
            <div class="size14">
              <font color="#FFFFFF">
              <%-- 日付表示パーツ --%>
                  <jsp:include page="../../common/pc/salesdisp_datedisp.jsp" flush="true" />
              </font>
            </div>
          </td>
          <td width="8" valign="top">&nbsp;</td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3" height="20"></td>
        </tr>
        <tr> 
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="80"></td>
          <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td align="center" valign="middle" nowrap class="size14 tableLN" rowspan="2">日付</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN" rowspan="2">売上金額</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN" rowspan="2">組数</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN" rowspan="2">回転数</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN" rowspan="2">客単価</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN" rowspan="2">部屋単価</td>
              <td align="center" valign="middle" nowrap class="size14 tableRN" colspan="5">累　計</td>
            </tr>
            <tr>
              <td align="center" valign="middle" nowrap class="size14 tableLN">売上金額</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">組数</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">回転数</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">客単価</td>
              <td align="center" valign="middle" nowrap class="size14 tableRN">部屋単価</td>
            </tr>
<%
    //月合計
    int      sales_total = 0;
    int      sales_total_count =0 ;
    int      room_count  = 0;
    int      room_count_total = 0 ;
    int      date_count = 0 ;

    //料金モードの取得
    ownerinfo.CalGetDate = start_year * 100 + start_month;
    ownerinfo.sendPacket0142(1,hotelid);
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
        ownerinfo.SalesGetEndDate = target_date;

        // 売上情報取得	
        ownerinfo.sendPacket0102(1,hotelid);
        sales_total        += ownerinfo.SalesTotal;
        sales_total_count  += ownerinfo.SalesTotalCount;
        room_count = 0;
        if (ownerinfo.SalesRoomPrice != 0)
        {
            room_count    = Math.round((float)ownerinfo.SalesTotal / (float)ownerinfo.SalesRoomPrice);
        }
        room_count_total  = room_count_total + room_count;
        if(ownerinfo.SalesTotalCount != 0 || ownerinfo.SalesTotal != 0)
        {
           date_count++;
        }
        int total_rate = 0;
        if (room_count_total != 0)
        { 
           total_rate = Math.round((float)sales_total_count*100 / (float)room_count_total);
        }

        String nationalHolidayName = null;
        nationalHolidayName = getNationalHolidayName(Integer.toString(target_date));
        String tdTitle  = "";
        if (ownerinfo.CalDayModeName[target_day-1] != null)
        {
           tdTitle = ownerinfo.CalDayModeName[target_day-1];
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
            <tr onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();">
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><%= target_day %>(<small><%= arrWday[wday] %></small>)</a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><%= Kanma.get(ownerinfo.SalesTotal) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><%= Kanma.get(ownerinfo.SalesTotalCount) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><%= (float)ownerinfo.SalesTotalRate / (float)100 %><% if((float)ownerinfo.SalesTotalRate%10==0){%>0<%}%></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><%= Kanma.get(ownerinfo.SalesTotalPrice) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><%= Kanma.get(ownerinfo.SalesRoomPrice) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><%= Kanma.get(sales_total) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><%= Kanma.get(sales_total_count) %></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><%= (float)total_rate / (float)100 %><% if((float)total_rate%10==0){%>0<%}%></a></td>
              <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><% if(sales_total_count!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)sales_total_count))%><%}%></a></td>
              <td align="right" valign="middle" class="size14 tableRW<%=tdColor%>" title="<%=tdTitle%>" nowrap><a href="#" class="size14" onclick="top.Main.selectFrame.document.selectstore.Kind.selectedIndex=0;top.Main.selectFrame.document.selectstore.StartDayStore.value=<%=i%>;top.Main.mainFrame.location.href='sales_wait.html';top.Main.selectFrame.document.selectstore.submit();"><% if(room_count_total!=0){%><%=Kanma.get(Math.round(((float)sales_total*(float)date_count)/(float)room_count_total))%><%}%></a></td>
            </tr>
<%
    }
%>
          </table>
          </td>
          <td width="8" valign="top">&nbsp;</td>
          <td bgcolor="#666666" height="100%" ><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
        <tr>
          <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3" height="8"></td>
       </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="3" bgcolor="#999999">
      <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="3"><img src="../../common/pc/image/bg.gif" width="3" height="3"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
          <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
        </tr>
      </table>
    </td>
  </tr>
  
  <!-- ここまで -->
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="340" height="8"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10">Copyrights&copy; almex inc.
    All Rights Reserved.</td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="340" height="12"></td>
  </tr>
</table>
</body>
</html>
