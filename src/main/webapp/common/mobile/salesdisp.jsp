<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page errorPage="error.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>
<%@ include file="checkHotelId.jsp" %>
<%
    String loginHotelId =  (String)session.getAttribute("HotelId");
    String hotelid;

    boolean    HotelIdCheck = true;

    // ホテルIDのセット
    hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,1);
    }
    String day   = request.getParameter("Day");
    String month = request.getParameter("Month");
    String year  = request.getParameter("Year");
    String endyear  = request.getParameter("EndYear");
    String endmonth = request.getParameter("EndMonth");
    String endday   = request.getParameter("EndDay");
    String[] dates = new String[]{day, month, year, endyear, endmonth, endday};
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
<title>売上情報(<%=hotelid%>)<%if(day==null){%>本日分<%}else if(day.equals("-1")){%>前回計上日分<%}else if(month == null){%>本月分<%}else{%><%=year%>/<%=month%><%if(!day.equals("0")){%>/<%=day%><%}%><%if( endyear != null && endmonth != null && endday != null){%>～<%=endyear%>/<%=endmonth%>/<%=endday%><%}%>分<%}%></title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
%>
<%-- ここで売上を取得します --%>
<jsp:include page="salesget.jsp" flush="true" />
<%-- ここで売上を取得します（ここまで） --%>
☆売上情報<%if(day==null){%><font size=1>（本日分）</font><%}else if(day.equals("-1")){%><font size=1>（前回計上日分）</font><%}else if(month == null){%><font size=1>（本月分）</font><%}%><br>
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
<%-- 売上データの表示 --%>
<jsp:include page="salesdispdata.jsp" flush="true"/>
<%-- 売上データの表示ここまで --%>
<%
        }
    }
    else
    {
%>
管理店舗ではありません。
<%
    }
%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
