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
    boolean  NewFlag   = false;

    // ì˙ït√ﬁ∞¿ÇÃï“èW
    int start_ymd = 0;
    int end_ymd = 0;
    int old_ymd = 0;

    int start_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_start_yy"));
    int start_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_start_mm"));
    int start_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_start_dd"));
    start_ymd = start_yy*10000 + start_mm*100 + start_dd;

    int end_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_end_yy"));
    int end_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_end_mm"));
    int end_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_end_dd"));
    end_ymd = end_yy*10000 + end_mm*100 + end_dd;

    int    id          = 0;
    String msg1        = "";
    String param_id    = ReplaceString.getParameter(request,"Id");
    if (param_id == null)
    {
        NewFlag   = true;
    }
    else
    {
        NewFlag   = false;
        id = Integer.parseInt(param_id);
    }
    if  (id == 0) NewFlag   = true;
    int result_new  = 0;
    msg1 = ReplaceString.getParameter(request,"col_msg1");

    if( NewFlag)
    {
        query = "INSERT INTO edit_event_info SET ";
        query = query + "hotelid='"          + hotelid      + "', ";
        query = query + "data_type="         + data_type    + ", ";
        query = query + "add_hotelid='"      + loginhotel + "', ";
        query = query + "add_userid='"       + ownerinfo.DbUserId + "', ";
        query = query + "add_date='"         + nowdate + "', ";
        query = query + "add_time='"         + nowtime + "', ";
        query = query + "msg2='', ";
        query = query + "msg3='', ";
        query = query + "msg4='', ";
        query = query + "msg5='', ";
        query = query + "msg6='', ";
        query = query + "msg7='', ";
        query = query + "msg8='', ";
    }
    else
    {
        query = "UPDATE edit_event_info SET ";
    }
        query = query + "disp_idx=0, ";
        query = query + "disp_flg=1, ";
        query = query + "start_date="        + start_ymd + ", ";
        query = query + "end_date="          + end_ymd + ", ";
        query = query + "msg1='"             + ReplaceString.SQLEscape(msg1) + "', ";
        query = query + "upd_hotelid='"      + loginhotel + "', ";
        query = query + "upd_userid='"       + ownerinfo.DbUserId + "', ";
        query = query + "last_update='"      + nowdate + "', ";
        query = query + "last_uptime='"      + nowtime + "' ";
    if(!NewFlag)
    {
        query = query + " WHERE hotelid='"   + hotelid + "' AND data_type=" + data_type + " AND id=" + id;
    }
    try
    {
        prestate        = connection.prepareStatement(query);
        result_new  = prestate.executeUpdate();
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
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>ÉÅÉãÉ}ÉKä«óù</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
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
    if( result_new == 1 )
    {
%>
ìoò^ÇµÇ‹ÇµÇΩÅB<br>
<%
    }
    else
    {
%>
ìoò^Ç…é∏îsÇµÇ‹ÇµÇΩÅB<br>
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

