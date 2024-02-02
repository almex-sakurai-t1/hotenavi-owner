<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
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

    // imedia_user �̃`�F�b�N
    int imedia_user = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user = result.getInt("imedia_user");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }

    boolean timeChartFlag = false;
    try
    {
        final String query = "SELECT * FROM hotel WHERE hotel_id = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result = prestate.executeQuery();
        if( result.next() )
        {
            if (result.getInt("timechart_flag") == 1)
            {
                timeChartFlag = true;
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }

    int i;
    int count;
    int now_ymd;
    int add_ymd;
    int now_time;
    int starttime = 0;
    DateEdit df;
    StringFormat sf;
    NumberFormat nf;

	// �v����t�擾
    add_ymd = ownerinfo.Addupdate;
	
    // IN/OUT�g���擾�i�J�n�����擾�̂��߁j
    ownerinfo.InOutGetStartDate = add_ymd;
    ownerinfo.InOutGetEndDate = 0;
    ownerinfo.sendPacket0106();

	// �J�n����
    starttime = ownerinfo.InOutTime[0] / 100;
 
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
<h2><%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>����</h2>
<hr class="border">
<%-- ���ݕ������\�� --%>

<table class="roomdetail">
<tr>
<th width="50%">�X�e�[�^�X</th><th>����</th>
</tr>
<%
    for( i = 0 ; i < ownerinfo.OWNERINFO_ROOMSTATUSMAX ; i++ )
    {
        if( ownerinfo.StatusCount[i] != 0 )
        {
%>
<tr>
<td><%= sf.leftFitFormat(ownerinfo.StatusName[i], 10) %></td><td class="roomdetail"><%= sf.rightFitFormat(Integer.toString(ownerinfo.StatusCount[i]), 3) %></td>
</tr>
<%
        }
    }
%>
</table>
<center>
<h1>
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
     �E�F�C�e�B���O:<%= ownerinfo.StatusWaiting %>&nbsp;�g
<%
    }
%>
</h1>
</center>
<%-- ���ݕ������\�������܂� --%>

<hr class="border" />
<ul class="link">
<div align="right">
<li><a href="<%= response.encodeURL("roomdetail.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">�����ڍׂ�����</a></li>
<li><a href="<%= response.encodeURL("closeinout.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">���P��IN/OUT�g��</a></li>
<li><a href="<%= response.encodeURL("salesinout.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J") + "&Ymd=" + add_ymd) %>">IN/OUT�g��</a></li>
<li><a href="<%= response.encodeURL("roomhistory.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">�����X�e�[�^�X�J��</a></li>
<%
    if(timeChartFlag)
   {
%>
<li><a href="<%= response.encodeURL("timechart.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">�^�C���`���[�g</a></li>
<%
    }
%>
</div>
</ul>
<%
    if (request.getParameter("jump") == null)
    {
%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="roominfo.jsp" />
  <jsp:param name="dispname" value="�������" />
</jsp:include>
<%
    }
%>
