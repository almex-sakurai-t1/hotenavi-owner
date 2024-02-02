<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
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
    int         value;
    int         year;
    int         month;
    int         day;
    String      ymd;
    String      endymd;
    String      hotelid;
    String      query;
    Calendar    cal;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;


    hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = session.getAttribute("HotelId").toString();
    }

%>

<hr>
���ʱ�����<br>
<%
// �O�����擾
    cal = Calendar.getInstance();
    cal.add(cal.DATE, -1);
    year  = cal.get(cal.YEAR);
    month = cal.get(cal.MONTH) + 1;
    day   = cal.get(cal.DAY_OF_MONTH);
    ymd   = year + "-" + month + "-" + day;
    // �O�����݌v
    value  = 0;

    try
    {
        connection        = DBConnection.getConnection();
        prestate = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id = ? AND acc_date = ?"
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);

        result      = prestate.executeQuery();
        if( result.next() )
        {
            value = result.getInt(1);
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
&nbsp;&nbsp;&nbsp;&nbsp;�O��:<%= Kanma.get(value) %><br>

<%
// �O�X�����擾
    cal = Calendar.getInstance();
    cal.add(cal.DATE, -2);
    year  = cal.get(cal.YEAR);
    month = cal.get(cal.MONTH) + 1;
    day   = cal.get(cal.DAY_OF_MONTH);
    ymd   = year + "-" + month + "-" + day;
    // �O�����݌v
    value  = 0;


    try
    {
        connection        = DBConnection.getConnection();
        prestate    = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id= ? AND acc_date= ? "
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);

        result      = prestate.executeQuery();
        if( result.next() )
        {
            value = result.getInt(1);
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
&nbsp;&nbsp;�O�X��:<%= Kanma.get(value) %><br>

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
        connection        = DBConnection.getConnection();
        prestate    = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id= ?  AND acc_date BETWEEN  ? AND ? "
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        prestate.setString(3, endymd);

        result      = prestate.executeQuery();
        if( result.next() )
        {
            value = result.getInt(1);
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
<a href="<%= response.encodeURL("accessmonth.jsp?HotelId=" + hotelid + "&Year=" + year + "&Month=" + month) %>">�����݌v:<%= Kanma.get(value) %></a><br>

<hr>
���ʱ�����<br>
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
        connection        = DBConnection.getConnection();
        prestate    = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id= ? AND acc_date BETWEEN ? AND ? "
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        prestate.setString(3, endymd);

        result      = prestate.executeQuery();
        if( result.next() )
        {
            value = result.getInt(1);
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
&nbsp;&nbsp;&nbsp;&nbsp;�O��:<%= Kanma.get(value) %><br>

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
        connection        = DBConnection.getConnection();
        prestate    = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id= ? AND acc_date BETWEEN ? AND ? "
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        prestate.setString(3, endymd);

        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() )
            {
                value = result.getInt(1);
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
&nbsp;&nbsp;�O�X��:<%= Kanma.get(value) %><br>

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
        connection        = DBConnection.getConnection();
        prestate    = connection.prepareStatement(
            "SELECT sum(total) FROM access_mobile_detail WHERE hotel_id= ? AND acc_date BETWEEN ? AND ? "
        );
        prestate.setString(1, hotelid);
        prestate.setString(2, ymd);
        prestate.setString(3, endymd);

        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() )
            {
                value = result.getInt(1);
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
<a href="<%= response.encodeURL("accessyear.jsp?HotelId=" + hotelid + "&Year=" + year) %>">���N�݌v:<%= Kanma.get(value) %></a><br>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="access.jsp" />
  <jsp:param name="dispname" value="��ı������" />
</jsp:include>

