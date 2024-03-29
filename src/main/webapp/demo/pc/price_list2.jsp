<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

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
<title>料金システム</title>
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
	location.href="price_copy_input2.jsp?HotelIdInput=<%=hotelid%>&HotelId=" + hotelid + "&DispType=" + disptype;
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
                      <form action="price_edit_form2.jsp?HotelId=<%= hotelid %>&Id=0" method=POST>
                        <input name="submit00" type=submit value="新しく料金システムを追加" >
                      </td></form>
                    <td colspan="2">
                      <form action="price_list2.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
                        <input name="InputDate" id="InputDate" type=text size=9 maxlength=8 style="ime-mode:disable" value="<%=now_date%>">
                        <input name="submit01" type=submit value="対象日付で表示する" >
                      </td></form>
                    <td colspan="1">
                      <form action="http://www.hotenavi.com/<%=hotelid%>/priceinfo.jsp" method=POST target="_blank">
                        <input name="date" id="websitedate" type=hidden value="<%=now_date%>">
                        <input name="hotel" id="websitehotel" type=hidden value="<%=hotelid%>">
                        <input name="submit02" type=submit value="WebSite" onclick="document.getElementById('websitedate').value=document.getElementById('InputDate').value;">
                      </td></form>
<%
if (disp_type.compareTo("0") == 0)
{
%>
                    <td colspan="5">
                      <form action="price_list2.jsp?HotelId=<%= hotelid %>&DispType=1" method=POST>
                        <input name="submit01" type=submit value="非表示分も表示する" >
                    </td></form>
<%
}else{
%>
                    <td colspan="5">
                      <form action="price_list2.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
                        <input name="submit01" type=submit value="表示分のみ表示する" >
                    </td></form>
<%
}
%>
				</tr>
                  <form name=form1 method=post action="price_copy_input2.jsp">
                    <tr>
                      <td colspan="8">
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
						  <td>
						  編集ID    : <input name="HotelIdInput" type=hidden value="<%= hotelid %>" ><strong><%=hotelid%></strong>
<% if (!ownerinfo.DbLoginUser.equals("i-enter")){%>
						  （コピー先ID: <input name="hotelid" type=text value="<%= hotelid %>" size="10">
						<input type="button" onClick="MM_Copy(document.form1.hotelid.value,<%=disp_type%>)" value="コピー">）
<%}%>							</td>
                        </tr>
                      </table>
                      </td>
                    </tr>

<%

				String user_name = "";
				// MySQLへの接続を確立する
				DbAccess db = new DbAccess();

				query = "SELECT * FROM price WHERE hotelid='" + hotelid + "'";
			if (disp_type.compareTo("1") != 0)
			{
				query = query + " AND start_date<=" + now_date;
				query = query + " AND end_date>=" + now_date;
				query = query + " AND disp_flg=1";
			}
				query = query + " ORDER BY id DESC";

				// SQLクエリーの実行
				ResultSet result = db.execQuery(query);
				if( result.next() != false )
				{
						event_id  =  result.getInt("id");
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
				<tr valign="top">
					<td  colspan="1">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr onclick="disp_find('honbun_<%= hotelid %>',this)">
								<input type="hidden" name="id_<%= seq %>" value="<%= result.getString("id") %>">
								<td align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %>" class="subtitle_text">
									<div class="subtitle_text">
									<a href="price_edit_form2.jsp?HotelId=<%= hotelid %>&Id=<%= result.getInt("id") %>"><%if (result.getString("price_name").compareTo("")==0){%>料金システム<%}else{%><%= result.getString("price_name") %><%}%></a>
									<br><%= start_yy+1900 %>/<%= start_mm+1 %>/<%= start_dd %>〜<%= end_yy+1900 %>/<%= end_mm+1 %>/<%= end_dd %>
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
							<tr id="honbun_<%= hotelid %>" style="display:none;">
							<td>
								<table width="166" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" bordercolor="#666666">
									<tr><td width="160" nowrap>
									<font color="#000000">
							<%
							String  system =  result.getString("system");
							system = system.replace("<br>","&lt;br&gt;");
							system = system.replace("\r\n","<BR>");
						    %>
							<%= system %>
							<%
							String  memo =  result.getString("memo");
							memo = memo.replace("<br>","&lt;br&gt;");
							memo = memo.replace("\r\n","<BR>");
						    %>
						<% if(memo.compareTo("") != 0){%>
							<hr>
							<%= memo %>
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


