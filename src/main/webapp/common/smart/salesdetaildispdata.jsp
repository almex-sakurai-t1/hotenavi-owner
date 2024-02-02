<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- 売上詳細表示処理 --%>
<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }
    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;

    StringFormat sf = new StringFormat();

    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String day   = request.getParameter("Day");
    if (day != null)
    {
      if(day.equals("0")) 
      {
          MonthFlag = true; 
      }
    }

    if( ownerinfo.SalesGetStartDate != 0 && ownerinfo.SalesGetEndDate != 0  && ownerinfo.SalesGetEndDate != ownerinfo.SalesGetStartDate)
    {
        RangeFlag = true; 
        // 期間指定されている場合
%>
<h2><%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %>~<%= (ownerinfo.SalesGetEndDate / 10000) %>/<%= (ownerinfo.SalesGetEndDate / 100 % 100) %>/<%= (ownerinfo.SalesGetEndDate % 100) %></h2>
<%
    }
    else
    {
        // それ以外
        if( (ownerinfo.SalesGetStartDate % 100) != 0 )
        {
%>
<h2><%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %></h2>
<%
        }
        else
        {
          MonthFlag = true; 
%>
<h2><%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %></h2>
<%
        }
    }
%>
<h1>計上分<font size="-1"><a href="#DateSelect">[<%if(MonthFlag){%>年月<%}else{%>日付<%}%>選択]</a></font></h1>
<%
    // ホテルIDのセット
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
        hotelid = ownerinfo.HotelId;

    int hostkind = 0;
    int newsales = 0;
    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT * FROM hotel WHERE hotel_id = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result = prestate.executeQuery();
        if(result.next())
        {
            hostkind = result.getInt("host_kind");
            newsales = result.getInt("host_detail");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }
%>
<hr class="border">
<%-- 売上表示 --%>
<h1>☆売上</h1>
<%if (newsales == 1){%>
<table class="uriage">
<tr>
<th width="50%">項目</th><th>金額</th>
</tr>
<%
for(int i = 0 ; i < ownerinfo.ManualSalesDetailCount ; i++ )
{
    if (!ownerinfo.ManualSalesDetailName[i].equals("") || ownerinfo.ManualSalesDetailAmount[i] != 0 )
    {
%>
<tr>
<td><%=ownerinfo.ManualSalesDetailName[i]%></td><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.ManualSalesDetailAmount[i]), 11 ) %></td>
</tr>
<%
    }
}
%>
<tr class="uriage">
<td>合計</td><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.ManualSalesDetailTotal), 11 ) %></td>
</tr>
<tr>
<td>(内消費税)</td><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.ManualSalesDetailTaxIn), 10 ) %></td>
</tr>
</table>
<%}else{%>
<table class="uriage">
<tr>
<th width="50%">項目</th><th>金額</th>
</tr>
<tr>
<td>宿泊</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(185850*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStay), 11 ) %></td><%}%>
</tr>
<tr>
<td>泊前延長</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(  6750*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStayBeforeOver), 11 ) %></td><%}%>
</tr>
<tr>
<td>泊後延長</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round( 35500*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStayAfterOver), 11 ) %></td><%}%>
</tr>
<tr>
<td>休憩</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(206600*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRest), 11 ) %></td><%}%>
</tr>
<tr>
<td>休前延長</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRestBeforeOver), 11 ) %></td><%}%>
</tr>
<tr>
<td>休後延長</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(112560*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRestAfterOver), 11 ) %></td><%}%>
</tr>
<tr>
<td>飲食</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round( 13450*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailMeat), 11 ) %></td><%}%>
</tr>
<tr>
<td>出前</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailDelivery), 11 ) %></td><%}%>
<tr>
<td>コンビニ</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(  9135*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailConveni), 11 ) %></td><%}%>
</tr>
<tr>
<td>冷蔵庫</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(  7410*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRef), 11 ) %></td><%}%>
</tr>
<tr>
<td>マルチメディア</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailMulti), 11 ) %></td><%}%>
</tr>
<tr>
<td>販売商品</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(  2000*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailSales), 11 ) %></td><%}%>
</tr>
<tr>
<td>レンタル</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRental), 11 ) %></td><%}%>
</tr>
<tr>
<td>タバコ</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailCigarette), 11 ) %></td><%}%>
</tr>
<tr>
<td>電話</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailTel), 11 ) %></td><%}%>
</tr>
<tr>
<td>その他</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(  4560*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailEtc), 11 ) %></td><%}%>
</tr>
<tr>
<td>割引</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round( -5000*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailDiscount), 11 ) %></td><%}%>
</tr>
<tr>
<td>割増</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailExtra), 11 ) %></td><%}%>
</tr>
<tr>
<td>メンバー割引</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(-53530*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailMember), 11 ) %></td><%}%>
</tr>
<tr>
<td>奉仕料</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailService), 11 ) %></td><%}%>
</tr>
<tr>
<td>消費税</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStax), 11 ) %></td><%}%>
</tr>
<tr>
<td>調整金</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(   615*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailAdjust), 11 ) %></td><%}%>
</tr>
<%if(ownerinfo.SalesDetailTaxRate1 != 0){%>
<tr>
<td>(<%=sf.rightFitFormat(((float)ownerinfo.SalesDetailTaxRate1 / (float)10+""),4) %>% 対象)</td><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailTaxableAmount1),11 ) %></td>
</tr>
<%}%>
<%if(ownerinfo.SalesDetailTaxRate2 != 0){%>
<tr>
<td>(<%=sf.rightFitFormat(((float)ownerinfo.SalesDetailTaxRate2 / (float)10+""),4) %>% 対象)</td><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailTaxableAmount2),11 ) %></td>
</tr>
<%}%>
<tr class="uriage">
<td>合計</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(525000*per)), 11 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailTotal), 11 ) %></td><%}%>
</tr>
<tr>
<td>(内消費税)</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round( 25000*per)), 10 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStaxIn), 10 ) %></td><%}%>
</tr>
</table>
<%}%>
<hr class="border">
<%-- 売上表示ここまで --%>
<a name="DateSelect"></a>
<% 
    if(MonthFlag)
    {
%>
<%-- 年月選択画面表示 --%>
<jsp:include page="salesselectmonth.jsp" flush="true" >
  <jsp:param name="Contents" value="salesdetail" />
</jsp:include>

<%-- 年月選択画面表示ここまで --%>
<%
    }
    else if (RangeFlag)
    {
%>
<%-- 日付範囲選択画面表示 --%>
<jsp:include page="salesselectrange.jsp" flush="true" >
  <jsp:param name="Contents" value="salesdetail" />
</jsp:include>
<%-- 日付範囲選択画面表示ここまで --%>
<%
    }
    else
    {
%>
<%-- 日付選択画面表示 --%>
<jsp:include page="salesselectday.jsp" flush="true" >
  <jsp:param name="Contents" value="salesdetail" />
</jsp:include>
<%-- 日付選択画面表示ここまで --%>
<%
    }
%>
<!-- 組数表示ここまで -->
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="jumpurl" value="salesdetail.jsp" />
  <jsp:param name="dispname" value="売上詳細" />
</jsp:include>
