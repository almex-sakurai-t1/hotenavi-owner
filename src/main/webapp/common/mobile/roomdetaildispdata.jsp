<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- 現在部屋詳細情報表示処理 --%>

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    int            i = 0;
    int            now_ymd;
    int            now_time;
    DateEdit        df;
    StringFormat    sf;
    NumberFormat    nf;
 
    // ホテルIDのセット
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }

    df = new DateEdit();
    sf = new com.hotenavi2.common.StringFormat();
    nf = new DecimalFormat("00");

    // 現在日付・時刻取得(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));
%>
<%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>現在<br>
<hr>
<%-- 現在部屋詳細情報表示 --%>
<%
    for( i = 0 ; i < ownerinfo.StatusDetailCount ; i++ )
    {
        if( ownerinfo.StatusDetailRoomCode[i] != 0 )
        {
            if( i != 0 )
            {
%>
<hr>
<%
            }
%>
<a href="<%= response.encodeURL("roominfodetail.jsp?HotelId=" + hotelid + "&rc=" + ownerinfo.StatusDetailRoomCode[i]) %>">
<%= sf.rightFitFormat(ownerinfo.StatusDetailRoomName[i], 4) %></a>
&nbsp;<%= sf.leftFitFormat(ownerinfo.StatusDetailStatusName[i], 12) %>
<%
            if (ownerinfo.StatusDetailUserChargeMode[i] == 1)
            {
%><img src="/common/mobile/image/rest.gif">
<%
            }
            else if (ownerinfo.StatusDetailUserChargeMode[i] == 2)
            {
%><img src="/common/mobile/image/stay.gif">
<%
            }
%><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= ownerinfo.StatusDetailElapseTime[i] / 60 %>:<%= nf.format(ownerinfo.StatusDetailElapseTime[i] % 60) %><br>
<%
        }
    }
%>
<%-- 現在部屋詳細情報表示ここまで --%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="roomdetail.jsp" />
  <jsp:param name="dispname" value="部屋詳細" />
</jsp:include>

