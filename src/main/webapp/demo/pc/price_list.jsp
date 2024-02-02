<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>

<%
    // ホテルID取得
    String disp_type = request.getParameter("DispType");
if  (disp_type == null)
	{
	disp_type = "0";
	}
   
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
     hotelid = (String)session.getAttribute("SelectHotel");
    }
    if( hotelid == null )
    {
%>
 
        <jsp:forward page="error/error.html" />
<%
    }
				String		query;
				String		query_user;
				java.sql.Date start;
				java.sql.Date end;
				Calendar now = Calendar.getInstance();

				int event_id;
				int data_type   = 0;
				int data_type_sv = 999;
				int disp_idx_sv = 999;
				int yy;
				int mm;
				int dd;
				int start_date;
				int end_date;
				int now_date;
				now_date = now.get(now.YEAR)*10000 + (now.get(now.MONTH)+1)*100 + now.get(now.DATE);
                String InputDate = request.getParameter("InputDate");
                if    (InputDate != null)  now_date = Integer.parseInt(InputDate);
				int last_update;
				int last_uptime;
				int i;
				int seq = 0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>料金情報</title>
<link href="http://www.hotenavi.com//<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
<!--
function disp_find(id,obj) {
	target = document.all(id);
	if(target.style.display == "none"){
		target.style.display = "block";
	} else {
		target.style.display = "none";
	}
}

function MM_Copy(hotelid,disptype) {
if  (hotelid=="<%=hotelid%>")
	{
	alert("IDと種類を変更していないので、コピーできません！！");
	}
	else
	{  
	location.href="price_copy_input.jsp?HotelIdInput=<%=hotelid%>&HotelId=" + hotelid + "&DispType=" + disptype;
	}
}
-->
</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			

				<tr>
                    <td colspan="1">
                      <form action="price_edit_form.jsp?HotelId=<%= hotelid %>&Id=0" method=POST>
                        <input name="submit00" type=submit value="新しく料金を追加" >
                      </td></form>
                    <td colspan="2">
                      <form action="price_list.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
                        <input name="InputDate" type=text size=9 maxlength=8 style="ime-mode:disable" value="<%=now_date%>">
                        <input name="submit01" type=submit value="対象日付で表示する" >
                      </td></form>
<%
if (disp_type.compareTo("0") == 0)
{
%>
                    <td colspan="5">
                      <form action="price_list.jsp?HotelId=<%= hotelid %>&DispType=1" method=POST>
                        <input name="submit01" type=submit value="非表示分も表示する" >
                    </td></form>
<%
}else{
%>
                    <td colspan="5">
                      <form action="price_list.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
                        <input name="submit01" type=submit value="表示分のみ表示する" >
                    </td></form>
<%
}
%>
				</tr>
                  <form name=form1 method=post action="price_list_input.jsp">
                    <tr>
                      <td colspan="8">
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
						  <td>
						  編集ID    : <input name="HotelIdInput" type=hidden value="<%= hotelid %>" ><strong><%=hotelid%></strong>
						  （コピー先ID: <input name="hotelid" type=text value="<%= hotelid %>" size="10">
						<input type="button" onClick="MM_Copy(document.form1.hotelid.value,<%=disp_type%>)" value="コピー">）
                        </tr>
                      </table>
                      </td>
                    </tr>

<%

				String user_name = "";
				String msg_data;
				String msg_title;
				String msg_title_color;

				// MySQLへの接続を確立する
				DbAccess db = new DbAccess();

				query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
