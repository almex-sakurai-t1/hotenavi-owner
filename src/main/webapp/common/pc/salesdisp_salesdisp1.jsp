<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("demo"))
    {
        DemoMode = true;
    }
    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;

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

    String CompMode = "1";
    CompMode           = ReplaceString.getParameter(request,"CompMode");
    if (CompMode == null) 
    {
        CompMode = "1";
    }

    String pointtotal = "1";
    pointtotal = ReplaceString.getParameter(request,"PointTotal");
    if( pointtotal == null )
    {
        pointtotal = "0";
    }

    String param_hotelid = ReplaceString.getParameter(request,"NowHotel");
//    String param_hotelid  = ReplaceString.getParameter(request,"HotelIdfromGroup");

    int  inputInOutGetStartDate = ownerinfo.SalesGetStartDate;
    ownerinfo.InOutGetEndDate = ownerinfo.SalesGetEndDate;
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
    if (param_compare.equals("false"))
    {
%>
  <tr>
    <td valign="top" width="34%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="left" valign="middle" nowrap class="size14 tableLBU"><div class="space2">売 上 金 額</div></td>
          <td align="right"  valign="middle" class="size14 tableRBU" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per)))%><%}else{%><%=Kanma.get(ownerinfo.SalesTotal)%><%}%></td>
        </tr>
        <tr>
          <td align="left" valign="middle" class="tableLWU" nowrap width="56%" height="14"><div class="space2">精算機　(現金)</div>
          </td>
          <td align="right" valign="middle" class="size12 tableRWU" nowrap width="44%"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(416850*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesTex) %><%}%></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap height="14"><div class="space2">精算機　(CREDIT)</div>
          </td>
          <td align="right" valign="middle" class="size12 tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(107550*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesTexCredit) %><%}%></td>
        </tr>
        <tr>
          <td class="tableLW" nowrap height="14"><div class="space2">フロント(現金)</div>
          </td>
          <td align="right" valign="middle" class="size12 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(600*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesFront) %><%}%></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap height="14"><div class="space2">フロント(CREDIT)</div>
          </td>
          <td align="right" valign="middle" class="size12 tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesFrontCredit) %><%}%></td>
        </tr>
<%
    if (pointtotal.equals("1"))
    {
%>        <tr>
          <td class="tableLW" nowrap height="14"><div class="space2">提携ポイント</div>
          </td>
          <td align="right" valign="middle" class="size12 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%=Kanma.get(ownerinfo.SalesPointTotal)%><%}%></td>
        </tr>
<%
    }
