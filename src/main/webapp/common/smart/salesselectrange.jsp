<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%
    String contents = request.getParameter("Contents");
    if (contents == null)
    {
        contents = "salesdisp";
    }

    // ホテルIDのセット
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }

    // 現在日付
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;

    // 計上日付
    int addup_date = ownerinfo.Addupdate;
    if (addup_date == 0)
    {
        addup_date = now_date;
    }
    int addup_year  = addup_date / 10000;
    int addup_month = addup_date / 100 % 100;
    int addup_day   = addup_date % 100;

    // 取得日付
    String year     = request.getParameter("Year");
    String month    = request.getParameter("Month");
    String day      = request.getParameter("Day");
    String endyear  = request.getParameter("EndYear");
    String endmonth = request.getParameter("EndMonth");
    String endday   = request.getParameter("EndDay");

    int cal_date = ownerinfo.SalesGetStartDate;
    if (contents.equals("roomhistory") || contents.equals("salesinout"))
    {
        cal_date = ownerinfo.InOutGetStartDate;
    }
    if (year != null && month != null && day != null)
    {
        cal_date = (Integer.valueOf(year).intValue() * 10000) + (Integer.valueOf(month).intValue() * 100) + Integer.valueOf(day).intValue();
    }
    if (cal_date == 0)
    {
        cal_date = addup_date;
    }
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;

    int cal_enddate = ownerinfo.SalesGetEndDate;
    if (contents.equals("roomhistory") || contents.equals("salesinout"))
    {
        cal_enddate = ownerinfo.InOutGetEndDate;
    }
    if(endyear != null && endmonth != null && endday != null)
    {
        cal_enddate = (Integer.valueOf(endyear).intValue() * 10000) + (Integer.valueOf(endmonth).intValue() * 100) + Integer.valueOf(endday).intValue();
    }
    if (cal_enddate == 0)
    {
        cal_enddate = cal_date;
    }
    int cal_endyear  = cal_enddate/10000;
    int cal_endmonth = cal_enddate/100%100;
    int cal_endday   = cal_enddate%100;

    String paramDate       = Integer.toString(cal_date);
    String nextMonthDate   = getNextMonthDateString(paramDate);
    String beforeMonthDate = getBeforeMonthDateString(paramDate);
    String nextYearDate    = getNextYearDateString(paramDate);
    String beforeYearDate  = getBeforeYearDateString(paramDate);
    String nextDayDate     = getNextDayDateString(paramDate);
    String beforeDayDate   = getBeforeDayDateString(paramDate);
    String nextWeekDate    = getNextWeekDateString(paramDate);
    String beforeWeekDate  = getBeforeWeekDateString(paramDate);
    
    String paramEndDate       = Integer.toString(cal_enddate);
    String beforeEndMonthDate = getBeforeMonthDateString(paramEndDate);
    String beforeEndYearDate  = getBeforeYearDateString(paramEndDate);
    String beforeEndDayDate   = getBeforeDayDateString(paramEndDate);
    String beforeEndWeekDate  = getBeforeWeekDateString(paramEndDate);
    String nextEndMonthDate   = getNextMonthDateString(paramEndDate);
    String nextEndYearDate    = getNextYearDateString(paramEndDate);
    String nextEndDayDate     = getNextDayDateString(paramEndDate);
    String nextEndWeekDate    = getNextWeekDateString(paramEndDate);

    int min_date = 20050101; // 最小日付
    min_date = Integer.parseInt(getMinDateString(Integer.toString(addup_date))); //5年前の日付

    //リンクを無効チェック
    boolean beforeYearLink  = true;
    boolean beforeMonthLink = true;
    boolean beforeWeekLink  = true;
    boolean beforeDayLink   = true;
    boolean nextYearLink    = true;
    boolean nextMonthLink   = true;
    boolean nextWeekLink    = true;
    boolean nextDayLink     = true;

    if (Integer.parseInt(beforeYearDate) < min_date)
    {
        beforeYearLink = false;
    }
    if (Integer.parseInt(beforeMonthDate) <  min_date)
    {
        beforeMonthLink = false;
    }
    if (Integer.parseInt(beforeWeekDate) <  min_date)
    {
        beforeWeekLink = false;
    }
    if (Integer.parseInt(beforeDayDate) <  min_date)
    {
        beforeDayLink = false;
    }
    if (Integer.parseInt(nextEndYearDate) > addup_date)
    {
        nextYearLink = false;
    }
    if (Integer.parseInt(nextEndMonthDate) > addup_date)
    {
        nextMonthLink = false;
    }
    if (Integer.parseInt(nextEndWeekDate) > addup_date)
    {
        nextWeekLink = false;
    }
    if (Integer.parseInt(nextEndDayDate) > addup_date)
    {
        nextDayLink = false;
    }

    int target_date    = 0;
    int target_enddate = 0;

    //日付選択エリアの作成
    int    i;
    String tagyear;
    String tagmonth;
    String tagday;
    String tagendyear;
    String tagendmonth;
    String tagendday;

    tagyear = "<select id=\"year\" name=\"Year\">";
    for( i = -4 ; i < 2 ; i++ )
    {
        if( addup_year + (i-1) == cal_year )
        {
            tagyear = tagyear + "<option value=\"" + (addup_year + (i-1)) + "\" selected>" + (addup_year + (i-1));
        }
        else
        {
            tagyear = tagyear + "<option value=\"" + (addup_year + (i-1)) + "\">" + (addup_year + (i-1));
        }
    }
    tagyear = tagyear + "</select>年";

    tagmonth = "<select id=\"month\" name=\"Month\">";
    for( i = 0 ; i < 12 ; i++ )
    {
        if( (i + 1) == cal_month )
        {
            tagmonth = tagmonth + "<option value=\"" + (i + 1) + "\" selected>" + (i + 1);
        }
        else
        {
            tagmonth = tagmonth + "<option value=\"" + (i + 1) + "\">" + (i + 1);
        }
    }
    tagmonth = tagmonth + "</select>月";

    tagday = "<select id=\"day\" name=\"Day\">";
    for( i = 0 ; i < 31 ; i++ )
    {
        if( (i + 1) == cal_day )
        {
            tagday = tagday + "<option value=\"" + (i + 1) + "\" selected>" + (i + 1);
        }
        else
        {
            tagday = tagday + "<option value=\"" + (i + 1) + "\">" + (i + 1);
        }
    }
    tagday = tagday + "</select>日";

    // 終了
    tagendyear = "<select id=\"endyear\" name=\"EndYear\">";
    for( i = -4 ; i < 2 ; i++ )
    {
        if( now_year + (i-1) == cal_endyear )
        {
            tagendyear = tagendyear + "<option value=\"" + (addup_year + (i-1)) + "\" selected>" + (addup_year + (i-1));
        }
        else
        {
            tagendyear = tagendyear + "<option value=\"" + (addup_year + (i-1)) + "\">" + (addup_year + (i-1));
        }
    }
    tagendyear = tagendyear + "</select>年";

    tagendmonth = "<select id=\"endmonth\" name=\"EndMonth\">";
    for( i = 0 ; i < 12 ; i++ )
    {
        if( (i + 1) == cal_endmonth )
        {
            tagendmonth = tagendmonth + "<option value=\"" + (i + 1) + "\" selected>" + (i + 1);
        }
        else
        {
            tagendmonth = tagendmonth + "<option value=\"" + (i + 1) + "\">" + (i + 1);
        }
    }
    tagendmonth = tagendmonth + "</select>月";

    tagendday = "<select id=\"endday\" name=\"EndDay\">";
    for( i = 0 ; i < 31 ; i++ )
    {
        if( (i + 1) == cal_endday )
        {
            tagendday = tagendday + "<option value=\"" + (i + 1) + "\" selected>" + (i + 1);
        }
        else
        {
            tagendday = tagendday + "<option value=\"" + (i + 1) + "\">" + (i + 1);
        }
    }
    tagendday = tagendday + "</select>日";

    if (cal_date != cal_enddate)
    {
        if (beforeDayLink)
        {
            target_date    = Integer.parseInt(beforeDayDate);
            target_enddate = Integer.parseInt(beforeEndDayDate);
%>
<ul class="link_detail">
<li class="some">
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Range&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)+ "&EndYear="+(target_enddate/10000)+"&EndMonth="+(target_enddate/100%100)+"&EndDay="+(target_enddate%100)) %>">前日</a>
<%
        }
