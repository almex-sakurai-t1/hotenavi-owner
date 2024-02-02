<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if    (selecthotel == null) selecthotel = "all_manage";

    //管理店舗一覧からのリンク
    String param_hotelid  = ReplaceString.getParameter(request,"HotelIdfromGroup");
    if (param_hotelid != null)
    {
        selecthotel = param_hotelid;
    }

    int start_year  = ownerinfo.SalesGetStartDate / 10000;
    int start_month = ownerinfo.SalesGetStartDate / 100 % 100;
    int start_day   = ownerinfo.SalesGetStartDate % 100;
    int end_year  = 0;
    int end_month = 0;
    int end_day   = 0;

    if( ownerinfo.SalesGetEndDate != 0 )
    {
        end_year  = ownerinfo.SalesGetEndDate / 10000;
        end_month = ownerinfo.SalesGetEndDate / 100 % 100;
        end_day   = ownerinfo.SalesGetEndDate % 100;
    }
    int compare_start_year  = start_year;
    int compare_start_month = start_month;
    int compare_start_day   = start_day;
    int compare_end_year    = 0;
    int compare_end_month   = 0;
    int compare_end_day     = 0;

    String param_compare_year     = ReplaceString.getParameter(request,"CompareStartYear");
    String param_compare_month    = ReplaceString.getParameter(request,"CompareStartMonth");
    String param_compare_day      = ReplaceString.getParameter(request,"CompareStartDay");
    String param_compare_endyear  = ReplaceString.getParameter(request,"CompareEndYear");
    String param_compare_endmonth = ReplaceString.getParameter(request,"CompareEndMonth");
    String param_compare_endday   = ReplaceString.getParameter(request,"CompareEndDay");
    if (param_compare_year != null)
        compare_start_year  = Integer.parseInt(param_compare_year);
    if (param_compare_month != null)
        compare_start_month = Integer.parseInt(param_compare_month);
    if (param_compare_day  != null)
        compare_start_day   = Integer.parseInt(param_compare_day);
    if (param_compare_endyear != null)
        compare_end_year    = Integer.parseInt(param_compare_endyear);
    if (param_compare_endmonth != null)
        compare_end_month   = Integer.parseInt(param_compare_endmonth);
    if (param_compare_endday != null)
        compare_end_day     = Integer.parseInt(param_compare_endday);
%>
<%
    int compare_start_date = compare_start_year * 10000 + compare_start_month * 100 + compare_start_day;
    int compare_end_date   = compare_end_year * 10000   + compare_end_month * 100   + compare_end_day;


    ownerinfo.SalesGetStartDate       = compare_start_date;
    ownerinfo.SalesGetEndDate         = compare_end_date;
    ownerinfo.SalesDetailGetStartDate = compare_start_date;
    ownerinfo.SalesDetailGetEndDate   = compare_end_date;
    ownerinfo.ManualSalesDetailGetStartDate = compare_start_date;
    ownerinfo.ManualSalesDetailGetEndDate   = compare_end_date;
    ownerinfo.sendPacket0102(1, selecthotel);

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int  hostkind = 0;
    int  newsales = 0;
    try
    {
        connection  = DBConnection.getConnection();
        final String query   = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if (result.next())
        {
            hostkind  = result.getInt("host_kind");
            newsales  = result.getInt("host_detail");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

    // 新シリでかつ新シリ帳票用モジュールだった場合は0160電文送信
    if(newsales == 1 )
    {
        ownerinfo.sendPacket0160(1, selecthotel);
    }
    else
    {
        ownerinfo.sendPacket0104(1, selecthotel);
    }
%>
<%= compare_start_year %>年<%= compare_start_month %>月<%if( compare_start_day != 0 ){%><%= compare_start_day %>日
<%
    }
    if( compare_end_year != 0 )
    {
%>
～
<%= compare_end_year %>年<%= compare_end_month %>月<%= compare_end_day %>日
<%
    }
%>
計上分
<%
    if ( compare_start_day != 0  && compare_end_day == 0)
    {
        //料金モードの取得
        ownerinfo.CalGetDate = compare_start_year * 100 + compare_start_month;
        ownerinfo.sendPacket0142(1, selecthotel);
%>
<span style="position:relative;left:150px;">[料金モード：<span style="margin-right:5px"><%=ownerinfo.CalDayModeName[compare_start_day-1]%>]</span>
<%
    }
%>