%>      </table>
    </td>
    <td width="5" valign="top">&nbsp;</td>
    <td valign="top" width="40%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">&nbsp;</td>
          <td align="center" valign="middle" nowrap class="tableLN">組数</td>
          <td align="center" valign="bottom" nowrap class="tableLN">回転数</td>
          <td align="center" valign="bottom" nowrap class="tableRN">客単価</td>
        </tr>
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">休憩</td>
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(42*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesRestCount ) %><%}%></td>
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if(DemoMode){%>2.10<%}else{%><%= (float)ownerinfo.SalesRestRate / (float)100 %><% if((float)ownerinfo.SalesRestRate%10==0){%>0<%}%><%}%></td>
          <td align="right"  valign="middle" class="size14 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(6780*per))%><%}else{%><%= Kanma.get(ownerinfo.SalesRestPrice) %><%}%></td>
        </tr>
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">宿泊</td>
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(18*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesStayCount ) %><%}%></td>
          <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if(DemoMode){%>0.90<%}else{%><%= (float)ownerinfo.SalesStayRate / (float)100 %><% if((float)ownerinfo.SalesStayRate%10==0){%>0<%}%><%}%></td>
          <td align="right"  valign="middle" class="size14 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(9540*per))%><%}else{%><%= Kanma.get(ownerinfo.SalesStayPrice) %><%}%></td>
        </tr>
        <tr>
          <td align="center" valign="middle" nowrap class="tableLN">合計</td>
          <td align="right"  valign="middle" class="size14 tableLB" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(60*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesTotalCount ) %><%}%></td>
          <td align="right"  valign="middle" class="size14 tableLB" nowrap><%if(DemoMode){%>3.00<%}else{%><%= (float)ownerinfo.SalesTotalRate / (float)100 %><% if((float)ownerinfo.SalesTotalRate%10==0){%>0<%}%><%}%></td>
          <td align="right"  valign="middle" class="size14 tableRB" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8750*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesTotalPrice) %><%}%></td>
        </tr>
        <tr>
          <td colspan=5><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
        </tr>
        <tr>
          <td align="center" valign="middle" colspan=5><input name="inoutdetail" type="button" class="inoutbtn2" onClick="MM_openBrWindow('inout_detail.jsp?startDate=<%=ownerinfo.SalesGetStartDate%>&endDate=<%=ownerinfo.SalesGetEndDate%>&HotelId=<%=param_hotelid%>','INOUT組数','menubar=yes,scrollbars=yes,resizable=yes,width=340,height=600')" value="IN/OUT組数"></td>
        </tr>
      </table>
    </td>
    <td width="5" valign="top">&nbsp;</td>
    <td valign="top" width="26%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="56%" align="center" valign="middle" nowrap class="tableLN">項　目</td>
          <td width="44%" align="center" valign="middle" nowrap class="tableRN">&nbsp;</td>
        </tr>
        <tr>
          <td class="tableLW" nowrap><div class="space2">ビジター客単価</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8460*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesVisitorPrice) %><%}%></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap><div class="space2">メンバー客単価</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(7530*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesMemberPrice) %><%}%></td>
        </tr>
<%
        if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
        {
%>
        <tr>
          <td class="tableLW" nowrap><div class="space2">室単価平均</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesRoomPrice) %><%}%></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap><div class="space2">室単価累計</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per*30)))%><%}else{%><%= Kanma.get(ownerinfo.SalesRoomTotalPrice) %><%}%></td>
        </tr>
<%
        }
        else
        {
%>
        <tr>
          <td class="tableLW" nowrap><div class="space2">部屋単価</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesRoomPrice) %><%}%></td>
        </tr>
<%
        }
%>
      </table>
    </td>
  </tr>