//				query = "SELECT DISTINCT(disp_idx),start_date,id,end_date,disp_flg,msg1,title,title_color,upd_userid,upd_hotelid,add_userid,add_hotelid,add_date,add_time,last_update,last_uptime FROM edit_event_info WHERE hotelid='" + hotelid + "'";
			if (disp_type.compareTo("1") != 0)
			{
				query = query + " AND start_date<=" + now_date;
				query = query + " AND end_date>=" + now_date;
				query = query + " AND disp_flg=1";
			}
				query = query + " AND data_type>=200";
				query = query + " AND data_type<300";
				query = query + " ORDER BY data_type,id DESC";

				// SQLクエリーの実行
				ResultSet result = db.execQuery(query);
				while( result.next() != false )
				{
						event_id  =  result.getInt("id");
						data_type =  result.getInt("data_type");
						// 時間判定の準備
						start = result.getDate("start_date");
						yy = start.getYear();
						mm = start.getMonth();
						dd = start.getDate();
						start_date = (yy+1900)*10000 + (mm+1)*100 + dd;
			            int start_yy = yy;
				        int start_mm = mm;
			            int start_dd = dd;

						end = result.getDate("end_date");
						yy = end.getYear();
						mm = end.getMonth();
						dd = end.getDate();
						end_date = (yy+1900)*10000 + (mm+1)*100 + dd;
			            int end_yy = yy;
			            int end_mm = mm;
			            int end_dd = dd;
						yy = now.get(Calendar.YEAR);
						mm = now.get(Calendar.MONTH);
						dd = now.get(Calendar.DATE);
						now_date = (yy)*10000 + (mm+1)*100 + dd;

						if(( disp_type.compareTo("1") != 0 && result.getInt("disp_flg") == 1 && now_date <= end_date ) || (disp_type.compareTo("1") == 0) )
						{
							if(data_type_sv != data_type)
							{
							data_type_sv = data_type;
 						   // ユーザ一覧・セキュリティ情報取得
  							query_user = "SELECT * FROM owner_user WHERE hotelid='" + result.getString("upd_hotelid") + "'";
    						query_user = query_user + " AND userid='" + result.getString("upd_userid") + "'";
							DbAccess db_user = new DbAccess();
						    ResultSet result_user;
    						result_user = db_user.execQuery(query_user);
						    if( result_user != null )
   		 						{
       						 	if (result_user.next() != false )
									{
									user_name = result_user.getString("name");
									}
								else
									{
									user_name = "不明";
									} 
								}
							db_user.close();
%>
<% 
if (seq == 0 && data_type == 200)//料金システム
{
%>
				<tr valign="top">
					<td  colspan="1">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr onclick="disp_find('honbun_<%= hotelid %>_<%= data_type %>',this)">
								<input type="hidden" name="id_<%= seq %>" value="<%= result.getString("id") %>">
								<td align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %>" class="subtitle_text"><font color="<%= result.getString("title_color") %>">
									<div class="subtitle_text">
									<font color="<%= result.getString("title_color") %>">
									<input name="data_type_<%= seq %>" type=hidden value="<%= data_type %>" ><%= data_type-200 %>:
									<a href="price_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>"><%if (result.getString("title").compareTo("")==0){%>料金システム<%}else{%><%= result.getString("title") %><%}%></a>
									<input name="disp_idx_<%= seq %>" type=hidden value="<%=result.getString("disp_idx") %>" >
									<br><%= start_yy+1900 %>/<%= start_mm+1 %>/<%= start_dd %>～<%= end_yy+1900 %>/<%= end_mm+1 %>/<%= end_dd %>
<%
        if( now_date < start_date || now_date > end_date || result.getInt("disp_flg") != 1)
        {
%>
      <br><b>表示していません</b>
<%
        }
%>
									</div>
								</td>
							</tr>
							<tr id="honbun_<%= hotelid %>_<%= data_type %>" style="display:none;">
								<td>
								<table width="166" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" bordercolor="#666666">
									<tr><td width="160" nowrap>
									<font color="#000000">
							<%
							String  msg1 =  result.getString("msg1");
							msg1 = msg1.replace("<br>","&lt;br&gt;");
							msg1 = msg1.replace("\r\n","<BR>");
						    %>
							<%= msg1 %>
							<%
							String  msg2 =  result.getString("msg2");
							msg2 = msg2.replace("<br>","&lt;br&gt;");
							msg2 = msg2.replace("\r\n","<BR>");
						    %>
						<% if(msg2.compareTo("") != 0){%>
							<hr>
							<%= msg2 %>
						<%}%>
							<%
							String  msg3 =  result.getString("msg3");
							msg3 = msg3.replace("<br>","&lt;br&gt;");
							msg3 = msg3.replace("\r\n","<BR>");
						    %>
						<% if(msg3.compareTo("") != 0){%>
							<hr>
							<%= msg3 %>
						<%}%>
									</font><br>
									</td></tr>
								</table>
							</td></tr>
						</table>
					</td>
				</tr>
				<tr>
				<td>&nbsp;</td>
				</tr>
<%
}
else
{
	seq++;
	if (seq % 4 == 1)
	{
%>
				<tr valign="top">
<%
	}
%>
					<td  colspan="1">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr onclick="disp_find('honbun_<%= hotelid %>_<%= data_type %>',this)">
								<input type="hidden" name="id_<%= seq %>" value="<%= result.getString("id") %>">
								<td align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %>" class="subtitle_text"><font color="<%= result.getString("title_color") %>">
									<div class="subtitle_text">
									<font color="<%= result.getString("title_color") %>">
									<input name="data_type_<%= seq %>" type=hidden value="<%= data_type %>" ><%= data_type-200 %>:
									<a href="price_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>"><%if (result.getString("title").compareTo("")==0){%>名称なし<%}else{%><%= result.getString("title") %><%}%></a>
									<input name="disp_idx_<%= seq %>" type=hidden value="<%= result.getString("disp_idx") %>" >
<!--
									<br>表示順：<input name="disp_idx_<%= seq %>" type=hidden value="<%= result.getString("disp_idx") %>" ><%= result.getString("disp_idx") %>
-->
									<br><%= start_yy+1900 %>/<%= start_mm+1 %>/<%= start_dd %>～<%= end_yy+1900 %>/<%= end_mm+1 %>/<%= end_dd %>
<%
        if( now_date < start_date || now_date > end_date || result.getInt("disp_flg") != 1)
        {
%>
      <br><b>表示していません</b>
<%
        }
%>
									</div>
								</td>
							</tr>
							<tr id="honbun_<%= hotelid %>_<%= data_type %>" style="display:none;">
								<td>
								<table width="166" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" bordercolor="#666666">
									<tr><td width="160" nowrap>
									<font color="#000000">
							<%
							String  msg1 =  result.getString("msg1");
							msg1 = msg1.replace("<br>","&lt;br&gt;");
							msg1 = msg1.replace("\r\n","<BR>");
						    %>
							<%= msg1 %>
							<%
							String  msg2 =  result.getString("msg2");
							msg2 = msg2.replace("<br>","&lt;br&gt;");
							msg2 = msg2.replace("\r\n","<BR>");
						    %>
						<% if(msg2.compareTo("") != 0){%>
							<hr>
							<%= msg2 %>
						<%}%>
							<%
							String  msg3 =  result.getString("msg3");
							msg3 = msg3.replace("<br>","&lt;br&gt;");
							msg3 = msg3.replace("\r\n","<BR>");
						    %>
						<% if(msg3.compareTo("") != 0){%>
							<hr>
							<%= msg3 %>
						<%}%>
									</font><br>
									</td></tr>
								</table>
							</td></tr>
						</table>
					</td>
<% 
	if (seq % 4 == 0)
	{
%>
				</tr><tr><td>&nbsp;</td></tr>
<%
	}
%>

<%
}
						}
					}
				}
%>
<% 
if (seq % 4 != 0)
{
%>
				</tr>
<%
}
%>

<%
				db.close();
%>
			</table>
		</td>
	</tr>
	
	<tr valign="bottom">
		<td>
			<!-- copyright テーブル -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" valign="middle">
						<div class="copyright">
							Copy right &copy; imedia inc, All Rights Reserved
						</div>
					</td>
				</tr>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="500" height="20"></td>
				</tr>
			</table>
		
		</td>
	</tr>
</table>

</body>
</html>


