<%@ page contentType="text/html; charset=Windows-31J" %><%@ page errorPage="error.jsp" %><%@ page import="java.sql.*" %><%@ page import="com.hotenavi2.common.*" %><jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" /><%
    //ïîâÆàÍóóÇ©ÇÁÇÃÉäÉìÉN
    String param_hotelid = ReplaceString.getParameter(request,"NowHotel");
    ownerinfo.sendPacket0108(1, param_hotelid);
%>  <table border="0" cellpadding="0" cellspacing="0" bgcolor="#A8BEBC" width="50%" style="float:right;margin-top:-10px;margin-bottom:-5px;max-width:500px;">
	<tr>
      <td align="left">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr style="height:30px">
	    <td align="center" valign="middle" nowrap class="tableLN" width="50px"></td>
<%
    for( int i = 0 ; i < ownerinfo.OWNERINFO_ROOMSTATUSMAX ; i++ )
    {
        if( ownerinfo.StatusCount[i] != 0 )
        {
%>	    <td align="center" valign="bottom" nowrap class="tableRN" width="50px"><div class="space2"><%= ownerinfo.StatusName[i] %></div>
	    </td>
<%
        }
    }
%>	  </tr>
	  <tr style="height:25px">
	    <td align="center" valign="middle" nowrap class="tableLN">é∫êî</td>
<%
    for( int i = 0 ; i < ownerinfo.OWNERINFO_ROOMSTATUSMAX ; i++ )
    {
        if( ownerinfo.StatusCount[i] != 0 )
        {
%>	    <td align="right" valign="middle" class="tableRW size14px" nowrap><%= ownerinfo.StatusCount[i] %></td>
<%
        }
    }
%>	  </tr>
	</table>
      </td>
    </tr>
  </table>

