<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.owner.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int    i;
    NumberFormat nf = new DecimalFormat("00");
    OwnerInfo    comp1data;
    OwnerInfo    comp2data;

    String hotelid = ReplaceString.getParameter(request,"NowHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    String hotelname = ReplaceString.getParameter(request,"NowHotelName");
    if( hotelname == null )
    {
        hotelname = "";
    }

    comp1data = new OwnerInfo();
    comp2data = new OwnerInfo();

    // éÊìæÇµÇΩÉfÅ[É^ÇÉZÉbÉVÉáÉìëÆê´Ç©ÇÁéÊìæ
    comp1data = (OwnerInfo)session.getAttribute("comp1");
    comp2data = (OwnerInfo)session.getAttribute("comp2");
%>

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20">
            <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="140" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF"><%= hotelname %></font></td>
                <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
                <td height="20">
                  <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
                </td>
              </tr>
            </table>
          </td>
          <td width="3">&nbsp;</td>
        </tr>
        <!-- Ç±Ç±Ç©ÇÁï\ -->
        <tr>
          <td align="center" valign="top" bgcolor="#F8F8E7"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><img src="/common/pc/image/spacer.gif" width="400" height="12"></td>
              </tr>
            </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td></td>
                </tr>
                <tr>
                  <td valign="top"><table border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
                            <tr>
                              <td colspan="3" align="left" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="486" height="1"></td>
                            </tr>
                            <tr>
                              <td width="1" align="left" valign="top" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                              <td align="left" valign="top"><table border="0" cellpadding="0" cellspacing="0">
                                  <tr>
                                    <td width="10"><img src="/common/pc/image/spacer.gif" width="10" height="10"></td>
                                    <td width="484">&nbsp;</td>
                                    <td width="10">&nbsp;</td>
                                  </tr>
                                  <tr>
                                    <td width="10"><img src="/common/pc/image/spacer.gif" width="10" height="10"></td>
                                    <td width="484"><table width="484" border="0" cellpadding="0" cellspacing="0">
                                        <tr align="center" valign="middle">
                                          <td width="76" height="26">&nbsp;</td>
                                          <td width="4"><img src="/common/pc/image/spacer.gif" width="4" height="26"></td>
                                          <td width="200" height="26" bgcolor="#63636B" class="size12"><font color="#FFFFFF"><%= comp1data.SalesGetStartDate / 10000 %>îN<%= comp1data.SalesGetStartDate / 100 % 100 %>åé</font></td>
                                          <td width="4" height="26"><img src="/common/pc/image/spacer.gif" width="4" height="26"></td>
                                          <td width="200" height="26" bgcolor="#63636B" class="size12"><font color="#FFFFFF"><%= comp2data.SalesGetStartDate / 10000 %>îN<%= comp2data.SalesGetStartDate / 100 % 100 %>åé</font></td>
                                        </tr>
                                        <tr>
                                          <td colspan="5"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                                        </tr>
                                        <tr>
                                          <td width="76" height="26" align="left" valign="middle" bgcolor="#DDDDDD" class="size12"><font color="#3C3C46">&nbsp;îÑè„</font></td>
                                          <td width="4" align="left" valign="middle">&nbsp;</td>
                                          <td width="200" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get(comp1data.SalesTotal) %>â~&nbsp;</td>
                                          <td width="4" height="26"><img src="/common/pc/image/spacer.gif" width="4" height="26"></td>
                                          <td width="200" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get(comp2data.SalesTotal) %>â~&nbsp;</td>
                                        </tr>
                                        <tr>
                                          <td colspan="5"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                                        </tr>
                                        <tr>
                                          <td width="76" height="26" align="left" valign="middle" bgcolor="#C6CBD0" class="size12"><font color="#3C3C46">&nbsp;âÒì]</font></td>
                                          <td width="4" align="left" valign="middle">&nbsp;</td>
                                          <td width="200" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= (float)comp1data.SalesTotalRate / (float)100 %>âÒì]&nbsp;</td>
                                          <td width="4" height="26">&nbsp;</td>
                                          <td width="200" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= (float)comp2data.SalesTotalRate / (float)100 %>âÒì]&nbsp;</td>
                                        </tr>
                                        <tr>
                                          <td colspan="5"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                                        </tr>
                                        <tr>
                                          <td width="76" height="26" align="left" valign="middle" bgcolor="#DDDDDD" class="size12"><font color="#3C3C46">&nbsp;ãqíPâø</font></td>
                                          <td width="4" align="left" valign="middle">&nbsp;</td>
                                          <td width="200" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get(comp1data.SalesTotalPrice) %>â~&nbsp;</td>
                                          <td width="4" height="26"><img src="/common/pc/image/spacer.gif" width="4" height="26"></td>
                                          <td width="200" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get(comp2data.SalesTotalPrice) %>â~&nbsp;</td>
                                        </tr>
                                        <tr>
                                          <td colspan="5"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                                        </tr>
                                        <tr>
                                          <td width="76" height="26" align="left" valign="middle" bgcolor="#C6CBD0" class="size12"><font color="#3C3C46">&nbsp;ïîâÆíPâø</font></td>
                                          <td width="4" align="left" valign="middle">&nbsp;</td>
                                          <td width="200" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= Kanma.get(comp1data.SalesRoomPrice) %>â~&nbsp;</td>
                                          <td width="4" height="26">&nbsp;</td>
                                          <td width="200" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= Kanma.get(comp2data.SalesRoomPrice) %>â~&nbsp;</td>
                                        </tr>
                                      </table>
                                    </td>
                                    <td width="10"><img src="/common/pc/image/spacer.gif" width="10" height="10"></td>
                                  </tr>
                                  <tr>
                                    <td colspan="3"><img src="/common/pc/image/spacer.gif" width="10" height="8"></td>
                                  </tr>
                                </table>
                              </td>
                              <td width="1" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                            </tr>
                            <tr>
                              <td width="1" height="1" align="left" valign="top" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                              <td height="1" align="left" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                              <td height="1" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                            </tr>
                            <tr>
                              <td width="1" align="left" valign="top" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                              <td align="left"><img src="/common/pc/image/spacer.gif" width="484" height="2"></td>
                              <td bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                            </tr>
                            <tr>
                              <td width="1" align="left" valign="top" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                              <td align="left" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                              <td bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                            </tr>
                            <tr>
                              <td width="1" align="left" valign="top" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                              <td width="100%" align="left" valign="top"><table border="0" cellpadding="0" cellspacing="0">
                                  <tr>
                                    <td colspan="3"><img src="/common/pc/image/spacer.gif" width="10" height="8"></td>
                                  </tr>
                                  <tr>
                                    <td width="10"><img src="/common/pc/image/spacer.gif" width="10" height="10"></td>
                                    <td width="484"><table width="484" border="0" cellpadding="0" cellspacing="0">
                                        <tr align="center" valign="middle">
                                          <td height="26" colspan="3" bgcolor="#63636B" class="size12"><font color="#FFFFFF">ï™êÕÉfÅ[É^</font></td>
                                        </tr>
                                        <tr>
                                          <td colspan="3"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                                        </tr>
                                        <tr>
                                          <td width="318" height="26" align="left" valign="middle" bgcolor="#DDDDDD" class="size12"><font color="#3C3C46">&nbsp;îÑè„</font></td>
<%
    if( (comp1data.SalesTotal - comp2data.SalesTotal) > 0 )
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#DDDDDD"><img src="/common/pc/image/down.gif" width="14" height="13"></td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get((comp2data.SalesTotal - comp1data.SalesTotal)) %>â~&nbsp;</td>
<%
    }
    else if( (comp1data.SalesTotal - comp2data.SalesTotal) == 0 )
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#DDDDDD">-</td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get((comp2data.SalesTotal - comp1data.SalesTotal)) %>â~&nbsp;</td>
<%
    }
    else
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#DDDDDD"><img src="/common/pc/image/up.gif" width="14" height="13"></td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get((comp2data.SalesTotal - comp1data.SalesTotal)) %>â~&nbsp;</td>
<%
    }
