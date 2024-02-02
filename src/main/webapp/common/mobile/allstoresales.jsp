<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }

    NumberFormat    nf;
    nf = new DecimalFormat("00");
    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String Range   = request.getParameter("Range");
    if (Range != null)
    {
        RangeFlag = true; 
    }
    String day   = request.getParameter("Day");
    if (day != null)
    {
      if(day.equals("0")) 
      {
          MonthFlag = true; 
      }
    }
    String month   = request.getParameter("Month");
    String year  = request.getParameter("Year");
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
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>管理店舗情報(売上)<%if(day==null){%>本日分<%}else if(day.equals("-1")){%>前回計上日分<%}else if(month == null){%>本月分<%}else{%><%=year%>/<%=month%><%if(!day.equals("0")){%>/<%=day%><%}%><%if( endyear != null && endmonth != null && endday != null){%>～<%=endyear%>/<%=endmonth%>/<%=endday%><%}%>分<%}%></title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
管理店舗売上情報<%if(day==null){%><font size=1>（本日分）</font><%}else if(day.equals("-1")){%><font size=1>（前回計上日分）</font><%}else if(month == null){%><font size=1>（本月分）</font><%}%><br>
<%-- 管理店舗売上情報取得 --%>
<jsp:include page="allstoresalesget.jsp" flush="true" />
<%-- 管理店舗売上情報取得ここまで --%>
<%
    if( ownerinfo.SalesGetStartDate == 0 )
    {
%>
取得できませんでした<br>
<%
    }
    else
    {
%>
<%= ownerinfo.SalesGetStartDate / 10000 %>/<%= nf.format(ownerinfo.SalesGetStartDate / 100 % 100) %><%if(!MonthFlag){%>/<%= nf.format(ownerinfo.SalesGetStartDate % 100) %><%}%><%
        if (RangeFlag && ownerinfo.SalesGetEndDate != ownerinfo.SalesGetStartDate)
        {
%>～<%= ownerinfo.SalesGetEndDate / 10000 %>/<%= nf.format(ownerinfo.SalesGetEndDate / 100 % 100) %>/<%= nf.format(ownerinfo.SalesGetEndDate % 100) %>
<%
        }
%>計上分<font size=1>[<a href="#DateSelect"><%if(MonthFlag){%>年月<%}else{%>日付<%}%>選択</a>]</font><br>
<hr>
<%-- 管理店舗売上情報表示 --%>
<jsp:include page="allstoresalesdispdata.jsp" flush="true" />
<%-- 管理店舗売上情報表示ここまで --%>
<hr>
<a name="DateSelect"></a>
<% 
        if(MonthFlag)
        {
%>
<%-- 年月選択画面表示 --%>
<jsp:include page="allstoreselectmonth.jsp" flush="true" />
<%-- 年月選択画面表示ここまで --%>
<%
        }
        else if (RangeFlag)
        {
%>
<%-- 日付範囲選択画面表示 --%>
<jsp:include page="allstoreselectrange.jsp" flush="true" />
<%-- 日付範囲選択画面表示ここまで --%>
<%
        }
        else
        {
%>
<%-- 日付選択画面表示 --%>
<jsp:include page="allstoreselectday.jsp" flush="true" />
<%-- 日付選択画面表示ここまで --%>
<%
        }
%>
<%
    }
%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
