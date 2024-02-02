<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.HotelElement" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/menu_ini.jsp" %>
<%
    // �z�e��ID�擾
    String data_type = request.getParameter("DataType");
    boolean mobile = false;
    if (Integer.parseInt(data_type) % 2 == 0) mobile = true;//�g��
    String id = request.getParameter("Id");
    if(id != null && !CheckString.numCheck(id))
    {
        response.sendError(400);
        return;
    }
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String hotelid = (String)session.getAttribute("SelectHotel");

    String requestUri = request.getRequestURI();
    boolean DebugMode = false;
    if (requestUri.indexOf("_debug_/demo/") != -1)
    {
       DebugMode = true;
    }
    if( hotelid == null )
    {
%>
        <jsp:forward page="error/error.html" />
<%
    }
    NumberFormat nf2         = new DecimalFormat("00");
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query="";

    //���ݓ����̎擾
    DateEdit  de = new DateEdit();
    int    now_date        = Integer.parseInt(de.getDate(2));
    int    now_time        = Integer.parseInt(de.getTime(1));

    connection  = DBConnection.getConnection();
    int    startDate = HotelElement.getStartDate(hotelid);

    // HP�ҏW���j���[���̂̏�������
    try
    {
        query = "SELECT * FROM menu WHERE hotelid='" + hotelid + "'";
        query = query + " AND data_type= ? ";
        query = query + " AND start_date <=" + now_date;
        query = query + " AND end_date >=" + now_date;
        query = query + " AND disp_flg =1";
        query = query + " AND hpedit_id>0";
        prestate    = connection.prepareStatement(query);
        prestate.setInt(1, (((Integer.parseInt(data_type)+1) % 2)*2+1));
        result      = prestate.executeQuery();
        while( result.next() != false )
        {
            Title[0][result.getInt("hpedit_id")-1]=result.getString("title");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>HP�ҏW���e�@COPY</title>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
</head>

<script type="text/javascript">
<!--
function disp_find(id) {
	if(document.getElementById(id).style.display == "none"){
		document.getElementById(id).style.display = "table-row";
	} else {
		document.getElementById(id).style.display = "none";
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
					<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<strong><%= Title[(Integer.parseInt(data_type)+1) % 2][MenuDataType[(Integer.parseInt(data_type)+1)/2]]%></strong>
						<%if(mobile){%>(�g�єŁj<%}else{%>�iPC�Łj<%}%>�@�ȉ��̓��e���R�s�[���܂�
					</td>
				</tr>
                <tr>
                    <td colspan="3"  class="size10"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                 </tr>

<%
    Connection        connection_user  = null;
    PreparedStatement prestate_user    = null;
    ResultSet         result_user      = null;
    String        query_user;
    java.sql.Date start;
    java.sql.Date end;
    int event_id;
    int start_yy=0;
    int start_mm=0;
    int start_dd=0;
    int end_yy=0;
    int end_mm=0;
    int end_dd=0;
    int start_date;
    int end_date;
    int start_time=0;
    int end_time=0;

    int last_update;
    int last_uptime;
    int i;
    int count = 0;
    String user_name = "";
    String msg_data;
    String msg_title;
    String msg_title_color;
    int disp_idx = 0;
    int disp_flg = 0;
    int member_only = 0;
    int smart_flg = 0;

    query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
    query = query + " AND id=?";
    query = query + " AND data_type=?";
    prestate    = connection.prepareStatement(query);
    prestate.setInt(1, Integer.parseInt(id));
    prestate.setInt(2, Integer.parseInt(data_type));
    result      = prestate.executeQuery();

    // SQL�N�G���[�̎��s
    if( result.next() != false )
    {
            count++;
            event_id =  result.getInt("id");
            // ���Ԕ���̏���
            start = result.getDate("start_date");
            start_yy = start.getYear()+1900;
            start_mm = start.getMonth()+1;
            start_dd = start.getDate();
            start_date = start_yy*10000 + start_mm*100 + start_dd;
            end = result.getDate("end_date");
            end_yy = end.getYear() +1900;
            end_mm = end.getMonth()+1;
            end_dd = end.getDate();
            end_date = end_yy*10000 + end_mm*100 + end_dd;
            start_time =  result.getInt("start_time");
            end_time   =  result.getInt("end_time");
            disp_idx = result.getInt("disp_idx");
            member_only = result.getInt("member_only");
            smart_flg = result.getInt("smart_flg");
            disp_flg = result.getInt("disp_flg");

            // ���[�U�ꗗ�E�Z�L�����e�B���擾
            connection_user  = DBConnection.getConnection();
            query_user = "SELECT * FROM owner_user WHERE hotelid=?";
            query_user = query_user + " AND userid=?";
            prestate_user = connection_user.prepareStatement(query_user);
            prestate_user.setString(1,result.getString("upd_hotelid") );
            prestate_user.setInt(2,result.getInt("upd_userid"));
            result_user   = prestate_user.executeQuery();
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
            DBConnection.releaseResources(result_user,prestate_user,connection_user);
%>

				<tr>
					<td  colspan="3">
						<!-- ############### �T�u�^�C�g���o�[ ############### -->				
<% 
            if (Integer.parseInt(data_type) % 2 == 0) 
            { //�g�є�
%>
						<table cellspacing="0" bgcolor="#ffffff"  border="1" cellpadding="0" bordercolor="#666666" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor" onClick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)" ><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3" onClick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor" onClick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="400" onClick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)">
									<div><font color="<%= result.getString("title_color") %>"><%= result.getString("title") %></div>
								</td>
<%
            }
            else
            { //PC��
%>
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="400">
									<div class="subtitle_text"><font color="<%= result.getString("title_color") %>"><%= result.getString("title") %></div>
								</td>
<%
            }
%>
								<td align=left title="�쐬:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
�ҏW:<%= user_name %>&nbsp;<% if (user_name.compareTo("�s��") != 0) { %><%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %><% } %>"  class="subtitle_text"><font color="<%= result.getString("title_color") %>">
�\������:<%= result.getInt("disp_idx") %>
<%
            if( result.getInt("member_only") == 1 )
            {
%>
								<b>�����o�[�̂�</b>&nbsp;
<%
            }
            else if( result.getInt("member_only") == 2 )
            {
%>
								<b>�r�W�^�[�̂�</b>&nbsp;
<%
            }
%>
			<%if(result.getInt("smart_flg")==1){%><b>�y�X�}�z�̂݁z</b><%}%><%if(result.getInt("smart_flg")==2){%><b>�yPC�̂݁z</b><%}%>
								<br>����:<%= start_date/10000 %>�N<%= start_date/100%100 %>��<%= start_date%100 %>��<%if(start_time!=0){%><%=nf2.format(start_time/10000)%>:<%=nf2.format(start_time/100%100)%><%}%>�`<%= end_date/10000 %>�N<%= end_date/100%100 %>��<%= end_date%100 %>��<%if(end_time!=0&& end_time!=235959){%><%=nf2.format(end_time/10000)%>:<%=nf2.format(end_time/100%100)%><%}%>&nbsp;
<%
            if( now_date < start_date || now_date == start_date && now_time < start_time || now_date > end_date || now_date == end_date && now_time > end_time && end_time != 0 || result.getInt("disp_flg") != 1)
            {
%>
								<br><b>�\�����Ă��܂���B
<%
                if( now_date < start_date || now_date == start_date && now_time < start_time || now_date > end_date || now_date == end_date && now_time > end_time && end_time != 0)
                {
%>
								
								&nbsp;�\�����ԃ`�F�b�N�I
<%
                }
                if(result.getInt("disp_flg") == 2)
                {
%>
								
								&nbsp;�i�����ۑ��j
<%
                }
%>
								</b>
<%
            }
%>
								</td>
							</tr>
						</table>
						<!-- ############### �T�u�^�C�g���o�[ �����܂� ############### -->
					</td>
				</tr>

<% 
            if (Integer.parseInt(data_type) % 2 == 0)
            {
%>
				<tr id="honbun_<%= hotelid %>_<%= event_id %>" style="display:none;">
					<td>
						<table width="166" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" bordercolor="#666666">
						<tr>
							<td width="160" nowrap>
<%
                for( i = 1 ; i <= 8 ; i++ )
                {
                    msg_data    = "msg" + Integer.toString(i);
                    msg_title   = "msg" + Integer.toString(i) + "_title";
                    msg_title_color = "msg" + Integer.toString(i) + "_title_color";
                    if( result.getString(msg_title).length() != 0 || result.getString(msg_data).length() > 1 )
                    {
%>
								<font color="<%= result.getString(msg_title_color) %>"><%= result.getString(msg_title) %></font><br>
								<font color="#000000"><%=  result.getString(msg_data) %></font><br>
<%
                     }
                 }
%>
				</td></tr></table></td></tr>
<%
            }
            else
            {
                for( i = 1 ; i <= 8 ; i++ )
                {
                    msg_data    = "msg" + Integer.toString(i);
                    msg_title = "msg" + Integer.toString(i) + "_title";
                    msg_title_color = "msg" + Integer.toString(i) + "_title_color";
                    if( result.getString(msg_title).length() != 0 || result.getString(msg_data).length() > 1 )
                    {
%>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
<%
                        if( result.getString(msg_title).length() != 0 )
                        {
%>
				<tr>
					<td align="left" valign="top"  colspan="3">
						<!-- ############### �{���^�C�g���o�[ ############### -->
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="honbun_margin">
							<tr>
								<td width="4" class="honbuntitlebar_left"><img src="../../common/pc/image/spacer.gif" width="4" height="16"></td>
								<td class="honbuntatitlebar_text"><img src="../../common/pc/image/spacer.gif" width="6" height="16" border="0" align="absmiddle"><font color="<%= result.getString(msg_title_color) %>"><%= result.getString(msg_title) %></td>
							</tr>
						</table>
						<!-- ############### �{���^�C�g���o�[ �����܂� ############### -->
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
<%
                        }
%>
				<tr>
					<td align="left" valign="top" class="honbun"  colspan="3">

<%
                        String firstString  = result.getString(msg_data);
                        String taisyoString = "src=\"image/";
                        String henkanString = "src=\"http://www.hotenavi.com/" + hotelid +  "/image/";
                        String afterString = "";
                        //�Ώە�����𔻒f��CRLF�Ȃǂ̉��s�R�[�h���l�����ăC���v�������g����|�C���^���𐧌䂷��B
                        int plusPoint = taisyoString.length();
                        int startPoint = 0;
                        int endPoint   = firstString.indexOf(taisyoString, startPoint);
                        //������ɑΏە����񂪂Ȃ��ꍇ�A���̂܂܂̕������߂�
                        //������ɑΏە����񂪂���ԁA�ȉ��̏������J��Ԃ�
                        while (endPoint != -1)
                        {
                            //�����񂩂�Ώە���������Ɍ����s���ϊ�������ɒu������B
                            afterString = afterString + firstString .substring(startPoint, endPoint) + henkanString;
                            startPoint  = endPoint + plusPoint;
                            endPoint    = firstString .indexOf(taisyoString, startPoint);
                        }
                        afterString = afterString + firstString .substring(startPoint);
      
%>

						<div class="honbun_margin">
						<%= afterString %>
						</div>
					</td>
				</tr>
				
				<tr>
					<td  colspan="3" ><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>

<%
                    }
                }
%>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="10"></td>
				</tr>
<%
            }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);
%>
<%
    if (Integer.parseInt(data_type) % 2 == 0 && count > 0)
    { 
%>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="10"></td>
				</tr>
				<tr valign="middle" class="subtitlebar_basecolor">
					<td colspan="3">
						<div class="subtitle_text">�N���b�N����Ɩ��ׂ��\������܂��B</div>
					</td>
				</tr>
<%
    }
%>

<script type="text/javascript">
<!--
function setDayRange(obj){
	obj = obj.form;
	var years = parseInt(obj.col_start_yy.value,10);
	var months = parseInt(obj.col_start_mm.value,10);
	var days = parseInt(obj.col_start_dd.value,10);
	var yeare = parseInt(obj.col_end_yy.value,10);
	var monthe = parseInt(obj.col_end_mm.value,10);
	var daye = parseInt(obj.col_end_dd.value,10);

    if (isNaN(years))
	{
	obj.col_start_yy.value = <%= start_yy %>;
	years = <%= start_yy %>;
	}
	else
	{
	obj.col_start_yy.value = years;
	}
    if (years < 2000)
	{
	    obj.col_start_yy.value = 2000;
	}
    if (years > 2999)
	{
	    obj.col_start_yy.value = 2999;
	}

    if (isNaN(months))
	{
	obj.col_start_mm.value = <%= start_mm %>;
	months = <%= start_mm %>;
	}
	else
	{
	obj.col_start_mm.value = months;
	}
    if (months < 1)
	{
	    obj.col_start_mm.value = 1;
	}
    if (months > 12)
	{
	    obj.col_start_mm.value = 12;
	}

//���͂���Ă���N�����A���̌��̍ŏI���t���Z�o
	var lastday_s = monthday(years,months);

    if (isNaN(days))
	{
	obj.col_start_dd.value = <%= start_dd %>;
	days = <%= start_dd %>;
	}
	else
	{
	obj.col_start_dd.value = days;
	}
    if (days < 1)
	{
	    obj.col_start_dd.value = 1;
	}
		
//���͂���Ă��錎�ɂ���āA�ŏI���t��ύX�B���肦�Ȃ����t�̏ꍇ�ɂ́A�ŏI���t�ɋ����I�ɕϊ��B
	if (lastday_s < days) {
		obj.col_start_dd.value = lastday_s;
	}
}
function monthday(yearx,monthx){
	var lastday = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
		lastday[1] = 29;
	}
	return lastday[monthx - 1];
}

function validation_range(){
	var input_years = parseInt(form1.col_start_yy.value,10);
	var input_months = parseInt(form1.col_start_mm.value,10);
	var input_days = parseInt(form1.col_start_dd.value,10);
		
	var input_yeare = parseInt(form1.col_end_yy.value,10);
	var input_monthe = parseInt(form1.col_end_mm.value,10);
	var input_daye = parseInt(form1.col_end_dd.value,10);


	if  (input_yeare < input_years)
		     {
			 alert("���Ԏw��i�J�n���t���I�����t�j�𐳂������͂��Ă�������");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
	else
	  if  (input_yeare == input_years)
		{ 
		if (input_monthe < input_months)
		     {
			 alert("���Ԏw��i�J�n���t���I�����t�j�𐳂������͂��Ă�������");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
		else if  (input_monthe == input_months && input_daye < input_days)
		     {
			 alert("���Ԏw��i�J�n���t���I�����t�j�𐳂������͂��Ă�������");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
		else {
		     return true;
			}
		}
	  else
	 	{
		     return true;
		}
		
}
function setTimeRange(obj,start_input,end_input){
	var start_hour   = Math.floor(start_input / 10000);
	var start_minute = Math.floor(start_input / 100) % 100;
	var end_hour     = Math.floor(end_input / 10000);
	var end_minute   = Math.floor(end_input / 100) % 100;

	obj = obj.form;
	var hours  = parseInt(obj.col_start_hour.value,10);
	var minutes = parseInt(obj.col_start_minute.value,10);
	var houre  = parseInt(obj.col_end_hour.value,10);
	var minutee = parseInt(obj.col_end_minute.value,10);

    if (isNaN(hours))
	{
		obj.col_start_hour.value = start_hour;
		hours =  start_hour;
	}
	else
	{
		obj.col_start_hour.value = hours;
	}
    if (hours > 23)
	{
	    obj.col_start_hour.value = 23;
	}

    if (isNaN(minutes))
	{
		obj.col_start_minute.value = start_minute;
		minutes = start_minute;
	}
	else
	{
		obj.col_start_minute.value = minutes;
	}
    if (minutes > 59)
	{
	    obj.col_start_minute.value = 59;
	}

    if (isNaN(houre))
	{
		obj.col_end_hour.value = end_hour;
		houre = end_hour;
	}
	else
	{
		obj.col_end_hour.value = houre;
	}

    if (houre > 23)
	{
	    obj.col_end_hour.value = 23;
	}

    if (isNaN(minutee))
	{
		obj.col_end_minute.value = end_minute;
		minutee = end_minute;
	}
	else
	{
		obj.col_end_minute.value = minutee;
	}
    if (minutee > 59)
	{
	    obj.col_end_minute.value = 59;
	}
}

-->
</script>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="5"></td>
				</tr>
				<tr>
					<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<strong>�R�s�[��̎w��</strong>
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="5"></td>
				</tr>
				<form name=form1 action="event_copy_input.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= id %>" method="POST">
				<tr valign="middle" class="subtitlebar_basecolor">
					<td colspan="3">
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td  valign="top">
									<div class="subtitle_text">
<%
    int        imedia_user        = 0;
    int        level              = 0;
    int        store_count        = 0;
    int        type_count         = 0;
     // imedia_user �̃`�F�b�N
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    try
    {
        if(imedia_user == 1 && level == 3 && !ownerinfo.DbLoginUser.equals("i-enter"))
        {
            if (hotelid.compareTo("demo") == 0)
            {
                    store_count++;
%>
									<input type="checkbox" name="CopyStore<%=store_count%>" value="demo" checked>DEMO
									�@*���ݕ\��
									<br>
<%
            }
            else
            {
                String  group_id = "";
                query = "SELECT * FROM search_hotel_find WHERE findstr8 ='" + hotelid + "'";
                prestate    = connection.prepareStatement(query);
                result      = prestate.executeQuery();
                if  (result != null)
                {
                    if( result.next() != false )
                    {
                        group_id = result.getString("findstr7");
                    }
                }
                DBConnection.releaseResources(result);
                DBConnection.releaseResources(prestate);
                if (group_id.compareTo("") == 0)
                {
                    query = "SELECT * FROM hotel,search_hotel_find WHERE search_hotel_find.findstr8='" + hotelid + "'";
                }
                else
                {
                    query = "SELECT * FROM hotel,search_hotel_find WHERE search_hotel_find.findstr7='" + group_id + "'";
                }
                query = query + " AND  hotel.hotel_id = search_hotel_find.findstr8";
                query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
                prestate    = connection.prepareStatement(query);
                result      = prestate.executeQuery();
                if  (result != null)
                {
                    while( result.next() != false )
                    {
                        store_count++;
%>
									<input type="checkbox" name="CopyStore<%=store_count%>" value="<%= result.getString("hotel.hotel_id") %>" <%if( hotelid.compareTo(result.getString("hotel.hotel_id")) == 0 ){%> checked <%}%>><%= result.getString("hotel.name") %>
									<%if( hotelid.compareTo(result.getString("hotel.hotel_id")) == 0 ){%>�@*���ݕ\��<%}%>
									<br>
<%
                    }
                }
            }
            store_count++;
%>
									<input type="text" size="10" maxlength="10" name="CopyStore<%=store_count%>" value="" style="ime-mode:disabled">
<%
        }
        else
        {
            query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + loginHotelId + "'";
            query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
            query = query + " AND hotel.plan <= 4";
            query = query + " AND hotel.plan >= 1";
            query = query + " AND hotel.plan != 2";
            query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
            prestate    = connection.prepareStatement(query);
            result      = prestate.executeQuery();
            if  (result != null)
            {
                while( result.next() != false )
                {
                    store_count++;
%>
									<input type="checkbox" name="CopyStore<%=store_count%>" value="<%= result.getString("owner_user_hotel.accept_hotelid") %>" <%if( hotelid.compareTo(result.getString("owner_user_hotel.accept_hotelid")) == 0 ){%> checked <%}%>><%= result.getString("hotel.name") %>
									<%if( hotelid.compareTo(result.getString("owner_user_hotel.accept_hotelid")) == 0 ){%>�@*���ݕ\��<%}%>
									<br>
<%
                }
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
%>
									<input name="StoreCount" type="hidden" value="<%=store_count%>">
								</div>
								</td>
								<td  valign="top">
								<div  class="subtitle_text" >
<%
    type_count++;
%>
		<input type="checkbox" name="CopyType<%=type_count%>" value="<%=data_type%>"><%=Title[0][MenuDataType[(Integer.parseInt(data_type)+1)/2]]%>
<%
    if (Integer.parseInt(data_type)%2 == 0)
    {
%>
									�i�g�сj
<%
    }
    else
    {
%>
									�iPC�j
<%
    }
%>
		*���ݕ\��<br/><br/>
<%
    type_count++;
    if (Integer.parseInt(data_type)%2 == 0)
    {
%>
									<input type="checkbox" name="CopyType<%=type_count%>" value="<%=Integer.parseInt(data_type)-1%>"><%=Title[0][MenuDataType[(Integer.parseInt(data_type)+1)/2]]%>
									�iPC�j
<%
    }
    else
    {
        if (startDate > now_date)
        {

%>
									<input type="checkbox" name="CopyType<%=type_count%>" value="<%=Integer.parseInt(data_type)+1%>"><%=Title[0][MenuDataType[(Integer.parseInt(data_type)+1)/2]]%>
									�i�g�сj
<%
        }
    }
%>
		<br/>
<%
    boolean  ExistFlag     = false;
    boolean  MenuExistFlag     = false;

    // edit_event_info �L���`�F�b�N
    for(i = 0; i <  100 ; i++)
    {
        if (MenuNo[0][i] == 0) break;
        ExistFlag     = false;
        MenuExistFlag     = false;
        try
        {
            query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
            query = query + " AND (data_type=" + MenuNo[0][i];
            query = query + " OR data_type=" + (MenuNo[0][i]+1) + ")";
            prestate    = connection.prepareStatement(query);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                ExistFlag = true;
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
        if (startDate <= now_date)
        {
            MenuExistFlag = false;
            try
            {
                query = "SELECT * FROM menu_config WHERE hotelid='" + hotelid + "'";
                query = query + " AND event_data_type=" + MenuNo[0][i];
                prestate    = connection.prepareStatement(query);
                result      = prestate.executeQuery();
                if( result.next() != false )
                {
                    MenuExistFlag = true;
                }
            }
            catch( Exception e )
            {
                Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
            }
            finally
            {
                DBConnection.releaseResources(result);
                DBConnection.releaseResources(prestate);
            }
        }
        else
        {
            MenuExistFlag = true;
        }
        if ((imedia_user == 1 && MenuExistFlag) || ExistFlag)
        {
            if((MenuNo[0][i]+1)/2 != (Integer.parseInt(data_type)+1)/2)
            {
               type_count++;
%>
									<span <%if (type_count > 60){%>style="display:none"<%}%>  id="CopyType<%=type_count%>"><input type="checkbox" name="CopyType<%=type_count%>" value="<%=MenuNo[0][i]%>"><%=Title[0][i]%>
									�iPC�j<br/></span>
<%
               type_count++;
               if (startDate > now_date)
               {
%>
									<span <%if (type_count > 60){%>style="display:none"<%}%>  id="CopyType<%=type_count%>"><input type="checkbox" name="CopyType<%=type_count%>" value="<%=MenuNo[0][i]+1%>"><%=Title[0][i]%>
									�i�g�сj<br/></span>
<%
               }
               if (type_count == 60)
               {
%>				<span id="dispCheckbox"><input type="button" value="�����ƕ\������" onclick="disp_checkbox();"><br></span>
<%
               }
            }
        }
    }
    DBConnection.releaseResources(connection);
%>
<script>
function disp_checkbox(){
   document.getElementById("dispCheckbox").style.display="none";
   for (i = 60;i <= <%=type_count%>;i++)
   {
       document.getElementById("CopyType"+i).style.display="";
   }
}
</script>
									<input name="TypeCount" type="hidden" value="<%=type_count%>">
									<br/>
									<input name="col_member_only" type="radio" value=0 <% if (member_only == 0) { %> checked <% } %>>����
									<input name="col_member_only" type="radio" value=1 <% if (member_only == 1) { %> checked <% } %>>�����o�[�̂�
									<input name="col_member_only" type="radio" value=2 <% if (member_only == 2) { %> checked <% } %>>�r�W�^�[�̂�
									<br/><br/>
									�y�u���E�U�z�iPC�łɃR�s�[����Ƃ��̂ݗL���j
									<input name="col_smart_flg" type="radio" value=0 <% if (smart_flg == 0) { %> checked <% } %>>����
									<input name="col_smart_flg" type="radio" value=1 <% if (smart_flg == 1) { %> checked <% } %>>�X�}�z�̂�
									<input name="col_smart_flg" type="radio" value=2 <% if (smart_flg == 2) { %> checked <% } %>>PC�̂�
									<br/>
									�y�\�����z
									<input name="col_disp_idx" type="text" size="3" <% if (!DebugMode){%>maxlength="4"<%}%> value="<%= disp_idx %>" style="text-align:right;"><br/>
									�y�\�����ԁz
									<input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start_yy %>" onChange="setDayRange(this);" style="text-align:right;">�N<input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start_mm %>"  onchange="setDayRange(this);" style="text-align:right;">��<input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start_dd %>"  onchange="setDayRange(this);" style="text-align:right;">��
									<input name="col_start_hour"   type="text" size="2" maxlength="2" value="<%= start_time/10000 %>" style="text-align:right;" onChange="setTimeRange(this,'<%=start_time%>','<%=end_time%>');">:<input name="col_start_minute" type="text" size="2" maxlength="2" value="<%= start_time/100%100 %>" style="text-align:right;" onChange="setTimeRange(this,'<%=start_time%>','<%=end_time%>');">
									�`
									<input name="col_end_yy" type="text" size="4" maxlength="4"  value="<%= end_yy %>"  onchange="setDayRange(this);" style="text-align:right;">�N<input name="col_end_mm" type="text" size="2" maxlength="2" value="<%= end_mm %>"  onchange="setDayRange(this);" style="text-align:right;">��<input name="col_end_dd" type="text" size="2" maxlength="2" value="<%= end_dd %>"  onchange="setDayRange(this);" style="text-align:right;">��
									<input name="col_end_hour"   type="text" size="2" maxlength="2" value="<%= end_time/10000 %>" style="text-align:right;" onChange="setTimeRange(this,'<%=start_time%>','<%=end_time%>');">:<input name="col_end_minute" type="text" size="2" maxlength="2" value="<%= end_time/100%100 %>" style="text-align:right;" onChange="setTimeRange(this,'<%=start_time%>','<%=end_time%>');">
									<br/><br/>
									<small>*�\�����ԁE�J�n���t�E�I�����t�̂��ׂĂ��������̂�����ꍇ�ɂ̓R�s�[����܂���B</small></br>
									<small>*PC�ł���g�єłɃR�s�[�����ꍇ�A�p���J�^�J�i�̕����͎����I�ɔ��p�ɕϊ�����܂��B</small></br>
									<small>*PC�łɂ����ĉ摜�f�[�^��e�[�u��������ꍇ�́A�g�єłɃR�s�[���Ȃ��ł��������B</small></br>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td height="30" align="center" valign="middle" bgcolor="#969EAD">
						<input name="col_disp_flg" type="hidden" value="1">
                        <input name="regsubmit1" type=button value="�R�s�[���������ۑ��i��f�ځj" onClick="if (validation_range()){document.form1.col_disp_flg.value=2;document.form1.submit();}">
                        <input name="regsubmit2" type=button value="�R�s�[��ۑ��i�f�ځj" onClick="if (validation_range()){document.form1.col_disp_flg.value=1;document.form1.submit();}">
					</td>
				</form>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<form action="event_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>" method="POST">
				<tr>
					<td align="center">
						<input name="submit_ret" type=submit value="�߂�" >
					</td>
				</form>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr valign="bottom">
		<td>
			<!-- copyright �e�[�u�� -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" valign="middle">
						<div class="copyright">
							Copyright &copy; almex inc, All Rights Reserved
						</div>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>
			</table>
		
		</td>
	</tr>
</table>

</body>
</html>