%>
                                        </tr>
                                        <tr>
                                          <td colspan="3"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                                        </tr>
                                        <tr>
                                          <td width="318" height="26" align="left" valign="middle" bgcolor="#C6CBD0" class="size12"><font color="#3C3C46">&nbsp;âÒì]</font></td>
<%
    if( (comp1data.SalesTotalRate - comp2data.SalesTotalRate) > 0 )
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#C6CBD0"><img src="/common/pc/image/down.gif" width="14" height="13"></td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= (float)(comp2data.SalesTotalRate - comp1data.SalesTotalRate) / (float)100 %>âÒì]&nbsp;</td>
<%
    }
    else if( (comp1data.SalesTotalRate - comp2data.SalesTotalRate) == 0 )
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#C6CBD0">-</td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= (float)(comp2data.SalesTotalRate - comp1data.SalesTotalRate) / (float)100 %>âÒì]&nbsp;</td>
<%
    }
    else
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#C6CBD0"><img src="/common/pc/image/up.gif" width="14" height="13"></td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= (float)(comp2data.SalesTotalRate - comp1data.SalesTotalRate) / (float)100 %>âÒì]&nbsp;</td>
<%
    }
%>
                                        </tr>
                                        <tr>
                                          <td colspan="3"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                                        </tr>
                                        <tr>
                                          <td width="318" height="26" align="left" valign="middle" bgcolor="#DDDDDD" class="size12"><font color="#3C3C46">&nbsp;ãqíPâø</font></td>

