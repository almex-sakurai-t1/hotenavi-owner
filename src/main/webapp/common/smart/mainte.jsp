<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.DBConnection" %>
<%@ page import="jp.happyhotel.common.Logging" %>
<%
    NumberFormat nf2  = new DecimalFormat("00");
    DateEdit dateedit = new DateEdit();
    int nowdate = Integer.parseInt(dateedit.getDate(2));
    int nowtime = Integer.parseInt(dateedit.getTime(1));

    String title      = "";
    String param_id   = request.getParameter("id");
    String msg        = "";
    int    id         = param_id != null ? Integer.parseInt(param_id) : 0;
    int    start_date = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT * FROM hh_system_info WHERE data_type = 31 "
                           + "AND start_date <= ? "
                           + "AND end_date   >= ? "
                           + "AND (disp_flag = 1 OR disp_flag = 3) "
                           + "AND id = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setInt(1, nowdate);
        prestate.setInt(2, nowdate);
        prestate.setInt(3, id);

        id = 0;
        result = prestate.executeQuery();
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
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title><%=title%></title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
</head>
<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">
<jsp:include page="header.jsp" flush="true" />
<div align="center">
<font color="#FF0000">Åü<%=title%>Åü</font></div>
<hr>
<%=msg.replace("<br>\r\n","<br>").replace("\r\n","<br>")%>
</body>
</html>
