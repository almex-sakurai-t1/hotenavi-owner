<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String param_compare          = ReplaceString.getParameter(request,"Compare");
    if (param_compare == null) 
    {
        param_compare = "false";
    }
    String paramKindfromList   = ReplaceString.getParameter(request,"KindfromList");
    if    (paramKindfromList != null)
    {
        param_compare = "false";
    }

    String param_hotelid = ReplaceString.getParameter(request,"NowHotel");
//    String param_hotelid  = ReplaceString.getParameter(request,"HotelIdfromGroup");

    int  inputInOutGetStartDate = ownerinfo.SalesGetStartDate;
    ownerinfo.InOutGetEndDate = ownerinfo.SalesGetEndDate;

%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top" width="<%if (param_compare.equals("true")){%>65%<%}else{%>45%<%}%>">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
<!--
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN" rowspan="2">&nbsp;</td>
          <td align="center" valign="middle" nowrap class="tableLN" colspan="3">売上金額</td>
          <td align="center" valign="middle" nowrap class="tableLN" colspan="3">組数</td>
          <td align="center" valign="bottom" nowrap class="tableLN" rowspan="2">回転数</td>
          <td align="center" valign="bottom" nowrap class="tableRN" rowspan="2">客単価</td>
        </tr>
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">目標</td>
          <td align="center" valign="middle" nowrap class="tableLN">実績</td>
          <td align="center" valign="middle" nowrap class="tableLN">%</td>
          <td align="center" valign="middle" nowrap class="tableLN">目標</td>
          <td align="center" valign="middle" nowrap class="tableLN">実績</td>
          <td align="center" valign="middle" nowrap class="tableLN">%</td>
          </tr>
-->
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">&nbsp;</td>
          <td align="center" valign="middle" nowrap class="tableLN">売上金額</td>
          <td align="center" valign="middle" nowrap class="tableLN">組数</td>
          <td align="center" valign="bottom" nowrap class="tableLN">回転数</td>
          <td align="center" valign="bottom" nowrap class="tableRN">客単価</td>
        </tr>
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">休憩</td>
<!--      <td align="right"  valign="middle" class="size14 tableLW" nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>-->
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if (ownerinfo.SalesTotalPrice!=0&&ownerinfo.SalesTotalCount!=0){%><%=Kanma.get(Math.round(((float)ownerinfo.SalesTotal * (float)ownerinfo.SalesRestPrice * (float)ownerinfo.SalesRestCount )/((float)ownerinfo.SalesTotalPrice*(float)ownerinfo.SalesTotalCount)))%><%}%></td>
<!--      <td align="right"  valign="middle" class="size14 tableLW" nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;%</td>-->
<!--      <td align="right"  valign="middle" class="size14 tableLW" nowrap>&nbsp;&nbsp;&nbsp;&nbsp;</td>-->
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%= Kanma.get(ownerinfo.SalesRestCount ) %></td>
<!--      <td align="right"  valign="middle" class="size14 tableLW" nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;%</td>-->
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%= (float)ownerinfo.SalesRestRate / (float)100 %><% if((float)ownerinfo.SalesRestRate%10==0){%>0<%}%></td>
          <td align="right"  valign="middle" class="size14 tableRW" nowrap><%= Kanma.get(ownerinfo.SalesRestPrice) %></td>
        </tr>
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">宿泊</td>
<!--      <td align="right"  valign="middle" class="size14 tableLW" nowrap>&nbsp;</td>-->
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if (ownerinfo.SalesTotalPrice!=0&&ownerinfo.SalesTotalCount!=0){ %><%=Kanma.get(Math.round((float)ownerinfo.SalesTotal-((float)ownerinfo.SalesTotal * (float)ownerinfo.SalesRestPrice * (float)ownerinfo.SalesRestCount ) /((float)ownerinfo.SalesTotalPrice*(float)ownerinfo.SalesTotalCount)))%><%}%></td>
<!--      <td align="right"  valign="middle" class="size14 tableLW" nowrap>&nbsp;%</td>-->
<!--      <td align="right"  valign="middle" class="size14 tableLW" nowrap>&nbsp;</td>-->
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%= Kanma.get(ownerinfo.SalesStayCount ) %></td>
<!--      <td align="right"  valign="middle" class="size14 tableLW" nowrap>&nbsp;%</td>-->
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%= (float)ownerinfo.SalesStayRate / (float)100 %><% if((float)ownerinfo.SalesStayRate%10==0){%>0<%}%></td>
          <td align="right"  valign="middle" class="size14 tableRW" nowrap><%= Kanma.get(ownerinfo.SalesStayPrice) %></td>
        </tr>
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">合計</td>
<!--      <td align="right"  valign="middle" class="size14 tableLB" nowrap>&nbsp;</td>-->
          <td align="right"  valign="middle" class="size14 tableLB" nowrap><%=Kanma.get(ownerinfo.SalesTotal)%></td>
<!--      <td align="right"  valign="middle" class="size14 tableLB" nowrap>&nbsp;%</td>-->
<!--      <td align="right"  valign="middle" class="size14 tableLB" nowrap>&nbsp;</td>-->
          <td align="right"  valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(ownerinfo.SalesTotalCount ) %></td>
