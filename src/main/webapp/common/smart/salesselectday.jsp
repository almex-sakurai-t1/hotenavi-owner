<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }

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
    if (addup_date == 0 || DemoMode)
    {
        addup_date = now_date;
    }
    int addup_year  = addup_date / 10000;
    int addup_month = addup_date / 100 % 100;
    int addup_day   = addup_date % 100;

    // 取得日付
    int cal_date = ownerinfo.SalesGetStartDate;
    if (contents.equals("roomhistory") || contents.equals("salesinout"))
    {
        cal_date = ownerinfo.InOutGetStartDate;
    }
    if (cal_date == 0)
    {
        cal_date = addup_date;
    }
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;

    String paramDate       = Integer.toString(cal_date);
    String nextMonthDate   = getNextMonthDateString(paramDate);
    String beforeMonthDate = getBeforeMonthDateString(paramDate);
    String nextYearDate    = getNextYearDateString(paramDate);
    String beforeYearDate  = getBeforeYearDateString(paramDate);
    String nextDayDate     = getNextDayDateString(paramDate);
    String beforeDayDate   = getBeforeDayDateString(paramDate);
    String nextWeekDate    = getNextWeekDateString(paramDate);
    String beforeWeekDate  = getBeforeWeekDateString(paramDate);

    int min_date = 20050101; // 最小日付
    min_date     = Integer.parseInt(getMinDateString(Integer.toString(addup_date))); //5年前の日付

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
    if (Integer.parseInt(nextYearDate) > addup_date)
    {
        nextYearLink = false;
    }
    if (Integer.parseInt(nextMonthDate) > addup_date)
    {
        nextMonthLink = false;
    }
    if (Integer.parseInt(nextWeekDate) > addup_date)
    {
        nextWeekLink = false;
    }
    if (Integer.parseInt(nextDayDate) > addup_date)
    {
        nextDayLink = false;
    }

    int target_date = 0;

    //日付選択エリアの作成
    int    i;
    String tagyear;
    String tagmonth;
    String tagday;

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

    if (beforeDayLink)
    {
        target_date = Integer.parseInt(beforeDayDate);
%>
<ul class="link_detail">
<li class="some"><a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">前日</a>
<%
    }
%>
<%
    if (beforeWeekLink)
    {
        target_date = Integer.parseInt(beforeWeekDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">前週</a>
<%
    }
%>
<%
    if (beforeMonthLink)
    {
        target_date = Integer.parseInt(beforeMonthDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">前月</a>
<%
    }
%>
<%
    if (beforeYearLink)
    {
        target_date = Integer.parseInt(beforeYearDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">前年</a>
<%
    }
%>
<%
    if (nextDayLink)
    {
        target_date = Integer.parseInt(nextDayDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">翌日</a>
<%
    }
%>
<%
    if (nextWeekLink)
    {
        target_date = Integer.parseInt(nextWeekDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">翌週</a>
<%
    }
%>
<%
    if (nextMonthLink)
    {
        target_date = Integer.parseInt(nextMonthDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">翌月</a>
<%
    }
%>
<%
    if (nextYearLink)
    {
        target_date = Integer.parseInt(nextYearDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">翌年</a></li>
<%
    }
%>
</ul>

<div class="form" align="center">
<form action="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>" method="post">
日付を選択してください。<br>
<%= tagyear %><br>
<%= tagmonth %><br>
<%= tagday %><br>
<input type="submit" value="決定" id="button">
</form>
</div>
<hr class="border" />
