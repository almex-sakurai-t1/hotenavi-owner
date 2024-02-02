<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int compare_start_year  = ownerinfo.SalesGetStartDate / 10000;
    int compare_start_month = ownerinfo.SalesGetStartDate / 100 % 100;
    int compare_end_year    = 0;
    int compare_end_month   = 0;

    String param_compare_year     = ReplaceString.getParameter(request,"CompareStartYear");
    String param_compare_month    = ReplaceString.getParameter(request,"CompareStartMonth");
    String param_compare_endyear  = ReplaceString.getParameter(request,"CompareEndYear");
    String param_compare_endmonth = ReplaceString.getParameter(request,"CompareEndMonth");
    if (param_compare_year != null)
        compare_start_year  = Integer.parseInt(param_compare_year);
    if (param_compare_month != null)
        compare_start_month = Integer.parseInt(param_compare_month);
    if (param_compare_endyear != null)
        compare_end_year    = Integer.parseInt(param_compare_endyear);
    if (param_compare_endmonth != null)
        compare_end_month   = Integer.parseInt(param_compare_endmonth);
%><%= compare_start_year %>”N<%= compare_start_month %>ŒŽ
`
<%= compare_end_year %>”N<%= compare_end_month %>ŒŽ
Œvã•ª