<%
    if( (comp1data.SalesTotalPrice - comp2data.SalesTotalPrice) > 0 )
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#DDDDDD"><img src="/common/pc/image/down.gif" width="14" height="13"></td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get((comp2data.SalesTotalPrice - comp1data.SalesTotalPrice)) %>â~&nbsp;</td>
<%
    }
    else if( (comp1data.SalesTotalPrice - comp2data.SalesTotalPrice) == 0 )
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#DDDDDD">-</td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get((comp2data.SalesTotalPrice - comp1data.SalesTotalPrice)) %>â~&nbsp;</td>
<%
    }
    else
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#DDDDDD"><img src="/common/pc/image/up.gif" width="14" height="13"></td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#DDDDDD" class="size12"><%= Kanma.get((comp2data.SalesTotalPrice - comp1data.SalesTotalPrice)) %>â~&nbsp;</td>
<%
    }
%>
                                        </tr>
                                        <tr>
                                          <td colspan="3"><img src="/common/pc/image/spacer.gif" width="484" height="1"></td>
                                        </tr>
                                        <tr>
                                          <td width="318" height="26" align="left" valign="middle" bgcolor="#C6CBD0" class="size12"><font color="#3C3C46">&nbsp;ïîâÆíPâø</font></td>
<%
    if( (comp1data.SalesRoomPrice - comp2data.SalesRoomPrice) > 0 )
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#C6CBD0"><img src="/common/pc/image/down.gif" width="14" height="13"></td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= Kanma.get((comp2data.SalesRoomPrice - comp1data.SalesRoomPrice)) %>â~&nbsp;</td>
<%
    }
    else if( (comp1data.SalesRoomPrice - comp2data.SalesRoomPrice) == 0 )
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#C6CBD0">-</td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= Kanma.get((comp2data.SalesRoomPrice - comp1data.SalesRoomPrice)) %>â~&nbsp;</td>
<%
    }
    else
    {
%>
                                          <td width="26" height="26" align="center" valign="middle" bgcolor="#C6CBD0"><img src="/common/pc/image/up.gif" width="14" height="13"></td>
                                          <td width="140" height="26" align="right" valign="middle" bgcolor="#C6CBD0" class="size12"><%= Kanma.get((comp2data.SalesRoomPrice - comp1data.SalesRoomPrice)) %>â~&nbsp;</td>
<%
    }
%>
                                        </tr>
                                      </table>
                                    </td>
                                    <td width="10"><img src="/common/pc/image/spacer.gif" width="10" height="10"></td>
                                  </tr>
                                  <tr>
                                    <td colspan="3"><img src="/common/pc/image/spacer.gif" width="10" height="10"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                                  </tr>
                                </table>
                              </td>
                              <td width="1" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                            </tr>
                            <tr>
                              <td colspan="3" align="left" bgcolor="#3C3C46"><img src="/common/pc/image/spacer.gif" width="1" height="1"></td>
                            </tr>
                          </table>
                        </td>
                        <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
                      </tr>
                    </table>
                      <img src="/common/pc/image/spacer.gif" width="8" height="1"></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
              </table>
          </td>
          <td width="3" valign="top" align="left" height="100%">
            <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td><img src="/common/pc/image/tab_kado.gif" width="3" height="3"></td>
              </tr>
              <tr>
                <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td height="3" bgcolor="#999999">
            <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="3"><img src="/common/pc/image/tab_kado2.gif" width="3" height="3"></td>
                <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
              </tr>
            </table>
          </td>
          <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
        </tr>
        <!-- Ç±Ç±Ç‹Ç≈ -->
      </table>

