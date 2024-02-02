<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%
    NumberFormat nf2      = new DecimalFormat("00");
    DateEdit dateedit = new DateEdit();
    int nowdate   =  Integer.parseInt(dateedit.getDate(2));
    int nowtime   =  Integer.parseInt(dateedit.getTime(1));

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query = "";
    String            title = "";
    String            param_id   = ReplaceString.getParameter(request,"id");
    String            msg1  = "";
    int               id         = 0;
    if               (param_id  != null) id = Integer.parseInt(param_id);
    int               start_date = 0;
    try
    {
        connection    = DBConnection.getConnection();
        query = "SELECT * FROM hh_system_info WHERE data_type=31";
//        query = query + " AND start_date <=" + nowdate;
        query = query + " AND end_date   >=" + nowdate;
        query = query + " AND (disp_flag  =1 OR disp_flag  =2)";
        query = query + " AND id = " + id;
        id    = 0;
        prestate       = connection.prepareStatement(query);
        result         = prestate.executeQuery();
        if( result != null)
        {
            if( result.next() )
            {
                id         = result.getInt("id");
                title      = result.getString("title");
                start_date = result.getInt("start_date");
                msg1       = result.getString("msg1");
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title><%=title%></title>
<style type="text/css">
<!--
.midashi {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 12px;
	text-decoration: none;
	letter-spacing: 1px;
	padding-left: 8px;
	padding-top: 1px;
}
.midashi2 {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	letter-spacing: 1px;
	padding-top: 3px;
	padding-bottom: 2px;
	font-size: 12px;
}
.size16 {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 16px;
	line-height: 140%;
	text-decoration: none;
	letter-spacing: 1px;
}
.size12 {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 12px;
	line-height: 140%;
	text-decoration: none;
	letter-spacing: 1px;
}
.size10 {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 10px;
	line-height: 140%;
	text-decoration: none;
	letter-spacing: 1px;
}
a {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 12px;
	line-height: 140%;
	text-decoration: none;
}
a:link {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 12px;
	line-height: 140%;
	text-decoration: none;
}
a:visited {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 12px;
	line-height: 140%;
	text-decoration: none;
}
a:hover {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 12px;
	line-height: 140%;
	color: #FF9900;
	text-decoration: underline;
}
a:active {
	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 12px;
	line-height: 140%;
	text-decoration: none;
}
.size121 {	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 12px;
	line-height: 140%;
	text-decoration: none;
	letter-spacing: 1px;
}
.size121 {	font-family: "ÇlÇr ÇoÉSÉVÉbÉN", "Osaka";
	font-size: 12px;
	line-height: 140%;
	text-decoration: none;
	letter-spacing: 1px;
}


-->
</style>

<script language="JavaScript">
<!--
window.focus();
// -->
</script>
</head>
<body text="#000000" link="#000000" vlink="#000000" alink="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="<%if(id==0){%>javascript:window.close();<%}%>">
<div align="center">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr bgcolor="#BCCC9E">
      <td colspan="4"><img src="../../common/pc/image/spacer.gif" width="100" height="8"><img src="../../common/pc/image/spacer.gif" width="100" height="8"></td>
    </tr>
    <tr bgcolor="#BCCC9E">
      <td class="size16"><img src="../../common/pc/image/spacer.gif" width="10" height="8"><strong>Åü<%=title%>Åü</strong><img src="../../common/pc/image/spacer.gif" width="90" height="8"></td>
      <td width="59" valign="top" bgcolor="#BCCC9E"><a id="close" href="JavaScript:window.close()"><img src="../../common/pc/image/close.gif" alt="Close" width="59" height="19" border="0"></a></td>
      <td width="12" valign="top" bgcolor="#BCCC9E"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
    </tr>
    <tr bgcolor="#BCCC9E">
      <td colspan="4"><img src="../../common/pc/image/spacer.gif" width="100" height="20"></td>
    </tr>
  </table>
  <table width="90%" border="0" cellspacing="0" cellpadding="0">
	<tr>
      <td><img src="../../common/pc/image/spacer.gif" width="100" height="30"></td>
	</tr>
    <tr><td class="size12">
		<%=msg1.replace("<br>\r\n","<br>").replace("\r\n","<br>")%>
	</td>
	</tr>
	<tr>
      <td><img src="../../common/pc/image/spacer.gif" width="100" height="30"></td>
	</tr>
    <tr><td class="size12">
	</td></tr>
	<tr>
      <td><img src="../../common/pc/image/spacer.gif" width="100" height="5"></td>
	</tr>
  </table>
</td>
    </tr>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td height="36" align="center" valign="middle" bgcolor="#BCCC9E" class="size10"><font color="#FFFFFF">Copyright&copy; almex
      inc. All Rights Reserved</font></td>
    </tr>
  </table>
</div>
</body>
</html>
