<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%-- ���P��IN/OUT�g���\������ --%>
<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    // �z�e��ID�̃Z�b�g
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }

    int             i;
    int             now_ymd;
    int             now_time;
    DateEdit        df;
    StringFormat    sf;
    NumberFormat    nf;

    df = new DateEdit();
    sf = new StringFormat();
    nf = new DecimalFormat("00");
%>

<%
    if( ownerinfo.AddupInOutGetDate != 0 )
    {
%>
<h2><%= (ownerinfo.AddupInOutGetDate / 10000) %>/<%= (ownerinfo.AddupInOutGetDate / 100 % 100) %>/<%= (ownerinfo.AddupInOutGetDate % 100) %>�v�㕪</h2>
<%
    }
%>
<hr class="border">
<%
    // ���ݓ��t�E�����擾(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));
%>
<h2><%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>����</h2>
<%-- ���P��IN/OUT�g���\�� --%>

<table class="uriage">
<tr>
<td width="50%">����IN</td><td class="uriage"><%= sf.rightFitFormat(Integer.toString(ownerinfo.AddupInOutAfterIn), 4) %></td>
</tr>
<tr>
<td>���OIN</td><td class="uriage"><%= sf.rightFitFormat(Integer.toString(ownerinfo.AddupInOutBeforeIn), 4) %></td>
</tr>
<tr>
<td>��OUT</td><td class="uriage"><%= sf.rightFitFormat(Integer.toString(ownerinfo.AddupInOutAllOut), 4) %></td>
</tr>
<tr>
<td>���OOUT</td><td class="uriage"><%= sf.rightFitFormat(Integer.toString(ownerinfo.AddupInOutBeforeOut), 4) %></td>
</tr>
</table>
<%-- ���P��IN/OUT�g���\�������܂� --%>
<hr class="border" />

<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="closeinout.jsp" />
  <jsp:param name="dispname" value="���P��IN/OUT�g��" />
</jsp:include>

