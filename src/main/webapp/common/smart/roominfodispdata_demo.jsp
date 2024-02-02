<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- 現在部屋情報表示処理 --%>

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    // ホテルIDのセット
    String hotelid = request.getParameter("HotelId");
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }
%>

<%
    int          i;
    int          count;
    int          now_ymd;
    int          add_ymd;
    int          now_time;
    int          starttime = 0;
    DateEdit     df;
    StringFormat sf;
    NumberFormat nf;

	// 計上日付取得
    add_ymd = ownerinfo.Addupdate;
	

    // IN/OUT組数取得（開始時刻取得のため）
    ownerinfo.InOutGetStartDate = add_ymd;
    ownerinfo.InOutGetEndDate = 0;
    ownerinfo.sendPacket0106();

	// 開始時刻
    starttime  = ownerinfo.InOutTime[0] / 100;
 
    df = new DateEdit();
    sf = new StringFormat();

    // 現在日付・時刻取得(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));

    // 開始時刻が０時以外の場合は、現在計上日取得。０時の場合は今日の日付
    if(starttime == 0)
    {
        add_ymd = now_ymd;
    }
    if (DemoMode)
    {
        add_ymd = now_ymd;
    }
%>
<%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>現在<br>
<hr class="border">
<%-- 現在部屋情報表示 --%>
<%
    for( i = 0 ; i < ownerinfo.OWNERINFO_ROOMSTATUSMAX ; i++ )
    {
        if( ownerinfo.StatusCount[i] != 0 )
        {
%>
<pre><%= sf.leftFitFormat(ownerinfo.StatusName[i], 10) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.StatusCount[i]), 3) %></pre>
<%
        }
    }
%>
(
<%
    if( ownerinfo.StatusEmptyFullMode == 1 )
    {
%>
        手動
<%
    }
    else
    {
%>
        自動
<%
    }
%>
/
<%
    if( ownerinfo.StatusEmptyFullState == 1 )
    {
%>
        空室
<%
    }
    else
    {
%>
        満室
<%
     }
%>
)
<%
  if( ownerinfo.StatusWaiting != 0 )
    {
%>
<br>
     ｳｪｲﾃｨﾝｸﾞ:<%= ownerinfo.StatusWaiting %>&nbsp;組
<%
    }
%>
<%-- 現在部屋情報表示ここまで --%>

<hr class="border" />
<ul class="link">
<div align="right">
<li><a href="<%= response.encodeURL("roomdetail.jsp?HotelId="  + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">部屋詳細を見る</a><br>
<a href="<%=     response.encodeURL("closeinout.jsp?HotelId="  + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">締単位IN/OUT組数</a><br>
<a href="<%=     response.encodeURL("salesinout.jsp?HotelId="  + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J") + "&Ymd=" + add_ymd) %>">IN/OUT組数</a><br>
<a href="<%=     response.encodeURL("roomhistory.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">部屋ｽﾃｰﾀｽ遷移</a><br></li>
</div>
</ul>

<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="roominfo.jsp" />
  <jsp:param name="dispname" value="部屋情報" />
</jsp:include>
