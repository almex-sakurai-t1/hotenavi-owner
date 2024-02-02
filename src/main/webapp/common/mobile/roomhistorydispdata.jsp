<%@ page contentType="text/html; charset=Windows-31J" %>
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
<%= ownerinfo.RoomHistoryDate / 10000 %>/<%= nf.format(ownerinfo.RoomHistoryDate / 100 % 100) %>/<%= nf.format(ownerinfo.RoomHistoryDate % 100) %>��<font size=1>[<a href="#DateSelect">���t�I��</a>]</font><br>
<hr>
<%-- �������\�� --%>
������:<%= ownerinfo.RoomHistoryRoomCount %><br>
����&nbsp;��&nbsp;��&nbsp;��&nbsp;�~&nbsp;IN&nbsp;OT<br>
<%
    int in_total = 0;
    int out_total = 0;
	
    for( i = 0 ; i < 24 ; i++ )
    {
        in_total = in_total + ownerinfo.InOutIn[i];
		out_total = out_total + ownerinfo.InOutOut[i];
%>
<%= nf.format(ownerinfo.RoomHistoryTime[i] / 100) %>��&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryEmpty[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryExist[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryClean[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryStop[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutIn[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutOut[i]), 2) %><br>
<%
        if( i == 11 )
        {
%>
����&nbsp;��&nbsp;��&nbsp;��&nbsp;�~&nbsp;IN&nbsp;OT<br>
<%
        }
    }
%>
���v�@�@�@�@�@�@�@<%= in_total %>�@<%= out_total %><br />
<%-- �������\�������܂� --%>
<hr>
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

