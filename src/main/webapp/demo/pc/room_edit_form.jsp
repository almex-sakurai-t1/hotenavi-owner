<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />


<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<%
    }
%>

<%
    // �z�e��ID�擾
	String hotelid = request.getParameter("HotelId");

if  (hotelid.compareTo("all") == 0)
{
	hotelid = request.getParameter("HotelId");
}
    NumberFormat  nf3;
    nf3    = new DecimalFormat("000");

    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    boolean EditFlag      = false;
    int       last_update = nowdate; 

    String data_type = "100";
    String id = request.getParameter("Id");
    String header_msg = "";
    String query;
    boolean mobile = false;
    DbAccess  db = new DbAccess();

    int       disp_idx;
    int       disp_flg;
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();
    String    title       = "";
    String    msg1        = "";
    String    msg1_title  = ""; //�����ݸ
    String    msg2        = ""; //�g�їp�摜�����N
    String    msg3        = ""; //PC�p�摜�����N
    String    msg4        = ""; //PC�p�T���l�C��
    String    msg5        = ""; //PC�p���l
    boolean   msg2_exist  = false;
    boolean   msg3_exist  = false;
    boolean   msg4_exist  = false;

    disp_idx = 0;
    disp_flg = 1;
    String room_no = "";

    if( id.compareTo("0") == 0 )
    {
        header_msg = header_msg + " �V�K�쐬";
    }
    else
    {
        query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
        query = query + " AND data_type=100";
        query = query + " AND id=" + id;

        // SQL�N�G���[�̎��s
        ResultSet result = db.execQuery(query);
        if( result.next() != false )
        {
            header_msg = header_msg + " �X�V";

            disp_idx = result.getInt("disp_idx");
            disp_flg = result.getInt("disp_flg");
            start = result.getDate("start_date");
            end = result.getDate("end_date");
            title       = result.getString("title");
            msg1        = result.getString("msg1");
            msg1_title  = result.getString("msg1_title");
            msg2        = result.getString("msg2");
            msg3        = result.getString("msg3");
            msg4        = result.getString("msg4");
            msg5        = result.getString("msg5");
            last_update = result.getInt("last_update");
            if (disp_idx < 100)
            {
                room_no    = nf3.format(disp_idx);
            }
            else
            {
                room_no    = Integer.toString(disp_idx);
            }

        }
        else
        {
            header_msg = header_msg + " �V�K�쐬";
        }

        db.close();
    }
    if   (last_update != nowdate) EditFlag = true;

    if (msg2.compareTo("") == 0 || msg2.compareTo(" ") == 0)
    {
        msg2 = "<table width='400' border='0' cellspacing='0' cellpadding='0'>\r\n";
        msg2 = msg2+"<tr>\r\n";
        msg2 = msg2+"<td height='12'><img src='image/spacer.gif' width='100' height='12'></td>\r\n";
        msg2 = msg2+"</tr>\r\n";
        msg2 = msg2+"<tr>\r\n";
        msg2 = msg2+"<td><img src='image/r%ROOMNO%.jpg' width='400' height='300' border='0' alt='%ROOMTITLE%'></td>\r\n";
        msg2 = msg2+"</tr>\r\n";
        msg2 = msg2+"<tr>\r\n";
        msg2 = msg2+"<td><img src='image/spacer.gif' width='100' height='12'></td>\r\n";
        msg2 = msg2+"</tr>\r\n";
        msg2 = msg2+"</table>\r\n";
    }
    else
    {
        msg2_exist = true;
    }
    if (msg3.compareTo("") == 0 || msg3.compareTo(" ") == 0)
    {
        msg3 = "room%ROOMNO%s.jsp?HotelId=%HOTELID%�������摜\r\n";
        msg3 = msg3+"room%ROOMNO%l.jsp?HotelId=%HOTELID%�������摜�i��j\r\n";
    }
    else
    {
        msg3_exist = true;
    }
    if (msg4.compareTo("") == 0 || msg4.compareTo(" ") == 0)
    {
        msg4 = "<a href='room%ROOMNO%.jsp' class='link1'><img src='image/thumb%ROOMNO%.jpg' width='100' height='75' class='thumbroom' alt='%ROOMTITLE%'></a><br>\r\n";
        msg4 = msg4 + "<a href='room%ROOMNO%.jsp' class='link1'>%ROOMNAME%</a><br>\r\n";
    }
    else
    {
        msg4_exist = true;
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
	obj.col_start_yy.value = <%= start.getYear()+1900 %>;
	years = <%= start.getYear()+1900 %>;
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
	obj.col_start_mm.value = <%= start.getMonth()+1 %>;
	months = <%= start.getMonth()+1 %>;
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
	var lastday_s = monthday_s(years,months);

    if (isNaN(days))
	{
	obj.col_start_dd.value = <%= start.getDate() %>;
	days = <%= start.getDate() %>;
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



    if (isNaN(yeare))
	{
	obj.col_end_yy.value = <%= end.getYear()+1900 %>;
	yeare = <%= end.getYear()+1900 %>;
	}
	else
	{
	obj.col_end_yy.value = yeare;
	}
    if (yeare < 2000)
	{
	    obj.col_end_yy.value = 2000;
	}
    if (yeare > 2999)
	{
	    obj.col_end_yy.value = 2999;
	}

    if (isNaN(monthe))
	{
	obj.col_end_mm.value = <%= end.getMonth()+1 %>;
	monthe = <%= end.getMonth()+1 %>;
	}
	else
	{
	obj.col_end_mm.value = monthe;
	}
    if (monthe < 1)
	{
	    obj.col_end_mm.value = 1;
	}
    if (monthe > 12)
	{
	    obj.col_end_mm.value = 12;
	}

//���͂���Ă���N�����A���̌��̍ŏI���t���Z�o
	var lastday_e = monthday_e(yeare,monthe);

    if (isNaN(daye))
	{
	obj.col_end_dd.value = <%= end.getDate() %>;
	daye = <%= end.getDate() %>;
	}
	else
	{
	obj.col_end_dd.value = daye;
	}
    if (daye < 1)
	{
	    obj.col_end_dd.value = 1;
	}
		
//���͂���Ă��錎�ɂ���āA�ŏI���t��ύX�B���肦�Ȃ����t�̏ꍇ�ɂ́A�ŏI���t�ɋ����I�ɕϊ��B
	if (lastday_e < daye) {
		obj.col_end_dd.value = lastday_e;
	}

}


function monthday_s(yearx,monthx){
	var lastday_s = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
		lastday_s[1] = 29;
	}
	return lastday_s[monthx - 1];
}

