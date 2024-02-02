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

   String edit_type = request.getParameter("EditType");
	if  (edit_type == null)
	{
	edit_type = "0";
	}
	
	String param_seq = request.getParameter("seq");
	if  (param_seq == null)
	{
	param_seq = "0";
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
<%	
	int result_upd = 0;
	if (edit_type.compareTo("2") == 0)
	{
		String target_rank = "0";
		String target_id = "0";
		String target_disp_index = "0";
		int param_intseq = 0;
		param_intseq = Integer.parseInt(param_seq);

		Connection        connection_upd  = null;
		PreparedStatement prestate_upd    = null;
		connection_upd  = DBConnection.getConnection();
			
		for (int i = 1; i <= param_intseq; i++)
		{
			target_rank = request.getParameter("rank_"+i);
			target_id = request.getParameter("id_"+i);
			target_disp_index = request.getParameter("disp_index_"+i);
		
			//データ更新
			String query_upd = "";
			query_upd = "UPDATE roomrank SET ";
			query_upd = query_upd + "disp_index= " + target_disp_index;
			query_upd = query_upd + " WHERE hotelid='" + hotelid + "' AND rank=" + target_rank + " AND id=" + target_id;
			// SQLクエリーの実行
			prestate_upd = connection_upd.prepareStatement(query_upd);
			result_upd = prestate_upd.executeUpdate();
		}
		DBConnection.releaseResources(prestate_upd);
		DBConnection.releaseResources(connection_upd);
		
	}
%>
<%


	
				String		query;
				String		query_user;
				java.sql.Date start;
				java.sql.Date end;
				Calendar now = Calendar.getInstance();

				int event_id;
				int rank    = 0;
				int disp_index    = 0;
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
				boolean sync_flag = false;

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
if("<%=edit_type%>" == "2")
{
	if("<%=result_upd%>" == 1)
	{
		alert("更新しました");
	}
	else
	{
		alert("更新に失敗しました");
	}
}

function dispAllFind(){
	var targets = document.getElementsByName("honbun");
	var array = targets.length;
	for(var i = 0; i < array ; i++){
		targets[i].style.display = "block";
	}
}

function dispAllClose(){
	var targets = document.getElementsByName("honbun");
	var array = targets.length;
	for(var i = 0; i < array ; i++){
		targets[i].style.display = "none";
	}
}

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
	location.href="roomrank_copy_input2.jsp?HotelIdInput=<%=hotelid%>&HotelId=" + hotelid + "&DispType=" + disptype;
	}
}

function MM_Update(seq) {

	document.form2.seq.value = seq;
	document.form2.submit();
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
                      <form action="roomrank_edit_form2.jsp?HotelId=<%= hotelid %>&Id=0" method=POST>
                        <input name="submit00" type=submit value="新しく料金を追加" >
                      </td></form>
                    <td colspan="">
                      <form action="roomrank_list2.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
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
                      <form action="roomrank_list2.jsp?HotelId=<%= hotelid %>&DispType=1" method=POST>
                        <input name="submit01" type=submit value="非表示分も表示する" >
                    </td></form>
<%
}else{
%>
                    <td colspan="5">
                      <form action="roomrank_list2.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
                        <input name="submit01" type=submit value="表示分のみ表示する" >
                    </td></form>
<%
}
%>
				</tr>
				<tr>
				  <td colspan="8">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					  <tr>
					    <form name=form1 method=post action="roomrank_copy_input2.jsp">
						  <td>
							編集ID    : <input name="HotelIdInput" type=hidden value="<%= hotelid %>" ><strong><%=hotelid%></strong>
							<% if (!ownerinfo.DbLoginUser.equals("i-enter")){%>
							（コピー先ID: <input name="hotelid" type=text value="<%= hotelid %>" size="10">
							<input type="button" onClick="MM_Copy(document.form1.hotelid.value,<%=disp_type%>)" value="コピー">）
							<%}%>
							<input type="button" value="全て展開" onclick="dispAllFind()">
							<input type="button" value="全て閉じる" onclick="dispAllClose()">
						  </td>
						</form>
                      </tr>
                    </table>
                  </td>
                </tr>
				<form name=form2 action="roomrank_list2.jsp?HotelId=<%= hotelid %>&DispType=<%=disp_type%>&EditType=2" method=POST>
