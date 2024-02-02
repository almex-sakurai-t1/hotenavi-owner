<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String hotelid   = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    String RoomCount = ReplaceString.getParameter(request,"RoomCount");
    if(CheckString.isValidParameter(RoomCount) && !CheckString.numCheck(RoomCount))
    {
        response.sendError(400);
        return;
    }
%>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="size12" nowrap>

<%
    if( ownerinfo.RoomCode > 1 )
    {
%>
      <a href="roomdetail.jsp?HotelId=<%= hotelid %>&RoomCode=<%= ownerinfo.RoomCode - 1 %>" onMouseOver="MM_swapImage('fwd','','../../common/pc/image/yaji_w_l_o.gif',1)" onMouseOut="MM_swapImgRestore()"><IMG src="../../common/pc/image/yajirushiGrey2.gif" name="fwd" width="15" height="13" border="0" align="absmiddle" id="fwd" onmouseover="MM_swapImage('fwd','','../../common/pc/image/yaji_w_l_o.gif',1)" onmouseout="MM_swapImgRestore()"><img src="../../common/pc/image/spacer.gif" width="4" height="12" align="absmiddle" border="0">‘O‚Ì•”‰®</a>
<%
    }
%>

    </td>
    <td><img src="../../common/pc/image/spacer.gif" width="40" height="14"></td>
    <td class="size12" nowrap>

<%
    if( ownerinfo.RoomCode < Integer.parseInt(RoomCount))
    {
%>
      <a href="roomdetail.jsp?HotelId=<%= hotelid %>&RoomCode=<%= ownerinfo.RoomCode + 1 %>" onMouseOver="MM_swapImage('nxt','','../../common/pc/image/yajirushiGrey_f2.gif',1)" onMouseOut="MM_swapImgRestore()">ŽŸ‚Ì•”‰®<img src="../../common/pc/image/spacer.gif" width="4" height="12" border="0" align="absmiddle"><IMG src="../../common/pc/image/yajirushiGrey.gif" name="nxt" width="15" height="13" border="0" align="absmiddle" id="nxt" onmouseover="MM_swapImage('nxt','','../../common/pc/image/yajirushiGrey_f2.gif',1)" onmouseout="MM_swapImgRestore()"></a>
<%
    }
%>
    </td>
  </tr>
</table>

