<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
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
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    boolean EditFlag      = false;
    int       last_update = nowdate; 

    String id = request.getParameter("Id");
    String header_msg = "";
    String query;
    boolean mobile = false;

    boolean system_exist  = false;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    try
    {
        query = "SELECT * FROM price WHERE hotelid='" + hotelid + "'";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            system_exist = true; //�����V�X�e���̓��͂���
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }



    int       disp_flg;
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();
    String    price_name       = "";
    String    roomrank_name       = "";
    String    system        = "";
    String    detail_explain= ""; //PC�Łu�q���ڍׁv�ӏ�
    String    memo          = ""; //���l
    String    price_memo    = ""; //PC�ŗ����V�X�e�����̔��l
    String    layout        = ""; //ROOMINFO ���C�A�E�g
    boolean   NoDetail      = false;

    disp_flg = 1;

    if( id.compareTo("0") == 0 )
    {
        header_msg = header_msg + " �V�K�쐬";
        if(!system_exist)
        {
           price_name = "�T�[�r�X���e�ڍ�";
           roomrank_name = "���������N�ʗ����ꗗ�\";
        }
    }
    else
    {
        query = "SELECT * FROM price WHERE hotelid='" + hotelid + "'";
        query = query + " AND id=" + id;
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            header_msg = header_msg + " �X�V";

            disp_flg = result.getInt("disp_flg");
            start = result.getDate("start_date");
            end = result.getDate("end_date");
            price_name       = result.getString("price_name");
            roomrank_name    = result.getString("roomrank_name");
            system        = result.getString("system");
            memo          = result.getString("memo");
            detail_explain= result.getString("detail_explain");
            price_memo    = result.getString("price_memo");
            layout        = result.getString("layout");
            last_update = result.getInt("last_update");
        }
        else
        {
            header_msg = header_msg + " �V�K�쐬";
            if(!system_exist)
            {
               price_name = "�T�[�r�X���e�ڍ�";
               roomrank_name = "���������N�ʗ����ꗗ�\";
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    DBConnection.releaseResources(connection);

    if(layout.indexOf("NoDetail") > 0)
    {
        NoDetail=true;
        layout = layout.replace("NoDetail","");
    }

    if (last_update != nowdate) EditFlag = true;
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
function ExplainSet(){
    var explain_temp = "<tr>\r\n";
        explain_temp = explain_temp + "<td class='hyouyou_bgcolor honbun' align='center'>\r\n";
        explain_temp = explain_temp + "�q���ڍ�\r\n";
        explain_temp = explain_temp + "</td>\r\n";
        explain_temp = explain_temp + "<td class='heijitsu'>\r\n";
        explain_temp = explain_temp + "</td>\r\n";
        explain_temp = explain_temp + "<td class='honbun'>\r\n";
        explain_temp = explain_temp + "&nbsp;<strong>���̕\�̂������ԍ���I�ԂƋq���ڍׂ������ɂȂ�܂��B</strong>\r\n";
        explain_temp = explain_temp + "</td>\r\n";
        explain_temp = explain_temp + "</tr>";
    document.form1.col_detail_explain.value = explain_temp;
}
-->
</script>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>PC�ŃC�x���g���ҏW</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="price2.js"></script>
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
		                  
                          <td width="30%" align="center" valign="middle" bgcolor="#969EAD">
							  <input name="regsubmit" type=button value="�ۑ�" onClick="if (validation_range()){MM_openInput('input', '<%=hotelid%>',<%= id %>)}">
							  <input name="BackUp" type="checkbox" value="1">Backup���c��
						  </td>
						  <td width="70%" align="center" valign="middle" bgcolor="#969EAD">
<%
    if( id.compareTo("0") != 0 )
    {
%>
<% if (!ownerinfo.DbLoginUser.equals("i-enter")){%>
						  �R�s�[��ID:
						  <input name="hotelid" type=text value="<%= hotelid %>" >
						<input name="regsubmit" type=button value="�R�s�[" onClick="MM_openInput('input', document.form1.hotelid.value, 0)">
<%
	}
    }
%>
							</td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left">
                    <td>
                      <div align="left">
                        �����V�X�e��
<%
    if( disp_flg == 1 )
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1" checked>�\������
<%
    }
    else
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1">�\������
<%
    }
%>
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
                    <td height="24" bgcolor="#666666"><strong>&nbsp;<font color="#FFFFFF">���́F<input name="col_price_name" type="text" size="30" value="<%= price_name %>"  onchange="<%if(EditFlag){%>if (document.form1.old_price_name.value != '') if ( document.form1.old_price_name.value != document.form1.col_price_name.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                  <input name="old_price_name" type="hidden" value="<%= price_name %>" >
								  </font></strong>
									<strong>&nbsp;<font color="#FFFFFF">�����N�ʗ������́F<input name="col_roomrank_name" type="text" size="30" value="<%= roomrank_name %>"  onchange="<%if(EditFlag){%>if (document.form1.old_roomrank_name.value != '') if ( document.form1.old_roomrank_name.value != document.form1.col_roomrank_name.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                  <input name="old_roomrank_name" type="hidden" value="<%= roomrank_name %>" >
								  </font></strong>
					</td>
                  </tr>
                  <tr align="left">
                    <td>
					</td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table>
                      <tr>
                        <td rowspan="4">
						�����V�X�e��<br>
                           <textarea class=size name=col_system rows=30 cols=40  onchange="<%if(EditFlag){%>if (document.form1.old_system.value != '' || document.form1.old_system.value != ' ') if ( document.form1.old_system.value != document.form1.col_system.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= system %></textarea>
                           <input name=old_system type="hidden" value="<%= system %>">
                        </td>
                        <td  valign="top">�iPC�j�u�q���ڍׁv�ӏ�<input type="button" value="Default" onclick="ExplainSet();"><br>
                           <textarea class=size name=col_detail_explain rows=5 cols=50  onchange="<%if(EditFlag){%>if (document.form1.old_detail_explain.value != '' || document.form1.old_detail_explain.value != ' ') if ( document.form1.old_detail_explain.value != document.form1.col_detail_explain.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= detail_explain %></textarea>
                           <input name=old_detail_explain type="hidden" value="<%= detail_explain %>">
                        </td>
                      </tr>
                      <tr>
                        <td  valign="top">�����V�X�e�����̔��l�iPC�̂݁j<br>
                           <textarea class=size name=col_price_memo rows=5 cols=50  onchange="<%if(EditFlag){%>if (document.form1.old_price_memo.value != '' || document.form1.old_price_memo.value != ' ') if ( document.form1.old_price_memo.value != document.form1.col_price_memo.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= price_memo %></textarea>
                           <input name=old_price_memo type="hidden" value="<%= price_memo %>">
                        </td>
                      </tr>
                      <tr >
                        <td> 
							���l<br>
                           <textarea class=size name=col_memo rows=5 cols=50  onchange="<%if(EditFlag){%>if (document.form1.old_memo.value != '' || document.form1.old_memo.value != ' ') if ( document.form1.old_memo.value != document.form1.col_memo.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= memo %></textarea>
                           <input name=old_memo type="hidden" value="<%= memo %>">
                        </td>
                      </tr>
                      <tr >
                        <td> 
							PC�ŗ�����񃌃C�A�E�g�i<input name="col_no_detail" type="checkbox" value="NoDetail" <%if(NoDetail){%>checked<%}%>>PC�ŗ������ו\�����Ȃ��j<br>
                           <textarea class=size name=col_layout rows=7 cols=50  onchange="<%if(EditFlag){%>if (document.form1.old_layout.value != '' || document.form1.old_layout.value != ' ') if ( document.form1.old_layout.value != document.form1.col_layout.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= layout %></textarea>
                           <input name=old_layout type="hidden" value="<%= layout %>">
                        </td>
                      </tr>
                      </table>
                    </td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="�ۑ�" onClick="if (validation_range()){MM_openInput('input', '<%= hotelid %>', <%= id %>)}"></td>
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
                  <form action="price_edit_delete2.jsp?HotelId=<%= hotelid %>&Id=<%= id %>" method="POST">
                  <input name="submit_del" type=submit value="�폜" >
                  </form>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td align="center">
                  <form action="price_list2.jsp?HotelId=<%= hotelid %>" method="POST">
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