<%
				String user_name = "";

				// MySQLへの接続を確立する
				Connection        connection  = null;
				PreparedStatement prestate    = null;
				ResultSet         result      = null;
				connection  = DBConnection.getConnection();
				Connection        connection_user  = null;
				PreparedStatement prestate_user    = null;
				ResultSet         result_user      = null;
				connection_user  = DBConnection.getConnection();

				query = "SELECT * FROM roomrank AS roomrankA ";
				query = query + " WHERE roomrankA.hotelid='" + hotelid + "'";
				if (disp_type.compareTo("1") != 0)
				{
					query = query + " AND roomrankA.start_date<=" + now_date;
					query = query + " AND roomrankA.end_date>=" + now_date;
					query = query + " AND roomrankA.disp_flg=1";
				}
				query = query + " AND NOT EXISTS (";
				query = query + " SELECT 1";
				query = query + " FROM roomrank AS s";
				query = query + " WHERE roomrankA.hotelid = s.hotelid";
				query = query + " AND roomrankA.rank= s.rank";
				query = query + " AND roomrankA.id < s.id";
				if (disp_type.compareTo("1") != 0)
				{
					query = query + " AND s.start_date<=" + now_date;
					query = query + " AND s.end_date>=" + now_date;
					query = query + " AND s.disp_flg=1";
				}
				query = query + " )";
				if (edit_type.compareTo("0") == 0)
				{
					query = query + " ORDER BY roomrankA.rank, roomrankA.id DESC";
				}else{
					query = query + " ORDER BY roomrankA.disp_index, roomrankA.rank, roomrankA.id DESC";
				}
				prestate = connection.prepareStatement(query);
				result   = prestate.executeQuery();
				while( result.next() != false )
				{
						event_id  =  result.getInt("id");
						rank =  result.getInt("rank");
						disp_index =  result.getInt("disp_index");
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
                        if(result.getInt("sync_flag") == 1)
                        {
							sync_flag = true;
                        }

						if(( disp_type.compareTo("1") != 0 && result.getInt("disp_flg") == 1 && now_date <= end_date ) || (disp_type.compareTo("1") == 0) )
						{
 						   		// ユーザ一覧・セキュリティ情報取得
  								query_user = "SELECT * FROM owner_user WHERE hotelid='" + result.getString("upd_hotelid") + "'";
    							query_user = query_user + " AND userid='" + result.getString("upd_userid") + "'";
								prestate_user = connection_user.prepareStatement(query_user);
								result_user   = prestate_user.executeQuery();
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
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="180">
							<% if (edit_type.compareTo("0") == 0){%>
								<tr onclick="disp_find('honbun_<%= hotelid %>_<%= rank %>',this)">
							<%}else{%>
								<tr>
							<% } %>
								<input type="hidden" name="id_<%= seq %>" value="<%= result.getString("id") %>">
								<td align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %>" class="subtitle_text">
									<div class="subtitle_text">
									<input name="rank_<%= seq %>" type=hidden value="<%= rank %>" ><%= rank %>:
									<a href="roomrank_edit_form2.jsp?HotelId=<%= hotelid %>&Rank=<%= rank %>&Id=<%= result.getInt("id") %>"><%if (result.getString("rank_name").compareTo("")==0){%>名称なし<%}else{%><%= result.getString("rank_name") %><%}%></a>
									<span id="disp_index_<%= hotelid %>_<%= rank %>" name="disp_index" >
										<% if (edit_type.compareTo("0") == 0){%>
											<%if (disp_index>0){%><font color='red'>（新表示順：<%= disp_index %>）</font><%}%>	
										<%}else{%>
											<input name="disp_index_<%= seq %>" id="disp_index_<%= seq %>" type="text" size="2" maxlength="2" value=<%= disp_index %> onkeyUp="this.value=this.value.replace(/[^0-9]+/i,'')">
										<% } %>
									</span>
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
							<tr id="honbun_<%= hotelid %>_<%= rank %>" name="honbun" style="display:none;">
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
							<%
							String  system_mobile =  result.getString("system_mobile");
							system_mobile = system_mobile.replace("<br>","&lt;br&gt;");
							system_mobile = system_mobile.replace("\r\n","<BR>");
						    %>
						<% if(system_mobile.compareTo("") != 0){%>
							<hr>
							<%= system_mobile %>
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
				DBConnection.releaseResources(result_user);
				DBConnection.releaseResources(prestate_user);
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
</form>
<%
				DBConnection.releaseResources(connection_user);
				DBConnection.releaseResources(result);
				DBConnection.releaseResources(prestate);
%>
<% 
if (seq == 0)
{
%>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="1" height="20"></td>
				</tr>

				<tr valign="top">
					<td  colspan="1">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr id="" >
								<td>
                      <form action="room_ini_form.jsp?HotelId=<%= hotelid %>" method=POST>
                        <input name="submit99" type=submit value="ランクごと部屋情報一括作成" >
                      </td></form>
							</tr>
						</table>
					</td>
				</tr>
<%
}
%>
<% 
if (sync_flag)
{
                int hotenavi_count = 0;
                query  = "SELECT count(hh_hotel_basic.id) AS hotenavi_count FROM hh_hotel_basic ";
                query += " INNER JOIN hotel ON hotel.hotel_id = hh_hotel_basic.hotenavi_id";
                query += " AND hotel.plan IN (1,3,4)";
                query += " WHERE hh_hotel_basic.hotenavi_id='" + hotelid + "'";
                query += " GROUP BY hh_hotel_basic.hotenavi_id";
                prestate = connection.prepareStatement(query);
                result   = prestate.executeQuery();
                if( result.next() != false )
                {
                    hotenavi_count = result.getInt(1);
                }
                DBConnection.releaseResources(result);
                DBConnection.releaseResources(prestate);
                if (hotenavi_count != 0)
                {
%>
<script>alert("ハッピー・ホテル用データを作成してください！");</script>
<%
                }
				query = "SELECT * FROM hh_hotel_basic WHERE hh_hotel_basic.hotenavi_id='" + hotelid + "'";
				prestate = connection.prepareStatement(query);
				result   = prestate.executeQuery();
				while( result.next() != false )
				{
%>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="1" height="20"></td>
				</tr>

				<tr valign="top">
					<td  colspan="4">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr id="" >
								<td>
<%
                    if (hotenavi_count == 1)
                    {
%>                      <form action="hh_roomrank_input.jsp?HotelId=<%= hotelid %>&Id=<%=result.getInt("hh_hotel_basic.id")%>" method=POST>
                        <input name="submit99" type=submit value="<%=result.getString("hh_hotel_basic.name")%>（<%=result.getInt("hh_hotel_basic.id")%>）ハッピー・ホテル用データ作成" >
                      </form>
<%
                    }
                    else
                    {
%>						ホテナビ物件ホームページがある物件ではありません。ハッピーホテル用データ作成をして問題がないか確認してください。
<form action="hh_roomrank_input.jsp?HotelId=<%= hotelid %>&Id=<%=result.getInt("hh_hotel_basic.id")%>" method=POST>
	<input name="submit99" type=submit value="<%=result.getString("hh_hotel_basic.name")%>（<%=result.getInt("hh_hotel_basic.id")%>）ハッピー・ホテル用データ作成" >
  </form>
<%
                    }
%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
<%
				}
				DBConnection.releaseResources(result);
				DBConnection.releaseResources(prestate);
}
				DBConnection.releaseResources(connection);
%>
			</table>
		</td>
	</tr>
	
	<tr>
		<% if (edit_type.compareTo("0") == 0){%>
		<td colspan="5">
		  <form action="roomrank_list2.jsp?HotelId=<%= hotelid %>&DispType=<%=disp_type%>&EditType=1" method=POST>
			<input name="submit01" type=submit value="表示順変更（新ホテナビのみ）" >
		</td></form>
		<%}else{%>
		<td colspan="5">
			<form action="roomrank_list2.jsp?HotelId=<%= hotelid %>&DispType=<%=disp_type%>&EditType=0" method=POST>
				<input name="submit01" type=submit value="戻る" >
				<input name="submit01" type=button value="更新" onclick="MM_Update(<%=seq%>);" >
			</form>
			
		<td >	
		<%}%>
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


