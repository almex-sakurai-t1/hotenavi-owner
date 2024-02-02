<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.DateEdit" %><%@ page import="com.hotenavi2.common.ReplaceString" %><%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
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
%>
<%
    String    hotelid;
    String    hotelname;

    hotelid = ReplaceString.getParameter(request,"NowHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    hotelname = ReplaceString.getParameter(request,"NowHotelName");
    if( hotelname == null )
    {
        hotelname = "";
    }

    int    i;
%>
<!-- 店舗表示 -->
<a name="<%= hotelid %>"></a>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20" colspan=2>
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="150" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF"><%= hotelname %></font></td>
          <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td height="20">
            <div><img src="../../common/pc/image/spacer.gif" width="12" height="16" align="absmiddle"><a href="#pagetop" class="navy10px">&gt;&gt;このページのトップへ</a></div>
          </td>
          <td height="20" align="right">
          </td>
        </tr>
      </table>
    </td>
  </tr>
<!-- ここから表 -->
  <tr>
<%
	boolean dateError = false;
    int start_date = Integer.parseInt(new DateEdit().getDate(2));
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
	if(dateError)
	{
        startYear="0";
        startMonth="0";
        startDay="0";
%>
		<script type="text/javascript">
		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
    if( startYear != null && startMonth != null && startDay != null )
    {
        start_date = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;
    ownerinfo.CalGetDate = start_year * 100 + start_month;
    Calendar cal = Calendar.getInstance();
    int year = ownerinfo.CalGetDate / 100;
    int month = ownerinfo.CalGetDate % 100;
    int day = 1;
    cal = getCalendar(Integer.toString(year*10000+month*100+1));

    int max_month = cal.getActualMaximum(cal.DAY_OF_MONTH);
    int start = cal.get(cal.DAY_OF_WEEK);

     ownerinfo.sendPacket0142(1, hotelid);
%>
<%@ include file="../../common/pc/calendar_parts.jsp" %>

<%
    startYear  = ReplaceString.getParameter(request,"CompareStartYear");
    startMonth = ReplaceString.getParameter(request,"CompareStartMonth");
    startDay   = ReplaceString.getParameter(request,"CompareStartDay");
    if( startYear != null && startMonth != null && startDay != null )
    {
        start_date = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    start_year  = start_date/10000;
    start_month = start_date/100%100;
    start_day   = start_date%100;
    ownerinfo.CalGetDate = start_year * 100 + start_month;
    year = ownerinfo.CalGetDate / 100;
    month = ownerinfo.CalGetDate % 100;
    cal = getCalendar(Integer.toString(year*10000+month*100+1));
    max_month = cal.getActualMaximum(cal.DAY_OF_MONTH);
    start = cal.get(cal.DAY_OF_WEEK);
    ownerinfo.sendPacket0142(1, hotelid);
%>

<%@ include file="../../common/pc/calendar_parts.jsp" %>
  </tr>
</table>
<!-- 1店舗目ここまで -->

