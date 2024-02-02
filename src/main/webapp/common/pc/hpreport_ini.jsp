<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    DateEdit  de   = new DateEdit();
    int  nowdate   = Integer.parseInt(de.getDate(2));
    nowdate        = de.addDay(nowdate,-1);
    int  now_year  = nowdate / 10000;
    int  now_month = nowdate / 100 % 100;
    int  now_day   = nowdate % 100;

    String Year  = ReplaceString.getParameter(request,"Year");
    if (Year    == null)   Year  = Integer.toString(now_year);
    String Month = ReplaceString.getParameter(request,"Month");
    if (Month   == null)   Month = Integer.toString(now_month);
    String Day   = ReplaceString.getParameter(request,"Day");
    if (Day     == null)   Day   = "0";
    String[] dates = new String[]{Year, Month, Day};
    for(String date : dates)
    {
        if(date != null && !CheckString.numCheck(date))
        {
            response.sendError(400);
            return;
        }
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

    String Type  = ReplaceString.getParameter(request,"Type");
    if (Type    == null)  Type   = "0";
    if( !CheckString.numCheck(Type) )
    {
        response.sendError(400);
        return;
    }
    int    type  = Integer.parseInt(Type);

    String[] Title    = new String[5];
    String[] Contents = new String[5];

    if (month != 0)
    {
        Title   [0] = "日別アクセス数";
        Contents[0] = "hpreport_daily.jsp";
    }
    else
    {
        Title   [0] = "月別アクセス数";
        Contents[0] = "hpreport_monthly.jsp";
    }

    Title   [1] = "曜日別アクセス数";
    Contents[1] = "hpreport_weekly.jsp";

    Title   [2] = "時間帯別アクセス数";
    Contents[2] = "hpreport_time.jsp";

    Title   [3] = "ビジターページ";
    Contents[3] = "hpreport_visitor.jsp";

    Title   [4] = "メンバーページ";
    Contents[4] = "hpreport_member.jsp";

    String Max  = ReplaceString.getParameter(request,"Max");
    if (Max    == null)  Max   = "0";
%>
