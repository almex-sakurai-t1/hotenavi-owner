<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>

<%
    // �z�e��ID�擾
    String data_type = "100";
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
				int disp_idx_sv = 0;
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
<title>�������</title>
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
	alert("ID�Ǝ�ނ�ύX���Ă��Ȃ��̂ŁA�R�s�[�ł��܂���I�I");
	}
	else
	{  
	location.href="room_copy_input.jsp?HotelIdInput=<%=hotelid%>&HotelId=" + hotelid + "&DispType=" + disptype;
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
                    <td colspan="2">
                      <form action="room_edit_form.jsp?HotelId=<%= hotelid %>&Id=0" method=POST>
                        <input name="submit00" type=submit value="�V����������ǉ�" >
                      </td></form>
                    <td colspan="3">
                      <form action="room_list.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
                        <input name="InputDate" type=text size=9 maxlength=8 style="ime-mode:disable" value="<%=now_date%>">
                        <input name="submit01" type=submit value="�Ώۓ��t�ŕ\������" >
                      </td></form>
<%
if (disp_type.compareTo("0") == 0)
{
%>
                    <td colspan="3">
                      <form action="room_list.jsp?HotelId=<%= hotelid %>&DispType=1" method=POST>
                        <input name="submit01" type=submit value="��\�������\������" >
                    </td></form>
<%
}else{
%>
                    <td colspan="5">
                      <form action="room_list.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
                        <input name="submit01" type=submit value="�\�����̂ݕ\������" >
                    </td></form>
<%
}
%>
				</tr>
                  <form name=form1 method=post action="room_list_input.jsp">
                    <tr>
                      <td colspan="8">
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
						  <td>
						  �ҏWID    : <input name="HotelIdInput" type=hidden value="<%= hotelid %>" ><strong><%=hotelid%></strong>
						  �i�R�s�[��ID: <input name="hotelid" type=text value="<%= hotelid %>" size="10">
						<input type="button" onClick="MM_Copy(document.form1.hotelid.value,<%=disp_type%>)" value="�R�s�[">�j
                        </tr>
                      </table>
                      </td></form>
                    </tr>

<%

				String user_name = "";
				String msg_data;
				String msg_title;
				String msg_title_color;

				// MySQL�ւ̐ڑ����m������
				DbAccess db = new DbAccess();
			    ResultSet result_user;

				query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
//				query = "SELECT DISTINCT(disp_idx),start_date,id,end_date,disp_flg,msg1,title,title_color,upd_userid,upd_hotelid,add_userid,add_hotelid,add_date,add_time,last_update,last_uptime FROM edit_event_info WHERE hotelid='" + hotelid + "'";
			if (disp_type.compareTo("1") != 0)
			{
				query = query + " AND start_date<=" + now_date;
				query = query + " AND end_date>=" + now_date;
				query = query + " AND disp_flg=1";
			}
				query = query + " AND data_type=100";
//				query = query + " GROUP BY disp_idx";
				query = query + " ORDER BY disp_idx,id DESC";

				// SQL�N�G���[�̎��s
				ResultSet result = db.execQuery(query);
				while( result.next() != false )
				{
					if(result.getInt("disp_idx") != disp_idx_sv)
					{
						disp_idx_sv = result.getInt("disp_idx");
						event_id =  result.getInt("id");
						// ���Ԕ���̏���
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

						if(( disp_type.compareTo("1") != 0 && result.getInt("disp_flg") == 1 && now_date <= end_date ) || (disp_type.compareTo("1") == 0))
						{

 						   // ���[�U�ꗗ�E�Z�L�����e�B���擾
  							query_user = "SELECT * FROM owner_user WHERE hotelid='" + result.getString("upd_hotelid") + "'";
    						query_user = query_user + " AND userid='" + result.getString("upd_userid") + "'";
							DbAccess db_user = new DbAccess();
    						result_user = db_user.execQuery(query_user);
						    if( result_user != null )
   		 						{
       						 	if (result_user.next() != false )
									{
									user_name = result_user.getString("name");
									}
								else
									{
									user_name = "�s��";
									} 
								}
							db_user.close();

						seq++;
%>
<% 
if (seq % 4 == 1)
{
%>
				<tr valign="top">
<%
}
%>
					<td  colspan="2">
						<!-- ############### �T�u�^�C�g���o�[ ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)">
								<input type="hidden" name="id_<%= seq %>" value="<%= result.getString("id") %>">
								<td align=left title="�쐬:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
�ҏW:<%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %>" class="subtitle_text"><font color="<%= result.getString("title_color") %>">
									<div class="subtitle_text">
									<font color="<%= result.getString("title_color") %>">
									<input name="disp_idx_<%= seq %>" type=hidden value="<%= result.getString("disp_idx") %>" ><%= result.getString("disp_idx") %>:
									<a href="room_edit_form.jsp?HotelId=<%= hotelid %>&Id=<%= result.getInt("id") %>"><%= result.getString("title") %></a>�ݸ:<%= result.getString("msg1_title") %>
									<br><%= start_yy+1900 %>/<%= start_mm+1 %>/<%= start_dd %>�`<%= end_yy+1900 %>/<%= end_mm+1 %>/<%= end_dd %>
							<%
							String  msg3 =  result.getString("msg3");
						    %><%if(msg3.compareTo("")== 0 || msg3.compareTo(" ")== 0){%><BR>�����摜�Ȃ�<%}%>

<%
        if( now_date < start_date || now_date > end_date || result.getInt("disp_flg") != 1)
        {
%>
      <br><b>�\�����Ă��܂���</b>
<%
        }
%>
									</div>
								</td>
							</tr>
							<tr id="honbun_<%= hotelid %>_<%= event_id %>" style="display:none;">
								<td>
								<table width="166" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" bordercolor="#666666">
									<tr><td width="160" nowrap>
									<font color="#000000">
							<%
							String  msg1 =  result.getString("msg1");
							msg1 = msg1.replace("<br>","&lt;br&gt;");
							msg1 = msg1.replace("\r","<BR>");
						    %>
							<%= msg1 %>
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
                    <tr>
                      <td colspan="8">
                      <table border="0" cellspacing="0" cellpadding="0">
	                  <form name=form2 method=post action="room_str_change.jsp?DispType=<%=disp_type%>">
                        <tr>
							<td class="honbun">
							 �ݔ�
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_str rows=10 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">��</td>
                    		<td class="honbun">
								<textarea class=size name=new_str rows=10 cols=20 ></textarea><br>
							</td>
							<td width="120" class="honbun" rowspan=5><input name="BackUp" type="checkbox" value="1" checked>Backup���c��<br>
							<input type="submit" value="������ϊ�">
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 �J�n��
							</td>
		                    <td class="honbun">
							<input name="moto_date" type="text" size="8" maxlength="8">(yyyymmdd)
							</td>
							<td width="20" class="honbun">��</td>
                    		<td class="honbun">
							  <input name="new_date" type="text" size="8" maxlength="8">(yyyymmdd)
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 �����N
							</td>
		                    <td class="honbun">
							<input name="moto_rank" type="text" size="2" maxlength="2">
							</td>
							<td width="20" class="honbun">��</td>
                    		<td class="honbun">
							  <input name="new_rank" type="text" size="2" maxlength="2">
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 �����摜(PC)
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_img_pc rows=5 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">��</td>
                    		<td class="honbun">
								<textarea class=size name=new_img_pc rows=5 cols=20 ></textarea><br>
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 �����摜(�T���l�C��)
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_thumb rows=5 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">��</td>
                    		<td class="honbun">
								<textarea class=size name=new_thumb rows=5 cols=20 ></textarea><br>
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 �����摜(�g��)
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_img_m rows=5 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">��</td>
                    		<td class="honbun">
								<textarea class=size name=new_img_m rows=5 cols=20 ></textarea><br>
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 �������l(PC)
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_memo_pc rows=5 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">��</td>
                    		<td class="honbun">
								<textarea class=size name=new_memo_pc rows=5 cols=20 ></textarea><br>
							</td>
                        </tr>
                      </table>
                      </td></form>
                    </tr>
			</table>
		</td>
	</tr>
	
	<tr valign="bottom">
		<td>
			<!-- copyright �e�[�u�� -->
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


