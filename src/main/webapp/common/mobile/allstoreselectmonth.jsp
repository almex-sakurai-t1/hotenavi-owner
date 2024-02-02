<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%><%@ page import="com.hotenavi2.common.*" %>
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
    cal_date = (cal_date / 100)*100+1;


    String paramDate        = Integer.toString(cal_date);
    String nextMonthDate    = getNext1MDateString(paramDate);
    String beforeMonthDate  = getBefore1MDateString(paramDate);
    String nextYearDate     = getNext1YDateString(paramDate);
    String beforeYearDate   = getBefore1YDateString(paramDate);

    int min_date            = 20050101; // 最小日付
    min_date                = Integer.parseInt(getMinDateString(Integer.toString(addup_date))); //5年前の日付

    //リンクを無効チェック
    boolean beforeYearLink  = true;
    boolean beforeMonthLink = true;
    boolean nextYearLink    = true;
    boolean nextMonthLink   = true;

    if (Integer.parseInt(beforeYearDate)/100 < min_date/100)
    {
       beforeYearLink = false;
    }
    if (Integer.parseInt(beforeMonthDate)/100 <  min_date/100)
    {
       beforeMonthLink = false;
    }
    if (Integer.parseInt(nextYearDate)/100 > addup_date/100)
    {
       nextYearLink = false;
    }
    if (Integer.parseInt(nextMonthDate)/100 > addup_date/100)
    {
       nextMonthLink = false;
    }

    int target_date = 0;
%>
<%
    //日付選択エリアの作成
    int         i;
    String      tagyear;
    String      tagmonth;
    String      tagday;

    tagyear = "<select name=\"Year\">";
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

    tagmonth = "<select name=\"Month\">";
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

    tagday = "<input type=\"hidden\" name=\"Day\" value=\"0\">";
%>
<div align="left">
<%
  if (beforeYearLink)
  {
      target_date = Integer.parseInt(beforeYearDate);
%>
<a href="<%= response.encodeURL("allstoresales.jsp?Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day=0") %>">前年</a>
<%
  }
%>
<%
  if (beforeMonthLink)
  {
      target_date = Integer.parseInt(beforeMonthDate);
%>
<a href="<%= response.encodeURL("allstoresales.jsp?Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day=0") %>">前月</a>
<%
  }
%>
<%
  if (nextMonthLink)
  {
      target_date = Integer.parseInt(nextMonthDate);
%>
<a href="<%= response.encodeURL("allstoresales.jsp?Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day=0") %>">翌月</a>
<%
  }
%>
<%
  if (nextYearLink)
  {
      target_date = Integer.parseInt(nextYearDate);
%>
<a href="<%= response.encodeURL("allstoresales.jsp?Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day=0") %>">翌年</a>
<%
  }
%>
</div>
<hr>
<div align="center">
<form action="<%= response.encodeURL("allstoresales.jsp") %>" method="post">
日付を選択してください。<br>
<%= tagyear %><br>
<%= tagmonth %><br>
<%= tagday %><br>
<input type="submit" value="決定">
</form>
</div>

