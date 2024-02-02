<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.kitchen.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String          hotelid;
    String          hotelname;
    String          sorttype;
    boolean         KitchenExist = false;
    NumberFormat    nf;
    String[]  arrWday = {"日", "月", "火", "水", "木", "金", "土"};

    KitchenInfo kitcheninfo = new KitchenInfo();

    nf        = new DecimalFormat("00");

    hotelid = ReplaceString.getParameter(request,"NowHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    hotelname = ReplaceString.getParameter(request,"NowHotelName");
    if( hotelname == null )
    {
        hotelname = "";
    }

    hotelname = ReplaceString.getParameter(request,"NowHotelName");

    sorttype  = ReplaceString.getParameter(request,"sorttype");
    if (sorttype == null) sorttype = "ROOM";

    KitchenExist = kitcheninfo.sendPacket0000(1,hotelid);

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

<!-- 店舗表示 --> 
<a name="<%= hotelid %>"></a> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="25" border="0" cellpadding="0" cellspacing="0">
        <tr> 
	      <form action="kitchen_info.jsp" id="kitchen_info_submit" method="post">
          <td width="200" height="25" bgcolor="#22333F" class="tab"><font color="#FFFFFF"><%= hotelname %></font></td>
          <td width="15" height="25" valign="bottom"><IMG src="../../common/pc/image/tab1.gif" width="15" height="25"></td>
          <td valign="bottom">
            <div class="navy10px">
            <img src="../../common/pc/image/spacer.gif" width="12" height="16" align="absmiddle">
            <select name="sorttype" onchange="document.getElementById('kitchen_info_submit').submit()">
              <option value="ROOM" <%if(sorttype.equals("ROOM")){%>selected<%}%>>部屋番号順</option>
              <option value="ACCEPT" <%if(sorttype.equals("ACCEPT")){%>selected<%}%>>受付時刻順</option>
              <option value="SETTABLE" <%if(sorttype.equals("SETTABLE")){%>selected<%}%>>配膳時刻順</option>
              <option value="GOODSCODE" <%if(sorttype.equals("GOODSCODE")){%>selected<%}%>>商品コード順</option>
           </select>
           <input type="submit" value="更新">
         </div>
          </td></form>
        </tr>          
      </table>
    </td>
    <td width="3">&nbsp;</td>
  </tr>

<!-- ここから表 -->
  <tr>
    <td align="center" valign="top" bgcolor="#D0CED5">
      <table width="98%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="6" align="center"><img src="../../common/pc/image/spacer.gif" width="300" height="6"></td>
        </tr>
        <tr>
          <td align="left">
            <table width="80%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="bar">キッチン配膳状況</td>
                <td align="right" class="bar2" nowrap><%=kitcheninfo.InitialNowDate/1000%>年<%=kitcheninfo.InitialNowDate/100%100%>月<%=kitcheninfo.InitialNowDate%100%>日(<%=arrWday[kitcheninfo.InitialNowDayofweek]%>) <%=nf.format(kitcheninfo.InitialNowTime/100)%>:<%=nf.format(kitcheninfo.InitialNowTime%100)%>現在</td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="160" height="14"></td>
        </tr>
        <tr>
          <td align="left" valign="top">
            <table width="80%" border="0" cellspacing="0" cellpadding="0">
              <tr align="center" valign="middle">
                <td class="tableLN">部　屋</td>
                <td class="tableLN">受付時刻</td>
                <td class="tableLN" nowrap>商品名</td>
                <td class="tableLN">数量</td>
                <td class="tableRN">配膳時刻</td>
              </tr>
<%  if ( kitcheninfo.InitialOrderList.size() == 0)
    {%>
              <TR valign="middle">
                <td class="tableRW" align="center" colspan=5>現在、未配膳の注文はありません </td>
              </TR>
<%  }
    else
    for( int i = 0 ; i < kitcheninfo.InitialOrderList.size() ; i++ )
    {%>
              <TR valign="middle">
                <td class="tableL<%if(i%2 == 0){%>W<%}else{%>G<%}%>" align="center"><%=kitcheninfo.InitialOrderList.get( i ).RoomName%> </td>
                <td class="tableL<%if(i%2 == 0){%>W<%}else{%>G<%}%>" align="center"><%=kitcheninfo.InitialOrderList.get( i ).AcceptDatetime.substring( 8, 10 )%>:<%=kitcheninfo.InitialOrderList.get( i ).AcceptDatetime.substring( 10, 12 )%></td>
                <td class="tableL<%if(i%2 == 0){%>W<%}else{%>G<%}%>" nowrap><%=kitcheninfo.InitialOrderList.get( i ).GoodsName%></td>
                <td class="tableL<%if(i%2 == 0){%>W<%}else{%>G<%}%>" align=right><%=kitcheninfo.InitialOrderList.get( i ).GoodsCount%> </td>
                <td class="tableR<%if(i%2 == 0){%>W<%}else{%>G<%}%>" align="center"><%=kitcheninfo.InitialOrderList.get( i ).SettableDatetime.substring( 8, 10 )%>:<%=kitcheninfo.InitialOrderList.get( i ).SettableDatetime.substring( 10, 12 )%></td>
              </TR>
<%  }%>
            </table>
          </td>
        </tr>
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="160" height="14"></td>
        </tr>
      </table>
    </td>
    <td width="3" valign="top" align="left" height="100%">
      <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
        </tr>
        <tr>
          <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="3" bgcolor="#999999">
      <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
        </tr>
      </table>
    </td>
    <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>

<!-- ここまで -->