%>
<%
        if (beforeWeekLink)
        {
            target_date = Integer.parseInt(beforeWeekDate);
            target_enddate = Integer.parseInt(beforeEndWeekDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Range&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)+ "&EndYear="+(target_enddate/10000)+"&EndMonth="+(target_enddate/100%100)+"&EndDay="+(target_enddate%100)) %>">前週</a>
<%
        }
%>
<%
        if (beforeMonthLink)
        {
            target_date = Integer.parseInt(beforeMonthDate);
            target_enddate = Integer.parseInt(beforeEndMonthDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Range&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)+ "&EndYear="+(target_enddate/10000)+"&EndMonth="+(target_enddate/100%100)+"&EndDay="+(target_enddate%100)) %>">前月</a>
<%
        }
%>
<%
        if (beforeYearLink)
        {
            target_date = Integer.parseInt(beforeYearDate);
            target_enddate = Integer.parseInt(beforeEndYearDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Range&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)+ "&EndYear="+(target_enddate/10000)+"&EndMonth="+(target_enddate/100%100)+"&EndDay="+(target_enddate%100)) %>">前年</a>
<%
        }
%>
<%
        if (nextDayLink)
        {
            target_date = Integer.parseInt(nextDayDate);
            target_enddate = Integer.parseInt(nextEndDayDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Range&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)+ "&EndYear="+(target_enddate/10000)+"&EndMonth="+(target_enddate/100%100)+"&EndDay="+(target_enddate%100)) %>">翌日</a>
<%
        }
%>
<%
        if (nextWeekLink)
        {
            target_date = Integer.parseInt(nextWeekDate);
            target_enddate = Integer.parseInt(nextEndWeekDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Range&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)+ "&EndYear="+(target_enddate/10000)+"&EndMonth="+(target_enddate/100%100)+"&EndDay="+(target_enddate%100)) %>">翌週</a>
<%
        }
%>
<%
        if (nextMonthLink)
        {
            target_date = Integer.parseInt(nextMonthDate);
            target_enddate = Integer.parseInt(nextEndMonthDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Range&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)+ "&EndYear="+(target_enddate/10000)+"&EndMonth="+(target_enddate/100%100)+"&EndDay="+(target_enddate%100)) %>">翌月</a>
<%
        }
%>
<%
        if (nextYearLink)
        {
            target_date = Integer.parseInt(nextYearDate);
            target_enddate = Integer.parseInt(nextEndYearDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Range&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)+ "&EndYear="+(target_enddate/10000)+"&EndMonth="+(target_enddate/100%100)+"&EndDay="+(target_enddate%100)) %>">翌年</a></li>
<%
        }
    }
%>
</ul>
<div class="form" align="center">
<form action="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>" method="post">
日付を選択してください。<br>
<%= tagyear %><br>
<%= tagmonth %><br>
<%= tagday %><br>
~<br>
<%= tagendyear %><br>
<%= tagendmonth %><br>
<%= tagendday %><br>
<input type="hidden" name="Range">
<input type="submit" value="決定" id="button">
</form>
</div>
<hr class="border" />
