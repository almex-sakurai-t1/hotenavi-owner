<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    //管理店舗一覧からのリンク
    String param_hotelid = ReplaceString.getParameter(request,"NowHotel");
//    String param_hotelid  = ReplaceString.getParameter(request,"HotelIdfromGroup");
%>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#A8BEBC">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td class="size12" nowrap><font color="#006600"><strong>部屋情報
    </strong></font><input name="roomlist" type="button" class="syosaibtn" id="roomlist" onClick="MM_openBrWindow('roomlist.jsp?<% if (param_hotelid != null){%>&HotelId=<%=param_hotelid%><%}%>','部屋詳細','menubar=yes,scrollbars=yes,resizable=yes,width=340,height=600')" value="部屋詳細">
    </td>
    <td>&nbsp;
    </td>
  </tr>
   <tr>
      <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
      <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
      <td><img src="../../common/pc/image/spacer.gif" width="3" height="5"></td>
    </tr>
    <tr>
      <td><img src="../../common/pc/image/spacer.gif" width="6" height="100"></td>
      <td align="left">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td align="center" valign="middle" nowrap class="tableLGr">ステータス</td>
	    <td align="center" valign="middle" nowrap class="tableRGr">室数</td>
	  </tr>
<%
    for( int i = 0 ; i < ownerinfo.OWNERINFO_ROOMSTATUSMAX ; i++ )
    {
        if( ownerinfo.StatusCount[i] != 0 )
        {
%>
	  <tr>
	    <td align="left" valign="top" nowrap class="tableLW"><div class="space2"><%= ownerinfo.StatusName[i] %></div>
	    </td>
	    <td align="right" valign="middle" class="tableRW" nowrap><%= ownerinfo.StatusCount[i] %></td>
	  </tr>
<%
        }
    }
%>

	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td><img src="../../common/pc/image/spacer.gif" width="40" height="6"></td>
	  </tr>
	  <tr>
	    <td align="center" valign="middle" class="size12">

<%
	    if( ownerinfo.StatusEmptyFullMode == 1 )
	    {
%>
	        手動
<%
	    }
	    else
	    {
%>
	        自動
<%
	    }
%>
&nbsp;/&nbsp;
<%
	    if( ownerinfo.StatusEmptyFullState == 1 )
	    {
%>
	        空室
<%
	    }
	    else
	    {
%>
	        満室
<%
 	   }
%>
		&nbsp;
<%
	    if( ownerinfo.StatusWaiting != 0 )
	    {
%>
		<br>
	        ウェイティング数:<%= ownerinfo.StatusWaiting %>&nbsp;組
<%
	    }
%>
		 </td>
	  </tr>
	  <tr>
	    <td align="center" valign="top"><img src="../../common/pc/image/spacer.gif" width="40" height="6"></td>
	  </tr>
	</table>
      </td>
      <td><img src="../../common/pc/image/spacer.gif" width="6" height="100"></td>
    </tr>
  </table>

