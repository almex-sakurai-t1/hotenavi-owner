<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
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
    int       err_flg = 0;
    boolean   rangeMode = false;
    int addup_date  = ownerinfo.Addupdate;

    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");

    String    hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid != null )
    {
        ownerinfo.HotelId = hotelid;
    }

    Calendar    calnd;

    // 本日の日付取得
    calnd = Calendar.getInstance();
    today_year  = calnd.get(calnd.YEAR);
    today_month = calnd.get(calnd.MONTH) + 1;
    today_day   = calnd.get(calnd.DATE);
    today_ymd   = today_year * 10000 + today_month * 100 + today_day;

    DateEdit  de = new DateEdit();

    year     = ReplaceString.getParameter(request,"Year");
    month    = ReplaceString.getParameter(request,"Month");
    day      = ReplaceString.getParameter(request,"Day");
    String[] dates = new String[]{ year, month, day };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    if( year != null && month != null && day != null )
    {
        ymd = (Integer.valueOf(year).intValue() * 10000) + (Integer.valueOf(month).intValue() * 100) + Integer.valueOf(day).intValue();
    }
    else
    {
        ownerinfo.sendPacket0100(1,hotelid);
        // 日付が設定されていない場合は現在計上日
        ymd = ownerinfo.Addupdate;
    }
%>
<%
    if( ymd != 0 )
    {
        int_startyear  = ymd / 10000;
        int_startmonth = (ymd / 100 % 100) - 1;
        int_startday   = ymd % 100;
    }

    if (day != null)
    {
        if(day.equals("0"))
        {
            ymd = int_startyear * 10000 + (int_startmonth+1) * 100;
        }
        else if(day.equals("-1"))
        {
            ymd = de.addDay(ymd,-1);
        }
    }

    int[]     last_day_s = new int[12];
    last_day_s[0]  = 31;
    last_day_s[1]  = 28;
    if (((int_startyear % 4 == 0) && (int_startyear % 100 != 0)) || (int_startyear % 400 == 0))
        {
        last_day_s[1] = 29;
        }
    else
        {
        last_day_s[1] = 28;
        }
    last_day_s[2]  = 31;
    last_day_s[3]  = 30;
    last_day_s[4]  = 31;
    last_day_s[5]  = 30;
    last_day_s[6]  = 31;
    last_day_s[7]  = 31;
    last_day_s[8]  = 30;
    last_day_s[9]  = 31;
    last_day_s[10]  = 30;
    last_day_s[11]  = 31;

    int min_date            = 20050101; // 最小日付
    min_date                = Integer.parseInt(getMinDateString(Integer.toString(addup_date))); //5年前の日付

    if( ymd < min_date )
    {
        ownerinfo.RoomHistoryDate = 0;
        ownerinfo.InOutGetStartDate = 0;
        ownerinfo.InOutGetEndDate   = 0;
%>
5年前より前の日付は入力できません<br>
（データ未取得）
<hr><%
    }
    else
     if( ymd > today_ymd )
    {
        ownerinfo.RoomHistoryDate = 0;
        ownerinfo.InOutGetStartDate = 0;
        ownerinfo.InOutGetEndDate   = 0;
%>
先の日付は入力できません<br>
（データ未取得）
<hr><%
    }
    else
    if(int_startday > last_day_s[int_startmonth])
    {
        ownerinfo.RoomHistoryDate = 0;
        ownerinfo.InOutGetStartDate = 0;
        ownerinfo.InOutGetEndDate   = 0;
%>
日付を正しく入力してください<br>
（データ未取得）
<hr>
<%
    }
    else
    {
        // 売上取得日付のセット
        ownerinfo.RoomHistoryDate = ymd;
        ownerinfo.InOutGetStartDate = ymd;
        ownerinfo.InOutGetEndDate   = 0;
        // 部屋ｽﾃｰﾀｽ遷移
        ownerinfo.sendPacket0146();
        // INOUT組数
        ownerinfo.sendPacket0106();
    }
%>
