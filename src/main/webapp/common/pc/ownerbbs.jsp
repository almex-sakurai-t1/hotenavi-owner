<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
	//�@�I���z�e���擾
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    String loginhotel = (String)session.getAttribute("LoginHotelId");

    DbAccess db =  new DbAccess();
    DbAccess db_history =  new DbAccess();

    // �Z�L�����e�B���̎擾
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db, loginhotel, ownerinfo.DbUserId);

    // �Z�L�����e�B�`�F�b�N
    if( DbUserSecurity == null )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }

    // �ŏI���O�C�������擾
    String lastlogin = "�Ȃ�";

    ResultSet DbLoginHistory = ownerinfo.getLoginHistory(db_history, loginhotel, ownerinfo.DbUserId);
    if( DbLoginHistory != null )
    {
        if( DbLoginHistory.last() != false )
        {
            lastlogin = DbLoginHistory.getDate("login_date") +
                        "&nbsp;" +
                        DbLoginHistory.getTime("login_time");
        }
    }

    db_history.close();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta http-equiv="Pragma" content="no-cache">
<title>�f����</title>
<link href="/common/contents.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="/common/bbs/datacheck.js"></script>

</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%

	String group_id       = "";
	String query          = "";
	String input_id;
	String dbfile;
	String group_slash;

//�@���X�܌f���̏ꍇ�A�O���[�vID�̓ǂݍ���
if (bbsgroupflg.compareTo("1") == 0)
	{
		DbAccess  dbhotel =  new DbAccess();
 		query = "SELECT * FROM hotel WHERE hotel_id=?";
		// SQL�N�G���[�̎��s
		ResultSet resulthotel = dbhotel.execQuery(query,hotelid);
		if (resulthotel != null)
		{
			if( resulthotel.next() != false )
			{
				group_id  = resulthotel.getString("group_id");
			}
			else
			{
				group_id  = "";
			}
		}
		dbhotel.close();

		input_id = group_id;
		dbfile = "bbs_multi";
		group_slash = "/";
	}
else
	{
		input_id = hotelid;
		dbfile = "bbs";
		group_slash = "";
	}
		int 	 key;
		int 	 pageno;

		String pagestr = ReplaceString.getParameter(request,"page");
		if( pagestr != null )
		{
				pageno = Integer.valueOf(pagestr).intValue();
		}
		else
		{
				pageno = 0;
		}

		// �폜�L�[�̐���
		Random rnd = new Random();

		while( true )
		{
				key = rnd.nextInt(9999);
				if( key >= 1000 )
				{
						break;
				}
		}
%>

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>


			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td align="left" valign="middle" class="titlebar"><img src="/common/image/title_bbs.gif" width="140" height="14" alt="�f����"></td>
				</tr>
				<tr>
					<td><img src="/common/image/spacer.gif" width="500" height="20"></td>
				</tr>

