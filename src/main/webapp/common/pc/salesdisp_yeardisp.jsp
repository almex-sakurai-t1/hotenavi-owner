<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if    (selecthotel == null) selecthotel = "all_manage";

    int start_year  = ownerinfo.SalesGetStartDate / 10000;
    int start_month = ownerinfo.SalesGetStartDate / 100 % 100;
    int end_year  = ownerinfo.SalesGetEndDate / 10000;
    int end_month =  ownerinfo.SalesGetEndDate / 100 % 100;

    String param_year     = ReplaceString.getParameter(request,"StartYear");
    String param_month    = ReplaceString.getParameter(request,"StartMonth");
    String param_endyear  = ReplaceString.getParameter(request,"EndYear");
    String param_endmonth = ReplaceString.getParameter(request,"EndMonth");
    String param_year_fromlist  = ReplaceString.getParameter(request,"StartYearfromList");
    String param_month_fromlist = ReplaceString.getParameter(request,"StartMonthfromList");
    if(param_year_fromlist != null)  param_year  = param_year_fromlist;
    if(param_month_fromlist != null) param_month = param_month_fromlist;

    if (param_year != null)
        start_year  = Integer.parseInt(param_year);
    if (param_month != null)
        start_month = Integer.parseInt(param_month);
    if (param_endyear != null)
        end_year    = Integer.parseInt(param_endyear);
    if (param_endmonth != null)
        end_month   = Integer.parseInt(param_endmonth);
%>
<%= start_year %>”N<%= start_month %>ŒŽ
`
<%= end_year %>”N<%= end_month %>ŒŽ
Œvã•ª
