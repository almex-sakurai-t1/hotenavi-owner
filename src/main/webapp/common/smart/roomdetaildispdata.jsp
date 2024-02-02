<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- ���ݕ����ڍ׏��\������ --%>

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    int          i = 0;
    int          now_ymd;
    int          now_time;
    DateEdit     df;
    StringFormat sf;
    NumberFormat nf;
 
    // �z�e��ID�̃Z�b�g
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }

    df = new DateEdit();
    sf = new com.hotenavi2.common.StringFormat();
    nf = new DecimalFormat("00");

    // ���ݓ��t�E�����擾(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));
%>
<h2><%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>����</h2>
<hr class="border">

<%-- ���ݕ����ڍ׏��\�� --%>
<table class="uriage">
<tr>
<th>����<br />�ԍ�</th><th>�X�e�[�^�X</th><th>�o��<br />����</th>
</tr>
<%
    for( i = 0 ; i < ownerinfo.StatusDetailCount ; i++ )
    {
        if( ownerinfo.StatusDetailRoomCode[i] != 0 )
        {
%>
<tr><td><a href="<%= response.encodeURL("roominfodetail.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&rc=" + ownerinfo.StatusDetailRoomCode[i]) %>">
<%= sf.rightFitFormat(ownerinfo.StatusDetailRoomName[i], 4) %></a></td>
<td><%= sf.leftFitFormat(ownerinfo.StatusDetailStatusName[i], 12) %></td>
<td class="uriage">
<%
            if (ownerinfo.StatusDetailUserChargeMode[i] == 1)
            {
%>
				<img src="/common/smart/image/rest.gif">
<%
            }
            else if (ownerinfo.StatusDetailUserChargeMode[i] == 2)
            {
%>
				<img src="/common/smart/image/stay.gif">
<%
            }
%>
<%= ownerinfo.StatusDetailElapseTime[i] / 60 %>:<%= nf.format(ownerinfo.StatusDetailElapseTime[i] % 60) %></td>
</tr>
<%
        }
    }
%>
</table>

<hr class="border">
<%-- ���ݕ����ڍ׏��\�������܂� --%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="roomdetail.jsp" />
  <jsp:param name="dispname" value="�����ڍ�" />
</jsp:include>
