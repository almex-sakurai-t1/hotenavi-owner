<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    ReplaceString rs = new ReplaceString();
%>
<%!
    private static Calendar getCalendar(String currentDate) {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        if (currentDate == null || currentDate.length() != 8) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            int year = Integer.parseInt(currentDate.substring(0, 4));
            int month = Integer.parseInt(currentDate.substring(4, 6)) - 1;
            int date = Integer.parseInt(currentDate.substring(6, 8));
            calendar.set(year, month, date);
        }
        return calendar;
    }
    private static String formatDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        return simpleDateFormat.format(calendar.getTime());
    }
    private static String getCurrentDateString() {
        return formatDate(getCalendar(null));
    }
%>
<%
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    String param_compare          = ReplaceString.getParameter(request,"Compare");
    if (param_compare == null)
    {
        param_compare = "false";
    }
    String paramKind   = ReplaceString.getParameter(request,"Kind");
    if (paramKind == null)
    {
        paramKind = "YEARLIST";
    }

    boolean TotalMode = false;
    if (paramKind.indexOf("LISTR") != -1)
    {
        TotalMode = true;
    }

%>
<%-- 月別売上一覧表示処理 --%>
<%

    int             i;
    StringFormat    sf;
    NumberFormat    nf;

    sf = new StringFormat();
    nf = new DecimalFormat("00");

    int       target_date = 0;
    int       target_day;

    // 現在日付
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;

    // 計上日付
    int cal_date  = ownerinfo.Addupdate;
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;
    if  (cal_date == 0)
    {
        cal_date = now_date;
    }
    int   start_date = cal_date;

    String startYear  = ReplaceString.getParameter(request,"StartYear");
    String startMonth = ReplaceString.getParameter(request,"StartMonth");
    String startDay   = "1";
    if( startYear != null && startMonth != null && startDay != null )
    {
        start_date = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    else if (start_date == 0)
    {
        start_date = now_date;
    }
    String paramDate = Integer.toString(start_date);

    int start_year  = start_date/10000;
    int start_month = start_date/100%100;

    int end_date    = 0;
    String paramEndDate = paramDate;

    String endYear  = ReplaceString.getParameter(request,"EndYear");
    String endMonth = ReplaceString.getParameter(request,"EndMonth");
    if( endYear != null && endMonth != null  )
    {
        end_date = (Integer.valueOf(endYear).intValue() * 10000) + (Integer.valueOf(endMonth).intValue() * 100) + 1;
    }
    if (end_date == 0)
    {
        if (cal_date/100 == start_date/100)
        {
            end_date  = start_date + cal_day - 1;//当月なので計上日付
        }
        else
        {
            end_date  = start_date + getCalendar(paramDate).getActualMaximum(Calendar.DATE) - 1;//月末日付
        }
    }
    int end_year    = end_date/10000;
    int end_month   = end_date/100%100;
    int end_day     = end_date%100;
    paramEndDate    = Integer.toString(end_date);
    DateEdit de = new DateEdit();
    int      imedia_user = 0;
    int      level = 0;
    int      sales_cache_flag = 0;
    String hname = "";

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
     // imedia_user のチェック
    try
    {
        connection  = DBConnection.getConnection();
        final String query = "SELECT owner_user.imedia_user,owner_user.level,owner_user_security.sales_cache_flag"
                           + " FROM owner_user"
                           + " INNER JOIN owner_user_security ON owner_user.hotelid = owner_user_security.hotelid AND owner_user.userid = owner_user_security.userid"
                           + " WHERE owner_user.hotelid = ? AND owner_user.userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
            sales_cache_flag = result.getInt("sales_cache_flag");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    try
    {
        String query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,  hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            hname       = result.getString("name");
            hname       = rs.replaceKanaFull(hname);
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

//１．ダウンロードする時の決まりごととして、以下のソースを書く。
    response.setContentType("application/octetstream");

//２．ダウンロードするファイル名を指定(拡張子をcsvにする)
    String filename = "yearlist_" + hotelid + "_" + start_year + "_" + start_month;
    filename = filename + ".csv";
    response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
//３．ダウンロードするファイルをストリーム化する
    PrintWriter outstream = response.getWriter();

//  タイトル書き込み
    outstream.print("月別売上一覧");
    outstream.print(",,");
    outstream.print(hname);
    outstream.print(",,,");
    outstream.println("");

    outstream.print(start_year);
    outstream.print("年");
    outstream.print(start_month);
    outstream.print("月");
    outstream.print("～");
    outstream.print(end_year);
    outstream.print("年");
    outstream.print(end_month);
    outstream.print("月");
    outstream.print(",,,,");
    outstream.print(de.getDate(1));
    outstream.print(",");
    outstream.println(de.getTime(0));

// タイトル
    outstream.print("日付,");
    outstream.print("売上金額,");
    outstream.print("組数,");
    outstream.print("回転数,");
    outstream.print("客単価,");
    outstream.print("部屋単価");
    outstream.println("");

    int[]    t_target_date     = new int[12];
    int[]    t_SalesTotal      = new int[12];
    int[]    t_SalesTotalCount = new int[12];
    int[]    t_SalesTotalRate  = new int[12];
    int[]    t_SalesTotalPrice = new int[12];
    int[]    t_SalesRoomPrice  = new int[12];
    int[]    t_SalesRoomTotalPrice  = new int[12];
    int      Max               = 0;
    String   paramMax          = ReplaceString.getParameter(request,"Max");
    if      (paramMax         == null)    paramMax = "0";
    Max      = Integer.parseInt(paramMax);
    String   paramData         = "";

    for( i = 0 ; i < 12 ; i++ )
    {
        paramData =  ReplaceString.getParameter(request,"target_date_false_" + i);
        if (paramData == null)   paramData = "0";
        t_target_date[i]       = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotal_false_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotal[i]        = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalCount_false_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalCount[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalRate_false_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalRate[i]    = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalPrice_false_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRoomTotalPrice_false_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRoomTotalPrice[i]   = Integer.parseInt(paramData);
    }

    //累計
    int      sales_total = 0;
    int      sales_total_count =0 ;
    int      room_count  = 0;
    int      date_count = 0 ;
    int      month_count = 0 ;
    int      total_rate = 0;

    boolean      getresult = true;

    try
    {
        for( i = 0 ; i < 12; i++ )
        {
            target_date = start_date + i*100;
            if(target_date/100%100 > 12)
            {
                target_date = target_date + 8800;
            }
            if (target_date > end_date)
            {
                break;
            }
            // 売上取得日付のセット
            ownerinfo.SalesGetStartDate = target_date/100 * 100;
            ownerinfo.SalesGetEndDate  = 0;

            getresult = true;
            if (t_SalesTotal[i] == 0 && t_SalesTotalCount[i]==0)
            {
                if (ownerinfo.SalesGetStartDate < (cal_date/100)*100)
                {
                    getresult = ownerinfo.sendPacket0102(1, hotelid,sales_cache_flag);
                }
                else
                {
                    getresult = ownerinfo.sendPacket0102(1, hotelid);
                }
                if (getresult)
                {
                    t_SalesTotal[i]      = ownerinfo.SalesTotal;
                    t_SalesTotalCount[i] = ownerinfo.SalesTotalCount;
                    t_SalesTotalRate[i]  = ownerinfo.SalesTotalRate;
                    t_SalesTotalPrice[i] = ownerinfo.SalesTotalPrice;
                    t_SalesRoomPrice[i]  = ownerinfo.SalesRoomPrice;
                    t_SalesRoomTotalPrice[i]  = ownerinfo.SalesRoomTotalPrice;
                }
            }

            sales_total        += t_SalesTotal[i];
            sales_total_count  += t_SalesTotalCount[i];

            room_count = 0;
            if (t_SalesRoomTotalPrice[i] != 0)
            {
                room_count    = Math.round((float)t_SalesTotal[i] / (float)t_SalesRoomTotalPrice[i]);
            }
            if(getresult)
            {
                if (t_SalesTotal[i] != 0)
                {
                    month_count++;
                    if (target_date/100 == cal_date/100)
                    {
                        date_count = date_count + cal_date % 100;
                    }
                    else  if (target_date/100 < cal_date/100)
                    {
                        date_count = date_count + DateEdit.getLastDayOfMonth(target_date) % 100;
                    }
                }
            }
            total_rate = 0;
            if (date_count != 0)
            {
               total_rate = Math.round((float)sales_total_count*100 / ((float)date_count * (float)room_count));
            }
            outstream.print(target_date/10000); 
            outstream.print("年"); 
            outstream.print(target_date/100%100); 
            outstream.print("月"); 
            outstream.print(",");
            outstream.print(t_SalesTotal[i]);
            outstream.print(",");
            outstream.print(t_SalesTotalCount[i]);
            outstream.print(",");
            outstream.print((float)t_SalesTotalRate[i] / (float)100);
            outstream.print(",");
            outstream.print(t_SalesTotalPrice[i]);
            outstream.print(",");
            outstream.println(t_SalesRoomTotalPrice[i]);
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
// クローズ
      outstream.close();
    }
%>
 