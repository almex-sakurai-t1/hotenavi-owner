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

    String paramDate        = Integer.toString(cal_date);
    String nextMonthDate    = getNextMonthDateString(paramDate);
    String beforeMonthDate  = getBeforeMonthDateString(paramDate);
    String nextYearDate     = getNextYearDateString(paramDate);
    String beforeYearDate   = getBeforeYearDateString(paramDate);
    String nextDayDate      = getNextDayDateString(paramDate);
    String beforeDayDate    = getBeforeDayDateString(paramDate);
    String nextWeekDate     = getNextWeekDateString(paramDate);
    String beforeWeekDate   = getBeforeWeekDateString(paramDate);

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
%>
<%
    //日付選択エリアの作成
    int         i;
    String      tagyear;
    String      tagmonth;
    String      tagday;

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
%>
<ul class="link_detail">
<%
  if (beforeDayLink)
  {
      target_date = Integer.parseInt(beforeDayDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_date%>);">前日</a></li>
<%
  }
%>
<%
  if (beforeWeekLink)
  {
      target_date = Integer.parseInt(beforeWeekDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_date%>);">前週</a></li>
<%
  }
%>
<%
  if (beforeMonthLink)
  {
      target_date = Integer.parseInt(beforeMonthDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_date%>);">前月</a></li>
<%
  }
%>
<%
  if (beforeYearLink)
  {
      target_date = Integer.parseInt(beforeYearDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_date%>);">前年</a></li>
<%
  }
%>

<%
  if (nextDayLink)
  {
      target_date = Integer.parseInt(nextDayDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_date%>);">翌日</a></li>
<%
  }
%>
<%
  if (nextWeekLink)
  {
      target_date = Integer.parseInt(nextWeekDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_date%>);">翌週</a></li>
<%
  }
%>
<%
  if (nextMonthLink)
  {
      target_date = Integer.parseInt(nextMonthDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_date%>);">翌月</a></li>
<%
  }
%>
<%
  if (nextYearLink)
  {
      target_date = Integer.parseInt(nextYearDate);
%>
<li class="some"><a href="#DateSelect" onclick="dateSelect(<%= target_date%>,<%=target_date%>);">翌年</a></li>
<%
  }
%>
</ul>
<div align="center" class="form">
日付を選択してください。<br>
<%= tagyear %><br>
<%= tagmonth %><br>
<%= tagday %><br>
<input type="submit" value="決定" id="button">
</div>