<%
if (viewflg.compareTo("9") != 0 )
{
		try
		{
				int 		msgid;
				int 		delflag;
				int 		count;
				boolean 	nulldata;

				String		data;
				String		subject = "";
				String		input_date = "";
				String		name = "";
				String		mail = "";
				String		body = "";
				String		topic_hotel = "";
				String		topic_hotelid = "";

				// MySQL�ւ̐ڑ����m������
				DbAccess db =  new DbAccess();
				query = "SELECT * FROM " + dbfile + " WHERE hotel_id=?";
				query = query + " AND del_flag=1";
				query = query + " GROUP BY msg_id DESC";
				query = query + " ORDER BY input_date DESC";
				query = query + " LIMIT " + pageno * 10 + "," + 10;

				count = 0;

				// SQL�N�G���[�̎��s
				ResultSet result = db.execQuery(query,input_id);
				while( result.next() != false )
				{
						count++;
				// �X���b�h�̃��R�[�h�擾
				query = "SELECT * FROM " + dbfile + " WHERE hotel_id=?";
				query = query + " AND del_flag<=1";
				query = query + " AND sub_id=0";
				query = query + " AND msg_id=" + result.getInt("msg_id");
				// �X���b�h�\���p
				DbAccess dbthread =  new DbAccess();
				ResultSet resultthread = dbthread.execQuery(query,input_id);
				if( resultthread.next() != false )
				{

		   	         if( count > 10 )
    		 	   	    {
    			    	        // ���y�[�W����
     	      		 	    break;
     			   	   	 }
						msgid = resultthread.getInt("msg_id");
						delflag = resultthread.getInt("del_flag");

						data = resultthread.getString("subject");
						nulldata = resultthread.wasNull();
						if( nulldata == false )
						{
								subject = data;
						}

						data = resultthread.getString("input_date");
						nulldata = resultthread.wasNull();
						if( nulldata == false )
						{
								input_date = data;
						}

						data = resultthread.getString("name");
						nulldata = resultthread.wasNull();
						if( nulldata == false )
						{
								name = data;
						}

						data = resultthread.getString("mail");
						nulldata = resultthread.wasNull();
						if( nulldata == false )
						{
								mail = data;
						}
						else
						{
								mail = "";
						}

						data = resultthread.getString("body");
						nulldata = resultthread.wasNull();
						if( nulldata == false )
						{
								body = data;
						}
if (bbsgroupflg.compareTo("1") == 0)
	{
						DbAccess	db_topic =  new DbAccess();
						ResultSet	db_hotelname;
						// �g�s�b�N�z�e�����擾
						data = resultthread.getString("topic_hotel_id");
						nulldata = resultthread.wasNull();
						if( nulldata == false )
						{
							topic_hotelid = data;
						}
						query = "SELECT * FROM hotel WHERE hotel_id=?";
						db_hotelname = db_topic.execQuery(query,topic_hotelid);
						if( db_hotelname != null )
						{
							if( db_hotelname.next() != false )
							{
								topic_hotel = db_hotelname.getString("name");
							}
						}
						db_topic.close();
	}
%>


				<tr>
					<td class="<%=css_bbs_honbun%>">

						<!-- �������܂ꂽ���e�\������e�[�u�� -->

						<!-- �����o�[���\�� -->
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="<%=css_bbs_main_name%>" nowrap>
<%
		if( mail.compareTo("") == 0 )
		{
%>
								<%= name %> ����&nbsp;&nbsp;<%= group_slash %><%= topic_hotel %>
<%
		}
		else

%>
								<a href="mailto:<%= mail %>" class="<%=css_bbs_link1%>"><%= name %> ����</a>&nbsp;&nbsp;<%= group_slash %><%= topic_hotel %>
<%
		}
%>
								</td>
								<td align="right" class="<%=css_bbs_main_date%>"><%= input_date %></td>
							</tr>
						</table>

						<!-- ��������{���\�� -->
						<table width="100%" cellpadding="1" cellspacing="1" class="<%=css_bbs_main%>">
							<tr>
							  <td class="<%=css_bbs_main_title%>" width="80%">��<%= subject %></td>
<%
if (viewflg.compareTo("0") == 0 || custominfo.CustomId.compareTo("") != 0)
	{
%>

								<form action="/<%= selecthotel %>/ownerbbsok.jsp?msg_id=<%= msgid %>" method=POST>
								<td class="<%=css_bbs_main_delete%>" width="10%" align="center" nowrap><%if( delflag != 0 ) {%><input name="submit2" type=submit value=���� class="inputbtnGrey"><%}else{%>�ʏ�\����<%}%></td>
								</form>
								<form action="/<%= selecthotel %>/ownerbbsng.jsp?msg_id=<%= msgid %>" method=POST>
								  <td class="<%=css_bbs_main_delete%>" width="10%" align="center" nowrap><%if( delflag != 0 ) {%><input name="submit3" type=submit value=�s���� class="inputbtnGrey"><%}%></td>
								</form>
<%
}
%>
							</tr>
							<tr>
								<td class="<%=css_bbs_main_body%>" colspan="3">
								<%= body %>
								<br><br><br>
								</td>
							</tr>


						</table>
<%
	 // �ԐM�`�F�b�N
				int 			subid;
				boolean 	res_nulldata;
				String		res_query;
				String		res_data;
				String		res_subject = "";
				String		res_input_date = "";
				String		res_name = "";
				String		res_mail = "";
				String		res_body = "";

				DbAccess db_res =  new DbAccess();

				res_query = "SELECT * FROM " + dbfile + " WHERE hotel_id=?";
				res_query = res_query + " AND msg_id=" + result.getInt("msg_id");
				res_query = res_query + " AND sub_id <> 0";
				res_query = res_query + " AND del_flag=1";
				res_query = res_query + " ORDER BY sub_id";

				// SQL�N�G���[�̎��s
				ResultSet res_result = db_res.execQuery(res_query,input_id);
				while( res_result.next() != false )
				{
						subid = res_result.getInt("sub_id");
						delflag = res_result.getInt("del_flag");

						res_data = res_result.getString("subject");
						res_nulldata = res_result.wasNull();
						if( res_nulldata == false )

						{
								res_subject = res_data;
						}

						res_data = res_result.getString("input_date");
						res_nulldata = res_result.wasNull();
						if( res_nulldata == false )
						{
								res_input_date = res_data;
						}

						res_data = res_result.getString("name");
						res_nulldata = res_result.wasNull();
						if( res_nulldata == false )
						{
								res_name = res_data;
						}

						res_data = res_result.getString("mail");
						res_nulldata = res_result.wasNull();
						if( res_nulldata == false )
						{
								res_mail = res_data;
						}
						else
						{
								res_mail = "";
						}

						res_data = res_result.getString("body");
						res_nulldata = res_result.wasNull();
						if( res_nulldata == false )
						{
								res_body = res_data;
						}
%>

									<!-- �ԐM�p�e�[�u�� -->
									<div align="center">

									<!-- �����o�[���\�� -->
									<table width="95%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td class="<%=css_bbs_res_name%>" nowrap>
<%
		if( res_mail.compareTo("") == 0 )
		{
%>
												�ԁF<%= res_name %> ����
<%
		}
		else
		{
%>
												<A href="mailto:<%= res_mail %>" class="<%=css_bbs_link1%>">�ԁF<%= res_name %> ����</A>
<%
		}
%>
											</td>
											<td align="right" class="<%=css_bbs_res_date%>"><%= res_input_date %> </td>
										</tr>
									</table>

									<!-- ��������@�ԐM�̖{���\�� -->
									<table width="95%" cellpadding="1" cellspacing="1" class="<%=css_bbs_res%>">
										<tr>
											<td width="85%" class="<%=css_bbs_res_title%>"><img src="/common/image/resyajirushi.gif" width="10" height="11" align="absmiddle"><img src="/common/image/spacer.gif" width="4" height="12" align="absmiddle">��<%= res_subject %> subid��<%= subid %>����</td>
<%
if (viewflg.compareTo("0") == 0 || custominfo.CustomId.compareTo("") != 0)
	{
%>

											<form action="/<%= selecthotel %>/ownerbbsok.jsp?msg_id=<%= msgid %>&sub_id=<%= subid %>" method=POST>
									<td class="<%=css_bbs_main_delete%>" width="10%" align="center" nowrap><%= delflag %><input name="submit2" type=submit value=���� class="inputbtnGrey"></td>
									</form>
									<form action="/<%= selecthotel %>/ownerbbsng.jsp?msg_id=<%= msgid %>&sub_id=<%= subid %>" method=POST>
									  <td class="<%=css_bbs_main_delete%>" width="10%" align="center" nowrap><input name="submit3" type=submit value=�s���� class="inputbtnGrey"></td>
									</form>
<%
	}
%>
										</tr>

										<tr>
											<td colspan="3" class="<%=css_bbs_res_body%>"><%= res_body %><br><br></td>
										</tr>
									</table>
									<!-- �ԐM�p�e�[�u���@�����܂� -->
									<br>
									</div>
<%
				} // End of while( res_result.next() != false )

				db_res.close();
%>
					</td>
				</tr>

				<tr>
					<td><img src="/common/image/spacer.gif" width="500" height="16"></td>
				</tr>
<%
				}
				dbthread.close();
					} // End of while( result.next() != false )
%>

				<tr>
					<td>

					<!-- �����y�[�W�ɓn�蓊�e���������ꍇ�A���y�[�W�A�O�y�[�W�ւ̃����N �e�[�u�� -->

						<table class="<%=css_bbs_honbun%>" width="100%">
							<tr>
<%
				if( pageno != 0 )
				{
%>
								<td class="<%=css_bbs_honbun%>" align="center">
									<a href="/<%= selecthotel %>/bbs.jsp?page=<%= pageno - 1 %>" class="<%=css_bbs_link1%>">�O�y�[�W��</a>
								</td>
<%
	}
%>
								<td align="center" class="<%=css_bbs_honbun%>">
									<a href="/<%= selecthotel %>/bbs.jsp" class="<%=css_bbs_link1%>">�ŐV�̓��e��</a>
								</td>
<%
	if( count >= 10 )
	{
%>
								<td align="center" class="<%=css_bbs_honbun%>">
									<a href="/<%= selecthotel %>/bbs.jsp?page=<%= pageno + 1 %>" class="<%=css_bbs_link1%>">���y�[�W��</a>
								</td>
<%
	}
%>
							</tr>
						</table>
<%
			db.close();
		}
		catch( Exception e )
		{
%>
			<%= e.toString() %>
<%
		}
}
%>


					</td>
				</tr>



				<tr>
					<td><img src="/common/image/spacer.gif" width="500" height="50"></td>
				</tr>
			</table>

		</td>
	</tr>

	<tr valign="bottom">
		<td align="center" valign="middle" class="<%=css_bbs_honbun%>">
				Copy right &copy; almex inc, All Rights Reserved<br>

		</td>
	</tr>
</table>

</body>
</html>
