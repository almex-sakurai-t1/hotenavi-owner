<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    //�Ǘ��X�܈ꗗ����̃����N
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
    <td class="size12" nowrap><font color="#006600"><strong>�������
    </strong></font><input name="roomlist" type="button" class="syosaibtn" id="roomlist" onClick="MM_openBrWindow('roomlist.jsp?<% if (param_hotelid != null){%>&HotelId=<%=param_hotelid%><%}%>','�����ڍ�','menubar=yes,scrollbars=yes,resizable=yes,width=340,height=600')" value="�����ڍ�">
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
	    <td align="center" valign="middle" nowrap class="tableLGr">�X�e�[�^�X</td>
	    <td align="center" valign="middle" nowrap class="tableRGr">����</td>
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
	        �蓮
<%
	    }
	    else
	    {
%>
	        ����
<%
	    }
%>
&nbsp;/&nbsp;
<%
	    if( ownerinfo.StatusEmptyFullState == 1 )
	    {
%>
	        ��
<%
	    }
	    else
	    {
%>
	        ����
<%
 	   }
%>
		&nbsp;
<%
	    if( ownerinfo.StatusWaiting != 0 )
	    {
%>
		<br>
	        �E�F�C�e�B���O��:<%= ownerinfo.StatusWaiting %>&nbsp;�g
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

