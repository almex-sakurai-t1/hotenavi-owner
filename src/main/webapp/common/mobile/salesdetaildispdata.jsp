<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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

    StringFormat    sf;
    sf = new StringFormat();

    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String day   = request.getParameter("Day");
    if (day != null)
    {
        if (!CheckString.numCheck( day ) )
        {
            response.sendError(400);
            return;
        }
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
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %>～<br>
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
計上分<font size=1>[<a href="#DateSelect"><%if(MonthFlag){%>年月<%}else{%>日付<%}%>選択</a>]</font><br>
<%
    // ホテルIDのセット
    String hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
    int    hostkind = 0;
    int    newsales = 0;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        connection  = DBConnection.getConnection();
        final String query       = "SELECT * FROM hotel WHERE hotel_id = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result      = prestate.executeQuery();
        if( result.next() )
        {
            hostkind  = result.getInt("host_kind");
            newsales  = result.getInt("host_detail");
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
<hr>
<%-- 売上表示 --%>
<%if (newsales == 1){%>
<pre>
☆売上
 項目           金額<%
for(int i = 0 ; i < ownerinfo.ManualSalesDetailCount ; i++ )
{
    if (!ownerinfo.ManualSalesDetailName[i].equals("") || ownerinfo.ManualSalesDetailAmount[i] != 0 )
    {%>
<%=sf.leftFitFormat(ownerinfo.ManualSalesDetailName[i],10)%><%= sf.rightFitFormat( Kanma.get(ownerinfo.ManualSalesDetailAmount[i]), 10 ) %><%
    }
}
%>
---------------------
合計　　 <%= sf.rightFitFormat( Kanma.get(ownerinfo.ManualSalesDetailTotal), 11 ) %>
(内消費税)<%= sf.rightFitFormat( Kanma.get(ownerinfo.ManualSalesDetailTaxIn), 10 ) %>
</pre>
<%}else{%>
<pre>
☆売上
 項目           金額
宿泊　　 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(185850*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStay), 11 ) %><%}%>
泊前延長 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(  6750*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStayBeforeOver), 11 ) %><%}%>
泊後延長 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round( 35500*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStayAfterOver), 11 ) %><%}%>
休憩　　 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(206600*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRest), 11 ) %><%}%>
休前延長 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRestBeforeOver), 11 ) %><%}%>
休後延長 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(112560*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRestAfterOver), 11 ) %><%}%>
飲食　　 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round( 13450*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailMeat), 11 ) %><%}%>
出前　　 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailDelivery), 11 ) %><%}%>
ｺﾝﾋﾞﾆ 　 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(  9135*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailConveni), 11 ) %><%}%>
冷蔵庫　 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(  7410*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRef), 11 ) %><%}%>
ﾏﾙﾁﾒﾃﾞｨｱ <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailMulti), 11 ) %><%}%>
販売商品 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(  2000*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailSales), 11 ) %><%}%>
ﾚﾝﾀﾙ 　　<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailRental), 11 ) %><%}%>
ﾀﾊﾞｺ 　　<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailCigarette), 11 ) %><%}%>
電話 　　<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailTel), 11 ) %><%}%>
その他 　<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(  4560*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailEtc), 11 ) %><%}%>
割引 　　<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round( -5000*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailDiscount), 11 ) %><%}%>
割増 　　<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailExtra), 11 ) %><%}%>
ﾒﾝﾊﾞｰ割引<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(-53530*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailMember), 11 ) %><%}%>
奉仕料 　<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailService), 11 ) %><%}%>
消費税 　<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStax), 11 ) %><%}%>
調整金 　<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(   615*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailAdjust), 11 ) %><%}%>
<%if(ownerinfo.SalesDetailTaxRate1 != 0){%><%=sf.rightFitFormat(((float)ownerinfo.SalesDetailTaxRate1 / (float)10+""),4) %>%対象<%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailTaxableAmount1),11 ) %><%}%>
<%if(ownerinfo.SalesDetailTaxRate2 != 0){%><%=sf.rightFitFormat(((float)ownerinfo.SalesDetailTaxRate2 / (float)10+""),4) %>%対象<%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailTaxableAmount2),11 ) %><%}%>
--------------------
合計　　 <%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(525000*per)), 11 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailTotal), 11 ) %><%}%>
(内消費税)<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round( 25000*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesDetailStaxIn), 10 ) %><%}%>
</pre>
<%}%>
<hr>
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