<%
    }
    else
    {
%>
  <tr>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr>
                  <td align="center" valign="middle" nowrap class="size14 tableLBU"><div class="space2">売 上 金 額</div></td>
                  <td align="right"  valign="middle" class="size14 tableRBU" nowrap><input type="hidden" id="SalesTotal<%=CompMode%>" name="SalesTotal<%=CompMode%>" value="<%if(DemoMode){%><%=(int)(Math.round(525000*per))%><%}else{%><%=ownerinfo.SalesTotal%><%}%>"><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per)))%><%}else{%><%=Kanma.get(ownerinfo.SalesTotal)%><%}%></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="size14" valign="top" align="right">&nbsp;<%if (CompMode.equals("2")){%>
<script language="JavaScript">
    var sa = document.getElementById("SalesTotal2").value - document.getElementById("SalesTotal1").value;
    var yajirusi;
    if (sa < 0)
    {
        yajirusi = "<font color='red'>↓";
        sa = 0-sa;
    }
    else if (sa > 0)
    {
        yajirusi = "<font color='blue'>↑";
    }
    else
    {
        yajirusi = "±";
    }
    cnt = 0;
    str = "";
    sa  = "" + sa;
    for (i=sa.length-1; i>=0; i--)
    {
        str = sa.charAt(i) + str;
        cnt++;
        if (((cnt % 3) == 0) && (i != 0)) str = ","+str;
    }
    document.write(yajirusi+str+"</font>");
</script>
<%}%>
          </td>
        </tr>
        <tr>
          <td>
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr>
                <td align="center" valign="middle" nowrap class="tableLN">&nbsp;</td>
                <td align="center" valign="middle" nowrap class="tableLN">組数</td>
                <td align="center" valign="bottom" nowrap class="tableLN">回転数</td>
                <td align="center" valign="bottom" nowrap class="tableRN">客単価</td>
              </tr>
              <tr>
                <td align="center" valign="middle" nowrap class="tableLN">休憩</td>
                <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(42*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesRestCount ) %><%}%></td>
                <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if(DemoMode){%>2.10<%}else{%><%= (float)ownerinfo.SalesRestRate / (float)100 %><% if((float)ownerinfo.SalesRestRate%10==0){%>0<%}%><%}%></td>
                <td align="right"  valign="middle" class="size14 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(6780*per))%><%}else{%><%= Kanma.get(ownerinfo.SalesRestPrice) %><%}%></td>
              </tr>
              <tr>
                <td align="center" valign="middle" nowrap class="tableLN">宿泊</td>
                <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(18*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesStayCount ) %><%}%></td>
                <td align="right"  valign="middle" class="size14 tableLW" nowrap><%if(DemoMode){%>0.90<%}else{%><%= (float)ownerinfo.SalesStayRate / (float)100 %><% if((float)ownerinfo.SalesStayRate%10==0){%>0<%}%><%}%></td>
                <td align="right"  valign="middle" class="size14 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(9540*per))%><%}else{%><%= Kanma.get(ownerinfo.SalesStayPrice) %><%}%></td>
              </tr>
              <tr>
                <td align="center" valign="middle" nowrap class="tableLN">合計</td>
                <td align="right"  valign="middle" class="size14 tableLB" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(60*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesTotalCount ) %><%}%></td>
                <td align="right"  valign="middle" class="size14 tableLB" nowrap><%if(DemoMode){%>3.00<%}else{%><%= (float)ownerinfo.SalesTotalRate / (float)100 %><% if((float)ownerinfo.SalesTotalRate%10==0){%>0<%}%><%}%></td>
                <td align="right"  valign="middle" class="size14 tableRB" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8750*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesTotalPrice) %><%}%></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
    <td width="2" valign="top">&nbsp;</td>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="tableLWU" nowrap><div class="space2">ﾋﾞｼﾞﾀｰ単価</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRWU" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(8460*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesVisitorPrice) %><%}%></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap><div class="space2">ﾒﾝﾊﾞｰ単価</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(7530*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesMemberPrice) %><%}%></td>
        </tr>
<%
        if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
        {
%>
        <tr>
          <td class="tableLW" nowrap><div class="space2">室単価平均</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesRoomPrice) %><%}%></td>
        </tr>
        <tr>
          <td class="tableLG" nowrap><div class="space2">室単価累計</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per*30)))%><%}else{%><%= Kanma.get(ownerinfo.SalesRoomTotalPrice) %><%}%></td>
        </tr>
<%
        }
        else
        {
%>
        <tr>
          <td class="tableLW" nowrap><div class="space2">部屋単価</div>
          </td>
          <td align="right" valign="middle" class="size14 tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(26250*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesRoomPrice) %><%}%></td>
        </tr>
<%
        }
%>
      </table>
      <table>
        <tr>
          <td width="2" valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="6"></td>
        </tr>
      </table>
      <table>
        <tr>
          <td align="center" valign="middle"><input name="inoutdetail" type="button" class="inoutbtn2" onClick="MM_openBrWindow('inout_detail.jsp?startDate=<%=ownerinfo.SalesGetStartDate%>&endDate=<%=ownerinfo.SalesGetEndDate%>&HotelId=<%=param_hotelid%>','INOUT組数','menubar=yes,scrollbars=yes,resizable=yes,width=340,height=600')" value="IN/OUT組数">
          </td>
        </tr>
      </table>
    </td>
  </tr>
<%
    }
%>
</table>