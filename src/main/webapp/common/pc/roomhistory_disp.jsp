<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int    i;
    NumberFormat nf = new DecimalFormat("00");

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
%>

<table border="0" cellspacing="0" cellpadding="0">
<tr>
  <td colspan="3" nowrap><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
	<td height="17" align="left" valign="middle" nowrap bgcolor="#000066" class="size12"><font color="#FFFFFF">&nbsp;<%= hotelname %></font></td>
      </tr>
    </table>
  </td>
</tr>
<tr>
  <td align="right" valign="top" nowrap bgcolor="#FFFFFF">&nbsp;</td>
  <td align="right" valign="top" nowrap bgcolor="#FFFFFF">&nbsp;</td>
  <td valign="top" nowrap bgcolor="#FFFFFF">&nbsp;</td>
</tr>
<tr>
  <td width="44" align="right" valign="top" nowrap bgcolor="#FFFFFF">
    <table width="44" border="0" cellspacing="0" cellpadding="0">

      <tr>
	<td align="right" valign="top" nowrap><img src="../../common/pc/image/spacer.gif" width="13" height="14"></td>
      </tr>
<%
    for( i = 0 ; i < ownerinfo.OWNERINFO_TIMEHOURMAX ; i++ )
    {
%>
      <tr>
	<td height="26" align="right" valign="middle" nowrap class="size12"><font color="#3C3C46"><%= ownerinfo.RoomHistoryTime[i] / 100 %>:<%= nf.format(ownerinfo.RoomHistoryTime[i] % 100) %></font></td>
      </tr>
      <tr>
        <td align="right" valign="middle" nowrap><img src="../../common/pc/image/spacer.gif" width="44" height="1"></td>
      </tr>
<%
    }
%>
	<tr>
	<td height="26" align="right" valign="middle" nowrap class="size12"><font color="#3C3C46">çá åv</font></td>
      </tr>
      <tr>
        <td align="right" valign="middle" nowrap><img src="../../common/pc/image/spacer.gif" width="44" height="1"></td>
      </tr>
    </table>
  </td>
  <td align="right" valign="top" nowrap bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="6" height="100"></td>
  <td valign="top" nowrap bgcolor="#FFFFFF">
    <table width="195" border="0" cellspacing="0" cellpadding="0">
      <tr>
	<td width="13"><img src="../../common/pc/image/spacer.gif" width="13" height="12"></td>
	<td width="36" align="center" nowrap class="size10"><font color="#6666CC">ãÛé∫</font></td>
	<td width="36" align="center" nowrap class="size10"><font color="#CC3366">ç›é∫</font></td>
	<td width="36" align="center" nowrap class="size10"><font color="#66CCFF">èÄîı</font></td>
	<td width="36" align="center" nowrap class="size10"><font color="#993333">îÑé~</font></td>
	<td width="36" align="center" nowrap class="size10"><font color="#000066">IN</font></td>
	<td width="36" align="center" nowrap class="size10"><font color="#CC0000">OUT</font></td>
	<td width="44" align="center" nowrap class="size10">ãÛé∫ó¶</td>
	<td width="13"><img src="../../common/pc/image/spacer.gif" width="13" height="12"></td>
	<td width="13" bgcolor="#FFFFFF">&nbsp;</td>
      </tr>
      <tr>
	<td height="2" colspan="11"><img src="../../common/pc/image/spacer.gif" width="100" height="2"></td>
      </tr>
<%
    int    in_total=0;
    int    out_total=0;

    for( i = 0 ; i < ownerinfo.OWNERINFO_TIMEHOURMAX ; i++ )
    {
        nf = new DecimalFormat("##0.#");

            in_total  = in_total  + ownerinfo.InOutIn[i];
            out_total = out_total + ownerinfo.InOutOut[i];

        if( (i % 2) == 0 )
        {
%>
      <tr>
	<td width="13" height="26" bgcolor="#DDDDDD"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#DDDDDD" class="size12"><%= ownerinfo.RoomHistoryEmpty[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#DDDDDD" class="size12"><%= ownerinfo.RoomHistoryExist[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#DDDDDD" class="size12"><%= ownerinfo.RoomHistoryClean[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#DDDDDD" class="size12"><%= ownerinfo.RoomHistoryStop[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#DDDDDD" class="size12"><%= ownerinfo.InOutIn[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#DDDDDD" class="size12"><%= ownerinfo.InOutOut[i] %></td>
	<td width="44" align="center" valign="middle" nowrap bgcolor="#DDDDDD" class="size12"><%= nf.format(((float)ownerinfo.RoomHistoryEmpty[i] / (float)ownerinfo.RoomHistoryRoomCount) * 100) %>%</td>
	<td width="13" height="26" bgcolor="#DDDDDD"><img src="../../common/pc/image/spacer.gif" width="13" height="12"></td>
	<td width="13" bgcolor="#FFFFFF">&nbsp;</td>
      </tr>
      <tr>
	<td colspan="11"><img src="../../common/pc/image/spacer.gif" width="1" height="1"></td>
      </tr>
<%
        }
        else
        {
%>
      <tr>
	<td width="13" height="26" bgcolor="#C6CBD0"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#C6CBD0" class="size12"><%= ownerinfo.RoomHistoryEmpty[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#C6CBD0" class="size12"><%= ownerinfo.RoomHistoryExist[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#C6CBD0" class="size12"><%= ownerinfo.RoomHistoryClean[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#C6CBD0" class="size12"><%= ownerinfo.RoomHistoryStop[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#C6CBD0" class="size12"><%= ownerinfo.InOutIn[i] %></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#C6CBD0" class="size12"><%= ownerinfo.InOutOut[i] %></td>
	<td width="44" align="center" valign="middle" nowrap bgcolor="#C6CBD0" class="size12"><%= nf.format(((float)ownerinfo.RoomHistoryEmpty[i] / (float)ownerinfo.RoomHistoryRoomCount) * 100) %>%</td>
	<td width="13" height="26" bgcolor="#C6CBD0"><img src="../../common/pc/image/spacer.gif" width="13" height="12"></td>
	<td width="13" bgcolor="#FFFFFF">&nbsp;</td>
      </tr>
      <tr>
	<td colspan="11"><img src="../../common/pc/image/spacer.gif" width="1" height="1"></td>
      </tr>
<%
        }
    }
%>
	<tr>
	<td width="13" height="26" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#000066" class="size12">&nbsp;</td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#000066" class="size12">&nbsp;</td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#000066" class="size12">&nbsp;</td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#000066" class="size12">&nbsp;</td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#000066" class="size12"><font color="#ffffff"><%= in_total %></font></td>
	<td width="36" align="center" valign="middle" nowrap bgcolor="#000066" class="size12"><font color="#ffffff"><%= out_total %></font></td>
	<td width="44" align="center" valign="middle" nowrap bgcolor="#000066" class="size12">&nbsp;</td>
	<td width="13" height="26" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="13" height="12"></td>
	<td width="13" bgcolor="#FFFFFF">&nbsp;</td>
      </tr>
    </table>
  </td>
</tr>
<tr bgcolor="#FFFFFF">
  <td nowrap>&nbsp;</td>
  <td nowrap>&nbsp;</td>
  <td valign="top" nowrap>&nbsp;</td>
</tr>
</table>
