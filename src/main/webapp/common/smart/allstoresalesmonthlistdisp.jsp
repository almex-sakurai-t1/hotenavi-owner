<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

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

    int wday;
    int year;
    int month;
    int mday;
    String[] arrWday = {"日", "月", "火", "水", "木", "金", "土"};

    int             i;
    StringFormat    sf;
    NumberFormat    nf;

    sf = new StringFormat();
    nf = new DecimalFormat("00");

    //月合計
    int      salestotal = 0;
    int      salestotalcount =0 ;

    String   sday;

    int       ymd;

    int       today_year;
    int       today_month;
    int       today_day;
    int       today_ymd;
    int       now_addup;
    int       addup_year;
    int       addup_month;
    int       addup_day;
    String startYear  = request.getParameter("Year");
    String startMonth = request.getParameter("Month");

    Calendar    calnd;

    // 本日の日付取得
    calnd = Calendar.getInstance();
    today_year  = calnd.get(calnd.YEAR);
    today_month = calnd.get(calnd.MONTH) + 1;
    today_day   = calnd.get(calnd.DATE);
    today_ymd   = today_year * 10000 + today_month * 100 + today_day;

    int       start_year  = today_year;
    int       start_month = today_month;
    int       start_day   = today_day;

    if (startYear != null)
    {
        start_year = Integer.parseInt(startYear);
    }
    if (startMonth != null)
    {
        if (!startMonth.equals("0"))
        {
            start_month = Integer.parseInt(startMonth);
        }
    }

    int       start_ymd   = start_year * 10000 + start_month * 100 + start_day;

    int[]     last_day = new int[12];
    last_day[0]  = 31;
    last_day[1]  = 28;
    if (((start_year % 4 == 0) && (start_year % 100 != 0)) || (start_year % 400 == 0))
    {
        last_day[1] = 29;
    }
    else
    {
        last_day[1] = 28;
    }
    last_day[2]   = 31;
    last_day[3]   = 30;
    last_day[4]   = 31;
    last_day[5]   = 30;
    last_day[6]   = 31;
    last_day[7]   = 31;
    last_day[8]   = 30;
    last_day[9]   = 31;
    last_day[10]  = 30;
    last_day[11]  = 31;

    // 現在計上日取得
    now_addup = ownerinfo.Addupdate;
    addup_year  = now_addup / 10000;
    addup_month = now_addup / 100 % 100;
    addup_day   = now_addup % 100;

    //当月の場合は、その最終日
    if (today_year == start_year && today_month == start_month)
    {
        if(addup_day == 0)
        {
            last_day[today_month - 1] = today_day;
        }
        else
        {
            last_day[today_month - 1] = addup_day;
        }
    }
    if (startMonth != null)
    {
%>
<h2><%= start_year %>/<%= start_month %><br />
計上分<font size="-1"><a href="#DateSelect">[年月選択]</a></font></h2>
<hr class="border">
<%-- 月計日別売上一覧表示 --%>
<table class="uriage">
<tr>
<th>日</th><th>売上金額</th><th>組数</th>
</tr>
<%
        for( i = 1 ; i <= last_day[start_month - 1] ; i++ )
        {
            // 曜日の算出
            month = start_month;
            year  = start_year;
            mday  = i;

            if(month == 1 || month == 2) {
                year--;
                month += 12;
            }
            wday = (year + (int)(year/4) - (int)(year/100) + (int)(year/400) + (int)((13*month+8)/5) + mday) % 7;

            // 売上取得日付のセット
            ymd = (start_year * 10000) + (start_month * 100) + i;
            ownerinfo.SalesGetStartDate = ymd;
            ownerinfo.SalesGetEndDate = 0;

            int sales = 0;
            int salescount = 0;

            Connection connection = null;
            PreparedStatement prestate = null;
            ResultSet result = null;
            try
            {
                final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                                   + "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                                   + "AND hotel.plan <= 2 "
                                   + "AND hotel.plan >= 1 "
                                   + "AND owner_user_hotel.sales_disp_flag = 1 "
                                   + "AND owner_user_hotel.userid = ? "
                                   + "ORDER BY hotel.sort_num,hotel.hotel_id";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement(query);
                prestate.setString(1, loginHotelId);
                prestate.setInt(2, ownerinfo.DbUserId);

                result      = prestate.executeQuery();
                Object lock = request.getSession();
                synchronized(lock)
                {
                    while( result.next() )
                    {
                        String hotelid = result.getString("hotel.hotel_id");
                        boolean getresult = ownerinfo.sendPacket0102(1, hotelid,1);
                        if (getresult)
                        {
                            sales             += ownerinfo.SalesTotal;
                            salestotal        += ownerinfo.SalesTotal;
                            salescount        += ownerinfo.SalesTotalCount;
                            salestotalcount   += ownerinfo.SalesTotalCount;
                        }
                    }
                }
            }
            catch( Exception e )
            {
                Logging.error("foward Exception e=" + e.toString(), e);
            }
            finally
            {
                DBConnection.releaseResources(result, prestate, connection);
            }
%><tr>
<td><a href="<%= response.encodeURL("allstoresales.jsp?Year=" + start_year + "&Month=" + start_month + "&Day=" + i) %>"><%= sf.rightFitFormat( Integer.toString(i), 2) %>(<%= arrWday[wday] %>)</a></td><td class="uriage"><%= sf.rightFitFormat( Kanma.get(sales), 10 ) %></td><td class="uriage"><%= sf.rightFitFormat( Integer.toString(salescount), 5) %><font size="-1">組</font></td>
</tr>
<%
        }
%><tr>
<td>合計</td><td class="total"><%= sf.rightFitFormat( Kanma.get(salestotal), 12) %></td><td class="total"><%= sf.rightFitFormat( Integer.toString(salestotalcount), 5) %><font size="-1">組</font></td>
</tr>
</table>
<input type=button value="管理店舗選択" class="changeButton" onclick="document.form1.action='salestargetedit.jsp';document.form1.submit();">
<%
    }
%>
