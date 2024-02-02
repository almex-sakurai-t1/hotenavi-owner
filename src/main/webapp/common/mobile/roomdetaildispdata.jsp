<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    int            i = 0;
    int            now_ymd;
    int            now_time;
    DateEdit        df;
    StringFormat    sf;
    NumberFormat    nf;
 
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

    df = new DateEdit();
    sf = new com.hotenavi2.common.StringFormat();
    nf = new DecimalFormat("00");

    // ���ݓ��t�E�����擾(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));
%>
<%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>����<br>
<hr>
<%-- ���ݕ����ڍ׏��\�� --%>
<%
    for( i = 0 ; i < ownerinfo.StatusDetailCount ; i++ )
    {
        if( ownerinfo.StatusDetailRoomCode[i] != 0 )
        {
            if( i != 0 )
            {
%>
<hr>
<%
            }
%>
<a href="<%= response.encodeURL("roominfodetail.jsp?HotelId=" + hotelid + "&rc=" + ownerinfo.StatusDetailRoomCode[i]) %>">
<%= sf.rightFitFormat(ownerinfo.StatusDetailRoomName[i], 4) %></a>
&nbsp;<%= sf.leftFitFormat(ownerinfo.StatusDetailStatusName[i], 12) %>
<%
            if (ownerinfo.StatusDetailUserChargeMode[i] == 1)
            {
%><img src="/common/mobile/image/rest.gif">
<%
            }
            else if (ownerinfo.StatusDetailUserChargeMode[i] == 2)
            {
%><img src="/common/mobile/image/stay.gif">
<%
            }
%><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= ownerinfo.StatusDetailElapseTime[i] / 60 %>:<%= nf.format(ownerinfo.StatusDetailElapseTime[i] % 60) %><br>
<%
        }
    }
%>
<%-- ���ݕ����ڍ׏��\�������܂� --%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="roomdetail.jsp" />
  <jsp:param name="dispname" value="�����ڍ�" />
</jsp:include>