<!--      <td align="right"  valign="middle" class="size14 tableLB" nowrap>&nbsp;%</td>-->
          <td align="right"  valign="middle" class="size14 tableLB" nowrap><%= (float)ownerinfo.SalesTotalRate / (float)100 %><% if((float)ownerinfo.SalesTotalRate%10==0){%>0<%}%></td>
          <td align="right"  valign="middle" class="size14 tableRB" nowrap><%= Kanma.get(ownerinfo.SalesTotalPrice) %></td>
        </tr>
        <tr>
          <td align="center" valign="middle" colspan=5><input name="inoutdetail" type="button" class="inoutbtn2" onClick="MM_openBrWindow('inout_detail.jsp?startDate=<%=ownerinfo.SalesGetStartDate%>&endDate=<%=ownerinfo.SalesGetEndDate%>&HotelId=<%=param_hotelid%>','INOUT組数','menubar=yes,scrollbars=yes,resizable=yes,width=340,height=600')" value="IN/OUT組数">
          </td>
        </tr>
      </table>
    </td>
<%
    if (param_compare.equals("false"))
    {
%>
    <td width="8" valign="top">&nbsp;</td>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">取引種別</td>
          <td align="center" valign="middle" nowrap class="tableRN">金　額</td>
        </tr>
        <tr>
          <td align="left" valign="middle" class="tableLW" nowrap><div class="space2">精算機　(現金)</div>
          </td>
          <td align="right" valign="middle" class="tableRW" nowrap><%= Kanma.get(ownerinfo.SalesTex) %></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap><div class="space2">精算機　(CREDIT)</div>
          </td>
          <td align="right" valign="middle" class="tableRG" nowrap><%= Kanma.get(ownerinfo.SalesTexCredit) %></td>
        </tr>
        <tr>
          <td class="tableLW" nowrap><div class="space2">フロント(現金)</div>
          </td>
          <td align="right" valign="middle" class="tableRW" nowrap><%= Kanma.get(ownerinfo.SalesFront) %></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap><div class="space2">フロント(CREDIT)</div>
          </td>
          <td align="right" valign="middle" class="tableRG" nowrap><%= Kanma.get(ownerinfo.SalesFrontCredit) %></td>
        </tr>
      </table>
    </td>
    <td width="8" valign="top">&nbsp;</td>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="56%" align="center" valign="middle" nowrap class="tableLN">項　目</td>
          <td width="44%" align="center" valign="middle" nowrap class="tableRN">&nbsp;</td>
        </tr>
        <tr>
          <td class="tableLW" nowrap><div class="space2">ビジター客単価</div>
          </td>
          <td align="right" valign="middle" class="tableRW" nowrap><%= Kanma.get(ownerinfo.SalesVisitorPrice) %></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap><div class="space2">メンバー客単価</div>
          </td>
          <td align="right" valign="middle" class="tableRG" nowrap><%= Kanma.get(ownerinfo.SalesMemberPrice) %></td>
        </tr>
<%
        if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
        {
%>
        <tr>
          <td class="tableLW" nowrap><div class="space2">室単価平均</div>
          </td>
          <td align="right" valign="middle" class="tableRW" nowrap><%= Kanma.get(ownerinfo.SalesRoomPrice) %></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap><div class="space2">室単価累計</div>
          </td>
          <td align="right" valign="middle" class="tableRG" nowrap><%= Kanma.get(ownerinfo.SalesRoomTotalPrice) %></td>
        </tr>
<%
        }
        else
        {
%>
        <tr>
          <td class="tableLW" nowrap><div class="space2">部屋単価</div>
          </td>
          <td align="right" valign="middle" class="tableRW" nowrap><%= Kanma.get(ownerinfo.SalesRoomPrice) %></td>
        </tr>
<%
        }
%>
      </table>
    </td>
<%
    }
    else
    {
%>
  </tr>
  <tr>
     <td><img src="../../common/pc/image/spacer.gif" width="2" height="2"></td>
  </tr>
  <tr>
    <td valign="top" width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="tableLWU" nowrap><div class="space2">ビジター客単価</div>
          </td>
          <td align="right" valign="middle" class="tableRWU" nowrap><%= Kanma.get(ownerinfo.SalesVisitorPrice) %></td>
          <td><img src="../../common/pc/image/spacer.gif" width="2" height="2"></td>
<%
        if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
        {
%>
          <td class="tableLWU" nowrap><div class="space2">室単価平均</div>
          </td>
          <td align="right" valign="middle" class="tableRWU" nowrap><%= Kanma.get(ownerinfo.SalesRoomPrice) %></td>
<%
        }
        else
        {
%>
          <td class="tableLWU" nowrap><div class="space2">部屋単価</div>
          </td>
          <td align="right" valign="middle" class="tableRWU" nowrap><%= Kanma.get(ownerinfo.SalesRoomPrice) %></td>
<%
        }
%>
        </tr>
        <tr>
          <td class="tableLG" nowrap><div class="space2">メンバー客単価</div>
          </td>
          <td align="right" valign="middle" class="tableRG" nowrap><%= Kanma.get(ownerinfo.SalesMemberPrice) %></td>
          <td><img src="../../common/pc/image/spacer.gif" width="2" height="2"></td>
<%
        if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
        {
%>
          <td class="tableLG" nowrap><div class="space2">室単価累計</div>
          </td>
          <td align="right" valign="middle" class="tableRG" nowrap><%= Kanma.get(ownerinfo.SalesRoomTotalPrice) %></td>
<%
        }
        else
        {
%>
          <td colspan=2>&nbsp;</div>
          </td>
<%
        }
%>
        </tr>
      </table>
    </td>
<%
    }
%>
  </tr>
</table>