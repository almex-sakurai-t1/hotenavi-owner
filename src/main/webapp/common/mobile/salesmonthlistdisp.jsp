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
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
        {
            response.sendError(400);
            return;
        }
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,1);
    }
    String year  = request.getParameter("Year");
    String month = request.getParameter("Month");
    String[] dates = new String[]{ year, month };
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
<title>月次日別一覧(<%=hotelid%>)<%if(month == null){%>本月分<%}else{%><%=year%>/<%=month%>分<%}%></title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
        if (ownerinfo.Addupdate == 0)
        {
            ownerinfo.sendPacket0100(1,hotelid);
        }
%>
<%-- ここで売上を取得します（とりあえず月合計を取得するため）--%>
<jsp:include page="salesget.jsp" flush="true" />
<%-- ここで売上を取得します（ここまで） --%>

☆月次日別売上<%if(month == null){%><font size=1>（本月分）</font><%}%><br>
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
<%-- 日別売上一覧数データの表示 --%>
<jsp:include page="salesmonthlistdispdata.jsp" flush="true" />
<%-- 日別売上一覧数データの表示ここまで --%>
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
