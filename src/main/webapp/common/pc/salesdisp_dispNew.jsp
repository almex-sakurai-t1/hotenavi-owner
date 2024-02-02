<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String    hotelid;
    String    hotelname;
    String    salesLayout = "1";
    String    pointtotal;

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

    pointtotal = ReplaceString.getParameter(request,"PointTotal");
    if( pointtotal == null )
    {
        pointtotal = "0";
    }

 
%>
<!-- 店舗表示 -->
<a name="<%= hotelid %>"></a>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="150" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF"><%= hotelname %></font></td>
          <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td height="20">
            <div><img src="../../common/pc/image/spacer.gif" width="12" height="16" align="absmiddle"><a href="#pagetop" class="navy10px">&gt;&gt;このページのトップへ</a></div>
          </td>
          <td height="20" align="right">
          </td>
        </tr>
      </table>
    </td>
    <td width="3">&nbsp;</td>
  </tr>
<!-- ここから表 -->
  <tr>
    <td bgcolor="#BBBBBB">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td height="3" align="left" valign="top" colspan=3><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td width="3"><img src="../../common/pc/image/bg.gif" width="3" height="3"></td>
        </tr>
        <tr>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="20"></td>
          <td valign="top" colspan=3>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="20"></td>
                <td height="20">
                  <div class="size14">
                    <font color="#FFFFFF">
                  <%-- 日付表示パーツ --%>
                      <jsp:include page="../../common/pc/salesdisp_datedisp.jsp" flush="true" />
                    </font>
                  </div>
                </td>
                <form action="salesdisp_select.jsp" method="post">
                <td height="20" align="right">
                  <div class="size14">
                    <font color="#FFFFFF">
                    <%if (ReplaceString.getParameter(request,"KindfromList")!=null) {%> 
                 <%-- 戻る機能--%>
                      <input type="button" value="戻る" onclick="history.back();">
                    <%}else if(ReplaceString.getParameter(request,"HotelIdfromGroup")!=null){%>
                      <input type="button" value="戻る" onclick="history.back();">
                    <%}%>
                    </font>
                  </div>
                </td></form>
              </tr>
            </table>
          </td>
          <td width="8" valign="top">&nbsp;</td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
        <tr>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td valign="top" colspan=3>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr align="left" valign="top">
                <td width="6"><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                <td>
              <%-- 売上情報取得・表示パーツ --%>
<%
    if (salesLayout.equals("1"))
    {
%>
                  <jsp:include page="salesdisp_salesdisp1.jsp" flush="true" >
                     <jsp:param name="NowHotel" value="<%= hotelid %>" />
                     <jsp:param name="PointTotal" value="<%= pointtotal %>" />
                  </jsp:include>
<%
    }
    else
    {
%>
                  <jsp:include page="salesdisp_salesdisp2.jsp" flush="true" >
                     <jsp:param name="NowHotel" value="<%= hotelid %>" />
                  </jsp:include>
<%
    }
%>
                </td>
              </tr>
            </table>
          </td>
          <td width="8" valign="top">&nbsp;</td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
        <tr>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="3" align="left" valign="top" colspan=3><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
        <tr>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td valign="top" width="45%">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr align="left" valign="top">
                <td width="6"><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                <td>
              <%-- 売上詳細情報取得・表示パーツ --%>
                  <jsp:include page="salesdisp_detailNew.jsp" flush="true" />
                </td>
              </tr>
            </table>
          </td>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="3"></td>
          <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0"  bgcolor="#A8BEBC">
              <tr>
                <td><img src="../../common/pc/image/spacer.gif" width="6" height="100"></td>
                <td valign="top">
              <%-- 締単位IN/OUT組数表示パーツ --%>
                    <jsp:include page="salesdisp_closeinoutNew.jsp" flush="true" />
                </td>
                <td><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
                <td valign="top">
              <%-- 部屋情報表示パーツ --%>
                   <jsp:include page="salesdisp_roomNew.jsp" flush="true" />
                </td>
              </tr>
            </table>
          </td>
          <td width="8" valign="top">&nbsp;</td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
        <tr>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td height="3" align="left" valign="top" colspan=3><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
          <td width="3"><img src="../../common/pc/image/grey.gif" width="3" height="8"></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="3" bgcolor="#BBBBBB">
      <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="3"><img src="../../common/pc/image/bg.gif" width="3" height="3"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
          <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<!-- 1店舗目ここまで -->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="200" height="6"></td>
  </tr>
</table>
