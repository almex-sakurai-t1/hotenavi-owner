<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
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
<%= (ownerinfo.AddupInOutGetDate / 10000) %>/<%= (ownerinfo.AddupInOutGetDate / 100 % 100) %>/<%= (ownerinfo.AddupInOutGetDate % 100) %>�v�㕪<br>
<%
    }
%>
<hr>
<%
    // ���ݓ��t�E�����擾(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));
%>
<%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>����<br>
<%-- ���P��IN/OUT�g���\�� --%>
<pre>
����IN   <%= sf.rightFitFormat(Integer.toString(ownerinfo.AddupInOutAfterIn), 4) %>
���OIN   <%= sf.rightFitFormat(Integer.toString(ownerinfo.AddupInOutBeforeIn), 4) %>
��OUT    <%= sf.rightFitFormat(Integer.toString(ownerinfo.AddupInOutAllOut), 4) %>
���OOUT  <%= sf.rightFitFormat(Integer.toString(ownerinfo.AddupInOutBeforeOut), 4) %>
</pre>
<%-- ���P��IN/OUT�g���\�������܂� --%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="closeinout.jsp" />
  <jsp:param name="dispname" value="���P��IN/OUT�g��" />
</jsp:include>

