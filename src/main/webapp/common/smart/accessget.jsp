<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %> 
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    int      value;
    int      year;
    int      month;
    int      day;
    String   ymd;
    String   endymd;
    String   hotelid;
    Calendar cal;

    hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = session.getAttribute("HotelId").toString();
    }

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
%>

<hr class="border">
<table class="uriage">
<tr>
<th colspan="2">���ʱ�����</th>
</tr>

<%
// �O�����擾
    cal = Calendar.getInstance();
    cal.add(cal.DATE, -1);
    year  = cal.get(cal.YEAR);
    month = cal.get(cal.MONTH) + 1;
    day   = cal.get(cal.DAY_OF_MONTH);
    ymd   = year + "-" + month + "-" + day;
    // �O�����݌v
    value = 0;

    try
    {
        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id = ? AND acc_date = ?"
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        result = prestate.executeQuery();

        if( result.next() )
        {
            value = result.getInt(1);
        }
    }
    catch (Exception e)
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }
%>
<tr>
<td width="50%">�O��</td><td class="uriage"><%= Kanma.get(value) %></td>
</tr>
<%
// �O�X�����擾
    cal = Calendar.getInstance();
    cal.add(cal.DATE, -2);
    year  = cal.get(cal.YEAR);
    month = cal.get(cal.MONTH) + 1;
    day   = cal.get(cal.DAY_OF_MONTH);
    ymd   = year + "-" + month + "-" + day;
    // �O�����݌v
    value = 0;

    try
    {
        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id = ? AND acc_date = ?"
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        result = prestate.executeQuery();

        if( result.next() )
        {
            value = result.getInt(1);
        }
    }
    catch (Exception e)
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }
%>
<tr>
<td>�O�X��</td><td class="uriage"><%= Kanma.get(value) %></td>
</tr>

<%
// �����݌v�擾
    cal    = Calendar.getInstance();
    year   = cal.get(cal.YEAR);
    month  = cal.get(cal.MONTH) + 1;
    day    = 0;
    ymd    = year + "-" + month + "-01";
    endymd = year + "-" + month + "-31";
    // �����݌v
    value  = 0;

    try
    {
        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id = ? AND acc_date BETWEEN ? AND ?"
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        prestate.setString(3, endymd);
        result = prestate.executeQuery();

        if( result.next() )
        {
            value = result.getInt(1);
        }
    }
    catch (Exception e)
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }
%>
<tr>
<td><a href="<%= response.encodeURL("accessmonth.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year=" + year + "&Month=" + month) %>">�����݌v</a></td><td class="uriage"><a href="<%= response.encodeURL("accessmonth.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year=" + year + "&Month=" + month) %>"><%= Kanma.get(value) %></a></td>
</tr>
</table>
<hr class="border">
<table class="uriage">
<tr>
<th colspan="2">���ʃA�N�Z�X��</th>
</tr>

<%
// �O�����擾
    cal = Calendar.getInstance();
    cal.add(cal.MONTH, -1);
    year   = cal.get(cal.YEAR);
    month  = cal.get(cal.MONTH) + 1;
    day    = 0;
    ymd    = year + "-" + month + "-01";
    endymd = year + "-" + month + "-31";
    // �O�����݌v
    value  = 0;

    try
    {
        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id = ? AND acc_date BETWEEN ? AND ?"
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        prestate.setString(3, endymd);
        result = prestate.executeQuery();

        if( result.next() )
        {
            value = result.getInt(1);
        }
    }
    catch (Exception e)
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }
%>
<tr>
<td width="50%">�O��</td><td class="uriage"><%= Kanma.get(value) %></td>
</tr>

<%
// �O�X�����擾
    cal = Calendar.getInstance();
    cal.add(cal.MONTH, -2);
    year   = cal.get(cal.YEAR);
    month  = cal.get(cal.MONTH) + 1;
    day    = 0;
    ymd    = year + "-" + month + "-01";
    endymd = year + "-" + month + "-31";
    // �O�����݌v
    value  = 0;

    try
    {
        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id = ? AND acc_date BETWEEN ? AND ?"
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        prestate.setString(3, endymd);
        result = prestate.executeQuery();

        if( result.next() )
        {
            value = result.getInt(1);
        }
    }
    catch (Exception e)
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }
%>
<tr>
<td width="50%">�O�X��</td><td class="uriage"><%= Kanma.get(value) %></td>
</tr>

<%
// ���N�݌v�擾
    cal    = Calendar.getInstance();
    year   = cal.get(cal.YEAR);
    month  = 0;
    day    = 0;
    ymd    = year + "-01-01";
    endymd = year + "-12-31";
    // ���N�݌v
    value  = 0;

    try
    {
        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id = ? AND acc_date BETWEEN ? AND ?"
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        prestate.setString(3, endymd);
        result = prestate.executeQuery();

        if( result.next() )
        {
            value = result.getInt(1);
        }
    }
    catch (Exception e)
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }
%>
<tr>
<td><a href="<%= response.encodeURL("accessyear.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year=" + year) %>">���N�݌v</a></td><td class="uriage"><a href="<%= response.encodeURL("accessyear.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year=" + year) %>"><%= Kanma.get(value) %></a></td>
</tr>
</table>
<hr class="border" />

<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="access.jsp" />
  <jsp:param name="dispname" value="�T�C�g�A�N�Z�X���" />
</jsp:include>
