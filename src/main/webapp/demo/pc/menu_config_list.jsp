<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../../common/pc/menu_ini.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />


<%
    // �z�e��ID�擾
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
 
        <jsp:forward page="timeout.html?<%=DataEdit.getDate(2)%><%=DateEdit.getTime(1)%>" />
<%
    }
	boolean existData = true;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>�V�z�e�i�r�Ή��@���j���[���X�g</title>
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
	alert("ID�Ǝ�ނ�ύX���Ă��Ȃ��̂ŁA�R�s�[�ł��܂���I�I");
	}
	else
	{  
	location.href="menu_config_copy_input.jsp?HotelIdInput=<%=hotelid%>&DataTypeInput=<%=data_type%>&HotelId=" + hotelid + "&DataType=" + datatype + "&DispType=" + disptype;
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
                      <form action="menu_config_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=0" method=POST>
                        <input name="submit00" type=submit value="�V�������j���[��ǉ�" >
                      </form></td><td>
<%
if (disp_type.compareTo("0") == 0)
{
%>
                      <form action="menu_config_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&DispType=1" method=POST>
                        <input name="submit01" type=submit value="��\�������\������" >
                      </form>
<%
}
%>
                    </td>
				</tr>
                  <form name=form1 method=post action="menu_config_list_input.jsp">
                    <tr>
                      <td colspan="2">
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
						  <td>
						  �y�V�z�e�i�r�Ή��z�ҏWID    : <input name="HotelIdInput" type=hidden value="<%= hotelid %>" ><strong><%=hotelid%></strong>
						  �ҏW�f�[�^: <input name="DataTypeInput" type=hidden value="<%= data_type %>" >
						  <strong>
						  <% if (data_type.compareTo("1") == 0) { %> PC�Ńr�W�^�[ <% } %>
						  <% if (data_type.compareTo("2") == 0) { %> PC�Ń����o�[ <% } %>
						  <% if (data_type.compareTo("11") == 0) { %> PC�Ń����N <% } %>
						  <% if (data_type.compareTo("12") == 0) { %> PC��Official <% } %>
						  </strong>
						  �i�R�s�[��ID: <input name="hotelid" type=text value="<%= hotelid %>" size="10">
							<select name="datatype">
							<option value = "1" <% if (data_type.compareTo("1") == 0) { %> selected <% } %> >PC�Ńr�W�^�[
							<option value = "2" <% if (data_type.compareTo("2") == 0) { %> selected <% } %> >PC�Ń����o�[
							<option value = "11" <% if (data_type.compareTo("11") == 0) { %> selected <% } %> >PC�Ń����N
							<option value = "12" <% if (data_type.compareTo("12") == 0) { %> selected <% } %> >PC��Official
							</select>
						<input type="button" onClick="MM_Copy(document.form1.hotelid.value, document.form1.datatype.value,<%=disp_type%>)" value="�R�s�[">�j
                        </tr>
                      </table>
                      </td>
                    </tr>
					<tr>
					<td  colspan="2">
						<!-- ############### �T�u�^�C�g���o�[ ############### -->				
						<table cellspacing="0" bgcolor="#ffffff"  border="1" cellpadding="0" bordercolor="#666666" width="100%">
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

				Connection        connection  = null;
				PreparedStatement prestate    = null;
				ResultSet         result      = null;

				connection  = DBConnection.getConnection();
				query = "SELECT * FROM menu_config WHERE hotelid='" + hotelid + "'";
				query = query + " AND data_type=" + data_type;
				query = query + " ORDER BY disp_idx";
				prestate = connection.prepareStatement(query);
				result   = prestate.executeQuery();
				if ( result.next() == false )
				{
					existData  = false;
				}
				result.beforeFirst();
				while( result.next() != false )
				{
						menu_id =  result.getInt("id");
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

						if(( disp_type.compareTo("1") != 0 && now_date <= end_date ) || (disp_type.compareTo("1") == 0))
						{

 						   // ���[�U�ꗗ�E�Z�L�����e�B���擾
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
									user_name = "�s��";
									} 
								}
							db_user.close();
						seq++;
%>
<%                          if (seq == 1)
                            {%>	<tr valign="middle">
								<td>�\��<br>����</td>
								<td>�^�C�g��</td>
								<td>�y�[�W<br>�^�C�g��</td>
								<td>�R���e���c</td>
								<td>menu<br>�Ȃ�</td>
								<td>�D��<br>����</td>
								<td>���ԑ�</td>
							</tr>
<%                          }%>	<tr valign="middle">
								<td>
								<input type="hidden" name="id_<%= seq %>" value="<%= result.getString("id") %>">
									<div><font color="<%= result.getString("title_color") %>"><input name="disp_idx_<%= seq %>" value="<%= result.getString("disp_idx") %>" size="2"></font></div>
								</td>
								<td>
									<div><font color="<%= result.getString("title_color") %>"><input name="title_<%= seq %>" value="<%= result.getString("title") %>" size="20"><%= result.getString("decoration") %></font></div>
								</td>
								<td>
									<div><font color="<%= result.getString("title_color") %>"><input name="page_title_<%= seq %>" value="<%= result.getString("page_title") %>" size="20"></font></div>
								</td>
								<td>
									<div><font color="<%= result.getString("title_color") %>"><input name="contents_<%= seq %>" value="<%= result.getString("contents") %>" size="10"></font></div>
								</td>
								<td>
									<div><font color="<%= result.getString("title_color") %>"><input type="checkbox" name="page_only_flg_<%= seq %>" <% if(result.getInt("page_only_flg")==1){%>checked<%}%>></font></div>
								</td>
								<td>
									<div><input name="priority_index_<%= seq %>" value="<%if (result.getString("priority_index") != null){%><%= result.getInt("priority_index") %><%}%>" size="2"></div>
								</td>
								<td align=left title="�쐬:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
�ҏW:<%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %>" class="subtitle_text" style="color:#000000">

<%if(result.getString("title_color").equals("")){%><font color="<%= result.getString("title_color") %>"><%}%>
<%= start_yy+1900 %>/<%= start_mm+1 %>/<%= start_dd %>�`<%= end_yy+1900 %>/<%= end_mm+1 %>/<%= end_dd %>
								<input type="button" onclick="location.href='menu_config_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>'" value="�ڍׁE�폜">
<%                          if( now_date < start_date || now_date > end_date || result.getInt("disp_flg") != 1)
                            {
%>
      <br><b>�\�����Ă��܂���B�\�����ԃ`�F�b�N�I</b>
<%
                            }
                                if( result.getInt("hpedit_id") != 0)
                                {
%>
								&nbsp;HP�ҏW�F�u<%=Title[0][result.getInt("hpedit_id")-1]%>�v
<%                              }
%>								</td>
							</tr>
<%
						}
				}
				DBConnection.releaseResources(result,prestate,connection);
%>
						</table>
					</td>
				</tr>
				<tr>
                   <td  colspan="2" align="center" valign="middle" bgcolor="#969EAD">
						<input name="seq" value="<%= seq %>" type="hidden">
						<input name="regsubmit" type=submit value="�ۑ�" >
				   </td>
				</tr>
				</form>
				<tr>
					<td  colspan="2"><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="500" height="50"></td>
				</tr>
				<% if(!existData){%>
				<tr>
					<td>
						<form name=form2 method=post action="menu_config_convert.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>">
							<input name="convsubmit" type=submit value="�R���o�[�g" >
						</form>
					</td>
				</tr>
				<%}%>
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
