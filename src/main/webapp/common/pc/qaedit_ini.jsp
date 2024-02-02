<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    DateEdit  de   = new DateEdit();
    int  nowdate   = Integer.parseInt(de.getDate(2));
//    nowdate        = de.addDay(nowdate,-1);
    int  now_year  = nowdate / 10000;
    int  now_month = nowdate / 100 % 100;
    int  now_day   = nowdate % 100;

    String Year  = ReplaceString.getParameter(request,"Year");
    if (Year    == null)   Year  = Integer.toString(now_year);
    String Month = ReplaceString.getParameter(request,"Month");
    if (Month   == null)   Month = Integer.toString(now_month);
    String Day   = ReplaceString.getParameter(request,"Day");
    if (Day     == null)   Day   = Integer.toString(now_day);
    if( !CheckString.numCheck(Year) || !CheckString.numCheck(Month) || !CheckString.numCheck(Day) )
    {
        Year= "0";
        Month= "0";
        Day= "0";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    int year     = Integer.parseInt(Year);
    int month    = Integer.parseInt(Month);
    int day      = Integer.parseInt(Day);
    int from_date = year * 10000 + month * 100 + day;
    int to_date   = year * 10000 + month * 100 + day;
    if (month == 0)
    {
        from_date = year * 10000 + 101;
        to_date   = year * 10000 + 1231;
    }
    else if (day == 0)
    {
        to_date   = year * 10000 + month * 100 + 31;
    }
%>
