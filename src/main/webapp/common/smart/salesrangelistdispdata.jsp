<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%!
    private static Calendar getCalendar(String currentDate)
    {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        if (currentDate == null || currentDate.length() != 8)
        {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        else
        {
            int year = Integer.parseInt(currentDate.substring(0, 4));
            int month = Integer.parseInt(currentDate.substring(4, 6)) - 1;
            int date = Integer.parseInt(currentDate.substring(6, 8));
            calendar.set(year, month, date);
        }
        return calendar;
    }

    private static String formatDate(Calendar calendar)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        return simpleDateFormat.format(calendar.getTime());
    }

    private static String getCurrentDateString()
    {
        return formatDate(getCalendar(null));
    }
    
    private static ArrayList<String> getDateList(Calendar calendar,Calendar calendar2)
    {
        ArrayList<String> list = new ArrayList<String>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        int dateInt2 =   Integer.parseInt(simpleDateFormat.format(calendar2.getTime()));
        while (dateInt2 >=   Integer.parseInt(simpleDateFormat.format(calendar.getTime())))
        {
            list.add(simpleDateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        return list;
    }
%>
<%-- 日別売上一覧表示処理 --%>
<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }

    String[] arrWday = {"日", "月", "火", "水", "木", "金", "土"};

    int          i;
    StringFormat sf;
    NumberFormat nf;

    sf = new StringFormat();
    nf = new DecimalFormat("00");

    //月合計
    int salestotal = 0;
    int salestotalcount =0 ;
    String year     = request.getParameter("Year");
    String month    = request.getParameter("Month");
    String day      = request.getParameter("Day");
    String endyear  = request.getParameter("EndYear");
    String endmonth = request.getParameter("EndMonth");
    String endday   = request.getParameter("EndDay");
    String[] dates = new String[]{ year, month, day, endyear, endmonth, endday };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    int start_year  = ownerinfo.SalesGetStartDate / 10000;
    int start_month = ownerinfo.SalesGetStartDate / 100 % 100;
    int start_day   = ownerinfo.SalesGetStartDate % 100;
    int start_ymd   = start_year * 10000 + start_month * 100 + start_day;
    int end_year    = ownerinfo.SalesGetEndDate / 10000;
    int end_month   = ownerinfo.SalesGetEndDate / 100 % 100;
    int end_day     = ownerinfo.SalesGetEndDate % 100;
    int end_ymd     = end_year * 10000 + end_month * 100 + end_day;

    if( year != null && month != null && day != null )
    {
        start_ymd = (Integer.valueOf(year).intValue() * 10000) + (Integer.valueOf(month).intValue() * 100) + Integer.valueOf(day).intValue();
    }
    if( endyear != null && endmonth != null && endday != null )
    {
        end_ymd   = (Integer.valueOf(endyear).intValue() * 10000) + (Integer.valueOf(endmonth).intValue() * 100) + Integer.valueOf(endday).intValue();
    }

    ArrayList<String> list = getDateList(getCalendar(Integer.toString(start_ymd)),getCalendar(Integer.toString(end_ymd)));
%>
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %>~<br>
<%= (ownerinfo.SalesGetEndDate / 10000) %>/<%= (ownerinfo.SalesGetEndDate / 100 % 100) %>/<%= (ownerinfo.SalesGetEndDate % 100) %>
計上分<font size=1>[<a href="#DateSelect">日付選択</a>]</font><br>
<%
    // ホテルIDのセット
    String hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
%>
<hr class="border">
<%-- 月計日別売上一覧表示 --%>
<pre>
　日　　売上金額　 組数</pre>
<%
    for( i = 0 ; i < list.size(); i++ )
    {
        int target_date = Integer.parseInt(list.get(i));
        // 曜日の算出        
        int target_year  = target_date/10000;
        int target_month = target_date/100%100;
        int target_day   = target_date%100;
        int cal_year     = target_year;
        int cal_month    = target_month;
        if(cal_month == 1 || cal_month == 2) {
            cal_year--;
            cal_month += 12;
        }
        int wday = (cal_year + (int)(cal_year/4) - (int)(cal_year/100) + (int)(cal_year/400) + (int)((13*cal_month+8)/5) + target_day) % 7;

        // 売上取得日付のセット
        ownerinfo.SalesGetStartDate = target_date;
        ownerinfo.SalesGetEndDate = target_date;
        if (DemoMode)
        {
            float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;
            ownerinfo.SalesTotal      = (int)(525000*per);
            ownerinfo.SalesTotalCount = (int)(60*per);
        }
        else
        {
            // 売上情報取得
            ownerinfo.sendPacket0102(1, hotelid);
        }
        salestotal      += ownerinfo.SalesTotal;
        salestotalcount += ownerinfo.SalesTotalCount;
%>
<pre><a href="<%= response.encodeURL("salesdisp.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year=" + target_year + "&Month=" + target_month + "&Day=" + target_day) %>"><%= sf.rightFitFormat( Integer.toString(target_month), 2) %>/<%= sf.rightFitFormat( Integer.toString(target_day), 2) %><font size=1>(<%= arrWday[wday] %>)</font></a><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTotal), 9 ) %><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesTotalCount), 5) %><font size=1>組</font></pre>
<%
    }
%>
<pre>
<font size=1>合計</font><%= sf.rightFitFormat( Kanma.get(salestotal), 14) %><%= sf.rightFitFormat( Integer.toString(salestotalcount), 5) %><font size=1>組</font>
</pre>
<%-- 表示ここまで --%>
<hr class="border">
<a name="DateSelect"></a>
<%-- 年月選択画面表示 --%>
<jsp:include page="salesselectrange.jsp" flush="true" >
  <jsp:param name="Contents" value="salesrangelistdisp" />
</jsp:include>

<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="salesrangelistdisp.jsp" />
  <jsp:param name="dispname" value="期間指定日別売上" />
</jsp:include>