function monthday_e(yearx,monthx){
	var lastday_e = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
		lastday_e[1] = 29;
	}
	return lastday_e[monthx - 1];
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
-->
</script>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>PC�ŃC�x���g���ҏW</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="room.js"></script>
<script type="text/javascript" src="tohankaku.js"></script>

</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"> �ҁ@�W</font></td>
              <td width="15" height="20"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ��������\ -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="/common/pc/image/spacer.gif" width="400" height="12">
              </td>
            </tr>
            <tr>
              <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><font color="#CC0000"><strong>�����̃y�[�W��ҏW���I������A�u�ۑ��v�{�^����K�������Ă�������</strong></font></td>
                    <td align="right">&nbsp;</td>
                    <td align="right">&nbsp;</td>
                  </tr>
                  <tr>
<%
    if( mobile != false )
    {
%>
                    <td><font color="#CC0000"><strong>���g�т̋@��ɂ��F��ύX����Ɛ���ɕ\������Ȃ��ꍇ������܂�</strong></font></td>
<%
    }
    else
    {
%>
                    <td align="right">&nbsp;</td>
<%
    }
%>
                    <td align="right">&nbsp;</td>
                    <td align="right">&nbsp;</td>
                  </tr>
                  <tr>
                    <td><strong><%= header_msg %></strong></td>
                    <td align="right">&nbsp;</td>
                    <td width="20" align="right"><img src="/common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                </table>
              </div>
              </td>
            </tr>
          </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
                <td>
                  <table width="100%" border="0" cellspacing="0" cellpadding="2">

                  <form name=form1 method=post>
                  <!-- ���s�[�g�̈悱������ -->
                    <tr>
                      <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
		                  
                          <td width="30%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="�ۑ�" onClick="if (validation_range()){MM_openInput('input', document.form1.hotelid.value, <%= disp_idx %>, <%= id %>)}">
						  <input name="BackUp" type="checkbox" value="1">Backup���c��
						  </td>
						  <td width="70%" align="center" valign="middle" bgcolor="#969EAD">�R�s�[��ID:
						  <input name="hotelid" type=text value="<%= hotelid %>" >
						<input name="regsubmit" type=button value="�R�s�[" onClick="MM_openInput('input', document.form1.hotelid.value, document.form1.col_disp_idx.value, 0)">
							</td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left">
                    <td>
                      <div align="left">
                        �����ԍ��F
                        <input name="old_disp_idx" type="hidden" value="<%= disp_idx %>" >
                        <input name="col_disp_idx" type="text" size="3" value="<%= disp_idx %>" onchange="document.form1.col_title.value=document.form1.col_title.value.replace(document.form1.old_disp_idx.value,document.form1.col_disp_idx.value);document.form1.old_disp_idx.value=document.form1.col_disp_idx.value;">

<%
    if( disp_flg == 1 )
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1" checked>�\������<br>
<%
    }
    else
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1">�\������<br>
<%
    }
%>

<br>
                      </div>
                    </td>
                  </tr>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong><font color="#FFFFFF">&nbsp;�\������</font></strong>
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start.getYear()+1900 %>" onchange="setDayRange(this);">
                      �N
                      <input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start.getMonth()+1 %>"  onchange="setDayRange(this);">
                      ��
                      <input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start.getDate() %>"  onchange="setDayRange(this);">
                      ���`
                      <input name="col_end_yy" type="text" size="4" maxlength="4" value="<%if(id.compareTo("0") == 0){%>2999<%}else{%><%= end.getYear()+1900 %><%}%>"  onchange="setDayRange(this);">
                      �N
                      <input name="col_end_mm" type="text" size="2" maxlength="2" value="<%= end.getMonth()+1 %>"  onchange="setDayRange(this);">
                      ��
                      <input name="col_end_dd" type="text" size="2" maxlength="2" value="<%= end.getDate() %>"  onchange="setDayRange(this);">
                      ��
                    </td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong>&nbsp;<font color="#FFFFFF">
                        �������́F<input name="col_title" type="text" size="30" value="<%= title %>" onchange="<%if(EditFlag){%>if (document.form1.old_title.value != '') if ( document.form1.old_title.value != document.form1.col_title.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                  <input name="old_title" type="hidden" value="<%= title %>" >
                        ���������N�F<input name="col_msg1_title" type="text" size="2" maxlength="2" style="ime-mode:disable;text-align:right;" value="<%=msg1_title %>"   onchange="<%if(EditFlag){%>if (document.form1.old_msg1_title.value != '') if ( document.form1.old_msg1_title.value != document.form1.col_msg1_title.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                    <input name="old_msg1_title" type="hidden" value="<%=msg1_title %>">
                        </font></strong>
                    </td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table>
                      <tr>
                        <td rowspan="2">�ݔ�<br>
                           <textarea class=size name=col_msg1 rows=20 cols=30  onchange="<%if(EditFlag){%>if (document.form1.old_msg1.value != '' || document.form1.old_msg1.value != ' ') if ( document.form1.old_msg1.value != document.form1.col_msg1.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= msg1 %></textarea>
                           <input name=old_msg1 type="hidden" value="<%= msg1 %>"><br>
						   �������l<font size="2">�i���͖��˕��������N���l��\���j</font><br>
                           <textarea class=size name=col_msg5 rows=5 cols=30  onchange="<%if(EditFlag){%>if (document.form1.old_msg5.value != '' || document.form1.old_msg5.value != ' ') if ( document.form1.old_msg5.value != document.form1.col_msg5.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= msg5 %></textarea>
                           <input name=old_msg5 type="hidden" value="<%= msg5 %>">
                        </td>
                        <td>�����摜�iPC)<%if(!msg2_exist){%><font color=red>�@*���ݖ��o�^�ł�</font><%}%><br>
                           <textarea class=size name=col_msg2 rows=14 cols=60 style="float:left;clear:both;"><%= msg2 %></textarea>
                           <small>
                             %ROOMTITLE%<br>�������̂�\��<%if(title.compareTo("")!=0){%>�i<%=title%>�j<%}else{%>�iXX�����j<%}%><br><br>
                             %ROOMNO%<br>�����ԍ���3���ȏ�ŕ\��<%if(title.compareTo("")!=0){%>�i<%=room_no%>�j<%}else{%>�i0XX�j<%}%><br><br>
                             %ROOMNAME%<br>�������̂���u�����v���Ȃ��ĕ\��<%if(title.compareTo("")!=0){%>�i<%=title.replace("����","")%>�j<%}else{%>�iXX�j<%}%><br>
                           </small><br><br>
						</td>
					  <tr>
						<td>						
							�����摜�i�T���l�C��)<%if(!msg4_exist){%><font color=red>�@*���ݖ��o�^�ł�</font><%}%><br>
                           <textarea class=size name=col_msg4 rows=5 cols=60 ><%= msg4 %></textarea><br>
							�����摜�i�g��)<%if(!msg3_exist){%><font color=red>�@*���ݖ��o�^�ł�</font><%}%><br>
                           <textarea class=size name=col_msg3 rows=4 cols=60 ><%= msg3 %></textarea><br>
                        </td>
                      </tr>
                      </table>
                    </td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="�ۑ�" onClick="if (validation_range()){MM_openInput('input', '<%= hotelid %>', <%= disp_idx %>, <%= id %>)}"></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </form>
              </td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
                  <form action="room_edit_delete.jsp?HotelId=<%= hotelid %>&Id=<%= id %>" method="POST">
                  <input name="submit_del" type=submit value="�폜" >
                  </form>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td align="center">
                  <form action="room_list.jsp?HotelId=<%= hotelid %>" method="POST">
                  <input name="submit_ret" type=submit value="�߂�" >
                  </form>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="new/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="/common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- �����܂� -->
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18"><img src="/common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; imedia
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
