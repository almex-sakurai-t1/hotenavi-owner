<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../../common/pc/menu_ini.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />


<%
    // ホテルID取得
    String data_type = request.getParameter("DataType");
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
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>メニューリスト</title>
<link href="http://www.hotenavi.com//<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
</head>

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

function MM_Copy(hotelid,datatype,disptype) {
if  (hotelid=="<%=hotelid%>" && datatype == "<%=data_type%>")
	{
	alert("IDと種類を変更していないので、コピーできません！！");
	}
	else
	{  
	location.href="menu_copy_input.jsp?HotelIdInput=<%=hotelid%>&DataTypeInput=<%=data_type%>&HotelId=" + hotelid + "&DataType=" + datatype + "&DispType=" + disptype;
	}
}
-->
</script>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			

				<tr>
                    <td>
                      <form action="menu_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=0" method=POST>
                        <input name="submit00" type=submit value="新しくメニューを追加" >
                      </form></td><td>
<%
if (disp_type.compareTo("0") == 0)
{
%>
                      <form action="menu_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&DispType=1" method=POST>
                        <input name="submit01" type=submit value="非表示分も表示する" >
                      </form>
<%
}
%>
                    </td>
				</tr>
                  <form name=form1 method=post action="menu_list_input.jsp">
                    <tr>
                      <td colspan="2">
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
						  <td>
						  編集ID    : <input name="HotelIdInput" type=hidden value="<%= hotelid %>" ><strong><%=hotelid%></strong>
						  編集データ: <input name="DataTypeInput" type=hidden value="<%= data_type %>" >
						  <strong>
						  <% if (data_type.compareTo("1") == 0) { %> PC版ビジター <% } %>
						  <% if (data_type.compareTo("2") == 0) { %> PC版メンバー <% } %>
						  <% if (data_type.compareTo("3") == 0) { %> 携帯版ビジター<% } %>
						  <% if (data_type.compareTo("4") == 0) { %> 携帯版メンバー <% } %>
						  <% if (data_type.compareTo("11") == 0) { %> PC版リンク <% } %>
						  <% if (data_type.compareTo("12") == 0) { %> PC版Official <% } %>
						  <% if (data_type.compareTo("13") == 0) { %> 携帯版リンク <% } %>
						  <% if (data_type.compareTo("14") == 0) { %> 携帯版Official <% } %>
						  <% if (data_type.compareTo("16") == 0) { %> 携帯版お知らせ <% } %>
						  </strong>
<% if (!ownerinfo.DbLoginUser.equals("i-enter")){%>
						  （コピー先ID: <input name="hotelid" type=text value="<%= hotelid %>" size="10">
							<select name="datatype">
							<option value = "1" <% if (data_type.compareTo("1") == 0) { %> selected <% } %> >PC版ビジター
							<option value = "2" <% if (data_type.compareTo("2") == 0) { %> selected <% } %> >PC版メンバー
							<option value = "3" <% if (data_type.compareTo("3") == 0) { %> selected <% } %> >携帯版ビジター
							<option value = "4" <% if (data_type.compareTo("4") == 0) { %> selected <% } %> >携帯版メンバー
							<option value = "11" <% if (data_type.compareTo("11") == 0) { %> selected <% } %> >PC版リンク
							<option value = "12" <% if (data_type.compareTo("12") == 0) { %> selected <% } %> >PC版Official
							<option value = "13" <% if (data_type.compareTo("13") == 0) { %> selected <% } %> >携帯版リンク
							<option value = "14" <% if (data_type.compareTo("14") == 0) { %> selected <% } %> >携帯版Official
							<option value = "16" <% if (data_type.compareTo("16") == 0) { %> selected <% } %> >携帯版お知らせ
							</select>
						<input type="button" onClick="MM_Copy(document.form1.hotelid.value, document.form1.datatype.value,<%=disp_type%>)" value="コピー">）
<%}%>
							</td>
                        </tr>
                      </table>
                      </td>
                    </tr>

<%

				String		query;
				String		query_user;
				java.sql.Date start;
				java.sql.Date end;
				Calendar now = Calendar.getInstance();

				int menu_id;
				int yy;
				int mm;
				int dd;
				int start_date;
				int end_date;
				int now_date;
				int last_update;
				int last_uptime;
				int i;
				int seq = 0;
				String user_name = "";
				String msg_data;
				String msg_title;
				String msg_title_color;

				// MySQLへの接続を確立する
				DbAccess db = new DbAccess();

				query = "SELECT * FROM menu WHERE hotelid='" + hotelid + "'";
				query = query + " AND data_type=" + data_type;
				query = query + " ORDER BY disp_idx";

				// SQLクエリーの実行
				ResultSet result = db.execQuery(query);
				while( result.next() != false )
				{

						menu_id =  result.getInt("id");
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

						if(( disp_type.compareTo("1") != 0 && now_date <= end_date ) || (disp_type.compareTo("1") == 0))
						{

 						   // ユーザ一覧・セキュリティ情報取得
							DbAccess db_user = new DbAccess();
			    			ResultSet result_user;
  							query_user = "SELECT * FROM owner_user WHERE hotelid='" + result.getString("upd_hotelid") + "'";
    						query_user = query_user + " AND userid='" + result.getString("upd_userid") + "'";
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
						seq++;
%>

				<tr>
					<td  colspan="2">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellspacing="0" bgcolor="#ffffff"  border="1" cellpadding="0" bordercolor="#666666" width="100%">
							<tr valign="middle">
								<td width="20" >
								<input type="hidden" name="id_<%= seq %>" value="<%= result.getString("id") %>">
									<div><font color="<%= result.getString("title_color") %>"><input name="disp_idx_<%= seq %>" value="<%= result.getString("disp_idx") %>" size="2"></div>
								</td>
								<td width="220" >
									<div><font color="<%= result.getString("title_color") %>"><input name="title_<%= seq %>" value="<%= result.getString("title") %>" size="20"><%= result.getString("decoration") %></div>
								</td>
								<td width="150" >
									<div><font color="<%= result.getString("title_color") %>"><input name="contents_<%= seq %>" value="<%= result.getString("contents") %>" size="20"></div>
								</td>
								<td align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %>" class="subtitle_text" style="color:#000000">

<%if(result.getString("title_color").equals("")){%><font color="<%= result.getString("title_color") %>"><%}%>
<%= start_yy+1900 %>/<%= start_mm+1 %>/<%= start_dd %>～<%= end_yy+1900 %>/<%= end_mm+1 %>/<%= end_dd %>
								<input type="button" onclick="location.href='menu_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>'" value="詳細・削除">
<%
        if( now_date < start_date || now_date > end_date || result.getInt("disp_flg") != 1)
        {
%>
      <br><b>表示していません。表示期間チェック！</b>
<%
        }
%>
<%
    if (Integer.parseInt(data_type) == 1 || Integer.parseInt(data_type) == 3)
    {
        if( result.getInt("hpedit_id") != 0)
        {
%>
								&nbsp;HP編集：「<%=Title[Integer.parseInt(data_type)/2][result.getInt("hpedit_id")-1]%>」
<%
        }
    }
%>
								</td>
							</tr>
						</table>

					</td>
				</tr>

<%
						}
				}
				db.close();
%>
				<tr>

                   <td  colspan="2" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=submit value="保存" ></td>
				</tr>
				<tr>
					<td  colspan="2"><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="500" height="50"></td>
				</tr>


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


