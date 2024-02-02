<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    int    report_flag = 0;
    String param_flag  = request.getParameter("flg");
    if    (param_flag != null)
    {
           report_flag = Integer.parseInt(param_flag);
    }
%>
<%@ include file="../../common/pc/report_ini.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>帳票設定</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" id="disp" style="display:block">
            <tr>
              <td width="100" height="20" bgcolor="#22333F" class="tab" align="center"><font color="#FFFFFF"><%=hname%></font></td>
              <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">帳票設定</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td width="200" height="20"  class="tab" align="right">&nbsp;</td>
              <td height="20" align="right">&nbsp;</td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td bgcolor="#E2D8CF">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="8" height="4" valign="top" colspan="2"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
			</tr>
            <tr>
              <td width="8" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td align="left" valign="top">
<%
    java.sql.Date start;
    java.sql.Date end;
    int start_date  = 0;
    int start_year  = 0;
    int start_month = 0;
    int start_day   = 0;
    int end_date    = 0;
    int end_year    = 0;
    int end_month   = 0;
    int end_day     = 0;
    int count       = 0;
    boolean  TargetFlag     = false;
    boolean  TargetSelected = false;

    String       temp     = "";
    StringBuffer buf      = new StringBuffer();
    String       title    = "";
    int          i        = 0;
    try
    {
        query = "SELECT * FROM edit_event_info WHERE hotelid=?";
        query = query + " AND data_type=?";
        query = query + " ORDER BY id DESC";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setInt(2,data_type);
        result      = prestate.executeQuery();
%>
				<table width="98%" border="0" cellspacing="1" cellpadding="0" >

<%
        if( result != null )
        {
            while( result.next() != false )
            {
                count++;
                start = result.getDate("start_date");
                start_year  = start.getYear() + 1900;
                start_month = start.getMonth() + 1;
                start_day   = start.getDate();
                start_date  = start_year*10000 + start_month*100 + start_day;
                end         = result.getDate("end_date");
                end_year    = end.getYear() + 1900;
                end_month   = end.getMonth() + 1;
                end_day     = end.getDate();
                end_date    = end_year*10000 + end_month*100 + end_day;
                TargetFlag  = false;
                if  (!TargetSelected && start_date <= target_date && end_date >= target_date)
                {
                    TargetFlag     = true;
                    TargetSelected = true;
                }
%>
					<tr>
						<td align="right" <% if (count == 1){%>class="tableLWU"<%}else{%>class="tableLW"<%}%> id="line1<%=result.getInt("id")%>" <% if (TargetFlag){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%> width="90">
							 <% if (TargetFlag){%>適用設定<%}%>(<%=count%>)
						</td>
						<td align="left" <% if (count == 1){%>class="tableLWU"<%}else{%>class="tableLW"<%}%> id="line2<%=result.getInt("id")%>" <% if (TargetFlag){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>&nbsp;
							<select>
<%
                title    = result.getString("msg1");
                title    = title + "\r\n";
                temp     = "";
                buf = new StringBuffer();
                for( i = 0 ; i < title.length() ; i++ )
                {
                    char c = title.charAt(i);
                    switch( c )
                    {
                        case '\r':
                            temp = buf.toString();
                            temp = temp.replace("\r\n","");
%>
						<% if(temp.length() > 1){%><option><%=temp%></option><%}%>
<%
                            buf = new StringBuffer();
                            buf.append(c);
                            break;
                        default :
                            buf.append(c);
                           break;
                     }
                 }
%>
							</select>
							<%= start_year %>年<%= start_month %>月<%= start_day %>日～<%= end_year %>年<%= end_month %>月<%= end_day %>日
						</td>
						<form action="report_edit_form.jsp" target="mainFrame" method="post">
						<td align="center" <% if (count == 1){%>class="tableRWU"<%}else{%>class="tableRW"<%}%> id="line3<%=result.getInt("id")%>" <% if (TargetFlag){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%> width="90">
							<input type="hidden" name="Id"    value="<%=result.getInt("id")%>">
							<input type="hidden" name="flg"   value="<%= report_flag %>">
							<input type="hidden" name="Year"  value="<%= year%>">
							<input type="hidden" name="Month" value="<%= month%>">
							<input type="hidden" name="Day"   value="<%= day%>">
							<input type="submit" value="修正">
						</td></form>
					</tr>
<%
           }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

%>
					<tr >
						<td align="right" valign="middle" class="tableLWU" id="line1end"  <% if (!TargetSelected){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%> width="90">
							 <% if (!TargetSelected){%>適用設定<%}%>(999)
						</td>
						<td align="left"  valign="middle" class="tableRWU" id="line2end"  <% if (!TargetSelected){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%> colspan="2">&nbsp;
							<select>
<%
                title    = ReportList[report_flag][type];
                buf = new StringBuffer();
                temp     = "";
                for( i = 0 ; i < title.length() ; i++ )
                {
                    char c = title.charAt(i);
                    switch( c )
                    {
                        case '\r':
                            temp = buf.toString();
                            temp = temp.replace("\r\n","");
%>
							<option><%=temp%></option>
<%
                            buf = new StringBuffer();
                            buf.append(c);
                            break;
                        default :
                            buf.append(c);
                           break;
                     }
                 }
%>
							</select>
							サービス開始～
						</td>
					</tr>
					<tr >
						<form action="report_edit_form.jsp" target="mainFrame" method="post">
						<td align="left" colspan="3">
							<input type="hidden" name="flg"   value="<%= report_flag %>">
							<input type="hidden" name="Year"  value="<%= year%>">
							<input type="hidden" name="Month" value="<%= month%>">
							<input type="hidden" name="Day"   value="<%= day%>">
							<input type="submit" value="新規設定追加">
						</td></form>
					</tr>
				</table>
              </td>
              <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td height="4" valign="top">&nbsp;</td>
              <td align="left" valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
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
    <td align="center" valign="middle" class="size10"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
<%
    db_sec.close();
%>
