<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %> 
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- 売上表示処理 --%>
<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }
    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;

    StringFormat    sf;
    sf = new StringFormat();

    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String param_day  = request.getParameter("Day");
    if (param_day != null)
    {
       if(param_day.equals("0")) 
       {
           MonthFlag = true; 
       }
    }
    String param_month = request.getParameter("Month");

    if( ownerinfo.SalesGetStartDate != 0 && ownerinfo.SalesGetEndDate != 0  && ownerinfo.SalesGetEndDate != ownerinfo.SalesGetStartDate)
    {
        RangeFlag = true; 
        // 期間指定されている場合
%>
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %>~<br>
<%= (ownerinfo.SalesGetEndDate / 10000) %>/<%= (ownerinfo.SalesGetEndDate / 100 % 100) %>/<%= (ownerinfo.SalesGetEndDate % 100) %>
<%
    }
    else
    {
        // それ以外
        if( (ownerinfo.SalesGetStartDate % 100) != 0 )
        {
%>
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %>
<%
        }
        else
        {
          MonthFlag = true; 
%>
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>
<%
        }
    }
%>

<h1>計上分<font size=1>[<a href="#DateSelect"><%if(MonthFlag){%>年月<%}else{%>日付<%}%>選択</a>]</font><br></h1>
<%
    int year     = ownerinfo.SalesGetStartDate / 10000;
    int month    = ownerinfo.SalesGetStartDate / 100 % 100;
    int day      = ownerinfo.SalesGetStartDate % 100;
    int endyear  = ownerinfo.SalesGetEndDate / 10000;
    int endmonth = ownerinfo.SalesGetEndDate / 100 % 100;
    int endday   = ownerinfo.SalesGetEndDate % 100;

    // ホテルIDのセット
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
    String param = "?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J");
    if (param_month != null)
    {
        param = param + "&Year=" + year + "&Month=" + month;
    }
    if (MonthFlag)
    {
        param = param + "&Day=0";
    }
    else
    {
        param = param + "&Day=" + day;
    }
    if (RangeFlag)
    {
        param = param + "&EndYear=" + endyear + "&EndMonth=" + endmonth + "&EndDay=" + endday + "&Range";
    }
%>
<!-- 売上表示 -->

<h1>売上</h1>

<pre>
☆売上
 項目       金額
精(現)<%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTex), 10 ) %>
精(ｸﾚ)<%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTexCredit), 10 ) %>
ﾌﾛ(現)<%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFront), 10 ) %>
ﾌﾛ(ｸﾚ)<%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFrontCredit), 10 ) %>
----------------
合計
<%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTotal), 10 ) %>
</pre>
<div align="right">
<ul class="link">
<li><a href="<%= response.encodeURL("salesdetail.jsp" + param) %>">売上詳細を見る</a></li>
</ul>
</div>

<!-- 売上表示ここまで -->
　<br>
<!-- 組数表示 -->

<h1>組数</h1>

<pre>
☆組数
 項目       組数
休憩<%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesRestCount), 10) %>組
宿泊<%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesStayCount), 10) %>組
<%
    if( ownerinfo.SalesNowCheckin != 999 )
    {
%>
現在入室<%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesNowCheckin), 6) %>組
<%
    }
%>
----------------
退室済合計<%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesTotalCount), 4) %>組
----------------
回転数　　　　<%=(float)ownerinfo.SalesTotalRate / (float)100 %>
客単価<%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesTotalPrice),12) %>
<%
    if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
	{
%>室単価平均<%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %>
室単価累計<%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomTotalPrice),8) %>
<%
	}
	else
	{
%>部屋単価<%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),10) %>
<%
	}
%>
</pre>

<div align="right">
<ul class="link">
<li><a href="<%= response.encodeURL("salesinout.jsp" + param) %>">IN/OUT組数を見る</a></li>
</ul>
</div>


<a name="DateSelect"></a>
<% 
    if(MonthFlag)
    {
%>
<%-- 年月選択画面表示 --%>
<jsp:include page="salesselectmonth.jsp" flush="true" />
<%-- 年月選択画面表示ここまで --%>
<%
    }
    else if (RangeFlag)
    {
%>
<%-- 日付範囲選択画面表示 --%>
<jsp:include page="salesselectrange.jsp" flush="true" />
<%-- 日付範囲選択画面表示ここまで --%>
<%
    }
    else
    {
%>
<%-- 日付選択画面表示 --%>
<jsp:include page="salesselectday.jsp" flush="true" />
<%-- 日付選択画面表示ここまで --%>
<%
    }
%>

<!-- 組数表示ここまで -->
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="jumpurl" value="salesdisp.jsp" />
  <jsp:param name="dispname" value="売上情報" />
</jsp:include>


