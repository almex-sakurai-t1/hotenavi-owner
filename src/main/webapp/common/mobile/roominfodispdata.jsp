<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    String       hotelid;
    // �z�e��ID�̃Z�b�g
    hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int               imedia_user = 0;

    // imedia_user �̃`�F�b�N
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user      = result.getInt("imedia_user");
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    boolean TimeChartFlag = false;
    // imedia_user �̃`�F�b�N
    try
    {
        final String query = "SELECT * FROM hotel WHERE hotel_id = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() )
            {
                if (result.getInt("timechart_flag") == 1)
                {
                    TimeChartFlag = true;
                }
            }
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
<%
    int            i;
    int            count;
    int            now_ymd;
    int            add_ymd;
    int            now_time;
    int            starttime = 0;
    DateEdit        df;
    StringFormat    sf;
    NumberFormat    nf;

	// �v����t�擾
    add_ymd         = ownerinfo.Addupdate;


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
<hr>
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

<div align="right">
<a href="<%= response.encodeURL("roomdetail.jsp?HotelId=" + hotelid) %>">�����ڍׂ�����</a><br>
<a href="<%= response.encodeURL("closeinout.jsp?HotelId=" + hotelid) %>">���P��IN/OUT�g��</a><br>
<a href="<%= response.encodeURL("salesinout.jsp?HotelId=" + hotelid + "&Ymd=" + add_ymd) %>">IN/OUT�g��</a><br>
<a href="<%= response.encodeURL("roomhistory.jsp?HotelId=" + hotelid) %>">�����ð���J��</a><br>
<%
    if(imedia_user == 1 || TimeChartFlag)
    {
%>
<a href="<%= response.encodeURL("timechart.jsp?HotelId=" + hotelid) %>">�������</a><br>
<%
    }
%>
</div>
<%
    if (ReplaceString.getParameter(request,"jump") == null)
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
