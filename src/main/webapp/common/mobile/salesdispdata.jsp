<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="java.util.*" %>
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
    String param_day   = ReplaceString.getParameter(request,"Day");
    if (param_day != null)
    {
        if ( !CheckString.numCheck( param_day ) )
        {
            response.sendError(400);
            return;
        }
        if(param_day.equals("0"))
        {
            MonthFlag = true;
        }
    }
    String param_month = ReplaceString.getParameter(request,"Month");
    if ( param_month != null && !CheckString.numCheck( param_month ) )
    {
        response.sendError(400);
        return;
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
    int year     = ownerinfo.SalesGetStartDate / 10000;
    int month    = ownerinfo.SalesGetStartDate / 100 % 100;
    int day      = ownerinfo.SalesGetStartDate % 100;
    int endyear  = ownerinfo.SalesGetEndDate / 10000;
    int endmonth = ownerinfo.SalesGetEndDate / 100 % 100;
    int endday   = ownerinfo.SalesGetEndDate % 100;

    // ホテルIDのセット
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }

    int    pointtotal = 0;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT pointtotal_flag FROM hotel WHERE hotel_id = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            pointtotal= result.getInt("hotel.pointtotal_flag");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        if (pointtotal == 0 && ownerinfo.SalesPointTotal!=0)
        {
            pointtotal = 1;
            prestate = connection.prepareStatement("UPDATE hotel SET pointtotal_flag = 1 WHERE hotel_id = ? ");
            prestate.setString(1, hotelid);
            prestate.executeUpdate();
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

    String param = "?HotelId=" + hotelid;
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
<hr><!-- 売上表示 -->
<pre>
☆売上
 項目       金額
精(現)<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(416850*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTex), 10 ) %><%}%>
精(ｸﾚ)<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(107550*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTexCredit), 10 ) %><%}%>
ﾌﾛ(現)<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(   600*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFront), 10 ) %><%}%>
ﾌﾛ(ｸﾚ)<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFrontCredit), 10 ) %><%}%>
<%
    if (pointtotal == 1)
    {
%>提携ﾎﾟｲﾝﾄ<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 7 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesPointTotal), 7 ) %><%}%>
<%
    }
%>----------------
合計<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(525000*per)), 12 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTotal), 12 ) %><%}%></pre>
<div align="right">
<a href="<%= response.encodeURL("salesdetail.jsp" + param) %>">売上詳細を見る</a>
</div>
<!-- 売上表示ここまで -->
<hr>
<!-- 組数表示 -->
<pre>
☆組数
 項目       組数
休憩<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(42*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesRestCount), 10) %><%}%>組
宿泊<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(18*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesStayCount), 10) %><%}%>組
----------------
退室済計<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(60*per)), 6 ) %><%}else{%><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesTotalCount), 6) %><%}%>組
----------------
<!--<%
    if( ownerinfo.SalesNowCheckin != 999 )
    {
%>
現在入室<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(5*per)), 6 ) %><%}else{%><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesNowCheckin), 6) %><%}%>組
<%
    }
%>-->
回転数　　　<%if(DemoMode){%>3.00<%}else{%><%=(float)ownerinfo.SalesTotalRate / (float)100 %><%}%>
客単価<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(8750*per)), 10 ) %><%}else{%><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesTotalPrice),10) %><%}%>
<%
    if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
    {
%>室単平均<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %><%}else{%><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %><%}%>
室単計<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per*30)), 10 ) %><%}else{%><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomTotalPrice),10) %><%}%>
<%
    }
    else
    {
%>部屋単価<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %><%}else{%><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %><%}%>
<%
    }
%></pre>
<%@ include file="checkHotelId.jsp" %>
<%
    boolean    HotelIdCheck = true;
    HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,2);
    if (HotelIdCheck && ownerinfo.Addupdate == ownerinfo.SalesGetStartDate && ownerinfo.Addupdate == ownerinfo.SalesGetEndDate)
    {
%>
<hr>
☆現在部屋情報<br>
<%-- ここで現在部屋情報を取得します --%>
<jsp:include page="roominfoget.jsp" flush="true" />
<%-- ここで現在部屋情報を取得します（ここまで） --%>
<%-- 現在部屋情報データの表示 --%>
<jsp:include page="roominfodispdata.jsp" flush="true">
 <jsp:param name="jump" value="no"/>
</jsp:include>
<%-- 現在部屋情報データの表示ここまで --%>
<%
    }
    else
    {
%>
<div align="right">
<a href="<%= response.encodeURL("salesinout.jsp" + param) %>">IN/OUT組数を見る</a>
</div>
<%
    }
%>
<hr>
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


