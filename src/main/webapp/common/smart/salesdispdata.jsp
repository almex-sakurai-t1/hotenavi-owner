<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %> 
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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

    StringFormat sf = new StringFormat();

    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String param_day  = ReplaceString.getParameter(request,"Day");
    if (param_day != null)
    {
       if(param_day.equals("0")) 
       {
           MonthFlag = true; 
       }
    }
    String param_month = ReplaceString.getParameter(request,"Month");
    String[] dates = new String[]{ param_month, param_day };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
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

<h1>計上分 <font size="-1"><a href="#DateSelect">[<%if(MonthFlag){%>年月<%}else{%>日付<%}%>選択]</a></font></h1>
<%
    int year     = ownerinfo.SalesGetStartDate / 10000;
    int month    = ownerinfo.SalesGetStartDate / 100 % 100;
    int day      = ownerinfo.SalesGetStartDate % 100;
    int endyear  = ownerinfo.SalesGetEndDate / 10000;
    int endmonth = ownerinfo.SalesGetEndDate / 100 % 100;
    int endday   = ownerinfo.SalesGetEndDate % 100;

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

    int pointtotal = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT pointtotal_flag FROM hotel WHERE hotel_id = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result = prestate.executeQuery();
        if( result.next() )
        {
            pointtotal= result.getInt("hotel.pointtotal_flag");
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

    if (pointtotal == 0 && ownerinfo.SalesPointTotal != 0)
    {
        try
        {
            final String query = "UPDATE hotel SET pointtotal_flag = 1 WHERE hotel_id = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, hotelid);

            prestate.executeUpdate();
            pointtotal = 1;
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }
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

<h1>☆売上</h1>

<table class="uriage">
<tr>
<th width="50%">項目</th><th>金額</th>
</tr>
<tr>
<td><div align=left style="margin-left:10px">精算機<font size=-1>(現金)</font></div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(416850*per)), 10 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTex), 10 ) %></td><%}%>
</tr>
<tr>
<td><div align=left style="margin-left:10px">精算機<font size=-1>(CREDIT)</font></div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(107550*per)), 10 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTexCredit), 10 ) %></td><%}%>
</tr>
<tr>
<td><div align=left style="margin-left:10px">フロント<font size=-1>(現金)</font></div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(   600*per)), 10 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFront), 10 ) %></td><%}%>
</tr>
<tr>
<td><div align=left style="margin-left:10px">フロント<font size=-1>(CREDIT)</font></div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 10 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFrontCredit), 10 ) %></td><%}%>
</tr>
<%
    if (pointtotal == 1)
    {
%><tr>
<td><div align=left style="margin-left:10px">提携ポイント</div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 7 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesPointTotal), 7 ) %></td><%}%>
</tr>
<%
    }
%><tr class="uriage">
<td>合計</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(525000*per)), 12 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTotal), 12 ) %></td><%}%>
</tr>
</table>

<div align="right">
<ul class="link">
<li><a href="<%= response.encodeURL("salesdetail.jsp" + param) %>">売上詳細を見る</a></li>
</ul>
</div>
<!-- 売上表示ここまで -->

<!-- 組数表示 -->
<h1>☆組数</h1>
<table class="uriage">
<tr>
<th width="50%">項目</td><th>組数</td>
</tr>
<tr>
<td>休憩</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(42*per)), 10 ) %>組</td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesRestCount), 10) %><%}%>組</td>
<tr>
<td>宿泊</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(18*per)), 10 ) %>組</td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesStayCount), 10) %><%}%>組</td>
</tr>
<tr class="uriage">
<td>退室済計</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(60*per)), 6 ) %>組</td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesTotalCount), 6) %><%}%>組</td>
</tr>
</table>
　<br>
<table class="uriage">
<tr>
<th width="50%">項目</td><th>　</td>
</tr>
<!--<%
    if( ownerinfo.SalesNowCheckin != 999 )
    {
%>
<tr>
<td>現在入室</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(5*per)), 6 ) %>組</td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesNowCheckin), 6) %>組</td><%}%>
</tr>
<%
    }
%>-->
<tr>
<td>回転数</td><%if(DemoMode){%><td class="uriage">3.00</td><%}else{%><td class="uriage"><%=(float)ownerinfo.SalesTotalRate / (float)100 %></td><%}%>
</tr>
<tr>
<td>客単価</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(8750*per)), 10 ) %></td><%}else{%><td class="uriage"><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesTotalPrice),10) %></td><%}%>
</tr>
<%
    if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
    {
%>
<tr>
<td>室単平均</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %></td><%}else{%><td class="uriage"><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %></td><%}%>
</tr>
<tr>
<td>室単計</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per*30)), 10 ) %></td><%}else{%><td class="uriage"><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomTotalPrice),10) %></td><%}%>
</tr>
<%
    }
    else
    {
%>
<tr>
<td>部屋単価</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %></td><%}else{%><td class="uriage"><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %></td><%}%>
</tr>
<%
    }
%>
</table>
<%@ include file="checkHotelId.jsp" %>
<%
    boolean HotelIdCheck = true;
    HotelIdCheck = checkHotelId(loginHotelId, hotelid, ownerinfo.DbUserId, 2);
    if (HotelIdCheck && ownerinfo.Addupdate == ownerinfo.SalesGetStartDate && ownerinfo.Addupdate == ownerinfo.SalesGetEndDate)
    {
%>
　<br>
<h1 class="title">現在部屋情報</h1>
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
<ul class="link">
<li><a href="<%= response.encodeURL("salesinout.jsp" + param) %>">IN/OUT組数を見る</a></li>
</ul>
</div>
<%
    }
%>
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
