<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String hotelid = ReplaceString.getParameter(request,"NowHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
%>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td height="16" class="size12"><IMG src="../../common/pc/image/spacer.gif" width="20" height="12"></td>
    </tr>
    <tr> 
      <td height="4"><IMG src="../../common/pc/image/spacer.gif" width="20" height="4"></td>
    </tr>
  </table>

      <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	  <td width="50%" align="left" valign="middle" nowrap class="tableLWU"><div class="space2">èhîë</div>
	  </td>
	  <td width="50%" align="right" valign="middle" nowrap class="tableRWU"><%= Kanma.get(ownerinfo.SalesStayCount ) %></td>
	</tr>
	<tr>
	  <td width="50%" nowrap class="tableLG"><div class="space2">ãxåe</div>
	  </td>
	  <td width="50%" align="right" valign="middle" nowrap class="tableRG"><%= Kanma.get(ownerinfo.SalesRestCount ) %></td>
	</tr>
	<tr>
	  <td width="50%" nowrap class="tableLP"><div class="space2">çáåv</div>
	  </td>
	  <td width="50%" align="right" valign="middle" nowrap class="tableRP"><%= Kanma.get(ownerinfo.SalesTotalCount ) %></td>
	</tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	  <td height="6"><img src="../../common/pc/image/spacer.gif" width="40" height="6"></td>
	</tr>
	<tr>
	  <td align="center" valign="middle"><input name="inoutdetail" type="button" class="inoutbtn" onClick="MM_openBrWindow('inout_detail.jsp?HotelId=<%= hotelid %>','INOUTëgêî','menubar=yes,scrollbars=yes,resizable=yes,width=340,height=600')" value="IN/OUTëgêî">
	  </td>
	</tr>
	<tr>
	  <td>&nbsp;</td>
	</tr>
      </table>

