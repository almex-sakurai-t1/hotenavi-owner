<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- �����ð���J�ڕ\������ --%>
<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }

    int             i;
    boolean         ret;
    NumberFormat    nf;
    StringFormat    sf;

    nf = new DecimalFormat("00");
    sf = new StringFormat( );
%>
<h2><%= ownerinfo.RoomHistoryDate / 10000 %>/<%= nf.format(ownerinfo.RoomHistoryDate / 100 % 100) %>/<%= nf.format(ownerinfo.RoomHistoryDate % 100) %>��<font size="-1"><a href="#DateSelect">[���t�I��]</a></font></h2>
<hr class="border">
<%-- �������\�� --%>
<table class="uriage">
<tr>
<th width="50%">������</th><td class="uriage"><%= ownerinfo.RoomHistoryRoomCount %></td>
</tr>
</table>
<hr class="border" />
<table class="roomdetail2">
<tr>
<th class="roomdetail3">����</th><th class="roomdetail3">��</th><th class="roomdetail3">��</th><th class="roomdetail3">��</th><th class="roomdetail3">�~</th><th class="roomdetail3">IN</th><th class="roomdetail3">OT</th>
</tr>
<%
    int in_total = 0;
    int out_total = 0;
    for( i = 0 ; i < 24 ; i++ )
    {
		in_total = in_total + ownerinfo.InOutIn[i];
		out_total = out_total + ownerinfo.InOutOut[i];
%>
<tr>
<td class="roomdetail2"><%= nf.format(ownerinfo.RoomHistoryTime[i] / 100) %>��</td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryEmpty[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryExist[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryClean[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryStop[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutIn[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutOut[i]), 2) %></td></tr>
<%
        if( i == 11 )
        {
%>
<tr>
<th class="roomdetail3">����</th><th class="roomdetail3">��</th><th class="roomdetail3">��</th><th class="roomdetail3">��</th><th class="roomdetail3">�~</th><th class="roomdetail3">IN</th><th class="roomdetail3">OT</th>
</tr>
<%
        }
    }
%>
<tr>
<th class="roomdetail3">���v</th><th class="roomdetail3">&nbsp;</th>
<th class="roomdetail3">&nbsp;</th>
<th class="roomdetail3">&nbsp;</th>
<th class="roomdetail3">&nbsp;</th>
<th class="roomdetail4"><%= in_total %></th><th class="roomdetail4"><%= out_total %></th>
</tr>
</table>
<%-- �������\�������܂� --%>
<hr class="border">
<a name="DateSelect"></a>
<%-- ���t�I����ʕ\�� --%>
<jsp:include page="salesselectday.jsp" flush="true" >
  <jsp:param name="Contents" value="roomhistory" />
</jsp:include>
<%-- ���t�I����ʕ\�������܂� --%>

<!-- �g���\�������܂� -->
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="jumpurl" value="roomhistory.jsp" />
  <jsp:param name="dispname" value="�����ð���J��" />
</jsp:include>

