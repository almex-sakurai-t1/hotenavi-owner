<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%
    // 現在日付
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;

    // 計上日付
    int addup_date  = ownerinfo.Addupdate;
    if (addup_date == 0)
    {
        addup_date = now_date;
    }
    int addup_year  = addup_date / 10000;
    int addup_month = addup_date / 100 % 100;
    int addup_day   = addup_date % 100;

    // 取得日付
    int cal_date  = ownerinfo.SalesGetStartDate;
    if  (cal_date == 0)
    {
        cal_date = addup_date;
    }
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;

    int cal_enddate  = ownerinfo.SalesGetEndDate;
    if  (cal_enddate == 0)
    {
        cal_enddate = cal_date;
    }
    int cal_endyear  = cal_enddate/10000;
    int cal_endmonth = cal_enddate/100%100;
    int cal_endday   = cal_enddate%100;

    String paramDate        = Integer.toString(cal_date);
    String nextMonthDate    = getNextMonthDateString(paramDate);
    String beforeMonthDate  = getBeforeMonthDateString(paramDate);
    String nextYearDate     = getNextYearDateString(paramDate);
    String beforeYearDate   = getBeforeYearDateString(paramDate);
    String nextDayDate      = getNextDayDateString(paramDate);
    String beforeDayDate    = getBeforeDayDateString(paramDate);
    String nextWeekDate     = getNextWeekDateString(paramDate);
    String beforeWeekDate   = getBeforeWeekDateString(paramDate);
    
    String paramEndDate     = Integer.toString(cal_enddate);
    String beforeEndMonthDate = getBeforeMonthDateString(paramEndDate);
    String beforeEndYearDate  = getBeforeYearDateString(paramEndDate);
    String beforeEndDayDate   = getBeforeDayDateString(paramEndDate);
    String beforeEndWeekDate  = getBeforeWeekDateString(paramEndDate);
    String nextEndMonthDate = getNextMonthDateString(paramEndDate);
    String nextEndYearDate  = getNextYearDateString(paramEndDate);
    String nextEndDayDate   = getNextDayDateString(paramEndDate);
    String nextEndWeekDate  = getNextWeekDateString(paramEndDate);

    int min_date            = 20050101; // 最小日付
    min_date                = Integer.parseInt(getMinDateString(Integer.toString(addup_date))); //5年前の日付

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
%>
<%
    //日付選択エリアの作成
    int         i;
    String      tagyear;
    String      tagmonth;
    String      tagday;
    String      tagendyear;
    String      tagendmonth;
    String      tagendday;

    tagyear = "<select id=\"year\" onchange=\"document.form1.Year.value=this.value\">";
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

    tagmonth = "<select id=\"month\" onchange=\"document.form1.Month.value=this.value\">";
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

    tagday = "<select id=\"day\" onchange=\"document.form1.Day.value=this.value\">";
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
    tagendyear = "<select id=\"endyear\" onchange=\"document.form1.EndYear.value=this.value\">";
    for( i = -3 ; i < 2 ; i++ )
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

    tagendmonth = "<select id=\"endmonth\" onchange=\"document.form1.EndMonth.value=this.value\">";
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

    tagendday = "<select id=\"endday\" onchange=\"document.form1.EndDay.value=this.value\">";
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

%>
<ul class="link_detail">
<%
    if (ownerinfo.SalesGetStartDate != ownerinfo.SalesGetEndDate)
    {
%>

<%
        if (beforeDayLink)
        {
            target_date    = Integer.parseInt(beforeDayDate);
            target_enddate = Integer.parseInt(beforeEndDayDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_enddate%>);">前日</a></li>
<%
        }
%>
<%
        if (beforeWeekLink)
        {
            target_date = Integer.parseInt(beforeWeekDate);
            target_enddate = Integer.parseInt(beforeEndWeekDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_enddate%>);">前週</a></li>
<%
        }
%>
<%
        if (beforeMonthLink)
        {
            target_date = Integer.parseInt(beforeMonthDate);
            target_enddate = Integer.parseInt(beforeEndMonthDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_enddate%>);">前月</a></li>
<%
        }
%>
<%
        if (beforeYearLink)
        {
            target_date = Integer.parseInt(beforeYearDate);
            target_enddate = Integer.parseInt(beforeEndYearDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_enddate%>);">前年</a></li>
<%
        }
%>

<%
        if (nextDayLink)
        {
            target_date = Integer.parseInt(nextDayDate);
            target_enddate = Integer.parseInt(nextEndDayDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_enddate%>);">翌日</a></li>
<%
        }
%>
<%
        if (nextWeekLink)
        {
            target_date = Integer.parseInt(nextWeekDate);
            target_enddate = Integer.parseInt(nextEndWeekDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_enddate%>);">翌週</a></li>
<%
        }
%>
<%
        if (nextMonthLink)
        {
            target_date = Integer.parseInt(nextMonthDate);
            target_enddate = Integer.parseInt(nextEndMonthDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_enddate%>);">翌月</a></li>
<%
        }
%>
<%
        if (nextYearLink)
        {
            target_date = Integer.parseInt(nextYearDate);
            target_enddate = Integer.parseInt(nextEndYearDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_enddate%>);">翌年</a></li>
<%
        }
    }
%>
</ul>
<div align="center" class="form">
日付を選択してください。<br>
<%= tagyear %><br>
<%= tagmonth %><br>
<%= tagday %><br>
～<br>
<%= tagendyear %><br>
<%= tagendmonth %><br>
<%= tagendday %><br>
<input type="hidden" name="Range">
<input type="submit" value="決定" id="button">
</div>
