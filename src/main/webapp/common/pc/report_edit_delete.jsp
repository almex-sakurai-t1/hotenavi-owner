<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    int    report_flag = 0;
    String param_flag  = ReplaceString.getParameter(request,"flg");
    if    (param_flag != null)
    {
           report_flag = Integer.parseInt(param_flag);
    }
%>
<%@ include file="../../common/pc/report_ini.jsp" %>
<%
    int result_del  = 0;
    int    id       = 0;
    String param_id = ReplaceString.getParameter(request,"Id");
    if (param_id != null)
    {
        id = Integer.parseInt(param_id);
        query = "DELETE FROM edit_event_info ";
        query = query + " WHERE hotelid=? AND data_type=? AND id=?";
        try
        {
	        prestate        = connection.prepareStatement(query);
	        prestate.setString(1, hotelid);
	        prestate.setInt(2,  data_type);
	        prestate.setInt(3, id);
	        result_del      = prestate.executeUpdate();
    	}
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
        	DBConnection.releaseResources(connection);
            DBConnection.releaseResources(prestate);
        }
    }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>ÉÅÉãÉ}ÉKä«óù</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">ìoò^ämîF</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- Ç±Ç±Ç©ÇÁï\ -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if( result_del == 1 )
    {
%>
çÌèúÇµÇ‹ÇµÇΩÅB<br>
<%
    }
    else
    {
%>
çÌèúÇ…é∏îsÇµÇ‹ÇµÇΩÅB<br>
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
				<form action="report_edit_list.jsp" name="formmain" target="mainFrame" method="post" onsubmit="document.formreload.submit()">
                    <td class="size12">
					<input type="hidden" name="Id"    value="<%=id%>">
					<input type="hidden" name="flg"   value="<%= report_flag %>">
					<input type="hidden" name="Year"  value="<%= year%>">
					<input type="hidden" name="Month" value="<%= month%>">
					<input type="hidden" name="Day"   value="<%= day%>">
					<input type="submit" value="ñﬂÇÈ">
				</td></form>
<%
    if (report_flag == 0)
    {
%>
				<form action="report_dispselect_day.jsp" name="formreload" target="topFrame" method="post">
					<input type="hidden" name="flg"   value="<%= report_flag %>">
					<input type="hidden" name="Year"  value="<%= year%>">
					<input type="hidden" name="Month" value="<%= month%>">
					<input type="hidden" name="Day"   value="<%= day%>">
				</form>
<%
    }
    else
    {
%>
				<form action="report_dispselect_month.jsp" name="formreload" target="topFrame" method="post">
					<input type="hidden" name="flg"   value="<%= report_flag %>">
					<input type="hidden" name="Year"  value="<%= year%>">
					<input type="hidden" name="Month" value="<%= month%>">
				</form>
<%
    }
%>
                  </tr>
                 </table>
              </div>
            </td>
          </tr>
        </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- Ç±Ç±Ç‹Ç≈ -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
<%
    db_sec.close();
%>
