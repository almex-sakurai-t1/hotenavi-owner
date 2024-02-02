<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    String    year;
    String    month;
    String    day;
    int       ymd;
    int       today_year;
    int       today_month;
    int       today_day;
    int       today_ymd;
    int       int_startyear = 0;
    int       int_startmonth = 0;
    int       int_startday = 0;

    year  = ReplaceString.getParameter(request,"Year");
    month = ReplaceString.getParameter(request,"Month");
    day   = ReplaceString.getParameter(request,"Day");
    String[] dates = new String[]{ year, month, day };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    Calendar    calnd;

    // 本日の日付取得
    calnd = Calendar.getInstance();
    today_year  = calnd.get(calnd.YEAR);
    today_month = calnd.get(calnd.MONTH) + 1;
    today_day   = calnd.get(calnd.DATE);
    today_ymd   = today_year * 10000 + today_month * 100 + today_day;



    if( year != null && month != null && day != null )
    {
        ymd = (Integer.valueOf(year).intValue() * 10000) + (Integer.valueOf(month).intValue() * 100) + Integer.valueOf(day).intValue();
    }
    else
    {
        // 日付が設定されていない場合は現在計上日
        ymd = ownerinfo.Addupdate;
    }

    if( ymd != 0 )
    {
        int_startyear  = ymd / 10000;
        int_startmonth = (ymd / 100 % 100) - 1;
        int_startday   = ymd % 100;
    }


    int[]     last_day = new int[12];
    last_day[0]  = 31;
    last_day[1]  = 28;
    if (((int_startyear % 4 == 0) && (int_startyear % 100 != 0)) || (int_startyear % 400 == 0))
        {
        last_day[1] = 29;
        }
    else
        {
        last_day[1] = 28;
        }
    last_day[2]  = 31;
    last_day[3]  = 30;
    last_day[4]  = 31;
    last_day[5]  = 30;
    last_day[6]  = 31;
    last_day[7]  = 31;
    last_day[8]  = 30;
    last_day[9]  = 31;
    last_day[10]  = 30;
    last_day[11]  = 31;
   if( ymd > today_ymd )
    {
    ownerinfo.RoomHistoryDate = 0;
    ownerinfo.InOutGetStartDate = 0;
    ownerinfo.InOutGetEndDate   = 0;
%>
先の日付は入力できません<br>
（データ未取得）
<hr class="border">
<%
    }
    else     if(int_startday > last_day[int_startmonth])
    {
    ownerinfo.RoomHistoryDate = 0;
    ownerinfo.InOutGetStartDate = 0;
    ownerinfo.InOutGetEndDate   = 0;
%>
日付を正しく入力してください<br>
（データ未取得）
<hr class="border">
<%
    }
    else
    {
    ownerinfo.RoomHistoryDate = ymd;
    ownerinfo.InOutGetStartDate = ymd;
    ownerinfo.InOutGetEndDate   = 0;
    }
%>
