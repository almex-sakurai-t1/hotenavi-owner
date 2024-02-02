<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<%
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("demo"))
    {
        DemoMode = true;
    }
    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;

    DateEdit df = new DateEdit();
    String now_date = df.getDate(1);
    String now_time = df.getTime(2);


    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
%>
  <tr>
    <td class="size12" nowrap colspan=2><font color="#006600"><strong>Åöåªç›èÛãµ
    </strong></font></td>
    <td>&nbsp;
    </td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
  </tr>
  <tr>
    <td colspan=3>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td height="16" nowrap class="size12"><%= now_date %>&nbsp;<%= now_time %>åªç›</td>
	  </tr>
	  <tr>
	    <td height="4" nowrap class="size12"><img src="../../common/pc/image/spacer.gif" width="20" height="4"></td>
	  </tr>
	</table>
    </td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
  </tr>
  <tr>
    <td class="size12" nowrap colspan=3><font color="#006600"><strong>í˜íPà IN/OUTêî
    </strong></font></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
  </tr>
  <tr>
    <td width="50%" align="left" valign="middle" nowrap class="tableLWU"><div class="space2">í˜Çﬂå„&nbsp;IN</div>
    </td>
    <td width="50%" align="right" valign="middle" nowrap class="tableRWU"><%if(DemoMode){%>13<%}else{%><%= Kanma.get(ownerinfo.AddupInOutAfterIn) %><%}%></td>
  </tr>
  <tr>
    <td width="50%" nowrap class="tableLG"><div class="space2">í˜ÇﬂëO&nbsp;IN</div>
    </td>
    <td width="50%" align="right" valign="middle" nowrap class="tableRG"><%if(DemoMode){%>12<%}else{%><%= Kanma.get(ownerinfo.AddupInOutBeforeIn) %><%}%></td>
  </tr>
  <tr>
    <td width="50%" nowrap class="tableLW"><div class="space2">ëç&nbsp;OUT</div>
    </td>
    <td width="50%" align="right" valign="middle" nowrap class="tableRW"><%if(DemoMode){%>7<%}else{%><%= Kanma.get(ownerinfo.AddupInOutAllOut) %><%}%></td>
  </tr>
  <tr>
    <td width="50%" nowrap class="tableLG"><div class="space2">í˜ÇﬂëO&nbsp;OUT</div>
    </td>
    <td width="50%" align="right" valign="middle" nowrap class="tableRG"><%if(DemoMode){%>7<%}else{%><%= Kanma.get(ownerinfo.AddupInOutBeforeOut) %><%}%></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
  </tr>
</table>
