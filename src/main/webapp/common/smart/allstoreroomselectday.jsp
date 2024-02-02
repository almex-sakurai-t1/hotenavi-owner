<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="com.hotenavi2.common.*"%>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%
    String param_count = ReplaceString.getParameter(request,"count");
    if( param_count == null )
    {
        param_count = "3";
    }
    String param_tmcount = ReplaceString.getParameter(request,"tmcount");
    if( param_tmcount == null )
    {
        param_tmcount = "1";
    }
    String param_page = ReplaceString.getParameter(request,"page");
    if( param_page == null )
    {
        param_page = "0";
    }
    int pageno = Integer.parseInt(param_page);
    int dispno = Integer.parseInt(param_count);
    int tmcount = Integer.parseInt(param_tmcount);

    String param_year  = ReplaceString.getParameter(request,"Year");
    String param_month = ReplaceString.getParameter(request,"Month");
    String param_day   = ReplaceString.getParameter(request,"Day");
    String[] dates = new String[]{ param_year, param_month, param_day, param_count, param_tmcount, param_page };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    String Parameter = "?page="+pageno+"&count="+dispno+"&tmcount="+tmcount;


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
    int cal_date  = ownerinfo.InOutGetStartDate;
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
%>
<ul class="link_detail">
<%
    if (ReplaceString.getParameter(request,"count") != null)
    {
%>

<%
        if (beforeDayLink)
        {
            target_date = Integer.parseInt(beforeDayDate);
%>
<li class="some"><a href="<%= response.encodeURL("allstoreroom.jsp"+ Parameter + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">前日</a></li>
<%
        }
%>
<%
        if (beforeWeekLink)
        {
            target_date = Integer.parseInt(beforeWeekDate);
%>
<li class="some"><a href="<%= response.encodeURL("allstoreroom.jsp"+ Parameter + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">前週</a></li>
<%
        }
%>
<%
        if (beforeMonthLink)
        {
            target_date = Integer.parseInt(beforeMonthDate);
%>
<li class="some"><a href="<%= response.encodeURL("allstoreroom.jsp"+ Parameter + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">前月</a></li>
<%
        }
%>
<%
        if (beforeYearLink)
        {
            target_date = Integer.parseInt(beforeYearDate);
%>
<li class="some"><a href="<%= response.encodeURL("allstoreroom.jsp"+ Parameter + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">前年</a></li>
<%
        }
%>

<%
        if (nextDayLink)
        {
            target_date = Integer.parseInt(nextDayDate);
%>
<li class="some"><a href="<%= response.encodeURL("allstoreroom.jsp"+ Parameter + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">翌日</a></li>
<%
        }
%>
<%
        if (nextWeekLink)
        {
            target_date = Integer.parseInt(nextWeekDate);
%>
<li class="some"><a href="<%= response.encodeURL("allstoreroom.jsp"+ Parameter + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">翌週</a></li>
<%
        }
%>
<%
        if (nextMonthLink)
        {
            target_date = Integer.parseInt(nextMonthDate);
%>
<li class="some"><a href="<%= response.encodeURL("allstoreroom.jsp"+ Parameter + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">翌月</a></li>
<%
        }
%>
<%
        if (nextYearLink)
        {
            target_date = Integer.parseInt(nextYearDate);
%>
<li class="some"><a href="<%= response.encodeURL("allstoreroom.jsp"+ Parameter + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day="+(target_date%100)) %>">翌年</a></li>
<%
        }
    }
%>
</ul>
<hr class="border">
<div align="center" class="form">
<form class="form" action="<%= response.encodeURL("allstoreroom.jsp") %>" method="post">
日付を選択してください。<br>
<%= tagyear %><br>
<%= tagmonth %><br>
<%= tagday %><br>
<select name="count">
<option value="3"  <%if(param_count.equals("3")){%>selected<%}%>>3
<option value="5"  <%if(param_count.equals("5")){%>selected<%}%>>5
<option value="99" <%if(param_count.equals("99")){%>selected<%}%>>全て
</select>店舗単位<br>
<select name="tmcount">
<option value="1" <%if(param_tmcount.equals("1")){%>selected<%}%>>1
<option value="2" <%if(param_tmcount.equals("2")){%>selected<%}%>>2
<option value="3" <%if(param_tmcount.equals("3")){%>selected<%}%>>3
</select>時間おき<br>
<input type="submit" value="決定" id="button">
</form>
</div>

