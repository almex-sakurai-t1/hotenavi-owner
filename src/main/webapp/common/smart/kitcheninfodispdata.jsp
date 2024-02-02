<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.kitchen.*" %>
<%@ page errorPage="error.jsp" %>
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
    String loginId = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }

    NumberFormat nf = new DecimalFormat("00");

    String sorttype = request.getParameter("sorttype");
    if (sorttype == null)
        sorttype = "ROOM";

    String[] arrWday = {"日", "月", "火", "水", "木", "金", "土"};

    KitchenInfo kitcheninfo = new KitchenInfo();

    kitcheninfo.sendPacket0000(1, hotelid);

    if (sorttype.equals("ROOM"))
    {
        kitcheninfo.SortOrderInfo(kitcheninfo.ORDERINFOTYPE_INITIAL,kitcheninfo.SORTTYPE_ROOM);
    }
    else if (sorttype.equals("ACCEPT"))
    {
        kitcheninfo.SortOrderInfo(kitcheninfo.ORDERINFOTYPE_INITIAL,kitcheninfo.SORTTYPE_ACCEPT);
    }
    else if (sorttype.equals("SETTABLE"))
    {
        kitcheninfo.SortOrderInfo(kitcheninfo.ORDERINFOTYPE_INITIAL,kitcheninfo.SORTTYPE_SETTABLE);
    }
    else if (sorttype.equals("GOODSCODE"))
    {
        kitcheninfo.SortOrderInfo(kitcheninfo.ORDERINFOTYPE_INITIAL,kitcheninfo.SORTTYPE_GOODSCODE);
    }
%>
<h2><%=kitcheninfo.InitialNowDate/100%100%>月<%=kitcheninfo.InitialNowDate%100%>日(<%=arrWday[kitcheninfo.InitialNowDayofweek]%>) <%=nf.format(kitcheninfo.InitialNowTime/100)%>:<%=nf.format(kitcheninfo.InitialNowTime%100)%>現在
</h2>
<div class="form">
<form action="<%= response.encodeURL("kitcheninfo.jsp") %>" method="post">
<select name="sorttype" id="sorttype">
<option value="ROOM" <%if(sorttype.equals("ROOM")){%>selected<%}%>>部屋番号順</option>
<option value="ACCEPT" <%if(sorttype.equals("ACCEPT")){%>selected<%}%>>受付時刻順</option>
<option value="SETTABLE" <%if(sorttype.equals("SETTABLE")){%>selected<%}%>>配膳時刻順</option>
<option value="GOODSCODE" <%if(sorttype.equals("GOODSCODE")){%>selected<%}%>>商品コード順</option>
</select>
<input type="hidden" name="HotelId" value="<%= StringEscapeUtils.escapeHtml4(hotelid) %>">
<input type="submit" value="更新" id="button3">
</form>
</div>
<hr class="border">
<%-- 現在部屋情報表示 --%>

<table class="roomdetail">
<tr>
<th width="25%">部屋</th><th>受付</th><th>配膳</th>
</tr>
<%  if ( kitcheninfo.InitialOrderList.size()  == 0)
    {%>
<tr>
<td colspan=3>現在、未配膳の注文はありません</td>
</tr>
<%  }
    else
    for( int i = 0 ; i < kitcheninfo.InitialOrderList.size() ; i++ )
    {%>
<tr>
<td rowspan=2><%=kitcheninfo.InitialOrderList.get( i ).RoomName%></td>
<td><%=kitcheninfo.InitialOrderList.get( i ).AcceptDatetime.substring( 8, 10 )%>:<%=kitcheninfo.InitialOrderList.get( i ).AcceptDatetime.substring( 10, 12 )%></td>
<td><%=kitcheninfo.InitialOrderList.get( i ).SettableDatetime.substring( 8, 10 )%>:<%=kitcheninfo.InitialOrderList.get( i ).SettableDatetime.substring( 10, 12 )%></td>
</tr>
<tr>
<td style="text-align:left;font-size:15px" colspan="2"><%=kitcheninfo.InitialOrderList.get( i ).GoodsName.replace(" ","").replace("　","")%>(<%=kitcheninfo.InitialOrderList.get( i ).GoodsCount%>)
</tr>
<%  }%>
</table>
<hr class="border">

<%-- 現在キッチン端末情報表示ここまで --%>

<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="kitcheninfo.jsp" />
  <jsp:param name="dispname" value="キッチン端末情報" />
</jsp:include>
