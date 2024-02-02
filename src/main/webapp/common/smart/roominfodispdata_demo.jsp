<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- ���ݕ������\������ --%>

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
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }
%>

<%
    int          i;
    int          count;
    int          now_ymd;
    int          add_ymd;
    int          now_time;
    int          starttime = 0;
    DateEdit     df;
    StringFormat sf;
    NumberFormat nf;

	// �v����t�擾
    add_ymd = ownerinfo.Addupdate;
	

    // IN/OUT�g���擾�i�J�n�����擾�̂��߁j
    ownerinfo.InOutGetStartDate = add_ymd;
    ownerinfo.InOutGetEndDate = 0;
    ownerinfo.sendPacket0106();

	// �J�n����
    starttime  = ownerinfo.InOutTime[0] / 100;
 
    df = new DateEdit();
    sf = new StringFormat();

    // ���ݓ��t�E�����擾(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));

    // �J�n�������O���ȊO�̏ꍇ�́A���݌v����擾�B�O���̏ꍇ�͍����̓��t
    if(starttime == 0)
    {
        add_ymd = now_ymd;
    }
    if (DemoMode)
    {
        add_ymd = now_ymd;
    }
%>
<%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>����<br>
<hr class="border">
<%-- ���ݕ������\�� --%>
<%
    for( i = 0 ; i < ownerinfo.OWNERINFO_ROOMSTATUSMAX ; i++ )
    {
        if( ownerinfo.StatusCount[i] != 0 )
        {
%>
<pre><%= sf.leftFitFormat(ownerinfo.StatusName[i], 10) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.StatusCount[i]), 3) %></pre>
<%
        }
    }
%>
(
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
/
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
)
<%
  if( ownerinfo.StatusWaiting != 0 )
    {
%>
<br>
     ���èݸ�:<%= ownerinfo.StatusWaiting %>&nbsp;�g
<%
    }
%>
<%-- ���ݕ������\�������܂� --%>

<hr class="border" />
<ul class="link">
<div align="right">
<li><a href="<%= response.encodeURL("roomdetail.jsp?HotelId="  + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">�����ڍׂ�����</a><br>
<a href="<%=     response.encodeURL("closeinout.jsp?HotelId="  + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">���P��IN/OUT�g��</a><br>
<a href="<%=     response.encodeURL("salesinout.jsp?HotelId="  + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J") + "&Ymd=" + add_ymd) %>">IN/OUT�g��</a><br>
<a href="<%=     response.encodeURL("roomhistory.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">�����ð���J��</a><br></li>
</div>
</ul>

<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="roominfo.jsp" />
  <jsp:param name="dispname" value="�������" />
</jsp:include>
