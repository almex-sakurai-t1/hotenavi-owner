<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int         i;
    int         now_year;
    int         now_month;
    int         now_day;
    int         now_addup;
    int         addup_year;
    int         addup_month;
    int         addup_day;
    String      tagyear;
    String      tagmonth;
    String      tagday;
    Calendar    cal;

    // 本日の日付取得
    cal = Calendar.getInstance();
    now_year  = cal.get(cal.YEAR);
    now_month = cal.get(cal.MONTH) + 1;
    now_day   = cal.get(cal.DATE);
    // 取得日付
    now_addup = ownerinfo.Addupdate;
    addup_year  = now_addup / 10000;
    addup_month = now_addup / 100 % 100;
    addup_day   = now_addup % 100;

    tagyear = "<select name=\"Year\">";
    for( i = -3 ; i < 2 ; i++ )
    {
        if( now_year + (i-1) == addup_year )
        {
            tagyear = tagyear + "<option value=\"" + (now_year + (i-1)) + "\" selected>" + (now_year + (i-1));
        }
        else
        {
            tagyear = tagyear + "<option value=\"" + (now_year + (i-1)) + "\">" + (now_year + (i-1));
        }
    }
    tagyear = tagyear + "</select>年";

    tagmonth = "<select name=\"Month\">";
    for( i = 0 ; i < 12 ; i++ )
    {
        if( (i + 1) == addup_month )
        {
            tagmonth = tagmonth + "<option value=\"" + (i + 1) + "\" selected>" + (i + 1);
        }
        else
        {
            tagmonth = tagmonth + "<option value=\"" + (i + 1) + "\">" + (i + 1);
        }
    }
    tagmonth = tagmonth + "</select>月";

    tagday = "<select name=\"Day\">";
    for( i = 0 ; i < 31 ; i++ )
    {
        if( (i + 1) == addup_day )
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

<%= tagyear %><br>
<%= tagmonth %><br>
<%= tagday %><br>
<br>

