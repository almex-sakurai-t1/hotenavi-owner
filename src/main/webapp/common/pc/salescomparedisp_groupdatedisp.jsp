<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if    (selecthotel == null) selecthotel = "all_manage";

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
%>
<%= compare_start_year %>”N<%= compare_start_month %>ŒŽ<%if( compare_start_day != 0 ){%><%= compare_start_day %>“ú<%}%>
<%
    if( compare_end_year != 0 )
    {
%>
`
<%= compare_end_year %>”N<%= compare_end_month %>ŒŽ<%= compare_end_day %>“ú
<%
    }
%>
Œvã•ª
