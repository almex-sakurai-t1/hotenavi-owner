<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.DBConnection" %>
<%@ page import="jp.happyhotel.common.Logging" %>
<%
    NumberFormat nf2      = new DecimalFormat("00");
    DateEdit dateedit = new DateEdit();
    int nowdate   =  Integer.parseInt(dateedit.getDate(2));
    int nowtime   =  Integer.parseInt(dateedit.getTime(1));

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            title = "";
    String            param_id   = ReplaceString.getParameter(request,"id");
    String            msg  = "";
    int               id         = 0;
    if               (param_id  != null) id = Integer.parseInt(param_id);
    int               start_date = 0;
    try
    {
        connection    = DBConnection.getConnection();
        final String query = "SELECT * FROM hh_system_info WHERE data_type=31"
            + " AND start_date <= ? "
            + " AND end_date   >= ? "
            + " AND (disp_flag  =1 OR disp_flag  =3)"
            + " AND id = ? ";
        prestate       = connection.prepareStatement(query);
        prestate.setInt(1, nowdate);
        prestate.setInt(2, nowdate);
        prestate.setInt(3, id);

        id    = 0;

        result         = prestate.executeQuery();
        if( result.next() )
        {
            id         = result.getInt("id");
            title      = result.getString("msg2_title");
            start_date = result.getInt("start_date");
            msg        = result.getString("msg2");
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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html ; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title><%=title%></title>
<body>
<jsp:include page="header.jsp" flush="true" />
<div align="center">
<font color="#FF0000">Ÿ<%=title%>Ÿ</font></div>
<hr>
<%=msg.replace("<br>\r\n","<br>").replace("\r\n","<br>")%>
</body>
</html>
