<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    StringFormat    sf;
    sf = new StringFormat();

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("demo"))
    {
        DemoMode = true;
    }
    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;

    int hostkind = 0;
    int newsales = 0;

    String hotelid = ReplaceString.getParameter(request,"NowHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        connection  = DBConnection.getConnection();
        final String query = "SELECT * FROM hotel WHERE hotel_id=?";
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
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>

<%
    // �V�V���ł��V�V�����[�p���W���[���������ꍇ
    if(newsales == 1)
    {
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle" nowrap class="tableLN">���@��</td>
    <td align="center" valign="middle" nowrap class="tableRN">���@�z</td>
  </tr>

<%
    int i;
    int line = 0;
    for( i = 0 ; i < ownerinfo.ManualSalesDetailCount ; i++ )
    {
        if (!ownerinfo.ManualSalesDetailName[i].equals("") || ownerinfo.ManualSalesDetailAmount[i] != 0 )
        {
            line++;
%>
  <tr>
    <td class="tableL<%if(line%2 ==0){%>W<%}else{%>G<%}%>" nowrap><div class="space2"><%= ownerinfo.ManualSalesDetailName[i] %></div>
    </td>
    <td align="right" valign="middle" class="tableR<%if(line%2 ==0){%>W<%}else{%>G<%}%>" nowrap><%= Kanma.get(ownerinfo.ManualSalesDetailAmount[i]) %></td>
  </tr>
<%
        }
    }
%>

  <tr>
    <td class="tableLW" nowrap><div class="space2">�@</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap>�@</td>
  </tr>
  <tr>
    <td class="tableLG" nowrap><div class="space2">�����v</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%= Kanma.get(ownerinfo.ManualSalesDetailTotal) %></td>
  </tr>
  <tr>
    <td class="tableLW" nowrap><div class="space2">�i���Łj</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%= Kanma.get(ownerinfo.ManualSalesDetailTaxIn) %></td>
  </tr>
</table>

<%
    }
    else
    {
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#D5D8CB">
  <tr>
    <td height="16" class="size12" colspan=7>&nbsp;&nbsp;<font color="#660033"><strong>������ڍ�</strong></font><img src="../../common/pc/image/spacer.gif" width="20" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td align="center" valign="middle" nowrap class="tableLN">���@��</td>
    <td align="center" valign="middle" nowrap class="tableLN">���@�z</td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td align="center" valign="middle" nowrap class="tableLN">���@��</td>
    <td align="center" valign="middle" nowrap class="tableRN">���@�z</td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">�h��</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(185000*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailStay) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">���H</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(13450*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailMeat) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">�h���O����</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(6700*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailStayBeforeOver) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">�o�O</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailDelivery) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">�h���㉄��</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(35500*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailStayAfterOver) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">�R���r�j</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(9135*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailConveni) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td align="left" valign="middle" class="tableLW" nowrap><div class="space2">�x�e</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(206600*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailRest) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">�①��</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(7410*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailRef) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">�x�e�O����</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailRestBeforeOver) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">�}���`���f�B�A</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailMulti) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td align="left" valign="middle" class="tableLW" nowrap><div class="space2">�x�e�㉄��</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(112560*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailRestAfterOver) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">�̔����i</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(2000*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailSales) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td align="left" valign="middle" class="tableLG" nowrap><div class="space2">&nbsp;</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;</td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">�����^�����i</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailRental) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td align="left" valign="middle" class="tableLW" nowrap><div class="space2">����</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(-5000*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailDiscount) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">���΂�</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailCigarette) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">����</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailExtra) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">�d�b</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailTel) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">�����o�[����</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailMember) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">���̑�</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(4560*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailEtc) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">��d��</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailService) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">&nbsp;</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;</td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">�����</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(0*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailStax) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td align="left" valign="middle" class="tableLW" nowrap><div class="space2">&nbsp;</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;</td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLG" nowrap><div class="space2">������</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(615*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailAdjust) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td align="left" valign="middle" class="tableLG" nowrap><div class="space2">&nbsp;</div>
    </td>
    <td align="right" valign="middle" class="tableRG" nowrap>&nbsp;</td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
<%if(ownerinfo.SalesDetailTaxRate1 != 0){%>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">(<%=sf.rightFitFormat(((float)ownerinfo.SalesDetailTaxRate1 / (float)10+""),4) %>% �Ώ�)</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%= Kanma.get(ownerinfo.SalesDetailTaxableAmount1) %></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td align="left" valign="middle" class="tableLW" nowrap><div class="space2">&nbsp;</div></td>
    <td align="right" valign="middle" class="tableRW" nowrap>&nbsp;</td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
<%}%>
<%if(ownerinfo.SalesDetailTaxRate2 != 0){%>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableL<%=ownerinfo.SalesDetailTaxRate1!=0?"G":"W"%>" nowrap><div class="space2">(<%=sf.rightFitFormat(((float)ownerinfo.SalesDetailTaxRate2 / (float)10+""),4) %>% �Ώ�)</div>
    </td>
    <td align="right" valign="middle" class="tableR<%=ownerinfo.SalesDetailTaxRate1!=0?"G":"W"%>" nowrap><%= Kanma.get(ownerinfo.SalesDetailTaxableAmount2) %></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td align="left" valign="middle" class="tableL<%=ownerinfo.SalesDetailTaxRate1!=0?"G":"W"%>" nowrap><div class="space2">&nbsp;</div></td>
    <td align="right" valign="middle" class="tableR<%=ownerinfo.SalesDetailTaxRate1!=0?"G":"W"%>" nowrap>&nbsp;</td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
<%}%>
  <tr>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
    <td class="tableLN" nowrap><div class="space2">�����v</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(525000*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailTotal) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="1" height="4"></td>
    <td class="tableLW" nowrap><div class="space2">�i������Łj</div>
    </td>
    <td align="right" valign="middle" class="tableRW" nowrap><%if(DemoMode){%><%=Kanma.get((int)(Math.round(25000*per)))%><%}else{%><%= Kanma.get(ownerinfo.SalesDetailStaxIn) %><%}%></td>
    <td height="16" class="size12" ><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
  </tr>
  <tr>
    <td height="16" class="size12" colspan=7><img src="../../common/pc/image/spacer.gif" width="20" height="4"></td>
  </tr>
</table>
<%
    }
%>

