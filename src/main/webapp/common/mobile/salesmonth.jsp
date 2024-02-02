<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>月計売上(<%=hotelid%>)年月指定</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<jsp:include page="salesget.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
        if (ownerinfo.Addupdate == 0)
        {
            ownerinfo.sendPacket0100(1,hotelid);
        }
        // 現在日付
        String nowDate = getCurrentDateString();
        int now_date   = Integer.parseInt(nowDate);
        // 計上日付
        int addup_date  = ownerinfo.Addupdate;
        if (addup_date == 0)
        {
            addup_date = now_date;
        }
%>
<%-- 月計売上選択 --%>
<a name="monthly"></a>
☆月計売上<br>
<a href="<%= response.encodeURL("salesdisp.jsp?HotelId=" + hotelid + "&Day=0") %>">本月</a>（<%= addup_date / 10000 %>/<%= nf.format(addup_date / 100 % 100) %>）<br>
<%-- 日付選択アイテムを表示します --%>
<jsp:include page="salesselectmonth.jsp" flush="true" />
<%-- 日付選択アイテムを表示します（ここまで） --%>
<%-- 月計売上選択End --%>
<%
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
